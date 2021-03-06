package org.egov.web.actions.payment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.payment.PaymentBean;
import org.egov.model.payment.Paymentheader;
import org.egov.pims.commons.DesignationMaster;
import org.egov.services.payment.PaymentService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.annotation.ValidationErrorPage;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;
import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")  
@Validation
public class PaymentAction extends BasePaymentAction{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private String expType,fromDate,toDate,mode,voucherdate,paymentMode,contractorIds="",supplierIds="",vouchernumber,voucherNumberPrefix="",voucherNumberSuffix="";
	private Long functionSel;   
	private String contingentIds = "";
	private String salaryIds = "";
	private String pensionIds = "";
	private int miscount=0;
	private boolean isDepartmentDefault;
	private BigDecimal balance;
	private CommonsService commonsService;                                   	
	private Paymentheader paymentheader;
	private PaymentService paymentService;
	private VoucherService voucherService;
	private Integer bankaccount,bankbranch; 
	private Integer departmentId;
	private Integer defaultDept;
	private SimpleWorkflowService<Paymentheader> paymentWorkflowService;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	private static final Logger LOGGER = Logger.getLogger(PaymentAction.class);
	private static final String PAYMENTID="paymentid";
	private static final String VIEW="view";
	private static final String LIST="list";
	private static final String MODIFY="modify"; 
	private String wfitemstate;
	private String type;
	private String billNumber;
	private String typeOfAccount;
	private List<EgBillregister> contractorBillList = null;
	private List<EgBillregister> supplierBillList = null;
	private List<EgBillregister> contingentBillList = null;
	private List<EgBillregister> salaryBillList = new ArrayList<EgBillregister>();
	private List<EgBillregister> pensionBillList = new ArrayList<EgBillregister>();
	private List<EgBillregister> totalBillList = new ArrayList<EgBillregister>();
	private List<Bankaccount> bankaccountList=null;
	private List<Miscbilldetail> miscBillList=null;
	private List<PaymentBean> billList;
	private List<PaymentBean> contractorList = null;
	private List<PaymentBean> supplierList = null;
	private List<PaymentBean> contingentList = null;
	private List<PaymentBean> salaryList = new ArrayList<PaymentBean>();
	private List<PaymentBean> pensionList = new ArrayList<PaymentBean>();
	private List<InstrumentHeader> instrumentHeaderList;
	private List<Paymentheader> paymentheaderList;
	private EgBillregister billregister;
	private boolean disableExpenditureType = false;
	private boolean enablePensionType = false;
	private boolean changePartyName;
	private String newPartyname;
	private String chk="";
	private String fundNameStr="";
	private String functionNameStr="";
	private String deptNameStr="";
	private String fundSourceNameStr="";
	private String schemeStr="";
	private String subSchemeStr="";
	private Map<String,String> payeeMap = new HashMap<String,String>();
	private Map<Long,BigDecimal> deductionAmtMap = new HashMap<Long,BigDecimal>();
	private Map<Long,BigDecimal> paidAmtMap = new HashMap<Long,BigDecimal>();
	private List<EgAdvanceRequisition> advanceRequisitionList = new ArrayList<EgAdvanceRequisition>();
	private VoucherHelper voucherHelper;
	private CFunction cFunctionobj;
	private String rtgsDefaultMode;
	private Date rtgsModeRestrictionDateForCJV;
	private String paymentRestrictionDateForCJV;
	private String billSubType;
	private String region;
	private String month;
	private String year;
	private String bank_branch;
	private String bank_account;	
	private ScriptService scriptService;
	private Map<Integer, String> monthMap = new LinkedHashMap<Integer, String> ();
	private FinancialYearHibernateDAO financialYearDAO;
	
	public PaymentAction(){                                         
		if(LOGGER.isDebugEnabled())     LOGGER.debug("creating PaymentAction...");
		addRelatedEntity("paymentheader", Paymentheader.class);    
		if(LOGGER.isDebugEnabled())     LOGGER.debug("creating PaymentAction completed.");
	}
	
	public void prepare(){
		super.prepare();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting prepare...");
		mandatoryFields = new ArrayList<String>();
		
		if(parameters.containsKey("salaryType"))
			setDisableExpenditureType(true);
		if(parameters.containsKey("pensionType")){
			setEnablePensionType(true);
			setDisableExpenditureType(true);
		} 
		if(parameters.get("fundId")!=null && !parameters.get("fundId")[0].equals("-1")){
		   	Fund fund  = (Fund) persistenceService.find("from Fund where id=?", Integer.parseInt(parameters.get("fundId")[0]));
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and type in ('RECEIPTS_PAYMENTS','PAYMENTS') ) and br.isactive=1 order by br.bank.name asc",fund));
		}
		else                 
			addDropdownData("bankbranchList",Collections.EMPTY_LIST);
		
		if(parameters.get("functionSel")!=null && !parameters.get("functionSel")[0].equals("-1") && !parameters.get("functionSel")[0].equals("")){
			 cFunctionobj  = (CFunction) persistenceService.find("from CFunction where id=?", Long.valueOf(parameters.get("functionSel")[0]));
			
		}
            
		if(getBankbranch()!=null)
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=1 ",getBankbranch()));
		else if(parameters.get("paymentheader.bankaccount.bankbranch.id")!=null && !parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=1 ",Integer.valueOf(parameters.get("paymentheader.bankaccount.bankbranch.id")[0])));
		else
			addDropdownData("bankaccountList", Collections.EMPTY_LIST);			
		if(getBillregister()!=null && getBillregister().getId()!=null){
			billregister=(EgBillregister) persistenceService.find(" from EgBillregister where id=?",getBillregister().getId());
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive=1 order by br.bank.name asc",billregister.getEgBillregistermis().getFund()));
		}
		
		paymentRestrictionDateForCJV=paymentService.getAppConfDateValForCJVPaymentModeRTGS();
		try {
			rtgsModeRestrictionDateForCJV=formatter.parse(paymentRestrictionDateForCJV);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addDropdownData("designationList", Collections.EMPTY_LIST);
		addDropdownData("userList", Collections.EMPTY_LIST);
		addDropdownData("regionsList", VoucherHelper.TNEB_REGIONS);
		addDropdownData("financialYearsList", financialYearDAO.getAllActiveFinancialYearList());
		monthMap = DateUtils.getAllMonthsWithFullNames();
		typeOfAccount = FinancialConstants.TYPEOFACCOUNT_PAYMENTS+","+FinancialConstants.TYPEOFACCOUNT_RECEIPTS_PAYMENTS;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed prepare.");
	}
	
	private void loadbankBranch(Fund fund){
		if(typeOfAccount != null && !typeOfAccount.equals("")) {
			if(typeOfAccount.indexOf(",") !=  -1 ) {
				String [] strArray = typeOfAccount.split(",");
				addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = 1 and type in (?,?) ) and br.isactive=1 and br.bank.isactive = 1 order by br.bank.name asc",fund, (String)strArray[0], (String)strArray[1] ));
			} else {
				addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = 1 and type in (?) ) and br.isactive=1 and br.bank.isactive = 1 order by br.bank.name asc",fund, typeOfAccount ));
			}
		} else {
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and isactive = 1) and br.isactive=1 and br.bank.isactive = 1 order by br.bank.name asc",fund));
		}
		String bankCode = null;
		
		if(billSubType!=null && !billSubType.equalsIgnoreCase("")){
			
			try{
				List<AppConfigValues> configValues =genericDao.getAppConfigValuesDAO().
						getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,FinancialConstants.EB_VOUCHER_PROPERTY_BANK); 
				
				for (AppConfigValues appConfigVal : configValues) {
					bankCode = appConfigVal.getValue();
						 }
				} catch (Exception e) {
					 throw new EGOVRuntimeException("Appconfig value for EB Voucher propartys is not defined in the system");
				}
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? and type in ('RECEIPTS_PAYMENTS','PAYMENTS') ) and br.isactive=1 and br.bank.code = ? order by br.bank.name asc",fund,bankCode));
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed loadbankBranch.");
	}
	
	@Override
	public Object getModel() {
		voucherHeader=(CVoucherHeader)super.getModel();
		voucherHeader.setType("Payment");
		return voucherHeader;
	}
	
	@SkipValidation
	@Action(value="/payment/payment-beforeSearch")
	public String beforeSearch()throws Exception{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting beforeSearch...");
		
		//Get App config value
		rtgsDefaultMode=paymentService.getAppConfValForCJVPaymentModeRTGS();
		
		
		
		if(validateUser("deptcheck"))
			voucherHeader.getVouchermis().setDepartmentid((Department)paymentService.getAssignment().getDeptId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed beforeSearch.");
		return "search";
	}
	@SkipValidation
	@Action(value="/payment/payment-beforeTNEBSearch")
	public String beforeTNEBSearch()throws Exception{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting beforeTNEBSearch...");
		setTNEBMandatoryFields();
		if(validateUser("deptcheck"))
			voucherHeader.getVouchermis().setDepartmentid((Department)paymentService.getAssignment().getDeptId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed beforeSearch.");
		return "tnebSearch";
	}
	private void setTNEBMandatoryFields(){
		
		List<String> propartyAppConfigKeysList = new ArrayList<String>();
		Map<String, String> propartyAppConfigResultList = new LinkedHashMap<String, String> ();
		propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_FUND);
		propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_FUNCTION);
		propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_DEPARTMENT);
		propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_BANKBRANCH);
		propartyAppConfigKeysList.add(FinancialConstants.EB_VOUCHER_PROPERTY_BANKACCOUNT);
		
		//Get App config value
		rtgsDefaultMode=paymentService.getAppConfValForCJVPaymentModeRTGS();
		for(String key:propartyAppConfigKeysList){
			String value = null;
		try{
			List<AppConfigValues> configValues =genericDao.getAppConfigValuesDAO().
					getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,key); 
			
			for (AppConfigValues appConfigVal : configValues) {
				value = appConfigVal.getValue();
				propartyAppConfigResultList.put(key, value);
					 }
			} catch (Exception e) {
				 throw new EGOVRuntimeException("Appconfig value for EB Voucher propartys is not defined in the system");
			}
		}
		for(String key:propartyAppConfigResultList.keySet()){
			
			if(key.equals("EB Voucher Property-Fund")){
				voucherHeader.setFundId((Fund) persistenceService.find("from Fund where code = ?",propartyAppConfigResultList.get(key)));
			}
			if(key.equals("EB Voucher Property-Function")){
				voucherHeader.getVouchermis().setFunction((CFunction) persistenceService.find("from CFunction where code = ?",propartyAppConfigResultList.get(key)));
			}
			if(key.equals("EB Voucher Property-Department")){
				voucherHeader.getVouchermis().setDepartmentid((Department) persistenceService.find("from Department where deptCode = ?",propartyAppConfigResultList.get(key)));
			}
			if(key.equals("EB Voucher Property-BankBranch")){
				bank_branch = propartyAppConfigResultList.get(key);
			}
			if(key.equals("EB Voucher Property-BankAccount")){
				bank_account = propartyAppConfigResultList.get(key);
				Bankaccount ba = (Bankaccount) persistenceService.find(" from Bankaccount where accountnumber=?",bank_account);
				if(ba.getId()!=null){
				bankaccount = ba.getId();
				}
		}
		
	}
}
	@ValidationErrorPage(value="search")
	public String search() throws Exception{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting search...");
		//Get App config value
		rtgsDefaultMode=paymentService.getAppConfValForCJVPaymentModeRTGS();
		StringBuffer sql =new StringBuffer();
		if(!"".equals(billNumber))
			sql.append(" and bill.billnumber = '"+billNumber+"' ");
		if(!"".equals(fromDate))
			sql.append(" and bill.billdate>='"+sdf.format(formatter.parse(fromDate))+"' ");
		if(!"".equals(toDate))
			sql.append(" and bill.billdate<='"+sdf.format(formatter.parse(toDate))+"'");
		if(voucherHeader.getFundId()!=null)                 
			sql.append(" and bill.egBillregistermis.fund.id="+voucherHeader.getFundId().getId());
		if(voucherHeader.getVouchermis().getFundsource()!=null)
			sql.append(" and bill.egBillregistermis.fundsource.id="+voucherHeader.getVouchermis().getFundsource().getId());
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			sql.append(" and bill.egBillregistermis.egDepartment.id="+voucherHeader.getVouchermis().getDepartmentid().getId());
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
			sql.append(" and bill.egBillregistermis.scheme.id="+voucherHeader.getVouchermis().getSchemeid().getId());
		if(voucherHeader.getVouchermis().getSubschemeid()!=null)
			sql.append(" and bill.egBillregistermis.subScheme.id="+voucherHeader.getVouchermis().getSubschemeid().getId());
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
			sql.append(" and bill.egBillregistermis.functionaryid.id="+voucherHeader.getVouchermis().getFunctionary().getId());
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
			sql.append(" and bill.egBillregistermis.fieldid="+voucherHeader.getVouchermis().getDivisionid().getId());
		// function field is intruduced later as mandatory , so we getting for the vocuhermis table 
		if(voucherHeader.getVouchermis().getFunction()!=null)
			sql.append(" and bill.egBillregistermis.function="+voucherHeader.getVouchermis().getFunction().getId());
		
		EgwStatus egwStatus=null;
/*		String mainquery = "from EgBillregister bill where bill.egBillregistermis.voucherHeader is not null and bill.egBillregistermis.voucherHeader.status=0 " +
				" and bill.expendituretype=? and ( ( bill.passedamount >(select sum(paidamount) from Miscbilldetail where  billVoucherHeader in " +
				" (select voucherHeader from EgBillregistermis where egBillregister.id = bill.id ) and (payVoucherHeader is null or " +
				" payVoucherHeader.status not in (1,2,4) )    group by billVoucherHeader)) " +
				"  or(select count(*) from Miscbilldetail where payVoucherHeader.status!=4 and billVoucherHeader in " +
				" (select voucherHeader from EgBillregistermis where egBillregister.id = bill.id ) )=0 ) ";*/
		String mainquery = "from EgBillregister bill where bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 " +
				" and bill.passedamount > (select SUM(misc.paidamount) from Miscbilldetail misc where misc.billVoucherHeader = bill.egBillregistermis.voucherHeader " +
				" and misc.payVoucherHeader.status in (0,5))";
		
		String mainquery1 = "from EgBillregister bill where bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 " +
				" and bill.egBillregistermis.voucherHeader NOT IN (select misc.billVoucherHeader from Miscbilldetail misc where misc.billVoucherHeader is not null and misc.payVoucherHeader.status <> 4)";
		
		if((disableExpenditureType==true && enablePensionType==false) || (expType!=null && !expType.equals("-1") && expType.equals("Salary"))){
			return salaryBills(sql,mainquery,mainquery1);
		}
		if((disableExpenditureType==true && enablePensionType==true) || (expType!=null && !expType.equals("-1") && expType.equals("Pension"))){
			return pensionBills(sql,mainquery,mainquery1);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("start purchase bill");
		if(expType==null || expType.equals("-1") || expType.equals("Purchase"))
		{
			egwStatus = commonsService.getStatusByModuleAndCode("SBILL", "Approved");
			EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("PURCHBILL", "Passed");
			String supplierBillSql=mainquery+" and bill.status in (?,?) "+sql.toString() +" order by bill.billdate desc";
			String supplierBillSql1=mainquery1+" and bill.status in (?,?) "+sql.toString() +" order by bill.billdate desc";
			supplierBillList = getPersistenceService().findPageBy(supplierBillSql,1,500,"Purchase",egwStatus,egwStatus1).getList();
			if(supplierBillList != null){
				supplierBillList.addAll(getPersistenceService().findPageBy(supplierBillSql1,1,500,"Purchase",egwStatus,egwStatus1).getList());
			}else{
				supplierBillList = getPersistenceService().findPageBy(supplierBillSql1,1,500,"Purchase",egwStatus,egwStatus1).getList();
			}
			Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(supplierBillList);
			supplierBillList.clear();
			supplierBillList.addAll(tempBillList);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("supplierBillSql  ===> "+supplierBillSql);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("end purchase bill");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("start works bill");
		if(expType==null || expType.equals("-1") || expType.equals("Works"))
		{
			// right not we dont know, the EGW-Status for works bill, passed from external system
			egwStatus = commonsService.getStatusByModuleAndCode("WORKSBILL", "Passed");
			EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("CONTRACTORBILL", "APPROVED"); // for external systems
			String contractorBillSql=mainquery+" and bill.status in (?,?) "+sql.toString() + " order by bill.billdate desc";
			String contractorBillSql1=mainquery1+" and bill.status in (?,?) "+sql.toString() + " order by bill.billdate desc";
			contractorBillList = getPersistenceService().findPageBy(contractorBillSql,1,500,"Works",egwStatus,egwStatus1).getList();
			if(contractorBillList != null){
				contractorBillList.addAll(getPersistenceService().findPageBy(contractorBillSql1,1,500,"Works",egwStatus,egwStatus1).getList());
			}else{
				contractorBillList = getPersistenceService().findPageBy(contractorBillSql1,1,500,"Works",egwStatus,egwStatus1).getList();
			}
			Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(contractorBillList);
			contractorBillList.clear();
			contractorBillList.addAll(tempBillList);
			if(LOGGER.isInfoEnabled())     LOGGER.info("contractorBillSql  ===> "+contractorBillSql);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("end works bill");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("start contingent bill");
		if((expType==null || expType.equals("-1") || expType.equals(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT)))
		{
			
			String cBillmainquery = "from EgBillregister bill left join fetch bill.egBillregistermis.egBillSubType egBillSubType where (egBillSubType is null or egBillSubType.name not in ('"+FinancialConstants.BILLSUBTYPE_TNEBBILL+"')) and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 " +
					" and bill.passedamount > (select SUM(misc.paidamount) from Miscbilldetail misc where misc.billVoucherHeader = bill.egBillregistermis.voucherHeader " +
					" and misc.payVoucherHeader.status in (0,5))";
			
			String cBillmainquery1 = "from EgBillregister bill left join fetch bill.egBillregistermis.egBillSubType egBillSubType where (egBillSubType is null or egBillSubType.name not in ('"+FinancialConstants.BILLSUBTYPE_TNEBBILL+"')) and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 " +
					" and bill.egBillregistermis.voucherHeader NOT IN (select misc.billVoucherHeader from Miscbilldetail misc where misc.billVoucherHeader is not null and misc.payVoucherHeader.status <> 4)";
			
			egwStatus = commonsService.getStatusByModuleAndCode("EXPENSEBILL", "Approved"); // for financial expense bills
			EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("CBILL", "APPROVED"); // for external systems
			String cBillSql=cBillmainquery+" and bill.status in (?,?) "+sql.toString() + " order by bill.billdate desc";
			String cBillSql1=cBillmainquery1+" and bill.status in (?,?) "+sql.toString() + " order by bill.billdate desc";
			contingentBillList = getPersistenceService().findPageBy(cBillSql,1,500,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,egwStatus,egwStatus1).getList();
			if(contingentBillList != null){
				contingentBillList.addAll(getPersistenceService().findPageBy(cBillSql1,1,500,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,egwStatus,egwStatus1).getList());
			}else{
				contingentBillList = getPersistenceService().findPageBy(cBillSql1,1,500,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,egwStatus,egwStatus1).getList();
			}
			Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(contingentBillList);
			contingentBillList.clear();
			contingentBillList.addAll(tempBillList);
			if(LOGGER.isInfoEnabled())     LOGGER.info("cBillSql  ===> "+cBillSql);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("end contingent bill");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getting glcodeids");
		paymentService.getGlcodeIds();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("done glcodeids");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("deduction works start");
		deductionAmtMap = paymentService.getDeductionAmt(contractorBillList, "Works");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("deduction works end");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("deduction supplier start");
		deductionAmtMap.putAll(paymentService.getDeductionAmt(supplierBillList, "Purchase"));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("deduction supplier end");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("deduction contingent start");
		deductionAmtMap.putAll(paymentService.getDeductionAmt(contingentBillList,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("deduction contingent end");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("paidamt works start");
		paidAmtMap = paymentService.getEarlierPaymentAmt(contractorBillList, "Works");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("paidamt works end");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("paidamt purchase start");
		paidAmtMap.putAll(paymentService.getEarlierPaymentAmt(supplierBillList, "Purchase"));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("paidamt purchase end");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("paidamt contingent start");
		paidAmtMap.putAll(paymentService.getEarlierPaymentAmt( contingentBillList,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("paidamt contingent end");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getCSList all 3 start");
		contractorList = paymentService.getCSList(contractorBillList, deductionAmtMap, paidAmtMap);
		supplierList = paymentService.getCSList(supplierBillList, deductionAmtMap, paidAmtMap);
		
		contingentList = paymentService.getCSList(contingentBillList, deductionAmtMap, paidAmtMap);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getCSList all 3 end");
		if(LOGGER.isInfoEnabled())     LOGGER.info("contingentList size ==="+contingentList.size());
		
		setMode("search");
		paymentMode=FinancialConstants.MODEOFCOLLECTION_CHEQUE;
		loadSchemeSubscheme();
		loadFundSource();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed search.");
		return "searchbills";
	}
	
	private String salaryBills(StringBuffer sql,String mainquery,String mainquery1) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting salaryBills...");
		EgwStatus egwStatus = commonsService.getStatusByModuleAndCode("SALBILL", "Approved"); // for financial salary bills
		EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("SBILL", "Approved"); // for external systems
		String sBillSql=mainquery+" and bill.status in (?,?) "+sql.toString()+" order by bill.billdate desc";
		String sBillSql1=mainquery1+" and bill.status in (?,?) "+sql.toString()+" order by bill.billdate desc";
		salaryBillList = getPersistenceService().findAllBy(sBillSql,FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY,egwStatus,egwStatus1);
		if(salaryBillList != null){
			salaryBillList.addAll(getPersistenceService().findAllBy(sBillSql1,FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY,egwStatus,egwStatus1));
		}else{
			salaryBillList = getPersistenceService().findAllBy(sBillSql1,FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY,egwStatus,egwStatus1);
		}
		Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(salaryBillList);
		salaryBillList.clear();
		salaryBillList.addAll(tempBillList);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("sBillSql  ===> "+sBillSql);
		paymentService.getGlcodeIds();
		deductionAmtMap = paymentService.getDeductionAmt(salaryBillList, "Salary");
		paidAmtMap = paymentService.getEarlierPaymentAmt(salaryBillList, "Salary");
		salaryList = paymentService.getCSList(salaryBillList, deductionAmtMap, paidAmtMap);
		setMode("search");
		paymentMode=FinancialConstants.MODEOFPAYMENT_CASH;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed salaryBills.");
		return "salaryBills";
	}
	private String pensionBills(StringBuffer sql,String mainquery,String mainquery1) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting pensionBills...");
		EgwStatus egwStatus = commonsService.getStatusByModuleAndCode("PENSIONBILL", "Approved");
		String pBillSql=mainquery+" and bill.status in (?) "+sql.toString()+" order by bill.billdate desc";
		String pBillSql1=mainquery1+" and bill.status in (?) "+sql.toString()+" order by bill.billdate desc";
		pensionBillList = getPersistenceService().findAllBy(pBillSql,FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION,egwStatus);
		if(pensionBillList != null){
			pensionBillList.addAll(getPersistenceService().findAllBy(pBillSql1,FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION,egwStatus));
		}else{
			pensionBillList = getPersistenceService().findAllBy(pBillSql1,FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION,egwStatus);
		}
		Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(pensionBillList);
		pensionBillList.clear();
		pensionBillList.addAll(tempBillList);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("pBillSql  ===> "+pBillSql);
		paymentService.getGlcodeIds();
		deductionAmtMap = paymentService.getDeductionAmt(pensionBillList, FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION);
		paidAmtMap = paymentService.getEarlierPaymentAmt(pensionBillList, FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION);
		pensionList = paymentService.getCSList(pensionBillList, deductionAmtMap, paidAmtMap);
		setMode("search");
		paymentMode=FinancialConstants.MODEOFPAYMENT_CASH;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed pensionBills.");
		return "pensionBills";
	}
	@ValidationErrorPage(value="tnebSearch")
	public String tnebBills() {
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting tnebBills...");
		
		StringBuffer sql =new StringBuffer();
		if(!"".equals(billNumber))
			sql.append(" and bill.billnumber = '"+billNumber+"' ");
		if(voucherHeader.getFundId()!=null)                 
			sql.append(" and bill.egBillregistermis.fund.id="+voucherHeader.getFundId().getId());
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			sql.append(" and bill.egBillregistermis.egDepartment.id="+voucherHeader.getVouchermis().getDepartmentid().getId());
		if(voucherHeader.getVouchermis().getFunction()!=null)
			sql.append(" and bill.egBillregistermis.function="+voucherHeader.getVouchermis().getFunction().getId());
		
		String tnebSqlMainquery = "select bill from EgBillregister bill , EBDetails ebd   where  bill.id = ebd.egBillregister.id and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 " +
				" and bill.passedamount > (select SUM(misc.paidamount) from Miscbilldetail misc where misc.billVoucherHeader = bill.egBillregistermis.voucherHeader " +
				" and misc.payVoucherHeader.status in (0,5))";
		
		String tnebSqlMainquery1 = "select bill from EgBillregister bill , EBDetails ebd  where  bill.id = ebd.egBillregister.id and bill.expendituretype=? and bill.egBillregistermis.voucherHeader.status=0 " +
				" and bill.egBillregistermis.voucherHeader NOT IN (select misc.billVoucherHeader from Miscbilldetail misc where misc.billVoucherHeader is not null and misc.payVoucherHeader.status <> 4)";
		if(billSubType!=null && !billSubType.equalsIgnoreCase("")){             
			sql.append(" and bill.egBillregistermis.egBillSubType.name='"+billSubType+"'");	       
		}
		if(region!=null && !region.equalsIgnoreCase("")){             
			sql.append(" and ebd.region='"+region+"'");	       
		}
		if(month!=null && !month.equalsIgnoreCase("")){             
			sql.append(" and ebd.month="+month+"");	       
		}
		if(year!=null && !year.equalsIgnoreCase("")){             
			sql.append(" and ebd.financialyear.id="+year+"");	       
		}
		EgwStatus egwStatus = commonsService.getStatusByModuleAndCode("EXPENSEBILL", "Approved"); // for financial expense bills
		EgwStatus egwStatus1 = commonsService.getStatusByModuleAndCode("CBILL", "APPROVED"); // for external systems
		String tnebBillSql=tnebSqlMainquery+" and bill.status in (?,?) "+sql.toString()+" order by bill.billdate desc";
		String tnebBillSql1=tnebSqlMainquery1+" and bill.status in (?,?) "+sql.toString()+" order by bill.billdate desc";
		contingentBillList = getPersistenceService().findPageBy(tnebBillSql,1,500,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,egwStatus,egwStatus1).getList();
		if(contingentBillList != null){
			contingentBillList.addAll(getPersistenceService().findPageBy(tnebBillSql1,1,500,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,egwStatus,egwStatus1).getList());
		}else{
			contingentBillList = getPersistenceService().findPageBy(tnebBillSql1,1,500,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT,egwStatus,egwStatus1).getList();
		}
		Set<EgBillregister> tempBillList = new LinkedHashSet<EgBillregister>(contingentBillList);
		contingentBillList.clear();
		contingentBillList.addAll(tempBillList);
		if(LOGGER.isInfoEnabled())     LOGGER.info("tnebBillSql  ===> "+tnebBillSql);
		paymentService.getGlcodeIds();
		deductionAmtMap = paymentService.getDeductionAmt(contingentBillList,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
		paidAmtMap = paymentService.getEarlierPaymentAmt(contingentBillList,FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
		contingentList = paymentService.getCSList(contingentBillList, deductionAmtMap, paidAmtMap);
		setMode("search");
		paymentMode=FinancialConstants.MODEOFPAYMENT_RTGS;
		setTNEBMandatoryFields();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed tnebBills.");
		return "tnebBills";
	}
	
/**
 * 
 * @return
 * @throws ValidationException
 * this api is called from searchbills method is changed to save to enable csrf fix
 * actaul method name was generate payment. I doesnot save  data but forwards to screen where for selected bill we can make payment
 */
	public String save() throws ValidationException{
		List<PaymentBean> paymentList=new ArrayList<PaymentBean>();
		try{
			String paymentMd=parameters.get("paymentMode")[0];
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting generatePayment...");
			if(LOGGER.isInfoEnabled())     LOGGER.info("Expenditure type is--------------------------------- "+expType);
			
			if(null!=contractorList && !contractorList.isEmpty())
				paymentList.addAll(contractorList);
			if(null!=supplierList && !supplierList.isEmpty())
				paymentList.addAll(supplierList);
			if(null!=contingentList && !contingentList.isEmpty())
				paymentList.addAll(contingentList);
			if(null!=salaryList && !salaryList.isEmpty()) 
				paymentList.addAll(salaryList);
			if(null!=pensionList && !pensionList.isEmpty())
				paymentList.addAll(pensionList);
			
			if(rtgsDefaultMode!=null && rtgsDefaultMode.equalsIgnoreCase("Y") && !paymentMd.equalsIgnoreCase("rtgs") )
			{
				if(paymentService.CheckForContractorSubledgerCodes(paymentList,rtgsModeRestrictionDateForCJV))
				  throw new ValidationException(Arrays.asList(new ValidationError("Payment Mode of any bill having Contractor/Supplier subledger should  RTGS For Bill Date Greater than 01-Oct-2013", "Payment Mode of any bill having Contractor/Supplier subledger should  RTGS For Bill Date Greater than 01-Oct-2013")));
			}
			billList = new ArrayList<PaymentBean>();
			contractorIds = contractorIds+populateBillListFor(contractorList,contractorIds);
			supplierIds = supplierIds+populateBillListFor(supplierList,supplierIds);
			contingentIds = contingentIds+populateBillListFor(contingentList,contingentIds);
			salaryIds = salaryIds+populateBillListFor(salaryList,salaryIds);
			pensionIds = pensionIds+populateBillListFor(pensionList, pensionIds);
			//functionSel=
		if(salaryIds!=null && salaryIds.length()>0){
				disableExpenditureType = true;
			}
			if(pensionIds!=null && pensionIds.length()>0){
				disableExpenditureType = true;
				enablePensionType = true;
			}
			
			billregister = (EgBillregister) persistenceService.find(" from EgBillregister where id=?", (billList.get(0)).getBillId()) ;
			if(billregister.getEgBillregistermis().getFunction()!=null){
				setFunctionSel(billregister.getEgBillregistermis().getFunction().getId());      
			}
			loadbankBranch(billregister.getEgBillregistermis().getFund());                        
			miscount = billList.size();
			if(parameters.get("paymentMode")[0].equalsIgnoreCase("RTGS")){
				paymentService.validateForRTGSPayment(contractorList, "Contractor");
				paymentService.validateForRTGSPayment(supplierList, "Supplier");
				if(billSubType == null || billSubType.equalsIgnoreCase("")){
				paymentService.validateForRTGSPayment(contingentList, FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
				}
			}

			if(! "Auto".equalsIgnoreCase(new VoucherTypeForULB().readVoucherTypes("Payment"))){
				headerFields.add("vouchernumber");
				mandatoryFields.add("vouchernumber");
			}
			if(LOGGER.isInfoEnabled())     LOGGER.info("Expenditure type is--------------------------------- "+expType);
			voucherdate =formatter.format(new Date());
		}catch(ValidationException e){
			LOGGER.error(e.getErrors(),e);
			throw new ValidationException(e.getErrors());
		}catch(EGOVException e){
			LOGGER.error(e.getMessage(),e);
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}                                   
		loadApproverUser(voucherHeader.getType());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed generatePayment.");
		return "form";
		
	}
	
	private String populateBillListFor(List<PaymentBean> list,String ids) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting populateBillListFor...");
		if(list!=null){
			for(PaymentBean bean :list){
				if(bean.getIsSelected()){
					if(chk.equals("")){
						chk="checked";
						fundNameStr=bean.getFundName();                
						functionNameStr=bean.getFunctionName();
						deptNameStr=bean.getDeptName();
						fundSourceNameStr=bean.getFundsourceName();
						schemeStr=bean.getSchemeName();
						subSchemeStr=bean.getSubschemeName();
						region=bean.getRegion();
					   }
					if(region!=null){
						if(fundNameStr.equals(bean.getFundName()) && deptNameStr.equals(bean.getDeptName()) && functionNameStr.equals(bean.getFunctionName())){
										billList.add(bean);
							ids = ids+bean.getBillId()+",";
						 }
						else{
								if(LOGGER.isDebugEnabled())     LOGGER.debug("Validation Error mismatch in attributes ");
								throw new ValidationException(Arrays.asList(new ValidationError("Mismatch in attributes", "Mismatch in attributes!!")));	
						}     
					}else{
						if(fundNameStr.equals(bean.getFundName()) && deptNameStr.equals(bean.getDeptName()) && fundSourceNameStr.equals(bean.getFundsourceName()
							) && functionNameStr.equals(bean.getFunctionName())  && schemeStr.equals(bean.getSchemeName()) && subSchemeStr.equals(bean.getSubschemeName())){
									billList.add(bean);
						ids = ids+bean.getBillId()+",";
						}
						else{
							if(LOGGER.isDebugEnabled())     LOGGER.debug("Validation Error mismatch in attributes ");
							throw new ValidationException(Arrays.asList(new ValidationError("Mismatch in attributes", "Mismatch in attributes!!")));	
						}        
					}
				}
			else
				continue;
			}
			if(ids.length()>0)
				ids = ids.substring(0,ids.length()-1);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed populateBillListFor.");
		return ids;
	}
	
	@ValidationErrorPage(value="form")
	@SkipValidation
	public String create(){
		try{               
			//billregister.getEgBillregistermis().setFunction(functionSel);
			billregister.getEgBillregistermis().setFunction(cFunctionobj);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting createPayment...");
			paymentheader = paymentService.createPayment(parameters,billList,billregister);
			paymentheader.getVoucherheader().getVouchermis().setSourcePath("/EGF/payment/payment!view.action?"+PAYMENTID+"="+paymentheader.getId());
			getPaymentBills();
			paymentheader.start().withOwner(paymentService.getPosition());
			sendForApproval();
			addActionMessage(getMessage("payment.transaction.success",new String[]{paymentheader.getVoucherheader().getVoucherNumber()}));
			loadApproverUser(voucherHeader.getType());
		}catch(ValidationException e){
			loadApproverUser(voucherHeader.getType());
			throw new ValidationException(e.getErrors());
		}catch(EGOVRuntimeException e){
			LOGGER.error(e.getMessage());
			loadApproverUser(voucherHeader.getType());
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}catch(Exception e){
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed createPayment.");
		return VIEW;
	}

	@ValidationErrorPage(value=VIEW)
	@SkipValidation
	public String sendForApproval() 
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting sendForApproval...");
		paymentheader = getPayment();
		if(paymentheader != null && paymentheader.getState() != null){
			if(!validateOwner(paymentheader.getState())){
				 List<ValidationError> errors=new ArrayList<ValidationError>();
				 errors.add(new ValidationError("exp","Invalid Access"));
				 throw new ValidationException(errors);
			}
		}
		getPaymentBills();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Paymentheader=="+paymentheader.getId()+", actionname="+parameters.get(ACTIONNAME)[0]);
		action=parameters.get(ACTIONNAME)[0];
		
		Integer userId = null;
		if( parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject")){
			userId = paymentheader.getCreatedBy().getId().intValue();
		}
		else if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) {
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		}else {
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Paymentheader=="+paymentheader.getStateType());
		if(parameters.get("comments")!=null){
			paymentWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, paymentheader, parameters.get("comments")[0]);
		}else{
			paymentWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, paymentheader, paymentheader.getVoucherheader().getDescription());	
		}
		paymentService.persist(paymentheader);
		if(parameters.get(ACTIONNAME)[0].contains("approve"))
			if("END".equals(paymentheader.getState().getValue()))
				addActionMessage(getMessage("payment.voucher.final.approval"));
			else
				addActionMessage(getMessage("payment.voucher.approved",new String[]{paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition())}));
		else
			addActionMessage(getMessage("payment.voucher.rejected",new String[]{paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition())}));
		if(Constants.ADVANCE_PAYMENT.equalsIgnoreCase(paymentheader.getVoucherheader().getName())){
			getAdvanceRequisitionDetails();
			return "advancePaymentView";
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed sendForApproval.");
		return VIEW;
	}
	
	public String getComments(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside getComments...");
		return getText("payment.comments", new String[]{paymentheader.getPaymentAmount().toPlainString()});
	}
	
	@SkipValidation
	public List<org.egov.infstr.workflow.Action> getValidActions(){
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside getValidActions...");
		return paymentWorkflowService.getValidActions(getPayment());
 	}
	@SkipValidation
	@Action(value="/payment/payment-view")
	public String view() 
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting view...");
		paymentheader = getPayment();
		if(paymentheader.getState().getValue()!=null && !paymentheader.getState().getValue().isEmpty()  && paymentheader.getState().getValue().contains("Rejected"))
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed view.");
			return modify();
		}
		getPaymentBills();
		getChequeInfo(paymentheader);
		if(null != parameters.get("showMode") && parameters.get("showMode")[0].equalsIgnoreCase("view") ){
			// if user is  drilling down form source , parameter showMode is passed with value view, in this case we do not show the
			 // approver drop down in the view screen .
			wfitemstate = "END";
		}else{
			/*if(paymentheader != null && paymentheader.getState() != null){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting validate owner");
				if(!validateOwner(paymentheader.getState())){	
					 List<ValidationError> errors=new ArrayList<ValidationError>();
					 errors.add(new ValidationError("exp","Invalid Access"));
					 throw new ValidationException(errors);
				}
			}
*/			loadApproverUser(voucherHeader.getType());
		}
		
		if(LOGGER.isInfoEnabled())     LOGGER.info("defaultDept in vew : "+getDefaultDept());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed view.");
		return VIEW;
	}
	
	@SkipValidation
	public String advanceView(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting advanceView...");
		paymentheader = getPayment();
		if(paymentheader.getState().getValue()!=null && !paymentheader.getState().getValue().isEmpty()  
				&& paymentheader.getState().getValue().contains("Rejected")){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed advanceView.");
			return modifyAdvancePayment();
		}
		getAdvanceRequisitionDetails();
		getChequeInfo(paymentheader);
		loadApproverUser(voucherHeader.getType());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed advanceView.");
		return "advancePaymentView";
	}
	
	private void getAdvanceRequisitionDetails() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside getAdvanceRequisitionDetails");
		advanceRequisitionList.addAll(persistenceService.findAllBy("from EgAdvanceRequisition where egAdvanceReqMises.voucherheader.id=?", paymentheader.getVoucherheader().getId()));
	}
	
	public void getChequeInfo(Paymentheader paymentheader)
	{
	//	if(LOGGER.isInfoEnabled())     LOGGER.info("Inside getChequeInfo");
		paymentheader = getPayment();
		instrumentHeaderList = getPersistenceService().findAllBy(" from InstrumentHeader ih where ih.id in (select iv.instrumentHeaderId.id from InstrumentVoucher iv where iv.voucherHeaderId.id=?) order by instrumentNumber", paymentheader.getVoucherheader().getId());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Retrived cheque info details for the paymentheader");
	}
	
	@SkipValidation
	public void getPaymentBills()
	{
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside getPaymentBills");
		try{
		miscBillList = getPersistenceService().findAllBy(" from Miscbilldetail where payVoucherHeader.id = ? order by paidto", paymentheader.getVoucherheader().getId());
		}catch (Exception e) {
			throw new ValidationException("","Total Paid Amount Exceeding Net Amount For This Bill");
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Retrived bill details fro the paymentheader");
	}
	@SkipValidation
	public boolean validateUser(String purpose)throws ParseException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("------------------Starting validateUser...");
		if(LOGGER.isInfoEnabled())     LOGGER.info("-------------------------------------------------------------------------------------------------");
		if(LOGGER.isInfoEnabled())     LOGGER.info("Calling Validate User "+purpose);
		if(LOGGER.isInfoEnabled())     LOGGER.info("-------------------------------------------------------------------------------------------------");
		Script validScript = (Script) getPersistenceService().findAllByNamedQuery(Script.BY_NAME,"Paymentheader.show.bankbalance").get(0);
		List<String> list = (List<String>) scriptService.executeScript(validScript,ScriptService.createContext("persistenceService",paymentService,"purpose",purpose));
		if(list.get(0).equals("true"))
		{
			if(purpose.equals("balancecheck"))
			{
				paymentheader = getPayment(); 
				try { 
					getBankBalance(paymentheader.getBankaccount().getId().toString(),formatter.format(new Date()),paymentheader.getPaymentAmount(),paymentheader.getId(), paymentheader.getBankaccount().getChartofaccounts().getId());
				} catch (ValidationException e) {
					LOGGER.error("Error"+e.getMessage(),e);
					balance=BigDecimal.valueOf(-1);
				}
			}           
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed validateUser.");
			return true;
		}
		else{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed validateUser.");
			return false;
		}
	}
	
	@SkipValidation
	public String ajaxLoadBankAccounts()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting ajaxLoadBankAccounts...");
		if(LOGGER.isInfoEnabled())     LOGGER.info("Bankbranch id = "+parameters.get("bankbranch")[0]);
		Bankbranch bankbranch = (Bankbranch) persistenceService.find("from Bankbranch where id = ?", Integer.parseInt(parameters.get("bankbranch")[0]));
		bankaccountList = getPersistenceService().findAllBy(" FROM Bankaccount where bankbranch=? and isactive=1 ", bankbranch);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed ajaxLoadBankAccounts.");
		return "bankaccount";
	}
	
	@SkipValidation
	@Action(value="/payment/payment-ajaxGetAccountBalance")
	public String ajaxGetAccountBalance() throws ParseException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside ajaxGetAccountBalance.");
		getBankBalance(parameters.get("bankaccount")[0],parameters.get("voucherDate")[0],null,null, null);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed ajaxGetAccountBalance.");
		return "balance";
	}
	@SkipValidation
	public void getBankBalance(String accountId,String vdate,BigDecimal amount,Long paymentId, Long bankGlcodeId)throws ParseException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside getBankBalance.");
		
		try {
			balance = paymentService.getAccountBalance(accountId,vdate,amount,paymentId, bankGlcodeId);
		} catch (Exception e) {
			balance=BigDecimal.valueOf(-1);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getBankBalance.");
	}
	
	@SkipValidation
	@Action(value="/payment/payment-beforeModify")
	public String beforeModify()throws Exception
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting beforeModify.");
		if(validateUser("deptcheck"))
			voucherHeader.getVouchermis().setDepartmentid((Department)paymentService.getAssignment().getDeptId());
		action="search";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed beforeModify.");
		return LIST;
	}
	@ValidationErrorPage(value=LIST)
	public String list() throws Exception
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting list...");
		List<String> descriptionList = new ArrayList<String>();
		descriptionList.add("New");
		descriptionList.add("Deposited");
		descriptionList.add("Reconciled");
		List<EgwStatus> egwStatusList = commonsService.getStatusListByModuleAndCodeList("Instrument", descriptionList);
		String statusId="";
		for(EgwStatus egwStatus : egwStatusList)
			statusId = statusId+egwStatus.getId()+",";
		statusId = statusId.substring(0, statusId.length()-1);
		
		StringBuffer sql =new StringBuffer();
		if(!StringUtils.isBlank(fromDate))
			sql.append(" and ph.voucherheader.voucherDate>='"+sdf.format(formatter.parse(fromDate))+"' ");
		if(!StringUtils.isBlank(toDate))
			sql.append(" and ph.voucherheader.voucherDate<='"+sdf.format(formatter.parse(toDate))+"'");
		if(!StringUtils.isBlank(voucherHeader.getVoucherNumber()))
			sql.append(" and ph.voucherheader.voucherNumber like '%"+voucherHeader.getVoucherNumber()+"%'");
		if(voucherHeader.getFundId()!=null)
			sql.append(" and ph.voucherheader.fundId.id="+voucherHeader.getFundId().getId());
		if(voucherHeader.getFundsourceId()!=null)
			sql.append(" and ph.voucherheader.vouchermis.fundsource.id="+voucherHeader.getFundsourceId().getId());
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.departmentid.id="+voucherHeader.getVouchermis().getDepartmentid().getId());
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.schemeid.id="+voucherHeader.getVouchermis().getSchemeid().getId());
		if(voucherHeader.getVouchermis().getSubschemeid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.subschemeid.id="+voucherHeader.getVouchermis().getSubschemeid().getId());
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
			sql.append(" and ph.voucherheader.vouchermis.functionary.id="+voucherHeader.getVouchermis().getFunctionary().getId());
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
			sql.append(" and ph.voucherheader.vouchermis.divisionid.id="+voucherHeader.getVouchermis().getDivisionid().getId());


		paymentheaderList = getPersistenceService().findAllBy(" from Paymentheader ph where ph.voucherheader.status=0 and (ph.voucherheader.isConfirmed=null or ph.voucherheader.isConfirmed=0) "+sql.toString()+
				"  and ph.voucherheader.id not in (select iv.voucherHeaderId.id from InstrumentVoucher iv where iv.instrumentHeaderId in (from InstrumentHeader ih where ih.statusId.id in ("+statusId+") ))");
		action=LIST;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed list...");
		return LIST;
	}
	@ValidationErrorPage(value=LIST)
	@SkipValidation
	@Action(value="/payment/payment-modify")
	public String modify() 
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting modify...");
		paymentheader = getPayment();
		if(paymentheader != null && paymentheader.getState() != null){
			if(!validateOwner(paymentheader.getState())){
				 List<ValidationError> errors=new ArrayList<ValidationError>();
				 errors.add(new ValidationError("exp","Invalid Access"));
				 throw new ValidationException(errors);
			}
		}
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if (!"Auto".equalsIgnoreCase(vNumGenMode))
		{
			voucherNumberPrefix=paymentheader.getVoucherheader().getVoucherNumber().substring(0,Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			voucherNumberSuffix=paymentheader.getVoucherheader().getVoucherNumber().substring(Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH,paymentheader.getVoucherheader().getVoucherNumber().length()));
		}
		addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=1 ",paymentheader.getBankaccount().getBankbranch().getId()));
		//addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive=1 order by br.bank.name asc",paymentheader.getVoucherheader().getFundId()));
		loadbankBranch(paymentheader.getVoucherheader().getFundId());
		billList = paymentService.getMiscBillList(paymentheader);
		if(FinancialConstants.PAYMENTVOUCHER_NAME_SALARY.equalsIgnoreCase(paymentheader.getVoucherheader().getName()))
		{
			disableExpenditureType=true;
			
		}
		 // commented by msahoo to avoid account balance check for the bill creator.
		/*try {
			balance = paymentService.getAccountBalance(paymentheader.getBankaccount().getId().toString(), formatter.format(new Date()),paymentheader.getPaymentAmount(),paymentheader.getId());
		} catch (ParseException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Error While formatting date","Error While formatting date")));
		}*/   
		loadApproverUser(paymentheader.getVoucherheader().getType());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed modify.");
		return MODIFY;
		
	}
	
	@ValidationErrorPage(value=LIST)
	@SkipValidation
	public String modifyAdvancePayment(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting modifyAdvancePayment...");
		paymentheader = (Paymentheader) persistenceService.find(" from Paymentheader where id=? ",paymentheader.getId());
		addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=1 and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
		loadbankBranch(paymentheader.getVoucherheader().getFundId());
		getAdvanceRequisitionDetails();
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if (!"Auto".equalsIgnoreCase(vNumGenMode)){
			voucherNumberPrefix=paymentheader.getVoucherheader().getVoucherNumber().substring(0,Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			voucherNumberSuffix=paymentheader.getVoucherheader().getVoucherNumber().substring(Integer.valueOf(FinancialConstants.VOUCHERNO_TYPE_LENGTH,paymentheader.getVoucherheader().getVoucherNumber().length()));
		}
		try {
			balance = paymentService.getAccountBalance(paymentheader.getBankaccount().getId().toString(), formatter.format(new Date()),paymentheader.getPaymentAmount(),paymentheader.getId(),paymentheader.getBankaccount().getChartofaccounts().getId());
		} catch (ParseException e) {
			LOGGER.error("Error"+e.getMessage(),e);
			throw new ValidationException(Arrays.asList(new ValidationError("Error While formatting date","Error While formatting date")));
		}
		loadApproverUser(paymentheader.getVoucherheader().getType());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed modifyAdvancePayment...");
		return "advancePaymentModify";
	}

	@ValidationErrorPage(value=MODIFY)
	@SkipValidation
	public String cancelPayment()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting cancelPayment...");
		paymentheader = (Paymentheader) persistenceService.find(" from Paymentheader where id=? ",paymentheader.getId());
		voucherHeader=paymentheader.getVoucherheader();
		voucherHeader.setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
		persistenceService.setType(CVoucherHeader.class);
		paymentheader.transition(true).end();
		persistenceService.persist(voucherHeader);
		addActionMessage(getMessage("payment.cancel.success"));  
		action=parameters.get(ACTIONNAME)[0];
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed cancelPayment...");
		return VIEW;      
	}          
	@ValidationErrorPage(value=MODIFY)
	@SkipValidation
	public String edit()throws Exception
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting update...");
		try
		{
			validateForUpdate();
			if(getFieldErrors().isEmpty())
			{
				paymentheader =paymentService.updatePayment(parameters, billList,paymentheader);
				getPaymentBills();
				sendForApproval();
				addActionMessage(getMessage("payment.transaction.success",new String[]{paymentheader.getVoucherheader().getVoucherNumber()}));
				//System.out.println("The retun value is :VIEW");
				loadApproverUser(voucherHeader.getType());
			}
			else{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed update...");
				return MODIFY;
			}
		}
		catch(ValidationException e) 
		{
			LOGGER.error("Error"+e.getMessage(),e);
			addDropdownData("bankbranchList", persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund=? ) and br.isactive=1 order by br.bank.name asc",paymentheader.getVoucherheader().getFundId()));
			throw new ValidationException(e.getErrors());
		}
		catch(EGOVRuntimeException e) 
		{
			LOGGER.error(e.getMessage(),e);
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
		catch(Exception e) 
		{
			LOGGER.error(e.getMessage(),e);
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
   
		//action=MODIFY;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed update...");
		return VIEW;
	}
	@ValidationErrorPage(value="advancePaymentModify")
	@SkipValidation
	public String updateAdvancePayment()throws Exception{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting updateAdvancePayment...");
		paymentheader = (Paymentheader) persistenceService.find("from Paymentheader where id=?",paymentheader.getId());
		getAdvanceRequisitionDetails();
		try{
			validateAdvancePayment();
			paymentheader.setBankaccount((Bankaccount) persistenceService.find("from Bankaccount where id=?",Integer.valueOf(parameters.get("paymentheader.bankaccount.id")[0])));
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=1 and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
			loadbankBranch(paymentheader.getBankaccount().getFund());
			if(getFieldErrors().isEmpty()){
				Integer userId = null;
				if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) {
					userId = Integer.valueOf(parameters.get("approverUserId")[0]);
				}else {
					userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
				}
				paymentWorkflowService.transition(( getValidActions().get(0)).getName()+"|"+userId, paymentheader, paymentheader.getVoucherheader().getDescription());
				addActionMessage(getMessage("payment.voucher.approved",new String[]{paymentService.getEmployeeNameForPositionId(paymentheader.getState().getOwnerPosition())}));
				loadApproverUser(voucherHeader.getType());
			}else{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed updateAdvancePayment.");
				return "advancePaymentModify";
			}
		}catch(ValidationException e){
			LOGGER.error("Error"+e.getMessage(),e);
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=1 and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
			loadbankBranch(paymentheader.getBankaccount().getFund());
			throw new ValidationException(e.getErrors());
		}catch(Exception e){
			addDropdownData("bankaccountList", persistenceService.findAllBy(" from Bankaccount where bankbranch.id=? and isactive=1 and fund.id=?",paymentheader.getBankaccount().getBankbranch().getId(),paymentheader.getBankaccount().getFund().getId()));
			loadbankBranch(paymentheader.getBankaccount().getFund());
			LOGGER.error(e.getMessage(),e);
			List<ValidationError> errors=new ArrayList<ValidationError>();
			errors.add(new ValidationError("exception",e.getMessage()));
			throw new ValidationException(errors);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed updateAdvancePayment.");
		return "advancePaymentView";
	}
	
	private void validateAdvancePayment()throws ValidationException,EGOVException,ParseException{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting validateAdvancePayment...");
		if(paymentheader.getVoucherheader().getVoucherDate()==null || paymentheader.getVoucherheader().getVoucherDate().equals(""))
			throw new ValidationException(Arrays.asList(new ValidationError("payment.voucherdate.empty","payment.voucherdate.empty")));
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if(!"Auto".equalsIgnoreCase(vNumGenMode) &&( voucherNumberSuffix==null || voucherNumberSuffix.equals("")))
			throw new ValidationException(Arrays.asList(new ValidationError("payment.vouchernumber.empty","payment.vouchernumber.empty")));
		if(parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
			throw new ValidationException(Arrays.asList(new ValidationError("bankbranch.empty","bankbranch.empty")));
		if(parameters.get("paymentheader.bankaccount.id")[0].equals("-1"))
			throw new ValidationException(Arrays.asList(new ValidationError("bankaccount.empty","bankaccount.empty")));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed validateAdvancePayment...");
	}


	private void validateForUpdate()throws ValidationException,EGOVException,ParseException
	{
		List<PaymentBean> tempBillList=new ArrayList<PaymentBean>();
		List<String> expenseTypeList=new ArrayList<String>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting validateForUpdate...");
		if(paymentheader.getVoucherheader().getVoucherDate()==null || paymentheader.getVoucherheader().getVoucherDate().equals(""))
			addFieldError("paymentheader.voucherheader.voucherDate",getMessage("payment.voucherdate.empty"));
		String  vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Payment");
		if(!"Auto".equalsIgnoreCase(vNumGenMode) &&( voucherNumberSuffix==null || voucherNumberSuffix.equals("")))
			addFieldError("paymentheader.voucherheader.voucherNumber",getMessage("payment.vouchernumber.empty"));
		if(parameters.get("paymentheader.bankaccount.bankbranch.id")[0].equals("-1"))
			addFieldError("paymentheader.bankaccount.bankbranch.id",getMessage("bankbranch.empty"));
		if(parameters.get("paymentheader.bankaccount.id")[0].equals("-1"))
			addFieldError("paymentheader.bankaccount.id",getMessage("bankaccount.empty"));
		if(billList==null)
			addFieldError("paymentheader.bankaccount.id",getMessage("bill.details.empty"));
		
		/*if(!parameters.get("paymentheader.bankaccount.id")[0].equals("-1") && paymentheader.getVoucherheader().getVoucherDate()!=null)
		{
			balance = paymentService.getAccountBalance(parameters.get("paymentheader.bankaccount.id")[0], formatter.format(paymentheader.getVoucherheader().getVoucherDate()),paymentheader.getPaymentAmount(),paymentheader.getId()); 
			if(BigDecimal.valueOf(Long.valueOf(parameters.get("grandTotal")[0])).compareTo(balance)==1)
				addFieldError("balance",getMessage("insufficient.bank.balance"));
		}*/
		int i=0;
		rtgsDefaultMode=paymentService.getAppConfValForCJVPaymentModeRTGS();
		boolean selectedContractorPay=false;               
		for(PaymentBean bean : billList)
		{
			tempBillList=new ArrayList<PaymentBean>();
			tempBillList.add(bean);
			if(expenseTypeList.size()!=0 && expenseTypeList.contains(bean.getExpType())){
				continue;      
			}else{
				expenseTypeList.add(bean.getExpType());       
			}
			if(bean.getIsSelected()){        
				i++;
				if(bean.getPaymentAmt().compareTo(BigDecimal.ZERO)<=0)
					addFieldError("billList["+i+"].paymentAmt",getMessage("payment.amount.null"));
				if(rtgsDefaultMode!=null &&  rtgsDefaultMode.equalsIgnoreCase("Y") 
						&& bean.getExpType().equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS)){
					if( bean.getBillDate().compareTo(rtgsModeRestrictionDateForCJV) > 0
	                         && !paymentheader.getType().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)) {
						selectedContractorPay=true;
						break;
					}
				}
			}       
		}
		
		try{    
			for(int j=0;j<expenseTypeList.size();j++){
				if(paymentheader.getType().equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS)){
					//if(expenseTypeList.get(j).equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS))
						paymentService.validateRTGSPaymentForModify(tempBillList);
					/*if(expenseTypeList.get(j).equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE))
						paymentService.validateRTGSPaymentForModify(tempBillList);
					if(expenseTypeList.get(j).equalsIgnoreCase(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT))
						paymentService.validateRTGSPaymentForModify(tempBillList);*/
				}
			}  
		}catch(ValidationException e){
			addFieldError(e.getErrors().get(0).getMessage(), getMessage(e.getErrors().get(0).getMessage()));
			//addFieldError("rtgs.payment.mandatory.details.missing",getMessage("rtgs.payment.mandatory.details.missing"));
		}
		
		if(selectedContractorPay){                  
			addFieldError("contractor.bills.only.rtgs.payment",getMessage("contractor.bills.only.rtgs.payment"));
			//throw new ValidationException(Arrays.asList(new ValidationError("Mode of payment for contractor bills should only be RTGS For Bill Date Greater than 01-Oct-2013", "Mode of payment for contractor bills should only be RTGS For Bill Date Greater than 01-Oct-2013")));
		}
		if(i==0)
			addFieldError("paymentheader.bankaccount.id",getMessage("bill.details.empty"));
		
		/*
		 * Commenting since Salary payable is not subledger and Party is DO-bank name
		 * can use this in future when salary payable is subledger financial needs to generate advice
		 * 
		 * if(paymentheader.getType().equalsIgnoreCase("RTGS"))
			paymentService.validateRTGSPaymentForModify(billList);*/
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed validateForUpdate.");
	}
	public void validate()
	{
		checkMandatory("fundId",Constants.FUND,voucherHeader.getFundId(),"voucher.fund.mandatory");
		checkMandatory("vouchermis.departmentid",Constants.DEPARTMENT,voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
		checkMandatory("vouchermis.function",Constants.FUNCTION,voucherHeader.getVouchermis().getFunction(),"voucher.function.mandatory");
		checkMandatory("vouchermis.schemeid",Constants.SCHEME,voucherHeader.getVouchermis().getSchemeid(),"voucher.scheme.mandatory");
		checkMandatory("vouchermis.subschemeid",Constants.SUBSCHEME,voucherHeader.getVouchermis().getSubschemeid(),"voucher.subscheme.mandatory");
		checkMandatory("vouchermis.functionary",Constants.FUNCTIONARY,voucherHeader.getVouchermis().getFunctionary(),"voucher.functionary.mandatory");
		checkMandatory("fundsourceId",Constants.FUNDSOURCE,voucherHeader.getVouchermis().getFundsource(),"voucher.fundsource.mandatory");
		checkMandatory("vouchermis.divisionId",Constants.FIELD,voucherHeader.getVouchermis().getDivisionid(),"voucher.field.mandatory");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed validate.");
	}
	private void checkMandatory(String objectName,String fieldName,Object value,String errorKey) 
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside checkMandatory.");
		if(mandatoryFields.contains(fieldName) && value == null)
			addFieldError(objectName, getMessage(errorKey));
	}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
	private void loadApproverUser(String type){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("----------------------Starting loadApproverUser...");
		String scriptName = "paymentHeader.nextDesg";
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		//defaultDept = voucherService.getCurrentDepartment().getId();
		//if(LOGGER.isInfoEnabled())     LOGGER.info("defaultDept :"+defaultDept);
		Map<String, Object>  map = new HashMap<String, Object>(); 
		if(paymentheader!=null && paymentheader.getVoucherheader().getFiscalPeriodId()!=null){
			map = voucherService.getDesgByDeptAndTypeAndVoucherDate("", scriptName, paymentheader.getVoucherheader().getVoucherDate(), paymentheader);
		}else{
			map = voucherService.getDesgByDeptAndTypeAndVoucherDate("", scriptName, new Date(), paymentheader);
		}
		boolean isDeptMandate=false;
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf("|"));
				String mandate = value.substring(value.indexOf("|")+1);
				if(header.equalsIgnoreCase("department")){
					if(mandate.equalsIgnoreCase("M")){
						isDeptMandate = true;
					}
					break;
				}
			}
		}
		if(isDeptMandate){
			//  If the department is mandatory show the logged in users assigned department only.
			addDropdownData("departmentList", voucherHelper.getAllAssgnDeptforUser());
		}else{
			addDropdownData("departmentList", masterCache.get("egi-department"));
		}
		List<Map<String,Object>> desgList  =  (List<Map<String,Object>>) map.get("designationList");
		String  strDesgId = "", dName = "";
		boolean bDefaultDeptId = false;
		List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
		Map<String, Object> desgFuncryMap;
		for(Map<String,Object> desgIdAndName : desgList) {
			desgFuncryMap = new HashMap<String, Object>();
			
			if(desgIdAndName.get("designationName") != null ) {
				desgFuncryMap.put("designationName",(String) desgIdAndName.get("designationName"));
			}
			
			if(desgIdAndName.get("designationId") != null ) {
				strDesgId = (String) desgIdAndName.get("designationId");
				if(strDesgId.indexOf("~") != -1) {
					strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
					dName = (String) desgIdAndName.get("designationId");
					dName = dName.substring(dName.indexOf('~')+1);
					bDefaultDeptId = true;
				}
				desgFuncryMap.put("designationId",strDesgId);
			}
			designationList.add(desgFuncryMap);
		}
		map.put("designationList", designationList);
		
		addDropdownData("designationList", (List<DesignationMaster>)map.get("designationList")); 
		if(bDefaultDeptId && !dName.equals("")) {
			Department dept = (Department) persistenceService.find("from Department where deptName like '%"+dName+"' ");
			defaultDept = dept.getId().intValue();
		}
		wfitemstate = map.get("wfitemstate")!=null?map.get("wfitemstate").toString():"";
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed loadApproverUser.");	
	}
	
	protected String getMessage(String key) {
		return getText(key);
	}
	protected String getMessage(String key,String[] value) {
		return getText(key,value);
	}
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public List<EgBillregister> getContractorBillList() {
		return contractorBillList;
	}
	public void setContractorBillList(List<EgBillregister> contractorBillList) {
		this.contractorBillList = contractorBillList;
	}
	public List<EgBillregister> getSupplierBillList() {
		return supplierBillList;
	}
	public void setSupplierBillList(List<EgBillregister> supplierBillList) {
		this.supplierBillList = supplierBillList;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setCommonsService(final CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	public Map<String, String> getPayeeMap() {
		return payeeMap;
	}
	public void setPayeeMap(Map<String, String> payeeMap) {
		this.payeeMap = payeeMap;
	}
	public List<EgBillregister> getTotalBillList() {
		return totalBillList;
	}
	public void setTotalBillList(List<EgBillregister> totalBillList) {
		this.totalBillList = totalBillList;
	}
	public List<Bankaccount> getBankaccountList() {
		return bankaccountList;
	}
	public void setBankaccountList(List<Bankaccount> bankaccountList) {
		this.bankaccountList = bankaccountList;
	}
	public Paymentheader getPaymentheader() {
		return paymentheader;
	}
	/**
	 * 
	 * @return
	 */
	public Paymentheader getPayment(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getPayment...");
		String paymentid=null;
		//System.out.println("Payment id is"+parameters.get(PAYMENTID));
		if(parameters.get(PAYMENTID)==null || "".equals(parameters.get(PAYMENTID)))
		{
			Object obj =getSession().get(PAYMENTID);
			if(obj!=null)
			{
				paymentid=(String)obj;	
			}
		}else
		{
			paymentid=parameters.get(PAYMENTID)[0];            
		}
		if(paymentheader==null && paymentid!=null && !"".equals(paymentid) || (paymentheader!=null && null==paymentheader.getId())) 
		{                  
			paymentheader =  paymentService.find("from Paymentheader where id=?", Long.valueOf(paymentid));    
			
		}                            
		if(paymentheader==null)
		{
			paymentheader=new Paymentheader();
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getPayment.");
		return paymentheader;
	}
	public void setPaymentheader(Paymentheader paymentheader) {
		this.paymentheader = paymentheader;
	}
	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Map<Long,BigDecimal> getDeductionAmtMap() {
		return deductionAmtMap;
	}
	public void setDeductionAmtMap(Map<Long,BigDecimal> deductionAmtMap) {
		this.deductionAmtMap = deductionAmtMap;
	}
	public Map<Long,BigDecimal> getPaidAmtMap() {
		return paidAmtMap;
	}
	public void setPaidAmtMap(Map<Long,BigDecimal> paidAmtMap) {
		this.paidAmtMap = paidAmtMap;
	}
	public String getVoucherdate() {
		return voucherdate;
	}
	public void setVoucherdate(String voucherdate) {
		this.voucherdate = voucherdate;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public List<Miscbilldetail> getMiscBillList() {
		return miscBillList;
	}
	public void setMiscBillList(List<Miscbilldetail> miscBillList) {
		this.miscBillList = miscBillList;
	}
	public int getMiscount() {
		return miscount;
	}
	public void setMiscount(int miscount) {
		this.miscount = miscount;
	}
	public Integer getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(Integer bankaccount) {
		this.bankaccount = bankaccount;
	}
	public Integer getBankbranch() {
		return bankbranch;
	}
	public void setBankbranch(Integer bankbranch) {
		this.bankbranch = bankbranch;
	}
	public List<PaymentBean> getBillList() {
		return billList;
	}
	public void setBillList(List<PaymentBean> billList) {
		this.billList = billList;
	}
	public String getContractorIds() {
		return contractorIds;
	}
	public void setContractorIds(String contractorIds) {
		this.contractorIds = contractorIds;
	}
	public String getSalaryIds() {
		return salaryIds;
	}
	public void setSalaryIds(String salaryIds) {
		this.salaryIds = salaryIds;
	}
	public String getSupplierIds() {
		return supplierIds;
	}
	public void setSupplierIds(String supplierIds) {
		this.supplierIds = supplierIds;
	}
	public String getVouchernumber() {
		return vouchernumber;
	}
	public void setVouchernumber(String vouchernumber) {
		this.vouchernumber = vouchernumber;
	}
	public EgBillregister getBillregister() {
		return billregister;
	}
	public void setBillregister(EgBillregister billregister) {
		this.billregister = billregister;
	}
	public void setPaymentWorkflowService(SimpleWorkflowService<Paymentheader> paymentWorkflowService) {
		this.paymentWorkflowService = paymentWorkflowService;
	}

	public boolean isDepartmentDefault() {
		return isDepartmentDefault;
	}
	public void setDepartmentDefault(boolean isDepartmentDefault) {
		this.isDepartmentDefault = isDepartmentDefault;
	}
	public List<InstrumentHeader> getInstrumentHeaderList() {
		return instrumentHeaderList;
	}
	public void setInstrumentHeaderList(List<InstrumentHeader> instrumentHeaderList) {
		this.instrumentHeaderList = instrumentHeaderList;
	}
	public List<PaymentBean> getContractorList() {
		return contractorList;
	}
	public void setContractorList(List<PaymentBean> contractorList) {
		this.contractorList = contractorList;
	}
	public List<PaymentBean> getSupplierList() {
		return supplierList;
	}
	public void setSupplierList(List<PaymentBean> supplierList) {
		this.supplierList = supplierList;
	}
	public List<Paymentheader> getPaymentheaderList() {
		return paymentheaderList;
	}
	public void setPaymentheaderList(List<Paymentheader> paymentheaderList) {
		this.paymentheaderList = paymentheaderList;
	}
	public String getVoucherNumberPrefix() {
		return voucherNumberPrefix;
	}
	public void setVoucherNumberPrefix(String voucherNumberPrefix) {
		this.voucherNumberPrefix = voucherNumberPrefix;
	}
	public String getVoucherNumberSuffix() {
		return voucherNumberSuffix;
	}
	public void setVoucherNumberSuffix(String voucherNumberSuffix) {
		this.voucherNumberSuffix = voucherNumberSuffix;
	}
	public List<EgBillregister> getContingentBillList() {
		return contingentBillList;
	}
	public void setContingentBillList(List<EgBillregister> contingentBillList) {
		this.contingentBillList = contingentBillList;
	}
	public List<EgBillregister> getSalaryBillList() {
		return salaryBillList;
	}
	public void setSalaryBillList(List<EgBillregister> salaryBillList) {
		this.salaryBillList = salaryBillList;
	}
	public List<PaymentBean> getContingentList() {
		return contingentList;
	}
	public void setContingentList(List<PaymentBean> contingentList) {
		this.contingentList = contingentList;
	}
	public List<PaymentBean> getSalaryList() {
		return salaryList;
	}
	public void setSalaryList(List<PaymentBean> salaryList) {
		this.salaryList = salaryList;
	}
	public String getContingentIds() {
		return contingentIds;
	}
	public void setContingentIds(String contingentIds) {
		this.contingentIds = contingentIds;
	}
	public String getWfitemstate() {
		return wfitemstate;
	}
	public void setWfitemstate(String wfitemstate) {
		this.wfitemstate = wfitemstate;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getDefaultDept() {
		return defaultDept;
	}
	public void setDefaultDept(Integer defaultDept) {
		this.defaultDept = defaultDept;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getTypeOfAccount() {
		return typeOfAccount;
	}
	public void setTypeOfAccount(String typeOfAccount) {
		this.typeOfAccount = typeOfAccount;
	}
	public void setAdvanceRequisition(List<EgAdvanceRequisition> advanceRequisition) {
		this.advanceRequisitionList = advanceRequisition;
	}
	public List<EgAdvanceRequisition> getAdvanceRequisitionList() {
		return advanceRequisitionList;
	}

	public void setDisableExpenditureType(boolean disableExpenditureType) {
		this.disableExpenditureType = disableExpenditureType;
	}

	public boolean isDisableExpenditureType() {
		return disableExpenditureType;
	}

	public void setVoucherHelper(VoucherHelper voucherHelper) {
		this.voucherHelper = voucherHelper;
	}
	public boolean isChangePartyName() {
		return changePartyName;
	}
	public void setChangePartyName(boolean changePartyName) {
		this.changePartyName = changePartyName;
	}
	public String getNewPartyname() {
		return newPartyname;
	}
	public void setNewPartyname(String newPartyname) {
		this.newPartyname = newPartyname;
	}

	public boolean isEnablePensionType() {
		return enablePensionType;
	}

	public void setEnablePensionType(boolean enablePensionType) {
		this.enablePensionType = enablePensionType;
	}

	public List<EgBillregister> getPensionBillList() {
		return pensionBillList;
	}

	public void setPensionBillList(List<EgBillregister> pensionBillList) {
		this.pensionBillList = pensionBillList;
	}

	public List<PaymentBean> getPensionList() {
		return pensionList;
	}

	public void setPensionList(List<PaymentBean> pensionList) {
		this.pensionList = pensionList;
	}

	public String getPensionIds() {
		return pensionIds;
	}

	public void setPensionIds(String pensionIds) {
		this.pensionIds = pensionIds;
	}

	public Long getFunctionSel() {
		return functionSel;
	}

	public void setFunctionSel(Long functionSel) {
		this.functionSel = functionSel;
	}

	public String getRtgsDefaultMode() {
		return rtgsDefaultMode;
	}

	public void setRtgsDefaultMode(String rtgsDefaultMode) {
		this.rtgsDefaultMode = rtgsDefaultMode;
	}

	public Date getRtgsModeRestrictionDateForCJV() {
		return rtgsModeRestrictionDateForCJV;
	}

	public void setRtgsModeRestrictionDateForCJV(Date rtgsModeRestrictionDateForCJV) {
		this.rtgsModeRestrictionDateForCJV = rtgsModeRestrictionDateForCJV;
	}

	public String getPaymentRestrictionDateForCJV() {
		return paymentRestrictionDateForCJV;
	}

	public void setPaymentRestrictionDateForCJV(String paymentRestrictionDateForCJV) {
		paymentRestrictionDateForCJV = paymentRestrictionDateForCJV;
	}

	public String getBillSubType() {
		return billSubType;
	}

	public void setBillSubType(String billSubType) {
		this.billSubType = billSubType;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	public Map<Integer, String> getMonthMap() {
		return monthMap;
	}

	public void setMonthMap(Map<Integer, String> monthMap) {
		this.monthMap = monthMap;
	}
	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}

	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	public ScriptService getScriptService() {
		return scriptService;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

	
}
