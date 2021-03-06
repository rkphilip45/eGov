package org.egov.web.actions.payment;

import org.apache.struts2.convention.annotation.Action;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.SearchFormAction;
import org.hibernate.FlushMode;

public class SearchAdvanceRequisitionForPaymentAction extends SearchFormAction{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SearchAdvanceRequisitionForPaymentAction.class);
	public static final String ARF_STATUS_APPROVED="APPROVED";
	public static final String ARF_TYPE="Contractor";
	private Date fromDate;
	private Date toDate;
	private Integer departmentId = -1; 
	private String arfNumber;
	private VoucherHelper voucherHelper;
	private VoucherService voucherService;
	
	@Override
	public Object getModel() {	
		return null;
	}
	
	@Override
	public void prepare() {
		super.prepare(); 
		addDropdownData("departmentList", voucherHelper.getAllAssgnDeptforUser());
		 if(departmentId == null || departmentId == -1 ){
			 departmentId = voucherService.getCurrentDepartment().getId().intValue();
			}
	}
	
@Action(value="/payment/searchAdvanceRequisitionForPayment-beforeSearch")
	public String beforeSearch() {		
		return "search";
	}
		
	private Map getQuery(){
		StringBuffer query = new StringBuffer(700); 
		List<Object> paramList = new ArrayList<Object>();
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
		query.append("from EgAdvanceRequisition arf where arf.arftype = ? and arf.status.code = ? and " +
				" NOT EXISTS (select 1 from CVoucherHeader vh where vh.id=arf.egAdvanceReqMises.voucherheader.id and arf.egAdvanceReqMises.voucherheader.status<>4) ");
		paramList.add(ARF_TYPE);
		paramList.add(ARF_STATUS_APPROVED);
				
		if(StringUtils.isNotBlank(arfNumber)){
			query.append(" and UPPER(arf.advanceRequisitionNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(arfNumber).toUpperCase());
		}
		
		if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			query.append(" and arf.advanceRequisitionDate between ? and ? ");
			paramList.add(fromDate);
			paramList.add(toDate);
		}		
	
		if(departmentId !=0 && departmentId!= -1){		
			query.append(" and arf.egAdvanceReqMises.egDepartment.id = ? ");
			paramList.add(departmentId);
		}	
		//TODO - Order by Department and advanceRequisitionDate
		query.append(" order by arf.advanceRequisitionDate");
		
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
			countQuery="select count(distinct arf.id) " + query;
			query="select distinct arf "+query;
			return new SearchQueryHQL(query, countQuery, paramList);
	}
	
	public String search() 	{
		return "search";
	}
	
	public String searchList() {
	HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
	HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
		boolean isError=false;
		if(fromDate!=null && toDate==null){
			addFieldError("toDate",getText("search.toDate.null"));
			isError=true;
		}
		if(toDate!=null && fromDate==null){
			addFieldError("fromDate",getText("search.fromDate.null"));		
			isError=true;
		}
		
		if(!DateUtils.compareDates(toDate,fromDate)){
			addFieldError("toDate",getText("fromDate.greaterthan.toDate"));
			isError=true;
		}
		
		if(!DateUtils.compareDates(new Date(),toDate)){
			addFieldError("toDate",getText("toDate.greaterthan.currentdate"));
			isError=true;
		}
		
		if(isError){
			return "search";
		}

		setPageSize(30);
		super.search();
		
		return "search";
	}


	public String getArfNumber() {
		return arfNumber;
	}

	public void setArfNumber(String arfNumber) {
		this.arfNumber = arfNumber;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public void setVoucherHelper(VoucherHelper voucherHelper) {
		this.voucherHelper = voucherHelper;
	}

	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}

}
