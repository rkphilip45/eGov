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
package org.egov.infstr.models;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;

public class ServiceAccountDetails {

	private Long id;

	private BigDecimal amount = BigDecimal.ZERO;

	private CChartOfAccounts glCodeId;

	private ServiceDetails serviceDetails;

	private CFunction function;

	private Set<ServiceSubledgerInfo> subledgerDetails = new LinkedHashSet<ServiceSubledgerInfo>(0);

	public void addSubledgerDetails(ServiceSubledgerInfo subledgerInfo) {
		getSubledgerDetails().add(subledgerInfo);
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return null != this.amount ? this.amount.setScale(2, BigDecimal.ROUND_HALF_EVEN) : null;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the glCodeId
	 */
	public CChartOfAccounts getGlCodeId() {
		return glCodeId;
	}

	/**
	 * @param glCodeId the glCodeId to set
	 */
	public void setGlCodeId(CChartOfAccounts glCodeId) {
		this.glCodeId = glCodeId;
	}

	/**
	 * @return the serviceDetails
	 */
	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	/**
	 * @param serviceDetails the serviceDetails to set
	 */
	public void setServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Set<ServiceSubledgerInfo> getSubledgerDetails() {
		return subledgerDetails;
	}

	public void setSubledgerDetails(Set<ServiceSubledgerInfo> subledgerDetails) {
		this.subledgerDetails = subledgerDetails;
	}

	public CFunction getFunction() {
		return function;
	}

	public void setFunction(CFunction function) {
		this.function = function;
	}

}
