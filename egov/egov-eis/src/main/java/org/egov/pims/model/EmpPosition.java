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
package org.egov.pims.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;

@Entity
@Table(name="EGEIS_POST_CREATION")
public class EmpPosition extends StateAware {

    private static final long serialVersionUID = 9220002621595085170L;

    @NotNull(message = "postname.required")
    @Column(name="POST_NAME",nullable=false)
    private String postName;
    
    @NotNull(message = "desig.required")
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="DESIG_ID")
    private DesignationMaster desigId;
    
    @NotNull(message = "dept.required")
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="DEPT_ID")
    private Department deptId;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STATUS")
    private EgwStatus status;
    
    @Column(name="QUALIFY_DETAILS")
    private String qualificationDetails;
    
    private String remarks;
    
    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name="POSITION_ID")
    private Position position;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public DesignationMaster getDesigId() {
        return desigId;
    }

    public void setDesigId(DesignationMaster desigId) {
        this.desigId = desigId;
    }

    public Department getDeptId() {
        return deptId;
    }

    public void setDeptId(Department deptId) {
        this.deptId = deptId;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(EgwStatus status) {
        this.status = status;
    }

    public String getQualificationDetails() {
        return qualificationDetails;
    }

    public void setQualificationDetails(String qualificationDetails) {
        this.qualificationDetails = qualificationDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getStateDetails() {

        return "" + getDeptId().getName() + "-" + getDesigId().getDesignationName() + "-" + getPostName();
    }

}
