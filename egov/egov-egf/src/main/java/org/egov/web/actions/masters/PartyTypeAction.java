package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgPartytype;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validation()
public class PartyTypeAction extends BaseFormAction{
	
	private static final long serialVersionUID = -1076021355881784888L;
	private EgPartytype partyType = new EgPartytype();
	private boolean close = false;
	private String showMode="view";
	private List<EgPartytype> partyTypeList;
	private List<EgPartytype> partySearchList;
	private EgPartytype parentParty = null;
	protected static final String REQUIRED = "required";
	private String success = "";
	protected static final Logger LOGGER = Logger.getLogger(PartyTypeAction.class);

	@SkipValidation
	public Object getModel() {
		return partyType;
	}

	@Override
	@SkipValidation
	public void prepare() {
		super.prepare();
		dropdownData.put("partyTypeList", persistenceService
				.findAllBy("from EgPartytype order by code"));
	}

	@SkipValidation
@Action(value="/masters/partyType-newform")
	public String newform() {
		return NEW;
	}

	@SkipValidation
	@ValidationErrorPage(NEW)
	public String create() {
		validatemandatoryFields();
		try {
			if (partyType.getEgPartytype() != null && partyType.getEgPartytype().getId() != null) {
				parentParty = (EgPartytype) persistenceService
						.find("from EgPartytype where id=?", partyType.getEgPartytype().getId());
			}
			partyType.setEgPartytype(parentParty);
			partyType.setCode(partyType.getCode());
			partyType.setDescription(partyType.getDescription());
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");
			
			persistenceService.setType(EgPartytype.class);
			persistenceService.persist(partyType);
			HibernateUtil.getCurrentSession().flush();
			HibernateUtil.getCurrentSession().clear();
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in PartyTypeAction-create ", e);
             
            throw new EGOVRuntimeException("Exception occurred in PartyTypeAction-create ", e);
		}
		return NEW;
	}
	
	@SkipValidation
	@ValidationErrorPage(EDIT)
	public String edit() {
		validatemandatoryFields();
		try {
			EgPartytype partyOld = (EgPartytype) persistenceService.find("from EgPartytype where id=?", partyType.getId());
			
			partyOld.setCode(partyType.getCode());

			partyOld.setDescription(partyType.getDescription());
			if (partyType.getEgPartytype() != null && partyType.getEgPartytype().getId() != null) {
				parentParty = (EgPartytype) persistenceService
						.find("from EgPartytype where id=?", partyType.getEgPartytype().getId());
			}
			partyOld.setEgPartytype(parentParty);
			
			setPartyType(partyOld);
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");
			
			persistenceService.setType(EgPartytype.class);
			persistenceService.persist(partyType);
	//		showMode = "view";
			setSuccess("yes");
		} catch (Exception e) {
			setSuccess("no");
			LOGGER.error("Exception occurred in PartyTypeAction-edit ", e);
             
            throw new EGOVRuntimeException("Exception occurred in PartyTypeAction-edit ", e);
		}
		showMode = "edit";
		return EDIT;
	}
	
	@SkipValidation
@Action(value="/masters/partyType-beforeSearch")
	public String beforeSearch() {
		return "search";
	}
	
	@SkipValidation
	public String search() {
		StringBuffer query = new StringBuffer();

		query.append("From EgPartytype where createdBy is not null ");
		if (!partyType.getCode().isEmpty()) {
			query.append(" and upper(code) like upper('%" + partyType.getCode() + "%')");
		}
		if (!partyType.getDescription().isEmpty()) {
			query.append(" and upper(description) like upper('%" + partyType.getDescription() + "%')");
		}
		if (partyType.getEgPartytype() != null && partyType.getEgPartytype().getId() != null) {
			query.append(" and egPartytype =" + partyType.getEgPartytype());
		}
		this.partySearchList = persistenceService.findAllBy(query.toString());
		
		//this.partySearchList = EgovMasterDataCaching.getInstance().get(query.toString());
		return "search";
	}
	
	@SkipValidation
@Action(value="/masters/partyType-beforeModify")
	public String beforeModify() {
		partyType = (EgPartytype) persistenceService.find("from EgPartytype where id=?", partyType.getId());

		return EDIT;
	}
	
	private void validatemandatoryFields() {
		if (partyType.getCode() == null || "".equals(partyType.getCode())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"party.code.mandatory", getText("mandatory.party.code"))));
		}
		if (partyType.getDescription() == null || "".equals(partyType.getDescription())) {
			throw new ValidationException(Arrays.asList(new ValidationError(
					"party.desc.mandatory", getText("mandatory.party.description"))));
		}
		if (partyType.getCode() != null) {
			if(getCheckCode()) {
				throw new ValidationException(Arrays.asList(new ValidationError(
						"party.code.unique", getText("party.code.unique"))));
			}
		}
	}
	@SkipValidation
	public boolean getCheckCode() {
		EgPartytype pt = null;
		boolean isDuplicate = false;
		if (!this.partyType.getCode().equals("") && this.partyType.getId() != null)
			pt = (EgPartytype) persistenceService.find("from EgPartytype where code=? and id!=?",
					this.partyType.getCode(), this.partyType.getId());
		else if (!this.partyType.getCode().equals(""))
			pt = (EgPartytype) persistenceService.find("from EgPartytype where code=?",
							this.partyType.getCode());
		if (pt != null) {
			isDuplicate = true;
		}
		return isDuplicate;
	}
	
	public EgPartytype getPartyType() {
		return partyType;
	}

	public void setPartyType(EgPartytype partyType) {
		this.partyType = partyType;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

	public List<EgPartytype> getPartyTypeList() {
		return partyTypeList;
	}

	public void setPartyTypeList(List<EgPartytype> partyTypeList) {
		this.partyTypeList = partyTypeList;
	}

	public List<EgPartytype> getPartySearchList() {
		return partySearchList;
	}

	public void setPartySearchList(List<EgPartytype> partySearchList) {
		this.partySearchList = partySearchList;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

}
