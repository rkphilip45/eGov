/*
 * Created on Mar 29, 2008
 * @author Iliyaraja.S
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class Egw_Works_Deductions
{
	private static final Logger LOGGER=Logger.getLogger(Egw_Works_Deductions.class);

	private String id =null;
	private String worksdetailid =null;
	private String glcodeid =null;
	private String amount = null;
	private String perc = null;
	private String dedType="";
	private String tdsId = null;
	private String lastmodifieddate = "";

	private String created ="";



	EGovernCommon cm = new EGovernCommon();

	private String updateQuery="UPDATE Egw_Works_Deductions SET";
	private boolean isId=false, isField=false;

	public void setId(String aId){ id = aId;isId=true;  }
	public int getId() {return Integer.valueOf(id).intValue(); }

	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
		updateQuery = updateQuery + " amount = "+ amount + ","; isField = true;
	}

	public String getDedType() {
		return dedType;
	}
	public void setDedType(String dedType) {
		this.dedType = dedType;
		updateQuery = updateQuery + " dedType = '"+ dedType + "',"; isField = true;
	}
	public String getGlcodeid() {
		return glcodeid;
	}
	public void setGlcodeid(String glcodeid) {
		this.glcodeid = glcodeid;
		updateQuery = updateQuery + " glcodeid = "+ glcodeid + ","; isField = true;
	}

	public String getLastmodifieddate() {
		return lastmodifieddate;
	}
	public void setLastmodifieddate(String lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
		updateQuery = updateQuery + " lastmodifieddate = '" + lastmodifieddate + "',"; isField = true;
		}

	public String getPerc() {
		return perc;
	}
	public void setPerc(String perc) {
		this.perc = perc;
		updateQuery = updateQuery + " perc = "+ perc + ","; isField = true;
	}
	public String getTdsId() {
		return tdsId;
	}
	public void setTdsId(String tdsId) {
		this.tdsId = tdsId;
		updateQuery = updateQuery + " tdsId = "+ tdsId + ","; isField = true;
	}
	public String getWorksdetailid() {
		return worksdetailid;
	}
	public void setWorksdetailid(String worksdetailid) {
		this.worksdetailid = worksdetailid;
		updateQuery = updateQuery + " worksdetailid = "+ worksdetailid + ",";
		isId=true;
		isField = true;
	}


public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("Egw_Works_Deductions")) );
		created = cm.getCurrentDate(connection);
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String currentdate = formatter.format(sdf.parse( created ));
			setLastmodifieddate(currentdate);
		}catch(Exception e){throw new TaskFailedException(e.getMessage());}



		String insertQuery = "INSERT INTO Egw_Works_Deductions (id, worksdetailid, glcodeid, amount, perc, " +
					"dedType, tdsId, lastmodifieddate) " +
					"VALUES (" + id + ", " + worksdetailid + ", " + glcodeid + ", " + amount + ", "	+ perc + ","
					+"'" + dedType + "', " + tdsId + ","+"'"+lastmodifieddate+"')";
        if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		Statement statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		statement.close();

	} //insert

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		created = cm.getCurrentDate(connection);
		String currentdate="";
		try{
			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			currentdate = formatter.format(sdf.parse( created ));
		}
		catch(Exception e){throw new TaskFailedException(e.getMessage());}
		setLastmodifieddate(currentdate);

		if(isId && isField)
		{
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE worksdetailid = " + worksdetailid;
            if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateQuery);
			statement.close();
			updateQuery="UPDATE Egw_Works_Deductions SET";
		}
	} // update

} // class





