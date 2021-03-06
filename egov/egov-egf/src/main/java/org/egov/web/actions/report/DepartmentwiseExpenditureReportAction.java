package org.egov.web.actions.report;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.services.report.DEReportService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

@Results(value={
		@Result(name="PDF",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=DepartmentwiseExpenditureReport.pdf"}),
		@Result(name="XLS",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=DepartmentwiseExpenditureReport.xls"}),
		@Result(name="HTML",type="stream",location=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"text/html",Constants.CONTENT_DISPOSITION,"no-cache;filename=DepartmentwiseExpenditureReport.html"})
	})
@ParentPackage("egov") 
public class DepartmentwiseExpenditureReportAction  extends BaseFormAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InputStream inputStream;
	private ReportHelper reportHelper;
	SimpleDateFormat sqlformat=new SimpleDateFormat("dd-MMM-yyyy");
	private DepartmentwiseExpenditureReport deptReport=new DepartmentwiseExpenditureReport();
	List<DepartmentwiseExpenditureReport> departmentwiseExpList=new ArrayList<DepartmentwiseExpenditureReport>();
	private List<Department> departmentList=new ArrayList<Department>();
	DEReportService  deService =new DEReportService();
	private StringBuffer heading=new StringBuffer();
	private StringBuffer Preheading=new StringBuffer();
	static final String CURRENT="current";
	static final String PREVIOUS="previous";
	final static Logger LOGGER = Logger.getLogger(DepartmentwiseExpenditureReportAction.class);
	
	
	
	public void prepare(){
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
			EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
			super.prepare();
			addDropdownData("fundDropDownList", masterCache.get("egi-fund"));
			addDropdownData("financialYearList", getPersistenceService().findAllBy("from CFinancialYear where isActive=1 " +
					"  and startingDate >='01-Apr-2010' order by finYearRange desc  "));
	}

  
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return deptReport;
	}
	
	@SkipValidation	
	@ValidationErrorPage(value=NEW)
	public String search() throws Exception
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("@-- Inside search method --@");
		setRelatedEntitesOn();
		validateBeforeSearch();
		populateResult();
		
		if (deptReport.getExportType().equals("xls"))
			return generateReportXls();
		else if(deptReport.getExportType().equals("pdf"))
			return generateReportPdf();
		else if(deptReport.getExportType().equals("html"))
			return generateReportHTML();
		
		
		return NEW;
	}
	public void populateResult() throws Exception{
		if(LOGGER.isInfoEnabled())     LOGGER.info(" @-- Inside popgetConcurrenceDateForPeriodulateResult --@");
			populateCurrentYearReport();
			populatePreviousYearReport();
	
	}


	public void populateCurrentYearReport() {
		deptReport.setPeriod(CURRENT);
		deService.populateDepartment(deptReport);
		populateConcurrenceGivenResult();
		populateConcurrenceForthePeriod();
		removeDepartmentWithNoValues();  
		populateDaywiseTotal();
		populateTotalConcurrenceGiven();
		calculateDatewiseTotalAcrossDepartment();
	}
	
	public void populatePreviousYearReport() {
		deptReport.setPeriod(PREVIOUS);
		deService.populateDepartment(deptReport);
		populateConcurrenceGivenResult();
		populateConcurrenceForthePeriod();
		removeDepartmentWithNoValues();
		populateDaywiseTotal();
		populateTotalConcurrenceGiven();
		calculateDatewiseTotalAcrossDepartment();
		
	}
	
	void removeDepartmentWithNoValues() {
		List<DepartmentwiseExpenditureResult> resultList=new LinkedList<DepartmentwiseExpenditureResult>();
		//Set<String> rowTobeRemoved;
		if(deptReport.getPeriod().equalsIgnoreCase("current")){
			resultList=new LinkedList<DepartmentwiseExpenditureResult>();
			for (DepartmentwiseExpenditureResult obj : deptReport.getCurrentyearDepartmentList()) {
					if (!obj.isDepartmentWithNodata()){
						resultList.add(obj);
					}
			}deptReport.setCurrentyearDepartmentList(resultList);
			
		}   
		if(deptReport.getPeriod().equalsIgnoreCase(PREVIOUS)){
			resultList=new LinkedList<DepartmentwiseExpenditureResult>();
			for (DepartmentwiseExpenditureResult obj : deptReport.getPreviousyearDepartmentList()) {
					if (!obj.isDepartmentWithNodata()){
						resultList.add(obj);
					}
			}
			deptReport.setPreviousyearDepartmentList(resultList);
		}    
		
	}

	public void validateBeforeSearch(){
		if(null!= deptReport.getFromDate() && null!= deptReport.getToDate() && deptReport.getFromDate().after(deptReport.getToDate())){
			 throw new ValidationException(Arrays.asList(new ValidationError("date",
					 "from date can not be greater than to date")));
		}
		if(!deService.validateDateRange(deptReport)){
			 throw new ValidationException(Arrays.asList(new ValidationError("date",
					 "from date and to date should be in same financial year")));
		}
		if(deptReport.getReportType().equalsIgnoreCase("daterange")){
			if(deService.getNumberOfDays(deptReport.getFromDate(),deptReport.getToDate())>30){
				throw new ValidationException(Arrays.asList(new ValidationError("date",
						 "Maximun date range of 30 days can be selected")));
			}
		}
		
	}
	/*
	 * This API will get total concurrence amount given from 
	 * the start of financial year till From Date 
	 */  
	@SuppressWarnings("unchecked")
	public void populateConcurrenceGivenResult() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("------------------ Inside populateConcurrenceGivenResult--------------");
		departmentwiseExpList=deService.getConcurrenceGivenFortheFinancialYearTillGivenDate(deptReport);
		/* 
		 * Populating conccurrence given till date for the departments 
		 * 
		 */
		for(DepartmentwiseExpenditureReport obj:departmentwiseExpList){  
			if(deptReport.getPeriod().equalsIgnoreCase("current")){
				if(deptReport.containsDepartmentInResultList(obj.getDepartmentName(),deptReport.getCurrentyearDepartmentList())){
					for(DepartmentwiseExpenditureResult entry : deptReport.getCurrentyearDepartmentList()){
						if (obj.getDepartmentName().equalsIgnoreCase(entry.getDepartmentNm())){
								entry.setConcurrenceGiven(obj.getConcurrenceAmount());
								entry.setDepartmentWithNodata(false);
							break;
						}
					}
				}
				if(LOGGER.isDebugEnabled())     LOGGER.debug(" Concurrence Given till Date for Currentyear List"+deptReport.getCurrentyearDepartmentList().size());
				if(LOGGER.isInfoEnabled())     LOGGER.info(" Concurrence Given  List"+deptReport.getCurrentyearDepartmentList());
			}else if(deptReport.getPeriod().equalsIgnoreCase(PREVIOUS)){
				if(deptReport.containsDepartmentInResultList(obj.getDepartmentName(),deptReport.getPreviousyearDepartmentList())){
					for(DepartmentwiseExpenditureResult entry : deptReport.getPreviousyearDepartmentList()){
						if (obj.getDepartmentName().equalsIgnoreCase(entry.getDepartmentNm())){
								entry.setConcurrenceGiven(obj.getConcurrenceAmount());
								entry.setDepartmentWithNodata(false);
							break;
						}
					}
				}
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug(" Concurrence Given till Date for previous List"+deptReport.getPreviousyearDepartmentList().size());
			//if(LOGGER.isInfoEnabled())     LOGGER.info(" Concurrence Given  List"+deptReport.getPreviousyearDepartmentList());
		}
		
		
	}
	
	
	public void populateConcurrenceForthePeriod() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" --Inside populateConcurrenceForthePeriod--- ");
		//String subDate=Constants.DDMMYYYYFORMAT2.format(obj.getConcurrenceDate()).toString()
		String subDate="";
		//boolean addDepartmentRow=false;
		DepartmentwiseExpenditureResult deptExpResult;
		List<DepartmentwiseExpenditureReport> deList=new ArrayList<DepartmentwiseExpenditureReport>();
		deList =deService.getConcurrenceGivenForthePeriodQuery(deptReport);
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Total concurrence given for the period --- "+deList.size());
		for(DepartmentwiseExpenditureReport obj:deList){  
			subDate=Constants.DDMMYYYYFORMAT2.format(obj.getConcurrenceDate()).toString();
			subDate=subDate.substring(0,subDate.length()-5);
			if(deptReport.getPeriod().equalsIgnoreCase(CURRENT)){
				if(deptReport.containsDepartmentInResultList(obj.getDepartmentName(),deptReport.getCurrentyearDepartmentList())){
					if(LOGGER.isInfoEnabled())     LOGGER.info(" obj.getDepartmentName()------ exists"+obj.getDepartmentName());
					for(DepartmentwiseExpenditureResult entry : deptReport.getCurrentyearDepartmentList()){
						if (obj.getDepartmentName().equalsIgnoreCase(entry.getDepartmentNm())){
							if(obj.getConcurrenceAmount()!=BigDecimal.ZERO)
								entry.getDayAmountMap().put(subDate, obj.getConcurrenceAmount());
								entry.setDepartmentWithNodata(false);
							break;
						}
					}    
					}else{
						//if(LOGGER.isInfoEnabled())     LOGGER.info(" W==Doenst exist=====t "+obj.getDepartmentName());
						deptExpResult=new DepartmentwiseExpenditureResult();
						deptExpResult.setConcurrenceGiven(BigDecimal.ZERO);
						deptExpResult.setDepartmentNm(obj.getDepartmentName());
						deptExpResult.setDepartmentWithNodata(false);
						deptExpResult.getDayAmountMap().put(subDate, obj.getConcurrenceAmount());
						deptReport.getCurrentyearDepartmentList().add(deptExpResult);
					}
				// adding date to date set
				deptReport.getConcurrenceDateSet().add(subDate);
			}else if (deptReport.getPeriod().equalsIgnoreCase(PREVIOUS)){
				if(deptReport.containsDepartmentInResultList(obj.getDepartmentName(),deptReport.getPreviousyearDepartmentList())){
					if(LOGGER.isInfoEnabled())     LOGGER.info(" obj.getDepartmentName()------ exists"+obj.getDepartmentName());
					for(DepartmentwiseExpenditureResult entry : deptReport.getPreviousyearDepartmentList()){
						if (obj.getDepartmentName().equalsIgnoreCase(entry.getDepartmentNm())){
							entry.getDayAmountMap().put(subDate, obj.getConcurrenceAmount());
							entry.setDepartmentWithNodata(false);
							break;
						}
					}
					}else{
						//if(LOGGER.isInfoEnabled())     LOGGER.info(" W==Doenst exist=====t "+obj.getDepartmentName());
						deptExpResult=new DepartmentwiseExpenditureResult();
						deptExpResult.setConcurrenceGiven(BigDecimal.ZERO);
						deptExpResult.setDepartmentNm(obj.getDepartmentName());
						deptExpResult.setDepartmentWithNodata(false);
						deptExpResult.getDayAmountMap().put(subDate, obj.getConcurrenceAmount());
						deptReport.getPreviousyearDepartmentList().add(deptExpResult);
					}
				deptReport.getPreviousConcurrenceDateSet().add(subDate);
			}
		}
	}   
	public void populateTotalConcurrenceGiven() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("------------------ Inside populateTotalConcurrenceGiven--------------");
		BigDecimal totalconGiven= BigDecimal.ZERO;
		DepartmentwiseExpenditureResult deptExpResult=new DepartmentwiseExpenditureResult();
		List<DepartmentwiseExpenditureResult> departmentwiseConcurrenceReport=new ArrayList<DepartmentwiseExpenditureResult>();
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Putting concurrene given values to the depratment map  ");
		if(deptReport.getPeriod().equalsIgnoreCase("current")){
			for(DepartmentwiseExpenditureResult entry : deptReport.getCurrentyearDepartmentList()){
				totalconGiven=BigDecimal.ZERO;
				totalconGiven=totalconGiven.add(entry.getConcurrenceGiven()!=null?entry.getConcurrenceGiven():BigDecimal.ZERO)
						.add(entry.getDayAmountMap().get("Total")!=null?entry.getDayAmountMap().get("Total"):BigDecimal.ZERO);
				
				entry.setTotalConcurrenceGivenTillDate(totalconGiven);
				departmentwiseConcurrenceReport.add(entry);
			}
			deptReport.setCurrentyearDepartmentList(departmentwiseConcurrenceReport);
		}else if(deptReport.getPeriod().equalsIgnoreCase("previous")){
				for(DepartmentwiseExpenditureResult entry : deptReport.getPreviousyearDepartmentList()){
					totalconGiven=BigDecimal.ZERO;
					totalconGiven=totalconGiven.add(entry.getConcurrenceGiven()!=null?entry.getConcurrenceGiven():BigDecimal.ZERO)
							.add(entry.getDayAmountMap().get("Total")!=null?entry.getDayAmountMap().get("Total"):BigDecimal.ZERO);
					
					entry.setTotalConcurrenceGivenTillDate(totalconGiven);
					departmentwiseConcurrenceReport.add(entry);
				}
				deptReport.setPreviousyearDepartmentList(departmentwiseConcurrenceReport);
		}
	}   
	
	public void calculateDatewiseTotalAcrossDepartment() {
		if(LOGGER.isInfoEnabled())     LOGGER.info("------------------ Inside pupulateDatewiseTotalAcrossDepartment--------------");
		BigDecimal conGivenTotal=BigDecimal.ZERO;
		BigDecimal dayConTotal=BigDecimal.ZERO;
		BigDecimal conGivenTillDateTotal=BigDecimal.ZERO;
		Map<String,BigDecimal> dayTotalMap =new LinkedHashMap<String,BigDecimal>();
		DepartmentwiseExpenditureResult deptExpResult=new DepartmentwiseExpenditureResult();
		List<DepartmentwiseExpenditureResult> departmentwiseConcurrenceReport=new ArrayList<DepartmentwiseExpenditureResult>();
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Putting concurrene given values to the depratment map  ");
		if(deptReport.getPeriod().equalsIgnoreCase("current")){
			for(DepartmentwiseExpenditureResult entry : deptReport.getCurrentyearDepartmentList()){
				conGivenTotal=conGivenTotal.add(entry.getConcurrenceGiven()!=null?entry.getConcurrenceGiven():BigDecimal.ZERO);
				for (Entry<String, BigDecimal> row : entry.getDayAmountMap().entrySet()) {
					if(LOGGER.isDebugEnabled())     LOGGER.debug(" row.getKey() "+row.getKey());
						//dayTotalMap.put(row.getKey(),BigDecimal.ZERO);
					
					if(dayTotalMap.containsKey(row.getKey())){
						dayConTotal=dayTotalMap.get(row.getKey()).add(row.getValue());
						dayTotalMap.put(row.getKey(),dayConTotal);
					}
						//.put(row.getKey(),dayConTotal.add(dayTotalMap.get(row.getKey())));
					else
						dayTotalMap.put(row.getKey(),row.getValue());
				}
				conGivenTillDateTotal=conGivenTillDateTotal.add(entry.getTotalConcurrenceGivenTillDate()!=null?entry.getTotalConcurrenceGivenTillDate():BigDecimal.ZERO);
			  }	
		}else if(deptReport.getPeriod().equalsIgnoreCase("previous")){
			for(DepartmentwiseExpenditureResult entry : deptReport.getPreviousyearDepartmentList()){
				conGivenTotal=conGivenTotal.add(entry.getConcurrenceGiven()!=null?entry.getConcurrenceGiven():BigDecimal.ZERO);
				for (Entry<String, BigDecimal> row : entry.getDayAmountMap().entrySet()) {
					if(LOGGER.isDebugEnabled())     LOGGER.debug(" row.getKey() "+row.getKey());
						//dayTotalMap.put(row.getKey(),BigDecimal.ZERO);
					
					if(dayTotalMap.containsKey(row.getKey())){
						dayConTotal=dayTotalMap.get(row.getKey()).add(row.getValue());
						dayTotalMap.put(row.getKey(),dayConTotal);
					}
						//.put(row.getKey(),dayConTotal.add(dayTotalMap.get(row.getKey())));
					else
						dayTotalMap.put(row.getKey(),row.getValue());
				}
				conGivenTillDateTotal=conGivenTillDateTotal.add(entry.getTotalConcurrenceGivenTillDate()!=null?entry.getTotalConcurrenceGivenTillDate():BigDecimal.ZERO);
			  }	
		}
			  deptExpResult.setConcurrenceGiven(conGivenTotal);
			  deptExpResult.setDayAmountMap(dayTotalMap);
			  deptExpResult.setTotalConcurrenceGivenTillDate(conGivenTillDateTotal);
			  deptExpResult.setDepartmentNm("Total");
			//  deptExpResult.setSlNo(null);
		if(deptReport.getPeriod().equalsIgnoreCase("current")){
			deptReport.getCurrentyearDepartmentList().add(deptExpResult);
		}else if(deptReport.getPeriod().equalsIgnoreCase("previous")){
			deptReport.getPreviousyearDepartmentList().add(deptExpResult);
		}
	}   

	public void populateDaywiseTotal() {
		if(LOGGER.isInfoEnabled())     LOGGER.info("------------------ Inside populateDaywiseTotal--------------");
		
		DepartmentwiseExpenditureResult deptExpResult=new DepartmentwiseExpenditureResult();
		departmentwiseExpList=new ArrayList<DepartmentwiseExpenditureReport>();
		Query query =deService.getConcurrenceDaywiseTotalQuery(deptReport);
		query.setResultTransformer(Transformers.aliasToBean(DepartmentwiseExpenditureReport.class));
		departmentwiseExpList.addAll(query.list());

		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Putting concurrene given values to the depratment map  ");
		
		//adding total to the set
	
		
		for(DepartmentwiseExpenditureReport obj:departmentwiseExpList){  
			deptExpResult=new DepartmentwiseExpenditureResult();
			if(deptReport.getPeriod().equalsIgnoreCase("current")){
				if(deptReport.containsDepartmentInResultList(obj.getDepartmentName(),deptReport.getCurrentyearDepartmentList())){
					for(DepartmentwiseExpenditureResult entry : deptReport.getCurrentyearDepartmentList()){
						if (obj.getDepartmentName().equalsIgnoreCase(entry.getDepartmentNm())){
							//	if(LOGGER.isInfoEnabled())     LOGGER.info("@&& Total for -----"+obj.getDepartmentName());
								entry.getDayAmountMap().put("Total", divideAndRound(obj.getConcurrenceAmount(),BigDecimal.valueOf(100000)));
								break;
						}
					}
				}	deptReport.getConcurrenceDateSet().add("Total");
			}else if(deptReport.getPeriod().equalsIgnoreCase("previous")){
				if(deptReport.containsDepartmentInResultList(obj.getDepartmentName(),deptReport.getPreviousyearDepartmentList())){
					for(DepartmentwiseExpenditureResult entry : deptReport.getPreviousyearDepartmentList()){
						if (obj.getDepartmentName().equalsIgnoreCase(entry.getDepartmentNm())){
							//	if(LOGGER.isInfoEnabled())     LOGGER.info("@&& Total for -----"+obj.getDepartmentName());
								entry.getDayAmountMap().put("Total", divideAndRound(obj.getConcurrenceAmount(),BigDecimal.valueOf(100000)));
								break;
						}
					}
				}	deptReport.getPreviousConcurrenceDateSet().add("Total");
			}
		}
	}
	
@Action(value="/report/departmentwiseExpenditureReport-newForm")
	public String newForm(){
		return NEW;
	}
	
	protected void setRelatedEntitesOn() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("------------------ Inside setRelatedEntites API @setting header values --------------");
		
		heading.append(" Department wise Expenditure Report for the Period from ");
		Preheading.append(" Department wise Expenditure Report for the Period from ");
		if(deptReport.getFinancialYearId() != null && !( deptReport.getFinancialYearId().toString().equals("0")||deptReport.getFinancialYearId().equals(" ") || deptReport.getFinancialYearId()==0)){
			deptReport.setFinyearObj((CFinancialYear)getPersistenceService().find(" from CFinancialYear where id=?", deptReport.getFinancialYearId()));
		}else{
			deptReport.setFinyearObj(deService.getFinancialYearDAO().getFinancialYearByDate(deptReport.getFromDate()));
		}
		if(deptReport.getFundId()!=null  && deptReport.getFundId() !=0 ){
			deptReport.setFund((Fund)getPersistenceService().find("from Fund where id=?", deptReport.getFundId()));
		}
		if(deptReport.getReportType()!=null && deptReport.getReportType().equals("Month")){
			deptReport.setFromDate(deService.getStartDayOfMonth(deptReport));
			deptReport.setToDate(deService.getLastDayOfMonth(deptReport));
			heading.append(deService.getFormattedDate(deptReport.getFromDate()) +" To "+deService.getFormattedDate( deptReport.getToDate()));
			Preheading.append(deService.getFormattedDate(deService.getPreviousYearFor(deptReport.getFromDate())) +" To "+deService.getFormattedDate(deService.getPreviousYearFor( deptReport.getToDate())));
		}else{
			heading.append(deService.getFormattedDate(deptReport.getFromDate()) +" To "+deService.getFormattedDate( deptReport.getToDate()));
			Preheading.append(deService.getFormattedDate(deService.getPreviousYearFor(deptReport.getFromDate())) +" To "+deService.getFormattedDate(deService.getPreviousYearFor( deptReport.getToDate())));
		}
		
		deptReport.setPreviousYearConcurrenceGivenTillDate(deService.getPreviousYearFor(deptReport.getToDate()));
		deptReport.setPreviousYearConcurrenceGivenUptoDate(deService.getPreviousDateFor(deService.getPreviousYearFor(deptReport.getFromDate())));
		deptReport.setCurrentYearConcurrenceGivenUptoDate(deService.getPreviousDateFor(deptReport.getFromDate()));
		
	}
	
	public String generateReportXls() throws Exception{
		//populateReAppropriationData();
		//prepareFormattedList();
		String title=getUlbName()+"\\n"+heading.toString();
		//String Preheading="Amount in Rupess";
		JasperPrint jasper = reportHelper.generateDepartmentwiseExpenditureJasperPrint(deptReport,title,Preheading.toString());
		inputStream = reportHelper.exportXls(inputStream, jasper);
		return "XLS";
	}   
	
	public String generateReportPdf() throws Exception{
		//populateReAppropriationData();
		//prepareFormattedList();
		String title=getUlbName()+"\\n"+heading.toString();
		JasperPrint jasper = reportHelper.generateDepartmentwiseExpenditureJasperPrint(deptReport,title,Preheading.toString());
		inputStream = reportHelper.exportPdf(inputStream, jasper);
		return "PDF";
	}   
	

	public String generateReportHTML() throws Exception{
		String title=getUlbName()+"\\n"+heading.toString();
		JasperPrint jasper = reportHelper.generateDepartmentwiseExpenditureJasperPrint(deptReport,title,Preheading.toString());
		inputStream = reportHelper.exportHtml(inputStream, jasper);
				//.exportPdf(inputStream, jasper);
		return "HTML";
	}   
	
	@SuppressWarnings("unchecked")
	public String getUlbName() {
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
		List<String> result = query.list();
		if (result != null)
			return result.get(0);
		return "";
	}
	public BigDecimal divideAndRound(BigDecimal value, BigDecimal divisor) {
		value = value.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
		return value;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public ReportHelper getReportHelper() {
		return reportHelper;
	}
	public DepartmentwiseExpenditureReport getDeptReport() {
		return deptReport;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}
	public void setDeptReport(DepartmentwiseExpenditureReport deptReport) {
		this.deptReport = deptReport;
	}

	public DEReportService getDepartmentExpenditureService() {
		return deService;
	}

	public void setDepartmentExpenditureService(
			DEReportService departmentExpenditureService) {
		this.deService = departmentExpenditureService;
	}

	public StringBuffer getHeading() {
		return heading;
	}

	

	public void setHeading(StringBuffer heading) {
		this.heading = heading;
	}

	


}
