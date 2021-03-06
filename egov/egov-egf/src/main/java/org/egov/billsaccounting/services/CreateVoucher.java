package org.egov.billsaccounting.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Chequedetail;
import org.egov.commons.EgfRecordStatus;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.ReceiptHeader;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.VoucherDetail;
import org.egov.commons.Vouchermis;
import org.egov.commons.service.CommonsService;
import org.egov.dao.bills.BillsDaoFactory;
import org.egov.dao.bills.EgBillRegisterHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.RBACException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;

import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.masters.services.MastersService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.voucher.PreApprovedVoucher;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.model.PersonalInformation;
import org.egov.services.bills.BillsService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.transactions.CommonMethodsI;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import com.exilant.exility.common.TaskFailedException;

/**
 * This Class will create voucher from bill <br>
 * @author Manikanta
 * created on 15-sep-2008
 *
 */
public class CreateVoucher {
	private static final String DD_MMM_YYYY = "dd-MMM-yyyy";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private static final String REVERSAL_VOUCHER_DATE = "Reversal voucher date";
	private static final String VOUCHER_HEADER_ID = "Original voucher header id";
	@Autowired
	private GenericHibernateDaoFactory    genericDao;
	private  CommonsService  commonsService;
	final private static Logger LOGGER=Logger.getLogger(CreateVoucher.class);
	//Expenditure Types
	private final static String CONBILL="Works";
	private final static String SUPBILL="Purchase";
	private final static String SALBILL="Salary";
	private final static String CONTINGENTBILL="Contingency";
	private final  static String PENSBILL="Pension";
	private final static String GRATBILL="Gratuity";
	//messages
	private final static String FUNDMISSINGMSG="Fund is not used in Bill ,cannot create Voucher";
	private String cgnCode=null;
	private String name=null;
	private String vStatus=null;
	private final String ISREQUIRED =".required";
	private final String SELECT ="  Please Select  ";


	//add here for other bills

	//bill related common variables for back end updation
	Timestamp curDate = new Timestamp(System.currentTimeMillis());
	SimpleDateFormat sdf =new SimpleDateFormat(DD_MM_YYYY);
	SimpleDateFormat formatter=new SimpleDateFormat(DD_MMM_YYYY);
	NumberFormat nf=new DecimalFormat("##############.00") ;
	EGovernCommon cm = new EGovernCommon();

	//transaction related common variables
	private int  usrId;
	private static	CommonsService comm=null;
	private static	BillsService billsMngr=null;
	
	private static final String ERR="Exception in CreateVoucher";
	private static final String DEPTMISSINGMSG = "Department is missing in the Bill cannot proceed creating vouvher";
	private static final String IS_MISSING = "is missing";
	private static final String NAME = "Reversal voucher name";
	private static final String IS_EMPTY = "is empty";
	private static final String TYPE = "Reversal voucher type";
	private static final String REVERSAL_VOUCHER_NUMBER = "Reversal voucher number";
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	@Autowired
	private WorksBillService wm;
	@Autowired
	private CbillService cBillmgr;
	@Autowired
	private  DepartmentService deptM;
	@Autowired
	private  BoundaryService boundary;
	@Autowired
	private  SalaryBillService salBillMgr;
	@Autowired
	private UserService userMngr;
	@Autowired
	private  EisCommonService eisCommonService;
	private MastersService masters;
	CommonMethodsI cmImpl=new CommonMethodsImpl();
	PersistenceService<ReceiptHeader, Integer> rcptHeaderService;
	PersistenceService<Chequedetail, Integer> chequeDetailSer;
	PersistenceService<Bankreconciliation, Integer> bankReconSer;
	PersistenceService<EgBillregistermis, Integer>  billMisSer;
	PersistenceService<EgBilldetails, Integer>  billDetailSer;
	PersistenceService<Fund,Integer> fundService;
	public void setGeneralLedgerService(
			PersistenceService<CGeneralLedger, Long> generalLedgerService) {
		this.generalLedgerService = generalLedgerService;
	}
	public void setGeneralLedgerDetailService(
			PersistenceService<CGeneralLedgerDetail, Long> generalLedgerDetailService) {
		this.generalLedgerDetailService = generalLedgerDetailService;
	}
	PersistenceService<CGeneralLedger, Long> generalLedgerService;
	PersistenceService<CGeneralLedgerDetail, Long> generalLedgerDetailService;
	
	private Fund fundByCode;
	private PersonalInformationDAO personalInformationDAO;
	public	CreateVoucher()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Initializing CreateVoucher Service");
		try
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("calling setGenericHibDao"+genericDao);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("calling setGenericHibDao"+commonsService);

			generalLedgerService=new PersistenceService<CGeneralLedger, Long>();
			generalLedgerService.setType(CGeneralLedger.class);
			//generalLedgerService.setSessionFactory(new SessionFactory());

			generalLedgerDetailService=new PersistenceService<CGeneralLedgerDetail, Long>();
			generalLedgerDetailService.setType(CGeneralLedgerDetail.class);
			//generalLedgerDetailService.setSessionFactory(new SessionFactory());

		}
		catch(Exception e)
		{
			LOGGER.error("Exception in CreateVoucher",e);
			throw new EGOVRuntimeException(e.getMessage());
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Initialization completed");
	}
	/**
	 * creates voucher From billId
	 * @param billId
	 * @param voucherDate TODO
	 * @return voucherheaderId long
	 * @throws EGOVRuntimeException
	 * @throws SQLException
	 * @throws Exception
	 */
	public long createVoucherFromBill(int billId,String voucherStatus, String voucherNumber, Date voucherDate) throws EGOVRuntimeException, SQLException ,TaskFailedException{
		CVoucherHeader vh=null;
		try{
			this.vStatus=voucherStatus;
			this.usrId=Integer.parseInt(EGOVThreadLocals.getUserId());
			if(LOGGER.isDebugEnabled())     LOGGER.debug(" ---------------Generating Voucher for Bill-------");
			EgBillregister egBillregister=null;
			egBillregister=billsMngr.getBillRegisterById(Integer.valueOf(billId));
			PersistenceService persistenceService = new PersistenceService();
			//persistenceService.setSessionFactory(new SessionFactory());
			/*identify the bill type and delegate
			get the fund and fundsource
			check for mandatory fields for implementation if missing throw exception
			department is mandatory for implementation type
			fund is mandatory for all implementations*/
			EgBillregistermis  billMis=egBillregister.getEgBillregistermis();
			//checking voucher already exists or not for this bill
			try{
				CVoucherHeader result;
				if(billMis.getVoucherHeader()!=null){
					result = (CVoucherHeader) persistenceService.find("select vh from CVoucherHeader vh where vh.id = ? and vh.status!=?",billMis.getVoucherHeader().getId(),FinancialConstants.CANCELLEDVOUCHERSTATUS);
					if(result!=null){
						 throw new  EGOVRuntimeException("Voucher "+result.getVoucherNumber()+" already exists for this bill ");
					}
				}
				} catch (Exception e) {
					 throw new  EGOVRuntimeException(e.getMessage());
				}
			Integer fundId=null;
			Integer fundSrcId=null;
			Fund  fund=billMis.getFund();
			if(fund==null)
			{
				LOGGER.error(FUNDMISSINGMSG);
				throw new EGOVRuntimeException(FUNDMISSINGMSG);
			}
			else
			{
				fundId=fund.getId();
			}
			String deptMandatory=EGovConfig.getProperty("egf_config.xml","deptRequired","","general");
			if(deptMandatory.equalsIgnoreCase("Y"))
			{
				if(billMis.getEgDepartment()==null)
				{
					throw new EGOVRuntimeException(DEPTMISSINGMSG);
				}
			}

			Fundsource fundSrc=billMis.getFundsource();
			if(fundSrc!=null)
			{
				fundSrcId=Integer.valueOf(fundSrc.getId().toString());
			}

			//Read scheme and sub scheme from billregistermis
			Integer schemeId=null;
			Integer subSchemeId=null;
			if(billMis.getScheme()!=null)
			{
				schemeId=billMis.getScheme().getId();
			}
			if(billMis.getSubScheme()!=null)
			{
				subSchemeId=billMis.getSubScheme().getId();
			}
			String expType=egBillregister.getExpendituretype();
			String voucherType=null;
			String voucherSubType=null;
			if(expType.equalsIgnoreCase(CONBILL))
			{
				name="Contractor Journal";
				cgnCode="CJV";
				voucherSubType=FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL;
			}
			else if(expType.equalsIgnoreCase(SUPBILL))
			{
				name="Supplier Journal";
				cgnCode="SJV";
				if(null!= billMis.getEgBillSubType() && billMis.getEgBillSubType().getName().equalsIgnoreCase("Fixed Asset"))
					voucherSubType=FinancialConstants.JOURNALVOUCHER_NAME_FIXEDASSETJOURNAL;
				else
					voucherSubType=FinancialConstants.JOURNALVOUCHER_NAME_PURCHASEJOURNAL;
			}
			else if(expType.equalsIgnoreCase(SALBILL))
			{
				name="Salary Journal";
				cgnCode="SAL";
				voucherSubType=FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL;
			}
			//Pension,Gratuity are saved as Expense Bill
			else if(expType.equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
			{
				name=FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL;
				cgnCode="OJV";
				voucherSubType=FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL;
			}
			else if(expType.equalsIgnoreCase(PENSBILL))
			{
				name="Pension Journal";
				cgnCode="TJV";
				voucherSubType=FinancialConstants.JOURNALVOUCHER_NAME_PENSIONJOURNAL;
			}
			else if(expType.equalsIgnoreCase(GRATBILL))
			{
				name="Gratuity Journal";
				cgnCode="OJV";
				voucherSubType=FinancialConstants.CBILL_VOUCHERNO_TYPE;
			}
			else
			{
				name="JVGeneral";
				cgnCode="JVG";
				voucherSubType = FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
			}
			voucherType = FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
			HashMap<String, Object> headerDetails = new HashMap<String, Object>();
			HashMap<String, Object> detailMap = null;
			HashMap<String, Object> subledgertDetailMap = null;
			Set<EgBillPayeedetails> subLedgerlist;
			final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
			final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
			Set<EgBilldetails> billDetailslist=egBillregister.getEgBilldetailes();
			detailMap = new HashMap<String, Object>();
			Map<String, Object> glcodeMap = new HashMap<String, Object>();
			
			headerDetails.put(VoucherConstant.VOUCHERNAME, name);
			headerDetails.put(VoucherConstant.VOUCHERTYPE, voucherType);
			headerDetails.put("vouchersubtype", voucherSubType);
			SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
			headerDetails.put(VoucherConstant.VOUCHERNUMBER, voucherNumber==null?"":voucherNumber);
			Date dt=new Date();
			Connection conn=null;
			PreparedStatement vNumRptst=null;
			ResultSet vNumRptrs=null;
			Date vdt;
			conn=null;
			String purposeValueVN="",purposeValue="";
				
				
				 
				 /** 
				  * Starting to check for voucher date
				  * First check if the value needs to be read from the UI.
				  * If YES, check if the value is passed from the UI, and then set data if present and throw error else.
				  * If NO, check if the value needs to be read from the bill
				  * If YES, set the voucher date same as that of bill date.
				  * If NO, set the value as system date
				  **/
				try{
				List<AppConfigValues> configValues =genericDao.getAppConfigValuesDAO().
						getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,"VOUCHERDATE_FROM_UI"); 
				
				for (AppConfigValues appConfigVal : configValues) {
						  purposeValueVN=appConfigVal.getValue();
						 }
				} catch (Exception e) {
					 throw new EGOVRuntimeException("Appconfig value for VOUCHERDATE_FROM_UI is not defined in the system");
				}
				if(purposeValueVN.equals("Y")){
					if(voucherDate==null )
					{
						throw new ValidationException(Arrays.asList(new ValidationError("Voucherdate Should be entered by user","voucherfrombill.voucherdate.mandatory")));
						
					}
					else{
						dt=voucherDate;
						vdt=dt;
					}
				
				}
				else{
					
					try{
						List<AppConfigValues> configValues =genericDao.getAppConfigValuesDAO().
								getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,"USE BILLDATE IN CREATE VOUCHER FROM BILL"); 
						
						for (AppConfigValues appConfigVal : configValues) {
								  purposeValueVN=appConfigVal.getValue();
								 }
						} catch (Exception e) {
							 throw new  EGOVRuntimeException("Appconfig value for USE BILLDATE IN CREATE VOUCHER FROM BILL is not defined in the system");
						}
					if(purposeValue.equals("Y")){
						vdt=egBillregister.getBilldate();
						dt=egBillregister.getBilldate();
					}else{
						 vdt=dt;
					}
				}
			 headerDetails.put(VoucherConstant.VOUCHERDATE, vdt);
			if(egBillregister.getId()!=null)
					headerDetails.put("billid", egBillregister.getId());
			if(billMis.getSourcePath()!=null)
				headerDetails.put(VoucherConstant.SOURCEPATH, billMis.getSourcePath());
			if(billMis.getEgDepartment()!=null)
				headerDetails.put(VoucherConstant.DEPARTMENTCODE, billMis.getEgDepartment().getCode());
			if(billMis.getFund()!=null)
				headerDetails.put(VoucherConstant.FUNDCODE, billMis.getFund().getCode());
			if(billMis.getScheme()!=null)
				headerDetails.put(VoucherConstant.SCHEMECODE, billMis.getScheme().getCode());
			if( billMis.getSubScheme()!=null)
				headerDetails.put(VoucherConstant.SUBSCHEMECODE, billMis.getSubScheme().getCode());
			if(billMis.getFundsource()!=null)
				headerDetails.put(VoucherConstant.FUNDSOURCECODE, billMis.getFundsource().getCode());
			if(billMis.getFieldid()!=null)
			{
				if(billMis.getFieldid().getId()!=null)
				{
				headerDetails.put(VoucherConstant.DIVISIONID, billMis.getFieldid().getId().toString());
				}
			}
			if(billMis.getFunctionaryid()!=null)
				headerDetails.put(VoucherConstant.FUNCTIONARYCODE, billMis.getFunctionaryid().getCode());
			//TODO- read the fnction from billdetails. We can remove this
			if(billMis.getFunction()!=null)
				headerDetails.put(VoucherConstant.FUNCTIONCODE, billMis.getFunction().getCode());
			
			for (EgBilldetails egBilldetails : billDetailslist)
			{     
				
				//persistenceService.setSessionFactory(new SessionFactory());
				detailMap = new HashMap<String, Object>();
					if(null!=egBilldetails.getFunctionid()){
						/*CFunction function = (CFunction) HibernateUtil.getCurrentSession().load(CFunction.class, (egBilldetails.getFunctionid()).longValue());
						detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());*/
					}
					detailMap.put(VoucherConstant.DEBITAMOUNT, egBilldetails.getDebitamount()==null?BigDecimal.ZERO:egBilldetails.getDebitamount());
					detailMap.put(VoucherConstant.CREDITAMOUNT, egBilldetails.getCreditamount()==null?BigDecimal.ZERO:egBilldetails.getCreditamount());
					String glcode = (persistenceService.find("select glcode from CChartOfAccounts where id = ? ",(egBilldetails.getGlcodeid()).longValue())).toString();
					detailMap.put(VoucherConstant.GLCODE, glcode);
					accountdetails.add(detailMap);
					subLedgerlist=egBilldetails.getEgBillPaydetailes();
				for (EgBillPayeedetails egBillPayeedetails : subLedgerlist) {
					subledgertDetailMap = new HashMap<String, Object>();
					subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, egBillPayeedetails.getDebitAmount()==null?BigDecimal.ZERO:egBillPayeedetails.getDebitAmount());
					subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, egBillPayeedetails.getCreditAmount()==null?BigDecimal.ZERO:egBillPayeedetails.getCreditAmount());
					subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, egBillPayeedetails.getAccountDetailTypeId());
					subledgertDetailMap.put(VoucherConstant.DETAILKEYID, egBillPayeedetails.getAccountDetailKeyId());
					subledgertDetailMap.put(VoucherConstant.GLCODE, glcode);
					subledgerDetails.add(subledgertDetailMap);
				}
			}
			vh=createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);
			egBillregister.getEgBillregistermis().setVoucherHeader(vh);

		}
		 catch (ValidationException e) {
			 LOGGER.error(e.getErrors());
				throw new ValidationException(e.getErrors());
			} 
		catch(Exception e)
		{
			LOGGER.error("Error in create voucher from bill"+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		return vh.getId().longValue();
	}

	

	/**
	 * creates voucher From billId
	 * @param billId
	 * @return voucherheaderId long
	 * @throws EGOVRuntimeException
	 * @throws SQLException
	 * @throws Exception
	 */
	public long createVoucherFromBillForPJV(int billId,String voucherStatus,List<PreApprovedVoucher> voucherdetailList,List<PreApprovedVoucher> subLedgerList) throws EGOVRuntimeException, SQLException ,TaskFailedException{
		CVoucherHeader vh=null;
		try
		{
			this.vStatus=voucherStatus;
			this.usrId=Integer.parseInt(EGOVThreadLocals.getUserId());
			if(LOGGER.isDebugEnabled())     LOGGER.debug(" ---------------Generating Voucher-------");
			EgBillregister egBillregister=null;
			egBillregister=billsMngr.getBillRegisterById(Integer.valueOf(billId));
			/*identify the bill type and delegate
			get the fund and fundsource
			check for mandatory fields for implementation if missing throw exception
			department is mandatory for implementation type
			fund is mandatory for all implementations*/
			EgBillregistermis  billMis=egBillregister.getEgBillregistermis();
			Integer fundId=null;
			Integer fundSrcId=null;
			Fund  fund=billMis.getFund();
			if(fund==null)
			{
				LOGGER.error(FUNDMISSINGMSG);
				throw new EGOVRuntimeException(FUNDMISSINGMSG);
			}
			else
			{
				fundId=fund.getId();
			}
			String deptMandatory=EGovConfig.getProperty("egf_config.xml","deptRequired","","general");
			if(deptMandatory.equalsIgnoreCase("Y"))
			{
				if(billMis.getEgDepartment()==null)
				{
					throw new EGOVRuntimeException(DEPTMISSINGMSG);
				}
			}

			Fundsource fundSrc=billMis.getFundsource();
			if(fundSrc!=null)
			{
				fundSrcId=Integer.valueOf(fundSrc.getId().toString());
			}

			//Read scheme and sub scheme from billregistermis
			Integer schemeId=null;
			Integer subSchemeId=null;
			if(billMis.getScheme()!=null)
			{
				schemeId=billMis.getScheme().getId();
			}
			if(billMis.getSubScheme()!=null)
			{
				subSchemeId=billMis.getSubScheme().getId();
			}
			String expType=egBillregister.getExpendituretype();
			String voucherType=null;
			if(expType.equalsIgnoreCase(CONBILL))
			{
				name="Contractor Journal";
				cgnCode="CJV";
				voucherType=FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
			}
			else if(expType.equalsIgnoreCase(SUPBILL))
			{
				name="Supplier Journal";
				cgnCode="SJV";
				if(null!= billMis.getEgBillSubType() && billMis.getEgBillSubType().getName().equalsIgnoreCase("Fixed Asset"))
					voucherType=FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
				else
					voucherType=FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
			}
			else if(expType.equalsIgnoreCase(SALBILL))
			{
				name="Salary Journal";
				cgnCode="SAL";
				voucherType=FinancialConstants.SALBILL_VOUCHERNO_TYPE;
			}
			//Pension,Gratuity are saved as contingency Bill
			else if(expType.equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
			{
				name=FinancialConstants.JOURNALVOUCHER_NAME_EXPENSEJOURNAL;
				cgnCode="OJV";
				voucherType=FinancialConstants.CBILL_VOUCHERNO_TYPE;
			}
			else if(expType.equalsIgnoreCase(PENSBILL))
			{
				name="Pension Journal";
				cgnCode="OJV";
				voucherType=FinancialConstants.CBILL_VOUCHERNO_TYPE;
			}
			else if(expType.equalsIgnoreCase(GRATBILL))
			{
				name="Gratuity Journal";
				cgnCode="OJV";
				voucherType=FinancialConstants.CBILL_VOUCHERNO_TYPE;
			}
			else
			{
				name="JVGeneral";
				cgnCode="JVG";
				voucherType=FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
			}

			//vh=createVoucherheaderAndPostGLForPJV(fundId,egBillregister,fundSrcId,schemeId,subSchemeId,name,cgnCode,voucherType,voucherdetailList,subLedgerList);
		}catch(Exception e)
		{
			LOGGER.error("Error in createVoucherFromBillForPJV "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		return vh.getId().longValue();
	}



	 /**
	  * @description - This method is used to create the vouchers for the preapproved  vouchers.
	  * @param vouhcerheaderid - the vouhcerheaderid of the preapproved voucher.
	  * @param status - status of the vouchers.
	  * @return void - This method does not return anything as its only create the vouchers for the preapproved vouchers.s
	  */
	 public void  createVoucherFromPreApprovedVoucher(long vouhcerheaderid,String status)throws EGOVRuntimeException {
		 try{
			 VoucherHeader vh=new VoucherHeader();
			 vh.setId(String.valueOf(vouhcerheaderid));
			 vh.setStatus(status);
			 Connection con;
			 vh.update(null);
		 }
		 catch(Exception e)
		 {
			 LOGGER.error(e.getMessage());
			 throw new EGOVRuntimeException(e.getMessage()) ;

		 }

	 }

	 /**
	  * This API is to create a voucher from the list of HashMap values that are passed.
	  * This will take care of various types of vouchers like -receipt, payments and journal vouchers
	  * @param headerdetails
	  * <p>
	  * HashMap<String, Object> headerdetails will have the data required for the voucher header and mis :
	  * <p>vouchername -This will be the name of the voucher, will need to set the values from enumeration (mandatory)
	  * <p>vouchertype -This will be the type of the voucher, will need to set the values from enumeration (mandatory)
	  * <p>description -This will be the description of the voucher (optional)
	  * <p>vouchernumber - This will be the vouchernumber if not set will be populated (optional)
	  * <p>voucherdate - This is the date on which the voucher needs to be created (mandatory)
	  * <p>fundid - This will be the id from the fund master (mandatory)
	  * <p>moduleid - This will be the id from module master,(not present now). Any external system sending data needs to set the value for this .
	  * If value not set will take as null (optional)
	  * <p>divisionid - This is the fieldid from the boundary master (optional)
	  * <p>departmentid - This is the departmentid from the department master (optional)
	  * <p>fundsourceid - This is the fundsourceid from the fund source master (optional)
	  * <p>schemeid - This is the schemeid from the scheme master (optional)
	  * <p>subschemeid - This is the subschemeid from the subscheme master (optional)
	  * <p>status - This is the status of voucher . If not set will be taken care (optional)
	  * <p>originalvoucher - This is the reference voucherid in care of reversal voucher (optional)
	  * <p>refvoucher - This the reference voucherheaderid in case of dependency vouchers (optional)
	  * <p>budgetCheckReq -Boolean- This is a flag set in vouchermis and decided whether budget check should be done or not . 
	  * 'true' is yes 'false' is no . Default is 'true' (optional).
	  * <p>
	  * @param accountcodedetails
	  * <p>HashMap<String, Object> accountcodedetails will have data required for the ledger details
	  * <p>glcodeid - This the ledger codeid from the chartofaccounts master. (mandatory)
	  * <p>debitamount - This is the debit amount for that voucher (optional)
	  * <P>creditamount - This is the credit amount for that voucher (optional)
	  * <p>functionid - This is the functionid from the function master (optional)
	  * <p>narration -This is the narration if any for that account code (optional)
	  * <p>
	  *
	  *  <p>
	  * @param subledgerdetails
	  * <p>HashMap<String, Object> subledgerdetails will have the subledger details only for all the control codes in the voucher.
	  * <p>glcodeid -This the ledger codeid from the chartofaccounts master. (mandatory)
	  * <p>detailtypeid - This is the detailtypeid from the detailtype object (mandatory)
	  * <p>detailkeyid - This is the detailkey from the detailkey object (mandatory)
	  * <detailamount> - This is the amount for that detailkey (mandatory)
	  * <tdsid> - This is the id from the recovery master.If the glcode used is mapped in the recovery master then this data is mandatory.
	  * @return voucherheader object in case of success and null in case of fail.
	  * @throws EGOVRuntimeException
	  */
	 public CVoucherHeader createPreApprovedVoucher(HashMap<String, Object> headerdetails,List<HashMap<String,Object>> accountcodedetails,List<HashMap<String,Object>> subledgerdetails)throws EGOVRuntimeException,ValidationException {
		 PersistenceService<AppConfig, Integer> appConfigSer;
		 appConfigSer = new PersistenceService<AppConfig, Integer>();
		 //appConfigSer.setSessionFactory(new SessionFactory());
		 appConfigSer.setType(AppConfig.class);
		 AppConfig appConfig= (AppConfig) appConfigSer.find("from AppConfig where key_name =?", "PREAPPROVEDVOUCHERSTATUS");
		 if(null != appConfig && null!= appConfig.getAppDataValues() ){
			 for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				  headerdetails.put(VoucherConstant.STATUS,(Integer.valueOf(appConfigVal.getValue())));
				 }
		 }else
			 throw new EGOVRuntimeException("Appconfig value for PREAPPROVEDVOUCHERSTATUS is not defined in the system");
		 CVoucherHeader vh;
		 try
		 {
			 vh=createVoucher(headerdetails,accountcodedetails,subledgerdetails);
			 if(vh.getModuleId()!=null)
				 startWorkflow(vh);
		 }catch (ValidationException ve)
		 {
			 LOGGER.error(ERR,ve);
			 throw ve;
		 }
		 catch (Exception e) {
			 LOGGER.error(ERR,e);
			 throw new EGOVRuntimeException(e.getMessage());
		 }
		 return vh;
	 }
	 /**
	  * action name //////
	  * check all junits, EisCommonsManger is added in create voucher constructor
	  * voucher service need to pass
	 * @param voucherheader
	 * @throws ValidationException
	 */
	 public void startWorkflow(CVoucherHeader voucherheader) throws ValidationException
	 {
		 try
		 {
			 ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
					 "classpath:org/serviceconfig-Bean.xml","classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml","classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml","classpath:org/egov/infstr/beanfactory/applicationContext-pims.xml" });
			 if(voucherheader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)||voucherheader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT))
			 {
				 LOGGER.error("Calling StartWorkflow...in create voucher.....for ......ContraJournalVoucher......"+voucherheader.getType()+" ----"+voucherheader.getName());
				 EgBillRegisterHibernateDAO egBillRegDao=BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO();
				 String billtype = egBillRegDao.getBillTypeforVoucher(voucherheader);
				 if(billtype==null)
				 {
					 SimpleWorkflowService<CVoucherHeader> voucherWorkflowService =  (SimpleWorkflowService) applicationContext.getBean("voucherWorkflowService");
					//This fix is for Phoenix Migration.
					 //voucherWorkflowService.start(voucherheader, getPosition());
					// voucherWorkflowService.transition("aa_approve", voucherheader, "Created");    // action name need to pass
					// Position position = eisCommonService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
					 
					 VoucherService vs = (VoucherService)applicationContext.getBean("voucherService");
					PersistenceService persistenceService = (PersistenceService)applicationContext.getBean("persistenceService");
					Position nextPosition= getNextPosition(voucherheader,vs,persistenceService,null);
					// voucherheader.changeState("WORKFLOW INITIATED",nextPosition, "WORKFLOW STARTED");
				 }
	  		 }
			/* this logic is moved to top since both have same workflow
			 else if(voucherheader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT))
			 {
				 //
				 ReceiptVoucher rv=new ReceiptVoucher();
				 PersistenceService<ReceiptVoucher, Long> persistenceService = new PersistenceService<ReceiptVoucher, Long>();
				 persistenceService.setSessionFactory(new SessionFactory());
				 persistenceService.setType(ReceiptVoucher.class);
				 rv.setId(voucherheader.getId());
				 rv.setVoucherHeader(voucherheader);
				 persistenceService.create(rv);

				 SimpleWorkflowService<ReceiptVoucher> receiptWorkflowService =  (SimpleWorkflowService) applicationContext.getBean("receiptWorkflowService");
				 receiptWorkflowService.start(rv, getPosition());
				 receiptWorkflowService.transition("co_approve", rv, "Created");    // action name need to pass
	
				
			 }*/
			 
		 }catch(Exception e)
		 {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 LOGGER.error(ERR,e);
			 errors.add(new ValidationError("Exp in startWorkflow for JV/Receipt voucher=",e.getMessage()));
			 throw new ValidationException(errors);
		 }
	 }
	 private Position getNextPosition(CVoucherHeader voucherheader, VoucherService vs, PersistenceService persistenceService,Position position) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Getting next Position for WorkFlow");
		 Position nextPosition=null;
		EgovCommon common=new EgovCommon();  
		
		
		
		Department department = vs.getTempDepartmentForWfItem(voucherheader,position) ;
		if(department==null)
		{
			LOGGER.warn("Not able to get tempoaray Assignment defaulting to primary asssigment");
			department = vs.getDepartmentForWfItem(voucherheader) ;
			if(department==null)
			{
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("Unable to get Temp Department","Unable to get Temp Department"));
			 throw new ValidationException(errors);
			}
		}
		String functionaryName="";
		String designationName="";
		designationName="SECTION MANAGER";
		Boundary boundaryForUser =null;
		if(department.getCode().equalsIgnoreCase("A"))
		{
			try {
				HierarchyType hierarchyTypeByName = new HeirarchyTypeDAO(null).getHierarchyTypeByName("ADMINISTRATION");
				 List topBoundaries = new BoundaryDAO(null).getTopBoundaries( hierarchyTypeByName);
				 if(topBoundaries!=null && topBoundaries.size()>0)
					 boundaryForUser=(Boundary)topBoundaries.get(0);
			} catch (NoSuchObjectException e) {
				List<ValidationError> errors=new ArrayList<ValidationError>();
				errors.add(new ValidationError("Coundnot fetch Boundary","Coundnot fetch Boundary"));
				 throw new ValidationException(errors);
			} catch (TooManyValuesException e) {
				List<ValidationError> errors=new ArrayList<ValidationError>();
				errors.add(new ValidationError("Coundnot fetch Boundary","Coundnot fetch Boundary"));
				 throw new ValidationException(errors);
			}
		
			functionaryName="Compilation";
		}else
		{
			 boundaryForUser = vs.getBoundaryForUser(voucherheader);
			functionaryName="ZONE";
		}
		DesignationMaster next_desig=(DesignationMaster)persistenceService.find("from DesignationMaster  where upper(designationName)=upper(?)",designationName);
		Functionary functionary = (Functionary)persistenceService.find("from Functionary where upper(name)=upper(?)",functionaryName);
		   
		PersonalInformation employeeByFunctionary=null;
		try {
			employeeByFunctionary = personalInformationDAO.getEmployeeByFunctionary(department.getId().intValue(),next_desig.getDesignationId(),boundaryForUser.getId(),functionary.getId());
		} catch (TooManyValuesException e) {
			LOGGER.error(e.getMessage(),e);
			List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("Too Many  Employee Exists","Too Many  Employee Exists"));
			 throw new ValidationException(errors);
		
		} catch (NoSuchObjectException e) {
			LOGGER.error(e.getMessage(),e);
			List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("No  Employee Exists","No  Employee Exists"));
			 throw new ValidationException(errors);
		}
		nextPosition=vs.getPositionForEmployee(employeeByFunctionary) ;
		if(nextPosition!=null)
		{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Returning next Position for WorkFlow"+nextPosition.getName());
		}else
		{
			LOGGER.error("Could not get next Position for WorkFlow");
		}
	return nextPosition;
			
		 
	}
	 @Deprecated
	public void startWorkflow(ContraJournalVoucher cjv) throws ValidationException
	 {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting Contra Journal Voucher Workflow....startWorkflow(ContraJournalVoucher cjv)...");
		
		try
		 {
			 if(cjv.getVoucherHeaderId().getState()==null)
			 {
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("Calling StartWorkflow...in create voucher.....for ......ContraJournalVoucher.....................................................................................");
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("fetching voucherWorkflowService from application context.......");
				 ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
						 "classpath:org/serviceconfig-Bean.xml","classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml","classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml","classpath:org/egov/infstr/beanfactory/applicationContext-pims.xml" });
				//This fix is for Phoenix Migration.
				 /*SimpleWorkflowService<CVoucherHeader> voucherWorkflowService =  (SimpleWorkflowService) applicationContext.getBean("voucherWorkflowService");
//				 if(LOGGER.isDebugEnabled())     LOGGER.debug("completed voucherWorkflowService from application context.......");
				 voucherWorkflowService.start(cjv.getVoucherHeaderId(), getPosition());
				// voucherWorkflowService.transition("am_approve", cjv.getVoucherHeaderId(), "Created");    // action name need to pass
				 Position position = eisCommonService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				 cjv.changeState("WORKFLOW INITIATED",position, "WORKFLOW STARTED");
				 VoucherService vs = (VoucherService)applicationContext.getBean("voucherService");
					PersistenceService persistenceService = (PersistenceService)applicationContext.getBean("persistenceService");
					Position nextPosition= getNextPosition(cjv.getVoucherHeaderId(),vs,persistenceService,null);
					cjv.changeState("WORKFLOW INITIATED",nextPosition, "WORKFLOW STARTED");*/
			 }
		 }catch(Exception e)
		 {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 LOGGER.error(ERR,e);
			 errors.add(new ValidationError("Exp in startWorkflow for Contra=",e.getMessage()));
			 throw new ValidationException(errors);
		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed Contra Journal Voucher Workflow.......");
	 }
	/**
	 * only for contra workflow
	 * @param voucherHeader
	 * @throws ValidationException
	 *  Uses VoucherWorkflow since contra and brv workflows are same
	 */
	public void startWorkflowForCashUpdate(CVoucherHeader voucherHeader) throws ValidationException
	 {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting  Journal Voucher Workflow.  for contra......");
		
		try
		 {
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Calling StartWorkflow...For Cash");
				ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
						 "classpath:org/serviceconfig-Bean.xml","classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml","classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml","classpath:org/egov/infstr/beanfactory/applicationContext-pims.xml" });
				SimpleWorkflowService<CVoucherHeader> voucherWorkflowService =  (SimpleWorkflowService) applicationContext.getBean("voucherWorkflowService");
//				if(LOGGER.isDebugEnabled())     LOGGER.debug("completed voucherWorkflowService from application context.......");
				//voucherWorkflowService.start(voucherHeader, getPosition());
			 	VoucherService vs = (VoucherService)applicationContext.getBean("voucherService");
				PersistenceService persistenceService = (PersistenceService)applicationContext.getBean("persistenceService");
				Position nextPosition= getNextPosition(voucherHeader,vs,persistenceService,null);
				//voucherHeader.changeState("Forwarded",nextPosition, "Forwarded");
				//This fix is for Phoenix Migration.
			 
		 }catch(Exception e)
		 {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 LOGGER.error(ERR,e);
			 errors.add(new ValidationError("Exp in startWorkflow for Contra=",e.getMessage()));
			 throw new ValidationException(errors);
		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed Contra Journal Voucher Workflow.......");
	 }
	 public Position getPosition()throws EGOVRuntimeException
	 {
		 Position pos;
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("getPosition===="+Integer.valueOf(EGOVThreadLocals.getUserId()));
		// pos = eisCommonService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		//This fix is for Phoenix Migration.
		// if(LOGGER.isDebugEnabled())     LOGGER.debug("position==="+pos.getId());
		return null;//pos;
	}
	 /**
	  * This API is to create a voucher from the list of HashMap values that are passed.
	  * This will take care of various types of vouchers like -receipt, payments and journal vouchers
	  * @param headerdetails
	  * <p>
	  * HashMap<String, Object> headerdetails will have the data required for the voucher header and mis :
	  * <p>vouchername -This will be the name of the voucher, will need to set the values from enumeration (mandatory)
	  * <p>vouchertype -This will be the type of the voucher, will need to set the values from enumeration (mandatory)
	  * <p>description -This will be the description of the voucher (optional)
	  * <p>vouchernumber - This will be the vouchernumber if not set will be populated (optional)
	  * <p>voucherdate - This is the date on which the voucher needs to be created (mandatory)
	  * <p>fundid - This will be the id from the fund master (mandatory)
	  * <p>functionid - This is the functionid from the function master (optional)
	  * <p>moduleid - This will be the id from module master,(not present now). Any external system sending data needs to set the value for this .
	  * If value not set will take as null (optional)
	  * <p>divisionid - This is the fieldid from the boundary master (optional)
	  * <p>departmentid - This is the departmentid from the department master (optional)
	  * <p>fundsourceid - This is the fundsourceid from the fund source master (optional)
	  * <p>schemeid - This is the schemeid from the scheme master (optional)
	  * <p>subschemeid - This is the subschemeid from the subscheme master (optional)
	  * <p>status - This is the status of voucher . If not set will be taken care (optional)
	  * <p>originalvoucher - This is the reference voucherid in care of reversal voucher (optional)
	  * <p>refvoucher - This the reference voucherheaderid in case of dependency vouchers (optional)
	  * <p>vouchersubtype - This is the sub voucher type in case of Journals where there are various kinds (optional)
	  * <p>budgetCheckReq -Boolean- This is a flag set in vouchermis and decided whether budget check should be done or not . 
	  * 'true' is yes 'false' is no . Default is 'true' (optional).
	  * <p>billid-this is the bill number for which voucher is getting created . (optional)
	  * <p>
	  * @param accountcodedetails
	  * <p>HashMap<String, Object> accountcodedetails will have data required for the ledger details
	  * <p>glcodeid - This the ledger codeid from the chartofaccounts master. (mandatory)
	  * <p>debitamount - This is the debit amount for that voucher (optional)
	  * <P>creditamount - This is the credit amount for that voucher (optional)
	  * <p>functionid - This is the functionid from the function master (optional)
	  * <p>narration -This is the narration if any for that account code (optional)
	  * <p>
	  *
	  *  <p>
	  * @param subledgerdetails
	  * <p>HashMap<String, Object> subledgerdetails will have the subledger details only for all the control codes in the voucher.
	  * <p>glcodeid -This the ledger codeid from the chartofaccounts master. (mandatory)
	  * <p>detailtypeid - This is the detailtypeid from the detailtype object (mandatory)
	  * <p>detailkeyid - This is the detailkey from the detailkey object (mandatory)
	  * <detailamount> - This is the amount for that detailkey (mandatory)
	  * <tdsid> - This is the id from the recovery master.If the glcode used is mapped in the recovery master then this data is mandatory.
	  * @return voucherheader object in case of success and null in case of fail.
	  * @throws EGOVRuntimeException
	  */
	 public CVoucherHeader createVoucher(HashMap<String, Object> headerdetails,List<HashMap<String,Object>> accountcodedetails,List<HashMap<String,Object>> subledgerdetails)throws EGOVRuntimeException {
		 CVoucherHeader vh;
		 Vouchermis mis;
		 Connection conn ;
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("start | createVoucher API");
		 try{
			 validateMandateFields(headerdetails);
			 validateLength(headerdetails);
			 validateVoucherMIS(headerdetails);
			 validateTransaction(accountcodedetails, subledgerdetails);
			 validateFunction(headerdetails,accountcodedetails);
			 vh = createVoucherHeader(headerdetails);
			 mis = createVouchermis(headerdetails);
			 mis.setVoucherheaderid(vh);
			 vh.setVouchermis(mis);
			 insertIntoVoucherHeader(vh);
			 insertIntoRecordStatus(vh);
			 List<Transaxtion> transactions = createTransaction(headerdetails,accountcodedetails,subledgerdetails,vh);
			HibernateUtil.getCurrentSession().flush();
			 ChartOfAccounts engine=ChartOfAccounts.getInstance();
			 Transaxtion txnList[]=new Transaxtion[transactions.size()];
			 txnList=(Transaxtion[])transactions.toArray(txnList);
			 SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
			 if(!engine.postTransaxtions(txnList, null,formatter.format(vh.getVoucherDate())))
			 {
				 throw new EGOVRuntimeException("Voucher creation Failed");
			 }
		 }

		 catch (ValidationException ve)
		 {
			 LOGGER.error(ERR,ve);
			 throw ve;
		 }
		 catch (Exception e) {
			 LOGGER.error(ERR,e);
			 throw new EGOVRuntimeException(e.getMessage());
		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("End | createVoucher API");
		 return vh;

	 }
	 /**
	  * 
	  * @param headerdetails
	  * @param accountcodedetails
	  * Validates based on Single Function for a transaction
	  * 
	  */
	 private void validateFunction(HashMap<String, Object> headerdetails,List<HashMap<String, Object>> accountcodedetails) {
		 PersistenceService persistenceService=new PersistenceService();
		// persistenceService.setSessionFactory(new SessionFactory());

		 AppConfigValues appConfigValues = (AppConfigValues) persistenceService.find("from AppConfigValues where key in " +
				 "(select id from AppConfig where key_name='ifRestrictedToOneFunctionCenter' and module='EGF' )");
		 if(appConfigValues==null)
		 {
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("app config ifRestrictedToOneFunctionCenter is not defined");
			 throw new ValidationException("Error","Use Single Function For a transaction is not defined");
		 }
		 else if(appConfigValues.getValue().equalsIgnoreCase("No"))
		 { 
			 
			 //Keep last two lines when making single function mandatory.
			 // Now this will support both. 
			 // If function is added in header it will consider it for all details else
			 // it will use from detail.
			 boolean foundInHeader=false;
			 boolean foundInDetails=false;
			 boolean atLeastOneMissing=false;
			 Map functionMap=new HashMap<String,String>();// to find duplicates. finally we should have one and only entry in this
			 String functionCodeInHeader="";
			 String functionCodeInDetail="";
		
			 //1. check if function is passed in header
			 if(null!=headerdetails.get(VoucherConstant.FUNCTIONCODE))
			 {

				 functionCodeInHeader = (String)headerdetails.get(VoucherConstant.FUNCTIONCODE);
				 functionMap.put(functionCodeInHeader, functionCodeInHeader);
				 foundInHeader=true;
			 }
			 if(foundInHeader)
			 {
				 for (HashMap<String, Object> accDetailMap : accountcodedetails) 
				 {
						 accDetailMap.put(VoucherConstant.FUNCTIONCODE,(String)headerdetails.get(VoucherConstant.FUNCTIONCODE));
				 }
			 }
			 
			 
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Not a single Function Transaction  No need of Further check on function");
			 return;
		 }
		 else if(appConfigValues.getValue().equalsIgnoreCase("Yes"))
		 {   
			 boolean foundInHeader=false;
			 boolean foundInDetails=false;
			 boolean atLeastOneMissing=false;
			 Map functionMap=new HashMap<String,String>();// to find duplicates. finally we should have one and only entry in this
			 String functionCodeInHeader="";
			 String functionCodeInDetail="";
		
			 //1. check if function is passed in header
			 if(null!=headerdetails.get(VoucherConstant.FUNCTIONCODE))
			 {

				 functionCodeInHeader = (String)headerdetails.get(VoucherConstant.FUNCTIONCODE);
				 functionMap.put(functionCodeInHeader, functionCodeInHeader);
				 foundInHeader=true;
			 }

		// 2. Check if function is passed in Details  
			 for (HashMap<String, Object> accDetailMap : accountcodedetails) 
			 {
				 if(null != accDetailMap.get(VoucherConstant.FUNCTIONCODE) && "" != accDetailMap.get(VoucherConstant.FUNCTIONCODE))
				 {
					 functionCodeInDetail = accDetailMap.get(VoucherConstant.FUNCTIONCODE).toString();
					 functionMap.put(functionCodeInDetail, functionCodeInDetail);
					 foundInDetails=true;
				 }else
				 {
					 //Mark if Function is missing in detail level
					 atLeastOneMissing=true;
				 }
				 
				 
			 } 
			 //If no function is passed or more than one passed throw error
			 if(functionMap.size()>1)
			 {
				 LOGGER.error("multiple functions found in Header and details Cannot Continue Transaction....");
				 throw new ValidationException(Arrays.asList(new ValidationError("multiple functions found in Header and details","multiple.functions.found.in.Header.and.details")));
			 }
			 // commented by Shamili - functionMap.isEmpty() needs to be validated when function is mandatory
              // Uncommenting since one function center is mandatory
			 if(functionMap.isEmpty())
			 {
				 LOGGER.error("Function not found in Header or details Cannot Continue Transaction....");
				 throw new ValidationException(Arrays.asList(new ValidationError("function not found in Header or details","function.not.found.in.Header.or.details")));
			 }      
			 // since we passed the above check, there is one function.If it is not in header then it must be from detail
			 if(!foundInHeader)
			 {
				 headerdetails.put(VoucherConstant.FUNCTIONCODE,functionCodeInDetail);
			 }
            // Now fetch it from the header which we have populated
			 if(atLeastOneMissing)
			 {
				 for (HashMap<String, Object> accDetailMap : accountcodedetails) 
				 {
					 if(null == accDetailMap.get(VoucherConstant.FUNCTIONCODE) || "" == accDetailMap.get(VoucherConstant.FUNCTIONCODE))
					 {
						 accDetailMap.put(VoucherConstant.FUNCTIONCODE,(String)headerdetails.get(VoucherConstant.FUNCTIONCODE));
					 } 
				 }
			 } 
		 }


	 }
	/**
	 * @param headerdetails
	 */
	private void validateLength(HashMap<String, Object> headerdetails) {
		if(headerdetails.get(VoucherConstant.DESCRIPTION)!=null && headerdetails.get(VoucherConstant.DESCRIPTION).toString().length()>250)
		{
			 throw new ValidationException(Arrays.asList(new ValidationError("voucher.description.exceeds.max.length","Narration exceeds maximum length")));
		}
		 String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes(headerdetails.get(VoucherConstant.VOUCHERTYPE).toString());
		 if(vNumGenMode!="Auto" && headerdetails.get(VoucherConstant.VOUCHERNUMBER)!=null )
		 {
			 int typeLength=Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH);
			 int voucherNumberColumnLength=30;//length in the db
			 int fundIdentfierLength=1;
			 if(headerdetails.get(VoucherConstant.VOUCHERNUMBER).toString().length()>voucherNumberColumnLength-(typeLength+fundIdentfierLength))
			 {
				 String voucheNumberErrMsg=" VoucherNumber length should be lessthan "+(voucherNumberColumnLength-(typeLength+fundIdentfierLength));
				 throw new ValidationException(Arrays.asList(new ValidationError("voucher.number.exceeds.max.length",voucheNumberErrMsg)));
			 }
		 }


	}
	//used for reversal
	protected void insertIntoVoucherHeader(CVoucherHeader vh) throws EGOVRuntimeException{
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("start | insertIntoVoucherHeader");
		 Connection conn = null;
		 PersistenceService<CVoucherHeader, Long> cVoucherHeaderSer;
		 cVoucherHeaderSer = new PersistenceService<CVoucherHeader, Long>();
		 //cVoucherHeaderSer.setSessionFactory(new SessionFactory());
		 cVoucherHeaderSer.setType(CVoucherHeader.class);

			 String vdt = formatter.format(vh.getVoucherDate());
			 String fiscalPeriod=null;
			 try {
				fiscalPeriod=cm.getFiscalPeriod(vdt, conn);
			} catch (TaskFailedException e) {
				throw new EGOVRuntimeException("error while getting fiscal period");
			}
			 if(null == fiscalPeriod){
				 throw new EGOVRuntimeException("Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "+ fiscalPeriod);
			 }
			 vh.setFiscalPeriodId(Integer.valueOf(fiscalPeriod));
			 String cgn=getCgnType(vh.getType())+cm.getCGNumber();
			if(LOGGER.isDebugEnabled())     LOGGER.debug("cgn------"+cgn);
			 vh.setCgn(cgn);
			 vh.setCgDate(new Date());
			 String fundIdentifier =vh.getFundId().getIdentifier().toString(); 
			 String vType=fundIdentifier+"/"+getCgnType(vh.getType())+"/CGVN";
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("vType"+vType);
			 String eg_voucher=null;
			try {
				eg_voucher = cm.getEg_Voucher(vType,fiscalPeriod,conn);
			} catch (TaskFailedException e) {
				LOGGER.error(ERR,e);
				throw new EGOVRuntimeException(e.getMessage());
			} catch (SQLException e) {
				LOGGER.error(ERR,e);
				throw new EGOVRuntimeException(e.getMessage());
			}
			 for(int i=eg_voucher.length();i<10;i++)
			 {
				 eg_voucher="0"+eg_voucher;
			 }
			 String	cgNum=vType+eg_voucher;
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("cgNum------"+cgNum);
			 vh.setCgvn(cgNum);
			
			 try {
				if(!cm.isUniqueVN(vh.getVoucherNumber(),vdt, conn))
					 throw new ValidationException(Arrays.asList(new ValidationError("Duplicate Voucher Number","Duplicate Voucher Number")));
			} catch (SQLException e) {
				LOGGER.error(ERR,e);
				throw new 	EGOVRuntimeException(e.getMessage());
			} catch (TaskFailedException e) {
				LOGGER.error(ERR,e);
				throw new	EGOVRuntimeException(e.getMessage());
			}
			 vh.setCreatedBy(userMngr.getUserById(Long.valueOf(EGOVThreadLocals.getUserId())));
			 if(LOGGER.isInfoEnabled())     LOGGER.info("++++++++++++++++++"+vh.toString());
			 cVoucherHeaderSer.persist(vh);
			 if(null != vh.getVouchermis().getSourcePath() && null ==vh.getModuleId() && 
					 (vh.getVouchermis().getSourcePath().length() == vh.getVouchermis().getSourcePath().indexOf("=")+1)){
				 StringBuffer sourcePath = new StringBuffer();
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("Source Path received : " + vh.getVouchermis().getSourcePath());
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Header Id  : " + vh.getId());
				 sourcePath.append(vh.getVouchermis().getSourcePath()).append(vh.getId().toString());
				 vh.getVouchermis().setSourcePath(sourcePath.toString());
				 cVoucherHeaderSer.update(vh);
			 }


		 if(LOGGER.isDebugEnabled())     LOGGER.debug("End | insertIntoVoucherHeader");
	 }
	 protected String  getCgnType(String vType){
		 vType = vType.toUpperCase().replaceAll(" ", "");
		 String cgnType = null;
		 String typetoCheck=vType;
		 if (vType.equalsIgnoreCase("JOURNAL VOUCHER"))
			 typetoCheck="JOURNALVOUCHER";

		 switch (voucherTypeEnum.valueOf(typetoCheck.toUpperCase())) {
		 case JOURNALVOUCHER:
			 cgnType ="JVG";
			 break;
		 case CONTRA:
			 cgnType ="CSL";
			 break;
		 case RECEIPT:
			 cgnType ="MSR";
			 break;
		 case PAYMENT:
			 cgnType="DBP";
			 break;
		 }
		 return cgnType;
	 }
	 protected void insertIntoRecordStatus(final CVoucherHeader voucherHeader){

		 EgfRecordStatus recordStatus = new EgfRecordStatus();
		 PersistenceService<EgfRecordStatus, Long> recordStatusSer;
		 recordStatusSer = new PersistenceService<EgfRecordStatus, Long>();
		// recordStatusSer.setSessionFactory(new SessionFactory());
		 recordStatusSer.setType(EgfRecordStatus.class);
		 String code=EGovConfig.getProperty("egf_config.xml","confirmoncreate","",voucherHeader.getType());
		 if("N".equalsIgnoreCase(code)){
			 recordStatus.setStatus(Integer.valueOf(1));
		 }else {
			 recordStatus.setStatus(Integer.valueOf(0));
		 }
		 recordStatus.setUpdatedtime(new Date());
		 recordStatus.setVoucherheader(voucherHeader);
		 recordStatus.setRecordType(voucherHeader.getType());
		 recordStatus.setUserid(Integer.parseInt(EGOVThreadLocals.getUserId()));
		 recordStatusSer.persist(recordStatus);
	 }
	 /**
	  * This method will validate all the master data that are passed.
	  * This will also check if the data send are correct with respect to the inter master dependency.
	  * @param headerdetails
	  * @throws EGOVRuntimeException
	  */
	 public void validateVoucherMIS(final HashMap<String, Object> headerdetails) throws EGOVRuntimeException{
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("START | validateVoucherMIS");
		 //Validate Department.
		 Department dept = null;
		 if(headerdetails.containsKey(VoucherConstant.DEPARTMENTCODE) && null != headerdetails.get(VoucherConstant.DEPARTMENTCODE)){
			 dept= deptM.getDepartmentByCode(headerdetails.get(VoucherConstant.DEPARTMENTCODE).toString());
			 if(dept == null)
				 throw new EGOVRuntimeException("not a valid Department");
		 }

		 if(null != headerdetails.get(VoucherConstant.FUNCTIONARYCODE)){
			 Functionary functionary =  comm.getFunctionaryByCode(BigDecimal.valueOf(Long.
					 valueOf(headerdetails.get(VoucherConstant.FUNCTIONARYCODE).toString())));
			 if(null == functionary){
				 throw new EGOVRuntimeException("not a valid functionary");
			 }
		 }
		 //validate fund.
		 String fundCode=null;
		 Fund fund= null;
		 if(headerdetails.containsKey(VoucherConstant.FUNDCODE) && null != headerdetails.get(VoucherConstant.FUNDCODE)){
			 fundCode = headerdetails.get(VoucherConstant.FUNDCODE).toString();
			 fund = comm.fundByCode(fundCode);
			 if(null == fund){
				 throw new EGOVRuntimeException("not a valid fund");
			 }
		 }else{
			 throw new EGOVRuntimeException("fund value is missing");
		 }
		 //validate Scheme
		 Scheme scheme = null;
		 if(headerdetails.containsKey(VoucherConstant.SCHEMECODE) && null != headerdetails.get(VoucherConstant.SCHEMECODE)){
			 String schemecode = headerdetails.get(VoucherConstant.SCHEMECODE).toString();
			 scheme = comm.schemeByCode(schemecode);
			 if(null == scheme){
				 throw new EGOVRuntimeException("not a valid scheme");
			 }
			 if(! (fund.getId().equals(scheme.getFund().getId()))){
				 throw new EGOVRuntimeException("This scheme does not belong to this fund");
			 }
		 }
		 // validate subscheme
		 SubScheme subScheme = null;
		 if(headerdetails.containsKey(VoucherConstant.SUBSCHEMECODE)&& null != headerdetails.get(VoucherConstant.SUBSCHEMECODE)){
			 String subSchemeCode =  headerdetails.get(VoucherConstant.SUBSCHEMECODE).toString();
			 subScheme = comm.getSubSchemeByCode(subSchemeCode);
			 if(null == subScheme){
				 throw new EGOVRuntimeException("not a valid subscheme");
			 }
			 if( !(subScheme.getScheme().getId().equals(scheme.getId()))){
				 throw new EGOVRuntimeException("This subscheme does not belong to this scheme");
			 }
		 }
		 // validate fundsource
		 if(headerdetails.containsKey(VoucherConstant.FUNDSOURCECODE) && null != headerdetails.get(VoucherConstant.FUNDSOURCECODE)){
			 Fundsource fundsource =  comm.getFundSourceByCode(headerdetails.get(VoucherConstant.FUNDSOURCECODE).toString());
			 if(null == fundsource){
				 throw new EGOVRuntimeException("not a valid fund source");
			 }
		 }

		 if(headerdetails.containsKey(VoucherConstant.DIVISIONID) && null != headerdetails.get(VoucherConstant.DIVISIONID)){

			 if(null == boundary.getBoundaryById(Long.parseLong(headerdetails.get(VoucherConstant.DIVISIONID).toString()))){
				 throw new EGOVRuntimeException("not a valid divisionid");
			 }
		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("END | validateVoucherMIS");
	 }
	 public void validateMandateFields(final HashMap<String, Object> headerdetails){

		 	getHeaderMandateFields();
		 	if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside Validate Method");
			checkMandatoryField("vouchernumber",headerdetails.get(VoucherConstant.VOUCHERNUMBER),headerdetails);
			checkMandatoryField("voucherdate",headerdetails.get(VoucherConstant.VOUCHERDATE),headerdetails);
			checkMandatoryField("fund",headerdetails.get(VoucherConstant.FUNDCODE),headerdetails);
			checkMandatoryField("department",headerdetails.get(VoucherConstant.DEPARTMENTCODE),headerdetails);
			checkMandatoryField("scheme",headerdetails.get(VoucherConstant.SCHEMECODE),headerdetails);
			checkMandatoryField("subscheme",headerdetails.get(VoucherConstant.SUBSCHEMECODE),headerdetails);
			checkMandatoryField("functionary",headerdetails.get(VoucherConstant.FUNCTIONARYCODE),headerdetails);
			//checkMandatoryField("function",headerdetails.get(VoucherConstant.FUNCTIONCODE),headerdetails);
			checkMandatoryField("fundsource",headerdetails.get(VoucherConstant.FUNDSOURCECODE),headerdetails);
			checkMandatoryField("field",headerdetails.get(VoucherConstant.DIVISIONID),headerdetails);
   
		 }
	 private void validateVoucherType(String voucherType) {
		 voucherType = voucherType.toUpperCase().replaceAll(" ", "");
		 boolean typeFound=false;
		 voucherTypeEnum[] allvoucherTypeEnum=voucherTypeEnum.values();
		 for (voucherTypeEnum voucherTypeEnum : allvoucherTypeEnum) {
			 if(voucherTypeEnum.toString().equalsIgnoreCase(voucherType)){
				 typeFound=true;break;
			 }
		 }
		 if(!typeFound)throw new EGOVRuntimeException("Voucher type is not valid");
	 }
	 @SuppressWarnings("deprecation")
	 public  CVoucherHeader createVoucherHeader(final HashMap<String, Object> headerdetails) throws EGOVRuntimeException,Exception{
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("START | createVoucherHeader");
		 //Connection con = null;
		 Query query=null;
		 CVoucherHeader cVoucherHeader = new CVoucherHeader();
		 try{             
			 //String voucherSubType="";
			 cVoucherHeader.setName(headerdetails.get(VoucherConstant.VOUCHERNAME).toString());
			 String voucherType = headerdetails.get(VoucherConstant.VOUCHERTYPE).toString();
			 cVoucherHeader.setType(headerdetails.get(VoucherConstant.VOUCHERTYPE).toString());
			 String  vNumGenMode= null;
			 
			 //-- Voucher Type checking. --START
			 if(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(voucherType) ) {
					vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Journal");
			 } else {
					 vNumGenMode= new VoucherTypeForULB().readVoucherTypes(voucherType);
			 }
			 //--END --
			 voucherType = voucherType.toUpperCase().replaceAll(" ", "");
			
			 String voucherSubType=null;
			 if(headerdetails.get(VoucherConstant.VOUCHERSUBTYPE)!=null)
			 {
				 voucherSubType=(String)headerdetails.get(VoucherConstant.VOUCHERSUBTYPE);
				 voucherSubType =voucherSubType.toUpperCase().replaceAll(" ", "");   
			 }
			
			               
			 //why it is type,subtype where api expects subtype,type ?
			 //if()
			 String voucherNumberPrefix = getVoucherNumberPrefix(voucherType,voucherSubType);
			 String voucherNumber = null;
			 if(headerdetails.get(VoucherConstant.DESCRIPTION)!=null)
			 cVoucherHeader.setDescription(headerdetails.get(VoucherConstant.DESCRIPTION).toString());
			 Date voucherDate = (Date)(headerdetails.get(VoucherConstant.VOUCHERDATE));
			 cVoucherHeader.setVoucherDate(voucherDate);
			  fundByCode = comm.fundByCode((headerdetails.get(VoucherConstant.FUNDCODE)).toString());

			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Type is :"+voucherType);
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("vNumGenMode is  :"+vNumGenMode);
               
			 if(headerdetails.get(VoucherConstant.VOUCHERNUMBER) != null )
				 voucherNumber = headerdetails.get(VoucherConstant.VOUCHERNUMBER).toString();
			 if( null !=  headerdetails.get(VoucherConstant.MODULEID) ) vNumGenMode = "Auto";
			 
			 String strVoucherNumber = VoucherHelper.getGeneratedVoucherNumber(fundByCode.getId(), voucherNumberPrefix, 
					 voucherDate, vNumGenMode, voucherNumber);
			 cVoucherHeader.setVoucherNumber(strVoucherNumber);

			 /*
			 if("Auto".equalsIgnoreCase(vNumGenMode) || null !=  headerdetails.get(VoucherConstant.MODULEID)){
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("Generating auto voucher number");
				 SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
				 String vDate = df.format(voucherDate);
				 cVoucherHeader.setVoucherNumber(cmImpl.getTxnNumber(fundId.toString(),voucherNumberPrefix,vDate,con));
			 }else {
				 voucherNumber = headerdetails.get(VoucherConstant.VOUCHERNUMBER).toString();
				 query=HibernateUtil.getCurrentSession().createQuery("select f.identifier from Fund f where id=:fundId");
				 query.setInteger("fundId", fundId);
				 String fundIdentifier = query.uniqueResult().toString();
				 cVoucherHeader.setVoucherNumber(new StringBuffer().append(fundIdentifier).append(voucherNumberPrefix).
						 append(voucherNumber).toString());

			 }*/

			 cVoucherHeader.setFundId(fundByCode);
			 if(headerdetails.containsKey(VoucherConstant.MODULEID) && null != headerdetails.get(VoucherConstant.MODULEID)){
				 cVoucherHeader.setModuleId(Integer.valueOf(headerdetails.get(VoucherConstant.MODULEID).toString()));
				 cVoucherHeader.setIsConfirmed(Integer.valueOf(1));
			 }else{
				 //Fix Me
				/* PersistenceService<AppConfig, Integer> appConfigSer;
				 appConfigSer = new PersistenceService<AppConfig, Integer>();
				 appConfigSer.setSessionFactory(new SessionFactory());
				 appConfigSer.setType(AppConfig.class);
				 AppConfig appConfig= (AppConfig) appConfigSer.find("from AppConfig where key_name =?", "JournalVoucher_ConfirmonCreate");
				 if(null != appConfig && null!= appConfig.getAppDataValues() ){
					 for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
						 cVoucherHeader.setIsConfirmed(Integer.valueOf(appConfigVal.getValue()));
					}
				 }*/
			 }


			 if(headerdetails.containsKey(VoucherConstant.DEPARTMENTCODE) && null != headerdetails.get(VoucherConstant.DEPARTMENTCODE)){
				 String departmentCode = headerdetails.get(VoucherConstant.DEPARTMENTCODE).toString();
				 cVoucherHeader.setDepartmentId(deptM.getDepartmentByCode(departmentCode).getId().intValue());
			 }
			 
			 if(headerdetails.containsKey(VoucherConstant.FUNDSOURCECODE) && null != headerdetails.get(VoucherConstant.FUNDSOURCECODE)){
				 String fundsourcecode = headerdetails.get(VoucherConstant.FUNDSOURCECODE).toString();
				 cVoucherHeader.setFundsourceId(comm.getFundSourceByCode(fundsourcecode));
			 }              
			 if(headerdetails.containsKey(VoucherConstant.STATUS) && null != headerdetails.get(VoucherConstant.STATUS)){
				 cVoucherHeader.setStatus(Integer.valueOf(headerdetails.get(VoucherConstant.STATUS).toString()));

			 }else{
				//This fix is for Phoenix Migration.
				 List list = null;//new GenericHibernateDaoFactory().getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", "DEFAULTVOUCHERCREATIONSTATUS");
				 cVoucherHeader.setStatus(Integer.parseInt(((AppConfigValues)list.get(0)).getValue()));
			 }

			 if(null != headerdetails.get(VoucherConstant.ORIGIONALVOUCHER)){

				 final Long origionalVId = Long.parseLong(headerdetails.get(VoucherConstant.ORIGIONALVOUCHER).toString());
				 query =HibernateUtil.getCurrentSession().createQuery("from CVoucherHeader where id=:id");
				 query.setLong("id", origionalVId);
				 if(query.list().size() == 0){
					 throw new EGOVRuntimeException("Not a valid origional voucherheader id");
				 }else{
					 cVoucherHeader.setOriginalvcId(origionalVId);
				 }
			 }

			 cVoucherHeader.setRefcgNo((String)headerdetails.get(VoucherConstant.REFVOUCHER));
			 cVoucherHeader.setEffectiveDate(new Date());
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Printing Voucher Details------------------------------------------------------------------------------");
			 if(LOGGER.isDebugEnabled())     LOGGER.debug(cVoucherHeader.toString());
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Printing Voucher Details------------------------------------------------------------------------------");
		 }catch(ValidationException e){
			LOGGER.error(e.getMessage());
			 throw e;
		 }
		 catch(Exception e){
			 LOGGER.error(e);
			 throw new Exception(e.getMessage());

		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("END | createVoucherHeader");
		 return cVoucherHeader;
		 }	 
	 enum voucherTypeEnum {
		 JOURNALVOUCHER, CONTRA, RECEIPT,PAYMENT;
	 }
	 enum voucherSubTypeEnum {
		 JOURNALVOUCHER, CONTRA, RECEIPT,PAYMENT,PURCHASEJOURNAL,PENSIONJOURNAL,PURCHASE,WORKS,CONTRACTORJOURNAL,FIXEDASSETJOURNAL,FIXEDASSET,PENSION,WORKSJOURNAL,CONTINGENTJOURNAL,SALARY,SALARYJOURNAL,EXPENSE,EXPENSEJOURNAL,JVGENERAL;
	 }
	// we cannot provide enum for all names so we need to find a way
	 //or code it for all standard type like CJV,SJV,PJV,EJV
	 
	 private String getVoucherNumberPrefix(String type,String subtype){

		 //if sub type is null use type
		 if(subtype==null)    
		 {                    
			 subtype=type;
		 }
		 subtype = subtype.toUpperCase().trim();
		 String voucherNumberPrefix = null;
		 String typetoCheck=subtype;
		 
		 if (subtype.equalsIgnoreCase("JOURNAL VOUCHER"))
			 typetoCheck="JOURNALVOUCHER";

		 switch (voucherSubTypeEnum.valueOf(typetoCheck)) {
		 case JVGENERAL:
			 voucherNumberPrefix =FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
			 break;
		 case JOURNALVOUCHER:
			 voucherNumberPrefix =FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
			 break;
		 case CONTRA:
			 voucherNumberPrefix =FinancialConstants.CONTRA_VOUCHERNO_TYPE;
			 break;
		 case RECEIPT:
			 voucherNumberPrefix =FinancialConstants.RECEIPT_VOUCHERNO_TYPE;
			 break;
		 case PAYMENT:   
			 voucherNumberPrefix =FinancialConstants.PAYMENT_VOUCHERNO_TYPE;
			 break;
		 case PURCHASEJOURNAL:
			 voucherNumberPrefix =FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
			 break;
		 case WORKS:
			 voucherNumberPrefix =FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
		 case CONTRACTORJOURNAL:
			 voucherNumberPrefix =FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
		 case WORKSJOURNAL:                
			 voucherNumberPrefix =FinancialConstants.WORKSBILL_VOUCHERNO_TYPE;
			 break;
		 case FIXEDASSETJOURNAL:
			 voucherNumberPrefix =FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
			 break;
		 case CONTINGENTJOURNAL:
			 voucherNumberPrefix =FinancialConstants.CBILL_VOUCHERNO_TYPE;
			 break;
		 case PURCHASE:
			 voucherNumberPrefix =FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
			 break;
		 case EXPENSEJOURNAL:
			 voucherNumberPrefix =FinancialConstants.CBILL_VOUCHERNO_TYPE;
			 break;
		 case EXPENSE:
			 voucherNumberPrefix =FinancialConstants.CBILL_VOUCHERNO_TYPE;
			 break;
		 case SALARYJOURNAL:
			 voucherNumberPrefix =FinancialConstants.SALBILL_VOUCHERNO_TYPE;
		 case SALARY:    
			 voucherNumberPrefix =FinancialConstants.SALBILL_VOUCHERNO_TYPE;
			 break;
		 case FIXEDASSET:
			 voucherNumberPrefix =FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
			 break;
		 case PENSIONJOURNAL:
			 voucherNumberPrefix =FinancialConstants.PENBILL_VOUCHERNO_TYPE;
		 case PENSION:
			 voucherNumberPrefix =FinancialConstants.PENBILL_VOUCHERNO_TYPE;
			 break;
		default:
					{//if subtype is invalid then  use type
						if(voucherNumberPrefix==null)
							voucherNumberPrefix=checkwithvouchertype(type);
					}
		 }
		 return voucherNumberPrefix;

	 }
	 /**
	 *
	 */
	 private String checkwithvouchertype(String type) {
		 String typetoCheck=type;
		 if (type.equalsIgnoreCase("JOURNAL VOUCHER"))
			 	 typetoCheck="JOURNALVOUCHER";
		 String voucherNumberPrefix=null;
		 switch (voucherTypeEnum.valueOf(typetoCheck)) {
		 case JOURNALVOUCHER:
			 voucherNumberPrefix =FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
			 break;
		 case CONTRA:
			 voucherNumberPrefix =FinancialConstants.CONTRA_VOUCHERNO_TYPE;
			 break;
		 case RECEIPT:
			 voucherNumberPrefix =FinancialConstants.RECEIPT_VOUCHERNO_TYPE;
			 break;
		 case PAYMENT:
			 voucherNumberPrefix =FinancialConstants.PAYMENT_VOUCHERNO_TYPE;
			 break;
		 }
		 return voucherNumberPrefix;

	 }
	public  Vouchermis createVouchermis(final HashMap<String, Object> headerdetails) throws EGOVRuntimeException{
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("START | createVouchermis");
		 Vouchermis vouchermis= new Vouchermis();
		 if(headerdetails.containsKey(VoucherConstant.DEPARTMENTCODE) && null != headerdetails.get(VoucherConstant.DEPARTMENTCODE)){
			 String departmentCode = headerdetails.get(VoucherConstant.DEPARTMENTCODE).toString();
			 vouchermis.setDepartmentid((Department)deptM.getDepartmentByCode(departmentCode));
		 }
		 if(headerdetails.containsKey(VoucherConstant.SCHEMECODE) && null != headerdetails.get(VoucherConstant.SCHEMECODE)){
			 String schemecode = headerdetails.get(VoucherConstant.SCHEMECODE).toString();
			 vouchermis.setSchemeid((comm.schemeByCode(schemecode)));
		 }

		 if(headerdetails.containsKey(VoucherConstant.SUBSCHEMECODE) && null != headerdetails.get(VoucherConstant.SUBSCHEMECODE)){
			 String subschemecode = headerdetails.get(VoucherConstant.SUBSCHEMECODE).toString();
			 vouchermis.setSubschemeid(comm.getSubSchemeByCode(subschemecode));
		 }

		 if(headerdetails.containsKey(VoucherConstant.FUNDSOURCECODE) && null != headerdetails.get(VoucherConstant.FUNDSOURCECODE)){
			 String fundsourcecode = headerdetails.get(VoucherConstant.FUNDSOURCECODE).toString();
			 vouchermis.setFundsource(comm.getFundSourceByCode(fundsourcecode));
		 }
		 if(null != headerdetails.get(VoucherConstant.FUNCTIONARYCODE)){
			 vouchermis.setFunctionary(comm.getFunctionaryByCode(BigDecimal.valueOf(Long.
					 valueOf(headerdetails.get(VoucherConstant.FUNCTIONARYCODE).toString()))));

		 }
		 if(headerdetails.containsKey(VoucherConstant.FUNCTIONCODE) && null != headerdetails.get(VoucherConstant.FUNCTIONCODE)){
			 String functionCode = headerdetails.get(VoucherConstant.FUNCTIONCODE).toString();
			 vouchermis.setFunction(comm.getFunctionByCode(functionCode));
		 }
		 
		 if( null != headerdetails.get(VoucherConstant.SOURCEPATH)){
			 vouchermis.setSourcePath(headerdetails.get(VoucherConstant.SOURCEPATH).toString());
		 }
		 if(headerdetails.containsKey(VoucherConstant.DIVISIONID) && null != headerdetails.get(VoucherConstant.DIVISIONID)){
			 vouchermis.setDivisionid((Boundary)boundary.getBoundaryById(Long.parseLong(headerdetails.get(VoucherConstant.DIVISIONID).toString())));
		 }
		 if(headerdetails.containsKey(VoucherConstant.BUDGETCHECKREQ) && null != headerdetails.get(VoucherConstant.BUDGETCHECKREQ)){
			 vouchermis.setBudgetCheckReq((Boolean)headerdetails.get(VoucherConstant.BUDGETCHECKREQ));
		 }else
		 {
			 vouchermis.setBudgetCheckReq(true);
		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("END | createVouchermis");
		 return vouchermis;
	 }


	 public void validateTransaction(List<HashMap<String,Object>> accountcodedetails,List<HashMap<String,Object>> subledgerdetails)throws EGOVRuntimeException,Exception{
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("START | validateTransaction");
		 // List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
		 BigDecimal totaldebitAmount = BigDecimal.valueOf(0);
		 BigDecimal totalcreditAmount = BigDecimal.valueOf(0);
		 Map<String, BigDecimal> accDetAmtMap = new HashMap<String, BigDecimal>();
		 for (HashMap<String, Object> accDetailMap : accountcodedetails) {

			 String glcode=null;

			 BigDecimal debitAmount = new BigDecimal(accDetailMap.get(VoucherConstant.DEBITAMOUNT).toString());
			 BigDecimal creditAmount = new BigDecimal(accDetailMap.get(VoucherConstant.CREDITAMOUNT).toString());

			 totaldebitAmount = totaldebitAmount.add(debitAmount);
			 totalcreditAmount = totalcreditAmount.add(creditAmount);
			 if(accDetailMap.containsKey(VoucherConstant.GLCODE) && null != accDetailMap.get(VoucherConstant.GLCODE)){
				 glcode = accDetailMap.get(VoucherConstant.GLCODE).toString();
				 if(null == comm.getCChartOfAccountsByGlCode(glcode)){
					 throw new EGOVRuntimeException("Not a valid account code"+glcode);
				 }
			 }else{
				 throw new EGOVRuntimeException("glcode is missing or null");
			 }
			 if(!debitAmount.equals(BigDecimal.valueOf(0)) && !creditAmount.equals(BigDecimal.valueOf(0))){
				 throw new EGOVRuntimeException("Both debit amount and credit amount cannot be greater than zero");
			 }
			 if(debitAmount.equals(BigDecimal.valueOf(0))  && creditAmount.equals(BigDecimal.valueOf(0))){
				 throw new EGOVRuntimeException("debit and credit both amount is Zero");
			 }
			 if(null != accDetailMap.get(VoucherConstant.FUNCTIONCODE) && "" != accDetailMap.get(VoucherConstant.FUNCTIONCODE)){
				 String functionCode = accDetailMap.get(VoucherConstant.FUNCTIONCODE).toString();
				 if(null == comm.getFunctionByCode(functionCode)){
					 throw new EGOVRuntimeException("not a valid function code");
				 }
			 }
			 if(!debitAmount.equals(BigDecimal.valueOf(0))){
				 if(null!=accDetAmtMap.get(VoucherConstant.DEBIT+glcode)){
					 BigDecimal accountCodeTotDbAmt  =accDetAmtMap.get(VoucherConstant.DEBIT+glcode).add(debitAmount);
					 accDetAmtMap.put(VoucherConstant.DEBIT+glcode,accountCodeTotDbAmt);
				 }else {
					 accDetAmtMap.put(VoucherConstant.DEBIT+glcode,debitAmount);
				 }

			 }else if(!creditAmount.equals(BigDecimal.valueOf(0))){
				 if(null!=accDetAmtMap.get(VoucherConstant.CREDIT+glcode)){
					 BigDecimal accountCodeTotCrAmt  =accDetAmtMap.get(VoucherConstant.CREDIT+glcode).add(creditAmount);
					 accDetAmtMap.put(VoucherConstant.CREDIT+glcode,accountCodeTotCrAmt);
				 }else {
					 accDetAmtMap.put(VoucherConstant.CREDIT+glcode,creditAmount);
				 }

			 }
		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Total Debit  amount   :"+ totaldebitAmount);
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Total Credit amount   :"+ totalcreditAmount);
		 totaldebitAmount  =  totaldebitAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		 totalcreditAmount = totalcreditAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Total Debit  amount after round off :"+ totaldebitAmount);
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Total Credit amount after round off :"+ totalcreditAmount);
		 if(totaldebitAmount.compareTo(totalcreditAmount)!=0){
			 throw new EGOVRuntimeException("total debit and total credit amount is not matching");
		 }
		 Map<String,BigDecimal> subledAmtmap = new HashMap<String,BigDecimal> ();
		 for (HashMap<String, Object> subdetailDetailMap : subledgerdetails) {
			 String glcode = null;
			 String detailtypeid = null;
			 String detailKeyId = null;
			 if(null != subdetailDetailMap.get(VoucherConstant.GLCODE)){
				 glcode = subdetailDetailMap.get(VoucherConstant.GLCODE).toString();
				 if(null == comm.getCChartOfAccountsByGlCode(glcode)){
					 throw new EGOVRuntimeException("not a valid glcode");
				 }
			 }else{
				 throw new EGOVRuntimeException("glcode is missing");
			 }
			 Query querytds =HibernateUtil.getCurrentSession().createQuery("select t.id from Recovery t where " +
			 "t.chartofaccounts.glcode=:glcode");
			 querytds.setString("glcode", glcode);
			 querytds.setCacheable(true);
			 if(null !=querytds.list() &&  querytds.list().size()>0 && null== subdetailDetailMap.get(VoucherConstant.TDSID)&&
				 null != subdetailDetailMap.get(VoucherConstant.CREDITAMOUNT ) &&  ! new BigDecimal((subdetailDetailMap.get(VoucherConstant.CREDITAMOUNT).toString())).equals(BigDecimal.ZERO)){
				/* Commenting out throw EGOVRuntimeException since we are using the same API for create Journal Voucher. 
				 * There we are not setting the TDS id.. 
				 */
				 //throw new EGOVRuntimeException("Recovery detail is missing for glcode :"+glcode);
			 }
			 // validate the glcode is a subledger code or not.

			 Query query =HibernateUtil.getCurrentSession().createQuery("from CChartOfAccountDetail cd,CChartOfAccounts c where " +
			 "cd.glCodeId = c.id and c.glcode=:glcode");
			 
			 query.setString(VoucherConstant.GLCODE,glcode);
			 query.setCacheable(true);
			 if(null ==query.list() || query.list().size()==0){
				 throw new EGOVRuntimeException("This code is not a control code"+glcode);
			 }

			 // validate subledger Detailtypeid

			 if(null != subdetailDetailMap.get(VoucherConstant.DETAILTYPEID)){
				 detailtypeid = subdetailDetailMap.get(VoucherConstant.DETAILTYPEID).toString();
				 Session session =HibernateUtil.getCurrentSession();
				 Query qry = session.createQuery("from CChartOfAccountDetail cd,CChartOfAccounts c where " +
				 "cd.glCodeId = c.id and c.glcode=:glcode and cd.detailTypeId=:detailTypeId");
				 qry.setString(VoucherConstant.GLCODE,glcode);
				 qry.setString("detailTypeId",detailtypeid);
				 qry.setCacheable(true);
				 if(null ==qry.list() || qry.list().size() ==0){
					 throw new EGOVRuntimeException("The subledger type mapped to this account code is not correct "+glcode);
				 }
			 }else {
				 throw new EGOVRuntimeException("Subledger type value is missing for account code "+glcode);
			 }
			 // validate detailkey id.

			 if(null != subdetailDetailMap.get(VoucherConstant.DETAILKEYID)){
				 detailKeyId = subdetailDetailMap.get(VoucherConstant.DETAILKEYID).toString();
				 Session session =HibernateUtil.getCurrentSession();
				 Query qry = session.createQuery("from Accountdetailkey adk where adk.accountdetailtype=:detailtypeid and adk.detailkey=:detailkey");
				 qry.setString(VoucherConstant.DETAILTYPEID,detailtypeid);
				 qry.setString("detailkey",detailKeyId);
				 qry.setCacheable(true);
				 if(null ==qry.list() || qry.list().size() ==0){
					 throw new EGOVRuntimeException("Subledger data is not valid for account code "+glcode);
				 }
			 }else {
				 throw new EGOVRuntimeException("detailkeyid is missing");
			 }

			 if(null != subdetailDetailMap.get(VoucherConstant.DEBITAMOUNT ) &&  ! new BigDecimal((subdetailDetailMap.get(VoucherConstant.DEBITAMOUNT).toString())).equals(BigDecimal.ZERO) ){
				 BigDecimal dbtAmount = new BigDecimal(subdetailDetailMap.get(VoucherConstant.DEBITAMOUNT).toString());
				 if(null !=subledAmtmap.get(VoucherConstant.DEBIT+glcode)){
					 subledAmtmap.put(VoucherConstant.DEBIT+glcode,subledAmtmap.get(VoucherConstant.DEBIT+glcode).add(dbtAmount));
				 }else {
					 subledAmtmap.put(VoucherConstant.DEBIT+glcode,dbtAmount);
				 }

			 }else if(null != subdetailDetailMap.get(VoucherConstant.CREDITAMOUNT ) &&  ! new BigDecimal((subdetailDetailMap.get(VoucherConstant.CREDITAMOUNT).toString())).equals(BigDecimal.ZERO) ){
				 BigDecimal creditAmt = new BigDecimal(subdetailDetailMap.get(VoucherConstant.CREDITAMOUNT).toString());
				 if(null !=subledAmtmap.get(VoucherConstant.CREDIT+glcode)){
					 subledAmtmap.put(VoucherConstant.CREDIT+glcode,subledAmtmap.get(VoucherConstant.CREDIT+glcode).add(creditAmt));
				 }else {
					 subledAmtmap.put(VoucherConstant.CREDIT+glcode,creditAmt);
				 }

			 }
			 else {
				 throw new EGOVRuntimeException("Incorrect Sub ledger amount supplied for glcode : "+glcode);
			 }

		 }



		 for (HashMap<String, Object> accDetailMap : accountcodedetails) {

			 String glcode = accDetailMap.get(VoucherConstant.GLCODE).toString();

			 if(null != subledAmtmap.get(VoucherConstant.DEBIT+glcode)){
				 //changed since equals does considers decimal values eg 20.0 is not equal to 2 
				 if( subledAmtmap.get(VoucherConstant.DEBIT+glcode).compareTo(accDetAmtMap.get(VoucherConstant.DEBIT+glcode))!=0 ){

					 throw new EGOVRuntimeException("Total of subleger debit amount is not matching with the account code amount "+glcode);
				 }
			 }
			 if(null != subledAmtmap.get(VoucherConstant.CREDIT+glcode)){
				//changed since equals does considers decimal values eg 20.0 is not equal to 2 
				 if( subledAmtmap.get(VoucherConstant.CREDIT+glcode).compareTo(accDetAmtMap.get(VoucherConstant.CREDIT+glcode))!=0 ){

					 throw new EGOVRuntimeException("Total of subleger credit amount is not matching with the account code amount "+glcode);
				 }
			 }

		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("END | validateTransaction");

	 }
	 public List<Transaxtion> createTransaction(HashMap<String, Object> headerdetails, List<HashMap<String,Object>> accountcodedetails,List<HashMap<String,Object>> subledgerdetails,CVoucherHeader vh)throws EGOVRuntimeException{
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Start | createTransaction ");
		 List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
		 int lineId = 1;
		 try{

			 for (HashMap<String, Object> accDetailMap : accountcodedetails) {
				 String glcode = accDetailMap.get(VoucherConstant.GLCODE).toString();
				 
				 String debitAmount = accDetailMap.get(VoucherConstant.DEBITAMOUNT).toString();
				 String creditAmount = accDetailMap.get(VoucherConstant.CREDITAMOUNT).toString();
				 String narration = null;
				 String functionId = null;
				 String functioncode = null;
				 if(null != accDetailMap.get(VoucherConstant.NARRATION)){
					 narration =accDetailMap.get(VoucherConstant.NARRATION).toString();
				 }
				 if(null != accDetailMap.get(VoucherConstant.FUNCTIONCODE) && "" != accDetailMap.get(VoucherConstant.FUNCTIONCODE)){
					 functioncode  = accDetailMap.get(VoucherConstant.FUNCTIONCODE).toString();
					 functionId = comm.getFunctionByCode(accDetailMap.get(VoucherConstant.FUNCTIONCODE).toString()).getId().toString();
					 if(LOGGER.isDebugEnabled())     LOGGER.debug("functionId>>>>>>>> "+functionId);
				 }
				 VoucherDetail voucherDetail = new VoucherDetail();
				 voucherDetail.setLineId(lineId++);
				 voucherDetail.setVoucherHeaderId(vh);
				 CChartOfAccounts chartOfAcc=comm.getCChartOfAccountsByGlCode(glcode);
				 voucherDetail.setGlCode(chartOfAcc.getGlcode());
				 voucherDetail.setAccountName(chartOfAcc.getName());
				 voucherDetail.setDebitAmount(new BigDecimal(debitAmount));
				 voucherDetail.setCreditAmount(new BigDecimal(creditAmount));
				 voucherDetail.setNarration(narration);

				 // insert into voucher detail.
				 insertIntoVoucherDetail(voucherDetail);
				 vh.addVoucherDetail(voucherDetail);

				 Transaxtion transaction = new Transaxtion();
				 transaction.setGlCode(chartOfAcc.getGlcode());
				 transaction.setGlName(chartOfAcc.getName());
				 transaction.setVoucherLineId(String.valueOf(voucherDetail.getId()));
				 transaction.setVoucherHeaderId(vh.getId().toString());
				 transaction.setCrAmount(creditAmount);
				 transaction.setDrAmount(debitAmount);
				 transaction.setFunctionId(functionId);
				 if(headerdetails!=null && headerdetails.get("billid")!=null)
					 transaction.setBillId((Long)headerdetails.get("billid"));

				 ArrayList reqParams=new ArrayList();
				 for (HashMap<String, Object> sublegDetailMap : subledgerdetails) {

					 String detailGlCode = sublegDetailMap.get(VoucherConstant.GLCODE).toString();
					 String detailtypeid = sublegDetailMap.get(VoucherConstant.DETAILTYPEID).toString();
					 if(sublegDetailMap.containsKey(VoucherConstant.FUNCTIONCODE) && null != sublegDetailMap.get(VoucherConstant.FUNCTIONCODE) && "" != sublegDetailMap.get(VoucherConstant.FUNCTIONCODE)){
					 String detailFunctionCode = sublegDetailMap.get(VoucherConstant.FUNCTIONCODE).toString();
					 if(glcode.equals(detailGlCode) && functioncode!=null && functioncode.equals(detailFunctionCode)){
						 TransaxtionParameter reqData=new TransaxtionParameter();
						 Accountdetailtype adt = masters.getAccountdetailtypeById(Integer.valueOf(detailtypeid));
						 reqData.setDetailName(adt.getAttributename());
						 reqData.setGlcodeId(chartOfAcc.getId().toString());
						 if(null != sublegDetailMap.get(VoucherConstant.DEBITAMOUNT ) &&  ! new BigDecimal((sublegDetailMap.get(VoucherConstant.DEBITAMOUNT).toString())).equals(BigDecimal.ZERO) ){
							 reqData.setDetailAmt(sublegDetailMap.get(VoucherConstant.DEBITAMOUNT).toString());
						 }else{
							 reqData.setDetailAmt(sublegDetailMap.get(VoucherConstant.CREDITAMOUNT).toString());
						 }

						 reqData.setDetailKey(sublegDetailMap.get(VoucherConstant.DETAILKEYID).toString());
						 reqData.setDetailTypeId(detailtypeid);
						 reqData.setTdsId(sublegDetailMap.get("tdsId")!=null?sublegDetailMap.get("tdsId").toString():null);
						 reqParams.add(reqData);
					 }
					}else{
						 if(glcode.equals(detailGlCode)){
							 TransaxtionParameter reqData=new TransaxtionParameter();
							 Accountdetailtype adt = masters.getAccountdetailtypeById(Integer.valueOf(detailtypeid));
							 reqData.setDetailName(adt.getAttributename());
							 reqData.setGlcodeId(chartOfAcc.getId().toString());
							 if(null != sublegDetailMap.get(VoucherConstant.DEBITAMOUNT ) &&  ! new BigDecimal((sublegDetailMap.get(VoucherConstant.DEBITAMOUNT).toString())).equals(BigDecimal.ZERO) ){
								 reqData.setDetailAmt(sublegDetailMap.get(VoucherConstant.DEBITAMOUNT).toString());
							 }else{
								 reqData.setDetailAmt(sublegDetailMap.get(VoucherConstant.CREDITAMOUNT).toString());
							 }

							 reqData.setDetailKey(sublegDetailMap.get(VoucherConstant.DETAILKEYID).toString());
							 reqData.setDetailTypeId(detailtypeid);
							 reqData.setTdsId(sublegDetailMap.get("tdsId")!=null?sublegDetailMap.get("tdsId").toString():null);
							 reqParams.add(reqData);
						 }
					}

				 }
				 if(reqParams!=null && reqParams.size()>0 ){
					 transaction.setTransaxtionParam(reqParams);
				 }
				 transaxtionList.add(transaction);
			 }
		 }catch (Exception e) {
			 LOGGER.error("Exception occured while posting data into voucher detail and transaction");
			 throw new EGOVRuntimeException("Exception occured while posting data into voucher detail and transaction"+e.getMessage());
		 }
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("END | createTransaction ");
		 return transaxtionList;
	 }
	 private void insertIntoVoucherDetail(final VoucherDetail vd){
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Start | insertIntoVoucherDetail");
		 PersistenceService<VoucherDetail, Long> voucherDetailSer;
		 voucherDetailSer = new PersistenceService<VoucherDetail, Long>();
		// voucherDetailSer.setSessionFactory(new SessionFactory());
		 voucherDetailSer.setType(VoucherDetail.class);
		 voucherDetailSer.persist(vd);

		 if(LOGGER.isDebugEnabled())     LOGGER.debug("END | insertIntoVoucherDetail");
	 }
	 public Functionary  getFunctionaryByCode(BigDecimal code){
		 PersistenceService<Functionary, Integer> functionarySer;
		 functionarySer = new PersistenceService<Functionary, Integer>();
		// functionarySer.setSessionFactory(new SessionFactory());
		 functionarySer.setType(Functionary.class);
		 Functionary functionary= functionarySer.find("from Functionary where code=?", code);
		 return functionary;

	 }
	 public void validateVoucherHeader(CVoucherHeader voucherHeader){
		 if(null == voucherHeader){
			 throw new EGOVRuntimeException("voucherHeader object passed is null");
		 }
		 if(null == voucherHeader.getType() || ! voucherHeader.getType().equalsIgnoreCase(voucherTypeEnum.RECEIPT.toString())){

			 throw new EGOVRuntimeException("Voucher type is not Receipt");
		 }
	 }
	 public void validateReceiptDetails(final HashMap<String, Object> receiptdetails){
		 String modeofcollection = null;
		 if(null == receiptdetails){
			 throw new EGOVRuntimeException("receiptdetails is null");
		 }
		 if(null == receiptdetails.get(VoucherConstant.MODEOFCOLLECTION)){
			 throw new EGOVRuntimeException("modeofcollection is null");
		 }else{
			 modeofcollection = chkModeOfCollection(receiptdetails.get(VoucherConstant.MODEOFCOLLECTION).toString());
			 if(null == modeofcollection){
				 throw new EGOVRuntimeException("Not a valid modeofcollection");
			 }
		 }
		 if(VoucherConstant.BANK.equalsIgnoreCase(modeofcollection)){
			 validateBankDetails(receiptdetails);
		 }

		 if(null == receiptdetails.get(VoucherConstant.NETAMOUNT)){
			 throw new EGOVRuntimeException("Net amount is null");
		 }
	 }
	 public void validateBankDetails(final HashMap<String, Object> receiptdetails){
		 String bankCode = null;
		 String bankBranchCode = null;
		 String bankAccNumber = null;
		 if(null == receiptdetails.get(VoucherConstant.BANKCODE)){
			 throw new EGOVRuntimeException("Bank Code is null");
		 }
		 if(null == receiptdetails.get(VoucherConstant.BANKBRANCHCODE)){
			 throw new EGOVRuntimeException("Bank branch code  is null");
		 }
		 if(null == receiptdetails.get(VoucherConstant.BANKACCOUNTNUMBER)){
			 throw new EGOVRuntimeException("Bank Account number is null");
		 }
		 bankCode = receiptdetails.get(VoucherConstant.BANKCODE).toString();
		 bankBranchCode = receiptdetails.get(VoucherConstant.BANKBRANCHCODE).toString();
		 bankAccNumber = receiptdetails.get(VoucherConstant.BANKACCOUNTNUMBER).toString();
		 Bankaccount bankAccount = comm.getBankAccountByAccBranchBank(bankAccNumber, bankBranchCode, bankCode);
		 if (null == bankAccount){
			 throw new EGOVRuntimeException("not a valid bank account number");
		 }else {
			 receiptdetails.put(VoucherConstant.BANKACCID, bankAccount.getId());
		 }
	 }
	 /**
	  *
	  * @param instrumentdetails
	  */
	 public void validateInstrumentdetails(final List<HashMap<String,Object>> instrumentdetailsList,HashMap<String, Object> receiptdetails){
		 String bankCode = null;
		 BigDecimal chequeAmounts = BigDecimal.valueOf(0);
		 for (HashMap<String, Object> instrumentdetails : instrumentdetailsList) {
			 if(null == instrumentdetails.get(VoucherConstant.INSTRUMENTNO)){
				 throw new EGOVRuntimeException("Cheque number is null");
			 }
			 if(null == instrumentdetails.get(VoucherConstant.INSTRUMENTDATE)){
				 throw new EGOVRuntimeException("Cheque date is null");
			 }
			 if(null == instrumentdetails.get(VoucherConstant.INSTRUMENTAMOUNT)){
				 throw new EGOVRuntimeException("Cheque amount is null");
			 }else {
				 chequeAmounts = chequeAmounts.add(new BigDecimal(instrumentdetails.get(VoucherConstant.INSTRUMENTAMOUNT).toString()));
			 }
			 if(null != instrumentdetails.get(VoucherConstant.BANKCODE)){
				 bankCode = instrumentdetails.get(VoucherConstant.BANKCODE).toString();
				 if(null == comm.getBankByCode(bankCode)){
					 throw new EGOVRuntimeException("not a valid bank code");
				 }
			 }
		 }
		 if(! chequeAmounts.equals(receiptdetails.get(VoucherConstant.NETAMOUNT))){
			 throw new EGOVRuntimeException("total cheque amount is not matching with net amount");
		 }

	 }

	 protected void postInReceiptHeader(final HashMap<String, Object> receiptdetails,Object CVoucherHeader,Chequedetail chequeDetail){

		 rcptHeaderService = new PersistenceService<ReceiptHeader, Integer>();
		// rcptHeaderService.setSessionFactory(new SessionFactory());
		 rcptHeaderService.setType(ReceiptHeader.class);

		 String modeOfCollection = receiptdetails.get(VoucherConstant.MODEOFCOLLECTION).toString();
		 BigDecimal netAmount = new BigDecimal(receiptdetails.get(VoucherConstant.NETAMOUNT).toString());
		 CVoucherHeader voucherHeader = (CVoucherHeader) CVoucherHeader;
		 ReceiptHeader receiptHeader = new ReceiptHeader();

		 //Setting data into receipt header
		 receiptHeader.setModeOfCollection(chkModeOfCollection(modeOfCollection));
		 receiptHeader.setCashAmount(netAmount);
		 receiptHeader.setVoucherHeaderId(voucherHeader);
		 receiptHeader.setType(voucherHeader.getType());
		 if(null != receiptdetails.get(VoucherConstant.MANNUALRECEIPTNUMBER)){
			 receiptHeader.setManualReceiptNo(receiptdetails.get(VoucherConstant.MANNUALRECEIPTNUMBER).toString());
		 }
		 if(null != receiptdetails.get(VoucherConstant.NARRATION)){
			 receiptHeader.setManualReceiptNo(receiptdetails.get(VoucherConstant.NARRATION).toString());
		 }
		 if(null != receiptdetails.get(VoucherConstant.REVENUESOURCE)){
			 receiptHeader.setRevenueSource((receiptdetails.get(VoucherConstant.REVENUESOURCE).toString()));
		 }
		 if(null != receiptdetails.get(VoucherConstant.WARDID)){
			 receiptHeader.setRevenueSource((receiptdetails.get(VoucherConstant.WARDID).toString()));
		 }
		 receiptHeader.setIsReversed(0);
		 if(null != chequeDetail){
			 receiptHeader.setChequeId(chequeDetail);
		 }if(null != receiptdetails.get(VoucherConstant.RECEIPTNUMBER)){
			 receiptHeader.setReceiptNo( receiptdetails.get(VoucherConstant.RECEIPTNUMBER).toString());
		 }
		 if(null != receiptdetails.get(VoucherConstant.MANUUALRECEIPTNO)){
			 receiptHeader.setManualReceiptNo(receiptdetails.get(VoucherConstant.MANUUALRECEIPTNO).toString());
		 }
		 if(null != receiptdetails.get(VoucherConstant.NARRATION)){
			 receiptHeader.setNarration(receiptdetails.get(VoucherConstant.NARRATION).toString());
		 }
		 rcptHeaderService.persist(receiptHeader);

	 }

	 enum modeOfCollectionEnum {
		 Cheque, Cash, Bank;
	 }
	 private String chkModeOfCollection(String mode){
		 String collectionMode = null;
		 try{
			 switch (Integer.valueOf(mode)) {
			 case 1:
				 collectionMode=modeOfCollectionEnum.Cheque.toString();
				 break;
			 case 2:
				 collectionMode=modeOfCollectionEnum.Cash.toString();
				 break;
			 case 3:
				 collectionMode=modeOfCollectionEnum.Bank.toString();
				 break;
			 }
		 }catch (Exception e) {
			 LOGGER.error(ERR,e);
			 throw new EGOVRuntimeException("Not a valid modeofcollection");
		 }

		 return collectionMode;
	 }

	 public EgBillregister createBill(HashMap<String, Object> supplierBillDetails, List<HashMap<String,Object>> ledgerlist) throws EGOVRuntimeException,Exception{
		 EgBillregister billregister = new EgBillregister();

		 postInBillRegister(supplierBillDetails,billregister);
		 postInEgbillMis(billregister,supplierBillDetails);
		 postinbilldetail(billregister,ledgerlist);
		 billregister =  billsMngr.createBillRegister(billregister);

		 return billregister;

	 }
	 public void postInBillRegister(HashMap<String, Object> supplierBillDetails,EgBillregister billregister) throws EGOVRuntimeException,Exception{

		 billregister.setWorksdetailId(supplierBillDetails.get("worksdetailid").toString());
		 billregister.setBilldate((Date)supplierBillDetails.get("billdate"));
		 billregister.setBillamount(new BigDecimal(supplierBillDetails.get("billamount").toString()));
		 billregister.setPassedamount(new BigDecimal(supplierBillDetails.get("passedamount").toString()));
		 if(null != supplierBillDetails.get("adjustedamount").toString()){
			 billregister.setAdvanceadjusted(new BigDecimal(supplierBillDetails.get("adjustedamount").toString()));
		 }
		 billregister.setBillstatus("CREATED");
		 billregister.setBilltype(supplierBillDetails.get("billtype").toString());
		 billregister.setExpendituretype(supplierBillDetails.get("expendituretype").toString());
		 if(null != supplierBillDetails.get("narration").toString()){
			 billregister.setNarration(supplierBillDetails.get("narration").toString());
		 }
		 EgwStatus status = null;
		 Connection conn = (Connection) null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
		 status = comm.findEgwStatusById(Integer.valueOf(cm.getEGWStatusId(conn, "PURCHBILL", "Pending")));
		 billregister.setStatus(status);
		 if(null != EGOVThreadLocals.getUserId()){
			 billregister.setCreatedBy(userMngr.getUserById(Long.valueOf(EGOVThreadLocals.getUserId())));
		 }
		 billregister.setCreatedDate(new DateTime());
		 SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
		 String date = df.format((Date)supplierBillDetails.get("billdate"));
		 billregister.setBillnumber(cmImpl.getTxnNumber("WBILL",date ,conn));
	 }
	 public void postInEgbillMis(EgBillregister billregister,HashMap<String, Object> supplierBillDetails ) throws Exception{

		 Connection conn = (Connection) null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
		 EgBillregistermis billMis = new EgBillregistermis();
		 billMis.setEgBillregister(billregister);
		 billMis.setFund(((Worksdetail)supplierBillDetails.get("worksdetail")).getFund());
		 billMis.setFundsource(((Worksdetail)supplierBillDetails.get("worksdetail")).getFundsource());
		 SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
		 String date = df.format((Date)supplierBillDetails.get("billdate"));
		 String sanctionNo = cmImpl.getTxnNumber("SAN",date, conn);
		 billMis.setSanctiondetail(sanctionNo);
		 billMis.setSanctiondate(billregister.getBilldate());
		 billMis.setFinancialyear(comm.getFinancialYearByFinYearRange(date));
		 if( null != supplierBillDetails.get(VoucherConstant.DEPARTMENTCODE)){
			 String departmentCode = supplierBillDetails.get(VoucherConstant.DEPARTMENTCODE).toString();
			 billMis.setEgDepartment((Department)deptM.getDepartmentByCode(departmentCode));
		 }
		 billMis.setLastupdatedtime(new Date());
		 billregister.setEgBillregistermis(billMis);
	 }
	 public void  postinbilldetail(EgBillregister billregister,List<HashMap<String,Object>> ledgerlist){

		 Set<EgBilldetails> egBilldetailes = new HashSet<EgBilldetails>(0);
		 for (HashMap<String, Object> ledgerMap : ledgerlist) {

			 EgBilldetails billDetail = new EgBilldetails();
			 billDetail.setEgBillregister(billregister);
			 String glcode = ledgerMap.get(VoucherConstant.GLCODE).toString();
			 CChartOfAccounts chartOfAcc=comm.getCChartOfAccountsByGlCode(glcode);
			 billDetail.setGlcodeid(new BigDecimal(chartOfAcc.getId()));
			 if(null!=ledgerMap.get(VoucherConstant.DEBITAMOUNT) &&  ! new BigDecimal(ledgerMap.get(VoucherConstant.DEBITAMOUNT).toString()).equals(BigDecimal.ZERO)){
				 billDetail.setDebitamount(new BigDecimal(ledgerMap.get(VoucherConstant.DEBITAMOUNT).toString()));
			 }
			 if(null!=ledgerMap.get(VoucherConstant.CREDITAMOUNT)&& ! new BigDecimal(ledgerMap.get(VoucherConstant.CREDITAMOUNT).toString()).equals(BigDecimal.ZERO)){
				 billDetail.setCreditamount(new BigDecimal(ledgerMap.get(VoucherConstant.CREDITAMOUNT).toString()));
			 }
			 egBilldetailes.add(billDetail);
		 }
		 billregister.setEgBilldetailes(egBilldetailes);

	 }
	 public void updatePJV(CVoucherHeader vh, List<PreApprovedVoucher> detailList,List<PreApprovedVoucher> subledgerlist)  throws EGOVRuntimeException
	 {
		 try
		 {
			//This fix is for Phoenix Migration.
			 Connection conn = null;
			 //delete the existing voucherdetail, gl entries.
			 deleteVoucherdetailAndGL(conn,vh);

			 HashMap<String,Object> detailMap = null;
			 HashMap<String,Object> subledgerMap = null;
			 List<HashMap<String,Object>> accountdetails = new ArrayList<HashMap<String,Object>>();
			 List<HashMap<String,Object>> subledgerdetails = new ArrayList<HashMap<String,Object>>();
			 CChartOfAccounts coa =null;
			 CFunction function = null;
			 //iterate it
			 for(PreApprovedVoucher detail : detailList)
			 {
				 detailMap = new HashMap<String,Object>();
				 detailMap.put(VoucherConstant.GLCODE, detail.getGlcodeDetail());
				 detailMap.put(VoucherConstant.DEBITAMOUNT, detail.getDebitAmountDetail());
				 detailMap.put(VoucherConstant.CREDITAMOUNT, detail.getCreditAmountDetail());
				 if(detail.getFunctionIdDetail()!=null)
				 {
					 function = comm.getFunctionById(detail.getFunctionIdDetail());
					 detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
				 }
				 accountdetails.add(detailMap);
			 }
			 if(subledgerlist!=null)
				 for(PreApprovedVoucher subledger : subledgerlist)
				 {
					 subledgerMap = new HashMap<String,Object>();
					 coa = comm.getCChartOfAccountsById(subledger.getGlcode().getId());
					 subledgerMap.put(VoucherConstant.GLCODE, coa.getGlcode());
					 subledgerMap.put(VoucherConstant.DETAILTYPEID, subledger.getDetailType().getId());
					 subledgerMap.put(VoucherConstant.DETAILKEYID, subledger.getDetailKeyId());
					 if(subledger.getDebitAmountDetail()==null || subledger.getDebitAmountDetail().compareTo(BigDecimal.ZERO)==0)
						 subledgerMap.put(VoucherConstant.CREDITAMOUNT,subledger.getCreditAmount());
					 else
						 subledgerMap.put(VoucherConstant.DEBITAMOUNT,subledger.getDebitAmount());
					 subledgerdetails.add(subledgerMap);
				 }
			 List<Transaxtion> transactions = createTransaction(null,accountdetails, subledgerdetails, vh);
			HibernateUtil.getCurrentSession().flush();
			 ChartOfAccounts engine=ChartOfAccounts.getInstance();
			 Transaxtion txnList[]=new Transaxtion[transactions.size()];
			 txnList=(Transaxtion[])transactions.toArray(txnList);
			 SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
			 if(!engine.postTransaxtions(txnList, conn,formatter.format(vh.getVoucherDate())))
			 {
				 throw new RBACException("Voucher creation Failed");
			 }
		 }catch(Exception e)
		 {
			 LOGGER.error("Inside exception updatePJV"+e.getMessage());
			 throw new EGOVRuntimeException(e.getMessage());
		 }
	 }
	 public void deleteVoucherdetailAndGL(Connection con,CVoucherHeader vh) throws SQLException,EGOVRuntimeException
	 {
		 try
		 {
			 Query pstmt1=null;
			 Query pstmt2=null;
			 Query pstmt3=null;
			 Query pstmt4=null;
			 
			 String glQry="select id from generalledger where voucherheaderid= ?";
			 String glidQry="select id from generalledgerdetail where generalledgerid= ?";
			 String delQry="delete from EG_REMITTANCE_GLDTL where gldtlid= ?";
			 String delQrr="delete from generalledgerdetail where generalledgerid=?";
			 String delgl=" delete from generalledger where voucherheaderid=?";
			 String delvh=" delete from voucherdetail where voucherheaderid=?";
			 pstmt1 = HibernateUtil.getCurrentSession().createSQLQuery(glQry);
			 pstmt1.setFloat(1,vh.getId());
			 
			 List<Object[]>  rs = pstmt1.list();
			 List<Object[]>  rs1= null;
			 boolean delete = false;
			 while(rs!=null && rs.size()>0)
			 {
				 pstmt2 = HibernateUtil.getCurrentSession().createSQLQuery(glidQry);
				 pstmt2.setLong(1,Long.parseLong(rs.get(1).toString()));
				 rs1 = pstmt2.list();
				 while(rs1!=null && rs1.size()>0)
				 {
					 delete=true;
					 pstmt3 = HibernateUtil.getCurrentSession().createSQLQuery(delQry);
					 pstmt3.setLong(1,Long.parseLong(rs1.get(1).toString()));
					 pstmt3.executeUpdate();
				 }
				 if(delete){
					 pstmt4 = HibernateUtil.getCurrentSession().createSQLQuery(delQrr);
					 pstmt4.setLong(1,Long.parseLong(rs1.get(1).toString()));
					 pstmt4.executeUpdate();
				 }
			 }
			 pstmt1 = HibernateUtil.getCurrentSession().createSQLQuery(delgl);
			 pstmt1.setLong(1,vh.getId());
			 pstmt1.executeUpdate();
			 
			 pstmt1 = HibernateUtil.getCurrentSession().createSQLQuery(delvh);
			 pstmt1.setLong(1,vh.getId());
			 pstmt1.executeUpdate();
			 
		 }catch(Exception e)
		 {
			 LOGGER.error("Inside exception deleteVoucherdetailAndGL"+e.getMessage());
			 throw new EGOVRuntimeException(e.getMessage());
		 }
	 }
	 /**
	  * HashMap<String, Object> voucher details to reverse a voucher
	  * <p>Original voucher header id -long- is the voucher header  id of the voucher  to be reversed later callled originalvoucher
	  * <p>Reversal voucher name -String - name of the reversal voucher (mandatory)
	  * <p>Reversal voucher type -String - type of the reversal voucher (mandatory)
	  * <p>Reversal voucher date - Date('dd/MM/yyyy') date of the reversal vocuher
	  * <p>Reversal voucher number - String - voucher number of reversal voucher
	  * Newly created voucher is called reversal voucher
	  * @param paramList
	  * @return
	  */
	 public CVoucherHeader reverseVoucher(List<HashMap<String,Object>> paramList) throws EGOVRuntimeException,ParseException
	 {
		 // -- Reversal Voucher date check ----
		 CVoucherHeader reversalVoucherObj = new CVoucherHeader();
		 CVoucherHeader originalVocher;
		 for(HashMap<String,Object> paramMap:paramList) {

			 if(paramMap.get(VOUCHER_HEADER_ID)==null) {
				 throw new IllegalArgumentException(VOUCHER_HEADER_ID+IS_MISSING);
			 } else {
				 try {
					 originalVocher =comm.findVoucherHeaderById((Long)paramMap.get(VOUCHER_HEADER_ID));
				 } catch (Exception e) {
					 throw new EGOVRuntimeException("cannot find "+VOUCHER_HEADER_ID+"in the system");
				 }
				 reversalVoucherObj.setOriginalvcId(originalVocher.getId());
			 }

			 if(paramMap.get(REVERSAL_VOUCHER_DATE)==null) {
				 SimpleDateFormat sdf=new SimpleDateFormat(DD_MM_YYYY);
				 Date reversalVoucherDate=sdf.parse(sdf.format(originalVocher.getVoucherDate()));
				 reversalVoucherObj.setVoucherDate(reversalVoucherDate);
			 } else {
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher  end REVERSAL_VOUCHER_DAT :"+paramMap.get(REVERSAL_VOUCHER_DATE));
				 reversalVoucherObj.setVoucherDate((Date)paramMap.get(REVERSAL_VOUCHER_DATE));
			 }
		 }
		 PersistenceService persistenceService=new PersistenceService();
		// persistenceService.setSessionFactory(new SessionFactory());
		 originalVocher = (CVoucherHeader)	persistenceService.find("from CVoucherHeader where id=?",reversalVoucherObj.getOriginalvcId());
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("original voucher is "+reversalVoucherObj.getOriginalvcId());
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("reversalVoucherObj getVoucherDate is "+reversalVoucherObj.getVoucherDate());
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("originalVocher getVoucherDate is "+originalVocher.getVoucherDate());
		 if(reversalVoucherObj.getVoucherDate().before(originalVocher.getVoucherDate()) ) {
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("Reversal Voucher Date should be greater than the Origianal Voucher Date");
			 throw new ValidationException(Arrays.asList(new ValidationError("reversal.voucher.date.validate","Reversal Voucher Date should be greater than the Original Voucher Date")));
	    }
		// -- Reversal Voucher date check ----END --//

		 CVoucherHeader reversalVoucher= createReversalVoucher(paramList);
		HibernateUtil.getCurrentSession().flush();
		 reversalVoucher=reverseVoucherAndLedger(reversalVoucher);
		 return reversalVoucher;
	 }
	 private CVoucherHeader reverseVoucherAndLedger(CVoucherHeader reversalVoucher) {
		 CVoucherHeader originalVocher;
		 SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);

		 try {
			 PersistenceService persistenceService=new PersistenceService();
			// persistenceService.setSessionFactory(new SessionFactory());
			 if(LOGGER.isDebugEnabled())     LOGGER.debug("original voucher is "+reversalVoucher.getOriginalvcId());
			 originalVocher=(CVoucherHeader)	persistenceService.find("from CVoucherHeader where id=?",reversalVoucher.getOriginalvcId());
			 List<CGeneralLedger> orginalLedgerEntries = generalLedgerService.findAllBy("from CGeneralLedger where voucherHeaderId=?",originalVocher);
			 HashMap<String,Object> detailMap = null;
			 HashMap<String,Object> subledgerMap = null;
			 List<HashMap<String,Object>> accountdetails = new ArrayList<HashMap<String,Object>>();
			 List<HashMap<String,Object>> subledgerdetails = new ArrayList<HashMap<String,Object>>();
			 CFunction function=null;
			 for(CGeneralLedger ledger:orginalLedgerEntries)
			 {
				 detailMap = new HashMap<String,Object>();
				 detailMap.put(VoucherConstant.GLCODE, ledger.getGlcode());
				 detailMap.put(VoucherConstant.DEBITAMOUNT, ledger.getCreditAmount());
				 detailMap.put(VoucherConstant.CREDITAMOUNT, ledger.getDebitAmount());
				 if(ledger.getFunctionId()!=null)
				 {
					 function = comm.getFunctionById(ledger.getFunctionId().longValue());
					 detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
				 }
				 List<CGeneralLedgerDetail> ledgerDetailSet = generalLedgerDetailService.findAllBy("from CGeneralLedgerDetail where generalLedgerId=?", ledger.getId().intValue());
				 for(CGeneralLedgerDetail ledgerDetail:ledgerDetailSet)
				 {
					 subledgerMap=new HashMap<String, Object>();
					 subledgerMap.put(VoucherConstant.GLCODE, ledger.getGlcode());
					 subledgerMap.put(VoucherConstant.DETAILTYPEID, ledgerDetail.getDetailTypeId());
					 subledgerMap.put(VoucherConstant.DETAILKEYID, ledgerDetail.getDetailKeyId());
					 if(ledger.getDebitAmount().equals(BigDecimal.ZERO)){
						 subledgerMap.put(VoucherConstant.CREDITAMOUNT,ledgerDetail.getAmount());
					 }else{
						 subledgerMap.put(VoucherConstant.DEBITAMOUNT,ledgerDetail.getAmount());
					 }

					 subledgerdetails.add(subledgerMap);
				 }
				 accountdetails.add(detailMap);
			 }
			 List<Transaxtion> transactions = createTransaction(null,accountdetails,subledgerdetails,reversalVoucher);
			HibernateUtil.getCurrentSession().flush();
			 ChartOfAccounts engine=ChartOfAccounts.getInstance();
			 Transaxtion txnList[]=new Transaxtion[transactions.size()];
			 txnList=(Transaxtion[])transactions.toArray(txnList);
			 //SimpleDateFormat formatter = new SimpleDateFormat(DD_MMM_YYYY);
			 Connection con = null;
			 if(!engine.postTransaxtions(txnList,con,formatter.format(reversalVoucher.getVoucherDate())))
			 {
				 throw new EGOVRuntimeException("Voucher Reversal Failed");
			 }
		 }catch (ValidationException e) {
			 LOGGER.error(ERR,e);
			 throw new ValidationException(Arrays.asList(new ValidationError(e.getErrors().get(0).getKey(),e.getErrors().get(0).getMessage())));
		 }
		 catch (HibernateException e) {
			 LOGGER.error(ERR,e);
			 throw  new  EGOVRuntimeException(e.getMessage());
		 } catch (TaskFailedException e) {
			 LOGGER.error(ERR,e);
			 throw new EGOVRuntimeException(e.getMessage());
		 } catch (Exception e) {
			 LOGGER.error(ERR,e);
			 throw new EGOVRuntimeException(e.getMessage());
		 }

		 return reversalVoucher;
	 }

	 private CVoucherHeader createReversalVoucher(List<HashMap<String, Object>> paramList) throws ParseException
	 {
		 CVoucherHeader reversalVoucher;
		 reversalVoucher = validateAndAssignReversalVoucherParams(paramList);
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("reversalVoucher -- reversalVoucher "+reversalVoucher);
			 try {
				CVoucherHeader	 originalVoucher =comm.findVoucherHeaderById(reversalVoucher.getOriginalvcId());
				 //replace "space" of type and name to empty since enum is without space 
				//if the voucher name is not in vouchersubtype enum it will throw runtime exception.
				
				String autoVoucherType="";
				
				if(!reversalVoucher.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL))
				{
					autoVoucherType=getVoucherNumberPrefix(null,reversalVoucher.getType().replaceAll(" ", ""));
				}
				else
				{
					autoVoucherType=getVoucherNumberPrefix(reversalVoucher.getName().replaceAll(" ",""),reversalVoucher.getType().replaceAll(" ", ""));
				}
				 String  vNumGenMode = null;
				 if(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(reversalVoucher.getType()) ){
						vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Journal");
					}else{
						 vNumGenMode= new VoucherTypeForULB().readVoucherTypes(reversalVoucher.getType());
					}
				 SimpleDateFormat df = new SimpleDateFormat(DD_MM_YYYY);
				 String vDate = df.format(reversalVoucher.getVoucherDate());

				 String strVoucherNumber = VoucherHelper.getGeneratedVoucherNumber(originalVoucher.getFundId().getId(), autoVoucherType, 
						 reversalVoucher.getVoucherDate(), vNumGenMode, reversalVoucher.getVoucherNumber());
				 reversalVoucher.setVoucherNumber(strVoucherNumber);
				 /*
				 if("Auto".equalsIgnoreCase(vNumGenMode))
				 {

						reversalVoucher.setVoucherNumber((cmImpl.getTxnNumber(originalVoucher.getFundId().getId().toString(),autoVoucherType,vDate,null)));

				 }else
				 {
					 String	voucherNumber = reversalVoucher.getVoucherNumber();
					 if(voucherNumber==null)
					 {
						reversalVoucher.setVoucherNumber((cmImpl.getTxnNumber(originalVoucher.getFundId().getId().toString(),autoVoucherType,vDate,null)));
						}
					 else
					 {
						 Query	query=HibernateUtil.getCurrentSession().createQuery("select f.identifier from Fund f where id=:fundId");
						 query.setInteger("fundId", originalVoucher.getFundId().getId());
						 String fundIdentifier = query.uniqueResult().toString();
						 reversalVoucher.setVoucherNumber(new StringBuffer().append(fundIdentifier).append(autoVoucherType).
								 append(voucherNumber).toString());
					 }
				 } */
				 reversalVoucher.setFiscalPeriodId(originalVoucher.getFiscalPeriodId());
				 reversalVoucher.setDepartmentId(originalVoucher.getDepartmentId());
				 reversalVoucher.setFunctionId(originalVoucher.getFunctionId());
				 reversalVoucher.setFundId(originalVoucher.getFundId());
				 if(originalVoucher.getVouchermis()!=null)
				 {
					 Vouchermis reversalVMis=new Vouchermis();
					 reversalVMis.setDepartmentid(originalVoucher.getVouchermis().getDepartmentid());
					 reversalVMis.setFunctionary(originalVoucher.getVouchermis().getFunctionary());
					 reversalVMis.setSchemeid(originalVoucher.getVouchermis().getSchemeid());
					 reversalVMis.setSubschemeid(originalVoucher.getVouchermis().getSubschemeid());
					 reversalVMis.setDivisionid(originalVoucher.getVouchermis().getDivisionid());
					 reversalVMis.setFundsource(originalVoucher.getVouchermis().getFundsource());
					 reversalVMis.setSourcePath(originalVoucher.getVouchermis().getSourcePath());
					 reversalVoucher.setVouchermis(reversalVMis);
					 reversalVMis.setVoucherheaderid(reversalVoucher);

				 }
				 reversalVoucher.setEffectiveDate(new Date());
				 reversalVoucher.setStatus(FinancialConstants.REVERSALVOUCHERSTATUS);
				 insertIntoVoucherHeader(reversalVoucher);
				 originalVoucher.setStatus(FinancialConstants.REVERSEDVOUCHERSTATUS);
			} catch (HibernateException e) {
				LOGGER.error(ERR,e);
				throw new EGOVRuntimeException(e.getMessage());

			}catch (ValidationException e) {
				LOGGER.error(ERR,e);
			throw e;

		} catch (Exception e) {
			LOGGER.error(ERR,e);
				throw new EGOVRuntimeException(e.getMessage());
			}



		 return reversalVoucher;
	 }
	 private CVoucherHeader validateAndAssignReversalVoucherParams(
			 List<HashMap<String, Object>> paramList) throws ParseException {
		 CVoucherHeader reversalVoucher=new CVoucherHeader();
		 for(HashMap<String,Object> paramMap:paramList)
		 {
			 CVoucherHeader originalVoucher=null;
			 if(paramMap.get(VOUCHER_HEADER_ID)==null)
			 {
				 throw new IllegalArgumentException(VOUCHER_HEADER_ID+IS_MISSING);
			 }
			 else
			 {
				 try {
					 originalVoucher =comm.findVoucherHeaderById((Long)paramMap.get(VOUCHER_HEADER_ID));
				 } catch (Exception e) {
					 throw new EGOVRuntimeException("cannot find "+VOUCHER_HEADER_ID+"in the system");
				 }
				 reversalVoucher.setOriginalvcId(originalVoucher.getId());

			 }
			 if(paramMap.get(NAME)==null || ((String) paramMap.get(NAME)).isEmpty())
			 {
				 throw new IllegalArgumentException(NAME+IS_MISSING+"or"+IS_EMPTY);
			 }
			 else
			 {
				 reversalVoucher.setName((String) paramMap.get(NAME));
			 }
			 if(paramMap.get(TYPE)==null || ((String) paramMap.get(TYPE)).isEmpty())
			 {
				 throw new IllegalArgumentException(TYPE+IS_MISSING+"or"+IS_EMPTY);

			 }
			 else
			 {
				 validateVoucherType((String) paramMap.get(TYPE));
				 reversalVoucher.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL);
			 }
			 if(paramMap.get(REVERSAL_VOUCHER_DATE)==null)
			 {
				 SimpleDateFormat sdf=new SimpleDateFormat(DD_MM_YYYY);
				 Date reversalVoucherDate=sdf.parse(sdf.format(originalVoucher.getVoucherDate()));
				 reversalVoucher.setVoucherDate(reversalVoucherDate);
			 }
			 else
			 {
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("vo end REVERSAL_VOUCHER_DAT :"+paramMap.get(REVERSAL_VOUCHER_DATE));
				 reversalVoucher.setVoucherDate((Date)paramMap.get(REVERSAL_VOUCHER_DATE));
			 }
			 //it may be null and taken care in calling api
			 reversalVoucher.setVoucherNumber((String)paramMap.get(REVERSAL_VOUCHER_NUMBER));

		 }
		 return reversalVoucher;
	 }

	 protected void validateMandotyFields(final HashMap<String, Object> headerdetails) {

		}
	 protected void checkMandatoryField(String fieldName,Object value,final HashMap<String, Object>headerdetails ) {
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Filed name :="+fieldName +" Value = :"+value);
		 String vNumGenMode = null;
		 if(fieldName.equals("vouchernumber"))
		 {
			 if(headerdetails.get(VoucherConstant.VOUCHERTYPE)==null)
			 {
				 throw new ValidationException(Arrays.asList(new ValidationError(VoucherConstant.VOUCHERTYPE+ISREQUIRED,VoucherConstant.VOUCHERTYPE+ISREQUIRED)));
			 }else
			 {
				 validateVoucherType(headerdetails.get(VoucherConstant.VOUCHERTYPE).toString());
				 if(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(headerdetails.get(VoucherConstant.VOUCHERTYPE).toString()) ) {
						vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Journal");
				 } else {
						 vNumGenMode= new VoucherTypeForULB().readVoucherTypes(headerdetails.get(VoucherConstant.VOUCHERTYPE).toString());
				 }
				 if(!"Auto".equalsIgnoreCase(vNumGenMode) && (value==null || ((String)value).isEmpty()) && headerdetails.get(VoucherConstant.MODULEID)== null ){
					 throw new ValidationException(Arrays.asList(new ValidationError(SELECT +fieldName,SELECT +fieldName)));
				 }
			 }
		 }else
		 {

			 if(mandatoryFields.contains(fieldName) && (value == null || StringUtils.isEmpty(value.toString())))
				 throw new ValidationException(Arrays.asList(new ValidationError(SELECT +fieldName,SELECT +fieldName)));
		 }
	 }


		@SuppressWarnings("unchecked")
		protected void getHeaderMandateFields() {
			PersistenceService persistenceService=new PersistenceService();
			//persistenceService.setSessionFactory(new SessionFactory());
			List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
			for (AppConfig appConfig : appConfigList) {
				for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
					String value = appConfigVal.getValue();
					String header=value.substring(0, value.indexOf("|"));
					headerFields.add(header);
					String mandate = value.substring(value.indexOf("|")+1);
					if(mandate.equalsIgnoreCase("M")){
						mandatoryFields.add(header);
					}
				}
			}


		}
		
}