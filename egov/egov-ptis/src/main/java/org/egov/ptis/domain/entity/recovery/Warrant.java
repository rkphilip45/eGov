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
package org.egov.ptis.domain.entity.recovery;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.ptis.notice.PtNotice;

/**
 * EgptWarrant entity. @author MyEclipse Persistence Tools
 */

public class Warrant extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Recovery recovery;
    private PtNotice notice;
    private String remarks;

    @Valid
	private List<WarrantFee> warrantFees = new LinkedList<WarrantFee>();
    
    @Required(message="warrant.recovery.null")
    public Recovery getRecovery() {
        return recovery;
    }

    public PtNotice getNotice() {
        return notice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRecovery(Recovery recovery) {
        this.recovery = recovery;
    }

    public void setNotice(PtNotice notice) {
        this.notice = notice;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	public void setWarrantFees(List<WarrantFee> warrantFees) {
		this.warrantFees = warrantFees;
	}

	public List<WarrantFee> getWarrantFees() {
		return warrantFees;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(null!=notice?notice.getNoticeNo():" ").
		append("|").append(null!=remarks?remarks:" ");
		return sb.toString();
	}

}
