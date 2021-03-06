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
package org.egov.commons;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Fund implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Fund fund;

	private CChartOfAccounts chartofaccountsByRecvglcodeid;

	private EgfAccountcodePurpose egfAccountcodePurpose;

	private CChartOfAccounts chartofaccountsByPayglcodeid;

	private String code;

	private String name;

	private BigDecimal llevel;

	private BigDecimal openingdebitbalance;

	private BigDecimal openingcreditbalance;

	private BigDecimal transactiondebitamount;

	private BigDecimal transactioncreditamount;

	private boolean isactive;

	private Date lastmodified;

	private Date created;

	private BigDecimal modifiedby;

	private Boolean isnotleaf;

	private Character identifier;

	private Set voucherheaders = new HashSet(0);

	private Set funds = new HashSet(0);

	public Fund() {
		//For hibernate to work
	}

	public Fund(Integer id, String code, BigDecimal llevel, BigDecimal openingdebitbalance, BigDecimal openingcreditbalance, BigDecimal transactiondebitamount, BigDecimal transactioncreditamount, boolean isactive, Date created) {
		this.id = id;
		this.code = code;
		this.llevel = llevel;
		this.openingdebitbalance = openingdebitbalance;
		this.openingcreditbalance = openingcreditbalance;
		this.transactiondebitamount = transactiondebitamount;
		this.transactioncreditamount = transactioncreditamount;
		this.isactive = isactive;
		this.created = created;
	}

	public Fund(Integer id, Fund fund, CChartOfAccounts chartofaccountsByRecvglcodeid, EgfAccountcodePurpose egfAccountcodePurpose, CChartOfAccounts chartofaccountsByPayglcodeid, String code, String name, BigDecimal llevel, BigDecimal openingdebitbalance,
			BigDecimal openingcreditbalance, BigDecimal transactiondebitamount, BigDecimal transactioncreditamount, boolean isactive, Date lastmodified, Date created, BigDecimal modifiedby, Boolean isnotleaf, Character identifier, Set voucherheaders, Set funds) {
		this.id = id;
		this.fund = fund;
		this.chartofaccountsByRecvglcodeid = chartofaccountsByRecvglcodeid;
		this.egfAccountcodePurpose = egfAccountcodePurpose;
		this.chartofaccountsByPayglcodeid = chartofaccountsByPayglcodeid;
		this.code = code;
		this.name = name;
		this.llevel = llevel;
		this.openingdebitbalance = openingdebitbalance;
		this.openingcreditbalance = openingcreditbalance;
		this.transactiondebitamount = transactiondebitamount;
		this.transactioncreditamount = transactioncreditamount;
		this.isactive = isactive;
		this.lastmodified = lastmodified;
		this.created = created;
		this.modifiedby = modifiedby;
		this.isnotleaf = isnotleaf;
		this.identifier = identifier;
		this.voucherheaders = voucherheaders;
		this.funds = funds;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Fund getFund() {
		return this.fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public CChartOfAccounts getChartofaccountsByRecvglcodeid() {
		return this.chartofaccountsByRecvglcodeid;
	}

	public void setChartofaccountsByRecvglcodeid(CChartOfAccounts chartofaccountsByRecvglcodeid) {
		this.chartofaccountsByRecvglcodeid = chartofaccountsByRecvglcodeid;
	}

	public EgfAccountcodePurpose getEgfAccountcodePurpose() {
		return this.egfAccountcodePurpose;
	}

	public void setEgfAccountcodePurpose(EgfAccountcodePurpose egfAccountcodePurpose) {
		this.egfAccountcodePurpose = egfAccountcodePurpose;
	}

	public CChartOfAccounts getChartofaccountsByPayglcodeid() {
		return this.chartofaccountsByPayglcodeid;
	}

	public void setChartofaccountsByPayglcodeid(CChartOfAccounts chartofaccountsByPayglcodeid) {
		this.chartofaccountsByPayglcodeid = chartofaccountsByPayglcodeid;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getLlevel() {
		return this.llevel;
	}

	public void setLlevel(BigDecimal llevel) {
		this.llevel = llevel;
	}

	public BigDecimal getOpeningdebitbalance() {
		return this.openingdebitbalance;
	}

	public void setOpeningdebitbalance(BigDecimal openingdebitbalance) {
		this.openingdebitbalance = openingdebitbalance;
	}

	public BigDecimal getOpeningcreditbalance() {
		return this.openingcreditbalance;
	}

	public void setOpeningcreditbalance(BigDecimal openingcreditbalance) {
		this.openingcreditbalance = openingcreditbalance;
	}

	public BigDecimal getTransactiondebitamount() {
		return this.transactiondebitamount;
	}

	public void setTransactiondebitamount(BigDecimal transactiondebitamount) {
		this.transactiondebitamount = transactiondebitamount;
	}

	public BigDecimal getTransactioncreditamount() {
		return this.transactioncreditamount;
	}

	public void setTransactioncreditamount(BigDecimal transactioncreditamount) {
		this.transactioncreditamount = transactioncreditamount;
	}

	public boolean isIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public Date getLastmodified() {
		return this.lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public BigDecimal getModifiedby() {
		return this.modifiedby;
	}

	public void setModifiedby(BigDecimal modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Boolean getIsnotleaf() {
		return this.isnotleaf;
	}

	public void setIsnotleaf(Boolean isnotleaf) {
		this.isnotleaf = isnotleaf;
	}

	public Character getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(Character identifier) {
		this.identifier = identifier;
	}

	public Set getVoucherheaders() {
		return this.voucherheaders;
	}

	public void setVoucherheaders(Set voucherheaders) {
		this.voucherheaders = voucherheaders;
	}

	public Set getFunds() {
		return this.funds;
	}

	public void setFunds(Set funds) {
		this.funds = funds;
	}

}
