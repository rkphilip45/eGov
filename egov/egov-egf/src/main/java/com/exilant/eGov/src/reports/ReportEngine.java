package com.exilant.eGov.src.reports;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;

/**
 * 
 * @author Manikanta
 *  
 */

public class ReportEngine {
	private final  static Logger LOGGER=Logger.getLogger(ReportEngine.class);
	/**
	 * 
	 * @param reBean
	 * @return query 
	 * 
	 */
	
	public String	getVouchersListQuery(ReportEngineBean reBean)throws EGOVRuntimeException
	{
		boolean includeVouchermis=false;
		boolean includeGeneralLedger=false;  
		String firstParam="";
		final String andParam=" and ";
		StringBuffer reportEngineQry=new StringBuffer("");
		
		try {
			if(reBean.getSchemeId()!=null ||reBean.getSubSchemeId()!=null || reBean.getFundsourceId()!=null || reBean.getDivisionId()!=null || reBean.getDepartmentId()!=null|| reBean.getFunctionaryId()!=null)
			{
				includeVouchermis=true;
			}
			if(reBean.getFunctionId()!=null)
			{
				includeGeneralLedger=true;
			}
			
			
			reportEngineQry.append("select ");
			/** add fields which are to be fetched
			 */
			reportEngineQry.append("voucher.id as \"vocherId\" ");
			
			reportEngineQry.append(" from ");
			/** add the table names 
				 	if no fields of a perticular table is  passed ommit it
				 	 eg if scheme,subscheme or divisionid is not passed donot include vouchermis 
				 	 or if function is not passed 	donot include generalledger
			 */
			if(includeVouchermis==true && includeGeneralLedger==true)
			{
				reportEngineQry.append(" ( voucherheader voucher left join vouchermis mis on voucher.id=mis.voucherheaderid)"
					+"left join generalledger ledger on voucher.id=ledger.voucherheaderid ");
			}
			else if(includeVouchermis==true)
			{
				reportEngineQry.append(" voucherheader voucher left join vouchermis mis on voucher.id=mis.voucherheaderid ");
			}
			else if(includeGeneralLedger==true)
			{
				reportEngineQry.append(" voucherheader voucher left join generalledger ledger on voucher.id=ledger.voucherheaderid ");
			}
			else
			{
				reportEngineQry.append(" voucherheader voucher ");
			}
			
//			if parmeters are passed then set "where" clause	
			if(reBean.getFiltersCount()>=1)
			{
				reportEngineQry.append(" where ");	
			}
			
			/**
			 * where conditions
			 * add  the conditions for the variables in the  ReportEngineBean
			 * 
			 */
			if(checkNullandEmpty(reBean.getFundId()))
			{  	
				reportEngineQry.append(firstParam+" voucher.fundId="+reBean.getFundId());
				firstParam=andParam;
			}
			if(checkNullandEmpty(reBean.getFundsourceId()))
			{
				reportEngineQry.append(firstParam+" mis.fundsourceId="+reBean.getFundsourceId());
				firstParam=andParam;	
			}
			
			if(checkNullandEmpty(reBean.getFromDate()))
			{
				reportEngineQry.append(firstParam+" voucher.voucherDate>=to_date('"+reBean.getFromDate()+"','dd/MM/yyyy')");
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getToDate()))
			{
				reportEngineQry.append(firstParam+" voucher.voucherDate<=to_date('"+reBean.getToDate()+"','dd/MM/yyyy')");
				firstParam=andParam;	
			}
			
			if(checkNullandEmpty(reBean.getFromVoucherNumber()))
			{
				reportEngineQry.append(firstParam+" voucher.fromVouchernumber>="+reBean.getFromVoucherNumber());
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getToVoucherNumber()))
			{
				reportEngineQry.append(firstParam+" voucher.toVouchernumber<="+reBean.getToVoucherNumber());
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getSchemeId()))
			{
				reportEngineQry.append(firstParam+" mis.schemeId="+reBean.getSchemeId());
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getSubSchemeId()))
			{
				reportEngineQry.append(firstParam+" mis.subSchemeId="+reBean.getSubSchemeId());
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getDivisionId()))
			{
				reportEngineQry.append(firstParam+" mis.divisionId="+reBean.getDivisionId());
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getDepartmentId()))
			{
				reportEngineQry.append(firstParam+" mis.departmentId="+reBean.getDepartmentId());
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getFunctionaryId()))
			{
				reportEngineQry.append(firstParam+" mis.functionaryId="+reBean.getFunctionaryId());
				firstParam=andParam;	
			}
			if(checkNullandEmpty(reBean.getFunctionId()))
			{
				reportEngineQry.append(firstParam+" ledger.functionid="+reBean.getFunctionId());
				firstParam=andParam;	
			}
			
			/**
			 * add statuses to be included
			 * 
			 */
			
			/**
			 * default exclude status
			 */
			ArrayList<String> defaultStatusExcludeList=new ArrayList<String>();
			String defaultStatusExclude=null;
			List<AppConfigValues> listAppConfVal=new AppConfigValuesHibernateDAO(AppConfigValues.class,HibernateUtil.getCurrentSession()).
			getConfigValuesByModuleAndKey("finance","statusexcludeReport");
			if(null!= listAppConfVal)
			{
				defaultStatusExclude = ((AppConfigValues)listAppConfVal.get(0)).getValue();
			}
			else
			{
				throw new EGOVRuntimeException("Exlcude statusses not  are not defined for Reports");
			}
			reportEngineQry.append(firstParam+"voucher.status not in("+defaultStatusExclude);
			if(reBean.getExcludeStatuses()!=null && reBean.getExcludeStatuses().size()>0)
			{
				reportEngineQry.append(","+reBean.getCommaSeperatedValues(reBean.getExcludeStatuses())+" )");
				firstParam=andParam;
			}
			else
			{
				reportEngineQry.append(")");
				firstParam=andParam;
			}
			 
			if(reBean.getIncludeStatuses()!=null && reBean.getIncludeStatuses().size()>0)
			{
				reportEngineQry.append(firstParam+" voucher.status in( "+reBean.getCommaSeperatedValues(reBean.getIncludeStatuses())+" )");
				firstParam=andParam;
			}
			
			
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}	
		if(LOGGER.isDebugEnabled())     LOGGER.debug("-----------------------Engine Query-------------------");
		if(LOGGER.isDebugEnabled())     LOGGER.debug(reportEngineQry.toString());
		return reportEngineQry.toString();
		
	}
	 
	private boolean checkNullandEmpty(String column)
	{
		if(column!=null && !column.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public ReportEngineBean populateReportEngineBean(final GeneralLedgerReportBean reportBean)
	{
		ReportEngineBean reBean=new ReportEngineBean();
		reBean.setDepartmentId(reportBean.getDepartmentId());
		reBean.setDivisionId(reportBean.getFieldId());
		reBean.setFundId(reportBean.getFund_id());
		reBean.setFundsourceId(reportBean.getFundSource_id());
		reBean.setFunctionaryId(reportBean.getFunctionaryId());
		reBean.setFinacialYearId(null);
		reBean.setFiscalPeriodId(null);
		reBean.setFromDate(reportBean.getStartDate());
		reBean.setFromVoucherNumber(null);
		reBean.setFunctionId(reportBean.getFunctionCodeId());
		reBean.setSchemeId(null);
		reBean.setSubSchemeId(null);
		reBean.setToDate(reportBean.getEndDate());
		reBean.setToVoucherNumber(null);
		return reBean;
	}
	
	
	
	
	
}