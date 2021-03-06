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

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.AreaTaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

public class OpenPlotUnitTaxCalculator {

	private static final Logger LOGGER = Logger.getLogger(OpenPlotUnitTaxCalculator.class);
	private PersistenceService persistenceService;
	private PropertyTaxUtil propertyTaxUtil;

	public UnitTaxCalculationInfo calculateUnitTax(UnitTaxCalculationInfo unitTaxCalculationInfo, PropertyDetail propertyDetail,
			List<String> applicableTaxes, Installment installment, Long categoryId, BigDecimal areaofPlot,
			Property property, BoundaryCategory category) {

		String locFactIndex = "";
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		BigDecimal baseRent = unitTaxCalculationInfo.getBaseRent();

		/*
		 * Commented because we should not apply any factors on guidance value
		 * for rent chart method (dated 29/06/2012)
		 */
		/*
		 * BigDecimal propertyUsageFactor = new
		 * BigDecimal(propertyDetail.getPropertyUsage().getUsagePercentage()
		 * .toString()); BigDecimal propertyOccpationFactor = new
		 * BigDecimal(propertyDetail
		 * .getPropertyOccupation().getOccupancyFactor() .toString());
		 * BigDecimal propertyLocationFactor = null;
		 */

		String propType = propertyDetail.getPropertyTypeMaster().getCode();

		// Add Unit Info
		this.addOpenPlotUnitinfo(propertyDetail, unitTaxCalculationInfo);

		Date dateConstant = null;
		/*
		 * Boolean taxRuleFlag =
		 * Boolean.valueOf(propertyTaxUtil.getAppConfigValue(PTMODULENAME,
		 * APPCONFIG_TAXCALC_RULE_RENTCHART, ""));
		 */
		/**
		 * if taxRuleFlag is true then apply base rate from Rent chart till
		 * today or apply different base rate if floor occupancy date is
		 * before/after 1-Apr-2008
		 */
		try {
			dateConstant = dateFormatter.parse(NMCPTISConstants.DATE_CONSTANT);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (baseRent == ZERO && (propertyDetail.getManualAlv() == null || propertyDetail.getManualAlv().equals(""))) {
			LOGGER.debug("Base rent value for the installment : " + installment + " is not available in Rent Chart");
			throw new EGOVRuntimeException("Base rent value for the installment : " + installment
					+ " is not available in Rent Chart");
		} else if (propertyDetail.getManualAlv() != null && !propertyDetail.getManualAlv().equals("")) {
			LOGGER.info("Using Manual ALV in tax calculation for the installment : " + installment);
			locFactIndex = "BASERATE";
			unitTaxCalculationInfo.setAnnualRentAfterDeduction(propertyDetail.getManualAlv());
			unitTaxCalculationInfo.setManualAlv(propertyDetail.getManualAlv().toString());

			// Adding AreaTaxCalculationInfo to UnitTaxCalculationInfo for
			// display in ICS
			AreaTaxCalculationInfo areaTaxCalculationInfo1 = new AreaTaxCalculationInfo();

			areaTaxCalculationInfo1.setTaxableArea(areaofPlot);
			unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo1);
		} else {

			/**
			 * Calculate Base Rate
			 */
			/*
			 * if (installment.getToDate().after(dateConstant)) { baseRent =
			 * propertyLocationFactor.multiply(baseRent); }
			 */

			/*
			 * Commented because we should not apply any factors on guidance
			 * value for rent chart method (dated 29/06/2012)
			 */
			// BigDecimal baseRentPerSqMtPerMonth =
			// propertyOccpationFactor.multiply(propertyUsageFactor.multiply(baseRent));

			BigDecimal baseRentPerSqMtPerMonth = baseRent.setScale(2, BigDecimal.ROUND_HALF_UP);
			/**
			 * Calculate Monthly Rate
			 */
			BigDecimal monthlyBaseRentOfPlotArea = areaofPlot.multiply(baseRentPerSqMtPerMonth).setScale(0,
					BigDecimal.ROUND_HALF_UP);

			/**
			 * If the Plot is tenant occupied then pick the value greater of MRV
			 * and Actual Rent Paid
			 */
			if (propertyDetail.getPropertyOccupation().getOccupancyCode().equals(NMCPTISConstants.TENANT)) {
				if (propertyDetail.getExtra_field2() != null && monthlyBaseRentOfPlotArea != null) {
					unitTaxCalculationInfo.setMonthlyRentPaidByTenant(new BigDecimal(propertyDetail.getExtra_field2())
							.setScale(0, BigDecimal.ROUND_HALF_UP));
					if (new BigDecimal(propertyDetail.getExtra_field2()).compareTo(monthlyBaseRentOfPlotArea) == 1) {
						monthlyBaseRentOfPlotArea = new BigDecimal(propertyDetail.getExtra_field2()).setScale(0,
								BigDecimal.ROUND_HALF_UP);
					}
				}
			}
			/**
			 * Calculate Annual Rate
			 */
			BigDecimal annualRentOfPlot = monthlyBaseRentOfPlotArea.multiply(new BigDecimal("12")).setScale(0,
					BigDecimal.ROUND_HALF_UP);

			/**
			 * Apply Standard 10% Deduction
			 */
			BigDecimal applicableDeducationAmount = annualRentOfPlot.multiply(new BigDecimal(
					NMCPTISConstants.STANDARD_DEDUCTION_PERCENTAGE));

			applicableDeducationAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

			// ALV
			BigDecimal grossAnnualRentAfterDeduction = annualRentOfPlot.subtract(applicableDeducationAmount);

			unitTaxCalculationInfo.setBaseRentPerSqMtPerMonth(baseRentPerSqMtPerMonth);
			unitTaxCalculationInfo.setMonthlyRent(monthlyBaseRentOfPlotArea);
			unitTaxCalculationInfo.setAnnualRentBeforeDeduction(annualRentOfPlot);
			unitTaxCalculationInfo.setAnnualRentAfterDeduction(grossAnnualRentAfterDeduction.setScale(0,
					BigDecimal.ROUND_HALF_UP));

			// Adding AreaTaxCalculationInfo to UnitTaxCalculationInfo for
			// display in ICS
			AreaTaxCalculationInfo areaTaxCalculationInfo2 = new AreaTaxCalculationInfo();

			areaTaxCalculationInfo2.setTaxableArea(areaofPlot);
			areaTaxCalculationInfo2.setMonthlyBaseRent(baseRentPerSqMtPerMonth);
			areaTaxCalculationInfo2.setCalculatedTax(monthlyBaseRentOfPlotArea);
			unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo2);
			propertyDetail.setAlv(unitTaxCalculationInfo.getAnnualRentAfterDeduction());

		}
		unitTaxCalculationInfo.setLocationFactorCode("LF");

		/*
		 * if (installment.getToDate().before(dateConstant)) {
		 * unitTaxCalculationInfo.setLocationFactorValue(baseRent); } else {
		 * category = (Category)
		 * persistenceService.find("from Category c where c.id = ?",
		 * categoryId); propertyLocationFactor = new
		 * BigDecimal(category.getCategoryAmount());
		 * unitTaxCalculationInfo.setLocationFactorValue
		 * (propertyLocationFactor); }
		 */

		unitTaxCalculationInfo.setLocationFactorIndex(locFactIndex);
		unitTaxCalculationInfo.setUsageFactorCode("UF");
		unitTaxCalculationInfo.setUsageFactorIndex(propertyDetail.getPropertyUsage().getUsageName());
		// unitTaxCalculationInfo.setUsageFactorValue(propertyUsageFactor);
		unitTaxCalculationInfo.setOccupancyFactorCode("OF");
		unitTaxCalculationInfo.setOccupancyFactorIndex(propertyDetail.getPropertyOccupation().getOccupancyCode());
		// unitTaxCalculationInfo.setOccupancyFactorValue(propertyOccpationFactor);

		unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null, unitTaxCalculationInfo,
				installment, propType, propertyDetail.getExtra_field5(), null, property, category, null);

		/**
		 * Commented out as Tax Calculation is already day based
		 */
		// unitTaxCalculationInfo =
		// propertyTaxUtil.calculateTaxPayableByOccupancyDate(unitTaxCalculationInfo,
		// installment);

		unitTaxCalculationInfo.setFloorNumber("Open Plot");
		unitTaxCalculationInfo.setInstDate(DateUtils.getDefaultFormattedDate(propertyDetail.getProperty()
				.getBasicProperty().getPropCreateDate()));
		return unitTaxCalculationInfo;

	}

	public UnitTaxCalculationInfo addOpenPlotUnitinfo(PropertyDetail propertyDetail, UnitTaxCalculationInfo unitTaxCalculationInfo) {

		// Open Plot is considered as single unit
		unitTaxCalculationInfo.setUnitNumber(0);
		unitTaxCalculationInfo.setUnitArea(new BigDecimal(propertyDetail.getSitalArea().getArea().toString()));
		unitTaxCalculationInfo.setOccpancyDate(new Date(propertyDetail.getProperty().getBasicProperty()
				.getPropCreateDate().getTime()));
		unitTaxCalculationInfo.setUnitOccupation(propertyDetail.getPropertyOccupation().getOccupation());
		if (propertyDetail.getOccupierName() != null && !propertyDetail.getOccupierName().equals("")) {
			unitTaxCalculationInfo.setUnitOccupier(propertyDetail.getOccupierName());
		}

		return unitTaxCalculationInfo;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

}
