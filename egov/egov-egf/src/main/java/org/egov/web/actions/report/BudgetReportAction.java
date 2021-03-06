package org.egov.web.actions.report;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.FlushMode;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
@Results(value={
		@Result(name="PDF",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/pdf","contentDisposition","no-cache;filename=BudgetReport.pdf"}),
		@Result(name="XLS",type="stream",location="inputStream", params={"inputName","inputStream","contentType","application/xls","contentDisposition","no-cache;filename=BudgetReport.xls"})
	})
@ParentPackage("egov")
public class BudgetReportAction extends BaseFormAction{
	ReportHelper reportHelper;
	private BudgetDetail budgetDetail = new BudgetDetail();
	private List<BudgetReportView> budgetDetailsList = new ArrayList<BudgetReportView>();
	private BudgetDetailService budgetDetailService;
	private List<Budget> budgetList = new ArrayList<Budget>();
	FinancialYearDAO financialYearDAO;
	private boolean canViewREApprovedAmount = false;
	private boolean canViewBEApprovedAmount = false;
	private BudgetService budgetService;
	BudgetDetailHelper budgetDetailHelper;
	private InputStream inputStream;
	private boolean showResults = false;
	private String currentYearRange = "";
	private String nextYearRange = "";
	private String lastYearRange = "";
	
	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public void setBudgetDetailHelper(BudgetDetailHelper budgetHelper) {
		this.budgetDetailHelper = budgetHelper;
	}

	public BudgetReportAction() {
		addRelatedEntity(Constants.BUDGET_GROUP, BudgetGroup.class);
		addRelatedEntity(Constants.EXECUTING_DEPARTMENT, Department.class);
		addRelatedEntity(Constants.BUDGET, Budget.class);
	}
	
	@Override
	public void prepare() {
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		setupDropdownsInHeader();
	}
	
	@Override
	public String execute() throws Exception {
		return "form";
	}
	
@Action(value="/report/budgetReport-ajaxLoadBudgets")
	public String ajaxLoadBudgets(){
		String isbere = budgetDetail.getBudget().getIsbere();
		if(budgetDetail.getBudget()!=null && budgetDetail.getBudget().getFinancialYear()!=null && isbere!=null){
			Long finYearId = budgetDetail.getBudget().getFinancialYear().getId();
			setBudgetList(getPersistenceService().findAllBy("from Budget where isbere=? and financialYear.id=? and isPrimaryBudget=1 " +
					"and isActiveBudget=1 and id not in (select parent from Budget where parent is not null and isbere=? and " +
					"financialYear.id=? and isPrimaryBudget=1) order by name",isbere,finYearId,isbere,finYearId));
		}
		return "budgets";
	}

	private void setupDropdownsInHeader() {
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		setupDropdownDataExcluding(Constants.SUB_SCHEME);
		dropdownData.put("budgetGroupList", masterCache.get("egf-budgetGroup"));
		dropdownData.put("executingDepartmentList", masterCache.get("egi-department"));
		addDropdownData("financialYearList", budgetService.getFYForNonApprovedBudgets());
		List<String> isbereList = new ArrayList<String>(); 
		isbereList.add("BE");
		isbereList.add("RE"); 
		dropdownData.put("isbereList", isbereList);
	}

	public boolean canViewApprovedAmount(Budget budget){
		return budgetDetailService.canViewApprovedAmount(persistenceService,budget);
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	private Date getNextYearFor(Date date) {
		GregorianCalendar previousYearToDate = new GregorianCalendar();
		previousYearToDate.setTime(date);
	    int prevYear = previousYearToDate.get(Calendar.YEAR) + 1;
	    previousYearToDate.set(Calendar.YEAR,prevYear);
		return previousYearToDate.getTime();
	}
	
	public String exportXls() throws Exception{
		generateReport();
		JasperPrint jasper = reportHelper.generateBudgetReportJasperPrint(budgetDetailsList,getParamMap().get("heading").toString(),
				canViewBEApprovedAmount,canViewREApprovedAmount,lastYearRange,currentYearRange,nextYearRange);
		inputStream = reportHelper.exportXls(inputStream, jasper);
	    return "XLS";
	}
	
	Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String budgetName = "";
		if(budgetDetail.getBudget()!=null)
			budgetName = budgetDetail.getBudget().getName();
		paramMap.put("heading", "Budget Report For "+budgetName);
		paramMap.put("enableReApproved", Boolean.valueOf(canViewREApprovedAmount));
		paramMap.put("enableBeApproved", Boolean.valueOf(canViewBEApprovedAmount));
		return paramMap;
	}

	public String exportPdf() throws Exception{
		generateReport();
		JasperPrint jasper = reportHelper.generateBudgetReportJasperPrint(budgetDetailsList,getParamMap().get("heading").toString(),
				canViewBEApprovedAmount,canViewREApprovedAmount,lastYearRange,currentYearRange,nextYearRange);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
	    return "PDF";
	}


	@ValidationErrorPage(value="form")
	public String generateReport(){
		showResults = true;
		CFinancialYear finYear = budgetService.find("from Budget where id=?",budgetDetail.getBudget().getId()).getFinancialYear();
		List<BudgetDetail> currentYearBeList = new ArrayList<BudgetDetail>();
		List<BudgetDetail> nextYearBeList = new ArrayList<BudgetDetail>();
		List<BudgetDetail> lastYearBe = new ArrayList<BudgetDetail>();
		List<BudgetDetail> lastYearRe = new ArrayList<BudgetDetail>();
		Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
		if("BE".equalsIgnoreCase(b.getIsbere())){
			CFinancialYear previousYear = budgetDetailHelper.getPreviousYearFor(finYear);
			if(previousYear!=null){
				lastYearBe = budgetDetailService.findAllBy("from BudgetDetail where budget.financialYear.id=? and budget.isPrimaryBudget=1 and " +
							"budget.isActiveBudget=1 and budget.isbere='BE'", previousYear.getId());
				lastYearRe = budgetDetailService.findAllBy("from BudgetDetail where budget.financialYear.id=? and budget.isPrimaryBudget=1 and " +
							"budget.isActiveBudget=1 and budget.isbere='RE'", previousYear.getId());
			}
		}else{
			nextYearBeList = populateNextYearBe(finYear); 
		}
		List<BudgetDetail> results = budgetDetailService.findAllBudgetDetailsForParent(budgetDetail.getBudget(),budgetDetail,persistenceService);
		for (BudgetDetail detail : results) {
			BudgetReportView view = new BudgetReportView();
			view.setId(detail.getId());
			view.setDepartmentCode(detail.getExecutingDepartment().getCode());
			view.setFunctionCode(detail.getFunction().getCode());
			view.setBudgetGroupName(detail.getBudgetGroup().getName());
			if("BE".equalsIgnoreCase(detail.getBudget().getIsbere())){
				view.setBeNextYearApproved(detail.getApprovedAmount());
				view.setBeNextYearOriginal(detail.getOriginalAmount());
				for (BudgetDetail budgetDetail : lastYearBe) {
					if(compareDetails(budgetDetail, detail)){
						view.setBeCurrentYearApproved(budgetDetail.getApprovedAmount());
					}
				}
				for (BudgetDetail budgetDetail : lastYearRe) {
					if(compareDetails(budgetDetail, detail)){
						view.setReCurrentYearApproved(budgetDetail.getApprovedAmount());
						view.setReCurrentYearOriginal(budgetDetail.getOriginalAmount());
					}
				}
			}else{
				view.setReCurrentYearApproved(detail.getApprovedAmount());
				view.setReCurrentYearOriginal(detail.getOriginalAmount());
				currentYearBeList = populateCurrentYearBe();
				for (BudgetDetail budgetDetail : currentYearBeList) {
					if(compareDetails(budgetDetail, detail)){
						view.setBeCurrentYearApproved(budgetDetail.getApprovedAmount());
					}
				}
			}
			for (BudgetDetail nextYear : nextYearBeList) {
				if(compareDetails(nextYear,detail)){
					view.setBeNextYearApproved(nextYear.getApprovedAmount());
					view.setBeNextYearOriginal(nextYear.getOriginalAmount());
				}
			}
			budgetDetailsList.add(view);
		}
		populatePreviousYearActuals(results,budgetDetail.getBudget().getFinancialYear());
		ajaxLoadBudgets();
		populateYearRange();
		canViewREApprovedAmount = canViewApprovedAmount(budgetDetail.getBudget());
		canViewBEApprovedAmount = canViewApprovedAmount(budgetService.getReferenceBudgetFor(budgetDetail.getBudget()));
		return "form";
	}

	private void populateYearRange() {
		CFinancialYear financialYear = budgetDetail.getBudget().getFinancialYear();
		if(financialYear!=null){
			if("BE".equalsIgnoreCase(budgetDetail.getBudget().getIsbere())){
				currentYearRange = budgetDetailHelper.computePreviousYearRange(financialYear.getFinYearRange());
				lastYearRange = currentYearRange;
				nextYearRange = budgetDetailHelper.computeNextYearRange(currentYearRange);
			}else{
				currentYearRange = financialYear.getFinYearRange();
				lastYearRange = budgetDetailHelper.computePreviousYearRange(currentYearRange);
				nextYearRange = budgetDetailHelper.computeNextYearRange(currentYearRange);
			}
		}
	}

	private boolean compareDetails(BudgetDetail nextYear,BudgetDetail current) {
		if(nextYear.getExecutingDepartment()!=null && current.getExecutingDepartment()!=null && current.getExecutingDepartment().getId()!=nextYear.getExecutingDepartment().getId())
			return false;
		if(nextYear.getFunction()!=null && current.getFunction()!=null && current.getFunction().getId()!=nextYear.getFunction().getId())
			return false;
		if(nextYear.getFund()!=null && current.getFund()!=null && current.getFund().getId()!=nextYear.getFund().getId())
			return false;
		if(nextYear.getFunctionary()!=null && current.getFunctionary()!=null && current.getFunctionary().getId()!=nextYear.getFunctionary().getId())
			return false;
		if(nextYear.getScheme()!=null && current.getScheme()!=null && current.getScheme().getId()!=nextYear.getScheme().getId())
			return false;
		if(nextYear.getSubScheme()!=null && current.getSubScheme()!=null && current.getSubScheme().getId()!=nextYear.getSubScheme().getId())
			return false;
		if(nextYear.getBoundary()!=null && current.getBoundary()!=null && current.getBoundary().getId()!=nextYear.getBoundary().getId())
			return false;
		if(nextYear.getBudgetGroup()!=null && current.getBudgetGroup()!=null && current.getBudgetGroup().getId()!=nextYear.getBudgetGroup().getId())
			return false;
		if(nextYear.getBudget()!=null && current.getBudget()!=null && current.getBudget().getId()==nextYear.getBudget().getId())
			return false;
		return true;
	}

	private List<BudgetDetail> populateNextYearBe(CFinancialYear finYear) {
		BudgetDetail detail = new BudgetDetail();
		detail.copyFrom(budgetDetail);
		detail.setBudget(null);
		Date nextYear = getNextYearFor(finYear.getStartingDate());
		return budgetDetailService.searchByCriteriaWithTypeAndFY(financialYearDAO.getFinYearByDate(nextYear).getId(),"BE",detail);
	}
	
	private List<BudgetDetail> populateCurrentYearBe() {
		BudgetDetail detail = new BudgetDetail();
		detail.copyFrom(budgetDetail);
		detail.setBudget(null);
		return budgetDetailService.searchByCriteriaWithTypeAndFY(budgetDetail.getBudget().getFinancialYear().getId(),"BE",detail);
	}

	protected ValueStack getValueStack() {
		return ActionContext.getContext().getValueStack();
	}

	private void populatePreviousYearActuals(List<BudgetDetail> budgetDetails,CFinancialYear financialYear) {
		if(financialYear!=null && financialYear.getId()!=null)
			financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id=?",financialYear.getId());
		Map<String, Object> paramMap;
		for (BudgetDetail detail : budgetDetails) {
			paramMap = budgetDetailHelper.constructParamMap(getValueStack(),detail);
			BigDecimal amount = budgetDetailHelper.getTotalPreviousActualData(paramMap, financialYear.getEndingDate());
			for (BudgetReportView row : budgetDetailsList) {
				if(row.getId().equals(detail.getId()))
					row.setActualsLastYear(amount==null?BigDecimal.ZERO:amount);
			}
		}
	}

	public Date subtractYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	@Override
	public Object getModel() {
		return budgetDetail;
	}

	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}

	public BudgetDetail getBudgetDetail() {
		return budgetDetail;
	}

	public void setBudgetDetailsList(List<BudgetReportView> budgetDetailsList) {
		this.budgetDetailsList = budgetDetailsList;
	}

	public List<BudgetReportView> getBudgetDetailsList() {
		return budgetDetailsList;
	}

	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}

	public void setBudgetList(List<Budget> budgetList) {
		this.budgetList = budgetList;
	}

	public List<Budget> getBudgetList() {
		return budgetList;
	}

	public boolean getCanViewREApprovedAmount() {
		return canViewREApprovedAmount;
	}

	public boolean getCanViewBEApprovedAmount() {
		return canViewBEApprovedAmount;
	}

	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}

	public void setShowResults(boolean showResults) {
		this.showResults = showResults;
	}

	public boolean getShowResults() {
		return showResults;
	}

	public void setCurrentYearRange(String currentYearRange) {
		this.currentYearRange = currentYearRange;
	}

	public String getCurrentYearRange() {
		return currentYearRange;
	}

	public void setNextYearRange(String nextYearRange) {
		this.nextYearRange = nextYearRange;
	}

	public String getNextYearRange() {
		return nextYearRange;
	}

	public void setLastYearRange(String lastYearRange) {
		this.lastYearRange = lastYearRange;
	}

	public String getLastYearRange() {
		return lastYearRange;
	}
}
