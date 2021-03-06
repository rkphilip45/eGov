/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.entity.property;

import java.util.List;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.pims.commons.DesignationMaster;

public class WorkflowBean {
	private String actionName;
	private String actionState;
	private List<User> appoverUserList;
	private Integer approverUserId;
	private String comments;
	private Integer departmentId;
	private List<Department> departmentList;
	private Integer designationId;
	private List<DesignationMaster> designationList;

	public String getActionName() {
		return actionName;
	}

	public String getActionState() {
		return actionState;
	}

	public Integer getApproverUserId() {
		return approverUserId;
	}

	public String getComments() {
		return comments;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public List<DesignationMaster> getDesignationList() {
		return designationList;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setActionState(String actionState) {
		this.actionState = actionState;
	}

	public List<User> getAppoverUserList() {
		return appoverUserList;
	}

	public void setAppoverUserList(List<User> appoverUserList) {
		this.appoverUserList = appoverUserList;
	}

	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public void setDesignationList(List<DesignationMaster> designationList) {
		this.designationList = designationList;
	}

	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("WorkflowBean[DepartmentId: ").append(this.getDepartmentId()).append(" , DesignationId: ")
				.append(this.designationId).append(" , User Id:").append(this.approverUserId).append(" , ActionName:")
				.append(this.actionName).append(" , departmentList(Size:").append(
						(departmentList != null) ? this.departmentList.size() : "").append("):").append(
						this.departmentList).append(" , designationList(Size:").append(
						(designationList != null) ? this.designationList.size() : "").append("):").append(
						this.designationList).append(" , appoverUserList(Size:").append(
						(appoverUserList != null) ? this.appoverUserList.size() : "").append("):").append(
						this.appoverUserList).append("]");

		return objStr.toString();
	}
}
