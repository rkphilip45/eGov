package org.egov.assets.web.actions.assetmaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.assets.model.Asset;
import org.egov.assets.model.AssetActivities;
import org.egov.assets.model.AssetCategory;
import org.egov.assets.model.AssetDepreciation;
import org.egov.assets.model.AssetType;
import org.egov.assets.service.AppService;
import org.egov.assets.service.AssetActivitiesService;
import org.egov.assets.service.AssetDepreciationService;
import org.egov.assets.service.AssetService;
import org.egov.assets.util.AssetConstants;
import org.egov.assets.util.AssetIdentifier;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.voucher.VoucherDetails;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

import com.opensymphony.xwork2.Action;

@ParentPackage("egov")
@Result(name = Action.SUCCESS, type = "redirect", location = "asset.action")
public class AssetAction extends SearchFormAction{
	public static final  String SEARCH="search";
	public static final  String SEARCH_PLUGIN="searchplugin";
	public static final  String CREATE_PLUGIN="create";
	public static final  String VIEW="view";
	
	private static final Logger LOGGER = Logger.getLogger(AssetAction.class);
	private AssetService assetService;
	private AssetActivitiesService assetActivitiesService;
	private AssetDepreciationService assetDepreciationService;
	private AppService appService;
	private Asset asset = new Asset();
	private List<Asset> assetList = null;
	private Long id;
	private static final String LOCATION_HIERARCHY_TYPE = "LOCATION";
	private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
	
	private static final String AREA_BOUNDARY_TYPE = "Area";
	private static final String LOACTION_BOUNDARY_TYPE = "Locality";
	private static final String WARD_BOUNDARY_TYPE = "Ward";
	private static final String Zone_BOUNDARY_TYPE = "Zone";
	private static final String Asset_SAVE_SUCCESS ="asset.save.success";
	private static final String WardList="wardList";
	private static final String StatusList="statusList";
	private static final String Unable_To_Load_Heirarchy_Information="Unable to load Heirarchy information";
	private static final String Error_While_Loading_HeirarchyType="Error while loading HeirarchyType - HeirarchyType.";
	// UI fields
	private String userMode;
	private boolean fDisabled;
	private boolean sDisabled;
	private String dataDisplayStyle;
	private Integer rowId;
	
	//asset search page
	private Long parentId;
	private Long catTypeId;
	private Integer departmentId;	
	private List<Integer> statusId;
	private List<String> assetStatus;
	//selectedstatusId
	private String code;
	private String description;
	private Integer locationId; 
	private Integer zoneId; 
	private Integer areaId;
	private Integer streetId;
	private Integer street2Id;
	private Integer wardId;
    
	private String selectType;
	private String xmlconfigname;
	private String isAutoGeneratedCode;
	
	private String categoryname;
	private String category;
	private String searchBy;

	private String messageKey;
	private List<Long> assetChildCategoryList= new LinkedList<Long>();
	Query query=null;
	
	/**
	 * Default Constructor
	 */
	public AssetAction(){
		addRelatedEntity("assetType", AssetType.class);
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("assetCategory", AssetCategory.class);
		addRelatedEntity("area", BoundaryImpl.class);
		addRelatedEntity("location", BoundaryImpl.class);
		addRelatedEntity("street", BoundaryImpl.class);
		addRelatedEntity("street2", BoundaryImpl.class);
		addRelatedEntity("ward", BoundaryImpl.class);
		addRelatedEntity("zone", BoundaryImpl.class);
		addRelatedEntity("status", EgwStatus.class,"description");
	}
	
	@Override
	public void prepare() {
		isAutoGeneratedCode = appService.getUniqueAppConfigValue("IS_ASSET_CODE_AUTOGENERATED");
		if (id != null && id != -1) {
			 asset = assetService.findById(id, false);
			 AssetActivities activities = assetActivitiesService.find(" from AssetActivities where asset.id=?",asset.getId());
			 	if(activities!=null)
			 		asset.setGrossValue(activities.getAdditionAmount());
		 		asset.setAccDepreciation(assetService.getDepreciationAmt(asset));
	    }
		super.prepare();
		setupDropdownDataExcluding("area","location","street","street2","ward","zone","status");	
		
		// Fetch HeirarchyType
		HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(LOCATION_HIERARCHY_TYPE);
		}catch(EGOVException e){
			LOGGER.error(Error_While_Loading_HeirarchyType + e.getMessage());
			addFieldError("Heirarchy", Unable_To_Load_Heirarchy_Information);
			throw new EGOVRuntimeException(Unable_To_Load_Heirarchy_Information,e);
		}
		
		/**
		 * Fetch Area Dropdown List
		 */
		List<Boundary> areaList =new ArrayList<Boundary>();
		if(asset.getArea()!=null || (areaId!=null && areaId!=-1)){
			BoundaryType bType = new BoundaryTypeDAO().getBoundaryType(AREA_BOUNDARY_TYPE, hType);
			areaList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		}
		addDropdownData("areaList", areaList);
		
		
		 /**
		 *  Fetch Location Dropdown List
		 */
		List<Boundary> locationList = new ArrayList<Boundary>();
		try{
			if(asset.getArea()!=null){
				locationList = new BoundaryDAO().getChildBoundaries(String.valueOf(asset.getArea().getId()));
			}
			if(areaId!=null){
				locationList = new BoundaryDAO().getChildBoundaries(String.valueOf(areaId));
			}
		}catch(Exception e){}
		addDropdownData("locationList", locationList);

		 /**
		 *  Fetch Ward Dropdown List
		 */
		if(zoneId==null){
			List<Boundary> wardList = new ArrayList<Boundary>();
			addDropdownData(WardList, wardList);
		}else{
			List<Boundary> wardList = null;
			try {
				wardList = new BoundaryDAO().getChildBoundaries(String.valueOf(zoneId));
			} catch (Exception e){
				LOGGER.error("Error while loading wards - wards." + e.getMessage());
		}
		addDropdownData(WardList,wardList);
		}
		
		
		
		 /**
		 *  Fetch Ward Dropdown List
		 */
		 addDropdownData("wardsList",getAllWard());
		
		
		/**
		 *  Fetch Street Dropdown List
		 */
		 List<Boundary> streetList = new ArrayList<Boundary>();
		if(wardId!=null){
			 BoundaryType childBoundaryType = new BoundaryTypeDAO().getBoundaryType("Street", hType);
			 Boundary parentBoundary = new BoundaryDAO().getBoundaryById(wardId);
			 streetList = new LinkedList(new BoundaryDAO().getCrossHeirarchyChildren(parentBoundary, childBoundaryType));
		 }
		 if(asset.getWard()!=null){
			 BoundaryType childBoundaryType = new BoundaryTypeDAO().getBoundaryType("Street", hType);
			 Boundary parentBoundary = new BoundaryDAO().getBoundaryById(asset.getWard().getId());
			 //streetList = new LinkedList(new BoundaryDAO().getCrossHeirarchyChildren(parentBoundary, childBoundaryType));
		 }
		 addDropdownData("streetList", streetList);
		 
		/**
		 *  Fetch Street Dropdown List
		 */
		 if(locationId==null){
			 List<Boundary> street2List = new ArrayList<Boundary>();
			 addDropdownData("street2List", street2List);
		 }else{
			 List<Boundary> street2List = null;
			 try {
				 street2List = new BoundaryDAO().getChildBoundaries(String.valueOf(locationId));	
			 } catch (Exception e){
				 LOGGER.error("Error while loading wards - wards." + e.getMessage());
			 }
			 addDropdownData("street2List", street2List);
		}
		
		/**
		 *   Fetch Acquisition Mode Dropdown List
		 */
		List<AppConfigValues> configList = appService.getAppConfigValue("Assets", "MODE_OF_ACQUISITION");
		addDropdownData("acquisitionModeList", configList);
		
		/**
		 *  Fetch Status Dropdown List
		 */
		String query = "from EgwStatus st where st.moduletype='ASSET' order by description";
		List<EgwStatus> status  = (List<EgwStatus>) persistenceService.findAllBy(query);
		addDropdownData(StatusList, status);
		
		/**
		 *  Fetch Zone Dropdown List
		 */
		addDropdownData("zoneList", getAllZone());
		if(searchBy==null)
			searchBy="1";
	} 

	
	/**
	 * This method is invoked to create a new form.
	 * 
	 * @return a <code>String</code> representing the value 'NEW'
	 */
	public String newform(){ 
		userMode = NEW;
		return showform();  
	}
	
	public String showform()
	{  
		LOGGER.info("****User Mode: " + userMode);
		String result=null;
		
		if(userMode==null)
		{
			userMode=NEW;
		}
		if(NEW.equals(userMode))
		{
			fDisabled = false;
			sDisabled = false;
			result = NEW;
		}else if(VIEW.equals(userMode))
		{
			if(id == null){
				addActionError(getText("asset.category.id.null"));			
				result = SEARCH;
			}
			else{
				fDisabled = true;
				sDisabled = true;
				setLocationDetails(asset);
				result = NEW;
			}
		}else if(EDIT.equals(userMode))
		{
			if(id == null){
				addActionError(getText("asset.id.null"));			
				result = SEARCH;
			}
			else{
				fDisabled = false;
				sDisabled = true;
				setCategory(category);
				setLocationDetails(asset);
				result = NEW;
			}
		}
			
		return result;  
	}
   	private void setLocationDetails(Asset asset){
   		
   		
		if(asset.getArea()!=null){
			List<Boundary> locationList = new LinkedList<Boundary>();
			try{	
				locationList = new BoundaryDAO().getChildBoundaries(String.valueOf(asset.getArea().getId()));
			}catch(Exception e){
				LOGGER.error("Error while loading locations - locations." + e.getMessage());
				addFieldError("location", "Unable to load location information");
				throw new EGOVRuntimeException("Unable to load location information",e);
			}
			addDropdownData("locationList", locationList);
		}
		if(asset.getWard()!=null){
			AjaxAssetAction ajaxAssetAction=new AjaxAssetAction();
			/*List<Boundary> streetList = null;
			Boundary parentBoundary = new BoundaryDAO().getBoundaryById(asset.getLocation().getId());
		    streetList =new LinkedList<Boundary>(parentBoundary.getChildren());*/
			ajaxAssetAction.setWardId(asset.getWard().getId());
			try{
				try{
					ajaxAssetAction.populateStreets();
				}catch(EGOVRuntimeException noStreetExp){
					dropdownData.put("streetList", Collections.EMPTY_LIST);
				}
				addDropdownData("streetList",ajaxAssetAction.getStreetList());
			}
			catch(Exception e){
				LOGGER.error("Error while loading Streets." + e.getMessage());
				addFieldError("streets", "Unable to load Streets Information");
				throw new EGOVRuntimeException("Unable to load Streets information",e);
			}
		}
		if(asset.getWard()!=null){
			setZoneId(asset.getWard().getParent().getId());
			List<Boundary> wardList = null;
			try {
				wardList = new BoundaryDAO().getChildBoundaries(String.valueOf(zoneId));
			} catch (Exception e){
				LOGGER.error("Error while loading wards - wards." + e.getMessage());
				wardList=Collections.EMPTY_LIST;
				
			}
			addDropdownData(WardList,wardList);
		}
	}
	
	public List<Boundary> getAllLocation() {
		HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(LOCATION_HIERARCHY_TYPE);
		}catch(EGOVException e){
			LOGGER.error(Error_While_Loading_HeirarchyType + e.getMessage());
			throw new EGOVRuntimeException(Unable_To_Load_Heirarchy_Information,e);
		}
		List<Boundary> locationList = null;
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType(LOACTION_BOUNDARY_TYPE, hType);
		locationList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		return locationList;
	}

	public List<Boundary> getAllWard() {
	    HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
		}catch(EGOVException e){
			LOGGER.error(Error_While_Loading_HeirarchyType + e.getMessage());
			throw new EGOVRuntimeException(Unable_To_Load_Heirarchy_Information,e);
		}
		List<Boundary> wardList = null;
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType(WARD_BOUNDARY_TYPE, hType);
		wardList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		return wardList;
	}
	
	public List<Boundary> getAllZone() {
	    HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
		}catch(EGOVException e){
			LOGGER.error(Error_While_Loading_HeirarchyType+ e.getMessage());
			throw new EGOVRuntimeException(Unable_To_Load_Heirarchy_Information,e);
		}
		List<Boundary> zoneList = null;
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType(Zone_BOUNDARY_TYPE,hType);
		zoneList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		return zoneList;
	}
	
	public String edit()
	{  
		userMode = EDIT;
		return SEARCH;  
	} 
	
	public String view()
	{  
		userMode = VIEW;
		return SEARCH;
	} 
	
	/**
	 * Search Page for Assets view and edit screen
	 * @throws Exception 
	 */
	public String list() throws Exception 
	{  
		setXmlconfigname(xmlconfigname);
	    setCategoryname(categoryname);
	    setCatTypeId(catTypeId);
		if((parentId == null  || parentId == -1)
				&& departmentId==null 
				&& locationId==null
				&& catTypeId == null
				&& (code==null || code.trim().equalsIgnoreCase("")) 
				&& (description==null || description.trim().equalsIgnoreCase("")) 
				&& (statusId==null || statusId.isEmpty()) && zoneId==-1){		
			messageKey = "message.mandatory";
			addActionError(getText(messageKey, "At least one selection is required"));			
			return SEARCH;
		}
		setPageSize(AssetConstants.PAGE_SIZE);
		search();
		return SEARCH;  
	}
	
	private List<Asset> searchAssets() throws Exception{
		return assetService.findAllBy(getQuery());
	}
	
	private String getAllChilds(){
	 StringBuffer assetCatIdStr = new StringBuffer(100);
	 query = HibernateUtil.getCurrentSession().getNamedQuery("ParentChildCategories");
     query.setParameter("assetcatId",parentId);
     assetChildCategoryList=query.list();
     
     for(int i=0,len=assetChildCategoryList.size(); i<len;i++){
    	 assetCatIdStr.append(assetChildCategoryList.get(i).toString());
		 if(i<len-1)
			 assetCatIdStr.append(',');
	}
     return assetCatIdStr.toString();
	}
	/**
	 * Method to setup request parameter received from other modules
	 */
	private void setupRequestData(){
		getSession().put(StatusList, getStatusList(assetStatus));
		setStatusList();
	}
	
	/**
	 * Get the list of <code>EgwStatus</code> related to ASSET module.
	 * @param statusDescList - List of status descriptions
	 * @return
	 */
	private List<EgwStatus> getStatusList(List<String> statusDescList){
		List<EgwStatus> lStatusList = null;
		if(statusDescList!=null && !statusDescList.isEmpty()){
			StringBuffer sql = new StringBuffer(100);
			sql.append("from EgwStatus st where st.moduletype='ASSET'  and UPPER(st.description) in (");
			//sql.append(" and UPPER(st.description) in ("); 
			for(int i=0,len=statusDescList.size(); i<len;i++){
				 sql.append("'" + statusDescList.get(i).trim().toUpperCase() + "'");
				 if(i<len-1){
					 sql.append(',');
				 }
			 }			 
			 sql.append(") order by description");
			 String query = sql.toString();
			 lStatusList  = (List<EgwStatus>) persistenceService.findAllBy(query);
		}
		return lStatusList;
	}
	
	private void setStatusList(){
		List<EgwStatus> statusList = (List<EgwStatus>)getSession().get("statusList");
		if(statusList==null){
			statusList = new LinkedList<EgwStatus>();
		}
		addDropdownData(StatusList, statusList);
	}
	
	/**
	 * asset search plugin for other modules - works and stores
	 */
	public String showSearchPage(){
		setupRequestData();
		return SEARCH_PLUGIN;
	}
	
	
	public String showSerachResult() throws Exception{
		setStatusList();
		if(statusId!=null && !statusId.isEmpty())	
			assetList = searchAssets();
		else
			addFieldError("status", "Please select at least one status");

		return SEARCH_PLUGIN;
	}
	
	/**
	 * test page for search plugin - not in use
	 */
	public String showPlugin(){
		return "plugin";
	}
	
	/**
	 * asset create plugin for other modules - works and stores
	 */
	public String showCreatePage(){
		setupRequestData();
		return CREATE_PLUGIN;
	}
	
	/**
	 * create asset from other modules
	 */
	@ValidationErrorPage(value="create")
	public String create(){
		try {
			setStatusList();
			assetService.setAssetNumber(asset);
			assetService.persist(asset);
			addActionMessage( "\'" + asset.getCode() + "\' " + getText(Asset_SAVE_SUCCESS));
			id = asset.getId();
			// make to view mode
			fDisabled = true;
			sDisabled = true;
			setLocationDetails(asset);
			return CREATE_PLUGIN;
		} catch (ValidationException e) {
			 clearMessages();
			 prepare();
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
			 throw new ValidationException(errors);
		} 
		
	}
	
	

	public String save(){
		if(asset.getDateOfCreation()!=null)
			assetService.setAssetNumber(asset);
		assetService.persist(asset);
		
		if(NEW.equals(userMode) && asset.getStatus().getDescription().equalsIgnoreCase("Capitalized"))
		{
			AssetActivities activities = new AssetActivities();
			activities.setAsset(asset);
			activities.setActivityDate(asset.getDateOfCreation());
			activities.setIdentifier(AssetIdentifier.C);
			activities.setAdditionAmount(asset.getGrossValue());
			assetActivitiesService.persist(activities);
			
			AssetDepreciation depreciation = new AssetDepreciation();
			depreciation.setAsset(asset);
			depreciation.setFromDate(asset.getDateOfCreation());
			depreciation.setToDate(new Date());
			depreciation.setAmount(asset.getAccDepreciation());
			assetDepreciationService.persist(depreciation);
		}
		addActionMessage( "\'" + asset.getCode() + "\' " + getText(Asset_SAVE_SUCCESS));
		userMode = EDIT;
		id = asset.getId();
		// setLocationDetails(asset);
		return showform();
	}

	/**
	 * The default action method
	 */
	public String execute() { 
		return view();
	}
	
	// Property accessors
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Object getModel() {
		return asset;
	}

	public void setModel(Asset asset){
		this.asset = asset;
	}
	
	// Spring Injection
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
	
	// Spring Injection
	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	public String getUserMode() {
		return userMode;
	}

	public void setUserMode(String userMode) {
		this.userMode = userMode;
	}

	public boolean isFDisabled() {
		return fDisabled;
	}

	public boolean isSDisabled() {
		return sDisabled;
	}

	public List<Asset> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<Asset> assetList) {
		this.assetList = assetList;
	}

	public String getDataDisplayStyle() {
		return dataDisplayStyle;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the catTypeId
	 */
	public Long getCatTypeId() {
		return catTypeId;
	}

	/**
	 * @param catTypeId the catTypeId to set
	 */
	public void setCatTypeId(Long catTypeId) {
		this.catTypeId = catTypeId;
	}

	/**
	 * @return the departmentId
	 */
	public Integer getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the wardId to set for search
	 */
	public void setLocationId(Integer wardId) {
		this.locationId = wardId;
	}

	/**
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * @param messageKey the messageKey to set
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @return the statusId
	 */
	public List<Integer> getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(List<Integer> statusId) {
		this.statusId = statusId;
	}

	public void setAssetStatus(List<String> assetStatus) {
		this.assetStatus = assetStatus;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public List<Long> getAssetChildCategoryList() {
		return assetChildCategoryList;
	}

	
	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public String getXmlconfigname() {
		return xmlconfigname;
	}

	public void setXmlconfigname(String xmlconfigname) {
		this.xmlconfigname = xmlconfigname;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIsAutoGeneratedCode() {
		return isAutoGeneratedCode;
	}

	public void setIsAutoGeneratedCode(String isAutoGeneratedCode) {
		this.isAutoGeneratedCode = isAutoGeneratedCode;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String query = getQuery();
		return new SearchQueryHQL(query, "select count(*) " + query, null);
	}

	/**
	 * @return query in string format
	 */
	private String getQuery() {
		StringBuilder sql = new StringBuilder(265);
		sql.append("from Asset asset where asset.code is not null ");
		if(parentId!=null && Integer.parseInt(parentId.toString())!=-1)
			sql.append(" and asset.assetCategory.id in ("+ getAllChilds()+  ")");
		if(catTypeId!=null)
			sql.append(" and asset.assetCategory.assetType.id = "+catTypeId);
		if(departmentId!=null && departmentId!=-1)
			sql.append(" and asset.department.id = "+departmentId);	
		if(zoneId!=null && zoneId !=-1 )
			sql.append(" and asset.ward.parent.id = "+zoneId);
		if(wardId!=null && wardId!=-1)
			sql.append(" and asset.ward.id = "+wardId);
		if(streetId!=null && streetId!=-1)
			sql.append(" and asset.street.id = "+streetId);
		if(areaId!=null && areaId!=-1)
			sql.append(" and asset.area.id = "+areaId);
		if(locationId!=null && locationId!=-1)
			sql.append(" and asset.location.id = "+locationId);
		if(street2Id!=null && street2Id!=-1)
			sql.append(" and asset.street.id = "+street2Id);
		if(code!=null && !code.trim().equalsIgnoreCase(""))
			sql.append(" and UPPER(asset.code) like '%" + code.toUpperCase()+ "%'");
		if(description!=null && !description.trim().equalsIgnoreCase(""))
			sql.append(" and UPPER(asset.description) like '%" + description.toUpperCase()+ "%'");
		
		if(statusId!=null && !statusId.isEmpty()){
			sql.append(" and asset.status.id in ("); 
			 for(int i=0,len=statusId.size(); i<len;i++){
				 sql.append(statusId.get(i));
				 if(i<len-1){
					 sql.append(',');
				 }
			 }			 
			 sql.append(')');		
		}
		return sql.toString();
	}
	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getStreetId() {
		return streetId;
	}

	public void setStreetId(Integer streetId) {
		this.streetId = streetId;
	}

	public Integer getStreet2Id() {
		return street2Id;
	}

	public void setStreet2Id(Integer street2Id) {
		this.street2Id = street2Id;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public void setAssetActivitiesService(AssetActivitiesService assetActivitiesService) {
		this.assetActivitiesService = assetActivitiesService;
	}

	public void setAssetDepreciationService(AssetDepreciationService assetDepreciationService) {
		this.assetDepreciationService = assetDepreciationService;
	}
}

