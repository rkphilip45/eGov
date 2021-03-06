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
package org.egov.collection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.web.actions.BaseFormAction;

//@Result(name=Action.SUCCESS, type=ServletRedirectResult.class, value = "receiptNumberSearch-searchResults")  

@ParentPackage("egov")  

public class ReceiptNumberSearchAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private ReceiptHeaderService receiptHeaderService;   
	private static final String SEARCH_RESULTS = "searchResults";
	private static final String MANUALRECEIPTNUMBER_SEARCH_RESULTS = "manualReceiptNumberResults";
	private List<ReceiptHeader> receiptNumberList = new ArrayList<ReceiptHeader>();
	private List<ReceiptHeader> manualReceiptNumberList  = new ArrayList<ReceiptHeader>();
	private String query;
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String searchAjax(){
		return SEARCH_RESULTS;
	}

	public Object getModel() {
		return null;
	}
	public String searchManualReceiptNumberAjax()
	{
		return MANUALRECEIPTNUMBER_SEARCH_RESULTS;
	}

	public Collection<ReceiptHeader> getManualReceiptNumberList() {
		if(StringUtils.isNotBlank(query))
			receiptNumberList = receiptHeaderService.findAllBy("from org.egov.erpcollection.models.ReceiptHeader where upper(manualreceiptnumber) like  ? || '%'",query.toUpperCase());
		return receiptNumberList;
	}
	
	public Collection<ReceiptHeader> getReceiptNumberList() {
		if(StringUtils.isNotBlank(query))
			receiptNumberList = receiptHeaderService.findAllBy("from org.egov.erpcollection.models.ReceiptHeader where upper(receiptnumber) like '%' || ? || '%'",query.toUpperCase());
		return receiptNumberList;
	}


	/**
	 * @param receiptHeaderService the receiptHeaderService to set
	 */
	public void setReceiptHeaderService(ReceiptHeaderService receiptHeaderService) {
		this.receiptHeaderService = receiptHeaderService;
	}
}

