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
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServiceSubledgerInfo {

	private Long id;

	private Accountdetailtype detailType;

	private Integer detailKeyId;

	private BigDecimal amount = BigDecimal.ZERO;

	private ServiceAccountDetails serviceAccountDetail;

	private EntityType entity;

	/**
	 * @return the detailType
	 */
	public Accountdetailtype getDetailType() {
		return detailType;
	}

	/**
	 * @param detailType the detailType to set
	 */
	public void setDetailType(Accountdetailtype detailType) {
		this.detailType = detailType;
	}

	/**
	 * @return the detailKey
	 */
	public Integer getDetailKeyId() {
		return detailKeyId;
	}

	/**
	 * @param detailKey the detailKey to set
	 */
	public void setDetailKeyId(Integer detailKeyId) {
		this.detailKeyId = detailKeyId;
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
	 * @return the serviceAccountDetail
	 */
	public ServiceAccountDetails getServiceAccountDetail() {
		return serviceAccountDetail;
	}

	/**
	 * @param serviceAccountDetail the serviceAccountDetail to set
	 */
	public void setServiceAccountDetail(ServiceAccountDetails serviceAccountDetail) {
		this.serviceAccountDetail = serviceAccountDetail;
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

	public String getDetailCode() {
		entity = getEntityInfo();
		return null != this.entity ? entity.getCode() : null;
	}

	public String getDetailKey() {

		return null != this.entity ? entity.getName() : null;
	}

	@SuppressWarnings("unchecked")
	private EntityType getEntityInfo() throws ValidationException {
		EntityType entity = null;
		if (null == this.getDetailType() || null == this.detailType.getId() || this.detailType.getId().equals(0) || this.detailType.getId().equals(-1) || null == this.detailKeyId || this.detailKeyId.equals(0) || this.detailKeyId.equals(-1)) {

			return null;
		}
		try {
			Class<?> service = Class.forName(this.detailType.getFullQualifiedName());
			// getting the entity type service.
			String detailTypeName = service.getSimpleName();
			String detailTypeService = detailTypeName.substring(0, 1).toLowerCase() + detailTypeName.substring(1) + "Service";
			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
			PersistenceService entityPersistenceService = (PersistenceService) wac.getBean(detailTypeService);
			String dataType = "";
			// required to know data type of the id of the detail type object.
			java.lang.reflect.Method method = service.getMethod("getId");
			dataType = method.getReturnType().getSimpleName();
			if (dataType.equals("Long")) {
				entity = (EntityType) entityPersistenceService.findById(Long.valueOf(this.detailKeyId.toString()), false);
			} else {
				entity = (EntityType) entityPersistenceService.findById(detailKeyId, false);
			}
		} catch (Exception e) {
			List<ValidationError> errors = new ArrayList<ValidationError>();
			errors.add(new ValidationError("exp", e.getMessage()));
			throw new ValidationException(errors);
		}

		return entity;

	}
}
