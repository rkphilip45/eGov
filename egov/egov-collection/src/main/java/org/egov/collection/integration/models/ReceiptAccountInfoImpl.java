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
package org.egov.collection.integration.models;

import java.math.BigDecimal;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.utils.FinancialsUtil;
/**
 * Provides account information for receipts
 */
public class ReceiptAccountInfoImpl implements ReceiptAccountInfo {
	/**
	 * This is used to check if an account is a revenue account.
	 */
	private final boolean isRevenueAccount;
	/**
	 * The private instance of receipt detail.
	 * This is used by all public getters.
	 */
	private final ReceiptDetail receiptDetail;
	
	/**
	 * Creates the receipt account info for given receipt detail.
	 * @param receiptDetail The receipt detail object
	 */
	public ReceiptAccountInfoImpl(ReceiptDetail receiptDetail) {
		this.receiptDetail = receiptDetail;
		this.isRevenueAccount = FinancialsUtil.isRevenueAccountHead(this.receiptDetail.getAccounthead(),FinancialsUtil.getBankChartofAccountCodeList());
	}
	
	@Override
	public String toString() {
	    return receiptDetail.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getGlCode()
	 */
	public String getGlCode() {
		return receiptDetail.getAccounthead()==null?
				null:receiptDetail.getAccounthead().getGlcode();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getAccountName()
	 */
	public String getAccountName() {
		return receiptDetail.getAccounthead()==null?
				null : receiptDetail.getAccounthead().getName();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getFunction()
	 */
	public String getFunction() {
		return receiptDetail.getFunction()==null?
				null:receiptDetail.getFunction().getCode();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getFunctionName()
	 */
	public String getFunctionName() {
		return receiptDetail.getFunction()==null?
				null:receiptDetail.getFunction().getName();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getDrAmount()
	 */
	public BigDecimal getDrAmount() {
		return receiptDetail.getDramount();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getCrAmount()
	 */
	public BigDecimal getCrAmount() {
		return receiptDetail.getCramount();
	}

	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getIsRevenueAccount()
	 */
	public boolean getIsRevenueAccount() {
		return this.isRevenueAccount;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getOrderNumber()
	 */
	public Long getOrderNumber() {
		return receiptDetail.getOrdernumber()==null?
				null:receiptDetail.getOrdernumber();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getDescription()
	 */
	public String getDescription() {
		return receiptDetail.getDescription();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptAccountInfo#getFinancialYear()
	 */
	public String getFinancialYear(){
		return receiptDetail.getFinancialYear()==null?
				null:receiptDetail.getFinancialYear().getFinYearRange();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.egov.infstr.collections.integration.models.IReceiptAccountInfo#
	 * getCreditAmountToBePaid()
	 */
	public BigDecimal getCreditAmountToBePaid() {
		return receiptDetail.getCramountToBePaid();
	}
}
