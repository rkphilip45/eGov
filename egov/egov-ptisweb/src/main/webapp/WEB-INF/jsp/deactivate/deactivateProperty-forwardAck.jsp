#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<html>
<head>
	<title><s:text name="deactivate.prop"/></title>	
</head>
	<body onload=" refreshParentInbox();">
	<s:form name="deactivate" >
		<div style="margin-left: 30px">
		<table border="1" width="100%" cellpadding="0" cellspacing="0" style="margin-left: 8px">
			<div class="formmainbox"></div>
			  <div class="formheading"></div>
					<div class="headingbg">
						<s:text name="deactivate.prop.ack"/>
					</div>
					<table  border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td class="bluebox">
								&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
					        <td colspan="5" style="background-color: #FDF7F0;font-size: 13px;" align="center">
					        	<span class="bold"> <s:property value="%{ackMessage}"/>
					        	</span>
					       </td>
						</tr>
						<tr>
							<td class="bluebox">
								&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						<tr style="background-color:#E4E4E4; padding-top: 5px; padding-bottom: 5px" height="40px">
					        <td colspan="5" css="greybox" align="center">
					        	<input type="button" class="button" value="Close" onclick="window.close()">
					       </td>
						</tr>
					</table>
		</table>
		</div>
	</s:form>
	</body>
</html>
