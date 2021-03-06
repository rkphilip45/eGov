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
package org.egov.ptis.domain.service.property;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE127;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OPEN_PLOT_UNIT_FLOORNUMBER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_IS_DEFAULT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_AMALG;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_DATA_ENTRY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_STATUS_MARK_DEACTIVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROP_CREATE_RSN;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROP_CREATE_RSN_BIFUR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROP_SOURCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_APPROVAL_PENDING;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.DepreciationMaster;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Address;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
//TODO -- Fix me (Commented to Resolve compilation issues)
/*import org.egov.infstr.flexfields.dao.EgAttributetypeDao;
 import org.egov.infstr.flexfields.dao.EgAttributevaluesDao;
 import org.egov.infstr.flexfields.dao.FlexfieldsDaoFactory;
 import org.egov.infstr.flexfields.model.EgAttributetype;
 import org.egov.infstr.flexfields.model.EgAttributevalues;*/
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.nmc.adapter.TaxXmlToDbConverterAdapter;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.MiscellaneousTaxDetail;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.service.TaxCalculator;
import org.egov.ptis.nmc.service.TaxXMLToDBCoverterService;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PropertyService extends PersistenceService<PropertyImpl, Long> {

	private static final String PROPERTY_WORKFLOW_STARTED = "Property Workflow Started";

	private static final Logger LOGGER = Logger.getLogger(PropertyService.class);

	private PersistenceService propPerServ;
	private Installment currentInstall;
	private TaxCalculator taxCalculator;
	private HashMap<Installment, TaxCalculationInfo> instTaxMap;
	private PropertyTaxUtil propertyTaxUtil;
	@Autowired
	protected EisCommonsService eisCommonsService;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;
	@Autowired
	private InstallmentDao installmentDao;
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	protected PersistenceService<BasicProperty, Long> basicPrpertyService;
	private Map<Installment, Set<EgDemandDetails>> demandDetails = new HashMap<Installment, Set<EgDemandDetails>>();
	private Map<Installment, Map<String, BigDecimal>> excessCollAmtMap = new LinkedHashMap<Installment, Map<String, BigDecimal>>();
	private PropertyTaxNumberGenerator ptNumberGenerator;

	public PropertyImpl createProperty(PropertyImpl property, String areaOfPlot,
			String mutationCode, String propTypeId, String propUsageId, String propOccId,
			Character status, String docnumber, String nonResPlotArea,
			boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createProperty");
		LOGGER.debug("createProperty: Property: " + property + ", areaOfPlot: " + areaOfPlot
				+ ", mutationCode: " + mutationCode + ",propTypeId: " + propTypeId
				+ ", propUsageId: " + propUsageId + ", propOccId: " + propOccId + ", status: "
				+ status);
		currentInstall = (Installment) getPropPerServ()
				.find("from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
						PTMODULENAME, new Date(), new Date());
		PropertySource propertySource = (PropertySource) getPropPerServ().find(
				"from PropertySource where propSrcCode = ?", PROP_SOURCE);

		if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
			Area area = new Area();
			area.setArea(new Float(areaOfPlot));
			property.getPropertyDetail().setSitalArea(area);
		}

		if (nonResPlotArea != null && !nonResPlotArea.isEmpty()) {
			Area area = new Area();
			area.setArea(new Float(nonResPlotArea));
			property.getPropertyDetail().setNonResPlotArea(area);
		}

		property.getPropertyDetail().setFieldVerified('Y');
		property.getPropertyDetail().setProperty(property);
		PropertyMutationMaster propMutMstr = (PropertyMutationMaster) getPropPerServ().find(
				"from PropertyMutationMaster PM where upper(PM.code) = ?", mutationCode);
		PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPropPerServ().find(
				"from PropertyTypeMaster PTM where PTM.id = ?", Long.valueOf(propTypeId));
		if (propTypeMstr != null) {
			if (!(propTypeMstr.getCode().equals(PROPTYPE_NON_RESD)
					|| propTypeMstr.getCode().equals(PROPTYPE_RESD) || propTypeMstr.getCode()
					.equals(PROPTYPE_OPEN_PLOT))) {
				property.getPropertyDetail().setExtra_field5(null);
			}
		}
		if (propUsageId != null) {
			PropertyUsage usage = (PropertyUsage) getPropPerServ().find(
					"from PropertyUsage pu where pu.id = ?", Long.valueOf(propUsageId));
			property.getPropertyDetail().setPropertyUsage(usage);
		} else {
			property.getPropertyDetail().setPropertyUsage(null);
		}
		if (propOccId != null) {
			PropertyOccupation occupancy = (PropertyOccupation) getPropPerServ().find(
					"from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
			property.getPropertyDetail().setPropertyOccupation(occupancy);
		} else {
			property.getPropertyDetail().setPropertyOccupation(null);
		}
		if (propTypeMstr.getCode().equals(PROPTYPE_OPEN_PLOT)) {
			property.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
		} else {
			property.getPropertyDetail().setPropertyType(BUILT_UP_PROPERTY);
		}
		property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
		property.getPropertyDetail().setPropertyMutationMaster(propMutMstr);
		property.getPropertyDetail().setUpdatedTime(new Date());
		createFloors(property, mutationCode, propUsageId, propOccId, isfloorDetailsRequired);
		// property.setStatus(status);
		property.setIsDefaultProperty(PROPERTY_IS_DEFAULT);
		property.setInstallment(currentInstall);
		property.setEffectiveDate(currentInstall.getFromDate());
		property.setPropertySource(propertySource);
		property.setDocNumber(docnumber);
		LOGGER.debug("Exiting from createProperty");
		return property;
	}

	public void createFloors(Property property, String mutationCode, String propUsageId,
			String propOccId, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createFloors");
		LOGGER.debug("createFloors: Property: " + property + ", mutationCode: " + mutationCode
				+ ", propUsageId: " + propUsageId + ", propOccId: " + propOccId);
		Area totBltUpArea = new Area();
		Float totBltUpAreaVal = new Float(0);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode()
				.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if (((property.getPropertyDetail().getPropertyTypeMaster().getCode()
					.equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property.getPropertyDetail()
					.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) && !isfloorDetailsRequired)
					|| !(property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property.getPropertyDetail()
							.getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))) {
				if (!mutationCode.equals("NEW") || STATUS_WORKFLOW.equals(property.getStatus())) {
					property.getPropertyDetail().setFloorDetails(new HashSet<FloorIF>());
				}
				for (FloorIF floor : property.getPropertyDetail().getFloorDetailsProxy()) {
					if (floor != null) {
						totBltUpAreaVal = totBltUpAreaVal + floor.getBuiltUpArea().getArea();
						floor.setCreatedTimeStamp(new Date());

						if ("-1".equals(floor.getExtraField7())) {
							floor.setExtraField7(null);
						}

						if ("-1".equalsIgnoreCase(floor.getTaxExemptedReason())) {
							floor.setTaxExemptedReason(null);
						}

						PropertyTypeMaster unitType = null;
						PropertyUsage usage = null;
						PropertyOccupation occupancy = null;
						if (floor.getUnitType() != null) {
							unitType = (PropertyTypeMaster) getPropPerServ().find(
									"from PropertyTypeMaster utype where utype.id = ?",
									floor.getUnitType().getId());
						}
						if (floor.getPropertyUsage() != null) {
							usage = (PropertyUsage) getPropPerServ().find(
									"from PropertyUsage pu where pu.id = ?",
									floor.getPropertyUsage().getId());
						}
						if (floor.getPropertyOccupation() != null) {
							occupancy = (PropertyOccupation) getPropPerServ().find(
									"from PropertyOccupation po where po.id = ?",
									floor.getPropertyOccupation().getId());
						}

						StructureClassification structureClass = null;

						if (floor.getStructureClassification() != null) {
							structureClass = (StructureClassification) getPropPerServ().find(
									"from StructureClassification sc where sc.id = ?",
									floor.getStructureClassification().getId());
						}

						DepreciationMaster depMaster = null;

						if (floor.getDepreciationMaster() != null) {
							depMaster = (DepreciationMaster) getPropPerServ().find(
									"from DepreciationMaster dm where dm.id=?",
									floor.getDepreciationMaster().getId());
						}

						floor.setDepreciationMaster(depMaster);

						LOGGER.debug("createFloors: PropertyUsage: " + usage
								+ ", PropertyOccupation: " + occupancy + ", StructureClass: "
								+ structureClass);

						if (unitType != null
								&& unitType.getCode().equalsIgnoreCase(
										NMCPTISConstants.UNITTYPE_OPEN_PLOT)) {
							floor.setFloorNo(OPEN_PLOT_UNIT_FLOORNUMBER);
						}

						floor.setUnitType(unitType);
						floor.setPropertyUsage(usage);
						floor.setPropertyOccupation(occupancy);
						floor.setStructureClassification(structureClass);
						property.getPropertyDetail().addFloor(floor);
						// setting total builtup area.
						totBltUpArea.setArea(totBltUpAreaVal);
						property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);

						if (occupancy.getOccupancyCode().equalsIgnoreCase(NMCPTISConstants.TENANT)) {
							floor.getRentAgreementDetail().setFloor(floor);
						} else {
							floor.setRentAgreementDetail(null);
						}
					}
				}
			} else {
				// -added for state govt property without floors
				if (property.getPropertyDetail().getFloorDetailsProxy().size() > 0
						&& property.getPropertyDetail().getFloorDetails().size() > 0) {
					property.getPropertyDetail().setFloorDetailsProxy(Collections.EMPTY_LIST);
					property.getPropertyDetail().setFloorDetails(Collections.EMPTY_SET);
				}
				PropertyOccupation occupancy = (PropertyOccupation) getPropPerServ().find(
						"from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
				PropertyUsage usage = (PropertyUsage) getPropPerServ().find(
						"from PropertyUsage pu where pu.id = ?", Long.valueOf(propUsageId));
				LOGGER.debug("createFloors: PropertyUsage: " + usage + ", PropertyOccupation: "
						+ occupancy);
				property.getPropertyDetail().setPropertyOccupation(occupancy);
				property.getPropertyDetail().setPropertyUsage(usage);
				// setting total builtup area.
				totBltUpArea.setArea(totBltUpAreaVal);
				property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);
			}

		} else {
			if (property.getPropertyDetail().getFloorDetailsProxy().size() > 0
					&& property.getPropertyDetail().getFloorDetails().size() > 0) {
				property.getPropertyDetail().setFloorDetailsProxy(Collections.EMPTY_LIST);
				property.getPropertyDetail().setFloorDetails(Collections.EMPTY_SET);
			}
			PropertyOccupation occupancy = (PropertyOccupation) getPropPerServ().find(
					"from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
			PropertyUsage usage = (PropertyUsage) getPropPerServ().find(
					"from PropertyUsage pu where pu.id = ?", Long.valueOf(propUsageId));
			LOGGER.debug("createFloors: PropertyUsage: " + usage + ", PropertyOccupation: "
					+ occupancy);
			property.getPropertyDetail().setPropertyOccupation(occupancy);
			property.getPropertyDetail().setPropertyUsage(usage);
		}
		LOGGER.debug("Exiting from createFloors");
	}

	public PropertyStatusValues createPropStatVal(BasicProperty basicProperty, String statusCode,
			Date propCompletionDate, String courtOrdNum, Date orderDate, String judgmtDetails,
			String parentPropId) {
		LOGGER.debug("Entered into createPropStatVal");
		LOGGER.debug("createPropStatVal: basicProperty: " + basicProperty + ", statusCode: "
				+ statusCode + ", propCompletionDate: " + propCompletionDate + ", courtOrdNum: "
				+ courtOrdNum + ", orderDate: " + orderDate + ", judgmtDetails: " + judgmtDetails
				+ ", parentPropId: " + parentPropId);
		PropertyStatusValues propStatVal = new PropertyStatusValues();
		PropertyStatus propertyStatus = (PropertyStatus) getPropPerServ().find(
				"from PropertyStatus where statusCode=?", statusCode);
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(statusCode)
				|| PROPERTY_MODIFY_REASON_AMALG.equals(statusCode)
				|| PROPERTY_MODIFY_REASON_BIFURCATE.equals(statusCode)) {
			propStatVal.setIsActive("W");
		} else {
			propStatVal.setIsActive("Y");
		}

		propStatVal.setPropertyStatus(propertyStatus);
		if (orderDate != null || (courtOrdNum != null && !courtOrdNum.equals(""))
				|| (judgmtDetails != null && !judgmtDetails.equals(""))) {
			propStatVal.setReferenceDate(orderDate);
			propStatVal.setReferenceNo(courtOrdNum);
			propStatVal.setRemarks(judgmtDetails);
		} else {
			propStatVal.setReferenceDate(new Date());
			propStatVal.setReferenceNo("0001");// There should be rule to create
			// order number, client has to give it
		}
		if (!statusCode.equals(PROP_CREATE_RSN) && propCompletionDate != null) {
			// persist the DateOfCompletion in case of modify property for
			// future reference
			String propCompDateStr = sdf.format(propCompletionDate);
			propStatVal.setExtraField1(propCompDateStr);
		}
		propStatVal.setBasicProperty(basicProperty);
		if (basicProperty.getPropertyMutationMaster() != null
				&& basicProperty.getPropertyMutationMaster().getCode()
						.equals(PROP_CREATE_RSN_BIFUR)) {
			BasicProperty referenceBasicProperty = (BasicProperty) propPerServ.find(
					"from BasicPropertyImpl bp where bp.upicNo=?", parentPropId);
			propStatVal.setReferenceBasicProperty(referenceBasicProperty);
		}
		LOGGER.debug("createPropStatVal: PropertyStatusValues: " + propStatVal);
		LOGGER.debug("Exiting from createPropStatVal");
		return propStatVal;
	}

	public Property createDemand(PropertyImpl property, Property oldProperty,
			Date dateOfCompletion, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createDemand");
		LOGGER.debug("createDemand: Property: " + property + ", dateOfCompletion: "
				+ dateOfCompletion);

		instTaxMap = taxCalculator.calculatePropertyTax(property, dateOfCompletion);

		Ptdemand ptDemand;
		Set<Ptdemand> ptDmdSet = new HashSet<Ptdemand>();
		Set<EgDemandDetails> dmdDetailSet;
		List<Installment> instList = new ArrayList<Installment>();
		instList = new ArrayList<Installment>(instTaxMap.keySet());
		LOGGER.debug("createDemand: instList: " + instList);
		currentInstall = PropertyTaxUtil.getCurrentInstallment();

		for (Installment installment : instList) {
			TaxCalculationInfo taxCalcInfo = instTaxMap.get(installment);
			dmdDetailSet = createAllDmdDeatails(installment, instList, instTaxMap);
			PTDemandCalculations ptDmdCalc = new PTDemandCalculations();
			ptDemand = new Ptdemand();
			ptDemand.setBaseDemand(taxCalcInfo.getTotalTaxPayable());
			ptDemand.setCreateTimestamp(new Date());
			ptDemand.setEgInstallmentMaster(installment);
			ptDemand.setEgDemandDetails(dmdDetailSet);
			ptDemand.setIsHistory("N");
			ptDemand.setEgptProperty(property);
			ptDmdSet.add(ptDemand);

			ptDmdCalc.setPtDemand(ptDemand);
			ptDmdCalc.setPropertyTax(taxCalcInfo.getTotalTaxPayable());
			ptDmdCalc.setTaxInfo(taxCalcInfo.getTaxCalculationInfoXML().getBytes());
			ptDemand.setDmdCalculations(ptDmdCalc);

			// In case of Property Type as (Open Plot,State Govt,Central Govt),
			// set the alv to PTDemandCalculations
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode()
					.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				ptDmdCalc.setAlv(taxCalcInfo.getTotalAnnualLettingValue());
			} else if (!property.getPropertyDetail().getPropertyTypeMaster().getCode()
					.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
					&& !property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					&& !property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				if (installment.equals(currentInstall)) {
					// FloorwiseDemandCalculations should be set only for the
					// current installment for each floor.
					for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
						ptDmdCalc.addFlrwiseDmdCalculations(createFloorDmdCalc(ptDmdCalc, floor,
								taxCalcInfo));
					}
				}
			}
		}

		property.setPtDemandSet(ptDmdSet);

		try {

			property = (PropertyImpl) new TaxXmlToDbConverterAdapter(
					TaxXMLToDBCoverterService.createConverter(property.getBasicProperty(),
							propertyTaxUtil, basicPrpertyService, ptNumberGenerator))
					.convertXmlToDB(property, oldProperty);

		} catch (Exception e) {
			LOGGER.error("Error while converting Tax XML", e);
			throw new EGOVRuntimeException("Error while converting Tax XML", e);
		}

		LOGGER.debug("Exiting from createDemand");
		return property;
	}

	/**
	 * Called to modify Property demands when the property is modified
	 *
	 * @param oldProperty
	 * @param newProperty
	 * @param dateOfCompletion
	 * @return newProperty
	 */
	public Property createDemandForModify(Property oldProperty, Property newProperty,
			Date dateOfCompletion) {
		LOGGER.debug("Entered into createDemandForModify");
		LOGGER.debug("createDemandForModify: oldProperty: " + oldProperty + ", newProperty: "
				+ newProperty + ", dateOfCompletion: " + dateOfCompletion);

		List<Installment> instList = new ArrayList<Installment>();
		instList = new ArrayList<Installment>(instTaxMap.keySet());
		LOGGER.debug("createDemandForModify: instList: " + instList);
		Ptdemand ptDemandOld = new Ptdemand();
		Ptdemand ptDemandNew = new Ptdemand();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		Installment currentInstall = installmentDao.getInsatllmentByModuleForGivenDate(module,
				new Date());
		Map<String, Ptdemand> oldPtdemandMap = getPtdemandsAsInstMap(oldProperty.getPtDemandSet());
		ptDemandOld = oldPtdemandMap.get(currentInstall.getDescription());
		PropertyTypeMaster oldPropTypeMaster = oldProperty.getPropertyDetail()
				.getPropertyTypeMaster();
		PropertyTypeMaster newPropTypeMaster = newProperty.getPropertyDetail()
				.getPropertyTypeMaster();
		Set<EgDemandDetails> newEgDmdDtlsSet = new HashSet<EgDemandDetails>();

		if (!oldProperty
				.getPropertyDetail()
				.getPropertyTypeMaster()
				.getCode()
				.equalsIgnoreCase(newProperty.getPropertyDetail().getPropertyTypeMaster().getCode())
				|| !oldProperty.getPropertyDetail().getExtra_field1()
						.equals(newProperty.getPropertyDetail().getExtra_field1())
				|| (isBigResidentialType(oldProperty.getPtDemandSet()) ^ isBigResidentialType(newProperty
						.getPtDemandSet()))
				|| oldProperty.getTaxExemptReason() == null
				^ newProperty.getTaxExemptReason() == null) {
			LOGGER.info("Old Property and New Property are different, Going for adjustments");

			LOGGER.info("newProperty:" + prepareRsnWiseDemandForOldProp(newProperty));

			LOGGER.info("------------------------Start OldProperty demand set ---------------------------");
			for (Ptdemand ptd : oldProperty.getPtDemandSet()) {
				LOGGER.info(ptd.getEgInstallmentMaster().getDescription() + " : "
						+ ptd.getEgDemandDetails());
			}
			LOGGER.info("------------------------End OldProperty demand set ---------------------------");
			LOGGER.info("------------------------Start New Property demand set ---------------------------");
			for (Ptdemand ptd : newProperty.getPtDemandSet()) {
				LOGGER.info(ptd.getEgInstallmentMaster().getDescription() + " : "
						+ ptd.getEgDemandDetails());
			}
			LOGGER.info("------------------------End New Property demand set ---------------------------");

			for (Installment installment : instList) {
				createAllDmdDeatails(oldProperty, newProperty, installment, instList, instTaxMap);
			}

			LOGGER.info("----------------------------------------------- Adjustments --------------------------------------------------");
			for (Ptdemand ptd : newProperty.getPtDemandSet()) {
				LOGGER.info(ptd.getEgInstallmentMaster().getDescription() + " : "
						+ ptd.getEgDemandDetails());
			}
			LOGGER.info("--------------------------------------------------------------------------------------------------------------");
		}

		Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());
		ptDemandNew = newPtdemandMap.get(currentInstall.getDescription());

		Map<Installment, Set<EgDemandDetails>> newDemandDtlsMap = getEgDemandDetailsSetAsMap(
				new ArrayList(ptDemandNew.getEgDemandDetails()), instList);

		List<EgDemandDetails> penaltyDmdDtlsList = null;

		for (Installment inst : instList) {

			newEgDmdDtlsSet = carryForwardCollection(newProperty, inst, newDemandDtlsMap.get(inst),
					ptDemandOld, oldPropTypeMaster, newPropTypeMaster);

			if (inst.equals(currentInstall)) {
				// carry forward the penalty from the old property to the new
				// property
				penaltyDmdDtlsList = getEgDemandDetailsListForReason(
						ptDemandOld.getEgDemandDetails(), DEMANDRSN_CODE_PENALTY_FINES);
				if (penaltyDmdDtlsList != null && penaltyDmdDtlsList.size() > 0) {
					ptDemandNew.getEgDemandDetails().addAll(penaltyDmdDtlsList);
				}
				penaltyDmdDtlsList = getEgDemandDetailsListForReason(
						ptDemandOld.getEgDemandDetails(), DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY);
				if (penaltyDmdDtlsList != null && penaltyDmdDtlsList.size() > 0) {
					ptDemandNew.getEgDemandDetails().addAll(penaltyDmdDtlsList);
				}
			}
		}

		// sort the installment list in ascending order to start the excessColl
		// adjustment from 1st inst
		LOGGER.info("before adjustExcessCollAmt newDemandDtlsMap.size: " + newDemandDtlsMap.size());
		Collections.sort(instList);

		if (!excessCollAmtMap.isEmpty()) {
			adjustExcessCollectionAmount(instList, newDemandDtlsMap, ptDemandNew);
		}

		LOGGER.debug("Exiting from createDemandForModify");
		return newProperty;
	}

	public Date getPropOccupatedDate(String dateOfCompletion) {
		LOGGER.debug("Entered into getPropOccupatedDate, dateOfCompletion: " + dateOfCompletion);
		Date occupationDate = null;
		try {
			occupationDate = sdf.parse(dateOfCompletion);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.debug("Exiting from getPropOccupatedDate");
		return occupationDate;
	}

	private Set<EgDemandDetails> createAllDmdDeatails(Installment installment,
			List<Installment> instList, HashMap<Installment, TaxCalculationInfo> instTaxMap) {
		LOGGER.debug("Entered into createAllDmdDeatails");
		LOGGER.debug("createAllDmdDeatails: installment: " + installment + ", instList: "
				+ instList + ", instTaxMap: " + instTaxMap);

		Set<EgDemandDetails> dmdDetSet = new HashSet<EgDemandDetails>();

		for (Installment inst : instList) {
			if (inst.getFromDate().before(installment.getFromDate())
					|| inst.getFromDate().equals(installment.getFromDate())) {
				TaxCalculationInfo taxCalcInfo = instTaxMap.get(inst);

				Map<String, BigDecimal> taxMap = taxCalculator.getMiscTaxesForProp(taxCalcInfo
						.getConsolidatedUnitTaxCalculationInfo());

				for (Map.Entry<String, BigDecimal> tax : taxMap.entrySet()) {
					EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(
							tax.getKey(), inst);
					dmdDetSet.add(createDemandDetails(tax.getValue(), egDmdRsn, inst));
				}
			}
		}

		LOGGER.debug("createAllDmdDeatails: dmdDetSet: " + dmdDetSet);
		return dmdDetSet;
	}

	@SuppressWarnings("unchecked")
	private void createAllDmdDeatails(Property oldProperty, Property newProperty,
			Installment installment, List<Installment> instList,
			HashMap<Installment, TaxCalculationInfo> instTaxMap) {
		LOGGER.debug("Entered into createAllDmdDeatails");
		LOGGER.debug("createAllDmdDeatails: oldProperty: " + oldProperty + ", newProperty: "
				+ newProperty + ",installment: " + installment + ", instList: " + instList);
		Set<EgDemandDetails> adjustedDmdDetailsSet = new HashSet<EgDemandDetails>();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		Installment currentInstall = installmentDao.getInsatllmentByModuleForGivenDate(module,
				new Date());

		Map<String, Ptdemand> oldPtdemandMap = getPtdemandsAsInstMap(oldProperty.getPtDemandSet());
		Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());

		Ptdemand ptDemandOld = new Ptdemand();
		Ptdemand ptDemandNew = new Ptdemand();

		Set<EgDemandDetails> newEgDemandDetailsSet = null;
		Set<EgDemandDetails> oldEgDemandDetailsSet = null;

		List<String> adjstmntReasons = new ArrayList<String>() {
			{
				add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
				add(DEMANDRSN_CODE_GENERAL_WATER_TAX);
				add(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX);
			}
		};

		List<String> rsnsForNewResProp = new ArrayList<String>() {
			{
				add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				add(DEMANDRSN_CODE_GENERAL_TAX);
				add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			}
		};

		List<String> rsnsForNewNonResProp = new ArrayList<String>() {
			{
				add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				add(DEMANDRSN_CODE_GENERAL_TAX);
				add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
			}
		};

		ptDemandOld = oldPtdemandMap.get(currentInstall.getDescription());
		ptDemandNew = newPtdemandMap.get(installment.getDescription());

		LOGGER.info("instList==========" + instList);

		Map<Installment, Set<EgDemandDetails>> oldDemandDtlsMap = getEgDemandDetailsSetAsMap(
				new ArrayList(ptDemandOld.getEgDemandDetails()), instList);
		LOGGER.info("oldDemandDtlsMap : " + oldDemandDtlsMap);

		for (Installment inst : instList) {
			oldEgDemandDetailsSet = new HashSet<EgDemandDetails>();

			oldEgDemandDetailsSet = oldDemandDtlsMap.get(inst);

			if (inst.getFromDate().before(installment.getFromDate())
					|| inst.getFromDate().equals(installment.getFromDate())) {
				LOGGER.info("inst==========" + inst);
				Set<EgDemandDetails> demandDtls = demandDetails.get(inst);
				if (demandDtls != null) {
					for (EgDemandDetails dd : demandDtls) {
						EgDemandDetails ddClone = (EgDemandDetails) dd.clone();
						ddClone.setEgDemand(ptDemandNew);
						adjustedDmdDetailsSet.add(ddClone);
					}
				} else {

					EgDemandDetails oldEgdmndDetails = null;
					EgDemandDetails newEgDmndDetails = null;

					newEgDemandDetailsSet = new HashSet<EgDemandDetails>();

					// Getting EgDemandDetails for inst installment

					for (EgDemandDetails edd : ptDemandNew.getEgDemandDetails()) {
						if (edd.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
							newEgDemandDetailsSet.add((EgDemandDetails) edd.clone());
						}
					}

					PropertyTypeMaster newPropTypeMaster = newProperty.getPropertyDetail()
							.getPropertyTypeMaster();

					LOGGER.info("Old Demand Set:" + inst + "=" + oldEgDemandDetailsSet);
					LOGGER.info("New Demand set:" + inst + "=" + newEgDemandDetailsSet);

					if (oldProperty.getTaxExemptReason() == null
							&& newProperty.getTaxExemptReason() == null) {
						for (int i = 0; i < adjstmntReasons.size(); i++) {
							String oldPropRsn = adjstmntReasons.get(i);
							String newPropRsn = null;

							/*
							 * Gives EgDemandDetails from newEgDemandDetailsSet
							 * for demand reason oldPropRsn, if we dont have
							 * EgDemandDetails then doing collection adjustments
							 */
							newEgDmndDetails = getEgDemandDetailsForReason(newEgDemandDetailsSet,
									oldPropRsn);

							if (newEgDmndDetails == null) {
								if (newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
									newPropRsn = rsnsForNewResProp.get(i);
								} else if (newPropTypeMaster.getCode().equalsIgnoreCase(
										PROPTYPE_NON_RESD)) {
									newPropRsn = rsnsForNewNonResProp.get(i);
								}

								oldEgdmndDetails = getEgDemandDetailsForReason(
										oldEgDemandDetailsSet, oldPropRsn);
								newEgDmndDetails = getEgDemandDetailsForReason(
										newEgDemandDetailsSet, newPropRsn);

								if (newEgDmndDetails != null && oldEgdmndDetails != null) {
									newEgDmndDetails.setAmtCollected(newEgDmndDetails
											.getAmtCollected().add(
													oldEgdmndDetails.getAmtCollected()));
								} else {
									continue;
								}
							}
						}
					} else {
						if (oldProperty.getTaxExemptReason() == null) {
							newEgDemandDetailsSet = adjustmentsForTaxExempted(
									ptDemandOld.getEgDemandDetails(), newEgDemandDetailsSet, inst);
						}
					}

					// Collection carry forward logic (This logic is moved out
					// of this method, bcoz it has to be invoked in all usecases
					// and not only when there is property type change

					newEgDemandDetailsSet = carryForwardCollection(newProperty, inst,
							newEgDemandDetailsSet, ptDemandOld, oldProperty.getPropertyDetail()
									.getPropertyTypeMaster(), newPropTypeMaster);
					LOGGER.info("Adjusted set:" + inst + ":" + newEgDemandDetailsSet);
					adjustedDmdDetailsSet.addAll(newEgDemandDetailsSet);
					demandDetails.put(inst, newEgDemandDetailsSet);
				}
			}
		}

		// forwards the base collection for current installment Ptdemand
		if (installment.equals(currentInstall)) {
			Ptdemand ptdOld = oldPtdemandMap.get(currentInstall.getDescription());
			Ptdemand ptdNew = newPtdemandMap.get(currentInstall.getDescription());
			ptdNew.setAmtCollected(ptdOld.getAmtCollected());
		}

		LOGGER.info("Exit from PropertyService.createAllDmdDeatails, Modify Adjustments for "
				+ oldProperty.getBasicProperty().getUpicNo() + " And installment : " + installment
				+ "\n\n" + adjustedDmdDetailsSet);
		ptDemandNew.setEgDemandDetails(adjustedDmdDetailsSet);
		LOGGER.debug("Exiting from createAllDmdDeatails");
	}

	private Set<EgDemandDetails> carryForwardCollection(Property newProperty, Installment inst,
			Set<EgDemandDetails> newEgDemandDetailsSet, Ptdemand ptDmndOld,
			PropertyTypeMaster oldPropTypeMaster, PropertyTypeMaster newPropTypeMaster) {
		LOGGER.debug("Entered into carryForwardCollection");
		LOGGER.debug("carryForwardCollection: newProperty: " + newProperty + ", inst: " + inst
				+ ", newEgDemandDetailsSet: " + newEgDemandDetailsSet + ", ptDmndOld: " + ptDmndOld
				+ ", oldPropTypeMaster: " + oldPropTypeMaster + ", newPropTypeMaster: "
				+ newPropTypeMaster);

		Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();

		List<String> demandReasonsWithAdvance = new ArrayList<String>(DEMAND_RSNS_LIST);
		demandReasonsWithAdvance.add(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE);

		for (String rsn : demandReasonsWithAdvance) {

			List<EgDemandDetails> oldEgDmndDtlsList = null;
			List<EgDemandDetails> newEgDmndDtlsList = null;

			/**
			 * When a unit in new property is exempted from tax, tax exemption
			 * made unit level instead of property level with new requirement,
			 * refer card #3427 Modified on 16 October 2014 [Nayeem]
			 */
			if (newProperty.getTaxExemptReason() != null
					&& !newProperty.getTaxExemptReason().equals("-1")) {
				if (!rsn.equalsIgnoreCase(DEMANDRSN_CODE_SEWERAGE_TAX)
						&& !rsn.equalsIgnoreCase(DEMANDRSN_CODE_LIGHTINGTAX)
						&& !rsn.equalsIgnoreCase(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {

					continue;
				}
			}

			if (rsn.equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
				if (oldPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)
						&& newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(
							ptDmndOld.getEgDemandDetails(), DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				} else if (oldPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)
						&& newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(
							ptDmndOld.getEgDemandDetails(), DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				} else if (oldPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)
						&& newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(
							ptDmndOld.getEgDemandDetails(), DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				} else {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(
							ptDmndOld.getEgDemandDetails(), DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				}
			} else {

				oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(),
						rsn);
				newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet, rsn);
			}

			Map<Installment, EgDemandDetails> oldDemandDtlsMap = null;
			Map<Installment, EgDemandDetails> newDemandDtlsMap = null;
			EgDemandDetails oldDmndDtls = null;
			EgDemandDetails newDmndDtls = null;

			if (oldEgDmndDtlsList != null) {
				oldDemandDtlsMap = getEgDemandDetailsAsMap(oldEgDmndDtlsList);
				oldDmndDtls = oldDemandDtlsMap.get(inst);
			}
			if (newEgDmndDtlsList != null) {
				newDemandDtlsMap = getEgDemandDetailsAsMap(newEgDmndDtlsList);
				newDmndDtls = newDemandDtlsMap.get(inst);
			}

			calculateExcessCollection(dmdRsnAmt, rsn, oldDmndDtls, newDmndDtls);
		}
		excessCollAmtMap.put(inst, dmdRsnAmt);

		demandDetails.put(inst, newEgDemandDetailsSet);
		LOGGER.debug("carryForwardCollection: newEgDemandDetailsSet: " + newEgDemandDetailsSet);
		LOGGER.debug("Exiting from carryForwardCollection");
		return newEgDemandDetailsSet;
	}

	public void calculateExcessCollection(Map<String, BigDecimal> dmdRsnAmt, String rsn,
			EgDemandDetails oldDmndDtls, EgDemandDetails newDmndDtls) {
		if (newDmndDtls != null && oldDmndDtls != null) {
			newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(
					oldDmndDtls.getAmtCollected()));
			newDmndDtls.setAmtRebate(newDmndDtls.getAmtRebate().add(oldDmndDtls.getAmtRebate()));
		} else if (newDmndDtls != null && oldDmndDtls == null) {
			newDmndDtls.setAmtCollected(ZERO);
			newDmndDtls.setAmtRebate(ZERO);
		}

		if (newDmndDtls != null && !rsn.equalsIgnoreCase(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE)) {
			// This part of code handles the adjustment of extra collections
			// when there is decrease in tax during property modification.

			BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(
					newDmndDtls.getAmount());
			// If there is extraColl then add to map
			if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
				dmdRsnAmt.put(rsn, extraCollAmt);
				newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
			}
		}

		/**
		 * after modify the old demand reason is not there in new property just
		 * take the entire collected amount as excess collection
		 *
		 * when a unit in new property is exempted from tax 16-Oct-2014 with new
		 * requirement, refer card #3427
		 */
		if (oldDmndDtls != null && newDmndDtls == null) {
			if (oldDmndDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0) {
				dmdRsnAmt.put(rsn, oldDmndDtls.getAmtCollected());
			}
		}
	}

	/**
	 * Called locally to get Map of Installment/Ptdemand pair
	 *
	 * @param ptdemandSet
	 * @return
	 */
	private Map<String, Ptdemand> getPtdemandsAsInstMap(Set<Ptdemand> ptdemandSet) {
		LOGGER.debug("Entered into getPtDemandsAsInstMap, PtDemandSet: " + ptdemandSet);
		Map<String, Ptdemand> ptDemandMap = new TreeMap<String, Ptdemand>();
		for (Ptdemand ptDmnd : ptdemandSet) {
			ptDemandMap.put(ptDmnd.getEgInstallmentMaster().getDescription(), ptDmnd);
		}
		LOGGER.debug("getPtDemandsAsInstMap, ptDemandMap: " + ptDemandMap);
		LOGGER.debug("Exiting from getPtDemandsAsInstMap");
		return ptDemandMap;
	}

	/**
	 * Called locally to get Map of Installment/EgDemandDetail pair from list of
	 * EgDemandDetails
	 *
	 * @param demandDetailsList
	 * @return demandDetailsMap
	 */
	public Map<Installment, EgDemandDetails> getEgDemandDetailsAsMap(
			List<EgDemandDetails> demandDetailsList) {
		LOGGER.debug("Entered into getEgDemandDetailsAsMap, demandDetailsList: "
				+ demandDetailsList);
		Map<Installment, EgDemandDetails> demandDetailsMap = new HashMap<Installment, EgDemandDetails>();
		for (EgDemandDetails dmndDtls : demandDetailsList) {
			demandDetailsMap.put(dmndDtls.getEgDemandReason().getEgInstallmentMaster(), dmndDtls);
		}
		LOGGER.debug("getEgDemandDetailsAsMap: demandDetailsMap: " + demandDetailsMap);
		LOGGER.debug("Exiting from getEgDemandDetailsAsMap");
		return demandDetailsMap;
	}

	/**
	 * Called locally to get Installment/Set<EgDemandDetails> pair map
	 *
	 * @param demandDetailsList
	 * @return
	 */
	public Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetAsMap(
			List<EgDemandDetails> demandDetailsList, List<Installment> instList) {
		LOGGER.debug("Entered into getEgDemandDetailsSetAsMap, demandDetailsList: "
				+ demandDetailsList + ", instList: " + instList);
		Map<Installment, Set<EgDemandDetails>> demandDetailsMap = new HashMap<Installment, Set<EgDemandDetails>>();
		Set<EgDemandDetails> ddSet = null;

		for (Installment inst : instList) {
			ddSet = new HashSet<EgDemandDetails>();
			for (EgDemandDetails dd : demandDetailsList) {
				if (dd.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
					ddSet.add(dd);
				}
			}
			demandDetailsMap.put(inst, ddSet);
		}
		LOGGER.debug("getEgDemandDetailsSetAsMap: demandDetailsMap: " + demandDetailsMap);
		LOGGER.debug("Exiting from getEgDemandDetailsSetAsMap");
		return demandDetailsMap;
	}

	/**
	 * Called locally to get EgDemandDetails from the egDemandDetailsSet for
	 * demand reason demandReason
	 *
	 * @param egDemandDetailsSet
	 * @param demandReason
	 * @return EgDemandDetails
	 */
	public EgDemandDetails getEgDemandDetailsForReason(Set<EgDemandDetails> egDemandDetailsSet,
			String demandReason) {
		LOGGER.debug("Entered into getEgDemandDetailsForReason, egDemandDetailsSet: "
				+ egDemandDetailsSet + ", demandReason: " + demandReason);
		List<Map<String, EgDemandDetails>> egDemandDetailsList = getEgDemandDetailsAsMap(egDemandDetailsSet);
		EgDemandDetails egDemandDetails = null;
		for (Map<String, EgDemandDetails> egDmndDtlsMap : egDemandDetailsList) {
			egDemandDetails = egDmndDtlsMap.get(demandReason);
			if (egDemandDetails != null) {
				break;
			}
		}
		LOGGER.debug("getEgDemandDetailsForReason: egDemandDetails: " + egDemandDetails);
		LOGGER.debug("Exiting from getEgDemandDetailsForReason");
		return egDemandDetails;
	}

	/**
	 * Called locally to get EgDemandDetails from the egDemandDetailsSet for
	 * demand reason demandReason
	 *
	 * @param egDemandDetailsSet
	 * @param demandReason
	 * @return EgDemandDetails
	 */
	private List<EgDemandDetails> getEgDemandDetailsListForReason(
			Set<EgDemandDetails> egDemandDetailsSet, String demandReason) {
		LOGGER.debug("Entered into getEgDemandDetailsListForReason: egDemandDetailsSet: "
				+ egDemandDetailsSet + ", demandReason: " + demandReason);
		List<Map<String, EgDemandDetails>> egDemandDetailsList = getEgDemandDetailsAsMap(egDemandDetailsSet);
		List<EgDemandDetails> demandListForReason = new ArrayList<EgDemandDetails>();
		for (Map<String, EgDemandDetails> egDmndDtlsMap : egDemandDetailsList) {
			if (egDmndDtlsMap.get(demandReason) != null) {
				demandListForReason.add(egDmndDtlsMap.get(demandReason));
			}
		}
		LOGGER.debug("getEgDemandDetailsListForReason: demandListForReason: " + demandListForReason);
		LOGGER.debug("Exiting from getEgDemandDetailsListForReason");
		return demandListForReason;
	}

	/**
	 * Called locally to get the egDemandDetailsSet as list of maps with demand
	 * reason as key and EgDemandDetails as value
	 *
	 * @param egDemandDetailsSet
	 * @param installment
	 * @return
	 */
	public List<Map<String, EgDemandDetails>> getEgDemandDetailsAsMap(
			Set<EgDemandDetails> egDemandDetailsSet) {
		LOGGER.debug("Entered into getEgDemandDetailsAsMap, egDemandDetailsSet: "
				+ egDemandDetailsSet);
		List<EgDemandDetails> egDemandDetailsList = new ArrayList<EgDemandDetails>(
				egDemandDetailsSet);
		List<Map<String, EgDemandDetails>> egDemandDetailsListOfMap = new ArrayList<Map<String, EgDemandDetails>>();

		for (EgDemandDetails egDmndDtls : egDemandDetailsList) {
			Map<String, EgDemandDetails> egDemandDetailsMap = new HashMap<String, EgDemandDetails>();
			EgDemandReasonMaster dmndRsnMstr = egDmndDtls.getEgDemandReason()
					.getEgDemandReasonMaster();
			if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_GENERAL_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_SEWERAGE_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_SEWERAGE_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_FIRE_SERVICE_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIGHTINGTAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_LIGHTINGTAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_WATER_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_GENERAL_WATER_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD, egDmndDtls);
			} else if (dmndRsnMstr.getCode()
					.equalsIgnoreCase(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_PENALTY_FINES, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					NMCPTISConstants.DEMANDRSN_CODE_ADVANCE)) {
				egDemandDetailsMap.put(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE, egDmndDtls);
			}
			egDemandDetailsListOfMap.add(egDemandDetailsMap);
		}
		LOGGER.debug("egDemandDetailsListOfMap: " + egDemandDetailsListOfMap
				+ "\n Exiting from getEgDemandDetailsAsMap");
		return egDemandDetailsListOfMap;
	}

	/**
	 * Called locally to Adjust EgDemandDetails for Tax Exempted property
	 *
	 * @param ptDemandOld
	 * @param newEgDemandDetails
	 * @return newEgDemandDetails
	 */
	private Set<EgDemandDetails> adjustmentsForTaxExempted(Set<EgDemandDetails> oldEgDemandDetails,
			Set<EgDemandDetails> newEgDemandDetails, Installment inst) {
		LOGGER.debug("Entered into adjustmentsForTaxExempted, oldEgDemandDetails: "
				+ oldEgDemandDetails + ", newEgDemandDetails: " + newEgDemandDetails + ", inst:"
				+ inst);
		BigDecimal totalDmndAdjstmntAmnt = BigDecimal.ZERO;
		BigDecimal totalCollAdjstmntAmnt = BigDecimal.ZERO;

		for (EgDemandDetails egDmndDtls : oldEgDemandDetails) {
			if (egDmndDtls.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
				EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason()
						.getEgDemandReasonMaster();
				if (!egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_SEWERAGE_TAX)
						&& !egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIGHTINGTAX)
						&& !egDmndRsnMstr.getCode().equalsIgnoreCase(
								DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {
					// totalDmndAdjstmntAmnt =
					// totalDmndAdjstmntAmnt.add(egDmndDtls.getAmount().subtract(
					// egDmndDtls.getAmtCollected()));
					totalCollAdjstmntAmnt = totalCollAdjstmntAmnt.add(egDmndDtls.getAmtCollected());
				}
			}
		}

		List<EgDemandDetails> newEgDmndDetails = new ArrayList<EgDemandDetails>(newEgDemandDetails);

		for (EgDemandDetails egDmndDtls : newEgDemandDetails) {

			EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason()
					.getEgDemandReasonMaster();

			if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_SEWERAGE_TAX)) {

				egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.50")));

			} else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIGHTINGTAX)) {

				egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));

			} else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {

				egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));
			}
		}
		LOGGER.debug("newEgDmndDetails: " + newEgDmndDetails
				+ "\nExiting from adjustmentsForTaxExempted");
		return (new HashSet<EgDemandDetails>(newEgDmndDetails));
	}

	/**
	 * Called to determine whether the Property is of Big Residential type
	 *
	 * @param Ptdemand
	 *            of Property
	 * @return true if Big Residential type
	 */
	private Boolean isBigResidentialType(Set<Ptdemand> ptDemandSet) {
		LOGGER.debug("Entred into isBigResidentialType, ptDenandSet: " + ptDemandSet);
		Boolean isBigResi = FALSE;
		for (Ptdemand ptDemand : ptDemandSet) {
			for (EgDemandDetails egDmndDtls : ptDemand.getEgDemandDetails()) {
				if (egDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode()
						.equalsIgnoreCase(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
					isBigResi = TRUE;
					break;
				}
			}
		}
		LOGGER.debug("isBigResi: " + isBigResi + "\n Exiting from isBigResidentialType");
		return isBigResi;
	}

	private EgDemandDetails createDemandDetails(BigDecimal amount, EgDemandReason dmdRsn,
			Installment inst) {
		LOGGER.debug("Entered into createDemandDetails, amount: " + amount + ", dmdRsn: " + dmdRsn
				+ ", inst: " + inst);
		EgDemandDetails demandDetail = new EgDemandDetails();
		demandDetail.setAmount(amount);
		demandDetail.setAmtCollected(BigDecimal.ZERO);
		demandDetail.setAmtRebate(BigDecimal.ZERO);
		demandDetail.setEgDemandReason(dmdRsn);
		demandDetail.setCreateTimestamp(new Date());
		demandDetail.setLastUpdatedTimeStamp(new Date());
		LOGGER.debug("demandDetail: " + demandDetail + "\nExiting from createDemandDetails");
		return demandDetail;
	}

	public EgDemandDetails createDemandDetails(BigDecimal amount, BigDecimal amountCollected,
			EgDemandReason dmdRsn, Installment inst) {
		LOGGER.debug("Entered into createDemandDetails, amount: " + amount + "amountCollected: "
				+ amountCollected + ", dmdRsn: " + dmdRsn + ", inst: " + inst);
		EgDemandDetails demandDetail = new EgDemandDetails();
		demandDetail.setAmount(amount != null ? amount : BigDecimal.ZERO);
		demandDetail.setAmtCollected(amountCollected != null ? amountCollected : BigDecimal.ZERO);
		demandDetail.setAmtRebate(BigDecimal.ZERO);
		demandDetail.setEgDemandReason(dmdRsn);
		demandDetail.setCreateTimestamp(new Date());
		demandDetail.setLastUpdatedTimeStamp(new Date());
		LOGGER.debug("demandDetail: " + demandDetail + "\nExiting from createDemandDetails");
		return demandDetail;
	}

	private FloorwiseDemandCalculations createFloorDmdCalc(PTDemandCalculations ptDmdCal,
			FloorIF floor, TaxCalculationInfo taxCalcInfo) {
		// LOGGER.debug("Entered into createFloorDmdCalc, ptDmdCal: " + ptDmdCal
		// + ", floor: " + floor + ", taxCalcInfo: " + taxCalcInfo);
		FloorwiseDemandCalculations floorDmdCalc = new FloorwiseDemandCalculations();

		try {
			for (List<UnitTaxCalculationInfo> unitTaxs : taxCalcInfo.getUnitTaxCalculationInfos()) {

				floorDmdCalc.setPTDemandCalculations(ptDmdCal);
				floorDmdCalc.setFloor(floor);

				for (UnitTaxCalculationInfo unitTax : unitTaxs) {
					/**
					 * This condition is applied because floor number is not
					 * mandatory in case of Mixed Property.(for UnitType =
					 * OPEN_PLOT). So UnitType(mandatory only in case of Mixed
					 * Property) is considered. The main purpose of this
					 * condition is to check each unitTax corresponds to which
					 * floor
					 */
					if (((floor.getUnitType() == null || !floor.getUnitType().getCode()
							.equals(PROPTYPE_OPEN_PLOT))
							&& unitTax.getFloorNumberInteger().equals(floor.getFloorNo())
							&& unitTax.getUnitNumber().equals(
									Integer.valueOf(floor.getExtraField1())) && (unitTax
								.getUnitArea().toString()).equals(floor.getBuiltUpArea().getArea()
							.toString()))
							|| (floor.getUnitType() != null
									&& floor.getUnitType().getCode().equals(PROPTYPE_OPEN_PLOT)
									&& unitTax.getUnitNumber().equals(
											Integer.valueOf(floor.getExtraField1()))
									&& (unitTax.getUnitArea().toString()).equals(floor
											.getBuiltUpArea().getArea().toString())
									&& unitTax.getUnitUsage().equals(
											floor.getPropertyUsage().getUsageName())
									&& unitTax.getUnitOccupation().equals(
											floor.getPropertyOccupation().getOccupation()) && unitTax
									.getOccpancyDate().equals(sdf.parse(floor.getExtraField3())))) {

						setFloorDmdCalTax(unitTax, floorDmdCalc);
					}
				}
			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.debug("floorDmdCalc: " + floorDmdCalc + "\nExiting from createFloorDmdCalc");
		return floorDmdCalc;
	}

	public void setFloorDmdCalTax(UnitTaxCalculationInfo unitTax,
			FloorwiseDemandCalculations floorDmdCalc) {
		floorDmdCalc.setAlv(unitTax.getAnnualRentAfterDeduction());
		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				if (NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax1(floorDmdCalc.getTax1().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax2(floorDmdCalc.getTax2().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX
						.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax3(floorDmdCalc.getTax3().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax4(floorDmdCalc.getTax4().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX.equals(miscTax
						.getTaxName())) {
					floorDmdCalc.setTax5(floorDmdCalc.getTax5().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX.equals(miscTax
						.getTaxName())) {
					floorDmdCalc.setTax6(floorDmdCalc.getTax6().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX.equals(miscTax
						.getTaxName())) {
					floorDmdCalc.setTax7(floorDmdCalc.getTax7().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD.equals(miscTax
						.getTaxName())) {
					floorDmdCalc.setTax8(floorDmdCalc.getTax8().add(
							taxDetail.getCalculatedTaxValue()));
				} else if (NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD.equals(miscTax
						.getTaxName())) {
					floorDmdCalc.setTax9(floorDmdCalc.getTax9().add(
							taxDetail.getCalculatedTaxValue()));
				}
			}
		}
	}

	public void createAttributeValues(Property property, Installment curInstall) {
		LOGGER.debug("Entered into createAttributeValues, property: " + property + ", curInstall: "
				+ curInstall);
		Set<Ptdemand> ptDmdSet = property.getPtDemandSet();
		if (currentInstall == null) {
			currentInstall = curInstall;
		}
		TaxCalculationInfo taxCalInfo = instTaxMap.get(currentInstall);
		if (ptDmdSet != null && ptDmdSet.size() > 0) {
			Ptdemand propPtDemand = null;
			for (Ptdemand ptDemand : ptDmdSet) {
				if (ptDemand.getEgInstallmentMaster().equals(currentInstall)
						&& ptDemand.getIsHistory().equalsIgnoreCase("N")) {
					propPtDemand = ptDemand;
				}
				propPtDemand = ptDemand;
			}

			Set<FloorwiseDemandCalculations> floorDmdCalcSet = propPtDemand.getDmdCalculations()
					.getFlrwiseDmdCalculations();
			if (floorDmdCalcSet != null && floorDmdCalcSet.size() > 0) {
				List<List<UnitTaxCalculationInfo>> unitTaxCalInfos = taxCalInfo
						.getUnitTaxCalculationInfos();
				for (FloorwiseDemandCalculations floorDmdCalc : floorDmdCalcSet) {
					FloorIF floor = floorDmdCalc.getFloor();
					UnitTaxCalculationInfo unitTaxCalInfo1 = null;
					String floorString = (floor.getFloorNo() == null || floor.getFloorNo().equals(
							OPEN_PLOT_UNIT_FLOORNUMBER)) ? propertyTaxUtil
							.getFloorStr(OPEN_PLOT_UNIT_FLOORNUMBER) : propertyTaxUtil
							.getFloorStr(floor.getFloorNo());

					try {
						for (List<UnitTaxCalculationInfo> unitTaxCalcs : unitTaxCalInfos) {

							UnitTaxCalculationInfo unitTaxCalInfo = unitTaxCalcs.get(0);
							/**
							 * This condition is applied because floor number is
							 * not mandatory in case of Mixed Property.(for
							 * UnitType = OPEN_PLOT). So UnitType(mandatory only
							 * in case of Mixed Property) is considered.
							 */
							if (((floor.getUnitType() == null || !floor.getUnitType().getCode()
									.equals(PROPTYPE_OPEN_PLOT))
									&& (unitTaxCalInfo.getFloorNumber() == null || unitTaxCalInfo
											.getFloorNumber().equalsIgnoreCase(floorString))
									&& unitTaxCalInfo.getUnitNumber()
											.equals(floor.getExtraField1()) && (unitTaxCalInfo
										.getUnitArea().toString()).equals(floor.getBuiltUpArea()
									.getArea().toString()))
									|| (floor.getUnitType() != null
											&& floor.getUnitType().getCode()
													.equals(PROPTYPE_OPEN_PLOT)
											&& unitTaxCalInfo.getUnitNumber().equals(
													floor.getExtraField1())
											&& (unitTaxCalInfo.getUnitArea().toString())
													.equals(floor.getBuiltUpArea().getArea()
															.toString())
											&& unitTaxCalInfo.getUnitUsage().equals(
													floor.getPropertyUsage().getUsageName())
											&& unitTaxCalInfo.getUnitOccupation().equals(
													floor.getPropertyOccupation().getOccupation()) && unitTaxCalInfo
											.getOccpancyDate().equals(
													sdf.parse(floor.getExtraField3())))) {
								unitTaxCalInfo1 = unitTaxCalInfo;
								break;

							}
							if ((unitTaxCalInfo.getFloorNumber() != null && unitTaxCalInfo
									.getFloorNumber().equalsIgnoreCase(floorString))
									&& unitTaxCalInfo.getUnitNumber()
											.equals(floor.getExtraField1())) {
								unitTaxCalInfo1 = unitTaxCalInfo;
								break;
							}
						}
					} catch (ParseException e) {
						LOGGER.error(e.getMessage(), e);
					}
					// TODO -- Fix me (Commented to Resolve compilation issues)
					/*
					 * if (unitTaxCalInfo1 != null &&
					 * !property.getPropertyDetail()
					 * .getPropertyTypeMaster().getCode()
					 * .equalsIgnoreCase(PROPTYPE_STATE_GOVT) &&
					 * !property.getPropertyDetail()
					 * .getPropertyTypeMaster().getCode()
					 * .equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))
					 * createAttributeValue(NMCPTISConstants.ALV, floorDmdCalc,
					 * unitTaxCalInfo1 .getAnnualRentAfterDeduction()
					 * .toString());
					 */
				}
			}
		}
		LOGGER.debug("Exiting from createAttributeValues");
	}

	// TODO -- Fix me (Commented to Resolve compilation issues)
	/*
	 * private EgAttributevalues createAttributeValue(String attributeType,
	 * FloorwiseDemandCalculations floorDmdCalc, String attrValue) {
	 * LOGGER.debug("Entered into createAttributeValue, attributeType: " +
	 * attributeType + ", floorDmdCalc: " + floorDmdCalc + ", attrValue: " +
	 * attrValue); EgAttributevalues attributeVal = new EgAttributevalues();
	 * EgAttributetypeDao egAttrTypeDao = FlexfieldsDaoFactory.getDAOFactory()
	 * .getEgAttributetypeDao(); EgAttributevaluesDao egAttrValDao =
	 * FlexfieldsDaoFactory .getDAOFactory().getEgAttributevaluesDao();
	 * EgAttributetype attrType = egAttrTypeDao
	 * .getAttributeTypeByDomainNameAndAttrName(floorDmdCalc
	 * .getClass().getName(), attributeType);
	 * attributeVal.setEgApplDomain(attrType.getEgApplDomain());
	 * attributeVal.setEgAttributetype(attrType);
	 * attributeVal.setDomaintxnid(Long.valueOf(floorDmdCalc.getId()));
	 * attributeVal.setAttValue(attrValue); egAttrValDao.create(attributeVal);
	 * LOGGER.debug("attributeVal: " + attributeVal +
	 * "\nExiting from createAttributeValue"); return attributeVal; }
	 */

	public Date getLowestDtOfCompFloorWise(List<FloorImpl> floorList) {
		LOGGER.debug("Entered into getLowestDtOfCompFloorWise, floorList: " + floorList);
		Date completionDate = null;
		for (FloorIF floor : floorList) {
			Date floorDate = null;
			if (floor != null) {
				try {
					if (floor.getExtraField3() != null) {
						floorDate = sdf.parse(floor.getExtraField3());
					}
				} catch (ParseException e) {
					LOGGER.error(e.getMessage(), e);
				}
				if (floorDate != null) {
					if (completionDate == null) {
						completionDate = floorDate;
					} else if (completionDate.after(floorDate)) {
						completionDate = floorDate;
					}
				}
			}
		}
		LOGGER.debug("completionDate: " + completionDate
				+ "\nExiting from getLowestDtOfCompFloorWise");
		return completionDate;
	}

	public void createAmalgPropStatVal(String[] amalgPropIds, BasicProperty parentBasicProperty) {
		LOGGER.debug("Entered into createAmalgPropStatVal, amalgPropIds(length): "
				+ ((amalgPropIds != null) ? amalgPropIds.length : ZERO) + ", parentBasicProperty: "
				+ parentBasicProperty);
		List<PropertyStatusValues> activePropStatVal = propPerServ.findAllByNamedQuery(
				QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, parentBasicProperty.getUpicNo(), "Y",
				NMCPTISConstants.PROP_CREATE_RSN);
		LOGGER.debug("createAmalgPropStatVal: activePropStatVal: " + activePropStatVal);
		for (PropertyStatusValues propstatval : activePropStatVal) {
			propstatval.setIsActive("N");
		}

		for (String amalgId : amalgPropIds) {
			if (amalgId != null && !amalgId.equals("")) {
				BasicProperty amalgBasicProp = (BasicProperty) getPropPerServ().findByNamedQuery(
						NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO, amalgId);
				PropertyStatusValues amalgPropStatVal = new PropertyStatusValues();
				PropertyStatus propertyStatus = (PropertyStatus) getPropPerServ().find(
						"from PropertyStatus where statusCode=?", PROPERTY_STATUS_MARK_DEACTIVE);
				amalgPropStatVal.setIsActive("Y");
				amalgPropStatVal.setPropertyStatus(propertyStatus);
				amalgPropStatVal.setReferenceDate(new Date());
				amalgPropStatVal.setReferenceNo("0001");
				amalgPropStatVal.setRemarks("Property Amalgamated");
				amalgBasicProp.addPropertyStatusValues(amalgPropStatVal);
				// At final approval a new PropetyStatusValues has to created
				// with status INACTIVE and set the amalgBasicProp status as
				// INACTIVE and ISACTIVE as 'N'
				amalgPropStatVal.setBasicProperty(amalgBasicProp);

				PropertyStatusValues propertyStatusValueschild = new PropertyStatusValues();
				PropertyStatus propertyStatuschild = (PropertyStatus) getPropPerServ().find(
						"from PropertyStatus where statusCode=?", "CREATE");
				propertyStatusValueschild.setIsActive("Y");
				propertyStatusValueschild.setPropertyStatus(propertyStatuschild);
				propertyStatusValueschild.setReferenceDate(new Date());
				propertyStatusValueschild.setReferenceNo("0001");
				propertyStatusValueschild.setReferenceBasicProperty(amalgBasicProp);
				parentBasicProperty.addPropertyStatusValues(propertyStatusValueschild);
				propertyStatusValueschild.setBasicProperty(parentBasicProperty);
				LOGGER.debug("propertyStatusValueschild: " + propertyStatusValueschild);
			}
		}
		LOGGER.debug("Exiting from createAmalgPropStatVal");
	}

	public Property createArrearsDemand(Property oldproperty, Date dateOfCompletion,
			PropertyImpl property) {
		LOGGER.debug("Entered into createArrearsDemand, oldproperty: " + oldproperty
				+ ", dateOfCompletion: " + dateOfCompletion + ", property: " + property);
		Ptdemand oldPtDmd = null;
		Ptdemand currPtDmd = null;
		Ptdemand oldCurrPtDmd = null;
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		Installment effectiveInstall = installmentDao.getInsatllmentByModuleForGivenDate(module,
				dateOfCompletion);
		Installment currInstall = installmentDao.getInsatllmentByModuleForGivenDate(module,
				new Date());
		for (Ptdemand demand : property.getPtDemandSet()) {
			if (demand.getIsHistory().equalsIgnoreCase("N")) {
				if (demand.getEgInstallmentMaster().equals(currInstall)) {
					currPtDmd = demand;
					break;
				}
			}
		}
		for (Ptdemand ptDmd : oldproperty.getPtDemandSet()) {
			if (ptDmd.getIsHistory().equalsIgnoreCase("N")) {
				if ((ptDmd.getEgInstallmentMaster().getFromDate()).before(effectiveInstall
						.getFromDate())) {
					oldPtDmd = (Ptdemand) ptDmd.clone();
					oldPtDmd.setEgptProperty(property);
					property.addPtDemand(oldPtDmd);
				}
				if (ptDmd.getEgInstallmentMaster().equals(currInstall)) {
					oldCurrPtDmd = ptDmd;
				}
			}
		}

		addArrDmdDetToCurrentDmd(oldCurrPtDmd, currPtDmd, effectiveInstall);

		LOGGER.debug("Exiting from createArrearsDemand");
		return property;
	}

	private void addArrDmdDetToCurrentDmd(Ptdemand ptDmd, Ptdemand currPtDmd,
			Installment effectiveInstall) {
		LOGGER.debug("Entered into addArrDmdDetToCurrentDmd. ptDmd: " + ptDmd + ", currPtDmd: "
				+ currPtDmd);
		for (EgDemandDetails dmdDet : ptDmd.getEgDemandDetails()) {
			if ((dmdDet.getEgDemandReason().getEgInstallmentMaster().getFromDate())
					.before(effectiveInstall.getFromDate())) {
				currPtDmd.addEgDemandDetails((EgDemandDetails) dmdDet.clone());
			}
		}
		LOGGER.debug("Exiting from addArrDmdDetToCurrentDmd");
	}

	/**
	 * The purpose of this api is to initiate modify property workflow once the
	 * objection workflow has ended.
	 *
	 * @param propertyId
	 *            (Is the BasicProperty upicNo)
	 * @param objectionNum
	 * @param objectionDate
	 * @param objWfInitiator
	 *            (This is the objection workflow initiator, who will be set as
	 *            the initiator of modify property initiator/owner)
	 */
	public void initiateModifyWfForObjection(Long basicPropId, String objectionNum,
			Date objectionDate, User objWfInitiator, String docNumber, String modifyRsn) {
		LOGGER.debug("Entered into initiateModifyWfForObjection, basicPropId: " + basicPropId
				+ ", objectionNum: " + objectionNum + ", objectionDate: " + objectionDate
				+ ", objWfInitiator: " + objWfInitiator);
		// Retrieve BasicProperty by basicPropId bcoz, upicno will be generated
		// during final approval for create property and this
		// api is used to initiate modify workflow before upicno is generated
		BasicProperty basicProperty = ((BasicProperty) getPropPerServ().findByNamedQuery(
				NMCPTISConstants.QUERY_BASICPROPERTY_BY_BASICPROPID, basicPropId));

		basicProperty.setAllChangesCompleted(FALSE);

		LOGGER.debug("initiateModifyWfForObjection: basicProperty: " + basicProperty);
		PropertyImpl oldProperty = ((PropertyImpl) basicProperty.getProperty());
		PropertyImpl newProperty = (PropertyImpl) oldProperty.createPropertyclone();
		LOGGER.debug("initiateModifyWfForObjection: oldProperty: " + oldProperty
				+ ", newProperty: " + newProperty);
		List floorProxy = new ArrayList();
		String propUsageId = null;
		String propOccId = null;

		Date propCompletionDate = getPropertyCompletionDate(basicProperty, newProperty);

		for (FloorIF floor : newProperty.getPropertyDetail().getFloorDetails()) {
			if (floor != null) {
				floorProxy.add(floor);
			}
		}
		newProperty.getPropertyDetail().setFloorDetailsProxy(floorProxy);
		basicProperty.addPropertyStatusValues(createPropStatVal(basicProperty,
				PROPERTY_MODIFY_REASON_MODIFY, propCompletionDate, objectionNum, objectionDate,
				null, null));
		if (newProperty.getPropertyDetail().getPropertyOccupation() != null) {
			propOccId = newProperty.getPropertyDetail().getPropertyOccupation().getId().toString();
		}
		if (newProperty.getPropertyDetail().getPropertyUsage() != null) {
			propUsageId = newProperty.getPropertyDetail().getPropertyUsage().getId().toString();
		}
		newProperty = createProperty(newProperty, null, modifyRsn, newProperty.getPropertyDetail()
				.getPropertyTypeMaster().getId().toString(), propUsageId, propOccId,
				STATUS_WORKFLOW, null, null, false);

		newProperty.setStatus(STATUS_WORKFLOW);
		// Setting the property state to the objection workflow initiator
		Position owner = eisCommonsService.getPositionByUserId(Integer.valueOf(objWfInitiator
				.getId().toString()));
		String desigName = owner.getDeptDesigId().getDesigId().getDesignationName();
		String value = WFLOW_ACTION_NAME_MODIFY + ":" + desigName + "_" + WF_STATE_APPROVAL_PENDING;

		newProperty.transition(true).start().withSenderName(objWfInitiator.getName())
				.withComments(PROPERTY_WORKFLOW_STARTED).withStateValue(value).withOwner(owner)
				.withDateInfo(new Date());

		// Notice type doesnt exist for objection so set the notice type to 127
		if (newProperty.getExtra_field2() == null || newProperty.getExtra_field2().equals("")) {
			newProperty.setExtra_field2(NOTICE127);
		}
		newProperty.setExtra_field3(null);
		newProperty.setExtra_field4(null);
		newProperty.setExtra_field5(null);

		newProperty.setBasicProperty(basicProperty);

		newProperty.getPtDemandSet().clear();
		createDemand(newProperty, oldProperty, propCompletionDate, false);
		createArrearsDemand(oldProperty, propCompletionDate, newProperty);
		basicProperty.addProperty(newProperty);

		basicProperty = basicPrpertyService.update(basicProperty);
		if (!newProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
				.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			createAttributeValues(newProperty, null);
		}
		LOGGER.debug("Exiting from initiateModifyWfForObjection");
	}

	private Date getPropertyCompletionDate(BasicProperty basicProperty, PropertyImpl newProperty) {
		LOGGER.debug("Entered into getPropertyCompletionDate - basicProperty.upicNo="
				+ basicProperty.getUpicNo());
		Date propCompletionDate = null;
		String propertyTypeMasterCode = newProperty.getPropertyDetail().getPropertyTypeMaster()
				.getCode();
		if (propertyTypeMasterCode.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
				|| ((propertyTypeMasterCode.equalsIgnoreCase(PROPTYPE_STATE_GOVT) || propertyTypeMasterCode
						.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) && newProperty
						.getPropertyDetail().getFloorDetails().isEmpty())) {
			for (PropertyStatusValues propstatval : basicProperty.getPropertyStatusValuesSet()) {
				if (propstatval.getExtraField1() != null) {
					try {
						propCompletionDate = sdf.parse(propstatval.getExtraField1());
					} catch (ParseException e) {
						LOGGER.error(e.getMessage(), e);
					}
				} else {
					propCompletionDate = basicProperty.getPropCreateDate();
				}
			}
		} else {
			List floorList = new ArrayList(newProperty.getPropertyDetail().getFloorDetails());
			propCompletionDate = getLowestDtOfCompFloorWise(floorList);
			if (propCompletionDate == null) {
				propCompletionDate = basicProperty.getPropCreateDate();
			}
		}

		LOGGER.debug("propCompletionDate=" + propCompletionDate
				+ "\nExiting from getPropertyCompletionDate");
		return propCompletionDate;
	}

	/**
	 * Copies the owners from old property to new property
	 *
	 * @param newProp
	 * @param oldProp
	 * @return @PropertyImpl
	 */
	public Property createOwnersForNew(Property newProp, Property oldProp) {
		LOGGER.debug("Entered into createOwnersForNew, newProp: " + newProp + ", OldProp; "
				+ oldProp);
		for (PropertyOwner owner : oldProp.getPropertyOwnerSet()) {
			PropertyOwner newOwner = new PropertyOwner();
			String ownerName = owner.getName();
			ownerName = propertyTaxUtil.antisamyHackReplace(ownerName);
			newOwner.setName(ownerName);
			newOwner.setOrderNo(owner.getOrderNo());

			for (Address address : owner.getAddress()) {
				Address ownerAddr = new Address();
				String street = address.getStreetRoadLine();
				if (street != null && !street.isEmpty()) {
					street = propertyTaxUtil.antisamyHackReplace(street);
				}
				ownerAddr.setType(address.getType());
				ownerAddr.setStreetRoadLine(street);
				ownerAddr.setHouseNoBldgApt(address.getHouseNoBldgApt());
				if (address.getPinCode() != null && !address.getPinCode().isEmpty()) {
					ownerAddr.setPinCode(address.getPinCode());
				}
				newOwner.addAddress(ownerAddr);
			}

			newProp.addPropertyOwners(newOwner);
		}
		LOGGER.debug("Exiting from createOwnersForNew");
		return newProp;
	}

	public PersistenceService getPropPerServ() {
		return propPerServ;
	}

	public void setPropPerServ(PersistenceService propPerServ) {
		this.propPerServ = propPerServ;
	}

	public TaxCalculator getTaxCalculator() {
		return taxCalculator;
	}

	public void setTaxCalculator(TaxCalculator taxCalculator) {
		this.taxCalculator = taxCalculator;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	// setting property status values to Basic Property
	public void setWFPropStatValActive(BasicProperty basicProperty) {
		LOGGER.debug("Entered into setWFPropStatValActive, basicProperty: " + basicProperty);
		for (PropertyStatusValues psv : basicProperty.getPropertyStatusValuesSet()) {
			if (PROPERTY_MODIFY_REASON_MODIFY.equals(psv.getPropertyStatus().getStatusCode())
					&& psv.getIsActive().equals("W")) {
				PropertyStatusValues activePropStatVal = (PropertyStatusValues) propPerServ
						.findByNamedQuery(QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE,
								basicProperty.getUpicNo(), "Y",
								NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY);
				if (activePropStatVal != null) {
					activePropStatVal.setIsActive("N");
				}
				PropertyStatusValues wfPropStatVal = (PropertyStatusValues) propPerServ
						.findByNamedQuery(QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE,
								basicProperty.getUpicNo(), "W",
								NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY);
				if (wfPropStatVal != null) {
					wfPropStatVal.setIsActive("Y");
				}
			}
			if (PROPERTY_MODIFY_REASON_AMALG.equals(psv.getPropertyStatus().getStatusCode())
					&& psv.getIsActive().equals("W")) {
				PropertyStatusValues activePropStatVal = (PropertyStatusValues) propPerServ
						.findByNamedQuery(QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE,
								basicProperty.getUpicNo(), "Y",
								NMCPTISConstants.PROPERTY_MODIFY_REASON_AMALG);
				if (activePropStatVal != null) {
					activePropStatVal.setIsActive("N");
				}
				PropertyStatusValues wfPropStatVal = (PropertyStatusValues) propPerServ
						.findByNamedQuery(QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE,
								basicProperty.getUpicNo(), "W",
								NMCPTISConstants.PROPERTY_MODIFY_REASON_AMALG);
				if (wfPropStatVal != null) {
					wfPropStatVal.setIsActive("Y");
				}
			}
			if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(psv.getPropertyStatus().getStatusCode())
					&& psv.getIsActive().equals("W")) {
				PropertyStatusValues activePropStatVal = (PropertyStatusValues) propPerServ
						.findByNamedQuery(QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE,
								basicProperty.getUpicNo(), "Y",
								NMCPTISConstants.PROPERTY_MODIFY_REASON_BIFURCATE);
				if (activePropStatVal != null) {
					activePropStatVal.setIsActive("N");
				}
				PropertyStatusValues wfPropStatVal = (PropertyStatusValues) propPerServ
						.findByNamedQuery(QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE,
								basicProperty.getUpicNo(), "W",
								NMCPTISConstants.PROPERTY_MODIFY_REASON_BIFURCATE);
				LOGGER.debug("setWFPropStatValActive: wfPropStatVal: " + wfPropStatVal);
				if (wfPropStatVal != null) {
					wfPropStatVal.setIsActive("Y");
				}
			}
		}
		LOGGER.debug("Exitinf from setWFPropStatValActive");
	}

	/**
	 * Prepares a map of installment and respective reason wise demand for each
	 * installment
	 *
	 * @param property
	 * @return Map of installment and respective reason wise demand for each
	 *         installment
	 */
	public Map<Installment, Map<String, BigDecimal>> populateTaxesForVoucherCreation(
			Property property) {
		LOGGER.debug("Entered into populateTaxesForVoucherCreation, property: " + property);
		Map<Installment, Map<String, BigDecimal>> amounts = new HashMap<Installment, Map<String, BigDecimal>>();
		if ((instTaxMap != null)) {
			for (Map.Entry<Installment, TaxCalculationInfo> instTaxRec : instTaxMap.entrySet()) {
				Map<String, BigDecimal> taxMap = taxCalculator.getMiscTaxesForProp(instTaxRec
						.getValue().getConsolidatedUnitTaxCalculationInfo());
				amounts.put(instTaxRec.getKey(), taxMap);
			}
		} else {
			amounts = prepareRsnWiseDemandForOldProp(property);
		}
		LOGGER.debug("amounts: " + amounts + "\nExiting from populateTaxesForVoucherCreation");
		return amounts;
	}

	/**
	 * Prepares a map of installment and respective reason wise demand for each
	 * installment
	 *
	 * @param property
	 * @return Map of installment and respective reason wise demand for each
	 *         installment
	 */
	public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForOldProp(
			Property property) {
		LOGGER.debug("Entered into prepareRsnWiseDemandForOldProp, property: " + property);
		Installment inst = null;
		Map<Installment, Map<String, BigDecimal>> instWiseDmd = new HashMap<Installment, Map<String, BigDecimal>>();
		for (Ptdemand ptdemand : property.getPtDemandSet()) {
			if (ptdemand.getIsHistory().equals("N")) {
				inst = ptdemand.getEgInstallmentMaster();
				Map<String, BigDecimal> rsnWiseDmd = new HashMap<String, BigDecimal>();
				for (EgDemandDetails dmdDet : ptdemand.getEgDemandDetails()) {
					if (inst.equals(dmdDet.getEgDemandReason().getEgInstallmentMaster())) {
						if (!dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
								.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
								&& !dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
										.equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
							rsnWiseDmd.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster()
									.getCode(), dmdDet.getAmount());
						}
					}
				}
				instWiseDmd.put(inst, rsnWiseDmd);
			}
		}
		LOGGER.debug("Exiting from prepareRsnWiseDemandForOldProp");
		return instWiseDmd;
	}

	public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForPropToBeDeactivated(
			Property property) {
		LOGGER.debug("Entered into prepareRsnWiseDemandForPropToBeDeactivated, property: "
				+ property);

		Map<Installment, Map<String, BigDecimal>> amts = prepareRsnWiseDemandForOldProp(property);
		for (Installment inst : amts.keySet()) {
			for (String dmdRsn : (amts.get(inst)).keySet()) {
				amts.get(inst).put(dmdRsn, amts.get(inst).get(dmdRsn).negate());
			}
		}
		LOGGER.debug("amts: " + amts + "\n Exiting from prepareRsnWiseDemandForPropToBeDeactivated");
		return amts;
	}

	/**
	 * <p>
	 * Adjusts the excess collection amount to Demand Details
	 * </p>
	 *
	 * Ex:
	 *
	 * if there is excess collection for GEN_TAX then adjustments happens from
	 * beginning installment to current installment if still there is excess
	 * collecion remaining then it will be adjust to the group to which GEN_TAX
	 * belongs.
	 *
	 *
	 * @param installments
	 * @param newDemandDetailsByInstallment
	 */
	public void adjustExcessCollectionAmount(List<Installment> installments,
			Map<Installment, Set<EgDemandDetails>> newDemandDetailsByInstallment, Ptdemand ptDemand) {
		LOGGER.info("Entered into adjustExcessCollectionAmount");
		LOGGER.info("adjustExcessCollectionAmount: installments - " + installments
				+ ", newDemandDetailsByInstallment.size - " + newDemandDetailsByInstallment.size());

		/**
		 * Demand reason groups to adjust the excess collection amount if a
		 * demand reason is collected fully.
		 *
		 * Ex: if GEN_TAX is collected for the installment fully then remaining
		 * excess collection will be adjusted to the group to which GEN_TAX
		 * belongs i.e., demandReasons1[GROUP1]
		 */
		Set<String> demandReasons1 = new LinkedHashSet<String>(Arrays.asList(
				DEMANDRSN_CODE_GENERAL_TAX, DEMANDRSN_CODE_SEWERAGE_TAX,
				DEMANDRSN_CODE_FIRE_SERVICE_TAX, DEMANDRSN_CODE_LIGHTINGTAX,
				DEMANDRSN_CODE_GENERAL_WATER_TAX));

		Set<String> demandReasons2 = new LinkedHashSet<String>(Arrays.asList(
				DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD, DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX,
				DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD));

		Installment currerntInstallment = PropertyTaxUtil.getCurrentInstallment();

		for (Map.Entry<Installment, Map<String, BigDecimal>> excessAmountByDemandReasonForInstallment : excessCollAmtMap
				.entrySet()) {
			LOGGER.debug("adjustExcessCollectionAmount : excessAmountByDemandReasonForInstallment - "
					+ excessAmountByDemandReasonForInstallment);

			for (String demandReason : excessAmountByDemandReasonForInstallment.getValue().keySet()) {

				adjustExcessCollection(installments, newDemandDetailsByInstallment, demandReasons1,
						demandReasons2, excessAmountByDemandReasonForInstallment, demandReason,
						false, null);

				// when the demand details is absent in all the installments /
				// fully collected(in case of current installment demand
				// details) , adjusting to its group
				// and remaining to one of group for current installment
				// if (!isDemandDetailExists) {
				Set<String> reasons = demandReasons1.contains(demandReason) ? new LinkedHashSet<String>(
						demandReasons1) : new LinkedHashSet<String>(demandReasons2);
				reasons.remove(demandReason);
				for (String reason : reasons) {
					adjustExcessCollection(installments, newDemandDetailsByInstallment,
							demandReasons1, demandReasons2,
							excessAmountByDemandReasonForInstallment, reason, true, demandReason);
					if (excessAmountByDemandReasonForInstallment.getValue().get(demandReason)
							.compareTo(BigDecimal.ZERO) == 0) {
						break;
					}
				}
				// }

				if (excessAmountByDemandReasonForInstallment.getValue().get(demandReason)
						.compareTo(BigDecimal.ZERO) > 0) {

					EgDemandDetails currentDemandDetail = getEgDemandDetailsForReason(
							newDemandDetailsByInstallment.get(currerntInstallment),
							NMCPTISConstants.DEMANDRSN_CODE_ADVANCE);

					if (currentDemandDetail == null) {
						LOGGER.info("adjustExcessCollectionAmount - Advance demand details is not present, creating.. ");

						currentDemandDetail = new PropertyTaxCollection().insertAdvanceCollection(
								NMCPTISConstants.DEMANDRSN_CODE_ADVANCE,
								excessAmountByDemandReasonForInstallment.getValue().get(
										demandReason), currerntInstallment);
						ptDemand.addEgDemandDetails(currentDemandDetail);
						newDemandDetailsByInstallment.get(currerntInstallment).add(
								currentDemandDetail);
						// HibernateUtil.getCurrentSession().flush();
					} else {
						currentDemandDetail.setAmtCollected(currentDemandDetail.getAmtCollected()
								.add(excessAmountByDemandReasonForInstallment.getValue().get(
										demandReason)));
						currentDemandDetail.setLastUpdatedTimeStamp(new Date());

					}
				}

				excessAmountByDemandReasonForInstallment.getValue().put(demandReason,
						BigDecimal.ZERO);
			}
		}
		LOGGER.info("Excess collection adjustment is successfully completed..");
		LOGGER.debug("Exiting from adjustExcessCollectionAmount");
	}

	private Boolean adjustExcessCollection(
			List<Installment> installments,
			Map<Installment, Set<EgDemandDetails>> newDemandDetailsByInstallment,
			Set<String> demandReasons1,
			Set<String> demandReasons2,
			Map.Entry<Installment, Map<String, BigDecimal>> excessAmountByDemandReasonForInstallment,
			String demandReason, Boolean isGroupAdjustment, String reasonNotExists) {

		LOGGER.info("Entered into adjustExcessCollection");

		Boolean isDemandDetailExists = Boolean.FALSE;
		BigDecimal balanceDemand = BigDecimal.ZERO;

		for (Installment installment : installments) {
			EgDemandDetails newDemandDetail = getEgDemandDetailsForReason(
					newDemandDetailsByInstallment.get(installment), demandReason);

			if (newDemandDetail == null) {
				isDemandDetailExists = Boolean.FALSE;
			} else {
				isDemandDetailExists = Boolean.TRUE;
				balanceDemand = newDemandDetail.getAmount().subtract(
						newDemandDetail.getAmtCollected());

				if (balanceDemand.compareTo(BigDecimal.ZERO) > 0) {

					BigDecimal excessCollection = isGroupAdjustment ? excessAmountByDemandReasonForInstallment
							.getValue().get(reasonNotExists)
							: excessAmountByDemandReasonForInstallment.getValue().get(demandReason);

					if (excessCollection.compareTo(BigDecimal.ZERO) > 0) {

						if (excessCollection.compareTo(balanceDemand) <= 0) {
							newDemandDetail.setAmtCollected(newDemandDetail.getAmtCollected().add(
									excessCollection));
							newDemandDetail.setLastUpdatedTimeStamp(new Date());
							excessCollection = BigDecimal.ZERO;
						} else {
							newDemandDetail.setAmtCollected(newDemandDetail.getAmtCollected().add(
									balanceDemand));
							newDemandDetail.setLastUpdatedTimeStamp(new Date());
							BigDecimal remainingExcessCollection = excessCollection
									.subtract(balanceDemand);

							while (remainingExcessCollection.compareTo(BigDecimal.ZERO) > 0) {

								/**
								 * adjust to next installments in asc order for
								 * the reason demandReason
								 */

								Set<String> oneReason = new LinkedHashSet<String>();
								oneReason.add(demandReason);
								remainingExcessCollection = adjustToInstallmentDemandDetails(
										installments, newDemandDetailsByInstallment,
										excessAmountByDemandReasonForInstallment,
										remainingExcessCollection, oneReason);

								if (remainingExcessCollection.compareTo(BigDecimal.ZERO) == 0) {
									excessCollection = BigDecimal.ZERO;
								}

								if (remainingExcessCollection.compareTo(BigDecimal.ZERO) > 0) {
									Set<String> reasons = demandReasons1.contains(demandReason) ? new LinkedHashSet<String>(
											demandReasons1) : new LinkedHashSet<String>(
											demandReasons2);
									reasons.remove(demandReason);

									remainingExcessCollection = adjustToInstallmentDemandDetails(
											installments, newDemandDetailsByInstallment,
											excessAmountByDemandReasonForInstallment,
											remainingExcessCollection, reasons);
								}

								/**
								 * There is still remainingExcessCollection
								 * after adjusting to demandReason[Installment1]
								 * demandReason[Installment2] . . . . . . . . .
								 * . . . . . demandReason[CurrentInstallment]
								 *
								 * So, adjusting the remaining excess collection
								 * to demandReason[currentInstallment]
								 */
								if (remainingExcessCollection.compareTo(BigDecimal.ZERO) > 0) {
									EgDemandDetails currentDemandDetail = getEgDemandDetailsForReason(
											newDemandDetailsByInstallment.get(PropertyTaxUtil
													.getCurrentInstallment()), demandReason);
									/**
									 * if the demand reason does not exist in
									 * the current installment then adjusting
									 * the remaining excess collection to its
									 * group
									 */
									if (currentDemandDetail == null) {
										Set<String> reasons = demandReasons1.contains(demandReason) ? new LinkedHashSet<String>(
												demandReasons1) : new LinkedHashSet<String>(
												demandReasons2);
										reasons.remove(demandReason);
										for (String rsn : reasons) {
											currentDemandDetail = getEgDemandDetailsForReason(
													newDemandDetailsByInstallment.get(PropertyTaxUtil
															.getCurrentInstallment()), rsn);
											if (currentDemandDetail != null) {
												break;
											}
										}
									}

									currentDemandDetail.setAmtCollected(currentDemandDetail
											.getAmtCollected().add(remainingExcessCollection));
									currentDemandDetail.setLastUpdatedTimeStamp(new Date());
									remainingExcessCollection = BigDecimal.ZERO;
									excessCollection = BigDecimal.ZERO;

								}

								if (remainingExcessCollection.compareTo(BigDecimal.ZERO) == 0) {
									excessCollection = BigDecimal.ZERO;
								}
							}
						}

						if (excessCollection.compareTo(BigDecimal.ZERO) == 0) {
							String rsn = isGroupAdjustment ? reasonNotExists : demandReason;
							excessAmountByDemandReasonForInstallment.getValue().put(rsn, ZERO);
						}
					}
				}
				String rsn = isGroupAdjustment ? reasonNotExists : demandReason;
				if (excessAmountByDemandReasonForInstallment.getValue().get(rsn)
						.compareTo(BigDecimal.ZERO) == 0) {
					break;
				}
			}
		}

		LOGGER.info("Exiting from adjustExcessCollection");

		return isDemandDetailExists;
	}

	private BigDecimal adjustToInstallmentDemandDetails(
			List<Installment> installments,
			Map<Installment, Set<EgDemandDetails>> newDemandDetailsByInstallment,
			Map.Entry<Installment, Map<String, BigDecimal>> excessAmountByDemandReasonForInstallment,
			BigDecimal remainingExcessCollection, Set<String> reasons) {
		LOGGER.debug("Entered into adjustToInstallmentDemandDetails");
		LOGGER.debug("adjustToInstallmentDemandDetails : reasons=" + reasons
				+ ", remainingExcessCollection=" + remainingExcessCollection);
		for (String reason : reasons) {
			for (Installment nextInstallment : installments) {
				EgDemandDetails nextNewDemandDetail = getEgDemandDetailsForReason(
						newDemandDetailsByInstallment.get(nextInstallment), reason);

				if (nextNewDemandDetail != null) {
					BigDecimal balance = nextNewDemandDetail.getAmount().subtract(
							nextNewDemandDetail.getAmtCollected());

					if (balance.compareTo(BigDecimal.ZERO) > 0) {
						if (remainingExcessCollection.compareTo(balance) <= 0) {
							nextNewDemandDetail.setAmtCollected(nextNewDemandDetail
									.getAmtCollected().add(remainingExcessCollection));
							nextNewDemandDetail.setLastUpdatedTimeStamp(new Date());
							remainingExcessCollection = BigDecimal.ZERO;
						} else {
							nextNewDemandDetail.setAmtCollected(nextNewDemandDetail
									.getAmtCollected().add(balance));
							nextNewDemandDetail.setLastUpdatedTimeStamp(new Date());
							remainingExcessCollection = remainingExcessCollection.subtract(balance);
						}
					}

					if (remainingExcessCollection.compareTo(BigDecimal.ZERO) == 0) {
						break;
					}
				}
			}
		}
		LOGGER.debug("adjustToInstallmentDemandDetails : remainingExcessCollection="
				+ remainingExcessCollection);
		LOGGER.debug("Exiting from adjustToInstallmentDemandDetails");
		return remainingExcessCollection;
	}

	public void initiateDataEntryWorkflow(BasicProperty basicProperty, User initiater) {
		LOGGER.debug("Entered into initiateDataEntryWorkflow");

		PropertyImpl oldProperty = (PropertyImpl) basicProperty.getProperty();
		PropertyImpl newProperty = (PropertyImpl) oldProperty.createPropertyclone();

		// Setting the property state to the objection workflow initiator
		Position owner = eisCommonsService.getPositionByUserId(Integer.valueOf(initiater.getId()
				.toString()));
		String desigName = propertyTaxUtil.getDesignationName(initiater.getId());
		String value = WFLOW_ACTION_NAME_MODIFY + ":" + desigName + "_" + WF_STATE_APPROVAL_PENDING;

		newProperty.transition(true).start().withSenderName(initiater.getName())
				.withComments(PROPERTY_WORKFLOW_STARTED).withStateValue(value).withOwner(owner)
				.withDateInfo(new Date());

		PropertyMutationMaster propMutMstr = (PropertyMutationMaster) getPropPerServ().find(
				"from PropertyMutationMaster PM where upper(PM.code) = ?",
				PROPERTY_MODIFY_REASON_DATA_ENTRY);
		newProperty.getPropertyDetail().setPropertyMutationMaster(propMutMstr);
		newProperty.setStatus(PropertyTaxConstants.STATUS_WORKFLOW);
		basicProperty.addProperty(newProperty);

		basicProperty.addPropertyStatusValues(createPropStatVal(basicProperty,
				PROPERTY_MODIFY_REASON_MODIFY,
				getPropertyCompletionDate(basicProperty, newProperty), null, null, null, null));

		if (!newProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
				.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			createAttributeValues(newProperty, null);
		}

		basicProperty = basicPrpertyService.update(basicProperty);
		LOGGER.debug("Exiting from initiateDataEntryWorkflow");

	}

	public Map<Installment, Map<String, BigDecimal>> getExcessCollAmtMap() {
		return excessCollAmtMap;
	}

	public void setExcessCollAmtMap(Map<Installment, Map<String, BigDecimal>> excessCollAmtMap) {
		this.excessCollAmtMap = excessCollAmtMap;
	}

	public void setPtNumberGenerator(PropertyTaxNumberGenerator ptNumberGenerator) {
		this.ptNumberGenerator = ptNumberGenerator;
	}
}
