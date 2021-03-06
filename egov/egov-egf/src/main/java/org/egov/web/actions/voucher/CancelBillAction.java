package org.egov.web.actions.voucher;

import org.apache.struts2.convention.annotation.Action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.Fund;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
import org.hibernate.Session;

import com.exilant.eGov.src.domain.BillRegisterBean;


public class CancelBillAction extends BaseFormAction  {
	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(CancelBillAction.class);
	private String billNumber;
	private String fromDate;
	private String toDate;
	private Fund fund=new Fund();
	private Department deptImpl = new Department();
	private String expType;
	private List<BillRegisterBean> billListDisplay= new ArrayList<BillRegisterBean>();
	private boolean afterSearch=false;
	Integer loggedInUser = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
	public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);

	
	@Override
	public Object getModel() {

		return null;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setFromDate(String fromBillDate) {
		this.fromDate = fromBillDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setToDate(String toBillDate) {
		this.toDate = toBillDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public Fund getFund() {
		return fund;
	}
	
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public String getExpType() {
		return expType;
	}
	public void  prepare()
	{
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside Prepare method");
		dropdownData.put("DepartmentList", masterCache.get("egi-department"));
		//get this from master data cache
		addDropdownData("fundList", persistenceService.findAllBy("from Fund where isactive=1 and isnotleaf=0 order by name"));
		// Important - Remove the like part of the query below to generalize the bill cancellation screen 
		addDropdownData("expenditureList",persistenceService.findAllBy("select distinct bill.expendituretype from EgBillregister bill where bill.expendituretype like '"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"'   order by bill.expendituretype"));
	}
	public void prepareBeforeSearch()
	{
		fund.setId(null);
		billNumber="";
		fromDate="";
		toDate="";
		expType="";
		billListDisplay.clear();
	}
	@SkipValidation
@Action(value="/voucher/cancelBill-beforeSearch")
	public String beforeSearch()
	{
		return "search" ;
	}
	
	@SuppressWarnings("unused")
	private boolean isSuperUser(){
    	List<Integer> superUserList=new ArrayList<Integer>();
    	superUserList.addAll((List<Integer>) persistenceService.findAllBy( "select user.id from UserRole usrRole where lower(usrRole.role.roleName) =?",FinancialConstants.SUPERUSER));
    	//if(superUserList.con)
    	if(superUserList.contains(loggedInUser))
    		return true;
    	else
    		return false ;
    }
	
	public StringBuilder filterQuery()
	{
		String userCond="";
		if(isSuperUser())
			userCond=" ";
		else
			userCond=" and billmis.egBillregister.createdBy="+loggedInUser;	
		
		StringBuilder query=new StringBuilder(" select billmis.egBillregister.id, billmis.egBillregister.billnumber, billmis.egBillregister.billdate, billmis.egBillregister.billamount, billmis.egDepartment.deptName  from EgBillregistermis billmis left join billmis.egBillSubType  egBillSubType where ");
		
		
		//Excluding TNEB Bills for Cancellation 	
		query.append(" (egBillSubType is null or egBillSubType.name not in ("+FinancialConstants.EXCLUDED_BILL_TYPES+"))");
		// if the logged in user is same as creator or is superruser
		query.append(userCond);
		
		if(fund!=null && fund.getId()!=null && fund.getId()!=-1 && fund.getId()!=0 )
		{
			query.append(" and billmis.fund.id="+fund.getId());
		}
		
		if(billNumber!=null && billNumber.length()!=0)
		{
			query.append(" and billmis.egBillregister.billnumber ='"+billNumber+"'");
		}
		if(deptImpl!=null && deptImpl.getId()!=null && deptImpl.getId()!=-1 && deptImpl.getId()!=0 )
		{
			query.append(" and billmis.egDepartment.id ='"+deptImpl.getId()+"'");
		}
		if(fromDate!=null && fromDate.length()!=0)
		{
			Date fDate;
			try {
				fDate = formatter.parse(fromDate);
				query.append(" and billmis.egBillregister.billdate >= '"+Constants.DDMMYYYYFORMAT1.format(fDate)+"'");
			} catch (ParseException e) {
				LOGGER.error(" From Date parse error");
				//e.printStackTrace();
			}
		}
		if(toDate!=null && toDate.length()!=0)
		{
			Date tDate;
			try {
				tDate = formatter.parse(toDate);
				query.append(" and billmis.egBillregister.billdate <= '"+Constants.DDMMYYYYFORMAT1.format(tDate)+"'");
			} catch (ParseException e) {
				LOGGER.error(" To Date parse error");
				//e.printStackTrace();
			}
		} 
		if(expType==null || expType.equalsIgnoreCase(""))
		{
			query.append(" and billmis.egBillregister.expendituretype ='"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"'");
			query.append(" and billmis.egBillregister.status.moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and billmis.egBillregister.status.description='"
					+FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS+"'");
		}
		else
		{
			query.append(" and billmis.egBillregister.expendituretype ='"+expType+"'");
			if(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY.equalsIgnoreCase(expType))
			{
				query.append(" and billmis.egBillregister.status.moduletype='"+FinancialConstants.SALARYBILL+"' and billmis.egBillregister.status.description='"
						+FinancialConstants.SALARYBILL_APPROVED_STATUS+"'");
			}else if(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equalsIgnoreCase(expType))
			{
				query.append(" and billmis.egBillregister.status.moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and billmis.egBillregister.status.description='"
						+FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS+"'");
				
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE.equalsIgnoreCase(expType))
			{
				query.append(" and billmis.egBillregister.status.moduletype='"+FinancialConstants.SUPPLIERBILL+"' and billmis.egBillregister.status.description='"
						+FinancialConstants.SUPPLIERBILL_PASSED_STATUS+"'");
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equalsIgnoreCase(expType))
			{
				query.append(" and billmis.egBillregister.status.moduletype='"+FinancialConstants.CONTRACTORBILL+"' and billmis.egBillregister.status.description='"
						+FinancialConstants.CONTRACTORBILL_PASSED_STATUS+"'");
			}
		}  
		
		return query;
		  
	}
	public String[] query()
	{
		String[] retQry= new String[2];
		StringBuilder filterQry= filterQuery();
		
		retQry[0]=filterQry+" and billmis.voucherHeader is null ";
		retQry[1]=filterQry+" and billmis.voucherHeader.status in ("+FinancialConstants.REVERSEDVOUCHERSTATUS+","+FinancialConstants.CANCELLEDVOUCHERSTATUS+") ";
		
		return retQry;
	}
	
	public void prepareSearch()
	{
		billListDisplay.clear();
	}
	public void validateFund()
	{
		if(fund==null || fund.getId()==-1)
			addFieldError("fund.id", getText("voucher.fund.mandatory"));
	}
	
	@ValidationErrorPage(value="search") 
	public String search()
	{
		validateFund();
		if(!hasFieldErrors())
		{
			billListDisplay.clear();
			String[] searchQuery=query();
			List<Object[]> tempBillList = new ArrayList<Object[]>();
			List<Object[]> billListWithNoVouchers,billListWithCancelledReversedVouchers;
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Search Query - "+searchQuery);
			billListWithNoVouchers=persistenceService.findAllBy(searchQuery[0]);
			billListWithCancelledReversedVouchers=persistenceService.findAllBy(searchQuery[1]);
			tempBillList.addAll(billListWithNoVouchers);
			tempBillList.addAll(billListWithCancelledReversedVouchers);
			
			BillRegisterBean billRegstrBean;
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Size of tempBillList - "+tempBillList.size());
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			Date date;
			for(Object[] bill:tempBillList)
			{
				billRegstrBean=new BillRegisterBean();
				billRegstrBean.setId(bill[0].toString());
				billRegstrBean.setBillNumber(bill[1].toString());
				if(!bill[2].toString().equalsIgnoreCase(""))
				{
					billRegstrBean.setBillDate(sdf.format(bill[2]));
				}
				billRegstrBean.setBillAmount(Double.parseDouble((bill[3].toString())));
				billRegstrBean.setBillDeptName(bill[4].toString());
				billListDisplay.add(billRegstrBean);
			}
			afterSearch=true;
		}
		return "search" ;
	}  
	public String cancelBill()
	{
		Date modifiedDate=new Date();
		Long[] idList=new  Long[billListDisplay.size()];
		int i=0,idListLength=0;
		String idString="";
		StringBuilder statusQuery=new StringBuilder("from EgwStatus where ");
		StringBuilder cancelQuery=new StringBuilder("Update EgBillregister set " );
		for(BillRegisterBean billRgstrBean: billListDisplay)
		{
			if(billRgstrBean.getIsSelected())
			{
				idList[i++]=Long.parseLong(billRgstrBean.getId());
				idListLength++;
			}
		}
		if(expType==null || expType.equalsIgnoreCase(""))
		{
			statusQuery.append("moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and description='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"'");
			cancelQuery.append(" billstatus='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
		}
		else
		{
			if(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.SALARYBILL+"' and description='"+FinancialConstants.SALARYBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.SALARYBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
				
			}else if(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.CONTINGENCYBILL_FIN+"' and description='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.SUPPLIERBILL+"' and description='"+FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
			}else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equalsIgnoreCase(expType))
			{
				statusQuery.append("moduletype='"+FinancialConstants.CONTRACTORBILL+"' and description='"+FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS+"'");
				cancelQuery.append(" billstatus='"+FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS+"' , status.id=:statusId ");
			}
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Status Query - "+statusQuery.toString());
		EgwStatus status=(EgwStatus) persistenceService.find(statusQuery.toString());
		Session session=HibernateUtil.getCurrentSession();
		
		if(idListLength!=0)
		{
				for(i=0;i<idListLength;i++)
				{
					idString+=idList[i]+((i==idListLength-1)?"":",");
				}
				
				cancelQuery.append(" where id in ("+idString+")");
				if(LOGGER.isDebugEnabled())     LOGGER.debug(" Cancel Query - "+cancelQuery.toString());
				Query query = session.createQuery(cancelQuery.toString());
				query.setLong("statusId", status.getId());
				/*query.setInteger("modifiedby",Integer.valueOf(EGOVThreadLocals.getUserId().trim()));
				query.setDate("modifiedDate",modifiedDate);*/
				int executeUpdate = query.executeUpdate();
		}
		addActionMessage("Bills Cancelled Successfully");
		prepareBeforeSearch();
		return "search";
	}
	public void setBillListDisplay(List<BillRegisterBean> billListDisplay) {
		this.billListDisplay = billListDisplay;
	}
	public List<BillRegisterBean> getBillListDisplay() {
		return billListDisplay;
	}
	public void setAfterSearch(boolean afterSearch) {
		this.afterSearch = afterSearch;
	}
	public boolean getAfterSearch() {
		return afterSearch;
	}
	public Department getDeptImpl() {
		return deptImpl;
	}
	public void setDeptImpl(Department deptImpl) {
		this.deptImpl = deptImpl;
	}
	public Integer getLoggedInUser() {
		return loggedInUser;
	}
	public void setLoggedInUser(Integer loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
}
