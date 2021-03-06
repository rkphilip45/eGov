package org.egov.services.voucher;

import java.math.BigDecimal;
import java.sql.Connection;
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

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgfRecordStatus;
import org.egov.commons.EgwStatus;
import org.egov.commons.VoucherDetail;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.CommonsService;
import org.egov.commons.utils.EntityType;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.masters.services.MastersService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBillSubType;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.eis.service.EisCommonService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.HibernateException;

import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.eGov.src.transactions.VoucherTypeForULB;

public class VoucherService extends PersistenceService<CVoucherHeader, Long>
{
	private static final Logger	LOGGER	= Logger.getLogger(VoucherService.class);
	protected PersistenceService persistenceService;
	protected EisCommonService eisCommonService;
	private BudgetDetailsDAO budgetDetailsDAO;
	private GenericHibernateDaoFactory genericDao;
	private VoucherHibernateDAO voucherHibDAO;
	private CommonsService comm;
	private  UserService userMngr;
	private MastersService masters;
	private SimpleDateFormat FORMATDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	public SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	private SequenceGenerator sequenceGenerator;
	private FinancialYearHibernateDAO financialYearDAO;
	public VoucherService(){

	}
	private EisUtilService eisService;
	private ScriptService scriptExecutionService;
	private PersistenceService<EgBillregister, Long> billRegisterSer;
	public Boundary getBoundaryForUser(CVoucherHeader rv)
	{
		return new EgovCommon().getBoundaryForUser(rv.getCreatedBy());
	}
	public String getEmployeeNameForPositionId(Position pos)throws EGOVRuntimeException
	{
		PersonalInformation pi = null;//This fix is for Phoenix Migration.eisCommonService.getEmployeeforPosition(pos);
		Assignment assignment = null;//This fix is for Phoenix Migration.eisCommonService.getLatestAssignmentForEmployee(pi.getIdPersonalInformation());
		return pi.getEmployeeFirstName()+" ("+assignment.getDesigId().getDesignationName()+")";
	}
	public Department getCurrentDepartment()
	{
		PersonalInformation pi = null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		Assignment assignment= null;//This fix is for Phoenix Migration.eisCommonService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		return (Department)assignment.getDeptId();
	}
	public Department getDepartmentForWfItem(CVoucherHeader cv)
	{
		PersonalInformation pi = null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(cv.getCreatedBy().getId());
		Assignment assignment = null;//This fix is for Phoenix Migration.eisCommonService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
		return assignment.getDeptId();
	}
	public Department getTempDepartmentForWfItem(CVoucherHeader cv,Position position)
	{
		Department d=null;
		//PersonalInformation pi = eisCommonService.getEmpForUserId(cv.getCreatedBy().getId());
	   d = (Department)persistenceService.find("select v.deptId from EmployeeView v left join v.userMaster  as user where v.isPrimary='N' and user.id=?",Integer.valueOf(EGOVThreadLocals.getUserId()));
	return d;
	}
	
	
	
	public Department getDepartmentForUser(User user) 
	{
		return new EgovCommon().getDepartmentForUser(user, eisCommonService, eisService,persistenceService);
    }
	public PersonalInformation getEmpForCurrentUser()
	{
		return null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
	}
	public Position getPositionForWfItem(CVoucherHeader rv)
	{
		return null;//This fix is for Phoenix Migration.eisCommonService.getPositionByUserId(rv.getCreatedBy().getId());
	}
	public boolean budgetaryCheck(EgBillregister billregister)throws ValidationException
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CChartOfAccounts coa=null;
		boolean result=false;
		paramMap.put("asondate", billregister.getBilldate());
		if(billregister.getEgBillregistermis().getScheme()!=null)
			paramMap.put("schemeid", billregister.getEgBillregistermis().getScheme().getId());
		if(billregister.getEgBillregistermis().getSubScheme()!=null)
			paramMap.put("subschemeid", billregister.getEgBillregistermis().getSubScheme().getId());
		if(billregister.getEgBillregistermis().getFieldid()!=null)
			paramMap.put("boundaryid", billregister.getEgBillregistermis().getFieldid().getId());
		if(billregister.getEgBillregistermis().getEgDepartment()!=null)
			paramMap.put("deptid", billregister.getEgBillregistermis().getEgDepartment().getId());
		if(billregister.getEgBillregistermis().getFunctionaryid()!=null)
			paramMap.put("functionaryid", billregister.getEgBillregistermis().getFunctionaryid().getId());
		if(billregister.getEgBillregistermis().getFund()!=null)
			paramMap.put("fundid", billregister.getEgBillregistermis().getFund().getId());
		paramMap.put("mis.budgetcheckreq", billregister.getEgBillregistermis().isBudgetCheckReq());
		for(EgBilldetails detail :billregister.getEgBilldetailes())
		{
			paramMap.put("debitAmt", detail.getDebitamount());
			paramMap.put("creditAmt", detail.getCreditamount());
			coa = (CChartOfAccounts) persistenceService.find(" from CChartOfAccounts where id=?",Long.valueOf(detail.getGlcodeid().toString()));
			paramMap.put("glcode", coa.getGlcode());
			if(detail.getFunctionid()!=null)
				paramMap.put("functionid", Long.valueOf(detail.getFunctionid().toString()));
			paramMap.put("bill", billregister);
			result= budgetDetailsDAO.budgetaryCheckForBill(paramMap);
			if(!result)
				throw new ValidationException("","Budget Check failed for "+coa.getGlcode());
		}
		return result;
	}
	 @SuppressWarnings({ "unchecked", "deprecation" })
		public  Map<String, Object> getDesgByDeptAndTypeAndVoucherDate(String type, String scriptName, Date vouDate, Paymentheader paymentheader) {
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
		 Map<String , Object> map = new HashMap<String, Object>();
		 DesignationMaster designation=null;
		 Double grossAmount =  getJVsGrassAmount(paymentheader);
			Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
			List<String> list = null;/*(List<String>) validScript.eval(Script.createContext("eisCommonServiceBean", eisCommonService,"grossAmount",grossAmount,"userId",
								Integer.valueOf(EGOVThreadLocals.getUserId().trim()),"DATE",new Date(),"type",type,"vouDate",vouDate.getTime(),
								"paymentheader" , paymentheader));*///This fix is for Phoenix Migration.
			Map<String, Object> desgFuncryMap;
			List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
			for (String desgFuncryName : list) {
				if(desgFuncryName.trim().length()!=0 && !desgFuncryName.equalsIgnoreCase("END")){
					desgFuncryMap = new HashMap<String, Object>();
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Designation and Functionary  Name  = "+ desgFuncryName);
					try {
						designation =new DesignationMasterDAO().getDesignationByDesignationName(desgFuncryName.substring(desgFuncryName.indexOf('-')+1));
						desgFuncryMap.put("designationName",(desgFuncryName.substring(0,desgFuncryName.indexOf('-'))).split("~")[0] +"-"+ designation.getDesignationName()  );
						desgFuncryMap.put("designationId",designation.getDesignationId()+"-"+desgFuncryName.substring(0,desgFuncryName.indexOf('-')));
						designationList.add(desgFuncryMap);
					} catch (NoSuchObjectException e) {
						LOGGER.error(e);
					}
				}else if (desgFuncryName.equalsIgnoreCase("END")) {
					map.put("wfitemstate", "END");
				}
				
				
			}
			map.put("designationList", designationList);
			return map;
		}	 
	 public Double getJVsGrassAmount(Paymentheader paymentheader)
		{
		 if(paymentheader!=null){		 
			if(!paymentheader.getVoucherheader().getName().equalsIgnoreCase(FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK)){
				Paymentheader ph = (Paymentheader) persistenceService.find("select ph from Paymentheader ph , Miscbilldetail misc ,CVoucherHeader vh,CGeneralLedger gl where misc.payVoucherHeader = ph.voucherheader and misc.billVoucherHeader = vh and gl.voucherHeaderId = vh and vh.status not in ("+FinancialConstants.CANCELLEDVOUCHERSTATUS+") and gl.debitAmount > 0  and (gl.glcode like '210%' or gl.glcode like '460%') and ph = ? ",paymentheader);
				if(ph!=null){
					return (double) 0;
				}else{
					Double grossAmount = (Double) persistenceService.find("select sum(gl.debitAmount) from Paymentheader ph , Miscbilldetail misc ,CVoucherHeader vh,CGeneralLedger gl where misc.payVoucherHeader = ph.voucherheader and misc.billVoucherHeader = vh and gl.voucherHeaderId = vh and vh.status not in ("+FinancialConstants.CANCELLEDVOUCHERSTATUS+") and gl.debitAmount > 0 and ph = ?",paymentheader);
				
					return grossAmount!=null?grossAmount:(double) 0;
				
					}
			}else{
				Paymentheader ph = (Paymentheader) persistenceService.find("select ph from Paymentheader ph ,CVoucherHeader vh,CGeneralLedger gl where ph.voucherheader = vh and gl.voucherHeaderId = vh and vh.status not in ("+FinancialConstants.CANCELLEDVOUCHERSTATUS+") and gl.debitAmount > 0 and (gl.glcode like '210%' or gl.glcode like '460%') and ph = ? ",paymentheader);
				if(ph!=null){
					return (double) 0;
				}else{
					Double grossAmount = (Double) persistenceService.find("select sum(gl.debitAmount) from Paymentheader ph ,CVoucherHeader vh,CGeneralLedger gl where ph.voucherheader = vh and gl.voucherHeaderId = vh and vh.status not in ("+FinancialConstants.CANCELLEDVOUCHERSTATUS+") and gl.debitAmount > 0 and ph = ?",paymentheader);
				
					return grossAmount!=null?grossAmount:(double) 0;
				
					}
			}
		 }else{
		return (double) 0;
		 }
			
		}
	public void createVoucherfromPreApprovedVoucher(CVoucherHeader vh){
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","APPROVEDVOUCHERSTATUS");
		final String approvedVoucherStatus = appList.get(0).getValue();
		vh.setStatus(Integer.valueOf(approvedVoucherStatus));
	}
	@SuppressWarnings("unchecked")
	public  List<Map<String, Object>> getJournalVouchers(final CVoucherHeader voucherHeader,Map<String, Object> searchFilterMap) throws EGOVException,ParseException{
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("VoucherService | getJournalVouchers | Start");
		List<CVoucherHeader> vouchers=  voucherHibDAO.getVoucherList(voucherHeader, searchFilterMap);
		Map<String,Object> voucherMap = null;
		List<Map<String, Object>> voucherList = new ArrayList<Map<String,Object>>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherList size = "+voucherList.size());
		for(CVoucherHeader voucherheader :vouchers)
		{
			voucherMap = new HashMap<String,Object>();
			BigDecimal amt =BigDecimal.ZERO;
			voucherMap.put("id", voucherheader.getId());
			voucherMap.put("cgn", voucherheader.getCgn());
			voucherMap.put("vouchernumber", voucherheader.getVoucherNumber());
			voucherMap.put("type", voucherheader.getType());
			voucherMap.put("voucherdate", voucherheader.getVoucherDate());
			voucherMap.put("fundname", voucherheader.getFundId().getName());
			Set<VoucherDetail> vDetailSet =voucherheader.getVoucherDetail();
			for(VoucherDetail detail:vDetailSet)
			{
				amt = amt.add(detail.getDebitAmount());
			}
			voucherMap.put("amount", amt);
			if(voucherheader.getStatus() != null){
				voucherMap.put("status", (voucherheader.getStatus()==0?(voucherheader.getIsConfirmed()==0?"UnConfirmed":"Confirmed"):(voucherheader.getStatus()==1?"Reversed":(voucherheader.getStatus()==2?"Reversal":""))));
			}
			
			voucherList.add(voucherMap);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Total number of vouchers = "+voucherList.size());
		return voucherList;
		
		
	}
	
	public Map<String, Object> getVoucherInfo(final Long voucherId){
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("VoucherService | getVoucherDetails | Start");
		Map<String, Object> voucherMap = new HashMap<String, Object>();
		 CVoucherHeader voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?",voucherId);
		voucherMap.put(Constants.VOUCHERHEADER, voucherHeader);
		List<CGeneralLedger> glList  = voucherHibDAO.getGLInfo(voucherHeader.getId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("total number of general ledger entry "+glList.size());
		List<VoucherDetails> billDetailslist =  new ArrayList<VoucherDetails>();
		List<VoucherDetails> subLedgerlist = new ArrayList<VoucherDetails>();
		VoucherDetails voucherDetail ;
		VoucherDetails subLedgerDetail ;
		BigDecimal crAmount=BigDecimal.ZERO;
		BigDecimal drAmount=BigDecimal.ZERO;
		try {
			for (CGeneralLedger generalLedger : glList) {
				voucherDetail = new VoucherDetails();
				if(null != generalLedger.getFunctionId()){
					voucherDetail.setFunctionIdDetail(Long.valueOf((generalLedger.getFunctionId().toString())));
					voucherDetail.setFunctionDetail((comm.getFunctionById(Long.valueOf(generalLedger.getFunctionId().toString()))).getName());
				}
				voucherDetail.setGlcodeIdDetail(generalLedger.getGlcodeId().getId());
				voucherDetail.setGlcodeDetail(generalLedger.getGlcodeId().getGlcode());
				voucherDetail.setAccounthead(comm.getCChartOfAccountsById(generalLedger.getGlcodeId().getId()).getName());
				 drAmount=new BigDecimal(generalLedger.getDebitAmount());
				 crAmount=new BigDecimal(generalLedger.getCreditAmount());
				voucherDetail.setDebitAmountDetail(drAmount.setScale(2,BigDecimal.ROUND_HALF_UP));
				voucherDetail.setCreditAmountDetail(crAmount.setScale(2,BigDecimal.ROUND_HALF_UP));
				billDetailslist.add(voucherDetail);
				
				List<CGeneralLedgerDetail> gledgerDetailList = voucherHibDAO.getGeneralledgerdetail(Integer.valueOf(generalLedger.getId().toString()));
				
					for (CGeneralLedgerDetail gledgerDetail : gledgerDetailList) {
						subLedgerDetail = new VoucherDetails();
						subLedgerDetail.setAmount(gledgerDetail.getAmount().setScale(2));
						subLedgerDetail.setGlcode(comm.getCChartOfAccountsById(generalLedger.getGlcodeId().getId()));
						subLedgerDetail.setSubledgerCode(generalLedger.getGlcodeId().getGlcode());
						Accountdetailtype accountdetailtype = voucherHibDAO.getAccountDetailById(gledgerDetail.getDetailTypeId());
						subLedgerDetail.setDetailType(accountdetailtype);
						subLedgerDetail.setDetailTypeName(accountdetailtype.getName());
						EntityType entity= voucherHibDAO.getEntityInfo(gledgerDetail.getDetailKeyId(),accountdetailtype.getId());
						subLedgerDetail.setDetailCode(entity.getCode());
						subLedgerDetail.setDetailKeyId(gledgerDetail.getDetailKeyId());
						subLedgerDetail.setDetailKey(entity.getName());
						subLedgerDetail.setFunctionDetail(generalLedger.getFunctionId()!=null?generalLedger.getFunctionId().toString():"0");
						subLedgerlist.add(subLedgerDetail);
					}
					
				
				
			}
		} catch (HibernateException e) {
			LOGGER.error("Exception occured in VoucherSerive |getVoucherInfo "+e);
		}
		catch (Exception e) {
			LOGGER.error("Exception occured in VoucherSerive |getVoucherInfo "+e);
		}
	
		voucherMap.put(Constants.GLDEATILLIST,billDetailslist);
		/**
		 * create empty sub ledger row 
		 */
		if(subLedgerlist.isEmpty()){
			subLedgerlist.add(new VoucherDetails());
		}
		voucherMap.put("subLedgerDetail",subLedgerlist);
		return voucherMap;
		
	}
	
	@SuppressWarnings("deprecation")
	public CVoucherHeader updateVoucherHeader(CVoucherHeader voucherHeader){
		String voucherNumType = voucherHeader.getType();
		return updateVoucherHeader(voucherHeader, voucherNumType);
	}
	@SuppressWarnings("deprecation")
	public CVoucherHeader updateVoucherHeader(CVoucherHeader voucherHeader,final VoucherTypeBean voucherTypeBean){
		String voucherNumType = voucherTypeBean.getVoucherNumType();
		if(voucherTypeBean.getVoucherNumType()==null)
		{
			voucherNumType=voucherHeader.getType();
		}
		 return updateVoucherHeader(voucherHeader, voucherNumType);
	}

	public CVoucherHeader updateVoucherHeader(CVoucherHeader voucherHeader,String voucherNumType) {
		PersistenceService<CVoucherHeader, Long> vHeaderService;
		 vHeaderService = new PersistenceService<CVoucherHeader, Long>();
		// vHeaderService.setSessionFactory(new SessionFactory());
		 vHeaderService.setType(CVoucherHeader.class);
		 CVoucherHeader existingVH=null;
		try {
			if(voucherHeader.getCgn()!=null && !voucherHeader.getCgn().isEmpty())
				{
				existingVH = comm.getVoucherHeadersByCGN(voucherHeader.getCgn());
				}
				else if(voucherHeader.getId()!=null && voucherHeader.getId()!=-1)
				{
					existingVH=find("from CVoucherHeader where id=?",voucherHeader.getId());
				}
			existingVH = getUpdatedVNumCGVN(existingVH,voucherHeader,voucherNumType);
			//existingVH.setModifiedDate(new Date());
			//existingVH.setModifiedBy(userMngr.getUserById(Integer.valueOf(EGOVThreadLocals.getUserId())));
			existingVH.setFundId(voucherHeader.getFundId());
			existingVH.getVouchermis().setDepartmentid(voucherHeader.getVouchermis().getDepartmentid());
			existingVH.getVouchermis().setFunction(voucherHeader.getVouchermis().getFunction());
			existingVH.getVouchermis().setSchemeid(voucherHeader.getVouchermis().getSchemeid());
			existingVH.getVouchermis().setSubschemeid(voucherHeader.getVouchermis().getSubschemeid());
			existingVH.getVouchermis().setFunctionary(voucherHeader.getVouchermis().getFunctionary());
			existingVH.getVouchermis().setDivisionid(voucherHeader.getVouchermis().getDivisionid());
			existingVH.getVouchermis().setFundsource(voucherHeader.getVouchermis().getFundsource());
			existingVH.setVoucherDate(voucherHeader.getVoucherDate());
			existingVH.setDescription(voucherHeader.getDescription());
			vHeaderService.update(existingVH);
		} catch (HibernateException e) {
				LOGGER.error(e);
				throw new HibernateException("Exception occured in voucher service while updating voucher header"+e);
		}catch (EGOVRuntimeException e) {
			LOGGER.error(e);

			throw new EGOVRuntimeException("Exception occured in voucher service while updating voucher header"+e);
		}
		return existingVH;
	}
	private CVoucherHeader getupdatedVNumCGVN(final CVoucherHeader existingVH,final CVoucherHeader voucherHeader,final VoucherTypeBean voucherTypeBean){
		String voucherNumType = voucherTypeBean.getVoucherNumType();
		if(voucherTypeBean.getVoucherNumType()==null)
		{
			voucherNumType=voucherHeader.getType();
		}
		return getUpdatedVNumCGVN(existingVH, voucherHeader, voucherNumType);
	}

	public CVoucherHeader getUpdatedVNumCGVN(final CVoucherHeader existingVH,final CVoucherHeader voucherHeader, String voucherNumType) {
		//CommonMethodsI cmImpl=new CommonMethodsImpl();
		EGovernCommon eGovernCommon = new EGovernCommon();
		String autoVoucherType = null;
		Connection conn = null;
		if(voucherNumType.equalsIgnoreCase("Journal Voucher"))
		{
			voucherNumType="Journal";
			//autoVoucherType = FinancialConstants.JOURNAL_VOUCHERNO_TYPE;
		}//else if(voucherNumType.equalsIgnoreCase("Contra")){
		//	autoVoucherType = FinancialConstants.CONTRA_VOUCHERNO_TYPE;
			
		//}
		autoVoucherType = existingVH.getVoucherNumber().substring((Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH) - Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_SUBLENGTH)),
						  Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("autoVoucherType FOR MODIFIED VOUCHER :"+ autoVoucherType);
		
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes(voucherNumType);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("new fund id :"+ voucherHeader.getFundId().getId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("old fund id :"+ existingVH.getFundId().getId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("new voucher date :"+ voucherHeader.getVoucherDate());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("old voucher date :"+ existingVH.getVoucherDate());
		// generating an new voucher number If fund or voucher date is modified.
		// change voucher number only if fund is changed 
		// no need to change voucher number if onlydate is changed since vouchernumber-sequence is for the year not for month
		try {
			String fiscalPeriodIdStr = eGovernCommon.getFiscalPeriod(sdf.format(voucherHeader.getVoucherDate()), conn);
			if(null == fiscalPeriodIdStr){
				 throw new EGOVRuntimeException("Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "+ fiscalPeriodIdStr);
			 }
			voucherHeader.setFiscalPeriodId(Integer.parseInt(fiscalPeriodIdStr));
			if( !voucherHeader.getFundId().equals(existingVH.getFundId())){
				
				//String vDate = FORMATDDMMYYYY.format(voucherHeader.getVoucherDate());
				String strVoucherNumber = VoucherHelper.getGeneratedVoucherNumber(voucherHeader.getFundId().getId(), autoVoucherType, voucherHeader.getVoucherDate(), vNumGenMode, voucherHeader.getVoucherNumber());
				existingVH.setVoucherNumber(strVoucherNumber);
				 String vType=voucherHeader.getFundId().getIdentifier()+"/"+autoVoucherType+"/CGVN";
				 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher type  : "+ vType);
				 String	eg_voucher=eGovernCommon.getEg_Voucher(vType,existingVH.getFiscalPeriodId().toString(),conn);
				 for(int i=eg_voucher.length();i<10;i++)
				 {
					eg_voucher="0"+eg_voucher;  
				 }
				 existingVH.setCgvn(vType+eg_voucher);
				
			}else if(! voucherHeader.getVoucherDate().equals(existingVH.getVoucherDate()))
			{
				// A financial Year can have multiple fiscalPeriod  so comparing previous and new financial year id 
				CFiscalPeriod fiscalPeriod= (CFiscalPeriod) persistenceService.find(" from CFiscalPeriod where id=?", Long.valueOf(fiscalPeriodIdStr));
				String financialYearId = null;//This fix is for Phoenix Migration.financialYearDAO.getFinancialYearId(Constants.DDMMYYYYFORMAT2.format(existingVH.getVoucherDate()));
				CFinancialYear financialYear = (CFinancialYear) financialYearDAO.getFinancialYearById(Long.valueOf(financialYearId));
				if( existingVH.getFiscalPeriodId().equals(voucherHeader.getFiscalPeriodId()) && fiscalPeriod.getFinancialYearId().longValue()==financialYear.getId().longValue()){
				String vDate = Constants.DDMMYYYYFORMAT2.format(voucherHeader.getVoucherDate());
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Number  : "+ existingVH.getVoucherNumber());
				String strTempVoucherNumber=existingVH.getVoucherNumber();
				String strVoucherNumber="";
				String newDate[]=vDate.split("/");
				String voucherArr[] = strTempVoucherNumber.split("/");
				strVoucherNumber=voucherArr[0]+"/"+voucherArr[1]+"/"+voucherArr[2]+"/"+newDate[1]+"/"+voucherArr[4];                                                        
				existingVH.setVoucherNumber(strVoucherNumber);
			}else{
				String strVoucherNumber = VoucherHelper.getGeneratedVoucherNumber(voucherHeader.getFundId().getId(), autoVoucherType, voucherHeader.getVoucherDate(), vNumGenMode, voucherHeader.getVoucherNumber());
				existingVH.setVoucherNumber(strVoucherNumber);
				String vType=voucherHeader.getFundId().getIdentifier()+"/"+autoVoucherType+"/CGVN";
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher type  : "+ vType);
				String	eg_voucher=eGovernCommon.getEg_Voucher(vType,existingVH.getFiscalPeriodId().toString(),conn);
				for(int i=eg_voucher.length();i<10;i++)
				{
					eg_voucher="0"+eg_voucher;  
				}
				existingVH.setCgvn(vType+eg_voucher);   
				
			  }
			}
				// If only the voucher number is modified then just appending the manual voucher number
			else if("Manual".equalsIgnoreCase(vNumGenMode) && ! existingVH.getVoucherNumber().substring(Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH)
                    ).equalsIgnoreCase(voucherHeader.getVoucherNumber())){
				String strVoucherNumber = VoucherHelper.getGeneratedVoucherNumber(voucherHeader.getFundId().getId(), autoVoucherType,
						voucherHeader.getVoucherDate(), vNumGenMode, voucherHeader.getVoucherNumber());
				//existingVH.setVoucherNumber(existingVH.getVoucherNumber().substring(0,
				//		                    Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH))+voucherHeader.getVoucherNumber());
				existingVH.setVoucherNumber(strVoucherNumber);
				
			}
			conn.close();
		}
		catch (Exception e) {
			LOGGER.error(e);
			throw new EGOVRuntimeException("Exception occured while getting upadetd voucher number and cgvn number"+e);
	}
	
	return existingVH;
	}
	
	public void deleteVDByVHId(final Object voucherHeaderId){
		voucherHibDAO.deleteVoucherDetailByVHId(voucherHeaderId);
	}
	public void deleteGLDetailByVHId(final Object voucherHeaderId){
		voucherHibDAO.deleteGLDetailByVHId(voucherHeaderId);
	}
	/**
	 *  Post into Voucher detail and create transaction list for posting into GL.
	 * @param billDetailslist
	 * @param subLedgerlist
	 * @param voucherHeader
	 * @return
	 */
	public List<Transaxtion> postInTransaction(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist,CVoucherHeader voucherHeader){
		 List<Transaxtion> transaxtionList = new ArrayList<Transaxtion>();
		 int lineId=1;
		 String accDetailFunc="";
		 String detailedFunc ="";     
		 List<String> repeatedglCodes = VoucherHelper.getRepeatedGlcodes(billDetailslist);
		try {
			for (VoucherDetails accountDetails : billDetailslist) {
				 String glcodeId = accountDetails.getGlcodeIdDetail().toString();
				 VoucherDetail voucherDetail = new VoucherDetail();
				 voucherDetail.setLineId(lineId++);
				 voucherDetail.setVoucherHeaderId(voucherHeader);
				 voucherDetail.setGlCode(accountDetails.getGlcodeDetail());
				 voucherDetail.setAccountName(accountDetails.getAccounthead());
				 voucherDetail.setDebitAmount(accountDetails.getDebitAmountDetail());
				 voucherDetail.setCreditAmount(accountDetails.getCreditAmountDetail());
				 voucherDetail.setNarration(voucherHeader.getVouchermis().getNarration());
				 voucherHibDAO.postInVoucherDetail(voucherDetail);
				 
				 Transaxtion transaction = new Transaxtion();
				 transaction.setGlCode(accountDetails.getGlcodeDetail());
				 transaction.setGlName(accountDetails.getAccounthead());
				 transaction.setVoucherLineId(String.valueOf(voucherDetail.getId()));
				 transaction.setVoucherHeaderId(voucherHeader.getId().toString());
				 transaction.setCrAmount(accountDetails.getCreditAmountDetail().toString());
				 transaction.setDrAmount(accountDetails.getDebitAmountDetail().toString());
				 if(null != accountDetails.getFunctionIdDetail()){
					 transaction.setFunctionId(accountDetails.getFunctionIdDetail().toString());
				 }
				 /*if(null!=voucherHeader.getIsRestrictedtoOneFunctionCenter() && voucherHeader.getIsRestrictedtoOneFunctionCenter()){
				 	 if(null != voucherHeader.getFunctionId()){
					 transaction.setFunctionId(voucherHeader.getFunctionId().toString());
				 	 }	
					 if(null != voucherHeader.getVouchermis().getFunction()){
					 transaction.setFunctionId(voucherHeader.getVouchermis().getFunction().getId().toString());
				 }
				 }else{
					  accDetailFunc = accountDetails.getFunctionIdDetail()!=null?accountDetails.getFunctionIdDetail().toString():"0";
				 }*/
				 ArrayList reqParams=new ArrayList();
				 for (VoucherDetails subledgerDetails: subLedgerlist) {
					 
					 String detailGlCode = subledgerDetails.getGlcode().getId().toString();
					 /*if(null!=voucherHeader.getIsRestrictedtoOneFunctionCenter() && voucherHeader.getIsRestrictedtoOneFunctionCenter()){
					
					  detailedFunc = voucherHeader.getFunctionId().toString();
					 }else{
						  detailedFunc = subledgerDetails.getFunctionDetail();
					 }*///This fix is for Phoenix Migration.
					 String detailtypeid = subledgerDetails.getDetailType().getId().toString();
					 if(glcodeId.equals(detailGlCode) && (repeatedglCodes.contains(glcodeId)?accDetailFunc.equals(detailedFunc):true)){
						 TransaxtionParameter reqData=new TransaxtionParameter();
						 Accountdetailtype adt = masters.getAccountdetailtypeById(Integer.valueOf(detailtypeid));
							reqData.setDetailName(adt.getAttributename());
							reqData.setGlcodeId(detailGlCode);
							reqData.setDetailAmt(subledgerDetails.getAmount().toString());
							reqData.setDetailKey(subledgerDetails.getDetailKeyId().toString());
							reqData.setDetailTypeId(detailtypeid);
							reqParams.add(reqData);
					 }
					 
				 }
				 if(reqParams!=null && reqParams.size()>0 ){
					 transaction.setTransaxtionParam(reqParams);
				 }
				 transaxtionList.add(transaction);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while posting data into voucher detail and transaction");
			throw new EGOVRuntimeException("Exception occured while posting data into voucher detail and transaction"+e.getMessage());
		}
	
		return transaxtionList;
		
	}
	public void setVoucherHeaderDetails(CVoucherHeader voucherHeader,VoucherTypeBean voucherTypeBean) throws Exception{
		EGovernCommon cm = new EGovernCommon();
		//CommonMethodsI cmImpl=new CommonMethodsImpl();
		Connection conn = null;
		voucherHeader.setName(voucherTypeBean.getVoucherName());
		voucherHeader.setType(voucherTypeBean.getVoucherType());
		String  vNumGenMode = null;
		if(null != voucherHeader.getType() && FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(voucherHeader.getType()) ){
			vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Journal");
		}else{
			 vNumGenMode= new VoucherTypeForULB().readVoucherTypes(voucherTypeBean.getVoucherNumType());
		}
		String autoVoucherType =EGovConfig.getProperty(FinancialConstants.APPLCONFIGNAME,voucherTypeBean.getVoucherNumType().toLowerCase(),"",FinancialConstants.CATEGORYFORVNO);
		String vocuherNumber= VoucherHelper.getGeneratedVoucherNumber(voucherHeader.getFundId().getId(), autoVoucherType, voucherHeader.getVoucherDate(), vNumGenMode, voucherHeader.getVoucherNumber());
		voucherHeader.setVoucherNumber(vocuherNumber);
		
	}
	public CVoucherHeader postIntoVoucherHeader(CVoucherHeader voucherHeader,final VoucherTypeBean voucherTypeBean) throws Exception{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("start | insertIntoVoucherHeader");
		EGovernCommon cm = new EGovernCommon();
		//CommonMethodsI cmImpl=new CommonMethodsImpl();
		Connection conn = null;
		voucherHeader.setName(voucherTypeBean.getVoucherName());
		voucherHeader.setType(voucherTypeBean.getVoucherType());
		String  vNumGenMode = null;
		if(null != voucherHeader.getType() && FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(voucherHeader.getType()) ){
			vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Journal");
		}else{
			 vNumGenMode= new VoucherTypeForULB().readVoucherTypes(voucherTypeBean.getVoucherNumType());
		}
		String autoVoucherType =EGovConfig.getProperty(FinancialConstants.APPLCONFIGNAME,voucherTypeBean.getVoucherNumType().toLowerCase(),"",FinancialConstants.CATEGORYFORVNO);
		String vocuherNumber= VoucherHelper.getGeneratedVoucherNumber(voucherHeader.getFundId().getId(), autoVoucherType, voucherHeader.getVoucherDate(), vNumGenMode, voucherHeader.getVoucherNumber());
		voucherHeader.setVoucherNumber(vocuherNumber);
		/*
		if("Auto".equalsIgnoreCase(vNumGenMode)){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Generating auto voucher number");
			String vDate = Constants.DDMMYYYYFORMAT2.format(voucherHeader.getVoucherDate());
			//String vn1 = getGeneratedVoucherNumber(voucherHeader.getFundId().getId(), autoVoucherType, voucherHeader.getVoucherDate());
			voucherHeader.setVoucherNumber(cmImpl.getTxnNumber(voucherHeader.getFundId().getId().toString(),autoVoucherType,vDate,conn));
		} else {
			Query query=HibernateUtil.getCurrentSession().createQuery("select f.identifier from Fund f where id=:fundId");
			query.setInteger("fundId", voucherHeader.getFundId().getId());
			String fundIdentifier = query.uniqueResult().toString();
			//String vn2 = getFormattedManualVoucherNumber(fundIdentifier, autoVoucherType, voucherHeader.getVoucherNumber());
			voucherHeader.setVoucherNumber(new StringBuffer().append(fundIdentifier).append(autoVoucherType).
					append(voucherHeader.getVoucherNumber()).toString());
	    }
		*/
		 try {
			 String vdt = Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate());
			 String fiscalPeriod=null;
			 fiscalPeriod=cm.getFiscalPeriod(vdt, conn);
			 if(null == fiscalPeriod){
					throw new EGOVRuntimeException("Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "+ fiscalPeriod);
				}
			 voucherHeader.setFiscalPeriodId(Integer.valueOf(fiscalPeriod));  
			 String cgn=voucherTypeBean.getCgnType()+cm.getCGNumber();	
			 voucherHeader.setCgn(cgn);
			 voucherHeader.setCgDate(new Date()); 
			 
			 //String	vType=voucherHeader.getVoucherNumber().substring(0,Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			 String vType=voucherHeader.getFundId().getIdentifier()+"/"+getCgnType(voucherHeader.getType())+"/CGVN";
			 String	eg_voucher=cm.getEg_Voucher(vType,fiscalPeriod,conn);
			 
			 for(int i=eg_voucher.length();i<10;i++)
			 {
				eg_voucher="0"+eg_voucher;  
			 }
			 String	cgNum=vType+eg_voucher;
			 voucherHeader.setCgvn(cgNum);
			 voucherHeader.setEffectiveDate(new Date());
			 if(!cm.isUniqueVN(voucherHeader.getVoucherNumber(),vdt, conn)) 
					throw new EGOVRuntimeException("Duplicate Voucher Number"); 
			// vh.setCreatedBy(userMngr.getUserById(Integer.valueOf(EGOVThreadLocals.getUserId())));
			 voucherHeader.getVouchermis().setVoucherheaderid(voucherHeader);
			 voucherHeader.setStatus(FinancialConstants.PREAPPROVEDVOUCHERSTATUS);
			 AppConfig appConfig= (AppConfig) persistenceService.find("from AppConfig where key_name =?", "JournalVoucher_ConfirmonCreate");
			 if(null != appConfig && null!= appConfig.getAppDataValues() ){
				 for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
					 voucherHeader.setIsConfirmed(Integer.valueOf(appConfigVal.getValue()));
				}
			 }
			 persist(voucherHeader);
			 if(! voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)){
				 StringBuffer sourcePath = new StringBuffer();
				 sourcePath.append(voucherHeader.getVouchermis().getSourcePath()).append(voucherHeader.getId().toString());
				 voucherHeader.getVouchermis().setSourcePath(sourcePath.toString());
				
				 update(voucherHeader);
			 }
			
		}
		catch (EGOVRuntimeException e) {   
			LOGGER.error(e);
			throw new EGOVRuntimeException(e.getMessage());
		}
		catch (Exception e) {
			LOGGER.error(e);
			throw new EGOVRuntimeException(e.getMessage());
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("End | insertIntoVoucherHeader");
		return voucherHeader;
	}
	
	 enum voucherTypeEnum {
		 JOURNALVOUCHER, CONTRA, RECEIPT,PAYMENT;
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
	
	 public void insertIntoRecordStatus(final CVoucherHeader voucherHeader) throws Exception{
		 try {
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
		} catch (HibernateException he) {
			LOGGER.error(he.getMessage());
			throw new HibernateException(he);
		}  
		catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new HibernateException(e);
		}
		
	 }
	 @SuppressWarnings({ "unchecked", "deprecation" })
		public  Map<String, Object> getDesgByDeptAndType(String type, String scriptName) {
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
		 Map<String , Object> map = new HashMap<String, Object>();
		 DesignationMaster designation=null;
			Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
			List<String> list = null;/*(List<String>) validScript.eval(Script.createContext("eisCommonServiceBean", eisCommonService,"userId",
								Integer.valueOf(EGOVThreadLocals.getUserId().trim()),"DATE",new Date(),"type",type));*/
			Map<String, Object> desgFuncryMap;
			List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
			for (String desgFuncryName : list) {
				if(desgFuncryName.trim().length()!=0 && !desgFuncryName.equalsIgnoreCase("END")){
					desgFuncryMap = new HashMap<String, Object>();
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Designation and Functionary  Name  = "+ desgFuncryName);
					try {
						designation =new DesignationMasterDAO().getDesignationByDesignationName(desgFuncryName.substring(desgFuncryName.indexOf('-')+1));
						desgFuncryMap.put("designationName", designation.getDesignationName());
						desgFuncryMap.put("designationId",designation.getDesignationId()+"-"+desgFuncryName.substring(0,desgFuncryName.indexOf('-')));
						designationList.add(desgFuncryMap);
					} catch (NoSuchObjectException e) {
						LOGGER.error(e);
					}
				}else if (desgFuncryName.equalsIgnoreCase("END")) {
					map.put("wfitemstate", "END");
				}
				
				
			}
			map.put("designationList", designationList);
			return map;
		}
	 
	 @SuppressWarnings({ "unchecked", "deprecation" })
		public  Map<String, Object> getDesgByDeptAndTypeAndvouDate(String type, String scriptName, Date vouDate) {
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
		 Map<String , Object> map = new HashMap<String, Object>();
		 DesignationMaster designation=null;
			Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
			List<String> list = null;/*(List<String>) validScript.eval(Script.createContext("eisCommonServiceBean", eisCommonService,"userId",
								Integer.valueOf(EGOVThreadLocals.getUserId().trim()),"DATE",new Date(),"type",type,"vouDate",vouDate.getTime()));*/
			Map<String, Object> desgFuncryMap;
			List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
			for (String desgFuncryName : list) {
				if(desgFuncryName.trim().length()!=0 && !desgFuncryName.equalsIgnoreCase("END")){
					desgFuncryMap = new HashMap<String, Object>();
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Designation and Functionary  Name  = "+ desgFuncryName);
					try {
						designation =new DesignationMasterDAO().getDesignationByDesignationName(desgFuncryName.substring(desgFuncryName.indexOf('-')+1));
						desgFuncryMap.put("designationName", designation.getDesignationName());
						desgFuncryMap.put("designationId",designation.getDesignationId()+"-"+desgFuncryName.substring(0,desgFuncryName.indexOf('-')));
						designationList.add(desgFuncryMap);
					} catch (NoSuchObjectException e) {
						LOGGER.error(e);
					}
				}else if (desgFuncryName.equalsIgnoreCase("END")) {
					map.put("wfitemstate", "END");
				}
				
				
			}
			map.put("designationList", designationList);
			return map;
		}
	 
	 @SuppressWarnings({ "unchecked", "deprecation" })
		public  Map<String, Object> getDesgByDeptAndTypeAndVoudate(String type, String scriptName, Date vouDate) {
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
		 Map<String , Object> map = new HashMap<String, Object>();
		 DesignationMaster designation=null;
			Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
			List<String> list =null;/* (List<String>) validScript.eval(Script.createContext("eisCommonServiceBean", eisCommonService,"userId",
								Integer.valueOf(EGOVThreadLocals.getUserId().trim()),"DATE",new Date(),"type",type,"vouDate",vouDate.getTime()));*/
			Map<String, Object> desgFuncryMap;
			List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
			for (String desgFuncryName : list) {
				if(desgFuncryName.trim().length()!=0 && !desgFuncryName.equalsIgnoreCase("END")){
					desgFuncryMap = new HashMap<String, Object>();
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Designation and Functionary  Name  = "+ desgFuncryName);
					try {
						designation =new DesignationMasterDAO().getDesignationByDesignationName(desgFuncryName.substring(desgFuncryName.indexOf('-')+1));
						desgFuncryMap.put("designationName", designation.getDesignationName());
						desgFuncryMap.put("designationId",designation.getDesignationId()+"-"+desgFuncryName.substring(0,desgFuncryName.indexOf('-')));
						designationList.add(desgFuncryMap);
					} catch (NoSuchObjectException e) {
						LOGGER.error(e);
					}
				}else if (desgFuncryName.equalsIgnoreCase("END")) {
					map.put("wfitemstate", "END");
				}
				
				
			}
			map.put("designationList", designationList);
			return map;
		}
	 
	 @SuppressWarnings({ "unchecked", "deprecation" })
		public  Map<String, Object> getDesgBYPassingWfItem(String scriptName,Object wfitem , Integer deptId) {
		 if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | getDesgUserByDeptAndDesgName | Start");
		 Map<String , Object> map = new HashMap<String, Object>();
		 DesignationMaster designation=null;
			Script validScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME,scriptName).get(0);
			List<String> list = null;/*(List<String>) validScript.eval(Script.createContext("eisCommonServiceBean", eisCommonService,"userId",
								Integer.valueOf(EGOVThreadLocals.getUserId().trim()),"DATE",new Date(),"type",type,
								"wfitem",wfitem,"deptId",deptId,"persistenceService",persistenceService));*/
			Map<String, Object> desgFuncryMap;
			List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
			for (String desgFuncryName : list) {
				if(desgFuncryName.trim().length()!=0 && !desgFuncryName.equalsIgnoreCase("END") 
						&& !desgFuncryName.equalsIgnoreCase("ANYFUNCTIONARY-ANYDESG")){
					desgFuncryMap = new HashMap<String, Object>();
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Designation and Functionary  Name  = "+ desgFuncryName);
					try {
						designation =new DesignationMasterDAO().getDesignationByDesignationName(desgFuncryName.substring(desgFuncryName.indexOf('-')+1));
						desgFuncryMap.put("designationName", designation.getDesignationName());
						desgFuncryMap.put("designationId",designation.getDesignationId()+"-"+desgFuncryName.substring(0,desgFuncryName.indexOf('-')));
						designationList.add(desgFuncryMap);
						map.put("wfitemstate", desgFuncryName);
					} catch (NoSuchObjectException e) {
						LOGGER.error(e);
					}
				}else if(desgFuncryName.equalsIgnoreCase("ANYFUNCTIONARY-ANYDESG")){
					designationList = getAllDesgByAndDept(deptId,desgFuncryName);
					map.put("wfitemstate", desgFuncryName);
				}else if(desgFuncryName.equalsIgnoreCase("END")){
					map.put("wfitemstate", desgFuncryName);
				}
				
			}
			map.put("designationList", designationList);
			return map;
		}
	 
	 
	 
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllDesgByAndDept(Integer deptId,String desgfuncry){
		Map<String, Object> desgMap;
		List<Map<String, Object>> desgList = new ArrayList<Map<String,Object>>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("VoucherService | getAllDesgByFuncryAndDept | Start");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("department id = "+ deptId);
		StringBuffer query = new StringBuffer(200);
		query.append("select DISTINCT desg.designationName,desg.designationId from DesignationMaster desg , EmployeeView ev where ").
		append(" desg.designationId=ev.desigId.designationId and ev.deptId.id = ? and ev.userMaster is not null");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getAllDesgByFuncryAndDept Query : = "+ query.toString());
		List<Object[]> list = persistenceService.findAllBy(query.toString(), deptId);
		for (Object[] objects : list) {
			desgMap = new HashMap<String, Object>();
			desgMap.put("designationName", objects[0]);
			desgMap.put("designationId", objects[1]+"-"+desgfuncry.substring(0,desgfuncry.indexOf('-'))); 
			desgList.add(desgMap);
			
		}
		return desgList;
		 
		 
	 }
public List<EmployeeView>  getUserByDeptAndDesgName(String departmentId,String designationId,String functionaryId){
	
	
	HashMap<String,String> paramMap = new HashMap<String, String>();
	
	paramMap.put("departmentId",departmentId);
	paramMap.put("designationId", designationId);
	paramMap.put("functionaryId",functionaryId);
	List<EmployeeView> empInfoList = null;//This fix is for Phoenix Migration. eisService.getEmployeeInfoforPosition(paramMap); 
	return empInfoList;
}
/**
 * 
 * @description - creates the bill register object for the vouchers except expense JV.
 * @param billDetailslist -having account details info.
 * @param subLedgerlist - having sub ledger details info.
 * @param voucherHeader - header and misc details info.
 * @param voucherTypeBean - voucher type info.
 * @param totalBillAmount - total bill amount.
 * @return - returns the created egbillregister object.
 * @throws Exception
 */
public EgBillregister createBillForVoucherSubType(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist,CVoucherHeader voucherHeader,
		VoucherTypeBean voucherTypeBean,BigDecimal totalBillAmount) throws Exception{
	if(LOGGER.isDebugEnabled())     LOGGER.debug("VoucherService | createBillForVoucherSubType | Start");
	EgBillregister egBillregister = new EgBillregister();
	try {
		egBillregister.setBillstatus("APPROVED");
		EgwStatus egwstatus = null;
		if("Contractor Journal".equalsIgnoreCase(voucherTypeBean.getVoucherName())){
			egwstatus =(EgwStatus) persistenceService.find("from EgwStatus where upper(moduletype)=? and upper(description)=?","CONTRACTORBILL","APPROVED");
			egBillregister.setExpendituretype("Works");
		}
		else if("Supplier Journal".equalsIgnoreCase(voucherTypeBean.getVoucherName())){
			egwstatus =(EgwStatus) persistenceService.find("from EgwStatus where upper(moduletype)=? and upper(description)=?","SBILL","APPROVED");
			egBillregister.setExpendituretype("Purchase");
		}else if("Salary Journal".equalsIgnoreCase(voucherTypeBean.getVoucherName())){
			egwstatus =(EgwStatus) persistenceService.find("from EgwStatus where upper(moduletype)=? and upper(description)=?","SALBILL","APPROVED");
			egBillregister.setExpendituretype("Salary");
		}else if("Expense Journal".equalsIgnoreCase(voucherTypeBean.getVoucherName())){
			egwstatus =(EgwStatus) persistenceService.find("from EgwStatus where upper(moduletype)=? and upper(description)=?","EXPENSEBILL","APPROVED");
			egBillregister.setExpendituretype("Expense");
		}
		else if("Pension Journal".equalsIgnoreCase(voucherTypeBean.getVoucherName())){
			egwstatus =(EgwStatus) persistenceService.find("from EgwStatus where upper(moduletype)=? and upper(description)=?","PENSIONBILL","APPROVED");
			egBillregister.setExpendituretype("Pension");
		}
		egBillregister.setStatus(egwstatus);
		if(null != voucherTypeBean.getBillDate()){
			egBillregister.setBilldate(voucherTypeBean.getBillDate());
		}else{
			egBillregister.setBilldate(voucherHeader.getVoucherDate());
		}
		if(null != voucherHeader.getVouchermis().getDivisionid()){
			egBillregister.setFieldid(new BigDecimal(voucherHeader.getVouchermis().getDivisionid().getId().toString()));
		}
		egBillregister.setNarration(voucherHeader.getVouchermis().getNarration());
		egBillregister.setIsactive(true);
		egBillregister.setBilltype("Final Bill");
		egBillregister.setPassedamount(totalBillAmount);
		egBillregister.setBillamount(totalBillAmount);
		
		EgBillregistermis egBillregistermis = new EgBillregistermis();
		egBillregistermis.setFund(voucherHeader.getFundId());
		egBillregistermis.setEgDepartment(voucherHeader.getVouchermis().getDepartmentid());
		egBillregistermis.setFunctionaryid(voucherHeader.getVouchermis().getFunctionary());
		egBillregistermis.setFunction(voucherHeader.getVouchermis().getFunction());                                                           
		egBillregistermis.setFundsource(voucherHeader.getVouchermis().getFundsource());
		egBillregistermis.setScheme(voucherHeader.getVouchermis().getSchemeid());
		egBillregistermis.setSubScheme(voucherHeader.getVouchermis().getSubschemeid());
		egBillregistermis.setNarration(voucherHeader.getVouchermis().getNarration());
		egBillregistermis.setPartyBillDate(voucherTypeBean.getPartyBillDate());
		egBillregistermis.setPayto(voucherTypeBean.getPartyName());
		egBillregistermis.setPartyBillNumber(voucherTypeBean.getPartyBillNum());
		egBillregistermis.setFieldid(voucherHeader.getVouchermis().getDivisionid());
		if(voucherTypeBean.getVoucherNumType().equalsIgnoreCase("fixedassetjv")){
			EgBillSubType egBillSubType = (EgBillSubType)persistenceService.find("from EgBillSubType where name=? and expenditureType=?", "Fixed Asset","Purchase");
			egBillregistermis.setEgBillSubType(egBillSubType);
		}
		egBillregistermis.setLastupdatedtime(new Date());
		egBillregistermis.setVoucherHeader(voucherHeader);
		egBillregister.setEgBillregistermis(egBillregistermis);
		if(null != voucherTypeBean.getBillNum() && StringUtils.isNotEmpty(voucherTypeBean.getBillNum())){
			egBillregister.setBillnumber(voucherTypeBean.getBillNum());
		}else{
			ScriptContext scriptContext = ScriptService.createContext("sequenceGenerator",sequenceGenerator,"bill",egBillregister);
			String billNumber =(String)scriptExecutionService.executeScript("autobillnumber", scriptContext);
			egBillregister.setBillnumber(billNumber);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("VoucherService | createBillForVoucherSubType | Bill number generated :=" + billNumber);
		}
		if(! isBillNumUnique(egBillregister.getBillnumber())){
			throw new ValidationException(Arrays.asList(new ValidationError("bill number","Duplicate Bill Number : "+egBillregister.getBillnumber())));
		}
		egBillregistermis.setEgBillregister(egBillregister);
		
		
		Set<EgBilldetails> egBilldetailes = new HashSet<EgBilldetails>(0);
	    egBilldetailes = prepareBillDetails(egBillregister, billDetailslist,subLedgerlist,voucherHeader,egBilldetailes);
		 egBillregister.setEgBilldetailes(egBilldetailes);
		 
		 	billRegisterSer.persist(egBillregister);
			
			voucherHeader.getVouchermis().setSourcePath("/EGF/voucher/journalVoucherModify!beforeModify.action?voucherHeader.id="+voucherHeader.getId());
			update(voucherHeader);
	}catch (ValidationException e) {
		 List<ValidationError> errors=new ArrayList<ValidationError>();
		 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
		 throw new ValidationException(errors);
	}
	catch (Exception e) {
		 List<ValidationError> errors=new ArrayList<ValidationError>();
		 errors.add(new ValidationError("exp",e.getMessage()));
		 throw new ValidationException(errors);
	}
	if(LOGGER.isDebugEnabled())     LOGGER.debug("VoucherService | createBillForVoucherSubType | End | bill number : = "+ egBillregister.getBillnumber() );
	return egBillregister;
	
}
/**
 * @description - update the bill register objects in the JV modify screen for voucher sub types.
 * @param billDetailslist - account detail info.
 * @param subLedgerlist - Bill Payee details info.
 * @param voucherHeader - bill register and mis info.
 * @param voucherTypeBean - different voucher sub type and bill type info.
 * @param totalBillAmount - total debit amount.
 * @return egBillregister - the updated bill register object.
 * @throws ValidationException
 */
	public EgBillregister updateBillForVSubType(List<VoucherDetails> billDetailslist,List<VoucherDetails> subLedgerlist,CVoucherHeader voucherHeader,
			VoucherTypeBean voucherTypeBean,BigDecimal totalBillAmount) throws ValidationException{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | updateBillForVSubType | Start");
		EgBillregister egBillregister = null;
		try {
			egBillregister =(EgBillregister) persistenceService.find("from EgBillregister br where br.egBillregistermis.voucherHeader.id="+voucherHeader.getId());
			EgBillregistermis egBillregistermis = egBillregister.getEgBillregistermis();
			if(null != voucherTypeBean.getBillDate()){
				egBillregister.setBilldate(voucherTypeBean.getBillDate());
			}else{
				egBillregister.setBilldate(voucherHeader.getVoucherDate());
			}
			if(null != voucherTypeBean.getBillNum() && StringUtils.isNotEmpty(voucherTypeBean.getBillNum()))
			{
				if(!voucherTypeBean.getBillNum().equalsIgnoreCase(egBillregister.getBillnumber()))
				{
					if(! isBillNumUnique(voucherTypeBean.getBillNum())){
						throw new ValidationException(Arrays.asList(new ValidationError("bill number","Duplicate Bill Number : "+voucherTypeBean.getBillNum())));
					}
					egBillregister.setBillnumber(voucherTypeBean.getBillNum());
				}
			}
			
			if(null != voucherHeader.getVouchermis().getDivisionid()){
				egBillregister.setFieldid(new BigDecimal(voucherHeader.getVouchermis().getDivisionid().getId().toString()));
			}
			egBillregister.setNarration(voucherHeader.getVouchermis().getNarration());
			egBillregister.setPassedamount(totalBillAmount);
			egBillregister.setBillamount(totalBillAmount);
			
			
			egBillregistermis.setFund(voucherHeader.getFundId());
			egBillregistermis.setEgDepartment(voucherHeader.getVouchermis().getDepartmentid());
			egBillregistermis.setFunction(voucherHeader.getVouchermis().getFunction());
			egBillregistermis.setFunctionaryid(voucherHeader.getVouchermis().getFunctionary());
			egBillregistermis.setFundsource(voucherHeader.getVouchermis().getFundsource());
			egBillregistermis.setScheme(voucherHeader.getVouchermis().getSchemeid());
			egBillregistermis.setSubScheme(voucherHeader.getVouchermis().getSubschemeid());
			egBillregistermis.setNarration(voucherHeader.getVouchermis().getNarration());
			egBillregistermis.setPartyBillDate(voucherTypeBean.getPartyBillDate());
			egBillregistermis.setPayto(voucherTypeBean.getPartyName());
			egBillregistermis.setPartyBillNumber(voucherTypeBean.getPartyBillNum());
			
			Set<EgBilldetails> egBilldetailes = egBillregister.getEgBilldetailes();
			egBilldetailes.clear();
			
			prepareBillDetails(egBillregister, billDetailslist,subLedgerlist,voucherHeader,egBilldetailes);
			billRegisterSer.update(egBillregister);
				 
		} catch (ValidationException e) {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
			 throw new ValidationException(errors);
		}
		catch (Exception e) {
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | updateBillForVSubType | End");
		return egBillregister;
	}
	/**
	 * 
	 * @param egBillregister
	 * @param billDetailslist
	 * @param subLedgerlist
	 * @param voucherHeader
	 * @return
	 */
	private Set<EgBilldetails> prepareBillDetails(EgBillregister egBillregister,List<VoucherDetails> billDetailslist,
									List<VoucherDetails> subLedgerlist,CVoucherHeader voucherHeader,Set<EgBilldetails> egBilldetailes){
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | prepareBillDetails | Start");
		for (VoucherDetails accountDetail : billDetailslist) {
			
			EgBilldetails egBilldetail = new EgBilldetails();
			egBilldetail.setEgBillregister(egBillregister);
			egBilldetail.setGlcodeid(new BigDecimal(accountDetail.getGlcodeIdDetail().toString()));
			egBilldetail.setDebitamount(accountDetail.getDebitAmountDetail());
			egBilldetail.setCreditamount(accountDetail.getCreditAmountDetail());
			if(null != accountDetail.getFunctionIdDetail()){
				egBilldetail.setFunctionid(new BigDecimal(accountDetail.getFunctionIdDetail()));
			}
			if(voucherHeader.getVouchermis().getFunction()!=null && !voucherHeader.getVouchermis().getFunction().equals("0")){
				egBilldetail.setFunctionid(new BigDecimal( voucherHeader.getVouchermis().getFunction().getId()));             
			}                         
			egBilldetail.setNarration(voucherHeader.getVouchermis().getNarration());
			Set<EgBillPayeedetails> egBillPaydetailes = null;
			
			for (VoucherDetails subledgerDetail : subLedgerlist) {
				if(accountDetail.getGlcodeIdDetail().equals(subledgerDetail.getGlcode().getId())){
					if(null == egBillPaydetailes){
						egBillPaydetailes = new HashSet<EgBillPayeedetails>(0);
					}
					EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
					egBillPaydetail.setEgBilldetailsId(egBilldetail);
					egBillPaydetail.setAccountDetailTypeId(subledgerDetail.getDetailType().getId());
					egBillPaydetail.setAccountDetailKeyId(subledgerDetail.getDetailKeyId());
					if(egBilldetail.getDebitamount().compareTo(BigDecimal.ZERO) == 1){
						egBillPaydetail.setDebitAmount(subledgerDetail.getAmount());
					}else{
						egBillPaydetail.setCreditAmount(subledgerDetail.getAmount());
					}
					egBillPaydetail.setNarration(voucherHeader.getVouchermis().getNarration());
					egBillPaydetailes.add(egBillPaydetail);
				}
				
				
			}
			egBilldetail.setEgBillPaydetailes(egBillPaydetailes);
			egBilldetailes.add(egBilldetail);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucher Service | prepareBillDetails | End");
		return egBilldetailes;
	}
	

	private boolean isBillNumUnique(String billNumber){
		
		String billNum = (String)persistenceService.find("select billnumber from EgBillregister where upper(billnumber)='"+billNumber.toUpperCase()+"'");
		if(null == billNum){
			return true;
		}else{
			return false;
		}
	}
	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}
	public void setBudgetDetailsDAO(BudgetDetailsHibernateDAO detailsHibernateDAO) {
		this.budgetDetailsDAO = detailsHibernateDAO;
	}
	public VoucherHibernateDAO getVoucherHibDAO() {
		return voucherHibDAO;
	}
	public void setVoucherHibDAO(VoucherHibernateDAO voucherHibDAO) {
		this.voucherHibDAO = voucherHibDAO;
	}
	public void cancelVoucher(CVoucherHeader voucher) {
		voucher.setStatus(4);
		update(voucher);
		
		
	}
	public EisUtilService getEisService() {
		return eisService;
	}
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao; 
	}
	public Position getPositionForEmployee(PersonalInformation emp)throws EGOVRuntimeException
	{
		return null;//This fix is for Phoenix Migration. eisCommonService.getPositionforEmp(emp.getIdPersonalInformation());
	}
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	public void setBillRegisterSer(
			PersistenceService<EgBillregister, Long> billRegisterSer) {
		this.billRegisterSer = billRegisterSer;
	}
	public Integer getDefaultDepartment() {
		Script script = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME, "BudgetDetail.get.default.department").get(0);
		String defaultDepartmentName = null;/* (String) script.eval(Script.createContext("eisCommonServiceBean", eisCommonService,
				"userId",Integer.valueOf(EGOVThreadLocals.getUserId().trim())));*/
		if(!"".equalsIgnoreCase(defaultDepartmentName)) {
			Department dept = (Department) persistenceService.find("from Department where deptName=?",defaultDepartmentName);
			if(dept!=null)
				return dept.getId().intValue();
		}
		return 0;
	}
	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}
	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}
	
}
