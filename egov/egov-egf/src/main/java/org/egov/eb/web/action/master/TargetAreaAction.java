package org.egov.eb.web.action.master;

import org.apache.struts2.convention.annotation.Action;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.eb.domain.master.entity.EBDetails;
import org.egov.eb.domain.master.entity.TargetArea;
import org.egov.eb.domain.master.entity.TargetAreaMappings;
import org.egov.eb.service.master.TargetAreaService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmployeeView;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

public class TargetAreaAction extends BaseFormAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3995689918654261397L;
	private static final Logger LOGGER = Logger.getLogger(TargetAreaAction.class);
	private TargetArea targetArea = new TargetArea();
	private EBDetails eBDetails = new EBDetails();
	private TargetAreaMappings targetMappings = new TargetAreaMappings();
	private TargetAreaService targetAreaService;
	private String mode;
	private boolean isDuplicate;
	private boolean isModify = true;

	public List<TargetAreaMappings> targetAreaMappingsResultList = new ArrayList<TargetAreaMappings>();
	public List<TargetAreaMappings> targetAreaMappingsList = new ArrayList<TargetAreaMappings>();
	public List<TargetArea> targetAreaList = new ArrayList<TargetArea>();

	
	public void prepareNewForm() {
		super.prepare();
		addDropdownData("positionsList",persistenceService.findAllBy("from EmployeeView where position.id in(select id from Position where " +
				" desigId in(select designationId from DesignationMaster where designationName='ASSISTANT ENGINEER') and deptDesigId " +
				"in(select id from DeptDesig where deptId in(select id from Department where deptName='L-Electrical')))  " +
				" order by employeeName"));
	
	     addDropdownData("wardList",persistenceService.findAllBy("from Boundary where boundaryType.id in(select id from BoundaryType where name='Ward') " +
				"and id not in (select boundary.id from TargetAreaMappings) order by name"));  
	}

	@Override
	public Object getModel() {
		return targetArea;
	}

	public TargetAreaAction() {
		addRelatedEntity("position", Position.class);
		addRelatedEntity("targetAreaMappingsResultList.boundary", Boundary.class);
	}
	
	public void prepareBeforeSearch() {
		addDropdownData("positionsList",persistenceService.findAllBy("from EmployeeView where position.id in(select id from Position where " +
				" desigId in(select designationId from DesignationMaster where designationName='ASSISTANT ENGINEER') and deptDesigId " +
				" in(select id from DeptDesig where deptId in(select id from Department where deptName='L-Electrical'))) " +
				" order by employeeName"));
		
		addDropdownData("wardList",persistenceService.findAllBy("from Boundary " +
				" where boundaryType.id in(select id from BoundaryType where name='Ward') ORDER BY NAME"));
	}
	
	@SkipValidation 
@Action(value="/master/targetArea-newForm")
	public String newForm() {
		return FinancialConstants.STRUTS_RESULT_PAGE_NEW;
	}
	
	@SkipValidation
@Action(value="/master/targetArea-beforeSearch")
	public String beforeSearch() {
		if(LOGGER.isInfoEnabled())     LOGGER.info("TargetArea Mode="+mode);
		return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
	}
	
	@SuppressWarnings("unchecked")
	@SkipValidation
@Action(value="/master/targetArea-beforeEdit")
	public String beforeEdit() {
	 	if (LOGGER.isDebugEnabled())			LOGGER.debug("..Inside Before Edit Method..");
		List<Boundary> wards = new ArrayList<Boundary>();
		wards = persistenceService.findAllBy("from Boundary where boundaryType.id in(select id from BoundaryType where name='Ward') " +
				" and id not in (select boundary.id from TargetAreaMappings) ORDER BY NAME");
		//this.targetArea = targetAreaService.findByNamedQuery("TARGETAREABYID", this.targetArea.getId());
		//This fix is for Phoenix Migration.
		this.targetAreaMappingsList.addAll(targetArea.getTargetAreaMappings());
		for (TargetAreaMappings mapping : targetAreaMappingsList) {
			wards.add(mapping.getBoundary());
			eBDetails = (EBDetails) persistenceService.find("from EBDetails where ebConsumer.ward.id = ? and status.code != 'CANCELLED'", mapping.getBoundary().getId());
			if (eBDetails != null) {
				isModify = false;
			}
		}
		addDropdownData("positionsList", getNonMappedPositions());
		addDropdownData("wardList", wards);
		return FinancialConstants.STRUTS_RESULT_PAGE_EDIT;
	}
	
	@SuppressWarnings("unchecked")
	private List<EmployeeView> getNonMappedPositions() {
		List<EmployeeView> positions = null;
		EmployeeView employeeView;
		positions = persistenceService
				.findAllBy("from EmployeeView where position.id in(select id from Position where desigId in"
						+ "(select designationId from DesignationMaster where designationName='ASSISTANT ENGINEER') and deptDesigId in(select id from DeptDesig where"
						+ " deptId in(select id from Department where deptName='L-Electrical'))) and position.id not in(select position.id from TargetArea)  "
						+ " order by employeeName");
		employeeView = (EmployeeView) persistenceService.find("from EmployeeView where position = ? ",
				this.targetArea.getPosition());
		positions.add(employeeView);
		return positions;

	}
	
	@SkipValidation
@Action(value="/master/targetArea-beforeView")
	public String beforeView(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("..Inside Before View Method..");
		//this.targetArea = targetAreaService.findByNamedQuery("TARGETAREABYID", this.targetArea.getId());
		//This fix is for Phoenix Migration.
		this.targetAreaMappingsList.addAll(targetArea.getTargetAreaMappings());
		return FinancialConstants.STRUTS_RESULT_PAGE_VIEW;
	}	
	
	@SuppressWarnings("unchecked")
	@ValidationErrorPage(EDIT) 
	public String edit() {
		
		TargetArea targetAreaFromDB = null;//targetAreaService.findByNamedQuery("TARGETAREABYID", this.targetArea.getId());
		//This fix is for Phoenix Migration.
		Set<TargetAreaMappings> oldMappings = targetAreaFromDB.getTargetAreaMappings();
		Set<TargetAreaMappings> newMappings = new HashSet<TargetAreaMappings>(targetAreaMappingsResultList);	
		
		Set<TargetAreaMappings> oldMappingsToBeDeleted = new HashSet<TargetAreaMappings>(oldMappings);
		oldMappingsToBeDeleted.removeAll(newMappings);
		
		// adding selected wards to the list to show on UI
		for (TargetAreaMappings mappings : newMappings) {
			if (mappings != null && mappings.getBoundary().getId() != 0) {
				targetAreaMappingsList.add(mappings);
			}
		}

		Set<TargetAreaMappings> completelyNewMappings = new HashSet<TargetAreaMappings>(newMappings);
		completelyNewMappings.removeAll(oldMappings);

		// removing the old mappings from targetArea

		targetAreaFromDB.getTargetAreaMappings().removeAll(
				oldMappingsToBeDeleted);

		for (TargetAreaMappings oldMapping : oldMappingsToBeDeleted) {
			persistenceService.setType(TargetAreaMappings.class);
			persistenceService.delete(oldMapping);
		}
		
		targetAreaFromDB.setCode(this.targetArea.getCode());
		targetAreaFromDB.setName(this.targetArea.getName());
		targetAreaFromDB.setPosition(this.targetArea.getPosition());
		
		// adding the completely new mappings
		Set<TargetAreaMappings> MappingsToBeSaved = new HashSet<TargetAreaMappings>();
		for (TargetAreaMappings completeNewMapping : completelyNewMappings) {
			if (completeNewMapping != null && completeNewMapping.getBoundary() != null
					&& completeNewMapping.getBoundary().getId() != 0) {
				completeNewMapping.setArea(targetAreaFromDB);
				MappingsToBeSaved.add(completeNewMapping);
			}
		}

		targetAreaFromDB.getTargetAreaMappings().addAll(MappingsToBeSaved);
		//targetAreaService.update(targetAreaFromDB);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug(".................................Target Area Modified Successfully......................");

		prepareBeforeSearch();
		addActionMessage(getText("Target Area Modified Successfully"));
		mode = "edit";

		return FinancialConstants.STRUTS_RESULT_PAGE_RESULT;
	}
	
	@Validations(
			requiredFields = { 
					@RequiredFieldValidator(fieldName = "model.position.id", message = "", key = FinancialConstants.REQUIRED),
					@RequiredFieldValidator(fieldName = "model.code", message = "", key = FinancialConstants.REQUIRED),
					@RequiredFieldValidator(fieldName = "model.name", message = "", key = FinancialConstants.REQUIRED)
		 })
	
	@ValidationErrorPage(FinancialConstants.STRUTS_RESULT_PAGE_RESULT) 
@Action(value="/master/targetArea-create")
	public String create() {
		TargetArea duplicateTargetArea = null;
		targetArea.getTargetAreaMappings().clear();
		for (TargetAreaMappings mapping : targetAreaMappingsResultList) {
			if (mapping != null && mapping.getBoundary() != null && mapping.getBoundary().getId() != 0 ) {
				mapping.setArea(targetArea);
				targetArea.getTargetAreaMappings().add(mapping);
				targetAreaMappingsList.add(mapping); 
			} 
		}
		targetArea.setIsActive(true);
		duplicateTargetArea = (TargetArea) getPersistenceService().find("from TargetArea where upper(code) = ?",
				targetArea.getCode().toUpperCase());
		if (duplicateTargetArea != null) {
			addActionMessage("Target area Already Exists");
		} else {
			/*targetAreaService.persist(targetArea);
			HibernateUtil.getCurrentSession().flush();*/
			addActionMessage("Target area created successfully");
			
				LOGGER.error(".................................Target Area created Successfully......................"); 
		}
		return FinancialConstants.STRUTS_RESULT_PAGE_RESULT;       	    			 																																																																																																																																																																																																																																																																																														
	}  
	
	@SkipValidation
@Action(value="/master/targetArea-codeUniqueCheck")
	public String codeUniqueCheck() {
		if (LOGGER.isInfoEnabled())
			LOGGER.info("......Target Area Unique check for code......");

		if (targetAreaService.isCodeUnique(targetArea.getCode(), targetArea.getId())) {
			isDuplicate = false;
		} else {
			isDuplicate = true;
		}

		return FinancialConstants.STRUTS_RESULT_PAGE_UNIQUECHECK;
	}
		
	
	@SkipValidation
@Action(value="/master/targetArea-ajaxSearch")
	public String ajaxSearch(){
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Inside getData |Search TargetArea Action Starts");
		StringBuffer query = new StringBuffer();
		query.append("From TargetArea targetArea where targetArea.id is not null ");
		if ((targetArea.getCode() != null && !targetArea.getCode().equals(""))) {
			query.append(" and targetArea.code= '" + targetArea.getCode() + "'");
		}

		if ((targetArea.getName() != null && !targetArea.getName().equals(""))) {
			query.append(" and targetArea.name= '" + targetArea.getName() + "'");
		}

		query.append(" order by targetArea.code");
		//targetAreaList = targetAreaService.findAllBy(query.toString());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("TargetArea List Size is"+targetAreaList.size());
		return "results";
	}

	private User getLoggedInUser() {
		return null;//This fix is for Phoenix Migration.(User) persistenceService.getSession().load(User.class, Integer.valueOf(EGOVThreadLocals.getUserId()));
	}

	public TargetAreaService getTargetAreaService() {
		return targetAreaService;
	}

	public void setTargetAreaService(TargetAreaService targetAreaService) {
		this.targetAreaService = targetAreaService;
	}

	public TargetArea getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(TargetArea targetArea) {
		this.targetArea = targetArea;
	}

	public TargetAreaMappings getTargetMappings() {
		return targetMappings;
	}

	public void setTargetMappings(TargetAreaMappings targetMappings) {
		this.targetMappings = targetMappings;
	}

	public List<TargetAreaMappings> getTargetAreaMappingsResultList() {
		return targetAreaMappingsResultList;
	}

	public void setTargetAreaMappingsResultList(List<TargetAreaMappings> targetAreaMappingsResultList) {
		this.targetAreaMappingsResultList = targetAreaMappingsResultList;
	}

	public String getMode() { 
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<TargetAreaMappings> getTargetAreaMappingsList() {
		return targetAreaMappingsList;
	}

	public void setTargetAreaMappingsList(List<TargetAreaMappings> targetAreaMappingsList) {
		this.targetAreaMappingsList = targetAreaMappingsList;
	}

	public boolean getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public List<TargetArea> getTargetAreaList() {
		return targetAreaList;
	}

	public void setTargetAreaList(List<TargetArea> targetAreaList) {
		this.targetAreaList = targetAreaList;
	}
	
	public boolean getIsModify() {
		return isModify;
	}

	public void setIsModify(boolean isModify) {
		this.isModify = isModify;
	}
	

}
