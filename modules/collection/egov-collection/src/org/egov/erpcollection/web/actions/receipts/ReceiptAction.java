package org.egov.erpcollection.web.actions.receipts;

import static org.egov.erpcollection.web.constants.CollectionConstants.RCPT_CANCEL;
import static org.egov.erpcollection.web.constants.CollectionConstants.RCPT_CREATE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.service.CommonsService;
import org.egov.erpcollection.integration.models.BillInfoImpl;
import org.egov.erpcollection.models.AccountPayeeDetail;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.models.ReceiptDetailInfo;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.models.ReceiptMisc;
import org.egov.erpcollection.models.ReceiptPayeeDetails;
import org.egov.erpcollection.models.ReceiptVoucher;
import org.egov.erpcollection.services.ReceiptHeaderService;
import org.egov.erpcollection.services.ReceiptService;
import org.egov.erpcollection.util.CollectionCommon;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.util.FinancialsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.erpcollection.web.handler.BillCollectXmlHandler;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.ServiceCategory;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.NumberUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

@ParentPackage("egov")
public class ReceiptAction extends BaseFormAction {
	private static final String ACCOUNT_NUMBER_LIST = "accountNumberList";
	private static final Logger LOGGER = Logger.getLogger(ReceiptAction.class);
	private ReceiptService receiptPayeeDetailsService;
	private BillInfoImpl collDetails = new BillInfoImpl();
	private static final long serialVersionUID = 1L;
	private static final String CANCEL = "cancel";
	private Integer reportId = -1;

	/**
	 * A <code>String</code> representing the input xml coming from the billing
	 * system
	 */
	private String collectXML;
	private CommonsService commonsService;
	private BillCollectXmlHandler xmlHandler;
	private FinancialsUtil financialsUtil;

	/**
	 * A <code>Long</code> array of receipt header ids , which have to be
	 * displayed for view/print/cancel purposes
	 */
	private Long[] selectedReceipts;

	/**
	 * An array of <code>ReceiptHeader</code> instances which have to be
	 * displayed for view/print/cancel purposes
	 */
	private ReceiptHeader[] receipts;

	private ReceiptHeaderService receiptHeaderService;

	/**
	 * Collections utility object
	 */
	private CollectionsUtil collectionsUtil;

	private List<ReceiptHeader> receiptHeaderValues = new ArrayList<ReceiptHeader>();

	// Instrument information derived from UI
	private List<InstrumentHeader> instrumentProxyList;
	private int instrumentCount;

	private BigDecimal cashOrCardInstrumenttotal = BigDecimal.ZERO;
	private BigDecimal chequeInstrumenttotal = BigDecimal.ZERO;
	private BigDecimal instrumenttotal = BigDecimal.ZERO;
	private String reasonForCancellation;
	private String target = "view";
	private String paidBy;

	/**
	 * A <code>Long</code> value representing the receipt header id captured
	 * from the front end, which has to be cancelled.
	 */
	private Long oldReceiptId;

	private Boolean overrideAccountHeads;
	private Boolean partPaymentAllowed;
	private Boolean callbackForApportioning;
	private BigDecimal totalAmountToBeCollected;

	private Boolean cashAllowed = Boolean.TRUE;
	private Boolean cardAllowed = Boolean.TRUE;
	private Boolean chequeAllowed = Boolean.TRUE;
	private Boolean ddAllowed = Boolean.TRUE;
	private Boolean bankAllowed = Boolean.TRUE;

	/**
	 * An instance of <code>InstrumentHeader</code> representing the cash
	 * instrument details entered by the user during receipt creation
	 */
	private InstrumentHeader instrHeaderCash;

	/**
	 * An instance of <code>InstrumentHeader</code> representing the card
	 * instrument details entered by the user during receipt creation
	 */
	private InstrumentHeader instrHeaderCard;

	/**
	 * An instance of <code>InstrumentHeader</code> representing the 'bank'
	 * instrument details entered by the user during receipt creation
	 */
	private InstrumentHeader instrHeaderBank;

	private Date voucherDate;
	private String voucherNum;
	private List<ReceiptDetailInfo> subLedgerlist;
	private List<ReceiptDetailInfo> billCreditDetailslist;
	private List<ReceiptDetailInfo> billRebateDetailslist;
	protected List<String> headerFields;
	protected List<String> mandatoryFields;
	private String billSource = "bill";
	private ReceiptMisc receiptMisc = new ReceiptMisc();
	private String deptId;
	private BigDecimal totalDebitAmount;
	/**
	 * A code>String</code> representing the service name
	 */
	private String serviceName;

	/**
	 * A <code>List</code> of <code>String</code> informations sent by the
	 * billing system indicating which are the modes of payment that are not
	 * allowed during receipt creation
	 */
	private List<String> collectionModesNotAllowed = new ArrayList<String>();

	/**
	 * The <code>User</code> representing the counter operator who has created
	 * the receipt
	 */
	private User receiptCreatedByCounterOperator;

	/**
	 * A <code>List</code> of <code>ReceiptPayeeDetails</code> representing the
	 * model for the action.
	 */
	private List<ReceiptPayeeDetails> modelPayeeList = new ArrayList<ReceiptPayeeDetails>();

	private List<ReceiptDetail> receiptDetailList = new ArrayList<ReceiptDetail>();

	private String instrumentTypeCashOrCard;

	private CollectionCommon collectionCommon;

	private Integer bankAccountId;

	private Integer bankBranchId;

	private String payeename = "";

	private Date manualReceiptDate;

	private String manualReceiptNumber;

	private Boolean manualReceiptNumberAndDateReq = Boolean.FALSE;

	private Boolean receiptBulkUpload = Boolean.FALSE;

	private PersistenceService<ServiceCategory, Long> serviceCategoryService;

	private PersistenceService<ServiceDetails, Long> serviceDetailsService;

	private ServiceDetails service;
	
	private List<CChartOfAccounts> bankCOAList =  FinancialsUtil.getBankChartofAccountCodeList();

	@Override
	public void prepare() {
		super.prepare();

		setReceiptCreatedByCounterOperator(collectionsUtil.getLoggedInUser(getSession()));
		// populates model when request is from the billing system
		if (getCollectXML() != null && !getCollectXML().equals("")) {
			String decodedCollectXML = java.net.URLDecoder.decode(getCollectXML());
			try {
				modelPayeeList.clear();
				collDetails = (BillInfoImpl) xmlHandler.toObject(decodedCollectXML);

				Fund fund = commonsService.fundByCode(collDetails.getFundCode());
				if (fund == null) {
					addActionError(getText("billreceipt.improperbilldata.missingfund"));
				}

				setFundName(fund.getName());

				DepartmentImpl dept = (DepartmentImpl) getPersistenceService().findByNamedQuery(
						CollectionConstants.QUERY_DEPARTMENT_BY_CODE, collDetails.getDepartmentCode());
				if (dept == null) {
					addActionError(getText("billreceipt.improperbilldata.missingdepartment"));
				}

				ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
						CollectionConstants.QUERY_SERVICE_BY_CODE, collDetails.getServiceCode());

				setServiceName(service.getServiceName());
				setCollectionModesNotAllowed(collDetails.getCollectionModesNotAllowed());
				setOverrideAccountHeads(collDetails.getOverrideAccountHeadsAllowed());
				setCallbackForApportioning(collDetails.getCallbackForApportioning());
				setPartPaymentAllowed(collDetails.getPartPaymentAllowed());
				totalAmountToBeCollected = BigDecimal.valueOf(0);

				// populate bank account list
				populateBankBranchList(true);

				modelPayeeList = collectionCommon.initialiseReceiptModelWithBillInfo(collDetails, fund, dept);
				for (ReceiptPayeeDetails payeeDetails : modelPayeeList) {
					for (ReceiptHeader receiptHeader : payeeDetails.getReceiptHeaders()) {
						totalAmountToBeCollected = totalAmountToBeCollected.add(receiptHeader
								.getTotalAmountToBeCollected());
						for (ReceiptDetail rDetails : receiptHeader.getReceiptDetails()) {
							rDetails.getCramountToBePaid().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,
									BigDecimal.ROUND_UP);
						}
						this.setReceiptDetailList(new ArrayList<ReceiptDetail>(receiptHeader.getReceiptDetails()));
					}
				}
				this.setTotalAmountToBeCollected(totalAmountToBeCollected.setScale(
						CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));

			} catch (Exception e) {
				LOGGER.error(getText("billreceipt.error.improperbilldata") + e.getMessage());
				addActionError(getText("billreceipt.error.improperbilldata"));
			}
		}
		addDropdownData("serviceCategoryList", serviceCategoryService.findAllByNamedQuery("SERVICE_CATEGORY_ALL"));
		if (null != service && null != service.getServiceCategory() && service.getServiceCategory().getId() != -1) {
			addDropdownData("serviceList", serviceDetailsService.findAllByNamedQuery("SERVICE_BY_CATEGORY_FOR_TYPE",
					service.getServiceCategory().getId(), CollectionConstants.SERVICE_TYPE_COLLECTION, Boolean.TRUE));
		} else {
			addDropdownData("serviceList", Collections.EMPTY_LIST);
		}
		if (instrumentProxyList == null) {
			instrumentCount = 0;
		} else {
			instrumentCount = instrumentProxyList.size();
		}
	}

	/**
	 * 
	 * @param populate
	 */
	private void populateBankBranchList(boolean populate) {
		AjaxBankRemittanceAction ajaxBankRemittanceAction = new AjaxBankRemittanceAction();
		ajaxBankRemittanceAction.setServiceName(getServiceName());

		if (populate) {
			ajaxBankRemittanceAction.setFundName(getFundName());
			ajaxBankRemittanceAction.bankBranchList();
			addDropdownData("bankBranchList", ajaxBankRemittanceAction.getBankBranchArrayList());
			addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
		} else {
			// to load branch list and account list while returning after an
			// error
			if (getServiceName() != null && receiptMisc.getFund() != null) {
				Fund fund = commonsService.fundById(receiptMisc.getFund().getId());
				ajaxBankRemittanceAction.setFundName(fund.getName());
				ajaxBankRemittanceAction.bankBranchList();
				addDropdownData("bankBranchList", ajaxBankRemittanceAction.getBankBranchArrayList());

				// account list should be populated only if bank branch had been
				// chosen
				if (bankBranchId != null && bankBranchId != 0) {
					Bankbranch branch = commonsService.getBankbranchById(bankBranchId);

					ajaxBankRemittanceAction.setBranchId(branch.getId());
					ajaxBankRemittanceAction.accountList();
					addDropdownData(ACCOUNT_NUMBER_LIST, ajaxBankRemittanceAction.getBankAccountArrayList());
				} else {
					addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
				}
			} else {
				addDropdownData("bankBranchList", Collections.emptyList());
				addDropdownData(ACCOUNT_NUMBER_LIST, Collections.emptyList());
			}
		}
	}

	/**
	 * This method checks for the modes of payment allowed
	 */
	private void setCollectionModesNotAllowed() {

		List<String> modesNotAllowed = collectionsUtil.getCollectionModesNotAllowed(this
				.getReceiptCreatedByCounterOperator());

		List<String> collectionModesNotAllowed = getCollectionModesNotAllowed();

		if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CASH)
				|| (collectionModesNotAllowed != null && collectionModesNotAllowed
						.contains(CollectionConstants.INSTRUMENTTYPE_CASH))) {
			setCashAllowed(Boolean.FALSE);
		}

		if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CARD)
				|| (collectionModesNotAllowed != null && collectionModesNotAllowed
						.contains(CollectionConstants.INSTRUMENTTYPE_CARD))) {
			setCardAllowed(Boolean.FALSE);
		}
		if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
				|| (collectionModesNotAllowed != null && (collectionModesNotAllowed
						.contains(CollectionConstants.INSTRUMENTTYPE_CHEQUE)))) {
			setChequeAllowed(Boolean.FALSE);
		}

		if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_DD)
				|| (collectionModesNotAllowed != null && (collectionModesNotAllowed
						.contains(CollectionConstants.INSTRUMENTTYPE_DD)))) {
			setDdAllowed(Boolean.FALSE);
		}

		if (modesNotAllowed.contains(CollectionConstants.INSTRUMENTTYPE_BANK)
				|| (collectionModesNotAllowed != null && collectionModesNotAllowed
						.contains(CollectionConstants.INSTRUMENTTYPE_BANK))) {
			setBankAllowed(Boolean.FALSE);
		}
	}

	/**
	 * To set the receiptpayee details for misc receipts
	 */
	private boolean setMiscReceiptDetails() {
		modelPayeeList.clear();

		// for manually created misc receipts the payee name is picked from the
		// given file.
		// for regular misc receipts, the payee name has to be picked from
		// config

		if (CollectionConstants.BLANK.equals(payeename)) {
			payeename = collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
					CollectionConstants.APPCONFIG_VALUE_PAYEEFORMISCRECEIPTS);
		}

		ReceiptPayeeDetails receiptPayee = new ReceiptPayeeDetails(payeename, "");

		ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
				CollectionConstants.QUERY_SERVICE_BY_CODE, CollectionConstants.SERVICE_CODE_COLLECTIONS);
		if (null != this.service && null != this.service.getId() && this.service.getId() != -1) {
			service = serviceDetailsService.findById(this.service.getId(), false);

		}
		ReceiptHeader receiptHeader = new ReceiptHeader();
		receiptHeader.setPartPaymentAllowed(false);
		receiptHeader.setService(service);
		Fund fund = commonsService.fundById(receiptMisc.getFund().getId());
		Functionary functionary = null;
		Scheme scheme = null;
		SubScheme subscheme = null;
		try {
			if (receiptMisc.getIdFunctionary() != null) {
				functionary = commonsService.getFunctionaryById(receiptMisc.getIdFunctionary().getId());
			}
			if (receiptMisc.getScheme() != null) {
				scheme = commonsService.getSchemeById(receiptMisc.getScheme().getId());
			}
			if (receiptMisc.getSubscheme() != null) {
				subscheme = commonsService.getSubSchemeById(receiptMisc.getSubscheme().getId());
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting functionary for id [" + receiptMisc.getIdFunctionary().getId() + "]", e);
		}

		Fundsource fundSource = null;
		if (receiptMisc.getFundsource() != null)
			fundSource = commonsService.fundsourceById(receiptMisc.getFundsource().getId());
		DepartmentImpl dept = (DepartmentImpl) getPersistenceService().findByNamedQuery(
				CollectionConstants.QUERY_DEPARTMENT_BY_ID, Integer.valueOf(this.deptId));

		ReceiptMisc receiptMisc = new ReceiptMisc(null, fund, functionary, fundSource, dept, receiptHeader, scheme,
				subscheme);
		receiptHeader.setReceiptMisc(receiptMisc);

		totalAmountToBeCollected = BigDecimal.valueOf(0);
		int m = 0;
		BigDecimal debitamount = BigDecimal.ZERO;
		removeEmptyRows(billCreditDetailslist);
		removeEmptyRows(billRebateDetailslist);
		removeEmptyRows(subLedgerlist);
		if (validateData(billCreditDetailslist, subLedgerlist)) {

			for (ReceiptDetailInfo voucherDetails : billCreditDetailslist) {
				CChartOfAccounts account = commonsService.getCChartOfAccountsByGlCode(voucherDetails.getGlcodeDetail());
				CFunction function = null;
				if (voucherDetails.getFunctionIdDetail() != null) {
					function = commonsService.getFunctionById(voucherDetails.getFunctionIdDetail());
				}
				ReceiptDetail receiptDetail = new ReceiptDetail(account, function,
						voucherDetails.getCreditAmountDetail(), voucherDetails.getDebitAmountDetail(), BigDecimal.ZERO,
						Long.valueOf(m), null, Long.valueOf(1), receiptHeader);

				if (voucherDetails.getCreditAmountDetail() == null) {
					receiptDetail.setCramount(BigDecimal.ZERO);
				} else {
					receiptDetail.setCramount(voucherDetails.getCreditAmountDetail());
				}

				if (voucherDetails.getDebitAmountDetail() == null) {
					receiptDetail.setDramount(BigDecimal.ZERO);
				} else {
					receiptDetail.setDramount(voucherDetails.getDebitAmountDetail());
				}

				receiptDetail = setAccountPayeeDetails(subLedgerlist, receiptDetail);
				receiptHeader.addReceiptDetail(receiptDetail);
				debitamount = debitamount.add(voucherDetails.getCreditAmountDetail());
				debitamount = debitamount.subtract(voucherDetails.getDebitAmountDetail());
				m++;
			}
		} else {
			return false;
		}
		if (validateRebateData(billRebateDetailslist, subLedgerlist)) {
			for (ReceiptDetailInfo voucherDetails : billRebateDetailslist) {

				if (voucherDetails.getGlcodeDetail() != null
						&& StringUtils.isNotBlank(voucherDetails.getGlcodeDetail())) {
					CChartOfAccounts account = commonsService.getCChartOfAccountsByGlCode(voucherDetails
							.getGlcodeDetail());
					CFunction function = null;
					if (voucherDetails.getFunctionIdDetail() != null) {
						function = commonsService.getFunctionById(voucherDetails.getFunctionIdDetail());
					}
					ReceiptDetail receiptDetail = new ReceiptDetail(account, function,
							voucherDetails.getCreditAmountDetail(), voucherDetails.getDebitAmountDetail(),
							BigDecimal.ZERO, Long.valueOf(m), null, Long.valueOf(1), receiptHeader);

					if (voucherDetails.getDebitAmountDetail() == null) {
						receiptDetail.setDramount(BigDecimal.ZERO);
					} else {
						receiptDetail.setDramount(voucherDetails.getDebitAmountDetail());
					}
					if (voucherDetails.getCreditAmountDetail() == null) {
						receiptDetail.setCramount(BigDecimal.ZERO);
					} else {
						receiptDetail.setCramount(voucherDetails.getCreditAmountDetail());
					}

					receiptDetail = setAccountPayeeDetails(subLedgerlist, receiptDetail);
					receiptHeader.addReceiptDetail(receiptDetail);
					debitamount = debitamount.add(voucherDetails.getCreditAmountDetail());
					debitamount = debitamount.subtract(voucherDetails.getDebitAmountDetail());
					m++;
				}
			}
		} else {
			return false;
		}
		setTotalDebitAmount(debitamount);
		receiptHeader.setReceiptPayeeDetails(receiptPayee);
		receiptPayee.addReceiptHeader(receiptHeader);
		modelPayeeList.add(receiptPayee);
		return true;
	}

	public ReceiptDetail setAccountPayeeDetails(List<ReceiptDetailInfo> subLedgerlist, ReceiptDetail receiptDetail) {
		for (ReceiptDetailInfo subvoucherDetails : subLedgerlist) {

			if (subvoucherDetails.getGlcode() != null && subvoucherDetails.getGlcode().getId() != 0
					&& subvoucherDetails.getGlcode().getId().equals(receiptDetail.getAccounthead().getId())) {

				Accountdetailtype accdetailtype = (Accountdetailtype) getPersistenceService().findByNamedQuery(
						CollectionConstants.QUERY_ACCOUNTDETAILTYPE_BY_ID, subvoucherDetails.getDetailType().getId());
				Accountdetailkey accdetailkey = (Accountdetailkey) getPersistenceService().findByNamedQuery(
						CollectionConstants.QUERY_ACCOUNTDETAILKEY_BY_DETAILKEY, subvoucherDetails.getDetailKeyId(),
						subvoucherDetails.getDetailType().getId());

				AccountPayeeDetail accPayeeDetail = new AccountPayeeDetail(accdetailtype, accdetailkey,
						subvoucherDetails.getAmount(), receiptDetail);

				receiptDetail.addAccountPayeeDetail(accPayeeDetail);
			}
		}
		return receiptDetail;
	}

	public String newform() {
		String manualReceiptInfoRequired = collectionsUtil.getAppConfigValue(
				CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG, CollectionConstants.MANUALRECEIPTINFOREQUIRED);
		if (CollectionConstants.YES.equalsIgnoreCase(manualReceiptInfoRequired)) {
			setManualReceiptNumberAndDateReq(Boolean.TRUE);
		}
		String[] receiptType = parameters.get("Receipt");
		if (receiptType != null && receiptType[0].equalsIgnoreCase("Misc")) {
			createMisc();
		}
		// set collection modes allowed rule through script
		setCollectionModesNotAllowed();

		return NEW;
	}

	/**
	 * This method is invoked when user creates a receipt.
	 * 
	 * @return
	 */
	@ValidationErrorPage(value = "new")
	public String save() {
		LOGGER.info("Receipt creation process is started !!!!!!");
		long startTimeMillis = System.currentTimeMillis();

		// For interday cancellation
		if (oldReceiptId != null) {
			recreateNewReceiptOnCancellation();
		}

		if (billSource.equalsIgnoreCase("misc")) {
			createMisc();
			if (!setMiscReceiptDetails()) {
				return NEW;
			}
		} else {
			long callBackURLStartedinMillis = System.currentTimeMillis();
			if (callbackForApportioning && !overrideAccountHeads) {
				this.apportionBillAmount();
				for (ReceiptPayeeDetails payee : modelPayeeList) {
					for (ReceiptHeader receiptHeader : payee.getReceiptHeaders()) {
						receiptHeader.setReceiptDetails(new HashSet(receiptDetailList));
					}
				}
			}
			LOGGER.info("Call back for apportioning is completed in :"
					+ (System.currentTimeMillis() - callBackURLStartedinMillis) + "  milliseconds");
		}

		// initialise receipt info,persist receipts,create vouchers & update
		// billing system
		populateAndPersistReceipts();

		ReceiptHeader rh = modelPayeeList.get(0).getReceiptHeaders().iterator().next();
		collectionsUtil.auditEventForReceiptEntity(rh, RCPT_CREATE);
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.info("$$$$$$ Receipt Persisted with Receipt Number: " + rh.getReceiptnumber()
				+ (rh.getConsumerCode() != null ? " and consumer code: " + rh.getConsumerCode() : "")
				+ "; Time taken(ms) = " + elapsedTimeMillis);
		// Do not invoke print receipt in case of bulk upload.
		if (!receiptBulkUpload) {
			return printReceipts();
		} else {
			return SUCCESS;
		}
	}

	public void createMisc() {
		headerFields = new ArrayList<String>();
		mandatoryFields = new ArrayList<String>();
		getHeaderMandateFields();
		setupDropdownDataExcluding();

		if (headerFields.contains(CollectionConstants.DEPARTMENT)) {
			addDropdownData("departmentList",
					persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_DEPARTMENTS));
		}
		if (headerFields.contains(CollectionConstants.FUNCTIONARY)) {
			addDropdownData("functionaryList",
					persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUNCTIONARY));
		}
		if (headerFields.contains(CollectionConstants.FUND)) {
			addDropdownData("fundList", persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUND));
		}
		if (headerFields.contains(CollectionConstants.FIELD)) {
			addDropdownData("fieldList", persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FIELD));
		}
		if (headerFields.contains(CollectionConstants.FUNDSOURCE)) {
			addDropdownData("fundsourceList",
					persistenceService.findAllByNamedQuery(CollectionConstants.QUERY_ALL_FUNDSOURCE));
		}
		if (headerFields.contains(CollectionConstants.SCHEME)) {
			if (receiptMisc.getFund() == null || receiptMisc.getFund().getId() == null) {
				addDropdownData("schemeList", Collections.EMPTY_LIST);
			} else {
				addDropdownData("schemeList", persistenceService.findAllByNamedQuery(
						CollectionConstants.QUERY_SCHEME_BY_FUNDID, receiptMisc.getFund().getId()));
			}
		}
		if (headerFields.contains(CollectionConstants.SUBSCHEME)) {
			if (receiptMisc.getScheme() == null || receiptMisc.getScheme().getId() == null) {
				addDropdownData("subschemeList", Collections.EMPTY_LIST);
			} else {
				addDropdownData("subschemeList", persistenceService.findAllByNamedQuery(
						CollectionConstants.QUERY_SUBSCHEME_BY_SCHEMEID, receiptMisc.getScheme().getId()));
			}
		}

		if (billCreditDetailslist == null) {
			billCreditDetailslist = new ArrayList<ReceiptDetailInfo>();
			billRebateDetailslist = new ArrayList<ReceiptDetailInfo>();
			subLedgerlist = new ArrayList<ReceiptDetailInfo>();
			billRebateDetailslist.add(new ReceiptDetailInfo());
			billCreditDetailslist.add(new ReceiptDetailInfo());
			subLedgerlist.add(new ReceiptDetailInfo());
		}
		this.billSource = "misc";
		this.partPaymentAllowed = false;
		setHeaderFields(headerFields);
		setMandatoryFields(mandatoryFields);

		if (paidBy == null || CollectionConstants.BLANK.equals(paidBy)) {
			this.paidBy = collectionsUtil.getAppConfigValue(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
					CollectionConstants.APPCONFIG_VALUE_PAYEEFORMISCRECEIPTS);
		}
		// this.paidBy = payeename;
		if (null != service && null != service.getId() && service.getId() != -1) {
			setServiceName(serviceDetailsService.findById(service.getId(), false).getServiceName());

		} else {
			ServiceDetails service = (ServiceDetails) getPersistenceService().findByNamedQuery(
					CollectionConstants.QUERY_SERVICE_BY_CODE, CollectionConstants.SERVICE_CODE_COLLECTIONS);
			setServiceName(service.getServiceName());
		}

		Department dept = collectionsUtil.getDepartmentOfLoggedInUser(getSession());
		if (getDeptId() == null) {
			setDeptId(dept.getId().toString());
		}
		populateBankBranchList(false);
	}

	public boolean isBillSourcemisc() {
		boolean flag = false;
		if (getBillSource().equalsIgnoreCase("misc")) {
			flag = true;
		}
		return flag;
	}

	public boolean isFieldMandatory(String field) {
		return mandatoryFields.contains(field);
	}

	public boolean shouldShowHeaderField(String field) {
		return headerFields.contains(field);
	}

	protected void getHeaderMandateFields() {
		List<AppConfigValues> appConfigValuesList = collectionsUtil.getAppConfigValues(
				CollectionConstants.MISMandatoryAttributesModule, CollectionConstants.MISMandatoryAttributesKey);

		for (AppConfigValues appConfigVal : appConfigValuesList) {
			String value = appConfigVal.getValue();
			String header = value.substring(0, value.indexOf('|'));
			headerFields.add(header);
			String mandate = value.substring(value.indexOf('|') + 1);
			if (mandate.equalsIgnoreCase("M")) {
				mandatoryFields.add(header);
			}
		}
		if (!"Auto".equalsIgnoreCase(new VoucherTypeForULB().readVoucherTypes("Receipt"))) {
			headerFields.add("vouchernumber");
			mandatoryFields.add("vouchernumber");
		}
		mandatoryFields.add("voucherdate");
	}

	/**
	 * This method performs the following for receipts to be newly created:
	 * <ol>
	 * <li>The user instrument header details, and actual amount paid by user is
	 * captured.</li>
	 * <li>A debit receipt detail account head is created for the total credit
	 * collected.</li>
	 * <li>Vouchers are created</li>
	 * </ol>
	 * 
	 * <p>
	 * The receipts are persisted and work flow is started for these persisted
	 * receipts where in the receipt state is set to NEW
	 * 
	 * The billing system is updated about the persisted receipts. These include
	 * details of both newly created as well as cancelled receipts.
	 * 
	 * If the instrument list and voucher list are not empty, the .... is
	 * updated
	 * 
	 * The receipt ids of the newly created receipts are collectively populated
	 * to be shown on the print screen
	 * 
	 */
	private void populateAndPersistReceipts() {
		List<InstrumentHeader> receiptInstrList = new ArrayList<InstrumentHeader>();
		int noOfNewlyCreatedReceipts = 0;
		boolean setInstrument = true;
		String serviceType = "";

		for (ReceiptPayeeDetails payee : modelPayeeList) {
			for (ReceiptHeader receiptHeader : payee.getReceiptHeaders()) {

				// only newly created receipts need to be initialised with the
				// data.
				// The cancelled receipt can be excluded from this processing.
				if (receiptHeader.getStatus() == null) {
					noOfNewlyCreatedReceipts++;
					// Set created by Date as this required to generate receipt
					// number before persist
					if (manualReceiptDate == null) {
						receiptHeader.setCreatedDate(new Date());

					} else {
						// If the receipt has been manually created, the receipt
						// date is same as the date of manual creation.
						receiptHeader.setCreatedDate(manualReceiptDate);
						// set Createdby, in MySavelistner if createdBy is null
						// it set both createdBy and createdDate with
						// currentDate.
						// Thus overridding the manualReceiptDate set above
						receiptHeader.setCreatedBy(collectionsUtil.getLoggedInUser(getSession()));
						receiptHeader.setManualreceiptdate(manualReceiptDate);
						receiptHeader.setVoucherDate(manualReceiptDate);
					}
					if (manualReceiptNumber != null) {
						receiptHeader.setManualreceiptnumber(manualReceiptNumber);
					}
					if (isBillSourcemisc()) {
						receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_ADHOC);
						receiptHeader.setVoucherDate(voucherDate);
						receiptHeader.setVoucherNum(voucherNum);
						receiptHeader.setIsReconciled(Boolean.TRUE);
						receiptHeader.setManualreceiptdate(manualReceiptDate);

					} else {
						receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
						receiptHeader.setIsModifiable(Boolean.TRUE);
						receiptHeader.setIsReconciled(Boolean.FALSE);
					}
					serviceType = receiptHeader.getService().getServiceType();
					receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);
					receiptHeader.setLocation(collectionsUtil.getLocationOfUser(getSession()));
					receiptHeader.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
							CollectionConstants.MODULE_NAME_RECEIPTHEADER,
							CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED));
					receiptHeader.setPaidBy(paidBy);

					// If this is a new receipt in lieu of cancelling old
					// receipt, update
					// old receipt id to the reference collection header id
					// field of this new receipt.
					if (this.getOldReceiptId() != null) {
						receiptHeader.setReceiptHeader(receiptHeaderService.findById(getOldReceiptId(), false));

					}
					if (setInstrument) {
						receiptInstrList = populateInstrumentDetails();
						setInstrument = false;
					}

					receiptHeader.setReceiptInstrument(new HashSet(receiptInstrList));

					BigDecimal debitAmount = BigDecimal.ZERO;

					for (ReceiptDetail creditChangeReceiptDetail : receiptDetailList) {
						for (ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
							if (creditChangeReceiptDetail.getReceiptHeader().getReferencenumber()
									.equals(receiptDetail.getReceiptHeader().getReferencenumber())
									&& receiptDetail.getOrdernumber()
											.equals(creditChangeReceiptDetail.getOrdernumber())) {

								receiptDetail.setCramount(creditChangeReceiptDetail.getCramount());
								receiptDetail.setDramount(creditChangeReceiptDetail.getDramount());
								// calculate sum of creditamounts as a debit
								// value to create a
								// debit account head and add to receipt details
								debitAmount = debitAmount.add(creditChangeReceiptDetail.getCramount());
								debitAmount = debitAmount.subtract(creditChangeReceiptDetail.getDramount());
							}
						}
					}// end of outer for loop

					if (chequeInstrumenttotal != null && chequeInstrumenttotal.compareTo(BigDecimal.ZERO) != 0) {
						receiptHeader.setTotalAmount(chequeInstrumenttotal);
					}

					if (cashOrCardInstrumenttotal != null && cashOrCardInstrumenttotal.compareTo(BigDecimal.ZERO) != 0) {
						receiptHeader.setTotalAmount(cashOrCardInstrumenttotal);
					}
					if (isBillSourcemisc()) {
						receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(totalDebitAmount,
								receiptHeader, chequeInstrumenttotal, cashOrCardInstrumenttotal,
								instrumentTypeCashOrCard));
					} else {
						receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount,
								receiptHeader, chequeInstrumenttotal, cashOrCardInstrumenttotal,
								instrumentTypeCashOrCard));
					}

				}
			}// end of looping through receipt headers
		}// end of looping through model receipt payee list

		// Persist the receipt payee details which will internally persist all
		// the receipt headers
		List<ReceiptPayeeDetails> receiptPayeePersistedList = receiptPayeeDetailsService
				.persist(new HashSet<ReceiptPayeeDetails>(modelPayeeList));

		receiptPayeeDetailsService.getSession().flush();

		LOGGER.info("Persisted receipts");

		// Start work flow for all newly created receipts This might internally
		// create vouchers also based on configuration
		for (ReceiptPayeeDetails payeeDetails : receiptPayeePersistedList) {
			receiptHeaderService.startWorkflow(payeeDetails.getReceiptHeaders(), this.getReceiptBulkUpload());
		}
		receiptHeaderService.getSession().flush();
		LOGGER.info("Workflow started for newly created receipts");

		if (serviceType.equalsIgnoreCase(CollectionConstants.SERVICE_TYPE_BILLING)) {
			if (!receiptBulkUpload) {
				for (ReceiptPayeeDetails payeeDetails : receiptPayeePersistedList) {
					collectionCommon.updateBillingSystemWithReceiptInfo(payeeDetails);
				}
			}
			LOGGER.info("Updated billing system ");
		}

		List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();
		Set<ReceiptVoucher> receiptVouchers = new HashSet<ReceiptVoucher>();
		for (ReceiptPayeeDetails payeeDetails : receiptPayeePersistedList) {
			for (ReceiptHeader receiptHeader : payeeDetails.getReceiptHeaders()) {
				// If vouchers are created during work flow step, add them to
				// the list
				receiptVouchers = receiptHeader.getReceiptVoucher();
				for (ReceiptVoucher receiptVoucher : receiptVouchers) {
					try {
						voucherHeaderList.add(receiptVoucher.getVoucherheader());
					} catch (Exception e) {
						LOGGER.error("Error in getting voucher header for id [" + receiptVoucher.getVoucherheader()
								+ "]", e);
					}
				}
			}
		}
		// populate all receipt header ids except the cancelled receipt
		// (in effect the newly created receipts)
		selectedReceipts = new Long[noOfNewlyCreatedReceipts];
		for (ReceiptPayeeDetails payeeDetails : receiptPayeePersistedList) {
			int i = 0;
			for (ReceiptHeader header : payeeDetails.getReceiptHeaders()) {
				if (!(header.getId().equals(oldReceiptId))) {
					selectedReceipts[i] = header.getId();
					i++;
				}
			}
		}

		if (voucherHeaderList != null && receiptInstrList != null) {
			receiptPayeeDetailsService.updateInstrument(voucherHeaderList, receiptInstrList);
		}

	}// end of method

	private List<InstrumentHeader> populateInstrumentDetails() {
		List<InstrumentHeader> instrumentHeaderList = new ArrayList<InstrumentHeader>();

		if (CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrumentTypeCashOrCard)) {
			instrHeaderCash.setInstrumentType(financialsUtil
					.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CASH));

			instrHeaderCash.setIsPayCheque(CollectionConstants.ZERO_INT);
			// the cash amount is set into the object through binding
			// this total is needed for creating debit account head

			cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderCash.getInstrumentAmount());

			instrumentHeaderList.add(instrHeaderCash);
		}
		if (CollectionConstants.INSTRUMENTTYPE_CARD.equals(instrumentTypeCashOrCard)) {
			instrHeaderCard.setInstrumentType(financialsUtil
					.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CARD));
			if (instrHeaderCard.getTransactionDate() == null) {
				instrHeaderCard.setTransactionDate(new Date());
			}
			instrHeaderCard.setIsPayCheque(CollectionConstants.ZERO_INT);

			// the instrumentNumber, transactionNumber, instrumentAmount are
			// set into the object directly through binding
			cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderCard.getInstrumentAmount());

			instrumentHeaderList.add(instrHeaderCard);
		}

		if (CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrumentTypeCashOrCard)) {
			instrHeaderBank.setInstrumentType(financialsUtil
					.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_BANK));
			if (instrHeaderBank.getTransactionDate() == null) {
				instrHeaderBank.setTransactionDate(new Date());
			}
			instrHeaderBank.setIsPayCheque(CollectionConstants.ZERO_INT);

			Bankaccount account = commonsService.getBankaccountById(bankAccountId);

			instrHeaderBank.setBankAccountId(account);
			instrHeaderBank.setBankBranchName(account.getBankbranch().getBranchname());

			// the instrumentNumber, transactionNumber, instrumentAmount are
			// set into the object directly through binding
			cashOrCardInstrumenttotal = cashOrCardInstrumenttotal.add(instrHeaderBank.getInstrumentAmount());

			instrumentHeaderList.add(instrHeaderBank);
		}

		// cheque/DD types
		if (instrumentProxyList != null) {
			if (instrumentProxyList.get(0).getInstrumentType().getType()
					.equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
					|| instrumentProxyList.get(0).getInstrumentType().getType()
							.equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
				instrumentHeaderList = populateInstrumentHeaderForChequeDD(instrumentHeaderList, instrumentProxyList);
			}
		}
		instrumentHeaderList = receiptPayeeDetailsService.createInstrument(instrumentHeaderList);
		return instrumentHeaderList;
	}

	/**
	 * This instrument creates instrument header instances for the receipt, when
	 * the instrument type is Cheque or DD. The created
	 * <code>InstrumentHeader</code> instance is persisted
	 * 
	 * @param k
	 *            an int value representing the index of the instrument type as
	 *            chosen from the front end
	 * 
	 * @return an <code>InstrumentHeader</code> instance populated with the
	 *         instrument details
	 */
	private List<InstrumentHeader> populateInstrumentHeaderForChequeDD(List<InstrumentHeader> instrumentHeaderList,
			List<InstrumentHeader> instrumentProxyList) {

		for (InstrumentHeader instrumentHeader : instrumentProxyList) {
			if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)) {
				instrumentHeader.setInstrumentType(financialsUtil
						.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_CHEQUE));
			} else if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
				instrumentHeader.setInstrumentType(financialsUtil
						.getInstrumentTypeByType(CollectionConstants.INSTRUMENTTYPE_DD));
			}
			if (instrumentHeader.getBankId() != null) {
				instrumentHeader.setBankId(commonsService.getBankById(Integer.valueOf(instrumentHeader.getBankId()
						.getId())));
			}
			chequeInstrumenttotal = chequeInstrumenttotal.add(instrumentHeader.getInstrumentAmount());
			instrumentHeader.setIsPayCheque(CollectionConstants.ZERO_INT);
			instrumentHeaderList.add(instrumentHeader);
		}
		return instrumentHeaderList;
	}

	/**
	 * This method sets the status of the receipt to be cancelled as CANCELLED
	 * and persists it A new receipt header object is populated with the data
	 * contained in the cancelled receipt. This instance is added to the model.
	 * 
	 * @param receiptHeaderToBeCancelled
	 */
	private void recreateNewReceiptOnCancellation() {
		ReceiptHeader receiptHeaderToBeCancelled = receiptHeaderService.findById(oldReceiptId, false);

		receiptHeaderToBeCancelled.setStatus(commonsService.getStatusByModuleAndCode(
				CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));
		receiptHeaderToBeCancelled.setReasonForCancellation(reasonForCancellation);
		// set isReconciled to false before calling update to billing system for
		// cancel receipt
		receiptHeaderToBeCancelled.setIsReconciled(false);

		receiptHeaderService.persist(receiptHeaderToBeCancelled);

		if (receiptHeaderToBeCancelled.getReceipttype() == CollectionConstants.RECEIPT_TYPE_BILL) {
			// In case of post remittance cancellation,update billing system for
			// cancelled receipt
			collectionCommon.updateBillingSystemWithReceiptInfo(receiptHeaderToBeCancelled.getReceiptPayeeDetails());
			populateReceiptModelWithExistingReceiptInfo(receiptHeaderToBeCancelled);
			LOGGER.info("Receipt Cancelled with Receipt Number(recreateNewReceiptOnCancellation): "
					+ receiptHeaderToBeCancelled.getReceiptnumber() + "; Consumer Code: "
					+ receiptHeaderToBeCancelled.getConsumerCode());
		}
	}

	/**
	 * This method create a new receipt header object with details contained in
	 * given receipt header object.
	 * 
	 * Both the receipt header objects are added to the same parent
	 * <code>ReceiptPayeeDetail</code> object which in turn is added to the
	 * model.
	 * 
	 * @param oldReceiptHeader
	 *            the instance of <code>ReceiptHeader</code> whose data is to be
	 *            copied
	 */
	private void populateReceiptModelWithExistingReceiptInfo(ReceiptHeader oldReceiptHeader) {
		modelPayeeList.clear();

		ReceiptPayeeDetails payee = new ReceiptPayeeDetails(oldReceiptHeader.getReceiptPayeeDetails().getPayeename(),
				oldReceiptHeader.getReceiptPayeeDetails().getPayeeAddress());
		totalAmountToBeCollected = BigDecimal.valueOf(0);
		ReceiptHeader receiptHeader = new ReceiptHeader(oldReceiptHeader.getReferencenumber(),
				oldReceiptHeader.getReferencedate(), oldReceiptHeader.getConsumerCode(),
				oldReceiptHeader.getReferenceDesc(), oldReceiptHeader.getTotalAmount(),
				oldReceiptHeader.getMinimumAmount(), oldReceiptHeader.getPartPaymentAllowed(),
				oldReceiptHeader.getOverrideAccountHeads(), oldReceiptHeader.getCallbackForApportioning(),
				oldReceiptHeader.getDisplayMsg(), oldReceiptHeader.getService(),
				oldReceiptHeader.getCollModesNotAllwd());
		if (oldReceiptHeader.getCollModesNotAllwd() != null)
			setCollectionModesNotAllowed(Arrays.asList(oldReceiptHeader.getCollModesNotAllwd().split(",")));
		setOverrideAccountHeads(oldReceiptHeader.getOverrideAccountHeads());
		setPartPaymentAllowed(oldReceiptHeader.getPartPaymentAllowed());
		setServiceName(oldReceiptHeader.getService().getServiceName());

		ReceiptMisc receiptMisc = new ReceiptMisc(oldReceiptHeader.getReceiptMisc().getBoundary(), oldReceiptHeader
				.getReceiptMisc().getFund(), oldReceiptHeader.getReceiptMisc().getIdFunctionary(), oldReceiptHeader
				.getReceiptMisc().getFundsource(), oldReceiptHeader.getReceiptMisc().getDepartment(), receiptHeader,
				oldReceiptHeader.getReceiptMisc().getScheme(), oldReceiptHeader.getReceiptMisc().getSubscheme());
		receiptHeader.setReceiptMisc(receiptMisc);
		
		for (ReceiptDetail oldDetail : oldReceiptHeader.getReceiptDetails()) {
			// debit account heads for revenue accounts should not be considered
			if (oldDetail.getOrdernumber() != null && !financialsUtil.isRevenueAccountHead(oldDetail.getAccounthead(),bankCOAList)) {
				ReceiptDetail receiptDetail = new ReceiptDetail(oldDetail.getAccounthead(), oldDetail.getFunction(),
						oldDetail.getCramount(), oldDetail.getDramount(), oldDetail.getCramount(),
						oldDetail.getOrdernumber(), oldDetail.getDescription(), oldDetail.getIsActualDemand(),
						receiptHeader);
				receiptDetail.setCramountToBePaid(oldDetail.getCramountToBePaid());
				receiptDetail.setCramount(oldDetail.getCramount());
				if (oldDetail.getAccountPayeeDetails() != null) {
					for (AccountPayeeDetail oldAccountPayeeDetail : oldDetail.getAccountPayeeDetails()) {
						AccountPayeeDetail accountPayeeDetail = new AccountPayeeDetail(
								oldAccountPayeeDetail.getAccountDetailType(),
								oldAccountPayeeDetail.getAccountDetailKey(), oldAccountPayeeDetail.getAmount(),
								receiptDetail);
						receiptDetail.addAccountPayeeDetail(accountPayeeDetail);
					}
				}

				if (oldDetail.getIsActualDemand() == 1) {
					totalAmountToBeCollected = totalAmountToBeCollected.add(oldDetail.getCramountToBePaid())
							.subtract(oldDetail.getDramount())
							.setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP);
				}

				receiptHeader.addReceiptDetail(receiptDetail);
			}
		}

		if (oldReceiptHeader.getReceipttype() == CollectionConstants.RECEIPT_TYPE_ADHOC) {
			loadReceiptDetails(receiptHeader);
			createMisc();
			if (oldReceiptHeader.getVoucherNum() != null) {
				setVoucherNum(voucherNum);
			}
		}

		receiptHeader.setReceiptPayeeDetails(payee);
		payee.addReceiptHeader(receiptHeader);
		this.setReceiptDetailList(new ArrayList<ReceiptDetail>(receiptHeader.getReceiptDetails()));

		modelPayeeList.add(payee);
	}

	private void loadReceiptDetails(ReceiptHeader receiptHeader) {
		setReceiptMisc(receiptHeader.getReceiptMisc());
		setBillCreditDetailslist(collectionCommon.setReceiptDetailsList(receiptHeader,
				CollectionConstants.COLLECTIONSAMOUNTTPE_CREDIT));
		setBillRebateDetailslist(collectionCommon.setReceiptDetailsList(receiptHeader,
				CollectionConstants.COLLECTIONSAMOUNTTPE_DEBIT));
		setSubLedgerlist(collectionCommon.setAccountPayeeList(receiptHeader));
	}

	/**
	 * Same method handles both view and print modes. If print receipts flag is
	 * passed as true, the PDF receipt will be generated in such a way that it
	 * will show the print dialog box whenever it is opened.
	 * 
	 * @param printReceipts
	 *            Flag indicating whether the receipts are to be printed
	 * @return Result page ("view")
	 */
	private String viewReceipts(boolean printReceipts) {
		if (selectedReceipts == null || selectedReceipts.length == 0) {
			throw new EGOVRuntimeException("No receipts selected to view!");
		}

		receipts = new ReceiptHeader[selectedReceipts.length];
		for (int i = 0; i < selectedReceipts.length; i++) {
			try {
				receipts[i] = receiptHeaderService.findById(selectedReceipts[i], false);
			} catch (Exception e) {
				LOGGER.error("Error in printReceipts", e);
			}
		}

		try {
			reportId = collectionCommon.generateReport(receipts, getSession(), printReceipts);
		} catch (Exception e) {
			String errMsg = "Error during report generation!";
			LOGGER.error(errMsg, e);
			throw new EGOVRuntimeException(errMsg, e);
		}

		return CollectionConstants.REPORT;
	}

	public String viewReceipts() {
		return viewReceipts(false);
	}

	public String printReceipts() {
		return viewReceipts(true);
	}

	@ValidationErrorPage(value = "error")
	public String cancel() {
		if (getSelectedReceipts() != null && getSelectedReceipts().length > 0) {
			receipts = new ReceiptHeader[selectedReceipts.length];
			for (int i = 0; i < selectedReceipts.length; i++) {
				receipts[i] = (ReceiptHeader) getPersistenceService().findByNamedQuery("getReceiptHeaderById",
						Long.valueOf(selectedReceipts[i]));

			}
		}
		return CANCEL;
	}

	/**
	 * This method is invoked when receipt is cancelled
	 * 
	 * @return
	 */
	public String saveOnCancel() {
		String instrumentType = "";
		boolean isInstrumentDeposited = false;

		ReceiptHeader receiptHeaderToBeCancelled = receiptHeaderService.findById(oldReceiptId, false);

		LOGGER.info("Receipt Header to be Cancelled : " + receiptHeaderToBeCancelled.getReceiptnumber());

		for (InstrumentHeader instrumentHeader : receiptHeaderToBeCancelled.getReceiptInstrument()) {
			if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
				if (instrumentHeader.getStatusId().getDescription()
						.equals(CollectionConstants.INSTRUMENT_RECONCILED_STATUS)) {
					isInstrumentDeposited = true;
					break;
				}
			} else {
				if (instrumentHeader.getStatusId().getDescription()
						.equals(CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)) {
					isInstrumentDeposited = true;
					break;
				}
			}
		}

		if (isInstrumentDeposited) {
			// if instrument has been deposited create a new receipt in place of
			// the cancelled

			populateReceiptModelWithExistingReceiptInfo(receiptHeaderToBeCancelled);
			setFundName(receiptHeaderToBeCancelled.getReceiptMisc().getFund().getName());
			setServiceName(receiptHeaderToBeCancelled.getService().getServiceName());
			populateBankBranchList(true);

			// set collection modes allowed rule through script
			setCollectionModesNotAllowed();

			return NEW;
		} else {
			// if instrument has not been deposited, cancel the old instrument,
			// reverse the
			// voucher and persist
			receiptHeaderToBeCancelled.setStatus(commonsService.getStatusByModuleAndCode(
					CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));
			receiptHeaderToBeCancelled.setIsReconciled(false);
			receiptHeaderToBeCancelled.setReasonForCancellation(reasonForCancellation);

			for (InstrumentHeader instrumentHeader : receiptHeaderToBeCancelled.getReceiptInstrument()) {
				instrumentHeader.setStatusId(commonsService.getStatusByModuleAndCode(
						CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
						CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED));
				instrumentType = instrumentHeader.getInstrumentType().getType();

			}
			for (ReceiptVoucher receiptVoucher : receiptHeaderToBeCancelled.getReceiptVoucher()) {
				receiptPayeeDetailsService.createReversalVoucher(receiptVoucher, instrumentType);
			}

			receiptPayeeDetailsService.persist(receiptHeaderToBeCancelled.getReceiptPayeeDetails());

			// End work-flow for the cancelled receipt
			if (!receiptHeaderToBeCancelled.getState().getValue().equals(CollectionConstants.WF_STATE_END)) {
				receiptHeaderService.endReceiptWorkFlowOnCancellation(receiptHeaderToBeCancelled);
			}

			// Update Billing System regarding cancellation of the existing
			// receipt(when the instrument is not deposited)
			collectionCommon.updateBillingSystemWithReceiptInfo(receiptHeaderToBeCancelled.getReceiptPayeeDetails());

			receiptHeaderValues.clear();
			receiptHeaderValues.add(receiptHeaderToBeCancelled);
			LOGGER.info("Receipt Cancelled with Receipt Number(saveOnCancel): "
					+ receiptHeaderToBeCancelled.getReceiptnumber() + "; Consumer Code: "
					+ receiptHeaderToBeCancelled.getConsumerCode());
		}
		collectionsUtil.auditEventForReceiptEntity(receiptHeaderToBeCancelled, RCPT_CANCEL);
		target = "cancel";
		return INDEX;
	}

	public String amountInWords(BigDecimal amount) {
		return NumberUtil.amountInWords(amount);
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	/**
	 * @param receiptPayeeDetailsService
	 *            the receiptPayeeDetailsService to set
	 */
	public void setReceiptPayeeDetailsService(ReceiptService receiptPayeeDetailsService) {
		this.receiptPayeeDetailsService = receiptPayeeDetailsService;
	}

	/**
	 * @return the receiptHeaderValues
	 */
	public List<ReceiptHeader> getReceiptHeaderValues() {
		return receiptHeaderValues;
	}

	/**
	 * @param receiptHeaderValues
	 *            the receiptHeaderValues to set
	 */
	public void setReceiptHeaderValues(List<ReceiptHeader> receiptHeaderValues) {
		this.receiptHeaderValues = receiptHeaderValues;
	}

	public String getReasonForCancellation() {
		return reasonForCancellation;
	}

	public void setReasonForCancellation(String reasonForCancellation) {
		this.reasonForCancellation = reasonForCancellation;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the paidBy
	 */
	public String getPaidBy() {
		return paidBy;
	}

	/**
	 * @param paidBy
	 *            the paidBy to set
	 */
	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}

	/**
	 * @return the oldReceiptId
	 */
	public Long getOldReceiptId() {
		return oldReceiptId;
	}

	/**
	 * @param oldReceiptId
	 *            the oldReceiptId to set
	 */
	public void setOldReceiptId(Long oldReceiptId) {
		this.oldReceiptId = oldReceiptId;
	}

	public Integer getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(Integer bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public BigDecimal getTotalAmountToBeCollected() {
		return totalAmountToBeCollected;
	}

	public void setTotalAmountToBeCollected(BigDecimal totalAmountToBeCollected) {
		this.totalAmountToBeCollected = totalAmountToBeCollected;
	}

	public InstrumentHeader getInstrHeaderCash() {
		return instrHeaderCash;
	}

	public void setInstrHeaderCash(InstrumentHeader instrHeaderCash) {
		this.instrHeaderCash = instrHeaderCash;
	}

	public InstrumentHeader getInstrHeaderCard() {
		return instrHeaderCard;
	}

	public void setInstrHeaderCard(InstrumentHeader instrHeaderCard) {
		this.instrHeaderCard = instrHeaderCard;
	}

	public InstrumentHeader getInstrHeaderBank() {
		return instrHeaderBank;
	}

	public void setInstrHeaderBank(InstrumentHeader instrHeaderBank) {
		this.instrHeaderBank = instrHeaderBank;
	}

	public List<String> getCollectionModesNotAllowed() {
		return collectionModesNotAllowed;
	}

	public void setCollectionModesNotAllowed(List<String> collectionModesNotAllowed) {
		this.collectionModesNotAllowed = collectionModesNotAllowed;
	}

	public User getReceiptCreatedByCounterOperator() {
		return receiptCreatedByCounterOperator;
	}

	public void setReceiptCreatedByCounterOperator(User receiptCreatedByCounterOperator) {
		this.receiptCreatedByCounterOperator = receiptCreatedByCounterOperator;
	}

	public List<ReceiptPayeeDetails> getModelPayeeList() {
		return modelPayeeList;
	}

	public void setModelPayeeList(List<ReceiptPayeeDetails> modelPayeeList) {
		this.modelPayeeList = modelPayeeList;
	}

	public List<ReceiptDetail> getReceiptDetailList() {
		return receiptDetailList;
	}

	public void setReceiptDetailList(List<ReceiptDetail> receiptDetailList) {
		this.receiptDetailList = receiptDetailList;
	}

	public String getInstrumentTypeCashOrCard() {
		return instrumentTypeCashOrCard;
	}

	public void setInstrumentTypeCashOrCard(String instrumentTypeCashOrCard) {
		this.instrumentTypeCashOrCard = instrumentTypeCashOrCard;
	}

	/**
	 * This getter will be invoked by framework from UI. It returns the total
	 * number of bill accounts that are present in the XML arriving from the
	 * billing system
	 * 
	 * @return
	 */
	public Integer getTotalNoOfAccounts() {
		Integer totalNoOfAccounts = 0;
		for (ReceiptPayeeDetails payee : modelPayeeList) {
			for (ReceiptHeader header : payee.getReceiptHeaders()) {
				totalNoOfAccounts += header.getReceiptDetails().size();
			}
		}
		return totalNoOfAccounts;
	}

	/**
	 * This getter will be invoked by framework from UI. This value will be used
	 * during bill apportioning.
	 * 
	 * @return
	 */
	public BigDecimal getMinimumAmount() {
		return modelPayeeList.get(0).getReceiptHeaders().iterator().next().getMinimumAmount();
	}

	public Boolean getOverrideAccountHeads() {
		return overrideAccountHeads;
	}

	public void setOverrideAccountHeads(Boolean overrideAccountHeads) {
		this.overrideAccountHeads = overrideAccountHeads;
	}

	public Boolean getCallbackForApportioning() {
		return callbackForApportioning;
	}

	public void setCallbackForApportioning(Boolean callbackForApportioning) {
		this.callbackForApportioning = callbackForApportioning;
	}

	public Boolean getPartPaymentAllowed() {
		return partPaymentAllowed;
	}

	public void setPartPaymentAllowed(Boolean partPaymentAllowed) {
		this.partPaymentAllowed = partPaymentAllowed;
	}

	public BigDecimal getCashOrCardInstrumenttotal() {
		return cashOrCardInstrumenttotal;
	}

	public void setCashOrCardInstrumenttotal(BigDecimal cashOrCardInstrumenttotal) {
		this.cashOrCardInstrumenttotal = cashOrCardInstrumenttotal;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	public Boolean getCashAllowed() {
		return cashAllowed;
	}

	public void setCashAllowed(Boolean cashAllowed) {
		this.cashAllowed = cashAllowed;
	}

	public Boolean getCardAllowed() {
		return cardAllowed;
	}

	public void setCardAllowed(Boolean cardAllowed) {
		this.cardAllowed = cardAllowed;
	}

	public Boolean getChequeAllowed() {
		return chequeAllowed;
	}

	public void setChequeAllowed(Boolean chequeAllowed) {
		this.chequeAllowed = chequeAllowed;
	}

	public Boolean getDdAllowed() {
		return ddAllowed;
	}

	public void setDdAllowed(Boolean ddAllowed) {
		this.ddAllowed = ddAllowed;
	}

	public Boolean getBankAllowed() {
		return bankAllowed;
	}

	public void setBankAllowed(Boolean bankAllowed) {
		this.bankAllowed = bankAllowed;
	}

	/**
	 * @return the voucherDate
	 */
	public Date getVoucherDate() {
		return voucherDate;
	}

	/**
	 * @param voucherDate
	 *            the voucherDate to set
	 */
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	/**
	 * @return the voucherNumber
	 */
	public String getVoucherNum() {
		return voucherNum;
	}

	/**
	 * @param voucherNumber
	 *            the voucherNumber to set
	 */
	public void setVoucherNum(String voucherNum) {
		this.voucherNum = voucherNum;
	}

	/**
	 * This getter will be invoked by framework from UI. This value will be used
	 * during misc receipts for account details
	 * 
	 * @return
	 */
	public List<ReceiptDetailInfo> getBillCreditDetailslist() {
		return billCreditDetailslist;
	}

	public void setBillCreditDetailslist(List<ReceiptDetailInfo> billCreditDetailslist) {
		this.billCreditDetailslist = billCreditDetailslist;
	}

	public List<ReceiptDetailInfo> getBillRebateDetailslist() {
		return billRebateDetailslist;
	}

	public void setBillRebateDetailslist(List<ReceiptDetailInfo> billRebateDetailslist) {
		this.billRebateDetailslist = billRebateDetailslist;
	}

	public List<ReceiptDetailInfo> getSubLedgerlist() {
		return subLedgerlist;
	}

	public void setSubLedgerlist(List<ReceiptDetailInfo> subLedgerlist) {
		this.subLedgerlist = subLedgerlist;
	}

	public String getBillSource() {
		return billSource;
	}

	public void setBillSource(String billSource) {
		this.billSource = billSource;
	}

	/**
	 * @return the receiptPayeeDetailsService
	 */
	public ReceiptMisc getReceiptMisc() {
		return receiptMisc;
	}

	/**
	 * @return the reportId
	 */
	public Integer getReportId() {
		return reportId;
	}

	/**
	 * @param receiptPayeeDetailsService
	 *            the receiptPayeeDetailsService to set
	 */
	public void setReceiptMisc(ReceiptMisc receiptMisc) {
		this.receiptMisc = receiptMisc;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public BigDecimal getTotalDebitAmount() {
		return totalDebitAmount;
	}

	public void setTotalDebitAmount(BigDecimal totalDebitAmount) {
		this.totalDebitAmount = totalDebitAmount;
	}

	protected boolean validateData(final List<ReceiptDetailInfo> billDetailslistd,
			final List<ReceiptDetailInfo> subLedgerList) {
		BigDecimal totalDrAmt = BigDecimal.ZERO;
		BigDecimal totalCrAmt = BigDecimal.ZERO;
		int index = 0;
		boolean isDataValid = true;
		for (ReceiptDetailInfo rDetails : billDetailslistd) {
			index = index + 1;
			totalDrAmt = totalDrAmt.add(rDetails.getDebitAmountDetail());
			totalCrAmt = totalCrAmt.add(rDetails.getCreditAmountDetail());
			if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
					&& rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
					&& rDetails.getGlcodeDetail().trim().length() == 0) {

				addActionError(getText("miscreceipt.accdetail.emptyaccrow", new String[] { "" + index }));
				isDataValid = false;
			} else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
					&& rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0
					&& rDetails.getGlcodeDetail().trim().length() != 0) {
				addActionError(getText("miscreceipt.accdetail.amountZero", new String[] { rDetails.getGlcodeDetail() }));
				isDataValid = false;
			} else if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0
					&& rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0) {
				addActionError(getText("miscreceipt.accdetail.amount", new String[] { rDetails.getGlcodeDetail() }));
				isDataValid = false;
			} else if ((rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0 || rDetails
					.getCreditAmountDetail().compareTo(BigDecimal.ZERO) > 0)
					&& rDetails.getGlcodeDetail().trim().length() == 0) {
				addActionError(getText("miscreceipt.accdetail.accmissing", new String[] { "" + index }));
				isDataValid = false;
			}

		}
		if (isDataValid) {
			isDataValid = validateSubledgerDetails(billCreditDetailslist, subLedgerList);
		}
		return isDataValid;
	}

	protected boolean validateRebateData(final List<ReceiptDetailInfo> billDetailslistd,
			final List<ReceiptDetailInfo> subLedgerList) {
		int index = 0;
		boolean isDataValid = true;
		for (ReceiptDetailInfo rDetails : billDetailslistd) {
			index = index + 1;
			if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0
					&& rDetails.getGlcodeDetail().trim().length() != 0) {
				addActionError(getText("miscreceipt.accdetail.amountZero", new String[] { rDetails.getGlcodeDetail() }));
				isDataValid = false;
			} else if ((rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) > 0)
					&& rDetails.getGlcodeDetail().trim().length() == 0) {
				addActionError(getText("miscreceipt.accdetail.accmissing", new String[] { "" + index }));
				isDataValid = false;
			}

		}
		if (isDataValid) {
			isDataValid = validateSubledgerDetails(billRebateDetailslist, subLedgerList);
		}
		return isDataValid;
	}

	protected boolean validateSubledgerDetails(List<ReceiptDetailInfo> billRebateDetailslist,
			List<ReceiptDetailInfo> subLedgerlist) {
		Map<String, Object> accountDetailMap;
		Map<String, BigDecimal> subledAmtmap = new HashMap<String, BigDecimal>();
		List<Map<String, Object>> subLegAccMap = null; // this list will contain
		// the details about the
		// account coe those are
		// detail codes.
		for (ReceiptDetailInfo rDetails : billRebateDetailslist) {
			CChartOfAccountDetail chartOfAccountDetail = (CChartOfAccountDetail) getPersistenceService().find(
					" from CChartOfAccountDetail" + " where glCodeId=(select id from CChartOfAccounts where glcode=?)",
					rDetails.getGlcodeDetail());
			if (null != chartOfAccountDetail) {
				accountDetailMap = new HashMap<String, Object>();
				accountDetailMap.put("glcodeId", rDetails.getGlcodeIdDetail());
				accountDetailMap.put("glcode", rDetails.getGlcodeDetail());
				if (rDetails.getDebitAmountDetail().compareTo(BigDecimal.ZERO) == 0) {
					accountDetailMap.put("amount", rDetails.getCreditAmountDetail());
				} else if (rDetails.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0) {
					accountDetailMap.put("amount", rDetails.getDebitAmountDetail());
				}
				if (null == subLegAccMap) {
					subLegAccMap = new ArrayList<Map<String, Object>>();
					subLegAccMap.add(accountDetailMap);
				} else {
					subLegAccMap.add(accountDetailMap);
				}

			}
		}
		if (null != subLegAccMap) {
			Map<String, String> subLedgerMap = new HashMap<String, String>();
			for (ReceiptDetailInfo rDetails : subLedgerlist) {

				if (rDetails.getGlcode().getId() != 0) {
					if (null == subledAmtmap.get(rDetails.getGlcode().getId().toString())) {
						subledAmtmap.put(rDetails.getGlcode().getId().toString(), rDetails.getAmount());
					} else {
						BigDecimal debitTotalAmount = subledAmtmap.get(rDetails.getGlcode().getId().toString()).add(
								rDetails.getAmount());
						subledAmtmap.put(rDetails.getGlcode().getId().toString(), debitTotalAmount);
					}
					StringBuffer subledgerDetailRow = new StringBuffer();
					subledgerDetailRow.append(rDetails.getGlcode().getId().toString())
							.append(rDetails.getDetailType().getId().toString())
							.append(rDetails.getDetailKeyId().toString());
					if (null == subLedgerMap.get(subledgerDetailRow.toString())) {
						subLedgerMap.put(subledgerDetailRow.toString(), subledgerDetailRow.toString());
					}// to check for the same subledger should not allow for
						// same gl code
					else {
						addActionError(getText("miscreciept.samesubledger.repeated"));
						return false;
					}

				}
			}
			for (Map<String, Object> map : subLegAccMap) {
				String glcodeId = map.get("glcodeId").toString();
				if (null == subledAmtmap.get(glcodeId)) {
					addActionError(getText("miscreciept.subledger.entrymissing", new String[] { map.get("glcode")
							.toString() }));
					return false;
				} else if (!subledAmtmap.get(glcodeId).equals(new BigDecimal(map.get("amount").toString()))) {
					addActionError(getText("miscreciept.subledger.amtnotmatchinng", new String[] { map.get("glcode")
							.toString() }));
					return false;
				}
			}
		}
		List<CFinancialYear> list = persistenceService.findAllBy(
				"from CFinancialYear where isActiveForPosting=1 and startingDate <= ? and endingDate >= ?",
				getVoucherDate(), getVoucherDate());
		if (list.isEmpty()) {
			addActionError(getText("miscreciept.fYear.notActive"));
			return false;
		}
		return true;
	}

	public void apportionBillAmount() {
		receiptDetailList = collectionCommon.apportionBillAmount(instrumenttotal,
				(ArrayList) this.getReceiptDetailList());
	}

	void removeEmptyRows(List<ReceiptDetailInfo> list) {
		for (Iterator<ReceiptDetailInfo> detail = list.iterator(); detail.hasNext();) {
			if (detail.next() == null) {
				detail.remove();
			}
		}
	}

	public void setFinancialsUtil(FinancialsUtil financialsUtil) {
		this.financialsUtil = financialsUtil;
	}

	public List<String> getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(List<String> headerFields) {
		this.headerFields = headerFields;
	}

	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}

	public void setMandatoryFields(List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}

	public boolean isRevenueAccountHead(CChartOfAccounts coa) {
		return financialsUtil.isRevenueAccountHead(coa, bankCOAList);
	}

	public Integer getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(Integer bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	private String fundName;

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	/**
	 * @param collectionCommon
	 *            the collectionCommon to set
	 */
	public void setCollectionCommon(CollectionCommon collectionCommon) {
		this.collectionCommon = collectionCommon;
	}

	/**
	 * @param receiptHeaderService
	 *            The receipt header service to set
	 */
	public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}

	public String getCollectXML() {
		return collectXML;
	}

	public void setCollectXML(String collectXML) {
		this.collectXML = collectXML;
	}

	public Object getModel() {
		return modelPayeeList;
	}

	public ReceiptHeader[] getReceipts() {
		return receipts;
	}

	public void setReceipts(ReceiptHeader[] receipts) {
		this.receipts = receipts;
	}

	public void setXmlHandler(BillCollectXmlHandler xmlHandler) {
		this.xmlHandler = xmlHandler;
	}

	public Long[] getSelectedReceipts() {
		return selectedReceipts;
	}

	public void setSelectedReceipts(Long[] selectedReceipts) {
		this.selectedReceipts = selectedReceipts;
	}

	public void setPayeename(String payeename) {
		this.payeename = payeename;
	}

	public Date getManualReceiptDate() {
		return manualReceiptDate;
	}

	public void setManualReceiptDate(Date manualReceiptDate) {
		this.manualReceiptDate = manualReceiptDate;
	}

	public Boolean getReceiptBulkUpload() {
		return receiptBulkUpload;
	}

	public void setReceiptBulkUpload(Boolean receiptBulkUpload) {
		this.receiptBulkUpload = receiptBulkUpload;
	}

	public BigDecimal getInstrumenttotal() {
		return instrumenttotal;
	}

	public void setInstrumenttotal(BigDecimal instrumenttotal) {
		this.instrumenttotal = instrumenttotal;
	}

	public PersistenceService<ServiceCategory, Long> getServiceCategoryService() {
		return serviceCategoryService;
	}

	public void setServiceCategoryService(PersistenceService<ServiceCategory, Long> serviceCategoryService) {
		this.serviceCategoryService = serviceCategoryService;
	}

	public void setServiceDetailsService(PersistenceService<ServiceDetails, Long> serviceDetailsService) {
		this.serviceDetailsService = serviceDetailsService;
	}

	public ServiceDetails getService() {
		return service;
	}

	public void setService(ServiceDetails service) {
		this.service = service;
	}

	public List<InstrumentHeader> getInstrumentProxyList() {
		return instrumentProxyList;
	}

	public void setInstrumentProxyList(List<InstrumentHeader> instrumentProxyList) {
		this.instrumentProxyList = instrumentProxyList;
	}

	public int getInstrumentCount() {
		return instrumentCount;
	}

	public void setInstrumentCount(int instrumentCount) {
		this.instrumentCount = instrumentCount;
	}

	/**
	 * @return the manualReceiptNumber
	 */
	public String getManualReceiptNumber() {
		return manualReceiptNumber;
	}

	/**
	 * @param manualReceiptNumber
	 *            the manualReceiptNumber to set
	 */
	public void setManualReceiptNumber(String manualReceiptNumber) {
		this.manualReceiptNumber = manualReceiptNumber;
	}

	/**
	 * @return the manualReceiptNumberAndDateReq
	 */
	public Boolean getManualReceiptNumberAndDateReq() {
		return manualReceiptNumberAndDateReq;
	}

	/**
	 * @param manualReceiptNumberAndDateReq
	 *            the manualReceiptNumberAndDateReq to set
	 */
	public void setManualReceiptNumberAndDateReq(Boolean manualReceiptNumberAndDateReq) {
		this.manualReceiptNumberAndDateReq = manualReceiptNumberAndDateReq;
	}

}