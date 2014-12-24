/*
 * PayStructureDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;

import java.util.Date;
import java.util.List;

import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;

/**
 * <p>
 * This is an interface which would be implemented by the Individual Frameworks
 * for all the CRUD (create, read, update, delete) basic data access operations
 * for exception
 * 
 * @author Lokesh
 * @version 2.00
 * 
 */

public interface PayStructureDAO extends org.egov.infstr.dao.GenericDAO {
	  public PayStructure getPayStructureById(Integer Id);
	  public List getPayStructureByEmp(Integer empId);
	  public PayStructure getCurrentPayStructureForEmp(Integer empid);
	  public PayStructure getPayStructureForEmpByDate(Integer empid,Date date);
	  public String checkExistingPayStructureInPayslip(PayScaleHeader payScaleHeader)throws Exception;

}