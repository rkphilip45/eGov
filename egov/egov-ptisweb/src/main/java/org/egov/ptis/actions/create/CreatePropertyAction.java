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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.create;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNER_ADDR_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_ADDR_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AREA_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ASSISTANT_ROLE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AUDITDATA_STRING_SEP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CREATE_AUDIT_ACTION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DOCS_CREATE_PROPERTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.END_APPROVER_DESGN;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE127;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NOTICE134;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CAT_RESD_CUM_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROP_CREATE_RSN;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROP_CREATE_RSN_BIFUR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATUS_BILL_NOTCREATED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.VOUCH_CREATE_RSN_CREATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_NOTICE_GENERATION_PENDING;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ZONE_BNDRY_TYPE;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Address;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.enums.AddressType;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.FinancialUtil;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.utils.OwnerNameComparator;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validation;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validation()
public class CreatePropertyAction extends WorkflowAction {
	private static final String NO = "No";
	private static final String YES = "Yes";
	private static final String RESULT_ACK = "ack";
	private static final String RESULT_NEW = "new";
	private static final String RESULT_VIEW = "view";
	private static final String WORKFLOW_END = "END";
	private static final String MSG_REJECT_SUCCESS = " Property Rejected Successfully ";
	
	private Logger LOGGER = Logger.getLogger(getClass());
	private PropertyImpl property = new PropertyImpl();
	private PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PersistenceService<Property, Long> propertyImplService;
	BoundaryDAO boundaryDao;
	private Long zoneId;
	private Long wardId;
	private Long areaId;
	private String mobileNo;
	private String email;
	private String houseNumber;
	private String oldHouseNo;
	private String addressStr;
	private String pinCode;
	private String parcelID;
	private String northBound;
	private String southBound;
	private String eastBound;
	private String westBound;
	private String areaOfPlot;
	private String dateOfCompletion;
	private TreeMap<Integer, String> floorNoMap;
	private boolean chkIsTaxExempted;
	private String taxExemptReason;
	private boolean chkIsCorrIsDiff;
	private String corrAddress1;
	private String corrAddress2;
	private String corrPinCode;
	private boolean generalTax;
	private boolean sewerageTax;
	private boolean lightingTax;
	private boolean fireServTax;
	private boolean bigResdBldgTax;
	private boolean educationCess;
	private boolean empGuaCess;
	private String genWaterRate;
	private BasicProperty basicProp;
	private Map<String, String> waterMeterMap;
	private PropertyService propService;
	private Map<String, String> amenitiesMap;
	private Integer mutationId;
	private String parentIndex;
	private String isAuthProp;
	Date propCompletionDate = null;
	private String amenities;
	private String[] floorNoStr = new String[20];
	private String propTypeId;
	private String propUsageId;
	private String propOccId;
	private String ackMessage;
	private Map<Long, String> ZoneBndryMap;
	private List<PropertyOwner> propOwnerProxy;
	private Map<String, String> propTypeCategoryMap;
	private String propTypeCategoryId;
	private String method;
	FinancialUtil financialUtil = new FinancialUtil();

	private String khasraNumber;
	private String mauza;
	private String citySurveyNumber;
	private String sheetNumber;

	private HttpServletRequest servletRequest;
	private PropertyTypeMaster propTypeMstr;

	private String docNumber;
	private String partNo;
	private String nonResPlotArea;
	private Category propertyCategory;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

	private boolean isfloorDetailsRequired;
	private PropertyImpl propWF;// would be current property workflow obj
	private List<PropertyOwner> propertyOwnerProxy = new ArrayList<PropertyOwner>();
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private PropertyImpl newProperty = new PropertyImpl();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EisCommonService eisCommonService;
	
	public CreatePropertyAction() {
		super();
		property.setPropertyDetail(new BuiltUpProperty());
		this.addRelatedEntity("propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.unitType", PropertyTypeMaster.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.propertyUsage", PropertyUsage.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.propertyOccupation", PropertyOccupation.class);
		this.addRelatedEntity("propertyDetail.floorDetailsProxy.structureClassification", StructureClassification.class);
	}

	@SkipValidation
	@Override
	public Object getModel() {
		return property;
	}

	@SkipValidation
	public String newForm() {
		return RESULT_NEW;
	}

	public String create() {
		LOGGER.debug("create: Property creation started, Property: " + property + ", zoneId: " + zoneId + ", wardId: "
				+ wardId + ", areaId: " + areaId + ", areaOfPlot: " + areaOfPlot + ", dateOfCompletion: "
				+ dateOfCompletion + ", chkIsTaxExempted: " + chkIsTaxExempted + ", taxExemptReason: "
				+ taxExemptReason + ", isAuthProp: " + isAuthProp + ", propTypeId: " + propTypeId + ", propUsageId: "
				+ propUsageId + ", propOccId: " + propOccId);
		long startTimeMillis = System.currentTimeMillis();
				
		BasicProperty basicProperty = createBasicProp(STATUS_ISACTIVE, isfloorDetailsRequired);
		LOGGER.debug("create: BasicProperty after creatation: " + basicProperty);
		String indexNum = propertyTaxNumberGenerator.generateIndexNumber(basicProperty.getPropertyID().getWard()
				.getBoundaryNum().toString());
		basicProperty.setUpicNo(indexNum);
		basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
		
		if (StringUtils.isNotBlank(getDocNumber())) {		
			PropertyDocs pd = createPropertyDocs(basicProperty, getDocNumber());
			basicProperty.addDocs(pd);
		}
		
		basicPrpertyService.persist(basicProperty);
		setBasicProp(basicProperty);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((property.getPropertyDetail().getPropertyTypeMaster().getCode()
					.equalsIgnoreCase(PROPTYPE_STATE_GOVT) && !isfloorDetailsRequired)
					|| !property.getPropertyDetail().getPropertyTypeMaster()
							.getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)) {
				propService.createAttributeValues(property, null);
			}
		}
		transitionWorkFlow();
		Long userId = propertyTaxUtil.getLoggedInUser(getSession()).getId();
		User user = userService.getUserById(userId);			
		
		// For Data Entry; not creating the Voucher 
		if (allChangesCompleted) {
			Map<Installment, Map<String, BigDecimal>> amounts = propService.populateTaxesForVoucherCreation(property);
			financialUtil.createVoucher(basicProperty.getUpicNo(), amounts, VOUCH_CREATE_RSN_CREATE);
			property.setStatus(PropertyTaxConstants.STATUS_DEMAND_INACTIVE);
		} else {
			endWorkFlow();
			propService.initiateDataEntryWorkflow(basicProp, user);			
		} 
		
		setAckMessage("Property Created Successfully in System");
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.info("create: Property created successfully in system" 
				+ "; Time taken(ms) = "+elapsedTimeMillis);
		LOGGER.debug("create: Property creation ended");
		return RESULT_ACK;
	}

	@SkipValidation
	public String view() {
		LOGGER.debug("Entered into view, BasicProperty: " + basicProp + ", Property: " + property + ", userDesgn: "
				+ userDesgn);
		
		//This is reqd when the workflow object is returned to initiator(creator's) inbox after rejection
		/*if(ASSISTANT_ROLE.equals(userRole) 
				&& !propWF.getState().getValue().endsWith(WF_STATE_NOTICE_GENERATION_PENDING)) {
			setProperty(newProperty);
			setPropertyWfData();
			//return NEW;
		} */
		
		String currWfState = property.getState().getValue();
		
		if (currWfState.endsWith(WF_STATE_NOTICE_GENERATION_PENDING) || userDesgn.equalsIgnoreCase(END_APPROVER_DESGN)) {
			setIsApprPageReq(Boolean.FALSE);
			if (basicProp != null && basicProp.getUpicNo() != null && !basicProp.getUpicNo().isEmpty()) {
				setIndexNumber(basicProp.getUpicNo());
			}
		}

		PropertyDetail propertyDetail = property.getPropertyDetail();

		if (propertyDetail.getExtra_field4() != null && !propertyDetail.getExtra_field4().trim().isEmpty()) {
			setAmenities(CommonServices.getAmenitiesDtls(propertyDetail.getExtra_field4()));
		}

		if (propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT)
				|| propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)
				|| propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			setGenWaterRate(CommonServices.getWaterMeterDtls(propertyDetail.getExtra_field1()));
		}

		if (propertyDetail.getFloorDetails().size() > 0) {
			setFloorDetails(property);
		}
		List<PropertyOwner> propOwners = new ArrayList<PropertyOwner>(property.getPropertyOwnerSet());
		Collections.sort(propOwners, new OwnerNameComparator());
		setPropOwnerProxy(propOwners);
		setDocNumber(property.getDocNumber());
		setPropertyCategory((Category) persistenceService.find("from Category c where c.id = ?",
				Long.valueOf(property.getPropertyDetail().getExtra_field6())));
		
		LOGGER.debug("IndexNumber: " + indexNumber + ", GenWaterRate: " + genWaterRate + ", Amenities: " + amenities
				+ "NoOfFloors: " + ((getFloorDetails() != null) ? getFloorDetails().size() : "Floor list is NULL")
				+ " Exiting from view");
		return RESULT_VIEW;
	}

	@SkipValidation
	public String forward() {
		LOGGER.debug("forward: Property forward started " + property);
		long startTimeMillis = System.currentTimeMillis();		
		if (userRole.equalsIgnoreCase(ASSISTANT_ROLE) && isBlank(getModelId())) {
			this.validate();
			if (hasErrors()) {
				return NEW;
			}
			BasicProperty basicProperty = createBasicProp(STATUS_WORKFLOW, isfloorDetailsRequired);
			basicPrpertyService.persist(basicProperty);
			setBasicProp(basicProperty);
			setDocNumber(getDocNumber());

			if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
				propService.createAttributeValues(property, null);
			}
		} else {
						
			LOGGER.debug("forward: Property for Non Assistant role: " + property);
			
			super.validate();
			if (hasErrors()) {
				return RESULT_VIEW;
			}
			
			/**
			 * Reverted the Workflow changes, Do not remove the following commented blocks. 
			 */
			//This if block is used when the property is rejected and returns to creator's inbox.
			// Logic, if rejected n forwarding frm ASSISTANT role
			/*if(userRole.equalsIgnoreCase(PTCREATOR_ROLE)) { 
				this.validate();
				if (hasErrors()) {
					return NEW;
				}
				
				property = createPropertyDueToReject(STATUS_WORKFLOW, isfloorDetailsRequired);
				
				LOGGER.debug("forward: Property for Creator role: " + property);
			} else {
				super.validate();
				if (hasErrors()) {
					return "view";
				}
				property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
						Long.valueOf(getModelId()));
				LOGGER.debug("forward: Property for Non Creator role: " + property);
			}*/
			
			//setDocNumber(property.getDocNumber());
		}
		transitionWorkFlow();
		
		/*if (userRole.equalsIgnoreCase(PTCREATOR_ROLE) && isNotBlank(getModelId())) {
			Property propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));
			if (propWF.getStatus().equals(STATUS_WORKFLOW)) {
				basicProp.removeProperty(propWF);
				propertyImplService.delete(propWF);
				basicPrpertyService.update(basicProp);
			}
		}*/
		
		User approverUser = userService.getUserById(getWorkflowBean().getApproverUserId().longValue());
		setAckMessage("New Property forwarded successfully to " + approverUser.getUsername());
		long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
		LOGGER.info("forward: Property forwarded successfully to " + approverUser.getUsername() + "; Time taken(ms) = "+elapsedTimeMillis);
		LOGGER.debug("forward: Property forward ended");
		return RESULT_ACK;
	}

	@SkipValidation
	public String approve() {
		LOGGER.debug("approve: Property approval started");
		LOGGER.debug("approve: Property: " + property);
		LOGGER.debug("approve: BasicProperty: " + basicProp);
		property.setStatus(STATUS_ISACTIVE);
		setWardId(basicProp.getPropertyID().getWard().getId());
		basicProp.setUpicNo(propertyTaxNumberGenerator.generateIndexNumber(basicProp.getPropertyID().getWard()
				.getBoundaryNum().toString()));
		
		if (allChangesCompleted) {
			Map<Installment, Map<String, BigDecimal>> amounts = propService.populateTaxesForVoucherCreation(property);
			financialUtil.createVoucher(basicProp.getUpicNo(), amounts, VOUCH_CREATE_RSN_CREATE);
		}
		
		if (property.getDocNumber() != null && !property.getDocNumber().equals("")) {
			PropertyDocs pd = createPropertyDocs(basicProp, property.getDocNumber());
			basicProp.addDocs(pd);
		}
		
		basicPrpertyService.update(basicProp);
		transitionWorkFlow();
		this.createPropertyAuditTrail(basicProp, property, CREATE_AUDIT_ACTION, null);
		setAckMessage("Property Created Successfully in System with Index Number : ");
		LOGGER.debug("approve: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("approve: Property approval ended");
		return RESULT_ACK;
	}
	
	@SkipValidation
	public String reject() {
		LOGGER.debug("reject: Property rejection started");		
		transitionWorkFlow();
		
		if (WORKFLOW_END.equalsIgnoreCase(property.getState().getValue())) {
			setAckMessage(MSG_REJECT_SUCCESS);
		} else {
			setAckMessage(MSG_REJECT_SUCCESS + " and forwarded to initiator : "+ property.getCreatedBy().getUsername());
		}
		
		LOGGER.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
		LOGGER.debug("reject: Property rejection ended");
		
		return RESULT_ACK;
	}

	private PropertyDocs createPropertyDocs(BasicProperty basicProperty, String docNumber) {
		PropertyDocs pd = new PropertyDocs();
		pd.setDocNumber(docNumber);
		pd.setBasicProperty(basicProperty);
		pd.setReason(DOCS_CREATE_PROPERTY);
		return pd;
	}

	private boolean checkCorrespondingAddress() {
		LOGGER.debug("Entered into checkCorrespondingAddress, Property: " + property);
		PropertyOwner owner = property.getPropertyOwnerSet().iterator().next();
		Address address = (Address) owner.getAddress().iterator().next();
		LOGGER.debug("checkCorrespondingAddress: Property Address: " + address);
		if ((address.getStreetRoadLine() != null && !address.getStreetRoadLine().isEmpty())
				|| address.getPinCode() != null) {
			LOGGER.debug("checkCorrespondingAddress: CorrespondingAddress is available, Exiting from checkCorrespondingAddress");
			return true;
		}
		LOGGER.debug("CorrespondingAddress is Un-Available, Exiting from checkCorrespondingAddress");
		return false;
	}

	private void setFloorDetails(Property property) {
		LOGGER.debug("Entered into setFloorDetails, Property: " + property);
		Set<FloorIF> flrDtSet = property.getPropertyDetail().getFloorDetails();
		int i = 0;
		for (FloorIF flr : flrDtSet) {
			floorNoStr[i] = (propertyTaxUtil.getFloorStr(flr.getFloorNo()));
			LOGGER.debug("setFloorDetails: floorNoStr[" + i + "]->" + floorNoStr[i]);
			i++;
		}
		LOGGER.debug("Exiting from setFloorDetails");
	}

	public List<FloorIF> getFloorDetails() {
		return new ArrayList<FloorIF>(property.getPropertyDetail().getFloorDetails());
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	public void prepare() {

		LOGGER.debug("Entered into prepare, ModelId: " + getModelId() + ", PropTypeId: " + propTypeId + ", ZoneId: "
				+ zoneId + ", WardId: " + wardId);

		setUserInfo();
		if (StringUtils.isNotBlank(getModelId())) {
			
			property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(getModelId()));
			basicProp = property.getBasicProperty();
			
			/**
			 * Reverted the Workflow changes, Do not remove the following commented block. 
			 */
			
			/*if(userRole.equalsIgnoreCase(PTCREATOR_ROLE)) {
				propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
						Long.valueOf(getModelId()));
				if(propWF.getState().getValue().contains(WF_STATE_NOTICE_GENERATION_PENDING)) {
					property = propWF;
					basicProp = property.getBasicProperty();
				} else {
					newProperty = (PropertyImpl) propWF.createPropertyclone();
					newProperty.setBasicProperty(propWF.getBasicProperty());
					basicProp = newProperty.getBasicProperty();
				}
			} else if(!userRole.equalsIgnoreCase(PTCREATOR_ROLE)) {
				property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
						Long.valueOf(getModelId()));
				basicProp = property.getBasicProperty();
			}*/
			
			LOGGER.debug("prepare: Property by ModelId: " + property);
			LOGGER.debug("prepare: BasicProperty on property: " + basicProp);
			//setPropOwnerProxy(property.getPropertyOwnerProxy());
		}

		List<Boundary> wardList = getPersistenceService().findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.id", WARD_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
				"from PropertyTypeMaster order by orderNo");
		List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
		List<PropertyMutationMaster> mutationList = getPersistenceService().findAllBy(
				"from PropertyMutationMaster pmm where pmm.type=?", PROP_CREATE_RSN);
		List<String> authPropList = new ArrayList<String>();

		List<Boundary> zoneList = persistenceService.findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.id", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");

		authPropList.add(YES);
		authPropList.add(NO);
		List<String> noticeTypeList = new ArrayList<String>();
		noticeTypeList.add(NOTICE127);
		noticeTypeList.add(NOTICE134);
		prepareWardDropDownData(zoneId != null);
		prepareAreaDropDownData(wardId != null);
		addDropdownData("RightWard", wardList);
		addDropdownData("BackWard", wardList);
		addDropdownData("LeftWard", wardList);
		addDropdownData("PropTypeMaster", propTypeList);

		StringBuilder unitTypeQuery = new StringBuilder().append("from PropertyTypeMaster where code in ('")
				.append(PROPTYPE_OPEN_PLOT).append("', '").append(PROPTYPE_RESD).append("', '")
				.append(PROPTYPE_NON_RESD).append("') order by orderNo");

		addDropdownData("UnitTypes", getPersistenceService().findAllBy(unitTypeQuery.toString()));

		if (propTypeId == null || propTypeId.equals("-1")) {
			addDropdownData("UsageList", Collections.EMPTY_LIST);
		} else {
			addDropdownData("UsageList", CommonServices.usagesForPropType(Integer.parseInt(propTypeId)));
		}

		addDropdownData("OccupancyList", propOccList);
		addDropdownData("StructureList", Collections.EMPTY_LIST);
		addDropdownData("AgeFactorList", ageFacList);
		setWaterMeterMap(CommonServices.getWaterMeterMstr());
		addDropdownData("LocationList", Collections.EMPTY_LIST);
		addDropdownData("AuthPropList", authPropList);
		addDropdownData("NoticeTypeList", noticeTypeList);
		addDropdownData("MutationList", mutationList);
		addDropdownData("LocationFactorList", Collections.EMPTY_LIST);
		setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
		setAmenitiesMap(CommonServices.getAmenities());
		setFloorNoMap(CommonServices.floorMap());

		if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1")) {
			propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
					"from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
			if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
				setPropTypeCategoryMap(RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
				setPropTypeCategoryMap(NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
				setPropTypeCategoryMap(NMCPTISConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);
			}

			else {
				setPropTypeCategoryMap(Collections.EMPTY_MAP);
			}
		} else {
			setPropTypeCategoryMap(Collections.EMPTY_MAP);
		}
		// tax exempted properties
		addDropdownData("taxExemptedList", CommonServices.getTaxExemptedList());

		if (basicProp != null) {
			PropertyStatusValues psv = (PropertyStatusValues) getPersistenceService().findByNamedQuery(
					QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProp.getUpicNo(), "Y", PROP_CREATE_RSN);
			if (psv != null && psv.getReferenceBasicProperty() != null) {
				parentIndex = psv.getReferenceBasicProperty().getUpicNo();
			}
			
			if(basicProp.getAllChangesCompleted() != null) {
				setAllChangesCompleted(basicProp.getAllChangesCompleted());
			}
		}
		
		setupWorkflowDetails();
		
		super.prepare();
		LOGGER.debug("prepare: PropTypeList: "
				+ ((propTypeList != null) ? propTypeList : "NULL")
				+ ", PropOccuList: "
				+ ((propOccList != null) ? propOccList : "NLL")
				+ ", MutationList: "
				+ ((mutationList != null) ? mutationList : "NULL")
				+ ", ZoneList: "
				+ ((zoneList != null) ? zoneList : "NULL")
				+ ", AgeFactList: "
				+ ((ageFacList != null) ? ageFacList : "NULL")
				+ "UsageList: "
				+ ((getDropdownData().get("UsageList") != null) ? getDropdownData().get("UsageList") : "List is NULL")
				+ ", TaxExemptedReasonList: "
				+ ((getDropdownData().get("taxExemptedList") != null) ? getDropdownData().get("taxExemptedList")
						: "List is NULL"));

		LOGGER.debug("Exiting from prepare");
	}

	@SuppressWarnings("unchecked")
	private void prepareWardDropDownData(boolean zoneExists) {
		LOGGER.debug("Entered into prepareWardDropDownData, ZoneId: " + getZoneId());
		List<Boundary> wardNewList = new ArrayList<Boundary>();
		if (zoneExists) {
			wardNewList = getPersistenceService()
					.findAllBy(
							"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.id ",
							WARD_BNDRY_TYPE, getZoneId());
			addDropdownData("wardList", wardNewList);
		} else {
			addDropdownData("wardList", Collections.EMPTY_LIST);
		}

		LOGGER.debug("prepareWardDropDownData: NoOfWards for the Zone: "
				+ ((wardNewList != null) ? wardNewList.size() : "List is NULL"));
		LOGGER.debug("Exiting from prepareWardDropDownData");
	}

	@SuppressWarnings("unchecked")
	private void prepareAreaDropDownData(boolean wardExists) {
		LOGGER.debug("Entered into prepareAreaDropDownData, WardId: " + getWardId());
		List<Boundary> areaNewList = new ArrayList<Boundary>();
		if (wardExists) {
			areaNewList = getPersistenceService()
					.findAllBy(
							"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
							AREA_BNDRY_TYPE, getWardId());
			addDropdownData("areaList", areaNewList);
		} else {
			addDropdownData("areaList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("NoOfAreas in the ward: " + ((areaNewList != null) ? areaNewList.size() : "List is NULL")
				+ "\nExiting from prepareAreaDropDownData");
	}

	private BasicProperty createBasicProp(Character status, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createBasicProp, Property: " + property + ", status: " + status + ", ParcelId: "
				+ parcelID + ", wardId: " + wardId);

		BasicProperty basicProperty = new BasicPropertyImpl();
		PropertyStatus propStatus = (PropertyStatus) getPersistenceService().find(
				"from PropertyStatus where statusCode=?", "ASSESSED");

		//saving partno by removing preceding zeros ("0")
		basicProperty.setPartNo(StringUtils.removeStart(partNo, "0"));
		basicProperty.setActive(Boolean.TRUE);
		basicProperty.setGisReferenceNo(getParcelID());
		basicProperty.setAddress(createPropAddress());
		basicProperty.setPropertyID(createPropertyID(basicProperty));
		basicProperty.setStatus(propStatus);
		basicProperty.setAllChangesCompleted(allChangesCompleted);
		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
				"from PropertyMutationMaster pmm where pmm.type=? AND pmm.idMutation=?", PROP_CREATE_RSN, mutationId);
		basicProperty.setPropertyMutationMaster(propertyMutationMaster);
		basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, "CREATE", null, null, null,
				null, getParentIndex()));
		basicProperty.setBoundary(boundaryDao.getBoundary(getWardId()));
		basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
		property.setBasicProperty(basicProperty);
		
		/*
		 * isfloorDetailsRequired is used to check if floor details have to be
		 * entered for State Govt property or not if isfloorDetailsRequired -
		 * true : no floor details created false : floor details created
		 */
		property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
				propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), isfloorDetailsRequired);
		property.setStatus(status);
		
		LOGGER.debug("createBasicProp: Property after call to PropertyService.createProperty: " + property);
		basicProperty.setExtraField1(isAuthProp);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property
					.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))
					&& isfloorDetailsRequired) {
				propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
			} else {
				propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail()
						.getFloorDetailsProxy());
			}
		} else {
			propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
		}
		Date lowestInstDate = propertyTaxUtil.getStartDateOfLowestInstallment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lowestInstDate);

		if (propCompletionDate.before(calendar.getTime())) {
			propCompletionDate = calendar.getTime();
		}

		basicProperty.setPropCreateDate(propCompletionDate);
		createOwners();

		if ((propTypeMstr != null) 	&& propTypeMstr.getCode().equals(PROPTYPE_OPEN_PLOT)) {
			property.setPropertyDetail(changePropertyDetail());
		}

		property.getPropertyDetail().setEffective_date(propCompletionDate);
		basicProperty.addProperty(property);
		propService.createDemand(property,  null, propCompletionDate, isfloorDetailsRequired);
		LOGGER.debug("BasicProperty: " + basicProperty + "\nExiting from createBasicProp");
		return basicProperty;
	}

	private PropertyImpl createPropertyDueToReject(Character status, boolean isfloorDetailsRequired) {
		LOGGER.debug("Entered into createPropertyDueToReject, Property: " + property + ", status: " + status + ", ParcelId: "
				+ parcelID + ", wardId: " + wardId);
		PropertyImpl newProperty = new PropertyImpl();
		//saving partno by removing preceding zeros ("0")
		basicProp.setPartNo(StringUtils.removeStart(partNo, "0"));
		basicProp.setGisReferenceNo(getParcelID());
		basicProp.setAddress(createPropAddress());
		basicProp.setPropertyID(createPropertyID(basicProp));
		basicProp.getPropertyID();
		basicProp.setAllChangesCompleted(allChangesCompleted);
		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
				"from PropertyMutationMaster pmm where pmm.type=? AND pmm.idMutation=?", PROP_CREATE_RSN, mutationId);
		basicProp.setPropertyMutationMaster(propertyMutationMaster);
		basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, "CREATE", null, null, null,
				null, getParentIndex()));
		basicProp.setBoundary(boundaryDao.getBoundary(getWardId()));
		/*
		 * isfloorDetailsRequired is used to check if floor details have to be
		 * entered for State Govt property or not if isfloorDetailsRequired -
		 * true : no floor details created false : floor details created
		 */
		newProperty = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
				propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), isfloorDetailsRequired);
		LOGGER.debug("createPropertyDueToReject: Property after call to PropertyService.createProperty: " + property);
		basicProp.setExtraField1(isAuthProp);
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if ((property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_STATE_GOVT) || property
					.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT))
					&& isfloorDetailsRequired) {
				propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
			} else {
				propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail()
						.getFloorDetailsProxy());
			}
		} else {
			propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());
		}
		Date lowestInstDate = propertyTaxUtil.getStartDateOfLowestInstallment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lowestInstDate);

		if (propCompletionDate.before(calendar.getTime())) {
			propCompletionDate = calendar.getTime();
		}

		basicProp.setPropCreateDate(propCompletionDate);
		createOwners();
		newProperty.setBasicProperty(basicProp);

		if ((propTypeMstr != null) 	&& propTypeMstr.getCode().equals(PROPTYPE_OPEN_PLOT)) {
			newProperty.setPropertyDetail(changePropertyDetail());
		}

		newProperty.getPropertyDetail().setEffective_date(propCompletionDate);
		basicProp.addProperty(newProperty);
		propService.createDemand(newProperty, null, propCompletionDate, isfloorDetailsRequired);
		basicPrpertyService.update(basicProp);
		
		LOGGER.debug("BasicProperty: " + basicProp + "\nExiting from createPropertyDueToReject");
		return newProperty;
	}
	
	/**
	 * Changes the property details from {@link BuiltUpProperty} to
	 * {@link VacantProperty}
	 * 
	 * @return vacant property details
	 * 
	 * @see org.egov.ptis.domain.entity.property.VacantProperty
	 * 
	 */

	private VacantProperty changePropertyDetail() {

		LOGGER.debug("Entered into changePropertyDetail, Property is Vacant land");

		PropertyDetail propertyDetail = property.getPropertyDetail();
		VacantProperty vacantProperty = new VacantProperty(propertyDetail.getSitalArea(),
				propertyDetail.getTotalBuiltupArea(), propertyDetail.getCommBuiltUpArea(),
				propertyDetail.getPlinthArea(), propertyDetail.getCommVacantLand(), propertyDetail.getNonResPlotArea(),
				false, propertyDetail.getSurveyNumber(), propertyDetail.getFieldVerified(),
				propertyDetail.getFieldVerificationDate(), propertyDetail.getFloorDetails(),
				propertyDetail.getPropertyDetailsID(), propertyDetail.getWater_Meter_Num(),
				propertyDetail.getElec_Meter_Num(), 0, propertyDetail.getFieldIrregular(),
				propertyDetail.getCompletion_year(), propertyDetail.getEffective_date(),
				propertyDetail.getDateOfCompletion(), propertyDetail.getProperty(), propertyDetail.getUpdatedTime(),
				propertyDetail.getPropertyUsage(), null, propertyDetail.getPropertyTypeMaster(),
				propertyDetail.getPropertyType(), propertyDetail.getInstallment(),
				propertyDetail.getPropertyOccupation(), propertyDetail.getPropertyMutationMaster(),
				propertyDetail.getComZone(), propertyDetail.getCornerPlot());

		vacantProperty.setExtra_field1(propertyDetail.getExtra_field1());
		vacantProperty.setExtra_field2(propertyDetail.getExtra_field2());
		vacantProperty.setExtra_field3(propertyDetail.getExtra_field3());
		vacantProperty.setExtra_field4(propertyDetail.getExtra_field4());
		vacantProperty.setExtra_field5(propertyDetail.getExtra_field5());
		vacantProperty.setExtra_field6(propertyDetail.getExtra_field6());
		vacantProperty.setManualAlv(propertyDetail.getManualAlv());
		vacantProperty.setOccupierName(propertyDetail.getOccupierName());

		LOGGER.debug("Exiting from changePropertyDetail");
		return vacantProperty;
	}

	private void createOwners() {
		LOGGER.debug("Entered into createOwners, Property: " + property);

		AddressType addrTypeMstr = (AddressType) getPersistenceService().find(
				"from AddressType where addressTypeName = ?", OWNER_ADDR_TYPE);

		LOGGER.debug("createOwners: AddressTypemMaster: " + addrTypeMstr + ", CorrAddress1: " + getCorrAddress1()
				+ ", CorrAddress2: " + getCorrAddress2() + ", CorrPinCode: " + getCorrPinCode());
		PropertyOwner propertyOwner;
		String addrStr1;
		String addrStr2;
		Set<PropertyOwner> PropertyOwner = new HashSet<PropertyOwner>();
		int orderNo = 0;
		for (PropertyOwner owner : property.getPropertyOwnerProxy()) {
			orderNo++;
			if (owner != null) {
				String ownerName = owner.getName();
				ownerName = propertyTaxUtil.antisamyHackReplace(ownerName);
				propertyOwner = new PropertyOwner();
				propertyOwner.setName(ownerName);
				propertyOwner.setOrderNo(orderNo);
				Address ownerAddr = new Address();
				addrStr1 = getCorrAddress1();
				addrStr2 = getCorrAddress2();
				addrStr1 = propertyTaxUtil.antisamyHackReplace(addrStr1);
				addrStr2 = propertyTaxUtil.antisamyHackReplace(addrStr2);
				ownerAddr.setType(addrTypeMstr);
				//ownerAddr.setStreetAddress1(addrStr1);
				ownerAddr.setStreetRoadLine(addrStr2);
				if (getCorrPinCode() != null && !getCorrPinCode().isEmpty()) {
					ownerAddr.setPinCode(getCorrPinCode());
				}
				LOGGER.debug("createOwners: OwnerAddress: " + ownerAddr);
				propertyOwner.addAddress(ownerAddr);
				PropertyOwner.add(propertyOwner);
				//property.addPropertyOwner(owner);
			}
		}
		property.setPropertyOwnerSet(PropertyOwner);
		LOGGER.debug("Exiting from createOwners");
	}

	private PropertyAddress createPropAddress() {
		LOGGER.debug("Entered into createPropAddress, \nAreaId: " + getAreaId() + ", House Number: " + getHouseNumber()
				+ ", OldHouseNo: " + getOldHouseNo() + ", AddressStr: " + getAddressStr() + ", MobileNo: "
				+ getMobileNo() + ", Email: " + getEmail() + ", PinCode: " + getPinCode() + ", KhasraNumber: "
				+ getKhasraNumber() + ", Mauza:" + getMauza() + ", CitySurveyNumber: " + getCitySurveyNumber()
				+ ", SheetNumber:" + getSheetNumber());

		PropertyAddress propAddr = new PropertyAddress();
		StringBuffer addrStr1 = new StringBuffer();
		StringBuffer addrStr2 = new StringBuffer();
		propAddr.setType((AddressType) getPersistenceService().find(
				"from AddressType where addressTypeName = ?", PROP_ADDR_TYPE));
		//FIX ME
		//propAddr.setBlock(boundaryDao.getBoundary(getAreaId()).getName());
		propAddr.setHouseNoBldgApt(getHouseNumber());
		addrStr1.append(getHouseNumber());
		if (getOldHouseNo() != null && !getOldHouseNo().isEmpty()) {
			propAddr.setDoorNumOld(getOldHouseNo());
			addrStr1.append("(" + getOldHouseNo() + ")");
		}

		if (getAddressStr() != null && !getAddressStr().isEmpty()) {
			String addressStr = getAddressStr();
			addressStr = propertyTaxUtil.antisamyHackReplace(addressStr);
			propAddr.setStreetRoadLine(addressStr);
			addrStr1.append(", " + addressStr);
		}

		addrStr2.append(boundaryDao.getBoundary(getAreaId()).getName());

		if (getMobileNo() != null && !getMobileNo().isEmpty()) {
			propAddr.setMobileNo(getMobileNo());
		}
		if (getEmail() != null && !getEmail().isEmpty()) {
			propAddr.setEmailAddress(getEmail());
		}
		if (getPinCode() != null && !getPinCode().isEmpty()) {
			propAddr.setPinCode((getPinCode()));
		}
		if (!isChkIsCorrIsDiff()) {
			setCorrAddress1(addrStr1.toString());
			setCorrAddress2(addrStr2.toString());
			if (getPinCode() != null && !getPinCode().isEmpty()) {
				setCorrPinCode(getPinCode());
			}
		}

		propAddr.setExtraField1(getKhasraNumber());
		propAddr.setExtraField2(getMauza());
		propAddr.setExtraField3(getCitySurveyNumber());
		propAddr.setExtraField4(getSheetNumber());

		LOGGER.debug("PropertyAddress: " + propAddr + "\nExiting from createPropAddress");
		return propAddr;
	}

	private PropertyID createPropertyID(BasicProperty basicProperty) {
		LOGGER.debug("Entered into createPropertyID \nBasicProperty: " + basicProperty + ", NorthBound: "
				+ getNorthBound() + ", SouthBound: " + getSouthBound() + ", EastBound: " + getEastBound()
				+ ", WestBound: " + getWestBound());

		PropertyID propertyId = new PropertyID();
		propertyId.setZone(boundaryDao.getBoundary(getZoneId()));
		propertyId.setWard(boundaryDao.getBoundary(getWardId()));

		propertyId.setCreatedDate(new Date());
		propertyId.setModifiedDate(new Date());
		propertyId.setArea(boundaryDao.getBoundary(getAreaId()));

		if (getNorthBound() != null && !StringUtils.isEmpty(getNorthBound().trim())) {
			propertyId.setNorthBoundary(getNorthBound());
		}
		if (getSouthBound() != null && !StringUtils.isEmpty(getSouthBound().trim())) {
			propertyId.setSouthBoundary(getSouthBound());
		}
		if (getEastBound() != null && !StringUtils.isEmpty(getEastBound().trim())) {
			propertyId.setEastBoundary(getEastBound());
		}
		if (getWestBound() != null && !StringUtils.isEmpty(getWestBound().trim())) {
			propertyId.setWestBoundary(getWestBound());
		}

		propertyId.setBasicProperty(basicProperty);
		LOGGER.debug("PropertyID: " + propertyId + "\nExiting from createPropertyID");
		return propertyId;
	}

	public void validate() {
		LOGGER.debug("Entered into validate\nZoneId: " + zoneId + ", WardId: " + wardId + ", AreadId: " + areaId
				+ ", HouseNumber: " + houseNumber + ", MobileNo: " + mobileNo + ", PinCode: " + pinCode + ", ParcelID:"
				+ parcelID + ", MutationId: " + mutationId + ", PartNo: " + partNo);

		if (zoneId == null || zoneId == -1) {
			addActionError(getText("mandatory.zone"));
		}

		if (wardId == null || wardId == -1) {
			addActionError(getText("mandatory.ward"));
		} else if (houseNumber != null) {
			validateHouseNumber(wardId, houseNumber, basicProp);
		}

		if (StringUtils.isBlank(partNo)) {
			addActionError(getText("mandatory.partNo"));
		}
		for (PropertyOwner owner : property.getPropertyOwnerProxy()) {
			if (owner != null && owner.getName().equals("")) {
				addActionError(getText("mandatory.ownerName"));
			}
		}
		if (areaId == null || areaId == -1) {
			addActionError(getText("mandatory.area"));
		}
		if (houseNumber == null || houseNumber.equals("")) {
			addActionError(getText("mandatory.houseNo"));
		}
		if ((mobileNo != null && !mobileNo.equals("")) && mobileNo.length() < 10) {
			addActionError(getText("mandatory.mobileNo.size"));
		}
		if ((pinCode != null && !pinCode.equals("")) && pinCode.length() < 6) {
			addActionError(getText("mandatory.pincode.size"));
		}
		if (parcelID == null || parcelID.equals("")) {
			addActionError(getText("mandatory.parcelId"));
		}
		if (chkIsCorrIsDiff) {
			if (corrAddress1 == null || corrAddress1.equals("")) {
				addActionError(getText("mandatory.corr.addr1"));
			}
			if (corrAddress2 == null || corrAddress2.equals("")) {
				addActionError(getText("mandatory.corr.addr2"));
			}
			if ((corrPinCode != null && !corrPinCode.equals("")) && corrPinCode.length() < 6) {
				addActionError(getText("mandatory.corr.pincode.size"));
			}
		}

		if (getMutationId() == -1) {
			addActionError(getText("mandatory.createRsn"));
		}

		PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
				"from PropertyMutationMaster pmm where pmm.type=? AND pmm.idMutation=?", PROP_CREATE_RSN, 
				getMutationId());
		if (propertyMutationMaster != null) {
			if (StringUtils.equals(propertyMutationMaster.getCode(), PROP_CREATE_RSN_BIFUR)) {
				BasicProperty basicProperty = basicPrpertyService.findByNamedQuery(
						NMCPTISConstants.QUERY_BASICPROPERTY_BY_UPICNO, getParentIndex());
				if (getParentIndex() == null || StringUtils.isEmpty(getParentIndex())) {
					addActionError(getText("mandatory.parentIndex"));
				} else if (basicProperty == null) {
					addActionError(getText("mandatory.parentIndexNotFound"));
				}
			}
		}

		validateProperty(property, areaOfPlot, dateOfCompletion, chkIsTaxExempted, taxExemptReason, isAuthProp,
				propTypeId, propUsageId, propOccId, isfloorDetailsRequired, false);

		if (propTypeId != null && !propTypeId.equals("-1")) {
			PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
					"from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
			if (propTypeMstr != null) {
				if (propTypeMstr.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
					if (property.getPropertyDetail().getExtra_field5().equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)) {
						if (StringUtils.isBlank(nonResPlotArea)) {
							addActionError(getText("mandatory.nonResPlotArea"));
						} else if ((new Float(areaOfPlot)).compareTo(new Float(nonResPlotArea)) <= 0) {
							addActionError(getText("validNonResPlotArea"));
						}
					}
				}
			}
		}

		super.validate();

		LOGGER.debug("Exiting from validate");
	}

	/**
	 * 
	 */
	private void transitionWorkFlow() {
		LOGGER.debug("Entered method : transitionWorkFlow"); 
		
		if (workflowBean == null) {
			LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
		} else {
			LOGGER.debug("transitionWorkFlow - action : " + workflowBean.getActionName() + "property: " + property);
		}
		
		workflowAction = propertyTaxUtil.initWorkflowAction(property, workflowBean,
				Integer.valueOf(EGOVThreadLocals.getUserId()), eisCommonService);
		
		if (workflowAction.isNoWorkflow()) {
			startWorkFlow();
		}

		if (workflowAction.isStepRejectAndOwnerNextPositionSame()) {
			endWorkFlow();
		} else {		
			workflowAction.changeState();
		}
		
		if (workflowAction.isNoticeGenerated()) {
			endWorkFlow();
		}
		
		LOGGER.debug("transitionWorkFlow: Property transitioned to " + property.getState().getValue());
		propertyImplService.persist(property);

		LOGGER.debug("Exiting method : transitionWorkFlow");
	}
	
	private void setPropertyWfData() {
		property.setExtra_field1("");
		//property.setExtra_field2("");
		property.setExtra_field3("");
		property.setExtra_field4("");
		property.setExtra_field5("");
		setPartNo(basicProp.getPartNo());
		setParcelID(basicProp.getGisReferenceNo());
		setAllChangesCompleted(basicProp.getAllChangesCompleted());
		
		if(property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			setDateOfCompletion(sdf.format(property.getPropertyDetail().getEffective_date()));
			if(property.getPropertyDetail().getNonResPlotArea()!=null 
					&& !property.getPropertyDetail().getNonResPlotArea().equals("")) {
				setNonResPlotArea(property.getPropertyDetail().getNonResPlotArea().getArea().toString());
			}
		}
		
		PropertyID propId = basicProp.getPropertyID();
		setZoneId(propId.getZone().getId());
		setWardId(propId.getWard().getId());
		setAreaId(propId.getArea().getId());
		
		prepareWardDropDownData(true);
		prepareAreaDropDownData(true);
		
		setMutationId(basicProp.getPropertyMutationMaster().getIdMutation());
		PropertyAddress propAddress = basicProp.getAddress();
		setHouseNumber(propAddress.getHouseNoBldgApt());
		if(propAddress.getDoorNumOld() != null) {
			setOldHouseNo(propAddress.getDoorNumOld());
		}
		if(propAddress.getStreetRoadLine() != null) {
			setAddressStr(propAddress.getStreetRoadLine());
		}
		if(propAddress.getPinCode() != null) {
			setPinCode(propAddress.getPinCode().toString());
		}
		if(propAddress.getExtraField1() != null) {
			setKhasraNumber(propAddress.getExtraField1());
		}
		if(propAddress.getExtraField2() != null) {
			setMauza(propAddress.getExtraField2());
		}
		if(propAddress.getExtraField3() != null) {
			setCitySurveyNumber(propAddress.getExtraField3());
		}
		if(propAddress.getExtraField4() != null) {
			setSheetNumber(propAddress.getExtraField4());
		}
		if(propAddress.getMobileNo() != null) {
			setMobileNo(propAddress.getMobileNo());
		}
		if(propAddress.getEmailAddress() != null) {
			setEmail(propAddress.getEmailAddress());
		}
		
		Set<PropertyOwner> ownerSet = property.getPropertyOwnerSet();
		if (ownerSet != null && !ownerSet.isEmpty()) {
			List propOwProxy = new ArrayList();	
			for (PropertyOwner owner : ownerSet) {
				Set<Address> addrSet = (Set<Address>) owner.getAddress();
				for (Address address : addrSet) {
					
					if(address.getStreetRoadLine() != null) {
						setCorrAddress2(address.getStreetRoadLine());
					}
					if(address.getPinCode() != null) {
						setCorrPinCode(address.getPinCode().toString());
					}
					break;
				}
				propOwProxy.add(owner);
			}
			property.setPropertyOwnerProxy(propOwProxy);
		}
		
		if (basicProp.getExtraField1() != null) {
			setIsAuthProp(basicProp.getExtraField1());
		}
		if (property.getPropertyDetail().getSitalArea() != null) {
			setAreaOfPlot(property.getPropertyDetail().getSitalArea().getArea().toString());
		}
		if (property.getPropertyDetail().getFloorDetails() != null
				&& property.getPropertyDetail().getFloorDetails().size() > 0) {
			//isfloorDetailsRequired is enable the check box to be marked if floordetails present for State Govt property
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode()
					.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				isfloorDetailsRequired = false;
			}
			List flrDetsProxy = new ArrayList();					
			for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
				flrDetsProxy.add(floor);
			}
			property.getPropertyDetail().setFloorDetailsProxy(flrDetsProxy);
		} else{
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode()
					.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
				isfloorDetailsRequired = true;
			}
		}
		PropertyTypeMaster propertyType = property.getPropertyDetail().getPropertyTypeMaster();
		propTypeId = propertyType.getId().toString();
		if (propertyType.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
			setPropTypeCategoryMap(RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
		} else if (propertyType.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
			setPropTypeCategoryMap(NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
		} else if (propertyType.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			setPropTypeCategoryMap(NMCPTISConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);
		} else {
			setPropTypeCategoryMap(Collections.EMPTY_MAP);
		}

		if (property.getPropertyDetail().getExtra_field5() != null) {
			setPropTypeCategoryId(property.getPropertyDetail().getExtra_field5().toString());
		}
		
		prepareUsageList(propTypeId);
		if (property.getPropertyDetail().getPropertyUsage() != null) {
			propUsageId = property.getPropertyDetail().getPropertyUsage().getId().toString();
		}
		if (property.getPropertyDetail().getPropertyOccupation() != null) {
			propOccId = property.getPropertyDetail().getPropertyOccupation().getId().toString();
		}
		setDocNumber(property.getDocNumber());
	}
	
	private void prepareUsageList(String propTypeId) {
		LOGGER.debug("Entered into prepareUsageList, PropTypeId: " + propTypeId);
		if (propTypeId == null || propTypeId.equals("-1")) {
			addDropdownData("UsageList", Collections.EMPTY_LIST);
		} else {
			addDropdownData("UsageList", CommonServices.usagesForPropType(Integer.parseInt(propTypeId)));
		}
		LOGGER.debug("Exiting from prepareUsageList");
	}

	private void createPropertyAuditTrail(BasicProperty basicProperty, 
			Property property, String action, String auditDetails2) {
		Map<String,String> propTypeCategoryMap = new TreeMap<String, String>();
		String propCat = "";
		String locFact = "";
		StringBuilder auditDetail1 = new StringBuilder();
		if(property.getPropertyDetail().getExtra_field5() != null) {
			PropertyTypeMaster propType = property.getPropertyDetail().getPropertyTypeMaster();
			
			if (propType != null) {
				if (propType.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
					propTypeCategoryMap.putAll(NMCPTISConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
				} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
					propTypeCategoryMap.putAll(NMCPTISConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
				} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
					propTypeCategoryMap.putAll(NMCPTISConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);
				}
			}
			propCat = propTypeCategoryMap.get(property.getPropertyDetail().getExtra_field5());
		}
		if(property.getPropertyDetail().getExtra_field6() != null) {
			Category category = (Category) persistenceService.find("from Category c where c.id = ?",
					Long.valueOf(property.getPropertyDetail().getExtra_field6()));
			locFact = category.getCategoryName();
		}
		auditDetail1
				.append("Reason For Creation : ")
				.append(property.getPropertyDetail().getPropertyMutationMaster() != null ? property.getPropertyDetail()
						.getPropertyMutationMaster().getMutationName() : "")
				.append(AUDITDATA_STRING_SEP)
				.append("Property Type : ")
				.append(property.getPropertyDetail().getPropertyTypeMaster().getType() != null ? property
						.getPropertyDetail().getPropertyTypeMaster().getType() : "")
				.append(AUDITDATA_STRING_SEP)
				.append("Property Category : ")
				.append(propCat)
				.append(AUDITDATA_STRING_SEP)
				.append("Area of Plot : ")
				.append(property.getPropertyDetail().getSitalArea() != null ? property.getPropertyDetail()
						.getSitalArea().getArea() : "")
				.append(AUDITDATA_STRING_SEP)
				.append("Location Factor : ")
				.append(locFact)
				.append(AUDITDATA_STRING_SEP)
				.append("Exempted From Tax : ")
				.append(property.getIsExemptedFromTax() != null ? property.getIsExemptedFromTax() : "")
				.append(AUDITDATA_STRING_SEP)
				.append("Notice to be generated : ")
				.append(property.getExtra_field2() != null ? property.getExtra_field2() : "")
				.append(AUDITDATA_STRING_SEP)
				.append("Area : ")
				.append(basicProperty.getPropertyID().getArea() != null ? basicProperty.getPropertyID().getArea()
						.getName() : "");
		LOGGER.debug("Audit String : "+auditDetail1.toString());
		//propertyTaxUtil.generateAuditEvent(action, basicProperty, auditDetail1.toString(), auditDetails2);
	}
	
	public PropertyImpl getProperty() {
		return property;
	}

	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public BoundaryDAO getBoundaryDao() {
		return boundaryDao;
	}

	public void setBoundaryDao(BoundaryDAO boundaryDao) {
		this.boundaryDao = boundaryDao;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getOldHouseNo() {
		return oldHouseNo;
	}

	public void setOldHouseNo(String oldHouseNo) {
		this.oldHouseNo = oldHouseNo;
	}

	public String getAddressStr() {
		return addressStr;
	}

	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getParcelID() {
		return parcelID;
	}

	public void setParcelID(String parcelID) {
		this.parcelID = parcelID;
	}

	public String getNorthBound() {
		return northBound;
	}

	public void setNorthBound(String northBound) {
		this.northBound = northBound;
	}

	public String getSouthBound() {
		return southBound;
	}

	public void setSouthBound(String southBound) {
		this.southBound = southBound;
	}

	public String getEastBound() {
		return eastBound;
	}

	public void setEastBound(String eastBound) {
		this.eastBound = eastBound;
	}

	public String getWestBound() {
		return westBound;
	}

	public void setWestBound(String westBound) {
		this.westBound = westBound;
	}

	public String getAreaOfPlot() {
		return areaOfPlot;
	}

	public void setAreaOfPlot(String areaOfPlot) {
		this.areaOfPlot = areaOfPlot;
	}

	public String getDateOfCompletion() {
		return dateOfCompletion;
	}

	public void setDateOfCompletion(String dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public TreeMap<Integer, String> getFloorNoMap() {
		return floorNoMap;
	}

	public void setFloorNoMap(TreeMap<Integer, String> floorNoMap) {
		this.floorNoMap = floorNoMap;
	}

	public boolean isChkIsTaxExempted() {
		return chkIsTaxExempted;
	}

	public void setChkIsTaxExempted(boolean chkIsTaxExempted) {
		this.chkIsTaxExempted = chkIsTaxExempted;
	}

	public String getTaxExemptReason() {
		return taxExemptReason;
	}

	public void setTaxExemptReason(String taxExemptReason) {
		this.taxExemptReason = taxExemptReason;
	}

	public boolean isChkIsCorrIsDiff() {
		return chkIsCorrIsDiff;
	}

	public void setChkIsCorrIsDiff(boolean chkIsCorrIsDiff) {
		this.chkIsCorrIsDiff = chkIsCorrIsDiff;
	}

	public String getCorrAddress1() {
		return corrAddress1;
	}

	public void setCorrAddress1(String corrAddress1) {
		this.corrAddress1 = corrAddress1;
	}

	public String getCorrAddress2() {
		return corrAddress2;
	}

	public void setCorrAddress2(String corrAddress2) {
		this.corrAddress2 = corrAddress2;
	}

	public String getCorrPinCode() {
		return corrPinCode;
	}

	public void setCorrPinCode(String corrPinCode) {
		this.corrPinCode = corrPinCode;
	}

	public boolean isGeneralTax() {
		return generalTax;
	}

	public void setGeneralTax(boolean generalTax) {
		this.generalTax = generalTax;
	}

	public boolean isSewerageTax() {
		return sewerageTax;
	}

	public void setSewerageTax(boolean sewerageTax) {
		this.sewerageTax = sewerageTax;
	}

	public boolean isLightingTax() {
		return lightingTax;
	}

	public void setLightingTax(boolean lightingTax) {
		this.lightingTax = lightingTax;
	}

	public boolean isFireServTax() {
		return fireServTax;
	}

	public void setFireServTax(boolean fireServTax) {
		this.fireServTax = fireServTax;
	}

	public boolean isBigResdBldgTax() {
		return bigResdBldgTax;
	}

	public void setBigResdBldgTax(boolean bigResdBldgTax) {
		this.bigResdBldgTax = bigResdBldgTax;
	}

	public boolean isEducationCess() {
		return educationCess;
	}

	public void setEducationCess(boolean educationCess) {
		this.educationCess = educationCess;
	}

	public boolean isEmpGuaCess() {
		return empGuaCess;
	}

	public void setEmpGuaCess(boolean empGuaCess) {
		this.empGuaCess = empGuaCess;
	}

	public String getGenWaterRate() {
		return genWaterRate;
	}

	public void setGenWaterRate(String genWaterRate) {
		this.genWaterRate = genWaterRate;
	}

	public BasicProperty getBasicProp() {
		return basicProp;
	}

	public void setBasicProp(BasicProperty basicProp) {
		this.basicProp = basicProp;
	}

	public Map<String, String> getWaterMeterMap() {
		return waterMeterMap;
	}

	public void setWaterMeterMap(Map<String, String> waterMeterMap) {
		this.waterMeterMap = waterMeterMap;
	}

	/**
	 * This implementation transitions the <code>PropertyImpl</code> to the next
	 * workflow state.
	 */
/*	@Override
	protected PropertyImpl property() {
		return property;
	}*/

	public PropertyService getPropService() {
		return propService;
	}

	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}

	public Map<String, String> getAmenitiesMap() {
		return amenitiesMap;
	}

	public void setAmenitiesMap(Map<String, String> amenitiesMap) {
		this.amenitiesMap = amenitiesMap;
	}

	public Integer getMutationId() {
		return mutationId;
	}

	public void setMutationId(Integer mutationId) {
		this.mutationId = mutationId;
	}

	public String getParentIndex() {
		return parentIndex;
	}

	public void setParentIndex(String parentIndex) {
		this.parentIndex = parentIndex;
	}

	public String getIsAuthProp() {
		return isAuthProp;
	}

	public void setIsAuthProp(String isAuthProp) {
		this.isAuthProp = isAuthProp;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public String[] getFloorNoStr() {
		return floorNoStr;
	}

	public void setFloorNoStr(String[] floorNoStr) {
		this.floorNoStr = floorNoStr;
	}

	public String getPropTypeId() {
		return propTypeId;
	}

	public void setPropTypeId(String propTypeId) {
		this.propTypeId = propTypeId;
	}

	public String getPropUsageId() {
		return propUsageId;
	}

	public void setPropUsageId(String propUsageId) {
		this.propUsageId = propUsageId;
	}

	public String getPropOccId() {
		return propOccId;
	}

	public void setPropOccId(String propOccId) {
		this.propOccId = propOccId;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	public Address getCorrAddress() {
		return (Address) property.getPropertyOwnerProxy().get(0).getAddress().iterator().next();
	}

	public Map<Long, String> getZoneBndryMap() {
		return ZoneBndryMap;
	}

	public void setZoneBndryMap(Map<Long, String> ZoneBndryMap) {
		this.ZoneBndryMap = ZoneBndryMap;
	}

	@SuppressWarnings("unchecked")
	public List<PropertyOwner> getPropOwnerProxy() {
		Collections.sort(propOwnerProxy, new OwnerNameComparator());
		return propOwnerProxy;
	}

	public void setPropOwnerProxy(List<PropertyOwner> propOwnerProxy) {
		this.propOwnerProxy = propOwnerProxy;
	}

	public Map<String, String> getPropTypeCategoryMap() {
		return propTypeCategoryMap;
	}

	public void setPropTypeCategoryMap(Map<String, String> propTypeCategoryMap) {
		this.propTypeCategoryMap = propTypeCategoryMap;
	}

	public String getPropTypeCategoryId() {
		return propTypeCategoryId;
	}

	public void setPropTypeCategoryId(String propTypeCategoryId) {
		this.propTypeCategoryId = propTypeCategoryId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getKhasraNumber() {
		return khasraNumber;
	}

	public String getMauza() {
		return mauza;
	}

	public String getCitySurveyNumber() {
		return citySurveyNumber;
	}

	public String getSheetNumber() {
		return sheetNumber;
	}

	public void setKhasraNumber(String khasraNumber) {
		this.khasraNumber = khasraNumber;
	}

	public void setMauza(String mauza) {
		this.mauza = mauza;
	}

	public void setCitySurveyNumber(String citySurveyNumber) {
		this.citySurveyNumber = citySurveyNumber;
	}

	public void setSheetNumber(String sheetNumber) {
		this.sheetNumber = sheetNumber;
	}

	public PropertyTypeMaster getPropTypeMstr() {
		return propTypeMstr;
	}

	public void setPropTypeMstr(PropertyTypeMaster propTypeMstr) {
		this.propTypeMstr = propTypeMstr;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public Category getPropertyCategory() {
		return propertyCategory;
	}

	public void setPropertyCategory(Category propertyCategory) {
		this.propertyCategory = propertyCategory;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getNonResPlotArea() {
		return nonResPlotArea;
	}

	public void setNonResPlotArea(String nonResPlotArea) {
		this.nonResPlotArea = nonResPlotArea;
	}

	public boolean isIsfloorDetailsRequired() {
		return isfloorDetailsRequired;
	}

	public void setIsfloorDetailsRequired(boolean isfloorDetailsRequired) {
		this.isfloorDetailsRequired = isfloorDetailsRequired;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public List<PropertyOwner> getPropertyOwnerProxy() {
		return propertyOwnerProxy;
	}

	public void setPropertyOwnerProxy(List<PropertyOwner> propertyOwnerProxy) {
		this.propertyOwnerProxy = propertyOwnerProxy;
	}

	public PropertyImpl getNewProperty() {
		return newProperty;
	}

	public void setNewProperty(PropertyImpl newProperty) {
		this.newProperty = newProperty;
	}

}
