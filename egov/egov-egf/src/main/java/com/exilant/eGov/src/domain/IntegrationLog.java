/*
 * Created on May 8, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Tilak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IntegrationLog
{
	   private String id = null;
	   private String recordId =null;
	   private String vouchernumber =null;
	   private String message =null;
	   private String issuccessful =null;
	   private String transactiondate = "";
	   private String url = null;
	   private String userId = null;
	   private String status ="";
	   private static final Logger LOGGER=Logger.getLogger(IntegrationLog.class);
	   private static TaskFailedException taskExc;
	   public IntegrationLog() {}

	   public void setId(String id) {this.id = id; }
	   public void setRecordId(String recordId){ this.recordId=recordId;}
	   public void setVouchernumber(String vouchernumber){ this.vouchernumber=vouchernumber;}
	   public void setMessage(String message){ this.message=message;}
	   public void setIssuccessful(String issuccessful) { this.issuccessful=issuccessful; }
	   public void setTransactiondate(String transactiondate) { this.transactiondate=transactiondate; }
	   public void setUrl(String url) { this.url=url; }
	   public void setUserId(String userId) { this.userId=userId; }
	   public void setStatus(String status) { this.status=status; }


	   EGovernCommon commonmethods = new EGovernCommon();
	   public void insert(Connection connection) throws SQLException,TaskFailedException
	   {
	   		transactiondate = commonmethods.getCurrentDateTime(connection);
	   		try{
		   		SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
				transactiondate = formatter.format(sdf.parse( transactiondate ));
		   		setTransactiondate(transactiondate);
	   		}catch(Exception e){
				LOGGER.error("Exp in insert to intergationlog"+e.getMessage(),e);
				throw taskExc;
			}
	   		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("IntegrationLog")) );

	   		String insertQuery = "INSERT INTO IntegrationLog (Id, recordId, vouchernumber, message, issuccessful, transactiondate, url, userId, status) "+
			" values (?,?,?,?,?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'),?,?,?)";

	   		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
	   		PreparedStatement pstmt=null;
	   		pstmt = connection.prepareStatement(insertQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	   		pstmt.setString(1,id);
	   		pstmt.setString(2,recordId);
	   		pstmt.setString(3,vouchernumber);
	   		pstmt.setString(4,message);
	   		pstmt.setString(5,issuccessful);
	   		pstmt.setString(6,transactiondate);
	   		pstmt.setString(7,url);
	   		pstmt.setString(8,userId);
	   		pstmt.setString(9,status);
	   		
			pstmt.executeUpdate();
			pstmt.close();
	   }



}
