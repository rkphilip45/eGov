/*
 * @(#)Entity.java 3.0, 14 Jun, 2013 4:38:48 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.model;

import java.util.Date;

public class Entity implements Comparable<Entity> {

	private Integer id;
	private String name;
	private Date updatedTime;

	/**
	 * @return Returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the UpdatedTime.
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param UpdatedTime The UpdatedTime to set.
	 */
	public void setUpdatedTime(Date updatedtime) {
		this.updatedTime = updatedtime;
	}

	/**
	 * @return Returns if the given Object is equal to Action
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof Entity))
			return false;

		final Entity other = (Entity) obj;

		if (getName().equals(other.getName())) {
			return true;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		hashCode = hashCode + this.getName().hashCode();

		return hashCode;
	}

	public int compareTo(Entity entity) throws ClassCastException {
		return (entity == null || entity.getName() == null) ? -1 : this.name.compareTo(entity.getName());
	}

}