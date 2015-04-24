package org.egov.asset.web.action.assetcategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetType;
import org.egov.asset.model.DepreciationMetaData;
import org.egov.asset.model.DepreciationMethod;
import org.egov.asset.service.AppService;
import org.egov.asset.service.AssetCategoryService;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.web.actions.BaseFormAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;


@ParentPackage("egov")
@Result(name=Action.SUCCESS, type="ServletRedirectResult.class", location="assetCategory.action")
public class AssetCategoryAction extends BaseFormAction{
	
	public static final  String SEARCH="search";
	public static final  String VIEW="view";
	private static final Logger LOGGER = Logger.getLogger(AssetCategoryAction.class);
	private AssetCategoryService assetCategoryService;
	private AppService appService;
	private AssetCategory assetCategory = new AssetCategory();
	private List<AssetCategory> assetCategoryList = null;
	private List<DepreciationMetaData> depMetaDatas = new LinkedList<DepreciationMetaData>();
	private Long id;
	private Long catTypeId;
	private Long    parentId;
	
	//Purpose code keys
	private String assetAccCodePURPOSEID 			= "ASSET_ACCOUNT_CODE_PURPOSEID";
	private String revResAccPURPOSEID 				= "REVALUATION_RESERVE_ACCOUNT_PURPOSEID";
	private String depExpAccPURPOSEID 				= "DEPRECIATION_EXPENSE_ACCOUNT_PURPOSEID";
	private String accDepPURPOSEID 					= "ACCUMULATED_DEPRECIATION_PURPOSEID";
	
	// UI fields
	private String userMode;
	private boolean fDisabled;
	private boolean sDisabled;
	private String dataDisplayStyle;
	private String isAutoGeneratedCode;
	
	@Autowired
	private ChartOfAccountsDAO chartOfAccountsDAO;
	
	/**
	 * Default Constructor
	 */
	public AssetCategoryAction(){
		addRelatedEntity("assetCode", CChartOfAccounts.class);
		addRelatedEntity("accDepCode", CChartOfAccounts.class);
		addRelatedEntity("revCode", CChartOfAccounts.class);
		addRelatedEntity("depExpCode", CChartOfAccounts.class);
		addRelatedEntity("financialYear", CFinancialYear.class);
		addRelatedEntity("department", Department.class);
		addRelatedEntity("uom", EgUom.class);
	}
	
	@Override
	public void prepare() {
		String module_asset = "Assets";
		isAutoGeneratedCode = appService.getUniqueAppConfigValue("IS_ASSET_CATEGORYCODE_AUTOGENERATED");
		if (id != null && id != -1) 
			assetCategory = assetCategoryService.findById(id, false);
		super.prepare();
		
		setupDropdownDataExcluding("assetCode","accDepCode","revCode","depExpCode");
		addDropdownData("assetTypeList", Arrays.asList(AssetType.values()));
		addDropdownData("depreciationMethodList", Arrays.asList(DepreciationMethod.values()));
		try{
			String purposeId = appService.getUniqueAppConfigValue(module_asset, assetAccCodePURPOSEID);
			List<CChartOfAccounts> assetAccounts = chartOfAccountsDAO.getAccountCodeByPurpose(Integer.valueOf(purposeId));
			addDropdownData("assetCodeList", assetAccounts);
		}catch(EGOVException e){
			LOGGER.error("Error while loading dropdown data - assetCodeList." + e.getMessage());
			addFieldError("assetCodeList", "Unable to load asset account information");
			throw new EGOVRuntimeException("Unable to load asset account information",e);
		}
		
		try{
			String purposeId = appService.getUniqueAppConfigValue(module_asset, accDepPURPOSEID);
			List<CChartOfAccounts> accumulatedDeps = chartOfAccountsDAO.getAccountCodeByPurpose(Integer.valueOf(purposeId));
			addDropdownData("accDepCodeList", accumulatedDeps);
		}catch(EGOVException e){
			LOGGER.error("Error while loading dropdown data - accDepCodeList." + e.getMessage());
			addFieldError("accDepCodeList", "Unable to load accumulated depreciation information");
			throw new EGOVRuntimeException("Unable to load accumulated depreciation information",e);
		}
		
		try{
			String purposeId = appService.getUniqueAppConfigValue(module_asset, revResAccPURPOSEID);
			List<CChartOfAccounts> revAccounts = chartOfAccountsDAO.getAccountCodeByPurpose(Integer.valueOf(purposeId));
			addDropdownData("revCodeList", revAccounts);
		}catch(EGOVException e){
			LOGGER.error("Error while loading dropdown data - revCodeList." + e.getMessage());
			addFieldError("revCodeList", "Unable to load revaluation account information");
			throw new EGOVRuntimeException("Unable to load revaluation account information",e);
		}
		
				
		try{
			String purposeId = appService.getUniqueAppConfigValue(module_asset, depExpAccPURPOSEID);
			List<CChartOfAccounts> depExpenseAccounts = chartOfAccountsDAO.getAccountCodeByPurpose(Integer.valueOf(purposeId));
			addDropdownData("depExpCodeList", depExpenseAccounts);
		}catch(EGOVException e){
			LOGGER.error("Error while loading dropdown data - depExpCodeList." + e.getMessage());
			addFieldError("depExpCodeList", "Unable to load depreciation expense account information");
			throw new EGOVRuntimeException("Unable to load depreciation expense account information",e);
		}
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
				result = NEW;
			}
		}else if(EDIT.equals(userMode))
		{
			if(id == null){
				addActionError(getText("asset.category.id.null"));			
				result = SEARCH;
			}
			else{
				fDisabled = false;
				sDisabled = true;
				result = NEW;
			}
		}	
		return result;  
	} 
	
	public String edit()
	{  
		userMode = EDIT;
		dataDisplayStyle="none";
		return SEARCH;  
	} 
	
	public String view()
	{  
		userMode = VIEW;
		dataDisplayStyle="none";
		return SEARCH;  
	} 
	
	public String list() 
	{  
		if(catTypeId == -1 && (id == null || id == -1))
			assetCategoryList = assetCategoryService.findAllBy("from AssetCategory ac order by name asc");
		else if(catTypeId != -1 && (id == null || id == -1))
			assetCategoryList = assetCategoryService.findAllBy("from AssetCategory ac where ac.assetType.id=" 
									+catTypeId +	" order by name asc");
		else if (id != null && id != -1) {
			assetCategoryList = new ArrayList<AssetCategory>();
			assetCategoryList.add(assetCategoryService.findById(id, false));
		}
		if(assetCategoryList==null || assetCategoryList.isEmpty())
			dataDisplayStyle="noRecords";
		else
			dataDisplayStyle="display";
		
		return SEARCH;  
	} 
	
	/**
	 * The default action method
	 */
	public String execute() { 
		return list();
	}
	
	public String save() throws NumberFormatException, EGOVException{
		addDepMetaDatas();
		
		if(parentId != null && parentId != -1L)
		assetCategory.setParent(assetCategoryService.findById(parentId,false));
		assetCategoryService.setAssetCategoryNumber(assetCategory);
		try {
			assetCategory = assetCategoryService.persist(assetCategory);
		}catch (Exception valEx) {
			LOGGER.debug("Exception found:"+valEx.getMessage());
		}
		addActionMessage( "\'" + assetCategory.getCode() + "\' " + getText("asset.category.save.success"));
		userMode = EDIT;
		id = assetCategory.getId();
		return showform();
	}
	
	protected void addDepMetaDatas(){
		assetCategory.getDepreciationMetaDatas().clear();
		for(DepreciationMetaData lDepreciationMetaData: depMetaDatas) {
			 if (validDepMetaData(lDepreciationMetaData)) {
				 lDepreciationMetaData.setFinancialYear((CFinancialYear) getPersistenceService().find("from CFinancialYear where id = ?", lDepreciationMetaData.getFinancialYear().getId()));
				 lDepreciationMetaData.setAssetCategory(assetCategory);
				 assetCategory.addDepreciationMetaData(lDepreciationMetaData);
			 }
		 }

	}

	protected boolean validDepMetaData(DepreciationMetaData tDepreciationMetaData) {
		if (tDepreciationMetaData!= null && tDepreciationMetaData.getFinancialYear() != null 
				&& tDepreciationMetaData.getFinancialYear().getId() != null 
				&& tDepreciationMetaData.getDepreciationRate() != null 
				&& tDepreciationMetaData.getDepreciationRate() >= 0F) {
			 return true;
		 }
		 
		 return false;
	}
	
	/**
	 * This method will return list of asset category.
	 * Native query is used instead of HQL to get rid of dirty session issue(InvalidStateException).
	 * @return
	 */
	public Map<Long, String> getParentMap() {
		
		Map<Long, String> parentMap = new HashMap<Long, String>();
                String query = "select id,name from EG_ASSETCATEGORY ";
        	if(catTypeId != null && catTypeId != -1L)
        		query = query + "where ASSETTYPE_ID = "+catTypeId;
		try{ 
    		        List categoryList = persistenceService.getSession().createSQLQuery(query).list();
    		        if(categoryList != null && !categoryList.isEmpty()) { 
        		    Iterator assetCatIterator = categoryList.iterator();             
        		    Object[] assetCategoryObject;
                            while (assetCatIterator.hasNext()) {
                                assetCategoryObject = (Object[]) assetCatIterator.next();  
                                parentMap.put(Long.valueOf(assetCategoryObject[0].toString()), assetCategoryObject[1].toString());
                            }
        		}
    	            }
            	catch(Exception e) {
            		LOGGER.error("Exception in getParentMap() method:"+e.getMessage());
            	}				
		return parentMap;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Object getModel() {
		return assetCategory;
	}

	public void setModel(AssetCategory assetCategory){
		this.assetCategory = assetCategory;
	}
		
	// Spring Injection
	public void setAssetCategoryService(
			AssetCategoryService assetCategoryService) {
		this.assetCategoryService = assetCategoryService;
	}

	public List<AssetCategory> getAssetCategoryList() {
		return assetCategoryList;
	}

	public void setAssetCategoryList(List<AssetCategory> assetCategoryList) {
		this.assetCategoryList = assetCategoryList;
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

	public String getDataDisplayStyle() {
		return dataDisplayStyle;
	}
	public Long getCatTypeId() {
		return catTypeId;
	}
	
	public void setCatTypeId(Long catTypeId) {
		this.catTypeId = catTypeId;
	}

	public List<DepreciationMetaData> getDepMetaDatas() {
		return depMetaDatas;
	}

	public void setDepMetaDatas(List<DepreciationMetaData> depMetaDatas) {
		this.depMetaDatas = depMetaDatas;
	}

	public void setAssetAccCodePURPOSEID(String assetAccCodePURPOSEID) {
		this.assetAccCodePURPOSEID = assetAccCodePURPOSEID;
	}

	public void setRevResAccPURPOSEID(String revResAccPURPOSEID) {
		this.revResAccPURPOSEID = revResAccPURPOSEID;
	}

	public void setDepExpAccPURPOSEID(String depExpAccPURPOSEID) {
		this.depExpAccPURPOSEID = depExpAccPURPOSEID;
	}


	public void setAccDepPURPOSEID(String accDepPURPOSEID) {
		this.accDepPURPOSEID = accDepPURPOSEID;
	}

	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getIsAutoGeneratedCode() {
		return isAutoGeneratedCode;
	}

	public void setIsAutoGeneratedCode(String isAutoGeneratedCode) {
		this.isAutoGeneratedCode = isAutoGeneratedCode;
	}
	
}