package org.egov.web.actions.brs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Bankreconciliation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.brs.AutoReconcileBean;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.jboss.logging.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
@ParentPackage("egov")
@Results(value = {
		@Result(name = "PDF", type = "stream", location = "inputStream", params = {"inputName", "inputStream", "contentType", "application/pdf",	"contentDisposition","no-cache;filename=AutoReconcileReport.pdf" }),
		@Result(name = "XLS", type = "stream", location = "inputStream", params = {"inputName", "inputStream", "contentType", "application/xls",	"contentDisposition","no-cache;filename=AutoReconcileReport.xls" }) })

public class AutoReconciliationAction extends BaseFormAction {


	private static final String DID_NOT_FIND_MATCH_IN_BANKBOOK = "did not find match in Bank Book  (InstrumentHeader)";
	private  String file_already_uploaded = "This file (#name) already uploaded ";
	private String bank_account_not_match_msg = "Selected Bank account and spreadsheet ( #name ) account does not match";
	private static final long serialVersionUID = -4207341983597707193L;
	private static final Logger LOGGER =Logger.getLogger(AutoReconciliationAction.class);
	private static final int ACCOUNTNUMBER_ROW_INDEX = 2;
	private static final int STARTOF_DETAIL_ROW_INDEX = 8;
	private static final int SLNO_INDEX=0;
	private static final int TXNDT_INDEX=1;
	private static final int NARRATION_INDEX=2;
	private static final int CHEQUENO_INDEX=4;
	private static final int TYPE_INDEX=3;
	private static final int DEBIT_INDEX=5;
	private static final int CREDIT_INDEX=6;
	private static final int BALANCE_INDEX=7;
	private static final int CSLNO_INDEX=8;
	private final String BRS_TRANSACTION_TYPE_CHEQUE="CLG";
	private static final String BRS_TRANSACTION_TYPE_BANK = "TRF";
	private List<Bank> bankList;
	private List<Bankbranch> branchList=Collections.EMPTY_LIST;
	private List<Bankaccount> accountList=Collections.EMPTY_LIST;
	private Integer accountId;
	private Integer bankId;
	private Integer branchId;
	private Date reconciliationDate;
	private Date fromDate;
	private Date toDate;
	private String accNo;
	private File bankStatmentInXls;
	private String bankStatmentInXlsContentType;
	private String bankStatmentInXlsFileName;
	private String failureMessage="Invalid data in  the  following row(s), please correct and upload again\n";
	private String successMessage="BankStatement upload completed Successfully # rows processed";
	private boolean isFailed;
	private final String TABLENAME="egf_brs_bankstatements";
	private final String BRS_ACTION_TO_BE_PROCESSED="to be processed";
	private final String BRS_ACTION_TO_BE_PROCESSED_MANUALLY="to be processed manually";
	private final String BRS_ACTION_PROCESSED="processed";
	
	private String jasperpath = "/reports/templates/AutoReconcileReport.jasper";
	private ReportHelper reportHelper;
	private InputStream inputStream;
	private final String BRS_MESSAGE_DUPPLICATE="duplicate instrument number";
	private final String BRS_MESSAGE_MORE_THAN_ONE_MATCH="found more than one match in instruments"; 
	private final String BRS_MESSAGE_DUPPLICATE_IN_BANKSTATEMENT="duplicate instrument number within the bankstament";
	private List<AutoReconcileBean> failureList;
	private int executeUpdate;
	private String message="";
	private String dateInDotFormat="dd.mm.yyyy";
	private SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MMM/yyyy");
	private SQLQuery insertQuery;
	private String insertsql="insert into egf_brs_bankstatements (ID,ACCOUNTNUMBER,ACCOUNTID,TXDATE,TYPE,INSTRUMENTNO,DEBIT,CREDIT,BALANCE" +
			",NARRATION,CSLNO,CREATEDDATE) values (seq_egf_brs_bankstatements.nextval,:accNo,:accountId,to_date(:txDate,"+"'"+dateInDotFormat+"'),:type,:instrumentNo,:debit" +
			",:credit,:balance,:narration,:cslNo,sysdate)";
	private int count;
	private int rowIndex;
	private int rowCount;
	private List<AutoReconcileBean> statementsNotInBankBookList;
	private List<AutoReconcileBean> statementsFoundButNotProcessed;
	private FinancialYearDAO financialYearDAO;
	private Date finYearStartDate;
	private List<AutoReconcileBean> entriesNotInBankStament;
	private Bankaccount bankAccount;
	private BigDecimal notInBooktotalDebit;
	private BigDecimal notInBooktotalCredit;
	private BigDecimal notprocessedCredit;
	private BigDecimal notprocessedDebit;
	private BigDecimal notprocessedNet;
	private BigDecimal notInBookNet;
	private String notInBookNetBal;
	private BigDecimal notInStatementTotalDebit;
	private BigDecimal notInStatementTotalCredit;
	private BigDecimal notInStatementNet;
	private BigDecimal bankBookBalance;
	public BigDecimal getBankBookBalance() {
		return bankBookBalance;
	}
	public void setBankBookBalance(BigDecimal bankBookBalance) {
		this.bankBookBalance = bankBookBalance;
	}
	private BigDecimal brsBalance;
	private BigDecimal totalNotReconciledAmount;
	private Integer statusId;
	public BigDecimal getBrsBalance() {
		return brsBalance;
	}
	public void setBrsBalance(BigDecimal brsBalance) {
		this.brsBalance = brsBalance;
	}
	public Bankaccount getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(Bankaccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	@Override
	public Object getModel() {
		return new Bankreconciliation();
	}
	@SuppressWarnings("unchecked")
	public void prepare()
	{
		List bankList = persistenceService.findAllBy("select distinct b from Bank b , Bankbranch bb , Bankaccount ba WHERE bb.bank=b and ba.bankbranch=bb and b.isactive=1 order by upper(b.name)");
		dropdownData.put("bankList", bankList);
		dropdownData.put("branchList",branchList);
		dropdownData.put("accountList",accountList);
		if(branchId!=null )
		{
			branchList =  (List<Bankbranch>)persistenceService.findAllBy("select distinct bb from Bankbranch bb,Bankaccount ba where bb.bank.id=? and ba.bankbranch=bb and bb.isactive=1",bankId);
			dropdownData.put("branchList",branchList);
			
		}
		if(accountId!=null)
		{
			List<Bankaccount>	accountList =(List<Bankaccount>) getPersistenceService().findAllBy("from Bankaccount ba where ba.bankbranch.id=? and isactive=1 order by ba.chartofaccounts.glcode",branchId);
			dropdownData.put("accountList",accountList);  
		}
	}

@Action(value="/brs/autoReconciliation-newForm")
	public String newForm()
	{
		return NEW;
	}
	
@Action(value="/brs/autoReconciliation-beforeUpload")
	public String beforeUpload()
	{
		return "upload";
	}

	@ValidationErrorPage("upload")
	public String upload()
	{
		try {
			HibernateUtil.getCurrentSession().getTransaction().setTimeout(600);
			insertQuery = HibernateUtil.getCurrentSession().createSQLQuery(insertsql);
			Bankaccount ba = (Bankaccount)persistenceService.find("from Bankaccount ba where id=?",accountId);
			accNo=ba.getAccountnumber();
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(bankStatmentInXls));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			int numberOfSheets = wb.getNumberOfSheets();
			HSSFSheet sheet = wb.getSheetAt(0);
			int firstRowNum = sheet.getFirstRowNum();
			//Validating selected bankaccount and BankStatements bankaccount		
			HSSFRow row = sheet.getRow(ACCOUNTNUMBER_ROW_INDEX);
			if(row==null)
			{
				bank_account_not_match_msg=bank_account_not_match_msg.replace("#name", bankStatmentInXlsFileName);
				throw new ValidationException(Arrays.asList (new ValidationError(bank_account_not_match_msg,bank_account_not_match_msg)))	;
			}
			String strValue2 = getStrValue(row.getCell(0));
			strValue2=strValue2.substring(strValue2.indexOf(':')+1, strValue2.indexOf('-')).trim();
			if(!strValue2.equals(accNo.trim()))
			{
				bank_account_not_match_msg=bank_account_not_match_msg.replace("#name", bankStatmentInXlsFileName);
				throw new ValidationException(Arrays.asList (new ValidationError(bank_account_not_match_msg,bank_account_not_match_msg)))	;
			}
			
			AutoReconcileBean ab=null;
			HSSFRow detailRow=null;
			 String dateStr=null;
    		 rowIndex = STARTOF_DETAIL_ROW_INDEX;
    		 count = 0;
			do {
			try{
				
			ab=new AutoReconcileBean();
			if(rowIndex==STARTOF_DETAIL_ROW_INDEX)
			{
			detailRow = sheet.getRow(rowIndex);
			if(rowIndex>=9290)
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug(detailRow.getRowNum()); 
			}
			dateStr=getStrValue(detailRow.getCell(TXNDT_INDEX));
			if(alreadyUploaded(dateStr))
			{
				file_already_uploaded=file_already_uploaded.replace("#name", bankStatmentInXlsFileName);
				throw new ValidationException(Arrays.asList (new ValidationError(file_already_uploaded,file_already_uploaded)))	;
			}
			ab.setTxDateStr(dateStr);
			}
			ab.setTxDateStr(dateStr);
			 ab.setInstrumentNo(getStrValue(detailRow.getCell(CHEQUENO_INDEX)));
		   //if(strValue!=null)
			//	 ab.setInstrumentNo(strValue.replaceFirst(".0", ""));
			ab.setDebit(getNumericValue(detailRow.getCell(DEBIT_INDEX)));
			ab.setCredit(getNumericValue(detailRow.getCell(CREDIT_INDEX)));
			ab.setBalance(getNumericValue(detailRow.getCell(BALANCE_INDEX)));
			String	strValue=	getStrValue(detailRow.getCell(NARRATION_INDEX));
			  if(strValue!=null)
			  {
				  if(strValue.length()>125) 
					  strValue=strValue.substring(0, 125);
				  //strValue=strValue.replaceFirst(".0", "");
				  ab.setNarration(strValue);
			  }
			ab.setType(getStrValue(detailRow.getCell(TYPE_INDEX)));
			ab.setCSLno(getStrValue(detailRow.getCell(CSLNO_INDEX)));
			//if(ab.getType()==null)
				//ab.setType("CLG");
			if(LOGGER.isInfoEnabled())     LOGGER.info(detailRow.getRowNum()+"   "+ab.toString()); 
			insert(ab);	
			if(count%20==0)
			{
				HibernateUtil.getCurrentSession().flush();
			}
			
						
			
				}catch (NumberFormatException e) {
					if(!isFailed)
					{
						failureMessage+=detailRow.getRowNum()+1;
					}else
					{
						failureMessage+=" , "+detailRow.getRowNum()+1;
					}
					isFailed=true;
					
				throw new ValidationException(Arrays.asList (new ValidationError(failureMessage,failureMessage)))	;
				}
				rowIndex++;
				count++;
				detailRow = sheet.getRow(rowIndex);
				if(detailRow!=null)
					dateStr = getStrValue(detailRow.getCell(TXNDT_INDEX));
				else
					dateStr=null;
				//ab.setTxDateStr(detailRow.getRowNum()+"-->" + dateStr);

			}
			while (dateStr!=null && !dateStr.isEmpty());
			
			if(isFailed)  
		   {
				throw new ValidationException(Arrays.asList (new ValidationError(failureMessage,failureMessage)))	;
		   }else
		   {
			  message = successMessage.replace("#", ""+count); 
		   }    
			
	 
		    
		  
		  
	} catch (FileNotFoundException e) {
		throw new ValidationException(Arrays.asList (new ValidationError("File cannot be uploaded","File cannot be uploaded")));

	} catch (IOException e) {
		throw new ValidationException(Arrays.asList (new ValidationError("Unable to read uploaded file","Unable to read uploaded file")));
	}	
	catch (ValidationException ve)
	{
		ve.printStackTrace();
		throw ve;
	}
	catch (NullPointerException npe)
	{
		throw new ValidationException(Arrays.asList (new ValidationError("Unknown error at line "+rowIndex,"Unknown error at line "+rowIndex)))	;
	}
	catch(Exception e)
	{
		throw new ValidationException(Arrays.asList (new ValidationError("Unknown error at line "+rowIndex,"Unknown error at line "+rowIndex)));
	}
		
		return "upload";
	}
	
private void insert(AutoReconcileBean ab) {
	
	
	insertQuery.setString("accNo", accNo)
	.setInteger("accountId",accountId)
	.setString("txDate",ab.getTxDateStr())
	.setString("type",ab.getType())
	.setString("instrumentNo", ab.getInstrumentNo())
	.setBigDecimal("debit", ab.getDebit())
	.setBigDecimal("credit", ab.getCredit())
	.setBigDecimal("balance", ab.getBalance())
	.setString("narration", ab.getNarration())
	.setString("cslNo", ab.getCSLno());
	int inserted = insertQuery.executeUpdate();
	
	}
	public void validate()
	{
	
	}
private boolean alreadyUploaded(String dateStr) {
		List list = HibernateUtil.getCurrentSession().createSQLQuery("select id from egf_brs_bankstatements where accountid="+accountId
				+" and txdate=to_date('"+dateStr+"','"+dateInDotFormat+"')").list();
		if(list.size()>=1)
			return true;
		else
		return false;
	}
public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
public String getFailureMessage() {
		return failureMessage;
	}
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
private String getStrValue(HSSFCell cell) {
	if(cell==null)
		return null;  
	double numericCellValue=0d;
	String strValue="";
	switch (cell.getCellType())
	{
	case HSSFCell.CELL_TYPE_NUMERIC:
		numericCellValue = cell.getNumericCellValue();
		  DecimalFormat decimalFormat=new DecimalFormat("#");
		  strValue=decimalFormat.format(numericCellValue);
		break;
	case HSSFCell.CELL_TYPE_STRING:
		strValue=cell.getStringCellValue();
		break;
	}
	return strValue;
		
	}

private BigDecimal getNumericValue(HSSFCell cell) {
	if(cell==null)
		return null;
	double numericCellValue=0d;
	BigDecimal bigDecimalValue=BigDecimal.ZERO;
	String strValue="";
	
	switch (cell.getCellType())
	{
	case HSSFCell.CELL_TYPE_NUMERIC:
		numericCellValue = cell.getNumericCellValue();
		bigDecimalValue = BigDecimal.valueOf(numericCellValue);
		break;
	case HSSFCell.CELL_TYPE_STRING:
		strValue=cell.getStringCellValue();
		strValue=strValue.replaceAll("[^\\p{L}\\p{Nd}]", "");
		if(strValue!=null && strValue.contains("E+"))
		{
			String[] split = strValue.split("E+");
			String mantissa=split[0].replaceAll(".", "");
			int exp=Integer.parseInt(split[1]);
			while (mantissa.length()<=exp+1)
			{
				mantissa+="0";
			}
			numericCellValue = Double.parseDouble(mantissa);
			bigDecimalValue=BigDecimal.valueOf(numericCellValue);
		}else if(strValue!=null && strValue.contains(","))
		{
			strValue=strValue.replaceAll(",", "");
		}
		//Ignore the error and continue Since in numric field we find empty or non numeric value  
		try{
		numericCellValue = Double.parseDouble(strValue);
		bigDecimalValue=BigDecimal.valueOf(numericCellValue);
		}catch(Exception e)
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Found : Non numeric value in Numeric Field :"+strValue+":");
		}
		break;
	}
	return bigDecimalValue;
		
	}
/**
 * Step1: mark which are all we are going to process
 * step2 :find duplicate and mark to be processed manually
 * step3: process non duplicates
 * @return
 */
	/**
	 * @return
	 */
	public String schedule()
	{
		//Step1: mark which are all we are going to process
		count=0;
		HibernateUtil.getCurrentSession().getTransaction().setTimeout(900);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Started at "+new Date());
		markForProcessing(BRS_TRANSACTION_TYPE_CHEQUE);
		
		//step2 :find duplicate and mark to be processed manually
		findandUpdateDuplicates();
		
		List<AutoReconcileBean> detailList	=getStatmentsForProcessing(BRS_TRANSACTION_TYPE_CHEQUE);

		String statusQury = "select id from EgwStatus where upper(moduletype)=upper('instrument') and  upper(description)=upper('"
			+ FinancialConstants.INSTRUMENT_RECONCILED_STATUS + "')";
		statusId = (Integer) persistenceService.find(statusQury);
		Long instrumentTypeId = getInstrumentType(FinancialConstants.INSTRUMENT_TYPE_CHEQUE);
		Long instrumentTypeDDId = getInstrumentType(FinancialConstants.INSTRUMENT_TYPE_DD);
		//where instrumentheaderid= (select id.....) is used to fetch only one record may be double submit or two instrument entries
		//let the user decide
		
		String recociliationQuery="update EGF_InstrumentHeader set id_status=:statusId,  lastmodifiedby=:userId,lastmodifieddate=sysdate" +
		  " where id= (select id from egf_instrumentheader where instrumentNumber=:instrumentNo and " +
			" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in ("+instrumentTypeId+","+instrumentTypeDDId+")" +
			" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=upper(:instrumentStatus)))";
		  
		String	recociliationAmountQuery="update egf_instrumentOtherdetails set reconciledamount=:amount,instrumentstatusdate=:txDate " +
		" ,lastmodifiedby=:userId,lastmodifieddate=sysdate,reconciledOn=:reconciliationDate "+
	     " where instrumentheaderid= (select id from egf_instrumentheader where instrumentNumber=:instrumentNo and " +
		" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in ("+instrumentTypeId+","+instrumentTypeDDId+")" +
		" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=upper(:instrumentStatus)))";
		
		SQLQuery updateQuery = HibernateUtil.getCurrentSession().createSQLQuery(recociliationQuery);
		SQLQuery updateQuery2 = HibernateUtil.getCurrentSession().createSQLQuery(recociliationAmountQuery);

		String backUpdateBankStmtquery="update "+TABLENAME+" set action='"+BRS_ACTION_PROCESSED+"' ,reconciliationDate=:reconciliationDate where id=:id";
		
		String backUpdateFailureBRSquery="update "+TABLENAME+" set action='"+BRS_ACTION_TO_BE_PROCESSED_MANUALLY+"',errormessage=:e where id=:id";
		SQLQuery backupdateQuery = HibernateUtil.getCurrentSession().createSQLQuery(backUpdateBankStmtquery);
		SQLQuery backupdateFailureQuery = HibernateUtil.getCurrentSession().createSQLQuery(backUpdateFailureBRSquery);
		rowCount = 0;
		for(AutoReconcileBean bean:detailList)
		{
			int updated=-1;
			try {
				updateQuery.setLong("statusId", statusId);
				updateQuery.setLong("accountId", accountId);
				
				updateQuery.setString("instrumentNo", bean.getInstrumentNo());
				updateQuery.setInteger("userId", Integer.valueOf(EGOVThreadLocals.getUserId().trim()));
				
				updateQuery2.setDate("txDate", bean.getTxDate());
				updateQuery2.setDate("reconciliationDate", reconciliationDate);
				updateQuery2.setLong("accountId", accountId);
				
				updateQuery2.setString("instrumentNo", bean.getInstrumentNo());
				updateQuery2.setInteger("userId", Integer.valueOf(EGOVThreadLocals.getUserId().trim()));
				if(bean.getDebit()!=null && bean.getDebit().compareTo(BigDecimal.ZERO)!=0)
				{
					updateQuery.setBigDecimal("amount",bean.getDebit());
					updateQuery.setInteger("ispaycheque",1 );
					updateQuery.setString("instrumentStatus", FinancialConstants.INSTRUMENT_CREATED_STATUS);
					updated = updateQuery.executeUpdate();
				if(updated!=0)
				{
					updateQuery2.setBigDecimal("amount",bean.getDebit());
					updateQuery2.setInteger("ispaycheque",1 );
					updateQuery2.setString("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
					updated = updateQuery2.executeUpdate();
				}
					
				}else
				{
					updateQuery.setBigDecimal("amount",bean.getCredit());
					updateQuery.setInteger("ispaycheque",0 );
					updateQuery.setString("instrumentStatus", FinancialConstants.INSTRUMENT_DEPOSITED_STATUS);
					updated = updateQuery.executeUpdate();
					if(updated!=0)
					{
					updateQuery2.setBigDecimal("amount",bean.getCredit());
					updateQuery2.setInteger("ispaycheque",0 );
					updateQuery2.setString("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
					updated = updateQuery2.executeUpdate();
					}
				}
				//if updated is 0 means nothing got updated means could not find matching row in instrumentheader
				if(updated==0)
				{
					backupdateFailureQuery.setLong("id", bean.getId());
					backupdateFailureQuery.setString("e", DID_NOT_FIND_MATCH_IN_BANKBOOK);
					backupdateFailureQuery.executeUpdate();
					
					
				}else
				{
					backupdateQuery.setLong("id", bean.getId());
					backupdateQuery.setDate("reconciliationDate", reconciliationDate);
					backupdateQuery.executeUpdate();
					count++;
					//if(LOGGER.isDebugEnabled())     LOGGER.debug(count);
				}
				rowCount++;
				if(LOGGER.isDebugEnabled())     LOGGER.debug("out of "+rowCount+"==>succesfull "+count);
				
				if(rowCount%20==0)
				{
					HibernateUtil.getCurrentSession().flush();
				}
				
				//These exception might be because the other entires in instrument which is not in egf_brs_bankstatements
				//so any issues leave it for manual update
			} catch (HibernateException e) {
				if(e.getCause().getMessage().contains("single-row subquery returns more"))
				{
					backupdateFailureQuery.setString("e", BRS_MESSAGE_MORE_THAN_ONE_MATCH);
				}else
				{
					backupdateFailureQuery.setString("e", e.getMessage());	
				}
				backupdateFailureQuery.setLong("id", bean.getId());
				backupdateFailureQuery.executeUpdate();
			
			} catch (Exception e) {
				backupdateFailureQuery.setLong("id", bean.getId());
				backupdateFailureQuery.setString("e", e.getMessage());
				backupdateFailureQuery.executeUpdate();
			}
			

		}
		processCSL();
		return "result";
	}
	private Long getInstrumentType(String typeName) {
		
		return(Long) persistenceService.find("select id from InstrumentType where upper(type)=upper(?)",typeName);
	}
private void markForProcessing(String type) {
	
	StringBuffer sql=new StringBuffer(256);
	sql.append("update ").append(TABLENAME).append(" set action='").append(BRS_ACTION_TO_BE_PROCESSED).append("' where type='")
	.append(type).append("' and accountid=:accountId and txdate>=:fromDate and txDate<=:toDate and  (action is null or action!='processed')");
	if(BRS_TRANSACTION_TYPE_BANK.equalsIgnoreCase(type))
	{
		sql.append(" and CSLno is not null ");
	}
	SQLQuery markQuery = HibernateUtil.getCurrentSession().createSQLQuery(sql.toString());
	markQuery.setDate("fromDate", fromDate);
	markQuery.setDate("toDate", toDate);  
	markQuery.setLong("accountId", accountId);
	int result = markQuery.executeUpdate();
}
	private void processCSL() { 
		markForProcessing(BRS_TRANSACTION_TYPE_BANK);
		List<AutoReconcileBean> CSLList = getStatmentsForProcessing(BRS_TRANSACTION_TYPE_BANK);
		Long instrumentTypeId = getInstrumentType(FinancialConstants.INSTRUMENT_TYPE_BANK_TO_BANK);	
		String recociliationQuery="update EGF_InstrumentHeader set id_status=:statusId,  lastmodifiedby=:userId,lastmodifieddate=sysdate" +
		  " where id = (select ih.id from egf_instrumentheader ih,egf_instrumentvoucher iv,voucherheader vh where  " +
			" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in ("+instrumentTypeId+")" +
			" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=" +
			" upper(:instrumentStatus)) and iv.instrumentheaderid=ih.id and iv.voucherheaderid=ih.id and vh.vouchernumber=:cslNo )  ";
		  
		String	recociliationAmountQuery="update egf_instrumentOtherdetails set reconciledamount=:amount,instrumentstatusdate=:txDate " +
		" ,lastmodifiedby=:userId,lastmodifieddate=sysdate,reconciledOn=:reconciliationDate "+
	     " where instrumentheaderid =  (select ih.id from egf_instrumentheader ih,egf_instrumentvoucher iv,voucherheader vh where  " +
			" instrumentAmount=:amount and bankaccountid=:accountId and ispaycheque=:ispaycheque and instrumentType in ("+instrumentTypeId+")" +
			" and id_status=(select id from Egw_Status where upper(moduletype)=upper('instrument') and  upper(description)=" +
			" upper(:instrumentStatus)) and iv.instrumentheaderid=ih.id and iv.voucherheaderid=ih.id and vh.vouchernumber=:cslNo ) ";
		
		SQLQuery updateQuery = HibernateUtil.getCurrentSession().createSQLQuery(recociliationQuery);
		SQLQuery updateQuery2 = HibernateUtil.getCurrentSession().createSQLQuery(recociliationAmountQuery);

		String backUpdateBankStmtquery="update "+TABLENAME+" set action='"+BRS_ACTION_PROCESSED+"' ,reconciliationDate=:reconciliationDate where id=:id";
		
		String backUpdateFailureBRSquery="update "+TABLENAME+" set action='"+BRS_ACTION_TO_BE_PROCESSED_MANUALLY+"',errormessage=:e where id=:id";
		SQLQuery backupdateQuery = HibernateUtil.getCurrentSession().createSQLQuery(backUpdateBankStmtquery);
		SQLQuery backupdateFailureQuery = HibernateUtil.getCurrentSession().createSQLQuery(backUpdateFailureBRSquery);
		for(AutoReconcileBean bean:CSLList)
		{
			int updated=-1;
			try {
				updateQuery.setLong("statusId", statusId);
				updateQuery.setLong("accountId", accountId);
				
				updateQuery.setString("cslNo", bean.getCSLno());
				updateQuery.setInteger("userId", Integer.valueOf(EGOVThreadLocals.getUserId().trim()));
				
				updateQuery2.setDate("txDate", bean.getTxDate());
				updateQuery2.setDate("reconciliationDate", reconciliationDate);
				updateQuery2.setLong("accountId", accountId);
				
				updateQuery2.setString("cslNo", bean.getCSLno());
				updateQuery2.setInteger("userId", Integer.valueOf(EGOVThreadLocals.getUserId().trim()));
				if(bean.getDebit()!=null && bean.getDebit().compareTo(BigDecimal.ZERO)!=0)
				{
					updateQuery.setBigDecimal("amount",bean.getDebit());
					updateQuery.setInteger("ispaycheque",1 );
					updateQuery.setString("instrumentStatus", FinancialConstants.INSTRUMENT_CREATED_STATUS);
					updated = updateQuery.executeUpdate();
				if(updated!=0)
				{
					updateQuery2.setBigDecimal("amount",bean.getDebit());
					updateQuery2.setInteger("ispaycheque",1 );
					updateQuery2.setString("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
					updated = updateQuery2.executeUpdate();
				}
					
				}
			
				
				else
				{
					updateQuery.setBigDecimal("amount",bean.getCredit());
					updateQuery.setInteger("ispaycheque",1 );
					updateQuery.setString("instrumentStatus", FinancialConstants.INSTRUMENT_CREATED_STATUS);
					updated = updateQuery.executeUpdate();
					if(updated!=0)
					{
					updateQuery2.setBigDecimal("amount",bean.getCredit());
					updateQuery2.setInteger("ispaycheque",1 );
					updateQuery2.setString("instrumentStatus", FinancialConstants.INSTRUMENT_RECONCILED_STATUS);
					updated = updateQuery2.executeUpdate();
					}
					if(updated==0)
					{
						
					}
				}
				//if updated is 0 means nothing got updated means could not find matching row in instrumentheader
				
				
				if(updated==0)
				{
					backupdateFailureQuery.setLong("id", bean.getId());
					backupdateFailureQuery.setString("e", DID_NOT_FIND_MATCH_IN_BANKBOOK);
					backupdateFailureQuery.executeUpdate();
					
					
				}
				else if(updated==-1)
				{
					backupdateFailureQuery.setLong("id", bean.getId());
					backupdateFailureQuery.setString("e", DID_NOT_FIND_MATCH_IN_BANKBOOK);
					backupdateFailureQuery.executeUpdate();
					//if(LOGGER.isDebugEnabled())     LOGGER.debug(count);
				}
				else 
				{
					backupdateQuery.setLong("id", bean.getId());
					backupdateQuery.setDate("reconciliationDate", reconciliationDate);
					backupdateQuery.executeUpdate();
					count++;
					//if(LOGGER.isDebugEnabled())     LOGGER.debug(count);
				}
				rowCount++;
				if(LOGGER.isDebugEnabled())     LOGGER.debug("out of "+rowCount+"==>succesfull "+count);
				
				if(rowCount%20==0)
				{
					HibernateUtil.getCurrentSession().flush();
				}
				
				//These exception might be because the other entires in instrument which is not in egf_brs_bankstatements
				//so any issues leave it for manual update
			} catch (HibernateException e) {
				if(e.getCause().getMessage().contains("single-row subquery returns more"))
				{
					backupdateFailureQuery.setString("e", BRS_MESSAGE_MORE_THAN_ONE_MATCH);
				}else
				{
					backupdateFailureQuery.setString("e", e.getMessage());	
				}
				backupdateFailureQuery.setLong("id", bean.getId());
				backupdateFailureQuery.executeUpdate();
			
			} catch (Exception e) {
				backupdateFailureQuery.setLong("id", bean.getId());
				backupdateFailureQuery.setString("e", e.getMessage());
				backupdateFailureQuery.executeUpdate();
			}
			

		}
		
	
}
	private List<AutoReconcileBean> getStatmentsForProcessing(String type) {
		SQLQuery detailQuery = HibernateUtil.getCurrentSession().createSQLQuery("select id,txDate,instrumentNo,debit,credit,CSLno  from "+TABLENAME+
				" where accountId=:accountId  and type='"+type+"' and action='"+BRS_ACTION_TO_BE_PROCESSED+"'");
		detailQuery.setLong("accountId", accountId);
		detailQuery.addScalar("id",LongType.INSTANCE).addScalar("txDate").addScalar("instrumentNo").addScalar("debit").addScalar("credit").addScalar("CSLno")
		.setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));
		List<AutoReconcileBean> detailList = (List<AutoReconcileBean>)detailQuery.list();
		return detailList;
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public String  generateReport() {
        //bankStatments not in BankBook
		EGovernCommon cm = new EGovernCommon();
		
		try {
			bankBookBalance = cm.getAccountBalance(dateFormatter.format(toDate),null,accountId.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (HibernateException e) {
			throw new EGOVRuntimeException(e.getMessage());
		} catch (TaskFailedException e) {
			throw new EGOVRuntimeException(e.getMessage());
		}
		bankAccount= (Bankaccount)persistenceService.find("from Bankaccount ba where id=?",accountId);
		String statmentsNotInBankBookStr="select id,txDate,instrumentNo,debit,credit,narration,type,action as \"errorCode\",errorMessage from "+TABLENAME+" where accountId=:accountId and txdate>=:fromDate " +
				" and txdate<=:toDate and reconciliationdate is null and (errorMesSage is null or errorMessage !=:multipleEntryErrorMessage)"
				+" order by  txDate ";
		Query statmentsNotInBankBookQry = HibernateUtil.getCurrentSession().createSQLQuery(statmentsNotInBankBookStr)
		.addScalar("instrumentNo")
		.addScalar("credit")
		.addScalar("debit") 
		.addScalar("txDate")
		.addScalar("id",LongType.INSTANCE)
		.addScalar("narration")
		.addScalar("type")
		.addScalar("errorCode")
		.addScalar("errorMessage")
		.setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));
		
		statmentsNotInBankBookQry.setDate("fromDate", fromDate)
								 .setDate("toDate",toDate)
								 .setString("multipleEntryErrorMessage",BRS_MESSAGE_MORE_THAN_ONE_MATCH)
								 .setLong("accountId", accountId);
		statementsNotInBankBookList = statmentsNotInBankBookQry.list();
		notInBooktotalDebit = BigDecimal.ZERO;
		notInBooktotalCredit = BigDecimal.ZERO;
		notInBookNet = BigDecimal.ZERO;
		
		for(AutoReconcileBean ab:statementsNotInBankBookList)
		{
			notInBooktotalDebit = notInBooktotalDebit.add(ab.getDebit()==null?BigDecimal.ZERO:ab.getDebit());
			notInBooktotalCredit = notInBooktotalCredit.add(ab.getCredit()==null?BigDecimal.ZERO:ab.getCredit());
		}
		notInBookNet = notInBooktotalCredit.subtract(notInBooktotalDebit);
		if(notInBookNet.compareTo(BigDecimal.ZERO) == -1){
			notInBookNetBal = notInBookNet+"(Dr)";
		}else{
			notInBookNetBal = notInBookNet+"(Cr)";
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("notInBookNet"+notInBookNet);
	
		CFinancialYear finYearByDate = financialYearDAO.getFinYearByDate(toDate);
		finYearStartDate = finYearByDate.getStartingDate();
		
		String entriesNotInBankStamentStr="select  instrumentnumber as \"instrumentNo\"," +
				" instrumentdate as \"txDate\", instrumentamount as \"credit\",null as \"debit\", payto as \"narration\"  from egf_instrumentheader  where bankaccountid=:accountId and instrumentdate BETWEEN" +
				" :fromDate and :toDate and ispaycheque=0 and id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited')"+
				" and instrumentnumber is not null and instrumentamount is not null and instrumentnumber||'-'||instrumentamount not in (select  instrumentno||'-'|| credit from egf_brs_bankstatements " +
				" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage  and instrumentno is not null and  credit is not null and credit>0) " +
				" union "+  
				" select   instrumentnumber as \"instrumentNo\"," +
				" instrumentdate as \"txDate\", instrumentamount \"debit\",null as \"credit\", payto as \"narration\" "+ 
				" from egf_instrumentheader where bankaccountid=:accountId and instrumentdate BETWEEN :fromDate and :toDate "+
				" and ispaycheque=1 and id_status=(select id from egw_status where moduletype='Instrument'  and description='New')"+ 
				" and  instrumentnumber is not null   and instrumentamount is not null and instrumentnumber||'-'||instrumentamount not in  (select  instrumentno||'-'|| debit from egf_brs_bankstatements" +
				" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage  and instrumentno is not null and debit is not null and debit>0) order by \"txDate\"";
		Query entriesNotInBankStamentQry = HibernateUtil.getCurrentSession().createSQLQuery(entriesNotInBankStamentStr)  
				.addScalar("instrumentNo")
				.addScalar("credit")
				.addScalar("debit")    
				.addScalar("txDate")  
				.addScalar("narration")
				.setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));

		entriesNotInBankStamentQry.setDate("fromDate", finYearStartDate)
						 .setDate("toDate",toDate)
						 .setString("action",BRS_ACTION_TO_BE_PROCESSED_MANUALLY)
						 .setLong("accountId", accountId)
						 .setString("multipleEntryErrorMessage",BRS_MESSAGE_MORE_THAN_ONE_MATCH);
		entriesNotInBankStament = entriesNotInBankStamentQry.list(); 
		
		/**
		 *--------------------------------------- 
		 */
		
		notInStatementTotalDebit = BigDecimal.ZERO;
		notInStatementTotalCredit = BigDecimal.ZERO;  
		notInStatementNet = BigDecimal.ZERO;
		int i=0;  
		for(AutoReconcileBean ab:entriesNotInBankStament)
		{ i++;
			//LOGGER.error("notInStatementTotalDebit=="+notInStatementTotalDebit+"           "+ab.getDebit());
			notInStatementTotalDebit = notInStatementTotalDebit.add(ab.getDebit()==null?BigDecimal.ZERO:ab.getDebit());
			LOGGER.error("no="+ab.getInstrumentNo()+" t ="+notInStatementTotalCredit+" a="+ab.getCredit());
			notInStatementTotalCredit = notInStatementTotalCredit.add(ab.getCredit()==null?BigDecimal.ZERO:ab.getCredit());
			//LOGGER.error("notInStatementTotalCredit=="+notInStatementTotalCredit+"           "+"notInStatementTotalDebit=="+notInStatementTotalDebit+"           count"+i);
		}
		LOGGER.error("notInStatementTotalCredit=="+notInStatementTotalCredit+"           "+"notInStatementTotalDebit=="+notInStatementTotalDebit);
		notInStatementNet=notInStatementTotalCredit.subtract(notInStatementTotalDebit);//this one will be reverse
		//LOGGER.error("notInStatementTotalCredit=="+notInStatementTotalCredit+"           "+"notInStatementTotalDebit=="+notInStatementTotalDebit +"notInStatementNet                       "+notInStatementNet);
	//for match 
		
		 entriesNotInBankStamentStr="select  sum(instrumentamount) as \"credit\"  from egf_instrumentheader  where bankaccountid=:accountId and instrumentdate BETWEEN" +
		" :fromDate and :toDate and ispaycheque=0 and id_status=(select id from egw_status where moduletype='Instrument'  and description='Deposited')"+
		" and instrumentnumber is not null and instrumentamount is not null and instrumentnumber||'-'||instrumentamount not in (select  instrumentno||'-'|| credit from egf_brs_bankstatements " +
		" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage  and instrumentno is not null and  credit is not null and credit>0) " +
		" union "+  
		" select   sum(instrumentamount) as \"credit\" "+ 
		" from egf_instrumentheader where bankaccountid=:accountId and instrumentdate BETWEEN :fromDate and :toDate "+
		" and ispaycheque=1 and id_status=(select id from egw_status where moduletype='Instrument'  and description='New')"+ 
		" and  instrumentnumber is not null   and instrumentamount is not null and instrumentnumber||'-'||instrumentamount not in  (select  instrumentno||'-'|| debit from egf_brs_bankstatements" +
		" where accountid=:accountId and txdate between :fromDate and :toDate and action=:action and errorMessage =:multipleEntryErrorMessage  and instrumentno is not null and debit is not null and debit>0) ";
      entriesNotInBankStamentQry = HibernateUtil.getCurrentSession().createSQLQuery(entriesNotInBankStamentStr)
		//.addScalar("instrumentNo")
		.addScalar("credit")
		//.addScalar("debit")    
		//.addScalar("txDate")  
		//.addScalar("narration")
		.setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));

entriesNotInBankStamentQry.setDate("fromDate", finYearStartDate)
				 .setDate("toDate",toDate)
				 .setString("action",BRS_ACTION_TO_BE_PROCESSED_MANUALLY)
				 .setLong("accountId", accountId)
				 .setString("multipleEntryErrorMessage",BRS_MESSAGE_MORE_THAN_ONE_MATCH);
List<AutoReconcileBean> entriesNotInBankStament1 = entriesNotInBankStamentQry.list(); 
if(entriesNotInBankStament1.size()>0)
{
	notInStatementTotalCredit=	entriesNotInBankStament1.get(0).getCredit();
	if(notInStatementTotalCredit==null)
		notInStatementTotalCredit=BigDecimal.ZERO;
}
if(entriesNotInBankStament1.size()>1)
{
	notInStatementTotalDebit=entriesNotInBankStament1.get(1).getCredit();
	if(notInStatementTotalDebit==null)
		notInStatementTotalDebit=BigDecimal.ZERO;
}

notInStatementNet=notInStatementTotalCredit.subtract(notInStatementTotalDebit);
		
		String statmentsfoundButNotProcessed="select id,txDate,instrumentNo,debit,credit,narration,type,action as \"errorCode\",errorMessage " +
				"from "+TABLENAME+" where accountId=:accountId and txdate>=:fromDate  and txdate<=:toDate and reconciliationdate is null "
				+" and  errorMessage =:multipleEntryErrorMessage order by  txDate ";
		Query statmentsfoundButNotProcessedQry = HibernateUtil.getCurrentSession().createSQLQuery(statmentsfoundButNotProcessed)
		.addScalar("instrumentNo")
		.addScalar("credit")
		.addScalar("debit") 
		.addScalar("txDate")
		.addScalar("id",LongType.INSTANCE)
		.addScalar("narration")
		.addScalar("type")
		.addScalar("errorCode")
		.addScalar("errorMessage")
		.setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));
		
		statmentsfoundButNotProcessedQry.setDate("fromDate", fromDate)
								 .setDate("toDate",toDate)
								.setString("multipleEntryErrorMessage",BRS_MESSAGE_MORE_THAN_ONE_MATCH) 
								 .setLong("accountId", accountId);
		statementsFoundButNotProcessed = statmentsfoundButNotProcessedQry.list();
		notprocessedDebit = BigDecimal.ZERO;
		notprocessedCredit = BigDecimal.ZERO;
		notprocessedNet = BigDecimal.ZERO;
		
		for(AutoReconcileBean ab:statementsFoundButNotProcessed)
		{
			LOGGER.error("notprocessedDebit=="+notprocessedDebit+"           "+ab.getDebit());
			notprocessedDebit = notprocessedDebit.add(ab.getDebit()==null?BigDecimal.ZERO:ab.getDebit());
			LOGGER.error("notprocessedCredit=="+notprocessedCredit+"           "+ab.getCredit());
			notprocessedCredit = notprocessedCredit.add(ab.getCredit()==null?BigDecimal.ZERO:ab.getCredit());
			LOGGER.error("notprocessedDebit=="+notprocessedDebit+"           "+"notprocessedCredit=="+notprocessedCredit);
		}
		LOGGER.error("notprocessedDebit=="+notprocessedDebit+"           "+"notprocessedCredit=="+notprocessedCredit);
		notprocessedNet=notprocessedCredit.subtract(notprocessedDebit);//this one will be reverse
		LOGGER.error("notprocessedDebit=="+notprocessedDebit+"           "+"notprocessedCredit=="+notprocessedCredit);
		totalNotReconciledAmount=notInStatementNet.add(notprocessedNet.negate());
		brsBalance=bankBookBalance.add(notInStatementNet).add(notInBookNet).add(notprocessedNet);   
	return "report";	
	
}
	public BigDecimal getTotalNotReconciledAmount() {
		return totalNotReconciledAmount;
	}
	public void setTotalNotReconciledAmount(BigDecimal totalNotReconciledAmount) {
		this.totalNotReconciledAmount = totalNotReconciledAmount;
	}
	public BigDecimal getNotInBooktotalDebit() {
		return notInBooktotalDebit;
	}
	public BigDecimal getNotInBooktotalCredit() {
		return notInBooktotalCredit;
	}
	public BigDecimal getNotInBookNet() {
		return notInBookNet;
	}
	public BigDecimal getNotInStatementTotalDebit() {
		return notInStatementTotalDebit;
	}
	public BigDecimal getNotInStatementTotalCredit() {  
		return notInStatementTotalCredit;
	}
	public BigDecimal getNotInStatementNet() {
		return notInStatementNet;
	}
	public void setNotInBooktotalDebit(BigDecimal notInBooktotalDebit) {
		this.notInBooktotalDebit = notInBooktotalDebit;
	}
	public void setNotInBooktotalCredit(BigDecimal notInBooktotalCredit) {
		this.notInBooktotalCredit = notInBooktotalCredit;
	}
	public void setNotInBookNet(BigDecimal notInBookNet) {
		this.notInBookNet = notInBookNet;
	}
	public void setNotInStatementTotalDebit(BigDecimal notInStatementTotalDebit) {
		this.notInStatementTotalDebit = notInStatementTotalDebit;
	}
	public void setNotInStatementTotalCredit(BigDecimal notInStatementTotalCredit) {
		this.notInStatementTotalCredit = notInStatementTotalCredit;
	}
	public void setNotInStatementNet(BigDecimal notInStatementNet) {
		this.notInStatementNet = notInStatementNet;
	}
	public int getRowIndex() {
	return rowIndex;
}
public int getRowCount() {
	return rowCount;
}
public void setRowIndex(int rowIndex) {
	this.rowIndex = rowIndex;
}
public void setRowCount(int rowCount) {
	this.rowCount = rowCount;
}
	private void findandUpdateDuplicates() {
		//for payment cheques  instrumentNo,debit,accountId combination should be unique else mark it duplicate
		try {
			String duplicates="select instrumentNo,debit,accountId from "+TABLENAME+" where accountId=:accountId" +
			" and debit>0 and action='"+BRS_ACTION_TO_BE_PROCESSED+"'  group by  instrumentNo,debit,accountId having count(*)>1";
			SQLQuery paymentDuplicateChequesQuery = HibernateUtil.getCurrentSession().createSQLQuery(duplicates);
			paymentDuplicateChequesQuery.addScalar("instrumentNo")
			.addScalar("debit")
			.addScalar("accountId",LongType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));
			//paymentDuplicateChequesQuery.setParameter("accountId", Long.class);
			paymentDuplicateChequesQuery.setLong("accountId",accountId );
			List<AutoReconcileBean> duplicatePaymentCheques =(List<AutoReconcileBean>) paymentDuplicateChequesQuery.list();

			String backUpdateDuplicatePaymentquery="update "+TABLENAME+" set action='"+BRS_ACTION_TO_BE_PROCESSED_MANUALLY+"'," +
			" errorMessage='"+BRS_MESSAGE_DUPPLICATE_IN_BANKSTATEMENT+"' where debit=:debit and accountid=:accountId and instrumentNo=:instrumentNo " +
			" and action='"+BRS_ACTION_TO_BE_PROCESSED+"'";
			
			SQLQuery paymentDuplicateUpdate = HibernateUtil.getCurrentSession().createSQLQuery(backUpdateDuplicatePaymentquery);
			for(AutoReconcileBean bean:duplicatePaymentCheques)
			{

				paymentDuplicateUpdate.setLong("accountId", bean.getAccountId());
				paymentDuplicateUpdate.setBigDecimal("debit", bean.getDebit());
				paymentDuplicateUpdate.setString("instrumentNo", bean.getInstrumentNo());
				int noOfRowsUpdated = paymentDuplicateUpdate.executeUpdate();

			}
			//this portion is for receipts 	 instrumentNo,credit,accountId combination should be unique else mark it duplicate
			duplicates="select instrumentNo,credit,accountId from "+TABLENAME+" where accountid=:accountId" +
			" and  credit>0 and action='"+BRS_ACTION_TO_BE_PROCESSED+"' group by  instrumentNo,credit,accountId having count(*)>1";
			SQLQuery receiptsDuplicateChequesQuery = HibernateUtil.getCurrentSession().createSQLQuery(duplicates);
			receiptsDuplicateChequesQuery.addScalar("instrumentNo")
			.addScalar("credit")
			.addScalar("accountId",LongType.INSTANCE)  
			.setResultTransformer(Transformers.aliasToBean(AutoReconcileBean.class));
			receiptsDuplicateChequesQuery.setLong("accountId",accountId );
			List<AutoReconcileBean> duplicateReceiptsCheques =(List<AutoReconcileBean>) receiptsDuplicateChequesQuery.list();

			String backUpdateDuplicateReceiptsQuery="update "+TABLENAME+" set action='"+BRS_ACTION_TO_BE_PROCESSED_MANUALLY+"'" +
			" ,errorMessage='"+BRS_MESSAGE_DUPPLICATE_IN_BANKSTATEMENT+"' where credit=:credit and accountid=:accountId and instrumentNo=:instrumentNo " +
			" and action='"+BRS_ACTION_TO_BE_PROCESSED+"'";
			SQLQuery receiptDuplicateUpdate = HibernateUtil.getCurrentSession().createSQLQuery(backUpdateDuplicateReceiptsQuery);

			for(AutoReconcileBean bean:duplicateReceiptsCheques)
			{
				receiptDuplicateUpdate.setLong("accountId", bean.getAccountId());
				receiptDuplicateUpdate.setBigDecimal("credit", bean.getCredit());
				receiptDuplicateUpdate.setString("instrumentNo", bean.getInstrumentNo());
				int noOfRowsUpdated = receiptDuplicateUpdate.executeUpdate();
			}
		} catch (HibernateException e) {
		   throw new EGOVRuntimeException("Failed while processing autoreconciliation ");  
		}    

	}
	public Date getReconciliationDate() {
		return reconciliationDate;
	}
	public void setReconciliationDate(Date reconciliationDate) {
		this.reconciliationDate = reconciliationDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;  
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public File getBankStatmentInXls() {
		return bankStatmentInXls;
	}
	public void setBankStatmentInXls(File bankStatmentInXls) {
		this.bankStatmentInXls = bankStatmentInXls;
	}
	public void setBankStatmentInXlsContentType(String bankStatmentInXlsContentType) {
		this.bankStatmentInXlsContentType = bankStatmentInXlsContentType;
	}
	public void setBankStatmentInXlsFileName(String bankStatmentInXlsFileName) {
		this.bankStatmentInXlsFileName = bankStatmentInXlsFileName;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public int getBankId() {
		return bankId;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<AutoReconcileBean> getStatementsNotInBankBookList() {
		return statementsNotInBankBookList;
	}
	public void setStatementsNotInBankBookList(List<AutoReconcileBean> statementsNotInBankBookList) {
		this.statementsNotInBankBookList = statementsNotInBankBookList;
	}
	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}
	public List<AutoReconcileBean> getEntriesNotInBankStament() {
		return entriesNotInBankStament;
	}
	public void setEntriesNotInBankStament(List<AutoReconcileBean> entriesNotInBankStament) {
		this.entriesNotInBankStament = entriesNotInBankStament;
	}
	public List<AutoReconcileBean> getStatementsFoundButNotProcessed() {
		return statementsFoundButNotProcessed;
	}
	public BigDecimal getNotprocessedNet() {
		return notprocessedNet;
	}
	public void setStatementsFoundButNotProcessed(List<AutoReconcileBean> statementsFoundButNotProcessed) {
		this.statementsFoundButNotProcessed = statementsFoundButNotProcessed;
	}
	public void setNotprocessedNet(BigDecimal notprocessedNet) {
		this.notprocessedNet = notprocessedNet;
	}
	
	public String generatePDF() throws Exception{
		List<Object> dataSource = new ArrayList<Object>();
		AutoReconcileBean AutoReconcileObj=new AutoReconcileBean();
		
		generateReport();
		if(statementsNotInBankBookList.size()==0){
			AutoReconcileObj.setNoDetailsFound("No Dteails Found");
			statementsNotInBankBookList.add(AutoReconcileObj);
		}
		for (AutoReconcileBean row : statementsNotInBankBookList) {
			dataSource.add(row);
		}
		inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), dataSource);
		return "PDF";
	} 




public String generateXLS() throws JRException, IOException{
		List<Object> dataSource = new ArrayList<Object>();
		AutoReconcileBean AutoReconcileObj=new AutoReconcileBean();
		generateReport();
		
		if(statementsNotInBankBookList.size()==0){
			AutoReconcileObj.setNoDetailsFound("No Details Found");
			statementsNotInBankBookList.add(AutoReconcileObj);
		}
		for (AutoReconcileBean row : statementsNotInBankBookList) {
			dataSource.add(row);
		}
		inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), dataSource);
	    return "XLS";
	}





protected Map<String, Object> getParamMap() {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		AutoReconcileBean AutoReconcileObj=new AutoReconcileBean();
		paramMap.put("heading","Bank reconcilation statement from "+Constants.DDMMYYYYFORMAT2.format(fromDate)+" to "+Constants.DDMMYYYYFORMAT2.format(toDate));
		paramMap.put("bankName", bankAccount.getBankbranch().getBank().getName());
		paramMap.put("accountNumber", bankAccount.getAccountnumber());
		paramMap.put("accountCode", bankAccount.getChartofaccounts().getGlcode());
		paramMap.put("accountDescription", bankAccount.getChartofaccounts().getName());
		paramMap.put("bankBookBalance", bankBookBalance);
		paramMap.put("notInBookNet", notInBookNetBal);
		paramMap.put("notprocessedNet", notprocessedNet);
		paramMap.put("notInStatementNet", notInStatementNet);
		paramMap.put("totalNotReconciledAmount",totalNotReconciledAmount);
		paramMap.put("brsBalance",brsBalance);
		
		List<Object> statementDataSource = new ArrayList<Object>();
		List<Object> entriesNotInBankStamentDataSource = new ArrayList<Object>();
		paramMap.put("BankStatement", reportHelper.getClass().getResourceAsStream("/reports/templates/BankStatement.jasper"));
		if(statementsFoundButNotProcessed.size()==0){
			AutoReconcileObj.setNoDetailsFound("No Details Found");
			statementsFoundButNotProcessed.add(AutoReconcileObj);
		}
		for (AutoReconcileBean row : statementsFoundButNotProcessed) {
			statementDataSource.add(row);
		}
		
		paramMap.put("statementsFoundButNotProcessedList",statementDataSource);
		
		paramMap.put("EntriesNotinBankStatement", reportHelper.getClass().getResourceAsStream("/reports/templates/BankBookEntriesNotinBankStatement.jasper"));
		/*To print the subreport if no entires found for EntriesNotinBankStatement added nodetailFound Object
		 */
		if(entriesNotInBankStament.size()==0){
			AutoReconcileObj.setNoDetailsFound("No Details Found");
			entriesNotInBankStament.add(AutoReconcileObj);
		}
		for (AutoReconcileBean row : entriesNotInBankStament) {
			entriesNotInBankStamentDataSource.add(row);
		}
		paramMap.put("BankBookEntriesNotinBankStatementList",entriesNotInBankStamentDataSource);
			
		return paramMap;
	}


public ReportHelper getReportHelper() {
		return reportHelper;
	}

	public void setReportHelper(ReportHelper reportHelper) {
		this.reportHelper = reportHelper;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getNotInBookNetBal() {
		return notInBookNetBal;
	}
	public void setNotInBookNetBal(String notInBookNetBal) {
		this.notInBookNetBal = notInBookNetBal;
	}


	
	
}
