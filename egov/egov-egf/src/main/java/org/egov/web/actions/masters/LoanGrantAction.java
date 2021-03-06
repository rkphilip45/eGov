package org.egov.web.actions.masters;

import org.apache.struts2.convention.annotation.Action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.egf.masters.model.FundingAgency;
import org.egov.egf.masters.model.LoanGrantBean;
import org.egov.egf.masters.model.LoanGrantDetail;
import org.egov.egf.masters.model.LoanGrantHeader;
import org.egov.egf.masters.model.LoanGrantReceiptDetail;
import org.egov.egf.masters.model.SchemeBankaccount;
import org.egov.egf.masters.model.SubSchemeProject;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.User;
import org.egov.web.actions.masters.loangrant.LoanGrantBaseAction;
import org.egov.web.actions.voucher.CommonAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.type.*;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

public class LoanGrantAction extends LoanGrantBaseAction {
	private static final String	VIEW	= "view";
	private LoanGrantHeader loanGrantHeader;
	private List<LoanGrantDetail> sanctionedAmountLGDetails;
	private List<LoanGrantDetail> unsanctionedAmountLGDetails;
	private List<LoanGrantDetail> revisedAmountLGDetails;
	private List<FundingAgency> fundingAgencyList;
	private List<LoanGrantBean> projectCodeList;
	private Integer bankaccount;
	private Integer bank_branch;
	private CommonAction common;
	private String mode;
	private	Map<String, String> bankBranchMap;
	private List<LoanGrantHeader> loanGrantHeaderList;
	private Query query;
	private static final String	SANCTIONEDTYPE	= "sanctioned";
	private static final String	UNSANCTIONEDTYPE	= "unsanctioned";
	private static final String	REVISEDTYPE	= "revised";
	public LoanGrantAction() {
		super();
		loanGrantHeader=new LoanGrantHeader();
	}
	public void prepare()
	{
		super.prepare();
		
		
	}
	public void prepareNewForm()
	{
		addDropdownData("bankaccountList", Collections.EMPTY_LIST);
	}
	@SuppressWarnings("unchecked")
	public void prepareBeforeEdit()
	{
		
		loanGrantHeader = (LoanGrantHeader) persistenceService.find("from LoanGrantHeader where id=?", ((LoanGrantHeader) getModel()).getId());
		SchemeBankaccount account =(SchemeBankaccount) persistenceService.find("from SchemeBankaccount where subScheme=?",loanGrantHeader.getSubScheme());
		setFundId(loanGrantHeader.getSubScheme().getScheme().getFund().getId());
		setBank_branch(account.getBankAccount().getBankbranch().getId());
		List<Bankaccount> accNumList = (List<Bankaccount>) persistenceService.findAllBy("from Bankaccount ba where ba.bankbranch.id=? and fund.id=? and isactive=1 order by ba.chartofaccounts.glcode",
				bank_branch,fundId);
		setBankaccount(account.getBankAccount().getId());
		
		addDropdownData("bankaccountList", accNumList);
		List<Bankbranch> branchList =  (List<Bankbranch>)persistenceService.findAllBy("from Bankbranch br where br.id in (select bankbranch.id from Bankaccount where fund.id=? ) and br.isactive=1 order by br.bank.name asc",fundId);
		addDropdownData("bankbranchList", branchList);
		fundingAgencyList=new ArrayList<FundingAgency>();
		fundingAgencyList.addAll(persistenceService.findAllBy(" from FundingAgency where isActive=1 order by name"));
		schemeId=loanGrantHeader.getSubScheme().getScheme().getId();
		subSchemeId=loanGrantHeader.getSubScheme().getId();
		projectCodeList=new ArrayList<LoanGrantBean>();
		String strQuery="select pc.id as id , pc.code as code, pc.name as name from egw_projectcode pc," + 
						" egf_subscheme_project sp where pc.id= sp.projectcodeid and sp.subschemeid="+subSchemeId;
		query=HibernateUtil.getCurrentSession().createSQLQuery(strQuery)
		.addScalar("id",LongType.INSTANCE).addScalar("code").addScalar("name")
		.setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
		projectCodeList=query.list();
		List<LoanGrantDetail> lgDetailList=loanGrantHeader.getDetailList();
		if(lgDetailList!=null &&lgDetailList.size()!=0 )
		{
			boolean sanctionedDetailsPresent,unsanctionedDetailsPresent,revisedDetailsPresent;
			sanctionedDetailsPresent=unsanctionedDetailsPresent=revisedDetailsPresent=false;
			//Assumption is lgDetailList is atleast of size 1.
			for(LoanGrantDetail lgDetail:lgDetailList)
			{
				if(lgDetail.getPatternType().equalsIgnoreCase(SANCTIONEDTYPE))
					sanctionedDetailsPresent=true;
				if(lgDetail.getPatternType().equalsIgnoreCase(UNSANCTIONEDTYPE))
					unsanctionedDetailsPresent=true;
				if(lgDetail.getPatternType().equalsIgnoreCase(REVISEDTYPE))
					revisedDetailsPresent=true;
			}
			if(!sanctionedDetailsPresent)
			{
				sanctionedAmountLGDetails=new ArrayList<LoanGrantDetail>();
				sanctionedAmountLGDetails.add(new LoanGrantDetail());
			}
			if(!unsanctionedDetailsPresent)
			{
				unsanctionedAmountLGDetails=new ArrayList<LoanGrantDetail>();
				unsanctionedAmountLGDetails.add(new LoanGrantDetail());
			}
			if(!revisedDetailsPresent)
			{
				revisedAmountLGDetails=new ArrayList<LoanGrantDetail>();
				revisedAmountLGDetails.add(new LoanGrantDetail());
			}
		}
	}
	public void prepareBeforeView()
	{
	prepareBeforeEdit();	
	}
	@Override
	public Object getModel() {
		
		return loanGrantHeader;
	}
	@SkipValidation
@Action(value="/masters/loanGrant-beforeSearch")
	public String beforeSearch()
	{
		return "search";
	}
	@SuppressWarnings("unchecked")
	@SkipValidation
@Action(value="/masters/loanGrant-search")
	public String search()
	{
		loanGrantHeaderList=new ArrayList<LoanGrantHeader>();
		if(schemeId!=null && subSchemeId==null)
		{
			loanGrantHeaderList.addAll(persistenceService.findAllBy("from LoanGrantHeader where subScheme.scheme.id=? order by subScheme.name ", schemeId));
		}
		if(schemeId!=null && subSchemeId!=null)
		{
			loanGrantHeaderList.addAll(persistenceService.findAllBy("from LoanGrantHeader where subScheme.id=? order by subScheme.name ", subSchemeId));
		}
		return "search";
	}
	@SkipValidation
@Action(value="/masters/loanGrant-beforeView")
	public String beforeView()
	{
		beforeEdit();
		return VIEW;
	}
	@SkipValidation
@Action(value="/masters/loanGrant-beforeEdit")
	public String beforeEdit(){
		return EDIT;
	}
	@SuppressWarnings("unchecked")
	@SkipValidation
@Action(value="/masters/loanGrant-newForm")
	public String newForm()
	{
		projectCodeList=new ArrayList<LoanGrantBean>();
		projectCodeList.add(new LoanGrantBean());
		
		setBankBranchMap(new HashMap<String, String>());
		addDropdownData("bankaccountList", Collections.EMPTY_LIST);
		persistenceService.setType(LoanGrantDetail.class);
		sanctionedAmountLGDetails=new ArrayList<LoanGrantDetail>();
		sanctionedAmountLGDetails.add(new LoanGrantDetail());
		unsanctionedAmountLGDetails=new ArrayList<LoanGrantDetail>();
		unsanctionedAmountLGDetails.add(new LoanGrantDetail());
		revisedAmountLGDetails=new ArrayList<LoanGrantDetail>();
		revisedAmountLGDetails.add(new LoanGrantDetail());
		persistenceService.setType(FundingAgency.class);
		fundingAgencyList=new ArrayList<FundingAgency>();
		fundingAgencyList.addAll(persistenceService.findAllBy(" from FundingAgency where isActive=1 order by name"));
		loanGrantHeader.getReceiptList().add(new LoanGrantReceiptDetail());
		return "new";
	}
	@SkipValidation
@Action(value="/masters/loanGrant-codeUniqueCheckCode")
	public String codeUniqueCheckCode(){
		return "codeUniqueCheckCode";
	}
	@SkipValidation
	public boolean getCodeCheckCode(){
		LoanGrantHeader header = null;
		boolean isDuplicate = false;
		if (subSchemeId!=null && loanGrantHeader.getId()!=null)
			header = (LoanGrantHeader)persistenceService.find("from LoanGrantHeader where subScheme.id=? and id!=?", subSchemeId, loanGrantHeader.getId());
		else if(subSchemeId!=null)
			header = (LoanGrantHeader)persistenceService.find("from LoanGrantHeader where subScheme.id=?", subSchemeId);
		if(header!=null){
			isDuplicate=true;
		}
		return isDuplicate;
	}
	public void validate(){
		if(schemeId==null || schemeId==-1){
			addFieldError("schemeId", getText("masters.loangrant.scheme.mandatory"));
		}
		if(subSchemeId==null || subSchemeId==-1 ){
			addFieldError("subSchemeId", getText("masters.loangrant.subscheme.mandatory"));
		}
		if(bankaccount==null ||bankaccount==-1 ){
			addFieldError("bankaccount", getText("masters.loangrant.bankaccount.mandatory"));
		}
		if(loanGrantHeader.getAmendmentDate()==null ){
			addFieldError("amendmentDate", getText("masters.loangrant.amendmentdate.mandatory"));
		}
		if(loanGrantHeader.getAmendmentNo() ==null){
			addFieldError("amendmentNo", getText("masters.loangrant.amendmentno.mandatory"));
		}
		if(loanGrantHeader.getCouncilResDate() ==null){
			addFieldError("councilResDate", getText("masters.loangrant.councilresdate.mandatory"));
		}
		if(loanGrantHeader.getCouncilResNo()==null ){
			addFieldError("councilResNo", getText("masters.loangrant.councilresno.mandatory"));
		}
		if(loanGrantHeader.getGovtOrderDate()==null ){
			addFieldError("govtOrderDate", getText("masters.loangrant.govtorderdate.mandatory"));
		}
		if(loanGrantHeader.getGovtOrderNo()==null ){
			addFieldError("govtOrderNo", getText("masters.loangrant.govtorderno.mandatory"));
		}
		if(loanGrantHeader.getProjectCost()==null|| loanGrantHeader.getProjectCost().equals(BigDecimal.ZERO)){
			addFieldError("projectCost", getText("masters.loangrant.projectcost.mandatory"));
		}
		if(loanGrantHeader.getProjectCost()!=null && loanGrantHeader.getSanctionedCost()!=null 
				&& loanGrantHeader.getSanctionedCost().compareTo(loanGrantHeader.getProjectCost())>0 ){
			addFieldError("projectCost", getText("masters.loangrant.validate.projectcost"));
		}
		if(loanGrantHeader.getProjectCost()!=null && loanGrantHeader.getRevisedCost()!=null 
				&& loanGrantHeader.getRevisedCost().compareTo(loanGrantHeader.getProjectCost())<0 ){
			addFieldError("revisedCost", getText("masters.loangrant.validate.revisedcost"));
		}
		if(getCodeCheckCode())
			addActionError(getText("loangrant.subscheme.already.exists"));
	}
	@ValidationErrorPage(NEW)
	@SuppressWarnings("unchecked")
	public String save()
	{
		if(!getFieldErrors().isEmpty())
		{
			return NEW;
		}
		try{
				Scheme scheme= (Scheme) persistenceService.find(" from Scheme where id=?",getSchemeId());
				SubScheme subScheme= (SubScheme) persistenceService.find(" from SubScheme where id=?",getSubSchemeId());
				loanGrantHeader.setSubScheme(subScheme);
				if(loanGrantHeader.getRevisedCost()==null)
					loanGrantHeader.setRevisedCost(BigDecimal.ZERO);
				SubSchemeProject subSchemeProject;
				persistenceService.setType(SubSchemeProject.class);
				for(LoanGrantBean bean: projectCodeList )
				{
					subSchemeProject= new SubSchemeProject();
					subSchemeProject.setSubScheme(subScheme);
					subSchemeProject.setProjectCode(bean.getId());
					persistenceService.persist(subSchemeProject);
				}
				Bankaccount bankaccObj=(Bankaccount) persistenceService.find(" from Bankaccount where id=?",bankaccount);
				SchemeBankaccount schemeBankaccount= new SchemeBankaccount();
				schemeBankaccount.setBankAccount(bankaccObj);
				schemeBankaccount.setScheme(scheme);
				schemeBankaccount.setSubScheme(subScheme);
				persistenceService.setType(SchemeBankaccount.class);
				persistenceService.persist(schemeBankaccount);
				createDetailAndReceiptList();
				persistenceService.setType(LoanGrantHeader.class);
				persistenceService.persist(loanGrantHeader);
		}
		catch (ValidationException e) {
			throw e;
		}catch (Exception e){
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator","An error occured contact Administrator")));
		}
		return "result";
	}
	@ValidationErrorPage(EDIT)
	@SuppressWarnings("unchecked")
	public String update()
	{
		if(!getFieldErrors().isEmpty())
		{
			return EDIT;
		}
		try{
				SubScheme subScheme= (SubScheme) persistenceService.find(" from SubScheme where id=?",getSubSchemeId());
				loanGrantHeader.setSubScheme(subScheme);
				User user= (User) persistenceService.find("from User where id=?", Integer.parseInt(EGOVThreadLocals.getUserId()));
				Date currDate=new Date();
				loanGrantHeader.setCreatedBy(user);
				loanGrantHeader.setModifiedBy(user);
				loanGrantHeader.setCreatedDate(currDate);
				loanGrantHeader.setModifiedDate(currDate);
				if(loanGrantHeader.getRevisedCost()==null)
					loanGrantHeader.setRevisedCost(BigDecimal.ZERO);
				createDetailAndReceiptList();
				for(LoanGrantDetail lgDetail:loanGrantHeader.getDetailList())
				{
					if(lgDetail.getId()!=null)
					{
						lgDetail.setCreatedBy(user);
						lgDetail.setModifiedBy(user);
						lgDetail.setCreatedDate(currDate);
						lgDetail.setModifiedDate(currDate);
					}
				}
				for(LoanGrantReceiptDetail lgRecptDetail:loanGrantHeader.getReceiptList())
				{
					if(lgRecptDetail.getId()!=null)
					{
						lgRecptDetail.setCreatedBy(user);
						lgRecptDetail.setModifiedBy(user);
						lgRecptDetail.setCreatedDate(currDate);
						lgRecptDetail.setModifiedDate(currDate);
					}
				}
				query=HibernateUtil.getCurrentSession().createSQLQuery("delete from egf_subscheme_project where subschemeid= "+getSubSchemeId());
				int executeUpdate = query.executeUpdate();
				SubSchemeProject subSchemeProject;
				persistenceService.setType(SubSchemeProject.class);
				for(LoanGrantBean bean: projectCodeList )
				{
					subSchemeProject= new SubSchemeProject();
					subSchemeProject.setSubScheme(subScheme);
					subSchemeProject.setProjectCode(bean.getId());
					persistenceService.persist(subSchemeProject);
				}
				SchemeBankaccount schemeBankaccount= (SchemeBankaccount) persistenceService.find("from SchemeBankaccount where scheme.id=?",getSchemeId());
				Bankaccount accountObj=(Bankaccount) persistenceService.find("from Bankaccount where id=?",bankaccount);
				schemeBankaccount.setBankAccount(accountObj);
				persistenceService.setType(LoanGrantHeader.class);
				persistenceService.persist(loanGrantHeader);
		}
		catch (ValidationException e) {
			prepareBeforeEdit();
			throw e;
		}catch (Exception e){
			prepareBeforeEdit();
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator","An error occured contact Administrator")));
			
		}
		
		return "result";
	}
	@SkipValidation
	private void createDetailAndReceiptList()
	{
		for(LoanGrantDetail detail:sanctionedAmountLGDetails)
		{
			if(detail==null||detail.getFundingAgency()==null || detail.getFundingAgency().getId()==-1 
					||((detail.getLoanAmount()==null||detail.getLoanAmount().equals(BigDecimal.ZERO) )
							&&(detail.getGrantAmount()==null||detail.getGrantAmount().equals(BigDecimal.ZERO) )))
				continue;
			else
			{
				if(detail.getLoanAmount()==null)
					detail.setLoanAmount(BigDecimal.ZERO);
				if(detail.getGrantAmount()==null)
					detail.setGrantAmount(BigDecimal.ZERO);
				detail.setPatternType(SANCTIONEDTYPE);
				detail.setHeader(loanGrantHeader);
				loanGrantHeader.getDetailList().add(detail);
			}
		}
		for(LoanGrantDetail detail:unsanctionedAmountLGDetails)
		{
			if(detail==null||detail.getFundingAgency()==null || detail.getFundingAgency().getId()==-1 
					||((detail.getLoanAmount()==null||detail.getLoanAmount().equals(BigDecimal.ZERO) )
							&&(detail.getGrantAmount()==null||detail.getGrantAmount().equals(BigDecimal.ZERO) )))
				continue;
			else
			{
				if(detail.getLoanAmount()==null)
					detail.setLoanAmount(BigDecimal.ZERO);
				if(detail.getGrantAmount()==null)
					detail.setGrantAmount(BigDecimal.ZERO);
				detail.setPatternType(UNSANCTIONEDTYPE);
				detail.setHeader(loanGrantHeader);
				loanGrantHeader.getDetailList().add(detail);
			}
		}
		for(LoanGrantDetail detail:revisedAmountLGDetails)
		{
			if(detail==null||detail.getFundingAgency()==null || detail.getFundingAgency().getId()==-1 
					||((detail.getLoanAmount()==null||detail.getLoanAmount().equals(BigDecimal.ZERO) )
							&&(detail.getGrantAmount()==null||detail.getGrantAmount().equals(BigDecimal.ZERO) )))
				continue;
			else
			{
				if(detail.getLoanAmount()==null)
					detail.setLoanAmount(BigDecimal.ZERO);
				if(detail.getGrantAmount()==null)
					detail.setGrantAmount(BigDecimal.ZERO);
				detail.setPatternType(REVISEDTYPE);
				detail.setHeader(loanGrantHeader);
				loanGrantHeader.getDetailList().add(detail);
			}
		}
		List<LoanGrantReceiptDetail> newReceiptList=new ArrayList<LoanGrantReceiptDetail>();
		for(LoanGrantReceiptDetail  receiptDetail:loanGrantHeader.getReceiptList())
		{
			if(receiptDetail.getVoucherHeader()==null ||receiptDetail.getVoucherHeader().getId()==null )
				continue;
			else
			{	
				if(receiptDetail.getBankaccount()!=null && receiptDetail.getBankaccount().getId()==null  )
					receiptDetail.setBankaccount(null);
				if(receiptDetail.getInstrumentHeader()!=null && receiptDetail.getInstrumentHeader().getId()==null  )
					receiptDetail.setInstrumentHeader(null);
				if(receiptDetail.getFundingAgency()!=null && receiptDetail.getFundingAgency().getId()==null  )
					receiptDetail.setFundingAgency(null);
				receiptDetail.setLoanGrantHeader(loanGrantHeader);
				newReceiptList.add(receiptDetail);
			}
		}
		loanGrantHeader.setReceiptList(newReceiptList);
	}
	@SkipValidation
   public void loadBanks()
   {
	   common.ajaxLoadBanks();
	   addDropdownData("bankBranchList",common.getBankBranchList());
   }
	public void setLoanGrantHeader(LoanGrantHeader loanGrantHeader) {
		this.loanGrantHeader = loanGrantHeader;
	}
	public LoanGrantHeader getLoanGrantHeader() {
		return loanGrantHeader;
	}
	public List<LoanGrantHeader> getLoanGrantHeaderList() {
		return loanGrantHeaderList;
	}
	public void setUnsanctionedAmountLGDetails(
			List<LoanGrantDetail> unsanctionedAmountLGDetails) {
		this.unsanctionedAmountLGDetails = unsanctionedAmountLGDetails;
	}
	public List<LoanGrantDetail> getUnsanctionedAmountLGDetails() {
		return unsanctionedAmountLGDetails;
	}
	public void setRevisedAmountLGDetails(List<LoanGrantDetail> revisedAmountLGDetails) {
		this.revisedAmountLGDetails = revisedAmountLGDetails;
	}
	public List<LoanGrantDetail> getRevisedAmountLGDetails() {
		return revisedAmountLGDetails;
	}
	public void setSanctionedAmountLGDetails(
			List<LoanGrantDetail> sanctionedAmountLGDetails) {
		this.sanctionedAmountLGDetails = sanctionedAmountLGDetails;
	}
	public List<LoanGrantDetail> getSanctionedAmountLGDetails() {
		return sanctionedAmountLGDetails;
	}
	public void setCommon(CommonAction common) {
		this.common = common;
	}
	public List<LoanGrantBean> getProjectCodeList() {
		return projectCodeList;
	}
	public void setProjectCodeList(List<LoanGrantBean> projectCodeList) {
		this.projectCodeList = projectCodeList;
	}
	public void setBankBranchMap(Map<String, String> bankBranchMap) {
		this.bankBranchMap = bankBranchMap;
	}
	public Map<String, String> getBankBranchMap() {
		return bankBranchMap;
	}
	public void setFundingAgencyList(List<FundingAgency> fundingAgencyList) {
		this.fundingAgencyList = fundingAgencyList;
	}
	public List<FundingAgency> getFundingAgencyList() {
		return fundingAgencyList;
	}
	public void setBankaccount(Integer bankaccount) {
		this.bankaccount = bankaccount;
	}
	public Integer getBankaccount() {
		return bankaccount;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getBank_branch() {
		return bank_branch;
	}
	public void setBank_branch(Integer bank_branch) {
		this.bank_branch = bank_branch;
	}
}
