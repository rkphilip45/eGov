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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.admin.master.entity.Department;

public class ServiceDetails extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String serviceName;
	private String serviceUrl;
	private Boolean isEnabled;
	private String callBackurl;
	private String serviceType;
	private String code;
	private Boolean voucherCreation;
	private Boolean isVoucherApproved;
	private Set<Bankaccount> serviceBankAccount = new HashSet<Bankaccount>(0);
	private Set<CChartOfAccounts> serviceAccount = new HashSet<CChartOfAccounts>(0);
	private Fund fund;
	private Fundsource fundSource;

	private Functionary functionary;

	private Scheme scheme;
	private SubScheme subscheme;
	private ServiceCategory serviceCategory;

	private Set<Department> serviceDept = new LinkedHashSet<Department>(0);

	private Set<ServiceAccountDetails> serviceAccountDtls = new LinkedHashSet<ServiceAccountDetails>(0);

	public void addServiceAccountDtls(ServiceAccountDetails account) {
		getServiceAccountDtls().add(account);
	}

	public void addServiceDept(Department dept) {
		getServiceDept().add(dept);
	}

	/**
	 * @return the serviceName
	 */
	@Required(message = "service.name.null")
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}

	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(Boolean isEnabled) {
		if (null == isEnabled) {
			this.isEnabled = Boolean.FALSE;
		} else {
			this.isEnabled = isEnabled;
		}
	}

	/**
	 * @return the callBackurl
	 */
	public String getCallBackurl() {
		return callBackurl;
	}

	/**
	 * @param callBackurl the callBackurl to set
	 */
	public void setCallBackurl(String callBackurl) {
		this.callBackurl = callBackurl;
	}

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the code
	 */
	@Required(message = "service.code.null")
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the serviceBankAccount
	 */
	public Set<Bankaccount> getServiceBankAccount() {
		return serviceBankAccount;
	}

	/**
	 * @param serviceBankAccount the serviceBankAccount to set
	 */
	public void setServiceBankAccount(Set<Bankaccount> serviceBankAccount) {
		this.serviceBankAccount = serviceBankAccount;
	}

	/**
	 * Add to servicebankAccount set
	 * @param bankaccount
	 */
	public void addBankAccount(Bankaccount bankaccount) {
		this.serviceBankAccount.add(bankaccount);
	}

	/**
	 * @return the fund
	 */
	public Fund getFund() {
		return fund;
	}

	/**
	 * @param fund the fund to set
	 */
	public void setFund(Fund fund) {
		this.fund = fund;
	}

	/**
	 * @return the fundSource
	 */
	public Fundsource getFundSource() {
		return fundSource;
	}

	/**
	 * @param fundSource the fundSource to set
	 */
	public void setFundSource(Fundsource fundSource) {
		this.fundSource = fundSource;
	}

	/**
	 * @return the functionary
	 */
	public Functionary getFunctionary() {
		return functionary;
	}

	/**
	 * @param functionary the functionary to set
	 */
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}

	/**
	 * @return the serviceAccount
	 */
	public Set<CChartOfAccounts> getServiceAccount() {
		return serviceAccount;
	}

	/**
	 * @param serviceAccount the serviceAccount to set
	 */
	public void setServiceAccount(Set<CChartOfAccounts> serviceAccount) {
		this.serviceAccount = serviceAccount;
	}

	/**
	 * Add to serviceAccount set
	 * @param CChartOfAccounts
	 */
	public void addAccount(CChartOfAccounts account) {
		this.serviceAccount.add(account);
	}

	/**
	 * @return the scheme
	 */
	public Scheme getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the subscheme
	 */
	public SubScheme getSubscheme() {
		return subscheme;
	}

	/**
	 * @param subscheme the subscheme to set
	 */
	public void setSubscheme(SubScheme subscheme) {
		this.subscheme = subscheme;
	}

	/**
	 * @return the serviceCategory
	 */
	public ServiceCategory getServiceCategory() {
		return serviceCategory;
	}

	/**
	 * @param serviceCategory the serviceCategory to set
	 */
	public void setServiceCategory(ServiceCategory serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	/**
	 * @return the serviceDept
	 */
	public Set<Department> getServiceDept() {
		return serviceDept;
	}

	/**
	 * @param serviceDept the serviceDept to set
	 */
	public void setServiceDept(Set<Department> serviceDept) {
		this.serviceDept = serviceDept;
	}

	public Set<ServiceAccountDetails> getServiceAccountDtls() {
		return serviceAccountDtls;
	}

	public void setServiceAccountDtls(Set<ServiceAccountDetails> serviceAccountDtls) {
		this.serviceAccountDtls = serviceAccountDtls;
	}

	public Boolean getVoucherCreation() {
		return voucherCreation;
	}

	public void setVoucherCreation(Boolean voucherCreation) {
		if (null == voucherCreation) {
			this.voucherCreation = Boolean.FALSE;
		} else {
			this.voucherCreation = voucherCreation;
		}

	}

	/**
	 * @return the isVoucherApproved
	 */
	public Boolean getIsVoucherApproved() {
		return isVoucherApproved;
	}

	/**
	 * @param isVoucherApproved the isVoucherApproved to set
	 */
	public void setIsVoucherApproved(Boolean isVoucherApproved) {
		this.isVoucherApproved = isVoucherApproved;
	}

}
