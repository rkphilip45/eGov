<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib  uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title text-center no-float">
					<strong><spring:message code="msg.complaint.reg.success"/></strong>
					<div><spring:message code="lbl.complaint.reg.no"/>(<spring:message code="lbl.crn"/>) : <span id="ctn_no"><strong>${complaint.CRN}</strong></span>.<spring:message code="msg.crn.info"/></div>
				</div>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complaintDate"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-date">
						<joda:format value="${complaint.createdDate}" var="complaintDate" pattern="dd-MM-yyyy hh:mm:ss"/>
						${complaintDate}
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complainant.name"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-name">
						<c:choose>
							<c:when test="${not empty complaint.complainant.name}">
							${complaint.complainant.name}
							</c:when>
							<c:otherwise>
							${complaint.complainant.userDetail.name}
							</c:otherwise>
						</c:choose>
						
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.phoneNumber" />
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-mobno">
						${complaint.complainant.mobile}
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.email"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-email">
						${complaint.complainant.email}
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
		
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complaintType"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-type">
						${complaint.complaintType.name}
					</div>
					<div class="col-md-3 col-xs-6 add-margin">
						Complaint Title
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-title">
						${complaint.complaintType.name}
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.compDetails"/>
					</div>
					<div class="col-md-9 col-xs-6 add-margin" id="ct-details">
						${complaint.details}
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.complaintLocation"/>
					</div>
					<div class="col-md-9 col-xs-6 add-margin" id="ct-location">
						${complaint.location.localName}
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-xs-6 add-margin">
						<spring:message code="lbl.landmark"/>
					</div>
					<div class="col-md-3 col-xs-6 add-margin" id="ct-landmark">
						${complaint.landmarkDetails}
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
		
<div class="row text-center">
	<div class="add-margin">
		<button type="submit" class="btn btn-default"><spring:message code="lbl.print"/></button>
		<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
	</div>
</div>
