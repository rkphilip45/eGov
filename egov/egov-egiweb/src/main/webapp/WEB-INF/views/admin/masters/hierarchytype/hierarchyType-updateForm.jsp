<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">
<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>
		<form:form  method="post" class="form-horizontal form-groups-bordered" modelAttribute="hierarchyType" id="updaterole" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.updateHierarchyType"/></strong>
					</div>
				</div> 
				
				<div class="panel-body custom-form">
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.hierarchyType.name"/><small><i class="entypo-star error-msg"></i></small>
						</label>
						<div class="col-sm-6">
							<form:input path="name" id="name" type="text" class="form-control low-width is_valid_alphanumeric" placeholder="" autocomplete="off"/>
                            <form:errors path="name" cssClass="add-margin error-msg"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label"><spring:message code="lbl.hierarchyType.code"/></label>
						<div class="col-sm-6">
							<form:input path="code" id="description" type="text" class="form-control low-width is_valid_alphabet" placeholder="" autocomplete="off"/>
                            <form:errors path="code" cssClass="add-margin error-msg"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">
							<spring:message code="lbl.hierarchyType.localName"/>
						</label>
						<div class="col-sm-6">
							<form:input path="localName" id="localName" type="text" class="form-control low-width is_valid_alphanumeric" placeholder="" autocomplete="off"/>
                            <form:errors path="localName" cssClass="add-margin error-msg"/>
						</div>
					</div>	
				</div>
			</div>
			<div class="row">
				<div class="text-center">
					<button type="submit" class="btn btn-primary"><spring:message code="lbl.update"/></button>
			        <button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
			        <a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></a>
				</div>
			</div>
		</form:form>
			</div>
        </div>
    </div>
</div>

<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js'/>"></script>

<script src="<c:url value='/resources/global/js/egov/custom.js'/>"></script>