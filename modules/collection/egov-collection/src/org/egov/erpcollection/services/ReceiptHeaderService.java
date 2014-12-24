package org.egov.erpcollection.services;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.erpcollection.models.AccountPayeeDetail;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptMisc;
import org.egov.erpcollection.models.ReceiptVoucher;
import org.egov.erpcollection.util.CollectionsNumberGenerator;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.util.FinancialsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.user.User;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.pims.commons.Position;
import org.hibernate.Query;

/**
 * Provides services related to receipt header
 */
public class ReceiptHeaderService extends PersistenceService<ReceiptHeader, Long> {

	private static final Logger LOGGER = Logger.getLogger(ReceiptHeaderService.class);
	private CollectionsUtil collectionsUtil;
	private CollectionsNumberGenerator collectionsNumberGenerator;
	private FinancialsUtil financialsUtil;
	private WorkflowService<ContraJournalVoucher> contraWorkflowService;
	private PersistenceService persistenceService;

	private CommonsService commonsService;

	/**
	 * @param contraWorkflowService
	 *            the contraWorkflowService to set
	 */
	public void setContraWorkflowService(WorkflowService<ContraJournalVoucher> contraWorkflowService) {
		this.contraWorkflowService = contraWorkflowService;
	}

	/**
	 * @param financialsUtil
	 *            the financialsUtil to set
	 */
	public void setFinancialsUtil(FinancialsUtil financialsUtil) {
		this.financialsUtil = financialsUtil;
	}

	public void setCollectionsNumberGenerator(CollectionsNumberGenerator collectionsNumberGenerator) {
		this.collectionsNumberGenerator = collectionsNumberGenerator;
	}

	/**
	 * @return the Collections Util object
	 */
	public CollectionsUtil getCollectionsUtil() {
		return collectionsUtil;
	}

	/**
	 * @param collectionsUtil
	 *            the collections Util object to set
	 */
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	/**
	 * @param statusCode
	 *            Status code of receipts to be fetched. If null or ALL, then
	 *            receipts with all statuses are fetched
	 * @param userName
	 *            User name of the user who has created the receipts. If null or
	 *            ALL, then receipts of all users are fetched
	 * @param counterId
	 *            Counter id on which the receipts were created. If negative,
	 *            then receipts from all counters are fetched
	 * @param serviceCode
	 *            Service code for which the receipts were created. If null or
	 *            ALL, then receipts of all billing services are fetched
	 * @return List of all receipts created by given user from given counter id
	 *         and having given status
	 */
	public List<ReceiptHeader> findAllByStatusUserCounterService(String statusCode, String userName, Integer counterId, String serviceCode) {
		StringBuilder query = new StringBuilder("select receipt from org.egov.erpcollection.models.ReceiptHeader receipt where 1=1");

		boolean allStatuses = (statusCode == null || statusCode.equals(CollectionConstants.ALL));
		boolean allCounters = (counterId == null || counterId < 0);
		boolean allUsers = (userName == null || userName.equals(CollectionConstants.ALL));
		boolean allServices = (serviceCode == null || serviceCode.equals(CollectionConstants.ALL));

		int argCount = 0;
		argCount += allStatuses ? 0 : 1;
		argCount += allCounters ? 0 : 1;
		argCount += allUsers ? 0 : 1;
		argCount += allServices ? 0 : 1;
		Object arguments[] = new Object[argCount];

		argCount = 0;
		if (!allStatuses) {
			query.append(" and receipt.status.code = ?");
			arguments[argCount++] = statusCode;
		}

		if (!allUsers) {
			query.append(" and receipt.createdBy.userName = ?");
			arguments[argCount++] = userName;
		}

		if (!allCounters) {
			query.append(" and receipt.location.id = ?");
			arguments[argCount++] = counterId;
		}

		if (!allServices) {
			query.append(" and receipt.service.code = ?");
			arguments[argCount++] = serviceCode;
		}

		query.append(" order by receipt.createdDate desc");

		return findAllBy(query.toString(), arguments);
	}

	/**
	 * This method returns the internal reference numbers corresponding to the
	 * instrument type.
	 * 
	 * @param entity
	 *            an instance of <code>ReceiptHeader</code>
	 * 
	 * @return a <code>List</code> of strings , each representing the internal
	 *         reference numbers for each instrument type for the given receipt
	 */
	public List<String> generateInternalReferenceNo(ReceiptHeader entity) {
		CFinancialYear financialYear = collectionsUtil.getFinancialYearforDate(entity.getCreatedDate());
		CFinancialYear currentFinancialYear = collectionsUtil.getFinancialYearforDate(new Date());

		return collectionsNumberGenerator.generateInternalReferenceNumber(entity, financialYear, currentFinancialYear);
	}

	/**
	 * This method is called for voucher creation into the financial system. For
	 * each receipt created in the collections module, a voucher is created.
	 * 
	 * @param receiptHeader
	 *            Receipt header for which the pre-approval voucher is to be
	 *            created
	 * @param receiptBulkUpload
	 * @return The created voucher
	 * 
	 */
	protected CVoucherHeader createVoucher(ReceiptHeader receiptHeader, Boolean receiptBulkUpload) {
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		List<HashMap<String, Object>> accountCodeList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> subledgerList = new ArrayList<HashMap<String, Object>>();
		String fundCode = null, fundsourceCode = null, departmentCode = null;
		Boolean isVoucherApproved = Boolean.FALSE;

		if (receiptHeader.getService().getIsVoucherApproved() != null) {
			isVoucherApproved = receiptHeader.getService().getIsVoucherApproved();
		}

		ReceiptMisc receiptMisc = receiptHeader.getReceiptMisc();
		if (receiptMisc.getFund() != null) {
			fundCode = receiptMisc.getFund().getCode();
		}
		if (receiptMisc.getFundsource() != null) {
			fundsourceCode = receiptMisc.getFundsource().getCode();
		}
		if (receiptMisc.getDepartment() != null) {
			departmentCode = receiptMisc.getDepartment().getDeptCode();
		}

		for (InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument()) {
			if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)
					|| instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_BANK)) {
				headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
				headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
			} else if ((instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE))
					|| (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_DD))
					|| (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CARD))
					|| (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_ONLINE))) {
				if ((collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.APPCONFIG_VALUE_RECEIPTVOUCHERTYPEFORCHEQUEDDCARD))
						.equals(CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE)) {
					headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERNAME);
					headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE);
				} else {
					headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
					headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
				}

			}
		}
		headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
		if (receiptHeader.getVoucherDate() == null) {
			headerdetails.put(VoucherConstant.VOUCHERDATE, new Date());
		} else {
			headerdetails.put(VoucherConstant.VOUCHERDATE, receiptHeader.getVoucherDate());
		}

		if (receiptHeader.getVoucherNum() != null && !receiptHeader.getVoucherNum().equals("")) {
			headerdetails.put(VoucherConstant.VOUCHERNUMBER, receiptHeader.getVoucherNum());
		}

		headerdetails.put(VoucherConstant.FUNDCODE, fundCode);
		headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCode);
		headerdetails.put(VoucherConstant.FUNDSOURCECODE, fundsourceCode);
		headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);
		headerdetails.put(VoucherConstant.SOURCEPATH, CollectionConstants.RECEIPT_VIEW_SOURCEPATH + receiptHeader.getId());

		Set<ReceiptDetail> receiptDetailSet = new LinkedHashSet<ReceiptDetail>(0);

		/**
		 * Aggregate Amount in case of bill based receipt for account codes
		 * appearing more than once in receipt details
		 */
		if (receiptHeader.getReceipttype() == 'B') {

			receiptDetailSet = aggregateDuplicateReceiptDetailObject(new ArrayList<ReceiptDetail>(receiptHeader.getReceiptDetails()));
		} else {
			receiptDetailSet = receiptHeader.getReceiptDetails();
		}

		for (ReceiptDetail receiptDetail : receiptDetailSet) {
			if (receiptDetail.getCramount().compareTo(BigDecimal.ZERO) != 0 || receiptDetail.getDramount().compareTo(BigDecimal.ZERO) != 0) {

				HashMap<String, Object> accountcodedetailsHashMap = new HashMap<String, Object>();
				accountcodedetailsHashMap.put(VoucherConstant.GLCODE, receiptDetail.getAccounthead().getGlcode());

				accountcodedetailsHashMap.put(VoucherConstant.DEBITAMOUNT, receiptDetail.getDramount().compareTo(BigDecimal.ZERO) == 0 ? 0 : receiptDetail.getDramount());
				accountcodedetailsHashMap.put(VoucherConstant.CREDITAMOUNT, receiptDetail.getCramount().compareTo(BigDecimal.ZERO) == 0 ? 0 : receiptDetail.getCramount());
				if (receiptDetail.getFunction() != null)
					accountcodedetailsHashMap.put(VoucherConstant.FUNCTIONCODE, receiptDetail.getFunction().getCode());
				accountCodeList.add(accountcodedetailsHashMap);

				for (AccountPayeeDetail accpayeeDetail : receiptDetail.getAccountPayeeDetails()) {
					if (accpayeeDetail.getAmount().compareTo(BigDecimal.ZERO) != 0) {

						HashMap<String, Object> subledgerdetailsHashMap = new HashMap<String, Object>();
						subledgerdetailsHashMap.put(VoucherConstant.GLCODE, accpayeeDetail.getReceiptDetail().getAccounthead().getGlcode());
						subledgerdetailsHashMap.put(VoucherConstant.DETAILTYPEID, accpayeeDetail.getAccountDetailType().getId());
						subledgerdetailsHashMap.put(VoucherConstant.DETAILKEYID, accpayeeDetail.getAccountDetailKey().getDetailkey());
						if (accpayeeDetail.getReceiptDetail().getCramount().compareTo(BigDecimal.ZERO) != 0) {
							subledgerdetailsHashMap.put(VoucherConstant.CREDITAMOUNT, accpayeeDetail.getAmount().compareTo(BigDecimal.ZERO) == 0 ? 0 : accpayeeDetail.getAmount());
						} else if (accpayeeDetail.getReceiptDetail().getDramount().compareTo(BigDecimal.ZERO) != 0) {
							subledgerdetailsHashMap.put(VoucherConstant.DEBITAMOUNT, accpayeeDetail.getAmount().compareTo(BigDecimal.ZERO) == 0 ? 0 : accpayeeDetail.getAmount());
						}
						subledgerList.add(subledgerdetailsHashMap);

					}
				}

			}

		}

		return financialsUtil.createVoucher(headerdetails, accountCodeList, subledgerList, receiptBulkUpload, isVoucherApproved);
	}

	/**
	 * Creates voucher for given receipt header and maps it with the same.
	 * 
	 * @param receiptHeader
	 *            Receipt header for which voucher is to be created
	 * @return The created voucher header
	 */
	public CVoucherHeader createVoucherForReceipt(ReceiptHeader receiptHeader, Boolean receiptBulkUpload) throws EGOVRuntimeException {
		CVoucherHeader voucherheader = null;

		// Additional check for challan Based Receipt, if the receipt cancelled
		// before remittance
		// then need to check the instrument status of that receipt in order to
		// create voucher
		// as the challan has a 'PENDING' receipt object associated with it
		boolean isParentReceiptInstrumentDeposited = false;

		if (receiptHeader.getReceiptHeader() != null) {
			for (InstrumentHeader instrumentHeader : receiptHeader.getReceiptHeader().getReceiptInstrument()) {

				if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
					if (instrumentHeader.getStatusId().getDescription().equals(CollectionConstants.INSTRUMENT_RECONCILED_STATUS)) {
						isParentReceiptInstrumentDeposited = true;
						break;
					}
				} else {
					if (instrumentHeader.getStatusId().getDescription().equals(CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)) {
						isParentReceiptInstrumentDeposited = true;
						break;
					}
				}
			}
		}

		if (receiptHeader.getReceiptHeader() == null || (receiptHeader.getReceiptHeader() != null && !isParentReceiptInstrumentDeposited)) {
			String internalRefNo = "";
			List<String> actualinternalRefNo = generateInternalReferenceNo(receiptHeader);
			for (String checkinternalRefNo : actualinternalRefNo) {
				if (checkinternalRefNo.length() > 1) {
					internalRefNo = checkinternalRefNo;
				}
			}

			voucherheader = createVoucher(receiptHeader, receiptBulkUpload);

			if (voucherheader != null) {
				ReceiptVoucher receiptVoucher = new ReceiptVoucher();
				receiptVoucher.setInternalrefno(internalRefNo);
				receiptVoucher.setVoucherheader(voucherheader);
				receiptVoucher.setReceiptHeader(receiptHeader);
				receiptHeader.addReceiptVoucher(receiptVoucher);
			}
		}

		LOGGER.debug("Created voucher for receipt : " + receiptHeader.getReceiptnumber());

		return voucherheader;
	}

	/**
	 * Creates vouchers for given set of receipt headers
	 * 
	 * @param receiptHeaders
	 *            receipt headers for which vouchers are to be created
	 */
	public void createVouchers(Collection<ReceiptHeader> receiptHeaders, Boolean receiptBulkUpload) throws EGOVRuntimeException {
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			createVoucherForReceipt(receiptHeader, receiptBulkUpload);
		}
	}

	/**
	 * Starts workflow for given set of receipt headers. Internally performs the
	 * following: 1. Start workflow 2. Transition workflow state with action
	 * "Create Receipt" 3. Create vouchers (if required) 4. If vouchers created,
	 * transition workflow state with action "Create Voucher"
	 * 
	 * @param receiptHeaders
	 *            set of receipt headers on which workflow is to be started
	 * @param receiptBulkUpload
	 */
	public void startWorkflow(Collection<ReceiptHeader> receiptHeaders, Boolean receiptBulkUpload) throws EGOVRuntimeException {
		SimpleWorkflowService<ReceiptHeader> receiptWorkflowService = new SimpleWorkflowService<ReceiptHeader>(this);
		Boolean createVoucherForBillingService = Boolean.TRUE;
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			createVoucherForBillingService = null == receiptHeader.getService().getVoucherCreation() ? Boolean.FALSE : receiptHeader.getService().getVoucherCreation();
			if (receiptHeader.getState() == null)
				receiptWorkflowService.start(receiptHeader, collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy()), "Receipt created - work flow starts");
		}

		transition(receiptHeaders, CollectionConstants.WF_ACTION_CREATE_RECEIPT, "Receipt created");

		LOGGER.debug("Workflow state transition complete");

		if (createVoucherForBillingService) {
			createVouchers(receiptHeaders, receiptBulkUpload);
			getSession().flush();

			transition(receiptHeaders, CollectionConstants.WF_ACTION_CREATE_VOUCHER, "Receipt voucher created");
		}

		if (receiptBulkUpload) {
			for (ReceiptHeader receiptHeader : receiptHeaders) {
				// transition the receipt header workflow to Approved state
				receiptWorkflowService.transition(CollectionConstants.WF_ACTION_APPROVE, receiptHeader, "Approval of Data Migration Receipt Complete");
				// End the Receipt header workflow
				receiptWorkflowService.end(receiptHeader, collectionsUtil.getPositionOfUser(receiptHeader.getCreatedBy()), "Data Migration Receipt Approved - Workflow ends");
			}
		}
	}

	/**
	 * Transitions the given set of receipt headers with given action
	 * 
	 * @param receiptHeaders
	 *            Set of receipt headers to be transitioned
	 * @param actionName
	 *            Action name for the transition
	 * @param comment
	 *            Comment for the transition
	 */
	public void transition(Collection<ReceiptHeader> receiptHeaders, String actionName, String comment) {
		SimpleWorkflowService<ReceiptHeader> receiptWorkflowService = new SimpleWorkflowService<ReceiptHeader>(this);
		for (ReceiptHeader receiptHeader : receiptHeaders) {
			receiptWorkflowService.transition(actionName, receiptHeader, comment);
		}
	}

	/**
	 * Method to find all the Cash,Cheque and DD type instruments with status as
	 * :new and
	 * 
	 * @return List of HashMap
	 */
	public List<HashMap<String, Object>> findAllRemitanceDetails(String boundaryIdList) {
		List<HashMap<String, Object>> paramList = new ArrayList<HashMap<String, Object>>();

		String queryBuilder = "SELECT sum(ih.instrumentamount) as INSTRUMENTMAOUNT,vh.VOUCHERDATE AS VOUCHERDATE,"
				+ "sd.SERVICENAME as SERVICENAME,it.TYPE as INSTRUMENTTYPE,fnd.name AS FUNDNAME,dpt.dept_name AS DEPARTMENTNAME,"
				+ "fnd.code AS FUNDCODE,dpt.dept_code AS DEPARTMENTCODE from EGCL_COLLECTIONHEADER ch,VOUCHERHEADER vh,"
				+ "EGCL_COLLECTIONVOUCHER cv,EGF_INSTRUMENTHEADER ih,EGCL_COLLECTIONINSTRUMENT ci,EG_SERVICEDETAILS sd,"
				+ "EGF_INSTRUMENTTYPE it,EGCL_COLLECTIONMIS cm,FUND fnd,EG_DEPARTMENT dpt";

		String whereClauseBeforInstumentType = " where ch.id=cv.COLLECTIONHEADERID AND ch.id=cm.id_collectionheader AND "
				+ "fnd.id=cm.id_fund AND dpt.id_dept=cm.id_department and vh.id=cv.VOUCHERHEADERID and ci.INSTRUMENTMASTERID=ih.ID and "
				+ "ch.ID_SERVICE=sd.ID and ch.ID=ci.COLLECTIONHEADERID and ih.INSTRUMENTTYPE=it.ID and ";

		String whereClause = " AND ih.ID_STATUS=(select id from egw_status where moduletype='" + CollectionConstants.MODULE_NAME_INSTRUMENTHEADER + "' " + "and description='"
				+ CollectionConstants.INSTRUMENT_NEW_STATUS + "') and ih.ISPAYCHEQUE=0 and vh.MODULEID=(select id from eg_modules " + "where name='"
				+ CollectionConstants.MODULE_NAME_COLLECTIONS + "') and ch.ID_STATUS=(select id from egw_status where " + "moduletype='"
				+ CollectionConstants.MODULE_NAME_RECEIPTHEADER + "' and code='" + CollectionConstants.RECEIPT_STATUS_CODE_APPROVED + "') ";

		String groupByClause = " group by vh.VOUCHERDATE,sd.SERVICENAME,it.TYPE,fnd.name,dpt.dept_name,fnd.code,dpt.dept_code";
		String orderBy = " order by VOUCHERDATE";

		/**
		 * Query to get the collection of the instrument types Cash,Cheque,DD &
		 * Card for bank remittance
		 */
		StringBuilder queryStringForCashChequeDDCard = new StringBuilder(queryBuilder + ",EG_USER_JURLEVEL ujl,EG_USER_JURVALUES ujv" + whereClauseBeforInstumentType
				+ "it.TYPE in ('" + CollectionConstants.INSTRUMENTTYPE_CASH + "','" + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'," + "'"
				+ CollectionConstants.INSTRUMENTTYPE_DD + "','" + CollectionConstants.INSTRUMENTTYPE_CARD + "') " + whereClause
				+ "AND ch.CREATED_BY=ujl.ID_USER AND ujl.id_user_jurlevel= ujv.id_user_jurlevel and ujv.id_bndry in (" + boundaryIdList + ")" + groupByClause);

		/**
		 * If the department of login user is AccountCell .i.e., Department
		 * Code-'A',then this user will be able to remit online transaction as
		 * well. All the online receipts created by 'citizen' user will be
		 * remitted by Account Cell user.
		 */
		User citizenUser = collectionsUtil.getUserByUserName(CollectionConstants.CITIZEN_USER_NAME);

		if (boundaryIdList != null && citizenUser != null) {
			String queryStringForOnline = " union " + queryBuilder + whereClauseBeforInstumentType + "it.TYPE='" + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'" + whereClause
					+ "AND ch.CREATED_BY=" + citizenUser.getId() + groupByClause;

			queryStringForCashChequeDDCard.append(queryStringForOnline);
		}

		Query query = this.getSession().createSQLQuery(queryStringForCashChequeDDCard.toString() + orderBy);

		List<Object[]> queryResults = query.list();

		for (int i = 0; i < queryResults.size(); i++) {
			Object[] arrayObjectInitialIndex = queryResults.get(i);
			HashMap<String, Object> objHashMap = new HashMap<String, Object>();

			if (i == 0) {
				objHashMap.put(CollectionConstants.BANKREMITTANCE_VOUCHERDATE, arrayObjectInitialIndex[1]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
				objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, arrayObjectInitialIndex[0]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
				}
				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, arrayObjectInitialIndex[0]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
				}
				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
				}
				if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
				}
			} else {
				int checknew = checkIfMapObjectExist(paramList, arrayObjectInitialIndex);
				if (checknew == -1) {
					objHashMap.put(CollectionConstants.BANKREMITTANCE_VOUCHERDATE, arrayObjectInitialIndex[1]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICENAME, arrayObjectInitialIndex[2]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDNAME, arrayObjectInitialIndex[4]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTNAME, arrayObjectInitialIndex[5]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_FUNDCODE, arrayObjectInitialIndex[6]);
					objHashMap.put(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE, arrayObjectInitialIndex[7]);

					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, arrayObjectInitialIndex[0]);
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, arrayObjectInitialIndex[0]);
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, "");
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, "");
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					}
				} else {
					objHashMap = paramList.get(checknew);

					paramList.remove(checknew);

					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCASHAMOUNT, arrayObjectInitialIndex[0]);
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE) || arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
						BigDecimal existingAmount = BigDecimal.ZERO;
						if (objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT) != "") {
							existingAmount = new BigDecimal(objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT).toString());
						}
						existingAmount = existingAmount.add(new BigDecimal(arrayObjectInitialIndex[0].toString()));
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT, existingAmount);
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_CARD)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					}
					if (arrayObjectInitialIndex[3].equals(CollectionConstants.INSTRUMENTTYPE_ONLINE)) {
						objHashMap.put(CollectionConstants.BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT, arrayObjectInitialIndex[0]);
					}
				}
			}
			if (objHashMap.get(CollectionConstants.BANKREMITTANCE_VOUCHERDATE) != null && objHashMap.get(CollectionConstants.BANKREMITTANCE_SERVICENAME) != null)
				paramList.add(objHashMap);
		}
		return paramList;
	}

	/**
	 * Method to check if the given HashMap already exists in the List of
	 * HashMap
	 * 
	 * @param queryResults
	 * @param objHashMap
	 * @param m
	 * @return index of objHashMap in the queryResults
	 */
	public int checkIfMapObjectExist(List<HashMap<String, Object>> paramList, Object[] arrayObjectInitialIndexTemp) {
		int check = -1;
		for (int m = 0; m < paramList.size(); m++) {
			HashMap<String, Object> objHashMapTemp = paramList.get(m);

			if (arrayObjectInitialIndexTemp[1] != null && arrayObjectInitialIndexTemp[2] != null) {
				if (arrayObjectInitialIndexTemp[1].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_VOUCHERDATE))
						&& arrayObjectInitialIndexTemp[2].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_SERVICENAME))
						&& arrayObjectInitialIndexTemp[6].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_FUNDCODE))
						&& arrayObjectInitialIndexTemp[7].equals(objHashMapTemp.get(CollectionConstants.BANKREMITTANCE_DEPARTMENTCODE))) {
					check = m;
					break;
				} else
					continue;
			}

		}
		return check;
	}

	/**
	 * Create Contra Vouchers for String array passed of serviceName,
	 * totalCashAmount, totalChequeAmount, totalCardAmount and totalOnlineAcount
	 * 
	 * @param serviceName
	 * @param totalCashAmount
	 * @param totalChequeAmount
	 * @return List of Contra Vouchers Created
	 */
	public List<CVoucherHeader> createBankRemittance(String[] serviceNameArr, String[] totalCashAmount, String[] totalChequeAmount, String[] totalCardAmount,
			String[] totalOnlineAmount, String[] receiptDateArray, String[] fundCodeArray, String[] departmentCodeArray, Integer accountNumberId, Integer positionUser) {
		List<CVoucherHeader> newContraVoucherList = new ArrayList<CVoucherHeader>();
		SimpleDateFormat dateFomatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

		String instrumentGlCodeQueryString = "SELECT COA.GLCODE FROM CHARTOFACCOUNTS COA,EGF_INSTRUMENTACCOUNTCODES IAC,EGF_INSTRUMENTTYPE IT "
				+ "WHERE IT.ID=IAC.TYPEID AND IAC.GLCODEID=COA.ID AND IT.TYPE=";
		String receiptInstrumentQueryString = "select DISTINCT (instruments) from org.egov.erpcollection.models.ReceiptHeader receipt "
				+ "join receipt.receiptInstrument as instruments where ";
		String serviceNameCondition = "receipt.service.serviceName=? ";
		String receiptDateCondition = "and to_char(receipt.createdDate,'yyyy-MM-dd')=? ";
		String instrumentStatusCondition = "and instruments.statusId.id=? ";
		String instrumentTypeCondition = "and instruments.instrumentType.type = ? ";
		String receiptFundCondition = "and receipt.receiptMisc.fund.code = ? ";
		String receiptDepartmentCondition = "and receipt.receiptMisc.department.deptCode = ? ";

		String cashInHandQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_CASH + "'";
		String chequeInHandQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_CHEQUE + "'";
		String cardPaymentQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_CARD + "'";
		String onlinePaymentQueryString = instrumentGlCodeQueryString + "'" + CollectionConstants.INSTRUMENTTYPE_ONLINE + "'";

		Query cashInHand = this.getSession().createSQLQuery(cashInHandQueryString);
		Query chequeInHand = this.getSession().createSQLQuery(chequeInHandQueryString);
		Query cardPaymentAccount = this.getSession().createSQLQuery(cardPaymentQueryString);
		Query onlinePaymentAccount = this.getSession().createSQLQuery(onlinePaymentQueryString);

		String cashInHandGLCode = null, chequeInHandGlcode = null, cardPaymentGlCode = null, onlinePaymentGlCode = null;
		
		String voucherWorkflowMsg = "Voucher Workflow Started";

		if (!cashInHand.list().isEmpty()) {
			cashInHandGLCode = cashInHand.list().get(0).toString();
		}
		if (!chequeInHand.list().isEmpty()) {
			chequeInHandGlcode = chequeInHand.list().get(0).toString();
		}
		if (!cardPaymentAccount.list().isEmpty()) {
			cardPaymentGlCode = cardPaymentAccount.list().get(0).toString();
		}
		if (!onlinePaymentAccount.list().isEmpty()) {
			onlinePaymentGlCode = onlinePaymentAccount.list().get(0).toString();
		}

		EgwStatus status = commonsService.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_INSTRUMENTHEADER, CollectionConstants.INSTRUMENT_NEW_STATUS);

		/**
		 * Get the AppConfig parameter defined for the Remittance voucher type
		 * in case of instrument type Cheque,DD & Card
		 */

		Boolean voucherTypeForChequeDDCard = false;
		Boolean useReceiptDateAsContraVoucherDate = false;

		if ((collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD)
				.equals(CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE))) {
			voucherTypeForChequeDDCard = true;
		}

		if ((collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.APPCONFIG_VALUE_USERECEIPTDATEFORCONTRA)
				.equals(CollectionConstants.YES))) {
			useReceiptDateAsContraVoucherDate = true;
		}

		for (int i = 0; i < serviceNameArr.length; i++) {
			String serviceName = serviceNameArr[i].trim();
			Date voucherDate = new Date();

			if (useReceiptDateAsContraVoucherDate) {
				try {
					voucherDate = dateFomatter.parse(receiptDateArray[i]);
				} catch (ParseException exp) {
					LOGGER.debug("Exception in parsing date  " + receiptDateArray[i] + " - " + exp.getMessage());
				}
			}

			if (serviceName != null && serviceName.length() > 0) {
				String serviceGLCodeQueryString = "select coa.glcode from BANKACCOUNT ba,CHARTOFACCOUNTS coa where " + "ba.GLCODEID=coa.ID and ba.ID=" + accountNumberId;
				ServiceDetails serviceDetails = (ServiceDetails) persistenceService.findByNamedQuery(CollectionConstants.QUERY_SERVICE_BY_NAME, serviceName);

				Query serviceGLCodeQuery = this.getSession().createSQLQuery(serviceGLCodeQueryString);

				String serviceGlCode = serviceGLCodeQuery.list().get(0).toString();
				List<HashMap<String, Object>> subledgerList = new ArrayList<HashMap<String, Object>>();

				// If Cash Amount is present
				if (totalCashAmount[i].trim() != null && totalCashAmount[i].trim().length() > 0 && cashInHandGLCode != null) {
					StringBuilder cashQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					cashQueryBuilder.append(serviceNameCondition);
					cashQueryBuilder.append(receiptDateCondition);
					cashQueryBuilder.append(instrumentStatusCondition);
					cashQueryBuilder.append(instrumentTypeCondition);
					cashQueryBuilder.append(receiptFundCondition);
					cashQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[6];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_CASH;
					arguments[4] = fundCodeArray[i];
					arguments[5] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListCash = (List<InstrumentHeader>) persistenceService.findAllBy(cashQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();

					headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
					headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					List<HashMap<String, Object>> accountCodeCashList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditCashHashMap = new HashMap<String, Object>();

					accountcodedetailsCreditCashHashMap.put(VoucherConstant.GLCODE, cashInHandGLCode);
					accountcodedetailsCreditCashHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditCashHashMap.put(VoucherConstant.CREDITAMOUNT, totalCashAmount[i]);
					accountcodedetailsCreditCashHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeCashList.add(accountcodedetailsCreditCashHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCashAmount[i]);
						accountCodeCashList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCash = new CVoucherHeader();
					try {
						voucherHeaderCash = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeCashList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when cash amount>0");
					}
					newContraVoucherList.add(voucherHeaderCash);

					for (InstrumentHeader instrumentHeader : instrumentHeaderListCash) {
						if (voucherHeaderCash.getId() != null && serviceGlCode != null) {
							financialsUtil.updateCashDeposit(voucherHeaderCash.getId(), serviceGlCode, instrumentHeader);
							ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
									CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCash.getId(), instrumentHeader.getId());
							contraWorkflowService.start(contraJournalVoucher, collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()), "Voucher Created");
							contraWorkflowService.transition(contraJournalVoucher, collectionsUtil.getPositionById(positionUser), voucherWorkflowMsg);
						}
					}

				}
				// If Cheque Amount is present
				if (totalChequeAmount[i].trim() != null && totalChequeAmount[i].trim().length() > 0 && chequeInHandGlcode != null) {
					StringBuilder chequeQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					chequeQueryBuilder.append(serviceNameCondition);
					chequeQueryBuilder.append(receiptDateCondition);
					chequeQueryBuilder.append(instrumentStatusCondition);
					chequeQueryBuilder.append("and instruments.instrumentType.type in ( ?, ?)");
					chequeQueryBuilder.append("and receipt.status.id=(select id from org.egov.commons.EgwStatus where moduletype=? and code=?) ");
					chequeQueryBuilder.append(receiptFundCondition);
					chequeQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[9];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_CHEQUE;
					arguments[4] = CollectionConstants.INSTRUMENTTYPE_DD;
					arguments[5] = CollectionConstants.MODULE_NAME_RECEIPTHEADER;
					arguments[6] = CollectionConstants.RECEIPT_STATUS_CODE_APPROVED;
					arguments[7] = fundCodeArray[i];
					arguments[8] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListCheque = (List<InstrumentHeader>) persistenceService.findAllBy(chequeQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();
					List<HashMap<String, Object>> accountCodeChequeList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditChequeHashMap = new HashMap<String, Object>();

					if (voucherTypeForChequeDDCard) {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);

					} else {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					}
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.GLCODE, chequeInHandGlcode);
					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.CREDITAMOUNT, totalChequeAmount[i]);
					accountcodedetailsCreditChequeHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeChequeList.add(accountcodedetailsCreditChequeHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalChequeAmount[i]);
						accountCodeChequeList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCheque = new CVoucherHeader();
					try {
						voucherHeaderCheque = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeChequeList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when cheque amount>0");
					}
					newContraVoucherList.add(voucherHeaderCheque);

					for (InstrumentHeader instrumentHeader : instrumentHeaderListCheque) {
						if (voucherHeaderCheque.getId() != null && serviceGlCode != null) {
							if (voucherTypeForChequeDDCard) {
								financialsUtil.updateCheque_DD_Card_Deposit_Receipt(voucherHeaderCheque.getId(), serviceGlCode, instrumentHeader);
							} else {

								financialsUtil.updateCheque_DD_Card_Deposit(voucherHeaderCheque.getId(), serviceGlCode, instrumentHeader);
								ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
										CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCheque.getId(), instrumentHeader.getId());
								contraWorkflowService.start(contraJournalVoucher, collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
								contraWorkflowService.transition(contraJournalVoucher, collectionsUtil.getPositionById(positionUser), voucherWorkflowMsg);
							}
						}

					}
				}
				// If card amount is present
				if (totalCardAmount[i].trim() != null && totalCardAmount[i].trim().length() > 0 && cardPaymentGlCode != null) {
					StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					onlineQueryBuilder.append(serviceNameCondition);
					onlineQueryBuilder.append(receiptDateCondition);
					onlineQueryBuilder.append(instrumentStatusCondition);
					onlineQueryBuilder.append(instrumentTypeCondition);
					onlineQueryBuilder.append(receiptFundCondition);
					onlineQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[6];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_CARD;
					arguments[4] = fundCodeArray[i];
					arguments[5] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListOnline = (List<InstrumentHeader>) persistenceService.findAllBy(onlineQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();

					if (voucherTypeForChequeDDCard) {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
					} else {

						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					}
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>();

					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, cardPaymentGlCode);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalCardAmount[i]);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalCardAmount[i]);
						accountCodeOnlineList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCard = new CVoucherHeader();
					try {
						voucherHeaderCard = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeOnlineList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when online amount>0");
					}
					newContraVoucherList.add(voucherHeaderCard);

					for (InstrumentHeader instrumentHeader : instrumentHeaderListOnline) {
						if (voucherHeaderCard.getId() != null && serviceGlCode != null) {

							if (voucherTypeForChequeDDCard) {
								financialsUtil.updateCheque_DD_Card_Deposit_Receipt(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader);
							} else {
								financialsUtil.updateCheque_DD_Card_Deposit(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader);
								ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
										CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCard.getId(), instrumentHeader.getId());
								contraWorkflowService.start(contraJournalVoucher, collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
								contraWorkflowService.transition(contraJournalVoucher, collectionsUtil.getPositionById(positionUser), voucherWorkflowMsg);
							}
						}
					}
				}
				// If online amount is present
				if (totalOnlineAmount[i].trim() != null && totalOnlineAmount[i].trim().length() > 0 && onlinePaymentGlCode != null) {
					StringBuilder onlineQueryBuilder = new StringBuilder(receiptInstrumentQueryString);
					onlineQueryBuilder.append(serviceNameCondition);
					onlineQueryBuilder.append(receiptDateCondition);
					onlineQueryBuilder.append(instrumentStatusCondition);
					onlineQueryBuilder.append(instrumentTypeCondition);
					onlineQueryBuilder.append(receiptFundCondition);
					onlineQueryBuilder.append(receiptDepartmentCondition);

					Object arguments[] = new Object[6];

					arguments[0] = serviceName;
					arguments[1] = receiptDateArray[i];
					arguments[2] = status.getId();
					arguments[3] = CollectionConstants.INSTRUMENTTYPE_ONLINE;
					arguments[4] = fundCodeArray[i];
					arguments[5] = departmentCodeArray[i];

					List<InstrumentHeader> instrumentHeaderListOnline = (List<InstrumentHeader>) persistenceService.findAllBy(onlineQueryBuilder.toString(), arguments);

					HashMap<String, Object> headerdetails = new HashMap<String, Object>();

					if (voucherTypeForChequeDDCard) {
						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_RECEIPTS_VOUCHERTYPE);
					} else {

						headerdetails.put(VoucherConstant.VOUCHERNAME, CollectionConstants.FINANCIAL_CONTRATVOUCHER_VOUCHERNAME);
						headerdetails.put(VoucherConstant.VOUCHERTYPE, CollectionConstants.FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE);
					}
					headerdetails.put(VoucherConstant.DESCRIPTION, CollectionConstants.FINANCIAL_VOUCHERDESCRIPTION);
					headerdetails.put(VoucherConstant.VOUCHERDATE, voucherDate);
					headerdetails.put(VoucherConstant.FUNDCODE, fundCodeArray[i]);
					headerdetails.put(VoucherConstant.DEPARTMENTCODE, departmentCodeArray[i]);
					headerdetails.put(VoucherConstant.FUNDSOURCECODE, serviceDetails.getFundSource() == null ? null : serviceDetails.getFundSource().getCode());
					headerdetails.put(VoucherConstant.FUNCTIONARYCODE, serviceDetails.getFunctionary() == null ? null : serviceDetails.getFunctionary().getCode());
					headerdetails.put(VoucherConstant.MODULEID, CollectionConstants.COLLECTIONS_EG_MODULES_ID);

					List<HashMap<String, Object>> accountCodeOnlineList = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> accountcodedetailsCreditOnlineHashMap = new HashMap<String, Object>();

					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.GLCODE, onlinePaymentGlCode);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.FUNCTIONCODE, null);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.CREDITAMOUNT, totalOnlineAmount[i]);
					accountcodedetailsCreditOnlineHashMap.put(VoucherConstant.DEBITAMOUNT, 0);

					accountCodeOnlineList.add(accountcodedetailsCreditOnlineHashMap);
					// TODO: Add debit account details
					{
						HashMap<String, Object> accountcodedetailsDebitHashMap = new HashMap<String, Object>();
						accountcodedetailsDebitHashMap.put(VoucherConstant.GLCODE, serviceGlCode);
						accountcodedetailsDebitHashMap.put(VoucherConstant.FUNCTIONCODE, null);
						accountcodedetailsDebitHashMap.put(VoucherConstant.CREDITAMOUNT, 0);
						accountcodedetailsDebitHashMap.put(VoucherConstant.DEBITAMOUNT, totalOnlineAmount[i]);
						accountCodeOnlineList.add(accountcodedetailsDebitHashMap);
					}

					CVoucherHeader voucherHeaderCard = new CVoucherHeader();
					try {
						voucherHeaderCard = financialsUtil.createPreApprovalVoucher(headerdetails, accountCodeOnlineList, subledgerList);
					} catch (Exception e) {
						LOGGER.error("Error in createBankRemittance createPreApprovalVoucher when online amount>0");
					}
					newContraVoucherList.add(voucherHeaderCard);

					for (InstrumentHeader instrumentHeader : instrumentHeaderListOnline) {
						if (voucherHeaderCard.getId() != null && serviceGlCode != null) {

							if (voucherTypeForChequeDDCard) {
								financialsUtil.updateCheque_DD_Card_Deposit_Receipt(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader);
							} else {
								financialsUtil.updateCheque_DD_Card_Deposit(voucherHeaderCard.getId(), serviceGlCode, instrumentHeader);
								ContraJournalVoucher contraJournalVoucher = (ContraJournalVoucher) persistenceService.findByNamedQuery(
										CollectionConstants.QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID, voucherHeaderCard.getId(), instrumentHeader.getId());
								contraWorkflowService.start(contraJournalVoucher, collectionsUtil.getPositionOfUser(contraJournalVoucher.getCreatedBy()));
								contraWorkflowService.transition(contraJournalVoucher, collectionsUtil.getPositionById(positionUser), voucherWorkflowMsg);
							}
						}
					}

				}
			}
		}
		return newContraVoucherList;
	}

	/**
	 * @param persistenceService
	 *            the persistenceService to set
	 */
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	/**
	 * @param commonsMgr
	 *            the commonsMgr to set
	 */
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	/**
	 * For Bill Based Receipt, aggregate the amount for same account heads
	 * 
	 * @param receiptDetailSetParam
	 * @return Set of Receipt Detail after Aggregating Amounts
	 */
	public Set<ReceiptDetail> aggregateDuplicateReceiptDetailObject(List<ReceiptDetail> receiptDetailSetParam) {
		List<ReceiptDetail> newReceiptDetailList = new ArrayList<ReceiptDetail>();

		int counter = 0;

		for (ReceiptDetail receiptDetailObj : receiptDetailSetParam) {
			if (counter == 0) {
				newReceiptDetailList.add(receiptDetailObj);
			} else {
				int checknew = checkIfReceiptDetailObjectExist(newReceiptDetailList, receiptDetailObj);
				if (checknew == -1) {
					newReceiptDetailList.add(receiptDetailObj);
				} else {
					ReceiptDetail receiptDetail = new ReceiptDetail();

					ReceiptDetail newReceiptDetailObj = (ReceiptDetail) newReceiptDetailList.get(checknew);
					newReceiptDetailList.remove(checknew);

					receiptDetail.setAccounthead(newReceiptDetailObj.getAccounthead());
					receiptDetail.setAccountPayeeDetails(newReceiptDetailObj.getAccountPayeeDetails());
					receiptDetail.setCramount(newReceiptDetailObj.getCramount().add(receiptDetailObj.getCramount()));
					receiptDetail.setCramountToBePaid(newReceiptDetailObj.getCramountToBePaid());
					receiptDetail.setDescription(newReceiptDetailObj.getDescription());
					receiptDetail.setDramount(newReceiptDetailObj.getDramount().add(receiptDetailObj.getDramount()));
					receiptDetail.setFinancialYear(newReceiptDetailObj.getFinancialYear());
					receiptDetail.setFunction(newReceiptDetailObj.getFunction());
					receiptDetail.setOrdernumber(newReceiptDetailObj.getOrdernumber());

					newReceiptDetailList.add(receiptDetail);
				}
			}
			counter++;
		}
		return new HashSet<ReceiptDetail>(newReceiptDetailList);
	}

	/**
	 * API to check if the given receipt detail object already exists in the
	 * list passed passed as parameter
	 * 
	 * @param newReceiptDetailSet
	 * @param receiptDetailObj
	 * @return
	 */
	public int checkIfReceiptDetailObjectExist(List<ReceiptDetail> newReceiptDetailSet, ReceiptDetail receiptDetailObj) {
		int check = -1;

		for (int m = 0; m < newReceiptDetailSet.size(); m++) {

			ReceiptDetail receiptDetail = (ReceiptDetail) newReceiptDetailSet.get(m);

			if (receiptDetailObj.getAccounthead().getId().equals(receiptDetail.getAccounthead().getId())) {
				check = m;
				break;
			} else
				continue;
		}
		return check;
	}

	/**
	 * End Work-flow of the given cancelled receipt
	 * 
	 * @param receiptHeaders
	 *            Set of receipt headers to be transitioned
	 * @param actionName
	 *            Action name for the transition
	 * @param comment
	 *            Comment for the transition
	 */
	public void endReceiptWorkFlowOnCancellation(ReceiptHeader receiptHeaderToBeCancelled) {
		// End work-flow for the cancelled receipt
		SimpleWorkflowService<ReceiptHeader> receiptWorkflowService = new SimpleWorkflowService<ReceiptHeader>(this);
		Position position = collectionsUtil.getPositionOfUser(receiptHeaderToBeCancelled.getCreatedBy());
		if (position != null) {
			receiptWorkflowService.end(receiptHeaderToBeCancelled, position, "Receipt Cancelled - Workflow ends");
		}
	}
}