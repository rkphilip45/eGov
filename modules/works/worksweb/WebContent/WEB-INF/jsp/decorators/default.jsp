<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>eGov Works <decorator:title/></title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<link href="<egov:url path='/css/works.css'/>" rel="stylesheet" type="text/css" />
<link href="<egov:url path='/css/commonegov.css'/>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/fonts/fonts-min.css'/>" />
<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css'/>" />

<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/assets/skins/sam/autocomplete.css'/>" />

<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/dragdrop/dragdrop.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/element/element.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/connection/connection-min.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/datasource/datasource-min.js'/>"></script>

<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/autocomplete/autocomplete-min.js'/>"></script>
<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/datatable/datatable.js'/>"></script>

<script type="text/javascript" src="<egov:url prefix='/egi'  path='/commonyui/yui2.7/animation/animation-min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>

<script type="text/javascript" src="/egi/script/calendar.js"></script>
<script type="text/javascript" src="/egi/javascript/calender.js"></script>
<script type="text/javascript" src="/egi/script/jsCommonMethods.js"></script>
<script type="text/javascript" src="/egi/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/egi/javascript/validations.js"></script>
<script type="text/javascript" src="/egi/jsutils/prototype/prototype.js"></script>


<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/assets/skins/sam/skin.css'/>" />
<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/assets/skins/sam/button.css'/>" />
<link rel="stylesheet" type="text/css" href="<egov:url prefix='/egi'  path='/commonyui/yui2.7/assets/skins/sam/menu.css'/>" />

<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/container/container_core-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/menu/menu-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/button/button-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/editor/editor-min.js"></script>


<script src="/egworks/freerte_v1/js/richtext.js" type="text/javascript" language="javascript"></script>
<script src="/egworks/freerte_v1/js/config.js" type="text/javascript" language="javascript"></script>
<decorator:head/>
</head>
<body <decorator:getProperty property="body.id" writeEntireProperty="yes"/><decorator:getProperty property="body.class" writeEntireProperty="true"/> <decorator:getProperty property="body.onload" writeEntireProperty="true"/>  >
 <egov:breadcrumb/>
	<div class="topbar">
		<div style="margin-top:10px"><decorator:title/> </div>
	</div>
	
<!--<div class="navibarwk"> -->
<!-- div class="piconwk">
<a href="#">
<img src="<egov:url path='/image/help.png'/>" alt="Help" width="16" height="16" border="0" />
</a>
</div -->
<!--</div>-->
<!--<div class="navibarshadowwk"></div>-->
<decorator:body/>
<div class="urlwk"><div align>City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div></div>
</body>
</html>