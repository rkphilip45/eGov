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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css' context='/egi'/>">
<div class="row">
	<div class="col-md-12">
		<form:form role="form" action="register" modelAttribute="complaint" commandName="complaint" id="complaintform" cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.contact.info"/></strong>
					</div>
				</div>
				<div class="panel-body custom-form ">
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.name"/><span class="mandatory"></span></label>
						<div class="col-sm-6 add-margin">
							<form:input  class="form-control" path="complainant.name" id="f-name" placeholder="Name"/>
							 <form:errors path="complainant.name" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.mobileNo"/><span class="mandatory"></span></label>
						<div class="col-sm-2 add-margin">
							<form:input path="complainant.mobile" class="form-control" data-inputmask="'mask': '9999999999'" id="mob-no" placeholder="Mobile Number"/>
							<form:errors path="complainant.mobile" cssClass="add-margin error-msg"/>
						</div>
						
						<div class="col-sm-1">
							<label for="field-1" class="control-label"><spring:message code="lbl.email"/></label>
						</div>
						<div class="col-sm-3">
							<form:input  path="complainant.email" class="form-control" id="email" placeholder="abc@xyz.com"/>
							<form:errors path="complainant.email" cssClass="add-margin error-msg"/>
						</div>
					</div>
				</div>
				<div class="panel-heading custom_form_panel_heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.complaintInfo"/></strong>
					</div>
				</div> 
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.complaintType"/><span class="mandatory"></span></label>
						<div class="col-sm-6">
							<form:input path="complaintType.name" id="complaintTypeName"
								cssClass="form-control typeahead is_valid_alphabet"
								cssErrorClass="form-control error" placeholder=""
								autocomplete="off" required="required" />
							<form:hidden path="complaintType" id="complaintTypeId" value="" />
							<form:errors path="complaintType" cssClass="add-margin error-msg" />
							<c:forEach items="${complaintTypes}" var="complaintType">
								<a onclick="setComplaintTypeId(<c:out value="${complaintType.id}"/>)" href="javascript:void(0)" class="btn btn-secondary btn-xs tag-element freq-ct"><c:out
										value="${complaintType.name}" /> </a>
							</c:forEach>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.compDetails"/> <span class="mandatory"></span></label>
						<div class="col-sm-6">
							<form:textarea path="details" id="doc" placeholder="" maxlength="500" cssClass="form-control autogrow" required="required"/>
							<div class="text-right"><small id="rcc"><spring:message code="lbl.remainingChars"/> : 500</small></div>
							<form:errors path="details" cssClass="add-margin error-msg"/>
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-sm-3 col-xs-12 change-text-align">
							<a href="#" id="triggerFile" class="btn btn-secondary"><spring:message code="lbl.uploadPhotoVid"/></a>
							<input type="file" id="file1" name="files" data-id="1" class="filechange inline btn" style="display:none;"/>
							<input type="file" id="file2" name="files" data-id="2" class="filechange inline btn" style="display:none;"/>
							<input type="file" id="file3" name="files" data-id="3" class="filechange inline btn" style="display:none;"/>
						</div>
						<div class="col-sm-6 col-xs-12">
							<div id="file1block" class="add-margin col-sm-4 col-xs-4">
								<img id="preview1" src="#" alt="" class="display-hide "/>
								<div class="remove-img preview-cross1 display-hide" data-file-id><i class="entypo-cancel-circled"></i></div>
								<div class="add-padding" id="filename1"></div>
							</div>
							<div id="file2block" class="add-margin display-hide col-sm-4 col-xs-4">
								<img id="preview2" src="#" alt="" class="display-hide"/>
								<div class="remove-img preview-cross2 display-hide" data-file-id><i class="entypo-cancel-circled"></i></div>
								<div class="add-padding" id="filename2"></div>
							</div>
							<div id="file3block" class="add-margin display-hide col-sm-4 col-xs-4">
								<img id="preview3" src="#" alt="" class="display-hide"/>
								<div class="remove-img preview-cross3 display-hide" data-file-id><i class="entypo-cancel-circled"></i></div>
								<div class="add-padding" id="filename3"></div>
							</div>
						</div>
					</div>
					
					<div class="form-group optionalmandate">
						<label class="col-sm-3 control-label"><spring:message code="lbl.complaintLoc"/><span class="mandatory"></span></label>
						<div class="col-sm-6">
							<div class="input-group">
								<input id="location" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
								<span class="input-group-addon map-class btn-secondary" title="See on map" onclick="jQuery('#modal-6').modal('show', {backdrop: 'static'});"><i class="entypo-globe"></i></span>
								<form:hidden path="location" id="locationid"/>
								<form:errors path="location" cssClass="add-margin error-msg"/>
				   				<form:hidden path="lat" id="lat"/>
                   				<form:hidden path="lng" id="lng"/>
                   				<form:hidden path="complaintType.locationRequired" id="locationRequired" disabled="true"/>
							</div>
						</div>
					</div>
					
					<div class="form-group optionalmandate">
						<label class="col-sm-3 control-label"><spring:message code="lbl.landmark"/></label>
						<div class="col-sm-6">
							<form:textarea path="landmarkDetails" class="form-control" id="landmarkDetails" placeholder="" maxlength="200"/>
							<form:errors path="landmarkDetails" cssClass="add-margin error-msg"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary"><spring:message code="title.complaint.reg"/></button>
					<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</form:form>
	</div>
</div>


<div class="modal fade" id="modal-6">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0"><!-- to apply shadow add class "panel-shadow" -->
							<!-- panel head -->
							<div class="panel-heading">
								<div class="panel-title"><spring:message code="lbl.complaintLoc"/></div>
							</div>
						
							<!-- panel body -->
							<div class="panel-body no-padding">
								<script type="text/javascript" src="//maps.google.com/maps/api/js?sensor=true"></script>
								<script type="text/javascript" src="<c:url value='/resources/global/js/geolocation/geolocationmarker-compiled.js' context='/egi'/>"></script>
								<div id="normal" class="img-prop"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="modal-footer">
				<button type="button" class="btn btn-info btn-save-location" data-dismiss="modal"><spring:message code="lbl.ok"/></button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="lbl.close"/></button>
			</div>
		</div>
	</div>
</div>
<script>
	var complaintTypeId= '${complaint.complaintType.id}';
	if(complaintTypeId !== ''){
		$("#complaintTypeId").val(complaintTypeId);
	}
</script>
<script src="<c:url value='/resources/js/app/fileuploadndmaps.js'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>

<script src="<c:url value='/resources/js/app/complaint.js'/>"></script>
