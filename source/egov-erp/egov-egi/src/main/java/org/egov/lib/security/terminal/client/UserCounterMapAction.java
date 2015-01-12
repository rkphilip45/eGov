/*
 * @(#)UserCounterMapAction.java 3.0, 14 Jun, 2013 3:55:37 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.client;

import java.util.Date;
import java.util.List;

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
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.lib.security.terminal.dao.LocationHibernateDAO;
import org.egov.lib.security.terminal.dao.UserCounterDAO;
import org.egov.lib.security.terminal.dao.UserCounterHibernateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserCounterMap;

public class UserCounterMapAction extends DispatchAction {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(UserCounterMapAction.class);
	
	public ActionForward createUserControlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			UserCounterMapForm usercountermapform = (UserCounterMapForm) form;
			
			String buttonType = usercountermapform.getForward();
			String locationId = usercountermapform.getLocationId();
			String counter[] = usercountermapform.getCounter();
			String loginType = usercountermapform.getLoginType();
			UserCounterDAO daoObj = new UserCounterHibernateDAO(UserCounterMap.class, HibernateUtil.getCurrentSession());
			Integer modifiedBy = (Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
			
			if (loginType.equalsIgnoreCase("Location")) {
				if (locationId == null)
					throw new EGOVRuntimeException("ActionForm value cannot be null");
				
				String userId[] = usercountermapform.getUserId();
				if (userId != null) {
					for (int i = 0; i < userId.length; i++) {
						if (userId[i] != null && !userId[i].trim().equals("")) {
							Location locationobj = (Location) new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession()).findById(Integer.parseInt(locationId), false);
							UserImpl userobj = (UserImpl) new UserDAO().getUserByID(Integer.parseInt(userId[i]));
							if (usercountermapform.getSelCheck()[i].equals("yes") && usercountermapform.getUserCounterId()[i] != null && !usercountermapform.getUserCounterId()[i].equals("")) {
								UserCounterMap obj = (UserCounterMap) new UserCounterHibernateDAO(UserCounterMap.class, HibernateUtil.getCurrentSession()).findById(Integer.parseInt(usercountermapform.getUserCounterId()[i]), false);
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								daoObj.update(obj);
							} else if (usercountermapform.getSelCheck()[i].equals("no") && (usercountermapform.getUserCounterId()[i] == null || usercountermapform.getUserCounterId()[i].equals(""))) {
								UserCounterMap obj = new UserCounterMap();
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								daoObj.create(obj);
							}
							
						}
					}
				}
			} else if (loginType.equalsIgnoreCase("Terminal")) {
				String userId[] = usercountermapform.getUserId();
				if (userId != null && counter != null) {
					for (int i = 0; i < counter.length; i++) {
						if (userId[i] != null && counter[i] != null && !userId[i].trim().equals("") && !counter[i].trim().equals("")) {
							
							Location locationobj = (Location) new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession()).findById(Integer.parseInt(counter[i]), false);
							UserImpl userobj = (UserImpl) new UserDAO().getUserByID(Integer.parseInt(userId[i]));
							if (usercountermapform.getSelCheck()[i].equals("yes") && usercountermapform.getUserCounterId()[i] != null && !usercountermapform.getUserCounterId()[i].equals("")) {
								
								UserCounterMap obj = (UserCounterMap) new UserCounterHibernateDAO(UserCounterMap.class, HibernateUtil.getCurrentSession()).findById(Integer.parseInt(usercountermapform.getUserCounterId()[i]), false);
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								daoObj.update(obj);
							} else if (usercountermapform.getSelCheck()[i].equals("no") && (usercountermapform.getUserCounterId()[i] == null || usercountermapform.getUserCounterId()[i].equals(""))) {
								
								UserCounterMap obj = new UserCounterMap();
								obj.setCounterId(locationobj);
								obj.setUserId(userobj);
								String fromDate = usercountermapform.getFromDate()[i];
								fromDate = fromDate.substring(3, 5) + "/" + fromDate.substring(0, 2) + "/" + fromDate.substring(6, 10);
								obj.setFromDate(DateUtils.getDate(fromDate, DateUtils.DFT_DATE_FORMAT));
								if (usercountermapform.getToDate()[i] != null && !usercountermapform.getToDate()[i].equals("")) {
									String toDate = usercountermapform.getToDate()[i];
									toDate = toDate.substring(3, 5) + "/" + toDate.substring(0, 2) + "/" + toDate.substring(6, 10);
									obj.setToDate(DateUtils.getDate(toDate, DateUtils.DFT_DATE_FORMAT));
								}
								obj.setModifiedDate(new Date());
								obj.setModifiedBy(modifiedBy);
								daoObj.create(obj);
							}
							
						}
					}
				}
			}
			
			EgovMasterDataCaching.getInstance().removeFromCache("egi-usercountermap");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-location");
			EgovMasterDataCaching.getInstance().removeFromCache("egi-locationparent");
			target = buttonType;
			
		} catch (EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserCounterMapAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}
	
	public ActionForward getUserCounterForCurrentDate(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			UserCounterDAO objDao = (UserCounterDAO) new UserCounterHibernateDAO(UserCounterMap.class, HibernateUtil.getCurrentSession());
			UserCounterMapForm usercountermapform = (UserCounterMapForm) form;
			String loginType = usercountermapform.getLoginType();
			Location locationobj = (Location) new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession()).findById(Integer.valueOf(usercountermapform.getLocationId()), false);
			if (loginType.equalsIgnoreCase("Location")) {
				List userCounterMapList = objDao.getLocationBasedUserCounterMapForCurrentDate(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			} else if (loginType.equalsIgnoreCase("Terminal")) {
				List userCounterMapList = objDao.getTerminalBasedUserCounterMapForCurrentDate(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			}
			UserDAO userDAO = new UserDAO();
			List userList = userDAO.getAllUserForRoles(EGovConfig.getProperty("INCLUDE_ROLES", "", "IP-BASED-LOGIN"), new java.util.Date());
			
			req.setAttribute("userList", userList);
			req.setAttribute("locationParentList", EgovMasterDataCaching.getInstance().get("egi-locationparent"));
			req.setAttribute("locationList", EgovMasterDataCaching.getInstance().get("egi-location"));
			req.setAttribute("locationobj", locationobj);
			req.setAttribute("loginType", loginType);
			target = "currentDate";
			
		} catch (EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserCounterMapAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}
	
	public ActionForward getAllUserCounters(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String target = "";
		try {
			UserCounterDAO objDao = (UserCounterDAO) new UserCounterHibernateDAO(UserCounterMap.class, HibernateUtil.getCurrentSession());
			UserCounterMapForm usercountermapform = (UserCounterMapForm) form;
			String loginType = usercountermapform.getLoginType();
			if (loginType.equalsIgnoreCase("Location")) {
				List userCounterMapList = objDao.getUserCounterMapForLocationId(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			} else if (loginType.equalsIgnoreCase("Terminal")) {
				List userCounterMapList = objDao.getUserCounterMapForTerminalId(Integer.valueOf(usercountermapform.getLocationId()));
				req.setAttribute("userCounterMapList", userCounterMapList);
			}
			String buttonType = "showAll";
			Location locationobj = (Location) new LocationHibernateDAO(Location.class, HibernateUtil.getCurrentSession()).findById(Integer.valueOf(usercountermapform.getLocationId()), false);
			UserDAO userDAO = new UserDAO();
			List userList = userDAO.getAllUserForRoles(EGovConfig.getProperty("INCLUDE_ROLES", "", "IP-BASED-LOGIN"), new java.util.Date());
			
			req.setAttribute("userList", userList);
			req.setAttribute("locationParentList", EgovMasterDataCaching.getInstance().get("egi-locationparent"));
			req.setAttribute("locationList", EgovMasterDataCaching.getInstance().get("egi-location"));
			req.setAttribute("buttonType", buttonType);
			req.setAttribute("locationobj", locationobj);
			req.setAttribute("loginType", loginType);
			target = buttonType;
			
		} catch (EGOVRuntimeException ex) {
			target = "error";
			LOGGER.error("Exception occurred in UserCounterMapAction ", ex);
			throw ex;
		}
		return mapping.findForward(target);
	}
}