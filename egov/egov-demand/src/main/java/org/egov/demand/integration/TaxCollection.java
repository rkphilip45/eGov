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
package org.egov.demand.integration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.InvalidAccountHeadException;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDAOFactory;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgBillDetailsDao;
import org.egov.demand.dao.EgBillReceiptDao;
import org.egov.demand.dao.EgdmCollectedReceiptDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.demand.utils.DemandConstants;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.hibernate.ObjectNotFoundException;

/**
 * This class is used to persist Bills with Collection Details(i.e received from
 * ErpCollection) .This is used for the integration of Collections .
 *
 * @author Sathish Reddy K
 *
 */
public abstract class TaxCollection implements BillingIntegrationService {

	private static final Logger LOGGER = Logger.getLogger(TaxCollection.class);

	private EgBillDao billDao;
	private EgBillDetailsDao billDetDao;
	private EgBillReceiptDao billRctDao;
	private CommonsDAOFactory commonsDAOFactory;

	public TaxCollection() {

	}

	/**
	 *
	 * Called when there is a collection happened or a receipt cancelled or a
	 * cheque bounced.
	 *
	 * @param java
	 *            .util.Set<BillReceiptInfo> billReceipts
	 *
	 * @return java.lang.Boolean status(If any error occurred then status is
	 *         returned as false.)
	 */

	@Override
	public void updateReceiptDetails(Set<BillReceiptInfo> billReceipts) {
		LOGGER.info("Logs For HandHeldDevice Permance Test : Receipt Details Updating Started...");
		for (BillReceiptInfo bri : billReceipts) {
			try {
				LOGGER.debug("-----updateReceiptDetails is called----------------");
				updateNewReceipt(bri);
			} catch (Exception e) {
				LOGGER.error(
						"Exception while updating receipt details in billing system",
						e);
				throw new EGOVRuntimeException("", e);
			}

		}
		LOGGER.info("Logs For HandHeldDevice Permance Test : Receipt Details Updating Finished...");
	}

	/**
	 * creates or updates the EgBillReceipt ,EgBill,EgBillDetails , EgDemand and
	 * EgDemandDetails with Collection Details. Called when there is a
	 * collection happened or a receipt cancelled or a cheque bounced
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 */
	private void updateNewReceipt(BillReceiptInfo bri)
			throws InvalidAccountHeadException, ObjectNotFoundException {
		LOGGER.info("-----updateNewReceipt is called----------------");
		linkBillToReceipt(bri);
		updateBillDetails(bri);
		updateDemandDetails(bri);
		LOGGER.info("--end of Updation of all the Demand and PTIS tables-------------------");
	}

	/**
	 * Creates or updates the EgBill Receipt table depending upon the
	 * transaction i.e for Normal Collection new row will be created and for
	 * receipt cancel table gets updated
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 * @param org
	 *            .egov.lib.rjbac.user.User user
	 *
	 * @return org.egov.demand.model.BillReceipt
	 *
	 * @throws InvalidAccountHeadException
	 */
	BillReceipt linkBillToReceipt(BillReceiptInfo bri)
			throws InvalidAccountHeadException, ObjectNotFoundException {
		LOGGER.debug("-----Start of linkBillToReceipt----------------");
		billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		billRctDao = DCBDaoFactory.getDaoFactory().getEgBillReceiptDao();
		BillReceipt billRecpt = null;
		if (bri == null) {
			throw new EGOVRuntimeException(" BillReceiptInfo Object is null ");
		}
		EgBill egBill = (EgBill) billDao.findById(
				new Long(bri.getBillReferenceNum()), false);
		if (egBill == null) {
			throw new EGOVRuntimeException(
					" EgBill Object is null for the Bill Number"
							+ bri.getBillReferenceNum());
		}
		List<EgBillDetails> billDetList = billDetDao
				.getBillDetailsByBill(egBill);
		BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri,
				billDetList);
		if (bri.getEvent() == null) {
			throw new EGOVRuntimeException(
					" Event in BillReceiptInfo Object is Null");
		}
		if (bri.getEvent().equals(
				BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
			billRecpt = prepareBillReceiptBean(bri, egBill, totalCollectedAmt);
			billRctDao.create(billRecpt);

		} else if (bri.getEvent().equals(
				BillingIntegrationService.EVENT_RECEIPT_CANCELLED)) {
			billRecpt = updateBillReceiptForCancellation(bri, egBill,
					totalCollectedAmt);
		}
		LOGGER.debug("-----End of linkBillToReceipt----------------");
		return billRecpt;
	}

	/**
	 * Method will be called when a Collection has happened .Here in
	 * EGBillReceipt table a row will be created with the billnumber
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 * @param org
	 *            .egov.demand.model.EgBill egBill
	 * @param java
	 *            .math.BigDecimal totalCollAmt totalCollectedAmt
	 *
	 * @return org.egov.demand.model.BillReceipt billRecpt
	 *
	 *
	 */

	BillReceipt prepareBillReceiptBean(BillReceiptInfo bri, EgBill egBill,
			BigDecimal totalCollectedAmt) {
		BillReceipt billRecpt = null;
		if (bri != null && egBill != null && totalCollectedAmt != null) {
			billRecpt = new BillReceipt();
			billRecpt.setBillId(egBill);
			billRecpt.setReceiptAmt(totalCollectedAmt);
			billRecpt.setReceiptNumber(bri.getReceiptNum());
			billRecpt.setReceiptDate(bri.getReceiptDate().toDate());
			billRecpt.setCollectionStatus(bri.getReceiptStatus().getCode());
			billRecpt.setCreatedBy(bri.getCreatedBy());
			billRecpt.setModifiedBy(bri.getModifiedBy());
			billRecpt.setCreatedDate(new Date());
			billRecpt.setModifiedDate(new Date());
			billRecpt.setIsCancelled(Boolean.FALSE);
		}
		return billRecpt;
	}

	/**
	 * Method will be called when a payment Receipt is Cancelled .Here the
	 * EGBillReceipt table is updated with the cancelled receipt amount and mark
	 * the receipt status i.e is_cancelled to true,but in case of cheque Bounce
	 * the status of the receipt will not be changed.
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 *            bri
	 * @param org
	 *            .egov.demand.model.EgBill egBill
	 * @param java
	 *            .math.BigDecimal totalCollAmt totalCollectedAmt
	 *
	 * @return org.egov.demand.model.BillReceipt billRecpt
	 *
	 *
	 */
	BillReceipt updateBillReceiptForCancellation(BillReceiptInfo bri,
			EgBill egBill, BigDecimal totalCollectedAmt) {
		BillReceipt billRecpt = null;
		if (bri == null) {
			throw new EGOVRuntimeException(" BillReceiptInfo Object is null ");
		}
		if (egBill != null && totalCollectedAmt != null) {
			billRctDao = DCBDaoFactory.getDaoFactory().getEgBillReceiptDao();
			billRecpt = billRctDao.getBillReceiptByEgBill(egBill);
			if (billRecpt == null) {
				throw new EGOVRuntimeException(
						" Bill receipt Object is null for the EgBill "
								+ egBill.getId());
			}
			if (bri.getEvent().equals(
					BillingIntegrationService.EVENT_RECEIPT_CANCELLED)) {
				billRecpt.setIsCancelled(Boolean.TRUE);
			}
			billRecpt.setReceiptAmt(totalCollectedAmt.subtract(billRecpt
					.getReceiptAmt()));
		} else {
			throw new EGOVRuntimeException(
					" EgBill Object is null for the Bill Number"
							+ bri.getBillReferenceNum()
							+ "in updateBillReceiptForCancellation method");
		}
		return billRecpt;
	}

	/**
	 * API to update the bill details with the amount paid depending upon the
	 * event of the payment i.e payment or receipt cancellation or cheque bounce
	 * In case of Payment ,new EgBill is created and in case of cheque bounce
	 * only the EgBill is updated with necessary details(done for the DCB
	 * implementation)
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 *
	 * @return EgBill
	 *
	 * @throws InvalidAccountHeadException
	 */
	EgBill updateBillDetails(BillReceiptInfo bri)
			throws InvalidAccountHeadException {
		LOGGER.debug("-----Start of updateBillDetails----------------");
		EgBill egBill = null;
		billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		if (bri == null) {
			throw new EGOVRuntimeException(" BillReceiptInfo Object is null ");
		}
		egBill = (EgBill) billDao.findById(new Long(bri.getBillReferenceNum()),
				false);
		List<EgBillDetails> billDetList = billDetDao
				.getBillDetailsByBill(egBill);

		if (bri.getEvent() != null
				&& bri.getEvent().equals(
						BillingIntegrationService.EVENT_RECEIPT_CREATED)) {
			BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri,
					billDetList);
			egBill = updateBill(bri, egBill, totalCollectedAmt);
		} else if (bri.getEvent() != null
				&& bri.getEvent().equals(
						BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED)) {
			egBill = updateBillForChqBounce(bri, egBill, getTotalChequeAmt(bri));
		}
		LOGGER.debug("-----End of updateBillDetails----------------");
		return egBill;
	}

	/**
	 * Method will be called when a Cheque is Bounced .Here the EGBill and
	 * EgBillDetails tables are updated with the cancelled Cheque amount.
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 * @param org
	 *            .egov.demand.model.EgBill egBill
	 * @param java
	 *            .math.BigDecimal totalCollAmt totalCollectedAmt
	 *
	 * @return org.egov.demand.model.EgBill egBill
	 *
	 *
	 */

	public EgBill updateBillForChqBounce(BillReceiptInfo bri, EgBill egBill,
			BigDecimal totalChqAmt) {
		billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		BigDecimal zeroVal = BigDecimal.ZERO;
		if (totalChqAmt != null && !totalChqAmt.equals(zeroVal)
				&& egBill != null) {
			List<EgBillDetails> billList = new ArrayList<EgBillDetails>(
					egBill.getEgBillDetails());
			// Reversed the list because the knocking off the amount should
			// start from current Installment to least Installment.
			Collections.reverse(billList);
			BigDecimal carry = totalChqAmt;
			for (EgBillDetails billdet : billList) {
				BigDecimal balanceAmt = BigDecimal.ZERO;
				BigDecimal remAmount = BigDecimal.ZERO;
				balanceAmt = getEgBillDetailCollection(billdet);
				if (balanceAmt != null && balanceAmt.compareTo(zeroVal) > 0) {
					if (carry.compareTo(zeroVal) > 0
							&& carry.subtract(balanceAmt).compareTo(zeroVal) > 0) {
						carry = carry.subtract(balanceAmt);
						remAmount = balanceAmt;
					} else if (carry.compareTo(zeroVal) > 0
							&& carry.subtract(balanceAmt).compareTo(zeroVal) <= 0) {
						remAmount = carry;
						carry = BigDecimal.ZERO;
					}
					if (remAmount.compareTo(zeroVal) > 0) {
						billdet.setCollectedAmount(remAmount);
						billDetDao.update(billdet);
					}
				}
			}
			egBill.setTotalCollectedAmount(totalChqAmt);
			billDao.update(egBill);
		}
		return egBill;
	}

	/**
	 * Method will be called when a receipt is cancelled .Here the EGBill and
	 * EgBillDetails tables are updated with the cancelled amount and the mark
	 * the receipt status as cancelled in Eg_BillReceipt table. If the Glcode
	 * which exists in EgBillDetails is not matching with the Glcode the
	 * ErpCollection send ,then an exception will be
	 * raised(InvalidAccountHeadException).
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 * @param org
	 *            .egov.demand.model.EgBill
	 * @param java
	 *            .math.BigDecimal totalCollAmt
	 *
	 * @return org.egov.demand.model.EgBill egBill
	 *
	 * @throws InvalidAccountHeadException
	 *
	 */

	EgBill updateBillByCancelledRct(BillReceiptInfo bri, EgBill egBill,
			BigDecimal totalCollectedAmt) throws InvalidAccountHeadException {
		billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		if (bri != null && egBill != null && totalCollectedAmt != null) {
			List<EgBillDetails> billList = new ArrayList<EgBillDetails>(
					egBill.getEgBillDetails());
			// Reversed the list because the knocking off the amount should
			// start from current up to least installment.
			Collections.reverse(billList);
			for (EgBillDetails billDet : billList) {
				BigDecimal balanceAmt = getEgBillDetailCollection(billDet);
				Boolean glCodeExist = false;
				for (ReceiptAccountInfo acctDet : bri.getAccountDetails()) {
					if (billDet.getGlcode().equals(acctDet.getGlCode())) {
						glCodeExist = true;
						billDet.setCollectedAmount(acctDet.getCrAmount()
								.subtract(balanceAmt));
						billDetDao.update(billDet);
					}
				}
				if (!glCodeExist) {
					throw new InvalidAccountHeadException(
							"GlCode does not exist for " + billDet.getGlcode());
				}
			}
			egBill.setTotalCollectedAmount(totalCollectedAmt);
			billDao.update(egBill);
		}
		return egBill;
	}

	/**
	 * Here the EGBill and EgBillDetails tables are updated with the collected
	 * amount. If the Glcode which exists in EgBillDetails is not matching with
	 * the Glcode the ErpCollection send ,then an exception will be
	 * raised(InvalidAccountHeadException).
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 * @param org
	 *            .egov.demand.model.EgBill egBill
	 * @param java
	 *            .math.BigDecimal totalCollAmt totalCollectedAmt
	 *
	 * @return org.egov.demand.model.EgBill egBill
	 *
	 * @throws InvalidAccountHeadException
	 *
	 */

	private EgBill updateBill(BillReceiptInfo bri, EgBill egBill,
			BigDecimal totalCollectedAmt) throws InvalidAccountHeadException {
		billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
		billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		if (bri != null) {
			for (EgBillDetails billDet : egBill.getEgBillDetails()) {
				Boolean glCodeExist = false;
				for (ReceiptAccountInfo acctDet : bri.getAccountDetails()) {
					if (billDet.getGlcode().equals(acctDet.getGlCode())) {
						glCodeExist = true;
						BigDecimal amtCollected = billDet.getCollectedAmount();
						if (amtCollected == null) {
							amtCollected = BigDecimal.ZERO;
						}
						billDet.setCollectedAmount(acctDet.getCrAmount()
								.subtract(amtCollected));
						billDetDao.update(billDet);
					}
				}
				if (!glCodeExist) {
					throw new InvalidAccountHeadException(
							"GlCode does not exist for " + billDet.getGlcode());
				}
			}
			egBill.setTotalCollectedAmount(totalCollectedAmt);

			billDao.update(egBill);
		}
		return egBill;
	}

	/**
	 * Called to calculate the total Receipt Amount i,e the amount which has
	 * been paid for the Bill From the BillReceiptInfo the amount will be
	 * calculated. If the Glcode which exists in EgBillDetails is not matching
	 * with the Glcode the ErpCollection send ,then an exception will be
	 * raised(InvalidAccountHeadException).
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 * @param java
	 *            .util.List<EgBillDetails> billDetList
	 *
	 * @return java.math.BigDecimal totalCollAmt
	 *
	 * @throws InvalidAccountHeadException
	 */

	public BigDecimal calculateTotalCollectedAmt(BillReceiptInfo bri,
			List<EgBillDetails> billDetList) throws InvalidAccountHeadException {
		BigDecimal totalCollAmt = BigDecimal.ZERO;
		try {
			if (bri != null && billDetList != null) {
				for (EgBillDetails billDet : billDetList) {
					Boolean glCodeExist = false;
					for (ReceiptAccountInfo acctDet : bri.getAccountDetails()) {
						if (billDet.getGlcode().equals(acctDet.getGlCode())) {
							glCodeExist = true;
							totalCollAmt = totalCollAmt.add(acctDet
									.getCrAmount());
						}
					}
					if (!glCodeExist) {
						throw new InvalidAccountHeadException(
								"GlCode does not exist for "
										+ billDet.getGlcode());
					}
				}
			}
		} catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException(
					"Exception in calculate Total Collected Amt" + e);
		}

		return totalCollAmt;
	}

	/**
	 * Here we get the total cheque Amount (i.e the Amount in which the cheque
	 * gets bounced.)
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 *
	 * @return java.math.BigDecimal balanceAmt
	 *
	 * @exception org.egov.EGOVRuntimeException
	 */

	public BigDecimal getTotalChequeAmt(BillReceiptInfo bri) {
		BigDecimal totalCollAmt = BigDecimal.ZERO;
		try {
			if (bri != null) {
				for (ReceiptInstrumentInfo rctInst : bri
						.getBouncedInstruments()) {
					if (rctInst.getInstrumentAmount() != null) {
						totalCollAmt = totalCollAmt.add(rctInst
								.getInstrumentAmount());
					}
				}
			}
		} catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException(
					"Exception in calculate Total Collected Amt" + e);
		}

		return totalCollAmt;
	}

	/**
	 * Here we get the Bill DCB(Bill Amount - Collected amount).
	 *
	 * @param org
	 *            .egov.demand.model.EgBillDetails billdet
	 *
	 * @return java.math.BigDecimal balanceAmt
	 */

	public BigDecimal getEgBillDetailCollection(EgBillDetails billdet) {
		BigDecimal collectedAmt = billdet.getCollectedAmount();
		if (billdet.getCollectedAmount() == null) {
			collectedAmt = BigDecimal.ZERO;
		}
		return collectedAmt;

	}

	/**
	 * EgDemand and EgdemandDetails updation is a client specific .. So the
	 * Client which extends this abstract class needs to implement(override)
	 * this method.
	 *
	 * For Ex :- In PTIS Module: there will be only a base tax(basic Demand) in
	 * general, But in COCPTIS the base tax is divided into 4 individual taxes .
	 *
	 * If any info which is required is missed out then an exception needs to be
	 * raised with proper message
	 *
	 *
	 * @param org
	 *            .egov.infstr.collections.integration.models.BillReceiptInfo
	 *
	 */
	public abstract void updateDemandDetails(BillReceiptInfo bri);

	/**
	 * The module that defines the billing system.
	 * 
	 * @return
	 */
	protected abstract Module module();

	/**
	 * For a given date, finds the installment that contains it.
	 *
	 * @param date
	 * @return
	 */
	protected Installment getInstallmentForDate(Date date) {
		return commonsDAOFactory.getInstallmentDao()
				.getInsatllmentByModuleForGivenDate(module(), date);
	}

	/**
	 * Finds the currently active installment.
	 *
	 * @return
	 */
	protected Installment getCurrentInstallment() {
		return getInstallmentForDate(new Date());
	}

	/**
	 * Gets a list of all installments for the billing system.
	 *
	 * @return
	 */
	protected List<Installment> getAllInstallments() {
		return commonsDAOFactory.getInstallmentDao()
				.getInsatllmentByModule(module());
	}

	/**
	 * Finds the demand-reason-master having the given code.
	 *
	 * @param code
	 * @return
	 */
	protected EgDemandReasonMaster getDemandReasonMaster(String code) {
		DemandGenericDao dmdGenericDao = new DemandGenericHibDao();
		return dmdGenericDao.getDemandReasonMasterByCode(code, module());
	}

	/**
	 * Finds the demand-detail for the given installment and reason.
	 *
	 * @param egDemand
	 * @param instl
	 * @param code
	 * @return
	 */
	public EgDemandDetails getDemandDetail(EgDemand egDemand,
			Installment instl, String code) {
		DemandGenericDao dmdGenericDao = new DemandGenericHibDao();
		EgDemandDetails dmdDet = null;
		List<EgDemandDetails> dmdDetList = dmdGenericDao.getDmdDetailList(
				egDemand, instl, module(), getDemandReasonMaster(code));
		if (!dmdDetList.isEmpty()) {
			dmdDet = dmdDetList.get(0);
		}
		return dmdDet;
	}

	/**
	 * Finds the current reason for the given master and category.
	 *
	 * @param categoryCode
	 * @param reasonMasterCode
	 * @return
	 */
	protected EgDemandReason getCurrentReason(String categoryCode,
			String reasonMasterCode) {
		DemandGenericDao dmdGenDao = new DemandGenericHibDao();
		EgDemandReason reason = dmdGenDao
				.getEgDemandReasonByCodeInstallmentModule(reasonMasterCode,
						getCurrentInstallment(), module(), categoryCode);
		return reason;
	}

	/**
	 * Persists the Receipt Details with the EgDemandDetail id as the reference
	 * and the status as active.
	 * 
	 * @param egDemandDetails
	 * @param billRcptInfo
	 * @return egDmCollectedReceipt
	 */
	protected EgdmCollectedReceipt persistCollectedReceipts(
			EgDemandDetails egDemandDetails, String receiptNumber,
			BigDecimal receiptAmount, Date receiptDate, BigDecimal reasonAmount) {
		EgdmCollectedReceiptDao egdmCollectedReceiptDao = DCBDaoFactory
				.getDaoFactory().getEgdmCollectedReceiptsDao();
		EgdmCollectedReceipt egDmCollectedReceipt = new EgdmCollectedReceipt();
		egDmCollectedReceipt.setReceiptNumber(receiptNumber);
		egDmCollectedReceipt.setReceiptDate(receiptDate);
		egDmCollectedReceipt.setAmount(receiptAmount);
		egDmCollectedReceipt.setReasonAmount(reasonAmount);
		egDmCollectedReceipt.setStatus(DemandConstants.NEWRECEIPT);
		egDmCollectedReceipt.setEgdemandDetail(egDemandDetails);
		egdmCollectedReceiptDao.create(egDmCollectedReceipt);
		return egDmCollectedReceipt;
	}

	/**
	 * When receipt is cancelled the status of the receipt in
	 * EgdmCollectedReceipts is updated as Cancelled.
	 * 
	 * @param egDmCollectedReceipt
	 */
	protected void updateReceiptStatusWhenCancelled(String receiptNumber) {
		DemandGenericDao dmdGenDao = new DemandGenericHibDao();
		List<EgdmCollectedReceipt> egdmCollectedReceipts = dmdGenDao
				.getAllEgdmCollectedReceipts(receiptNumber);
		if (egdmCollectedReceipts != null && !egdmCollectedReceipts.isEmpty()) {
			for (EgdmCollectedReceipt egDmCollectedReceipt : egdmCollectedReceipts) {
				egDmCollectedReceipt
						.setStatus(DemandConstants.CANCELLED_RECEIPT);
				egDmCollectedReceipt.setUpdatedTime(new Date());
			}
		}
	}

	@Override
	public void apportionPaidAmount(String billReferenceNumber,
			BigDecimal actualAmountPaid, ArrayList<ReceiptDetail> receiptDetails) {
		apportionCollection(billReferenceNumber, actualAmountPaid,
				receiptDetails);
	}

	/**
	 * Billing system will implement this method when the billing system send
	 * "&lt;enablebillapportioning&gt; as true to Collection System in the
	 * bill-xml.
	 * 
	 * @param billRefNo
	 *            Bill Reference Number of the bill send by billing system
	 * @param amtPaid
	 *            Acutal amount paid at the counter
	 * @param receiptDetails
	 *            List of ReceiptDetails object associated with this bill
	 * @return void
	 */
	public void apportionCollection(String billRefNo, BigDecimal amtPaid,
			List<ReceiptDetail> receiptDetails) {
		return;
	}

	public CommonsDAOFactory getCommonsDAOFactory() {
		return commonsDAOFactory;
	}

	public void setCommonsDAOFactory(CommonsDAOFactory commonsDAOFactory) {
		this.commonsDAOFactory = commonsDAOFactory;
	}
	
}
