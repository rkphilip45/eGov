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

public class PaymentInfoCard implements PaymentInfo {

	private String instrumentNumber;
	private BigDecimal instrumentAmount;
	private String transactionNumber;
	private String expMonth;
	private String expYear;
	private String cvvNumber;
	//Enum for Card Type M: Master Card, V:Visa
	public enum CARDTYPE {M, V};
	public CARDTYPE cardTypeValue;
	
	/**
	 * Default Constructor
	 */
	public PaymentInfoCard(){
		
	}
	
	public PaymentInfoCard(String instrumentNumber,
			BigDecimal instrumentAmount, String transactionNumber,
			String expMonth, String expYear, String cvvNumber,CARDTYPE cardTypeValue) {
		this.instrumentNumber = instrumentNumber;
		this.instrumentAmount = instrumentAmount;
		this.transactionNumber = transactionNumber;
		this.expMonth = expMonth;
		this.expYear = expYear;
		this.cvvNumber = cvvNumber;
		this.cardTypeValue = cardTypeValue;
	}
	
	@Override
	public BigDecimal getInstrumentAmount() {
		return instrumentAmount;
	}

	@Override
	public TYPE getInstrumentType() {
		return TYPE.card;
	}

	/**
	 * @return the instrumentNumber
	 */
	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	/**String
	 * @return the transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param instrumentNumber the instrumentNumber to set
	 */
	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	/**
	 * @param instrumentAmount the instrumentAmount to set
	 */
	public void setInstrumentAmount(BigDecimal instrumentAmount) {
		this.instrumentAmount = instrumentAmount;
	}

	/**
	 * @param transactionNumber the transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public String getCvvNumber() {
		return cvvNumber;
	}

	public void setCvvNumber(String cvvNumber) {
		this.cvvNumber = cvvNumber;
	}
	
	public CARDTYPE getCardTypeValue() {
		return cardTypeValue;
	}

	public void setCardTypeValue(CARDTYPE cardType) {
		this.cardTypeValue = cardType;
	}

}
