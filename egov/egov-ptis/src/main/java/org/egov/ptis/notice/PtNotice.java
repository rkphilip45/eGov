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
package org.egov.ptis.notice;

import java.util.Date;

import org.egov.ptis.domain.entity.property.BasicProperty;

public class PtNotice implements java.io.Serializable {
	private Long id;
	private Integer moduleId;
	private String noticeType;
	private String noticeNo;
	private Date noticeDate;
	private BasicProperty basicProperty;
	// private String objectNo;
	// private String addressTo;
	// private String address;
	private Integer userId;
	// private File document;
	private byte[] noticeFile;
	private Character isBlob;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the moduleId
	 */
	public Integer getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId
	 *            the moduleId to set
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the noticeType
	 */
	public String getNoticeType() {
		return noticeType;
	}

	/**
	 * @param noticeType
	 *            the noticeType to set
	 */
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	/**
	 * @return the noticeNo
	 */
	public String getNoticeNo() {
		return noticeNo;
	}

	/**
	 * @param noticeNo
	 *            the noticeNo to set
	 */
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	/**
	 * @return the noticeDate
	 */
	public Date getNoticeDate() {
		return noticeDate;
	}

	/**
	 * @param noticeDate
	 *            the noticeDate to set
	 */
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public byte[] getNoticeFile() {
		return noticeFile;
	}

	public void setNoticeFile(byte[] noticeFile) {
		this.noticeFile = noticeFile;
	}

	public Character getIsBlob() {
		return isBlob;
	}

	public void setIsBlob(Character isBlob) {
		this.isBlob = isBlob;
	}

	@Override
	public String toString() {
		StringBuilder sbf = new StringBuilder();
		sbf.append("Id: ").append(getId()).append("|NoticeType: ").append(getNoticeType()).append("|NoticeNo: ")
				.append(getNoticeNo()).append("|isBlob: ").append(getIsBlob());
		return sbf.toString();
	}

}
