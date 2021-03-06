package org.egov.services.budget;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.dao.budget.BudgetDetailsHibernateDAO;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetReAppropriation;
import org.egov.model.budget.BudgetReAppropriationMisc;
import org.egov.pims.commons.Position;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;
import org.egov.web.actions.budget.BudgetReAppropriationView;


public class BudgetReAppropriationService extends PersistenceService<BudgetReAppropriation, Long>{
	WorkflowService<BudgetReAppropriationMisc> miscWorkflowService;
	BudgetDetailService budgetDetailService;
	protected WorkflowService<BudgetDetail> budgetDetailWorkflowService;
	BudgetDetailConfig budgetDetailConfig;
	PersistenceService persistenceService;
	SequenceGenerator sequenceGenerator;
	private GenericHibernateDaoFactory genericDao;
	private BudgetDetailsHibernateDAO budgetDetailsDAO;
	private static final Logger LOGGER = Logger.getLogger(BudgetReAppropriationService.class);

	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}
	
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	public void setBudgetDetailWorkflowService(
			WorkflowService<BudgetDetail> budgetDetailWorkflowService) {
		this.budgetDetailWorkflowService = budgetDetailWorkflowService;
	}

	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setMiscWorkflowService(WorkflowService<BudgetReAppropriationMisc> miscWorkflowService) {
		this.miscWorkflowService = miscWorkflowService;
	}

	public BudgetReAppropriationService(BudgetDetailConfig budgetDetailConfig){
		setType(BudgetReAppropriation.class);
		this.budgetDetailConfig = budgetDetailConfig;
	}
	
	public boolean checkRowEmpty(BudgetReAppropriationView appropriation) {
		if((appropriation.getBudget()==null || appropriation.getBudget().getId() == 0) && (appropriation.getBudgetDetail().getBudgetGroup()==null || appropriation.getBudgetDetail().getBudgetGroup().getId()==0) 
				&& isMandatoryGridFieldEmpty(appropriation)){
			return true;
		}
		return false;
	}
	public BudgetDetail createApprovedBudgetDetail(BudgetReAppropriationView appropriation,Position position) {
		BudgetDetail detail = new BudgetDetail();
		BudgetDetail budgetDetail = appropriation.getBudgetDetail();
		detail.copyFrom(budgetDetail);
		BudgetDetail savedBudgetDetail = budgetDetailService.createBudgetDetail(detail, position,persistenceService);
		//This fix is for Phoenix Migration.budgetDetailWorkflowService.end(detail, position);
		return savedBudgetDetail;
	}
	
	public void validateMandatoryFields(List<BudgetReAppropriationView> reAppropriationList) {
		for (BudgetReAppropriationView entry : reAppropriationList) {
			entry.setBudgetDetail(setRelatedValues(entry.getBudgetDetail()));
			if(entry.getBudgetDetail().getBudgetGroup() == null || entry.getBudgetDetail().getBudgetGroup().getId()==0L)
				throw new ValidationException(Arrays.asList(new ValidationError("budgetDetail.budgetGroup.mandatory","budgetDetail.budgetGroup.mandatory")));
			Map<String, Object> valueMap = constructValueMap(entry.getBudgetDetail());
			budgetDetailConfig.checkHeaderMandatoryField(valueMap);
			budgetDetailConfig.checkGridMandatoryField(valueMap);
		}
	}
	
	public BudgetDetail setRelatedValues(BudgetDetail detail) {
		if(detail.getExecutingDepartment() != null && detail.getExecutingDepartment().getId()==0)
			detail.setExecutingDepartment(null);
		if(detail.getFunction() != null && detail.getFunction().getId()==0)
			detail.setFunction(null);
		if(detail.getScheme() != null && detail.getScheme().getId()==0)
			detail.setScheme(null);
		if(detail.getSubScheme() != null && detail.getSubScheme().getId()==0)
			detail.setSubScheme(null);
		if(detail.getFunctionary() !=null && detail.getFunctionary().getId()==0)
			detail.setFunctionary(null);
		if(detail.getBoundary() != null && detail.getBoundary().getId()==0)
			detail.setBoundary(null);
		if(detail.getFund() != null && detail.getFund().getId()==0)
			detail.setFund(null);
		return detail;
	}
	private Map<String,Object> constructValueMap(BudgetDetail budgetDetail) {
		Map<String,Object> valueMap = new HashMap<String, Object>();
		valueMap.put(Constants.EXECUTING_DEPARTMENT, budgetDetail.getExecutingDepartment());
		valueMap.put(Constants.FUNCTION, budgetDetail.getFunction());
		valueMap.put(Constants.FUNCTIONARY, budgetDetail.getFunctionary());
		valueMap.put(Constants.SCHEME, budgetDetail.getScheme());
		valueMap.put(Constants.SUB_SCHEME, budgetDetail.getSubScheme());
		valueMap.put(Constants.BOUNDARY, budgetDetail.getBoundary());
		valueMap.put(Constants.FUND, budgetDetail.getFund());
		return valueMap;
	}
	
	private boolean isMandatoryGridFieldEmpty(BudgetReAppropriationView appropriation) {
		for (String entry : budgetDetailConfig.getGridFields()) {
			if(Constants.FUNCTION.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUNCTION) && (appropriation.getBudgetDetail().getFunction() == null || appropriation.getBudgetDetail().getFunction().getId() ==0) )
				return true;
			if(Constants.EXECUTING_DEPARTMENT.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.EXECUTING_DEPARTMENT) && (appropriation.getBudgetDetail().getExecutingDepartment() == null || appropriation.getBudgetDetail().getExecutingDepartment().getId()==0))
				return true;
			if(Constants.FUND.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUND) && (appropriation.getBudgetDetail().getExecutingDepartment() == null || appropriation.getBudgetDetail().getExecutingDepartment().getId()==0))
				return true;
			if(Constants.SCHEME.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.SCHEME) && (appropriation.getBudgetDetail().getScheme() == null || appropriation.getBudgetDetail().getScheme().getId()==0))
				return true;
			if(Constants.SUB_SCHEME.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.SUB_SCHEME) && (appropriation.getBudgetDetail().getSubScheme() == null || appropriation.getBudgetDetail().getSubScheme().getId()==0))
				return true;
			if(Constants.BOUNDARY.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.BOUNDARY) && (appropriation.getBudgetDetail().getBoundary() == null || appropriation.getBudgetDetail().getBoundary().getBndryId()==0))
				return true;
			if(Constants.FUNCTIONARY.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUNCTIONARY) && (appropriation.getBudgetDetail().getFunctionary() == null || appropriation.getBudgetDetail().getFunctionary().getId()==0))
				return true;
			if(Constants.FUND.equalsIgnoreCase(entry) && budgetDetailConfig.getMandatoryFields().contains(Constants.FUND) && (appropriation.getBudgetDetail().getFund() == null || appropriation.getBudgetDetail().getFund().getId()==0))
				return true;
		}
		return false;
	}
	
	public boolean rowsToAddExists(List<BudgetReAppropriationView> reAppropriationList) {
		for (BudgetReAppropriationView budgetReAppropriationView : reAppropriationList) {
			if(checkRowEmpty(budgetReAppropriationView))
				return false;
			BudgetDetail budgetDetail = budgetReAppropriationView.getBudgetDetail();
			setRelatedValues(budgetDetail);
			if(budgetDetail.getBudgetGroup() != null && budgetDetail.getBudgetGroup().getId()==0)
				budgetDetail.setBudgetGroup(null);
			if(!checkRowEmpty(budgetReAppropriationView))
				return true;
		}
		return false;
	}
	
	public void validateDuplicates(List<BudgetReAppropriationView> budgetReAppropriationList,BudgetReAppropriationView appropriation) {
		for (BudgetReAppropriationView budgetReAppropriationView : budgetReAppropriationList) {
			if(appropriation.getBudgetDetail().compareTo(budgetReAppropriationView.getBudgetDetail()))
				throw new ValidationException(Arrays.asList(new ValidationError("reApp.duplicate.entry","reApp.duplicate.entry")));
		}
	}

	public boolean rowsToAddForExistingDetails(List<BudgetReAppropriationView> reAppropriationList) {
		for (BudgetReAppropriationView budgetReAppropriationView : reAppropriationList) {
			BudgetDetail budgetDetail = budgetReAppropriationView.getBudgetDetail();
			setRelatedValues(budgetDetail);
			if(budgetDetail.getBudgetGroup() != null && budgetDetail.getBudgetGroup().getId()==0)
				budgetDetail.setBudgetGroup(null);
			if(!(budgetReAppropriationView.getBudgetDetail().getBudgetGroup()==null && isMandatoryGridFieldEmpty(budgetReAppropriationView)))
				return true;
		}
		return false;
	}

	

	/**
	 * This api checks whether the amount being deducted is greater than the budget available. If it is greater, a validation exception
	 * is thrown.
	 * @param reAppropriation - The budget reappropriation being created.(This could be the addition or the deduction reappropriation)
	 * @return
	 */
	public void validateDeductionAmount(BudgetReAppropriation appropriation) {
		BigDecimal multiplicationFactor ;
		if(appropriation.getBudgetDetail().getPlanningPercent()!=null){
			multiplicationFactor = appropriation.getBudgetDetail().getPlanningPercent().divide(new BigDecimal(String.valueOf(100)));
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Calculating multiplicationFactor from PlanningPercent : "+multiplicationFactor);
		}else{
			multiplicationFactor = new BigDecimal(Double.parseDouble(getAppConfigFor("EGF","planning_budget_multiplication_factor")));
			if(LOGGER.isDebugEnabled())     LOGGER.debug("MultiplicationFactor from AppConfig(planning_budget_multiplication_factor) : "+multiplicationFactor);
		}
		BigDecimal deductionAmount = appropriation.getOriginalDeductionAmount();
		if(deductionAmount!=null && BigDecimal.ZERO.compareTo(deductionAmount)==-1){
			if(deductionAmount.compareTo(appropriation.getBudgetDetail().getBudgetAvailable().divide(multiplicationFactor))==1 || !canDeduct(appropriation)){
				throw new ValidationException(Arrays.asList(new ValidationError("budget.deduction.greater.than.available","budget.deduction.greater.than.available"))); 
			}
		}
	}

	//checks if the deduction amount is greater than the available amount(i.e, approved-actuals)
	 private boolean canDeduct(BudgetReAppropriation appropriation){
		if(appropriation==null || appropriation.getOriginalDeductionAmount()==null || BigDecimal.ZERO.equals(appropriation.getOriginalDeductionAmount()))
			return true;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		BudgetDetail budgetDetail = appropriation.getBudgetDetail();
		if(budgetDetail.getFund()!=null && budgetDetail.getFund().getId()!=null)
			paramMap.put("fundid", budgetDetail.getFund().getId());
		if(budgetDetail.getExecutingDepartment()!=null && budgetDetail.getExecutingDepartment().getId()!=null)
			paramMap.put("deptid", budgetDetail.getExecutingDepartment().getId());
		if(budgetDetail.getFunction()!=null && budgetDetail.getFunction().getId()!=null)
			paramMap.put("functionid", budgetDetail.getFunction().getId());
		if(budgetDetail.getFunctionary()!=null && budgetDetail.getFunctionary().getId()!=null)
			paramMap.put("functionaryid",budgetDetail.getFunctionary().getId());
		if(budgetDetail.getScheme()!=null && budgetDetail.getScheme().getId()!=null)
			paramMap.put("schemeid",budgetDetail.getScheme().getId());
		if(budgetDetail.getSubScheme()!=null && budgetDetail.getSubScheme().getId()!=null)
			paramMap.put("subschemeid",budgetDetail.getSubScheme().getId());
		if(budgetDetail.getBoundary()!=null && budgetDetail.getBoundary().getId()!=null)
			paramMap.put("boundaryid", budgetDetail.getBoundary().getId());
		paramMap.put("budgetheadid", budgetDetail.getBudgetGroup().getId());
		paramMap.put("glcodeid", budgetDetail.getBudgetGroup().getMinCode().getId());
		paramMap.put(Constants.ASONDATE, appropriation.getAsOnDate());
		BigDecimal actualBudgetUtilized = budgetDetailsDAO.getActualBudgetUtilized(paramMap);
		BigDecimal billAmount = budgetDetailsDAO.getBillAmountForBudgetCheck(paramMap);
		BigDecimal approvedAmount = appropriation.getBudgetDetail().getApprovedAmount();
		approvedAmount = approvedAmount==null?BigDecimal.ZERO:approvedAmount;
		BigDecimal reAppropriationsTotal = budgetDetail.getApprovedReAppropriationsTotal();
		reAppropriationsTotal = reAppropriationsTotal==null?BigDecimal.ZERO:reAppropriationsTotal;
		approvedAmount=approvedAmount.add(reAppropriationsTotal);
		//if(LOGGER.isInfoEnabled())     LOGGER.info("*****************RIGHT side>"+approvedAmount.subtract(actualBudgetUtilized==null?BigDecimal.ZERO:actualBudgetUtilized).subtract(	billAmount==null?BigDecimal.ZERO:billAmount));
		if(appropriation.getOriginalDeductionAmount().compareTo(
				approvedAmount.subtract(actualBudgetUtilized==null?BigDecimal.ZERO:actualBudgetUtilized).subtract(billAmount==null?BigDecimal.ZERO:billAmount))>0)
			return false;
		return true;
	 }

	 public BudgetReAppropriationMisc createReAppropriationMisc(String actionName,BudgetReAppropriationMisc appropriationMisc,BudgetDetail detail,Position position){
		BudgetReAppropriationMisc misc = new BudgetReAppropriationMisc();
		misc.setReAppropriationDate(appropriationMisc.getReAppropriationDate());
		misc.setRemarks(appropriationMisc.getRemarks());
		misc.setSequenceNumber(getSequenceNumber(detail));
		//This fix is for Phoenix Migration.misc = miscWorkflowService.start(misc, position);
		miscWorkflowService.transition(actionName, misc, misc.getRemarks());
		return misc;
	}
	
	protected String getSequenceNumber(BudgetDetail detail){
		Script sequenceNumberScript = (Script) persistenceService.findAllByNamedQuery(Script.BY_NAME, "egf.budget.reappropriation.sequence.generator").get(0);
		return "";//This fix is for Phoenix Migration.(String) sequenceNumberScript.eval(Script.createContext("wfItem",detail,"sequenceGenerator",sequenceGenerator));
	}
	
	public BudgetReAppropriation findBySequenceNumberAndBudgetDetail(String sequenceNumber,Long budgetDetailId){
		return (BudgetReAppropriation) persistenceService.find("from BudgetReAppropriation b where b.reAppropriationMisc.sequenceNumber=? and b.budgetDetail.id=?", sequenceNumber,budgetDetailId);
	}

	public BudgetReAppropriationMisc performActionOnMisc(String action, BudgetReAppropriationMisc reApp,String comment) {
		BudgetReAppropriationMisc misc = miscWorkflowService.transition(action, reApp,comment);
		//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().flush();
		return misc;
	}
	
	/**
	 * This api updates the budget available amount for which the budget reappropriation is being done. The budget available is 
	 * calculated as,
	 * budget available = budget available + (additional amount * multiplication factor)    for addition and
	 * budget available = budget available - (deduction amount * multiplication factor)    for deduction
	 * @param reAppropriation - The budget reappropriation being created.(This could be the addition or the deduction reappropriation)
	 * @return
	 */
	public void updatePlanningBudget(BudgetReAppropriation reAppropriation){
		//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().flush();
		//BigDecimal multiplicationFactor = new BigDecimal(Double.parseDouble(getAppConfigFor("EGF","planning_budget_multiplication_factor")));
		BudgetDetail budgetDetail = (BudgetDetail) budgetDetailService.find("from BudgetDetail where id=?",reAppropriation.getBudgetDetail().getId());
		BigDecimal budgetAvailable = budgetDetail.getBudgetAvailable()==null?BigDecimal.ZERO:budgetDetail.getBudgetAvailable();
		BigDecimal Budgetapproved=BigDecimal.ZERO;
		BigDecimal planningBudgetApproved=BigDecimal.ZERO;
		BigDecimal planningBudgetUsage=BigDecimal.ZERO;  
		//budgetAvailable= (be+addrelease)*planingpercent - consumption
		
		Budgetapproved = budgetDetail.getApprovedAmount().add(budgetDetail.getApprovedReAppropriationsTotal());
		
		if(reAppropriation.getAdditionAmount()!=null && reAppropriation.getAdditionAmount().compareTo(BigDecimal.ZERO)==1){
			Budgetapproved	= Budgetapproved.add(reAppropriation.getAdditionAmount());
		}else if(reAppropriation.getDeductionAmount()!=null && reAppropriation.getDeductionAmount().compareTo(BigDecimal.ZERO)==1){
			Budgetapproved = Budgetapproved.subtract(reAppropriation.getDeductionAmount());
		}
		if(budgetDetail.getPlanningPercent()==null){
			
			planningBudgetApproved = Budgetapproved;
			
		}else {
			planningBudgetApproved = (Budgetapproved.multiply(budgetDetail.getPlanningPercent())).divide(new BigDecimal(String.valueOf(100)));
			
			
		}
		
		planningBudgetUsage=budgetDetailsDAO.getPlanningBudgetUsage(budgetDetail);
		budgetAvailable = planningBudgetApproved.subtract(planningBudgetUsage);
		budgetDetail.setBudgetAvailable(budgetAvailable);
		budgetDetailService.update(budgetDetail);
		//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().flush();
	}
	
	protected BigDecimal zeroOrValue(BigDecimal value) {
		return value==null?BigDecimal.ZERO:value;
	}

	private String getAppConfigFor(String module,String key) {
		try {
			List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key);
			return list.get(0).getValue().toString();
		} catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError(key+" not defined in appconfig",key+" not defined in appconfig")));
		}
	}

	
	
	public List<BudgetReAppropriation> getNonApprovedReAppByUser(Integer userId, BudgetDetail budgetDetail, CFinancialYear financialYear){
		StringBuffer query = new StringBuffer();
		query.append("from BudgetReAppropriation where state.value='NEW' and createdBy.id="+userId+" and budgetDetail.budget.financialYear.id="+financialYear.getId());
		if(budgetDetail.getExecutingDepartment()!=null && budgetDetail.getExecutingDepartment().getId()!=null && budgetDetail.getExecutingDepartment().getId()!=0)
			query.append(" and budgetDetail.executingDepartment.id="+budgetDetail.getExecutingDepartment().getId());
		if(budgetDetail.getFund()!=null && budgetDetail.getFund().getId()!=null && budgetDetail.getFund().getId()!=0)
			query.append(" and budgetDetail.fund.id="+budgetDetail.getFund().getId());
		if(budgetDetail.getFunction()!=null && budgetDetail.getFunction().getId()!=null && budgetDetail.getFunction().getId()!=0)
			query.append(" and budgetDetail.function.id="+budgetDetail.getFunction().getId());
		if(budgetDetail.getFunctionary()!=null && budgetDetail.getFunctionary().getId()!=null && budgetDetail.getFunctionary().getId()!=0)
			query.append(" and budgetDetail.functionary.id="+budgetDetail.getFunctionary().getId());
		if(budgetDetail.getScheme()!=null && budgetDetail.getScheme().getId()!=null && budgetDetail.getScheme().getId()!=0)
			query.append(" and budgetDetail.scheme.id="+budgetDetail.getScheme().getId());
		if(budgetDetail.getSubScheme()!=null && budgetDetail.getSubScheme().getId()!=null && budgetDetail.getSubScheme().getId()!=0)
			query.append(" and budgetDetail.subScheme.id="+budgetDetail.getSubScheme().getId());
		if(budgetDetail.getBoundary()!=null && budgetDetail.getBoundary().getId()!=null && budgetDetail.getBoundary().getId()!=0)
			query.append(" and budgetDetail.boundary.id="+budgetDetail.getBoundary().getId());
		query = query.append(" order by budgetDetail.budgetGroup ");
		return findAllBy(query.toString());
	}

	public void setBudgetDetailsDAO(BudgetDetailsHibernateDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

}

