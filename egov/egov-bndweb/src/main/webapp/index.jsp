<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ page import="java.sql.Connection,java.sql.PreparedStatement,
org.egov.infstr.client.administration.rjbac.user.UserForm,
org.egov.infstr.utils.HibernateUtil,
org.egov.infstr.utils.ServiceLocator,
org.egov.lib.rjbac.user.User,
org.egov.lib.rjbac.user.ejb.api.UserManager,
org.egov.lib.rjbac.user.ejb.api.UserManagerHome;"
%>


<HTML>

<HEAD>

<meta http-equiv=Content-Type content="text/html; charset=x-user-defined">


	<TITLE>eGov LEMS</TITLE>

<LINK rel="stylesheet" type="text/css" href="<c:url value="/common/css/commonegov.css" />

<LINK rel="stylesheet" type="text/css" href="<c:url value="/common/css/landestate.css" />


<%
boolean admin=false;
Integer userID= (Integer)request.getSession().getAttribute("com.egov.user.LoginUserId");
 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Egov Site mesh Header userID "+userID);
 String userName="";
 ServiceLocator serviceloc = ServiceLocator.getInstance();
 UserManagerHome uhome = (UserManagerHome)serviceloc.getLocalHome("UserManagerHome");
UserManager userManager = uhome.create();
if(userID !=null)
{
User user = userManager.getUserByID(userID);
 userName=user.getUserName();

}
 %>

</HEAD>

<BODY>


<div class="formmainbox"><div class="formheading"></div><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="41%"><img src="common/image/iconStore.jpg" alt="Login" width="368" height="292" /></td>
            	<% if(userID!=null)
			      {
      			%>
            <td width="59%"><div class="welcomestyles">Welcome  <%=userName%> </div>
            <%
			}
			else{
            %>
              <td width="59%"><div class="welcomestyles">Welcome </div>
              <%
		  }
		  %>

            <div class="welcomestylel">Welcome to eGov Birth And Death Online Application<br />
              Management System</div>
            <div class="welcomestylearrow">Use the links on the left to navigate
              through the application.</div></td>
          </tr>
        </table>
	  </div>
	  <div class="rbbot2"><div></div></div>
	</div>
  </div>
</div>
<div class="buttonbottom">City Administration System Designed and Implemented by <a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved </div>
</body>
</html>

