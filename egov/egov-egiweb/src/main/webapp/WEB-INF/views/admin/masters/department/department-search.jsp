<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>"/>
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>"/>
<div class="row" id="page-content">
	<div class="col-md-12">
		<div class="panel" data-collapsed = "0">
			<div class="panel-body">
				<c:if test="${not empty message}">
					<div class="success" id="message">${message}</div>
				</c:if>
				<form:form id="departmentSearchForm" method="post" modelAttribute="department" class="form-horizontal form-groups-bordered">
					<div class="panel panel-primary" data-collapsed = "0">
						<div class="panel-heading">
							<div class="panel-title">
								<strong><spring:message code="title.department.search"/></strong>
							</div>
						</div>
						<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label">
									<spring:message code="lbl.department"/><small><i class="entypo-star error-msg"></i></small>
								</label>
								<div class="col-sm-6 add-margin">
									<form:select path="name" id="dept" cssClass="form-control" cssErrorClass="form-control error" required="required">
										<form:option value="" ><spring:message code="lbl.select"/></form:option>
										<form:options items="${departments}" itemLabel="name" itemValue="name"></form:options>
									</form:select>
									<form:errors path="name" cssClass="error-msg"/>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="text-center">
							<button type="submit" class="btn btn-primary"><spring:message code="lbl.search"/></button>
							<button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
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