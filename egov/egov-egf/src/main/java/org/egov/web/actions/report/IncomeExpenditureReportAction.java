package org.egov.web.actions.report;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.services.report.IncomeExpenditureScheduleService;
import org.egov.services.report.IncomeExpenditureService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.FlushMode;
import org.hibernate.Query;

@Results(value={
		@Result(name="PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=IncomeExpenditureStatement.pdf"}),
		@Result(name="XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=IncomeExpenditureStatement.xls"})
	})
	
@ParentPackage("egov")
public class IncomeExpenditureReportAction extends BaseFormAction{
	private static final String INCOME_EXPENSE_PDF = "PDF";
	private static final String INCOME_EXPENSE_XLS = "XLS";
	private static SimpleDateFormat FORMATDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	InputStream inputStream;
	ReportHelper reportHelper;
	Statement incomeExpenditureStatement = new Statement();
	IncomeExpenditureService incomeExpenditureService;
	IncomeExpenditureScheduleService incomeExpenditureScheduleService;
	private String majorCode;
	private String minorCode;
	private String scheduleNo;
	private String financialYearId;
	private String previousYearFromDate;
	private String currentYearFromDate;
	//private String asOndate;
	private Date todayDate;
	private String asOnDateRange;
	private String period;
	private Integer fundId;
	private StringBuffer heading=new StringBuffer();
	private StringBuffer scheduleheading=new StringBuffer();
	private StringBuffer statementheading=new StringBuffer();
	List<CChartOfAccounts> listChartOfAccounts ;
	private boolean detailReport=false;

	
	public void setIncomeExpenditureService(IncomeExpenditureService incomeExpenditureService) {
		this.incomeExpenditureService = incomeExpenditureService;
	}
	
	public void setIncomeExpenditureScheduleService(IncomeExpenditureScheduleService incomeExpenditureScheduleService) {
		this.incomeExpenditureScheduleService = incomeExpenditureScheduleService;
	}

	public void setReportHelper(final ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public Statement getIncomeExpenditureStatement() {
		return incomeExpenditureStatement;
	}
	
	public IncomeExpenditureReportAction(){
		addRelatedEntity("department", Department.class);
		addRelatedEntity("function", CFunction.class);
		addRelatedEntity("functionary", Functionary.class);
		addRelatedEntity("financialYear",CFinancialYear.class);
		addRelatedEntity("field",Boundary.class);
		addRelatedEntity("fund",Fund.class);
	}
	
	public void prepare(){
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		super.prepare();
		if(!parameters.containsKey("showDropDown")){
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			addDropdownData("departmentList", masterCache.get("egi-department"));
			addDropdownData("functionList", masterCache.get("egi-function"));
			addDropdownData("functionaryList", masterCache.get("egi-functionary"));  
			addDropdownData("fundDropDownList", masterCache.get("egi-fund"));
			addDropdownData("fieldList", masterCache.get("egi-ward"));
			addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1  order by finYearRange desc "));
		}
	}

	protected void setRelatedEntitesOn() {
		setTodayDate(new Date());
		if(incomeExpenditureStatement.getFund()!= null && incomeExpenditureStatement.getFund().getId()!=null && incomeExpenditureStatement.getFund().getId()!=0){
			incomeExpenditureStatement.setFund((Fund)getPersistenceService().find("from Fund where id=?", incomeExpenditureStatement.getFund().getId()));
			heading.append(" in "+incomeExpenditureStatement.getFund().getName());
		}
		if(incomeExpenditureStatement.getDepartment() != null && incomeExpenditureStatement.getDepartment().getId()!=null && incomeExpenditureStatement.getDepartment().getId()!=0){
			incomeExpenditureStatement.setDepartment((Department)getPersistenceService().find("from Department where id=?", incomeExpenditureStatement.getDepartment().getId()));
			heading.append(" in "+incomeExpenditureStatement.getDepartment().getName()+" Department");
		}else{
			incomeExpenditureStatement.setDepartment(null);
		}
		if(incomeExpenditureStatement.getFinancialYear() != null && incomeExpenditureStatement.getFinancialYear().getId() !=null && incomeExpenditureStatement.getFinancialYear().getId() !=0){
			incomeExpenditureStatement.setFinancialYear((CFinancialYear)getPersistenceService().find("from CFinancialYear where id=?", incomeExpenditureStatement.getFinancialYear().getId()));
			heading.append(" for the Financial Year "+incomeExpenditureStatement.getFinancialYear().getFinYearRange());
		}
		if(incomeExpenditureStatement.getFunction() != null && incomeExpenditureStatement.getFunction().getId() != null && incomeExpenditureStatement.getFunction().getId() != 0){
			incomeExpenditureStatement.setFunction((CFunction)getPersistenceService().find("from CFunction where id=?", incomeExpenditureStatement.getFunction().getId()));
			heading.append(" in Function Code "+incomeExpenditureStatement.getFunction().getName());
		}
		if(incomeExpenditureStatement.getField() != null && incomeExpenditureStatement.getField().getId()!=null && incomeExpenditureStatement.getField().getId()!=0){
			incomeExpenditureStatement.setField((Boundary)getPersistenceService().find("from Boundary where id=?", incomeExpenditureStatement.getField().getId()));
			heading.append(" in the field value"+incomeExpenditureStatement.getField().getName());
		}
		
		if(incomeExpenditureStatement.getFunctionary() != null && incomeExpenditureStatement.getFunctionary().getId()!=null && incomeExpenditureStatement.getFunctionary().getId()!=0){
			incomeExpenditureStatement.setFunctionary((Functionary)getPersistenceService().find("from Functionary where id=?", incomeExpenditureStatement.getFunctionary().getId()));
			heading.append(" and "+incomeExpenditureStatement.getFunctionary().getName()+" Functionary");
		}
		
	}
	
	public void setIncomeExpenditureStatement(Statement incomeExpenditureStatement) {
		this.incomeExpenditureStatement = incomeExpenditureStatement;
	}

	@Override
	public Object getModel() {
		return incomeExpenditureStatement;
	}
	
@Action(value="/report/incomeExpenditureReport-generateIncomeExpenditureReport")
	public String generateIncomeExpenditureReport(){
		return "report";
	}
	
@Action(value="/report/incomeExpenditureReport-generateIncomeExpenditureSubReport")
	public String generateIncomeExpenditureSubReport(){
		setDetailReport(false);
		populateDataSourceForSchedule();
		return "scheduleResults";
	}
	  
@Action(value="/report/incomeExpenditureReport-generateScheduleReport")
	public String generateScheduleReport(){
		populateDataSourceForAllSchedules();
		return "allScheduleResults";
	}
@Action(value="/report/incomeExpenditureReport-generateDetailCodeReport")
	public String generateDetailCodeReport(){
		setDetailReport(true);
		populateSchedulewiseDetailCodeReport();
		return "scheduleResults";
	}
	  

	private void populateSchedulewiseDetailCodeReport() {
		setRelatedEntitesOn();
		scheduleheading.append("Income And Expenditure Schedule Statement").append(heading);
		if(incomeExpenditureStatement.getFund()!= null && incomeExpenditureStatement.getFund().getId()!=null && incomeExpenditureStatement.getFund().getId()!=0){
			List<Fund> fundlist=new ArrayList<Fund>();
			fundlist.add(incomeExpenditureStatement.getFund());
			incomeExpenditureStatement.setFunds(fundlist);
			incomeExpenditureScheduleService.populateDetailcode(incomeExpenditureStatement);
			
		}else{
			incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
			incomeExpenditureScheduleService.populateDetailcode(incomeExpenditureStatement);
		}	
	}
	
private void populateDataSourceForSchedule() {
	setDetailReport(false);
		setRelatedEntitesOn();
		
		scheduleheading.append("Income And Expenditure Schedule Statement").append(heading);
		if(incomeExpenditureStatement.getFund()!= null && incomeExpenditureStatement.getFund().getId()!=null && incomeExpenditureStatement.getFund().getId()!=0){
			List<Fund> fundlist=new ArrayList<Fund>();
			fundlist.add(incomeExpenditureStatement.getFund());
			incomeExpenditureStatement.setFunds(fundlist);
			incomeExpenditureScheduleService.populateDataForLedgerSchedule(incomeExpenditureStatement,parameters.get("majorCode")[0]);
			
		}else{
			incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
			incomeExpenditureScheduleService.populateDataForLedgerSchedule(incomeExpenditureStatement,parameters.get("majorCode")[0]);
			
		
		}	
	}

	private void populateDataSourceForAllSchedules() {
		setRelatedEntitesOn();
		if(incomeExpenditureStatement.getFund()!= null && incomeExpenditureStatement.getFund().getId()!=null && incomeExpenditureStatement.getFund().getId()!=0){
			List<Fund> fundlist=new ArrayList<Fund>();
			fundlist.add(incomeExpenditureStatement.getFund());
			incomeExpenditureStatement.setFunds(fundlist);
			incomeExpenditureScheduleService.populateDataForAllSchedules(incomeExpenditureStatement);
		}else{
			incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
			incomeExpenditureScheduleService.populateDataForAllSchedules(incomeExpenditureStatement);
		}
	}

	public String printIncomeExpenditureReport(){
		populateDataSource();
		return "report";
	}
	
@Action(value="/report/incomeExpenditureReport-ajaxPrintIncomeExpenditureReport")
	public String ajaxPrintIncomeExpenditureReport(){
		populateDataSource();
		return "results";
	}
	protected void populateDataSource() {
		
		setRelatedEntitesOn();
		
		statementheading.append("Income And Expenditure Statement").append(heading);
		if(incomeExpenditureStatement.getFund()!= null && incomeExpenditureStatement.getFund().getId()!=null && incomeExpenditureStatement.getFund().getId()!=0){
			List<Fund> fundlist=new ArrayList<Fund>();
			fundlist.add(incomeExpenditureStatement.getFund());
			incomeExpenditureStatement.setFunds(fundlist);
			incomeExpenditureService.populateIEStatement(incomeExpenditureStatement);  
		}else{
			incomeExpenditureStatement.setFunds(incomeExpenditureService.getFunds());
			incomeExpenditureService.populateIEStatement(incomeExpenditureStatement);		
		}
	}
	
@Action(value="/report/incomeExpenditureReport-generateIncomeExpenditurePdf")
	public String generateIncomeExpenditurePdf() throws Exception{
		populateDataSource();
		String heading=getUlbName()+"\\n"+statementheading.toString();
		String subtitle="Report Run Date-"+FORMATDDMMYYYY.format(getTodayDate());
		JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement,heading,
				getPreviousYearToDate(),getCurrentYearToDate(),subtitle,true);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return INCOME_EXPENSE_PDF;
	}
	
@Action(value="/report/incomeExpenditureReport-generateDetailCodePdf")
	public String generateDetailCodePdf() throws Exception{
		populateSchedulewiseDetailCodeReport();
		String heading=getUlbName()+"\\n"+statementheading.toString();
		String subtitle="Report Run Date-"+FORMATDDMMYYYY.format(getTodayDate());
		JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement,heading,
				getPreviousYearToDate(),getCurrentYearToDate(),subtitle,true);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return INCOME_EXPENSE_PDF;
	}
@Action(value="/report/incomeExpenditureReport-generateDetailCodeXls")
	public String generateDetailCodeXls() throws Exception{
		populateSchedulewiseDetailCodeReport();
		String heading=getUlbName()+"\\n"+statementheading.toString();
		String subtitle="Report Run Date-"+FORMATDDMMYYYY.format(getTodayDate())+"                                               ";
		JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement,heading,
				getPreviousYearToDate(),getCurrentYearToDate(),subtitle,true);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return INCOME_EXPENSE_XLS;
	}
	
	public String getUlbName() {
		Query query = HibernateUtil.getCurrentSession().createSQLQuery(
				"select name from companydetail");
		List<String> result = query.list();
		if (result != null)
			return result.get(0);
		return "";
	}
	
@Action(value="/report/incomeExpenditureReport-generateIncomeExpenditureXls")
	public String generateIncomeExpenditureXls() throws Exception{
		populateDataSource();
		String heading=getUlbName()+"\\n"+statementheading.toString();
		String subtitle="Report Run Date-"+FORMATDDMMYYYY.format(getTodayDate())+"                                               ";
		JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement,heading,
				getPreviousYearToDate(),getCurrentYearToDate(),subtitle,true);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return INCOME_EXPENSE_XLS;
	}

@Action(value="/report/incomeExpenditureReport-generateSchedulePdf")
	public String generateSchedulePdf() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.heading"),heading.toString(),
				getPreviousYearToDate(),getCurrentYearToDate(),false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return INCOME_EXPENSE_PDF;
	}
	
@Action(value="/report/incomeExpenditureReport-generateScheduleXls")
	public String generateScheduleXls() throws Exception{
		populateDataSourceForAllSchedules();
		JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(incomeExpenditureStatement,getText("report.ie.heading"),heading.toString(),
				getPreviousYearToDate(),getCurrentYearToDate(),false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return INCOME_EXPENSE_XLS;
	}

	
@Action(value="/report/incomeExpenditureReport-generateIncomeExpenditureSchedulePdf")
	public String generateIncomeExpenditureSchedulePdf() throws Exception{
		populateDataSourceForSchedule();
		String heading=getUlbName()+"\\n"+scheduleheading.toString();
		String subtitle="Report Run Date-"+FORMATDDMMYYYY.format(getTodayDate())+"                                             ";
		JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement,heading,
				getPreviousYearToDate(),getCurrentYearToDate(),subtitle,false);
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return INCOME_EXPENSE_PDF;
	}
	
@Action(value="/report/incomeExpenditureReport-generateIncomeExpenditureScheduleXls")
	public String generateIncomeExpenditureScheduleXls() throws Exception{
		populateDataSourceForSchedule();
		String heading=getUlbName()+"\\n"+scheduleheading.toString();
		// Blank space for space didvidion between left and right corner
		String subtitle="Report Run Date-"+FORMATDDMMYYYY.format(getTodayDate())+"					  						 ";
		JasperPrint jasper = reportHelper.generateIncomeExpenditureReportJasperPrint(incomeExpenditureStatement,heading,
				getPreviousYearToDate(),getCurrentYearToDate(),subtitle,false);
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return INCOME_EXPENSE_XLS;
	}
   
	
	public String getCurrentYearToDate(){
		return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getToDate(incomeExpenditureStatement));
	}
	public String getPreviousYearToDate(){
		return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(incomeExpenditureService.getToDate(incomeExpenditureStatement)));
	}
	public String getCurrentYearFromDate(){
		return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getFromDate(incomeExpenditureStatement));
	}
	public String getPreviousYearFromDate(){
		return incomeExpenditureService.getFormattedDate(incomeExpenditureService.getPreviousYearFor(incomeExpenditureService.getFromDate(incomeExpenditureStatement)));
	}

	public Date getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

	public String getMinorCode() {
		return minorCode;
	}

	public void setMinorCode(String minorCode) {
		this.minorCode = minorCode;
	}

	public String getScheduleNo() {
		return scheduleNo;
	}

	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}

	public List<CChartOfAccounts> getListChartOfAccounts() {
		return listChartOfAccounts;
	}

	public void setListChartOfAccounts(List<CChartOfAccounts> listChartOfAccounts) {
		this.listChartOfAccounts = listChartOfAccounts;
	}

	public String getFinancialYearId() {
		return financialYearId;
	}

	public void setFinancialYearId(String financialYearId) {
		this.financialYearId = financialYearId;
	}
	
	public Integer getFundId() {
		return fundId;
	}

	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}


	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getAsOnDateRange() {
		return asOnDateRange;
	}

	public void setAsOnDateRange(String asOnDateRange) {
		this.asOnDateRange = asOnDateRange;
	}

	public StringBuffer getScheduleheading() {
		return scheduleheading;
	}

	public void setScheduleheading(StringBuffer scheduleheading) {
		this.scheduleheading = scheduleheading;
	}

	public StringBuffer getStatementheading() {
		return statementheading;
	}

	public void setStatementheading(StringBuffer statementheading) {
		this.statementheading = statementheading;
	}

	public boolean isDetailReport() {
		return detailReport;
	}

	public void setDetailReport(boolean detailReport) {
		this.detailReport = detailReport;
	}

}
