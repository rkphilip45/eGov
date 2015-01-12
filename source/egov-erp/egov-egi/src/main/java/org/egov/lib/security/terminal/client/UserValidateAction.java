/*
 * @(#)UserValidateAction.java 3.0, 18 Jun, 2013 4:04:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.client;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rjbac.user.ejb.server.UserServiceImpl;
import org.egov.lib.security.terminal.dao.UserValidateDAO;
import org.egov.lib.security.terminal.dao.UserValidateHibernateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;

public class UserValidateAction extends DispatchAction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserValidateAction.class);
	private UserService userService = new UserServiceImpl();
	
	public ActionForward validateUser(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		boolean result = false;
		
		try {
			UserValidateForm userform = (UserValidateForm) form;
			
			String username = userform.getUsername();
			String password = userform.getPassword();
			String ipAddress = userform.getIpAddress();
			String locationId = userform.getLocationId();
			String counterId = userform.getCounterId();
			String loginType = userform.getLoginType();
			
			if (username == null || password == null || loginType == null)
				throw new EGOVRuntimeException("ActionForm value cannot be null");
			
			UserValidate obj = new UserValidate();
			
			UserValidateDAO daoObj = new UserValidateHibernateDAO();
			
			obj.setUsername(username);
			obj.setPassword(password);
			obj.setIpAddress(ipAddress);
			
			if (loginType.equalsIgnoreCase("Location")) {
				if (locationId == null || counterId == null)
					throw new EGOVRuntimeException("LocationId or CounterId is null");
				
				obj.setLocationId(Integer.parseInt(locationId));
				obj.setCounterId(Integer.parseInt(counterId));
				
				Location location = daoObj.getLocationByIP(ipAddress);
				if (location != null) {
					result = daoObj.validateUserLocation(obj);
				} else {
					location = daoObj.getTerminalByIP(ipAddress);
					if (location != null)
						result = daoObj.validateUserTerminal(obj);
				}
				
				req.getSession().setAttribute("com.egov.user.locationId", locationId);
				req.getSession().setAttribute("com.egov.user.counterId", counterId);
				
			} else {
				result = daoObj.validateUser(obj);
				// isActive = daoObj.validateActiveUserForPeriod(obj.getUsername());
			}
			
			if (result == true) {
				target = "success";
				req.getSession().setAttribute("com.egov.user.LoginUserName", username);
				User user = userService.getUserByUserName(username);
				req.getSession().setAttribute("com.egov.user.LoginUserId", user.getId());
				req.getSession().setAttribute("org.egov.user.UserFirstName",user.getFirstName());
			} else {
				target = "failure";
			}
			
			req.setAttribute("target", target);
		} catch (Exception ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserValidateAction ", ex);
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException("Exception occurred in UserValidateAction ", ex);
		}
		
		return mapping.findForward(target);
		
	}
	
}