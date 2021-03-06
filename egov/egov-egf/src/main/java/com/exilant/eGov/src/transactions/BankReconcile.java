/*
 * Created on Mar 24, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.domain.BankReconciliation;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class BankReconcile extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(BankReconcile.class);
	private TaskFailedException taskExc;

	public BankReconcile(){}

	public void execute(String taskName,
							String gridName,
							DataCollection dc,
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		taskExc = new TaskFailedException();
		String reconcileGrid[][] = (String[][])dc.getGrid("gridBankReconciliation");
		BankReconciliation br = new BankReconciliation();
		for(int i=0; i < reconcileGrid.length; i++) {
			if(reconcileGrid[i][1].equalsIgnoreCase("") || reconcileGrid[i][1] == null) continue;
			br.setId(reconcileGrid[i][0]);
			br.setIsReconciled("1");
			try
			{
				//if(LOGGER.isDebugEnabled())     LOGGER.debug("reconcileGrid[i][1]"+reconcileGrid[i][1]);
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				String vdt=(String)reconcileGrid[i][1];
				reconcileGrid[i][1]= formatter.format(sdf.parse(vdt));
				br.setReconciliationDate(reconcileGrid[i][1]);
			//	br.setReconciliationDate(ReconciliationDate[i]);
			}
			catch(Exception e){
				LOGGER.error("Exp in execute:"+e.getMessage());
				throw taskExc;
			}
			try{
				br.update(conn);
			}catch(SQLException sqlEx){
				if(LOGGER.isDebugEnabled())     LOGGER.debug("SQLEx BankReconciliation : " + sqlEx.toString());
				dc.addMessage("exilRPError","bankReconciliation Updation Failed");
				throw taskExc;
			}
		}
	}
}
