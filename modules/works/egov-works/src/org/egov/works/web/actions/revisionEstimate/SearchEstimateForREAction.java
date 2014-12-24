/**
 * 
 */
package org.egov.works.web.actions.revisionEstimate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.WorkType;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.web.actions.estimate.AjaxEstimateAction;

/**
 *
 */
@ParentPackage("egov")
public class SearchEstimateForREAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private AbstractEstimate estimates = new AbstractEstimate();
	private WorkOrder workOrder= new WorkOrder();
	private String searchType="searchType";
	private Date fromDate;
	private Date toDate;
	private PersonalInformationService personalInformationService;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private WorkOrderService workOrderService;
	private Integer deptId;
	private WorksService worksService;
	public final static String APPROVED="APPROVED";
	private String estimateNumber;
	private Long typeId;
	private String workOrderNumber;
	private Long parentCategory;
	private Long category;
	private Long contractorId;
	
	public SearchEstimateForREAction(){
		
		addRelatedEntity("category", EgwTypeOfWork.class);
		addRelatedEntity("parentCategory", EgwTypeOfWork.class);
		addRelatedEntity("executingDepartment", DepartmentImpl.class);
		addRelatedEntity("type", WorkType.class);
		
	}

	@Override
	public Object getModel() {
	
		return estimates;
	}
	@Override
	public void prepare() {
		
		super.prepare();
		AjaxEstimateAction ajaxEstimateAction = new AjaxEstimateAction();
		ajaxEstimateAction.setPersistenceService(getPersistenceService());
		ajaxEstimateAction.setPersonalInformationService(personalInformationService);
		addDropdownData("executingDepartmentList", persistenceService.findAllBy("from DepartmentImpl dt"));
		addDropdownData("typeList", persistenceService.findAllBy("from WorkType dt"));
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null")); 
		addDropdownData("categoryList", Collections.emptyList());
		populateCategoryList(ajaxEstimateAction, estimates.getParentCategory() != null);
	}
	
	@ValidationErrorPage(value="searchWO")
	public String searchWorkOrder(){
		
		return "searchWO";
	}
	private Map getQuery(){
		StringBuffer query = new StringBuffer(700);
		List<Object> paramList = new ArrayList<Object>();
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
			query.append("from WorkOrderEstimate woe where woe.workOrder.id is not null and woe.workOrder.parent is null and woe.workOrder.egwStatus.code<>? " +
					"and woe.workOrder.egwStatus.code = ? and woe.estimate.parent is null");
			paramList.add("NEW");
			paramList.add("APPROVED");
		if(getDeptId()!=null && getDeptId()!= -1){		
			query.append(" and woe.estimate.executingDepartment.id=? ");
			paramList.add(getDeptId());
		}			
		if(getTypeId()!= -1){
			query.append(" and woe.estimate.type.id=? ");
			paramList.add(Long.valueOf(getTypeId()));
		}
		if(StringUtils.isNotBlank(getEstimateNumber())){
			query.append(" and UPPER(woe.estimate.estimateNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(getEstimateNumber()).toUpperCase());
		}

		if(StringUtils.isNotBlank(getWorkOrderNumber())){
			query.append(" and UPPER(woe.workOrder.workOrderNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(getWorkOrderNumber()).toUpperCase());
		}

		if(estimates.getCategory()!=null){
			query.append(" and woe.estimate.category.id=?");
			paramList.add(estimates.getCategory().getId());
		}
		if(estimates.getParentCategory()!=null){
			query.append(" and woe.estimate.parentCategory.id=?");
			paramList.add(estimates.getParentCategory().getId());
		}
		
		if(getContractorId()!= -1){
			query.append(" and woe.workOrder.contractor.id=? ");
			paramList.add(Long.valueOf(getContractorId()));
		}
		
		if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			query.append(" and woe.estimate.estimateDate between ? and ? ");
			paramList.add(fromDate);
			paramList.add(toDate);
		}
		query.append("and woe.id not in (select distinct mbh.workOrderEstimate.id from MBHeader mbh left join mbh.mbBills mbBills where" +
				" mbh.egwStatus.code = ? and (mbBills.egBillregister.billstatus <> ? and mbBills.egBillregister.billtype = ?) and" +
				" mbh.workOrderEstimate.workOrder.egwStatus.code='APPROVED' and mbh.workOrderEstimate.estimate.egwStatus.code=?) and" +
				" woe.workOrder.id not in (select wo1.parent.id from WorkOrder wo1 where wo1.parent is not null and wo1.egwStatus.code not in ('APPROVED','CANCELLED')) " +
				"and woe.estimate.id not in " +
				"(select ae.parent.id from AbstractEstimate ae where ae.parent is not null and ae.egwStatus.code not in ('APPROVED','CANCELLED'))");
		paramList.add(MBHeader.MeasurementBookStatus.APPROVED.toString());		
		paramList.add(MBHeader.MeasurementBookStatus.CANCELLED.toString());	
		paramList.add(getFinalBillTypeConfigValue());
		paramList.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
		queryAndParams.put("query", query.toString());
		queryAndParams.put("params", paramList);
		
		return queryAndParams;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		String query =null;
		String countQuery = null;
		Map queryAndParms=null;
		List<Object> paramList = new ArrayList<Object>();
			queryAndParms=getQuery();
			paramList=(List<Object>)queryAndParms.get("params");
			query=(String) queryAndParms.get("query");
			countQuery="select count(distinct woe.id) " + query;
			query="select distinct woe "+query;
			return new SearchQueryHQL(query, countQuery, paramList);
	    }

	@Override
	public String search() {
		boolean isError=false;
		if(fromDate!=null && toDate==null){
			addFieldError("enddate",getText("search.endDate.null"));
			isError=true;
		}
		if(toDate!=null && fromDate==null){
			addFieldError("startdate",getText("search.startDate.null"));		
			isError=true;
		}
		
		if(!DateUtils.compareDates(getToDate(),getFromDate())){
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
			isError=true;
		}
		
		if(isError){
			return "searchWO";
		}

		setPageSize(WorksConstants.PAGE_SIZE);
		super.search();
		List<WorkOrderEstimate> woEstimateList=searchResult.getList();
		for(WorkOrderEstimate woEstimate:woEstimateList){
			woEstimate.getWorkOrder().setTotalWorkOrderAmount(workOrderService.getRevisionEstimateWOAmount(woEstimate.getWorkOrder()));
		}		     
		return "searchWO";
	}
	
	protected void populateCategoryList(
			AjaxEstimateAction ajaxEstimateAction, boolean categoryPopulated) {
		if (categoryPopulated) {
			ajaxEstimateAction.setCategory(estimates.getParentCategory().getId());
			ajaxEstimateAction.subcategories();
			addDropdownData("categoryList", ajaxEstimateAction.getSubCategories());		
		}
		else {
			addDropdownData("categoryList", Collections.emptyList());
		}
	}
	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new LinkedHashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){
				contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsWithWOList; 
	}
	
	public String getFinalBillTypeConfigValue() {		
		return worksService.getWorksConfigValue("FinalBillType");
	}
	
	public String getApprovedValue() {
		return worksService.getWorksConfigValue("WORKS_PACKAGE_STATUS");
	}
	public AbstractEstimate getEstimates() {
		return estimates;
	}

	public void setEstimates(AbstractEstimate estimates) {
		this.estimates = estimates;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public Long getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Long parentCategory) {
		this.parentCategory = parentCategory;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

}