package org.egov.model.instrument;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;

public class DishonorCheque extends StateAware {

	private InstrumentHeader instrumentHeader;
	private Integer payinSlipCreator;
	private User payinSlipCreatorUser;
	private CVoucherHeader originalVoucherHeader;
	private EgwStatus status;
	private BigDecimal bankChargesAmt;
	


	private CChartOfAccounts bankchargeGlCodeId;
	private Date transactionDate;
	private String bankReferenceNumber; 
	private String instrumentDishonorReason;
	private String bankreason;
	private CVoucherHeader reversalVoucherHeader;
	private CVoucherHeader bankchargesVoucherHeader;
	private boolean firstStepWk;
	private Set<DishonorChequeDetails> details = new HashSet<DishonorChequeDetails>(0);

	@Override
	public String getStateDetails() {
		String	instInfo="Instrument Number :"+getInstrumentHeader().getInstrumentNumber()+" Amount : "+getInstrumentHeader().getInstrumentAmount();
		return instInfo;
	}


	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Integer getPayinSlipCreator() {
		return payinSlipCreator;
	}

	public User getPayinSlipCreatorUser() {
		return payinSlipCreatorUser;
	}
               

	public void setPayinSlipCreatorUser(User payinSlipCreatorUser) {
		this.payinSlipCreatorUser = payinSlipCreatorUser;
	}

	public void setPayinSlipCreator(Integer payinSlipCreator) {
		this.payinSlipCreator = payinSlipCreator;
	}


	public String getBankReferenceNumber() {
		return bankReferenceNumber;
	}

	public void setBankReferenceNumber(String bankReferenceNumber) {
		this.bankReferenceNumber = bankReferenceNumber;
	}
	public InstrumentHeader getInstrumentHeader() {
		return instrumentHeader;
	}
	public void setInstrumentHeader(InstrumentHeader instrumentHeader) {
		this.instrumentHeader = instrumentHeader;
	}
	public CVoucherHeader getOriginalVoucherHeader() {
		return originalVoucherHeader;
	}
	public void setOriginalVoucherHeader(CVoucherHeader originalVoucherHeader) {
		this.originalVoucherHeader = originalVoucherHeader;
	}
	public EgwStatus getStatus() {
		return status;
	}
	public void setStatus(EgwStatus status) {
		this.status = status;
	}

	public BigDecimal getBankChargesAmt() {
		return bankChargesAmt;
	}
	public void setBankChargesAmt(BigDecimal bankChargesAmt) {
		this.bankChargesAmt = bankChargesAmt;
	}

	public CChartOfAccounts getBankchargeGlCodeId() {
		return bankchargeGlCodeId;
	}
	public void setBankchargeGlCodeId(CChartOfAccounts bankchargeGlCodeId) {
		this.bankchargeGlCodeId = bankchargeGlCodeId;
	}
	public CVoucherHeader getReversalVoucherHeader() {
		return reversalVoucherHeader;
	}
	public void setReversalVoucherHeader(CVoucherHeader reversalVoucherHeader) {
		this.reversalVoucherHeader = reversalVoucherHeader;
	}
	public CVoucherHeader getBankchargesVoucherHeader() {
		return bankchargesVoucherHeader;
	}
	public void setBankchargesVoucherHeader(CVoucherHeader bankchargesVoucherHeader) {
		this.bankchargesVoucherHeader = bankchargesVoucherHeader;
	}
	public Set<DishonorChequeDetails> getDetails() {
		return details;
	}
	public void setDetails(Set<DishonorChequeDetails> details) {
		this.details = details;
	}

	public Set<DishonorChequeDetails> addDishonorChqDetails(DishonorChequeDetails chqDet)
	{
		details.add(chqDet);
		return details;
	}


	public boolean isFirstStepWk() {
		return firstStepWk;
	}


	public void setFirstStepWk(boolean firstStepWk) {
		this.firstStepWk = firstStepWk;
	}


	public String getInstrumentDishonorReason() {
		return instrumentDishonorReason;
	}


	public String getBankreason() {
		return bankreason;
	}


	public void setInstrumentDishonorReason(String instrumentDishonorReason) {
		this.instrumentDishonorReason = instrumentDishonorReason;
	}


	public void setBankreason(String bankreason) {
		this.bankreason = bankreason;
	}


}
