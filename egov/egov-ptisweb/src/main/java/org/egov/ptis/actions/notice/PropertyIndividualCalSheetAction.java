package org.egov.ptis.actions.notice;

import static org.egov.infstr.utils.EgovUtils.roundOffTwo;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CENTRALGOVT_BUILDING_ALV_PERCENTAGE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOOR_MAP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTAVAIL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPENPLOT_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATEGOVT_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_CALSHEET_FOR_GOVT_PROPS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_DEMAND_CALSHEET;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATEGOVT_BUILDING_ALV_PERCENTAGE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.SqFt;
import static org.egov.ptis.nmc.util.PropertyTaxUtil.isNotNull;
import static org.egov.ptis.nmc.util.PropertyTaxUtil.isNotZero;
import static org.egov.ptis.nmc.util.PropertyTaxUtil.isNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.bean.PropertyCalSheetInfo;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.UnitAreaCalculationDetail;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.egov.ptis.nmc.model.AreaTaxCalculationInfo;
import org.egov.ptis.nmc.model.ConsolidatedUnitTaxCalReport;
import org.egov.ptis.nmc.model.GovtPropertyInfo;
import org.egov.ptis.nmc.model.GovtPropertyTaxCalInfo;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.BaseFormAction;

public class PropertyIndividualCalSheetAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private final BigDecimal TOTAL_MONTHS = new BigDecimal("12");
	private ReportService reportService;
	private Integer reportId = -1;
	private String indexNum;
	private PropertyTaxUtil propertyTaxUtil;
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat yearformatter = new SimpleDateFormat("yyyy");
	private BasicProperty basicProperty;
	
	@Override
	public Object getModel() {
		return null;
	}

	public String generateCalSheet() {
		try {
			LOGGER.debug("Entered into generateCalSheet method");
			LOGGER.debug("Index Num in generateCalSheet : " + indexNum);
			BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
			basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNum);
			Property property = basicProperty.getProperty();
			ReportRequest reportInput = null;
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(PROPTYPE_STATE_GOVT) 
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(PROPTYPE_CENTRAL_GOVT)) {
				reportInput = getReportInputDataForGovtProps(property);
			} else {
				reportInput = getReportInputData(property);
			}
			reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportInput), getSession());
			LOGGER.debug("Exit from generateCalSheet method");
			return "calsheet";
		} catch (Exception e) {
			LOGGER.error("Exception in Generate Cal Sheet: ", e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
	}

	private ReportRequest getReportInputDataForGovtProps(Property property) {
		ReportRequest reportInput = null;
		List<UnitCalculationDetail> unitCalculationDetails = getUniqueALVUnitCalcDetails(property, true);

		GovtPropertyInfo govtPropInfo = prepareGovtPropInfo(unitCalculationDetails, PropertyCalSheetInfo.createCalSheetInfo(property));
		
		reportInput = new ReportRequest(REPORT_TEMPLATENAME_CALSHEET_FOR_GOVT_PROPS, govtPropInfo, null);
		reportInput.setPrintDialogOnOpenReport(false);
		return reportInput;
	}

	
	private GovtPropertyInfo prepareGovtPropInfo(List<UnitCalculationDetail> unitCalcDetails,
			PropertyCalSheetInfo calSheetInfo) {
		GovtPropertyInfo govtPropertyInfo = new GovtPropertyInfo();

		for (UnitCalculationDetail unitCalcDetail : unitCalcDetails) {
			GovtPropertyTaxCalInfo govtPropTaxCalInfo = null;
			govtPropertyInfo.setArea(calSheetInfo.getArea());
			govtPropertyInfo.setWard(calSheetInfo.getWard());
			govtPropertyInfo.setHouseNumber(calSheetInfo.getHouseNumber());
			govtPropertyInfo.setIndexNo(calSheetInfo.getIndexNo());
			govtPropertyInfo.setParcelId(calSheetInfo.getParcelId());
			govtPropertyInfo.setPropertyAddress(calSheetInfo.getPropertyAddress());
			govtPropertyInfo.setPropertyOwnerName(calSheetInfo.getPropertyOwnerName());
			govtPropertyInfo.setPropertyType(calSheetInfo.getPropertyType());

			govtPropTaxCalInfo = prepareGovtPropTaxCalTnfo(unitCalcDetail, calSheetInfo);
			govtPropTaxCalInfo.setEffectiveDate(formatter.format(unitCalcDetail.getOccupancyDate()));
			govtPropertyInfo.addGovtPropTaxCalInfo(govtPropTaxCalInfo);
		}

		return govtPropertyInfo;
	}

	private GovtPropertyTaxCalInfo prepareGovtPropTaxCalTnfo(UnitCalculationDetail unitCalcDetail,
			PropertyCalSheetInfo calSheetInfo) {

		GovtPropertyTaxCalInfo govtPropTaxCalInfo = new GovtPropertyTaxCalInfo();
		govtPropTaxCalInfo.setAmenities(calSheetInfo.getAmenities());
		govtPropTaxCalInfo.setAnnualLettingValue(unitCalcDetail.getAlv());
		govtPropTaxCalInfo.setBuildingCost(unitCalcDetail.getBuildingCost());

		if (isNotNull(unitCalcDetail.getUnitArea()) && isNotZero(unitCalcDetail.getUnitArea())) {
			govtPropTaxCalInfo.setPropertyArea(unitCalcDetail.getUnitArea().toString());
		} else {
			govtPropTaxCalInfo.setPropertyArea(NOTAVAIL);
		}

		if (calSheetInfo.getPropertyType().equalsIgnoreCase(PROPTYPE_STATEGOVT_STR)) {
			govtPropTaxCalInfo.setAlvPercentage(STATEGOVT_BUILDING_ALV_PERCENTAGE);
			govtPropTaxCalInfo.setAmenities(NOTAVAIL);
		} else {
			govtPropTaxCalInfo.setAlvPercentage(CENTRALGOVT_BUILDING_ALV_PERCENTAGE);
		}

		return govtPropTaxCalInfo;
	}

	private Map<Installment, TaxCalculationInfo> getTaxCalInfoMap(Set<Ptdemand> ptDmdSet) {
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Installment, TaxCalculationInfo>();
		for (Ptdemand ptdmd : ptDmdSet) {
			TaxCalculationInfo taxCalcInfo = propertyTaxUtil.getTaxCalInfo(ptdmd);
			if (taxCalcInfo != null) {
				taxCalInfoMap.put(ptdmd.getEgInstallmentMaster(), taxCalcInfo);
			}
		}
		return taxCalInfoMap;
	}
	
	
	private List<UnitCalculationDetail> getUniqueALVUnitCalcDetails(Property property, boolean isCompareAlv) {
		LOGGER.debug("Entered into getUniqueALVUnitCalcDetails, property=" + property);
		
		String query = "from UnitCalculationDetail ucd join fetch ucd.unitAreaCalculationDetails " +
				"where ucd.property = ? " +
				"order by ucd.unitNumber, ucd.installmentFromDate, ucd.fromDate";

		@SuppressWarnings("unchecked")
		List<UnitCalculationDetail> unitCalculationDetails = HibernateUtil.getCurrentSession().createQuery(query)
				.setEntity(0, property).list();
		
		List<UnitCalculationDetail> uniqueALVUnitCalcDetails = new ArrayList<UnitCalculationDetail>();
		UnitCalculationDetail prevUnitCalcDetail = null;
		
		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {
			
			if (uniqueALVUnitCalcDetails.isEmpty()) {
				uniqueALVUnitCalcDetails.add(new UnitCalculationDetail(unitCalcDetail));
			} else {
				if (isNotNull(prevUnitCalcDetail)) {
					if (isCompareAlv) {
						if (prevUnitCalcDetail.getAlv().compareTo(unitCalcDetail.getAlv()) != 0) {
							uniqueALVUnitCalcDetails.add(new UnitCalculationDetail(unitCalcDetail));
						}
					} else {
						if (prevUnitCalcDetail.getBuildingCost().compareTo(unitCalcDetail.getBuildingCost()) != 0) {
							uniqueALVUnitCalcDetails.add(new UnitCalculationDetail(unitCalcDetail));
						}
					}
				}
			}
			
			prevUnitCalcDetail = unitCalcDetail;
			
		}

		LOGGER.debug("Entered into getUniqueALVUnitCalcDetails, uniqueALVUnitCalcDetails=" + uniqueALVUnitCalcDetails);
		return uniqueALVUnitCalcDetails;
	}
	
	private ReportRequest getReportInputData(Property property) {
		LOGGER.debug("Entered into getReportInputData method");
		LOGGER.debug("Property : " + property);
		Set<Ptdemand> ptDmdSet = property.getPtDemandSet();
		List<ConsolidatedUnitTaxCalReport> consolidatedUnitTaxCalReportList = new ArrayList<ConsolidatedUnitTaxCalReport>();
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = null; //propertyTaxUtil.getTaxCalInfoMap(ptDmdSet);
		//Map<Installment, TaxCalculationInfo> taxCalInfoList = getTaxCalInfoList(taxCalInfoMap);
		List<UnitCalculationDetail> unitCalculationDetails = getUniqueALVUnitCalcDetails(property, true);
		UnitTaxCalculationInfo unitTaxCalcInfo = null;
		
		Map<Integer, Set<UnitAreaCalculationDetail>> unitCalcDetails = new TreeMap<Integer, Set<UnitAreaCalculationDetail>>();
		
		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {
			ConsolidatedUnitTaxCalReport consolidatedUnitTaxCalReport = new ConsolidatedUnitTaxCalReport();

			consolidatedUnitTaxCalReport.setAnnualLettingValue(roundOffTwo(unitCalcDetail.getAlv()));
			consolidatedUnitTaxCalReport.setMonthlyRent(unitCalcDetail.getMonthlyRent().compareTo(BigDecimal.ZERO) == 0 ? null : unitCalcDetail.getMonthlyRent());

			if (isNotNull(unitCalcDetail.getAlv()) && propertyTaxUtil.isNotZero(unitCalcDetail.getMonthlyRent())) {				
				consolidatedUnitTaxCalReport.setAnnualRentBeforeDeduction(unitCalcDetail.getMonthlyRent().multiply(TOTAL_MONTHS));
				BigDecimal dedAmt = consolidatedUnitTaxCalReport.getAnnualRentBeforeDeduction().divide(new BigDecimal(10));
				consolidatedUnitTaxCalReport.setDeductionAmount(roundOffTwo(dedAmt));
			}
			
			// The Assessment date has to be displayed if
			// its the first installment
			if (yearformatter.format(unitCalcDetail.getInstallmentFromDate()).equals(
					yearformatter.format(unitCalcDetail.getOccupancyDate()))) {

				consolidatedUnitTaxCalReport.setInstDate(DateUtils.getDefaultFormattedDate(unitCalcDetail
						.getOccupancyDate()));
			} else {
				consolidatedUnitTaxCalReport.setInstDate(DateUtils.getDefaultFormattedDate(unitCalcDetail
						.getInstallmentFromDate()));
			}
			
			
			consolidatedUnitTaxCalReport.setUnitTaxCalInfo(prepareUnitCalculationDetails(unitCalcDetail));
			
			consolidatedUnitTaxCalReportList.add(consolidatedUnitTaxCalReport);

		}
		
		PropertyCalSheetInfo propertyCalSheetinfo = PropertyCalSheetInfo.createCalSheetInfo(property);
		propertyCalSheetinfo.setConsolidatedUnitTaxCalReportList(consolidatedUnitTaxCalReportList);
		
		ReportRequest reportInput = new ReportRequest(REPORT_TEMPLATENAME_DEMAND_CALSHEET, propertyCalSheetinfo, null);
		reportInput.setPrintDialogOnOpenReport(false);
		LOGGER.debug("Exit from getReportInputData method");
		return reportInput;
	}
	
	private List<UnitTaxCalculationInfo> prepareUnitCalculationDetails(UnitCalculationDetail unitCalcDetail) {
		List<UnitTaxCalculationInfo> unitTaxes = new ArrayList<UnitTaxCalculationInfo>();
		Map<String, Set<UnitAreaCalculationDetail>> unitAreaDetails = new TreeMap<String, Set<UnitAreaCalculationDetail>>();
		
		String unitId = null;
		
		for (UnitAreaCalculationDetail unitAreaCalcDetail : unitCalcDetail.getUnitAreaCalculationDetails()) {
			unitId = unitAreaCalcDetail.getUnitIdentifier();
			
			if (isNull(unitAreaDetails.get(unitId))) {
				
				Set<UnitAreaCalculationDetail> unitAreas = new TreeSet<UnitAreaCalculationDetail>(new UnitAreaCalculationDetailComparator());				
				unitAreas.add(unitAreaCalcDetail);
				
				unitAreaDetails.put(unitId, unitAreas);
				
			} else {
				unitAreaDetails.get(unitId).add(unitAreaCalcDetail);
			}
			
		}
		
		UnitTaxCalculationInfo unitTaxInfo = null;		
		AreaTaxCalculationInfo areaTaxInfo = null;
		BigDecimal totalUnitArea = BigDecimal.ZERO;
		BigDecimal totalMonthlyRent = BigDecimal.ZERO;
		String floorNumberString = null;
		String manualALV = null;
		int i = 0;
		
		for (Map.Entry<String, Set<UnitAreaCalculationDetail>> entry : unitAreaDetails.entrySet()) {
			
			unitTaxInfo = new UnitTaxCalculationInfo();
			unitTaxInfo.setUnitNumber(unitCalcDetail.getUnitNumber());
			
			i = 0;
			totalUnitArea = BigDecimal.ZERO;
			totalMonthlyRent = BigDecimal.ZERO;
			
			for (UnitAreaCalculationDetail unitArea : entry.getValue()) {
				
				areaTaxInfo = new AreaTaxCalculationInfo();
				floorNumberString = FLOOR_MAP.get(unitArea.getFloorNumber());
				
				if (i == 0) {
					unitTaxInfo.setFloorNumber(isNull(floorNumberString) ? unitArea.getFloorNumber() : floorNumberString);
					unitTaxInfo.setFloorNumberInteger(isNull(floorNumberString) ? null : Integer.valueOf(floorNumberString));
					unitTaxInfo.setUsageFactorIndex(unitArea.getUnitUsage());
					unitTaxInfo.setUnitOccupation(unitArea.getUnitOccupation());
					
					manualALV = unitArea.getManualALV().compareTo(BigDecimal.ZERO) == 0 ? null : roundOffTwo(unitArea
							.getManualALV()).toString();
					
					unitTaxInfo.setManualAlv(manualALV);
					unitTaxInfo.setBaseRentPerSqMtPerMonth(roundOffTwo(unitArea.getBaseRentPerSqMtr()));
					unitTaxInfo.setMonthlyRentPaidByTenant(unitArea.getMonthlyRentPaidByTenanted().compareTo(
							BigDecimal.ZERO) == 0 ? null : unitArea.getMonthlyRentPaidByTenanted());
				}
				
				totalUnitArea = totalUnitArea.add(unitArea.getTaxableArea());
				totalMonthlyRent = totalMonthlyRent.add(unitArea.getMonthlyRentalValue());
				
				areaTaxInfo.setTaxableArea(roundOffTwo(unitArea.getTaxableArea()));			
				areaTaxInfo.setMonthlyBaseRent(roundOffTwo(unitArea.getMonthlyBaseRent()));
				areaTaxInfo.setCalculatedTax(unitArea.getMonthlyRentalValue());
				
				unitTaxInfo.addAreaTaxCalculationInfo(areaTaxInfo);
				
				i++;
			}
			
			unitTaxInfo.setUnitArea(roundOffTwo(totalUnitArea));
			unitTaxInfo.setMonthlyRent(EgovUtils.roundOff(totalMonthlyRent));
			unitTaxInfo.setUnitAreaInSqFt(roundOffTwo(totalUnitArea.multiply(SqFt)));
			unitTaxes.add(unitTaxInfo);
		}
		
		return unitTaxes;
	}
	
	
	private class UnitAreaCalculationDetailComparator implements Comparator<UnitAreaCalculationDetail> {

		@Override
		public int compare(UnitAreaCalculationDetail o1, UnitAreaCalculationDetail o2) {
			
			int result = o1.getTaxableArea().compareTo(o2.getTaxableArea());
			
			if (result == 0) {
				result = o1.getMonthlyBaseRent().compareTo(o2.getMonthlyBaseRent());
				// reverse logic to reverse the order greater amount to smaller amount
				result = result == -1 ? 1 : result == 1 ? -1 : 0;
			} else {
				result = result == 1 ? -1 : 1;
			}
			
			return result;
		}

		
		
	}

	private Map<Installment, TaxCalculationInfo> getTaxCalInfoList(Map<Installment, TaxCalculationInfo> taxCalInfoMap) {
		Map<Installment, TaxCalculationInfo> taxCalInfoList = new TreeMap<Installment, TaxCalculationInfo>();
		TaxCalculationInfo firstInstTxCalInfo=null;
		TaxCalculationInfo prevTaxCalInfo = null;
		Boolean isPropertyModified = PropertyTaxUtil.isPropertyModified(basicProperty.getProperty());
		int i = 0;
		
		for (Map.Entry<Installment, TaxCalculationInfo> txCalInfo : taxCalInfoMap.entrySet()) {
			
			//first installment
			if (i==0) {
				firstInstTxCalInfo = txCalInfo.getValue();
				Boolean isMultipleBRsEffective = false;
				//set Installment date
				for (UnitTaxCalculationInfo unitinfo : firstInstTxCalInfo.getConsolidatedUnitTaxCalculationInfo()) {
					
					if (firstInstTxCalInfo.getUnitTaxCalculationInfos().get(0) instanceof List) {
						for (List<UnitTaxCalculationInfo> utax : txCalInfo.getValue().getUnitTaxCalculationInfos()) {
							if (utax.size() > 1
									&& utax.get(0).getUnitNumber().equals(unitinfo.getUnitNumber())) {
								isMultipleBRsEffective = false;
								if (!isPropertyModified || (isPropertyModified && !(propertyTaxUtil.between(utax.get(0).getOccpancyDate(), txCalInfo.getKey()
											.getFromDate(), txCalInfo.getKey().getToDate())))) {
									isMultipleBRsEffective = true;
									break;
								}
							}
							
						}
					}
					
					unitinfo.setInstDate(DateUtils.getDefaultFormattedDate(unitinfo.getOccpancyDate()));
				}
				
				if (!isMultipleBRsEffective) {
					taxCalInfoList.put(txCalInfo.getKey(), firstInstTxCalInfo);
					prevTaxCalInfo = firstInstTxCalInfo;
				} else {
					continue;
				}
			}
			i++;
			if (i == 1) continue;
			
			int size = 0;
			for (UnitTaxCalculationInfo unitInfo1 : prevTaxCalInfo.getConsolidatedUnitTaxCalculationInfo()) {
					TaxCalculationInfo taxcal = null;
					List<UnitTaxCalculationInfo> removeListConUnitInfo = new ArrayList<UnitTaxCalculationInfo>();

					// Compare alv of each UnitTaxCalculationInfo and
					// remove(adding
					// to separete list) it from the list if the alv has not
					// changed
					for (UnitTaxCalculationInfo unitInfo2 : txCalInfo.getValue().getConsolidatedUnitTaxCalculationInfo()) {
						taxcal = txCalInfo.getValue();
						if (unitInfo1.getUnitNumber().equals(unitInfo2.getUnitNumber())) {

							if (unitInfo1.getAnnualRentAfterDeduction().compareTo(
									unitInfo2.getAnnualRentAfterDeduction()) == 0) {
								removeListConUnitInfo.add(unitInfo2);
							}
						}
					}

					if (removeListConUnitInfo.size() > 0) {
						List<UnitTaxCalculationInfo> removeListUnitInfo = new ArrayList<UnitTaxCalculationInfo>();
						// Remove from the ConsolidatedUnitTaxCalculationInfo
						// list
						taxcal.getConsolidatedUnitTaxCalculationInfo().removeAll(removeListConUnitInfo);

						// Remove the corresponding unittaxinfo from the
						// UnitTaxCalculationInfo list
						for (UnitTaxCalculationInfo ui1 : removeListConUnitInfo) {
							if (taxcal.getUnitTaxCalculationInfos().get(0) instanceof List) {
								for (List<UnitTaxCalculationInfo> ui2 : taxcal.getUnitTaxCalculationInfos()) {
									if (ui2.size() == 1 && ui1.getUnitNumber().equals(ui2.get(0).getUnitNumber())) {
										removeListUnitInfo.add(ui2.get(0));
									}
								}
							} else {
								for (int j = 0; j < taxcal.getUnitTaxCalculationInfos().size(); j++) {
									UnitTaxCalculationInfo unit = (UnitTaxCalculationInfo) taxcal.getUnitTaxCalculationInfos().get(j);
									if (ui1.getUnitNumber().equals(unit.getUnitNumber())) {
										removeListUnitInfo.add(unit);
									}
								}
							}
						}
						taxcal.getUnitTaxCalculationInfos().removeAll(removeListUnitInfo);
					}
					size++;
					if ((prevTaxCalInfo.getConsolidatedUnitTaxCalculationInfo().size()) == size) {
						if (taxcal != null && taxcal.getConsolidatedUnitTaxCalculationInfo().size() != 0) {
							prevTaxCalInfo = taxcal;
							taxCalInfoList.put(txCalInfo.getKey(), taxcal);
							txCalInfo.getKey();
						}
					}
			}
		}
		return taxCalInfoList;
	}

	public String getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(String indexNum) {
		this.indexNum = indexNum;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

}