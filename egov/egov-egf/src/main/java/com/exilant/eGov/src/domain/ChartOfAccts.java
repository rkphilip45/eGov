/*
 * Created on Mar 4, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class ChartOfAccts {
	private String id = null;
	private String glCode = null;
	private String name = null;
	private String description = null;
	private String isActiveForPosting = "0";
	private String parentId = null;
	private String lastModified = "";
	private String modifiedBy = null;
	private String created = "";
	private String purposeid = null;
	private String operation = null;
	private String type = null;
	private String classname = "0";
	private String classification = null;
	private String functionreqd = "0";
	private String scheduleId = null;
	private Integer FIEscheduleId = null;
	private String FIEoperation = null;
	private String receiptscheduleid = null;
	private String receiptoperation = null;
	private String paymentscheduleid = null;
	private String paymentoperation = null;
	private String budgetCheckReqd = null;
	private String updateQuery = "UPDATE ChartOfAccounts SET ";
	private boolean isId = false, isField = false;
	private static final Logger LOGGER = Logger.getLogger(ChartOfAccts.class);
	private GenericHibernateDaoFactory genericDao;  
	EGovernCommon cm = new EGovernCommon();
	private TaskFailedException taskExc;
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

	public void setGLCode(String aGLCode) {
		glCode = aGLCode;
		isField = true;
	}

	public void setName(String aName) {
		name =aName;
		isField = true;
	}

	public void setDescription(String aDescription) {
		description = aDescription;
		isField = true;
	}

	public void setIsActiveForPosting(String aIsActiveForPosting) {
		isActiveForPosting = aIsActiveForPosting;
		isField = true;
	}

	public void setParentId(String aParentId) {
		parentId = aParentId;
		isField = true;
	}

	public void setLastModified(String aLastModified) {
		lastModified = aLastModified;
		isField = true;
	}

	public void setModifiedBy(String aModifiedBy) {
		modifiedBy = aModifiedBy;
		isField = true;
	}

	public void setCreated(String aCreated) {
		created = aCreated;
		isField = true;
	}

	public void setOperation(String aOperation) {
		operation = aOperation;
		isField = true;
	}

	public void setType(String aType) {
		type = aType;
		isField = true;
	}

	public void setClass(String aclass) {
		classname = aclass;
		isField = true;
	}

	public void setPurposeId(String aPurposeId) {
		purposeid = aPurposeId;
		isField = true;
	}

	public void setFunctionReqd(String aFunctionReqd) {
		functionreqd = aFunctionReqd;
		isField = true;
	}

	public void setClassification(String aClassification) {
		classification = aClassification;
		isField = true;
	}

	public void setScheduleId(String aScheduleId) {
		scheduleId = aScheduleId;
		isField = true;
	}

	public void setReceiptscheduleid(String aReceiptscheduleid) {
		receiptscheduleid = aReceiptscheduleid;
		isField = true;
	}

	public void setReceiptoperation(String aReceiptoperation) {
		receiptoperation = aReceiptoperation;
		isField = true;
	}

	public void setPaymentscheduleid(String aPaymentscheduleid) {
		paymentscheduleid = aPaymentscheduleid;
		isField = true;
	}

	public void setPaymentoperation(String aPaymentoperation) {
		paymentoperation = aPaymentoperation;
		isField = true;
	}

	public void setBudgetCheckReqd(String aBudgetCheckReqd) {
		budgetCheckReqd = aBudgetCheckReqd;
		isField = true;
	}
	public Integer getFIEscheduleId() {
		return FIEscheduleId;
	}

	public void setFIEscheduleId(int escheduleId) {
		FIEscheduleId = escheduleId;
		isField = true;
	}
	public String getFIEoperation() {
		return FIEoperation;
		
	}

	public void setFIEoperation(String eoperation) {
		FIEoperation = eoperation;
		isField = true;
	}

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		EGovernCommon commommethods = new EGovernCommon();
		created = new SimpleDateFormat("dd/mm/yyyy").format(new Date());
		PreparedStatement pst = null;
		try {
			created = formatter.format(sdf.parse(created));
			EgovMasterDataCaching.getInstance().removeFromCache(
					"egi-activeCoaCodes");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodes");
			EgovMasterDataCaching.getInstance().removeFromCache(
					"egi-chartOfAccounts");
			EgovMasterDataCaching.getInstance().removeFromCache(
					"egi-coaPurposeId10");
			EgovMasterDataCaching.getInstance().removeFromCache(
					"egi-accountCodes");
			EgovMasterDataCaching.getInstance().removeFromCache(
					"egi-liabilityCOACodes");
			EgovMasterDataCaching.getInstance().removeFromCache(
					"egi-coaCodesForLiability");
			setLastModified(created);
			setId(String.valueOf(PrimaryKeyGenerator
					.getNextKey("ChartOfAccounts")));

			String insertQuery = "INSERT INTO ChartOfAccounts (id, glCode, name, description, isActiveForPosting, "
					+ " parentId, lastModified, modifiedBy, "
					+ "created,  purposeid,functionreqd, operation,type,classification,class,budgetCheckReq,majorcode)"
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);

		HibernateUtil.getCurrentSession().createSQLQuery(insertQuery)
			.setInteger(1, Integer.parseInt(id))
			.setString(2, removeSingleQuotes(glCode))
			.setString(3, removeSingleQuotes(name))
			.setString(4, removeSingleQuotes(description))
			.setString(5, removeSingleQuotes(isActiveForPosting))
			.setString(6, removeSingleQuotes(parentId))
			.setString(7, removeSingleQuotes(lastModified))
			.setString(8, removeSingleQuotes(modifiedBy))
			.setString(9, removeSingleQuotes(created))
			.setString(10, removeSingleQuotes(purposeid))
			.setString(11, removeSingleQuotes(functionreqd))
			.setString(12, removeSingleQuotes(operation))
			.setString(13, removeSingleQuotes(type))
			.setString(14, removeSingleQuotes(classification))
			.setString(15, removeSingleQuotes(classname))
			.setString(16, removeSingleQuotes(budgetCheckReqd))
			.setString(17, removeSingleQuotes(getMajorCode(glCode))).executeUpdate();

		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new Exception(e.getMessage()));
		}
	}

	private String removeSingleQuotes(String obj) {
		if (obj != null) {
			obj = obj.replaceAll("'", "");
		}
		return obj;

	}

	/**
	 * glcode contains comma also
	 * 
	 * @param glcode
	 * @return
	 * @throws TaskFailedException
	 */
	private String getMajorCode(String glcode) throws TaskFailedException {
		final List<AppConfigValues> appList = genericDao
				.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF",
						"coa_majorcode_length");
		if (appList == null || appList.isEmpty())
			throw new TaskFailedException(
					"coa_majorcode_length is not defined in appconfig");
		final int majorcodelength = Integer.valueOf(appList.get(0).getValue());
		String glcode1 = glcode.substring(0, glcode.length());
		return glcode1.length() >= majorcodelength ? "'"
				+ glcode1.substring(0, majorcodelength) + "'" : "''";
	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
		if (isId && isField) {

			newUpdate(connection);
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(con);
		Query pstmt = null;
		try {
			created = formatter.format(sdf.parse(created));
		} catch (ParseException parseExp) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(), parseExp);
		}
		setCreated(created);
		setLastModified(created);
		StringBuilder query = new StringBuilder(500);
		query.append("update ChartOfAccounts set ");
		if (glCode != null)
			query.append("glCode=?,");
		if (name != null)
			query.append("name=?,");
		if (description != null)
			query.append("description=?,");
		if (isActiveForPosting != null)
			query.append("ISACTIVEFORPOSTING=?,");
		if (parentId != null)
			query.append("PARENTID=?,");
		if (lastModified != null)
			query.append("LASTMODIFIED=?,");
		if (modifiedBy != null)
			query.append("MODIFIEDBY=?,");
		if (created != null)
			query.append("CREATED=?,");
		if (purposeid != null)
			query.append("PURPOSEID=?,");
		if (operation != null)
			query.append("OPERATION=?,");
		if (FIEoperation != null)
			query.append("FIEOPERATION=?,");
		if (type != null)
			query.append("type=?,");
		if (classname != null)
			query.append("class=?,");
		if (classification != null)
			query.append("CLASSIFICATION=?,");
		if (functionreqd != null)
			query.append("FUNCTIONREQD=?,");
		if (scheduleId != null)
			query.append("SCHEDULEID=?,");
		if (FIEscheduleId != null)
			query.append("FIEscheduleId=?,");
		if (receiptscheduleid != null)
			query.append("RECEIPTSCHEDULEID=?,");
		if (receiptoperation != null)
			query.append("RECEIPTOPERATION=?,");
		if (paymentscheduleid != null)
			query.append("PAYMENTSCHEDULEID=?,");
		if (paymentoperation != null)
			query.append("PAYMENTOPERATION=?,");
		if (budgetCheckReqd != null)
			query.append("BUDGETCHECKREQ=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		
		try {
			int i = 1;
			pstmt =HibernateUtil.getCurrentSession().createSQLQuery(query.toString());

			if (glCode != null)
				pstmt.setString(i++, glCode);
			if (name != null)
				pstmt.setString(i++,name);
			if (description != null)
				pstmt.setString(i++,description);
			if (isActiveForPosting != null)
				pstmt.setString(i++, isActiveForPosting);
			if (parentId != null)
				pstmt.setString(i++, parentId);
			if (lastModified != null)
				pstmt.setString(i++, lastModified);
			if (modifiedBy != null)
				pstmt.setString(i++, modifiedBy);
			if (created != null)
				pstmt.setString(i++, created);
			if (purposeid != null)
				pstmt.setString(i++, purposeid);
			if (operation != null)
				pstmt.setString(i++, operation);
			if (FIEoperation != null)
				pstmt.setString(i++, FIEoperation);
			if (type != null)
				pstmt.setString(i++, type);
			if (classname != null)
				pstmt.setString(i++, classname);
			if (classification != null)
				pstmt.setString(i++, classification);
			if (functionreqd != null)
				pstmt.setString(i++,functionreqd);
			if (scheduleId != null)
				pstmt.setString(i++, scheduleId);
			if (FIEscheduleId != null)
				pstmt.setInteger(i++, FIEscheduleId);  
			if (receiptscheduleid != null)
				pstmt.setString(i++,receiptscheduleid);
			if (receiptoperation != null)
				pstmt.setString(i++, receiptoperation);
			if (paymentscheduleid != null)
				pstmt.setString(i++, paymentscheduleid);
			if (paymentoperation != null)
				pstmt.setString(i++, paymentoperation);
			if (budgetCheckReqd != null)
				pstmt.setString(i++,budgetCheckReqd);  
			pstmt.setString(i++, id);

			pstmt.executeUpdate();
		} catch (HibernateException e) {
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new Exception(e.getMessage()));
		}
	}

}
