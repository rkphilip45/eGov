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

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportRequest.ReportDataSourceType;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.util.ReportUtil;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.web.actions.BaseFormAction;

/**
 * Action class for the cash collection report
 */
@ParentPackage("egov")
public class CashCollectionReportAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;

	private final Map<String, Object> critParams = new HashMap<String, Object>();
	private ReportService reportService;
	private CollectionsUtil collectionsUtil;
	private Integer reportId = -1;

	public static final String REPORT = "report";
	private static final String EGOV_COUNTER_OPERATOR_ID = "EGOV_COUNTER_OPERATOR_ID";
	private static final String EGOV_COUNTER_ID = "EGOV_COUNTER_ID";
	private static final String EGOV_FROM_DATE = "EGOV_FROM_DATE";
	private static final String EGOV_TO_DATE = "EGOV_TO_DATE";
	private static final String EGOV_INSTRUMENT_TYPE = "EGOV_INSTRUMENT_TYPE";
	private static final String EGOV_INSTRUMENT_STATUS = "EGOV_INSTRUMENT_STATUS";
	private static final String EGOV_BOUNDARY_ID = "EGOV_BOUNDARY_ID";
	private static final String EGOV_RECEIPT_IDS = "EGOV_RECEIPT_IDS";
	private static final String CASH_COLLECTION_TEMPLATE = "cash_collection";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ModelDriven#getModel()
	 */
	public Object getModel() {
		return null;
	}

	/**
	 * @param instrumentStatus
	 *            the Instrument Status to set
	 */
	public void setInstrumentStatus(String instrumentStatus) {
		critParams.put(EGOV_INSTRUMENT_STATUS, instrumentStatus);
	}

	/**
	 * @param fromDate
	 *            the from date to set
	 */
	public void setFromDate(Date fromDate) {
		critParams.put(EGOV_FROM_DATE, fromDate);
	}

	/**
	 * @param toDate
	 *            the to date to set
	 */
	public void setToDate(Date toDate) {
		critParams.put(EGOV_TO_DATE, toDate);
	}

	/**
	 * @param counterId
	 *            the counter id to set
	 */
	public void setCounterId(Long counterId) {
		critParams.put(EGOV_COUNTER_ID, counterId);
	}

	/**
	 * @param userId
	 *            the user id to set
	 */
	public void setUserId(Long userId) {
		critParams.put(EGOV_COUNTER_OPERATOR_ID, userId);
	}

	/**
	 * 
	 * @param boundaryId
	 */
	public void setBoundaryId(Long boundaryId) {
		critParams.put(EGOV_BOUNDARY_ID, boundaryId);
	}

	/**
	 * @return the instrument status
	 */
	public String getInstrumentStatus() {
		return (String) critParams.get(EGOV_INSTRUMENT_STATUS);
	}

	/**
	 * @return the from date
	 */
	public Date getFromDate() {
		return (Date) critParams.get(EGOV_FROM_DATE);
	}

	/**
	 * @return the do date
	 */
	public Date getToDate() {
		return (Date) critParams.get(EGOV_TO_DATE);
	}

	/**
	 * @return the counter id
	 */
	public Long getCounterId() {
		return (Long) critParams.get(EGOV_COUNTER_ID);
	}

	/**
	 * @return the user id
	 */
	public Long getUserId() {
		return (Long) critParams.get(EGOV_COUNTER_OPERATOR_ID);
	}

	/**
	 * 
	 * @return the boundary id
	 */
	public Long getBoundaryId() {
		return (Long) critParams.get(EGOV_BOUNDARY_ID);
	}

	/**
	 * @return the reportId
	 */
	public Integer getReportId() {
		return reportId;
	}

	/**
	 * Initializes the report criteria map with default values
	 */
	private void initCriteriaMap() {
		critParams.clear();
		critParams.put(EGOV_COUNTER_OPERATOR_ID, Long.valueOf(-1L));
		critParams.put(EGOV_COUNTER_ID, Long.valueOf(-1L));
		critParams.put(EGOV_FROM_DATE, new Date());
		critParams.put(EGOV_TO_DATE, new Date());
		critParams.put(EGOV_INSTRUMENT_TYPE,
				CollectionConstants.INSTRUMENTTYPE_CASH);
		critParams.put(EGOV_INSTRUMENT_STATUS, null);
		critParams.put(EGOV_BOUNDARY_ID, Long.valueOf(-1L));
		critParams.put(EGOV_RECEIPT_IDS, null);
	}

	/**
	 * Initializes the drop down data
	 */
	private void initDropDowns() {
		setupDropdownDataExcluding();

		addDropdownData(CollectionConstants.DROPDOWN_DATA_COUNTER_LIST,
				collectionsUtil.getAllCounters());
		addDropdownData(CollectionConstants.DROPDOWN_DATA_RECEIPT_CREATOR_LIST,
				collectionsUtil.getReceiptCreators());
		addDropdownData(CollectionConstants.DROPDOWN_DATA_RECEIPTZONE_LIST,
				collectionsUtil.getReceiptZoneList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.web.actions.BaseFormAction#prepare()
	 */
	public void prepare() {
		super.prepare();
		initDropDowns();
		initCriteriaMap();
	}

	/**
	 * Action method to create the cash submission report
	 * 
	 * @return report
	 */
	public String submissionReport() {
		Map<String, Object> session = getSession();
		User user = collectionsUtil.getLoggedInUser(getSession());

		Date today = ReportUtil.today();
		critParams.put(EGOV_FROM_DATE, today);
		critParams.put(EGOV_TO_DATE, today);

		critParams.put(EGOV_COUNTER_OPERATOR_ID, user.getId().longValue());
		critParams.put(EGOV_COUNTER_ID, collectionsUtil.getLocationOfUser(
				getSession()).getId().longValue());
		critParams.put(EGOV_RECEIPT_IDS, Arrays.asList((Long[]) session
				.get(CollectionConstants.SESSION_VAR_RECEIPT_IDS)));

		return report();
	}

	/**
	 * Action method that creates the report
	 * 
	 * @return report
	 */
	public String report() {
		ReportRequest reportInput = new ReportRequest(CASH_COLLECTION_TEMPLATE,
				critParams, ReportDataSourceType.SQL);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportOutput,
				getSession());
		return REPORT;
	}

	/**
	 * Action method for criteria screen
	 * 
	 * @return index
	 */
	public String criteria() {
		return INDEX;
	}

	/**
	 * @param reportService
	 *            the reportService to set
	 */
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * @param collectionsUtil
	 *            the Collections Utility object to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}
}