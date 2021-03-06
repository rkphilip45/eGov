package org.egov.asset.service;

import org.egov.asset.model.Asset;

/**
 * This class will have all business logic related to Asset Master.
 * @author Nils
 *
 */
public interface AssetService extends BaseService<Asset,Long> {
	
	public void setAssetNumber(Asset entity);
	
	public boolean isAssetCodeAutoGenerated();
	
}
