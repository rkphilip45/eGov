/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pims.web.actions.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.EisUtilService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;

/**
 * 
 * @author Vaibhav.K
 * 
 */

@SuppressWarnings("serial")
@ParentPackage("egov")
public class EmployeeSearchAction extends SearchFormAction {

	private Integer designationId;
	private Integer deptId;
	private String empCode;
	private String empName;
	private Date fromDate;
	private Date toDate;
	private Integer reportBy;

	private String mode;
	private EmployeeService eisMgr;
	private EisUtilService eisService;

	private String countQuery = "";
	private SearchQuery searchquery;
	private PaginatedList paginatedList;

	private static final String REVERSION = "reversion";
	private static final String REVERSIONREPORT = "reversionReport";

	private static final String DEMOTION = "demotion";
	
	 
	@SkipValidation
	public String load() {
		return NEW;
	}

	@ValidationErrorPage(NEW)
	public String search() {
		super.search();
		setPaginatedList(searchResult);
		return NEW;
	}

	public void prepare() {
		super.prepare();
		addDropdownData("designationlist", (ArrayList) EgovMasterDataCaching
				.getInstance().get("egEmp-DesignationMaster"));
		addDropdownData("departmentlist", (ArrayList) EgovMasterDataCaching
				.getInstance().get("egi-department"));
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		
		List<Object> params=new ArrayList<Object>();
		
		if(mode.equalsIgnoreCase(REVERSION) || mode.equalsIgnoreCase(DEMOTION))
		{
			setQueryAndParamsForEmpSearch(params);
		}

		if (mode.equalsIgnoreCase(REVERSIONREPORT)) {
			setQueryAndParamsForReversionReport(params);
		}
		return searchquery;
	}

	// For reversion process
	private void setQueryAndParamsForEmpSearch(List<Object> params) {
		params.add(new Date());
		params.add(new Date());
		params.add(new Date());
		if (null != deptId && deptId != 0) {
			params.add(deptId);
		}
		if (null != designationId && designationId != 0) {
			params.add(designationId);
		}
		if (null != empCode && !("").equals(empCode)) {
			params.add(empCode.trim());
		}

		countQuery = "select count(*) " + getSearchQueryForEmp();
		searchquery = new SearchQueryHQL(getSearchQueryForEmp(), countQuery,
				params);
	}

	// For reversion process
	private String getSearchQueryForEmp() {
		StringBuffer query = new StringBuffer();
		query.append("from EmployeeView view where ((view.fromDate<=? and view.toDate>=?) or (view.fromDate<=? and view.toDate is null)) and view.isPrimary='Y' and view.isActive=1 ");
		if (null != deptId && deptId != 0) {
			query.append(" and view.deptId.id=? ");
		}
		if (null != designationId && designationId != 0) {
			query.append(" and view.desigId.designationId=? ");
		}
		if (null != empCode && !("").equals(empCode)) {
			query.append(" and view.employeeCode like ? ");
		}

		return query.toString();

	}

	private void setQueryAndParamsForReversionReport(List<Object> params) {
		
		if (deptId != null) {
			params.add(deptId);
			params.add(fromDate);
			params.add(toDate);
		} else {
			params.add(fromDate);
			params.add(toDate);
		}
		countQuery = getcountEmpForRevisionRequest(deptId);
		searchquery = new SearchQuerySQL(getEmpForRevisionRequest(deptId),
				countQuery, params);
	}

	public String getcountEmpForRevisionRequest(Integer deptId) {
		String countqry = " SELECT COUNT(*) FROM ";
		countqry += getEmpForRevisionRequest(deptId);
		return countqry;
	}

	public String getEmpForRevisionRequest(Integer deptId) {
		String qry = null;

		if (deptId != null) {
			qry = " (SELECT DISTINCT(ev.CODE),ev.NAME,dept.DEPT_NAME,status.DESCRIPTION FROM EG_EIS_EMPLOYEEINFO ev,EGEIS_REVERSION rev,"
					+ "EG_DEPARTMENT dept, EGW_STATUS status WHERE ev.ID=rev.ID_EMPLOYEE AND rev.DEPT_ID=dept.ID_DEPT AND " 
					+ "status.MODULETYPE='Reversion' AND "
					+ "rev.STATUS=status.ID AND rev.DEPT_ID = ? AND rev.REVERSION_EFF_FROM >= ? AND "
					+ "rev.REVERSION_EFF_FROM <= ? )";
		}

		else {
			qry = " (SELECT DISTINCT(ev.CODE),ev.NAME,dept.DEPT_NAME,status.DESCRIPTION FROM EG_EIS_EMPLOYEEINFO ev,EGEIS_REVERSION rev,"
					+ "EG_DEPARTMENT dept, EGW_STATUS status WHERE ev.ID=rev.ID_EMPLOYEE AND rev.DEPT_ID=dept.ID_DEPT AND " 
					+ "status.MODULETYPE='Reversion' AND "
					+ "rev.STATUS=status.ID AND rev.REVERSION_EFF_FROM >= ? AND "
					+ "rev.REVERSION_EFF_FROM <= ? )";
		}

		return qry;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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

	public Integer getReportBy() {
		return reportBy;
	}

	public void setReportBy(Integer reportBy) {
		this.reportBy = reportBy;
	}

	public EmployeeService getEisMgr() {
		return eisMgr;
	}

	public void setEisMgr(EmployeeService eisMgr) {
		this.eisMgr = eisMgr;
	}

	public EisUtilService getEisService() {
		return eisService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public PaginatedList getPaginatedList() {
		return paginatedList;
	}

	public void setPaginatedList(PaginatedList paginatedList) {
		this.paginatedList = paginatedList;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
