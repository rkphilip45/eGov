/**
 * Created on Aug 3, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.ChartOfAccts;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class SetUpReport extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(SetUpReport.class);
	Connection conn;
	public void execute(String taksName,
			String gridName,
			DataCollection dc,
			Connection conn,
			boolean erroOrNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException{

		this.conn = conn;
		String gridSummary[][] = dc.getGrid("gridSummary");
		String gridSummaryNew[][] = dc.getGrid("gridSummaryNew");
		String gridSchedules[][] = dc.getGrid("gridSchedules");
		String gridSchedulesNew[][] = dc.getGrid("gridSchedulesNew");

		if(!addGroups(dc)) throw new TaskFailedException();

		//if(!addSummaryGroup(dc)) throw new TaskFailedException();
		//if(!addScheduleGroup(dc)) throw new TaskFailedException();
		//if(!updateSummaryGroup(dc)) throw new TaskFailedException();
		//if(!updateScheduleGroup(dc)) throw new TaskFailedException();

		dc.addMessage("eGovSuccess", "SetUpReport");
	}

	private boolean addGroups(DataCollection dc) throws TaskFailedException{
		PreparedStatement pst=null;
		String updateString = "UPDATE chartOfAccounts SET schedule= ?, operation= ? " +
		"WHERE glCode LIKE ?";
		try{
			pst = conn.prepareStatement(updateString);
		}catch(SQLException ex){
			LOGGER.error("ERROR SetUpReport->addGroups: creatStatement Failed",ex);
			dc.addMessage("ERROR SetUpReport->addGroups: creatStatement Failed");
			throw new TaskFailedException();
		}
		String glCode=null, glCodeId=null, schedule=null, operation=null;
		ChartOfAccts coa = new ChartOfAccts();
		String grids[] = {"gridSummary", "gridSummaryNew", "gridSchedules", "gridSchedulesNew"};
		int gridIndex=0;
		String grid[][]=null;
		while(gridIndex < grids.length){
			grid = dc.getGrid(grids[gridIndex]);

			for(int index=0; index<grid.length; index++){
				if(grid[index][0].equalsIgnoreCase("")) continue;

				glCode = grid[index][0];
				glCodeId = getGLCodeId(dc, glCode);

				schedule = grid[index][2].equalsIgnoreCase("")?"":grid[index][2];
				//if schedule is not specified operation will be set to blank string
				//so if grid[index][2] is specified then operation grid[index][3] is set
				operation = grid[index][2].equalsIgnoreCase("")?"":grid[index][3];

				if(grids[gridIndex].equalsIgnoreCase("gridSchedules") || grids[gridIndex].equalsIgnoreCase("gridSchedulesNew")){
					try{
						pst.setString(1, schedule);
						pst.setString(2, operation);
						pst.setString(3, glCode+"%");
						pst.executeQuery();
					}catch(SQLException ex){
						LOGGER.error(ex.getMessage(), ex);
						dc.addMessage("eGovFailure", "SetUpReport->Summary Group Updation Failed");
						throw new TaskFailedException();
					}
					//if(LOGGER.isDebugEnabled())     LOGGER.debug("Executed..............");
				}else{
					coa.setId(glCodeId);
					coa.setOperation(operation);
					//if(LOGGER.isDebugEnabled())     LOGGER.debug("***operation: "+operation);
					try{
						coa.update(conn);
					}catch(SQLException ex){
						LOGGER.error(ex.getMessage(), ex);
						dc.addMessage("eGovFailure", "SetUpReport->Summary Group Updation Failed");
						throw new TaskFailedException();
					}
				}
			}
			gridIndex++;
		}
		return true;
	}

	private boolean addSummaryGroup(DataCollection dc) throws TaskFailedException{
		String glCodeId=null;
		ChartOfAccts coa = new ChartOfAccts();

		String gridSummary[][] = dc.getGrid("gridSummary");
		for(int index=0; index<gridSummary.length; index++){
			if(gridSummary[index][0].equalsIgnoreCase("")) continue;

			glCodeId = getGLCodeId(dc, gridSummary[index][0]);
			coa.setId(glCodeId);
			coa.setOperation("N");
			try{
				coa.update(conn);
			}catch(SQLException ex){
				LOGGER.error(ex.getMessage(), ex);
				dc.addMessage("eGovFailure", "SetUpReport->Summary Group Updation Failed");
				throw new TaskFailedException();
			}
		}

		String gridSummaryNew[][] = dc.getGrid("gridSummaryNew");
		for(int index=0; index<gridSummaryNew.length; index++){
			if(gridSummaryNew[index][0].equalsIgnoreCase("")) continue;

			glCodeId = getGLCodeId(dc, gridSummaryNew[index][0]);
			coa.setId(glCodeId);
			coa.setOperation("N");
			try{
				coa.update(conn);
			}catch(SQLException ex){
				LOGGER.error(ex.getMessage(), ex);
				dc.addMessage("eGovFailure", "SetUpReport->Summary Group Creation Failed");
				throw new TaskFailedException();
			}
		}

		return true;
	}
	private boolean addScheduleGroup(DataCollection dc) throws TaskFailedException{
		String glCodeId=null;
		ChartOfAccts coa = new ChartOfAccts();

		/****************** Summary Group ******************/
		String gridSummary[][] = dc.getGrid("gridSummary");
		for(int index=0; index<gridSummary.length; index++){
			if(gridSummary[index][0].equalsIgnoreCase("")) continue;

			glCodeId = getGLCodeId(dc, gridSummary[index][0]);
			coa.setId(glCodeId);
			coa.setOperation("N");
			try{
				coa.update(conn);
			}catch(SQLException ex){
				LOGGER.error(ex.getMessage(), ex);
				dc.addMessage("eGovFailure", "SetUpReport->Summary Group Updation Failed");
				throw new TaskFailedException();
			}
		}

		String gridSummaryNew[][] = dc.getGrid("gridSummaryNew");
		for(int index=0; index<gridSummaryNew.length; index++){
			if(gridSummaryNew[index][0].equalsIgnoreCase("")) continue;

			glCodeId = getGLCodeId(dc, gridSummaryNew[index][0]);
			coa.setId(glCodeId);
			coa.setOperation("N");
			try{
				coa.update(conn);
			}catch(SQLException ex){
				LOGGER.error(ex.getMessage(), ex);
				dc.addMessage("eGovFailure", "SetUpReport->Summary Group Creation Failed");
				throw new TaskFailedException();
			}
		}
		/***************************************************/


		/****************** Schedule Group ******************/
		String gridSchedules[][] = dc.getGrid("gridSchedules");
		for(int index=0; index<gridSchedules.length; index++){
			if(gridSchedules[index][0].equalsIgnoreCase("")) continue;

			glCodeId = getGLCodeId(dc, gridSchedules[index][0]);
			coa.setId(glCodeId);
			coa.setOperation(gridSchedules[index][3]);
			try{
				coa.update(conn);
			}catch(SQLException ex){
				LOGGER.error(ex.getMessage(), ex);
				dc.addMessage("eGovFailure", "SetUpReport->Summary Group Updation Failed");
				throw new TaskFailedException();
			}
		}

		String gridSchedulesNew[][] = dc.getGrid("gridSchedulesNew");
		for(int index=0; index<gridSchedulesNew.length; index++){
			if(gridSchedulesNew[index][0].equalsIgnoreCase("")) continue;

			glCodeId = getGLCodeId(dc, gridSchedulesNew[index][0]);
			coa.setId(glCodeId);
			coa.setOperation(gridSchedulesNew[index][3]);
			try{
				coa.update(conn);
			}catch(SQLException ex){
				LOGGER.error(ex.getMessage(), ex);
				dc.addMessage("eGovFailure", "SetUpReport->Summary Group Creation Failed");
				throw new TaskFailedException();
			}
		}
		/***************************************************/
		return true;
	}
	private String getGLCodeId(DataCollection dc,
									String glCode) throws TaskFailedException{
		String glCodeId="";
		try{
			String query = "select id from chartofaccounts where glCode= ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, glCode);
			ResultSet rset = pst.executeQuery();
			if(rset.next()){
				glCodeId = rset.getString(1);
			}else{
				dc.addMessage("eGovFailure", "invalid GLCode: "+glCode);
				throw new TaskFailedException();
			}
			rset.close();
			pst.close();
		}catch(Exception sqlex){
			if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR: SetUpReport->getAccountname"+sqlex.toString(),sqlex);
			dc.addMessage("eGovFailure", "SetUpReport->getAccountname"+sqlex.toString());
			throw new TaskFailedException();
		}
		return glCodeId;
	}
}
