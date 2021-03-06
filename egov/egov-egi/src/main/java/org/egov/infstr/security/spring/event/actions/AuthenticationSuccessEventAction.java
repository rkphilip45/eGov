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
package org.egov.infstr.security.spring.event.actions;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infstr.commons.EgLoginLog;
import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.lib.security.terminal.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class will get called when Authentication is successful. Now this class
 * only Logs the User Login information.
 **/
@Service
@Transactional
public class AuthenticationSuccessEventAction implements
        ApplicationSecurityEventAction<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void doAction(final InteractiveAuthenticationSuccessEvent authorizedEvent) {
        final Authentication authentication = authorizedEvent.getAuthentication();
        final HashMap<String, String> credentials = (HashMap<String, String>) authentication.getCredentials();
        final EgLoginLog login = new EgLoginLog();
        login.setLoginTime(new Date(authorizedEvent.getTimestamp()));
        login.setUser(userService.getUserById(((SecureUser) authentication.getPrincipal()).getUserId()));
        if (org.apache.commons.lang.StringUtils.isNotBlank(credentials.get(SecurityConstants.COUNTER_FIELD))) {
            final Location location = entityManager.find(Location.class,
                    Integer.valueOf(credentials.get(SecurityConstants.COUNTER_FIELD)));
            login.setLocation(location);
        }
        entityManager.persist(login);
        entityManager.flush();
        final String loginLogID = login.getId().toString();
        ((HashMap<String, String>) authentication.getCredentials()).put(SecurityConstants.LOGIN_LOG_ID, loginLogID);
    }
}
