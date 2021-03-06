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
package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_NOTICE_GENERATION_PENDING;

import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

/**
 *
 * @author nayeem
 *
 */
public class StepSave extends WorkflowActionStep {

	public StepSave() {
	}

	public StepSave(PropertyImpl propertyModel, Integer userId, String comments) {
		super(propertyModel, userId, comments);
	}

	@Override
	public String getStepName() {
		return WFLOW_ACTION_STEP_SAVE;
	}

	@Override
	public String getStepValue() {

		StringBuilder stepValue = new StringBuilder();

		stepValue.append(actionName);

		if (isAllChangesCompleted()) {
			stepValue = stepValue.append(WF_STATE_NOTICE_GENERATION_PENDING);
		} else {
			stepValue = stepValue
					.append(propertyTaxUtil.getDesignationName(Long
							.valueOf(userId))).append("_")
					.append(NMCPTISConstants.WF_STATE_APPROVAL_PENDING);
		}

		return stepValue.toString();
	}

	/**
	 * @return true if Data Entry is completed false if not
	 */
	private boolean isAllChangesCompleted() {
		return propertyModel.getBasicProperty().getAllChangesCompleted() == null ? true
				: propertyModel.getBasicProperty().getAllChangesCompleted();
	}

	/**
	 * @return true if workflow action is <code> Create </code> or
	 *         <code> Modify </code>
	 */
	private boolean isActionCreateOrModify() {
		return WFLOW_ACTION_NAME_MODIFY.equalsIgnoreCase(actionName)
				|| NMCPTISConstants.WFLOW_ACTION_NAME_CREATE
						.equalsIgnoreCase(actionName);
	}
}
