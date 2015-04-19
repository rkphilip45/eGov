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
package org.egov.infstr.client.administration.rjbac.user;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.utils.EgovInfrastrUtil;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.client.EgovAction;
import org.egov.infstr.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAction extends EgovAction {
        
        private final static Logger LOG = LoggerFactory.getLogger(UserAction.class);
        private final UserService userService;
        private final RoleService roleService;
        private EgovInfrastrUtil egovInfrastrUtil;

        public UserAction(UserService userService, RoleService roleService, EgovInfrastrUtil egovInfrastrUtil) {
                this.userService = userService;
                this.roleService = roleService;
                this.egovInfrastrUtil = egovInfrastrUtil;
        }

        /**
         * This method is used to forward the control to the jusersidiction page and for creation of
         * user If the user is to be given more levels of jurisdictions then the request is sent to the
         * same jursidiction page
         */
        @Override
        public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
                String target = null;           
                final UserForm userForm = (UserForm) form;
                final HttpSession session = req.getSession();
                
                if (req.getParameter("bool").equals("CREATE")) {
                        try {
                                final User user = new User();
                                
                                user.setName(userForm.getFirstName());
                                user.setPassword(userForm.getPassword());
                                //user.setPwdReminder(userForm.getPwdReminder());
                                user.setUsername(userForm.getUserName());
                                user.setPwdExpiryDate(new Date());
                                /*if (isNotBlank(userForm.getMiddleName())) {
                                        user.setMiddleName(userForm.getMiddleName());
                                }*/
                                /*if (isNotBlank(userForm.getLastName())) {
                                        user.setLastName(userForm.getLastName());
                                }*/
                                if (isNotBlank(userForm.getSalutation())) {
                                        user.setSalutation(userForm.getSalutation());
                                }                                       
                                
                                        user.setActive(userForm.getIsActive());
                                if (isNotBlank(userForm.getDob())) {
                                        user.setDob(DateUtils.getDate(userForm.getDob(), DateUtils.DFT_DATE_FORMAT));
                                }
                                /*if (isNotBlank(userForm.getFromDate())) {
                                        user.setFromDate(DateUtils.getDate(userForm.getFromDate(), DateUtils.DFT_DATE_FORMAT));
                                }
                                if (isNotBlank(userForm.getToDate())) {
                                        user.setToDate(DateUtils.getDate(userForm.getToDate(), DateUtils.DFT_DATE_FORMAT));
                                }*/
                                
                                /*if (userForm.getRoleId() != null) {
                                        final Set<UserRole> roles = new HashSet<UserRole>();
                                        final Integer roleid[] = userForm.getRoleId();
                                        Role role = null;
                                        for (int i = 0; i < roleid.length; i++) {
                                                final UserRole userrole = new UserRole();
                                                if (roleid[i] != 0) {
                                                        role = roleService.getRole(roleid[i]);
                                                        userrole.setRole(role);
                                                        userrole.setIsHistory('N');
                                                        userrole.setUser(user);
                                                        if (isNotBlank(userForm.getFromDate1()[i])) {
                                                                userrole.setFromDate(DateUtils.getDate(userForm.getFromDate1()[i], DateUtils.DFT_DATE_FORMAT));
                                                        }
                                                        if (isNotBlank(userForm.getToDate1()[i])) {
                                                                userrole.setToDate(DateUtils.getDate(userForm.getToDate1()[i], DateUtils.DFT_DATE_FORMAT));
                                                        }
                                                        roles.add(userrole);
                                                }
                                        }
                                        if (roles.size() != 0) {
                                                //user.setUserRoles(roles);
                                        }
                                }       */              
                                target = "success";
                                req.setAttribute("MESSAGE", "User created successfully");
                                /*if ((userForm.getFile() != null) && (userForm.getFile().getFileSize() > 0) && userForm.getFile().getContentType().indexOf("image") != -1) {
                                        final UserSignature userSignature = new UserSignature();
                                        userSignature.setSignature(CryptoHelper.encrypt(userForm.getFile().getFileData(), userForm.getPassword()));
                                        user.setUserSignature(userSignature);
                                }*/
                                userForm.reset(mapping, req);
                                session.removeAttribute("user");
                                session.removeAttribute("jurisdcnList");
                                //user.setIsSuspended('N');
                                //userService.createUser(user);
                                egovInfrastrUtil.resetCache();
                                
                        } catch (final Exception e) {
                                LOG.error("Exception occurred in User Create",e);
                                target = "error";
                                req.setAttribute("MESSAGE", "Error occurred while creating user");
                                throw new EGOVRuntimeException("Exception occurred in User Create", e);
                        }
                } else if (req.getParameter("bool").equals("UPDATE")) {
                        
                        try {
                                final User user = userService.getUserByUsername(userForm.getUserName());
                                /*if (isNotBlank(userForm.getMiddleName())) {
                                        user.setMiddleName(userForm.getMiddleName());
                                }*/
                                if (isNotBlank(userForm.getLastName())) {
                                        user.setName(userForm.getLastName());
                                }
                                if (isNotBlank(userForm.getSalutation())) {
                                        user.setSalutation(userForm.getSalutation());
                                }
                                /*if ((userForm.getFile() != null) && (userForm.getFile().getFileSize() > 0) && userForm.getFile().getContentType().indexOf("image") != -1) {
                                        UserSignature userSignature = null;
                                        if(user.getUserSignature() == null) {
                                                userSignature = new UserSignature();
                                        } else {
                                                userSignature = user.getUserSignature();
                                        }
                                        userSignature.setSignature(CryptoHelper.encrypt(userForm.getFile().getFileData(), userForm.getPassword()));
                                        user.setUserSignature(userSignature);
                                } else if ((user.getUserSignature() != null) && (user.getUserSignature().getSignature().length != 0) && (!userForm.getPassword().equals(CryptoHelper.decrypt(user.getPassword())))) {
                                        user.getUserSignature().setSignature(CryptoHelper.encrypt(CryptoHelper.decrypt(user.getUserSignature().getSignature(), CryptoHelper.decrypt(user.getPassword())), userForm.getPassword()));
                                }*/
                                
                                user.setPassword(userForm.getPassword());
                                //user.setPwdReminder(userForm.getPwdReminder());
                                user.setName(userForm.getFirstName());
                                user.setUsername(userForm.getUserName());
                                
                                if (isNotBlank(userForm.getDob())) {
                                        user.setDob(DateUtils.getDate(userForm.getDob(), DateUtils.DFT_DATE_FORMAT));
                                }
                                /*if (isNotBlank(userForm.getFromDate())) {
                                        user.setFromDate(DateUtils.getDate(userForm.getFromDate(), DateUtils.DFT_DATE_FORMAT));
                                }
                                if (isNotBlank(userForm.getToDate())) {
                                        user.setToDate(DateUtils.getDate(userForm.getToDate(), DateUtils.DFT_DATE_FORMAT));
                                }*/
                                user.setActive(userForm.getIsActive());
                                final String userRoleId[] = userForm.getUserRoleId();
                                // if role not present, create a new UserRole
                                // create set of newly updated UserRoles and setUserRoles for User.
                                // iterate through the roles in the form
                                // if no rows are present. empty values giving null pointer exception.
                                // so to avoid that the first condition will satisfy.
                                // the second condition is for if the user modify the first row.
                                
                                if (!(userRoleId[0].equals("0")) || isNotBlank(userForm.getFromDate1()[0])) {
                                        for (int i = 0; i < userRoleId.length; i++) { 
                                                
                                                final Integer currUserRoleId = Integer.valueOf(userRoleId[i]);
                                                final Role role = roleService.getRoleById(Long.valueOf(userForm.getSelRoleID()[i]));
                                                
                                                if (currUserRoleId == 0) {
                                                        /*final UserRole userrole = new UserRole();
                                                        if (isNotBlank(userForm.getFromDate1()[i])) {
                                                                userrole.setFromDate(DateUtils.getDate(userForm.getFromDate1()[i], DateUtils.DFT_DATE_FORMAT));
                                                        }
                                                        if (isNotBlank(userForm.getToDate1()[i])) {
                                                                userrole.setToDate(DateUtils.getDate(userForm.getToDate1()[i], DateUtils.DFT_DATE_FORMAT));
                                                        }
                                                        userrole.setRole(role);
                                                        userrole.setUser(user);
                                                        userrole.setIsHistory('N');*/
                                                        //user.getUserRoles().add(userrole);
                                                } else {
                                                        if ("yes".equals(userForm.getSelCheck()[i])) { // modifying existing role
                                                                /*final UserRole existingUserRole = this.getUserRole(user, currUserRoleId);
                                                                if (!userForm.getFromDate1()[i].equals(userForm.getExisFromDate()[i]) || (!userForm.getToDate1()[i].equals(userForm.getExisToDate()[i]))) {
                                                                        if (isNotBlank(userForm.getFromDate1()[i])) {
                                                                                existingUserRole.setFromDate(DateUtils.getDate(userForm.getFromDate1()[i], DateUtils.DFT_DATE_FORMAT));
                                                                        }
                                                                        if (isNotBlank(userForm.getToDate1()[i])) {
                                                                                existingUserRole.setToDate(DateUtils.getDate(userForm.getToDate1()[i], DateUtils.DFT_DATE_FORMAT));
                                                                        }
                                                                }*/
                                                        }
                                                }
                                        }
                                }
                                // handle deletions
                                final Set<Integer> delUserRoles = (Set<Integer>) req.getSession().getAttribute("delUserRoles");
                                if (delUserRoles != null) {
                                        /*for (final Integer delUserRoleId : delUserRoles) {
                                                if (delUserRoleId != 0) {
                                                        final UserRole existingUserRole = this.getUserRole(user, delUserRoleId);
                                                        //user.getUserRoles().remove(existingUserRole);
                                                }
                                        }*/
                                }
                                
                                target = "success";
                                req.setAttribute("MESSAGE", "User has been Updated Successfully!!");
                                userForm.reset(mapping, req);
                                session.removeAttribute("jurisdcnList");
                                //user.setIsSuspended('N');
                                //userService.updateUser(user);
                                egovInfrastrUtil.resetCache();
                        } catch (final Exception e) {
                                LOG.error("Exception occurred in User Update",e);
                                target = "error";
                                req.setAttribute("MESSAGE", "Error occurred while updates user info");
                                throw new EGOVRuntimeException("Exception occurred in User Update", e);
                        }
                }
                return mapping.findForward(target);
        }

        /*private UserRole getUserRole(final User user, final Integer userRoleId) {
                for (final UserRole userRole : user.getUserRoles()) {
                        if ((userRole.getId() != null) && userRole.getId().equals(userRoleId)) {
                                return userRole;
                        }
                }
                return null;
        }*/
}