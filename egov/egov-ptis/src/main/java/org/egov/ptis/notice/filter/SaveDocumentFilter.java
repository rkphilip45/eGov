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
package org.egov.ptis.notice.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.ptis.domain.dao.property.NoticeDAO;
import org.egov.ptis.notice.PtNotice;

/**
 * passes on a subclass of HttpServletResponseWrapper in order to replace the
 * output writer.
 */
public class SaveDocumentFilter implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		NoticeDAO noticeDao = new NoticeDAO();
		SetDomainJndiHibFactNames.setThreadLocals(request);
		// wrap the original response
		HttpServletResponse newResponse = new ReplacementHttpServletResponse(
				(HttpServletResponse) response);
		// pass it to the resource
		chain.doFilter(request, newResponse);
		// get what the resource wrote
		String output = newResponse.toString();
		// put it to the output stream
		PrintWriter out = response.getWriter();
		out.write(output);
		out.close();
		// and then put it where ever else you like
		InputStream inputStream = new ByteArrayInputStream(output.getBytes());
		PtNotice noticeForm = (PtNotice)request.getAttribute("NoticeDetailsForm");
		noticeDao.saveNoticeDetails(noticeForm, inputStream);
	}
}
