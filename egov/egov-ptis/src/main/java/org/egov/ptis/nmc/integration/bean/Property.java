package org.egov.ptis.nmc.integration.bean;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.bean.Payment;
import org.egov.dcb.bean.Receipt;
import org.egov.dcb.service.DCBService;
import org.egov.dcb.service.DCBServiceImpl;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.bill.NMCPropertyTaxBillable;
import org.egov.ptis.nmc.integration.impl.PropertyImpl;
import org.egov.ptis.nmc.integration.utils.CollectionHelper;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

public abstract class Property {

	private static final Logger LOGGER = Logger.getLogger(Property.class);
	public static final int FLAG_NONE = 0;
	public static final int FLAG_BASIC = 1;
	public static final int FLAG_DCB = 2;
	public static final int FLAG_RECEIPTS = 3;
	public static final int FLAG_BASIC_AND_RECEIPTINFO = 4;

	protected BasicProperty basicProperty;
	protected DCBService dcbService;

	private String propertyID;
	private String citizenName;
	private String doorNumber;
	private String wardName;
	private DCBReport dcbReport = new DCBReport();
	private PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	private BillReceiptInfo billreceiptInfo;
	private String receiptNo;
	private int infoFlag;

	protected abstract Billable getBillable();
	public abstract void setBillable(NMCPropertyTaxBillable billable);

	protected abstract EgBill createBill();

	protected abstract DCBDisplayInfo getDCBDisplayInfo();

	protected abstract void checkAuthorization();

	protected abstract void checkIsActive();

	/**
	 * Factory method that returns a property object with the given ID.
	 *
	 * @param propertyID
	 * @param flag
	 *            Should be one of the FLAG_* values defined in this class. It
	 *            determines whether any of Basic/DCB/payment info should be
	 *            populated in the returned property object.
	 * @return
	 */
	public static Property fromPropertyID(String propertyID, String receiptNo, int flag) {
		LOGGER.info("fromPropertyID : propertyID " + propertyID);
		Property p = new PropertyImpl();
		p.propertyID = propertyID;
		p.infoFlag = flag;
		p.receiptNo = receiptNo;
		p.validate();

		p.initBasicProperty();
		p.initDCBService();
		p.populate();
		return p;
	}

	private void initBasicProperty() {
		BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
		if (getPropertyID() != null) {
			basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(getPropertyID());
		}
	}

	private void initDCBService() {
		dcbService = new DCBServiceImpl(getBillable());
	}

	private void populate() {
		LOGGER.info("Instantiating property with propertyID: " + propertyID);
		if (basicProperty != null) {
			getBasicInfo();
			getDCB();
			getReceipts();
			getReceiptForRcptNo();
		}
		LOGGER.info("Property instantiated: " + this);
	}

	/**
	 * Executes a collection.
	 */
	public BillReceiptInfo collect(Payment payment) {
		LOGGER.info("Property.collect() called: " + payment);
		BillReceiptInfo billReceiptInfo = null;
		checkIsActive();
		checkAuthorization();
		EgBill egBill = createBill();
		CollectionHelper collHelper = new CollectionHelper(egBill);
		billReceiptInfo = collHelper.executeCollection(payment);
		LOGGER.info("Property.collect() returned: " + billReceiptInfo);
		return billReceiptInfo;
	}

	/**
	 * Fetches basic property information like owner name and address (if
	 * requested).
	 */
	private void getBasicInfo() {
		if (!isBasicInfoRequested()) {
			return;
		}
		this.propertyID = basicProperty.getUpicNo();
		this.citizenName = ptisCacheMgr.buildOwnerFullName(basicProperty.getProperty().getPropertyOwnerSet());
		if (basicProperty.getPropertyID() != null && basicProperty.getPropertyID().getWard() != null) {
			this.wardName = basicProperty.getPropertyID().getWard().getName();
		}
		if (basicProperty.getAddress() != null && basicProperty.getAddress().getHouseNo() != null) {
			this.doorNumber = basicProperty.getAddress().getHouseNo();
		}
		LOGGER.info("Got basic info...");
	}

	/**
	 * Fetches demand-collection-balance info (if requested).
	 */
	private void getDCB() {
		if (!isDCBRequested()) {
			return;
		}
		DCBReport report = dcbService.getCurrentDCBOnly(getDCBDisplayInfo());
		dcbReport.setFieldNames(report.getFieldNames());
		dcbReport.setRecords(report.getRecords());
		LOGGER.info("Got DCB...");
	}

	/**
	 * Fetches all receipts paid against this property (if requested).
	 */
	private void getReceipts() {
		if (!isPaymentsRequested()) {
			return;
		}
		DCBReport report = dcbService.getReceiptsOnly();

		Map<Installment, List<Receipt>> receipts = report.getReceipts();
		dcbReport.setReceipts(receipts);

		LOGGER.info("Got payments...");
	}

	/**
	 * Fetches the receipt for given receipt no (if requested).
	 */
	private void getReceiptForRcptNo() {
		if (!isReceiptInfoRequested()) {
			return;
		}
		CollectionHelper collectionHelper = new CollectionHelper();
		billreceiptInfo = collectionHelper.getReceiptInfo(receiptNo);

		LOGGER.info("Got payments...");
	}

	private boolean isBasicInfoRequested() {
		return (infoFlag == FLAG_BASIC || infoFlag == FLAG_BASIC_AND_RECEIPTINFO);
	}

	private boolean isDCBRequested() {
		return (infoFlag & FLAG_DCB) == FLAG_DCB;
	}

	private boolean isPaymentsRequested() {
		return (infoFlag & FLAG_RECEIPTS) == FLAG_RECEIPTS;
	}

	private boolean isReceiptInfoRequested() {
		return (infoFlag & FLAG_BASIC_AND_RECEIPTINFO) == FLAG_BASIC_AND_RECEIPTINFO;
	}

	private void validate() {
		if ((propertyID == null || propertyID.trim().equals(""))) {
			throw new EGOVRuntimeException("PropertyID was null or empty!");
		}
		if (isReceiptInfoRequested() && (receiptNo == null || receiptNo.equals(""))) {
			throw new EGOVRuntimeException("receiptNo was null or empty!");
		}
	}

	public String getFullAddress() {
		String address = ptisCacheMgr.buildAddress(basicProperty);
		return address;
	}

	public String getPropertyID() {
		return propertyID;
	}

	public String getCitizenName() {
		return citizenName;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public String getWardName() {
		return wardName;
	}

	public DCBReport getDcbReport() {
		return dcbReport;
	}

	@Override
	public String toString() {
		return getPropertyID() + "-" + getDcbReport();
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public BillReceiptInfo getBillreceiptInfo() {
		return billreceiptInfo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

}