/*
 * Property.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.exceptions.InvalidPropertyException;
import org.egov.commons.Installment;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.citizen.model.Owner;
import org.egov.lib.rjbac.user.User;
import org.egov.ptis.domain.entity.demand.Ptdemand;

/**
 * This is the interface for the Property which reperesents the Status and
 * Source of each Property. Every Property Object has some source associated
 * with it, which can be either Self-Assessment, Surveys or PropertyFiles.
 * Property from different Sources would be represented as seperate entities.
 * 
 * @author Neetu
 * @version 2.00
 */
public interface Property {

	public Long getId();

	public void setId(Long id);

	public User getCreatedBy();

	public void setCreatedBy(User createdBy);

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public User getModifiedBy();

	public void setModifiedBy(User modifiedBy);

	public Date getModifiedDate();

	public void setModifiedDate(Date modifiedDate);

	public Set<Owner> getPropertyOwnerSet();

	public void setPropertyOwnerSet(Set<Owner> propertyOwnerSet);

	public Set<Owner> getPropertyTenantSet();

	public void setPropertyTenantSet(Set<Owner> propertyTenantSet);

	public BasicProperty getBasicProperty();

	public void setBasicProperty(BasicProperty basicProperty);

	public String getExtra_field1();

	public void setExtra_field1(String extra_field1);

	public String getExtra_field2();

	public void setExtra_field2(String extra_field2);

	public String getExtra_field3();

	public void setExtra_field3(String extra_field3);

	public Boolean isVacant();

	public void setVacant(Boolean vacant);

	public org.egov.lib.address.model.Address getPropertyAddress();

	public void setPropertyAddress(Address address);

	public PropertySource getPropertySource();

	public void setPropertySource(PropertySource propertySource);

	public boolean validateProperty() throws InvalidPropertyException;

	public void addPropertyOwners(Owner owner);

	public void removePropertyOwners(Owner owner);

	public void addPropertyTenants(Owner owner);

	public void removePropertyTenants(Owner owner);

	public Character getIsDefaultProperty();

	/**
	 * @param isDefaultProperty
	 *            The isDefaultProperty to set. If a property is set to default,
	 *            this application will consider this property's details for all
	 *            the demand calculation etc.
	 */

	public void setIsDefaultProperty(Character isDefaultProperty);

	public Character getStatus();

	public void setStatus(Character status);

	public void setEffectiveDate(Date date);

	public Date getEffectiveDate();

	public Set<Ptdemand> getPtDemandSet();

	public void setPtDemandSet(Set<Ptdemand> ptDemandSet);

	public void addPtDemand(Ptdemand ptDmd);

	public void removePtDemand(Ptdemand ptDmd);

	public PropertyDetail getPropertyDetail();

	public void setPropertyDetail(PropertyDetail propertyDetail);

	public void setIsChecked(Character isChecked);

	public Character getIsChecked();

	public String getRemarks();

	public void setRemarks(String remarks);

	public PropertyModifyReason getPropertyModifyReason();

	public void setPropertyModifyReason(PropertyModifyReason propertyModifyReason);

	public Set<PtDemandARV> getPtDemandARVSet();

	public void setPtDemandARVSet(Set<PtDemandARV> ptDemandARVSet);

	public void addPtDemandARV(PtDemandARV ptDemandARV);

	public void setExtra_field4(String extra_field4);

	public String getExtra_field4();

	public void setExtra_field5(String extra_field5);

	public String getExtra_field5();

	public void setExtra_field6(String extra_field6);

	public String getExtra_field6();

	public void setInstallment(Installment installment);

	public Installment getInstallment();

	public List<Owner> getPropertyOwnerProxy();

	public void setPropertyOwnerProxy(List<Owner> propertyOwnerProxy);
	
	public Property createPropertyclone();

	public Boolean getIsExemptedFromTax();

	public void setIsExemptedFromTax(Boolean isExemptedFromTax) ;

	public String getTaxExemptReason();

	public void setTaxExemptReason(String taxExemptReason);
	
	public String getDocNumber();

	public void setDocNumber(String docNumber);
	
	public BigDecimal getManualAlv();

	public void setManualAlv(BigDecimal manualAlv);
	
	public String getOccupierName();

	public void setOccupierName(String occupierName);

	public Boundary getAreaBndry();
	
	public void setAreaBndry(Boundary areaBndry);
	
}