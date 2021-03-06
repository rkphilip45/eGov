package org.egov.web.actions.masters;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.Fund;
import org.egov.commons.utils.BankAccountType;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.StringUtils;
import org.egov.utils.Constants;

public class BankAccountAction extends JQueryGridActionSupport {
	private static final long serialVersionUID = 1L;
	private String mode;
	private String newGLCode = "", coaID = "";
	private Integer bankBranchId;
	private PersistenceService<Bankaccount, Integer> bankAccountPersistenceService;
	private PersistenceService<CChartOfAccounts, Long> chartOfAccountService;
	GenericHibernateDaoFactory genericDao;
	String code = EGovConfig.getProperty("egf_config.xml",
			"glcodeMaxLength", "", "AccountCode");

	@Override
	public String execute() {
		if ("CRUD".equals(mode)) {
			try {
				if (oper.equals(ADD)) {
					addBankAccount();
				} else if (oper.equals(EDIT)) {
					editBankAccount();
				} else if (oper.equals(DELETE)) {
					deleteBankAccount();
				}
				sendAJAXResponse(SUCCESS);
			} catch (RuntimeException e) {
				sendAJAXResponse("failed");
				throw new EGOVRuntimeException("Error occurred in Bank Account.", e);
			}
		} else if ("LIST_BRANCH_ACC".equals(mode)) {
			listAllBankBranchAccounts();
		}
		return null;
	}

	public PersistenceService<CChartOfAccounts, Long> getChartOfAccountService() {
		return chartOfAccountService;
	}

	public void setChartOfAccountService(
			PersistenceService<CChartOfAccounts, Long> chartOfAccountService) {
		this.chartOfAccountService = chartOfAccountService;
	}

	private void addBankAccount() {
		final Bankbranch bankBranch = (Bankbranch) persistenceService.getSession().load(Bankbranch.class, bankBranchId);
		final Date currentDate = new Date();
		final Bankaccount bankAccount = new Bankaccount();
		final HttpServletRequest request = ServletActionContext.getRequest();
		bankAccount.setBankbranch(bankBranch);
		bankAccount.setCreated(currentDate);
		bankAccount.setCurrentbalance(BigDecimal.ZERO);
		try {
			newGLCode = prepareBankAccCode(request.getParameter("accounttype").split("#")[0],code);
		
		coaID = postInChartOfAccounts(newGLCode,request.getParameter("accounttype").split("#")[0],request.getParameter("accountnumber"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (coaID != null && coaID.length() > 0){
			final CChartOfAccounts chartofaccounts = (CChartOfAccounts) persistenceService.getSession().load(CChartOfAccounts.class, Long.parseLong(coaID));
			bankAccount.setChartofaccounts(chartofaccounts);
		}
		populateBankAccountDetail(bankAccount);
		bankAccountPersistenceService.persist(bankAccount);
	}

	private void editBankAccount() {
		final Bankaccount bankAccount = (Bankaccount)bankAccountPersistenceService.getSession().get(Bankaccount.class, id);
		populateBankAccountDetail(bankAccount);
		bankAccountPersistenceService.update(bankAccount);
	}

	private void deleteBankAccount() {
		final Bankaccount bankBranch = (Bankaccount) bankAccountPersistenceService.getSession().load(Bankaccount.class, id);
		persistenceService.delete(bankBranch);
	}

	private void populateBankAccountDetail(final Bankaccount bankAccount) {
		final HttpServletRequest request = ServletActionContext.getRequest();
		bankAccount.setModifiedby(BigDecimal.valueOf(Long.valueOf(EGOVThreadLocals.getUserId())));
		bankAccount.setLastmodified(new Date());
		bankAccount.setAccountnumber(request.getParameter("accountnumber"));
		bankAccount.setAccounttype(request.getParameter("accounttype").split("#")[1]);
		if (StringUtils.isNotBlank(request.getParameter("fundname"))) {
			final Fund fund = (Fund) persistenceService.getSession().load(Fund.class, Integer.valueOf(request.getParameter("fundname")));
			bankAccount.setFund(fund);
		}
		bankAccount.setIsactive(request.getParameter("active").equals("Y"));
		bankAccount.setNarration(request.getParameter("narration"));
		if (StringUtils.isNotBlank(request.getParameter("typename"))) {
			final BankAccountType type = BankAccountType.valueOf(request.getParameter("typename"));
			bankAccount.setType(type);
		}
		bankAccount.setPayTo(request.getParameter("payto"));
	}

	private void listAllBankBranchAccounts() {
		final List<Bankaccount> bankAccounts = getPagedResult(Bankaccount.class,"bankbranch.id",bankBranchId).getList();
		final List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		String glCode = "";
	
		for (final Bankaccount bankaccount : bankAccounts) {
			try {
				final JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", bankaccount.getId());
				jsonObject.put("accountnumber", bankaccount.getAccountnumber());
				jsonObject.put("fundname", bankaccount.getFund().getName());
				jsonObject.put("accounttype", bankaccount.getAccounttype());
				jsonObject.put("narration", bankaccount.getNarration());
				jsonObject.put("payto", bankaccount.getPayTo());
				jsonObject.put("typename", bankaccount.getType() == null ? "" : bankaccount.getType().name());
				jsonObject.put("active", bankaccount.isIsactive() ? "Y" : "N");
				glCode=  (String) persistenceService.find("select glcode from CChartOfAccounts where id=(select chartofaccounts.id from Bankaccount where accountnumber = ?)",bankaccount.getAccountnumber());
				jsonObject.put("glcode", glCode);
				jsonObjects.add(jsonObject);
			} catch (final JSONException e) {
				sendAJAXResponse("error");
			}
		}
		/*final JSONArray jsonArray = new JSONArray(jsonObjects);
		sendAJAXResponse(constructJqGridResponse(jsonArray.toString()));*/
	}
	public String prepareBankAccCode(String accID, String code)
			throws Exception {
		String glCode = "";
		Long glcode;
		Long tempCode = 0L;
		PreparedStatement pstmt = null;
		glCode=  (String) persistenceService.find("select glcode from CChartOfAccounts where glcode = (select glcode from CChartOfAccounts where id=?) order by glcode desc",Long.parseLong(accID));
		String subminorvalue = EGovConfig.getProperty("egf_config.xml",
				"subminorvalue", "", "AccountCode");
		glCode = glCode.substring(0, Integer.parseInt(subminorvalue));
		glCode=(String) persistenceService.find("select glcode from CChartOfAccounts where glcode like ? || '%' order by glcode desc",glCode);
		String zero = EGovConfig.getProperty("egf_config.xml", "zerofill", "",
				"AccountCode");
		if (glCode.length() == Integer.parseInt(code)) {
			glcode = Long.parseLong(glCode);
			tempCode = glcode + 1;
		} else {
			glCode = glCode + zero;
			glcode = Long.parseLong(glCode);
			tempCode = glcode + 1;
		}
		glCode = Long.toString(tempCode);
		return glCode;
	}
	String getAppConfigValueFor(String module,String key){
		return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key).get(0).getValue();
	}
	public String postInChartOfAccounts(String glCode, String parentId,
			String accNumber) throws Exception {
		final Bankbranch bankBranch = (Bankbranch) persistenceService.getSession().load(Bankbranch.class, bankBranchId);
		int majorCodeLength = 0;
		majorCodeLength = Integer.valueOf(getAppConfigValueFor(Constants.EGF,"coa_majorcode_length"));
		CChartOfAccounts chart = new CChartOfAccounts();
		chart.setGlcode(glCode);
		chart.setName(bankBranch.getBank().getName() + " "
				+ bankBranch.getBranchname()+ " "
				+ accNumber);
		chart.setParentId(Long.parseLong(parentId));
		chart.setType('A');
		chart.setMyClass(Long.parseLong("5")); // This is the leaf level number.
		chart.setClassification(Long.parseLong("4"));
		chart.setIsActiveForPosting(Long.parseLong("1"));
		chart.setMajorCode(chart.getGlcode().substring(0,majorCodeLength));
		chartOfAccountService.persist(chart);
		return String.valueOf(chart.getId());
	}

	public GenericHibernateDaoFactory getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setBankAccountPersistenceService(final PersistenceService<Bankaccount, Integer> bankAccountPersistenceService) {
		this.bankAccountPersistenceService = bankAccountPersistenceService;
	}
 
	

	public void setMode(final String mode) {
		this.mode = mode;
	}

	public void setBankBranchId(final Integer bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
}
