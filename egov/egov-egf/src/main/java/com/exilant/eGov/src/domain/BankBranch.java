/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

public class BankBranch {
	private String id = null;
	private String branchCode = null;
	private String branchName = null;
	private String branchAddress1 = null;
	private String branchAddress2 = null;
	private String branchCity = null;
	private String branchState = null;
	private String branchPin = null;
	private String branchMICR = null;
	private String branchPhone = null;
	private String branchFax = null;
	private String bankId = null;
	private String contactPerson = null;
	private String isActive = "1";
	private String created = "1-Jan-1900";
	private String modifiedBy = null;
	private String lastModified = "1-Jan-1900";
	private String narration = null;
	private TaskFailedException taskExc;
	private final static Logger LOGGER = Logger.getLogger(BankBranch.class);
	private String updateQuery = "UPDATE bankbranch SET";
	private boolean isId = false, isField = false;
	EGovernCommon cm = new EGovernCommon();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
			.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
			Locale.getDefault());

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public String getId() {
		return id;
	}

	

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchAddress1() {
		return branchAddress1;
	}

	public void setBranchAddress1(String branchAddress1) {
		this.branchAddress1 = branchAddress1;
	}

	public String getBranchAddress2() {
		return branchAddress2;
	}

	public void setBranchAddress2(String branchAddress2) {
		this.branchAddress2 = branchAddress2;
	}

	public String getBranchCity() {
		return branchCity;
	}

	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}

	public String getBranchState() {
		return branchState;
	}

	public void setBranchState(String branchState) {
		this.branchState = branchState;
	}

	public String getBranchPin() {
		return branchPin;
	}

	public void setBranchPin(String branchPin) {
		this.branchPin = branchPin;
	}

	public String getBranchPhone() {
		return branchPhone;
	}

	public void setBranchPhone(String branchPhone) {
		this.branchPhone = branchPhone;
	}

	public String getBranchFax() {
		return branchFax;
	}

	public void setBranchFax(String branchFax) {
		this.branchFax = branchFax;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public boolean isField() {
		return isField;
	}

	public void setField(boolean isField) {
		this.isField = isField;
	}

	public String getBranchMICR() {
		return branchMICR;
	}

	public void setBranchMICR(String branchMICR) {
		this.branchMICR = branchMICR;
	}

	/**
	 * This is Insert API
	 * 
	 * @param connection
	 * @throws TaskFailedException
	 * @throws SQLException
	 */
	public void insert(Connection connection) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		PreparedStatement pst = null;
		try {
			/*
			 * created = cm.assignValue(formatter.format(sdf.parse( created
			 * )),created);
			 */
			created = formatter.format(sdf.parse(created));
			setLastModified(created);
			setId(String.valueOf(PrimaryKeyGenerator.getNextKey("BankBranch")));
			setBranchCode(id);
			String insertQuery = "INSERT INTO bankbranch (Id, BranchCode, BranchName, BranchAddress1, BranchAddress2, "
					+ "BranchCity, BranchState, BranchPin, BranchPhone, BranchFax, BankId, ContactPerson, "
					+ "IsActive, Created, ModifiedBy, LastModified, Narration, MICR) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
			pst = connection.prepareStatement(insertQuery);
			pst.setString(1, id);
			pst.setString(2, branchCode);
			pst.setString(3, branchName);
			pst.setString(4, branchAddress1);
			pst.setString(5, branchAddress2);
			pst.setString(6, branchCity);
			pst.setString(7, branchState);
			pst.setString(8, branchPin);
			pst.setString(9, branchPhone);
			pst.setString(10, branchFax);
			pst.setString(11, bankId);
			pst.setString(12, contactPerson);
			pst.setString(13, isActive);
			pst.setString(14, created);
			pst.setString(15, modifiedBy);
			pst.setString(16, lastModified);
			pst.setString(17, narration);
			pst.setString(18, branchMICR);
			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in insert" + e.getMessage());
			throw taskExc;
		} finally {
			try {
				pst.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of insert");
			}
		}
	}

	/**
	 * This is the update API
	 * 
	 * @param connection
	 * @throws TaskFailedException
	 * @throws SQLException
	 */
	public void update(Connection connection) throws TaskFailedException,
			SQLException {
		
		newUpdate(connection);
		
		/* if (isId && isField) {
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate(connection);
			PreparedStatement pst = null;
			try {

				created = formatter.format(sdf.parse(created));
				setCreated(created);
				setLastModified(created);
				updateQuery = updateQuery
						.substring(0, updateQuery.length() - 1);
				updateQuery = updateQuery + " WHERE id = ?";
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				pst = connection.prepareStatement(updateQuery);
				pst.setString(1, id);
				pst.executeUpdate();
				updateQuery = "UPDATE bankbranch SET";
			} catch (Exception e) {
				LOGGER.error("Exp in update: " + e.getMessage());
				throw taskExc;
			} finally {
				try {
					pst.close();
				} catch (Exception e) {
					LOGGER.error("Inside finally block of update");
				}
			}
		}*/
	}

	/**
	 * This API gives the details for all the branches
	 * 
	 * @param con
	 * @return
	 * @throws TaskFailedException
	 */
	public HashMap getBankBranch(Connection con) throws TaskFailedException {
		String query = "SELECT  CONCAT(CONCAT(bankBranch.bankId, '-'),bankBranch.ID) as \"bankBranchID\",concat(concat(bank.name , ' '),bankBranch.branchName) as \"bankBranchName\" FROM bank, bankBranch where"
				+ " bank.isactive=1 and bankBranch.isactive=1 and bank.ID = bankBranch.bankId order by bank.name";
		if(LOGGER.isInfoEnabled())     LOGGER.info("  query   " + query);
		Map hm = new LinkedHashMap<String, String>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				hm.put(rs.getString(1), rs.getString(2));
			}

		} catch (Exception e) {
			LOGGER.error("Error in getBankBranch ", e);
			throw taskExc;
		} finally {
			try {
				rs.close();
				pst.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of getBankBranch");
			}
		}
		return (HashMap) hm;
	}

	/**
	 * This API will give the list of Bankaccounts for any Bank branch
	 * 
	 * @param bankBranchId
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public HashMap getAccNumber(String bankBranchId, Connection con)
			throws TaskFailedException {
		String id[] = bankBranchId.split("-");
		String branchId = id[1];
		PreparedStatement pst = null;
		ResultSet rs = null;

		String query = "SELECT  ID, accountNumber from bankAccount WHERE branchId= ? and isactive=1  ORDER BY ID";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("  query   " + query);
		Map hm = new HashMap();
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, branchId);
			rs = pst.executeQuery();
			while (rs.next()) {
				hm.put(rs.getString(1), rs.getString(2));
			}
		} catch (Exception e) {
			LOGGER.error("Error in getAccNumber ", e);
			throw taskExc;
		} finally {
			try {
				rs.close();
				pst.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of getAccNumber");
			}
		}

		return (HashMap) hm;
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(con);
		try {
			created = formatter.format(sdf.parse(created));
		} catch (ParseException parseExp) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(),parseExp);
		}
		setCreated(created);
		setLastModified(created);
		StringBuilder query = new StringBuilder(500);
		query.append("update BankBranch set ");
		if (branchCode != null)
			query.append("branchCode=?");
		if (branchName != null)
			query.append(",branchName=?");
		if (branchAddress1 != null)
			query.append(",branchAddress1=?");
		if (branchAddress2 != null)
			query.append(",branchAddress2=?");
		if (branchCity != null)
			query.append(",branchCity=?");
		if (branchState != null)
			query.append(",branchState=?");
		if (branchPin != null)
			query.append(",branchPin=?");
		if (branchPhone != null)
			query.append(",branchPhone=?");
		if (branchFax != null)
			query.append(",branchFax=?");
		if (bankId != null)
			query.append(",bankId=?");
		if (contactPerson != null)
			query.append(",contactPerson=?");
		if (isActive != null)
			query.append(",isActive=?");
		if (created != null)
			query.append(",created=?");
		if (modifiedBy != null)
			query.append(",modifiedBy=?");
		if (lastModified != null)
			query.append(",lastModified=?");
		if (narration != null)
			query.append(",narration=?");
		if (branchMICR != null && !branchMICR.isEmpty() )
			query.append(",MICR=?");
		query.append(" where id=?");
		PreparedStatement pstmt = null;
		try {
			
			int i = 1;
			pstmt = con.prepareStatement(query.toString());
			if (branchCode != null)
				pstmt.setString(i++, branchCode);
			if (branchName != null)
				pstmt.setString(i++, branchName);
			if (branchAddress1 != null)
				pstmt.setString(i++, branchAddress1);
			if (branchAddress2 != null)
				pstmt.setString(i++, branchAddress2);
			if (branchCity != null)
				pstmt.setString(i++, branchCity);
			if (branchState != null)
				pstmt.setString(i++, branchState);
			if (branchPin != null)
				pstmt.setString(i++, branchPin);
			if (branchPhone != null)
				pstmt.setString(i++, branchPhone);
			if (branchFax != null)
				pstmt.setString(i++, branchFax);
			if (bankId != null)
				pstmt.setString(i++, bankId);
			if (contactPerson != null)
				pstmt.setString(i++, contactPerson);
			if (isActive != null)
				pstmt.setString(i++, isActive);
			if (created != null)
				pstmt.setString(i++, created);
			if (modifiedBy != null)
				pstmt.setString(i++, modifiedBy);
			if (lastModified != null)
				pstmt.setString(i++, lastModified);
			if (narration != null)
				pstmt.setString(i++, narration);
			if (branchMICR != null && !branchMICR.isEmpty())
				pstmt.setString(i++, branchMICR);
			pstmt.setString(i++, id);

			pstmt.executeQuery();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(),e);
			throw taskExc;
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				LOGGER.error("Inside finally block of update");
			}
		}
	}
}
