/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.web.actions.workflow;

import java.math.BigDecimal;

public class GenericWorkFlowFormBean extends org.apache.struts.action.ActionForm {

	private static final long serialVersionUID = 1L;
	protected String currentState;
	protected String currentDesignation;
	protected Integer approverPositionId;
	protected String additionalRule;
	protected BigDecimal amountRule;
	protected String pendingAction;
	protected String actionName;
	protected String approverComments;

	public String getPendingAction() {
		return this.pendingAction;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	public void setPendingAction(final String pendingAction) {
		this.pendingAction = pendingAction;
	}

	protected String workFlowDepartment;

	public String getCurrentState() {
		return this.currentState;
	}

	public void setCurrentState(final String currentState) {
		this.currentState = currentState;
	}

	public String getWorkFlowDepartment() {
		return this.workFlowDepartment;
	}

	public String getCurrentDesignation() {
		return this.currentDesignation;
	}

	public void setCurrentDesignation(final String currentDesignation) {
		this.currentDesignation = currentDesignation;
	}

	public String getAdditionalRule() {
		return this.additionalRule;
	}

	public void setAdditionalRule(final String additionalRule) {
		this.additionalRule = additionalRule;
	}

	public BigDecimal getAmountRule() {
		return this.amountRule;
	}

	public void setAmountRule(final BigDecimal amountRule) {
		this.amountRule = amountRule;
	}

	public void setWorkFlowDepartment(final String workFlowDepartment) {
		this.workFlowDepartment = workFlowDepartment;
	}

	public String getApproverComments() {
		return this.approverComments;
	}

	public void setApproverComments(final String approverComments) {
		this.approverComments = approverComments;
	}

	public Integer getApproverPositionId() {
		return this.approverPositionId;
	}

	public void setApproverPositionId(final Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

}
