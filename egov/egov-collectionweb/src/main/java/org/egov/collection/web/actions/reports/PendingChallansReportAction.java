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
package org.egov.collection.web.actions.reports;

import java.util.Date;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.web.actions.ReportFormAction;

/**
 * Action class for pending challans report action
 */
public class PendingChallansReportAction extends ReportFormAction {
	private static final long serialVersionUID = 1L;
	// Report parameter names
	private static final String EGOV_DEPT_ID = "EGOV_DEPT_ID";
	private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
	private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
	private static final String EGOV_CHALLAN_SERVICE_ID = "EGOV_CHALLAN_SERVICE_ID";

	private CollectionsUtil collectionsUtil;

	@Override
	public void prepare() {
		super.prepare();

		setReportFormat(FileFormat.PDF);
		setDataSourceType(ReportDataSourceType.HQL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.erpcollection.web.actions.reports.ReportFormAction#criteria()
	 */
	@Override
	public String criteria() {
		// Setup drop down data for department list
		addRelatedEntity("department", Department.class, "deptName");
		setupDropdownDataExcluding();

		// Add dropdown data for challan services (serviceList)
		addDropdownData(CollectionConstants.DROPDOWN_DATA_SERVICE_LIST,
				collectionsUtil.getChallanServiceList());

		// Set default values of criteria fields
		Department dept = collectionsUtil
				.getDepartmentOfLoggedInUser(getSession());
		if (dept != null) {
			setReportParam(EGOV_DEPT_ID, dept.getId());
		}
		
		// default value for from/to date = today
		setReportParam(EGOV_FROM_DATE, new Date());
		setReportParam(EGOV_TO_DATE, new Date());

		return INDEX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.erpcollection.web.actions.reports.ReportFormAction#
	 * getReportTemplateName()
	 */
	@Override
	protected String getReportTemplateName() {
		return CollectionConstants.REPORT_TEMPLATE_PENDING_CHALLANS;
	}

	/**
	 * @param collectionsUtil
	 *            The collections util object
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	/**
	 * @return the department id
	 */
	public Integer getDeptId() {
		return (Integer) getReportParam(EGOV_DEPT_ID);
	}

	/**
	 * @param deptId
	 *            the department id to set
	 */
	public void setDeptId(Integer deptId) {
		setReportParam(EGOV_DEPT_ID, deptId);
	}

	/**
	 * @return the from date
	 */
	public Date getFromDate() {
		return (Date) getReportParam(EGOV_FROM_DATE);
	}

	/**
	 * @param fromDate
	 *            the from date to set
	 */
	public void setFromDate(Date fromDate) {
		setReportParam(EGOV_FROM_DATE, fromDate);
	}

	/**
	 * @return the do date
	 */
	public Date getToDate() {
		return (Date) getReportParam(EGOV_TO_DATE);
	}

	/**
	 * @param toDate
	 *            the to date to set
	 */
	public void setToDate(Date toDate) {
		setReportParam(EGOV_TO_DATE, toDate);
	}

	/**
	 * @return The challan service id
	 */
	public Long getChallanServiceId() {
		return (Long) getReportParam(EGOV_CHALLAN_SERVICE_ID);
	}

	/**
	 * @param challanServiceId
	 *            The challan service id to set
	 */
	public void setChallanServiceId(Long challanServiceId) {
		setReportParam(EGOV_CHALLAN_SERVICE_ID, challanServiceId);
	}
}
