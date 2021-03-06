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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.ValidationError;
import org.egov.infstr.annotation.Introspection;
import org.hibernate.search.annotations.DocumentId;

public class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * Base class cannot be indexed only subclasses can be indexed for Lucene refer: http://opensource.atlassian.com/projects/hibernate/browse/HSEARCH-333
	 */
	@DocumentId
	protected Long id;
	protected User createdBy;
	protected Date createdDate;
	protected User modifiedBy;
	protected Date modifiedDate;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ValidationError> validate() {
		return new ArrayList<ValidationError>();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{ ");
		sb.append("class : ").append(this.getClass().getSimpleName()).append(", ");
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					if (field.isAnnotationPresent(Introspection.class)) {
						if (!field.isAccessible()) {
							field.setAccessible(true);
						}
						Object val = field.get(this);
						if (val != null) {
							String fieldName = field.getAnnotation(Introspection.class).value();
							sb.append((fieldName.trim().equals("") ? field.getName() : fieldName)).append(" : ").append(val).append(", ");
						}
					}
				} catch (Exception e) {
					throw new EGOVRuntimeException("Internal Server Error ", e);
				}

			}
		} catch (Exception e) {
			sb.append(e.toString());
		}
		sb.append(" }");
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}

}
