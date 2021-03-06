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
package org.egov.collection.entity;

import java.math.BigDecimal;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.utils.EntityType;

/**
 * The AccountPayeeDetail information class. Provides details of a AccountPayeeDetail in subledger detail.
 */
public class AccountPayeeDetailInfo {
	private final AccountPayeeDetail accountPayeeDetail = null;
	private EntityType entityType;
	
	/*public AccountPayeeDetailInfo(AccountPayeeDetail accountPayeeDetail,EgovCommon egovCommon){
		this.accountPayeeDetail = accountPayeeDetail;
		try {
			populateEntityType(accountPayeeDetail, egovCommon);
		} catch (EGOVException e) {
			throw new EGOVRuntimeException(
					"Could not get entity type for account detail type ["
							+ accountPayeeDetail.getAccountDetailType().getTablename()
							+ "], account detail key id ["
							+ accountPayeeDetail.getAccountDetailKey().getId()
							+ "]", e);
		}
	}

	public void populateEntityType(AccountPayeeDetail accountPayeeDetail,
			EgovCommon egovCommon) throws EGOVException {
		entityType = egovCommon.getEntityType(accountPayeeDetail
				.getAccountDetailType(), accountPayeeDetail
				.getAccountDetailKey().getDetailkey());
	}*/
	
	/**
	 * @return the GL code
	 */
	public String getGlCode() {
		return accountPayeeDetail.getReceiptDetail().getAccounthead().getGlcode()==null?
				null:accountPayeeDetail.getReceiptDetail().getAccounthead().getGlcode();
	}
	
	public String getAccountDetailTypeName(){
		return accountPayeeDetail.getAccountDetailType().getName();
	}
	
	public Accountdetailtype getAccountDetailType() {
		return accountPayeeDetail.getAccountDetailType();
	}
	
	public Accountdetailkey getAccountDetailKey() {
		return accountPayeeDetail.getAccountDetailKey();
	}
	
	public BigDecimal getAmount(){
		return accountPayeeDetail.getAmount();
	}
	
	public EntityType getEntityType(){
		return entityType;
	}
	
	/**
	 * @return the order number
	 */
	public Long getOrderNumber() {
		return accountPayeeDetail.getReceiptDetail().getOrdernumber()==null?
				null:accountPayeeDetail.getReceiptDetail().getOrdernumber();
	}
}
