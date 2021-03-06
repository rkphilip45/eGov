/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
/*
 * PropertyUsage.java Created on Nov 22, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;

/**
 * This class defines the reasons for the New property Creation
 * 
 * @author Neetu
 * @version 1.00
 * @see
 * @see
 * @since 1.00
 */
public class PropertyCreationReason implements Serializable {

	private Integer idReason = null;
	private String reasonName = null;
	private Date lastUpdatedTimeStamp = null;

	/**
	 * @return Returns the idReason.
	 */
	public Integer getIdReason() {
		return idReason;
	}

	/**
	 * @param idReason
	 *            The idReason to set.
	 */
	public void setIdReason(Integer idReason) {
		this.idReason = idReason;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp
	 *            The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return Returns the reasonName.
	 */
	public String getReasonName() {
		return reasonName;
	}

	/**
	 * @param reasonName
	 *            The reasonName to set.
	 */
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	/**
	 * @return Returns if the given Object is equal to PropertyCreationReason
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final PropertyCreationReason other = (PropertyCreationReason) that;

		if (this.getIdReason() != null && other.getIdReason() != null) {
			if (getIdReason().equals(other.getIdReason())) {
				return true;
			} else
				return false;
		} else if (this.getReasonName() != null && other.getReasonName() != null) {
			if (getReasonName().equals(other.getReasonName())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getIdReason() != null) {
			hashCode += this.getIdReason().hashCode();
		} else if (this.getReasonName() != null) {
			hashCode += this.getReasonName().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {
		if (getReasonName() == null)
			throw new EGOVRuntimeException("PropertyCreationReason.validate. ReasonName is not set, Please Check !!");

		return true;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();
		objStr.append("Id: ").append(getIdReason()).append("|ReasonName: ").append(getReasonName());
		return objStr.toString();
	}
}
