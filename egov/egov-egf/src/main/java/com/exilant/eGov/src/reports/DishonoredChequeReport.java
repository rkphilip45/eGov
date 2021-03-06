/*
 * Created on June 2,2008
 * @author Iliyaraja S
 */
package com.exilant.eGov.src.reports;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;

import org.egov.utils.FinancialConstants;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class DishonoredChequeReport
{
	Connection con;
	ResultSet rs;
	Statement statement;
	TaskFailedException taskExc;
	String chqFromDate="", chqToDate="";
	String chequeNo="";
	String intrumentMode="";
	Long fundId;
	public String originalVcId[];
	public String bankRefNo[];
	public String bankCharge[];
    ArrayList arList=new ArrayList();
    java.util.Date dt=new java.util.Date();
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	private SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",Locale.getDefault());
   	EGovernCommon egc=new EGovernCommon();
   	CommnFunctions cf=new CommnFunctions();
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeReport.class);
	public DishonoredChequeReport(){}


// get all Dishonored Cheque details -- chqstatus=2,isReconciled=1,isDishonored=1

public ArrayList getDishonoredChequeDetails(DishonoredChequeBean disChqBean)throws TaskFailedException,Exception
{
			if(LOGGER.isInfoEnabled())     LOGGER.info(" INSIDE getDishonoredChequeDetails()>>>>>>>> ");
			try{
				con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
			}catch(Exception exception){
				LOGGER.error("Could Not Get Connection");
				throw taskExc;
			}
			try{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Chq From date---->"+disChqBean.getStartDate());
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Chq To date------>"+disChqBean.getEndDate());
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Fund id--->"+disChqBean.getFundLst());
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Mode --->"+disChqBean.getMode());
				if(LOGGER.isDebugEnabled())     LOGGER.debug("Cheque Number --->"+disChqBean.getChequeNo());
				if(!"".equals(disChqBean.getStartDate())){
					dt = sdf.parse(disChqBean.getStartDate());
					chqFromDate = formatter.format(dt);
					if(LOGGER.isInfoEnabled())     LOGGER.info("After convert Chq From date is--->"+chqFromDate);
				}
				if(!"".equals(disChqBean.getEndDate())){
					dt = sdf.parse(disChqBean.getEndDate());
					chqToDate = formatter.format(dt);
					if(LOGGER.isInfoEnabled())     LOGGER.info("After convert Chq To date is--->"+chqToDate);
				}
				if(!"".equals(disChqBean.getFundLst()))
					fundId=Long.parseLong(disChqBean.getFundLst());

				if(!"".equals(disChqBean.getChequeNo()))
					chequeNo=disChqBean.getChequeNo();
				
				if(!"".equals(disChqBean.getMode())){
					//intrumentMode=disChqBean.getMode();
					if(disChqBean.getMode().equals("1"))
						intrumentMode=FinancialConstants.INSTRUMENT_TYPE_DD;
					else
						intrumentMode=FinancialConstants.INSTRUMENT_TYPE_CHEQUE;
					
				}	

			// This method for getting bank ref no,bank charge amount and old voucher header id for the Dishonored cheques
			getBankEntryDetails();


			StringBuffer basicquery1 = new StringBuffer("SELECT distinct vh.id as \"voucherHeaderId\",vh.id as \"payinVHeaderId\",vh.cgn as \"cgnumber\"" 
					+",vh.VOUCHERNUMBER as \"voucherNumber\",vh.TYPE as \"type\",vh.FUNDID as \"fundId\",vh.FUNDSOURCEID as \"fundSourceId\",ih.INSTRUMENTNUMBER as \"chequeNumber\","
					+" ih.INSTRUMENTDATE as \"chequeDate\",ih.INSTRUMENTAMOUNT as \"amount\",bank.NAME as \"bank\",bacc.ACCOUNTNUMBER as \"accNumber\"" 
					+",bacc.ID as \"accIdParam\",ih.PAYTO as \"payTo\" ,ih.ISPAYCHEQUE AS \"payCheque\","
					+" vmis.DEPARTMENTID AS \"departmentId\",vmis.FUNCTIONARYID AS \"functionaryId\",iod.INSTRUMENTSTATUSDATE as \"recChequeDate\""
					+" ,iod.dishonorbankrefno  as \"dishonorBankRefNo\",status.description  as \"status\""
					+" FROM VOUCHERHEADER vh,egf_instrumentheader ih,BANK bank,BANKACCOUNT bacc,VOUCHERMIS vmis,bankbranch branch," +
							"egf_instrumenttype it,EGF_INSTRUMENTVOUCHER iv, egf_instrumentotherdetails iod, egw_status status ");

			StringBuffer wherequery1 = new StringBuffer(" WHERE vh.status=0 AND vh.id=vmis.voucherheaderid " +
					" and ih.INSTRUMENTTYPE=it.id and it.TYPE='"+ intrumentMode +"' and iv.VOUCHERHEADERID=vh.ID and iv.INSTRUMENTHEADERID=ih.id " +
					" and iod.instrumentheaderid=ih.id "+
					" and ih.id_status=status.id and  status.moduletype='Instrument' and  status.description  in ('Dishonored','dishonour cheque in workflow') " +
					" and ih.BANKACCOUNTID=bacc.id and bacc.BRANCHID=branch.id and branch.BANKID=bank.id");               

			StringBuffer orderbyquery = new StringBuffer(" ORDER BY \"voucherNumber\",\"type\",\"chequeDate\" ");

					if (fundId !=null && fundId !=0){
						wherequery1 = wherequery1.append(" AND vh.FUNDID=").append(fundId);
					}

					if (!("").equals(chqFromDate)){
						wherequery1 = wherequery1.append(" AND ih.INSTRUMENTDATE  >='").append(chqFromDate).append("'");
					}
					if (!("").equals(chqToDate)){
						wherequery1 = wherequery1.append(" AND ih.INSTRUMENTDATE  <='").append(chqToDate).append("'");
					}
					if (!("").equals(chequeNo)){
						wherequery1 = wherequery1.append(" AND ih.INSTRUMENTNUMBER=trim('").append(chequeNo).append("')");
					}

					String query = new StringBuffer().append(basicquery1).append(wherequery1).append(orderbyquery).toString();
  					if(LOGGER.isDebugEnabled())     LOGGER.debug("  getDishonoredChequeDetails Query is  "+query);
					statement = con.createStatement();
					rs=statement.executeQuery(query);
					if(LOGGER.isDebugEnabled())     LOGGER.debug("After Execute Query----getDishonoredChequeDetails");
					int i=1;
					while (rs.next())
					{
					 boolean bkChgAvailable=false;
					 String voucherHeaderId="",payinSlipVHeaderId="",cgnum="",voucherNumber="",voucherType="";
					 String fundId="",chequeNumber="",chequeDate="",amount="",bankName="",accIdParam="";
					 String	recChequeDate="",payeeName="" ,insMode="",bankReferenceNo="",status="";
					 HashMap data = new HashMap();

					 if(rs.getString("voucherHeaderId")!= null)
						 voucherHeaderId=rs.getString("voucherHeaderId");
					 else
						 voucherHeaderId="&nbsp;";

					 //for bank charges
					 for(int k=0;k<originalVcId.length;k++)
					 {
						if(originalVcId[k].equals(voucherHeaderId))
						{
							bkChgAvailable=true;
							//data.put("bankRefNumber",bankRefNo[k] );
							data.put("bankChargeAmt", bankCharge[k]);
						}
					 }
					 if(!bkChgAvailable)
					 {
						//data.put("bankRefNumber","&nbsp;" );
						data.put("bankChargeAmt", "&nbsp;");
					 }
					 if(rs.getString("payinVHeaderId")!= null)
						 payinSlipVHeaderId=rs.getString("payinVHeaderId");
					 else
						 payinSlipVHeaderId="&nbsp;";
					
					 if(rs.getString("dishonorBankRefNo")!= null)
						 bankReferenceNo=rs.getString("dishonorBankRefNo");
					 else
						 bankReferenceNo="&nbsp;";

					 if(rs.getString("cgnumber")!= null)
						 cgnum=rs.getString("cgnumber");
					 else
						 cgnum="&nbsp;";

					 if(rs.getString("voucherNumber")!= null)
						 voucherNumber=rs.getString("voucherNumber");
					 else
						 voucherNumber="&nbsp;";

					 if(rs.getString("type")!= null)
						 voucherType=rs.getString("type");
					 else
						 voucherType="&nbsp;";

					 if(rs.getString("fundId")!= null)
						 fundId=rs.getString("fundId");
					 else
						 fundId="&nbsp;";
					 if(rs.getString("chequeNumber")!= null)
						 chequeNumber=rs.getString("chequeNumber");
					 else
						 chequeNumber="&nbsp;";
					 if(rs.getString("chequeDate")!= null)
					 {
					    dt = format.parse(rs.getString("chequeDate"));
						chequeDate = formatter.format(dt);
				 	 }
					 else
						 chequeDate="&nbsp;";
					 if(rs.getString("amount")!= null)
						 amount=rs.getString("amount");
					 else
						 amount="&nbsp;";
					 if(rs.getString("bank")!= null)
						 bankName=rs.getString("bank");
					 else
						 bankName="&nbsp;";
					 if(rs.getString("accIdParam")!= null)
						 accIdParam=rs.getString("accIdParam");
					 else
						 accIdParam="&nbsp;";
					 if(rs.getString("recChequeDate")!= null){
						 dt = format.parse(rs.getString("recChequeDate"));
						 recChequeDate = formatter.format(dt);
				 	 }else
						 recChequeDate="&nbsp;";
					 if(rs.getString("payTo")!= null)
						 payeeName=rs.getString("payTo");
					 else
						 payeeName="&nbsp;";
					 if(rs.getString("status")!= null)
						 status=rs.getString("status");
					 else                  
						 status="&nbsp;";
					// insMode
					data.put("voucherHeaderId",voucherHeaderId );
					data.put("payinSlipVHeaderId",payinSlipVHeaderId );
					data.put("cgnum", cgnum);
					data.put("insMode", intrumentMode);
					data.put("voucherNumber",voucherNumber );
					data.put("bankRefNumber",bankReferenceNo);
					data.put("voucherType",voucherType);
					data.put("fundId",fundId);
					data.put("chequeNumber", chequeNumber);
					data.put("chequeDate",chequeDate);
					data.put("amount", amount);
					data.put("bankName",bankName );
					data.put("accIdParam",accIdParam );
					data.put("recChequeDate", recChequeDate);
					data.put("payeeName", payeeName);
					data.put("status", status);
					data.put("serialNo", i+"");
					i++;

					arList.add(data);

				}//while
				}
				catch(SQLException sqlE){
					LOGGER.error("Exception in main "+sqlE);
					throw taskExc;
				}
				finally
				{
					statement.close();
					rs.close();
				}
				return arList;
	}//main method for getting Dishonored cheque details

	/**
	 * This function executes the Query-get the Bank charges and bank ref no for the dishonored cheques.

	 */

	private void getBankEntryDetails() throws Exception{
		try{
				StringBuffer basicquery1 = new StringBuffer("SELECT rvh.id AS \"voucherHeaderId\",rvh.cgn AS \"cgnumber\",rvh.VOUCHERNUMBER AS \"voucherNumber\","
				+" rvh.TYPE AS \"vouType\",rvh.FUNDID AS \"fundId\",rvh.ORIGINALVCID AS \"oldVhId\","
				+" be.REFNO AS \"bankRefNumber\",be.TXNDATE AS \"bankRefDate\",be.TXNAMOUNT AS \"bankChargeAmt\" "
				+" FROM VOUCHERHEADER rvh,bankentries be");
				StringBuffer wherequery1 = new StringBuffer(" WHERE rvh.NAME='Bank Entry' AND rvh.ID=be.VOUCHERHEADERID "
				+" AND rvh.ORIGINALVCID!=0  AND rvh.TYPE='Payment' ");
				StringBuffer orderbyquery = new StringBuffer(" ORDER BY \"oldVhId\" ");

				if (fundId !=null && fundId !=0){
					wherequery1 = wherequery1.append(" AND rvh.FUNDID=").append(fundId);
				}

				if (!chqFromDate.equals("") && !chqToDate.equals("")){
					if(LOGGER.isInfoEnabled())     LOGGER.info(" INSIDE FROM AND TO DATE ");
					wherequery1 = wherequery1.append(" AND be.txndate BETWEEN ").append("to_date('"+chqFromDate+"')").append(" and ").append("to_date('"+chqToDate+"')");
				}else{
					if(LOGGER.isInfoEnabled())     LOGGER.info(" INSIDE FROM OR TO DATE ");
					if (!chqFromDate.equals("")){
						wherequery1 = wherequery1.append(" AND be.txndate >='").append(chqFromDate).append("'");
					}
					if (!chqToDate.equals("")){
						wherequery1 = wherequery1.append(" AND be.txndate <='").append(chqToDate).append("'");
					}
				}

				String query = new StringBuffer().append(basicquery1).append(wherequery1).append(orderbyquery).toString();

				if(LOGGER.isInfoEnabled())     LOGGER.info("  getBankEntryDetails Query is  "+query);

				statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			 	rs = statement.executeQuery(query);

			 	if(LOGGER.isInfoEnabled())     LOGGER.info("After Execute Query----getBankEntryDetails");

			 	 int resSize = 0, i = 0;
				 if (rs.last())
				 resSize = rs.getRow();

				 originalVcId = new String[resSize];
				 bankRefNo = new String[resSize];
				 bankCharge = new String[resSize];

				rs.beforeFirst();

			 	while(rs.next())
			 	{
 					originalVcId[i] = rs.getString("oldVhId");
 					bankRefNo[i] = rs.getString("bankRefNumber");
					bankCharge[i] = cf.numberToString(rs.getString("bankChargeAmt")).toString();
					i += 1;
			 	}
			 }
			catch(SQLException sqlE){
				LOGGER.error("Exception in main "+sqlE);
				throw taskExc;
			}
	}
}