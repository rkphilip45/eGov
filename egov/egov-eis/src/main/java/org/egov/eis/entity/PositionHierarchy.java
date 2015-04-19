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
package org.egov.eis.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.commons.ObjectType;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pims.commons.Position;
import org.egov.search.domain.Searchable;

/**
 * @author Vaibhav.K
 *
 */
@Entity
@Table(name = "egeis_position_hierarchy")
@Searchable
public class PositionHierarchy extends AbstractAuditable<User,Long>{

    private static final long serialVersionUID = 8666462146278384384L;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="position_from")
    private Position fromPosition;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="position_to")
    private Position toPosition;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "object_type_id")
    private ObjectType objectType;
    
    @Column(name= "object_sub_type")
    private String objectSubType;

    public Position getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Position fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Position getToPosition() {
        return toPosition;
    }

    public void setToPosition(Position toPosition) {
        this.toPosition = toPosition;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public String getObjectSubType() {
        return objectSubType;
    }

    public void setObjectSubType(String objectSubType) {
        this.objectSubType = objectSubType;
    }
    
}