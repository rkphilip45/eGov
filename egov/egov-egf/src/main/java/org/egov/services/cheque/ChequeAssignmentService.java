package org.egov.services.cheque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bankaccount;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.commons.utils.EntityType;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.payment.ChequeAssignment;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

public class ChequeAssignmentService {
//extends PersistenceService<Paymentheader, Long> {

	public SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	public final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	private GenericHibernateDaoFactory genericDao;
	protected PersistenceService persistenceService;
	private Query query;
	private List<ChequeAssignment> finalCBillChequeAssignmentList ;
	private List<ChequeAssignment> tempExpenseChequeAssignmentList;
	private List<ChequeAssignment> finalChequeAssignmentList;
	private CommonsService commonsService;
	private static final Logger	LOGGER	= Logger.getLogger(ChequeAssignmentService.class);
	private static final String DELIMETER = "~";
	private String approvedstatus="";
	private String statusId="";
	private  List<BigDecimal> cBillGlcodeIdList=null;
	private String instrumentReconciledStatus="";
	private String instrumentNewStatus="";
	private String filterConditions="";
	public List<CChartOfAccounts> purchaseBillGlcodeList=new ArrayList<CChartOfAccounts>();
	public List<CChartOfAccounts> worksBillGlcodeList=new ArrayList<CChartOfAccounts>();
	public List<CChartOfAccounts> salaryBillGlcodeList=new ArrayList<CChartOfAccounts>();	
	public List<CChartOfAccounts> contingentBillGlcodeList=new ArrayList<CChartOfAccounts>();
	@Autowired
	private CommonsService cmnMngr ;
	
	

	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	@SuppressWarnings("unchecked")
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public void setCommonsService(final CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	//*************IMPORTANT - CALL THIS METHOD BEFORE CALLING ANYTHING ELSE**********************************************
	public void setStatusAndFilterValues(Map<String,String[]> parameters,CVoucherHeader voucherHeader) throws ParseException
	{
		filterConditions= getFilterParamaters(parameters,voucherHeader);
		setStatusValues();
	}
	// This method returns the Direct Bank Payments and Bill Payments for Expense, Contractor and Supplier bills for mode Cheque
	public List<ChequeAssignment> getPaymentVoucherNotInInstrument(Map<String,String[]> parameters) throws EGOVException, ParseException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getPaymentVoucherNotInInstrument...");
		finalChequeAssignmentList.addAll(getExpenseBillPayments());
		finalChequeAssignmentList.addAll(getDirectBankPaymentsForChequeAssignment());
		finalChequeAssignmentList.addAll(getContractorSupplierPaymentsForChequeAssignment(parameters));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getPaymentVoucherNotInInstrument.");
		return finalChequeAssignmentList;
	}
	// This method returns the Bill Payments for Expense for mode Cheque
	public List<ChequeAssignment> getExpenseBillPayments() throws ParseException, NumberFormatException, EGOVException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getExpenseBillPayments...");
		getExpenseBillPaymentsHavingNoCheques();
		getExpenseBillPaymentsWithNoSurrenderedCheque( );
		getExpenseBillPaymentsWithSurrenderedCheques();
		if(tempExpenseChequeAssignmentList!=null && tempExpenseChequeAssignmentList.size()!=0)
		{
			prepareChequeList();
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getExpenseBillPayments.");
		return finalCBillChequeAssignmentList;
	}
	// This method returns the consolidated mode payments that are not for salary or remittance
	@SuppressWarnings("unchecked")
	public List<ChequeAssignment> getPaymentVouchersConsolidatedMode(Map<String,String[]> parameters,CVoucherHeader voucherHeader) throws ParseException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getPaymentVouchersConsolidatedMode...");
		String filterConditions= getFilterParamaters(parameters,voucherHeader);
		setStatusValues();
		
		query =HibernateUtil.getCurrentSession().createSQLQuery("select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill " +
				" where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +
				" and vh.id not in (select voucherHeaderId from egf_InstrumentVoucher iv, EGF_INSTRUMENTHEADER ih where iv.INSTRUMENTHEADERID = ih.id and ih.ID_STATUS in ("+statusId+") ) and vh.type='"+FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT+"' and vh.name NOT IN ('"+FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE+"' , '"+FinancialConstants.PAYMENTVOUCHER_NAME_SALARY+"') "+
				" group by vh.id,vh.voucherNumber,vh.voucherDate order by vh.voucherNumber ")
				.addScalar("voucherid").addScalar("voucherNumber").addScalar("voucherDate").addScalar("paidAmount").addScalar("chequeDate")
				.setResultTransformer(Transformers.aliasToBean(ChequeAssignment.class));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getPaymentVouchersConsolidatedMode.");
		return query.list();
		
	}
	@SuppressWarnings("unchecked")
	public List<ChequeAssignment> getContractorSupplierPaymentsForChequeAssignment(Map<String,String[]> parameters) throws ParseException 
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getContractorSupplierPaymentsForChequeAssignment...");

		Bankaccount ba = (Bankaccount) persistenceService.find(" from Bankaccount where id=?",Integer.valueOf(parameters.get("bankaccount")[0]));
		
		String billCondition="";//"'"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"'";
		if(null!=parameters.get("voucherName") && null!= parameters.get("voucherName")[0] && 
				FinancialConstants.PAYMENTVOUCHER_NAME_PENSION.equalsIgnoreCase(parameters.get("voucherName")[0]) )
		{
			billCondition= " in ('"+ FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION+"') ";
		}
		else
		{
			billCondition=" not in ('"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"','"+ FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION+"')";
		}
		String supplierBillPaymentQuery="select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate " +
			" from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill ,voucherheader billvh, eg_billregister br, eg_billregistermis billmis, generalledger gl, " +
			" egf_instrumentvoucher iv right outer join voucherheader pvh on (pvh.id=iv.VOUCHERHEADERID) " +
			" where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name='Bill Payment' and  vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +
			" and gl.voucherheaderid =vh.id  and gl.creditamount>0 and gl.glcodeid in ("+ba.getChartofaccounts().getId()+") and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype "+billCondition+" and misbill.billvhid=billvh.id " +
			" and pvh.id=vh.id and iv.id IS NULL group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto  " +
			" union select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate " +
			" from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill ,voucherheader billvh, eg_billregister br, eg_billregistermis billmis, generalledger gl, " +
			" egf_instrumentvoucher iv right outer join voucherheader pvh on (pvh.id=iv.VOUCHERHEADERID)  left outer join egf_instrumentheader ih on (ih.ID=iv.INSTRUMENTHEADERID) " +
			" where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name='Bill Payment' and  vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +
			" and gl.voucherheaderid =vh.id  and gl.creditamount>0 and gl.glcodeid in ("+ba.getChartofaccounts().getId()+") and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype  "+billCondition+" and misbill.billvhid=billvh.id " +
			" and pvh.id=vh.id and ih.id IN (SELECT MAX(ih.id) FROM egf_instrumentvoucher iv RIGHT OUTER JOIN voucherheader pvh ON (pvh.id=iv.VOUCHERHEADERID) LEFT OUTER JOIN  "+
			" egf_instrumentheader ih ON (ih.ID =iv.INSTRUMENTHEADERID) WHERE pvh.id =vh.id AND ih.payto =misbill.paidto) AND ih.ID_STATUS NOT IN ("+statusId+") group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto  "+
			" union select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate " +
			" from Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill ,voucherheader billvh, eg_billregister br, eg_billregistermis billmis, generalledger gl " +
			" where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name='Bill Payment' and  vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +
			" and gl.voucherheaderid =vh.id  and gl.creditamount>0 and gl.glcodeid in ("+ba.getChartofaccounts().getId()+") and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype  "+billCondition+" and misbill.billvhid=billvh.id " +
			" and  misbill.paidto NOT IN (SELECT DISTINCT(ih.payto) FROM egf_instrumentvoucher iv  RIGHT OUTER JOIN voucherheader pvh    "+
			" ON (pvh.id=iv.VOUCHERHEADERID)  LEFT OUTER JOIN egf_instrumentheader ih  ON (ih.ID=iv.INSTRUMENTHEADERID)  WHERE pvh.id=vh.id AND ih.ID_STATUS IN ("+statusId+")) "+
			" group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto order by paidto,voucherNumber ";
		query =HibernateUtil.getCurrentSession().createSQLQuery(supplierBillPaymentQuery)
		.addScalar("voucherid").addScalar("voucherNumber").addScalar("voucherDate").addScalar("detailtypeid").addScalar("detailkeyid").addScalar("paidTo").addScalar("paidAmount").addScalar("chequeDate")
		.setResultTransformer(Transformers.aliasToBean(ChequeAssignment.class));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("CONTRACTOR/SUPLLIER BILL PAYMENT QUERY - "+supplierBillPaymentQuery);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getContractorSupplierPaymentsForChequeAssignment.");
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ChequeAssignment> getDirectBankPaymentsForChequeAssignment() throws ParseException
	{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getDirectBankPaymentsForChequeAssignment...");
			String bankPaymentQuery= "select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,0 as detailtypeid ,0 as detailkeyid,vh.voucherDate as voucherDate  ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate"+
			" From Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill, " +
			" egf_instrumentvoucher iv right outer join voucherheader pvh on (pvh.id=iv.VOUCHERHEADERID)"+ 
			" Where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name in ('Direct Bank Payment','Advance Payment') and vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +
			" and pvh.id=vh.id and  iv.id IS NULL  group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto "+
			" union select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,0 as detailtypeid ,0 as detailkeyid,vh.voucherDate as voucherDate  ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate"+
			" From Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill, " +
			" egf_instrumentvoucher iv right outer join voucherheader pvh on (pvh.id=iv.VOUCHERHEADERID)"+ 
			" left outer join egf_instrumentheader ih on (ih.ID=iv.INSTRUMENTHEADERID)"+
			" Where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name in ('Direct Bank Payment','Advance Payment') and vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +
			" and pvh.id=vh.id and ih.id IN (SELECT MAX(ih.id) FROM egf_instrumentvoucher iv RIGHT OUTER JOIN voucherheader pvh ON (pvh.id=iv.VOUCHERHEADERID) LEFT OUTER JOIN "+
			" egf_instrumentheader ih ON (ih.ID =iv.INSTRUMENTHEADERID) WHERE pvh.id =vh.id AND ih.payto =misbill.paidto) AND ih.ID_STATUS NOT IN ("+statusId+") group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto  "+
			" union select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,0 as detailtypeid ,0 as detailkeyid,vh.voucherDate as voucherDate  ,misbill.paidto as paidTo,sum(misbill.paidamount) as paidAmount,sysdate as chequeDate"+
			" From Paymentheader ph,voucherheader vh,vouchermis vmis, Miscbilldetail misbill " +
			" Where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vh.name in ('Direct Bank Payment','Advance Payment') and vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +
			" and misbill.paidto NOT IN (SELECT DISTINCT(ih.payto) FROM egf_instrumentvoucher iv  RIGHT OUTER JOIN voucherheader pvh  "+
			" ON (pvh.id=iv.VOUCHERHEADERID)  LEFT OUTER JOIN egf_instrumentheader ih  ON (ih.ID=iv.INSTRUMENTHEADERID)  WHERE pvh.id=vh.id AND ih.ID_STATUS IN ("+statusId+"))  group by vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto  "+
			" order by paidto,voucherNumber ";
		query =HibernateUtil.getCurrentSession().createSQLQuery(bankPaymentQuery)
		.addScalar("voucherid").addScalar("voucherNumber").addScalar("detailtypeid").addScalar("detailkeyid").addScalar("voucherDate").addScalar("paidTo").addScalar("paidAmount").addScalar("chequeDate")
		.setResultTransformer(Transformers.aliasToBean(ChequeAssignment.class));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("DIRECT BANK PAYMENT QUERY - "+bankPaymentQuery);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getDirectBankPaymentsForChequeAssignment.");
		return query.list();
	}
	
	// Getting only those payments for which cheques have not been assigned.
	@SuppressWarnings("unchecked")
	private void getExpenseBillPaymentsHavingNoCheques() throws NumberFormatException, EGOVException {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getExpenseBillPaymentsHavingNoCheques... NOT YET ASSIGNED");
		List<ChequeAssignment> billChequeAssignmentList = null;
		List<Long> billVHIds = new ArrayList<Long>(); 	
		List<Long> billVHIdsForDebtitSideCC = new ArrayList<Long>();
		List<Object[]> generalLedgerDetailList  = new ArrayList<Object[]>();
		List<Object[]> generalLedgerDetailListForDebtitSideCC = new ArrayList<Object[]>();
		Map<Long,List<Object[]>> billVHIdAndgeneralLedgerDetailListMap = new HashMap<Long,List<Object[]>>();
		Map<Long,List<Object[]>> billVHIdAndGLDListForDebtitSideCCMap = new HashMap<Long,List<Object[]>>();
		String strQuery="select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,"+
						" misbill.paidto as paidTo,decode(sum(misbill.paidamount),null,0,sum(misbill.paidamount)) as paidAmount,sysdate as chequeDate, misbill.billvhid as billVHId "+
						" from Paymentheader ph,egf_instrumentvoucher iv right outer join voucherheader vh on (vh.id=iv.VOUCHERHEADERID) ,vouchermis vmis, Miscbilldetail misbill, generalledger gl ,voucherheader billvh, eg_billregister br,eg_billregistermis billmis "+
						" where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+"  "  +  
						" and gl.voucherheaderid =vh.id and gl.creditamount>0 and misbill.billvhid=billvh.id  and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype='"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"' and iv.id is null  "+
						" group by  misbill.billvhid,vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto ";
		query  = HibernateUtil.getCurrentSession().createSQLQuery(strQuery)
				.addScalar("voucherid").addScalar("voucherNumber").addScalar("voucherDate").addScalar("paidAmount").addScalar("chequeDate").addScalar("paidTo").addScalar("billVHId").addScalar("detailtypeid").addScalar("detailkeyid")
				.setResultTransformer(Transformers.aliasToBean(ChequeAssignment.class));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED No cheques - "+strQuery);
		billChequeAssignmentList=query.list();
		
		for(ChequeAssignment ca :billChequeAssignmentList){
			billVHIds.add(ca.getBillVHId().longValue());
		}
		if(billVHIds!=null && billVHIds.size()>0){
		generalLedgerDetailList = getDetailTypeKeyAmtForBillVHId(billVHIds);
		}
		for(Object[] gld:generalLedgerDetailList){
			if(billVHIdAndgeneralLedgerDetailListMap.containsKey(getLongValue(gld[3]))){
				billVHIdAndgeneralLedgerDetailListMap.get(getLongValue(gld[3])).add(gld);
			}else{
				List<Object[]> generalLedgerDetails =new ArrayList<Object[]>();
				generalLedgerDetails.add(gld);
				billVHIdAndgeneralLedgerDetailListMap.put(getLongValue(gld[3]),generalLedgerDetails);
			}
			
		}
		for(ChequeAssignment ca :billChequeAssignmentList){
			List <Object[]> detailTypeKeyAmtList=  billVHIdAndgeneralLedgerDetailListMap.get(ca.getBillVHId().longValue());
			
			if(detailTypeKeyAmtList==null || detailTypeKeyAmtList.size()==0)
			{
				billVHIdsForDebtitSideCC.add(ca.getBillVHId().longValue());
			}
		}
		if(billVHIdsForDebtitSideCC!=null && billVHIdsForDebtitSideCC.size()>0){
			generalLedgerDetailListForDebtitSideCC = getDetailTypeKeyAmtForDebtitSideCC(billVHIdsForDebtitSideCC);
		}
		for(Object[] gld:generalLedgerDetailListForDebtitSideCC){
			if(billVHIdAndGLDListForDebtitSideCCMap.containsKey(getLongValue(gld[3]))){
				billVHIdAndGLDListForDebtitSideCCMap.get(getLongValue(gld[3])).add(gld);
			}else{
				List<Object[]> generalLedgerDetails =new ArrayList<Object[]>();
				generalLedgerDetails.add(gld);
				billVHIdAndGLDListForDebtitSideCCMap.put(getLongValue(gld[3]),generalLedgerDetails);
			}
			
		}
		for(ChequeAssignment chqAssgn:billChequeAssignmentList)
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: Start Checking for Billvhid "+chqAssgn.getBillVHId());
			if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: checking getDetailTypeKeyAmtForBillVHId for Net payable codes");
			
			List <Object[]> detailTypeKeyAmtList=  billVHIdAndgeneralLedgerDetailListMap.get(chqAssgn.getBillVHId().longValue())!=null?billVHIdAndgeneralLedgerDetailListMap.get(chqAssgn.getBillVHId().longValue()):new ArrayList<Object[]>();
			
			if(detailTypeKeyAmtList!=null && detailTypeKeyAmtList.size()!=0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: detailTypeKeyAmtList for Billvhid "+chqAssgn.getBillVHId()+" size :"+detailTypeKeyAmtList.size());
				if(detailTypeKeyAmtList.size()<2)
				{
					tempExpenseChequeAssignmentList.add(chqAssgn);
					if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: adding inside detailTypeKeyAmtList.size()<2 block to Assignment List\n"+chqAssgn);
				}
				else
				{
					for(Object[] detailTypeKeyAmtObj: detailTypeKeyAmtList)
					{
						ChequeAssignment ca=new ChequeAssignment();
						ca.setVoucherid( new BigDecimal(chqAssgn.getVoucherid()));
						ca.setVoucherNumber(chqAssgn.getVoucherNumber());
						if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: Voucher Number"+chqAssgn.getVoucherNumber());
						ca.setVoucherDate(chqAssgn.getVoucherDate());
						ca.setPaidAmount((BigDecimal) detailTypeKeyAmtObj[2]);
						ca.setChequeDate(chqAssgn.getChequeDate());
						ca.setPaidTo(getEntity( Integer.parseInt(detailTypeKeyAmtObj[0].toString()), (Serializable) detailTypeKeyAmtObj[1]).getName());
						ca.setDetailtypeid((BigDecimal) detailTypeKeyAmtObj[0]);
						ca.setDetailkeyid( (BigDecimal) detailTypeKeyAmtObj[1]);
						if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: detailTypeKeyAmtList.size()>=2 block to Assignment List\n"+ca);
						tempExpenseChequeAssignmentList.add(ca);
					}
				}
			}
			else
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED:  checking getDetailTypeKeyAmtForDebtitSideCC for "+chqAssgn.getBillVHId());
				detailTypeKeyAmtList =billVHIdAndGLDListForDebtitSideCCMap.get(chqAssgn.getBillVHId().longValue())!=null?billVHIdAndGLDListForDebtitSideCCMap.get(chqAssgn.getBillVHId().longValue()):new ArrayList<Object[]>();
				if(detailTypeKeyAmtList==null||detailTypeKeyAmtList.size()==0)
				{
					if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: adding to checkassignlist as detailTypeKeyAmtList is null or zero"+chqAssgn);
					tempExpenseChequeAssignmentList.add(chqAssgn);
				}
				else if (detailTypeKeyAmtList!=null && detailTypeKeyAmtList.size()==1)
                {
						if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: adding to checkassignlist as detailTypeKeyAmtList is 1"+chqAssgn);
                        tempExpenseChequeAssignmentList.add(chqAssgn);
                }
				else
				{
					BigDecimal deduction = BigDecimal.valueOf(0);
					//THIS dedcution will work for only one subledger .If more than one you cannot have non subledger dedcution as 
					//you can not distribute that among multiple people
					//Also this needs same subledger entity used on debit and credit side
					if(detailTypeKeyAmtList.size()==1)
					{		
						deduction=  getNonSubledgerDeductions(chqAssgn.getBillVHId());
					}
					Map<String,BigDecimal> dedMap =new HashMap<String,BigDecimal>();
					dedMap=getSubledgerAmtForDeduction(chqAssgn.getBillVHId());
					String key="";
					for(Object[] obj:detailTypeKeyAmtList)
					{
						ChequeAssignment c = new ChequeAssignment();
						c.setChequeDate(chqAssgn.getChequeDate());
						c.setVoucherHeaderId(chqAssgn.getVoucherid());
						c.setVoucherNumber(chqAssgn.getVoucherNumber());
						if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED: Voucher Number  :"+chqAssgn.getVoucherNumber());   
						c.setVoucherDate(chqAssgn.getVoucherDate());
						c.setDetailtypeid((BigDecimal)obj[0]);
						c.setDetailkeyid((BigDecimal)obj[1]);
						key = obj[0].toString()+DELIMETER+obj[1].toString();
						//deduct only if deduction is available
						if(deduction!=null){
						obj[2] = ((BigDecimal)obj[2]).subtract(deduction);
						}
						c.setPaidAmount((dedMap.get(key)==null?(BigDecimal)obj[2]:((BigDecimal)obj[2]).subtract(dedMap.get(key))));
						c.setPaidTo(getEntity(Integer.valueOf(obj[0].toString()), (Serializable)obj[1]).getName());
						if(LOGGER.isDebugEnabled())     LOGGER.debug("NOT YET ASSIGNED:  detailTypeKeyAmtList.size()>=2 block to Assignment List\n"+c);
						tempExpenseChequeAssignmentList.add(c);
					}
				}
			}
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getExpenseBillPaymentsHavingNoCheques.");
	}
	
private BigDecimal getNonSubledgerDeductions(BigDecimal billVHId) {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("SELECT SUM(gl.creditamount) " +
				"FROM generalledger gl " +
				"WHERE gl.creditamount>0 " +
				"AND gl.glcodeid NOT IN (:glcodeIdList) " +
				"AND voucherheaderid  ="+billVHId+" " +
				"AND gl.glcodeid NOT IN " +
				"(SELECT glcodeid FROM chartofaccountdetail) order by gl.glcode");
		query.setParameterList("glcodeIdList", (Collection)cBillGlcodeIdList);
		if(query.list()!=null && !query.list().isEmpty()){
			return (BigDecimal)query.list().get(0);
		}else{
			return BigDecimal.valueOf(0); 
		}
		
	}
	// Getting only those payments for which  cheques have been assigned but no cheque is surrendered. 
	@SuppressWarnings("unchecked")
	private void getExpenseBillPaymentsWithNoSurrenderedCheque() throws NumberFormatException, EGOVException {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getExpenseBillPaymentsWithNoSurrenderedCheque...ALREADY ASSIGNED: ");
		List<Long> billVHIds = new ArrayList<Long>(); 	
		List<Long> billVHIdsForDebtitSideCC = new ArrayList<Long>();
		Map<Long,List<Object[]>> billVHIdAndgeneralLedgerDetailListMap = new HashMap<Long,List<Object[]>>();
		Map<Long,List<Object[]>> billVHIdAndGLDListForDebtitSideCCMap = new HashMap<Long,List<Object[]>>();
		List<Object[]> generalLedgerDetailList  = new ArrayList<Object[]>();
		List<Object[]> generalLedgerDetailListForDebtitSideCC = new ArrayList<Object[]>();
		List<ChequeAssignment> billChequeAssignmentList = null;
		String strQuery=" select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,"+
						" misbill.paidto as paidTo,decode(sum(misbill.paidamount),null,0,sum(misbill.paidamount)) as paidAmount,sysdate as chequeDate,misbill.billvhid as billVHId "+
						" from Paymentheader ph, voucherheader vh ,vouchermis vmis, Miscbilldetail misbill , generalledger gl,voucherheader billvh, eg_billregister br,eg_billregistermis billmis  "+
						" where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +  
						" and gl.voucherheaderid =vh.id and gl.creditamount>0 and misbill.billvhid=billvh.id  and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype='"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"' "+
						" and not exists(select 1 from egf_instrumentvoucher iv, egf_instrumentheader ih where ih.id= iv.instrumentheaderid and iv.voucherheaderid=vh.id and ih.id_status not in ("+statusId+") )   "+
						" and exists (select 1 from egf_instrumentvoucher iv where  iv.voucherheaderid=vh.id) group by misbill.billvhid,vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto ";
		Query query = HibernateUtil.getCurrentSession().createSQLQuery(strQuery)
					.addScalar("voucherid").addScalar("voucherNumber").addScalar("voucherDate").addScalar("paidAmount").addScalar("chequeDate").addScalar("paidTo").addScalar("billVHId").addScalar("detailtypeid").addScalar("detailkeyid")
					.setResultTransformer(Transformers.aliasToBean(ChequeAssignment.class));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: No surrendered cheques - "+strQuery);
		billChequeAssignmentList = query.list();
		for(ChequeAssignment ca :billChequeAssignmentList){
			billVHIds.add(ca.getBillVHId().longValue());
		}
		if(billVHIds!=null && billVHIds.size()>0){
			generalLedgerDetailList = getDetailTypeKeyAmtForBillVHId(billVHIds);
		}
		for(Object[] gld:generalLedgerDetailList){
			if(billVHIdAndgeneralLedgerDetailListMap.containsKey(getLongValue(gld[3]))){
				billVHIdAndgeneralLedgerDetailListMap.get(getLongValue(gld[3])).add(gld);
			}else{
				List<Object[]> generalLedgerDetails =new ArrayList<Object[]>();
				generalLedgerDetails.add(gld);
				billVHIdAndgeneralLedgerDetailListMap.put(getLongValue(gld[3]),generalLedgerDetails);
			}
			
		}
		for(ChequeAssignment ca :billChequeAssignmentList){
			List <Object[]> detailTypeKeyAmtList=  billVHIdAndgeneralLedgerDetailListMap.get(ca.getBillVHId().longValue());
			
			if(detailTypeKeyAmtList==null || detailTypeKeyAmtList.size()==0)
			{
				billVHIdsForDebtitSideCC.add(ca.getBillVHId().longValue());
			}
		}
		if(billVHIdsForDebtitSideCC!=null && billVHIdsForDebtitSideCC.size()>0){
			generalLedgerDetailListForDebtitSideCC = getDetailTypeKeyAmtForDebtitSideCC(billVHIdsForDebtitSideCC);
		}
		for(Object[] gld:generalLedgerDetailListForDebtitSideCC){
			if(billVHIdAndGLDListForDebtitSideCCMap.containsKey(getLongValue(gld[3]))){
				billVHIdAndGLDListForDebtitSideCCMap.get(getLongValue(gld[3])).add(gld);
			}else{
				List<Object[]> generalLedgerDetails =new ArrayList<Object[]>();
				generalLedgerDetails.add(gld);
				billVHIdAndGLDListForDebtitSideCCMap.put(getLongValue(gld[3]),generalLedgerDetails);
			}
			
		}
		for(ChequeAssignment chqAssgn:billChequeAssignmentList)
		{
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: Start Checking for Billvhid "+chqAssgn.getBillVHId());
			if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: checking getDetailTypeKeyAmtForBillVHId for Net payable codes");
			List <Object[]> detailTypeKeyAmtList= billVHIdAndgeneralLedgerDetailListMap.get(chqAssgn.getBillVHId().longValue())!=null? billVHIdAndgeneralLedgerDetailListMap.get(chqAssgn.getBillVHId().longValue()):new ArrayList<Object[]>();
			if(detailTypeKeyAmtList!=null && detailTypeKeyAmtList.size()!=0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: detailTypeKeyAmtList for Billvhid "+chqAssgn.getBillVHId()+" size :"+detailTypeKeyAmtList.size());
				if(detailTypeKeyAmtList.size()<2)//single subledger
				{
					
					String queryString = " select distinct(ih.payTo) from egf_InstrumentHeader ih, egf_InstrumentVoucher iv where iv.instrumentHeaderId=ih.id " +
							"and iv.voucherHeaderId="+chqAssgn.getVoucherid()+" and ih.payTo=:payTo and ih.id_status in ("+statusId+")  ";
					if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: queryString"+queryString);
					
					List<Object> payTo=HibernateUtil.getCurrentSession().createSQLQuery(queryString ).setString("payTo", chqAssgn.getPaidTo()).list();
					
					if(payTo==null || payTo.size()==0)
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug(" ALREADY ASSIGNED: adding to chequeAssignlist as payTo s null or size 0"+chqAssgn);	
						tempExpenseChequeAssignmentList.add(chqAssgn);
					}
					else
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: Not adding continuing");
						continue;
					}
				}
				else
				{
					if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED:  Entering detailTypeKeyAmtList.size()>2 code");
					for(Object[] detailTypeKeyAmtObj: detailTypeKeyAmtList)
					{
						String queryString = " select distinct(ih.payTo) from egf_InstrumentHeader ih, egf_InstrumentVoucher iv where " +
								"iv.instrumentHeaderId=ih.id and iv.voucherHeaderId="+chqAssgn.getVoucherid()+" " +
										"and ih.detailTypeId="+detailTypeKeyAmtObj[0]+" and ih.detailKeyId="+detailTypeKeyAmtObj[1]+" " +
												"and ih.id_status in ("+statusId+")  ";
						if(LOGGER.isDebugEnabled())     LOGGER.debug("queryString"+queryString);
						List<Object> payTo=HibernateUtil.getCurrentSession().createSQLQuery(queryString).list();
						if(payTo==null || payTo.size()==0)
						{
							//this check will avoid already assigned by single subledger take subleger logic as it should be single subledger take payto
							 queryString = " select distinct(ih.payTo) from egf_InstrumentHeader ih, egf_InstrumentVoucher iv where iv.instrumentHeaderId=ih.id " +
									"and iv.voucherHeaderId="+chqAssgn.getVoucherid()+" and ih.payTo=:payTo and ih.id_status in ("+statusId+")  ";
							 if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: queryString"+queryString);
								payTo=HibernateUtil.getCurrentSession().createSQLQuery(queryString ).setString("payTo", chqAssgn.getPaidTo()).list();
								if(payTo!=null ) continue;
							ChequeAssignment ca=new ChequeAssignment();
							ca.setVoucherid( new BigDecimal(chqAssgn.getVoucherid()));
							ca.setVoucherNumber(chqAssgn.getVoucherNumber());
							ca.setVoucherDate(chqAssgn.getVoucherDate());
							ca.setPaidAmount((BigDecimal) detailTypeKeyAmtObj[2]);
							ca.setChequeDate(chqAssgn.getChequeDate());
							ca.setPaidTo(getEntity( Integer.parseInt(detailTypeKeyAmtObj[0].toString()), (Serializable) detailTypeKeyAmtObj[1]).getName());
							ca.setDetailtypeid((BigDecimal) detailTypeKeyAmtObj[0]);
							ca.setDetailkeyid((BigDecimal) detailTypeKeyAmtObj[1]);
							if(LOGGER.isDebugEnabled())     LOGGER.debug(" ALREADY ASSIGNED: adding to chequeAssignlist"+ca);
							tempExpenseChequeAssignmentList.add(ca);
						}
						else
						{
							if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: Not adding continuing");
							continue;
							
						}
					}
				}
			}
			else
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED:  entering getDetailTypeKeyAmtForDebtitSideCC  ");
				detailTypeKeyAmtList =billVHIdAndGLDListForDebtitSideCCMap.get(chqAssgn.getBillVHId().longValue())!=null?billVHIdAndGLDListForDebtitSideCCMap.get(chqAssgn.getBillVHId().longValue()):new ArrayList<Object[]>();
				if(detailTypeKeyAmtList==null||detailTypeKeyAmtList.size()==0)
				{
					String queryString = " select distinct(ih.payTo) from egf_InstrumentHeader ih, egf_InstrumentVoucher iv where iv.instrumentHeaderId=ih.id and iv.voucherHeaderId="+chqAssgn.getVoucherid()+" and ih.payTo =:payTo and ih.id_status in ("+statusId+")  ";
					if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: queryString"+queryString);
					List<Object> payTo=HibernateUtil.getCurrentSession().createSQLQuery(queryString ).setString("payTo", chqAssgn.getPaidTo()).list();
					if(payTo==null || payTo.size()==0)
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: adding to chequeAssignlist as payto is null or 0"+chqAssgn);
						tempExpenseChequeAssignmentList.add(chqAssgn);
					}
				}
				else
				{
					Map<String,BigDecimal> dedMap =new HashMap<String,BigDecimal>();
					dedMap=getSubledgerAmtForDeduction(chqAssgn.getBillVHId());
					String key="";
					for(Object[] obj:detailTypeKeyAmtList)
					{
						String queryString = " select distinct(ih.payTo) from egf_InstrumentHeader ih, egf_InstrumentVoucher iv where iv.instrumentHeaderId=ih.id and iv.voucherHeaderId="+chqAssgn.getVoucherid()+" and ih.detailTypeId="+obj[0]+" and ih.detailKeyId="+obj[1]+" and ih.id_status in ("+statusId+")  ";
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: Querying for "+queryString);
						List<Object> payTo=HibernateUtil.getCurrentSession().createSQLQuery(queryString).list();
						if(payTo==null || payTo.size()==0)
						{
						
						//this check will avoid already assigned by single subledger take subleger logic as it should be single subledger take payto
							queryString = " select distinct(ih.payTo) from egf_InstrumentHeader ih, egf_InstrumentVoucher iv where iv.instrumentHeaderId=ih.id " +
										"and iv.voucherHeaderId="+chqAssgn.getVoucherid()+" and ih.payTo=:payTo and ih.id_status in ("+statusId+")  ";
								 if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: queryString"+queryString);								
									payTo=HibernateUtil.getCurrentSession().createSQLQuery(queryString ).setString("payTo",chqAssgn.getPaidTo()).list();
									if(payTo!=null ) continue;
						
							ChequeAssignment c = new ChequeAssignment();
							c.setChequeDate(chqAssgn.getChequeDate());
							c.setVoucherHeaderId(chqAssgn.getVoucherid());
							c.setVoucherNumber(chqAssgn.getVoucherNumber());
							c.setVoucherDate(chqAssgn.getVoucherDate());
							c.setDetailtypeid((BigDecimal)obj[0]);
							c.setDetailkeyid((BigDecimal)obj[1]);
							key = obj[0].toString()+DELIMETER+obj[1].toString();
							c.setPaidAmount((dedMap.get(key)==null?(BigDecimal)obj[2]:((BigDecimal)obj[2]).subtract(dedMap.get(key))));
							c.setPaidTo(getEntity(Integer.valueOf(obj[0].toString()), (Serializable)obj[1]).getName());
							if(LOGGER.isDebugEnabled())     LOGGER.debug("ALREADY ASSIGNED: adding to chequeAssignlist as from payTo==null || payTo.size()==0 \n"+c);
							tempExpenseChequeAssignmentList.add(c);
						}
						else continue;
					}
				}
			}
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getExpenseBillPaymentsWithNoSurrenderedCheque.");
	}
	
	// Getting only those payments associated with surrendered cheques
	@SuppressWarnings("unchecked")
	private void getExpenseBillPaymentsWithSurrenderedCheques() throws NumberFormatException, EGOVException {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getExpenseBillPaymentsWithSurrenderedCheques...ASSIGNED BUT SURRENDARD: ");
		List<ChequeAssignment> billChequeAssignmentList = null;
		List<Long> billVHIds = new ArrayList<Long>(); 	
		List<Long> billVHIdsForDebtitSideCC = new ArrayList<Long>();
		Map<Long,List<Object[]>> billVHIdAndgeneralLedgerDetailListMap = new HashMap<Long,List<Object[]>>();
		Map<Long,List<Object[]>> billVHIdAndGLDListForDebtitSideCCMap = new HashMap<Long,List<Object[]>>();
		List<Object[]> generalLedgerDetailList  = new ArrayList<Object[]>();
		List<Object[]> generalLedgerDetailListForDebtitSideCC = new ArrayList<Object[]>();
		String strQuery=" select vh.id as voucherid ,vh.voucherNumber as voucherNumber ,vh.voucherDate as voucherDate ,0 as detailtypeid ,0 as detailkeyid ,"+
						" misbill.paidto as paidTo,decode(sum(misbill.paidamount),null,0,sum(misbill.paidamount)) as paidAmount,sysdate as chequeDate,misbill.billvhid as billVHId  "+
						" from Paymentheader ph, voucherheader vh ,vouchermis vmis, Miscbilldetail misbill , generalledger gl,voucherheader billvh, eg_billregister br,eg_billregistermis billmis   "+
						" where ph.voucherheaderid=misbill.payvhid and ph.voucherheaderid=vh.id and vmis.voucherheaderid= vh.id and vh.status ="+approvedstatus+" "+filterConditions+" " +  
						" and gl.voucherheaderid =vh.id and gl.creditamount>0 and misbill.billvhid=billvh.id  and br.id=billmis.billid and billmis.voucherheaderid=billvh.id and br.expendituretype='"+FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT+"' "+
						" and exists(select 1 from egf_instrumentvoucher iv, egf_instrumentheader ih where ih.id= iv.instrumentheaderid and iv.voucherheaderid=vh.id and ih.id_status not in ("+statusId+") ) "+
						" group by misbill.billvhid,vh.id,vh.voucherNumber,vh.voucherDate,misbill.paidto ";
		Query query = HibernateUtil.getCurrentSession().createSQLQuery(strQuery)
					.addScalar("voucherid").addScalar("voucherNumber").addScalar("voucherDate").addScalar("paidAmount").addScalar("chequeDate").addScalar("paidTo").addScalar("billVHId").addScalar("detailtypeid").addScalar("detailkeyid")
					.setResultTransformer(Transformers.aliasToBean(ChequeAssignment.class));
		if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: With surrendered cheques - "+strQuery);
		billChequeAssignmentList = query.list();
		for(ChequeAssignment ca :billChequeAssignmentList){
			billVHIds.add(ca.getBillVHId().longValue());
		}
		if(billVHIds!=null && billVHIds.size()>0){
			generalLedgerDetailList = getDetailTypeKeyAmtForBillVHId(billVHIds);
		}
		for(Object[] gld:generalLedgerDetailList){
			if(billVHIdAndgeneralLedgerDetailListMap.containsKey(getLongValue(gld[3]))){
				billVHIdAndgeneralLedgerDetailListMap.get(getLongValue(gld[3])).add(gld);
			}else{
				List<Object[]> generalLedgerDetails =new ArrayList<Object[]>();
				generalLedgerDetails.add(gld);
				billVHIdAndgeneralLedgerDetailListMap.put(getLongValue(gld[3]),generalLedgerDetails);
			}
			
		}
		for(ChequeAssignment ca :billChequeAssignmentList){
			List <Object[]> detailTypeKeyAmtList=  billVHIdAndgeneralLedgerDetailListMap.get(ca.getBillVHId().longValue());
			
			if(detailTypeKeyAmtList==null || detailTypeKeyAmtList.size()==0)
			{
				billVHIdsForDebtitSideCC.add(ca.getBillVHId().longValue());
			}
		}
		if(billVHIdsForDebtitSideCC!=null && billVHIdsForDebtitSideCC.size()>0){
			generalLedgerDetailListForDebtitSideCC = getDetailTypeKeyAmtForDebtitSideCC(billVHIdsForDebtitSideCC);
		}
		for(Object[] gld:generalLedgerDetailListForDebtitSideCC){
			if(billVHIdAndGLDListForDebtitSideCCMap.containsKey(getLongValue(gld[3]))){
				billVHIdAndGLDListForDebtitSideCCMap.get(getLongValue(gld[3])).add(gld);
			}else{
				List<Object[]> generalLedgerDetails =new ArrayList<Object[]>();
				generalLedgerDetails.add(gld);
				billVHIdAndGLDListForDebtitSideCCMap.put(getLongValue(gld[3]),generalLedgerDetails);
			}
			
		}
		for(ChequeAssignment chqAssgn:billChequeAssignmentList)
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: With surrendered cheques -  for Billvhid"+chqAssgn.getBillVHId());
			List <Object[]> detailTypeKeyAmtList=  billVHIdAndgeneralLedgerDetailListMap.get(chqAssgn.getBillVHId().longValue())!=null? billVHIdAndgeneralLedgerDetailListMap.get(chqAssgn.getBillVHId().longValue()):new ArrayList<Object[]>();
			
			if(detailTypeKeyAmtList!=null && detailTypeKeyAmtList.size()!=0)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: With surrendered cheques -  for Billvhid "+chqAssgn.getBillVHId()+" and size "+detailTypeKeyAmtList);
				if(detailTypeKeyAmtList.size()<2)
				{
					String queryString = " select iv.id,ih.id_status from egf_instrumentheader ih, egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and iv.voucherheaderid="+chqAssgn.getVoucherid()+" and ih.payTo=:payTo order by id desc   ";
					if(LOGGER.isDebugEnabled())     LOGGER.debug("instrumentStatus- "+queryString);
					List<Object[]> instrumentStatus = (List<Object[]>)HibernateUtil.getCurrentSession().createSQLQuery(queryString ).setString("payTo", chqAssgn.getPaidTo()).list();
					if(instrumentStatus==null || instrumentStatus.size()==0 ||(!instrumentStatus.get(0)[1].toString().equalsIgnoreCase(instrumentNewStatus) && !instrumentStatus.get(0)[1].toString().equalsIgnoreCase( instrumentReconciledStatus)))
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: Adding to chequeAssignmentlist as istrumentStatus "+chqAssgn);
						tempExpenseChequeAssignmentList.add(chqAssgn);
					}
					else
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: Continuing not adding");
						continue;
					}
				}
				else
				{
					for(Object[] detailTypeKeyAmtObj: detailTypeKeyAmtList)
					{
						String queryString = " select iv.id,ih.id_status from egf_instrumentheader ih, egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and iv.voucherheaderid="+chqAssgn.getVoucherid()+" and ih.detailtypeid="+detailTypeKeyAmtObj[0]+" and ih.detailkeyid="+detailTypeKeyAmtObj[1]+" order by id desc ";
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: Inside detailTypeKeyAmtList loop- "+queryString);
						List<Object[]> instrumentStatus = (List<Object[]>)HibernateUtil.getCurrentSession().createSQLQuery(queryString  ).list();
						if(instrumentStatus==null || instrumentStatus.size()==0 ||(!instrumentStatus.get(0)[1].toString().equalsIgnoreCase(instrumentNewStatus) && !instrumentStatus.get(0)[1].toString().equalsIgnoreCase( instrumentReconciledStatus)))
						{
							ChequeAssignment ca=new ChequeAssignment();
							ca.setVoucherid( new BigDecimal(chqAssgn.getVoucherid()));
							ca.setVoucherNumber(chqAssgn.getVoucherNumber());
							ca.setVoucherDate(chqAssgn.getVoucherDate());
							ca.setPaidAmount((BigDecimal) detailTypeKeyAmtObj[2]);
							ca.setChequeDate(chqAssgn.getChequeDate());
							ca.setPaidTo(getEntity( Integer.parseInt(detailTypeKeyAmtObj[0].toString()), (Serializable) detailTypeKeyAmtObj[1]).getName());
							ca.setDetailtypeid((BigDecimal) detailTypeKeyAmtObj[0]);
							ca.setDetailkeyid((BigDecimal) detailTypeKeyAmtObj[1]);
							if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: inside loop adding  "+ca);
							tempExpenseChequeAssignmentList.add(ca);
						}
						else
						{
							continue;
						}
					}
				}
			}// End of checking bills with SL where credit amount>0
			else
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD:  checking getDetailTypeKeyAmtForDebtitSideCC for "+chqAssgn.getBillVHId());
				detailTypeKeyAmtList =billVHIdAndGLDListForDebtitSideCCMap.get(chqAssgn.getBillVHId().longValue())!=null?billVHIdAndGLDListForDebtitSideCCMap.get(chqAssgn.getBillVHId().longValue()):new ArrayList<Object[]>();
				if(detailTypeKeyAmtList==null||detailTypeKeyAmtList.size()==0)
				{
					String queryString = " select iv.id,ih.id_status from egf_instrumentheader ih, egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and iv.voucherheaderid="+chqAssgn.getVoucherid()+" and ih.payTo=:payTo order by id desc   ";
					if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: getDetailTypeKeyAmtForDebtitSideCC "+queryString);
					List<Object[]> instrumentStatus = (List<Object[]>)HibernateUtil.getCurrentSession().createSQLQuery(queryString ).setString("payTo",chqAssgn.getPaidTo()).list();
					
					if(instrumentStatus==null || instrumentStatus.size()==0 ||(!instrumentStatus.get(0)[1].toString().equalsIgnoreCase(instrumentNewStatus) && !instrumentStatus.get(0)[1].toString().equalsIgnoreCase( instrumentReconciledStatus)))
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: Adding to chequeAssignmentlist in getDetailTypeKeyAmtForDebtitSideCC "+chqAssgn);
						tempExpenseChequeAssignmentList.add(chqAssgn);
					}
				}
				else if (detailTypeKeyAmtList!=null && detailTypeKeyAmtList.size()==1)
                {
					String queryString = " select iv.id,ih.id_status from egf_instrumentheader ih, egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and iv.voucherheaderid="+chqAssgn.getVoucherid()+" and ih.payTo=:payTo order by id desc   ";
					if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: detailTypeKeyAmtList size=1"+queryString);
					List<Object[]> instrumentStatus = (List<Object[]>)HibernateUtil.getCurrentSession().createSQLQuery(queryString ).setString("payTo", chqAssgn.getPaidTo()).list();
					if(instrumentStatus==null || instrumentStatus.size()==0 ||(!instrumentStatus.get(0)[1].toString().equalsIgnoreCase(instrumentNewStatus) && !instrumentStatus.get(0)[1].toString().equalsIgnoreCase( instrumentReconciledStatus)))
					{
						String queryString2 = " select iv.id,ih.id_status from egf_instrumentheader ih, " +
                		" egf_instrumentvoucher iv where iv.instrumentheaderid=ih.id and iv.voucherheaderid="+chqAssgn.getVoucherid()+" " +
						" and ih.payTo=:payTo order by id desc   ";
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: detailTypeKeyAmtList  again checking "+queryString2);
						List<Object[]> instrumentStatusWithsubledgerPaidto = (List<Object[]>)HibernateUtil.getCurrentSession().createSQLQuery(queryString2 )
								.setString("payTo", getEntity( Integer.parseInt(detailTypeKeyAmtList.get(0)[0].toString()), (Serializable) detailTypeKeyAmtList.get(0)[1]).getName())
								.list();
						if(instrumentStatusWithsubledgerPaidto==null || instrumentStatusWithsubledgerPaidto.size()==0 ||(!instrumentStatusWithsubledgerPaidto.get(0)[1].toString().equalsIgnoreCase(instrumentNewStatus) && !instrumentStatusWithsubledgerPaidto.get(0)[1].toString().equalsIgnoreCase( instrumentReconciledStatus)))
						{
							if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: adding inside  again checking"+chqAssgn);
							tempExpenseChequeAssignmentList.add(chqAssgn);
						}
					}
                }
				else// if more than 1 SL entries with debit side CC
				{
					Map<String,BigDecimal> dedMap =new HashMap<String,BigDecimal>();
					dedMap=getSubledgerAmtForDeduction(chqAssgn.getBillVHId());
					String key="";
					for(Object[] obj:detailTypeKeyAmtList)
					{
						String queryString = " select iv.id,ih.id_status from egf_instrumentheader ih, egf_instrumentvoucher iv where " +
								"iv.instrumentheaderid=ih.id and iv.voucherheaderid="+chqAssgn.getVoucherid()+"" +
										" and ih.detailtypeid="+obj[0]+" and ih.detailkeyid="+obj[1]+" order by id desc ";
						if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: detailTypeKeyAmtList  checking "+queryString);
						List<Object[]> instrumentStatus = (List<Object[]>)HibernateUtil.getCurrentSession().createSQLQuery(queryString  ).list();
						if(instrumentStatus==null || instrumentStatus.size()==0 ||(!instrumentStatus.get(0)[1].toString().equalsIgnoreCase(instrumentNewStatus) && !instrumentStatus.get(0)[1].toString().equalsIgnoreCase( instrumentReconciledStatus)))
						{
							ChequeAssignment c = new ChequeAssignment();
							c.setChequeDate(chqAssgn.getChequeDate());
							c.setVoucherHeaderId(chqAssgn.getVoucherid());
							c.setVoucherNumber(chqAssgn.getVoucherNumber());
							c.setVoucherDate(chqAssgn.getVoucherDate());
							c.setDetailtypeid((BigDecimal)obj[0]);
							c.setDetailkeyid((BigDecimal)obj[1]);
							key = obj[0].toString()+DELIMETER+obj[1].toString();
							c.setPaidAmount((dedMap.get(key)==null?(BigDecimal)obj[2]:((BigDecimal)obj[2]).subtract(dedMap.get(key))));
							c.setPaidTo(getEntity(Integer.valueOf(obj[0].toString()), (Serializable)obj[1]).getName());
							if(LOGGER.isDebugEnabled())     LOGGER.debug("ASSIGNED BUT SURRENDARD: adding inside  detailTypeKeyAmtList loop"+c);
							tempExpenseChequeAssignmentList.add(c);
						}
						else continue;
					}
				}
			}// End of main Else
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getExpenseBillPaymentsWithSurrenderedCheques.");
	}
	
	private String getFilterParamaters(Map<String,String[]> parameters,CVoucherHeader voucherHeader) throws ParseException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getFilterParamaters...");
		StringBuffer sql =new StringBuffer();
		if(!"".equals(parameters.get("fromDate")[0]))
			sql.append(" and vh.voucherDate>='"+sdf.format(formatter.parse(parameters.get("fromDate")[0]))+"' ");
		if(!"".equals(parameters.get("toDate")[0]))
			sql.append(" and vh.voucherDate<='"+sdf.format(formatter.parse(parameters.get("toDate")[0]))+"'");
		if(!StringUtils.isEmpty(voucherHeader.getVoucherNumber()))
			sql.append(" and vh.voucherNumber like '%"+voucherHeader.getVoucherNumber()+"%'");
		if(voucherHeader.getFundId()!=null)
			sql.append(" and vh.fundId="+voucherHeader.getFundId().getId());
		if(voucherHeader.getFundsourceId()!=null)
			sql.append(" and vmis.fundsourceId="+voucherHeader.getFundsourceId().getId());
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
			sql.append(" and vmis.departmentid="+voucherHeader.getVouchermis().getDepartmentid().getId());
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
			sql.append(" and vmis.schemeid="+voucherHeader.getVouchermis().getSchemeid().getId());
		if(voucherHeader.getVouchermis().getSubschemeid()!=null)
			sql.append(" and vmis.subschemeid="+voucherHeader.getVouchermis().getSubschemeid().getId());
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
			sql.append(" and vmis.functionaryid="+voucherHeader.getVouchermis().getFunctionary().getId());
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
			sql.append(" and vmis.divisionid="+voucherHeader.getVouchermis().getDivisionid().getId());
		sql.append(" and ph.bankaccountnumberid="+parameters.get("bankaccount")[0]);
		sql.append(" and lower(ph.type)=lower('"+parameters.get("paymentMode")[0]+"')");
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getFilterParamaters.");
		return sql.toString();

	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getDetailTypeKeyAmtForBillVHId(List<Long> billVHIds)
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getDetailTypeKeyAmtForBillVHId...");
		List<Long> cBillGlcodeIdsList = new ArrayList<Long>();
		for(BigDecimal glCodeId: cBillGlcodeIdList){
			cBillGlcodeIdsList.add(glCodeId.longValue());
		}
		List<Object[]> generalLedgerDetailList = new ArrayList<Object[]>();
		int size = billVHIds.size();
		if(size>999)
		{
			int fromIndex = 0;
			int toIndex = 0; 
			int step = 1000;
			List<Object[]> newGLDList ;
			while(size-step>=0)
			{
				newGLDList = new ArrayList<Object[]>();
				toIndex += step;  
				 Query generalLedgerDetailsQuery =HibernateUtil.getCurrentSession().createQuery(" select gld.detailTypeId,gld.detailKeyId,gld.amount,gl.voucherHeaderId.id from CGeneralLedger gl, CGeneralLedgerDetail gld  where gl.voucherHeaderId.id in ( :IDS ) and gl.id = gld.generalLedgerId and gl.creditAmount>0 and gl.glcodeId.id in (:glcodeIdList)");
				 generalLedgerDetailsQuery.setParameterList("IDS", billVHIds.subList(fromIndex, toIndex));
				 generalLedgerDetailsQuery.setParameterList("glcodeIdList",cBillGlcodeIdsList);
				 newGLDList = generalLedgerDetailsQuery.list();
				fromIndex = toIndex;
				size-=step;
				if(newGLDList!=null)
				{
					generalLedgerDetailList.addAll(newGLDList);
				}
				

			}

			if(size>0)
			{
				newGLDList = new ArrayList<Object[]>();
				fromIndex = toIndex;
				toIndex = fromIndex+size;
				Query generalLedgerDetailsQuery =HibernateUtil.getCurrentSession().createQuery(" select gld.detailTypeId,gld.detailKeyId,gld.amount,gl.voucherHeaderId.id from CGeneralLedger gl, CGeneralLedgerDetail gld  where gl.voucherHeaderId.id in ( :IDS ) and gl.id = gld.generalLedgerId and gl.creditAmount>0 and gl.glcodeId.id in (:glcodeIdList)");
				generalLedgerDetailsQuery.setParameterList("IDS", billVHIds.subList(fromIndex, toIndex));
				generalLedgerDetailsQuery.setParameterList("glcodeIdList", cBillGlcodeIdsList);
				newGLDList = generalLedgerDetailsQuery.list();
				if(newGLDList!=null)
				{
					generalLedgerDetailList.addAll(newGLDList);
				}
			}
			

		}else
		{
			Query generalLedgerDetailsQuery =HibernateUtil.getCurrentSession().createQuery(" select gld.detailTypeId,gld.detailKeyId,gld.amount,gl.voucherHeaderId.id from CGeneralLedger gl, CGeneralLedgerDetail gld  where gl.voucherHeaderId.id in ( :IDS ) and gl.id = gld.generalLedgerId and gl.creditAmount>0 and gl.glcodeId.id in (:glcodeIdList)");
			generalLedgerDetailsQuery.setParameterList("IDS",billVHIds);
			generalLedgerDetailsQuery.setParameterList("glcodeIdList",cBillGlcodeIdsList);
			generalLedgerDetailList = generalLedgerDetailsQuery.list();
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getDetailTypeKeyAmtForBillVHId.");
		return generalLedgerDetailList;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getDetailTypeKeyAmtForDebtitSideCC(List<Long> billVHIds)
	{
		List<Object[]> generalLedgerDetailList = new ArrayList<Object[]>();
		int size = billVHIds.size();
		if(size>999)
		{
			int fromIndex = 0;
			int toIndex = 0; 
			int step = 1000;
			List<Object[]> newGLDList ;
			while(size-step>=0)
			{
				newGLDList = new ArrayList<Object[]>();
				toIndex += step;  
				 Query generalLedgerDetailsQuery =HibernateUtil.getCurrentSession().createQuery(" select gld.detailTypeId,gld.detailKeyId,gld.amount,gl.voucherHeaderId.id from CGeneralLedger gl, CGeneralLedgerDetail gld  where gl.voucherHeaderId.id in ( :IDS ) and gl.id = gld.generalLedgerId and  gl.debitAmount>0");
				 generalLedgerDetailsQuery.setParameterList("IDS", billVHIds.subList(fromIndex, toIndex));
				 newGLDList = generalLedgerDetailsQuery.list();
				fromIndex = toIndex;
				size-=step;
				if(newGLDList!=null)
				{
					generalLedgerDetailList.addAll(newGLDList);
				}
				

			}

			if(size>0)
			{
				newGLDList = new ArrayList<Object[]>();
				fromIndex = toIndex;
				toIndex = fromIndex+size;
				Query generalLedgerDetailsQuery =HibernateUtil.getCurrentSession().createQuery(" select gld.detailTypeId,gld.detailKeyId,gld.amount,gl.voucherHeaderId.id from CGeneralLedger gl, CGeneralLedgerDetail gld  where gl.voucherHeaderId.id in ( :IDS ) and gl.id = gld.generalLedgerId and  gl.debitAmount>0");
				generalLedgerDetailsQuery.setParameterList("IDS", billVHIds.subList(fromIndex, toIndex));
				newGLDList = generalLedgerDetailsQuery.list();
				if(newGLDList!=null)
				{
					generalLedgerDetailList.addAll(newGLDList);
				}
			}
			

		}else
		{
			Query generalLedgerDetailsQuery =HibernateUtil.getCurrentSession().createQuery(" select gld.detailTypeId,gld.detailKeyId,gld.amount,gl.voucherHeaderId.id from CGeneralLedger gl, CGeneralLedgerDetail gld  where gl.voucherHeaderId.id in ( :IDS ) and gl.id = gld.generalLedgerId and  gl.debitAmount>0");
			generalLedgerDetailsQuery.setParameterList("IDS",billVHIds);
			generalLedgerDetailList = generalLedgerDetailsQuery.list();
		}
		return generalLedgerDetailList;
	}
	
	
	private void setStatusValues()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting setStatusValues...");
		finalChequeAssignmentList=new ArrayList<ChequeAssignment>();
		finalCBillChequeAssignmentList = new ArrayList<ChequeAssignment>();
		tempExpenseChequeAssignmentList = new ArrayList<ChequeAssignment>();
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","APPROVEDVOUCHERSTATUS");
		approvedstatus = appList.get(0).getValue();
		List<String> descriptionList = new ArrayList<String>();
		descriptionList.add("New");
		descriptionList.add("Reconciled");
		List<EgwStatus> egwStatusList = commonsService.getStatusListByModuleAndCodeList("Instrument", descriptionList);
		statusId="";
		for(EgwStatus egwStatus : egwStatusList)
		{
			statusId = statusId+egwStatus.getId()+",";
		}
		if(egwStatusList.size()==2)
		{
			instrumentNewStatus=egwStatusList.get(0).getId().toString();
			instrumentReconciledStatus=egwStatusList.get(1).getId().toString();
		}
		statusId = statusId.substring(0, statusId.length()-1);
		getGlcodeIds();
		//cBillGlcodeIdList=cBillGlcodeIdList;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed setStatusValues.");
	}
	
	private void prepareChequeList() {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting prepareChequeList...");
		ChequeAssignment outerChqAssgn;
		ChequeAssignment innerChqAssgn;
		List<Integer> alreadyProcessedIndices=new ArrayList<Integer>();
		for(int i=0;i<tempExpenseChequeAssignmentList.size();i++)
		{
			if(alreadyProcessedIndices.contains(i))
				continue;
			outerChqAssgn=tempExpenseChequeAssignmentList.get(i);
			for(int j=i+1;j<tempExpenseChequeAssignmentList.size();j++)
			{
				innerChqAssgn=tempExpenseChequeAssignmentList.get(j);
				if(outerChqAssgn.getVoucherid().equals(innerChqAssgn.getVoucherid())
					&& outerChqAssgn.getVoucherDate().equals(innerChqAssgn.getVoucherDate())
					&& outerChqAssgn.getVoucherNumber().equals(innerChqAssgn.getVoucherNumber())
					&& outerChqAssgn.getPaidTo().equals(innerChqAssgn.getPaidTo())
					&& outerChqAssgn.getDetailtypeid().equals(innerChqAssgn.getDetailtypeid())
					&& outerChqAssgn.getDetailkeyid().equals(innerChqAssgn.getDetailkeyid()))
				{
					outerChqAssgn.setPaidAmount(outerChqAssgn.getPaidAmount().add(innerChqAssgn.getPaidAmount()));
					alreadyProcessedIndices.add(j);
				}
			}
			finalCBillChequeAssignmentList.add(outerChqAssgn);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed prepareChequeList.");
	}
	@SuppressWarnings("unchecked")
	private Map<String, BigDecimal> getSubledgerAmtForDeduction(BigDecimal billVHId)
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getSubledgerAmtForDeduction...");
		Map<String,BigDecimal> map = new HashMap<String,BigDecimal>();
		Query query=HibernateUtil.getCurrentSession().createSQLQuery("SELECT gld.detailtypeid, gld.detailkeyid, SUM(gld.amount) FROM generalledgerdetail gld, generalledger gl" +
				" WHERE gl.voucherheaderid="+billVHId+" AND gl.id =gld.generalledgerid AND gl.creditamount  >0"+
				" AND gl.glcodeid NOT IN (:glcodeIdList) GROUP BY gld.detailtypeid, gld.detailkeyid"); 
		query.setParameterList("glcodeIdList", (Collection)cBillGlcodeIdList);
		List<Object[]> list = query.list();
		if(list!=null && !list.isEmpty())
		{
			for(Object[] ob : list)
				map.put(ob[0].toString()+DELIMETER+ob[1].toString(), (BigDecimal)ob[2]);
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getSubledgerAmtForDeduction.");
		return map;
	}
	public void getGlcodeIds() throws EGOVRuntimeException{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getGlcodeIds...");
		try{
			List<AppConfigValues> appList;

			worksBillGlcodeList = populateGlCodeIds(Constants.WORKS_BILL_PURPOSE_IDS);
			purchaseBillGlcodeList = populateGlCodeIds(Constants.PURCHASE_BILL_PURPOSE_IDS);
			salaryBillGlcodeList = populateGlCodeIds("salaryBillPurposeIds");
			
			//Contingent Bill
			appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,Constants.CONTINGENCY_BILL_PURPOSE_IDS);
			cBillGlcodeIdList = new ArrayList<BigDecimal>();
			if(appList != null && appList.size() > 0 ) {
				Integer iPurposeIds [] = new Integer[appList.size()]; 
				int z = 0;
				for (final AppConfigValues appConfigValues : appList) {
					iPurposeIds[z] = Integer.parseInt(appConfigValues.getValue()); 
					z++;
				}
				final List<CChartOfAccounts> coaList = cmnMngr.getAccountCodeByListOfPurposeId (iPurposeIds);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Size contingentBillGlcodeList"+coaList.size());
				contingentBillGlcodeList=coaList;
				for(CChartOfAccounts coa1 : coaList){
					//if(LOGGER.isDebugEnabled())     LOGGER.debug("Adding to contingentBillGlcodeList"+coa1.getGlcode()+":::"+coa1.getPurposeId());
					cBillGlcodeIdList.add(BigDecimal.valueOf(coa1.getId()));
				}
			}
		}catch (Exception e){
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getGlcodeIds.");
	}
	private List<CChartOfAccounts> populateGlCodeIds(String appConfigKey) throws  EGOVException {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting populateGlCodeIds...");
		List<CChartOfAccounts> glCodeList = new ArrayList<CChartOfAccounts>();
		
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,appConfigKey);
		String purposeids = appList.get(0).getValue();
		if(purposeids != null && !purposeids.equals("")){
			final String purposeIds[] = purposeids.split(",");
			for (final String purposeId : purposeIds){
				final List<CChartOfAccounts> coaList = cmnMngr.getAccountCodeByPurpose(Integer.parseInt(purposeId));
				for(CChartOfAccounts coa1 : coaList)
					glCodeList.add(coa1);
			}
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed populateGlCodeIds.");
		return glCodeList;
	}
	public EntityType getEntity(Integer detailTypeId,Serializable detailKeyId)throws EGOVException
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Starting getEntity...");
		EntityType entity;
		try
		{
			Accountdetailtype accountdetailtype = (Accountdetailtype) persistenceService.find(" from Accountdetailtype where id=?", detailTypeId);
			try
			{
				entity =  (EntityType) persistenceService.find(" from "+accountdetailtype.getFullQualifiedName()+ " where id=? ", Integer.valueOf(detailKeyId+""));
			}catch(Exception ee)
			{
				entity =  (EntityType) persistenceService.find(" from "+accountdetailtype.getFullQualifiedName()+ " where id=? ", Long.valueOf(detailKeyId+""));
			}
		}catch(Exception e)
		{
			LOGGER.error("Exception to get EntityType="+e.getMessage() + "for detailTypeId="+detailTypeId+"  for Detail key "+detailKeyId);
			throw new EGOVException("Exception to get EntityType="+e.getMessage());
		}
		if(entity==null)
		{
			LOGGER.error("Exception to get EntityType  for detailTypeId="+detailTypeId+"  for Detail key "+detailKeyId);
			throw new EGOVException("Exception to get EntityType");
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Completed getEntity.");
		return entity;
	}
	private Long getLongValue(Object object) {
		return object!= null? new Long(object.toString()):0;
	}
}
