package org.egov.assets.model;

import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;

public class AssetNumberGenrator {
	
	private SequenceGenerator sequenceGenerator;
	private PersistenceService<Script, Long> scriptService;
	private transient ScriptService scriptExecutionService;
	
	public String getAssetNumber(Asset asset,CFinancialYear financialYear){
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT", "assets.assetnumber.generator");
		return (String) scriptExecutionService.executeScript(scripts.get(0), ScriptService.createContext("asset",asset,"finYear",financialYear,"sequenceGenerator",sequenceGenerator));
	}

	public void setScriptService(
			PersistenceService<Script, Long> persistenceService) {
		this.scriptService = persistenceService;
	}
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

}