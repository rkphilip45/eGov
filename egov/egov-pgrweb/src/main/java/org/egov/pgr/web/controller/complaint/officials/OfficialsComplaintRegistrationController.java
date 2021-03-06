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
package org.egov.pgr.web.controller.complaint.officials;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.web.controller.complaint.GenericComplaintController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/complaint/officials/")
public class OfficialsComplaintRegistrationController extends GenericComplaintController {

    private @Autowired ReceivingCenterService receivingCenterService;
    
    public @ModelAttribute("receivingCenters") List<ReceivingCenter> receivingCenters() {
        return this.receivingCenterService.findAll();
    }
    
    @RequestMapping(value = "show-reg-form", method = GET)
    public String showComplaintRegistrationForm(@ModelAttribute final Complaint complaint) {
        return "complaint/officials/registration-form";
    }

    @RequestMapping(value = "register", method = POST)
    public String registerComplaint(@Valid @ModelAttribute final Complaint complaint, final BindingResult resultBinder, final RedirectAttributes redirectAttributes,@RequestParam("files") final MultipartFile[] files) {
        if(complaint.getReceivingMode().equals(ReceivingMode.PAPER) && complaint.getReceivingCenter().isCrnRequired() && complaint.getCRN().isEmpty()) 
            resultBinder.rejectValue("CRN", "crn.mandatory.for.receivingcenter");
        if (resultBinder.hasErrors())
            return "complaint/officials/registration-form";
        complaint.setSupportDocs(addToFileStore(files));
        complaintService.createComplaint(complaint);
        redirectAttributes.addFlashAttribute("complaint", complaint);
        return "redirect:/reg-success";
    }
}
