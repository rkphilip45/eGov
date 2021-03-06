package org.egov.web.actions.budget;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
@ParentPackage("egov")
@Results({ 
	@Result(name = "AJAX_RESULT", type = "stream", location = "returnStream", params = { "contentType", "text/plain"})
})

public class BudgetProposalDetailAction extends BaseBudgetDetailAction{
	private static final long serialVersionUID = 1L;

	private static final String ACTIONNAME="actionName";
	private Budget topBudget;
	private Map<Long,BigDecimal> beNextYearAmounts = new HashMap<Long,BigDecimal>();
	private static Logger LOGGER=Logger.getLogger(BudgetProposalDetailAction.class);	
	String streamResult="";
	private Long function;
	private Long budgetGroups;
	List<CFunction> functionList=Collections.EMPTY_LIST;
	List<BudgetGroup> budgetGroupList=Collections.EMPTY_LIST;
	
	public void setBudgetGroupList(List budgetGroupList) {
		this.budgetGroupList = budgetGroupList;
	}

	/**
	 * @return the streamResult
	 */
	public InputStream getReturnStream() {
		ByteArrayInputStream is = new ByteArrayInputStream(streamResult.getBytes());
		return is;
	}

		public BudgetProposalDetailAction(BudgetDetailConfig budgetDetailConfig) {
		super(budgetDetailConfig);
	}
	
	protected void saveAndStartWorkFlow(BudgetDetail detail) {
		try {
			if(budgetDocumentNumber!=null && budgetDetail.getBudget()!=null){
				Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
				b.setDocumentNumber(budgetDocumentNumber);
				budgetService.persist(b);
			HibernateUtil.getCurrentSession().flush();
			}
			BudgetDetail persist = budgetDetailService.createBudgetDetail(detail, getPosition(), getPersistenceService());
			populateSavedbudgetDetailListForDetail(detail);
			headerDisabled = true;
		} catch (ValidationException e) {
			LOGGER.error("Duplication in budget details"+ e.getMessage(),e);
			handleDuplicateBudgetDetailError(e);
			populateSavedbudgetDetailListForDetail(detail);
		}
	}
	protected void handleDuplicateBudgetDetailError(ValidationException e) {
		for (ValidationError error : e.getErrors()) {
			if("budgetDetail.duplicate".equals(error.getKey())){
				headerDisabled = true;
				break;
			}
		}
		throw e;
	}
	public void populateSavedbudgetDetailListFor(Budget budget){   
		if(budget != null && budget.getId()!=null)  
			savedbudgetDetailList = budgetDetailService.findAllBy("from BudgetDetail where budget=? order by function.name,budgetGroup.name", budget);
	}
	public void populateSavedbudgetDetailListForDetail(BudgetDetail bd){
		if(bd != null) {
			//find all RE for the functin 
			 List<BudgetDetail> findAllBy = budgetDetailService.findAllBy("from BudgetDetail where budget=? and function.id=? order by function.name,budgetGroup.name", bd.getBudget(),bd.getFunction().getId());
			 savedbudgetDetailList =findAllBy;
		      //find all next year be for the function
		    savedbudgetDetailList.addAll(budgetDetailService.findAllBy("from BudgetDetail where budget=(select bd from Budget bd where bd.referenceBudget=?) and function.id=? order by function.name,budgetGroup.name", bd.getBudget(),bd.getFunction().getId()));
		}
	}   

	public String ajaxLoadBudgetDetailList() {
		Long id = (Long)request.get("id");
		if(!Long.valueOf(0).equals(id)){
			savedbudgetDetailList = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?", id);
			Budget budget = budgetService.findById(id, false);
			re = budgetService.hasReForYear(budget.getFinancialYear().getId());
			budgetDetail.setBudget(budget);
			setReferenceBudget(budgetService.getReferenceBudgetFor(budget));
			budgetDocumentNumber = budget.getDocumentNumber();
		}
		populateBeNextYearAmounts();
		populateFinancialYear();
		return Constants.SAVED_DATA;
	}
	
	
	@SkipValidation    
	public String loadBudgetDetailList() {
		LOGGER.info("Initiating load budgets .....");
		if(addNewDetails)
		{
			return addNewDetails();
		}
		Long id =  budgetDetail.getBudget().getId(); 
			showRe=true;
			getDetailsFilterdBy();  
			Budget budget = budgetService.findById(id, false);
			re = budgetService.hasReForYear(budget.getFinancialYear().getId());     
			budgetDetail.setBudget(budget);
			setReferenceBudget(budgetService.getReferenceBudgetFor(budget));
			budgetDocumentNumber = budget.getDocumentNumber();
			budgetAmountView=new ArrayList<BudgetAmountView>(savedbudgetDetailList.size());   
			for(int i=0;i<savedbudgetDetailList.size();i++)  
			{
				budgetAmountView.add(new BudgetAmountView());
			}
			budgetDetailList=savedbudgetDetailList;
		populateBeNextYearAmountsAndBEAmounts();
		populateFinancialYear();
		loadAjaxedFunctionAndBudgetGroup();
		LOGGER.info("Budgets Loadded Succesfully");
		showDetails=true;
		return "new-re";   

	}
	@SkipValidation    
@Action(value="/budget/budgetProposalDetail-loadNewBudgetDetailList")
	public String loadNewBudgetDetailList() {
		LOGGER.info("Initiating load budgets .....");
		if(addNewDetails)
		{
			return addNewDetails();
		}
		Long id =  budgetDetail.getBudget().getId(); 
			showRe=true;
			getDetailsFilterdBy();  
			Budget budget = budgetService.findById(id, false);
			re = budgetService.hasReForYear(budget.getFinancialYear().getId());     
			budgetDetail.setBudget(budget);
			setReferenceBudget(budgetService.getReferenceBudgetFor(budget));
			budgetDocumentNumber = budget.getDocumentNumber();
			budgetAmountView=new ArrayList<BudgetAmountView>(savedbudgetDetailList.size());   
			for(int i=0;i<savedbudgetDetailList.size();i++)  
			{
				budgetAmountView.add(new BudgetAmountView());
			}
			budgetDetailList=savedbudgetDetailList;
		populateBeNextYearAmountsAndBEAmounts();
		populateFinancialYear();
		loadAjaxedFunctionAndBudgetGroup();
		LOGGER.info("Budgets Loadded Succesfully");
		showDetails=true;
		return "newDetail-re";   

	}
	public String addNewDetails() {
		Long id =  budgetDetail.getBudget().getId(); 
		addNewDetails=true;
		showRe=true;
		savedbudgetDetailList=new  ArrayList<BudgetDetail>();
		Budget budget = budgetService.findById(id, false);
		re = budgetService.hasReForYear(budget.getFinancialYear().getId());     
		budgetDetail.setBudget(budget);
		setReferenceBudget(budgetService.getReferenceBudgetFor(budget));
		budgetDocumentNumber = budget.getDocumentNumber();
		populateFinancialYear();
		loadAjaxedFunctionAndBudgetGroup();
		return "newDetail-re";
	}

	@SuppressWarnings("unchecked")
	private void loadAjaxedFunctionAndBudgetGroup() {
		
		ajaxLoadFunctions();
		ajaxLoadBudgetGroups();
		
		/*if(budgetDetail.getBudget()!=null )
		{
		String sqlStr="select distinct (f.name)  as name,f.id as id   from function f,egf_budgetdetail bd where  f.id=bd.function and bd.budget="+budgetDetail.getBudget().getId() +"  order  by f.name";
		SQLQuery sqlQuery = HibernateUtil.getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.addScalar("name")
		.addScalar("id",LongType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(CFunction.class));  
		 functionList = sqlQuery.list();
		
		 sqlStr="select  distinct (bg.name) as name ,bg.id  as id from egf_budgetgroup bg,egf_budgetdetail  bd where  bg.id=bd.budgetgroup and bd.budget="+budgetDetail.getBudget().getId()  +"  order  by bg.name";
		 sqlQuery = HibernateUtil.getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.addScalar("name")
		.addScalar("id",LongType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(BudgetGroup.class));  
		budgetGroupList = sqlQuery.list();
		}*/
		
	}
	

@Action(value="/budget/budgetProposalDetail-ajaxLoadBudgets")
	public String ajaxLoadBudgets() {
		String bere = (String)parameters.get("bere")[0];
		
		loadBudgets(bere);
		return "budgets";
	}

@Action(value="/budget/budgetProposalDetail-ajaxLoadFunctions")
	public String ajaxLoadFunctions() {
		Long id = (Long)request.get("id");
	if(getBudgetDetail()!=null && getBudgetDetail().getBudget()!=null && getBudgetDetail().getBudget().getName()!=null)
	{
		String budgetName=getBudgetDetail().getBudget().getName();
		
		//		(String)request.get("name");
		Integer deptId = getBudgetDetail().getExecutingDepartment().getId().intValue();  
		//this will load functions from budgetdeails table
		//String sqlStr="select distinct (f.name)  as name,f.id as id   from function f,egf_budgetdetail bd where  f.id=bd.function and bd.budget="+id +"  order  by f.name";
	String accountType;
	accountType = budgetDetailHelper.accountTypeForFunctionDeptMap(budgetName);
	
		String sqlStr="select distinct (f.name)  as name,f.id as id  from eg_dept_functionmap m,function f where departmentid=:deptId"+
				" and  budgetaccount_Type=:accountType and f.id= m.functionid order by f.name";  
		
		SQLQuery sqlQuery = HibernateUtil.getCurrentSession().createSQLQuery(sqlStr);
		
		 sqlQuery.setInteger("deptId", deptId)
		.setString("accountType",accountType);   
		sqlQuery.addScalar("name")
		.addScalar("id",LongType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(CFunction.class));  
		 functionList = sqlQuery.list();
		 dropdownData.put("functionList",  functionList);
	}
		return "functions";
		}

	
	
@Action(value="/budget/budgetProposalDetail-ajaxLoadBudgetGroups")
	public String ajaxLoadBudgetGroups() {
		Long id = (Long)request.get("id");
		String sqlStr="select  distinct (bg.name) as name ,bg.id  as id from egf_budgetgroup bg,egf_budgetdetail  bd where  bg.id=bd.budgetgroup and bd.budget="+id +"  order  by bg.name";
		SQLQuery sqlQuery = HibernateUtil.getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.addScalar("name")
		.addScalar("id",LongType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(BudgetGroup.class));  
		 budgetGroupList = sqlQuery.list();    
		return "budgetGroup";
		}
	
	public String saveAndNew() {
		return create();
	}
	public String saveAndNewRe() {
		return createRe();
	}

	@Override
	public void prepare() {
		super.prepare();
		populateSavedbudgetDetailListFor(budgetDetail.getBudget());  
		if(parameters.containsKey("re")){
			dropdownData.put("budgetList",Collections.EMPTY_LIST);
		}  
		loadAjaxedFunctionAndBudgetGroup();
	}
	
	@Override
	public boolean isShowMessage() {
		return super.isShowMessage();
	}
	
	public String getActionMessage(){
		if(getActionMessages() !=null && getActionMessages().iterator()!=null && getActionMessages().iterator().next() !=null)
			return getActionMessages().iterator().next().toString();
		else
			return "";
	}
	
	private void populateBeNextYearAmounts(){
		if(savedbudgetDetailList==null || savedbudgetDetailList.size()==0) return;
		Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(savedbudgetDetailList.get(0).getBudget());
		if(referenceBudgetFor!=null){
			List<BudgetDetail> result = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?",referenceBudgetFor.getId());
			for (BudgetDetail budgetDetail : savedbudgetDetailList) {
				for (BudgetDetail row : result) {
					if(compareDetails(row, budgetDetail)){
						beNextYearAmounts.put(budgetDetail.getId(), row.getOriginalAmount().setScale(2));
					}
				}
			}
		}
	}
	
	private void populateBeNextYearAmountsAndBEAmounts(){
		if(savedbudgetDetailList==null || savedbudgetDetailList.size()==0) return;
		beAmounts=new ArrayList<BigDecimal>(savedbudgetDetailList.size());
		Budget referenceBudgetFor = budgetService.getReferenceBudgetFor(savedbudgetDetailList.get(0).getBudget());
		if(referenceBudgetFor!=null){
			List<BudgetDetail> result = budgetDetailService.findAllBy("from BudgetDetail where budget.id=?",referenceBudgetFor.getId());
			for (BudgetDetail budgetDetail : savedbudgetDetailList) {
				for (BudgetDetail row : result) {
					if(compareDetails(row, budgetDetail)){
						beNextYearAmounts.put(budgetDetail.getId(), row.getOriginalAmount().setScale(2));
						beAmounts.add(row.getOriginalAmount());
					}
				}
			}
		}
	}
	

	
	protected void saveAndStartWorkFlowForRe(BudgetDetail detail,int index,CFinancialYear finYear,Budget refBudget) {
		try {
			if(budgetDocumentNumber!=null && budgetDetail.getBudget()!=null){
				Budget b = budgetService.findById(budgetDetail.getBudget().getId(), false);
				b.setDocumentNumber(budgetDocumentNumber);
				budgetService.persist(b);
			HibernateUtil.getCurrentSession().flush();
			}
			detail.getBudget().setFinancialYear(finYear);
			BudgetDetail reCurrentYear = budgetDetailService.createBudgetDetail(detail, null, getPersistenceService());
				reCurrentYear.setUniqueNo(reCurrentYear.getFund().getId() + "-" + reCurrentYear.getExecutingDepartment().getId() + "-"
					+ reCurrentYear.getFunction().getId() + "-" + reCurrentYear.getBudgetGroup().getId());
			budgetDetailService.persist(reCurrentYear);
			
			headerDisabled = true;
			BudgetDetail beNextYear = new BudgetDetail();
			//beNextYear=detail.clone();  
			if(addNewDetails){
				//This fix is for Phoenix Migration.beNextYear.changeState("END", getPosition(), "");
			}
			beNextYear.copyFrom(detail);
			beNextYear.setBudget(refBudget);
			beNextYear.setOriginalAmount(beAmounts.get(index));
			beNextYear.setDocumentNumber(detail.getDocumentNumber());
			beNextYear.setAnticipatoryAmount(reCurrentYear.getAnticipatoryAmount());
			beNextYear = budgetDetailService.createBudgetDetail(beNextYear, null, getPersistenceService());
				beNextYear.setUniqueNo(beNextYear.getFund().getId() + "-" + beNextYear.getExecutingDepartment().getId() + "-"
					+ beNextYear.getFunction().getId() + "-" + beNextYear.getBudgetGroup().getId());
			
			budgetDetailService.persist(beNextYear);
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage(), e);
			populateBeNextYearAmounts();
			handleDuplicateBudgetDetailError(e);
			populateSavedbudgetDetailListFor(budgetDetail.getBudget());
			throw e;
		}
	}
	
	public void approve(){
		if(!savedbudgetDetailList.isEmpty())
		{
		String budgetComment="";
		topBudget=savedbudgetDetailList.get(0).getBudget();
		setTopBudget(topBudget);
		if(parameters.get("budget.comments")!=null)
			budgetComment=parameters.get("budget.comments")[0];
		}
		Integer userId = null;
		if( parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject"))
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);
		else if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) 
			userId = Integer.valueOf(parameters.get("approverUserId")[0]);   
		else 
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		
		for (BudgetDetail detail : savedbudgetDetailList) {
		    if(new String("forward").equals(parameters.get(ACTIONNAME)[0])){
		    	//This fix is for Phoenix Migration.detail.changeState("Forwarded by "+getPosition().getName(), getPositionByUserId(userId) , detail.getComment());
			}
		    	budgetDetailService.persist(detail);
		}
		//We Dont need to start budget workflow here, Its starts frm HOD level.
		//forwardBudget(budgetComment, userId); //for RE
		if(topBudget!=null)
		setTopBudget(budgetService.getReferenceBudgetFor(topBudget));
		//forwardBudget(budgetComment, userId); //for BE
		
		if((parameters.get("actionName")[0]).contains("approv")){
			if(topBudget.getState().getValue().equals("END"))
				addActionMessage(getMessage("budgetdetail.approved.end"));
			else
				addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(getPositionByUserId(userId)));
		}
		else
			addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(getPositionByUserId(userId)));
	}

	private void forwardBudget(String budgetComment, Integer userId) {
		budgetWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, getTopBudget(),budgetComment);
	}   
@Action(value="/budget/budgetProposalDetail-newRe")
	public String newRe() {
		showRe = true;		
		CFinancialYear date=financialYearDAO.getFinancialYearByDate(new Date());
		HibernateUtil.getCurrentSession().setReadOnly(date, true);
		asOnDate=date.getStartingDate();
		asOnDate.setMonth(Calendar.SEPTEMBER);
		asOnDate.setDate(30);
		//setFinancialYear(null);
		return "new-re";
	}
	
@Action(value="/budget/budgetProposalDetail-newDetailRe")
	public String newDetailRe() {
		showRe = true;
		//setFinancialYear(null);
		return "newDetail-re";
	}
	
	@ValidationErrorPage(value="new-re")
@Action(value="/budget/budgetProposalDetail-loadActualsForRe")
	public String loadActualsForRe(){
		showRe = true;
		try {
			loadActuals();
			showDetails=true;
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage(),e);
			populateBudgetList();
			throw e;
		}
		populateBudgetList();
		return "new-re";
	}
	@ValidationErrorPage(value="newDetail-re")
@Action(value="/budget/budgetProposalDetail-loadActualsForBudgetDetailRe")
	public String loadActualsForBudgetDetailRe(){
		showRe = true;
		try {
			loadActuals();
			showDetails=true;
		} catch (ValidationException e) {
			LOGGER.error(e.getMessage(),e);
			populateBudgetList();
			throw e;
		}
		populateBudgetList();
		return "newDetail-re";
	}

	public void setShowRe(boolean showRe) {
		this.showRe = showRe;
	}

	public boolean isShowRe() {
		return showRe;
	}
	protected String getMessage(String key) {
		return getText(key);
	}
	public Budget getTopBudget()
	{
		return topBudget;
	}
	public void setTopBudget(Budget topBudget)
	{
		this.topBudget = topBudget;
	}
	public void setBeNextYearAmounts(Map<Long,BigDecimal> beNextYearAmounts) {
		this.beNextYearAmounts = beNextYearAmounts;
	}
	public Map<Long,BigDecimal> getBeNextYearAmounts() {
		return beNextYearAmounts;
	}
	public List getFunctionList() {
		return functionList;
	}
	public void setFunctionList(List functionList) {
		this.functionList = functionList;
	}
	public List getBudgetGroupList() {
		return budgetGroupList;
	}
	public Long getFunction() {
		return function;
	}
	public void setFunction(Long function) {
		this.function = function;
	}
	public Long getBudgetGroups() {
		return budgetGroups;
	}
	public void setBudgetGroups(Long budgetGroups) {
		this.budgetGroups = budgetGroups;
	}
	
}

