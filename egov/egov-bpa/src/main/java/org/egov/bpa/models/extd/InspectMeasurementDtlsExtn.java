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
package org.egov.bpa.models.extd;

// Generated 13 Nov, 2012 12:35:05 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

import org.egov.bpa.models.extd.masters.InspectionSourceExtn;
import org.egov.bpa.models.extd.masters.SurroundedBldgDtlsExtn;
import org.egov.infstr.models.BaseModel;

/**
 * InspectMeasurementdtls generated by hbm2java
 */
public class InspectMeasurementDtlsExtn extends BaseModel {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private InspectionDetailsExtn inspectionDetails;
	private InspectionSourceExtn inspectionSource;
	private BigDecimal fsb;
	private BigDecimal rsb;
	private BigDecimal ssb1;
	private BigDecimal ssb2;
	private BigDecimal passWidth;
	private BigDecimal passageLength;
	private String header;
	private SurroundedBldgDtlsExtn surroundedByNorth;
	private SurroundedBldgDtlsExtn surroundedBySouth;
	private SurroundedBldgDtlsExtn surroundedByEast;
	private SurroundedBldgDtlsExtn surroundedByWest;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InspectionDetailsExtn getInspectionDetails() {
		return inspectionDetails;
	}

	public void setInspectionDetails(InspectionDetailsExtn inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}

	public InspectionSourceExtn getInspectionSource() {
		return inspectionSource;
	}

	public void setInspectionSource(InspectionSourceExtn inspectionSource) {
		this.inspectionSource = inspectionSource;
	}

	public BigDecimal getFsb() {
		return fsb;
	}

	public void setFsb(BigDecimal fsb) {
		this.fsb = fsb;
	}

	public BigDecimal getRsb() {
		return rsb;
	}

	public void setRsb(BigDecimal rsb) {
		this.rsb = rsb;
	}

	public BigDecimal getSsb1() {
		return ssb1;
	}

	public void setSsb1(BigDecimal ssb1) {
		this.ssb1 = ssb1;
	}

	public BigDecimal getSsb2() {
		return ssb2;
	}

	public void setSsb2(BigDecimal ssb2) {
		this.ssb2 = ssb2;
	}

	public BigDecimal getPassWidth() {
		return passWidth;
	}

	public void setPassWidth(BigDecimal passWidth) {
		this.passWidth = passWidth;
	}

	public BigDecimal getPassageLength() {
		return passageLength;
	}

	public void setPassageLength(BigDecimal passageLength) {
		this.passageLength = passageLength;
	}

	public SurroundedBldgDtlsExtn getSurroundedByNorth() {
		return surroundedByNorth;
	}

	public void setSurroundedByNorth(SurroundedBldgDtlsExtn surroundedByNorth) {
		this.surroundedByNorth = surroundedByNorth;
	}

	public SurroundedBldgDtlsExtn getSurroundedBySouth() {
		return surroundedBySouth;
	}

	public void setSurroundedBySouth(SurroundedBldgDtlsExtn surroundedBySouth) {
		this.surroundedBySouth = surroundedBySouth;
	}

	public SurroundedBldgDtlsExtn getSurroundedByEast() {
		return surroundedByEast;
	}

	public void setSurroundedByEast(SurroundedBldgDtlsExtn surroundedByEast) {
		this.surroundedByEast = surroundedByEast;
	}

	public SurroundedBldgDtlsExtn getSurroundedByWest() {
		return surroundedByWest;
	}

	public void setSurroundedByWest(SurroundedBldgDtlsExtn surroundedByWest) {
		this.surroundedByWest = surroundedByWest;
	}

}
