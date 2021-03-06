/**
 * 
 */
package org.egov.model.contra;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;

/**
 * @author msahoo
 *
 */
public class TransactionSummary{	
	private Integer id;
	private Accountdetailtype accountdetailtype;
	private CFinancialYear financialyear;
	private Fundsource fundsource;
	private Fund fund;
	private CChartOfAccounts glcodeid;
	private BigDecimal openingdebitbalance;
	private BigDecimal openingcreditbalance;
	private BigDecimal debitamount;
	private BigDecimal creditamount;
	private Integer accountdetailkey;
	private String narration;
	private User modifiedBy;
	private Date modifiedDate;
	private Department departmentid;
	private Functionary functionaryid;
	private CFunction functionid;
	private Integer divisionid;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Accountdetailtype getAccountdetailtype() {
		return accountdetailtype;
	}
	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}
	public CFinancialYear getFinancialyear() {
		return financialyear;
	}
	public void setFinancialyear(CFinancialYear financialyear) {
		this.financialyear = financialyear;
	}
	public Fundsource getFundsource() {
		return fundsource;
	}
	public void setFundsource(Fundsource fundsource) {
		this.fundsource = fundsource;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public CChartOfAccounts getGlcodeid() {
		return glcodeid;
	}
	public void setGlcodeid(CChartOfAccounts glcodeid) {
		this.glcodeid = glcodeid;
	}
	public BigDecimal getOpeningdebitbalance() {
		return openingdebitbalance;
	}
	public void setOpeningdebitbalance(BigDecimal openingdebitbalance) {
		this.openingdebitbalance = openingdebitbalance;
	}
	public BigDecimal getOpeningcreditbalance() {
		return openingcreditbalance;
	}
	public void setOpeningcreditbalance(BigDecimal openingcreditbalance) {
		this.openingcreditbalance = openingcreditbalance;
	}
	public BigDecimal getDebitamount() {
		return debitamount;
	}
	public void setDebitamount(BigDecimal debitamount) {
		this.debitamount = debitamount;
	}
	public BigDecimal getCreditamount() {
		return creditamount;
	}
	public void setCreditamount(BigDecimal creditamount) {
		this.creditamount = creditamount;
	}
	public Integer getAccountdetailkey() {
		return accountdetailkey;
	}
	public void setAccountdetailkey(Integer accountdetailkey) {
		this.accountdetailkey = accountdetailkey;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public User getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Functionary getFunctionaryid() {
		return functionaryid;
	}
	public void setFunctionaryid(Functionary functionaryid) {
		this.functionaryid = functionaryid;
	}
	public CFunction getFunctionid() {
		return functionid;
	}
	public void setFunctionid(CFunction functionid) {
		this.functionid = functionid;
	}
	public Integer getDivisionid() {
		return divisionid;
	}
	public void setDivisionid(Integer divisionid) {
		this.divisionid = divisionid;
	}
	
	public Department getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Department departmentid) {
		this.departmentid = departmentid;
	}
}
