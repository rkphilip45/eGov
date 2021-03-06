/*this file generates returns Linked list of Yearly Bill register
 * Created on Jul 29, 2006
 * @author Chiranjeevi
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;


import com.exilant.exility.common.TaskFailedException;

public class RptBudgetList {
	Connection connection = null;
	PreparedStatement pstmt = null;
	ResultSet resultset = null;
	ResultSet resultset1 = null;
	Date sDate;
	Date eDate;
	int totalCount = 0;
	TaskFailedException taskExc;
	ResultSet rsset = null;

	String arr[] = new String[9];
	ArrayList data = new ArrayList();
	LinkedList dataList = new LinkedList();

	private final String errConnOpenString = "Unable to get a connection from Pool Please make sure that the connection pool is set up properly";
	private static final Logger LOGGER = Logger.getLogger(RptBudgetList.class);

	public RptBudgetList() {
	}

	// this function add the data corresponding to setted financial year and
	// contractor/Supplier type to datalist
	public LinkedList getRptBudgetList(RptBudgetBean reportBean)
			throws TaskFailedException {
		// GET CONNECTION
		try {
			connection = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
		} catch (Exception exception) {
			LOGGER.error("Inside getRptBudgetList" + exception.getMessage(),
					exception);
			throw new TaskFailedException();
		}

		int finId;
		finId = reportBean.getFinYear();
		int AccType = reportBean.getAccType();

		/*
		 * Setting to bean set previous, current and next year to bean -1
		 * previous year 0 current year 1 next year
		 */

		reportBean.setPreviousYear(getYear1(finId, -1));
		reportBean.setCurrentYear(getYear1(finId, 0));
		reportBean.setNextYear(getYear1(finId, 1));
		/* ends setting to bean */

		if(LOGGER.isInfoEnabled())     LOGGER.info("inside JAVA");

		if(LOGGER.isInfoEnabled())     LOGGER.info("value of finId:" + finId);
		String query = getQuery(finId, AccType);
		if(LOGGER.isInfoEnabled())     LOGGER.info("data Query : " + query);

		try {
			pstmt = connection.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt.setInt(1, finId);
			pstmt.setInt(2, AccType);
			resultset = pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp=" + e.getMessage(), e);
		}

		try {
			int slno = 1;
			String groupid, funId, prevFunId, groupname, funname, funCode, code, funParName;
			// if(LOGGER.isInfoEnabled())     LOGGER.info("00000000");
			int arrIndex = 0;

			// get financial period and financialyear first part eg: 2006 from
			// 2006-07
			String fiscalPeriodId = getFiscalPeriodId(finId);
			// if(LOGGER.isInfoEnabled())     LOGGER.info("getFiscalPeriodId(finId) : "+fiscalPeriodId);
			// String yr=getYear(finId).substring(0,getYear(finId).indexOf("-"))
			// ;
			String yr = getYear(finId);// .substring(0,getYear(finId).indexOf("-"))
										// ;
			if(LOGGER.isInfoEnabled())     LOGGER.info(" fin year first part: " + yr);
			ArrayList<String> al = new ArrayList<String>();
			while (resultset.next()) {
				// if(LOGGER.isInfoEnabled())     LOGGER.info("@@@@@@@@"+totalCount);
				totalCount += 1;

				// initializing array elements to empty
				arrIndex = 0;
				for (int i = 0; i < arr.length; i++)
					arr[i] = "";

				// String
				// particulars,code,actPrevYr,budgetCurYr,actUptoDec,revisedBudgetCurYr,budgetNextYr;

				funId = resultset.getString("funId");
				funname = resultset.getString("funname");
				funParName = resultset.getString("funParName");
				funCode = resultset.getString("fCode");
				groupid = resultset.getString("groupid");
				groupname = resultset.getString("groupname");
				code = resultset.getString("coaCodeId");

				String subcode[] = new String[2];
				// getting glcode of type 111
				if (code.contains("--")) { // if(LOGGER.isInfoEnabled())     LOGGER.info("inside if subcode");
					subcode = code.split("--");
				} else {
					subcode[0] = code;
					subcode[1] = "";
				}

				al.add(funId);
				al.add(funParName);
				al.add(funname);
				al.add(funCode);
				al.add(groupid);
				al.add(groupname);
				al.add(subcode[0]);
				al.add(subcode[1]);

				// if(LOGGER.isInfoEnabled())     LOGGER.info("inside END MAIN while datafilllist : TOTAL COUNT: "+
				// totalCount);
			}
			resultset.close();
			pstmt.close();

			if (al.size() > 0) {
				// adding column numbers for linked list
				RptBudgetBean reportBean0 = new RptBudgetBean();
				reportBean0.setSlno("<center><B>1</B></center>");
				reportBean0.setParticulars("<center><B>2</B></center>");
				reportBean0.setCode("<center><B>3</B></center>");
				reportBean0.setActPrevYr("<center><B>4</B></center>");
				reportBean0.setBudgetCurYr("<center><B>5</B></center>");
				reportBean0.setActUptoDec("<center><B>6</B></center>");
				reportBean0.setRevisedBudgetCurYr("<center><B>7</B></center>");
				reportBean0.setBudgetNextYr("<center><B>8</B></center>");
				dataList.add(reportBean0);

				fillData(al, finId, fiscalPeriodId, yr, AccType);
			}

		} catch (SQLException ex) {
			LOGGER.error("ERROR: " + ex.toString(), ex);
			throw new TaskFailedException();
		} finally {
			try {
				//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(connection, pstmt);
			} catch (Exception efinally) {
				//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(connection, pstmt);
				LOGGER
						.error("Exception in Finally Block " + efinally,
								efinally);
			}
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("returning from java file");
		return dataList;
	}

	// this function create the query string depending on code and type of
	// Contractor/Supplier
	String getQuery(int finId, int type) {
		return " select unique fun.id as \"funid\", fun.name as \"funname\",DECODE(fun.PARENTID,NULL,' ',(select name from function where id=fun.PARENTID)||'-')  AS \"funParName\","
				+ " fun.CODE as \"fCode\", bg.id  as \"groupid\", bg.NAME  as \"groupname\",DECODE(bg.MAJORCODE,null,bg.MINCODE ||'--' ||bg.MAXCODE,bg.MAJORCODE)AS \"coaCodeId\" "
				+ " FROM function fun,FUNCTION fun1,EGF_BUDGET b,EGF_BUDGETGROUP BG,EGF_BUDGETDETAIL bd, financialyear fy "
				+ " where bd.FUNCTIONID=fun.id And  fun.ISACTIVE=1 AND"
				+ " fy.ID=b.FINANCIALYEARID and b.ID=bd.BUDGETID  and"
				+ " bg.ID=bd.BUDGETGROUPID   AND fy.id= ?"
				+ " and bg.acctype= ?" + " order by \"funid\" ";
	}

	// String[] fillData(String code,int finId,int groupid,int funId,String
	// fiscalPeriodId,String yr)
	void fillData(ArrayList<String> al, int finId, String fiscalPeriodId,
			String yr, int AccType) {

		String str[] = new String[al.size()];
		str = al.toArray(str);
		String query = "", prevYr, nextYr, betweenCodesCondition = "";
		int slno = 1, groupid, funId, prevFunId = 0, mincodeId, maxcodeId;
		String groupname, funname, mincode, maxcode, funCode, funParName;
		if(LOGGER.isInfoEnabled())     LOGGER.info("ArrayList al.size(): " + al.size());

		prevYr = Integer.toString(Integer.parseInt(yr) - 1);
		nextYr = Integer.toString(Integer.parseInt(yr) + 1);

		BigDecimal temp, temp1, actPrevYrTotal, budgetCurYrTotal, actUptoDecTotal, revisedBudgetCurYrTotal, budgetNextYrTotal;
		temp = new BigDecimal(0);
		temp1 = new BigDecimal(0);
		actPrevYrTotal = new BigDecimal(0);
		budgetCurYrTotal = new BigDecimal(0);
		actUptoDecTotal = new BigDecimal(0);
		revisedBudgetCurYrTotal = new BigDecimal(0);
		budgetNextYrTotal = new BigDecimal(0);
		try {

			for (int i = 0; al.size() > 0 && i < al.size();) {
				funId = Integer.parseInt(str[i++]);
				funParName = str[i++];
				funname = str[i++];
				funCode = str[i++];
				groupid = Integer.parseInt(str[i++]);
				groupname = str[i++];
				mincode = str[i++];
				maxcode = str[i++];
				String particulars, actPrevYr, budgetCurYr, actUptoDec, revisedBudgetCurYr, budgetNextYr;

				// ADDING FUNCTION TO THE LINKED LIST
				if (funId != prevFunId) {
					RptBudgetBean reportBean1 = new RptBudgetBean();
					reportBean1
							.setSlno("<B>" + Integer.toString(slno) + "</B>");
					reportBean1.setParticulars("<B>" + "Function: "
							+ funParName + funname + "(" + funCode + ")"
							+ "</B>");
					reportBean1.setCode("&nbsp;");
					reportBean1.setActPrevYr("&nbsp;");
					reportBean1.setBudgetCurYr("&nbsp;");
					reportBean1.setActUptoDec("&nbsp;");
					reportBean1.setRevisedBudgetCurYr("&nbsp;");
					reportBean1.setBudgetNextYr("&nbsp;");
					dataList.add(reportBean1);
					slno++;
					prevFunId = funId;
				}

				String amt = "";
				switch (AccType) {
				case 1:
					amt = "sum(gl.creditAmount)-sum(gl.debitAmount)";
					break; // 80
				case 2:
					amt = "sum(gl.debitAmount)-sum(gl.creditAmount)";
					break;// 81
				case 3:
					amt = "sum(gl.creditAmount)";
					break;// 82
				case 4:
					amt = "sum(gl.debitAmount)";
					break;// 83
				case 5:
					amt = "sum(gl.debitAmount)-sum(gl.creditAmount)";
					break;// 84
				case 6:
					amt = "sum(gl.debitAmount)";
					break;// 85

				}

				/*
				 * query=" select distinct ca.name as \"name\", ca.glcode as \"glcode\" , "
				 * +"( select Decode("+amt+",null,0,"+amt+
				 * ") as \"amt\" from generalLedger gl,voucherHeader vh where gl.voucherHeaderId=vh.id and vh.fiscalPeriodId in ("
				 * + fiscalPeriodId +") and  gl.functionId="+funId
				 * +" and gl.glcode like ca.glcode ||'%' ) as \"amt0\", " +
				 * " (select decode(sum(bd.originalAmt),null,0,sum(bd.originalAmt)) as \"amt\" from egf_budgetDetail bd,egf_budget b where bd.budgetid= b.id and bd.budgetGroupid="
				 * + groupid+ " and b.financialyearid=" +finId +
				 * ") as \"amt1\", " +" (select Decode("+amt+",null,0,"+amt+
				 * ") as \"amt\" from generalLedger gl,voucherHeader vh where gl.voucherHeaderId=vh.id and vh.voucherDate >='01-apr-"
				 * + yr+"' and vh.voucherDate <='31-dec-"+ yr
				 * +"' and gl.functionId="+
				 * funId+" and gl.glcode like ca.glcode ||'%') as \"amt2\", " +
				 * " (select decode(sum(bd.originalAmt+bd.revisedAmt),null,0,sum(bd.originalAmt+bd.revisedAmt)) as \"amt\" from egf_BudgetDetail bd,egf_Budget b where bd.budgetid= b.id and bd.add_Less = 0 and bd.budgetGroupid="
				 * +groupid+
				 * " and  bd.functionid="+funId+" and b.financialYearid="+finId
				 * +") as \"amt3\", "+
				 * " (select decode(sum(bd.originalAmt+bd.revisedAmt),null,0,sum(bd.originalAmt+bd.revisedAmt)) as \"amt\" from egf_BudgetDetail bd,egf_Budget b where bd.budgetid= b.id and bd.add_Less = 0 and bd.budgetGroupid="
				 * +groupid+
				 * " and  bd.functionid="+funId+" and b.financialYearid="+finId
				 * +") as \"amt4\" "+
				 * " FROM chartofaccounts ca WHERE ca.glcode LIKE '"
				 * +mincode+"'";
				 * 
				 * 
				 * query=
				 * " select distinct ca.name as \"name\", ca.glcode as \"glcode\" , "
				 * +"( select Decode("+amt+",null,0,"+amt+
				 * ") as \"amt\" from generalLedger gl,voucherHeader vh where gl.voucherHeaderId=vh.id and vh.fiscalPeriodId in ("
				 * + fiscalPeriodId
				 * +") and  gl.functionId=fun.id and vh.voucherDate >='01-apr-"+
				 * prevYr+"' and vh.voucherDate <='31-mar-"+
				 * yr+"' and gl.glcode like ca.glcode ||'%' ) as \"amt0\", " +
				 * //
				 * " (select decode(sum(bd.originalAmt),null,0,sum(bd.originalAmt)) as \"amt\" from egf_budgetDetail bd,egf_budget b where bd.budgetid= b.id and bd.budgetGroupid=bg.id and b.financialyearid=fy.id and vh.voucherDate >='01-apr-"
				 * + yr+"' and vh.voucherDate <='31-dec-"+
				 * nextYr+"') as \"amt1\", " +
				 * " (select decode(sum(bd.originalAmt),null,0,sum(bd.originalAmt)) as \"amt\" from egf_budgetDetail bd,egf_budget b where bd.budgetid= b.id and bd.budgetGroupid=bg.id and b.financialyearid=fy.id ) as \"amt1\", "
				 * +" (select decode("+amt+",null,0,"+amt+
				 * ") as \"amt\" from generalLedger gl,voucherHeader vh where gl.voucherHeaderId=vh.id and vh.voucherDate >='01-apr-"
				 * + yr+"' and vh.voucherDate <='31-dec-"+yr+
				 * "' and gl.functionId=fun.id and gl.glcode like ca.glcode ||'%') as \"amt2\", "
				 * +
				 * " (select decode(sum(bd.originalAmt+bd.revisedAmt),null,0,sum(bd.originalAmt+bd.revisedAmt)) as \"amt\" from egf_BudgetDetail bd,egf_Budget b where bd.budgetid= b.id and bd.add_Less = 0 and bd.budgetGroupid=bg.id and  bd.functionid=fun.id and b.financialYearid=fy.id) as \"amt3\", "
				 * +
				 * " (select decode(sum(bd.originalAmt+bd.revisedAmt),null,0,sum(bd.originalAmt+bd.revisedAmt)) as \"amt\" from egf_BudgetDetail bd,egf_Budget b where bd.budgetid= b.id and bd.add_Less = 0 and bd.budgetGroupid=bg.id and  bd.functionid=fun.id and b.financialYearid=fy.id) as \"amt4\" "
				 * +
				 * "FROM chartofaccounts ca ,FUNCTION fun,EGF_BUDGETGROUP  bg,financialyear fy WHERE ca.glcode LIKE '"
				 * +
				 * mincode+"' AND bg.id="+groupid+" AND fy.id="+finId+" AND fun.id="
				 * +funId;
				 */
				String nameCondition;
				mincodeId = Integer.parseInt(mincode);
				maxcodeId = Integer.parseInt(maxcode);
				// if(LOGGER.isInfoEnabled())     LOGGER.info("mincodeId: "+mincode+"  maxcodeId: "+maxcode);
				if (maxcode != "") {
					maxcodeId = Integer.parseInt(maxcode);
					betweenCodesCondition = " and gl.glcode between (SELECT glcode FROM chartofaccounts WHERE id="
							+ mincodeId
							+ ") ||'%' and (SELECT glcode FROM chartofaccounts WHERE id="
							+ maxcodeId + ") ||'%' ";
					nameCondition = " ca.glcode=SUBSTR((SELECT glcode FROM chartofaccounts WHERE id ="
							+ mincodeId + "),1,3)";// get glcode name of
													// minorcode
				} else {
					betweenCodesCondition = " and gl.glcode like (SELECT glcode FROM chartofaccounts WHERE id="
							+ mincodeId + ") ||'%'";
					nameCondition = " ca.glcode=SUBSTR((SELECT glcode FROM chartofaccounts WHERE id ="
							+ mincodeId + "),1,4)"; // get glcode name of
													// subminor
				}

				query = " select distinct ca.name as \"name\", ca.glcode as \"glcode\" , "
						+ "( select Decode("
						+ amt
						+ ",null,0,"
						+ amt
						+ ") as \"amt\" from generalLedger gl,voucherHeader vh where gl.voucherHeaderId=vh.id and vh.fiscalPeriodId in ("
						+ fiscalPeriodId
						+ ") and  gl.functionId=fun.id and vh.voucherDate >='01-apr-"
						+ prevYr
						+ "' and vh.voucherDate <='31-mar-"
						+ yr
						+ "'"
						+ betweenCodesCondition
						+ ") as \"amt0\", "
						+ " (select decode(sum(bd.originalAmt),null,0,sum(bd.originalAmt)) as \"amt\" from egf_budgetDetail bd,egf_budget b where bd.budgetid= b.id and bd.budgetGroupid=bg.id and b.financialyearid=fy.id and bd.functionid ="
						+ funId
						+ " ) as \"amt1\", "
						+ " (select decode("
						+ amt
						+ ",null,0,"
						+ amt
						+ ") as \"amt\" from generalLedger gl,voucherHeader vh where gl.voucherHeaderId=vh.id and vh.voucherDate >='01-apr-"
						+ yr
						+ "' and vh.voucherDate <='31-dec-"
						+ yr
						+ "' and gl.functionId=fun.id "
						+ betweenCodesCondition
						+ ") as \"amt2\", "
						+ " (select sum(bd.originalAmt+bd.revisedAmt) as \"amt\" from egf_BudgetDetail bd,egf_Budget b where bd.budgetid= b.id and bd.add_Less = 0 and bd.budgetGroupid=bg.id and  bd.functionid=fun.id and b.financialYearid=fy.id and bd.functionid ="
						+ funId
						+ " AND fy.id="
						+ finId
						+ " ) as \"amt3\", "
						+ " (select sum(bd.originalAmt-bd.revisedAmt) as \"amt\" from egf_BudgetDetail bd,egf_Budget b where bd.budgetid= b.id and bd.add_Less = 1 and bd.budgetGroupid=bg.id and  bd.functionid=fun.id and b.financialYearid=fy.id and bd.functionid ="
						+ funId
						+ " AND fy.id="
						+ finId
						+ " ) as \"amt5\", "
						+ " (select sum(bd.originalAmt) as \"amt\" from egf_BudgetDetail bd,egf_Budget b where bd.budgetid= b.id  and bd.budgetGroupid=bg.id and  bd.functionid=fun.id and b.financialYearid=fy.id and bd.functionid ="
						+ funId
						+ " AND fy.id="
						+ finId
						+ " ) as \"amt4\" "
						+ "FROM chartofaccounts ca ,FUNCTION fun,EGF_BUDGETGROUP  bg,financialyear fy WHERE "
						+ nameCondition
						+ " AND bg.id="
						+ groupid
						+ " AND fy.id="
						+ finId
						+ " AND fun.isactive=1 AND fun.id=" + funId;

				int j = 1;
				pstmt = connection.prepareStatement(query);
				pstmt.setString(j++, amt);
				pstmt.setString(j++, amt);
				pstmt.setString(j++, fiscalPeriodId);
				if (maxcode != "") {
					pstmt.setInt(j++, mincodeId);
					pstmt.setInt(j++, maxcodeId);
				} else
					pstmt.setInt(j++, mincodeId);
				pstmt.setInt(j++, funId);
				pstmt.setString(j++, amt);
				pstmt.setString(j++, amt);
				if (maxcode != "") {
					pstmt.setInt(j++, mincodeId);
					pstmt.setInt(j++, maxcodeId);
				} else
					pstmt.setInt(j++, mincodeId);
				pstmt.setInt(j++, funId);
				pstmt.setInt(j++, finId);
				pstmt.setInt(j++, funId);
				pstmt.setInt(j++, finId);
				pstmt.setInt(j++, funId);
				pstmt.setInt(j++, finId);
				pstmt.setInt(j++, mincodeId);
				pstmt.setInt(j++, groupid);
				pstmt.setInt(j++, finId);
				pstmt.setInt(j++, funId);
				rsset = pstmt.executeQuery();

				if (rsset.next()) {
					RptBudgetBean reportBean1 = new RptBudgetBean();
					reportBean1.setSlno("&nbsp;");
					reportBean1.setParticulars(rsset.getString("name"));
					reportBean1.setCode(groupname); // if(LOGGER.isInfoEnabled())     LOGGER.info("3333");

					temp = rsset.getBigDecimal("amt0");
					reportBean1.setActPrevYr(temp.toString() + ".00");
					// if(LOGGER.isInfoEnabled())     LOGGER.info((actPrevYrTotal+Double.parseDouble(temp)));
					actPrevYrTotal = actPrevYrTotal.add(temp);

					temp = rsset.getBigDecimal("amt1");
					reportBean1.setBudgetCurYr(temp.toString() + ".00");
					budgetCurYrTotal = budgetCurYrTotal.add(temp);
					// if(LOGGER.isInfoEnabled())     LOGGER.info(budgetCurYrTotal +
					// BigDecimal.parseDouble(temp))

					temp = rsset.getBigDecimal("amt2");
					reportBean1.setActUptoDec(temp.toString() + ".00");
					actUptoDecTotal = actUptoDecTotal.add(temp);

					temp = rsset.getBigDecimal("amt3");
					temp1 = rsset.getBigDecimal("amt5");
					if (temp == null && temp1 == null) {
						// revisedBudgetCurYrTotal=revisedBudgetCurYrTotal.add(new
						// BigDecimal(0));
						reportBean1
								.setRevisedBudgetCurYr(revisedBudgetCurYrTotal
										.toString()
										+ ".00");
					} else if (temp == null) {
						revisedBudgetCurYrTotal = revisedBudgetCurYrTotal
								.add(temp1);
						reportBean1
								.setRevisedBudgetCurYr(revisedBudgetCurYrTotal
										.toString()
										+ ".00");
					} else if (temp1 == null) {
						revisedBudgetCurYrTotal = revisedBudgetCurYrTotal
								.add(temp);
						reportBean1
								.setRevisedBudgetCurYr(revisedBudgetCurYrTotal
										.toString()
										+ ".00");
					}
					// reportBean1.setRevisedBudgetCurYr(temp.toString()+".00");
					// revisedBudgetCurYrTotal =
					// revisedBudgetCurYrTotal.add(temp);

					temp = rsset.getBigDecimal("amt4");
					reportBean1.setBudgetNextYr(temp.toString() + ".00"); // if(LOGGER.isInfoEnabled())     LOGGER.info("4444");
					budgetNextYrTotal = budgetNextYrTotal.add(temp);
					dataList.add(reportBean1);

				}
				rsset.close();
				pstmt.close();
				// if(LOGGER.isInfoEnabled())     LOGGER.info("after fillData Query ["+i+"]: "+query);
			}// if(LOGGER.isInfoEnabled())     LOGGER.info("daaaata: "+query);

			RptBudgetBean reportBean2 = new RptBudgetBean();
			if(LOGGER.isInfoEnabled())     LOGGER.info("222");
			reportBean2.setSlno("&nbsp;");
			reportBean2.setParticulars("<RIGHT><B>Total:</B></RIGHT>");
			reportBean2.setCode("&nbsp;");
			reportBean2.setActPrevYr("<B>" + actPrevYrTotal.toString() + ".00"
					+ "</B>");
			reportBean2.setBudgetCurYr("<B>" + budgetCurYrTotal.toString()
					+ ".00" + "</B>");
			reportBean2.setActUptoDec("<B>" + actUptoDecTotal.toString()
					+ ".00" + "</B>");
			reportBean2.setRevisedBudgetCurYr("<B>"
					+ revisedBudgetCurYrTotal.toString() + ".00" + "</B>");
			reportBean2.setBudgetNextYr("<B>" + budgetNextYrTotal.toString()
					+ ".00" + "</B>");
			dataList.add(reportBean2);
			// if(LOGGER.isInfoEnabled())     LOGGER.info("fillData Query5 :str[4]= "+query+"-"+str[4]);
		} catch (Exception e) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("some exception:  " + e, e);
		}
	}

	// to get fiscal period
	String getFiscalPeriodId(int finId) throws SQLException {

		String period = "";
		PreparedStatement pstmt1 = null;
		ResultSet rsset1 = null;
		String query;
		try {
			query = " select id as \"id\" from FISCALPERIOD where financialyearid=?";
			pstmt1 = connection.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt1.setInt(1, finId);
			rsset1 = pstmt1.executeQuery();
			int i = 0;
			while (rsset1.next()) {
				if (i == 0)
					period = "" + rsset1.getString("id");
				else
					period = period + "," + rsset1.getString("id");
				i++;
			}
			rsset1.close();
			pstmt1.close();
			if(LOGGER.isInfoEnabled())     LOGGER.info("getFiscalPeriodId Query : " + query);
		} catch (Exception e) {
			LOGGER.error("Inside getFiscalPeriodId" + e.getMessage(), e);
			rsset1.close();
			pstmt1.close();
		}

		return period;
	}

	String getYear(int finId) throws SQLException {
		String yr = "";
		PreparedStatement pstmt1 = null;
		ResultSet rsset1 = null;
		String query;
		try {
			query = "select  TO_CHAR(STARTINGDATE,'DD-Mon-YYYY') as \"yr\" from financialyear where isactive=1 and id=?";
			pstmt = connection.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt1.setInt(1, finId);
			rsset1 = pstmt1.executeQuery();
			while (rsset1.next()) {
				yr = rsset1.getString("yr");
				// if(LOGGER.isInfoEnabled())     LOGGER.info("yr : "+yr+"::"+yr.length());
				yr = yr.substring(yr.length() - 4, yr.length());
			}
			rsset1.close();
			pstmt1.close();
			if(LOGGER.isInfoEnabled())     LOGGER.info("getYear Query : " + query);
		} catch (Exception e) {
			LOGGER.error("yr" + e, e);
			rsset1.close();
			pstmt1.close();
		}
		return yr;
	}

	String getYear1(int CurrentYearId, int whichYear) {
		String yr = "";
		PreparedStatement pstmt1 = null;
		ResultSet rsset1 = null;
		String query = null;
		try {
			if (whichYear == -1)// previous year
				query = " select financialyear from financialyear where startingdate=(select add_months(startingdate,-12) from financialyear where id=?)";
			else if (whichYear == 0) // current year
				query = " select financialyear from financialyear where  id=?";
			else if (whichYear == 1) // next year
				query = " select financialyear from financialyear where startingdate=(select add_months(startingdate,12) from financialyear where id=?)";
			pstmt1 = connection.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt1.setInt(1, CurrentYearId);
			rsset1 = pstmt1.executeQuery();
			if (rsset1.next()) {
				yr = rsset1.getString(1);
			}
			rsset1.close();
			pstmt1.close();
			if(LOGGER.isInfoEnabled())     LOGGER.info("getYear Query : " + query);
		} catch (Exception e) {
			LOGGER.error("yr" + e, e);
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info(whichYear + " year:" + yr);
		return yr;
	}

}
