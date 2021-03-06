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
package org.egov.infstr.security.spring.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.infstr.utils.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class EgovAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {
	
	private List<String> credentialFields = new ArrayList<String>();
	
	public void setCredentialFields(final List<String> credentialFields) {
		this.credentialFields = credentialFields;
	}

	@Override
	protected void setDetails(final HttpServletRequest request, final UsernamePasswordAuthenticationToken authToken) {
		authToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain, final Authentication authResult) throws IOException, ServletException {
		// Add information to session variables
		final String location = request.getParameter(SecurityConstants.LOCATION_FIELD);
		final String counter = request.getParameter(SecurityConstants.COUNTER_FIELD);
		if (StringUtils.isNotBlank(location)) {
			request.getSession().setAttribute(SecurityConstants.LOCATION_FIELD, location);
		}
		if (StringUtils.isNotBlank(counter)) {
			request.getSession().setAttribute(SecurityConstants.COUNTER_FIELD, counter);
		}
		if (authResult != null) {
			final SecureUser principal = (SecureUser) authResult.getPrincipal();
			request.getSession().setAttribute("com.egov.user.LoginUserId",principal.getUserId());
		}
		super.successfulAuthentication(request, response, filterChain, authResult);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
		final HashMap<String, String> credentials = new HashMap<String, String>();
		for (final String credential : this.credentialFields) {
			final String field = request.getParameter(credential) == null ? "" : request.getParameter(credential);
			credentials.put(credential, field);
		}
		final String username = request.getParameter(SecurityConstants.USERNAME_FIELD);
		final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, credentials);
		request.getSession().setAttribute(SecurityConstants.USERNAME_FIELD, username);
		this.setDetails(request, authToken);

		return this.getAuthenticationManager().authenticate(authToken);
	}

}
