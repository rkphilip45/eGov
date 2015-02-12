/*
 *	@(#)GenericCommonsManager.java		Oct 25, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * eGov PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pims.commons.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.commons.ObjectType;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;



/**
 * An interface for all the common entities, which are likely to be used in more than one applications.
 * @author Venkatesh.M.J,Divya
 * @version 1.2
 * @since 1.2, Oct 25, 2005
*/
public interface EisCommonsService 
{  
   public abstract void updatePosition(Position position);
   public abstract Position getPositionById(Integer positionId);
   public abstract Position getPositionByUserId(Integer userId);
   public abstract User getUserforPosition(Position pos);
   public abstract Position getPositionForUserByIdAndDate(Integer userId, Date assignDate);
   
 //Api for Auto Generate Employee code
	public abstract Boolean isEmployeeAutoGenerateCodeYesOrNo();
	//Api check's for employee code unique
	public abstract Boolean checkEmpCode(String empCode);
	//to get position by passing position name
	public abstract Position getPositionByName(String positionName);
	/**
	 * This method returns the current position id  of the user
	 * 
	 * @param user the user whose designation is needed.
	 * 
	 * 
	 * @return the position id as integer 
	 */
	public abstract Position getCurrentPositionByUser(User user);
	
	public abstract  User getUserForPosition(Integer posId, Date date);
	public abstract List<DesignationMaster> getDesigantionBasedOnFuncDept(Integer deptId,Integer functionaryId) throws Exception;
	/**
	  * Returning temporary  assigned employee object by pepartment,designation,functionary,date 
	  * @param deptId
	  * @param DesigId
	  * @param functionaryId
	  * @param onDate
	  * @return Employee
	  * @throws Exception 
	  */
	 public PersonalInformation getTempAssignedEmployeeByDeptDesigFunctionaryDate(Integer deptId, Integer desigId, Integer functionaryId, Date onDate) throws Exception;

}