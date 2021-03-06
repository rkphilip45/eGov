package org.egov.services.deduction;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.deduction.model.EgRemittance;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.deduction.model.EgRemittanceGldtl;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.deduction.AutoRemittanceBean;
import org.egov.model.payment.Paymentheader;
import org.egov.model.recoveries.Recovery;
import org.egov.model.recoveries.RemittanceSchedulePayment;
import org.egov.model.recoveries.RemittanceSchedulerLog;
import org.egov.pims.commons.DrawingOfficer;
import org.egov.pims.commons.Position;
import org.egov.services.payment.PaymentService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.utils.FinancialConstants;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * @author mani
 *
 */
public class ScheduledRemittanceService {
	private static final String INCOME_TAX = "INCOME TAX";

	/**
	 * 1. Select the Remittances pending for payment for a given coa,fund,department
	 * 2. keep the count of vouhcers selected and total payment amount
	 * 3. Create a payment voucher and send to workflow
	 * 4. Update the Remittance tables as
	 * 5. There will not be partial payment but Payment voucher cancellation will be there 
	 */
	private final static Logger LOGGER=LoggerFactory.getLogger(ScheduledRemittanceService.class);
	
	private final static int fundIndex=0;
	private final static int bankAccountIdIndex=1;
	private final static  int detailTypeIndex=2;
	private final static  int detailKeyIndex=3;
	// id,code into fundMap .
	private Map<Integer,String> fundMap;
	private Map<Integer, String> deptMap;
	private Map<Integer, Integer> deptDOMap;
	//fundcode-bankaccountid
	private Map<String, Integer> receiptBankAccountMap;  
	
	private FinancialYearDAO financialYearDAO;
	private RecoveryService recoveryService;
	private PersistenceService<EgRemittanceGldtl, Integer> egRemittancegldtlService;
	private RemittancePersistenceService remittancePersistenceService;
	private PaymentService paymentService;
	private PersistenceService<RemittanceSchedulerLog,Integer> remittanceSchedulerLogService;
	private SimpleWorkflowService<Paymentheader> paymentWorkflowService;
	private SimpleDateFormat sdf=new  SimpleDateFormat("dd-MMM-yyyy");
	private List<String> receiptFundCodes;
	private EisCommonService eisCommonService; 
	private HashMap<String, Integer> GJVBankAccountMap;

	private ArrayList<String> GJVFundCodes;

	private Date startDate;
	
	private Date today=new Date();
	
	private String glcode;
	
	private String jobName;
	
	private Date lastRunDate;
	private StringBuffer errorMessage=new StringBuffer(1024);
	private Recovery recovery;
	private Map<String, List<AutoRemittanceBean>> voucherGroupMap=new HashMap<String, List<AutoRemittanceBean>>();
	
	//Map of remittance amount grouped for a voucher combination and subledger
	Map<String,Double> detailKeyGroupeMap=new HashMap<String,Double>();

	private List<AutoRemittanceBean> recoveries;
	String remitted ;

	private boolean isControlCode=true;

	private Long schedularLogId;

	private boolean successForAutoRemittance=true;

	private User user;

	private Position nextOwner;
	
		

	
	/**
	 *  Our jboss Trnasaction manager does not support nested transactions 
	 *  https://community.jboss.org/thread/206684 so all have to be managed by flush,clear
	 *  
	 *  this  api  called the schedulars by coa and iniitates	
	 *  
	 *  any issues if trigger is not getting fired check QRTZ_TRIGGERS  table.
	 *  if the job type is manual on any exception  it is thrown back to 
	 * @return 
	 */
	public boolean searchRecovery(String recoveryGlcode,String recoveryjobName,Long recoverySchedularLogId,Integer deptId,Date recoverylastRunDate)
	{
		
 		
		glcode=recoveryGlcode;
		jobName=recoveryjobName;
		lastRunDate=recoverylastRunDate;
		schedularLogId=recoverySchedularLogId;
		
		if(LOGGER.isDebugEnabled()) LOGGER.debug( "Starting job :{} with glcode {} on {} "+jobName,glcode,new Date().toString());   
		try {
			//Common data like dept,fund,dept_do mapping populated to map 
			loadCommonData();
			validate(glcode);
			Date today=new Date();
			recovery = recoveryService.find("from Recovery where chartofaccounts.glcode="+glcode);
			if(recovery==null)
			{
				LOGGER.error("glcode is not mapped to tds :"+glcode +"\n");
				throw new EGOVRuntimeException("glcode is not mapped to tds  : "+glcode);
			}
			remitted=recovery.getRemitted();
			
			
			List coads = remittancePersistenceService.getPersistenceService().findAllBy("from CChartOfAccountDetail where glcodeId=?",recovery.getChartofaccounts());
			if(coads==null || coads.size()==0)
			{
				isControlCode = false;
			}

			 
			for(Integer dept : deptMap.keySet())
			{
				//if deptId is not null then it is from manual screen invocation for a department
				
				if(deptId!=null)
				{
					if(!dept.equals(deptId))
					{
						continue;
					}
				}
				if(LOGGER.isDebugEnabled())
					{
					LOGGER.debug("**********************************************");
					LOGGER.debug("Starting for Department :"+deptMap.get(dept));
					}
				//if drawing officer not found or he does not have tan number payment will not be generated
				
				DrawingOfficer drawingOfficer = validateDrawingOfficer(errorMessage, dept);
				if(drawingOfficer==null)
					continue;
				//initialize for each department so that i wont contain previous department data
				detailKeyGroupeMap=new HashMap<String,Double>();
				voucherGroupMap=new HashMap<String, List<AutoRemittanceBean>>();
				if(isControlCode==false)
				{
					recoveries = searchNonControlCodeRecoveryByCOA(dept);
					if(recoveries.isEmpty())
					{
						errorMessage.append("  No Recoveries found for "+deptMap.get(dept)+"\n");
						continue;
					}
					createRemittanceForNonControlCodeRecovery(dept,drawingOfficer);	
					
					
				}else
				{
				
					recoveries = searchRecoveryByCOA(dept);
					if(recoveries.isEmpty())
					{
						errorMessage.append("  No Recoveries found for "+deptMap.get(dept)+"\n");
						continue;
					}
				
			
				
				
				
				// combination for which different voucher should be created like fund or fund-function when
				//function is mandatory
				String voucherCombination=null; 
				// combination for which group the detailkey and find total amount
				String	detailKeyCombination=null;
				
				for(AutoRemittanceBean bean:recoveries)
				{	
					
				//All receipt will have 0 as bankaccountid and s it is targetted to have only municipal fund  
					if(bean.getBankAccountId()==0)
					{
				
						bean.setBankAccountId(receiptBankAccountMap.get(fundMap.get(bean.getFundId())));
					}
					//All GJV will have -1 as bankaccountid 
					else if(bean.getBankAccountId()==-1)
					{
				
						bean.setBankAccountId(GJVBankAccountMap.get(fundMap.get(bean.getFundId())));
					}
					
					voucherCombination=bean.getFundId()+"-"+bean.getBankAccountId();	
					detailKeyCombination=voucherCombination+"-"+bean.getDetailtypeId()+"-"+bean.getDetailkeyId();
					
				
					if(LOGGER.isDebugEnabled())
						{
						LOGGER.debug(bean.toString());
						LOGGER.debug("detailKeyCombination:"+detailKeyCombination);
						}
					if(voucherGroupMap.get(voucherCombination)==null){
						voucherGroupMap.put(voucherCombination, new ArrayList<AutoRemittanceBean>());
						voucherGroupMap.get(voucherCombination).add(bean);
					}else{
						voucherGroupMap.get(voucherCombination).add(bean);
					}
					if(detailKeyGroupeMap.get(detailKeyCombination)==null){
						detailKeyGroupeMap.put(detailKeyCombination, bean.getPendingAmount());
					}else{
						detailKeyGroupeMap.put(detailKeyCombination,detailKeyGroupeMap.get(detailKeyCombination)+bean.getPendingAmount());
					}
				}
				
				/*
				 * Commenting as of now pan validation 
				 * //validate for pan number only if income tax
				String name = recovery.getChartofaccounts().getName();
				
				if(name.toUpperCase().contains(INCOME_TAX))
				{
					validateTaxMandatoryFields();
				}*/
				
				for(String voucher:voucherGroupMap.keySet())
				{
					try {
				
						if(LOGGER.isDebugEnabled()) 		LOGGER.debug(" Starting for VoucherCombination :"+voucher);
						//create pre approved voucher,add to paymentheader, Miscbilldetail send to workflow
						// Here there is no chance of voucherheader coming as null as create voucher throws validation exception on any issues
						Bankaccount ba=(Bankaccount)remittancePersistenceService.getPersistenceService().
								find(" from  Bankaccount where id="+Integer.parseInt(voucher.split("-")[bankAccountIdIndex])+"");
										
						
						if(ba==null)
						{
						 LOGGER.error("Bank Glcode for fundId "+fundMap.get(Integer.parseInt(voucher.split("-")[fundIndex]))+" ,recoverId:"+recovery.getType()+" not found");
						 errorMessage.append("Bank Glcode for fundId "+fundMap.get(Integer.parseInt(voucher.split("-")[fundIndex]))+" ,recoverId:"+recovery.getType()+" not found \n");
						 continue;
						}
						CVoucherHeader voucherHeader = createPayment( dept, voucher,drawingOfficer,ba);
						if(LOGGER.isDebugEnabled()) 		LOGGER.debug("VoucherCreated :"+voucherHeader.getVoucherNumber());
						//create remittance,remittancedetail
						List<Integer> glIds = createRemittance(voucher,	voucherHeader);
					
						// update generalledger with remittance date. This field will help in limiting no of rows to search for payment
						// This field needs to be set to null when remittance payment voucher is cancelled.
						// This field should not be set when partial payment is done.
						updateRemittancedateInLeddger(glIds);
						
						updateScheduleLogDetail(voucherHeader,schedularLogId); //updates payment voucher ids into log table
						
						if(LOGGER.isDebugEnabled()) LOGGER.debug(" Remittance Created SuccessFully for "+voucher );
					HibernateUtil.getCurrentSession().flush();
					HibernateUtil.getCurrentSession().clear();
					
					} catch (ValidationException e) {
				
						
						errorMessage.append(dept+":"+e.getErrors().toString()+"\n" );
						successForAutoRemittance=false;
						
						
						//updateScheduleLog(errorMessage.toString(),jobName,glcode,false);
						
					}
					
					 catch (Exception e) {
				
						
						errorMessage.append(dept+":"+e.getMessage()+"\n" );
						successForAutoRemittance=false;
						
						
						//updateScheduleLog(errorMessage.toString(),jobName,glcode,false);
						
					}
				}
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("Done for Department :"+deptMap.get(dept));
					LOGGER.debug("**********************************************");
					
				}
			}
		}
		} 
		catch (ValidationException ve) {
			successForAutoRemittance=false;
			errorMessage.append(ve.getErrors().toString());
		
		}
		catch (EGOVRuntimeException e) {
			successForAutoRemittance=false;
			errorMessage.append(e.getMessage()+"\n" );
		
		}
		
		 catch (Exception e) {
			 successForAutoRemittance=false;
			 	errorMessage.append(e.getMessage()+"\n" );
			 
			}
		finally
		{
			updateScheduleLog(errorMessage.toString(),jobName,glcode,true,schedularLogId);
		 	
		}
		
		return successForAutoRemittance;
		
	}

/**
 * 
 * @param detailTypeMapForPanValidation
 */
	private void validateTaxMandatoryFields() 
	{
		Map<String,Set<Long>> detailTypeMapForPanValidation =new HashMap<String, Set<Long>>();
		
		
			for(String keyGroup:detailKeyGroupeMap.keySet())
			{
				String[] split = keyGroup.split("-");
				if(detailTypeMapForPanValidation.get(split[detailTypeIndex])==null)
				{
					detailTypeMapForPanValidation.put(split[detailTypeIndex], new HashSet<Long>());
					detailTypeMapForPanValidation.get(split[detailTypeIndex]).add(Long.parseLong(split[detailKeyIndex]));	
				}else
				{
					detailTypeMapForPanValidation.get(split[detailTypeIndex]).add(Long.parseLong(split[detailKeyIndex]));
				}


			}
		
		
		
		
		
		for(String s:detailTypeMapForPanValidation.keySet())
		{
			try
    		{
        	
        	Accountdetailtype detailType = (Accountdetailtype)remittancePersistenceService.getPersistenceService()
        			.find("from Accountdetailtype where id=? order by name",Integer.valueOf(s));
    		String table=detailType.getFullQualifiedName();
    		Class<?> service = Class.forName(table);
    		String simpleName = service.getSimpleName();
    		
    		simpleName = simpleName.substring(0,1).toLowerCase()+simpleName.substring(1)+"Service";

    		WebApplicationContext wac= WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
    		EntityTypeService entityService=(EntityTypeService)wac.getBean(simpleName);
             List<Long> entityIds=new ArrayList<Long>(detailTypeMapForPanValidation.get(s));
             List<EntityType> enities=new ArrayList<EntityType>();
             int size = entityIds.size();
           /*  if(size>999)
             {
            	 int fromIndex=0;
            	 int toIndex=999; 
    		    while (size%1000>1000)
    		    {
            	 enities.addAll((List<EntityType>)entityService.getEntitiesById(entityIds.subList(fromIndex, toIndex)));
            	 fromIndex+=1000;
               	 toIndex+=1000;
               	 size-=1000;
    		    }
    		    enities.addAll((List<EntityType>)entityService.getEntitiesById(entityIds.subList(toIndex+1, size)));
    		    
             }else
             {
            	 enities.addAll((List<EntityType>)entityService.getEntitiesById(entityIds));
             }*///This fix is for Phoenix Migration.
    		if(enities!=null && !enities.isEmpty())
    		{
    			
    			//can be used once the service is updated fro pan validation
    			/*StringBuffer failedNames=new StringBuffer(1024);
    			int i=0;
    			for(EntityType e:enities)
    			{
    				if(i==0)
    				failedNames.append(e.getName());
    				else
    				failedNames.append(",").append(e.getName());
    				i++;
    			}*/
    			StringBuffer failedNames=new StringBuffer(1024);
    			for(EntityType e:enities)
    			{
    				int i=0;
    				if(null==e.getPanno())
    				{
    					if(i==0)
    						failedNames.append(e.getName());
    					else
    						failedNames.append(",").append(e.getName());
    					i++;
    				}
    			}
    			//if failed names present then there are some entities which dont have panno
    			if(failedNames.length()>=1)
    			{
    			ValidationError validationError = new ValidationError("Pan number validation failed for following entities in "+simpleName,"");
    			ValidationError validationErrorNames = new ValidationError(failedNames.toString(),"");
    			List<ValidationError> errors=new ArrayList<ValidationError>();
    			errors.add(validationError);
    			errors.add(validationErrorNames);
    			throw new ValidationException(errors);
    			}
    		}
	 		}catch (ClassCastException e) {
    			LOGGER.error(e.getMessage());
    			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),e.getMessage())));
    		} catch (ClassNotFoundException e) {
				LOGGER.error(e.getMessage());
				throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),e.getMessage())));
			}
    		      
			
		}

		
	}


	private boolean createRemittanceForNonControlCodeRecovery(Integer dept,DrawingOfficer drawingOfficer) {
		
		
		
		// combination for which different voucher should be created like fund or fund-function when
		//function is mandatory
		String voucherCombination=null; 
		// combination for which group the detailkey and find total amount
	
		
		for(AutoRemittanceBean bean:recoveries)
		{	
		//All receipt will have 0 as bankaccountid and s it is targetted to have only municipal fund  
			if(bean.getBankAccountId()==0)
			{
				bean.setBankAccountId(receiptBankAccountMap.get(fundMap.get(bean.getFundId())));
			}
			//All GJV will have -1 as bankaccountid 
			else if(bean.getBankAccountId()==-1)
			{
				bean.setBankAccountId(GJVBankAccountMap.get(fundMap.get(bean.getFundId())));
			}
			
			voucherCombination=bean.getFundId()+"-"+bean.getBankAccountId();	
			if(LOGGER.isDebugEnabled())
				{
				LOGGER.debug(bean.toString());
				LOGGER.debug("voucherCombination:"+voucherCombination);
				}
			if(voucherGroupMap.get(voucherCombination)==null){
				voucherGroupMap.put(voucherCombination, new ArrayList<AutoRemittanceBean>());
				voucherGroupMap.get(voucherCombination).add(bean);
			}else{
				voucherGroupMap.get(voucherCombination).add(bean);
			}
		
		}
		
		for(String voucher:voucherGroupMap.keySet())
		{
			try {
		
				if(LOGGER.isDebugEnabled()) 		LOGGER.debug(" Starting for VoucherCombination :"+voucher);
				//create pre approved voucher,add to paymentheader, Miscbilldetail send to workflow
				// Here there is no chance of voucherheader coming as null as create voucher throws validation exception on any issues
				Bankaccount ba=(Bankaccount)remittancePersistenceService.getPersistenceService().
						find(" from  Bankaccount where id="+Integer.parseInt(voucher.split("-")[bankAccountIdIndex])+"");
				if(ba==null)
				{
				 LOGGER.error("Bank Glcode for fundId "+fundMap.get(Integer.parseInt(voucher.split("-")[fundIndex]))+" ,recoverId:"+recovery.getType()+" not found");
				 errorMessage.append("Bank Glcode for fundId "+fundMap.get(Integer.parseInt(voucher.split("-")[fundIndex]))+" ,recoverId:"+recovery.getType()+" not found \n");
				 continue;
				}
				CVoucherHeader voucherHeader = createPayment( dept, voucher,drawingOfficer,ba);
				if(LOGGER.isDebugEnabled()) 		LOGGER.debug("VoucherCreated :"+voucherHeader.getVoucherNumber());
				//create remittance,remittancedetail
				List<Integer> glIds = createRemittance( voucher,voucherHeader);
			
				// update generalledger with remittance date. This field will help in limiting no of rows to search for payment
				// This field needs to be set to null when remittance payment voucher is cancelled.
				// This field should not be set when partial payment is done.
				updateRemittancedateInLeddger(glIds);
				
				updateScheduleLogDetail(voucherHeader,schedularLogId); //updates payment voucher ids into log table
				
				if(LOGGER.isDebugEnabled()) LOGGER.debug(" Remittance Created SuccessFully for "+voucher );
			HibernateUtil.getCurrentSession().flush();
			HibernateUtil.getCurrentSession().clear();
			}catch (ValidationException e) {
					
					errorMessage.append(dept+":"+e.getErrors()+"\n" );
					//updateScheduleLog(errorMessage.toString(),jobName,glcode,false);
					
				}
			 catch (Exception e) {
		
				errorMessage.append(dept+":"+e.getMessage()+"\n" );
				//updateScheduleLog(errorMessage.toString(),jobName,glcode,false);
				
			}
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Done for Department :"+deptMap.get(dept));
			LOGGER.debug("**********************************************");
			
		}
		
	return true;	
		
	}


	
/**
 * 
 * @param dept
 * @param bankaccount
 * @return
 *  This query depends on back update of data remittancedate.
 *  If remittance date is null it will pick the amount else it will ignore 
 *  Voucher cancellation of remittance should reflect in setting remittancedate in gl to null
 *  This is taken care. Should be maintained as same.
 */

	private List<AutoRemittanceBean> getNonControleCodeReceiptRecoveries( Integer dept, int bankaccount) {
		
	StringBuffer qry=new StringBuffer(2048);
		 qry.append(" SELECT DISTINCT gl.id AS generalledgerId,  vh.fundid  AS fundId,  gl.debitAmount   " +
			 		"   AS gldtlAmount, "+bankaccount+" AS bankAccountId  "+ 
		" FROM VOUCHERHEADER vh ,  VOUCHERMIS mis,  GENERALLEDGER gl ,  VOUCHERHEADER payinslip,fund f,  " +
		" EGF_INSTRUMENTHEADER ih,EGF_INSTRUMENTOTHERDETAILS io , egcl_collectionvoucher cv,egcl_collectioninstrument ci, TDS recovery "+
		" WHERE  recovery.GLCODEID =gl.GLCODEID  AND vh.ID =gl.VOUCHERHEADERID"+
		" AND gl.remittanceDate    IS NULL AND mis.VOUCHERHEADERID   =vh.ID AND vh.STATUS =0 and vh.fundid=f.id "+
		" AND io.payinslipid =payinslip.id and io.instrumentheaderid=ih.id "+ 
		" and cv.voucherheaderid= vh.id and ci.collectionheaderid= cv.collectionheaderid and ci.instrumentmasterid= ih.id "+
		" and payinslip.status=0 AND ih.id_status NOT     IN  (" +
				" select id from egw_status where moduletype='Instrument' and  description in ('" +FinancialConstants.INSTRUMENT_CANCELLED_STATUS
				+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_STATUS
				+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS+"') )"+
		" AND recovery.ID      ="+recovery.getId()+" AND payinslip.voucherdate    >= :startdate  ");
		 if(lastRunDate!=null)
		 {
		 qry.append(" and  payinslip.voucherdate    <= :lastrundate");
		 }
		 
		 if(receiptFundCodes!=null && !receiptFundCodes.isEmpty())
			{
			 qry.append(" and  f.code in (:fundCodes) ");	 
			}
		 
		SQLQuery query =HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());
		query.addScalar("generalledgerId", IntegerType.INSTANCE)
		.addScalar("fundId", IntegerType.INSTANCE)
		.addScalar("gldtlAmount",DoubleType.INSTANCE)
		.addScalar("bankAccountId",IntegerType.INSTANCE);
		if(lastRunDate!=null)
		{
		query.setDate("lastrundate", new java.sql.Date(lastRunDate.getTime()));
		}
		
		if(startDate!=null)
		{
		query.setDate("startdate", new java.sql.Date(startDate.getTime()));
		}
		if(receiptFundCodes!=null && !receiptFundCodes.isEmpty())
		{
			query.setParameterList("fundCodes", receiptFundCodes);	
		}
		query.setResultTransformer(	Transformers.aliasToBean(AutoRemittanceBean.class));
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("ReceiptRecoveries query "+qry);
		return query.list();
	}   


	private DrawingOfficer validateDrawingOfficer(StringBuffer errorMessage, Integer dept) {
		
		if(deptDOMap.get(dept)==null)
		{
			LOGGER.error("Drawing officer Mapping Not found for department code "+deptMap.get(dept));
			errorMessage.append("Drawing officer Mapping Not found for department code "+deptMap.get(dept)+"\n");
		return null;
			
		}else
		{
			
		DrawingOfficer	drawingOfficer1=(DrawingOfficer) remittancePersistenceService.getPersistenceService().find("from DrawingOfficer where id=? ", deptDOMap.get(dept).intValue());
			if(drawingOfficer1.getTan()==null)
			{
				LOGGER.error("Drawing officer Mapping Not found for department code "+deptMap.get(dept));
				errorMessage.append("Drawing officer Mapping Not found for department code "+deptMap.get(dept)+"\n");
				return null;
			}
			else
			{
				return drawingOfficer1;
			}
				
		}
		
	}
	
	
    private void updateScheduleLogDetail(CVoucherHeader voucherHeader,Long schedularLogId) {
	RemittanceSchedulePayment rsPaymentLog=new RemittanceSchedulePayment();
	rsPaymentLog.setVoucherheaderId(voucherHeader);
	//This fix is for Phoenix Migration.rsPaymentLog.setSchId((RemittanceSchedulerLogHibernateUtil.getCurrentSession().load(RemittanceSchedulerLog.class,schedularLogId ));
HibernateUtil.getCurrentSession().save(rsPaymentLog);
		
	}
/**
 * updates generalledger  table's remittance date to date on which remittance created for recovery
 * @param glIds
 */
	private void updateRemittancedateInLeddger(List<Integer> glIds) {
        
		if(LOGGER.isDebugEnabled())
	       LOGGER.debug("Starting updateRemittancedateInLeddger with  "+glIds.size()+" glIds detailed as"+glIds);
		int size = glIds.size();
		int suceessCount=0;
		if(size<=999)
         {
        
        	 SQLQuery glQuery =HibernateUtil.getCurrentSession().createSQLQuery("update generalledger set remittancedate=:date where id in (:glIds)");
        	 glQuery.setDate("date", new java.sql.Date(new Date().getTime()));
        	 glQuery.setParameterList("glIds", glIds);
        	 suceessCount += glQuery.executeUpdate();
        	 
         }else
         {
        	 //this part is incomplete
        	 int fromIndex=0;
        	 int toIndex=999;
        	while(size%1000>=1000)
        	{
        		
        	 SQLQuery glQuery =HibernateUtil.getCurrentSession().createSQLQuery("update generalledger set remittancedate=:date where id in (:glIds)");
           	 glQuery.setDate("date", new java.sql.Date(new Date().getTime()));
           	 glQuery.setParameterList("glIds", glIds.subList(fromIndex, toIndex));
           	 suceessCount +=glQuery.executeUpdate();
           	 fromIndex+=1000;
           	 toIndex+=1000;
           	 size-=1000;
        	}
        	
        	 SQLQuery glQuery =HibernateUtil.getCurrentSession().createSQLQuery("update generalledger set remittancedate=:date where id in (:glIds)");
           	 glQuery.setDate("date", new java.sql.Date(new Date().getTime()));
           	 glQuery.setParameterList("glIds", glIds.subList(toIndex+1, size));
        	 
         }
		if(LOGGER.isDebugEnabled())
		       LOGGER.debug("Completed updateRemittancedateInLeddger "+suceessCount);
	}
/**
 *  Loads department <b>id,code</b> into deptMap .
 *  Loads fund <b>id,code</b> into fundMap .
 *  Loads department to Drawing officer as deptartment's id,Drawing officer's id into deptDOMap
 *  Useful as this is not bound to hibernate session can be used across sessions
 *  Remittance processing is done by departmnet ,fund. For search we need  "id" where as to create voucher we need
 *  "code" for both department and fund.
 *  Drawing officer id is saved into paymentheader table as these  are responsible for remittance of recovery done.
 *     
 */
	private void loadCommonData() {
		if(LOGGER.isDebugEnabled()) LOGGER.debug("Starting loadCommonData");
		// every time we need new map if dept-do mapping not found it will remove dept from processing.
		if(deptMap==null)
		{
		deptMap = getDepartments();
		}
		if(fundMap==null)
		{	
			fundMap = getFunds();
		}
		//do not check not null as it might get update run time any time
		deptDOMap=getDOsForDepartment();
		
		loadReceiptBankAccounts();
		loadGJVbankAccounts();
		loadStartDate();
		loadNextOwner();
		
		
		if(LOGGER.isDebugEnabled()) LOGGER.debug("loadCommonData Completed");
	}



/**
 * 
 */
private void loadNextOwner() {
	user = (User)remittancePersistenceService.getPersistenceService().find("from User where userName='ASSTBUDGET'");
	//This fix is for Phoenix Migration.nextOwner = eisCommonService.getPositionByUserId(user.getId());

	
}

private void loadGJVbankAccounts() {
	try{
	GJVBankAccountMap=new HashMap<String, Integer>();	 
	String value="";
	List<AppConfig> appConfigList = (List<AppConfig>) remittancePersistenceService.getPersistenceService().findAllBy("from AppConfig where key_name = 'AuoRemittance_Account_Number_For_GJV'");
	if(appConfigList==null)
	{
		throw new ValidationException(Arrays.asList(new ValidationError("AuoRemittance_Account_Number_For_GJV app config key not defined","AuoRemittance_Account_Number_For_GJV app config key not defined")));
	}
	for (AppConfig appConfig : appConfigList) 
	{
		for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) 
		{
			value = appConfigVal.getValue();
			
			List<Bankaccount> bankAcountsList = remittancePersistenceService.getPersistenceService().findAllBy("from Bankaccount ba where accountNumber=?",value.split("-")[1]);
			if(bankAcountsList.size()==1)
			{
				GJVBankAccountMap.put(value.split("-")[0], bankAcountsList.get(0).getId());
			}else
			{
				throw new ValidationException(Arrays.asList(new ValidationError("AuoRemittance_Account_Number_For_Receipts app config value  does not return proper single account","AuoRemittance_Account_Number_For_GJV app config value  does not return proper single account")));
			}
		   
		}
	}
	
	GJVFundCodes = new ArrayList<String>();
	for(String s:GJVBankAccountMap.keySet())
	{
		GJVFundCodes.add(s);
	}
	LOGGER.debug("Funds Mapped for GJVs:"+GJVFundCodes);

}	
catch (NullPointerException e) {
	throw new ValidationException(Arrays.asList(new ValidationError("AuoRemittance_Account_Number_For_GJV app config key not defined","AuoRemittance_Account_Number_For_GJV app config key not defined")));
	}
	
}


/**
 *  will load receipt bank accounts and 
 *  load the funds to be considered for receipt
 *  this is common for all . In case you need to consider fund for each glcode
 *  you have to implement that . Understand this logic before doing any code change
 *  adding appconfig for capital,etc fund ie other than Municipal fund 
 *   As of now some wrong receipt vouchers exist in system belong to capital fund
 *   where as that should be of Municipal fund 
 */

private void loadReceiptBankAccounts() {
	try {
		//will get result as fundcode-bankaccountnumber
		//expect is row 1. 01-****700
		//              2. 02-****701 
		receiptBankAccountMap=new HashMap<String, Integer>();	 
		String value="";
		List<AppConfig> appConfigList = (List<AppConfig>) remittancePersistenceService.getPersistenceService().findAllBy("from AppConfig where key_name = 'AuoRemittance_Account_Number_For_Receipts'");
		if(appConfigList==null)
		{
			throw new ValidationException(Arrays.asList(new ValidationError("AuoRemittance_Account_Number_For_Receipts app config key not defined","AuoRemittance_Account_Number_For_Receipts app config key not defined")));
		}
		for (AppConfig appConfig : appConfigList) 
		{
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) 
			{
				value = appConfigVal.getValue();
				
				List<Bankaccount> bankAcountsList = remittancePersistenceService.getPersistenceService().findAllBy("from Bankaccount ba where accountNumber=?",value.split("-")[1]);
				if(bankAcountsList.size()==1)
				{
					receiptBankAccountMap.put(value.split("-")[0], bankAcountsList.get(0).getId());
				}else
				{
					throw new ValidationException(Arrays.asList(new ValidationError("AuoRemittance_Account_Number_For_Receipts app config value  does not return proper single account","AuoRemittance_Account_Number_For_Receipts app config value  does not return proper single account")));
				}
			   
			}
		}
		
		
		receiptFundCodes = new ArrayList<String>();
		for(String s:receiptBankAccountMap.keySet())
		{
			receiptFundCodes.add(s);
		}
		if(LOGGER.isDebugEnabled())
		LOGGER.debug("Funds Mapped for Receipts:"+receiptFundCodes);
	}	
	catch (NullPointerException e) {
		throw new ValidationException(Arrays.asList(new ValidationError("AuoRemittance_Account_Number_For_Receipts app config key not defined","AuoRemittance_Account_Number_For_Receipts app config key not defined")));
		}
}
private Map<Integer, Integer> getDOsForDepartment() {

	@SuppressWarnings("unchecked")
	final List<Object[]> list =HibernateUtil.getCurrentSession().createSQLQuery("select department_id,drawingofficer_id from eg_dept_do_mapping  order by  department_id").list();
	Map<Integer, Integer> deptMap=new LinkedHashMap<Integer, Integer>();
	for(Object[] dept:list)
	{
		BigDecimal id=(BigDecimal)dept[0];
		BigDecimal officer=(BigDecimal)dept[1];
		deptMap.put(id.intValue(),officer.intValue());  

	}
	if(list==null || list.isEmpty())
	{
		throw new ValidationException(Arrays.asList(new ValidationError( "Department Drawing officer not found","Department Drawing officer not found")));
	}
	return deptMap;

	}
private Map<Integer, String> getDepartments() {
	@SuppressWarnings("unchecked")
	final List<Object[]> list =HibernateUtil.getCurrentSession().createSQLQuery("select id_dept,dept_Code from eg_department  order by dept_Code").list();
	Map<Integer, String> deptMap=new LinkedHashMap<Integer, String>();
	for(Object[] dept:list)
	{
		BigDecimal id=(BigDecimal)dept[0];

		deptMap.put(id.intValue(),(String)dept[1]);  

	}
	return deptMap;
	}
/**
 * 
 * @param glcode
 *  All common validation should be put here 
 *  as of now 2 validations
 *  1. Drawing officer mapping to department, drawing officer tan number added in the search itself
 *  2. Bank account to coa-fund mapping is added in the search itself 
 * 
 * 
 * */
	private void validate(String glcode) {
	 	

		
		
	}

	private void updateScheduleLog(String message,String jobName,String glcode,boolean success, Long schedularLogId) {
       RemittanceSchedulerLog record=null;//This fix is for Phoenix Migration.(RemittanceSchedulerLog.getSession().load(RemittanceSchedulerLog.class, schedularLogId);
       record.setGlcode(glcode);
       record.setLastRunDate(new Date());
       if(success)
       {
       record.setStatus(FinancialConstants.REMITTANCE_SCHEDULER_LOG_STATUS_SUCCESS);
       }else
       {
    	   record.setStatus(FinancialConstants.REMITTANCE_SCHEDULER_LOG_STATUS_FAILURE);
       }
       if(jobName!=null && jobName.equalsIgnoreCase("Manual"))
       {
    	   record.setSchType(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_MANUAL);   
       }else
       {
    	   record.setSchType(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_AUTO);
       }
       
       record.setRemarks(message);
       record.setSchJobName(jobName);
       record.setCreatedDate(new Date());
       record.setCreatedBy(Integer.valueOf(EGOVThreadLocals.getUserId()));
       remittanceSchedulerLogService.persist(record);
       
 	}

	@SuppressWarnings("deprecation")
	private List<Integer> createRemittance(String voucher, CVoucherHeader voucherHeader) {
		
		List<Integer> glIds=new ArrayList<Integer>();
		EgRemittance remit=new EgRemittance();
		remit.setFund(voucherHeader.getFundId());
		remit.setRecovery(recovery);
		CFinancialYear financialYearByDate = financialYearDAO.getFinancialYearByDate(voucherHeader.getVoucherDate());  
		remit.setFinancialyear(financialYearByDate);
		remit.setCreateddate(today);
		remit.setCreatedby(BigDecimal.valueOf(Integer.valueOf(EGOVThreadLocals.getUserId())));
		remit.setLastmodifiedby(BigDecimal.valueOf(Integer.valueOf(EGOVThreadLocals.getUserId())));
		remit.setLastmodifieddate(today);
		remit.setMonth(BigDecimal.valueOf(today.getMonth()));
		remit.setVoucherheader(voucherHeader);
		remit.setAsOnDate(voucherHeader.getVoucherDate());
		Set<EgRemittanceDetail> egRemittanceDetail=new HashSet<EgRemittanceDetail>();
		EgRemittanceDetail remitDetail=null;
		int count=0;
		for(AutoRemittanceBean bean:voucherGroupMap.get(voucher))
		{
			
			remitDetail=new EgRemittanceDetail();
			remitDetail.setEgRemittance(remit);
			remitDetail.setRemittedamt(BigDecimal.valueOf(bean.getPendingAmount()));
			remitDetail.setLastmodifieddate(today);
			//update to EgRemittanceGldtl only if the recovery is control code .
			if(isControlCode)
			{
			EgRemittanceGldtl remittancegldtl 	=null;//This fix is for Phoenix Migration.(EgRemittanceGldtl)egRemittancegldtlServiceHibernateUtil.getCurrentSession().load(EgRemittanceGldtl.class,Integer.valueOf(bean.getRemittanceGldtlId()));
			remittancegldtl.setRemittedamt(BigDecimal.valueOf(bean.getGldtlAmount()));
			remitDetail.setEgRemittanceGldtl(remittancegldtl);
			}else
			{
				//This fix is for Phoenix Migration.remitDetail.setGeneralLedger((CGeneralLedger)remittancePersistenceServiceHibernateUtil.getCurrentSession().load(CGeneralLedger.class,Long.valueOf(bean.getGeneralledgerId())));
				remitDetail.setRemittedamt(BigDecimal.valueOf(bean.getGldtlAmount()));
			}
			egRemittanceDetail.add(remitDetail);
			glIds.add(bean.getGeneralledgerId());
			if(LOGGER.isDebugEnabled())
			{
				count++;
				LOGGER.debug("No of remittance Items added. You can see if the transaction is getting slower here " +count);
			
			}
		
		}
		remit.setEgRemittanceDetail(egRemittanceDetail);
		remittancePersistenceService.persist(remit);
		return glIds;
	}

	private CVoucherHeader createPayment(Integer dept,String voucher,  DrawingOfficer drawingOfficer, Bankaccount ba) {
		double totalAmount=0d;   
		HashMap<String, Object> subledgertDetailMap = null;
		HashMap<String, Object> detailMap = null;
		final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
		final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
		detailMap = new HashMap<String, Object>();
		if(!isControlCode)
		{
			List<AutoRemittanceBean> uniquelist = voucherGroupMap.get(voucher);
			for(AutoRemittanceBean bean:uniquelist)
			{
				totalAmount+=bean.getGldtlAmount();	
			}
			
			
		}else
		{
		
		for(String detail:detailKeyGroupeMap.keySet())
		{
			if(detail.startsWith(voucher))
			{
				totalAmount+=detailKeyGroupeMap.get(detail);
				String[] split = detail.split("-");
				subledgertDetailMap = new HashMap<String, Object>(); 
				subledgertDetailMap.put(VoucherConstant.DETAILTYPEID,split[detailTypeIndex] );
				subledgertDetailMap.put(VoucherConstant.DETAILKEYID, split[detailKeyIndex]);
				subledgertDetailMap.put(VoucherConstant.GLCODE, glcode);
				subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, detailKeyGroupeMap.get(detail));
				subledgerDetails.add(subledgertDetailMap);

			}

		}
		}
		
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();  
		headerdetails.put(VoucherConstant.VOUCHERNAME, FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE );
		headerdetails.put(VoucherConstant.VOUCHERTYPE, FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);                
		headerdetails.put(VoucherConstant.VOUCHERDATE, today);
		headerdetails.put(VoucherConstant.DEPARTMENTCODE,deptMap.get(dept));
		headerdetails.put(VoucherConstant.FUNDCODE,fundMap.get(Integer.parseInt(voucher.split("-")[fundIndex])));
		//headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());


		detailMap = new HashMap<String, Object>();
		detailMap.put(VoucherConstant.CREDITAMOUNT, 0);
		detailMap.put(VoucherConstant.DEBITAMOUNT, totalAmount);
		detailMap.put(VoucherConstant.GLCODE,glcode);
		accountdetails.add(detailMap);

		detailMap = new HashMap<String, Object>();
		detailMap.put(VoucherConstant.CREDITAMOUNT, totalAmount);
		detailMap.put(VoucherConstant.DEBITAMOUNT, 0);
		

		detailMap.put(VoucherConstant.GLCODE,ba.getChartofaccounts().getGlcode());
		accountdetails.add(detailMap);
		
		CreateVoucher cv=new CreateVoucher();
		CVoucherHeader voucherHeader = cv.createPreApprovedVoucher(headerdetails, accountdetails, subledgerDetails);
		 
		Paymentheader ph=new Paymentheader();
		ph.setVoucherheader(voucherHeader);
		ph.setBankaccount(ba);
		ph.setPaymentAmount(BigDecimal.valueOf(totalAmount));
		ph.setType(FinancialConstants.MODEOFPAYMENT_RTGS);
		
		ph.setDrawingOfficer(drawingOfficer);
		
		paymentService.persist(ph);
		
		Miscbilldetail miscbillDetail = new Miscbilldetail();
		miscbillDetail.setBillamount(BigDecimal.valueOf(totalAmount));
		miscbillDetail.setPaidamount(BigDecimal.valueOf(totalAmount));
		miscbillDetail.setPassedamount(BigDecimal.valueOf(totalAmount));
		miscbillDetail.setPayVoucherHeader(voucherHeader);

		miscbillDetail.setPaidto(remitted);
	HibernateUtil.getCurrentSession().save(miscbillDetail);

	//This fix is for Phoenix Migration.paymentWorkflowService.start(ph,nextOwner );
		paymentWorkflowService.transition("uac_ao_approve|"+user.getId(), ph, "created from schedular");
		
		return voucherHeader;
	}
/**
 * 
 * @return
 * This api needs to be updated to logic of fetching 
 */
	private Map<Integer, String> getFunds() {
		@SuppressWarnings("unchecked")
		final List<Object[]> list =HibernateUtil.getCurrentSession().createSQLQuery("select id,code from Fund where isactive=1 order by code").list();
		Map<Integer, String> fundMap=new HashMap<Integer, String>();
		for(Object[] fund:list)
		{
			BigDecimal id=(BigDecimal)fund[0];

			fundMap.put(id.intValue(),(String)fund[1]);  

		}
		return fundMap;
	}

	/**
	 * @author Manikanta
	 * @param glcode
	 *  Will select the sum of recovery for the specified period
	 *  Vouchers selected of not yet paid or paid and cancelled
	 *  Will read misc getHeaderMandateFields from baseVoucherAction
	 *  Will need the egRemittanceGldetailid to update so you cannot group by coa,fund,department
	 *  that you have to manually sum and update the total remittance payment amount.
	 *  can make use of remitrecovery service search() method by passing search fields in voucherheader and date in remittancebean
	 *  but there no need to select the vouchernumber as we require only remittance_gldtl_id from voucher
	 *  
	 */
	/**
	 * @author Elzan
	 * @param glcode
	 *  Will select the sum of recovery for the specified period- period means the date from the last run scheduler date to system date.
	 **	From the remittance scheduler log get the latest rundate for the successful job. There can be case where the scheduler ran and failed
	 * 	We should not be considering this date.  
	 * 
	 ** Next step is to identify the vouchers that should come for remittance payment. There are basically 4 categories here. 
	 * For all these categories there is a common rule- once remitted vouchers should not come for remittance again unless the associated payment is cancelled.
	 *
	 ****1.Select all the approved vouchers where the selected glcode comes in the credit side and moduleid=10. This is to identify collection receipts.
	 * For all these vouchers check for the payinslip reference in the instrumentotherdetail table. Payinslip should exist and should be approved.
	 * 
	 ****2.Select all the Journal Vouchers where moduleid is not 10 (not collection receipts), where the selected glcode comes in the credit side.
	 * For all these vouchers check if the payment is made to the concerned parties (contractor, supplier, employee, DO etc).
	 * Check if the instrument is allocated to these payments - cheque,cash or RTGS. Consider the vouchers if all these are done. 
	 * The cheques/RTGS assigned should be valid and not in "Surrendered" state.
	 * 
	 ***3.Select all approved adjustment entries where there is no payment needed (voucher type="Journal Voucher" and name="JVGeneral") 
	 *	where the selected glcode comes in the credit side.
	 *
	 ***4.Direct bank payments where the deductions are made. Here select all such approved payments where the selected glcode comes in the credit side.
	 *	Check if there is a valid instrument allocated to these payments - cheque,cash or RTGS
	 *   this will be manual so not picking up
	 *
	 *   
	 ** How to figure out how many payment voucher needs to be created?
	 *** Read the configuration parameters from the app_config "DEFAULTTXNMISATTRRIBUTES".
	 *** Aggregate the vouchers based on the configuration parameters
	 *** Do not consider Drawing officer for aggregation since the department to DO mapping is one to one.
	 *   
	 ** How to calculate the remittance payment amount?
	 *** There are 2 categories here - not remitted at all, partially remitted (there in the system now)
	 ***1. For all the selected vouchers get the gldtlamt from EG_REMITTANCE_GLDTL WHERE remittedamt IS NOT NULL
	 ***2. For all the selected vouchers get the gldtlamt from EG_REMITTANCE_GLDTL WHERE gldtlamt!=remittedamt 
	 ***3. Select only those generalledger entries which has remittance date as null
	 ***4. One issue is the manual generation of remittance allows partial so generalledger entry for remittance payment 
	 *     is not updated. So the search might get longer. 
	 * All the parameters for remittance payment needs to be identified and set for the following-
	 * 1. Payto - will be from the recovery master
	 * 2. DO (new field) - based on the department of the payment, get this value from the DEPARTMENT-DO mapping master.
	 * 3. Subledger information will be same as that of the original voucher
	 * 4. The bank from which the payment needs to be made will be configured in the set up master (either in recovery master or separately)
	 * 5. Approver to which the payment needs to be send to
	 * 6. function to be used in case the function is made non-mandatory in "DEFAULTTXNMISATTRRIBUTES".
	 * 7. Department for the payment voucher will be the bill department (as per aggregation)
	 * 8. Fund for the payment voucher will be the bill fund (as per aggregation)
	 * @param lastRunDate 
	 * @return 
	 */
	private List<AutoRemittanceBean> searchRecoveryByCOA(Integer deptId)
	{
		recoveries=new ArrayList<AutoRemittanceBean>(); 
		recoveries.addAll(getReceiptRecoveries(deptId,0));
		recoveries.addAll(getJVRecoveries( deptId));
		recoveries.addAll(getGJVRecovries( deptId,-1));
		return recoveries;
	}

	private List<AutoRemittanceBean> searchNonControlCodeRecoveryByCOA(Integer dept) {
		
		recoveries=new ArrayList<AutoRemittanceBean>(); 
		recoveries.addAll(getNonControleCodeReceiptRecoveries(dept,0));
		recoveries.addAll(getNonControleCodeJVRecoveries(dept));
		recoveries.addAll(getNonControleCodeGJVRecovries(dept,-1));
		return recoveries;
	}
	

	private Collection<? extends AutoRemittanceBean> getNonControleCodeGJVRecovries(Integer dept, int bankaccount) {
		StringBuffer qry=new StringBuffer(2048);
		 qry.append(" SELECT DISTINCT gl.id AS generalledgerId,  vh.fundid           AS fundId,  gl.creditamount   " +
		 "   AS gldtlAmount, "+bankaccount+" AS bankAccountId "+ 
		" FROM VOUCHERHEADER vh ,  VOUCHERMIS mis,  GENERALLEDGER gl , fund f, TDS recovery "+
		" WHERE  recovery.GLCODEID =gl.GLCODEID  AND vh.ID =gl.VOUCHERHEADERID"+
		" AND gl.remittanceDate    IS NULL AND mis.VOUCHERHEADERID   =vh.ID AND vh.STATUS =0 and vh.fundid=f.id " +
		" and vh.name='"+FinancialConstants.JOURNALVOUCHER_NAME_GENERAL+"' and vh.moduleid is null "+
		" AND recovery.ID      ="+recovery.getId()+" AND vh.voucherdate    >= :startdate  ");
		 if(lastRunDate!=null)
		 {
		 qry.append(" and  vh.voucherdate    <= :lastrundate");
		 }
		 
		 if(receiptFundCodes!=null && !receiptFundCodes.isEmpty())
			{
			 qry.append(" and  f.code in (:fundCodes) ");	 
			 
			}
		 
		SQLQuery query =HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());
		query.addScalar("generalledgerId", IntegerType.INSTANCE)
		.addScalar("fundId", IntegerType.INSTANCE)
		.addScalar("gldtlAmount",DoubleType.INSTANCE)
		.addScalar("bankAccountId",IntegerType.INSTANCE);
		if(lastRunDate!=null)
		{
		query.setDate("lastrundate", new java.sql.Date(lastRunDate.getTime()));
		}
		
		if(startDate!=null)
		{
		query.setDate("startdate", new java.sql.Date(startDate.getTime()));
		}
		if(receiptFundCodes!=null && !receiptFundCodes.isEmpty())
		{
			query.setParameterList("fundCodes", receiptFundCodes);	
		}
		query.setResultTransformer(	Transformers.aliasToBean(AutoRemittanceBean.class));
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("ReceiptRecoveries query "+qry);
		return query.list();
		
		
		
	}


	private List<AutoRemittanceBean> getNonControleCodeJVRecoveries(Integer dept) {
		
		StringBuffer qry=new StringBuffer(2048);
		 qry.append(" SELECT DISTINCT gl.id AS generalledgerId,  vh.fundid           AS fundId,  gl.creditamount   " +
			 		"   AS gldtlAmount,ih.bankaccountid AS bankAccountId "+ 
		" FROM VOUCHERHEADER vh ,  VOUCHERMIS mis,  GENERALLEDGER gl ,  VOUCHERHEADER payment,  " +
		" EGF_INSTRUMENTHEADER ih, EGF_INSTRUMENTVOUCHER iv ,TDS recovery,miscbilldetail mb "+
		" WHERE  recovery.GLCODEID =gl.GLCODEID  AND vh.ID =gl.VOUCHERHEADERID "+
		" AND gl.remittanceDate    IS NULL AND mis.VOUCHERHEADERID   =vh.ID AND vh.STATUS =0  "+
		" AND ih.id =iv.instrumentheaderid "+ 
		" AND iv.voucherheaderid    =payment.id and payment.status=0 AND ih.id_status NOT     IN (" +
		"select id from egw_status where moduletype='Instrument' and description in ('" +FinancialConstants.INSTRUMENT_CANCELLED_STATUS
		+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_STATUS
		+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS+"') "+
		" ) and mb.billvhid=vh.id and mb.payvhid=payment.id "+
		" AND recovery.ID      ="+recovery.getId()+"  ");
		
			if(lastRunDate!=null)
			{
				qry.append(" and (ih.instrumentdate<= :lastrundate or ih.transactiondate<=:lastrundate )");
			}
			if(startDate!=null)
			{
				qry.append(" and (ih.instrumentdate >=:startdate or ih.transactiondate>=:startdate )");
			}
		 
		SQLQuery query =HibernateUtil.getCurrentSession().createSQLQuery(qry.toString());
		query.addScalar("generalledgerId", IntegerType.INSTANCE)
		.addScalar("fundId", IntegerType.INSTANCE)
		.addScalar("gldtlAmount",DoubleType.INSTANCE)
		.addScalar("bankAccountId",IntegerType.INSTANCE);
		if(lastRunDate!=null)  
		{
		query.setDate("lastrundate", new java.sql.Date(lastRunDate.getTime()));
		}
		
		if(startDate!=null)
		{
		query.setDate("startdate", new java.sql.Date(startDate.getTime()));
		}
		
		query.setResultTransformer(	Transformers.aliasToBean(AutoRemittanceBean.class));
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("getNonControleCodeJVRecoveries query "+qry);
		return query.list();
	 
		
		
		
	
	}


	private void loadStartDate() {
		SimpleDateFormat stringToDate=new SimpleDateFormat("dd/MM/yyyy");
		String value=null;
		try {
			List<AppConfig> appConfigList = (List<AppConfig>) remittancePersistenceService.getPersistenceService().findAllBy("from AppConfig where key_name = 'AutoRemittance_Start_Date'");
			if(appConfigList==null)
			{
				throw new ValidationException(Arrays.asList(new ValidationError("AutoRemittance_Start_Date app config key not defined","AutoRemittance_Start_Date app config key not defined")));
			}
			for (AppConfig appConfig : appConfigList) {
				for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
					value = appConfigVal.getValue();
					
				}
			}
			
			startDate= stringToDate.parse(value);
		} catch (ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Error in parsing AutoRemittance_Start_Date app config value (should be in dd/mm/yyyy format)","Error in parsing AutoRemittance_Start_Date app config value (should be in dd/mm/yyyy format)")));
		}
		 catch (NullPointerException e) {
			 throw new ValidationException(Arrays.asList(new ValidationError("AutoRemittance_Start_Date app config value  not added","AutoRemittance_Start_Date app config  value  not added")));
			}
		
	}   
	
	

	
	/**
	 *
	 * @param gjvBankAccountId 
	 * @param glcode
	 * will return all GJV recoveries which dont have payments attached to it
	 * fund condition is not added as we have to search .
	 * Whatever mapped in app config should succed others should fail
	 */
	@SuppressWarnings("unchecked")
	private List<AutoRemittanceBean> getGJVRecovries(Integer deptId, Integer gjvBankAccountId ) {
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Fetching GJVRecovries");
		
		StringBuffer queryStr=new StringBuffer("SELECT distinct gl.id as generalledgerId,  vh.fundid AS fundId,  egr.GLDTLAMT AS gldtlAmount,   gld.DETAILTYPEID  AS detailtypeId,"+
				" gld.DETAILKEYID   AS detailkeyId,   egr.ID   AS remittanceGldtlId, "+gjvBankAccountId+" as bankAccountId,  " +
				" egr.GLDTLAMT- (SELECT DECODE(SUM(egd.REMITTEDAMT),NULL,0,SUM(egd.REMITTEDAMT))    " +
				" FROM EG_REMITTANCE_GLDTL egr1,     eg_remittance_detail egd,     eg_remittance eg,     voucherheader vh   WHERE vh.status!    =4  " +
				" AND eg.PAYMENTVHID  =vh.id   AND egd.remittanceid=eg.id   AND egr1.id         =egd.remittancegldtlid  " +
				" AND egr1.id         =egr.id   ) AS pendingAmount FROM VOUCHERHEADER vh ,  " +
				" VOUCHERMIS mis,   GENERALLEDGER gl ," +
				" GENERALLEDGERDETAIL gld,   EG_REMITTANCE_GLDTL egr,   TDS recovery5_ WHERE recovery5_.GLCODEID =gl.GLCODEID AND" +
				" gld.ID                =egr.GLDTLID AND gl.ID                 =gld.GENERALLEDGERID AND vh.ID   =gl.VOUCHERHEADERID " +
				" and gl.remittanceDate is null " +
				" AND mis.VOUCHERHEADERID   =vh.ID AND vh.STATUS=0 and vh.moduleid is null  and vh.name= '" +FinancialConstants.JOURNALVOUCHER_NAME_GENERAL+"'"+
				" AND mis.departmentid  =  " +deptId+
				" AND vh.moduleid is null"+
				" AND egr.GLDTLAMT-   (SELECT DECODE(SUM(egd.REMITTEDAMT),NULL,0,SUM(egd.REMITTEDAMT))   FROM EG_REMITTANCE_GLDTL egr1,  " +
				" eg_remittance_detail egd,     eg_remittance eg,     voucherheader vh   WHERE vh.status !=4  " +
				" AND eg.PAYMENTVHID   =vh.id   AND egd.remittanceid =eg.id   AND egr1.id          =egd.remittancegldtlid   " +
				" AND egr1.id          =egr.id   )                   >0 AND recovery5_.ID      ="+recovery.getId());
		
		if(lastRunDate!=null)
		{
			queryStr.append(" and vh.voucherdate<= '"+sdf.format(lastRunDate)+"' ");
		}
		
		if(startDate!=null)
		{
			queryStr.append(" and vh.voucherdate>= '"+sdf.format(startDate)+"' ");
		}
		SQLQuery query =HibernateUtil.getCurrentSession().createSQLQuery(queryStr.toString());
		query.addScalar("generalledgerId", IntegerType.INSTANCE)
		.addScalar("fundId", IntegerType.INSTANCE)
		.addScalar("gldtlAmount",DoubleType.INSTANCE)
		.addScalar("detailtypeId",IntegerType.INSTANCE)
		.addScalar("detailkeyId",IntegerType.INSTANCE)
		.addScalar("remittanceGldtlId",IntegerType.INSTANCE)
		.addScalar("pendingAmount",DoubleType.INSTANCE)
		.addScalar("bankAccountId",IntegerType.INSTANCE);
		/*if(lastRunDate!=null)
		{
		query.setDate("lastrundate", new java.sql.Date(lastRunDate.getTime()));
		}
		if(lastRunDate!=null)
		{
		query.setDate("startdate", new java.sql.Date(startDate.getTime()));
		}*/
		query.setResultTransformer(	Transformers.aliasToBean(AutoRemittanceBean.class));
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Query for GJVRecovries"+queryStr);
		return query.list();

	}  

	/**
	 * 
	 * @param recoveryId
	 * Will return all voucher recoveries which are billpayment done and check also assigned
	 * @param deptId 
	 * @return 
	 */
	@SuppressWarnings("deprecation")
	private List<AutoRemittanceBean> getJVRecoveries(Integer deptId) {
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Fetching JVRecoveries");
		StringBuffer queryStr=new StringBuffer("SELECT distinct gl.id as generalledgerId,  vh.fundid AS fundId,  egr.GLDTLAMT AS gldtlAmount,   gld.DETAILTYPEID  AS detailtypeId,"+
				" gld.DETAILKEYID   AS detailkeyId,   egr.ID   AS remittanceGldtlId,ih.bankaccountid as bankAccountId,  " +
				" egr.GLDTLAMT- (SELECT DECODE(SUM(egd.REMITTEDAMT),NULL,0,SUM(egd.REMITTEDAMT))    " +
				" FROM EG_REMITTANCE_GLDTL egr1,     eg_remittance_detail egd,     eg_remittance eg,     voucherheader vh   WHERE vh.status!    =4  " +
				" AND eg.PAYMENTVHID  =vh.id   AND egd.remittanceid=eg.id   AND egr1.id         =egd.remittancegldtlid  " +
				" AND egr1.id         =egr.id   ) AS pendingAmount FROM VOUCHERHEADER vh left outer JOIN miscbilldetail mb on vh.id=mb.billvhid ,  " +
				" VOUCHERMIS mis,   GENERALLEDGER gl,   voucherheader ph,   egf_instrumentheader ih,   egf_instrumentvoucher iv ," +
				" GENERALLEDGERDETAIL gld,   EG_REMITTANCE_GLDTL egr,   TDS recovery5_ WHERE recovery5_.GLCODEID =gl.GLCODEID AND" +
				" gld.ID                =egr.GLDTLID AND gl.ID                 =gld.GENERALLEDGERID AND vh.ID   =gl.VOUCHERHEADERID " +
				" and gl.remittanceDate is null "+
				" AND mis.VOUCHERHEADERID   =vh.ID AND vh.STATUS=0 AND mb.payvhid =ph.id AND ih.id =iv.instrumentheaderid " +
				" AND iv.voucherheaderid    =ph.id and ph.status!=4 AND ih.id_status NOT    IN (" +
				" select id from egw_status where moduletype='Instrument' and description in ('" +FinancialConstants.INSTRUMENT_CANCELLED_STATUS
				+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_STATUS
				+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS+"') "+
				" ) AND mis.departmentid  =  " +deptId+
				" AND egr.GLDTLAMT-   (SELECT DECODE(SUM(egd.REMITTEDAMT),NULL,0,SUM(egd.REMITTEDAMT))   FROM EG_REMITTANCE_GLDTL egr1,  " +
				" eg_remittance_detail egd,     eg_remittance eg,     voucherheader vh   WHERE vh.status !=4  " +
				" AND eg.PAYMENTVHID   =vh.id   AND egd.remittanceid =eg.id   AND egr1.id          =egd.remittancegldtlid   " +
				" AND egr1.id          =egr.id   )                   >0 AND recovery5_.ID      ="+recovery.getId());
		if(lastRunDate!=null)
		{
			queryStr.append(" and (ih.instrumentdate<='"+sdf.format(lastRunDate)+"'  or ih.transactiondate<='"+sdf.format(lastRunDate)+"') ");
		}
		if(startDate!=null)
		{
			queryStr.append(" and (ih.instrumentdate>='"+sdf.format(startDate)+"' or ih.transactiondate>='"+sdf.format(startDate)+"' ) ");
		}
		SQLQuery query =HibernateUtil.getCurrentSession().createSQLQuery(queryStr.toString());
		query.addScalar("generalledgerId", IntegerType.INSTANCE)
		.addScalar("fundId", IntegerType.INSTANCE)
		.addScalar("gldtlAmount",DoubleType.INSTANCE)
		.addScalar("detailtypeId",IntegerType.INSTANCE)
		.addScalar("detailkeyId",IntegerType.INSTANCE)
		.addScalar("remittanceGldtlId",IntegerType.INSTANCE)
		.addScalar("pendingAmount",DoubleType.INSTANCE)
		.addScalar("bankAccountId",IntegerType.INSTANCE)
		;
		/*if(lastRunDate!=null)
		{
		query.setDate("lastrundate", new java.sql.Date(lastRunDate.getTime()));
		}
		if(lastRunDate!=null)
		{
		query.setDate("startdate", new java.sql.Date(startDate.getTime()));
		}*/
		query.setResultTransformer(	Transformers.aliasToBean(AutoRemittanceBean.class));
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("JVRecoveries query "+queryStr);
		return query.list();

	}

	/**
	 * 
	 * @param recoveryId
	 *  Will return all receipt recoveries which are Remitted and approved
	 * @param deptId 
	 * @param lastRunDate 
	 * @param startDate 
	 * @param receiptBankAccountId 
	 * @return 
	 * 
	 */
	private List getReceiptRecoveries(Integer deptId, Integer receiptBankAccountId) {
       
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Fetching ReceiptRecoveries");
			StringBuffer queryStr=new StringBuffer("SELECT distinct gl.id as generalledgerId,  vh.fundid AS fundId,  egr.GLDTLAMT AS gldtlAmount,   gld.DETAILTYPEID  AS detailtypeId,"+
					" gld.DETAILKEYID   AS detailkeyId,   egr.ID   AS remittanceGldtlId,"+receiptBankAccountId+" as bankAccountId, " +
					" egr.GLDTLAMT- (SELECT DECODE(SUM(egd.REMITTEDAMT),NULL,0,SUM(egd.REMITTEDAMT))    " +
					" FROM EG_REMITTANCE_GLDTL egr1,     eg_remittance_detail egd,     eg_remittance eg,     voucherheader vh   WHERE vh.status!    =4  " +
					" AND eg.PAYMENTVHID  =vh.id   AND egd.remittanceid=eg.id   AND egr1.id         =egd.remittancegldtlid  " +
					" AND egr1.id         =egr.id   ) AS pendingAmount FROM VOUCHERHEADER vh ,  " +
					" VOUCHERMIS mis,   GENERALLEDGER gl,   voucherheader payinslip, fund f,  egf_instrumentheader ih,  egf_instrumentotherdetails io," +
					" GENERALLEDGERDETAIL gld,   EG_REMITTANCE_GLDTL egr,  egcl_collectionvoucher cv, egcl_collectioninstrument ci,TDS recovery5_ WHERE recovery5_.GLCODEID =gl.GLCODEID AND" +
					" gld.ID                =egr.GLDTLID AND gl.ID                 =gld.GENERALLEDGERID AND vh.ID   =gl.VOUCHERHEADERID " +
					" and gl.remittanceDate is null and f.id=vh.fundid "+
					" AND mis.VOUCHERHEADERID   =vh.ID AND vh.STATUS=0  AND io.payinslipid =payinslip.id " +
					" and cv.voucherheaderid= vh.id 	and ci.collectionheaderid= cv.collectionheaderid and ci.instrumentmasterid= ih.id"+
					" and payinslip.status=0 AND ih.id_status NOT     IN (" +
					"select id from egw_status where moduletype='Instrument' and description in ('" +FinancialConstants.INSTRUMENT_CANCELLED_STATUS
					+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_STATUS
					+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS+"') "+
					" ) AND mis.departmentid  =  " +deptId+
					" AND egr.GLDTLAMT-   (SELECT DECODE(SUM(egd.REMITTEDAMT),NULL,0,SUM(egd.REMITTEDAMT))   FROM EG_REMITTANCE_GLDTL egr1,  " +
					" eg_remittance_detail egd,     eg_remittance eg,     voucherheader vh   WHERE vh.status !=4  " +
					" AND eg.PAYMENTVHID   =vh.id   AND egd.remittanceid =eg.id   AND egr1.id          =egd.remittancegldtlid   " +
					" AND egr1.id          =egr.id   )                   >0 AND recovery5_.ID      ="+recovery.getId());
			if(lastRunDate!=null)
			{
				queryStr.append(" and payinslip.voucherdate<='"+sdf.format(lastRunDate)+"' ");
			}
			
			if(startDate!=null)
			{
				queryStr.append(" and payinslip.voucherdate>='"+sdf.format(startDate)+"'");
			}
			if(receiptFundCodes!=null && !receiptFundCodes.isEmpty())
			{
			queryStr.append(" and f.code in (:fundCodes) ");
			}
			     
			SQLQuery query =HibernateUtil.getCurrentSession().createSQLQuery(queryStr.toString());
			query.addScalar("generalledgerId", IntegerType.INSTANCE)
			.addScalar("fundId", IntegerType.INSTANCE)
			.addScalar("gldtlAmount",DoubleType.INSTANCE)
			.addScalar("detailtypeId",IntegerType.INSTANCE)
			.addScalar("detailkeyId",IntegerType.INSTANCE)
			.addScalar("remittanceGldtlId",IntegerType.INSTANCE)
			.addScalar("pendingAmount",DoubleType.INSTANCE)
			.addScalar("bankAccountId",IntegerType.INSTANCE)
			;
			/*if(lastRunDate!=null)
			{
			query.setDate("lastrundate", new java.sql.Date(lastRunDate.getTime()));
			}
			
			if(startDate!=null)
			{
			query.setDate("startdate", new java.sql.Date(startDate.getTime()));
			}*/
			if(receiptFundCodes!=null && !receiptFundCodes.isEmpty())
			{
				query.setParameterList("fundCodes", receiptFundCodes);	
			}
			query.setResultTransformer(	Transformers.aliasToBean(AutoRemittanceBean.class));
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("ReceiptRecoveries query "+queryStr);
			return query.list();
    


	}

	
	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public void setRemittancePersistenceService(
			RemittancePersistenceService remittancePersistenceService) {
		this.remittancePersistenceService = remittancePersistenceService;
	}

	public void setRecoveryService(RecoveryService recoveryService) {
		this.recoveryService = recoveryService;
	}

	public void setEgRemittancegldtlService(
			PersistenceService<EgRemittanceGldtl, Integer> egRemittancegldtlService) {
		this.egRemittancegldtlService = egRemittancegldtlService;
	}
	public RemittancePersistenceService getRemittancePersistenceService() {
		return remittancePersistenceService;
	}
	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	public void setPaymentWorkflowService(
			SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
		this.paymentWorkflowService = paymentWorkflowService;
	}


	public void setRemittanceSchedulerLogService(
			PersistenceService<RemittanceSchedulerLog, Integer> remittanceSchedulerLogService) {
		this.remittanceSchedulerLogService = remittanceSchedulerLogService;
	}


	public PersistenceService<RemittanceSchedulerLog, Integer> getRemittanceSchedulerLogService() {
		return remittanceSchedulerLogService;
	}


	public Map<Integer, Integer> getDeptDOMap() {
		return deptDOMap;
	}


	public void setDeptDOMap(Map<Integer, Integer> deptDOMap) {
		this.deptDOMap = deptDOMap;
	}

	public boolean isSuccessForAutoRemittance() {
		return successForAutoRemittance;
	}

	public void setSuccessForAutoRemittance(boolean successForAutoRemittance) {
		this.successForAutoRemittance = successForAutoRemittance;
	}

	public StringBuffer getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(StringBuffer errorMessage) {
		this.errorMessage = errorMessage;
	}

}

