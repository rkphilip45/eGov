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
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetProposalBean;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.services.budget.BudgetDetailService;
import org.egov.services.budget.BudgetService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.BudgetDetailHelper;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
/*@Results(value={
		@Result(name="PDF",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/pdf",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport.pdf"}),
		@Result(name="XLS",type=StreamResult.class,value=Constants.INPUT_STREAM, params={Constants.INPUT_NAME,Constants.INPUT_STREAM,Constants.CONTENT_TYPE,"application/xls",Constants.CONTENT_DISPOSITION,"no-cache;filename=BudgetReport.xls"})
	})*/
@Results({ 
	@Result(name = "reportview", type = "stream", location = "inputStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
@ParentPackage("egov") 
public class BudgetProposalAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final String ACTIONNAME="actionName";
	private static final Logger    LOGGER    = Logger.getLogger(BudgetProposalAction.class);
	private BudgetProposalBean bpBean=new BudgetProposalBean();
	private BudgetDetail budgetDetail;
	private Budget topBudget;
	private VoucherService voucherService;
	protected FinancialYearHibernateDAO financialYearDAO;
	protected String currentfinYearRange = "";
	protected String nextfinYearRange = "";   
	protected String previousfinYearRange = "";
	protected String twopreviousfinYearRange = "";
	private List<BudgetDetail> budgetDetailList=new ArrayList<BudgetDetail>();  
	protected List<BudgetDetail> savedbudgetDetailList=new ArrayList<BudgetDetail>();
	protected BudgetDetailConfig budgetDetailConfig;
	protected BudgetDetailService budgetDetailService;
	protected BudgetService budgetService;
	private BudgetDetailHelper budgetDetailHelper;
	private Map<Long,String> previuosYearBudgetDetailMap = new HashMap<Long, String>();
	private Map<Long,String> beforePreviousYearBudgetDetailMap = new HashMap<Long, String>();
	private Map<String,BigDecimal> budgetDetailIdsAndAmount = new HashMap<String, BigDecimal>();
	private Map<String,BigDecimal> previousYearBudgetDetailIdsAndAmount = new HashMap<String, BigDecimal>();
	private Map<String,BigDecimal> twopreviousYearBudgetDetailIdsAndAmount = new HashMap<String, BigDecimal>();
	
	private Map<String, BigDecimal> uniqueNoAndBEMap = new HashMap<String, BigDecimal>();
	private Map<String, BigDecimal> uniqueNoAndApprMap = new HashMap<String, BigDecimal>();
	
	private Map<String, String> majorCodeAndNameMap = new TreeMap<String, String>();
	private String wfitemstate;
	private Integer defaultDept;   
	private Department    department;
	private List<BudgetProposalBean> bpBeanList = new ArrayList<BudgetProposalBean>();
	private static final String HEADING = "heading";
	private static final String MAJORCODE = "majorcode";
	private static final String DETAIL = "detail";  
	private static final String TOTAL = "total";
	private static final String FAILURE = "failure";
	private static final String SUCCESSFUL ="successful";
	private Date asOndate;
	private Date headerAsOnDate;
	private GenericHibernateDaoFactory genericDao;
	private InputStream inputStream;
	private ReportHelper reportHelper;
	private Long docNo;              

	private List<BudgetProposalBean> bpBeanList2;
	protected WorkflowService<Budget> budgetWorkflowService;
	protected EisCommonService eisCommonService;
	private boolean consolidatedScreen=false;
	private boolean allfunctionsArrived;
	private Integer approverUserId;
	private Integer userId;
	private String comment;
	private String actionName="";

	private CFinancialYear financialYear;
	private CFinancialYear prevFinancialYear;
	private CFinancialYear nextFinancialYear;
	private CFinancialYear beforeLastFinancialYear;
	private boolean hod;
	private boolean updateApprovedRE;
	private boolean asstFMU = false;
	private boolean functionHeading = true;
	private boolean deptHeading = true;
	private String functionsNotYetReceiced;
	private Map<Long,BudgetGroup> budgetGroupMap;
	private Map<Integer,Fund> fundMap;
	private Map<Long,CFunction> functionMap;
	private Map<Integer,Department> deptMap;
	List<AppConfigValues> excludelist = new ArrayList<AppConfigValues>();
	protected EisUtilService eisService;
	

	//report
	private String contentType;
	private String fileName;
	private ReportService reportService;
	private String amountField;
	private BigDecimal amount;
	private Long detailId;
	private String factor;
	private Long validId;
	private BigDecimal bigThousand = new BigDecimal(1000);
	
	
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}


	@Override
	public String execute() throws Exception {
		
		 super.execute();
		 return update();
	}


	public Object getModel() {
		// TODO Auto-generated method stub
		return budgetDetail;
	}

   
    @Override
    public void prepare() {
        super.prepare();
    }
    
    
@Action(value="/budget/budgetProposal-modifyDetailList")
	public String modifyDetailList() {
		loadToMasterDataMap();
		if(LOGGER.isInfoEnabled())     LOGGER.info("starting modifyDetailList...");
		if(parameters.get("budgetDetail.id")[0]!=null){
			budgetDetail=(BudgetDetail) persistenceService.find("from BudgetDetail where id=?",Long.valueOf(parameters.get("budgetDetail.id")[0]));
			setTopBudget(budgetDetail.getBudget());
		}
        /*computePreviousYearRange();
        computeTwopreviousYearRange();*/
		financialYear = topBudget.getFinancialYear();
		currentfinYearRange=financialYear.getFinYearRange();
		   
		//This fix is for Phoenix Migration.	nextFinancialYear=getFinancialYearDAO().getNextFinancialYearByDate(financialYear.getStartingDate());
		nextfinYearRange=nextFinancialYear.getFinYearRange();
		
		prevFinancialYear = getFinancialYearDAO().getPreviousFinancialYearByDate(financialYear.getStartingDate());
		previousfinYearRange=prevFinancialYear.getFinYearRange();
		
		//This fix is for Phoenix Migration.	beforeLastFinancialYear = getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());
		twopreviousfinYearRange=beforeLastFinancialYear.getFinYearRange();
		
		budgetDetailApprove();
        loadApproverUser(savedbudgetDetailList);
		if(LOGGER.isInfoEnabled())     LOGGER.info("completed modifyDetailList");
		/*return Constants.DETAILLIST;  */
		setDocNo(budgetDetail.getDocumentNumber()); 
		return Constants.DETAILLIST;
	}
	
@Action(value="/budget/budgetProposal-modifyBudgetDetailList")
	public String modifyBudgetDetailList(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting modifyBudgetDetailList..............");
		
		populateBudgetDetailReport();
		loadApproverUser(budgetDetailList);
		return Constants.DETAILLIST;
	}


	private void populateBudgetDetailReport() {
		loadToMasterDataMap();   
		if(budgetDetail.getBudget().getId()!=null)
    	{
    		topBudget=budgetService.find("from Budget where id=?",budgetDetail.getBudget().getId());
    		budgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where budget.id=?",Long.valueOf(parameters.get("budgetDetail.budget.id")[0]));
    	}
    	consolidatedScreen=budgetDetailService.toBeConsolidated();
    	hod = isHOD(); 
    	if(hod)
    	 {
    		 allfunctionsArrived = validateForAllFunctionsMappedForDept(topBudget,getPosition());
    	 }
    	
		financialYear = topBudget.getFinancialYear();
		currentfinYearRange=financialYear.getFinYearRange();
		
		//This fix is for Phoenix Migration.nextFinancialYear=getFinancialYearDAO().getNextFinancialYearByDate(financialYear.getStartingDate());
		nextfinYearRange=nextFinancialYear.getFinYearRange();
		
		prevFinancialYear = getFinancialYearDAO().getPreviousFinancialYearByDate(financialYear.getStartingDate());
		previousfinYearRange=prevFinancialYear.getFinYearRange();
		
		//This fix is for Phoenix Migration.beforeLastFinancialYear = getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());
		twopreviousfinYearRange=beforeLastFinancialYear.getFinYearRange();
		
		budgetApprove();
		if(isConsolidatedScreen()){
			divideByThousand();
		}
		loadApproverUser(budgetDetailList);
		
	}
	
    private void loadToMasterDataMap() {
    	if(LOGGER.isInfoEnabled())     LOGGER.info("Starting loadToMasterDataMap...... ");
    	EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
    	List<BudgetGroup> bgList=(List<BudgetGroup>) masterCache.get("egf-budgetGroup");
    	budgetGroupMap=new HashMap<Long, BudgetGroup>();
    	for(BudgetGroup bg:bgList)
		{
			budgetGroupMap.put(bg.getId(),bg);
		}
    	List<CFunction> fnList=(List<CFunction>) masterCache.get("egi-function");
    	functionMap=new HashMap<Long, CFunction>();
    	for(CFunction fn:fnList)  
		{
			functionMap.put(fn.getId(),fn);       
		}
		
    	List<Fund> fundList=(List<Fund>) masterCache.get("egi-fund");
    	fundMap=new HashMap<Integer, Fund>();
    	for(Fund f:fundList)
		{
    		fundMap.put(f.getId(),f);
		}
    	List<Department> deptList=(List<Department>) masterCache.get("egi-department");
    	deptMap=new HashMap<Integer, Department>();
    	for(Department d:deptList)
		{
    		deptMap.put(d.getId().intValue(),d);
		}
    	excludelist=genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"exclude_status_forbudget_actual");
    	if(excludelist.isEmpty())
			throw new ValidationException("","exclude_status_forbudget_actual is not defined in AppConfig");
    	if(LOGGER.isInfoEnabled())     LOGGER.info("Finished loadToMasterDataMap...... ");
	}
@SkipValidation
    public String ajaxUpdateBudgetDetail(){
    	
    	
    	if(validateOwner())
    	{
    	if(factor.equalsIgnoreCase("thousand"))
    	 {
    		   amount=amount.multiply(bigThousand);	   
    	 }
    	
    	String query="update egf_budgetdetail set  "+amountField+"=:amount,Modifiedby=:modifiedby,modifieddate=:modifiedate  where id=:detailId";
    	if(LOGGER.isInfoEnabled())     LOGGER.info(query);
    	
    	Query updateQuery = HibernateUtil.getCurrentSession().createSQLQuery(query)
    	.setBigDecimal("amount", amount)
    	.setLong("detailId", detailId)	
    	.setDate("modifiedate", new java.sql.Date(new Date().getTime()))
    	.setInteger("modifiedby", Integer.valueOf(EGOVThreadLocals.getUserId()));
    	  	int executeUpdate = updateQuery.executeUpdate();
          if(executeUpdate==1)  
          {
        	  return SUCCESSFUL;  
          }else
          {
        	  return FAILURE;
          }
    	  	
    	}else
    	{
    		return FAILURE;
    	}
    		
    }

@Action(value="/budget/budgetProposal-ajaxDeleteBudgetDetail")
	public String ajaxDeleteBudgetDetail(){
        try {
			if(bpBean.getId() != null && bpBean.getNextYrId() != null){
			    HibernateUtil.getCurrentSession().createSQLQuery("delete from egf_budgetdetail where id in ("+bpBean.getId()+","+bpBean.getNextYrId()+")").executeUpdate();
			   HibernateUtil.getCurrentSession().flush();
			}
		} catch (HibernateException e) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("error while deleting");
			return FAILURE;  
		}
        return SUCCESSFUL;
    }    
	
	public boolean isHod() {
		return hod;
	}


	public void setHod(boolean hod) {
		this.hod = hod;
	}


	public void budgetDetailApprove(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting budgetDetailApprove()..............");
			
		
			   //if u want only selected function centre filter here by owner  fecthing logic to be changed only 
		String query =" from BudgetDetail bd where bd.budget=? and (state.value='END' or state.owner=?) and bd.function="+budgetDetail.getFunction().getId()+"  order by bd.function.name,bd.budgetGroup.name"	;
		savedbudgetDetailList=budgetDetailService.findAllBy(query, topBudget, getPosition());
		
		// check what actuals needs to be shown for next year be AND possible remove if
		/*CFinancialYear previousYearFor = budgetDetailHelper.getPreviousYearFor(topBudget.getFinancialYear());*/
		if(!savedbudgetDetailList.isEmpty()){
			populateMajorCodewiseData();
			populateNextYrBEinBudgetDetailList();
			populateMajorCodewiseDetailData();
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("finished loading detail List--------------------------------------------------------------");
	}
	
	public void budgetApprove(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting budgetApprove..............");
		List<CFunction> functionList = (List<CFunction>) persistenceService.findAllBy("select distinct f from BudgetDetail bd inner join bd.function as f  where budget=? order by f.name ",this.topBudget);
		populateMajorCodewiseDataAcrossFunction();
int i=0;

		for(CFunction function : functionList){
			if(LOGGER.isInfoEnabled())     LOGGER.info("Starting budgetDetailApprove...for functin ....centre......."+function.getName() +"     count "+ ++i);
			budgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where budget=? and function=? order by budgetGroup.name",this.topBudget, function);
			budgetDetailApprove();
			if(LOGGER.isInfoEnabled())     LOGGER.info("Finished budgetDetailApprove...for functin ....centre......."+function.getName());
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("Finished budgetApprove");       
	}
	
	public void divideByThousand(){
		BigDecimal bigThousand = new BigDecimal(1000);
		BigDecimal bigZero = new BigDecimal(0).setScale(2);
		
		for(BudgetProposalBean bpBean : bpBeanList){
			if(!bpBean.getRowType().equals(this.HEADING)){
				if(bpBean.getPreviousYearActuals()!=null && !bpBean.getPreviousYearActuals().equalsIgnoreCase("0.00"))
					bpBean.setPreviousYearActuals(new BigDecimal(bpBean.getPreviousYearActuals()).divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				else
					bpBean.setPreviousYearActuals("0");
				if(bpBean.getTwoPreviousYearActuals()!=null && !bpBean.getTwoPreviousYearActuals().equalsIgnoreCase("0.00"))
					bpBean.setTwoPreviousYearActuals(new BigDecimal(bpBean.getTwoPreviousYearActuals()).divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				else
					bpBean.setTwoPreviousYearActuals("0");
				if(bpBean.getCurrentYearActuals()!=null && !bpBean.getCurrentYearActuals().equalsIgnoreCase("0.00"))
					bpBean.setCurrentYearActuals(new BigDecimal(bpBean.getCurrentYearActuals()).divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				else
					bpBean.setCurrentYearActuals("0");
				
				if(bpBean.getCurrentYearBE()!=null &&!bpBean.getCurrentYearBE().equalsIgnoreCase("0.00"))
					bpBean.setCurrentYearBE(new BigDecimal(bpBean.getCurrentYearBE()).divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				else
					bpBean.setCurrentYearBE("0");
				if(bpBean.getReappropriation()!=null && !bpBean.getReappropriation().equalsIgnoreCase("0.00"))
					bpBean.setReappropriation(new BigDecimal(bpBean.getReappropriation()).divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				else
					bpBean.setReappropriation("0");
				if(bpBean.getTotal()!=null &&!bpBean.getTotal().equalsIgnoreCase("0.00"))
					bpBean.setTotal(new BigDecimal(bpBean.getTotal()).divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				else
					bpBean.setTotal("0");
				if(bpBean.getAnticipatory()!=null && !bpBean.getAnticipatory().equalsIgnoreCase("0.00"))
					bpBean.setAnticipatory(new BigDecimal(bpBean.getAnticipatory()).divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				else                          
					bpBean.setAnticipatory("0");
				if(bpBean.getProposedRE()!=null && !bpBean.getProposedRE().equals(bigZero))
					bpBean.setProposedRE(bpBean.getProposedRE().divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP));
				else
					bpBean.setProposedRE(bigZero.setScale(0));
				if(bpBean.getProposedBE()!=null && !bpBean.getProposedBE().equals(bigZero))
					bpBean.setProposedBE(bpBean.getProposedBE().divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP));
				else
					bpBean.setProposedBE(bigZero.setScale(0));
				
				if(updateApprovedRE){  
					
					bpBean.setApprovedRE(bpBean.getProposedRE());
					if(bpBean.getBudgetGroup().startsWith("2101001") || bpBean.getBudgetGroup().startsWith("2101002")){
						bpBean.setApprovedBE(bpBean.getApprovedRE().multiply(new BigDecimal(1.25)).setScale(0,BigDecimal.ROUND_HALF_UP));
					}else{
						bpBean.setApprovedBE(bpBean.getProposedRE().setScale(0,BigDecimal.ROUND_HALF_UP));						
					}
					
				}else{
					if(bpBean.getApprovedRE()!=null && !bpBean.getApprovedRE().equals(bigZero))
						bpBean.setApprovedRE(bpBean.getApprovedRE().divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP));
					else
						bpBean.setApprovedRE(bigZero.setScale(0));
					if(bpBean.getApprovedBE()!=null && !bpBean.getApprovedBE().equals(bigZero))
						bpBean.setApprovedBE(bpBean.getApprovedBE().divide(bigThousand).setScale(0,BigDecimal.ROUND_HALF_UP));
					else
						bpBean.setApprovedBE(bigZero.setScale(0));
				}
			}
		}
	}
	
	void populateMajorCodewiseData(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting populateMajorCodewiseData()...........");
		Map<String, BigDecimal> majorCodeAndCurrentActuals = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndPreviousActuals = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBeforePreviousActuals = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBEMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndAppropriationMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndAnticipatoryMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndOriginalMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBENextYrMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndApprovedMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBENextYrApprovedMap = new HashMap<String, BigDecimal>();
		
		Position pos = getPosition();
		
		majorCodeAndNameMap.clear();
		
		List<Object[]> resultMajorCode = budgetDetailService.fetchMajorCodeAndName(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		addToMap(resultMajorCode,majorCodeAndNameMap);	
		 
		List<Object[]> resultCurrentActuals = budgetDetailService.fetchMajorCodeAndActuals(financialYear,topBudget, this.asOndate, budgetDetail.getFunction(),budgetDetail.getExecutingDepartment(), pos);
		addToMapStringBigDecimal(resultCurrentActuals,majorCodeAndCurrentActuals);
	
		List<Object[]> resultPreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(prevFinancialYear,topBudget, null, budgetDetail.getFunction(),budgetDetail.getExecutingDepartment(), pos);
		addToMapStringBigDecimal(resultPreviousActuals,majorCodeAndPreviousActuals);
		
		List<Object[]> resultBeforePreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(beforeLastFinancialYear,topBudget, null, budgetDetail.getFunction(),budgetDetail.getExecutingDepartment(), pos);
		addToMapStringBigDecimal(resultBeforePreviousActuals,majorCodeAndBeforePreviousActuals);
		
		List<Object[]> resultMajorCodeBE = budgetDetailService.fetchMajorCodeAndBEAmount(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		addToMapStringBigDecimal(resultMajorCodeBE,majorCodeAndBEMap);
		
		List<Object[]> resultMajorCodeAppropriation = budgetDetailService.fetchMajorCodeAndAppropriation(topBudget, budgetDetail, budgetDetail.getFunction(), pos, this.asOndate);
		addToMapStringBigDecimal(resultMajorCodeAppropriation,majorCodeAndAppropriationMap);
		
		List<Object[]> resultMajorCodeAncipatory = budgetDetailService.fetchMajorCodeAndAnticipatory(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
	//	addToMapStringBigDecimal(resultMajorCodeAncipatory,majorCodeAndAnticipatoryMap,majorCodeAndOriginalMap,majorCodeAndApprovedMap);
		for (Object[] row : resultMajorCodeAncipatory) {
			majorCodeAndAnticipatoryMap.put(row[0].toString(), ((BigDecimal)row[1]).setScale(2));
			majorCodeAndOriginalMap.put(row[0].toString(), ((BigDecimal)row[2]).setScale(2));
			majorCodeAndApprovedMap.put(row[0].toString(), ((BigDecimal)row[3]).setScale(2));     
        }
		
		//List<Object[]> resultMajorCodeOriginal = budgetDetailService.fetchMajorCodeAndOriginalAmount(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		//addToMapStringBigDecimal(resultMajorCodeOriginal,majorCodeAndOriginalMap);
		
		List<Object[]> resultMajorCodeBENextYr = budgetDetailService.fetchMajorCodeAndBENextYr(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		//addToMapStringBigDecimal(resultMajorCodeBENextYr,majorCodeAndBENextYrMap,majorCodeAndBENextYrApprovedMap);
		
		for (Object[] row : resultMajorCodeBENextYr) {
			majorCodeAndBENextYrMap.put(row[0].toString(), ((BigDecimal)row[1]).setScale(2));
			majorCodeAndBENextYrApprovedMap.put(row[0].toString(), ((BigDecimal)row[2]).setScale(2));
			
        }
		
		//List<Object[]> resultMajorCodeApproved = budgetDetailService.fetchMajorCodeAndApprovedAmount(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		//addToMapStringBigDecimal(resultMajorCodeApproved,majorCodeAndApprovedMap);
		
		//List<Object[]> resultMajorCodeBENextYrApproved = budgetDetailService.fetchMajorCodeAndBENextYrApproved(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		//addToMapStringBigDecimal(resultMajorCodeBENextYrApproved,majorCodeAndBENextYrApprovedMap);
        
        if(deptHeading){
        	bpBeanList.add(new BudgetProposalBean(budgetDetail.getExecutingDepartment().getName(), this.HEADING));
        	deptHeading = false;
        }
        if(functionHeading){
	        bpBeanList.add(new BudgetProposalBean("FUNCTIONWISE BUDGET SUMMARY", this.HEADING));
	        functionHeading = false;
		}
        
        bpBeanList.add(new BudgetProposalBean("FUNCTION CENTRE-"+budgetDetail.getFunction().getName(), this.HEADING));
        
        BudgetProposalBean bpbeanTotal = new BudgetProposalBean();
        
        for(Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()){
        	String formValue = getAppConfigValueByKey("functionWiseBudgetReport-"+entry.getKey());
        	BudgetProposalBean bpbean = new BudgetProposalBean();
        	bpbean.setReference(formValue);
        	bpbean.setRowType(this.MAJORCODE);
        	bpbean.setBudgetGroup(entry.getValue());
        	bpbean.setCurrentYearActuals(majorCodeAndCurrentActuals.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndCurrentActuals.get(entry.getKey()).toString());
        	bpbean.setPreviousYearActuals(majorCodeAndPreviousActuals.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndPreviousActuals.get(entry.getKey()).toString());
        	bpbean.setTwoPreviousYearActuals(majorCodeAndBeforePreviousActuals.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndBeforePreviousActuals.get(entry.getKey()).toString());
        	bpbean.setCurrentYearBE(majorCodeAndBEMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndBEMap.get(entry.getKey()).toString());
        	bpbean.setReappropriation(majorCodeAndAppropriationMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndAppropriationMap.get(entry.getKey()).toString());
        	bpbean.setTotal(new BigDecimal(bpbean.getCurrentYearBE()).add(new BigDecimal(bpbean.getReappropriation())).toString());
        	bpbean.setAnticipatory(majorCodeAndAnticipatoryMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndAnticipatoryMap.get(entry.getKey()).toString());
        	bpbean.setProposedRE(majorCodeAndOriginalMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndOriginalMap.get(entry.getKey()));
        	bpbean.setProposedBE(majorCodeAndBENextYrMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndBENextYrMap.get(entry.getKey()));
        	bpbean.setApprovedRE(majorCodeAndApprovedMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndApprovedMap.get(entry.getKey()));
        	bpbean.setApprovedBE(majorCodeAndBENextYrApprovedMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndBENextYrApprovedMap.get(entry.getKey()));
        	bpBeanList.add(bpbean);
        	
        	computeTotal(bpbeanTotal, bpbean);
        }
        bpBeanList.add(bpbeanTotal);
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished populateMajorCodewiseData()");
	}     
	
	void populateMajorCodewiseDataAcrossFunction(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting populateMajorCodewiseDataAcrossFunction..............");
		Map<String, BigDecimal> majorCodeAndCurrentActuals = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndPreviousActuals = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBeforePreviousActuals = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBEMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndAppropriationMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndAnticipatoryMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndOriginalMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBENextYrMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndApprovedMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> majorCodeAndBENextYrApprovedMap = new HashMap<String, BigDecimal>();
		
		Position pos = getPosition();
		
		List<Object[]> resultMajorCode = budgetDetailService.fetchMajorCodeAndName(topBudget, budgetDetail, null, pos);
		addToMap(resultMajorCode,majorCodeAndNameMap);	
		 
		List<Object[]> resultCurrentActuals = budgetDetailService.fetchMajorCodeAndActuals(financialYear,topBudget, this.asOndate, null,budgetDetail.getExecutingDepartment(), pos);
		addToMapStringBigDecimal(resultCurrentActuals,majorCodeAndCurrentActuals);
	
		List<Object[]> resultPreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(prevFinancialYear,topBudget, null, null,budgetDetail.getExecutingDepartment(), pos);
		addToMapStringBigDecimal(resultPreviousActuals,majorCodeAndPreviousActuals);
		
		List<Object[]> resultBeforePreviousActuals = budgetDetailService.fetchMajorCodeAndActuals(beforeLastFinancialYear,topBudget, null, null,budgetDetail.getExecutingDepartment(), pos);
		addToMapStringBigDecimal(resultBeforePreviousActuals,majorCodeAndBeforePreviousActuals);
		
		List<Object[]> resultMajorCodeBE = budgetDetailService.fetchMajorCodeAndBEAmount(topBudget, budgetDetail, null, pos);
		addToMapStringBigDecimal(resultMajorCodeBE,majorCodeAndBEMap);
		
		List<Object[]> resultMajorCodeAppropriation = budgetDetailService.fetchMajorCodeAndAppropriation(topBudget, budgetDetail, null, pos, this.asOndate);
		addToMapStringBigDecimal(resultMajorCodeAppropriation,majorCodeAndAppropriationMap);
		
		List<Object[]> resultMajorCodeAncipatory = budgetDetailService.fetchMajorCodeAndAnticipatory(topBudget, budgetDetail, null, pos);
		addToMapStringBigDecimal(resultMajorCodeAppropriation,majorCodeAndAnticipatoryMap);
		
		List<Object[]> resultMajorCodeOriginal = budgetDetailService.fetchMajorCodeAndOriginalAmount(topBudget, budgetDetail, null, pos);
		addToMapStringBigDecimal(resultMajorCodeOriginal,majorCodeAndOriginalMap);
		
		List<Object[]> resultMajorCodeBENextYr = budgetDetailService.fetchMajorCodeAndBENextYr(topBudget, budgetDetail, null, pos);
		addToMapStringBigDecimal(resultMajorCodeBENextYr,majorCodeAndBENextYrMap);
		
		List<Object[]> resultMajorCodeApproved = budgetDetailService.fetchMajorCodeAndApprovedAmount(topBudget, budgetDetail, null, pos);
		addToMapStringBigDecimal(resultMajorCodeApproved,majorCodeAndApprovedMap);
		
		List<Object[]> resultMajorCodeBENextYrApproved = budgetDetailService.fetchMajorCodeAndBENextYrApproved(topBudget, budgetDetail, null, pos);
		addToMapStringBigDecimal(resultMajorCodeBENextYrApproved,majorCodeAndBENextYrApprovedMap);
        
        bpBeanList.add(new BudgetProposalBean(budgetDetail.getExecutingDepartment().getName(), this.HEADING));
        deptHeading = false;
        bpBeanList.add(new BudgetProposalBean("DEPARTMENTWISE BUDGET SUMMARY", this.HEADING));
        //bpBeanList.add(new BudgetProposalBean("FUNCTIONWISE EXPENSE BUDGET SUMMARY", this.HEADING));
        
        //bpBeanList.add(new BudgetProposalBean("FUNCTION CENTRE-"+budgetDetail.getFunction().getName(), this.HEADING));
        
        BudgetProposalBean bpbeanTotal = new BudgetProposalBean();
        
        for(Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()){
        	String formValue = getAppConfigValueByKey("functionWiseBudgetReport-"+entry.getKey());
        	BudgetProposalBean bpbean = new BudgetProposalBean();
        	bpbean.setReference(formValue);
        	bpbean.setRowType(this.MAJORCODE);
        	bpbean.setBudgetGroup(entry.getValue());
        	bpbean.setCurrentYearActuals(majorCodeAndCurrentActuals.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndCurrentActuals.get(entry.getKey()).toString());
        	bpbean.setPreviousYearActuals(majorCodeAndPreviousActuals.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndPreviousActuals.get(entry.getKey()).toString());
        	bpbean.setTwoPreviousYearActuals(majorCodeAndBeforePreviousActuals.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndBeforePreviousActuals.get(entry.getKey()).toString());
        	bpbean.setCurrentYearBE(majorCodeAndBEMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndBEMap.get(entry.getKey()).toString());
        	bpbean.setReappropriation(majorCodeAndAppropriationMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndAppropriationMap.get(entry.getKey()).toString());
        	bpbean.setTotal(new BigDecimal(bpbean.getCurrentYearBE()).add(new BigDecimal(bpbean.getReappropriation())).toString());
        	bpbean.setAnticipatory(majorCodeAndAnticipatoryMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2).toString():majorCodeAndAnticipatoryMap.get(entry.getKey()).toString());
        	bpbean.setProposedRE(majorCodeAndOriginalMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndOriginalMap.get(entry.getKey()));
        	bpbean.setProposedBE(majorCodeAndBENextYrMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndBENextYrMap.get(entry.getKey()));
        	bpbean.setApprovedRE(majorCodeAndApprovedMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndApprovedMap.get(entry.getKey()));
        	bpbean.setApprovedBE(majorCodeAndBENextYrApprovedMap.get(entry.getKey())==null?BigDecimal.ZERO.setScale(2):majorCodeAndBENextYrApprovedMap.get(entry.getKey()));
        	bpBeanList.add(bpbean);
        	
        	computeTotal(bpbeanTotal, bpbean);
        }
        bpBeanList.add(bpbeanTotal);
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished populateMajorCodewiseDataAcrossFunction");
	}
	
	void populateMajorCodewiseDetailData(){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting populateMajorCodewiseDetailData()................");
		List<String> mandatoryFields = new ArrayList<String>();
		mandatoryFields.add("fund");
		mandatoryFields.add("function");
		mandatoryFields.add("executingDepartment");
		
		
		Position pos = getPosition();
		
		CFinancialYear financialYear = topBudget.getFinancialYear();
        CFinancialYear lastFinancialYearByDate = getFinancialYearDAO().getPreviousFinancialYearByDate(financialYear.getStartingDate());
        CFinancialYear beforeLastFinancialYearByDate = null;//getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());
		
		List<Object[]> resultCurrentActuals = budgetDetailService.fetchActualsForFinYear(financialYear,mandatoryFields,topBudget,null,this.asOndate,budgetDetail.getExecutingDepartment().getId().intValue(),budgetDetail.getFunction().getId(), excludelist);
		addToMapStringBigDecimal(resultCurrentActuals,budgetDetailIdsAndAmount);  
		
		List<Object[]> resultPreviousActuals = budgetDetailService.fetchActualsForFinYear(lastFinancialYearByDate,mandatoryFields,topBudget,null,null,budgetDetail.getExecutingDepartment().getId().intValue(),budgetDetail.getFunction().getId(), excludelist);
		addToMapStringBigDecimal(resultPreviousActuals,previousYearBudgetDetailIdsAndAmount);
		
		List<Object[]> resultTwoPreviousActuals = budgetDetailService.fetchActualsForFinYear(beforeLastFinancialYearByDate,mandatoryFields,topBudget,null,null,budgetDetail.getExecutingDepartment().getId().intValue(),budgetDetail.getFunction().getId(), excludelist);
		addToMapStringBigDecimal(resultTwoPreviousActuals,twopreviousYearBudgetDetailIdsAndAmount);
		
		List<Object[]> resultUniqueNoBE = budgetDetailService.fetchUniqueNoAndBEAmount(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		addToMapStringBigDecimal(resultUniqueNoBE,uniqueNoAndBEMap);
		
		List<Object[]> resultUniqueNoAppr = budgetDetailService.fetchUniqueNoAndApprAmount(topBudget, budgetDetail, budgetDetail.getFunction(), pos);
		addToMapStringBigDecimal(resultUniqueNoAppr,uniqueNoAndApprMap);
		
		
		for(Map.Entry<String, String> entry : majorCodeAndNameMap.entrySet()){
			String formValue = getAppConfigValueByKey("functionWiseBudgetReport-"+entry.getKey());
			boolean detailExist=false;
			boolean headerAdded=false;
			
			
			BudgetProposalBean bpbeanTotal = new BudgetProposalBean();
			for(BudgetDetail bd : savedbudgetDetailList){
				if(entry.getKey().equals(bd.getBudgetGroup().getMinCode().getMajorCode())){
					detailExist=true;
				if(headerAdded==false){
					bpBeanList.add(new BudgetProposalBean(entry.getValue(), this.HEADING, formValue));
					headerAdded=true;
				}
					BudgetProposalBean bpbean = new BudgetProposalBean();
					
					populateBudgetProposalBean(bpbean, bd);
					if(LOGGER.isDebugEnabled())     LOGGER.debug(bd.getUniqueNo()+"---"+budgetDetailIdsAndAmount.get(bd.getUniqueNo()));
					
					bpBeanList.add(bpbean);
					
					computeTotal(bpbeanTotal, bpbean);
				}
			}
			if(detailExist)
			{
			bpBeanList.add(bpbeanTotal);
			}
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("Finished populateMajorCodewiseDetailData()");
	}
	
	private void populateBudgetProposalBean(BudgetProposalBean bpbean, BudgetDetail bd){
	if(LOGGER.isInfoEnabled())     LOGGER.info("Starting populateBudgetProposalBean..... ");
		bpbean.setRowType(this.DETAIL);
		bpbean.setId(bd.getId());
		bpbean.setNextYrId(bd.getNextYrId());
		bpbean.setBudget(topBudget.getName());
		bpbean.setFund(fundMap.get(bd.getFund().getId()).getName());
		bpbean.setFunction(functionMap.get(bd.getFunction().getId()).getName());
		bpbean.setBudgetGroup(budgetGroupMap.get(bd.getBudgetGroup().getId()).getName());
		bpbean.setExecutingDepartment(deptMap.get(bd.getExecutingDepartment().getId()).getCode());
		bpbean.setPreviousYearActuals(previousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo())==null?BigDecimal.ZERO.setScale(2).toString():previousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo()).toString());
		bpbean.setTwoPreviousYearActuals(twopreviousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo())==null?BigDecimal.ZERO.setScale(2).toString():twopreviousYearBudgetDetailIdsAndAmount.get(bd.getUniqueNo()).toString());
		bpbean.setCurrentYearActuals(budgetDetailIdsAndAmount.get(bd.getUniqueNo())==null?BigDecimal.ZERO.setScale(2).toString():budgetDetailIdsAndAmount.get(bd.getUniqueNo()).toString());
		BigDecimal lastBEAmount = uniqueNoAndBEMap.get(bd.getUniqueNo())==null?BigDecimal.ZERO.setScale(2):uniqueNoAndBEMap.get(bd.getUniqueNo());
		BigDecimal approvedReAppropriationsTotal = uniqueNoAndApprMap.get(bd.getUniqueNo())==null?BigDecimal.ZERO.setScale(2):uniqueNoAndApprMap.get(bd.getUniqueNo());  
		bpbean.setCurrentYearBE(lastBEAmount.toString());
		bpbean.setReappropriation(approvedReAppropriationsTotal.toString());
		bpbean.setTotal(lastBEAmount.add(approvedReAppropriationsTotal).toString());
		bpbean.setAnticipatory(bd.getAnticipatoryAmount().setScale(2).toString());
		bpbean.setProposedRE(bd.getOriginalAmount().setScale(2));
		bpbean.setProposedBE(bd.getNextYroriginalAmount().setScale(2));
		bpbean.setApprovedRE(bd.getApprovedAmount().setScale(2));
		bpbean.setApprovedBE(bd.getNextYrapprovedAmount().setScale(2));
		if(LOGGER.isInfoEnabled())     LOGGER.info("before bd.getstate().getExtraInfo1()");
	//	bpbean.setRemarks(bd.getstate().getExtraInfo1());     
		if(LOGGER.isInfoEnabled())     LOGGER.info("Finished populateBudgetProposalBean..... ");    
	}

	private Map<String,String> addToMap(List<Object[]>  tempList,Map<String,String> resultMap) {
		for (Object[] row : tempList) {
			resultMap.put(row[0].toString(), row[1].toString());
        }
		return resultMap;
	}
	
	private Map<String,BigDecimal> addToMapStringBigDecimal(List<Object[]>  tempList,Map<String,BigDecimal> resultMap) {
		for (Object[] row : tempList) {
			resultMap.put(row[0].toString(), ((BigDecimal)row[1]).setScale(2));
        }
		return resultMap;
	}
	
    public void populateNextYrBEinBudgetDetailList(){
    	if(LOGGER.isInfoEnabled())     LOGGER.info("starting populateNextYrBEinBudgetDetailList..................");
    	if(!savedbudgetDetailList.isEmpty()){
    		for(BudgetDetail budgetDetail : savedbudgetDetailList){
    			BudgetDetail nextYrbudgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where uniqueNo=? and budget.referenceBudget=?", budgetDetail.getUniqueNo(), budgetDetail.getBudget());
    			//budgetDetail.setNextYrId(nextYrbudgetDetail.getId());
    			budgetDetail.setNextYroriginalAmount(nextYrbudgetDetail.getOriginalAmount());
    			budgetDetail.setNextYrapprovedAmount(nextYrbudgetDetail.getApprovedAmount());
    			budgetDetail.setNextYrId(nextYrbudgetDetail.getId());
    		}
    	}
    	if(LOGGER.isInfoEnabled())     LOGGER.info("Completed populateNextYrBEinBudgetDetailList");
    }
	
    @SkipValidation
    private void loadApproverUser(List<BudgetDetail> budgetDetailList)
    {
    	if(LOGGER.isInfoEnabled())     LOGGER.info("Starting loadApproverUser.....");
        EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
        Map<String, Object>  map = voucherService.getDesgBYPassingWfItem("BudgetDetail.nextDesg",null,budgetDetail.getExecutingDepartment().getId().intValue());
        addDropdownData("departmentList", masterCache.get("egi-department"));
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);    
        
        List<Map<String,Object>> desgList  =  (List<Map<String,Object>>) map.get("designationList");
        String  strDesgId = "", dName = "";
        boolean bDefaultDeptId = false;
        List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
        Map<String, Object> desgFuncryMap;
        for(Map<String,Object> desgIdAndName : desgList) {
            desgFuncryMap = new HashMap<String, Object>();

            if(desgIdAndName.get("designationName") != null ) {
                desgFuncryMap.put("designationName",(String) desgIdAndName.get("designationName"));
            }

            if(desgIdAndName.get("designationId") != null ) {
                strDesgId = (String) desgIdAndName.get("designationId");
                if(strDesgId.indexOf("~") != -1) {
                    strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
                    dName = (String) desgIdAndName.get("designationId");
                    dName = dName.substring(dName.indexOf('~')+1);
                    bDefaultDeptId = true;
                }
                desgFuncryMap.put("designationId",strDesgId);
            }
            designationList.add(desgFuncryMap);
        }
        map.put("designationList", designationList);

        addDropdownData("designationList", (List<DesignationMaster>)map.get("designationList"));
        if(bDefaultDeptId && !dName.equals("")) {
            Department dept = (Department) persistenceService.find("from Department where deptName like '%"+dName+"' ");
            defaultDept = dept.getId().intValue();
        }
        wfitemstate = map.get("wfitemstate")!=null?map.get("wfitemstate").toString():"";
        if(LOGGER.isInfoEnabled())     LOGGER.info("finished loadApproverUser.....");
    }
    
    
    public boolean isConsolidatedScreen() {
		return consolidatedScreen;
	}


	public void setConsolidatedScreen(boolean consolidatedScreen) {
		this.consolidatedScreen = consolidatedScreen;
	}


	public boolean isAllfunctionsArrived() {
		return allfunctionsArrived;
	}


	public void setAllfunctionsArrived(boolean allfunctionsArrived) {
		this.allfunctionsArrived = allfunctionsArrived;
	}


	public Integer getApproverUserId() {
		return approverUserId;
	}


	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getActionName() {
		return actionName;
	}


	public void setActionName(String actionName) {
		this.actionName = actionName;
	}


	public void setBudgetWorkflowService(
			WorkflowService<Budget> budgetWorkflowService) {
		this.budgetWorkflowService = budgetWorkflowService;
	}


	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}




	private BigDecimal getLastBE(BudgetDetail detail) {
        BudgetDetail detailWithoutBudget = new BudgetDetail();
        detailWithoutBudget.copyFrom(detail);
        detailWithoutBudget.setBudget(null);
        //List<Object[]> previousYearResult;
         CFinancialYear financialYear2 = detail.getBudget().getFinancialYear();
         Long finyearId=financialYear2.getId();
        if(detail.getBudget().getIsbere().equalsIgnoreCase("BE"))
        {
            Date startingDate = financialYear2.getStartingDate();
            Date lastyear = subtractYear(startingDate);
            CFinancialYear lastFinYear =(CFinancialYear) persistenceService.find("from CFinancialYear where startingDate=? and isActive=1",lastyear);
          if(lastFinYear!=null)
          {

              finyearId =lastFinYear.getId();
          }

        }
        BigDecimal approvedAmount=BigDecimal.ZERO;
        List<BudgetDetail> budgetDetail = budgetDetailService.searchByCriteriaWithTypeAndFY(finyearId,"BE",detailWithoutBudget);
            if(budgetDetail!=null && budgetDetail.size()>0)
            {
                approvedAmount = budgetDetail.get(0).getApprovedAmount();
            }
            return approvedAmount.setScale(2);

    }
	
	private BigDecimal getLastBEAppropriations(BudgetDetail detail) {
        BudgetDetail detailWithoutBudget = new BudgetDetail();
        detailWithoutBudget.copyFrom(detail);
        detailWithoutBudget.setBudget(null);
        //List<Object[]> previousYearResult;
         CFinancialYear financialYear2 = detail.getBudget().getFinancialYear();
         Long finyearId=financialYear2.getId();
        if(detail.getBudget().getIsbere().equalsIgnoreCase("BE"))
        {
            Date startingDate = financialYear2.getStartingDate();
            Date lastyear = subtractYear(startingDate);
            CFinancialYear lastFinYear =(CFinancialYear) persistenceService.find("from CFinancialYear where startingDate=? and isActive=1",lastyear);
          if(lastFinYear!=null)
          {

              finyearId =lastFinYear.getId();
          }

        }
        BigDecimal approvedAppropriationAmount=BigDecimal.ZERO;
        List<BudgetDetail> budgetDetail = budgetDetailService.searchByCriteriaWithTypeAndFY(finyearId,"BE",detailWithoutBudget);
            if(budgetDetail!=null && budgetDetail.size()>0)
            {
                approvedAppropriationAmount = budgetDetail.get(0).getApprovedReAppropriationsTotal();
            }
            return approvedAppropriationAmount.setScale(2);

    }
    
    public Date subtractYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }
    
	private String getAppConfigValueByKey(String key){
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",key);
		String appValue = "-1";
		if(appList.isEmpty())
			appValue = " ";
		else
			appValue = appList.get(0).getValue();
		return appValue;
	}
	
	void computeTotal(BudgetProposalBean bpbeanTotal, BudgetProposalBean bpbean){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting computeTotal................");
		bpbeanTotal.setPreviousYearActuals(bpbeanTotal.getPreviousYearActuals() == null?bpbean.getPreviousYearActuals():(new BigDecimal(bpbeanTotal.getPreviousYearActuals())).add((new BigDecimal(bpbean.getPreviousYearActuals()))).toString());
		bpbeanTotal.setTwoPreviousYearActuals(bpbeanTotal.getTwoPreviousYearActuals() == null?bpbean.getTwoPreviousYearActuals():(new BigDecimal(bpbeanTotal.getTwoPreviousYearActuals())).add((new BigDecimal(bpbean.getTwoPreviousYearActuals()))).toString());
		bpbeanTotal.setCurrentYearActuals(bpbeanTotal.getCurrentYearActuals() == null?bpbean.getCurrentYearActuals():(new BigDecimal(bpbeanTotal.getCurrentYearActuals())).add((new BigDecimal(bpbean.getCurrentYearActuals()))).toString());
		bpbeanTotal.setCurrentYearBE(bpbeanTotal.getCurrentYearBE() == null?bpbean.getCurrentYearBE():(new BigDecimal(bpbeanTotal.getCurrentYearBE())).add((new BigDecimal(bpbean.getCurrentYearBE()))).toString());
		bpbeanTotal.setReappropriation(bpbeanTotal.getReappropriation() == null?bpbean.getReappropriation():(new BigDecimal(bpbeanTotal.getReappropriation())).add((new BigDecimal(bpbean.getReappropriation()))).toString());
		bpbeanTotal.setTotal(bpbeanTotal.getTotal() == null?bpbean.getTotal():(new BigDecimal(bpbeanTotal.getTotal())).add((new BigDecimal(bpbean.getTotal()))).toString());
		bpbeanTotal.setAnticipatory(bpbeanTotal.getAnticipatory() == null?bpbean.getAnticipatory():(new BigDecimal(bpbeanTotal.getAnticipatory())).add((new BigDecimal(bpbean.getAnticipatory()))).toString());
		bpbeanTotal.setProposedRE(bpbeanTotal.getProposedRE() == null?bpbean.getProposedRE():bpbeanTotal.getProposedRE().add(bpbean.getProposedRE()).setScale(2));
		bpbeanTotal.setProposedBE(bpbeanTotal.getProposedBE() == null?bpbean.getProposedBE():bpbeanTotal.getProposedBE().add(bpbean.getProposedBE()).setScale(2));
		bpbeanTotal.setApprovedRE(bpbeanTotal.getApprovedRE() == null?bpbean.getApprovedRE():bpbeanTotal.getApprovedRE().add(bpbean.getApprovedRE()).setScale(2));
		bpbeanTotal.setApprovedBE(bpbeanTotal.getApprovedBE() == null?bpbean.getApprovedBE():bpbeanTotal.getApprovedBE().add(bpbean.getApprovedBE()).setScale(2));
		bpbeanTotal.setRowType(this.TOTAL);
		bpbeanTotal.setBudgetGroup("TOTAL");
		if(LOGGER.isInfoEnabled())     LOGGER.info("Finished computeTotal");
	}
	
	   @SkipValidation
	    public String update()
	    {   	
	    //Only save the items
		   if(LOGGER.isDebugEnabled())     LOGGER.debug("Stating updation .....");
	    	if (approverUserId!=null && approverUserId!=-1 ) {
	            userId = approverUserId;
	        }else {
	            userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
	        }

	    	topBudget=budgetService.find("from Budget where id=?",topBudget.getId());
	    	Position positionByUserId = null;//This fix is for Phoenix Migration.eisCommonService.getPositionByUserId(userId);
	        PersonalInformation empForCurrentUser = budgetDetailService.getEmpForCurrentUser();
	        String name="";
			if(empForCurrentUser!=null)
	        	name=empForCurrentUser.getName();   
			if(name==null)  
				name=empForCurrentUser.getEmployeeFirstName();    
	  
	    	if(actionName.contains("save"))
	    	{
	    		if(consolidatedScreen)
	            {
	         	   save(BigDecimal.valueOf(1000));
	            }else
	            {
	            	save(null);
	            }
	    		addActionMessage("Budget/BudgetDetails Saved Succesfully");

	    	}
	    	//Save and Push the items
	    	else if (actionName.contains("forward"))
	    	{
	    		boolean hod = isHOD();
	           if(consolidatedScreen || hod)
	           {
	        	   if(hod)
	        	   {
	        	   save(null);
	        	   }else
	        	   {
	        		   save(BigDecimal.valueOf(1000));
	        	   }
	        	 //This fix is for Phoenix Migration.  topBudget.changeState("Forwarded by "+name, positionByUserId, comment);
	        	   
	           }
	           else {
	        	   
	        	   saveWithForward(positionByUserId,name,hod);
	        	   if(isNextUserHOD(approverUserId) || hod)
	        	   {
	        		 //This fix is for Phoenix Migration.	     topBudget.changeState("Forwarded by "+name, positionByUserId, comment);
	        	   }
	        	  
	           }
	           addActionMessage("Budget/BudgetDetails Forwarded Succesfully to "+budgetService.getEmployeeNameAndDesignationForPosition(positionByUserId));
	    		
	    	}
	    	//Final approval 
	    	else if (actionName.contains("approve"))
	    	{
	    		save(BigDecimal.valueOf(1000));
	    		//This fix is for Phoenix Migration. topBudget.changeState("END",positionByUserId, comment);
	    		addActionMessage("Budget/BudgetDetails Approved Succesfully ");
	    	}
	    	 budgetService.persist(topBudget);
	    	 if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed updation .....");
	    	 return "message";    
	    }
	    
	    private void save(BigDecimal multiplicationFactor)
	    {
	    	String columntoupdate="originalAmount";
	    	int multiplicationValue=1;
	    	if(multiplicationFactor!=null)
	    	{
	    		columntoupdate="approvedAmount";
	    		multiplicationValue=1000;
	    	}
	    	String sql="update egf_budgetdetail set "+columntoupdate+"=:amount,document_Number=:docNo where id=:id";
	    	String commentsql = "";
	    	if(isHOD()){
	    		commentsql="update eg_wf_states set  text1=:text1, value='END' where id=(select state_id from egf_budgetdetail where  id=:id)";
	    	}else{
	    		commentsql="update eg_wf_states set  text1=:text1 where id=(select state_id from egf_budgetdetail where  id=:id)";
	    	}
	    	SQLQuery updateQuery = HibernateUtil.getCurrentSession().createSQLQuery(sql);
	    	SQLQuery updateCommentQuery = HibernateUtil.getCurrentSession().createSQLQuery(commentsql);
	    	int i=0;
	    	
	    	
	    	for(BudgetProposalBean bpBean :bpBeanList)
			{
	    	
	    		if(bpBean==null || bpBean.getId()==null )  
	    			continue;
	    		if(multiplicationFactor!=null)
	    		{    			
	    			updateQuery.setBigDecimal("amount", bpBean.getApprovedRE().multiply(multiplicationFactor));
	    	
	    		}else
	    		{
	    			updateQuery.setBigDecimal("amount", bpBean.getProposedRE());
	    		}
	    		
	    		updateQuery.setString("docNo", bpBean.getDocumentNumber()==null?null:bpBean.getDocumentNumber().toString());
	    		updateQuery.setLong("id", bpBean.getId());
	    		int executeUpdate = updateQuery.executeUpdate();
	    		if(multiplicationFactor!=null)
	    		{    			
	    		updateQuery.setBigDecimal("amount", bpBean.getApprovedBE().multiply(multiplicationFactor));
	    		}else{
	    			updateQuery.setBigDecimal("amount", bpBean.getProposedBE());
	    		}
	    		updateQuery.setLong("id", bpBean.getNextYrId());
	    		int executeUpdate2 = updateQuery.executeUpdate();
	    			updateCommentQuery.setString("text1", bpBean.getRemarks());
	    			updateCommentQuery.setLong("id", bpBean.getId());
	    			updateCommentQuery.executeUpdate(); 
	    		
	    		
	    		i++;
	    		if(LOGGER.isDebugEnabled())     LOGGER.debug("Updated  "+i+"record.....");
	    		if(i%10==0)
	    		{
	    		HibernateUtil.getCurrentSession().flush();
	    			if(LOGGER.isDebugEnabled())     LOGGER.debug("flushed for "+i+"record.....");
	    			
	    		}
				
			}
	    HibernateUtil.getCurrentSession().flush();
	    	if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed Save .....");
	    }
	    
	    private void saveWithForward(Position pos,String name,boolean hod)
	    {
	    	
	    	BudgetDetail bd=null;
	    	BudgetDetail nextYearBd=null;
	    	String stateString="Forwarded by "+name;
	    	int i=0;
	    	if(hod)
	    		stateString="END";
	    	for(BudgetProposalBean bpBean :bpBeanList)
	    	{
	    		if(bpBean==null || bpBean.getId()==null )
	    			continue;
	    		bd=budgetDetailService.find("from BudgetDetail where id=?",bpBean.getId());
	    		bd.setOriginalAmount(bpBean.getProposedRE());
	    		if(bpBean.getDocumentNumber()!=null)
	    			bd.setDocumentNumber(bpBean.getDocumentNumber());
	    		//This fix is for Phoenix Migration.	bd.changeState(stateString, pos, bpBean.getRemarks());
	    		budgetDetailService.persist(bd);
	    		
	    		nextYearBd=budgetDetailService.find("from BudgetDetail where id=?",bpBean.getNextYrId());
	    		nextYearBd.setOriginalAmount(bpBean.getProposedBE());
	    		//This fix is for Phoenix Migration.	nextYearBd.changeState(stateString, pos, bpBean.getRemarks());
	    		budgetDetailService.persist(nextYearBd);
	    		if(LOGGER.isDebugEnabled())     LOGGER.debug("Updated  "+i+"record.....");
	    		i++;
	    		if(i%10==0)
	    		{
	    		HibernateUtil.getCurrentSession().flush();
	    			if(LOGGER.isDebugEnabled())     LOGGER.debug("flushed for "+i+"record.....");
	    		}
				
	    	}
	    	if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed saveWithForward .....");
	    }
	    
	    
	    public String modifyList()
	    {
	    	if(budgetDetail.getBudget().getId()!=null)
	    	{
	    		topBudget=budgetService.find("from Budget where id=?",budgetDetail.getBudget().getId());
	    	}
	    	consolidatedScreen=budgetDetailService.toBeConsolidated();
	    	 if(isHOD())
	    	 {
	    		 allfunctionsArrived = validateForAllFunctionsMappedForDept(topBudget,getPosition());
	    	 }
	    	 
	    	return "detailList";
	    	
	    }
	    
	   private boolean isHOD()
	    {
	    	PersonalInformation emp = null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			Assignment empAssignment =null;//This fix is for Phoenix Migration. eisCommonService.getAssignmentByEmpAndDate(new Date(), emp.getIdPersonalInformation());
			if(empAssignment.getDesigId().getDesignationName().equalsIgnoreCase("assistant"))
			{
				 asstFMU = true;
				 BudgetDetail approvedBd=(BudgetDetail) persistenceService.find(" from  BudgetDetail where budget=? and approvedAmount>0 ",this.topBudget);
				 if(approvedBd!=null)
					 updateApprovedRE=false;
				 else
					 updateApprovedRE = true;
			}else{
				if(empAssignment.getDesigId().getDesignationName().equalsIgnoreCase("CHIEF ACCOUNTS OFFICER")){
				asstFMU = true;
			}
			}
				
			return false;//eisCommonService.getHodById(Integer.valueOf(empAssignment.getId().toString()));
	    }
	   
		public Position getPosition()throws EGOVRuntimeException
		{
			Position pos;
			PersonalInformation emp=null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			pos=null;//This fix is for Phoenix Migration.eisCommonService.getPositionforEmp(emp.getIdPersonalInformation());
			return pos;
		}
	   
	   private boolean isNextUserHOD(Integer approverUserId)
	    {
	    	PersonalInformation emp = null;//This fix is for Phoenix Migration.eisCommonService.getEmpForUserId(approverUserId);
			Assignment empAssignment = null;//This fix is for Phoenix Migration.eisCommonService.getAssignmentByEmpAndDate(new Date(), emp.getIdPersonalInformation());
			return false;//CommonService.getHodById(Integer.valueOf(empAssignment.getId()));
	    }
	   
	   
	   private boolean validateForAllFunctionsMappedForDept(Budget topBudget, Position position)
	   {
		   BudgetDetail bd=budgetDetailService.find("from BudgetDetail  where budget.id=?",topBudget.getId());	   
		   String Query="select distinct(f.name) as functionid from eg_dept_functionmap m,function f where departmentid="+
				   bd.getExecutingDepartment().getId()+" and f.id= m.functionid and m.budgetaccount_Type='"
				   +budgetDetailHelper.accountTypeForFunctionDeptMap(topBudget.getName())+"'"+
				   " minus "+ 
				   " select distinct(f.name) as functionid from egf_budgetdetail bd,eg_wf_states s,function f where bd.budget="
				   +topBudget.getId()+" and bd.state_id=s.id and s.owner="+position.getId()+" and bd.function=f.id order by functionid";	   
		   Query functionsNotUsed = HibernateUtil.getCurrentSession()
				   .createSQLQuery(Query);
		   List<String> notUsedList = (List<String>)functionsNotUsed.list();
		   
		   
		   if (notUsedList.size() > 0) {   
			   functionsNotYetReceiced="";
			   for(String s:notUsedList)
			   {
				   functionsNotYetReceiced=functionsNotYetReceiced+s+" ,";
			   }
			   return false;
			   
		   }else
		   {
			   return true;
		   }
	   }

	   public List<org.egov.infstr.workflow.Action> getValidActions(){
	       List<org.egov.infstr.workflow.Action> validButtons=null;
	           validButtons=budgetWorkflowService.getValidActions(getTopBudget());
	       return validButtons;

	   }
	   public String capitalize(String value){
	        if (value == null || value.length() == 0) return value;
	        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
	    }

		@SuppressWarnings("unchecked")
		public String getUlbName() {
			Query query = HibernateUtil.getCurrentSession().createSQLQuery("select name from companydetail");
			List<String> result = query.list();
			if (result != null)
				return result.get(0);
			return "";
		}
		
		
@Action(value="/budget/budgetProposal-generatePdf")
		public String generatePdf() throws Exception{
			try {
				bpBeanList=new ArrayList<BudgetProposalBean>(); 
				populateBudgetDetailReport();   
				Map<String, Object> reportParams = new HashMap<String, Object>();
				reportParams.put("title",getUlbName());
				reportParams.put("subtitle",getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():"");
				reportParams.put("amount",isConsolidatedScreen()?"Amount in Thousand":"Amount in Rupees");
				reportParams.put("twopreviousfinYearRange",twopreviousfinYearRange);
				reportParams.put("previousfinYearRange",previousfinYearRange);
				reportParams.put("currentfinYearRange",currentfinYearRange);
				reportParams.put("nextfinYearRange",nextfinYearRange);
				//bpBeanList=new ArrayList<BudgetProposalBean>(); 
				String templateName="";
				if(isConsolidatedScreen() ||isHod())
				{
					templateName="budgetProposalReport";
				this.fileName="BudgetProposalReport." + FileFormat.PDF.toString().toLowerCase();
				}else
				{
					templateName="budgetProposalReport-draft";
					this.fileName="BudgetProposalReport-draft." + FileFormat.PDF.toString().toLowerCase();
				}
				
				ReportRequest reportInput = new ReportRequest(templateName,bpBeanList, reportParams);
				reportInput.setReportFormat(FileFormat.PDF);
				this.contentType = ReportViewerUtil.getContentType(FileFormat.PDF);
				ReportOutput reportOutput = reportService.createReport(reportInput);  
				if (reportOutput != null && reportOutput.getReportOutputData() != null)
				    inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
			} catch (Exception e) {         
				LOGGER.error(e,e);
			}
			  return "reportview";
			/*
			bpBeanList=new ArrayList<BudgetProposalBean>();   
			populateBudgetDetailReport(); 
			String title=getUlbName() ; 
			String subtitle=getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():"";
			JasperPrint jasper = reportHelper.generateBudgetReportForHOD(bpBeanList,title,subtitle,twopreviousfinYearRange,previousfinYearRange,currentfinYearRange,nextfinYearRange,isConsolidatedScreen());
			inputStream = reportHelper.exportPdf(inputStream, jasper);
			return "PDF";*/
		}  
	/*	public String generateXls() throws Exception{
			//String subtitle="Amount in Rupess";   
			bpBeanList=new ArrayList<BudgetProposalBean>(); 
			populateBudgetDetailReport();   
			String title=getUlbName() ;    
			String subtitle=getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():"";
			
			JasperPrint jasper = reportHelper.generateBudgetReportForHOD(bpBeanList,title,subtitle,twopreviousfinYearRange,previousfinYearRange,currentfinYearRange,nextfinYearRange,isConsolidatedScreen());
			inputStream = reportHelper.exportXls(inputStream, jasper);  
			return "XLS";
		}*/
		
@Action(value="/budget/budgetProposal-generateXls")
	    public String generateXls() {
	    	try {
				bpBeanList=new ArrayList<BudgetProposalBean>(); 
				populateBudgetDetailReport();   
				Map<String, Object> reportParams = new HashMap<String, Object>();
				reportParams.put("title",getUlbName());
				reportParams.put("subtitle",getTopBudget().getName()!=null?"Budget-:"+getTopBudget().getName():"");
				reportParams.put("amount",isConsolidatedScreen()?"Amount in Thousand":"Amount in Rupees");
				reportParams.put("twopreviousfinYearRange",twopreviousfinYearRange);
				reportParams.put("previousfinYearRange",previousfinYearRange);
				reportParams.put("currentfinYearRange",currentfinYearRange);
				reportParams.put("nextfinYearRange",nextfinYearRange);
				//bpBeanList=new ArrayList<BudgetProposalBean>(); 
				String templateName="";
				if(isConsolidatedScreen() ||isHod())
				{
					templateName="budgetProposalReport";
				this.fileName="BudgetProposalReport." + FileFormat.XLS.toString().toLowerCase();
				}else
				{
					templateName="budgetProposalReport-draft";
					this.fileName="BudgetProposalReport-draft." + FileFormat.XLS.toString().toLowerCase();
				}
				
				ReportRequest reportInput = new ReportRequest(templateName,bpBeanList, reportParams);
				reportInput.setReportFormat(FileFormat.XLS);
				this.contentType = ReportViewerUtil.getContentType(FileFormat.XLS);
				
				ReportOutput reportOutput = reportService.createReport(reportInput);  
				if (reportOutput != null && reportOutput.getReportOutputData() != null)
				    inputStream = new ByteArrayInputStream(reportOutput.getReportOutputData());
			} catch (Exception e) {         
				LOGGER.error(e,e);
			}

	        return "reportview";
	    } 
	    
	    
	    
	    protected Boolean validateOwner()
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("validating owner for user "+EGOVThreadLocals.getUserId());
			List<Position> positionsForUser=null;
			positionsForUser = eisService.getPositionsForUser(Long.valueOf(EGOVThreadLocals.getUserId()), new Date());
			State state=null;
			if(factor.equalsIgnoreCase("thousand"))
			{
				state=	(State)persistenceService.find("select b.state from Budget b where b.id =(select bd.budget.id from BudgetDetail bd where bd.id=?) ",validId );	
			}
			else   
			{    
				 state=(State)persistenceService.find("select bd.state from BudgetDetail bd where bd.id=? ",validId );            
			}
			if( state!=null && positionsForUser.contains(state.getOwnerPosition()))      
			{  
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Valid Owner :return true");
				return true;  
			}else
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Invalid  Owner :return false");
				return false;
			}
		}

	public BudgetProposalBean getBpBean() {
		return bpBean;
	}


	public void setBpBean(BudgetProposalBean bpBean) {
		this.bpBean = bpBean;
	}


	public BudgetDetail getBudgetDetail() {
		return budgetDetail;
	}


	public void setBudgetDetail(BudgetDetail budgetDetail) {
		this.budgetDetail = budgetDetail;
	}


	public Budget getTopBudget() {
		return topBudget;
	}


	public void setTopBudget(Budget topBudget) {
		this.topBudget = topBudget;
	}


	public VoucherService getVoucherService() {
		return voucherService;
	}


	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}


	public FinancialYearHibernateDAO getFinancialYearDAO() {
		return financialYearDAO;
	}


	public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}


	public String getCurrentfinYearRange() {
		return currentfinYearRange;
	}


	public void setCurrentfinYearRange(String currentfinYearRange) {
		this.currentfinYearRange = currentfinYearRange;
	}


	public String getNextfinYearRange() {
		return nextfinYearRange;
	}


	public void setNextfinYearRange(String nextfinYearRange) {
		this.nextfinYearRange = nextfinYearRange;
	}


	public String getPreviousfinYearRange() {
		return previousfinYearRange;
	}


	public void setPreviousfinYearRange(String previousfinYearRange) {
		this.previousfinYearRange = previousfinYearRange;
	}


	public String getTwopreviousfinYearRange() {
		return twopreviousfinYearRange;
	}


	public void setTwopreviousfinYearRange(String twopreviousfinYearRange) {
		this.twopreviousfinYearRange = twopreviousfinYearRange;
	}


	public List<BudgetDetail> getBudgetDetailList() {
		return budgetDetailList;
	}


	public void setBudgetDetailList(List<BudgetDetail> budgetDetailList) {
		this.budgetDetailList = budgetDetailList;
	}


	public List<BudgetDetail> getSavedbudgetDetailList() {
		return savedbudgetDetailList;
	}


	public void setSavedbudgetDetailList(List<BudgetDetail> savedbudgetDetailList) {
		this.savedbudgetDetailList = savedbudgetDetailList;
	}


	public BudgetDetailConfig getBudgetDetailConfig() {
		return budgetDetailConfig;
	}


	public void setBudgetDetailConfig(BudgetDetailConfig budgetDetailConfig) {
		this.budgetDetailConfig = budgetDetailConfig;
	}


	public BudgetDetailService getBudgetDetailService() {
		return budgetDetailService;
	}


	public void setBudgetDetailService(BudgetDetailService budgetDetailService) {
		this.budgetDetailService = budgetDetailService;
	}


	public BudgetService getBudgetService() {
		return budgetService;
	}


	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}


	public Map<Long, String> getPreviuosYearBudgetDetailMap() {
		return previuosYearBudgetDetailMap;
	}


	public void setPreviuosYearBudgetDetailMap(
			Map<Long, String> previuosYearBudgetDetailMap) {
		this.previuosYearBudgetDetailMap = previuosYearBudgetDetailMap;
	}


	public Map<Long, String> getBeforePreviousYearBudgetDetailMap() {
		return beforePreviousYearBudgetDetailMap;
	}


	public void setBeforePreviousYearBudgetDetailMap(
			Map<Long, String> beforePreviousYearBudgetDetailMap) {
		this.beforePreviousYearBudgetDetailMap = beforePreviousYearBudgetDetailMap;
	}


	public Map<String, BigDecimal> getBudgetDetailIdsAndAmount() {
		return budgetDetailIdsAndAmount;
	}


	public void setBudgetDetailIdsAndAmount(
			Map<String, BigDecimal> budgetDetailIdsAndAmount) {
		this.budgetDetailIdsAndAmount = budgetDetailIdsAndAmount;
	}


	public Map<String, BigDecimal> getPreviousYearBudgetDetailIdsAndAmount() {
		return previousYearBudgetDetailIdsAndAmount;
	}


	public void setPreviousYearBudgetDetailIdsAndAmount(
			Map<String, BigDecimal> previousYearBudgetDetailIdsAndAmount) {
		this.previousYearBudgetDetailIdsAndAmount = previousYearBudgetDetailIdsAndAmount;
	}


	public Map<String, BigDecimal> getTwopreviousYearBudgetDetailIdsAndAmount() {
		return twopreviousYearBudgetDetailIdsAndAmount;
	}


	public void setTwopreviousYearBudgetDetailIdsAndAmount(
			Map<String, BigDecimal> twopreviousYearBudgetDetailIdsAndAmount) {
		this.twopreviousYearBudgetDetailIdsAndAmount = twopreviousYearBudgetDetailIdsAndAmount;
	}






	public String getWfitemstate() {
		return wfitemstate;
	}


	public void setWfitemstate(String wfitemstate) {
		this.wfitemstate = wfitemstate;
	}


	public Integer getDefaultDept() {
		return defaultDept;
	}


	public Date getHeaderAsOnDate() {
		return this.headerAsOnDate =asOndate!=null?asOndate:new Date();
	}


/*	public void setHeaderAsOnDate(Date headerAsOnDate) {
		
		this.headerAsOnDate = headerAsOnDate;
	}*/


	public void setDefaultDept(Integer defaultDept) {
		this.defaultDept = defaultDept;
	}


	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}


	public static String getActionname() {
		return ACTIONNAME;
	}


	public List<BudgetProposalBean> getBpBeanList() {
		return bpBeanList;
	}


	public void setBpBeanList(List<BudgetProposalBean> bpBeanList) {
		this.bpBeanList = bpBeanList;
	}


	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}


	public Date getAsOndate() {
		return asOndate;
	}


	public void setAsOndate(Date asOndate) {
		this.asOndate = asOndate;
	}



	public String getFunctionsNotYetReceiced() {
		return functionsNotYetReceiced;
	}


	public void setFunctionsNotYetReceiced(String functionsNotYetReceiced) {
		this.functionsNotYetReceiced = functionsNotYetReceiced;
	}


	public boolean isAsstFMU() {
		return asstFMU;
	}


	public void setAsstFMU(boolean asstFMU) {
		this.asstFMU = asstFMU;
	}
public void setBudgetDetailHelper(BudgetDetailHelper budgetDetailHelper) {
		this.budgetDetailHelper = budgetDetailHelper;
	}

	public InputStream getInputStream() {
		return inputStream;
	}


	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public ReportHelper getReportHelper() {
		return reportHelper;
	}


	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public Long getDocNo() {
		return docNo;
	}


	public void setDocNo(Long docNo) {
		this.docNo = docNo;
	}


	public String getAmountField() {
		return amountField;
	}


	public void setAmountField(String amountField) {
		this.amountField = amountField;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	


	public String getFactor() {
		return factor;
	}


	public void setFactor(String factor) {
		this.factor = factor;
	}


	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}


	public Long getDetailId() {
		return detailId;
	}


	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}


	public Long getValidId() {
		return validId;
	}


	public void setValidId(Long validId) {
		this.validId = validId;
	}

}
