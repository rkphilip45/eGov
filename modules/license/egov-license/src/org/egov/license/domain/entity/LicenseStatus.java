/*
 * @(#)LicenseStatus.java 3.0, 29 Jul, 2013 1:24:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;

public class LicenseStatus implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer ID = null;

	private String name = null;

	private Date lastUpdatedTimeStamp = null;

	private String statusCode = null;
	private boolean active;
	private Integer orderId;

	/**
	 * @return Returns the iD.
	 */
	public Integer getID() {
		return this.ID;
	}

	/**
	 * @param id The iD to set.
	 */
	public void setID(final Integer id) {
		this.ID = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return this.lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(final Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return this.statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(final String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return this.orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(final Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return Returns if the given Object is equal to PropertyStatus
	 */
	@Override
	public boolean equals(final Object that) {
		if (that == null) {
			return false;
		}

		if (this == that) {
			return true;
		}

		if (that.getClass() != this.getClass()) {
			return false;
		}
		final LicenseStatus thatPropStatus = (LicenseStatus) that;

		if (this.getID() != null && thatPropStatus.getID() != null) {
			if (getID().equals(thatPropStatus.getID())) {
				return true;
			} else {
				return false;
			}
		} else if (this.getName() != null && thatPropStatus.getName() != null) {
			if (getName().equals(thatPropStatus.getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the hashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		if (this.getID() != null) {
			hashCode += this.getID().hashCode();
		}
		if (this.getName() != null) {
			hashCode += this.getName().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {
		if (getName() == null) {
			throw new EGOVRuntimeException("In LicenseStatus Validate : 'Status Name' Attribute is Not Set, Please Check !!");
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseStatus={");
		str.append("ID=").append(this.ID);
		str.append("name=").append(this.name == null ? "null" : this.name.toString());
		str.append("lastUpdatedTimeStamp=").append(this.lastUpdatedTimeStamp == null ? "null" : this.lastUpdatedTimeStamp.toString());
		str.append("statusCode=").append(this.statusCode == null ? "null" : this.statusCode.toString());
		str.append("active=").append(this.active);
		str.append("orderId=").append(this.orderId == null ? "null" : this.orderId.toString());
		str.append("}");
		return str.toString();
	}

}