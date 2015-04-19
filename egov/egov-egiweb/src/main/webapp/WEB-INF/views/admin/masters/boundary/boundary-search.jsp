<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/typeahead.css'/>">
<link rel="stylesheet" href="<c:url value='/css/jquery/jquery-ui-1.8.22.custom.css'/>" />

<div class="row" id="page-content">
    <div class="col-md-12">
        <div class="panel" data-collapsed="0">
            <div class="panel-body">
                <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>

                <form:form id="boundarySearchForm" method="post" 
                           modelAttribute="boundary" class="form-horizontal form-groups-bordered">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title">
								<strong><spring:message code="lbl.hdr.searchBoundary"/></strong>
							</div>
						</div> 
						
						<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label">
									<spring:message code="lbl.hierarchyType" />
									<span class="mandatory"></span>
								</label>
								<div class="col-sm-6 add-margin">
		                            <form:select path="name"
		                                         id="hierarchyTypeSelect" cssClass="form-control" onchange="populateBoundaryTypes(this);" cssErrorClass="form-control error" required="required">
		                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
		                                <form:options items="${hierarchyTypes}" itemValue="id" itemLabel="name"/>
		                            </form:select>
		                            <form:errors path="name" cssClass="error-msg"/>
	                        	</div>
	                        </div>
	                        <div class="form-group">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.boundaryType" /><span class="mandatory"></span></label>
								<div class="col-sm-6 add-margin">
									<egov:ajaxdropdown id="boundaryTypeAjax" fields="['Text','Value']"
												dropdownId="boundaryTypeSelect" url="boundaryTypes-by-hierarchyType" />
		                            <form:select path="name"
		                                         id="boundaryTypeSelect" cssClass="form-control" cssErrorClass="form-control error" required="required">
		                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
		                            </form:select>
		                            <form:errors path="name" cssClass="error-msg"/>
		                            <%-- <egov:ajaxdropdown id="boundaryAjax" fields="['Text','Value']"
												dropdownId="boundaries" url="boundaries-by-boundaryType" afterSuccess="showBoundariesDiv();"/> --%>
		                        </div>
							</div>
							<%-- <div class="form-group" id="boundariesDiv" style="display: none">
								<label class="col-sm-3 control-label"><spring:message
										code="lbl.boundaries" />&nbsp;&nbsp;&nbsp;</label>
								<div class="col-sm-6 add-margin">
		                            <form:select path="name"
		                                         id="boundaries" cssClass="form-control" cssErrorClass="form-control error" >
		                                <form:option value=""> <spring:message code="lbl.select"/> </form:option>
		                            </form:select>
		                            <form:errors path="name" cssClass="error-msg"/>
		                        </div>
							</div> --%>
	                	</div>
	                </div>

                    <div class="form-group">
                        <div class="text-center">
                            <button type="submit" id="buttonView" class="btn btn-success">
                            	<spring:message code="lbl.viewBoundaries"/>
                            </button>
                            <button type="submit" id="buttonCreate" class="btn btn-success">
                            	<spring:message code="lbl.create"/>
                            </button>
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

<script src="<c:url value='/resources/js/app/boundary.js'/>"></script>