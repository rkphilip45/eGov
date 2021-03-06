/*
 * Created on Apr 20, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.ClosedPeriods;
import com.exilant.eGov.src.domain.FinancialYear;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;


class FY{
	String id;
	String sDate;
	String eDate;
	public FY(){};
	public void setId(String id){this.id = id;}
	public void setSDate(String sDate){this.sDate = sDate;}
	public void setEDate(String eDate){	this.eDate = eDate;	}
	public String getId(){return id;}
	public String getSDate(){return sDate;}
	public String getEDate(){return eDate;}
}
public class SetUp extends AbstractTask{

       private Connection connection;
       private PreparedStatement pst;
       private PreparedStatement pst1;
       private ResultSet resultset;
       private ResultSet resultsetdtl;
       private static  final Logger LOGGER = Logger.getLogger(SetUp.class);
       private String effectiveDate;

       public void execute(String taskName,
                                                       String gridName,
                                                       DataCollection dc,
                                                       Connection con,
                                                       boolean errorOnNoData,
                                                       boolean gridHasColumnHeading,
                                                       String prefix) throws TaskFailedException {
               this.connection = con;
               boolean transferred;
               EGovernCommon cm = new EGovernCommon();
               effectiveDate = cm.getCurrentDateTime(connection);
               try{
			   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					Date dt=new Date();
					dt = sdf.parse( effectiveDate );
					effectiveDate = formatter.format(dt);
		   		}catch(Exception e){
		   			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exception in date formatting "+e.getMessage(),e);
		   			throw new TaskFailedException();
		   		}

               try{
                       if(dc.getValue("activity").equalsIgnoreCase("OFY"))
                               openFY(dc); //Open Financial Year
                       else if(dc.getValue("activity").equalsIgnoreCase("CFY")){
                       	String fyId=dc.getValue("financialYear_id");
                       	if(isPreToFYOpen(fyId)){
                            dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                            throw new TaskFailedException();
                       		}

                               transferred=checkForTransferClosingBalance(con,dc); //Check for TransferClosingBalance
                               if(transferred==false){
                                       dc.addMessage("userFailure"," Cannot Close this financial year as Transfer Closing Balance not done");
                                       throw new TaskFailedException();
                               }

                               closeFY(dc); //Close Financial Year
                       }
                       else if(dc.getValue("activity").equalsIgnoreCase("OP"))
                               openDateRange(dc); //Open Periods
                       else if(dc.getValue("activity").equalsIgnoreCase("CP")){
                    	   /*	String fyId=dc.getValue("financialYear_id");
                       	if(isPreToFYOpen(fyId)){
                            dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                            throw new TaskFailedException();
                       		}*/

                               closeDateRange(dc); //Close Data Range

                       }
                       else if(dc.getValue("activity").equalsIgnoreCase("CB")){
                       	//	   String fyId = dc.getValue("financialYear_id");
                               calcClosingBalance(dc); //Calculate Closing Balance
                               updateTransferClosingBalance(con,dc);//Updating TransferClosingBalance in FinancialYear

                       }
                       else if(dc.getValue("activity").equalsIgnoreCase("CFNEW")){
                       	String fyId=dc.getValue("financialYear_id");
                       	if(isPreToFYOpen(fyId)){
                            dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                            throw new TaskFailedException();
                       		}
                                       calcClosingBalance(dc); //Calculate Closing Balance
                                       closeFY(dc); //Close Financial Year


                       }
                       dc.addMessage("eGovSuccess","SetUp");
               }catch(SQLException ex){
                   		dc.addMessage("exilError","Error in processing"+ex.getMessage());
//                       dc.addMessage("eGovFailure","SetUp");
                   		LOGGER.error(ex.getMessage(), ex);
                       throw new TaskFailedException();
               }
               /*catch(Exception ex){
                       throw new TaskFailedException();
               }*/
       }
       private void updateTransferClosingBalance(Connection connection, DataCollection dc)throws TaskFailedException{
               String fyId = dc.getValue("financialYear_id");
               int n=0;
               try{
               String query = "update financialYear set TRANSFERCLOSINGBALANCE = 1 where id= ?";
               pst = connection.prepareStatement(query);
               pst.setString(1, fyId);
               n = pst.executeUpdate();
           		if(n==1)if(LOGGER.isDebugEnabled())     LOGGER.debug("executed successfully n value "+n);
               }
               catch(Exception ex){
                               if(LOGGER.isDebugEnabled())     LOGGER.debug("Error : " + ex.toString(),ex);
                               dc.addMessage("eGovFailure","Unable to update the TRANSFERCLOSINGBALANCE in Financial Year" );
                               throw new TaskFailedException(ex);
                               }
       }

       private boolean checkForTransferClosingBalance(Connection connection, DataCollection dc) throws TaskFailedException{
               String fyId = dc.getValue("financialYear_id");
              try{
            	   	   String query = "select TRANSFERCLOSINGBALANCE from financialYear where id= ? and TRANSFERCLOSINGBALANCE=0";
                       pst = connection.prepareStatement(query);
                       pst.setString(1, fyId);
                       resultset = pst.executeQuery();

                       if(resultset.next()){
                               return false;
                       }
                       return true;
               }
               catch(Exception ex){
                               if(LOGGER.isDebugEnabled())     LOGGER.debug("Error : " + ex.toString(),ex);
                               throw new TaskFailedException(ex);
                               }
       }

      private void openFY(DataCollection dc) throws TaskFailedException{
               String fyId = dc.getValue("financialYear_id");
               try{
            	   	   String query = "SELECT id FROM financialYear " +
                       "WHERE startingDate > (SELECT endingDate FROM financialYear WHERE id = ?) " +
                       "AND isClosed = 1";
                       pst = connection.prepareStatement(query);
                       pst.setString(1, fyId);
                       resultset = pst.executeQuery();
                       if(resultset.next()){
                               dc.addMessage("exilError","This Financial Year can not be opened, later is closed");
                               throw new TaskFailedException();
                       }
                       resultset.close();
                       pst.close();

                       FinancialYear fy = new FinancialYear();
                       fy.setId(fyId);
                       fy.setIsActiveForPosting("1");
                       fy.update(connection);
               }catch(SQLException ex){
                       if(LOGGER.isDebugEnabled())     LOGGER.debug("Error (SetUp->openFY): " + ex.toString(),ex);
                       dc.addMessage("exilError", "SetUp->openFY failed");
                       throw new TaskFailedException();
               }
       }
       private void closeFY(DataCollection dc) throws TaskFailedException, SQLException {
               String fyId = dc.getValue("financialYear_id");
               boolean calcClBal = dc.getValue("calcClBal").equalsIgnoreCase("1")?true:false;
               if(!isFYOpen(fyId)){
                       dc.addMessage("exilError","Financial Year is already closed");
                       throw new TaskFailedException();
               }

              /* if(isPreToFYOpen(fyId)){
                       dc.addMessage("exilError","Previos Financial Year is Open, it can not be closed");
                       throw new TaskFailedException();
               }*/
               FinancialYear fy = new FinancialYear();
               fy.setId(fyId);
               fy.setIsActiveForPosting("0");
               fy.setIsClosed("1");

               fy.setTransferClosingBalance("1");
               try{
                       fy.update(connection);
               }catch(SQLException ex){
                LOGGER.error(ex.getMessage(), ex);
            	   dc.addMessage("exilError", "SetUp->closeFY failed");
                       throw new TaskFailedException();
               }

               //Closing Balance
               if(calcClBal && !calcClosingBalance(fyId,dc.getValue("current_UserID"))){
                       dc.addMessage("exilError","Closing Balance Failed");
                       throw new TaskFailedException();
               }
       }

       /**
        * This function is called to calculate the closing balance for any financial year
        * @param dc
        * @throws TaskFailedException
        */
       private void calcClosingBalance(DataCollection dc) throws TaskFailedException{
       	if(LOGGER.isDebugEnabled())     LOGGER.debug("Calculate cloasing balance is called...");
		String fyId = dc.getValue("financialYear_id");
		if(!calcClosingBalance(fyId,dc.getValue("current_UserID"))){
			dc.addMessage("exilError","Closing Balance Failed");
			throw new TaskFailedException();
		}
	}
       /**
        * This function will do the closing balance calculation
        * @param fyId
        * @return
        */
       	private boolean calcClosingBalance(String fyId,String userId)throws TaskFailedException{
		boolean isDone = false;
		FY fy = getFinancialYear(fyId);
		String sDate=fy.getSDate();
		String eDate=fy.getEDate();
		String nextFYId = getNextFYId(fyId);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Current Year is .."+fyId+"  Next Year for "+fyId+" is :"+nextFYId);
		String clBalQuery = "";
		try{
			
			//Getting the Closing Balance For glCode-Fund-FY-Department-Function from transaction table for all non-control codes
			clBalQuery = "SELECT gl.glcodeId, vh.fundId, mis.departmentid,gl.functionid,  sum(debitAmount) AS \"dr\", " +
							" sum(creditAmount) AS \"cr\", (sum(debitAmount) - sum(creditAmount)) AS \"balance\" " +
							" FROM voucherHeader vh,vouchermis mis, chartOfAccounts coa, generalledger gl " +
							" WHERE vh.id = gl.voucherHeaderId AND gl.glCode=coa.glcode " +
							" and vh.id=mis.voucherheaderid "+
							" AND voucherDate >= ? AND voucherDate <= ? AND vh.status not in(4,5)  " + // remove this
							" AND coa.type in('A','L') and coa.id not in(select glcodeid from chartofaccountdetail) "+
							" GROUP BY gl.glcodeId,vh.fundId,mis.departmentid,gl.functionid";
			pst = connection.prepareStatement(clBalQuery);
			pst.setString(1, sDate);
			pst.setString(2, eDate);
			if(LOGGER.isInfoEnabled())     LOGGER.info(clBalQuery);
			resultset = pst.executeQuery();

			String id="",query="";
			double bal=0, opDr=0, opCr=0, dr=0, cr=0;
			if(nextFYId.equalsIgnoreCase("")){
				if(LOGGER.isInfoEnabled())     
					LOGGER.info("Next FY not present ***************************");
				return false;
			}
			
			//Delete all the opening balances present for the new financial year. This is because we are going to recalculate from last year
			String query1 = "DELETE from transactionSummary WHERE financialYearId= ?";
			pst1 = connection.prepareStatement(query1);
			pst1.setString(1, nextFYId);
			pst1.executeUpdate();
			
			LOGGER.debug("Deleted the existing entries in transactionSummary for financialYearId="+nextFYId);
			// Preparing the insert statements for non-control codes
			int counter = 0;
			
			query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
			"openingdebitbalance, openingcreditbalance, debitamount, " +
			"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,departmentid,lastmodifiedby,lastmodifieddate,functionid) " +
			"VALUES ( ?, ?, ?, ?, ?, ?, ?, null, null, ?,?, ?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'),?)";
			PreparedStatement pstBatch = connection.prepareStatement(query);
			while(resultset.next()){
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
				bal = resultset.getDouble("balance");
				opDr = bal>0?bal:0;
				opCr = bal<0?Math.abs(bal):0;
				pstBatch.setString(1, id);
				pstBatch.setString(2, nextFYId);
				pstBatch.setString(3, resultset.getString("glCodeId"));
				pstBatch.setDouble(4, opDr);
				pstBatch.setDouble(5, opCr);
				pstBatch.setDouble(6, dr);
				pstBatch.setDouble(7, cr);
				pstBatch.setString(8, resultset.getString("fundId"));
				pstBatch.setString(9, resultset.getString("departmentid"));
				pstBatch.setString(10, userId);
				pstBatch.setString(11,effectiveDate);
				pstBatch.setString(12,resultset.getString("functionid"));
				pstBatch.addBatch();
				
				LOGGER.debug("Query for insert txnsummary for non-control codes -"+query);
				counter++;

                if (counter == 50) {
                	pstBatch.executeBatch();
                   HibernateUtil.getCurrentSession().flush();
                   HibernateUtil.getCurrentSession().clear();
                    pstBatch.clearBatch();
                    counter = 0;
                }
				
			}
			if (counter < 50) {
				pstBatch.executeBatch();
	           HibernateUtil.getCurrentSession().flush();
	           HibernateUtil.getCurrentSession().clear();
	            pstBatch.clearBatch();
			}

			 resultset.close();
			 resultset= null;
			 pstBatch.close();

			//Closing Balance For glCode-Fund-FY-accountEntity from transaction table
			String glCodeAndDetailType = "select coa.glCode AS \"glCode\", cod.detailTypeId AS \"detailTypeId\" " +
					"FROM chartOfAccounts coa, chartOfAccountDetail cod " +
					"WHERE coa.id = cod.glCodeId  and coa.type in ('A','L') order by  coa.glCode,cod.detailTypeId ";
			if(LOGGER.isInfoEnabled())
				LOGGER.info("glCodeAndDetailType: " + glCodeAndDetailType);
			resultset = null;
			pst = connection.prepareStatement(glCodeAndDetailType);
			resultset = pst.executeQuery();
			
			String codeAndDetailCondition = "";
			int counterSL = 0;
			//
			while(resultset.next())
			{
				codeAndDetailCondition = " coa.glCode = '"+resultset.getString("glCode")+"' AND gld.detailTypeId = "+resultset.getString("detailTypeId")+" ";
			
				if(codeAndDetailCondition.trim().length()>0)
				{
					clBalQuery = "SELECT gl.glcodeId, vh.fundId, mis.departmentid,gl.functionid,gld.detailTypeId, gld.detailKeyId, sum(decode(gl.debitamount,0,0, gld.amount)) AS \"dr\", " +
							" sum(decode(gl.debitamount,0,gld.amount,0)) AS \"cr\", sum(decode(gl.debitamount,0,0, gld.amount))-sum(decode(gl.debitamount,0,gld.amount,0)) AS \"balance\" " +
							" FROM voucherHeader vh,vouchermis mis, chartOfAccounts coa, generalledger gl, generalLedgerDetail gld  " +
							" WHERE vh.id = gl.voucherHeaderId and vh.id=mis.voucherheaderid  AND  gl.glCode=coa.glcode AND gl.id = gld.generalLedgerId AND " + codeAndDetailCondition +"" +
							" AND voucherDate >= ? AND voucherDate <= ? AND coa.type in('A','L') AND vh.status not in(4,5) " +
							" GROUP BY gl.glcodeId, vh.fundId,mis.departmentid,gl.functionid,gld.detailTypeId, gld.detailKeyId"+
							" order by gl.glcodeId, vh.fundId,mis.departmentid,gl.functionid,gld.detailTypeId, gld.detailKeyId";
		
					resultsetdtl = null;
					if(LOGGER.isInfoEnabled())     LOGGER.info("clBalQuery  ..."+clBalQuery);
					pst = connection.prepareStatement(clBalQuery);
					pst.setString(1, sDate);
					pst.setString(2, eDate);
					resultsetdtl = pst.executeQuery();
				}
				String txnSumBatch="", detailTypeId="", detailKeyId="";
				
				//pstBatch.clearBatch();
				txnSumBatch = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
				"openingdebitbalance, openingcreditbalance, debitamount, " +
				"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,departmentid,lastmodifiedby,lastmodifieddate,functionid) " +
				"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'),?)";
				pstBatch = connection.prepareStatement(txnSumBatch);
				while(resultsetdtl.next()){
					LOGGER.debug("Inside the control code loop :Counter : "+counterSL+".....Glcode :"+resultsetdtl.getString("glCodeId")+".....Detail type :"+resultsetdtl.getString("detailTypeId")+"....Detail Key :"+resultsetdtl.getString("detailKeyId"));
					id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
					//txnDetailId = String.valueOf(PrimaryKeyGenerator.getNextKey("transactionSummaryDetails"));
	
					bal = resultsetdtl.getDouble("balance");
					opDr = bal>0?bal:0;
					opCr = bal<0?Math.abs(bal):0;
					detailTypeId=resultsetdtl.getString("detailTypeId");
					detailKeyId=resultsetdtl.getString("detailKeyId");
					if(LOGGER.isInfoEnabled())     LOGGER.info(txnSumBatch);
					pstBatch.setString(1, id);
					pstBatch.setString(2, nextFYId);
					pstBatch.setString(3, resultsetdtl.getString("glCodeId"));
					pstBatch.setDouble(4, opDr);
					pstBatch.setDouble(5, opCr);
					pstBatch.setDouble(6, dr);
					pstBatch.setDouble(7, cr);
					pstBatch.setString(8, detailTypeId);
					pstBatch.setString(9, detailKeyId);
					pstBatch.setString(10, resultsetdtl.getString("fundId"));
					pstBatch.setString(11, resultsetdtl.getString("departmentid"));
					pstBatch.setString(12, userId);
					pstBatch.setString(13,effectiveDate);
					pstBatch.setString(14,resultsetdtl.getString("functionid"));
				//	LOGGER.error("before");
					pstBatch.addBatch();
					//LOGGER.error("after");
					LOGGER.info(txnSumBatch);
					counterSL++;
					
					if (counterSL == 50) {
						pstBatch.executeBatch();
	                   HibernateUtil.getCurrentSession().flush();
	                   HibernateUtil.getCurrentSession().clear();
	                    pstBatch.clearBatch();
	                    counterSL = 0;
					}
				}
				if (counterSL < 50) {
					pstBatch.executeBatch();
	               HibernateUtil.getCurrentSession().flush();
	               HibernateUtil.getCurrentSession().clear();
	                pstBatch.clearBatch();
				}
				pstBatch.close();
				resultsetdtl.close();
			}				
			resultset.close();
			
			/** Calculating current year openingBalance+ClosingBalance for next year's OpeningBalance **/
			/** ********************************* **/

			double bal1=0, opDr1=0, opCr1=0;
			String qryTxns="SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
			"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
			"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
			", (cl.openingDebitBalance+op.openingDebitBalance) - (cl.openingCreditBalance+op.openingCreditBalance) AS \"balance\" "+
			"FROM transactionSummary cl, transactionSummary op " +
			"WHERE cl.financialYearId = ? AND op.financialYearId = ? " +
			"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId " +
			"AND cl.departmentid = op.departmentid  " +
			"AND cl.functionid = op.functionid  and cl.functionid is not null and  op.functionid is not null " +
			"AND cl.accountDetailTypeId = op.accountDetailTypeId " +
			"AND cl.accountDetailKey = op.accountDetailKey  " +  
			" union "+
			"SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
			"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
			"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
			", (cl.openingDebitBalance+op.openingDebitBalance) - (cl.openingCreditBalance+op.openingCreditBalance) AS \"balance\" "+
			"FROM transactionSummary cl, transactionSummary op " +
			"WHERE cl.financialYearId = ? AND op.financialYearId = ? " +
			"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId  " +
			"AND cl.functionid = op.functionid  and cl.functionid is not null and  op.functionid is not null " +
			"AND cl.departmentid = op.departmentid  " +
			" and cl.accountdetailtypeid is  null and op.accountdetailtypeid is  null  and cl.accountdetailkey is  null and op.accountdetailkey is  null "+
			"UNION "+			
			"SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
			"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
			"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
			", (cl.openingDebitBalance+op.openingDebitBalance) - (cl.openingCreditBalance+op.openingCreditBalance) AS \"balance\" "+
			"FROM transactionSummary cl, transactionSummary op " +
			"WHERE cl.financialYearId = ? AND op.financialYearId = ? " +
			"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId " +
			"AND cl.departmentid = op.departmentid  " +
			"AND cl.functionid is null and  op.functionid is null " +
			"AND cl.accountDetailTypeId = op.accountDetailTypeId " +
			"AND cl.accountDetailKey = op.accountDetailKey  " +  
			" union "+
			"SELECT cl.id AS \"clId\", op.id AS \"opId\", " +
			"cl.openingDebitBalance+op.openingDebitBalance AS \"dr\", " +
			"cl.openingCreditBalance+op.openingCreditBalance AS \"cr\" " +
			", (cl.openingDebitBalance+op.openingDebitBalance) - (cl.openingCreditBalance+op.openingCreditBalance) AS \"balance\" "+
			"FROM transactionSummary cl, transactionSummary op " +
			"WHERE cl.financialYearId = ? AND op.financialYearId = ? " +
			"AND cl.glCodeId = op.glCodeId AND cl.fundId = op.fundId  " +
			"AND cl.functionid is null and  op.functionid is null " +
			"AND cl.departmentid = op.departmentid  " +
			" and cl.accountdetailtypeid is  null and op.accountdetailtypeid is  null  and cl.accountdetailkey is  null and op.accountdetailkey is  null ";

			if(LOGGER.isInfoEnabled())
				LOGGER.info("Transaction summary part for Calculating current year openingBalance+ClosingBalance :"+qryTxns);
			pst = connection.prepareStatement(qryTxns);
			pst.setString(1, fyId);
			pst.setString(2, nextFYId);
			pst.setString(3, fyId);
			pst.setString(4, nextFYId);
			pst.setString(5, fyId);
			pst.setString(6, nextFYId);
			pst.setString(7, fyId);
			pst.setString(8, nextFYId);
			resultset = pst.executeQuery();

			//pstBatch.clearBatch();
			//String clIds="";
			query = "UPDATE transactionSummary SET openingDebitBalance = ?,openingCreditBalance = ? WHERE id = ?";
			
			pstBatch = connection.prepareStatement(query);
					
			ArrayList<Integer> closingBalanceId = new ArrayList<Integer>();
			int totalCounterCB=0;
			
			while(resultset.next()){
				bal1=opDr1=opCr1=0;
				bal1 = resultset.getDouble("balance");
				opDr1 = bal1>0?bal1:0;
				opCr1 = bal1<0?Math.abs(bal1):0;
						
		//		LOGGER.debug("Adding to the  closingBalanceId list :"+resultset.getInt("clId"));
				closingBalanceId.add(totalCounterCB, resultset.getInt("clId"));
			
				//Get the opening balance and then find the net opening debit or credit.
				pstBatch.setDouble(1, opDr1);
				pstBatch.setDouble(2, opCr1);
				pstBatch.setString(3, resultset.getString("opId"));
				pstBatch.addBatch();
				if(LOGGER.isInfoEnabled())     LOGGER.info("Update query for where OPB and CLS balance is there :"+query);
				totalCounterCB++;

                if (totalCounterCB % 50==0) {
                	pstBatch.executeBatch();
                   HibernateUtil.getCurrentSession().flush();
                   HibernateUtil.getCurrentSession().clear();
                    pstBatch.clearBatch();
                }
			}	
			if (totalCounterCB % 50 !=0) {
				pstBatch.executeBatch();
               HibernateUtil.getCurrentSession().flush();
               HibernateUtil.getCurrentSession().clear();
                pstBatch.clearBatch();
            }
			resultset.close();
			
			/**
			 * Here is the logic to split the closing balance ids into groups of 1000s
			 */
			String closingBalanceStr="";
			String closingBalanceStrLoop="";
			
			LOGGER.info("Count of closing balance Ids :"+closingBalanceId.size());
			if (null!=closingBalanceId && closingBalanceId.size()>0){	
				closingBalanceStr=" AND id NOT IN (";
				for (int i=0;i<closingBalanceId.size();i++){
					closingBalanceStrLoop=closingBalanceStrLoop+closingBalanceId.get(i)+",";
					if((i+1) % 1000==0){
						LOGGER.debug("Comming inside the splitting block..");
						closingBalanceStr=closingBalanceStr+closingBalanceStrLoop;
						closingBalanceStr = closingBalanceStr.substring(0, closingBalanceStr.length()-1);
						closingBalanceStr=closingBalanceStr+")";
						closingBalanceStrLoop="";
						if (i+1!=closingBalanceId.size())	{
							//LOGGER.debug("Coming inside the splitting block where length does not matches");
							closingBalanceStr=closingBalanceStr+" AND id NOT IN (";
						}
					}
				}
				
				if(closingBalanceId.size() % 1000 != 0){
					LOGGER.debug("Comming inside the last block for framing the string.");
					closingBalanceStr=closingBalanceStr+closingBalanceStrLoop;
					closingBalanceStr = closingBalanceStr.substring(0, closingBalanceStr.length()-1)+")";
				}	
			}
			 LOGGER.debug("Final string of closing balance :"+closingBalanceStr);
			 
			resultset = null;
			//taking previous year opening balance for glcodes combination for which there is no txn and OPB in current year.
			String qry1="SELECT id, glCodeId, openingDebitBalance AS \"dr\", " +
			"openingCreditBalance AS \"cr\", debitAmount, creditAmount,accountDetailTypeId, " +
			"accountDetailKey, financialYearId, fundId,departmentid,functionid,(openingDebitBalance-openingCreditBalance)as \"balance\" FROM transactionSummary " +
			"WHERE financialYearId = ?" +closingBalanceStr;
			if(LOGGER.isInfoEnabled())     LOGGER.info("qry1 for OPB for new year ...."+qry1);
			pst = connection.prepareStatement(qry1);
			pst.setString(1, fyId);
			resultset = pst.executeQuery();
			
			query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid, " +
				"openingdebitbalance, openingcreditbalance, debitamount, " +
				"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,departmentid,lastmodifiedby,lastmodifieddate,functionid) " +
				"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'),?)";
			pstBatch.clearBatch();
			pstBatch= connection.prepareStatement(query);
			if(LOGGER.isInfoEnabled())     LOGGER.info("Insert for last year OPB same as next year OPB :"+query);
			int counterOPBOnly=0;
			while(resultset.next()){
				bal=opDr=opCr=0;
				bal=resultset.getDouble("balance");
				opDr = bal>0?bal:0;
				opCr = bal<0?Math.abs(bal):0;
				id = String.valueOf(PrimaryKeyGenerator.getNextKey("transactionSummary"));
				
				pstBatch.setString(1, id);
				pstBatch.setString(2, nextFYId);
				pstBatch.setString(3, resultset.getString("glCodeId"));
				pstBatch.setDouble(4, opDr);
				pstBatch.setDouble(5, opCr);
				pstBatch.setDouble(6, resultset.getDouble("debitAmount"));
				pstBatch.setDouble(7, resultset.getDouble("creditAmount"));
				pstBatch.setString(8, resultset.getString("accountDetailTypeId"));
				pstBatch.setString(9, resultset.getString("accountDetailKey"));
				pstBatch.setString(10, resultset.getString("fundId"));
				pstBatch.setString(11, resultset.getString("departmentid"));
				pstBatch.setString(12, userId);
				pstBatch.setString(13, effectiveDate);
				pstBatch.setString(14,resultset.getString("functionid"));
				pstBatch.addBatch();
				
				
				if (counterOPBOnly == 50) {
					pstBatch.executeBatch();
                   HibernateUtil.getCurrentSession().flush();
                   HibernateUtil.getCurrentSession().clear();
                    pstBatch.clearBatch();
                    counterOPBOnly = 0;
                }
			}
			if (counterOPBOnly < 50) {
				pstBatch.executeBatch();
               HibernateUtil.getCurrentSession().flush();
               HibernateUtil.getCurrentSession().clear();
                pstBatch.clearBatch();
            }

			resultset.close();
			pst.close();
			//This function will get the income over expense and transfer to the Fund Balance
			getIncomeOverExpense(sDate,eDate,nextFYId,fyId);
			if(LOGGER.isInfoEnabled())     LOGGER.info("**************************** Closing Balance Done");
			isDone = true;
		}catch(Exception ex){
			LOGGER.error("Exception in calculate closing balance :"+ex.getMessage(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return isDone;
	}

       	/**
       	 * Create the FY object when a financial year id is passed
       	 * @param fyId
       	 * @return
       	 * @throws TaskFailedException
       	 */
       	private FY getFinancialYear(String fyId)throws TaskFailedException{
		FY fy = new FY();
		try{
			String query = "SELECT to_char(startingDate, 'DD-Mon-yyyy') AS \"startingDate\", to_char(endingDate, 'DD-Mon-yyyy') AS \"endingDate\" " +
			"FROM financialYear WHERE id= ?";
			pst = connection.prepareStatement(query);
			pst.setString(1, fyId);
			resultset = pst.executeQuery();
			if(resultset.next()){
				fy.setId(fyId);
				fy.setSDate(resultset.getString("startingDate"));
				fy.setEDate(resultset.getString("endingDate"));
			}
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->getFinancialYear: " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return fy;
	}

       	/**
       	 * This function will return the next financial year of the financial id passed to the API
       	 * @param fyId
       	 * @return
       	 */
       	private String getNextFYId(String fyId)throws TaskFailedException{
		String nextFYId="";
		try{
			String nxtYearStr="SELECT id FROM financialYear " +
			"WHERE startingDate = (SELECT endingDate+1 FROM financialYear WHERE id = ?)";
			pst = connection.prepareStatement(nxtYearStr);
			pst.setString(1, fyId);
			if(LOGGER.isInfoEnabled())     LOGGER.info("Query for next year :"+nxtYearStr);
			resultset = pst.executeQuery();
			if(resultset.next())
				nextFYId = resultset.getString("id");
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->getNextFYId: " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return nextFYId;
	}

	private boolean isPreToFYOpen (String fyId)throws TaskFailedException{
		boolean isOpen=false;
		try{
			
			FinancialYearHibernateDAO findao=new FinancialYearHibernateDAO();
			CFinancialYear financialYearById = findao.getFinancialYearById(Long.parseLong(fyId));
			CFinancialYear previousFinancialYearByDate = findao.getPreviousFinancialYearByDate(financialYearById.getStartingDate());
			if(previousFinancialYearByDate.getIsClosed()!=1) isOpen = true;			
		}catch(Exception ex){
			isOpen = false;
			LOGGER.error("Error SetUp->getNextFYId: " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return isOpen;
	}
	
	private void openDateRange(DataCollection dc) throws TaskFailedException{
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("openDateRange");
		if(LOGGER.isDebugEnabled())     LOGGER.debug(dc.getValue("closedPeriods_id"));
		String sDate="", eDate="";
		try{
			String query1 = "SELECT to_char(startingDate, 'DD-Mon-yyyy') AS \"startingDate\", to_char(endingDate, 'DD-Mon-yyyy') AS \"endingDate\" " +
			"FROM closedPeriods " +
			"WHERE id= ?";
			pst = connection.prepareStatement(query1);
			pst.setString(1, dc.getValue("closedPeriods_id"));
			resultset = pst.executeQuery();
			if(resultset.next()){
				sDate = resultset.getString("startingDate");
				eDate = resultset.getString("endingDate");
			}
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->openDateRange() InValid Id: " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}

		if(!isFYOpen(sDate, eDate)){
			dc.addMessage("exilError","Financial Year is Closed");
			throw new TaskFailedException("Financial Year is Closed");
		}
		/*if(priorToHardClosed(sDate, eDate)){
			dc.addMessage("exilError","date range can not be opened prior to hard closed date range");
			throw new TaskFailedException("Date range can not be opened prior to hard closed date range");
		}*/
		try{
			String query2 = "DELETE from closedPeriods WHERE id= ?";
			pst = connection.prepareStatement(query2);
			pst.setString(1, dc.getValue("closedPeriods_id"));
			resultset = pst.executeQuery();
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->openDateRange(): " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
	}
	private void closeDateRange(DataCollection dc) throws TaskFailedException{
		String clstartingDate="";
		String clendingDate="";
		try
   		{
			//if(LOGGER.isDebugEnabled())     LOGGER.debug("inside try catch");
   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date dt=new Date();
			clstartingDate=dc.getValue("closedPeriods_startingDate");
			dt = sdf.parse( clstartingDate );
			clstartingDate = formatter.format(dt);
			clendingDate=dc.getValue("closedPeriods_endingDate");
			dt = sdf.parse( clendingDate );
			clendingDate = formatter.format(dt);
			//if(LOGGER.isDebugEnabled())     LOGGER.debug("within try transactionDate"+clendingDate);


   		}
   		catch(Exception e){
   			LOGGER.error(e.getMessage(), e);
   			throw new TaskFailedException(e.getMessage());
   		}
		//String sDate = dc.getValue("closedPeriods_startingDate");
		//String eDate = dc.getValue("closedPeriods_endingDate");

   		String sDate = clstartingDate;
   		String eDate = clendingDate;
		String hardClose = dc.getValue("hardClose");


		if(!isFYOpen(sDate, eDate)){
			dc.addMessage("exilError","Financial Year is Closed Or date range out of Financial Year");
			throw new TaskFailedException();
		}
		//String fyEndingDate = isFYEndingDate(eDate);
		/*if(isFYEndingDate(eDate)){
			dc.addMessage("exilError","last date of a Financial Year can not be closed");
			throw new TaskFailedException();
		}*/
		if(hardClose.equalsIgnoreCase("1") && isHardClosed(sDate, eDate)){
			dc.addMessage("exilError","date range already hard closed");
			throw new TaskFailedException();
		}
		int cpId = isSoftClosed(sDate, eDate);
		if(cpId > 0){
			if(hardClose.equalsIgnoreCase("0")){
				dc.addMessage("exilError","date range already soft closed");
				throw new TaskFailedException();
			}
			closePeriod(sDate, eDate, hardClose, cpId);
			/*if(canBeHardClosed(sDate))
				closePeriod(sDate, eDate, hardClose, cpId);
			else{
				dc.addMessage("exilError","can not be hard closed, all previous ranges must be hard closed");
				throw new TaskFailedException();
			}*/
			return;
		}
		if(rangeOverlaps(sDate, eDate)){
			dc.addMessage("exilError","date range overlaps with previously closed date range");
			throw new TaskFailedException();
		}

		if(hardClose.equalsIgnoreCase("0")){
			if(priorToHardClosed(sDate, eDate)){
				dc.addMessage("exilError","Soft Closing can not be performed prior to hard closed date range");
				throw new TaskFailedException();
			}
			closePeriod(sDate, eDate, hardClose, 0);
			return;
		}else
		{
			closePeriod(sDate, eDate, hardClose, 0);
		}

		/*if(canBeHardClosed(sDate))
			closePeriod(sDate, eDate, hardClose, 0);
		else{
			dc.addMessage("exilError","can not be hard closed, all previous ranges must be hard closed");
			throw new TaskFailedException();
		}*/
	}
	private boolean closePeriod(String sDate,
									String eDate,
									String hardClose,
									int id) throws TaskFailedException{
		boolean success = false;
		ClosedPeriods cp = new ClosedPeriods();
		cp.setStartingDate(sDate);
		cp.setEndingDate(eDate);
		cp.setIsClosed(hardClose);
		try{
			if(id==0) cp.insert(connection);
			else {
				cp.setId(id + "");
				cp.update(connection);
			}
			success = true;
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->closePeriod(): " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return success;
	}


	private boolean rangeOverlaps(String sDate,
									String eDate)throws TaskFailedException{
		boolean overlap = false;
		try{
			String query = "SELECT id FROM closedPeriods " +
			"WHERE (startingDate <= ? AND endingDate >= ?) " +
			"OR (startingDate <= ? AND endingDate >= ?) " +
			"OR (startingDate >= ? AND endingDate <= ?) " +
			"OR (startingDate >= ? AND startingDate <= ?) " +
			"OR (endingDate > ? AND endingDate < ?)";
			pst = connection.prepareStatement(query);
			pst.setString(1, sDate);
			pst.setString(2, sDate);
			pst.setString(3, eDate);
			pst.setString(4, eDate);
			pst.setString(5, sDate);
			pst.setString(6, eDate);
			pst.setString(7, sDate);
			pst.setString(8, eDate);
			pst.setString(9, sDate);
			pst.setString(10, sDate);
			resultset = pst.executeQuery();

			if(resultset.next()) overlap = true;
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->rangeOverlaps(): " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return overlap;
	}

	private boolean isHardClosed(String sDate,
									String eDate)throws TaskFailedException{
		boolean hardClosed = false;
		try{
			String query  = "SELECT id FROM closedPeriods " +
			"WHERE startingDate = ? AND endingDate = ? AND isClosed=1";
			pst = connection.prepareStatement(query);
			pst.setString(1, sDate);
			pst.setString(2, eDate);
			resultset = pst.executeQuery();
			if(resultset.next()) hardClosed = true;

			resultset.close();
			pst.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->isHardClosed(): " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return hardClosed;
	}
	private int isSoftClosed(String sDate,
								String eDate)throws TaskFailedException{
		int id=0;
		try{
			String query = "SELECT id AS \"id\" FROM closedPeriods " +
			"WHERE startingDate = ? AND endingDate = ? AND isClosed=0";
			pst = connection.prepareStatement(query);
			pst.setString(1, sDate);
			pst.setString(2, eDate);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("SELECT id AS \"id\" FROM closedPeriods " +
					"WHERE startingDate = '"+sDate+"' AND endingDate = '"+eDate+"' AND isClosed=0");
			resultset = pst.executeQuery();
			if(resultset.next()) id = resultset.getInt("id");
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			LOGGER.error("Error SetUp->isSoftClosed(): " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return id;
	}
	/*private boolean isFYEndingDate(String eDate)throws TaskFailedException{
		boolean isEndDate=false;
		try{
			String query = "SELECT id FROM financialYear " +
			"WHERE endingDate = ?";
			pst = connection.prepareStatement(query);
			pst.setString(1, eDate);
			resultset = pst.executeQuery();
			if(resultset.next()) isEndDate = true;
			resultset.close();
			pst.close();
		}catch(SQLException ex){
			isEndDate = true;
			LOGGER.error("Error SetUp->isFYEndingDate: " + ex.toString(),ex);
			throw new TaskFailedException(ex.getMessage());
		}
		return isEndDate;
	}
*/
       private boolean priorToHardClosed(String sDate,String eDate)throws TaskFailedException{
               boolean isPrior=false;
               try{
            	   	   String query = "SELECT id FROM closedPeriods " +
                       "WHERE (endingDate>= ? OR endingDate>= ?) " +
                       "AND isClosed = 1";
                       pst = connection.prepareStatement(query);
                       pst.setString(1, sDate);
                       pst.setString(2, eDate);
                       resultset = pst.executeQuery();
                       if(resultset.next()) isPrior = true;
                       resultset.close();
                       pst.close();
               }catch(SQLException ex){
                       LOGGER.error("Error SetUp->priorToHardClosed(): " + ex.toString(),ex);
                       throw new TaskFailedException(ex.getMessage());
               }
               return isPrior;
       }

  /*     private boolean canBeHardClosed(String sDate)throws TaskFailedException{
               boolean canBeHardClosed=false;
               try{
            	   			   String query1 = "SELECT id FROM financialYear WHERE isClosed=1";
                               pst = connection.prepareStatement(query1);
                               resultset = pst.executeQuery();
                               if(!resultset.next()) {
                                       resultset.close();
                                       pst.close();
                                       String sqlQuery = "SELECT id FROM financialYear " +
                                       "WHERE '"+sDate+"' = (SELECT min(startingDate) FROM financialYear)";
                                       pst = connection.prepareStatement(sqlQuery);
                                       resultset = pst.executeQuery();
                                       if(resultset.next()) canBeHardClosed = true;
                                       return canBeHardClosed;
                               }
                               resultset.close();
                               pst.close();

                               if(!canBeHardClosed){
                            	       String query2 = "SELECT id FROM closedPeriods WHERE (endingDate+1 = ? " +
                                       "AND endingDate = (SELECT max(endingDate) FROM closedPeriods WHERE isClosed = 1))";
                                       pst = connection.prepareStatement(query2);
                                       pst.setString(1, sDate);
                                       resultset = pst.executeQuery();
                                       if(resultset.next()) canBeHardClosed = true;
                                       resultset.close();
                                       pst.close();
                               }

                               if(!canBeHardClosed){
                            	   	   String query3 = "SELECT id FROM financialYear WHERE startingDate = ? " +
                                       "AND startingDate > (SELECT max(endingDate) FROM financialYear " +
                                       "WHERE isClosed=1) AND startingDate = (SELECT min(startingDate) " +
                                       "FROM financialYear WHERE isClosed=0)";
                                       pst = connection.prepareStatement(query3);
                                       pst.setString(1, sDate);
                                       resultset = pst.executeQuery();
                                       if(resultset.next()) canBeHardClosed = true;
                                       resultset.close();
                                       pst.close();
                               }
               }catch(SQLException ex){
                       canBeHardClosed = false;
                       LOGGER.error("Error SetUp->canBeHardClosed(): " + ex.toString(),ex);
                       throw new TaskFailedException(ex.getMessage());
               }
               return canBeHardClosed;
       }*/
       private boolean isFYOpen(String sDate, String eDate)throws TaskFailedException{
               boolean isOpen=false;
               try{
            	   String query = "SELECT id FROM financialYear " +
                   					"WHERE startingDate <= ? " +
                   					"AND endingDate >= ? " +
                   					"AND isActiveForPosting=1";
                       pst = connection.prepareStatement(query);
                       pst.setString(1, sDate);
                       pst.setString(2, eDate);
                       resultset = pst.executeQuery();
                       if(resultset.next()) isOpen = true;
                       resultset.close();
                       pst.close();
               }catch(SQLException ex){
                       isOpen = false;
                       LOGGER.error("Error SetUp->isFYOpen(): " + ex.toString(),ex);
                       throw new TaskFailedException(ex.getMessage());
               }
               return isOpen;
       }

       private boolean isFYOpen(String fyId)throws TaskFailedException{
               boolean isOpen=false;
               try{
            	   String query = "SELECT id FROM financialYear WHERE isClosed=1 AND id= ?";
                       pst = connection.prepareStatement(query);
                       pst.setString(1, fyId);
                       resultset = pst.executeQuery();
                       if(!resultset.next()) isOpen = true;
                       resultset.close();
                       pst.close();
               }catch(SQLException ex){
                       isOpen = false;
                       LOGGER.error("Error SetUp->isFYOpen(): " + ex.toString(),ex);
                       throw new TaskFailedException(ex.getMessage());
               }
               return isOpen;
       }

 // This method is to calculate the excess of income over expense. Then insert the Net amount to OPB of next year.
    private void getIncomeOverExpense(String sDate,String eDate,String nextFYId,String currFYId) throws Exception
   	{
   		
   		PreparedStatement pstmt2=null;
   		ResultSet rs2=null;
   		int glcodeid=0;
   		String id=null;
   		String reqFundId[]=getFundList(sDate,eDate);
   		
   		String sqlQuery = " select a.id from chartofaccounts a where purposeid=7";
		pst = connection.prepareStatement(sqlQuery);
		resultset=pst.executeQuery();
		if(resultset.next())
			glcodeid=resultset.getInt(1);
		else
			throw new Exception("Account Code not mapped for Excess IE.");
		resultset.close();
		pst.close();
		
   		for(int i=0;i<reqFundId.length;i++)
		{
			String fundCondition=reqFundId[i];
			double opDr=0,opCr=0;
			LOGGER.debug("Inside  getIncomeOverExpense() for Fund :"+fundCondition);
			
			try{
				String excessIE="SELECT SUM(Income)-SUM(Expense) AS NetAmt,Dep,Func FROM("
				+ " SELECT DECODE(SUM(gl.creditAmount)-SUM(gl.debitamount),NULL,0,SUM(gl.creditAmount)-SUM(gl.debitamount)) AS Income,0 AS Expense,"
				+ " vm.departmentid AS Dep,gl.functionid AS Func"
				+ " FROM chartofaccounts  coa,generalledger gl,voucherHeader vh,vouchermis vm"
				+ " WHERE coa.TYPE = 'I' AND vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode AND vm.voucherheaderid=vh.id"
				+ " AND vh.VOUCHERDATE >= ? AND vh.VOUCHERDATE <= ? AND vh.status NOT IN(4,5) AND vh.fundid="+fundCondition
				+ " GROUP BY vm.departmentid, gl.functionid"
				+ " UNION"
				+ " SELECT 0 AS Income,DECODE(SUM(gl.debitAmount)-SUM(gl.creditamount),NULL,0,SUM(gl.debitAmount)-SUM(gl.creditamount)) AS Expense,"
				+ " vm.departmentid AS Dep,gl.functionid AS Func"
				+ " FROM chartofaccounts  coa,generalledger gl,voucherHeader vh ,vouchermis vm"
				+ " WHERE coa.TYPE = 'E' AND vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode AND vm.voucherheaderid=vh.id"
				+ " AND vh.VOUCHERDATE >= ? AND vh.VOUCHERDATE <= ? AND vh.status NOT IN(4,5) AND vh.fundid="+fundCondition
				+ " GROUP BY vm.departmentid, gl.functionid"
				+ " UNION"
				+ " SELECT DECODE(SUM(openingcreditbalance),NULL,0,SUM(openingcreditbalance)) AS Income,"
				+ " DECODE(SUM(openingdebitbalance),NULL,0,SUM(openingdebitbalance)) AS Expense,departmentid AS Dep,functionid AS Func" 
				+ " FROM transactionsummary "
				+ " WHERE fundid="+fundCondition+ " AND financialyearid= ? AND glcodeid= ?" 
				+ " GROUP BY departmentid,functionid"
				+ " UNION"
				+ " SELECT DECODE(SUM(openingcreditbalance),NULL,0,SUM(openingcreditbalance)) AS Income,"
				+ " DECODE(SUM(openingdebitbalance),NULL,0,SUM(openingdebitbalance)) AS Expense,departmentid AS Dep,functionid AS Func" 
				+ " FROM transactionsummary "
				+ " WHERE fundid="+fundCondition+" AND financialyearid= ?AND glcodeid=? " 
				+ " GROUP BY departmentid,functionid"
				+ " )GROUP BY Dep,Func"
				+ " ORDER BY Dep,Func";
				
				LOGGER.debug("Getting excessIE :"+excessIE);
				
				pstmt2 = connection.prepareStatement(excessIE);
				pstmt2.setString(1, sDate);//fromdate
				pstmt2.setString(2, eDate);//todate
				pstmt2.setString(3, sDate);//fromdate
				pstmt2.setString(4, eDate);//todate
				pstmt2.setString(5, nextFYId);//nextfinancialyear
				pstmt2.setInt(6, glcodeid);//glcodeid
				pstmt2.setString(7, currFYId);//currentfinancialyear
				pstmt2.setInt(8, glcodeid);//glcodeid
				rs2=pstmt2.executeQuery();
				
				String deleteExcessIEForNextYear="Delete from transactionsummary where financialyearid= ? AND glcodeid=? and fundid="+fundCondition;
				pst=connection.prepareStatement(deleteExcessIEForNextYear);
				pst.setString(1, nextFYId);//nextfinancialyear
				pst.setInt(2, glcodeid);//glcodeid
				pst.execute(); //delete if there is any record existing for this code so that one shot we can insert
				
				LOGGER.debug("Deleted the excessIE record for Financial year :"+nextFYId+" for Fundid"+fundCondition);
				
				String query = "INSERT INTO TransactionSummary (id, financialYearId, glcodeid,openingdebitbalance, openingcreditbalance, debitamount, " +
			 	 		"creditamount, accountdetailtypeid, ACCOUNTDETAILKEY, fundId,departmentid,functionid) " +
			 	 		"VALUES ( ?, ?, ?, ?, ?, 0, 0, null, null, " + fundCondition + ",?,?)";
				while (rs2.next())
				{
					id = String.valueOf(PrimaryKeyGenerator.getNextKey("TransactionSummary"));
					pst=connection.prepareStatement(query);
					pst.setString(1, id);
					pst.setString(2, nextFYId);
					pst.setInt(3, glcodeid);
					if(rs2.getDouble("NetAmt")>0){
						opDr=0;
						opCr=rs2.getDouble("NetAmt");
					}else if(rs2.getDouble("NetAmt")<0)
					{
						opDr=Math.abs(rs2.getDouble("NetAmt"));
						opCr=0;
					}
					else{
						LOGGER.debug("Excess IE for this combination is zero hence dont insert "+fundCondition+"**"+rs2.getInt("Dep")+"**"+rs2.getInt("Func"));
						continue;
					}
					pst.setDouble(4, opDr);
					pst.setDouble(5, opCr);
					pst.setString(6, rs2.getString("Dep"));
					pst.setString(7, rs2.getString("Func"));
					
					LOGGER.debug("Inserting the excessIE record for :Department**"+rs2.getInt("Dep")+"**Function**"+rs2.getInt("Func")+"**Amount**"+opDr+"##"+opCr);
					pst.executeUpdate();
					pst.close();
				}
			}catch(Exception ex){
				LOGGER.error("Exception in calculating Excess of Income over Expense :"+ex.getMessage(),ex);
				throw new Exception();
			}
		}	
			
   	}


    private String[] getFundList(String startDate,String endDate) throws Exception
	{
		String fundCondition="";
		String fundCondition1="";
		String fundId="";
		String reqFundId[];
		if(!fundId.equalsIgnoreCase(""))
		{
			fundCondition="AND vh.fundId=?";
			fundCondition1="WHERE transactionsummary.fundId=?";
		}
		try
		{
			String	query = " select f.id,f.name from fund f where (f.id in(SELECT unique vh.fundId FROM chartofaccounts  coa,generalledger gl, "
			  +" voucherHeader vh WHERE (coa.TYPE = 'A' OR coa.TYPE = 'L')  and vh.ID =  gl.VOUCHERHEADERID AND  gl.glcode=coa.glcode "
			  +"  AND vh.status not in(4,5)  AND vh.VOUCHERDATE >= ? AND vh.VOUCHERDATE <= ? "+fundCondition+") or  f.id in(select unique fundid from transactionsummary "+fundCondition1+")) and f.isactive=1 and f.isnotleaf!=1 "
			  +" order by f.id ";
			pst = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int j=1;
			pst.setString(j++, startDate);
			pst.setString(j++, endDate);
			if(!fundId.equalsIgnoreCase("")){
				pst.setString(j++, fundId);
				pst.setString(j++, fundId);
			}
	 		resultset = pst.executeQuery();
	 		int resSize=0,i=0;
	 		if(resultset.last())
	 		resSize=resultset.getRow();
	 		reqFundId=new String[resSize];
	 //	 	Fund fu=null;
	 		resultset.beforeFirst();
	 		while(resultset.next())
	 		{
	 			reqFundId[i]=resultset.getString(1);
	 			i++;
			}
	 	}
		catch(Exception  ex)
		{
			LOGGER.error("Exp in getFundList.."+ex.getMessage(),ex);
			throw new Exception();
		}
		return reqFundId;
	}
}
