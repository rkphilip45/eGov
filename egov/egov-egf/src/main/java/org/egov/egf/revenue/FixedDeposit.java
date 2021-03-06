package org.egov.egf.revenue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.models.BaseModel;
import org.egov.model.instrument.InstrumentHeader;

public class FixedDeposit extends BaseModel {
	private String fileNo;
	private BigDecimal amount;
	private Date date;
	private Bankbranch bankBranch;
	private Bankaccount bankAccount;
	private BigDecimal interestRate;
	private String period;
	private String serialNumber;
	private CVoucherHeader outFlowVoucher; // GJV
	private BigDecimal gjvAmount;
	private BigDecimal receiptAmount;
	private BigDecimal maturityAmount;
	private Date maturityDate;
	private Date withdrawalDate;
	private CVoucherHeader inFlowVoucher;
	private CVoucherHeader challanReceiptVoucher;
	private InstrumentHeader instrumentHeader;
	private String remarks;
	private String referenceNumber;
	private FixedDeposit parentId;
	private boolean extend;
	private boolean extendTemp;
	private Long parentTemp;
	private List<Bankaccount> bankAccountList = new ArrayList<Bankaccount>();

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public Bankbranch getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(Bankbranch bankBranch) {
		this.bankBranch = bankBranch;
	}

	public BigDecimal getGjvAmount() {
		return gjvAmount;
	}

	public void setGjvAmount(BigDecimal gjvAmount) {
		this.gjvAmount = gjvAmount;
	}

	public Long getParentTemp() {
		return parentTemp;
	}

	public void setParentTemp(Long parentTemp) {
		this.parentTemp = parentTemp;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public boolean isExtendTemp() {
		return extendTemp;
	}

	public void setExtendTemp(boolean extendTemp) {
		this.extendTemp = extendTemp;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public InstrumentHeader getInstrumentHeader() {
		return instrumentHeader;
	}

	public void setInstrumentHeader(InstrumentHeader instrumentHeader) {
		this.instrumentHeader = instrumentHeader;
	}

	public List<Bankaccount> getBankAccountList() {
		return bankAccountList;
	}

	public void setBankAccountList(List<Bankaccount> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	public BigDecimal getMaturityAmount() {
		return maturityAmount;
	}

	public void setMaturityAmount(BigDecimal maturityAmount) {
		this.maturityAmount = maturityAmount;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Date getWithdrawalDate() {
		return withdrawalDate;
	}

	public void setWithdrawalDate(Date withdrawalDate) {
		this.withdrawalDate = withdrawalDate;
	}

	public Bankaccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Bankaccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public CVoucherHeader getOutFlowVoucher() {
		return outFlowVoucher;
	}

	public void setOutFlowVoucher(CVoucherHeader outFlowVoucher) {
		this.outFlowVoucher = outFlowVoucher;
	}

	public CVoucherHeader getInFlowVoucher() {
		return inFlowVoucher;
	}

	public void setInFlowVoucher(CVoucherHeader inFlowVoucher) {
		this.inFlowVoucher = inFlowVoucher;
	}

	public CVoucherHeader getChallanReceiptVoucher() {
		return challanReceiptVoucher;
	}

	public void setChallanReceiptVoucher(CVoucherHeader challanReceiptVoucher) {
		this.challanReceiptVoucher = challanReceiptVoucher;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public FixedDeposit getParentId() {
		return parentId;
	}

	public void setParentId(FixedDeposit parentId) {
		this.parentId = parentId;
	}

	public boolean getExtend() {
		return extend;
	}

	public void setExtend(boolean extend) {
		this.extend = extend;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
