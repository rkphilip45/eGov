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
package org.egov.demand.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.commons.Module;

/**
 * EgDemandReasonMaster entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class EgDemandReasonMaster implements java.io.Serializable {

	// Fields

	private Long id;
	private Module egModule;
	private EgReasonCategory egReasonCategory;
	private String reasonMaster;
	private String isDebit;
	private String code;
	private Long orderId;
	private Date createTimeStamp;
	private Date lastUpdatedTimeStamp;
	private Set<EgDemandReason> egDemandReasons = new HashSet<EgDemandReason>(0);

	public String toString() {
	    return code;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Module getEgModule() {
		return this.egModule;
	}

	public void setEgModule(Module egModule) {
		this.egModule = egModule;
	}

	public EgReasonCategory getEgReasonCategory() {
		return this.egReasonCategory;
	}

	public void setEgReasonCategory(EgReasonCategory egReasonCategory) {
		this.egReasonCategory = egReasonCategory;
	}

	public String getReasonMaster() {
		return this.reasonMaster;
	}

	public void setReasonMaster(String reasonMaster) {
		this.reasonMaster = reasonMaster;
	}

	public String getIsDebit() {
		return this.isDebit;
	}

	public void setIsDebit(String isDebit) {
		this.isDebit = isDebit;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getCreateTimeStamp() {
		return this.createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Set<EgDemandReason> getEgDemandReasons() {
		return this.egDemandReasons;
	}

	public void setEgDemandReasons(Set<EgDemandReason> egDemandReasons) {
		this.egDemandReasons = egDemandReasons;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

}