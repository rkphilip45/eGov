/**
 * Created on Aug 08, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class IESchedules extends AbstractTask {
	private static final Logger LOGGER = Logger.getLogger(IESchedules.class);
	Connection conn;
	NumberFormat formatter;

	/* this method is called by Exility */
	public void execute(String taskName, String gridName, DataCollection dc,
			Connection conn, boolean erroOrNoData,
			boolean gridHasColumnHeading, String prefix)
			throws TaskFailedException {
		// if(LOGGER.isDebugEnabled())     LOGGER.debug("****************IESchedules");
		this.conn = conn;
		formatter = new DecimalFormat();
		formatter = new DecimalFormat("###############.00");

		printSchedules(dc);

		// dc.addMessage("eGovSuccess","IESchedules");
	}

	private void printSchedules(DataCollection dc) throws TaskFailedException {
		String tableTime = dc.getValue("tableToDrop");
		String mainTable = "coaie" + tableTime;
		String report = "SELECT scheduleglCode AS \"glcode\", decode(operation,'L','Less: ',' ') || schedulename AS \"name\", 'Schedule ' || schschedule || ': ' || summaryname || '[Code No ' || summaryglcode || ']' AS \"schTitle\", DECODE(schschedule,NULL,'-',schschedule) AS \"schedule\", curYearAmount AS \"curyearamount\", preyearamount AS \"preyearamount\", operation AS \"operation\", TYPE AS \"type\" FROM "
				+ mainTable
				+ " WHERE TYPE = 'I' OR TYPE = 'E' ORDER BY scheduleglCode, TYPE, operation";
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList ar = new ArrayList();
		double balance = 0, curAmt = 0, preAmt = 0, sumCur = 0, sumPre = 0;
		String glCode = "", operation = "", schedule = "", preSchedule = "", title = "", schTitle[] = new String[20];
		int grids = 0, rowCount = 0, cnt = 0, rows[] = new int[20];

		String sDate = dc.getValue("sDate") == null ? "start of FY" : dc
				.getValue("sDate");
		String eDate = dc.getValue("eDate") == null ? "today" : dc
				.getValue("eDate");
		dc.addValue("pageTitle",
				"Income & Expenditure Schedules For the period of " + sDate
						+ " to " + eDate);

		try {
			pst = conn.prepareStatement(report);
			rs = pst.executeQuery();

			while (rs.next()) {
				curAmt = rs.getDouble("curyearamount");
				preAmt = rs.getDouble("preyearamount");
				schedule = rs.getString("schedule");
				operation = rs.getString("operation");

				if (!preSchedule.equalsIgnoreCase(schedule))
					grids++;
				if (preSchedule.equalsIgnoreCase(""))
					preSchedule = schedule;
				if (!preSchedule.equalsIgnoreCase(schedule)) {
					schTitle[cnt] = title;
					// if we dont have any records for this schedule we should
					// not show on screen
					if (sumCur != 0 || sumPre != 0) {
						dc.addValue("showRowIESchedule" + (cnt + 1), "true");
					} else {
						dc.addValue("showRowIESchedule" + (cnt + 1), "false");
						schTitle[cnt] += " -No Data";
					}
					String total[] = { "-", "Total", formatter.format(sumCur),
							formatter.format(sumPre) };
					ar.add(total);
					rowCount++;

					rows[cnt++] = rowCount;
					rowCount = 0;
					sumCur = sumPre = 0;
				}
				rowCount++;
				title = rs.getString("schTitle");

				if (operation.equalsIgnoreCase("L")
						&& preSchedule.equalsIgnoreCase(schedule)) {
					String total[] = { "-", "Sub Total",
							formatter.format(sumCur), formatter.format(sumPre) };
					ar.add(total);
					rowCount++;
				}
				if (operation.equalsIgnoreCase("L")) {
					sumCur = sumCur - curAmt;
					sumPre = sumPre - preAmt;
				} else {
					sumCur = sumCur + curAmt;
					sumPre = sumPre + preAmt;
				}
				String row[] = { rs.getString("glcode"), rs.getString("name"),
						formatter.format(curAmt), formatter.format(preAmt) };
				ar.add(row);
				preSchedule = schedule;
			}
			// rows[cnt] = rowCount;
			// if(LOGGER.isDebugEnabled())     LOGGER.debug("Out One rowcounnt: "+cnt+"-"+rowCount+" Preschedule:"+preSchedule+" schedule:"+schedule);

		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage(),ex);
			// dc.addMessage("eGovFailure","IESchedules->printSchedules: "+ex.toString());
			throw new TaskFailedException();
		}

		// //if(LOGGER.isDebugEnabled())     LOGGER.debug("******************grids-"+grids);

		String grid[][][] = new String[grids][][];
		int nextRow = 0;
		for (int gridNo = 0; gridNo < grids - 1; gridNo++) {
			String gridData[][] = new String[rows[gridNo] + 1][4];
			gridData[0][0] = "glCode";
			gridData[0][1] = "name";
			gridData[0][2] = "amountCurYear";
			gridData[0][3] = "amountPreYear";
			for (int rowNo = 0; rowNo < rows[gridNo]; rowNo++) {
				gridData[rowNo + 1] = (String[]) ar.get(nextRow++);
				// if(LOGGER.isDebugEnabled())     LOGGER.debug(gridData[rowNo][0] + " - " + gridData[rowNo][1]
				// + " - " + gridData[rowNo][2] + " - " + gridData[rowNo][3]);
			}
			grid[gridNo] = gridData;
		}
		for (int i = 0; i < grids - 1; i++) {
			if (grid[i] != null) {
				dc.addValue("schTitle" + (i + 1), schTitle[i]);
				dc.addGrid("gridIESchedule" + (i + 1), grid[i]);
			} else
				if(LOGGER.isDebugEnabled())     LOGGER.debug("grid is null: " + i);
		}
		/**
		 * We are commenting this part for implementing Back Buttob
		 * 
		 * @author :Elzan Dated :5/1/2006
		 */
		// dropping temporary tables
		/*
		 * try{ st.executeUpdate("drop table "+mainTable); }catch(Exception e){}
		 */
	}
}
