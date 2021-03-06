package org.egov.model.budget;

import java.math.BigDecimal;

import org.egov.model.budget.BudgetDetail;
import org.egov.utils.Constants;

public class BudgetProposalBean {
	private String accountCode;
	private Long id = null;
	private Long nextYrId = null;
	private String budget="";
	private String fund="";
	private String function="";
	private String budgetGroup="";
	private String executingDepartment="";
	private String previousYearActuals;
	private String twoPreviousYearActuals;
	private String currentYearActuals;
	private String currentYearBE;
	private String reappropriation;
	private String total;
	private String anticipatory;
	private BigDecimal proposedRE = BigDecimal.ZERO;
	private BigDecimal proposedBE = BigDecimal.ZERO;
	private BigDecimal approvedRE = BigDecimal.ZERO;
	private BigDecimal approvedBE = BigDecimal.ZERO;
	private String remarks="";
	private Long documentNumber;
	private Long stateId;
	private String rowType="detail";
	private String reference;

	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public BudgetProposalBean(){
		
	}
	public BudgetProposalBean(String name, String type){
		this.budgetGroup = name;
		this.rowType = type;
		this.proposedBE=null;
		this.proposedRE=null;
		this.approvedRE=null;
		this.approvedBE=null;
	}
	
	public BudgetProposalBean(String name, String type, String ref){
		this.budgetGroup = name;
		this.rowType = type;
		this.reference = ref;
		//this.proposedBE=null;
		//this.proposedRE=null;
	}
	
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getBudgetGroup() {
		return budgetGroup;
	}
	public void setBudgetGroup(String budgetGroup) {
		this.budgetGroup = budgetGroup;
	}
	public String getPreviousYearActuals() {
		return previousYearActuals;
	}
	public void setPreviousYearActuals(String previousYearActuals) {
		this.previousYearActuals = previousYearActuals;
	}
	public String getTwoPreviousYearActuals() {
		return twoPreviousYearActuals;
	}
	public void setTwoPreviousYearActuals(String twoPreviousYearActuals) {
		this.twoPreviousYearActuals = twoPreviousYearActuals;
	}
	public String getCurrentYearActuals() {
		return currentYearActuals;
	}
	public void setCurrentYearActuals(String currentYearActuals) {
		this.currentYearActuals = currentYearActuals;
	}
	public String getCurrentYearBE() {
		return currentYearBE;
	}
	public void setCurrentYearBE(String currentYearBE) {
		this.currentYearBE = currentYearBE;
	}
	public String getReappropriation() {
		return reappropriation;
	}
	public void setReappropriation(String reappropriation) {
		this.reappropriation = reappropriation;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAnticipatory() {
		return anticipatory;
	}
	public void setAnticipatory(String anticipatory) {
		this.anticipatory = anticipatory;
	}
	public BigDecimal getProposedRE() {
		return proposedRE;
	}
	public void setProposedRE(BigDecimal proposedRE) {
		this.proposedRE = proposedRE;
	}
	public BigDecimal getProposedBE() {
		return proposedBE;
	}
	public void setProposedBE(BigDecimal proposedBE) {
		this.proposedBE = proposedBE;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getExecutingDepartment() {
		return executingDepartment;
	}
	public void setExecutingDepartment(String executingDepartment) {
		this.executingDepartment = executingDepartment;
	}
	public BigDecimal getApprovedRE() {
		return approvedRE;
	}
	public void setApprovedRE(BigDecimal approvedRE) {
		this.approvedRE = approvedRE;
	}
	public BigDecimal getApprovedBE() {
		return approvedBE;
	}
	public void setApprovedBE(BigDecimal approvedBE) {
		this.approvedBE = approvedBE;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNextYrId() {
		return nextYrId;
	}
	public void setNextYrId(Long nextYrId) {
		this.nextYrId = nextYrId;
	}
	public Long getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getRowType() {
		return rowType;
	}
	public void setRowType(String rowType) {
		this.rowType = rowType;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

}
