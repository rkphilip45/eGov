package org.egov.web.actions.report;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetingType;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

@Results(value={
		@Result(name="PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetAppropriationRegisterRepor.pdf"}),
		@Result(name="XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetAppropriationRegisterRepor.xls"})
	})

@ParentPackage("egov")
public class BudgetAppropriationRegisterReportAction  extends BaseFormAction {
	private static final Logger LOGGER = Logger.getLogger(BudgetAppropriationRegisterReportAction.class);
	String jasperpath = "/org/egov/web/actions/report/BudgetAppReport.jasper";
	private Department department = new Department();
	private CFunction function = new CFunction();
	private Fund fund = new Fund();
	private BudgetGroup budgetGroup = new BudgetGroup();
	private List<BudgetAppDisplay> budgetAppropriationRegisterList = new ArrayList<BudgetAppDisplay>();
	private List<BudgetAppDisplay> updatedBdgtAppropriationRegisterList = new ArrayList<BudgetAppDisplay>();
	private String budgetHead;
	private BigDecimal totalGrant;
	BudgetDetailsDAO budgetDetailsDAO;
	FinancialYearDAO financialYearDAO;
	PersistenceService persistenceService;
	ReportHelper reportHelper;
	private InputStream inputStream;
	private String strAsOnDate;
	String financialYearId ="";
	Date dtAsOnDate = null;
	private BigDecimal addtionalAppropriationForBe = BigDecimal.ZERO;
	private BigDecimal addtionalAppropriationForRe = BigDecimal.ZERO;
	private BigDecimal beAmount;
	private BigDecimal reAmount=BigDecimal.ZERO;
	private String finYearRange;
	protected List<String> mandatoryFields = new ArrayList<String>();
	private BudgetDetailConfig budgetDetailConfig;
	private BudgetService budgetService;
	private boolean isBeDefined=true;
	private boolean isReDefined=true;
	private GenericHibernateDaoFactory genericDao;
	private Boolean shouldShowREAppropriations=false; 

	public BudgetAppropriationRegisterReportAction() {
		addRelatedEntity(Constants.FUNCTION, CFunction.class);
		addRelatedEntity(Constants.EXECUTING_DEPARTMENT, Department.class);
		addRelatedEntity(Constants.FUND, Fund.class);
	}
	
	public void prepare() {
		super.prepare();
		mandatoryFields = budgetDetailConfig.getMandatoryFields();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		dropdownData.put("functionList",  masterCache.get("egi-function"));
		dropdownData.put("executingDepartmentList", masterCache.get("egi-department"));
		dropdownData.put("budgetGroupList", masterCache.get("egf-budgetGroup"));
		dropdownData.put("fundList", masterCache.get("egi-fund"));
		if(department.getId()!=null && department.getId()!=-1)
			department = (Department)persistenceService.find("from Department where id=?",department.getId());
		if(function.getId()!=null && function.getId()!=-1)
			function = (CFunction)persistenceService.find("from CFunction where id=?",function.getId());
		if(fund.getId()!=null && fund.getId()!=-1)
			fund = (Fund)persistenceService.find("from Fund where id=?",fund.getId());
		if(budgetGroup.getId()!=null && budgetGroup.getId()!=-1)
			budgetGroup = (BudgetGroup)persistenceService.find("from BudgetGroup where id=?",budgetGroup.getId());
	}

	@SkipValidation
@Action(value="/report/budgetAppropriationRegisterReport-search")
	public String search() {
		CFinancialYear financialYear = new CFinancialYear();
		if(parameters.get("asOnDate")[0] != null) {
			strAsOnDate = parameters.get("asOnDate")[0];
			try {
				dtAsOnDate = Constants.DDMMYYYYFORMAT2.parse(strAsOnDate);
				financialYear	 = financialYearDAO.getFinancialYearByDate(dtAsOnDate);
			} catch (ParseException e) {
				if(LOGGER.isInfoEnabled())     LOGGER.info("ParseException the date :"+e.getMessage());
			}
		}
		// Get this to show at header level
		if(budgetService.hasApprovedBeForYear(financialYear.getId()))
		{
			beAmount = getBudgetBEorREAmt("BE");
		}
		else
		{
			isBeDefined=false;
			isReDefined=false;
		}
		// -- Consider RE if RE is present & approved for the current yr.
		if(budgetService.hasApprovedReForYear(Long.parseLong(financialYearId)))
		{
			reAmount = getBudgetBEorREAmt("RE");
			if(getConsiderReAppropriationAsSeperate())
				totalGrant = reAmount.add(addtionalAppropriationForRe);
			else
				totalGrant = reAmount;
		}
		else if(budgetService.hasApprovedBeForYear(Long.parseLong(financialYearId)))
		{
			isReDefined=false;
			totalGrant = beAmount.add(addtionalAppropriationForBe);
		}
		generateReport();
		return "result";
	}
	
	private void generateReport() {
		CFinancialYear financialYear = null;
		financialYear = (CFinancialYear) financialYearDAO.getFinancialYearById(Long.valueOf(financialYearId));
		finYearRange = financialYear.getFinYearRange();
		Date dStartDate = financialYear.getStartingDate();
		String strAODate = Constants.DDMMYYYYFORMAT1.format(dtAsOnDate);
		String strStDate = Constants.DDMMYYYYFORMAT1.format(dStartDate);
		
		Query query = null;
		
		if(budgetGroup != null) {
			budgetHead = budgetGroup.getName();
			String strQuery = "select vmis.budgetary_appnumber as bdgApprNumber, vh.vouchernumber as VoucherNumber, vh.voucherdate as voucherDate, vh.description as description, "+
			" null as billNumber, null as billDate, gl.debitamount as debitAmount, gl.creditamount as creditAmount from generalledger gl, vouchermis vmis,  "+
			" voucherheader vh  where vh.id = gl.voucherheaderid and vh.id = vmis.voucherheaderid and  gl.glcodeid = "+budgetGroup.getMinCode().getId()+" and  "+
			" (vmis.budgetary_appnumber  != 'null' and vmis.budgetary_appnumber is not null) and vh.status != 4 and vh.voucherdate  between '"+ strStDate+"' and '"+strAODate+"'  "+
			getFunctionQuery("gl.functionid")+getDepartmentQuery("vmis.departmentid")+getFundQuery("vh.fundid")+" "+
			" union "+
			" select distinct bmis.budgetary_appnumber as bdgApprNumber, vh.vouchernumber as VoucherNumber, vh.voucherdate as voucherDate , "+
			" br.narration as description, br.billnumber as billNumber, br.billdate as billDate,   bd.debitamount as debitAmount, bd.creditamount as creditAmount  "+
			" from eg_billdetails bd, eg_billregistermis bmis, eg_billregister br, voucherHeader vh where br.id = bd.billid and br.id = bmis.billid and  bd.glcodeid = "+budgetGroup.getMinCode().getId()+" and (bmis.budgetary_appnumber != 'null' and bmis.budgetary_appnumber is not null) "+
			" and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL')) and (vh.id = bmis.voucherheaderid )  and br.billdate  between '"+ strStDate+"' and '"+strAODate+"' "+getFunctionQuery("bd.functionid")+getDepartmentQuery("bmis.departmentid")+
			getFundQuery("bmis.fundid")+"  "+
			" union "+
			" select distinct bmis.budgetary_appnumber as bdgApprNumber, null as VoucherNumber, null as voucherDate , "+
			" br.narration as description, br.billnumber as billNumber, br.billdate as billDate,   bd.debitamount as debitAmount, bd.creditamount as creditAmount from eg_billdetails bd, eg_billregistermis bmis, eg_billregister br  "+
			" where br.id = bd.billid and br.id = bmis.billid and  bd.glcodeid = "+budgetGroup.getMinCode().getId()+" and (bmis.budgetary_appnumber != 'null' and bmis.budgetary_appnumber is not null) "+
			" and br.statusid not in (select id from egw_status where description='Cancelled' and moduletype in ('EXPENSEBILL', 'SALBILL', 'WORKSBILL', 'PURCHBILL', 'CBILL', 'SBILL', 'CONTRACTORBILL')) and bmis.voucherheaderid is null and br.billdate   between '"+ strStDate+"' and '"+strAODate+"' "+getFunctionQuery("bd.functionid")+getDepartmentQuery("bmis.departmentid")+
			getFundQuery("bmis.fundid")+"  order by bdgApprNumber ";
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("BudgetAppropriationRegisterReportAction -- strQuery...."+strQuery);
			
			query = HibernateUtil.getCurrentSession().createSQLQuery(strQuery)
			.addScalar("bdgApprNumber")
			.addScalar("voucherDate")
			.addScalar("billDate")
			.addScalar("creditAmount")
			.addScalar("description")
			.addScalar("VoucherNumber")
			.addScalar("billNumber")
			.addScalar("debitAmount")
			.addScalar("creditAmount")
			.setResultTransformer(Transformers.aliasToBean(BudgetAppDisplay.class));
		}
		budgetAppropriationRegisterList = query.list();
		

		List<BudgetAppDisplay> budgetApprRegNewList = new ArrayList<BudgetAppDisplay>();
		List<BudgetAppDisplay> budgetApprRegUpdatedList1 = new ArrayList<BudgetAppDisplay>();
		HashMap<String, BudgetAppDisplay> regMap = new HashMap<String, BudgetAppDisplay>();
		if(budgetAppropriationRegisterList.size() > 0) {
			String strsubQuery = "select vmis.budgetary_appnumber as bdgApprNumber, vh.vouchernumber as VoucherNumber, vh.voucherdate as voucherDate, vh.description as description, "+
			" br.billnumber as billNumber, br.billdate as billDate, gl.debitamount as debitAmount, gl.creditamount as creditAmount from generalledger gl, vouchermis vmis,  "+
			" voucherheader vh,  eg_billregistermis bmis, eg_billregister br  where vh.id = gl.voucherheaderid and vh.id = vmis.voucherheaderid and vh.id = bmis.voucherheaderid and bmis.billid = br.id " +
			" and  gl.glcodeid = "+budgetGroup.getMinCode().getId()+" and  "+
			" (vmis.budgetary_appnumber  != 'null' and vmis.budgetary_appnumber is not null) and vh.status != 4 and vh.voucherdate  between '"+ strStDate+"' and '"+strAODate+"'  "+
			getFunctionQuery("gl.functionid")+getDepartmentQuery("vmis.departmentid")+getFundQuery("vh.fundid")+"  order by bdgApprNumber ";
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("BudgetAppropriationRegisterReportAction -- strsubQuery...."+strsubQuery);
			
			 query = HibernateUtil.getCurrentSession().createSQLQuery(strsubQuery)
			.addScalar("bdgApprNumber")
			.addScalar("voucherDate")
			.addScalar("billDate")
			.addScalar("creditAmount")
			.addScalar("description")
			.addScalar("VoucherNumber")
			.addScalar("billNumber")
			.addScalar("debitAmount")
			.addScalar("creditAmount")
			.setResultTransformer(Transformers.aliasToBean(BudgetAppDisplay.class));
			 budgetApprRegNewList = query.list();
			 if(budgetApprRegNewList.size() > 0) {
				 for(BudgetAppDisplay budgetAppRtDisp : budgetApprRegNewList) {
					 regMap.put(budgetAppRtDisp.getBdgApprNumber(), budgetAppRtDisp);
				 }
				 
				for(BudgetAppDisplay budgetAppropriationRegisterDisp : budgetAppropriationRegisterList) {
					if(regMap.containsKey(budgetAppropriationRegisterDisp.getBdgApprNumber()) ) {
						budgetApprRegUpdatedList1.add(regMap.get(budgetAppropriationRegisterDisp.getBdgApprNumber()));
					} else {
						budgetApprRegUpdatedList1.add(budgetAppropriationRegisterDisp);
					}
				}
			 }
		}
		if(budgetApprRegUpdatedList1.size() > 0) {
			budgetAppropriationRegisterList.clear();
			budgetAppropriationRegisterList.addAll(budgetApprRegUpdatedList1);
		}
		updateBdgtAppropriationList();
	}

	private String getFundQuery(String string) {
		String query = "";
		if(fund.getId()!=null && fund.getId()!=-1)
			return " and "+string+" = "+fund.getId();
		return query;
	}

	private String getFunctionQuery(String string) {
		String query = "";
		if(function.getId()!=null && function.getId()!=-1)
			return " and "+string+" = "+function.getId();
		return query;
	}

	private String getDepartmentQuery(String string) {
		String query = "";
		if(department.getId()!=null && department.getId()!=-1)
			return " and "+string+" = "+department.getId();
		return query;
	}

	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}

	private void updateBdgtAppropriationList() {
		BigDecimal cumulativeAmt = null;
		BigDecimal balanceAvailableAmt = new  BigDecimal(0.0);
		BigDecimal totalDebit = new  BigDecimal(0.0);
		BigDecimal totalCredit = new  BigDecimal(0.0);
		if(totalGrant == null)
			totalGrant = new  BigDecimal(0.0);
		
		if(LOGGER.isInfoEnabled())     LOGGER.info("budgetAppropriationRegisterList.size() :"+budgetAppropriationRegisterList.size());
		if(budgetAppropriationRegisterList.size() > 0) {
			int iSerialNumber = 1;
			for(BudgetAppDisplay budgetAppropriationRegisterDisp : budgetAppropriationRegisterList) {
				if(BudgetingType.DEBIT.equals(budgetGroup.getBudgetingType())){
					if(budgetAppropriationRegisterDisp.getDebitAmount() != null && budgetAppropriationRegisterDisp.getDebitAmount().compareTo(BigDecimal.ZERO) == 1) {
					
					   budgetAppropriationRegisterDisp.setBillAmount(budgetAppropriationRegisterDisp.getDebitAmount());
					   totalDebit=totalDebit.add(budgetAppropriationRegisterDisp.getBillAmount());
					}else{
						
						budgetAppropriationRegisterDisp.setBillAmount(budgetAppropriationRegisterDisp.getCreditAmount().multiply(new BigDecimal("-1")));
						totalCredit=totalCredit.add(budgetAppropriationRegisterDisp.getBillAmount().abs());
					}
				}
				if(BudgetingType.CREDIT.equals(budgetGroup.getBudgetingType())){
					if(budgetAppropriationRegisterDisp.getCreditAmount() != null && budgetAppropriationRegisterDisp.getCreditAmount().compareTo(BigDecimal.ZERO) == 1) {
						
						   budgetAppropriationRegisterDisp.setBillAmount(budgetAppropriationRegisterDisp.getCreditAmount());
						   totalCredit=totalCredit.add(budgetAppropriationRegisterDisp.getBillAmount());
						}else{
							
							budgetAppropriationRegisterDisp.setBillAmount(budgetAppropriationRegisterDisp.getDebitAmount().multiply(new BigDecimal("-1")));
							totalDebit=totalDebit.add(budgetAppropriationRegisterDisp.getBillAmount().abs());
						}
				}
				if(BudgetingType.ALL.equals(budgetGroup.getBudgetingType())){
					if(budgetAppropriationRegisterDisp.getDebitAmount() != null && budgetAppropriationRegisterDisp.getDebitAmount().compareTo(BigDecimal.ZERO) == 1) {
						budgetAppropriationRegisterDisp.setBillAmount(budgetAppropriationRegisterDisp.getDebitAmount());
					} else {
						budgetAppropriationRegisterDisp.setBillAmount(budgetAppropriationRegisterDisp.getCreditAmount().multiply(new BigDecimal("-1")));
					}
				}
				if(cumulativeAmt == null) {
					if(BudgetingType.ALL.equals(budgetGroup.getBudgetingType())){ 
						cumulativeAmt = budgetAppropriationRegisterDisp.getBillAmount();
					}else if(BudgetingType.CREDIT.equals(budgetGroup.getBudgetingType())){
						cumulativeAmt=totalCredit.subtract(totalDebit);
					}else if(BudgetingType.DEBIT.equals(budgetGroup.getBudgetingType())){
						cumulativeAmt=totalDebit.subtract(totalCredit);
					}
					budgetAppropriationRegisterDisp.setCumulativeAmount(cumulativeAmt);
				} else {
					//when budgeting type is 'ALL', to calculate the cumulative balance,
					//if the debit amount>0, add the debit amount to cumulative amount
					//if the credit amount>0, subtract the credit amount from the cumulative amount
					if(BudgetingType.ALL.equals(budgetGroup.getBudgetingType())){ 
						if(budgetAppropriationRegisterDisp.getDebitAmount() != null && budgetAppropriationRegisterDisp.getDebitAmount().compareTo(BigDecimal.ZERO) == 1) {
							cumulativeAmt=budgetAppropriationRegisterDisp.getBillAmount().abs().add(cumulativeAmt);
							budgetAppropriationRegisterDisp.setCumulativeAmount(cumulativeAmt);      
						} else {   
							cumulativeAmt=cumulativeAmt.subtract(budgetAppropriationRegisterDisp.getBillAmount().abs());
							budgetAppropriationRegisterDisp.setCumulativeAmount(cumulativeAmt);
						}
					}else if(BudgetingType.CREDIT.equals(budgetGroup.getBudgetingType())){
						cumulativeAmt=cumulativeAmt.add(totalCredit.subtract(totalDebit));
						budgetAppropriationRegisterDisp.setCumulativeAmount(cumulativeAmt);
					}else if(BudgetingType.DEBIT.equals(budgetGroup.getBudgetingType())){
						cumulativeAmt=cumulativeAmt.add(totalDebit.subtract(totalCredit));
						budgetAppropriationRegisterDisp.setCumulativeAmount(cumulativeAmt);
					}
					
				}
				//when budgeting type is 'ALL', to calculate the running balance,
				//if the debit amount>0, subtract the cumulative from running balance
				//if the credit amount>0, add the cumulative to running balance
				if(BudgetingType.ALL.equals(budgetGroup.getBudgetingType())){ 
					if(budgetAppropriationRegisterDisp.getDebitAmount() != null && budgetAppropriationRegisterDisp.getDebitAmount().compareTo(BigDecimal.ZERO) == 1) {
						balanceAvailableAmt = totalGrant.subtract(budgetAppropriationRegisterDisp.getCumulativeAmount().abs());
					} else {
						balanceAvailableAmt = totalGrant.add(budgetAppropriationRegisterDisp.getCumulativeAmount());
					}
				}else{
					  balanceAvailableAmt = totalGrant.subtract(budgetAppropriationRegisterDisp.getCumulativeAmount());
				}
				budgetAppropriationRegisterDisp.setBalanceAvailableAmount(balanceAvailableAmt);
				budgetAppropriationRegisterDisp.setSerailNumber(Integer.toString(iSerialNumber));
				updatedBdgtAppropriationRegisterList.add(budgetAppropriationRegisterDisp);
				totalCredit=totalCredit.ZERO;totalDebit=totalDebit.ZERO;
				iSerialNumber++;
			}
		}
	}
	

	private BigDecimal getBudgetBEorREAmt(String type) {
		BigDecimal approvedAmount = new  BigDecimal(0.0);
		try {
			CFinancialYear financialYear = (CFinancialYear) financialYearDAO.getFinancialYearById(Long.valueOf(financialYearId));
			List<BudgetDetail> budgedDetailList = new ArrayList<BudgetDetail>();
			String query = " from BudgetDetail bd where bd.budget.isbere=? and bd.budgetGroup=? and bd.budget.financialYear=? ";
			if(department.getId()!=null && department.getId()!=-1)
					query = query+" and bd.executingDepartment.id="+department.getId();
			if(function.getId()!=null && function.getId()!=-1)
				query = query+" and bd.function.id="+function.getId();
			if(fund.getId()!=null && fund.getId()!=-1)
				query = query+" and bd.fund.id="+fund.getId();
			budgedDetailList = persistenceService.findAllBy(query, type, budgetGroup, financialYear);
			if(budgedDetailList != null && budgedDetailList.size() > 0) {
				for (BudgetDetail bdetail : budgedDetailList) {
					approvedAmount = approvedAmount.add(bdetail.getApprovedAmount());
					if("RE".equalsIgnoreCase(type)  && !getConsiderReAppropriationAsSeperate())
					{
						approvedAmount=approvedAmount.add(bdetail.getApprovedReAppropriationsTotal());
						continue;
					}
					else
					{
						if("BE".equalsIgnoreCase(type))
							addtionalAppropriationForBe = addtionalAppropriationForBe.add(bdetail.getApprovedReAppropriationsTotal());
						else
						{
							shouldShowREAppropriations=true;
							addtionalAppropriationForRe = addtionalAppropriationForRe.add(bdetail.getApprovedReAppropriationsTotal());
						}
					}
				}
			}
		} catch (ValidationException e) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("ValidationException while fetching BudgetBEorREAmt :"+e.getMessage());
			return new BigDecimal(0.0);
		}
		return approvedAmount;
	}
	
	private Map<String, Object> getParamMapForReportFile() {
		Map<String,Object> paramMapForReportFile = new HashMap<String,Object>();
		paramMapForReportFile.put("bgname", budgetHead);
		paramMapForReportFile.put("deptName", department.getName());
		paramMapForReportFile.put("function", function.getName());
		paramMapForReportFile.put("fund", fund.getName());
		
		String rBEorREAmountForm = " - ("+finYearRange+") (Rs.)  : ";
		paramMapForReportFile.put("rAsOnDate", strAsOnDate);
		if(isBeDefined)
		{
			paramMapForReportFile.put("rBE", rBEorREAmountForm+beAmount.toString());
			paramMapForReportFile.put("rAddiApprBe", addtionalAppropriationForBe.toString() );
		}
		else
		{
			paramMapForReportFile.put("rBE", rBEorREAmountForm+"Budget Not Defined ");
			paramMapForReportFile.put("rAddiApprBe", "" );
		}
		if(isReDefined)
		{
			paramMapForReportFile.put("rRE", rBEorREAmountForm+reAmount.toString());
			paramMapForReportFile.put("rAddiApprRe", addtionalAppropriationForRe.toString() );
		}
		else
		{
			paramMapForReportFile.put("rRE", rBEorREAmountForm);
			paramMapForReportFile.put("rAddiApprRe", "" );
		}
		paramMapForReportFile.put("showREAppr", shouldShowREAppropriations.toString());
		return paramMapForReportFile;
	}
	
@Action(value="/report/budgetAppropriationRegisterReport-generatePdf")
	public String generatePdf() throws JRException, IOException {
		updatedBdgtAppropriationRegisterList = new ArrayList<BudgetAppDisplay>();
		search();
		List<Object> data = new ArrayList<Object>();
		data.addAll(getUpdatedBdgtAppropriationRegisterList());
		inputStream = reportHelper.exportPdf(getInputStream(), jasperpath, getParamMapForReportFile(), data);
		return "PDF";
	}

@Action(value="/report/budgetAppropriationRegisterReport-generateXls")
	public String generateXls() throws JRException, IOException{
		updatedBdgtAppropriationRegisterList = new ArrayList<BudgetAppDisplay>();
		search();
		List<Object> data = new ArrayList<Object>();
		data.addAll(getUpdatedBdgtAppropriationRegisterList());
		inputStream = reportHelper.exportXls(getInputStream(), jasperpath, getParamMapForReportFile(), data);
		return "XLS";
	}
	
	public String getFormattedDate(Date date){
		SimpleDateFormat formatter = Constants.DDMMYYYYFORMAT1;
		return formatter.format(date);
	}
	
	public void setBudgetDetailConfig(BudgetDetailConfig budgetDetailConfig) {
		this.budgetDetailConfig = budgetDetailConfig;
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	
	@Override
	public String execute() throws Exception {
		return "form";
	}
	
	@Override
	public Object getModel() {
		return null;
	}
	
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public CFunction getFunction() {
		return function;
	}

	public void setFunction(CFunction function) {
		this.function = function;
	}

	public List<BudgetAppDisplay> getBudgetAppropriationRegisterList() {
		return budgetAppropriationRegisterList;
	}

	public void setBudgetAppropriationRegisterList(
			List<BudgetAppDisplay> budgetAppropriationRegisterList) {
		this.budgetAppropriationRegisterList = budgetAppropriationRegisterList;
	}

	public String getBudgetHead() {
		return budgetHead;
	}

	public void setBudgetHead(String budgetHead) {
		this.budgetHead = budgetHead;
	}

	public BigDecimal getTotalGrant() {
		return totalGrant;
	}

	public void setTotalGrant(BigDecimal totalGrant) {
		this.totalGrant = totalGrant;
	}

	public BudgetDetailsDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}
	public BudgetGroup getBudgetGroup() {
		return budgetGroup;
	}

	public void setBudgetGroup(BudgetGroup budgetGroup) {
		this.budgetGroup = budgetGroup;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public FinancialYearDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public List<BudgetAppDisplay> getUpdatedBdgtAppropriationRegisterList() {
		return updatedBdgtAppropriationRegisterList;
	}

	public void setUpdatedBdgtAppropriationRegisterList(
			List<BudgetAppDisplay> updatedBdgtAppropriationRegisterList) {
		this.updatedBdgtAppropriationRegisterList = updatedBdgtAppropriationRegisterList;
	}

	public String getStrAsOnDate() {
		return strAsOnDate;
	}

	public void setStrAsOnDate(String strAsOnDate) {
		this.strAsOnDate = strAsOnDate;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	

	public BigDecimal getBeAmount() {
		return beAmount;
	}

	public void setBeAmount(BigDecimal beAmount) {
		this.beAmount = beAmount;
	}

	public BigDecimal getReAmount() {
		return reAmount;
	}

	public void setReAmount(BigDecimal reAmount) {
		this.reAmount = reAmount;
	}

	public String getFinYearRange() {
		return finYearRange;
	}

	public void setFinYearRange(String finYearRange) {
		this.finYearRange = finYearRange;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public Fund getFund() {
		return fund;
	}
	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}

	public void setAddtionalAppropriationForBe(
			BigDecimal addtionalAppropriationForBe) {
		this.addtionalAppropriationForBe = addtionalAppropriationForBe;
	}

	public BigDecimal getAddtionalAppropriationForBe() {
		return addtionalAppropriationForBe;
	}
	public void setAddtionalAppropriationForRe(
			BigDecimal addtionalAppropriationForRe) {
		this.addtionalAppropriationForRe = addtionalAppropriationForRe;
	}

	public BigDecimal getAddtionalAppropriationForRe() {
		return addtionalAppropriationForRe;
	}
	public boolean getIsBeDefined() {
		return isBeDefined;
	}

	public boolean getIsReDefined() {
		return isReDefined;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	private boolean getConsiderReAppropriationAsSeperate(){
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","CONSIDER_RE_REAPPROPRIATION_AS_SEPARATE");
		String appValue = "-1"; 
		appValue = appList.get(0).getValue();
		return "Y".equalsIgnoreCase(appValue); 
	}

	public void setShouldShowREAppropriations(boolean shouldShowREAppropriations) {
		this.shouldShowREAppropriations = shouldShowREAppropriations;
	}

	public boolean getShouldShowREAppropriations() {
		return shouldShowREAppropriations;
	}

}
