package org.egov.egf.masters.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author mani
 * Simple helper class to get project code id,name,code etc 
 * can be used for other entities also
 */
public class LoanGrantBean {
	private  Long id;
	private  String code;
	private  String name;
	private  String voucherNumber;
	private  Date voucherDate;
	private  BigDecimal amount;
	private  BigDecimal agencyAmount;
	private  BigDecimal grantAmount;
	private  BigDecimal loanAmount;
	private  String subScheme;
	private  String agencyName;
	private  BigDecimal percentAmount;
	private  String status;
	private  Integer detailKey;  
	private  Integer detailType;
	private BigDecimal balance;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getSubScheme() {
		return subScheme;
	}
	public void setSubScheme(String subScheme) {
		this.subScheme = subScheme;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public BigDecimal getPercentAmount() {
		return percentAmount;
	}
	public void setPercentAmount(BigDecimal percentAmount) {
		this.percentAmount = percentAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getDetailKey() {
		return detailKey;
	}
	public void setDetailKey(Integer detailKey) {
		this.detailKey = detailKey;
	}
	public Integer getDetailType() {
		return detailType;
	}
	/**
	 * @param amount
	 * @param agencyAmount
	 * @param grantAmount
	 * @param agencyName
	 * @param status
	 */
	public LoanGrantBean(BigDecimal amount, BigDecimal agencyAmount, BigDecimal grantAmount, String agencyName, String status) {
		super();
		this.amount = amount;
		this.agencyAmount = agencyAmount;
		this.grantAmount = grantAmount;
		this.agencyName = agencyName;
		this.status = status;
	}
	public LoanGrantBean(BigDecimal agencyAmount, BigDecimal loanAmount, String agencyName ) {
		super();
		this.amount = amount;
		this.agencyAmount = agencyAmount;
		this.grantAmount = grantAmount;
		this.agencyName = agencyName;
		this.status = status;
	}
	/**
	 * 
	 */
	public LoanGrantBean() {
		super();
	}
	public void setDetailType(Integer detailType) {
		this.detailType = detailType;
	}
	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}
	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}
	public BigDecimal getGrantAmount() {
		return grantAmount;
	}
	public void setGrantAmount(BigDecimal grantAmount) {
		this.grantAmount = grantAmount;
	}
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}  
	

}
