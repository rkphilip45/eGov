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
import java.util.Date;

import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.EgwStatus;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.utils.FinancialConstants;

/**
 * Provides instrument information for a receipt
 */
public class ReceiptInstrumentInfoImpl implements ReceiptInstrumentInfo {
	/**
	 * The private instrument header. All the getters use this to provide the
	 * data.
	 */
	private final InstrumentHeader instrumentHeader;

	/**
	 * Creates the instrument information object from given instrument header
	 * 
	 * @param instrumentHeader
	 *            the instrument header object
	 */
	public ReceiptInstrumentInfoImpl(InstrumentHeader instrumentHeader) {
		this.instrumentHeader = instrumentHeader;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentNumber()*/
	 
	public String getInstrumentNumber() {
		return instrumentHeader.getInstrumentNumber();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentDate()
	 */
	public Date getInstrumentDate() {
		return instrumentHeader.getInstrumentDate();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentType()
	 */
	public String getInstrumentType() {
		return instrumentHeader.getInstrumentType() == null ? null : instrumentHeader
				.getInstrumentType().getType();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentAmount()
	 */
	public BigDecimal getInstrumentAmount() {
		return instrumentHeader.getInstrumentAmount();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getInstrumentStatus()
	 */
	public EgwStatus getInstrumentStatus() {
		return instrumentHeader.getStatusId();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getTransactionNumber()
	 */
	public String getTransactionNumber() {
		return instrumentHeader.getTransactionNumber();
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getTransactionDate()
	 */
	public Date getTransactionDate() {
		return instrumentHeader.getTransactionDate();
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#isBounced()
	 */
	public boolean isBounced() {
		return instrumentHeader.getStatusId().getDescription().equals(
				FinancialConstants.INSTRUMENT_DISHONORED_STATUS);
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getBankName()
	*/ 
	public String getBankName() {
		if(CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrumentHeader.getInstrumentType().getType())){
			Bankaccount bankAccount = instrumentHeader.getBankAccountId();
			return bankAccount.getBankbranch().getBank().getName();
		}
		Bank bank = instrumentHeader.getBankId();
		if (bank == null) {
			return null;
		} else {
			return bank.getName();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getBankAccountNumber()
	 */
	public String getBankAccountNumber() {
		Bankaccount bankAccount = instrumentHeader.getBankAccountId();
		if (bankAccount == null) {
			return null;
		} else {
			return bankAccount.getAccountnumber();
		}
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.collections.integration.models.IReceiptInstrumentInfo#getBankBranchName()
	 */
	public String getBankBranchName() {
		return instrumentHeader.getBankBranchName();
	}
}