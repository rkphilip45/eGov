package org.egov.ptis.domain.service.property;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ALV;
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
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GWR_IMPOSED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OPEN_PLOT_UNIT_FLOORNUMBER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_IS_DEFAULT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_AMALG;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_OBJ;
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
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMAND_RSNS_LIST;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE127;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.DepreciationMaster;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.flexfields.dao.EgAttributetypeDao;
import org.egov.infstr.flexfields.dao.EgAttributevaluesDao;
import org.egov.infstr.flexfields.dao.FlexfieldsDaoFactory;
import org.egov.infstr.flexfields.model.EgAttributetype;
import org.egov.infstr.flexfields.model.EgAttributevalues;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.address.model.Address;
import org.egov.lib.citizen.model.Owner;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
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
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.MiscellaneousTaxDetail;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.service.TaxCalculator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

public class PropertyService extends PersistenceService<PropertyImpl, Long> {
	private static final Logger LOGGER = Logger.getLogger(PropertyService.class);

	private PersistenceService propPerServ;
	private Installment currentInstall;
	private TaxCalculator taxCalculator;
	private HashMap<Installment, TaxCalculationInfo> instTaxMap;
	private PropertyTaxUtil propertyTaxUtil;
	protected EisCommonsService eisCommonsService;
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	protected PersistenceService<BasicProperty, Long> basicPrpertyService;
	private Map<Installment, Set<EgDemandDetails>> demandDetails = new HashMap<Installment, Set<EgDemandDetails>>();
	private Map<Installment,Map<String,BigDecimal>> excessCollAmtMap = new HashMap<Installment,Map<String,BigDecimal>>();
	
	public PropertyImpl createProperty(PropertyImpl property, String areaOfPlot, String mutationCode,
			String propTypeId, String propUsageId, String propOccId, Character status, String docnumber, String nonResPlotArea,
			boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createProperty");
		LOGGER.debug("createProperty: Property: " + property + ", areaOfPlot: " + areaOfPlot + ", mutationCode: "
				+ mutationCode + ",propTypeId: " + propTypeId + ", propUsageId: " + propUsageId + ", propOccId: "
				+ propOccId + ", status: " + status);
		currentInstall = (Installment) getPropPerServ().find(
				"from Installment I where I.module.moduleName=? and (I.fromDate <= ? and I.toDate >= ?) ",
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
			if (!(propTypeMstr.getCode().equals(PROPTYPE_NON_RESD) || propTypeMstr.getCode().equals(PROPTYPE_RESD) || propTypeMstr
					.getCode().equals(PROPTYPE_OPEN_PLOT))) {
				property.getPropertyDetail().setExtra_field5(null);
			}
		}
		if (propUsageId != null) {
			PropertyUsage usage = (PropertyUsage) getPropPerServ().find("from PropertyUsage pu where pu.id = ?",
					Long.valueOf(propUsageId));
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
		createFloors(property, mutationCode, propUsageId, propOccId,isfloorDetailsRequired);
		property.setStatus(status);
		property.setIsDefaultProperty(PROPERTY_IS_DEFAULT);
		property.setInstallment(currentInstall);
		property.setEffectiveDate(currentInstall.getFromDate());
		property.setPropertySource(propertySource);
		property.setDocNumber(docnumber);
		LOGGER.debug("Exiting from createProperty");
		return property;
	}

	public void createFloors(Property property, String mutationCode, String propUsageId, String propOccId,boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createFloors");
		LOGGER.debug("createFloors: Property: " + property + ", mutationCode: " + mutationCode + ", propUsageId: "
				+ propUsageId + ", propOccId: " + propOccId);
		
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if (((property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property
					.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) && !isfloorDetailsRequired)
					|| !(property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property.getPropertyDetail()
							.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))) {
			if (!mutationCode.equals("NEW") || STATUS_WORKFLOW.equals(property.getStatus())) {
				property.getPropertyDetail().setFloorDetails(new HashSet<FloorIF>());
			}
			for (FloorIF floor : property.getPropertyDetail().getFloorDetailsProxy()) {
				if (floor != null) {
					floor.setCreatedTimeStamp(new Date());
					
					if ("-1".equals(floor.getExtraField7())) {
						floor.setExtraField7(null);
					}
					
					PropertyTypeMaster unitType = null;
					PropertyUsage usage = null;
					PropertyOccupation occupancy = null;
					if (floor.getUnitType() != null) {
						unitType = (PropertyTypeMaster) getPropPerServ().find(
								"from PropertyTypeMaster utype where utype.id = ?", floor.getUnitType().getId());
					}
					if (floor.getPropertyUsage() != null) {
						usage = (PropertyUsage) getPropPerServ().find(
								"from PropertyUsage pu where pu.id = ?", floor.getPropertyUsage().getId());
					}
					if(floor.getPropertyOccupation()!=null){
						occupancy = (PropertyOccupation) getPropPerServ().find(
							"from PropertyOccupation po where po.id = ?", floor.getPropertyOccupation().getId());
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
								"from DepreciationMaster dm where dm.id=?", floor.getDepreciationMaster().getId());
					}
										 
					floor.setDepreciationMaster(depMaster);
					 
					LOGGER.debug("createFloors: PropertyUsage: " + usage + ", PropertyOccupation: " + occupancy
							+ ", StructureClass: " + structureClass);
					
					if (unitType != null && unitType.getCode().equalsIgnoreCase(NMCPTISConstants.UNITTYPE_OPEN_PLOT)) {
						floor.setFloorNo(OPEN_PLOT_UNIT_FLOORNUMBER);
					}
					
					floor.setUnitType(unitType);
					floor.setPropertyUsage(usage);
					floor.setPropertyOccupation(occupancy);
					floor.setStructureClassification(structureClass);
					property.getPropertyDetail().addFloor(floor);
				}
			}
			}else{
				//-added for state govt property without floors
				if (property.getPropertyDetail().getFloorDetailsProxy().size() > 0
						&& property.getPropertyDetail().getFloorDetails().size() > 0) {
					property.getPropertyDetail().setFloorDetailsProxy(Collections.EMPTY_LIST);
					property.getPropertyDetail().setFloorDetails(Collections.EMPTY_SET);
				}
				PropertyOccupation occupancy = (PropertyOccupation) getPropPerServ().find(
						"from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
				PropertyUsage usage = (PropertyUsage) getPropPerServ().find("from PropertyUsage pu where pu.id = ?",
						Long.valueOf(propUsageId));
				LOGGER.debug("createFloors: PropertyUsage: " + usage + ", PropertyOccupation: " + occupancy);
				property.getPropertyDetail().setPropertyOccupation(occupancy);
				property.getPropertyDetail().setPropertyUsage(usage);
			}
			
		} else {
			if (property.getPropertyDetail().getFloorDetailsProxy().size() > 0
					&& property.getPropertyDetail().getFloorDetails().size() > 0) {
				property.getPropertyDetail().setFloorDetailsProxy(Collections.EMPTY_LIST);
				property.getPropertyDetail().setFloorDetails(Collections.EMPTY_SET);
			}
			PropertyOccupation occupancy = (PropertyOccupation) getPropPerServ().find(
					"from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
			PropertyUsage usage = (PropertyUsage) getPropPerServ().find("from PropertyUsage pu where pu.id = ?",
					Long.valueOf(propUsageId));
			LOGGER.debug("createFloors: PropertyUsage: " + usage + ", PropertyOccupation: " + occupancy);
			property.getPropertyDetail().setPropertyOccupation(occupancy);
			property.getPropertyDetail().setPropertyUsage(usage);
		}
		LOGGER.debug("Exiting from createFloors");
	}

	public PropertyStatusValues createPropStatVal(BasicProperty basicProperty, String statusCode,
			Date propCompletionDate, String courtOrdNum, Date orderDate, String judgmtDetails, String parentPropId) {
		LOGGER.debug("Entered into createPropStatVal");
		LOGGER.debug("createPropStatVal: basicProperty: " + basicProperty + ", statusCode: " + statusCode
				+ ", propCompletionDate: " + propCompletionDate + ", courtOrdNum: " + courtOrdNum + ", orderDate: "
				+ orderDate + ", judgmtDetails: " + judgmtDetails + ", parentPropId: " + parentPropId);
		PropertyStatusValues propStatVal = new PropertyStatusValues();
		PropertyStatus propertyStatus = (PropertyStatus) getPropPerServ().find(
				"from PropertyStatus where statusCode=?", statusCode);
		if (PROPERTY_MODIFY_REASON_MODIFY.equals(statusCode) || PROPERTY_MODIFY_REASON_AMALG.equals(statusCode)
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
				&& basicProperty.getPropertyMutationMaster().getCode().equals(PROP_CREATE_RSN_BIFUR)) {
			BasicProperty referenceBasicProperty = (BasicProperty) propPerServ.find(
					"from BasicPropertyImpl bp where bp.upicNo=?", parentPropId);
			propStatVal.setReferenceBasicProperty(referenceBasicProperty);
		}
		LOGGER.debug("createPropStatVal: PropertyStatusValues: " + propStatVal);
		LOGGER.debug("Exiting from createPropStatVal");
		return propStatVal;
	}

	public Property createDemand(Property property, Date dateOfCompletion, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createDemand");
		LOGGER.debug("createDemand: Property: " + property + ", dateOfCompletion: " + dateOfCompletion);
		List<String> applicableTaxes = prepareApplTaxes(property);
		LOGGER.debug("createDemand: applicableTaxes: " + applicableTaxes);
		
		instTaxMap = taxCalculator.calculatePropertyTax(property, applicableTaxes, dateOfCompletion);		

		Ptdemand ptDemand;
		Set<Ptdemand> ptDmdSet = new HashSet<Ptdemand>();
		Set<EgDemandDetails> dmdDetailSet;
		List<Installment> instList = new ArrayList<Installment>();
		instList = new ArrayList<Installment>(instTaxMap.keySet());
		LOGGER.debug("createDemand: instList: " + instList);
		currentInstall = propertyTaxUtil.getCurrentInstallment();
		
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
			
			//In case of Property Type as (Open Plot,State Govt,Central Govt), set the alv to PTDemandCalculations
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT) 
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) 
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				ptDmdCalc.setAlv(taxCalcInfo.getTotalAnnualLettingValue());
			}
			else if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT) 
					&& !property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) 
					&& !property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				if (installment.equals(currentInstall)) {
					//FloorwiseDemandCalculations should be set only for the current installment for each floor.
					for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
						ptDmdCalc.addFlrwiseDmdCalculations(createFloorDmdCalc(ptDmdCalc, floor, taxCalcInfo));
					}
				}
			}
		}
		property.setPtDemandSet(ptDmdSet);
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
	public Property createDemandForModify(Property oldProperty, Property newProperty, Date dateOfCompletion) {
		LOGGER.debug("Entered into createDemandForModify");
		LOGGER.debug("createDemandForModify: oldProperty: " + oldProperty + ", newProperty: " + newProperty
				+ ", dateOfCompletion: " + dateOfCompletion);
		
		List<Installment> instList = new ArrayList<Installment>();
		instList = new ArrayList<Installment>(instTaxMap.keySet());
		LOGGER.debug("createDemandForModify: instList: " + instList);
		Ptdemand ptDemandOld = new Ptdemand();
		Ptdemand ptDemandNew = new Ptdemand();
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		Installment currentInstall = instalDao.getInsatllmentByModuleForGivenDate(module, new Date());
		Map<String, Ptdemand> oldPtdemandMap = getPtdemandsAsInstMap(oldProperty.getPtDemandSet());
		ptDemandOld = (Ptdemand) oldPtdemandMap.get(currentInstall.getDescription());
		PropertyTypeMaster oldPropTypeMaster = oldProperty.getPropertyDetail().getPropertyTypeMaster();
		PropertyTypeMaster newPropTypeMaster = newProperty.getPropertyDetail().getPropertyTypeMaster();
		Set<EgDemandDetails> newEgDmdDtlsSet = new HashSet<EgDemandDetails>();
		
		if (!oldProperty.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(
				newProperty.getPropertyDetail().getPropertyTypeMaster().getCode())
				|| !oldProperty.getPropertyDetail().getExtra_field1().equals(
						newProperty.getPropertyDetail().getExtra_field1())
				|| (isBigResidentialType(oldProperty.getPtDemandSet()) ^ isBigResidentialType(newProperty
						.getPtDemandSet()))
				|| oldProperty.getTaxExemptReason() == null
				^ newProperty.getTaxExemptReason() == null) {
			LOGGER.info("Old Property and New Property are different, Going for adjustments");

			LOGGER.info("newProperty:" + prepareRsnWiseDemandForOldProp(newProperty));

			LOGGER.info("------------------------Start OldProperty demand set ---------------------------");
			for (Ptdemand ptd : oldProperty.getPtDemandSet()) {
				LOGGER.info(ptd.getEgInstallmentMaster().getDescription() + " : " + ptd.getEgDemandDetails());
			}
			LOGGER.info("------------------------End OldProperty demand set ---------------------------");
			LOGGER.info("------------------------Start New Property demand set ---------------------------");
			for (Ptdemand ptd : newProperty.getPtDemandSet()) {
				LOGGER.info(ptd.getEgInstallmentMaster().getDescription() + " : " + ptd.getEgDemandDetails());
			}
			LOGGER.info("------------------------End New Property demand set ---------------------------");

			for (Installment installment : instList) {
				createAllDmdDeatails(oldProperty, newProperty, installment, instList, instTaxMap);
			}

			LOGGER
					.info("----------------------------------------------- Adjustments --------------------------------------------------");
			for (Ptdemand ptd : newProperty.getPtDemandSet()) {
				LOGGER.info(ptd.getEgInstallmentMaster().getDescription() + " : " + ptd.getEgDemandDetails());
			}
			LOGGER
					.info("--------------------------------------------------------------------------------------------------------------");
		}
		
		Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());
		ptDemandNew = (Ptdemand) newPtdemandMap.get(currentInstall.getDescription());
		Map<Installment, Set<EgDemandDetails>> newDemandDtlsMap = getEgDemandDetailsSetAsMap(new ArrayList(ptDemandNew
				.getEgDemandDetails()), instList);
		List<EgDemandDetails> penaltyDmdDtlsList = null;
		
		for (Installment inst : instList) {
			
			newEgDmdDtlsSet = carryForwardCollection(newProperty, inst, newDemandDtlsMap.get(inst), ptDemandOld, 
					oldPropTypeMaster, newPropTypeMaster);
			
			if(inst.equals(currentInstall)) {
				//carry forward the penalty from the old property to the new property
				penaltyDmdDtlsList = getEgDemandDetailsListForReason(ptDemandOld.getEgDemandDetails(), 
						DEMANDRSN_CODE_PENALTY_FINES);
				if (penaltyDmdDtlsList != null && penaltyDmdDtlsList.size() > 0) {
					ptDemandNew.getEgDemandDetails().addAll(penaltyDmdDtlsList);
				}
			}
		}
	
		// sort the installment list in ascending order to start the excessColl adjustment from 1st inst
		Collections.sort(instList);   
		for (Installment inst : instList) {
			adjustExcessCollAmt(instList,newDemandDtlsMap.get(inst),newDemandDtlsMap);
		}

		LOGGER.debug("Exiting from createDemandForModify");
		return newProperty;
	}

	private List<String> prepareApplTaxes(Property property) {
		LOGGER.debug("Entered into prepareApplTaxes");
		LOGGER.debug("prepareApplTaxes: property: " + property);
		List<String> applicableTaxes = new ArrayList<String>();
		String propType = property.getPropertyDetail().getPropertyTypeMaster().getCode();
		String waterRate = property.getPropertyDetail().getExtra_field1();
		String propTypeCategory = property.getPropertyDetail().getExtra_field5();
		LOGGER.debug("prepareApplTaxes: propType: " + propType + ", waterRate: " + waterRate);
		applicableTaxes.add(DEMANDRSN_CODE_FIRE_SERVICE_TAX);
		applicableTaxes.add(DEMANDRSN_CODE_SEWERAGE_TAX);
		applicableTaxes.add(DEMANDRSN_CODE_LIGHTINGTAX);
		
		if (property.getIsExemptedFromTax() != null) {
			return applicableTaxes;
		}
		
		applicableTaxes.add(DEMANDRSN_CODE_GENERAL_TAX);
		
		if (propType.equalsIgnoreCase(PROPTYPE_NON_RESD)) {
			applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
			applicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
		}
		
		if (propType.equalsIgnoreCase(PROPTYPE_OPEN_PLOT) || propType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
				|| propType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
			if (waterRate.equals(GWR_IMPOSED)) {
				applicableTaxes.add(DEMANDRSN_CODE_GENERAL_WATER_TAX);
			}
		}
		
		if (propType.equalsIgnoreCase(PROPTYPE_RESD)) {
			applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		}			
		
		if (propType.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if (NMCPTISConstants.PROPTYPE_CAT_RESD.equalsIgnoreCase(propTypeCategory)) {
				applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			} else if (NMCPTISConstants.PROPTYPE_CAT_NON_RESD.equalsIgnoreCase(propTypeCategory)) {
				applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				applicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			}
		}
				
		LOGGER.debug("prepareApplTaxes: applicableTaxes: " + applicableTaxes);
		LOGGER.debug("Exiting from prepareApplTaxes");
		return applicableTaxes;
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

	private Set<EgDemandDetails> createAllDmdDeatails(Installment installment, List<Installment> instList,
			HashMap<Installment, TaxCalculationInfo> instTaxMap) {
		LOGGER.debug("Entered into createAllDmdDeatails");
		/*LOGGER.debug("createAllDmdDeatails: installment: " + installment + ", instList: " + instList + ", instTaxMap: "
				+ instTaxMap);*/
		Set<EgDemandDetails> dmdDetSet = new HashSet<EgDemandDetails>();
		for (Installment inst : instList) {
			if (inst.getFromDate().before(installment.getFromDate())
					|| inst.getFromDate().equals(installment.getFromDate())) {
				TaxCalculationInfo taxCalcInfo = instTaxMap.get(inst);
				
				Map<String, BigDecimal> taxMap = taxCalculator.getMiscTaxesForProp(taxCalcInfo
						.getConsolidatedUnitTaxCalculationInfo());
				
				for (Map.Entry<String, BigDecimal> tax : taxMap.entrySet()) {
					EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(tax.getKey(), inst);
					dmdDetSet.add(createDemandDetails(tax.getValue(), egDmdRsn, inst));
				}
			}
		}
		LOGGER.debug("createAllDmdDeatails: dmdDetSet: " + dmdDetSet);
		return dmdDetSet;
	}

	@SuppressWarnings("unchecked")
	private void createAllDmdDeatails(Property oldProperty, Property newProperty, Installment installment,
			List<Installment> instList, HashMap<Installment, TaxCalculationInfo> instTaxMap) {
		LOGGER.debug("Entered into createAllDmdDeatails");
		LOGGER.debug("createAllDmdDeatails: oldProperty: " + oldProperty + ", newProperty: " + newProperty
				+ ",installment: " + installment + ", instList: " + instList);
		Set<EgDemandDetails> adjustedDmdDetailsSet = new HashSet<EgDemandDetails>();
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		Installment currentInstall = instalDao.getInsatllmentByModuleForGivenDate(module, new Date());

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

		ptDemandOld = (Ptdemand) oldPtdemandMap.get(currentInstall.getDescription());
		ptDemandNew = (Ptdemand) newPtdemandMap.get(installment.getDescription());

		LOGGER.info("instList==========" + instList);

		Map<Installment, Set<EgDemandDetails>> oldDemandDtlsMap = getEgDemandDetailsSetAsMap(new ArrayList(ptDemandOld
				.getEgDemandDetails()), instList);
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

					/*
					 * Getting EgDemandDetails for inst installment
					 */
					for (EgDemandDetails edd : ptDemandNew.getEgDemandDetails()) {
						if (edd.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
							newEgDemandDetailsSet.add((EgDemandDetails) edd.clone());
						}
					}

					PropertyTypeMaster newPropTypeMaster = newProperty.getPropertyDetail().getPropertyTypeMaster();

					LOGGER.info("Old Demand Set:" + inst + "=" + oldEgDemandDetailsSet);
					LOGGER.info("New Demand set:" + inst + "=" + newEgDemandDetailsSet);

					if (oldProperty.getTaxExemptReason() == null && newProperty.getTaxExemptReason() == null) {
						for (int i = 0; i < adjstmntReasons.size(); i++) {
							String oldPropRsn = adjstmntReasons.get(i);
							String newPropRsn = null;

							/*
							 * Gives EgDemandDetails from newEgDemandDetailsSet
							 * for demand reason oldPropRsn, if we dont have
							 * EgDemandDetails then doing collection adjustments
							 */
							newEgDmndDetails = getEgDemandDetailsForReason(newEgDemandDetailsSet, oldPropRsn);

							if (newEgDmndDetails == null) {
								if (newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
									newPropRsn = rsnsForNewResProp.get(i);
								} else if (newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
									newPropRsn = rsnsForNewNonResProp.get(i);
								}

								oldEgdmndDetails = getEgDemandDetailsForReason(oldEgDemandDetailsSet, oldPropRsn);
								newEgDmndDetails = getEgDemandDetailsForReason(newEgDemandDetailsSet, newPropRsn);

								if (newEgDmndDetails != null && oldEgdmndDetails != null) {
									newEgDmndDetails.setAmtCollected(newEgDmndDetails.getAmtCollected().add(
											oldEgdmndDetails.getAmtCollected()));
								} else {
									continue;
								}
							}
						}
					} else {
						if (oldProperty.getTaxExemptReason() == null) {
							newEgDemandDetailsSet = adjustmentsForTaxExempted(ptDemandOld.getEgDemandDetails(),
									newEgDemandDetailsSet, inst);
						}
					}

					// Collection carry forward logic (This logic is moved out of this method, bcoz it has to be invoked in all usecases
					// and not only when there is property type change
					
					/*newEgDemandDetailsSet = carryForwardCollection(newProperty, inst, newEgDemandDetailsSet,
							ptDemandOld, oldPropTypeMaster, newPropTypeMaster);*/
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
				+ oldProperty.getBasicProperty().getUpicNo() + " And installment : " + installment + "\n\n"
				+ adjustedDmdDetailsSet);
		ptDemandNew.setEgDemandDetails(adjustedDmdDetailsSet);
		LOGGER.debug("Exiting from createAllDmdDeatails");
	}

	private Set<EgDemandDetails> carryForwardCollection(Property newProperty, Installment inst,
			Set<EgDemandDetails> newEgDemandDetailsSet, Ptdemand ptDmndOld, PropertyTypeMaster oldPropTypeMaster,
			PropertyTypeMaster newPropTypeMaster) {
		LOGGER.debug("Entered into carryForwardCollection");
		LOGGER.debug("carryForwardCollection: newProperty: " + newProperty + ", inst: " + inst
				+ ", newEgDemandDetailsSet: " + newEgDemandDetailsSet + ", ptDmndOld: " + ptDmndOld
				+ ", oldPropTypeMaster: " + oldPropTypeMaster + ", newPropTypeMaster: " + newPropTypeMaster);
		
		Map<String,BigDecimal> dmdRsnAmt = new HashMap<String, BigDecimal>();
		
		for (String rsn : DEMAND_RSNS_LIST) {

			List<EgDemandDetails> oldEgDmndDtlsList = null;
			List<EgDemandDetails> newEgDmndDtlsList = null;
			
			if (newProperty.getTaxExemptReason() != null && !newProperty.getTaxExemptReason().equals("-1")) {
				if (!rsn.equalsIgnoreCase(DEMANDRSN_CODE_SEWERAGE_TAX)
						&& !rsn.equalsIgnoreCase(DEMANDRSN_CODE_LIGHTINGTAX)
						&& !rsn.equalsIgnoreCase(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {

					continue;
				}
			}

			if (rsn.equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
				if (oldPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)
						&& newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(),
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				} else if (oldPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)
						&& newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(),
							DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				} else if (oldPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)
						&& newPropTypeMaster.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(),
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				} else {
					oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(),
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
					newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet,
							DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				}
			} else {

				oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(), rsn);
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

			if (newDmndDtls != null && oldDmndDtls != null) {
				newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(oldDmndDtls.getAmtCollected()));
				newDmndDtls.setAmtRebate(newDmndDtls.getAmtRebate().add(oldDmndDtls.getAmtRebate()));
			} else if (newDmndDtls != null && oldDmndDtls == null) {
				newDmndDtls.setAmtCollected(ZERO);
				newDmndDtls.setAmtRebate(ZERO);
			}
			
			if(newDmndDtls != null) {
				//This part of code handles the adjustment of extra collections when there is decrease in tax during property modification.
				
				BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
				//If there is extraColl then add to map
				if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
					dmdRsnAmt.put(rsn, extraCollAmt);
					newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
				}
			}
			
		}
		excessCollAmtMap.put(inst, dmdRsnAmt);
		
		demandDetails.put(inst, newEgDemandDetailsSet);
		
		LOGGER.debug("carryForwardCollection: newEgDemandDetailsSet: " + newEgDemandDetailsSet);
		LOGGER.debug("Exiting from carryForwardCollection");
		return newEgDemandDetailsSet;
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
	private Map<Installment, EgDemandDetails> getEgDemandDetailsAsMap(List<EgDemandDetails> demandDetailsList) {
		LOGGER.debug("Entered into getEgDemandDetailsAsMap, demandDetailsList: " + demandDetailsList);
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
	private Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetAsMap(List<EgDemandDetails> demandDetailsList,
			List<Installment> instList) {
		LOGGER.debug("Entered into getEgDemandDetailsSetAsMap, demandDetailsList: " + demandDetailsList
				+ ", instList: " + instList);
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
	private EgDemandDetails getEgDemandDetailsForReason(Set<EgDemandDetails> egDemandDetailsSet, String demandReason) {
		LOGGER.debug("Entered into getEgDemandDetailsForReason, egDemandDetailsSet: " + egDemandDetailsSet
				+ ", demandReason: " + demandReason);
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
	private List<EgDemandDetails> getEgDemandDetailsListForReason(Set<EgDemandDetails> egDemandDetailsSet,
			String demandReason) {
		LOGGER.debug("Entered into getEgDemandDetailsListForReason: egDemandDetailsSet: " + egDemandDetailsSet
				+ ", demandReason: " + demandReason);
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
	private List<Map<String, EgDemandDetails>> getEgDemandDetailsAsMap(Set<EgDemandDetails> egDemandDetailsSet) {
		LOGGER.debug("Entered into getEgDemandDetailsAsMap, egDemandDetailsSet: " + egDemandDetailsSet);
		List<EgDemandDetails> egDemandDetailsList = new ArrayList<EgDemandDetails>(egDemandDetailsSet);
		List<Map<String, EgDemandDetails>> egDemandDetailsListOfMap = new ArrayList<Map<String, EgDemandDetails>>();

		for (EgDemandDetails egDmndDtls : egDemandDetailsList) {
			Map<String, EgDemandDetails> egDemandDetailsMap = new HashMap<String, EgDemandDetails>();
			EgDemandReasonMaster dmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();
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
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
				egDemandDetailsMap.put(DEMANDRSN_CODE_PENALTY_FINES, egDmndDtls);
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
		LOGGER.debug("Entered into adjustmentsForTaxExempted, oldEgDemandDetails: " + oldEgDemandDetails
				+ ", newEgDemandDetails: " + newEgDemandDetails + ", inst:" + inst);
		BigDecimal totalDmndAdjstmntAmnt = BigDecimal.ZERO;
		BigDecimal totalCollAdjstmntAmnt = BigDecimal.ZERO;

		for (EgDemandDetails egDmndDtls : oldEgDemandDetails) {
			if (egDmndDtls.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
				EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();
				if (!egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_SEWERAGE_TAX)
						&& !egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIGHTINGTAX)
						&& !egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {
					// totalDmndAdjstmntAmnt =
					// totalDmndAdjstmntAmnt.add(egDmndDtls.getAmount().subtract(
					// egDmndDtls.getAmtCollected()));
					totalCollAdjstmntAmnt = totalCollAdjstmntAmnt.add(egDmndDtls.getAmtCollected());
				}
			}
		}

		List<EgDemandDetails> newEgDmndDetails = new ArrayList<EgDemandDetails>(newEgDemandDetails);

		for (EgDemandDetails egDmndDtls : newEgDemandDetails) {

			EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();

			if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_SEWERAGE_TAX)) {

				egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.50")));

			} else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIGHTINGTAX)) {

				egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));

			} else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {

				egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));
			}
		}
		LOGGER.debug("newEgDmndDetails: " + newEgDmndDetails + "\nExiting from adjustmentsForTaxExempted");
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
				if (egDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(
						DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
					isBigResi = TRUE;
					break;
				}
			}
		}
		LOGGER.debug("isBigResi: " + isBigResi + "\n Exiting from isBigResidentialType");
		return isBigResi;
	}

	private EgDemandDetails createDemandDetails(BigDecimal amount, EgDemandReason dmdRsn, Installment inst) {
		LOGGER.debug("Entered into createDemandDetails, amount: " + amount + ", dmdRsn: " + dmdRsn + ", inst: " + inst);
		EgDemandDetails demandDetail = new EgDemandDetails();
		demandDetail.setAmount(amount);
		demandDetail.setAmtCollected(BigDecimal.ZERO);
		demandDetail.setAmtRebate(BigDecimal.ZERO);
		demandDetail.setEgDemandReason(dmdRsn);
		demandDetail.setCreateTimestamp(new Date());
		LOGGER.debug("demandDetail: " + demandDetail + "\nExiting from createDemandDetails");
		return demandDetail;
	}

	private FloorwiseDemandCalculations createFloorDmdCalc(PTDemandCalculations ptDmdCal, FloorIF floor,
			TaxCalculationInfo taxCalcInfo) {
		// LOGGER.debug("Entered into createFloorDmdCalc, ptDmdCal: " + ptDmdCal
		// + ", floor: " + floor + ", taxCalcInfo: " + taxCalcInfo);
		FloorwiseDemandCalculations floorDmdCalc = new FloorwiseDemandCalculations();
		floorDmdCalc.setPTDemandCalculations(ptDmdCal);
		floorDmdCalc.setFloor(floor);
		try {
			for (UnitTaxCalculationInfo unitTax : taxCalcInfo.getUnitTaxCalculationInfo()) {
				/**
				 * This condition is applied because floor number is not
				 * mandatory in case of Mixed Property.(for UnitType = OPEN_PLOT). 
				 * So UnitType(mandatory only in case of Mixed Property) is considered.
				 * The main purpose of this condition is to check each unitTax corresponds to which floor
				 */
				if (((floor.getUnitType() == null  || !floor.getUnitType().getCode().equals(PROPTYPE_OPEN_PLOT))
						&& unitTax.getFloorNumberInteger().equals(floor.getFloorNo())
						&& unitTax.getUnitNumber().equals(floor.getExtraField1())
						&& (unitTax.getUnitArea().toString()).equals(floor.getBuiltUpArea().getArea().toString())) 
					|| (floor.getUnitType() != null 
					        && floor.getUnitType().getCode().equals(PROPTYPE_OPEN_PLOT)
							&& unitTax.getUnitNumber().equals(floor.getExtraField1())
							&& (unitTax.getUnitArea().toString()).equals(floor.getBuiltUpArea().getArea().toString())
							&& unitTax.getUnitUsage().equals(floor.getPropertyUsage().getUsageName())
							&& unitTax.getUnitOccupation().equals(floor.getPropertyOccupation().getOccupation()) 
							&& unitTax.getOccpancyDate().equals(sdf.parse(floor.getExtraField3())))) {
					
					setFloorDmdCalTax(unitTax, floorDmdCalc);
				} 
			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.debug("floorDmdCalc: " + floorDmdCalc
				+ "\nExiting from createFloorDmdCalc");
		return floorDmdCalc;
	}

	private void setFloorDmdCalTax(UnitTaxCalculationInfo unitTax, FloorwiseDemandCalculations floorDmdCalc) {
		floorDmdCalc.setAlv(unitTax.getAnnualRentAfterDeduction());
		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				if (NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax1(floorDmdCalc.getTax1().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax2(floorDmdCalc.getTax2().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax3(floorDmdCalc.getTax3().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax4(floorDmdCalc.getTax4().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax5(floorDmdCalc.getTax5().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax6(floorDmdCalc.getTax6().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax7(floorDmdCalc.getTax7().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax8(floorDmdCalc.getTax8().add(taxDetail.getCalculatedTaxValue()));
				}
				if (NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax9(floorDmdCalc.getTax9().add(taxDetail.getCalculatedTaxValue()));
				}
			}
		}
	}
	public void createAttributeValues(Property property, Installment curInstall) {
		LOGGER.debug("Entered into createAttributeValues, property: " + property + ", curInstall: " + curInstall);
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
				List<UnitTaxCalculationInfo> unitTaxCalInfos = taxCalInfo.getUnitTaxCalculationInfo();
				for (FloorwiseDemandCalculations floorDmdCalc : floorDmdCalcSet) {
					FloorIF floor = floorDmdCalc.getFloor();
					UnitTaxCalculationInfo unitTaxCalInfo1 = null;
					String floorString = (floor.getFloorNo() == null || floor.getFloorNo().equals(OPEN_PLOT_UNIT_FLOORNUMBER)) ? propertyTaxUtil
							.getFloorStr(OPEN_PLOT_UNIT_FLOORNUMBER) : propertyTaxUtil.getFloorStr(floor.getFloorNo());
					
					try {
						for (UnitTaxCalculationInfo unitTaxCalInfo : unitTaxCalInfos) {
							
							/**
							 * This condition is applied because floor number is not
							 * mandatory in case of Mixed Property.(for UnitType = OPEN_PLOT). 
							 * So UnitType(mandatory only in case of Mixed Property) is considered.
							 */
							if (((floor.getUnitType() == null  || !floor.getUnitType().getCode().equals(PROPTYPE_OPEN_PLOT))
									&& (unitTaxCalInfo.getFloorNumber() == null || unitTaxCalInfo.getFloorNumber().equalsIgnoreCase(floorString))
									&& unitTaxCalInfo.getUnitNumber().equals(floor.getExtraField1())
									&& (unitTaxCalInfo.getUnitArea().toString()).equals(floor.getBuiltUpArea().getArea().toString())) 
								|| (floor.getUnitType() != null 
								        && floor.getUnitType().getCode().equals(PROPTYPE_OPEN_PLOT)
										&& unitTaxCalInfo.getUnitNumber().equals(floor.getExtraField1())
										&& (unitTaxCalInfo.getUnitArea().toString()).equals(floor.getBuiltUpArea().getArea().toString())
										&& unitTaxCalInfo.getUnitUsage().equals(floor.getPropertyUsage().getUsageName())
										&& unitTaxCalInfo.getUnitOccupation().equals(floor.getPropertyOccupation().getOccupation()) 
										&& unitTaxCalInfo.getOccpancyDate().equals(sdf.parse(floor.getExtraField3())))) {
								unitTaxCalInfo1 = unitTaxCalInfo;
								break;
								
							}
							/*if ((unitTaxCalInfo.getFloorNumber() != null 
									&& unitTaxCalInfo.getFloorNumber().equalsIgnoreCase(floorString))
									&& unitTaxCalInfo.getUnitNumber().equalsIgnoreCase(floor.getExtraField1())) {
								unitTaxCalInfo1 = unitTaxCalInfo;
								break;
							}*/
						}
					} catch (ParseException e) {
						LOGGER.error(e.getMessage(), e);
					}
					if (unitTaxCalInfo1 != null && !property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(
							PROPTYPE_STATE_GOVT)
							&& !property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(
									PROPTYPE_CENTRAL_GOVT))
						createAttributeValue(ALV, floorDmdCalc, unitTaxCalInfo1.getAnnualRentAfterDeduction()
								.toString());
				}
			}
		}
		LOGGER.debug("Exiting from createAttributeValues");
	}

	private EgAttributevalues createAttributeValue(String attributeType, FloorwiseDemandCalculations floorDmdCalc,
			String attrValue) {
		LOGGER.debug("Entered into createAttributeValue, attributeType: " + attributeType + ", floorDmdCalc: "
				+ floorDmdCalc + ", attrValue: " + attrValue);
		EgAttributevalues attributeVal = new EgAttributevalues();
		EgAttributetypeDao egAttrTypeDao = (EgAttributetypeDao) FlexfieldsDaoFactory.getDAOFactory()
				.getEgAttributetypeDao();
		EgAttributevaluesDao egAttrValDao = FlexfieldsDaoFactory.getDAOFactory().getEgAttributevaluesDao();
		EgAttributetype attrType = egAttrTypeDao.getAttributeTypeByDomainNameAndAttrName(floorDmdCalc.getClass()
				.getName(), attributeType);
		attributeVal.setEgApplDomain(attrType.getEgApplDomain());
		attributeVal.setEgAttributetype(attrType);
		attributeVal.setDomaintxnid(Long.valueOf(floorDmdCalc.getId()));
		attributeVal.setAttValue(attrValue);
		egAttrValDao.create(attributeVal);
		LOGGER.debug("attributeVal: " + attributeVal + "\nExiting from createAttributeValue");
		return attributeVal;
	}

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
		LOGGER.debug("completionDate: " + completionDate + "\nExiting from getLowestDtOfCompFloorWise");
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

	public Property createArrearsDemand(Property oldproperty, Date dateOfCompletion, Property property) {
		LOGGER.debug("Entered into createArrearsDemand, oldproperty: " + oldproperty + ", dateOfCompletion: "
				+ dateOfCompletion + ", property: " + property);
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		Ptdemand oldPtDmd = null;
		Ptdemand currPtDmd = null;
		Ptdemand oldCurrPtDmd = null;
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		Installment effectiveInstall = instalDao.getInsatllmentByModuleForGivenDate(module, dateOfCompletion);
		Installment currInstall = instalDao.getInsatllmentByModuleForGivenDate(module, new Date());
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
				if ((ptDmd.getEgInstallmentMaster().getFromDate()).before(effectiveInstall.getFromDate())) {
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

	private void addArrDmdDetToCurrentDmd(Ptdemand ptDmd, Ptdemand currPtDmd, Installment effectiveInstall) {
		LOGGER.debug("Entered into addArrDmdDetToCurrentDmd. ptDmd: " + ptDmd + ", currPtDmd: " + currPtDmd);
		for (EgDemandDetails dmdDet : ptDmd.getEgDemandDetails()) {
			if(!dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
				if ((dmdDet.getEgDemandReason().getEgInstallmentMaster().getFromDate()).before(effectiveInstall.getFromDate())) {
					currPtDmd.addEgDemandDetails((EgDemandDetails) dmdDet.clone());
				}
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
	public void initiateModifyWfForObjection(String propertyId, String objectionNum, Date objectionDate,
			User objWfInitiator,String docNumber, String modifyRsn) {
		LOGGER.debug("Entered into initiateModifyWfForObjection, propertyId: " + propertyId + ", objectionNum: "
				+ objectionNum + ", objectionDate: " + objectionDate + ", objWfInitiator: " + objWfInitiator);
		BasicProperty basicProperty = ((BasicProperty) getPropPerServ().findByNamedQuery(
				NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO, propertyId));
		LOGGER.debug("initiateModifyWfForObjection: basicProperty: " + basicProperty);
		PropertyImpl oldProperty = ((PropertyImpl) basicProperty.getProperty());
		PropertyImpl newProperty = (PropertyImpl) oldProperty.createPropertyclone();
		LOGGER.debug("initiateModifyWfForObjection: oldProperty: " + oldProperty + ", newProperty: " + newProperty);
		Date propCompletionDate = null;
		List floorProxy = new ArrayList();
		String propUsageId = null;
		String propOccId = null;
		
		String propertyTypeMasterCode = newProperty.getPropertyDetail().getPropertyTypeMaster().getCode();
		if (propertyTypeMasterCode.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
				|| ((propertyTypeMasterCode.equalsIgnoreCase(PROPTYPE_STATE_GOVT) 
						|| propertyTypeMasterCode.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) 
						&& newProperty.getPropertyDetail().getFloorDetails().isEmpty())) {
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
				
		for (FloorIF floor : newProperty.getPropertyDetail().getFloorDetails()) {
			if (floor != null) {
				floorProxy.add(floor);
			}
		}
		newProperty.getPropertyDetail().setFloorDetailsProxy(floorProxy);
		basicProperty.addPropertyStatusValues(createPropStatVal(basicProperty, PROPERTY_MODIFY_REASON_MODIFY,
				propCompletionDate, objectionNum, objectionDate, null, null));
		if (newProperty.getPropertyDetail().getPropertyOccupation() != null) {
			propOccId = newProperty.getPropertyDetail().getPropertyOccupation().getId().toString();
		}
		if (newProperty.getPropertyDetail().getPropertyUsage() != null) {
			propUsageId = newProperty.getPropertyDetail().getPropertyUsage().getId().toString();
		}
		newProperty = createProperty(newProperty, null, modifyRsn, newProperty.getPropertyDetail()
				.getPropertyTypeMaster().getId().toString(), propUsageId, propOccId, STATUS_WORKFLOW,null, null,false);

		// Setting the property state to the objection workflow initiator
		Position owner = eisCommonsService.getPositionByUserId(Integer.valueOf(objWfInitiator.getId()));
		String desigName = owner.getDesigId().getDesignationName();
		String value = WFLOW_ACTION_NAME_MODIFY + ":" + desigName + "_" + WF_STATE_APPROVAL_PENDING;
		State stateNew = new State("PropertyImpl", State.NEW, owner, "Property Workflow Started");
		State state = new State("PropertyImpl", value, owner, "");
		stateNew.setNext(state);
		state.setPrevious(stateNew);
		newProperty.setState(state);
		
		//Notice type doesnt exist for objection so set the notice type to 127
		if(newProperty.getExtra_field2() == null || newProperty.getExtra_field2().equals("")) {
			newProperty.setExtra_field2(NOTICE127);
		}
		newProperty.setExtra_field3(null);
		newProperty.setExtra_field4(null);
		newProperty.setExtra_field5(null);

		newProperty.setBasicProperty(basicProperty);

		newProperty.getPtDemandSet().clear();
		createDemand(newProperty, propCompletionDate,false);
		createArrearsDemand(oldProperty, propCompletionDate, newProperty);
		basicProperty.addProperty(newProperty);

		basicProperty = (BasicProperty) basicPrpertyService.update(basicProperty);
		if (!newProperty.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			createAttributeValues(newProperty, null);
		}
		LOGGER.debug("Exiting from initiateModifyWfForObjection");
	}

	/**
	 * Copies the owners from old property to new property
	 * 
	 * @param newProp
	 * @param oldProp
	 * @return @PropertyImpl
	 */
	public Property createOwnersForNew(Property newProp, Property oldProp) {
		LOGGER.debug("Entered into createOwnersForNew, newProp: " + newProp + ", OldProp; " + oldProp);
		Address oldOwnAddr = null;
		for (Owner owner : oldProp.getPropertyOwnerSet()) {
			Owner newOwner = new Owner();
			String ownerName = owner.getFirstName();
			ownerName = propertyTaxUtil.antisamyHackReplace(ownerName);
			newOwner.setFirstName(ownerName);
			newOwner.setLocale(owner.getLocale());
			for (Object address : owner.getAddressSet()) {
				oldOwnAddr = (Address) address;
				Address ownerAddr = new Address();
				String addrStr1 = oldOwnAddr.getStreetAddress1();
				String addrStr2 = oldOwnAddr.getStreetAddress2();
				if (addrStr1 != null && !addrStr1.isEmpty()) {
					addrStr1 = propertyTaxUtil.antisamyHackReplace(addrStr1);
				}
				if (addrStr2 != null && !addrStr2.isEmpty()) {
					addrStr2 = propertyTaxUtil.antisamyHackReplace(addrStr2);
				}
				ownerAddr.setAddTypeMaster(oldOwnAddr.getAddTypeMaster());
				ownerAddr.setStreetAddress1(addrStr1);
				ownerAddr.setStreetAddress2(addrStr2);
				if (oldOwnAddr.getPinCode() != null && !oldOwnAddr.getPinCode().toString().isEmpty()) {
					ownerAddr.setPinCode(Integer.valueOf(oldOwnAddr.getPinCode()));
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

	public EisCommonsService getEisCommonsService() {
		return eisCommonsService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
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
				PropertyStatusValues activePropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
						QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "Y",
						NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY);
				if (activePropStatVal != null) {
					activePropStatVal.setIsActive("N");
				}
				PropertyStatusValues wfPropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
						QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "W",
						NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY);
				if (wfPropStatVal != null) {
					wfPropStatVal.setIsActive("Y");
				}
			}
			if (PROPERTY_MODIFY_REASON_AMALG.equals(psv.getPropertyStatus().getStatusCode())
					&& psv.getIsActive().equals("W")) {
				PropertyStatusValues activePropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
						QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "Y",
						NMCPTISConstants.PROPERTY_MODIFY_REASON_AMALG);
				if (activePropStatVal != null) {
					activePropStatVal.setIsActive("N");
				}
				PropertyStatusValues wfPropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
						QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "W",
						NMCPTISConstants.PROPERTY_MODIFY_REASON_AMALG);
				if (wfPropStatVal != null) {
					wfPropStatVal.setIsActive("Y");
				}
			}
			if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(psv.getPropertyStatus().getStatusCode())
					&& psv.getIsActive().equals("W")) {
				PropertyStatusValues activePropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
						QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "Y",
						NMCPTISConstants.PROPERTY_MODIFY_REASON_BIFURCATE);
				if (activePropStatVal != null) {
					activePropStatVal.setIsActive("N");
				}
				PropertyStatusValues wfPropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
						QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "W",
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
	public Map<Installment, Map<String, BigDecimal>> populateTaxesForVoucherCreation(Property property) {
		LOGGER.debug("Entered into populateTaxesForVoucherCreation, property: " + property);
		Map<Installment, Map<String, BigDecimal>> amounts = new HashMap<Installment, Map<String, BigDecimal>>();
		if ((instTaxMap != null)) {
			for (Map.Entry<Installment, TaxCalculationInfo> instTaxRec : instTaxMap.entrySet()) {
				Map<String, BigDecimal> taxMap = taxCalculator.getMiscTaxesForProp(instTaxRec.getValue()
						.getUnitTaxCalculationInfo());
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
	public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForOldProp(Property property) {
		LOGGER.debug("Entered into prepareRsnWiseDemandForOldProp, property: " + property);
		Installment inst = null;
		Map<Installment, Map<String, BigDecimal>> instWiseDmd = new HashMap<Installment, Map<String, BigDecimal>>();
		for (Ptdemand ptdemand : property.getPtDemandSet()) {
			if (ptdemand.getIsHistory().equals("N")) {
				inst = ptdemand.getEgInstallmentMaster();
				Map<String, BigDecimal> rsnWiseDmd = new HashMap<String, BigDecimal>();
				for (EgDemandDetails dmdDet : ptdemand.getEgDemandDetails()) {
					if (inst.equals(dmdDet.getEgDemandReason().getEgInstallmentMaster())) {
						if (!dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(
								DEMANDRSN_CODE_PENALTY_FINES)
								|| dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(
										DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
							rsnWiseDmd.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode(), dmdDet
									.getAmount());
						}
					}
				}
				instWiseDmd.put(inst, rsnWiseDmd);
			}
		}
		LOGGER.debug("Exiting from prepareRsnWiseDemandForOldProp");
		return instWiseDmd;
	}

	public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForPropToBeDeactivated(Property property) {
		LOGGER.debug("Entered into prepareRsnWiseDemandForPropToBeDeactivated, property: " + property);

		Map<Installment, Map<String, BigDecimal>> amts = prepareRsnWiseDemandForOldProp(property);
		for (Installment inst : amts.keySet()) {
			for (String dmdRsn : (amts.get(inst)).keySet()) {
				amts.get(inst).put(dmdRsn, amts.get(inst).get(dmdRsn).negate());
			}
		}
		LOGGER.debug("amts: " + amts + "\n Exiting from prepareRsnWiseDemandForPropToBeDeactivated");
		return amts;
	}
	
	private void adjustExcessCollAmt(List<Installment> instList, Set<EgDemandDetails> newDemandDtlsSet
			, Map<Installment,Set<EgDemandDetails>> newDemandDtlsMap) {
		
		List<String> demandRsns1 = new ArrayList<String>() {
			{
				add("GEN_TAX");
				add("SEWERAGETAX");
				add("FIRE_SER_TAX");
				add("LIGHTINGTAX");
				add("GEN_WATER_TAX");
				//add("EDU_CESS_RESD");
				//add("EMP_GUA_CESS");
				//add("BIG_RESD_TAX");
			}
		};
		
		List<String> demandRsns2 = new ArrayList<String>() {
			{
				//add("GEN_TAX");
				//add("SEWERAGETAX");
				//add("FIRE_SER_TAX");
				//add("LIGHTINGTAX");
				//add("GEN_WATER_TAX");
				add("EDU_CESS_RESD");
				add("EMP_GUA_CESS");
				add("BIG_RESD_TAX");
			}
		};
		
	//	Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());
	//	Ptdemand ptDemandNew = (Ptdemand) newPtdemandMap.get(currentInstall.getDescription());
		
		// If the collected amt is less then tax, then adjust extra collection of same demand reason to it
		
		for(Installment inst1 : instList) {
			
			if(excessCollAmtMap.size() > 0) {
				Map<String,BigDecimal> excessInstDmdRsnAmt = excessCollAmtMap.get(inst1);
				
				for(String rsn1 : DEMAND_RSNS_LIST) {
					List<EgDemandDetails> newEgDmndDtlsList = null;
					Map<Installment, EgDemandDetails> newDmdDtlsMap = null;
					EgDemandDetails newDmndDtls = null;
					
					//newEgDmndDtlsList = getEgDemandDetailsListForReason(newDemandDtlsMap.get(inst1), rsn1);
					if (newDemandDtlsSet != null && newDemandDtlsSet.size() !=0) { 
						newEgDmndDtlsList = getEgDemandDetailsListForReason(newDemandDtlsSet, rsn1);
						if(newEgDmndDtlsList.size() != 0) {
							newDmndDtls = newEgDmndDtlsList.get(0);
						}
						
						if(newDmndDtls != null) {
							if(excessInstDmdRsnAmt.containsKey(rsn1) && excessInstDmdRsnAmt.get(rsn1).compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal excessTaxAmt1 = newDmndDtls.getAmount().subtract(newDmndDtls.getAmtCollected());
								//If excess Coll amt in map is greater than the amt collected then 
								if(excessTaxAmt1.compareTo(BigDecimal.ZERO) > 0 
										&& (excessInstDmdRsnAmt.get(rsn1)).compareTo(excessTaxAmt1) >= 0) {
									newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(excessTaxAmt1));
									BigDecimal remainingAmt = excessCollAmtMap.get(inst1).get(rsn1).subtract(excessTaxAmt1);
									//If there is still excess collection amount for the this particular reason and installment
									while(remainingAmt.compareTo(BigDecimal.ZERO) > 0) {
										for(Installment inst2 : instList) {
											List<String> rsnList = new ArrayList<String>();
											
											if(demandRsns1.contains(rsn1)) {
												rsnList = demandRsns1;
											} else if(demandRsns2.contains(rsn1)){
												rsnList = demandRsns2;
											}
											
											for(String rsn2 : rsnList) {
												List<EgDemandDetails> newEgDmndDtlsList2 = null;
												Map<Installment, EgDemandDetails> newDemandDtlsMap2 = null;
												EgDemandDetails newDmndDtls2 = null;
												
												newEgDmndDtlsList2 = getEgDemandDetailsListForReason(newDemandDtlsSet, rsn2);
												if (newEgDmndDtlsList2 != null) {
													newDemandDtlsMap2 = getEgDemandDetailsAsMap(newEgDmndDtlsList2);
													newDmndDtls2 = newDemandDtlsMap2.get(inst2);
												
													if(newDmndDtls2!=null) {
														BigDecimal excessTaxAmt2 = newDmndDtls2.getAmount().subtract(newDmndDtls2.getAmtCollected());
														if(excessTaxAmt2.compareTo(BigDecimal.ZERO) > 0 
																&& remainingAmt.compareTo(excessTaxAmt2) >= 0) {
															newDmndDtls2.setAmtCollected(newDmndDtls2.getAmtCollected().add(excessTaxAmt2));
															remainingAmt = excessCollAmtMap.get(inst1).get(rsn1).subtract(excessTaxAmt2);
														}
													}
												}
											}
										}
									}
								}
								//If the excess amount cannot be adjusted in the existing demand details, then add all the 
								//amount to the current installment's demand details with respective demand reasons
								else {
									newEgDmndDtlsList = new ArrayList<EgDemandDetails> (newDemandDtlsMap.get(currentInstall));
									newEgDmndDtlsList = getEgDemandDetailsListForReason(new HashSet<EgDemandDetails> (newEgDmndDtlsList), rsn1);
									if(newEgDmndDtlsList.size() != 0) {
										newDmndDtls = getEgDemandDetailsListForReason(new HashSet<EgDemandDetails> (newEgDmndDtlsList), rsn1).get(0);
									}
									
									if(newDmndDtls != null 
											&& newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(rsn1)) {
										newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(excessInstDmdRsnAmt.get(rsn1)));
										excessCollAmtMap.get(inst1).put(rsn1, excessCollAmtMap.get(inst1).get(rsn1)
												.subtract(excessInstDmdRsnAmt.get(rsn1)));
									} else {
										List<String> rsnList = new ArrayList<String>();
										
										if(demandRsns1.contains(rsn1)) {
											rsnList = demandRsns1;
										} else if(demandRsns2.contains(rsn1)){
											rsnList = demandRsns2;
										}
										
										for(String dmdrsn : rsnList) {
											newEgDmndDtlsList = getEgDemandDetailsListForReason(new HashSet<EgDemandDetails>(newEgDmndDtlsList), dmdrsn);
											if (newEgDmndDtlsList != null) {
												newDmdDtlsMap = getEgDemandDetailsAsMap(newEgDmndDtlsList);
												newDmndDtls = newDmdDtlsMap.get(currentInstall);
												if(newDmndDtls != null) {
													newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(excessInstDmdRsnAmt.get(rsn1)));
													excessCollAmtMap.get(inst1).put(rsn1, excessCollAmtMap.get(inst1).get(rsn1)
															.subtract(excessInstDmdRsnAmt.get(rsn1)));
													
													break;
												}
											}
										}
									}
								}
							}
						}
						
						if(newDmndDtls == null && excessInstDmdRsnAmt.containsKey(rsn1)) {
							List<String> rsnList = new ArrayList<String>();
							
							if(demandRsns1.contains(rsn1)) {
								rsnList = demandRsns1;
							} else if(demandRsns2.contains(rsn1)){
								rsnList = demandRsns2;
							}
							
							for(String dmdrsn : rsnList) {
								newEgDmndDtlsList = new ArrayList<EgDemandDetails> (newDemandDtlsMap.get(currentInstall));
								newEgDmndDtlsList = getEgDemandDetailsListForReason(new HashSet<EgDemandDetails>(newEgDmndDtlsList), dmdrsn);
								//newEgDmndDtlsList = getEgDemandDetailsListForReason(newDemandDtlsSet, dmdrsn);
								if (newEgDmndDtlsList != null) {
									newDmdDtlsMap = getEgDemandDetailsAsMap(newEgDmndDtlsList);
									newDmndDtls = newDmdDtlsMap.get(currentInstall);
									if(newDmndDtls != null) {
										newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(excessInstDmdRsnAmt.get(rsn1)));
										excessCollAmtMap.get(inst1).put(rsn1, excessCollAmtMap.get(inst1).get(rsn1)
												.subtract(excessInstDmdRsnAmt.get(rsn1)));
										
										break;
									}
								}
							}
						}
						
					}
				}
			}
		}
	}

	
}