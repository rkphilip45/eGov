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
package org.egov.ptis.nmc.service;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CENTRAL_GOVT_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.MIXED_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NONRESD_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NON_HISTORY_TAX_DETAIL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OPEN_PLOT_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OWNER_OCC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CAT_RESD_CUM_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESD_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATE_GOVT_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.TENANT_OCC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.VACANT_OCC;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.UnitAreaCalculationDetail;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.AreaTaxCalculationInfo;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.MiscellaneousTaxDetail;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.TaxDetail;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

// [CODE REVIEW] put javadoc explaining what this new class is for. Is it only used for migrating the XML? If so, 
// "PropertyNoticeService" is not the right name to use
public class PropertyNoticeService {

	private static final String STR_MIGRATED = "Migrated";
	private static final Logger LOGGER = Logger
			.getLogger(PropertyNoticeService.class);

	private String indexNumber;
	private BasicProperty basicProperty;
	private Map<Date, TaxCalculationInfo> taxCalculations = new TreeMap<Date, TaxCalculationInfo>();
	private List<UnitCalculationDetail> unitCalcDetails;
	private PropertyTaxUtil propertyTaxUtil;
	private PersistenceService<BasicProperty, Long> basicPropertyService;
	private Map<Date, String> occupancyAndPropertyType = new TreeMap<Date, String>();
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;
	@Autowired
	private InstallmentDao installmentDao;

	private static Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit;

	public PropertyNoticeService() {
	}

	public PropertyNoticeService(BasicProperty basicProperty,
			PropertyTaxUtil propertyTaxUtil,
			PersistenceService<BasicProperty, Long> basicPropertyService) {
		this.basicProperty = basicProperty;
		this.propertyTaxUtil = propertyTaxUtil;
		this.basicPropertyService = basicPropertyService;
	}

	// [CODE REVIEW] can this class be defined as a spring bean instead?
	public static PropertyNoticeService createNoticeService(
			BasicProperty basicProperty, PropertyTaxUtil propertyTaxUtil,
			PersistenceService<BasicProperty, Long> basicPropertyService) {
		return new PropertyNoticeService(basicProperty, propertyTaxUtil,
				basicPropertyService);
	}

	public Map<Date, TaxCalculationInfo> getTaxCaluculations() {
		return taxCalculations;
	}

	public void setTaxCaluculations(
			Map<Date, TaxCalculationInfo> taxCaluculations) {
		this.taxCalculations = taxCaluculations;
	}

	public List<UnitCalculationDetail> getUnitCalcDetails() {
		return unitCalcDetails;
	}

	public void setUnitCalcDetails(List<UnitCalculationDetail> unitCalcDetails) {
		this.unitCalcDetails = unitCalcDetails;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	private void preparePropertyTypesByOccupancy(
			Map<Date, Property> propertyByOccupancy) {
		for (Map.Entry<Date, Property> entry : propertyByOccupancy.entrySet()) {
			occupancyAndPropertyType.put(entry.getKey(), entry.getValue()
					.getPropertyDetail().getPropertyTypeMaster().getCode());
		}
	}

	public void migrateTaxXML() {
		LOGGER.debug("Entered into migrateTaxXML basicProperty.upicNo="
				+ basicProperty.getUpicNo());

		try {
			Map<Date, Property> propertyByOccupancy = getPropertiesByOccupancy();

			preparePropertyTypesByOccupancy(propertyByOccupancy);
			prepareInstallmentWiseTaxCalcs(propertyByOccupancy);

			initCurrentUnitSlabs();

			// here change can be ALV, guidance value, occupancy date or slab
			// change
			List<UnitCalculationDetail> unitCalculationDetails = getTheRowsForChange();

			persistUnitCalcDetails(unitCalculationDetails);
		} catch (Exception e) {
			String errorMsg = "Error in Tax XML migration for "
					+ basicProperty.getUpicNo();
			LOGGER.error(errorMsg, e);
			throw new EGOVRuntimeException(errorMsg, e);
		}
		LOGGER.debug("Exiting from migrateTaxXML");
	}

	private void initCurrentUnitSlabs() {
		dateAndTotalCalcTaxByTaxForUnit = new TreeMap<Integer, Map<String, Map<Date, BigDecimal>>>();
	}

	private List<UnitCalculationDetail> getTheRowsForChange() {
		LOGGER.debug("Entered into getTheRowsForChange");

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();

		Map<Integer, UnitTaxCalculationInfo> prevInstallmentUnitsByUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();
		Map<Integer, UnitTaxCalculationInfo> nextInstallmentUnitsByUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();

		InstallmentUnitTax instPrevCurrUnitTax = null;
		List<String> emptyList = Collections.<String> emptyList();
		Boundary propertyArea = null;

		for (Map.Entry<Date, TaxCalculationInfo> taxCalcAndInstallment : taxCalculations
				.entrySet()) {

			Module module = moduleDao
					.getModuleByName(PropertyTaxConstants.PTMODULENAME);
			Installment installment = installmentDao
					.getInsatllmentByModuleForGivenDate(module,
							taxCalcAndInstallment.getKey());
			TaxCalculationInfo taxCalcInfo = taxCalcAndInstallment.getValue();

			LOGGER.info("getTheRowsForChange - Installment =" + installment);

			prevInstallmentUnitsByUnitNo.putAll(nextInstallmentUnitsByUnitNo);

			// taking units freshly for each installment by clearing
			// the
			// map
			nextInstallmentUnitsByUnitNo.clear();

			for (UnitTaxCalculationInfo unitTax : taxCalcInfo
					.getConsolidatedUnitTaxCalculationInfo()) {
				nextInstallmentUnitsByUnitNo.put(unitTax.getUnitNumber(),
						unitTax);
			}

			if (basicProperty.getProperty().getAreaBndry() != null) {
				propertyArea = basicProperty.getProperty().getAreaBndry();
			} else {
				propertyArea = basicProperty.getPropertyID().getArea();
			}

			// checking for open plot, this will not work with Non Open plot
			// property
			// as we getting guidance value for per floor basis
			/*
			 * categories = propertyTaxUtil.getBoundaryCategories(null,
			 * propertyArea, installment,
			 * basicProperty.getProperty().getPropertyDetail());
			 */

			// For the first installment, directly saving the
			// details
			// into table as there is nothing
			// to compare against
			if (isStartingInstallment(prevInstallmentUnitsByUnitNo)) {

				for (Map.Entry<Integer, UnitTaxCalculationInfo> entry : nextInstallmentUnitsByUnitNo
						.entrySet()) {
					instPrevCurrUnitTax = InstallmentUnitTax.create(
							installment, null, entry.getValue());

					unitCalculationDetails.addAll(createUnitCalculationDetail(
							basicProperty.getProperty(), installment,
							taxCalcInfo,
							instPrevCurrUnitTax.getCurrentUnitAsList(), false));

					setOccupancyDateAsFromDate(unitCalculationDetails);

					instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);
				}

			} else {
				for (Map.Entry<Integer, UnitTaxCalculationInfo> currentUnitTaxEntry : nextInstallmentUnitsByUnitNo
						.entrySet()) {

					instPrevCurrUnitTax = InstallmentUnitTax.create(
							installment, prevInstallmentUnitsByUnitNo
									.get(currentUnitTaxEntry.getKey()),
							currentUnitTaxEntry.getValue());

					if (instPrevCurrUnitTax.isCurrentUnitNewUnit()) {

						unitCalculationDetails
								.addAll(createUnitCalculationDetail(
										basicProperty.getProperty(),
										installment, taxCalcInfo,
										instPrevCurrUnitTax
												.getCurrentUnitAsList(), false));

						instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);

					} else {

						if ((instPrevCurrUnitTax.isSameALV() && instPrevCurrUnitTax
								.isSameOccupancy())
								|| !instPrevCurrUnitTax.isSameALV()) {
							// check the occupancy date, if different occupancy
							// then create a row for this
							// check any tax slab is effective when alv is same
							// n occupancy is same

							if (!instPrevCurrUnitTax.isSameALV()) {

								// consider here the
								// isPrevCurrALVSame = false and
								// isPrevCurrOccupancySame = false

								unitCalculationDetails
										.addAll(createUnitCalculationDetail(
												basicProperty.getProperty(),
												installment,
												taxCalcInfo,
												instPrevCurrUnitTax
														.getCurrentUnitAsList(),
												false));
								instPrevCurrUnitTax
										.getCurrentUnitTaxSlabs(emptyList);
							}

							unitCalculationDetails
									.addAll(getUnitCalDetailsForSlabChange(
											basicProperty.getProperty(),
											taxCalcInfo, instPrevCurrUnitTax));

						}

						if (instPrevCurrUnitTax.isSameALV()
								&& !instPrevCurrUnitTax.isSameOccupancy()) {
							// indicates modification

							unitCalculationDetails
									.addAll(createUnitCalculationDetail(
											basicProperty.getProperty(),
											installment, taxCalcInfo,
											instPrevCurrUnitTax
													.getCurrentUnitAsList(),
											false));

							instPrevCurrUnitTax
									.getCurrentUnitTaxSlabs(emptyList);
						}
					}

				}

			}
		}

		LOGGER.debug("Exiting from getTheRowsForChange");
		return unitCalculationDetails;
	}

	private void setOccupancyDateAsFromDate(
			List<UnitCalculationDetail> unitCalculationDetails) {
		LOGGER.debug("Entered into setOccupancyDateAsFromDate");

		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {
			unitCalcDetail.setFromDate(unitCalcDetail.getOccupancyDate());
		}

		LOGGER.debug("Exiting from setOccupancyDateAsFromDate");
	}

	public void persistUnitCalcDetails(
			List<UnitCalculationDetail> unitCalculationDetails) {
		LOGGER.debug("Entered into persistUnitCalcDetails");

		basicProperty.getProperty()
				.addAllUnitCalculationDetails(
						new LinkedHashSet<UnitCalculationDetail>(
								unitCalculationDetails));
		basicProperty.setIsTaxXMLMigrated('Y');
		basicPropertyService.update(basicProperty);

		LOGGER.debug("Exiting from persistUnitCalcDetails");
	}

	/**
	 * @param prevInstallmentUnitsByUnitNo
	 * @return
	 */
	private boolean isStartingInstallment(
			Map<Integer, UnitTaxCalculationInfo> prevInstallmentUnitsByUnitNo) {
		return prevInstallmentUnitsByUnitNo.isEmpty();
	}

	private Map<Date, Property> getPropertiesByOccupancy() {
		LOGGER.debug("Entered into getPropertiesByOccupancy");

		Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		for (Property property : basicProperty.getPropertySet()) {
			if (property.getRemarks() == null
					|| !property.getRemarks().startsWith(STR_MIGRATED)) {
				propertyByCreatedDate.put(property.getCreatedDate().toDate(),
						property);
			}
		}

		Date effectiveDate = null;

		for (Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet()) {
			effectiveDate = entry.getValue().getPropertyDetail()
					.getEffective_date() == null ? entry.getValue()
					.getEffectiveDate() : entry.getValue().getPropertyDetail()
					.getEffective_date();

			propertyByOccupancyDate.put(effectiveDate, entry.getValue());
		}

		LOGGER.debug("Exiting from getPropertiesByOccupancy");

		return propertyByOccupancyDate;
	}

	private int getBeginIndex(Map<Date, Property> propertyByOccupancyDate) {
		LOGGER.debug("Entered into getBeginIndex");

		int beginIndex = 0;

		List<Date> occupancyDates = new ArrayList<Date>(
				propertyByOccupancyDate.keySet());

		if (occupancyDates.size() > 1) {
			Property firstProperty = propertyByOccupancyDate.get(occupancyDates
					.get(0));
			Property nextProperty = propertyByOccupancyDate.get(occupancyDates
					.get(1));

			if (firstProperty.getPropertyDetail().getPropertyMutationMaster()
					.getCode()
					.equalsIgnoreCase(NMCPTISConstants.MUTATION_CODE_NEW)
					&& nextProperty
							.getPropertyDetail()
							.getPropertyMutationMaster()
							.getCode()
							.equalsIgnoreCase(
									NMCPTISConstants.MUTATION_CODE_DATA_ENTRY)) {

				LOGGER.debug("Returning from getBeginIndex with value 1");
				return 1;
			}
		}

		LOGGER.debug("Exiting from getBeginIndex, beginIndex=" + beginIndex);
		return beginIndex;

	}

	private void prepareInstallmentWiseTaxCalcs(
			Map<Date, Property> propertyByOccupancyDate) {
		LOGGER.info("Entered into prepareInstallmentWiseTaxCalcs occupancyDates="
				+ propertyByOccupancyDate.keySet());

		Property prevProperty = null;
		Property nextProperty = null;
		List<Date> occupancyDates = new ArrayList<Date>(
				propertyByOccupancyDate.keySet());

		if (occupancyDates.size() > 1) {
			int first = getBeginIndex(propertyByOccupancyDate);
			int next = (first + 1);

			prevProperty = propertyByOccupancyDate.get(occupancyDates
					.get(first));
			nextProperty = propertyByOccupancyDate
					.get(occupancyDates.get(next));

			taxCalculations.putAll(propertyTaxUtil.getTaxCalInfoMap(
					prevProperty.getPtDemandSet(), occupancyDates.get(first)));

			// Consider the installment tax calcs only effective for the
			// prevProperty
			taxCalculations
					.keySet()
					.removeAll(
							getInstallmentStartDates(
									propertyTaxUtil
											.getInstallmentListByStartDate(getPropertyOccupancyDate(nextProperty)),
									occupancyDates.get(next)));

			List<Date> retainDates = new ArrayList<Date>();

			for (int i = next; i < propertyByOccupancyDate.size() - 1; i++) {

				prevProperty = propertyByOccupancyDate.get(occupancyDates
						.get(i));
				nextProperty = propertyByOccupancyDate.get(occupancyDates
						.get(i + 1));

				taxCalculations.putAll(propertyTaxUtil.getTaxCalInfoMap(
						prevProperty.getPtDemandSet(), occupancyDates.get(i)));

				retainDates
						.addAll(new ArrayList<Date>(taxCalculations.keySet()));

				retainDates
						.addAll(getInstallmentStartDates(
								propertyTaxUtil
										.getInstallmentListByStartDate(getPropertyOccupancyDate(prevProperty)),
								occupancyDates.get(i)));

				taxCalculations.keySet().retainAll(retainDates);

				// Consider the installment tax calcs only effective for the
				// prevProperty
				taxCalculations
						.keySet()
						.removeAll(
								getInstallmentStartDates(
										propertyTaxUtil
												.getInstallmentListByStartDate(getPropertyOccupancyDate(nextProperty)),
										occupancyDates.get(i + 1)));
			}
		}
		Date activePropOccupancyDate = occupancyDates
				.get(occupancyDates.size() - 1);
		Property activeProperty = propertyByOccupancyDate
				.get(activePropOccupancyDate);

		Map<Date, TaxCalculationInfo> activePropTaxCalcs = propertyTaxUtil
				.getTaxCalInfoMap(activeProperty.getPtDemandSet(),
						activePropOccupancyDate);

		// Consider the installment tax calcs only effective for the
		// activeProperty
		activePropTaxCalcs
				.keySet()
				.retainAll(
						getInstallmentStartDates(
								propertyTaxUtil
										.getInstallmentListByStartDate(getPropertyOccupancyDate(activeProperty)),
								activePropOccupancyDate));

		taxCalculations.putAll(activePropTaxCalcs);

		LOGGER.debug("prepareInstallmentWiseTaxCalcs - installments="
				+ taxCalculations.keySet());
		LOGGER.debug("Exiting from prepareInstallmentWiseTaxCalcs");
	}

	private List<Date> getInstallmentStartDates(List<Installment> installments,
			Date occupancyDate) {
		LOGGER.debug("Entered into getInstallmentStartDates installments="
				+ installments + ", occupancyDate=" + occupancyDate);
		List<Date> installmentStartDates = new ArrayList<Date>();

		for (Installment installment : installments) {
			if (propertyTaxUtil.between(occupancyDate,
					installment.getFromDate(), installment.getToDate())) {
				installmentStartDates.add(occupancyDate);
			} else {
				installmentStartDates.add(installment.getFromDate());
			}
		}

		LOGGER.debug("Exiting from getInstallmentStartDates - installmentStartDates="
				+ installmentStartDates);
		return installmentStartDates;
	}

	/**
	 * @param Property
	 * @return Date the occupancy date
	 */
	private Date getPropertyOccupancyDate(Property property) {
		return property.getPropertyDetail().getEffective_date() == null ? property
				.getEffectiveDate() : property.getPropertyDetail()
				.getEffective_date();
	}

	/**
	 * @param basicProperty
	 * @param unitCalculationDetails
	 * @param dateAndTotalCalcTaxByTaxForUnit
	 * @param installment
	 * @param taxCalcInfo
	 * @param currentUnitTax
	 * @param previousUnitTax
	 * @param units
	 */
	private List<UnitCalculationDetail> getUnitCalDetailsForSlabChange(
			Property property, TaxCalculationInfo taxCalcInfo,
			InstallmentUnitTax instUnitTax) {

		Map<String, Date> slabChangedTaxes = instUnitTax.getSlabChangedTaxes();

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();

		if (slabChangedTaxes.isEmpty()) {
			LOGGER.debug("slabChangedTaxes - No tax slabs have changed");
		} else {
			LOGGER.debug("slabChangedTaxes -" + slabChangedTaxes);

			List<UnitTaxCalculationInfo> unitsForTaxChange = propertyTaxUtil
					.prepareUnitTaxesForChangedTaxes(
							instUnitTax.getInstallment(),
							instUnitTax.getPrevUnitTax(),
							instUnitTax.getCurrentUnitTax(), slabChangedTaxes,
							PropertyTaxUtil.isPropertyModified(property));

			unitCalculationDetails.addAll(createUnitCalculationDetail(property,
					instUnitTax.getInstallment(), taxCalcInfo,
					unitsForTaxChange, true));

			instUnitTax.getCurrentUnitTaxSlabs(new ArrayList<String>(
					slabChangedTaxes.keySet()));
		}

		return unitCalculationDetails;
	}

	private List<UnitCalculationDetail> createUnitCalculationDetail(
			Property property, Installment installment,
			TaxCalculationInfo taxCalcInfo,
			List<UnitTaxCalculationInfo> unitTaxes, Boolean isTaxSlabChange) {
		LOGGER.debug("Entered into createUnitCalculationDetail");
		LOGGER.debug("createUnitCalculationDetail - property=" + property
				+ ", installment=" + installment + ", unitTaxes.size="
				+ unitTaxes.size() + ", isTaxSlabChange=" + isTaxSlabChange);

		UnitCalculationDetail unitCalculationDetail = null;
		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();

		try {
			for (UnitTaxCalculationInfo unitTax : unitTaxes) {
				unitCalculationDetail = new UnitCalculationDetail();
				unitCalculationDetail.setCreatedTimeStamp(new Date());
				unitCalculationDetail.setLastUpdatedTimeStamp(new Date());
				unitCalculationDetail.setUnitNumber(unitTax.getUnitNumber());
				unitCalculationDetail.setUnitArea(unitTax.getUnitArea());
				unitCalculationDetail.setOccupancyDate(unitTax
						.getOccpancyDate());

				unitCalculationDetail
						.setGuidanceValue(unitTax.getBaseRent() == null ? ZERO
								: unitTax.getBaseRent());
				unitCalculationDetail
						.setGuidValEffectiveDate(unitTax
								.getBaseRentEffectiveDate() == null ? unitTax
								.getOccpancyDate() : unitTax
								.getBaseRentEffectiveDate());
				unitCalculationDetail
						.setUnitOccupation(buildUnitOccupation(
								occupancyAndPropertyType.get(unitTax
										.getOccpancyDate()), unitTax));

				unitCalculationDetail.setInstallmentFromDate(installment
						.getFromDate());
				unitCalculationDetail
						.setMonthlyRent(unitTax.getMonthlyRent() == null ? ZERO
								: unitTax.getMonthlyRent());

				if (isTaxSlabChange) {
					unitCalculationDetail.setFromDate(new SimpleDateFormat(
							PropertyTaxConstants.DATE_FORMAT_DDMMYYY).parse(unitTax
							.getInstDate()));
				}

				setAnnualLettingValues(property, installment,
						unitCalculationDetail, taxCalcInfo);
				unitCalculationDetails.addAll(setMiscellaneousTaxDetails(
						property, installment, unitCalculationDetail, unitTax));
				setUnitAreaCalculationDetails(property, installment,
						unitCalculationDetail, unitTax);
			}
		} catch (ParseException e) {
			LOGGER.error("Error while parsing unit tax instDate", e);
			throw new EGOVRuntimeException(
					"Error while parsing unit tax instDate", e);
		}

		LOGGER.debug("createUnitCalculationDetail - unitCalculationDetails="
				+ unitCalculationDetails);
		LOGGER.debug("Exiting from createUnitCalculationDetail");

		return unitCalculationDetails;
	}

	private void setAnnualLettingValues(Property property,
			Installment installment,
			UnitCalculationDetail unitCalculationDetail,
			TaxCalculationInfo taxCalcInfo) {
		LOGGER.debug("Entered into setAnnualLettingValues");
		LOGGER.debug("setAnnualLettingValues - property=" + property
				+ ", installment=" + installment + ", unitCalculationDetail="
				+ unitCalculationDetail);

		Map<String, BigDecimal> taxNameAndALV = new TreeMap<String, BigDecimal>();

		List<List<UnitTaxCalculationInfo>> unitTaxes = taxCalcInfo
				.getUnitTaxCalculationInfos();

		for (int k = 0; k < unitTaxes.size(); k++) {

			if (unitTaxes.get(k) instanceof List) {
				if (unitTaxes.get(k).get(0).getUnitNumber()
						.equals(unitCalculationDetail.getUnitNumber())) {
					if (unitTaxes.get(k).size() > 1) {
						// here size > 1 indicates there are 2 base rents
						// effective,
						// in such cases taking the alv calculated using 2nd
						// base rent,
						// as the 1st BR is used in prvious case
						propertyTaxUtil.prepareTaxNameAndALV(taxNameAndALV,
								unitTaxes.get(k).get(1));
					} else {
						propertyTaxUtil.prepareTaxNameAndALV(taxNameAndALV,
								unitTaxes.get(k).get(0));
					}
				}
			} else {
				UnitTaxCalculationInfo unitTax = (UnitTaxCalculationInfo) unitTaxes
						.get(k);
				if (unitTax.getUnitNumber().equals(
						unitCalculationDetail.getUnitNumber())) {
					propertyTaxUtil
							.prepareTaxNameAndALV(taxNameAndALV, unitTax);
				}
			}
		}

		unitCalculationDetail.setAlv(taxNameAndALV
				.get(DEMANDRSN_CODE_SEWERAGE_TAX));
		unitCalculationDetail
				.setResidentialALV(taxNameAndALV
						.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD) == null ? BigDecimal.ZERO
						: taxNameAndALV
								.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD));
		unitCalculationDetail
				.setNonResidentialALV(taxNameAndALV
						.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD) == null ? BigDecimal.ZERO
						: taxNameAndALV
								.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD));
		unitCalculationDetail
				.setWaterTaxALV(taxNameAndALV
						.get(DEMANDRSN_CODE_GENERAL_WATER_TAX) == null ? BigDecimal.ZERO
						: taxNameAndALV.get(DEMANDRSN_CODE_GENERAL_WATER_TAX));
		unitCalculationDetail
				.setBigBuildingTaxALV(taxNameAndALV
						.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX) == null ? BigDecimal.ZERO
						: taxNameAndALV
								.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX));

		LOGGER.debug("setAnnualLettingValues - unitCalculationDetail="
				+ unitCalculationDetail);
		LOGGER.debug("Exiting from setAnnualLettingValues");
	}

	private List<UnitCalculationDetail> setMiscellaneousTaxDetails(
			Property property, Installment installment,
			UnitCalculationDetail unitCalculationDetail,
			UnitTaxCalculationInfo consolidatedUnitTax) {

		LOGGER.debug("Entered into setMiscellaneousTaxDetails");
		LOGGER.debug("setMiscellaneousTaxDetails - property=" + property
				+ ", installment=" + installment + ", unitCalculationDetail="
				+ unitCalculationDetail);

		BigDecimal totalCalculatedTax = BigDecimal.ZERO;

		Integer totalNoOfDays = PropertyTaxUtil.getNumberOfDays(
				installment.getFromDate(), installment.getToDate()).intValue();

		List<EgDemandReasonDetails> demandReasonDetails = new ArrayList<EgDemandReasonDetails>();

		String propertyType = property.getPropertyDetail()
				.getPropertyTypeMaster().getCode();
		String amenities = property.getPropertyDetail().getExtra_field4();
		String propertyTypeCategory = property.getPropertyDetail()
				.getExtra_field5();

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();
		Map<String, TaxDetail> taxDetailAndTaxName = new HashMap<String, TaxDetail>();

		BoundaryCategory boundaryCategory = null;

		Integer noOfDaysForNewTaxSlab = 0;

		for (MiscellaneousTax miscTax : consolidatedUnitTax
				.getMiscellaneousTaxes()) {

			if (hasNonHistoryTaxDetails(miscTax.getTaxDetails())) {

				String demandReasonCode = miscTax.getTaxName();

				BigDecimal alv = BigDecimal.ZERO;
				BigDecimal taxPercentage = BigDecimal.ZERO;
				BigDecimal calculatedAnnualTax = BigDecimal.ZERO;
				BigDecimal calculatedActualTax = BigDecimal.ZERO;
				BigDecimal demandRsnDtlPercResult = BigDecimal.ZERO;

				alv = getApplicableALV(unitCalculationDetail, demandReasonCode);

				LOGGER.debug("setMiscellaneousTaxDetails - demandReasonCode="
						+ demandReasonCode + ", alv = " + alv);

				demandReasonDetails = propertyTaxUtil.getDemandReasonDetails(
						demandReasonCode, alv, installment);
				EgDemandReasonDetails demandReasonDetail = demandReasonDetails
						.get(demandReasonDetails.size() - 1);

				if (propertyType != null
						&& propertyType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						&& miscTax.getTaxName().equalsIgnoreCase(
								DEMANDRSN_CODE_GENERAL_TAX)) {

					demandRsnDtlPercResult = BigDecimal.ZERO;

					if (demandReasonDetail != null) {

						if (ZERO.equals(demandReasonDetail.getFlatAmount())) {
							Amount amount = new Amount(
									demandReasonDetail.getPercentage());
							demandRsnDtlPercResult = amount.percentOf(alv);
							amount.setValue(new BigDecimal(
									STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION));
							calculatedAnnualTax = demandRsnDtlPercResult
									.subtract(amount
											.percentOf(demandRsnDtlPercResult));
						} else if (demandReasonDetail.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetail
									.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetail.getPercentage();
						}

					}

				} else {
					if (demandReasonDetails != null) {
						if (ZERO.equals(demandReasonDetail.getFlatAmount())) {
							taxPercentage = demandReasonDetail.getPercentage();
						} else if (demandReasonDetail.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetail
									.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetail.getPercentage();
						}
					}
				}

				if (propertyTypeCategory != null
						&& propertyTypeCategory
								.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
						&& (miscTax.getTaxName().equals(
								DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
								|| (miscTax.getTaxName()
										.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (miscTax
									.getTaxName()
								.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)))) {

					Amount amount = new Amount(new BigDecimal(
							RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE));
					calculatedAnnualTax = amount.percentOf(alv);
					amount.setValue(taxPercentage);
					calculatedAnnualTax = amount.percentOf(calculatedAnnualTax);

				} else if (!taxPercentage.equals(ZERO)
						&& ZERO.equals(calculatedAnnualTax)) {
					calculatedAnnualTax = new Amount(taxPercentage)
							.percentOf(alv);
				}

				if (demandReasonDetail != null
						&& demandReasonDetail.getFlatAmount().compareTo(ZERO) > 0) {

					// FlatAmount must be the maximum amount
					if (demandReasonDetail.getIsFlatAmntMax().equals(
							Integer.valueOf(1))
							&& (calculatedAnnualTax
									.compareTo(demandReasonDetail
											.getFlatAmount()) > 0)) {
						calculatedAnnualTax = demandReasonDetail
								.getFlatAmount();
					}

					// FlatAmount must be the minimum amount
					if (demandReasonDetail.getIsFlatAmntMax().equals(
							Integer.valueOf(0))
							&& (calculatedAnnualTax
									.compareTo(demandReasonDetail
											.getFlatAmount()) < 0)) {
						calculatedAnnualTax = demandReasonDetail
								.getFlatAmount();
					}
				}

				MiscellaneousTaxDetail miscTaxDetail = new MiscellaneousTaxDetail();
				miscTaxDetail.setFromDate(demandReasonDetail.getFromDate());
				miscTaxDetail.setTaxValue(demandReasonDetail.getPercentage());
				miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);

				if (propertyType != null
						&& propertyType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					calculatedActualTax = calculatedAnnualTax.setScale(0,
							ROUND_HALF_UP);
					calculatedAnnualTax = propertyTaxUtil
							.calcGovtTaxOnAmenities(amenities,
									calculatedAnnualTax);
					miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);
					miscTaxDetail.setActualTaxValue(calculatedActualTax);
				}

				calculatedAnnualTax = calculatedAnnualTax.setScale(0,
						ROUND_HALF_UP);

				miscTax.setTotalCalculatedTax(calculatedAnnualTax);
				miscTax.setTotalActualTax(calculatedActualTax.setScale(0,
						ROUND_HALF_UP));
				miscTax.getTaxDetails().clear();

				if (!ZERO.equals(calculatedAnnualTax)) {

					/*
					 * if (propertyType != null &&
					 * propertyType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					 * totalActualTax = totalActualTax.add(actualTaxValue); }
					 * 
					 * totalCalculatedTax =
					 * totalCalculatedTax.add(calculatedTaxValue);
					 */

					if (demandReasonDetail != null) {
						TaxDetail taxDetail = new TaxDetail();
						taxDetail.setTaxName(demandReasonCode);
						taxDetail.setCalculatedTax(calculatedAnnualTax);
						taxDetail.setFromDate(demandReasonDetail.getFromDate());
						taxDetailAndTaxName.put(demandReasonCode, taxDetail);
					}
				}
			}
		}

		setTaxDetails(unitCalculationDetail, taxDetailAndTaxName);
		unitCalculationDetail.setTaxPayable(totalCalculatedTax);

		if (noOfDaysForNewTaxSlab > 0) {
			unitCalculationDetail.setTaxDays(noOfDaysForNewTaxSlab);
		} else {
			unitCalculationDetail.setTaxDays(totalNoOfDays);
		}

		unitCalculationDetails.add(unitCalculationDetail);

		LOGGER.debug("unitCalculationDetails= " + unitCalculationDetails
				+ ", Exiting from setMiscellaneousTaxDetails");
		return unitCalculationDetails;
	}

	/**
	 *
	 * @param taxDetails
	 * @return true if there is a non history tax details, false if it only has
	 *         history tax detail
	 */
	private boolean hasNonHistoryTaxDetails(
			List<MiscellaneousTaxDetail> taxDetails) {

		for (MiscellaneousTaxDetail taxDetail : taxDetails) {
			if (isNonHistoryTaxDetail(taxDetail)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * @param taxDetail
	 * @return
	 */
	private boolean isNonHistoryTaxDetail(MiscellaneousTaxDetail taxDetail) {
		return taxDetail.getIsHistory() == null
				|| taxDetail.getIsHistory().equals(
						NMCPTISConstants.NON_HISTORY_TAX_DETAIL);
	}

	/**
	 * @param unitCalculationDetail
	 * @param demandReasonCode
	 * @return ALV
	 */
	private BigDecimal getApplicableALV(
			UnitCalculationDetail unitCalculationDetail, String demandReasonCode) {
		BigDecimal alv;

		if (demandReasonCode.equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_WATER_TAX)) {
			alv = unitCalculationDetail.getWaterTaxALV();
		} else if (demandReasonCode
				.equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
			alv = unitCalculationDetail.getResidentialALV();
		} else if (demandReasonCode
				.equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)
				|| demandReasonCode
						.equalsIgnoreCase(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)) {
			alv = unitCalculationDetail.getNonResidentialALV();
		} else if (demandReasonCode
				.equalsIgnoreCase(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
			alv = unitCalculationDetail.getBigBuildingTaxALV();
		} else {
			alv = unitCalculationDetail.getAlv();
		}

		return alv;
	}

	/**
	 * @param propertyTypeCategory
	 * @param demandReasonCode
	 * @return
	 */
	private boolean isPropTypeCatResdCumNonResd(String propertyTypeCategory,
			String demandReasonCode) {
		return propertyTypeCategory != null
				&& propertyTypeCategory
						.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
				&& (demandReasonCode
						.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
						|| (demandReasonCode
								.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (demandReasonCode
							.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)));
	}

	private static class Amount {
		private BigDecimal value;
		private static final BigDecimal HUNDRED = new BigDecimal(100);

		Amount(BigDecimal value) {
			this.value = value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}

		public BigDecimal percentOf(BigDecimal amount) {
			return amount.multiply(value).divide(HUNDRED);
		}
	}

	private void setTaxDetails(UnitCalculationDetail unitCalcDetail,
			Map<String, TaxDetail> taxDetailAndTaxName) {
		LOGGER.debug("Entered into setTaxDetails");
		LOGGER.debug("setTaxDetails - unitCalcDetail=" + unitCalcDetail
				+ ", taxDetailAndTaxName" + taxDetailAndTaxName);

		unitCalcDetail.setSewerageTax(taxDetailAndTaxName.get(
				DEMANDRSN_CODE_SEWERAGE_TAX).getCalculatedTax());
		unitCalcDetail.setSewerageTaxFromDate(taxDetailAndTaxName.get(
				DEMANDRSN_CODE_SEWERAGE_TAX).getFromDate());

		if (taxDetailAndTaxName.get(DEMANDRSN_CODE_GENERAL_WATER_TAX) != null) {
			unitCalcDetail.setWaterTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_GENERAL_WATER_TAX).getCalculatedTax());
			unitCalcDetail.setWaterTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_GENERAL_WATER_TAX).getFromDate());
		}

		unitCalcDetail.setGeneralTax(taxDetailAndTaxName.get(
				DEMANDRSN_CODE_GENERAL_TAX).getCalculatedTax());
		unitCalcDetail.setGeneralTaxFromDate(taxDetailAndTaxName.get(
				DEMANDRSN_CODE_GENERAL_TAX).getFromDate());

		if (taxDetailAndTaxName.get(DEMANDRSN_CODE_LIGHTINGTAX) != null) {
			unitCalcDetail.setLightTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_LIGHTINGTAX).getCalculatedTax());
			unitCalcDetail.setLightTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_LIGHTINGTAX).getFromDate());
		}

		if (taxDetailAndTaxName.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX) != null) {
			unitCalcDetail.setFireTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_FIRE_SERVICE_TAX).getCalculatedTax());
			unitCalcDetail.setLightTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_FIRE_SERVICE_TAX).getFromDate());
		}

		if (taxDetailAndTaxName.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD) != null) {
			unitCalcDetail.setEduCessResd(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD).getCalculatedTax());
			unitCalcDetail.setEduCessResdFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD).getFromDate());
		}

		if (taxDetailAndTaxName.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD) != null) {
			unitCalcDetail
					.setEduCessNonResd(taxDetailAndTaxName.get(
							DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)
							.getCalculatedTax());
			unitCalcDetail.setEduCessNonResdFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD).getFromDate());

			unitCalcDetail.setEmpGrntCess(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX).getCalculatedTax());
			unitCalcDetail.setEmpGrntCessFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX).getFromDate());
		}

		if (taxDetailAndTaxName.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX) != null) {
			unitCalcDetail
					.setBigBuildingTax(taxDetailAndTaxName.get(
							DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)
							.getCalculatedTax());
			unitCalcDetail.setBigBuildingTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX).getFromDate());
		}

		LOGGER.debug("setTaxDetails - unitCalcDetail=" + unitCalcDetail
				+ ", Exiting from setTaxDetails");
	}

	public void setUnitAreaCalculationDetails(Property property,
			Installment installment,
			UnitCalculationDetail unitCalculationDetail,
			UnitTaxCalculationInfo consolidatedUnitTax) {
		LOGGER.debug("Entered into setUnitAreaCalculationDetails");
		LOGGER.debug("setUnitAreaCalculationDetails - property=" + property
				+ ", installment=" + installment + ", unitCalculationDetail="
				+ unitCalculationDetail);

		for (AreaTaxCalculationInfo areaTaxCalc : consolidatedUnitTax
				.getAreaTaxCalculationInfos()) {
			UnitAreaCalculationDetail unitAreaCalcDetail = new UnitAreaCalculationDetail();

			unitAreaCalcDetail.setMonthlyBaseRent(areaTaxCalc
					.getMonthlyBaseRent() == null ? ZERO : areaTaxCalc
					.getMonthlyBaseRent());
			unitAreaCalcDetail.setMonthlyRentalValue(areaTaxCalc
					.getCalculatedTax() == null ? ZERO : areaTaxCalc
					.getCalculatedTax());
			unitAreaCalcDetail
					.setTaxableArea(areaTaxCalc.getTaxableArea() == null ? ZERO
							: areaTaxCalc.getTaxableArea());

			unitCalculationDetail
					.addUnitAreaCalculationDetail(unitAreaCalcDetail);

		}

		LOGGER.debug("setUnitAreaCalculationDetails - unitCalculationDetail="
				+ unitCalculationDetail);
		LOGGER.debug("Exiting from setUnitAreaCalculationDetails");
	}

	private Boolean isZero(BigDecimal value) {
		return BigDecimal.ZERO.compareTo(value) == 0;
	}

	/**
	 * unit occupancy in the 2nd column of the Notice/Prativrutta will be like
	 * for Open Plot if the occupancy is owner then its "Open Plot". In case of
	 * Open Plot if the occupancy is tenant then its "OP-Name of Occupier". In
	 * case of State govt and Central govt property's the format is
	 * "Prefix-Owner" (ex. SGovt-Owner) In case of other property types if the
	 * occupancy is owner or vacant then its "Prefix-Occupancy" (ex.
	 * R-Owner/R-Vacant). In case of other property types if the occupancy is
	 * tenant then its "Prefix-Name of Occupier" (ex. R-Suma).
	 */

	private String buildUnitOccupation(String propType,
			UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into buildUnitOccupation, propType=" + propType);

		StringBuilder occupierName = new StringBuilder();

		if (NMCPTISConstants.PROPTYPE_OPEN_PLOT.equals(propType)) {
			if (OWNER_OCC.equals(unit.getUnitOccupation())
					|| VACANT_OCC.equals(unit.getUnitOccupation())) {
				occupierName.append(propType);
			} else if (TENANT_OCC.equals(unit.getUnitOccupation())) {
				occupierName.append(OPEN_PLOT_SHORTFORM + "-"
						+ unit.getUnitOccupier());
			}
		} else if (NMCPTISConstants.PROPTYPE_RESD.equals(propType)) {
			occupierName.append(RESD_SHORTFORM);
		} else if (NMCPTISConstants.PROPTYPE_NON_RESD.equals(propType)) {
			occupierName.append(NONRESD_SHORTFORM);
		} else if (NMCPTISConstants.PROPTYPE_STATE_GOVT.equals(propType)) {
			occupierName.append(STATE_GOVT_SHORTFORM + "-" + OWNER_OCC);
		} else if (NMCPTISConstants.PROPTYPE_CENTRAL_GOVT.equals(propType)) {
			occupierName.append(CENTRAL_GOVT_SHORTFORM + "-" + OWNER_OCC);
		} else if (NMCPTISConstants.PROPTYPE_MIXED.equals(propType)) {
			occupierName.append(MIXED_SHORTFORM);
		}

		if (!NMCPTISConstants.PROPTYPE_OPEN_PLOT.equals(propType)
				&& !NMCPTISConstants.PROPTYPE_STATE_GOVT.equals(propType)
				&& !NMCPTISConstants.PROPTYPE_CENTRAL_GOVT.equals(propType)) {
			if (TENANT_OCC.equals(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupier());
			} else if (OWNER_OCC.equals(unit.getUnitOccupation())
					|| VACANT_OCC.equals(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupation());
			}
		}

		LOGGER.debug("occupierName=" + occupierName.toString()
				+ "\nExiting from buildUnitOccupation");

		return occupierName.toString();
	}

	private static class InstallmentUnitTax {

		private UnitTaxCalculationInfo prevUnitTax;
		private UnitTaxCalculationInfo currentUnitTax;
		private Installment installment;

		public InstallmentUnitTax() {
		}

		public InstallmentUnitTax(Installment installment,
				UnitTaxCalculationInfo prevUnitTax,
				UnitTaxCalculationInfo currentUnitTax) {
			this.installment = installment;
			this.prevUnitTax = prevUnitTax;
			this.currentUnitTax = currentUnitTax;
		}

		public static InstallmentUnitTax create(Installment installment,
				UnitTaxCalculationInfo prevUnitTax,
				UnitTaxCalculationInfo currentUnitTax) {
			return new InstallmentUnitTax(installment, prevUnitTax,
					currentUnitTax);
		}

		public boolean isCurrentUnitNewUnit() {
			return prevUnitTax == null ? currentUnitTax == null ? false : true
					: false;
		}

		public boolean isSameALV() {
			return prevUnitTax.getAnnualRentAfterDeduction().compareTo(
					currentUnitTax.getAnnualRentAfterDeduction()) == 0;
		}

		public boolean isSameOccupancy() {
			return prevUnitTax.getOccpancyDate().equals(
					currentUnitTax.getOccpancyDate());
		}

		public boolean isCurrentUnitSlabChanged() {
			return true;
		}

		public UnitTaxCalculationInfo getPrevUnitTax() {
			return prevUnitTax;
		}

		public UnitTaxCalculationInfo getCurrentUnitTax() {
			return currentUnitTax;
		}

		public Installment getInstallment() {
			return installment;
		}

		public List<UnitTaxCalculationInfo> getCurrentUnitAsList() {
			List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
			units.add(currentUnitTax);
			return units;
		}

		public void getCurrentUnitTaxSlabs(List<String> taxNames) {
			LOGGER.debug("Entered into getCurrentUnitTaxSlabs");
			LOGGER.debug("getCurrentUnitTaxSlabs - dateAndPercentageByTaxForUnit: "
					+ dateAndTotalCalcTaxByTaxForUnit);
			LOGGER.debug("getCurrentUnitTaxSlabs - taxNames: " + taxNames);

			Map<String, Map<Date, BigDecimal>> dateAndPercentageByTax = (dateAndTotalCalcTaxByTaxForUnit
					.get(currentUnitTax.getUnitNumber()) == null) ? new TreeMap<String, Map<Date, BigDecimal>>()
					: dateAndTotalCalcTaxByTaxForUnit.get(currentUnitTax
							.getUnitNumber());

			if (taxNames.isEmpty()) {
				for (MiscellaneousTax mt1 : currentUnitTax
						.getMiscellaneousTaxes()) {
					Map<Date, BigDecimal> dateAndPercentage1 = new TreeMap<Date, BigDecimal>();
					for (MiscellaneousTaxDetail mtd : mt1.getTaxDetails()) {
						if (isHistory(mtd)) {
							dateAndPercentage1.put(mtd.getFromDate(),
									mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt1.getTaxName(),
									dateAndPercentage1);
							break;
						}
					}
				}
			} else {
				for (MiscellaneousTax mt2 : currentUnitTax
						.getMiscellaneousTaxes()) {
					if (taxNames.contains(mt2.getTaxName())) {
						Map<Date, BigDecimal> dateAndPercentage2 = new TreeMap<Date, BigDecimal>();

						MiscellaneousTaxDetail mtd = mt2.getTaxDetails().size() > 1 ? mt2
								.getTaxDetails().get(1) : mt2.getTaxDetails()
								.get(0);

						if (isHistory(mtd)) {
							dateAndPercentage2.put(mtd.getFromDate(),
									mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt2.getTaxName(),
									dateAndPercentage2);
						}
					}
				}
			}

			dateAndTotalCalcTaxByTaxForUnit.put(currentUnitTax.getUnitNumber(),
					dateAndPercentageByTax);

			LOGGER.debug("Exiting from getCurrentUnitTaxSlabs - dateAndPercentageByTaxForUnit: "
					+ dateAndTotalCalcTaxByTaxForUnit);
		}

		// [CODE REVIEW] why is this public?
		public Map<String, Date> getSlabChangedTaxes() {
			LOGGER.debug("Entered into getSlabChangedTaxes");
			LOGGER.debug("getSlabChangedTaxes - dateAndPercentageByTaxForUnit: "
					+ dateAndTotalCalcTaxByTaxForUnit);
			LOGGER.debug("getSlabChangedTaxes - UnitNumber : "
					+ currentUnitTax.getUnitNumber());

			Map<String, Map<Date, BigDecimal>> taxAndListOfMapsOfDateAndPercentage = dateAndTotalCalcTaxByTaxForUnit
					.get(currentUnitTax.getUnitNumber());

			Map<String, Date> taxNames = new HashMap<String, Date>();

			if (taxAndListOfMapsOfDateAndPercentage != null
					&& !taxAndListOfMapsOfDateAndPercentage.isEmpty()) {
				for (MiscellaneousTax tax : currentUnitTax
						.getMiscellaneousTaxes()) {

					Map<Date, BigDecimal> taxDateAndPercentages = taxAndListOfMapsOfDateAndPercentage
							.get(tax.getTaxName());
					Map<Date, MiscellaneousTaxDetail> taxDetailAndEffectiveDate = new TreeMap<Date, MiscellaneousTaxDetail>();

					// Getting the slab effective dates in asc order
					for (MiscellaneousTaxDetail mtd : tax.getTaxDetails()) {
						if (mtd.getIsHistory() == null
								|| NON_HISTORY_TAX_DETAIL.equals(mtd
										.getIsHistory())) {
							taxDetailAndEffectiveDate.put(mtd.getFromDate(),
									mtd);
						}
					}

					// Getting the latest slab effective date,
					// as of now in NMC there can be only 2 slabs in a
					// installment period,
					// have considered this in order to simplify the process
					// else it will become complex
					MiscellaneousTaxDetail mtd = taxDetailAndEffectiveDate
							.get(taxDetailAndEffectiveDate.keySet().toArray()[taxDetailAndEffectiveDate
									.size() - 1]);

					LOGGER.info("getSlabChangedTaxes - " + mtd);

					if (taxDateAndPercentages != null) {
						if (taxDateAndPercentages.get(mtd.getFromDate()) == null) {
							taxNames.put(tax.getTaxName(), mtd.getFromDate());
						}
					} else {
						taxNames.put(tax.getTaxName(), mtd.getFromDate());
					}
				}
			}
			LOGGER.debug("getSlabChangedTaxes - slab changed taxes : "
					+ taxNames);
			LOGGER.debug("Exiting from getSlabChangedTaxes");
			return taxNames;
		}

		/**
		 * @param mtd
		 * @return true if the tax detail is history details else false
		 */
		private boolean isHistory(MiscellaneousTaxDetail mtd) {
			return mtd.getIsHistory() == null || mtd.getIsHistory().equals('N');
		}
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}
}
