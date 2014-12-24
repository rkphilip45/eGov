<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import= "org.egov.infstr.utils.*,
 		java.util.*"
%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Collection System</title>
 <link rel="stylesheet" type="text/css" href="<egov:url path='/css/collections.css'/>"/>
	<script type="text/javascript" src="<egov:url path='/js/collections.js'/>"></script>

 
</head>
<body>

<div class="navibar"><div align="right">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%
		if (request.getUserPrincipal() != null) //user has logged in
		{

	   %>
	   <tr>
		 <td width="50%" align="left">
		   <p ><b><font id="welcome_font"><span id="headerusername">Welcome &nbsp;
		   <%=EgovUtils.getPrincipalName(request.getUserPrincipal().getName()) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			  <td  width="50%" align="right">
		  <p ><b><font id="welcome_font"><span id="headerusername">
		    <a id="logouthref" name="logouthref" href="#" onclick="javascript:top.location='/egi/logout.do';top.opener.location = '/egi/logout.do';" /> Log out</a>

			 </span></font></b></p>
		 </td>

     </tr>
	  <%
		}
		else //user has not logged in
		{

	   %>
	   <tr>
		 <td  width="50%" align="left">
		   <p ><b><font id="welcome_font"><span id="headerusername">Welcome &nbsp;Guest &nbsp;
		   </span></font></b></p>
		 </td>
		 <td width="50%" align="right" >
		 <p ><b><font id="welcome_font"><span id="headerusername">
		   <a id="loginhref" name="loginhref" href="<egov:url path='/login/securityLogin.jsp'/>">Login</a>
		   </span></font></b></p>
		   </td></tr>
	   <%
		}

	  %>
	
  </table></div>
</div>
<div class="formmainbox"><div class="formheading"></div><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="41%"><img src="images/loginImage1.jpg" alt="Login" width="368" height="292" /></td>
            <td width="59%"><div class="welcomestyles">Welcome </div>
            <div class="welcomestylel">Welcome to eGov- Collection System</div>
            <div class="welcomestylearrow"><img src="<egov:url path='images/arrow_left.png'/>" alt="Left" width="16" height="16" align="absmiddle" /> Use the links on the left to navigate 
            through the application.</div></td>
          </tr>
        </table>
	  </div>
	  <div class="rbbot2"><div></div></div>
</div>
  </div>
</div>
<div class="buttonbottom">City Administration System Designed and Implemented by <a href="#" onclick=window.open("http://www.egovernments.org/")>eGovernments Foundation</a> All Rights Reserved </div>
</body>
</html>
