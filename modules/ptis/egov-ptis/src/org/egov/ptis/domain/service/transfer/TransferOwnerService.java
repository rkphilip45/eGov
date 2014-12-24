package org.egov.ptis.domain.service.transfer;

import static org.egov.ptis.constants.PropertyTaxConstants.OWNER_ADDR_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE_PRATIVRUTTA;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infstr.flexfields.model.EgAttributevalues;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.address.model.Address;
import org.egov.lib.address.model.AddressTypeMaster;
import org.egov.lib.citizen.model.Owner;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyMutationOwner;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

public class TransferOwnerService extends PersistenceService<PropertyMutation, Long> {
	private static final Logger LOGGER = Logger.getLogger(TransferOwnerService.class);

	private PersistenceService trnsfOwnerPerService;
	protected PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

	/*
	 * This method returns property's basic property which is undergoing
	 * mutation
	 */
	public PropertyImpl createPropertyClone(BasicProperty basicProp, PropertyMutation propertyMutation,
			List<Owner> propertyOwnerProxy, boolean chkIsCorrIsDiff, String corrAddress1, String corrAddress2,
			String corrPinCode, String email, String mobileNo, String docNumber) {
		Property oldProperty = basicProp.getProperty();
		Set<PropertyMutation> propertyMutationSet = getPropMutationSet(basicProp, propertyMutation, oldProperty);
		basicProp.setPropMutationSet(propertyMutationSet);
		// cloning property
		Property clonedProperty = oldProperty.createPropertyclone();
		clonedProperty.setPropertyOwnerSet(getNewPropOwnerAdd(clonedProperty, chkIsCorrIsDiff, corrAddress1,
				corrAddress2, corrPinCode, propertyOwnerProxy));
		clonedProperty.setPtDemandSet(cloneDemandSet(clonedProperty, oldProperty));
		basicProp.setAddress(getChangedOwnerContact(basicProp, email, mobileNo));
		clonedProperty.setStatus(STATUS_WORKFLOW);
		clonedProperty.setExtra_field1("");
		clonedProperty.setExtra_field2(NOTICE_PRATIVRUTTA);
		clonedProperty.setExtra_field3("");
		clonedProperty.setExtra_field4("");
		clonedProperty.setDocNumber(docNumber);
		basicProp.addProperty(clonedProperty);
		basicPrpertyService.update(basicProp);
		return (PropertyImpl) clonedProperty;
	}

	private Map<Installment, PTDemandCalculations> getDemandCalMap(Property oldProperty) {
		Map<Installment, PTDemandCalculations> dmdCalMap = new HashMap<Installment, PTDemandCalculations>();
		for (Ptdemand dmd : oldProperty.getPtDemandSet()) {
			dmdCalMap.put(dmd.getEgInstallmentMaster(), dmd.getDmdCalculations());
		}
		return dmdCalMap;

	}

	private Set<Ptdemand> cloneDemandSet(Property clonedProperty, Property oldProperty) {
		Map<Installment, PTDemandCalculations> dmdCalMap = getDemandCalMap(oldProperty);
		Set<Ptdemand> demandSet = new HashSet<Ptdemand>();
		PTDemandCalculations ptDmdCal;
		for (Ptdemand ptDmd : clonedProperty.getPtDemandSet()) {
			PTDemandCalculations OldPTDmdCal = dmdCalMap.get(ptDmd.getEgInstallmentMaster());
			ptDmdCal = new PTDemandCalculations(ptDmd, OldPTDmdCal.getPropertyTax(), OldPTDmdCal.getRateOfTax(), null,
					null, cloneFlrWiseDmdCal(OldPTDmdCal.getFlrwiseDmdCalculations()),
					cloneEgAttValues(OldPTDmdCal.getAttributeValues()), OldPTDmdCal.getTaxInfo(), OldPTDmdCal.getAlv());
			ptDmd.setDmdCalculations(ptDmdCal);
			demandSet.add(ptDmd);
		}
		return demandSet;
	}

	private Set<FloorwiseDemandCalculations> cloneFlrWiseDmdCal(Set<FloorwiseDemandCalculations> flrDmdCal) {
		FloorwiseDemandCalculations flrWiseDmdCal;
		Set<FloorwiseDemandCalculations> floorDmdCalSet = new HashSet<FloorwiseDemandCalculations>();
		for (FloorwiseDemandCalculations flrCal : flrDmdCal) {
			flrWiseDmdCal = new FloorwiseDemandCalculations(null, flrCal.getFloor(), flrCal.getPTDemandCalculations(),
					new Date(), new Date(), flrCal.getCategoryAmt(), flrCal.getOccupancyRebate(),
					flrCal.getConstructionRebate(), flrCal.getDepreciation(), flrCal.getUsageRebate(),
					cloneEgAttValues(flrCal.getAttributeValues()));
			floorDmdCalSet.add(flrWiseDmdCal);
		}
		return floorDmdCalSet;
	}

	private Set<EgAttributevalues> cloneEgAttValues(Set<EgAttributevalues> attributeValues) {
		EgAttributevalues egAttvalues;
		Set<EgAttributevalues> attValuesSet = new HashSet<EgAttributevalues>();
		for (EgAttributevalues attValue : attributeValues) {
			egAttvalues = new EgAttributevalues(null, attValue.getEgAttributetype(), attValue.getEgApplDomain(),
					attValue.getAttValue(), attValue.getDomaintxnid());
			attValuesSet.add(egAttvalues);
		}
		return attValuesSet;
	}

	/*
	 * This method returns Property Mutation as a Set
	 */
	private Set<PropertyMutation> getPropMutationSet(BasicProperty basicProp, PropertyMutation propertyMutation,
			Property oldProperty) {
		PropertyMutationMaster propMutMstr = null;
		if (propertyMutation.getPropMutationMstr() != null
				&& propertyMutation.getPropMutationMstr().getIdMutation() != -1) {
			propMutMstr = (PropertyMutationMaster) trnsfOwnerPerService.find(
					"from PropertyMutationMaster PM where PM.idMutation = ?",
					Integer.valueOf(propertyMutation.getPropMutationMstr().getIdMutation()).intValue());
		}
		propertyMutation.setRefPid(null);
		propertyMutation.setBasicProperty(basicProp);
		propertyMutation.setApplicationNo(propertyTaxNumberGenerator.generateNameTransApplNo(basicProp.getBoundary()));
		propertyMutation.setMutationDate(propertyMutation.getMutationDate());
		propertyMutation.setMutationOwnerSet(getMutOwners(oldProperty, propertyMutation));
		propertyMutation.setPropMutationMstr(propMutMstr);
		Set<PropertyMutation> propertyMutationSet = new HashSet();
		propertyMutationSet.add(propertyMutation);
		return propertyMutationSet;
	}

	/*
	 * This method returns Mutation Owner details as a Set for Property
	 * undergoing Mutation
	 */
	private Set<PropertyMutationOwner> getMutOwners(Property prop, PropertyMutation propertyMutation) {
		Set<PropertyMutationOwner> mutOwnerSet = new HashSet<PropertyMutationOwner>();
		for (Owner ownerprop : prop.getPropertyOwnerSet()) {
			PropertyMutationOwner propMutOwner = new PropertyMutationOwner();
			propMutOwner.setOwnerId(ownerprop.getCitizenID());
			propMutOwner.setPropertyMutation(propertyMutation);
			mutOwnerSet.add(propMutOwner);
		}
		return mutOwnerSet;
	}

	/*
	 * This method returns changed owner corr address as a Set
	 */
	private Set<Owner> getNewPropOwnerAdd(Property clonedProperty, boolean chkIsCorrIsDiff, String corrAddress1,
			String corrAddress2, String corrPinCode, List<Owner> propertyOwnerProxy) {
		AddressTypeMaster addrTypeMstr = (AddressTypeMaster) trnsfOwnerPerService.find(
				"from AddressTypeMaster where addressTypeName = ?", OWNER_ADDR_TYPE);
		Set<Owner> ownSet = ownSet = new HashSet<Owner>();
		PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
		Address ownerAddr = null;
		if (chkIsCorrIsDiff) {
			ownerAddr = new Address();
			if (corrAddress1 != null && !corrAddress1.isEmpty()) {
				corrAddress1 = propertyTaxUtil.antisamyHackReplace(corrAddress1);
			}
			if (corrAddress2 != null && !corrAddress2.isEmpty()) {
				corrAddress2 = propertyTaxUtil.antisamyHackReplace(corrAddress2);
			}
			ownerAddr.setAddTypeMaster(addrTypeMstr);
			ownerAddr.setStreetAddress1(corrAddress1);
			ownerAddr.setStreetAddress2(corrAddress2);
			if (StringUtils.isNotEmpty(corrPinCode) || StringUtils.isNotBlank(corrPinCode)) {
				ownerAddr.setPinCode(Integer.valueOf(corrPinCode));
			}
		} else {
			PropertyAddress propAddress = clonedProperty.getBasicProperty().getAddress();
			ownerAddr = propAddress;
		}
		for (Owner owner : propertyOwnerProxy) {
			Owner newOwner = new Owner();
			String ownerName = owner.getFirstName();
			ownerName = propertyTaxUtil.antisamyHackReplace(ownerName);
			newOwner.setFirstName(ownerName);
			newOwner.addAddress(ownerAddr);
			ownSet.add(newOwner);
		}
		return ownSet;
	}

	/*
	 * This method returns modified Owner Details for email and contact number
	 */
	private PropertyAddress getChangedOwnerContact(BasicProperty bp, String email, String mobileNo) {
		PropertyAddress propAddr = bp.getAddress();
		if (email != null && email != "") {
			propAddr.setEmailAddress(email);
		}
		if (mobileNo != null && mobileNo != "") {
			propAddr.setMobileNo(mobileNo);
		}
		return propAddr;
	}

	public PersistenceService getTrnsfOwnerPerService() {
		return trnsfOwnerPerService;
	}

	public void setTrnsfOwnerPerService(PersistenceService trnsfOwnerPerService) {
		this.trnsfOwnerPerService = trnsfOwnerPerService;
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

}