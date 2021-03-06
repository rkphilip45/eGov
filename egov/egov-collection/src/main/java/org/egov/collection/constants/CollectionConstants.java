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
package org.egov.collection.constants;

import org.egov.utils.FinancialConstants;


public final class CollectionConstants {
	// General messages/words like YES/NO/ALL
	public static final String ALL = "ALL";
	public static final String BLANK = "";
	public static final String COMMA = ", ";
	public static final String OPENBRACKET = "[ ";
	public static final String CLOSEBRACKET = " ]";

	// Service types
	public static final String SERVICE_TYPE_BILLING = "B";
	public static final String SERVICE_TYPE_COLLECTION = "C";
	public static final String SERVICE_TYPE_PAYMENT = "P";
	public static final String CHALLAN_SERVICE_TYPE = "H";

	// Dropdown data
	public static final String DROPDOWN_DATA_SERVICE_LIST = "serviceList";
	public static final String DROPDOWN_DATA_COUNTER_LIST = "counterList";
	public static final String DROPDOWN_DATA_RECEIPT_CREATOR_LIST = "receiptCreatorList";
	public static final String DROPDOWN_DATA_RECEIPTZONE_LIST = "activeZoneList";
	public static final String DROPDOWN_DATA_ONLINETRANSACTIONSTATUS_LIST = "onlineTransactionStatusList";

	// Instrument Types
	public static final String INSTRUMENTTYPE_CASH = FinancialConstants.INSTRUMENT_TYPE_CASH;
	public static final String INSTRUMENTTYPE_CHEQUE = FinancialConstants.INSTRUMENT_TYPE_CHEQUE;
	public static final String INSTRUMENTTYPE_CHEQUEORDD = "cheque/dd";
	public static final String INSTRUMENTTYPE_DD = FinancialConstants.INSTRUMENT_TYPE_DD;
	public static final String INSTRUMENTTYPE_CARD = FinancialConstants.INSTRUMENT_TYPE_CARD;
	public static final String INSTRUMENTTYPE_BANK = FinancialConstants.INSTRUMENT_TYPE_BANK;
	public static final String INSTRUMENTTYPE_ONLINE = FinancialConstants.INSTRUMENT_TYPE_ONLINE;
	
	// Receipt types
	public static final char RECEIPT_TYPE_ADHOC = 'A';
	public static final char RECEIPT_TYPE_BILL = 'B';
	public static final char RECEIPT_TYPE_CHALLAN = 'C';
	
	// Collection type
	public static final char COLLECTION_TYPE_COUNTER = 'C';
	public static final char COLLECTION_TYPE_ONLINECOLLECTION = 'O';
	public static final char COLLECTION_TYPE_FIELDCOLLECTION = 'F';
	
	// Report template names
	public static final String RECEIPT_TEMPLATE_NAME = "collection_receipt";
	public static final String CHALLAN_RECEIPT_TEMPLATE_NAME = "collection_receipt_challan";
	public static final String REPORT_TEMPLATE_PENDING_CHALLANS = "pending_challans";
	public static final String REPORT_TEMPLATE_ONLINE_TRANSACTION = "online_transaction";
	public static final String REPORT_TEMPLATE_RECEIPT_REGISTER = "collection_receipt_register";

	//Receipt View Source Path for Financials
	public static final String RECEIPT_VIEW_SOURCEPATH = "/collection/receipts/receipt!viewReceipts.action?selectedReceipts=";
	
	// action method return values
	public static final String REPORT = "report";
	public static final String VIEW = "view";
	public static final String CANCEL = "cancel";
	public static final String CREATERECEIPT = "createReceipt";
	public static final String CANCELRECEIPT = "cancelReceipt";
	
		
	// Status codes
	public static final String RECEIPT_STATUS_CODE_TO_BE_SUBMITTED = "TO_BE_SUBMITTED";
	public static final String RECEIPT_STATUS_CODE_SUBMITTED = "SUBMITTED";
	public static final String RECEIPT_STATUS_CODE_APPROVED = "APPROVED";
	public static final String RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED="INSTR_BOUNCED";
	public static final String RECEIPT_STATUS_CODE_CANCELLED = "CANCELLED";
	public static final String RECEIPT_STATUS_CODE_FAILED = "FAILED";
	public static final String RECEIPT_STATUS_CODE_PENDING = "PENDING";
	
	// Status descriptions - ideally we should not be using these - to be discussed
	public static final String RECEIPT_STATUS_DESC_CANCELLED = "Cancelled";
	public static final String RECEIPT_STATUS_DESC_CREATED= "To Be Submitted";
	public static final String RECEIPT_STATUS_DESC_PENDING= "Pending";
	
	// module types
	public static final String MODULE_NAME_RECEIPTHEADER= "ReceiptHeader";
	public static final String MODULE_NAME_CHALLAN= "Challan";
	public static final String MODULE_NAME_COLLECTIONS= "Collections";
	public static final String MODULE_NAME_COLLECTIONS_CONFIG= "erpCollection";
	public static final String MODULE_NAME_INSTRUMENTHEADER= "Instrument";	
	public static final String MODULE_NAME_ONLINEPAYMENT= "OnlinePayment";
	public static final String MODULE_NAME_PROPERTYTAX = "Property Tax";
	
	//AppConfig Values
	public static final String APPCONFIG_VALUE_ISVOUCHERAPPROVED="ISVOUCHERAPPROVED";
	public static final String APPCONFIG_VALUE_CHALLANVALIDUPTO="CHALLANVALIDUPTO";
	public static final String APPCONFIG_VALUE_ISBILLNUMREQD="ISBILLNUMREQD";
	public static final String APPCONFIG_VALUE_ISSERVICEREQD="ISSERVICEREQD";
	public static final String APPCONFIG_VALUE_RECEIPTVOUCHERTYPEFORCHEQUEDDCARD="RECEIPTVOUCHERTYPEFORCHEQUEDDCARD";
	public static final String APPCONFIG_VALUE_REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD="REMITTANCEVOUCHERTYPEFORCHEQUEDDCARD";
	public static final String APPCONFIG_VALUE_PAYEEFORMISCRECEIPTS="PayeeForMiscReceipts";
	public static final String APPCONFIG_VALUE_USERECEIPTDATEFORCONTRA="USERECEIPTDATEFORCONTRA";
	public static final String MANUALRECEIPTINFOREQUIRED = "MANUALRECEIPTINFOREQUIRED";
	
	// named queries (collections)
	public static final String QUERY_RECEIPTS_FOR_VOUCHERS = "RECEIPTS_FOR_VOUCHERS";
	public static final String QUERY_RECEIPTS_FOR_BOUNCED_INSTRUMENTS = "RECEIPTS_FOR_BOUNCED_INSTRUMENTS";
	public static final String QUERY_RECEIPTS_FOR_PAYEEDETAIL = "RECEIPTS_BY_PAYEEDETAILID";
	public static final String QUERY_RECEIPT_BY_RECEIPTNUM_AND_SERVICECODE = "RECEIPTS_BY_RECEIPTNUM_AND_SERVICECODE";
	public static final String QUERY_RECEIPTS_BY_RECEIPTNUM = "RECEIPTS_BY_RECEIPTNUM";
	public static final String QUERY_RECEIPTS_BY_REFNUM_AND_SERVICECODE = "RECEIPTS_BY_REFNUM_AND_SERVICECODE";
	public static final String QUERY_RECEIPTS_BY_INSTRUMENTNO_AND_SERVICECODE = "RECEIPTS_BY_INSTRUMENTNO_AND_SERVICECODE";
	public static final String QUERY_RECEIPTS_BY_RECONSTATUS = "RECEIPTS_BY_RECONSTATUS";
	public static final String QUERY_RECEIPT_BY_CHALLANID = "RECEIPT_BY_CHALLANID";
	public static final String QUERY_VALIDRECEIPT_BY_CHALLANNO = "VALIDRECEIPT_BY_CHALLANNO";
	public static final String QUERY_CHALLANRECEIPT_BY_REFERENCEID = "CHALLANRECEIPT_BY_REFERENCEID";
	public static final String QUERY_ONLINERECEIPTS_BY_STATUSCODE = "ONLINERECEIPTS_BY_STATUSCODE";
	public static final String QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE = "QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE";
	public static final String QUERY_RECEIPT_BY_SERVICE_RECEIPTNUMBER_CONSUMERCODE = "RECEIPT_BY_SERVICE_RECEIPTNUMBER_CONSUMERCODE";
	public static final String QUERY_RECEIPT_BY_RECEIPTID_AND_REFERENCENUMBER = "RECEIPT_BY_RECEIPTID_AND_REFERENCENUMBER";
	public static final String QUERY_RECEIPT_BY_SERVICE_MANUALRECEIPTNO_AND_DATE = "RECEIPT_BY_SERVICE_MANUALRECEIPTNO_AND_DATE";
		
	// named queries (other modules)
	public static final String QUERY_ACTIVE_COUNTERS = "getAllActiveCounters";
	public static final String QUERY_CREATEDBYUSERS_OF_RECEIPTS = "CREATEDBYUSERS_OF_RECEIPTS";
	public static final String QUERY_LOCATION_BY_USER = "getLocationByUser";
	public static final String QUERY_STATUS_BY_MODULE_AND_CODE = "getEgwStatusByModuleAndCode";
	public static final String QUERY_SERVICES_BY_TYPE = "getServicesByType";
	public static final String QUERY_COLLECTION_SERVICS = "getCollectionServices";
	public static final String QUERY_FUNCTIONARY_BY_CODE="getFunctionaryByCode";
	public static final String QUERY_DEPARTMENT_BY_CODE="getDepartmentByCode";
	public static final String QUERY_SERVICE_BY_CODE="getServiceByCode";
	public static final String QUERY_INSTRUMENTTYPE_BY_TYPE="getInstrumentTypeByType";
	public static final String QUERY_CHARTOFACCOUNT_BY_INSTRTYPE="getChartofAccountByInstrumentType";
	public static final String QUERY_ALLCOUNTERS = "getAllCounters";
	public static final String QUERY_ZONE_OF_RECEIPTS = "getAllReceiptBoundary";
	public static final String QUERY_SERVICE_BY_NAME="getServicesByName";
	public static final String QUERY_SERVICE_BY_ID="SERVICE_BY_ID";
	public static final String QUERY_SCHEME_BY_FUNDID="getAllSchemeByFundId";
	public static final String QUERY_SUBSCHEME_BY_SCHEMEID="getAllSubSchemeBySchemeId";
	public static final String QUERY_DEPARTMENT_BY_ID="getDepartmentById";
	public static final String QUERY_ACCOUNTDETAILTYPE_BY_ID="getAccountDetailtypeById";
	public static final String QUERY_ACCOUNTDETAILKEY_BY_DETAILKEY="getAccountDetailkeyByDetailKey";
	public static final String QUERY_ALL_STATUSES_FOR_MODULE="getAllStatusesForModule";
	public static final String QUERY_STATUSES_FOR_MODULE_AND_CODES="getStatusesForModuleAndCodes";
	
	public static final String QUERY_ALL_DEPARTMENTS="getallDepartment";
	public static final String QUERY_ALL_FUNCTION="getallFunction";
	public static final String QUERY_ALL_FUNCTIONARY="getallFunctionary";
	public static final String QUERY_ALL_FUND="getallFund";
	public static final String QUERY_ALL_FUNDSOURCE="getallFundsource";
	public static final String QUERY_ALL_FIELD="getallField";
	public static final String QUERY_VOUCHERHEADER_BY_VOUCHERNUMBER="getVoucherHeaderByVoucherNumber";
	public static final String QUERY_GETFINANCIALYEARBYDATE="getFinancialYearByDate";
	public static final String QUERY_GET_LOCATIONBYID="getLocationById";
	public static final String QUERY_GET_CONTRAVOUCHERBYVOUCHERHEADERID="getContraVoucherbyVoucherHeaderId";
	
	// Workflow actions
	public static final String WF_ACTION_CREATE_RECEIPT = "Create Receipt";
	public static final String WF_ACTION_CREATE_VOUCHER = "Create Receipt Voucher";
	public static final String WF_ACTION_SUBMIT = "Submit for Approval";
	public static final String WF_ACTION_APPROVE = "Approve";
	public static final String WF_ACTION_REJECT = "Reject";
	public static final String WF_STATE_NEW = "NEW";
	public static final String WF_STATE_END = "END";
	public static final String WF_ACTION_NAME_NEW_CHALLAN = "CHALLAN_NEW";
	public static final String WF_ACTION_NAME_CREATE_CHALLAN = "CHALLAN_CREATE";
	public static final String WF_ACTION_NAME_CHECK_CHALLAN = "CHALLAN_CHECK";
	public static final String WF_ACTION_NAME_APPROVE_CHALLAN = "CHALLAN_APPROVE";
	public static final String WF_ACTION_NAME_VALIDATE_CHALLAN = "CHALLAN_VALIDATE";
	public static final String WF_ACTION_NAME_REJECT_CHALLAN = "CHALLAN_REJECT";
	public static final String WF_ACTION_NAME_MODIFY_CHALLAN = "CHALLAN_MODIFY";
	public static final String WF_ACTION_NAME_CANCEL_CHALLAN = "CHALLAN_CANCEL";
	public static final String WF_ACTION_NAME_END_CHALLAN = "CHALLAN_END";

	
	// Workflow states
	public static final String WF_STATE_RECEIPT_CREATED = "Receipt Created";
	public static final String WF_STATE_VOUCHER_CREATED = "Receipt Voucher Created";
	public static final String WF_STATE_SUBMITTED = "Submitted";
	public static final String WF_STATE_APPROVED = "Approved";
	public static final String WF_STATE_REJECTED = "Rejected";
	public static final String WF_STATE_CREATE_CHALLAN = "CREATED";
	public static final String WF_STATE_CHECK_CHALLAN = "CHECKED";
	public static final String WF_STATE_APPROVE_CHALLAN = "APPROVED";
	public static final String WF_STATE_VALIDATE_CHALLAN = "VALIDATED";
	public static final String WF_STATE_REJECTED_CHALLAN = "REJECTED";
	public static final String WF_STATE_CANCEL_CHALLAN="CANCELLED";
	
	public static final String COLLECTIONS_INTERFACE_SUFFIX = "CollectionsInterface";
	
	public static final String BOUNCEDINSTRUPDATE_RECONDATE = "BOUNCEDINSTRUPDATE_RECONDATE";
		
	public static final String INSTRUMENTHEADER_STATUS_CANCELLED = FinancialConstants.INSTRUMENT_CANCELLED_STATUS;
	public static final String INSTRUMENT_DISHONORED_STATUS = FinancialConstants.INSTRUMENT_DISHONORED_STATUS;
	public static final String INSTRUMENT_NEW_STATUS = FinancialConstants.INSTRUMENT_CREATED_STATUS;
	public static final String INSTRUMENT_DEPOSITED_STATUS = FinancialConstants.INSTRUMENT_DEPOSITED_STATUS;
	public static final String INSTRUMENT_RECONCILED_STATUS = FinancialConstants.INSTRUMENT_RECONCILED_STATUS;
	
	//Voucher Constants
	public static final String FINANCIAL_RECEIPTS_VOUCHERTYPE = FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT;
	public static final String FINANCIAL_RECEIPTS_VOUCHERNAME = "Other receipts";
	public static final String FINANCIAL_JOURNALVOUCHER_VOUCHERTYPE = FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
	public static final String FINANCIAL_JOURNALVOUCHER_VOUCHERNAME = FinancialConstants.JOURNALVOUCHER_NAME_GENERAL;
	public static final String FINANCIAL_VOUCHERDESCRIPTION = "Collection Module";
	public static final String FINANCIAL_PAYMENTVOUCHER_VOUCHERTYPE = FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT;
	public static final String FINANCIAL_PAYMENTVOUCHER_VOUCHERNAME = FinancialConstants.PAYMENTVOUCHER_NAME_DIRECTBANK;	
	public static final String FINANCIAL_CONTRAVOUCHER_VOUCHERTYPE = FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA;
	public static final String FINANCIAL_CONTRATVOUCHER_VOUCHERNAME = FinancialConstants.CONTRAVOUCHER_NAME_PAYIN;
	
	// Name of session variable that contains currently logged in user name
	public static final String SESSION_VAR_LOGIN_USER_NAME = "com.egov.user.LoginUserName";
	public static final String SESSION_VAR_RECEIPT_IDS = "EGOV_RECEIPT_IDS";
	public static final String SUPER_USER_NAME = "egovernments";
	public static final String SESSION_VAR_LOGIN_USER_COUNTERID = "counterId";
	
	public static final String CITIZEN_USER_NAME = "citizenUser";
	
	// Separators
	public static final String SEPARATOR_HYPHEN = "-";
	public static final String SEPARATOR_UNDERSCORE = "_";
	public static final String ZERO_INT = "0";
	public static final String ZERO_DOUBLE = "0.0";

	public static final String FINANCIAL_INSTRUMENTSERVICE_VOUCHERHEADEROBJECT="Voucher header";
	public static final String FINANCIAL_INSTRUMENTSERVICE_INSTRUMENTHEADEROBJECT="Instrument header";
	
	public static final String BOUNDARY_HIER_CODE_ADMIN="ADMIN";
	
	public static final String BOOLEAN_TRUE="TRUE";
	public static final String BOOLEAN_FALSE="FALSE";
	
	//Script names
	public static final String SCRIPT_PAYMENTMODESNOTALLOWED_RULES="collection.paytmodesnotallowed.rules";
	public static final String SCRIPT_RECEIPTNUMBER_GENERERATOR="collection.receiptnumber.generator";
	public static final String SCRIPT_INTERNALREFNO_GENERERATOR="collection.internalrefno.generator";
	public static final String SCRIPT_CHALLANNO_GENERERATOR="collection.challannumber.generator";
	public static final String QUERY_CHALLAN_WORKFLOWDESIGNATIONS="collection.challanworkflowdesignations.rules";
	public static final String QUERY_CHALLAN_WORKFLOWDEPARTMENTS="collection.challanworkflowdepartment.rules";
	public static final String QUERY_BANKREMITTANCE_WORKFLOWDEPARTMENTS="collection.bankremittanceworkflowdepartment.rules";
	public static final String QUERY_BANKREMITTANCE_WORKFLOWDESIGNATIONS="collection.bankremittanceworkflowdesignations.rules";
	
	public static final String DEPARTMENT = "department";
	public static final String FUND = "fund";
	public static final String SCHEME = "scheme";
	public static final String SUBSCHEME = "subscheme";
	public static final String FUNCTION = "function";
	public static final String FUNCTIONARY = "functionary";
	public static final String FUNDSOURCE = "fundsource";
	public static final String FIELD = "field";
	public static final String BILLNUMBER = "billNumber";
	public static final String SERVICE = "service";
		
	// Purpose names
	public static final String PURPOSE_NAME_CASH_IN_HAND = "Cash In Hand";
	public static final String PURPOSE_NAME_CHEQUE_IN_HAND = "Cheque In Hand";
	public static final String PURPOSE_NAME_BANK_CODES = "Bank Codes";
	public static final String PURPOSE_NAME_CREDIT_CARD = "Credit Card";
	public static final String PURPOSE_NAME_ATM_ACCOUNTCODE = "ATM ACCOUNT CODE";
	public static final String PURPOSE_NAME_INTERUNITACCOUNT = "Inter-Unit Transfer Account";
	
	//Bank Remittance
	public static final String BANKREMITTANCE_SERVICETOTALCASHAMOUNT="SERVICETOTALCASHAMOUNT";
	public static final String BANKREMITTANCE_SERVICETOTALCHEQUEAMOUNT="SERVICETOTALCHEQUEAMOUNT";
	public static final String BANKREMITTANCE_SERVICETOTALCARDPAYMENTAMOUNT="SERVICETOTALCARDPAYMENTAMOUNT";
	public static final String BANKREMITTANCE_SERVICETOTALONLINEPAYMENTAMOUNT="SERVICETOTALONLINEPAYMENTAMOUNT";
	public static final String BANKREMITTANCE_VOUCHERDATE="VOUCHERDATE";
	public static final String BANKREMITTANCE_SERVICENAME="SERVICENAME";
	public static final String BANKREMITTANCE_DEPARTMENTCODE_ACCOUNTS="A";
	public static final String BANKREMITTANCE_FUNDNAME="FUNDNAME";
	public static final String BANKREMITTANCE_DEPARTMENTNAME="DEPARTMENTNAME";
	public static final String BANKREMITTANCE_FUNDCODE="FUNDCODE";
	public static final String BANKREMITTANCE_DEPARTMENTCODE="DEPARTMENTCODE";
	
	/*// Key entries in maps
	public static final String MAP_KEY_EGOVCOMMON_CASHINHAND = "cashInHand";
	public static final String MAP_KEY_EGOVCOMMON_CHEQUEINHAND = "chequeInHand";
	public static final String MAP_KEY_INSTRSERVICE_INSTRUMENTNUMBER = InstrumentService.INSTRUMENT_NUMBER;
	public static final String MAP_KEY_INSTRSERVICE_INSTRUMENTDATE = InstrumentService.INSTRUMENT_DATE;
	public static final String MAP_KEY_INSTRSERVICE_INSTRUMENTAMOUNT = InstrumentService.INSTRUMENT_AMOUNT;
	public static final String MAP_KEY_INSTRSERVICE_INSTRUMENTTYPE = InstrumentService.INSTRUMENT_TYPE;
	public static final String MAP_KEY_INSTRSERVICE_ISPAYCHEQUE = InstrumentService.IS_PAYCHECK;
	public static final String MAP_KEY_INSTRSERVICE_BANKCODE = InstrumentService.BANK_CODE;
	public static final String MAP_KEY_INSTRSERVICE_BANKBRANCHNAME = InstrumentService.BRANCH_NAME;
	public static final String MAP_KEY_INSTRSERVICE_TRANSACTIONNUMBER = InstrumentService.TRANSACTION_NUMBER;
	public static final String MAP_KEY_INSTRSERVICE_TRANSACTIONDATE = InstrumentService.TRANSACTION_DATE;
	public static final String MAP_KEY_INSTRSERVICE_BANKACCOUNTID = InstrumentService.BANKACCOUNTID;
	*/
	//Online Payments 
	public static final String SERVICECODE_PGI_BILLDESK="BDPGI";
	public static final String SERVICECODE_PROPERTYTAX="PT";
	public static final String SERVICECODE_PROFESSIONALTAX="PRFT";
	
	//Online Payment Statuses
	public static final String ONLINEPAYMENT_STATUS_CODE_PENDING="ONLINE_STATUS_PENDING";
	public static final String ONLINEPAYMENT_STATUS_CODE_SUCCESS="ONLINE_STATUS_SUCCESS";
	public static final String ONLINEPAYMENT_STATUS_CODE_FAILURE="ONLINE_STATUS_FAILURE";
	public static final String ONLINEPAYMENT_STATUS_CODE_TO_BE_REFUNDED="TO_BE_REFUNDED";
	public static final String ONLINEPAYMENT_STATUS_CODE_REFUNDED="ONLINE_STATUS_REFUNDED";
	public static final String ONLINEPAYMENT_STATUS_DESC_PENDING="Pending";
	public static final String ONLINEPAYMENT_STATUS_DESC_SUCCESS="Success";
	public static final String ONLINEPAYMENT_STATUS_DESC_FAILURE="Failure";
	public static final String ONLINEPAYMENT_STATUS_DESC_TO_BE_REFUNDED="To Be Refunded";
	public static final String ONLINEPAYMENT_STATUS_DESC_REFUNDED="Refunded";
	
	
	//Challan Statuses
	public static final String CHALLAN_STATUS_CODE_CREATED="CREATED";
	public static final String CHALLAN_STATUS_CODE_CHECKED="CHECKED";
	public static final String CHALLAN_STATUS_CODE_APPROVED="APPROVED";
	public static final String CHALLAN_STATUS_CODE_VALIDATED="VALIDATED";
	public static final String CHALLAN_STATUS_CODE_REJECTED="REJECTED";
	public static final String CHALLAN_STATUS_CODE_CANCELLED="CANCELLED";
	public static final String CHALLAN_STATUS_DESC_CREATED="Created";
	public static final String CHALLAN_STATUS_DESC_CHECKED="Checked";
	public static final String CHALLAN_STATUS_DESC_APPROVED="Approved";
	public static final String CHALLAN_STATUS_DESC_REJECTED="Rejected";
	public static final String CHALLAN_STATUS_DESC_CANCELLED="Cancelled";	
	
	public static final String PGI_AUTHORISATION_CODE_SUCCESS="0300";
	
	//Bill desk waiting for response from payment gateway.
	public static final String PGI_AUTHORISATION_CODE_WAITINGFOR_PAY_GATEWAY_RESPONSE = "0002";
	
	//Generic Payment Request Attribute
	public static final String ONLINEPAYMENT_INVOKE_URL="paymentGatewayURL";
	public static final String PAYMENTGATEWAYADAPTOR_INTERFACE_SUFFIX="PaymentGatewayAdaptor";
	
	//BillDesk Payment Request Attributes
	public static final String BILLDESK_TXT_CUSTOMERID="txtCustomerID";
	public static final String BILLDESK_TXT_TXNAMOUNT="txtTxnAmount";
	public static final String BILLDESK_TXT_ADDITIONALINFO1="txtAdditionalInfo1";
	public static final String BILLDESK_TXT_ADDITIONALINFO2="txtAdditionalInfo2";
	public static final String BILLDESK_TXT_ADDITIONALINFO3="txtAdditionalInfo3";
	public static final String BILLDESK_TXT_ADDITIONALINFO4="txtAdditionalInfo4";
	public static final String BILLDESK_TXT_ADDITIONALINFO5="txtAdditionalInfo5";
	public static final String BILLDESK_TXT_ADDITIONALINFO6="txtAdditionalInfo6";
	public static final String BILLDESK_TXT_ADDITIONALINFO7="txtAdditionalInfo7";
	public static final String BILLDESK_RU="RU";
	public static final String UNIQUE_CHECKSUM_KEY = "9azFh4Ge1ftL";
	
	public static final String ONLINE_PAYMENT_BILLDESK_MERCHANTID = "NAGPURMUNC"; 
	public static final String PAYMENT_REQUEST_MESSAGE_KEY = "paymentReqMsg";
	
	public static final String PIPE_SEPARATOR ="|";
	public static final String PAYMENT_REQUEST_MSG_NA = "NA";
	public static final String PAYMENT_REQUEST_SECURITY_ID = "nagpurmunc";
	
		
	// Error Messages
	public static final String REPORT_GENERATION_ERROR="Error during report generation!";
	
	public static final String YES="Y";
	public static final String NO="N";
	
	//Service Codes
	public static final String SERVICE_CODE_COLLECTIONS="CL";
	
	//Challan
	public static final String QUERY_CHALLAN_SERVICES="getServicesforChallan";
	public static final String QUERY_ALL_ACTIVE_FINANCIAL_YEAR="getActiveFinancialYear";
	public static final String Mandatory="M";
	public static final String MISMandatoryAttributesModule="EGF";
	public static final String MISMandatoryAttributesKey="DEFAULTTXNMISATTRRIBUTES";

	public static final String CHAIRPERSON = "CHAIRPERSON";
	public static final String CHALLAN_TEMPLATE_NAME="collection_challan";
	public static final String CHALLAN_CREATION_REMARKS = "Challan created";
	
	//Reversal Voucher Contants
	public static final String FINANCIALS_VOUCHERREVERSAL_ORIGINALVOUCHERID="Original voucher header id";
	public static final String FINANCIALS_VOUCHERREVERSAL_DATE="Reversal voucher date";
	public static final String FINANCIALS_VOUCHERREVERSAL_TYPE="Reversal voucher type";
	public static final String FINANCIALS_VOUCHERREVERSAL_NAME="Reversal voucher name";
	
	//COLLECTIONS MODULEID IN EG_MODULES
	public static final String COLLECTIONS_EG_MODULES_ID="10";

	//Collection Common List type
	public static final String COLLECTIONSAMOUNTTPE_CREDIT="credit";
	public static final String COLLECTIONSAMOUNTTPE_DEBIT="debit";
	public static final String COLLECTIONSAMOUNTTPE_BOTH="both";
	
	// Constants related to number formatting
	public static final int AMOUNT_PRECISION_DEFAULT = 2;

	// Report parameters
	public static final String REPORT_PARAM_EGOV_COMMON = "EGOV_COMMON";
	public static final String REPORT_PARAM_COLLECTIONS_UTIL = "EGOV_COLLECTIONS_UTIL";
	
	// Custom Properties Parameters
	public static final String CUSTOMPROPERTIES_FILENAME = "custom.properties";
	//public static final String MESSAGEKEY_SERVICECODE_="collection.servicecode.";
	public static final String MESSAGEKEY_BILLDESK_REV_HEAD_="billdesk.revenuehead.";
	
	//Historic Data Migration Parameters
	public static final String PROPERTYTAX_REFERENCEDESCRIPTION = "Property Tax Bill Number: "; 
	public static final String PROFESSIONALTAX_REFERENCEDESCRIPTION = "Professional Tax Bill Number: "; 
    
    	//Service Category Masters
	public static final String SERVICECATEGORY_CODE = "code";
	
	public static final String RCPT_CREATE = "Receipt Creation";
	public static final String RCPT_CANCEL = "Receipt Cancellation";
}
