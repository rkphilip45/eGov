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
package org.egov.infra.web.controller.admin.masters.role;

import java.util.List;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/role")
public class SearchRoleController {
    private RoleService roleService;

    @Autowired
    public SearchRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ModelAttribute
    public Role roleModel() {
        return new Role();
    }

    @ModelAttribute("roles")
    public List<Role> roles() {
        return roleService.getAllRoles();
    }

    @RequestMapping(value = "/viewsearch", method = RequestMethod.GET)
    public String viewSearch(Model model) {
        model.addAttribute("mode", "view");
        return "role-search";

    }
    @RequestMapping(value = "/updatesearch", method = RequestMethod.GET)
    public String updateSearch(Model model) {
        model.addAttribute("mode", "update");
        return "role-search";

    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String viewRole(@Valid @ModelAttribute Role role, final BindingResult errors, RedirectAttributes redirectAttrs) {

        return "redirect:/role/view/"+role.getName();

    }
    
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateSearch(@Valid @ModelAttribute Role role, final BindingResult errors, RedirectAttributes redirectAttrs) {

        return "redirect:/role/update/"+role.getName();
    }

}
