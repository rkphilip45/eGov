/*
 * Created on Jan 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

//import java.sql.*;
/**
 * @author siddhu
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DataValidator {
	private static final Logger LOGGER = Logger.getLogger(DataValidator.class);

	public boolean checkDepartmentId(String deptId, Connection connection) {
		try {
			// Statement statement=connection.createStatement();
			PreparedStatement pstmt = null;
			String strQry = "select dept_name from eg_department where id_dept= ?";
			pstmt = connection.prepareStatement(strQry);
			pstmt.setString(1, deptId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkDepartmentId: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkFunctionId(String funcId, Connection connection) {
		String str = "select name from function where id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(str);
			pstmt.setString(1, funcId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			rset.close();
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkFunctionId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkDepartmentName(String deptName, Connection connection) {
		String str = "select dept_name from eg_department where dept_name=?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(str);
			pstmt.setString(1, deptName);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkDepartmentName: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkFunctionName(String funcName, Connection connection) {
		PreparedStatement pstmt = null;
		String str = "select name from function where name= ?";
		try {
			pstmt = connection.prepareStatement(str);
			pstmt.setString(1, funcName);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkFunctionName: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkOrganizationStructureId(String orgId,
			Connection connection) {
		PreparedStatement pstmt = null;
		String valQry = "select name from organizationStructure where id= ?";
		try {
			pstmt = connection.prepareStatement(valQry);
			pstmt.setString(1, orgId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}

			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkOrganizationStructureId: "
					+ e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean taxCode(String taxCode, Connection connection) {
		PreparedStatement pstmt = null;
		String valQry = "select name from taxes where code= ?";
		try {
			// Statement statement=connection.createStatement();
			pstmt = connection.prepareStatement(valQry);
			pstmt.setString(1, taxCode);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in taxCode: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkBankAccount(String branchId, String accNumber,
			Connection connection) {
		PreparedStatement pstmt = null;
		String valQry = "select id from bankAccount  where branchId= ?";
		try {
			pstmt = connection.prepareStatement(valQry);
			pstmt.setString(1, branchId);
			ResultSet rset = pstmt.executeQuery();
			// " where branchId="+branchId+" and accountNumber='"+accNumber+"'");
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkBankAccount: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkAccount(String id, Connection connection) {
		PreparedStatement pstmt = null;
		String valQry = "select id from bankAccount  where id= ?";
		try {
			pstmt = connection.prepareStatement(valQry);
			pstmt.setString(1, id);
			ResultSet rset = pstmt.executeQuery();
			// " where branchId="+branchId+" and accountNumber='"+accNumber+"'");
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkAccount: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkBank(String bankId, Connection connection) {
		PreparedStatement pstmt = null;
		String valQry = "select name from bank where id= ?";
		try {
			pstmt = connection.prepareStatement(valQry);
			pstmt.setString(1, bankId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkBank: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundName(String fundName, Connection connection) {
		PreparedStatement pstmt = null;
		String valQry = "select name from fund where  name= ?";
		try {
			pstmt = connection.prepareStatement(valQry);
			pstmt.setString(1, fundName);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundName: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundId(String fundId, Connection connection) {
		PreparedStatement pstmt = null;
		String valQry = "select name from fund where id= ?";
		try {
			pstmt = connection.prepareStatement(valQry);
			pstmt.setString(1, fundId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundSourceName(String fundSourceName,
			Connection connection) {
		PreparedStatement pstmt = null;
		String fndSrc = "select name from fundsource where name=?";
		try {
			pstmt = connection.prepareStatement(fndSrc);
			pstmt.setString(1, fundSourceName);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundSourceName: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundSourceId(String fundSourceId, Connection connection) {
		PreparedStatement pstmt = null;
		String srtQry = "select name from fundsource where id= ?";
		try {
			pstmt = connection.prepareStatement(srtQry);
			pstmt.setString(1, fundSourceId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundSourceId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkSupplierId(String supplierId, Connection connection) {
		PreparedStatement pstmt = null;
		String srtQry = "select name from supplier where id= ?";
		try {
			pstmt = connection.prepareStatement(srtQry);
			pstmt.setString(1, supplierId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkSupplierId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkContractorId(String contractorId, Connection connection) {
		PreparedStatement pstmt = null;
		String srtQry = "select name from contractor where id= ?";
		try {
			pstmt = connection.prepareStatement(srtQry);
			pstmt.setString(1, contractorId);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkContractorId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkBillCollectorId(String id, String type,
			Connection connection) {
		PreparedStatement pstmt = null;
		String srtQry = "select name from billCollector  where id=? and type=?";
		try {
			pstmt = connection.prepareStatement(srtQry);
			pstmt.setString(1, id);
			pstmt.setString(2, type);
			ResultSet rset = pstmt.executeQuery();
			if (!rset.next()) {
				return false;
			}
			pstmt.close();
		} catch (Exception e) {
			LOGGER.error("Exp in checkBillCollectorId: " + e.getMessage());
			return false;
		}
		return true;
	}
}
