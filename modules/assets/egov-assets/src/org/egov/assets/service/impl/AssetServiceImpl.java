package org.egov.assets.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.assets.model.AccountInfo;
import org.egov.assets.model.Asset;
import org.egov.assets.model.AssetActivities;
import org.egov.assets.model.AssetNumberGenrator;
import org.egov.assets.model.SubledgerInfo;
import org.egov.assets.model.VoucherInput;
import org.egov.assets.service.AppService;
import org.egov.assets.service.AssetActivitiesService;
import org.egov.assets.service.AssetService;
import org.egov.assets.util.AssetConstants;
import org.egov.assets.util.AssetIdentifier;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;

/**
 * This class will expose all asset master related operations.
 * @author Nils
 *
 */
public class AssetServiceImpl extends BaseServiceImpl<Asset, Long>
										implements AssetService{

	private static final Logger logger = Logger.getLogger(AssetServiceImpl.class);
	private AppService appService;
	private AssetNumberGenrator assetNumberGenrator;
	public AssetServiceImpl(PersistenceService<Asset, Long> persistenceService) {
		super(persistenceService);
	}
	
	@Override
	public void setAssetNumber(Asset entity){
		CFinancialYear financialYear = getCurrentFinancialYear(entity.getDateOfCreation());
		if(entity!=null && (entity.getId()==null || entity.getCode()==null || "".equals(entity.getCode())) && isAssetCodeAutoGenerated()){
			logger.info("---Auto generating Asset code....");
			entity.setCode(assetNumberGenrator.getAssetNumber(entity, financialYear));
		}
	}
	
	@Override
	public boolean isAssetCodeAutoGenerated() {
		String isAutoGenerated = appService.getUniqueAppConfigValue("IS_ASSET_CODE_AUTOGENERATED");
		if("yes".equalsIgnoreCase(isAutoGenerated))
			return true;
		return false;
	}

	@Override
	public void capitaliseAsset(Asset asset, List<VoucherInput> voucherInputList, Date dateOfCapitalisation,
			BigDecimal assetIndrctExpns,Integer projectType) {
		BigDecimal totalAssetCapitaliseValue = new BigDecimal(0); 
		for(VoucherInput vi:voucherInputList){
			HashMap<String, Object> headerDetails = createHeaderAndMisDetails(asset,vi,dateOfCapitalisation);
			List<HashMap<String, Object>> accountDetails = createAccountDetails(asset,vi);
			List<HashMap<String, Object>> subledgerDetails = createSubledgerDetail(vi.getSubledgerInfo());
			createVoucher(headerDetails, accountDetails, subledgerDetails);
			for(HashMap<String, Object> ac:accountDetails)
				totalAssetCapitaliseValue = totalAssetCapitaliseValue.add(new BigDecimal((String)ac.get(VoucherConstant.DEBITAMOUNT)));
		}
		totalAssetCapitaliseValue = totalAssetCapitaliseValue.add(assetIndrctExpns);
		changeAssetToCapitaliseStatus(asset,appService.getUniqueAppConfigValue("ASSET_STATUS_AFTER_CAPITALISATION"),
				dateOfCapitalisation,totalAssetCapitaliseValue,projectType);
		logger.debug("Asset has been capitalised successfully. Asset Code: " + asset.getCode());
	}
	
	private void changeAssetToCapitaliseStatus(Asset asset, String statusCode, Date dateOfCapitalisation, 
			BigDecimal capitaliseValue,Integer projectType){
		EgwStatus egwStatus = getAssetStatusByCode(statusCode);
		//asset.setCommDate(dateOfCapitalisation); 
		asset.setStatus(egwStatus); 
		//asset.setInitialCapitalValue(capitaliseValue);
		//asset.setCurrentCapitalizationValue(capitaliseValue);
		
		AssetActivities assetActivities = new AssetActivities();
		assetActivities.setActivityDate(dateOfCapitalisation);
		assetActivities.setAdditionAmount(capitaliseValue);
		assetActivities.setAsset(asset);
		assetActivities.setDescription(asset.getRemark());
		AssetIdentifier assetIdentifier =projectType.equals(Integer.valueOf(0))?AssetIdentifier.C:
							projectType.equals(Integer.valueOf(1))?AssetIdentifier.I:AssetIdentifier.R;
		if(projectType.equals(Integer.valueOf(0)))
		assetActivities.setIdentifier(assetIdentifier);
		//assetActivitiesService.persist(assetActivities);
		genericService.setType(AssetActivities.class);
		genericService.persist(assetActivities);
	}
	
	protected HashMap<String, Object> createHeaderAndMisDetails(Asset asset, VoucherInput voucherInput,Date dateOfCapitalisation){
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		headerdetails.put(VoucherConstant.MODULEID, AssetConstants.MODULE_ID);
		if("Asset Indirect Expense".equalsIgnoreCase(voucherInput.getNarration())){
			headerdetails.put(VoucherConstant.VOUCHERNAME, "JVGeneral");
			headerdetails.put(VoucherConstant.DESCRIPTION, "Asset Indirect Expense");
		}else{
			headerdetails.put(VoucherConstant.VOUCHERNAME, "CapitalisedAsset");
			headerdetails.put(VoucherConstant.DESCRIPTION, voucherInput.getHeaderInfo().getDescription());
		}
		headerdetails.put(VoucherConstant.VOUCHERTYPE, AssetConstants.JOURNALVOUCHER);
		headerdetails.put(VoucherConstant.VOUCHERDATE, dateOfCapitalisation);
		headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherInput.getHeaderInfo().getDepartment());
		headerdetails.put(VoucherConstant.FUNDCODE, voucherInput.getHeaderInfo().getFund());
		headerdetails.put(VoucherConstant.FUNCTIONARYCODE,voucherInput.getHeaderInfo().getFunctionary());
		headerdetails.put(VoucherConstant.SCHEMECODE, voucherInput.getHeaderInfo().getScheme());
		headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherInput.getHeaderInfo().getSubScheme());
		headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherInput.getHeaderInfo().getFundSource());
		headerdetails.put(VoucherConstant.DIVISIONID, voucherInput.getHeaderInfo().getDivision());
		return headerdetails;   
	}
	
	protected List<HashMap<String, Object>> createAccountDetails(Asset asset, VoucherInput voucherInput){
		List<HashMap<String, Object>> accountDetails = new ArrayList<HashMap<String, Object>>();
		List<AccountInfo> creaditAccountList = voucherInput.getCreaditAccounts();
		HashMap<String, Object> account = null;
		String function = null;
		BigDecimal debitAmount = new BigDecimal(0);
		for(AccountInfo accountInfo : creaditAccountList){
			account = new HashMap<String, Object>();
			account.put(VoucherConstant.GLCODE, accountInfo.getGlcode());
			account.put(VoucherConstant.CREDITAMOUNT, accountInfo.getCreditAmount());
			account.put(VoucherConstant.DEBITAMOUNT, "0");
			account.put(VoucherConstant.FUNCTIONCODE, accountInfo.getFunction());
			function = accountInfo.getFunction();
			BigDecimal creaditAmount = new BigDecimal(accountInfo.getCreditAmount());
			debitAmount = debitAmount.add(creaditAmount);
			accountDetails.add(account);
		}
		account = new HashMap<String, Object>();
		account.put(VoucherConstant.GLCODE, asset.getAssetCategory().getAssetCode().getGlcode());
		account.put(VoucherConstant.CREDITAMOUNT, "0");
		account.put(VoucherConstant.DEBITAMOUNT, debitAmount.toString());
		account.put(VoucherConstant.FUNCTIONCODE, function);
		accountDetails.add(account);
		return accountDetails;
	}
	protected List<HashMap<String, Object>> createSubledgerDetail(SubledgerInfo subledgerInfo){
		List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> subledgerMap = new HashMap<String, Object>();
		subledgerMap.put(VoucherConstant.GLCODE, subledgerInfo.getGlcode());
		subledgerMap.put(VoucherConstant.DETAILTYPEID, subledgerInfo.getDetailType());
		subledgerMap.put(VoucherConstant.DETAILKEYID, subledgerInfo.getDetailKey());
		subledgerMap.put(VoucherConstant.CREDITAMOUNT, subledgerInfo.getDetailAmount());
		subledgerDetails.add(subledgerMap);
		return subledgerDetails;
	}
	protected void createVoucher(HashMap<String, Object> headerDetails,List<HashMap<String,Object>> accountDetails,List<HashMap<String,Object>> subledgerDetails){
		CreateVoucher jv = new CreateVoucher();
		CVoucherHeader voucherHeader = new CVoucherHeader();
		try{	
			voucherHeader = jv.createVoucher(headerDetails, accountDetails, subledgerDetails);
		} catch (EGOVRuntimeException e) {
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}
		logger.debug("Voucher has been saved successfully. Voucher Id: " + voucherHeader.getId());
	}
	
	public void setAppService(AppService appService) {
		this.appService = appService;
	}
	
	public void setAssetNumberGenrator(
			AssetNumberGenrator assetNumberGenrator) {
		this.assetNumberGenrator = assetNumberGenrator;
	}

	@Override
	public BigDecimal getDepreciationAmt(Asset asset) {
		Object obj =genericService.find(" select sum(amount) from AssetDepreciation where asset.id=?",asset.getId());
		if(obj==null)
			return BigDecimal.ZERO;
		else
			return (BigDecimal)obj;
	}

	
}