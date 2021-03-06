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
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='CreateAck.title'/></title>
  <script type="text/javascript">
  </script>
  </head>
  
  <body onload=" refreshParentInbox();" >
  <s:form name="CreatePropertyAckForm" theme="simple">
  <s:push value="model">
  <s:token />
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name="CreatePropertyAckHeader"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<s:if test="%{basicProperty.upicNo!=null && !basicProperty.upicNo.isEmpty()}">
				<td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
		        	<span class="bold"><s:property value="%{ackMessage}"/></span>
		        	<a href='../view/viewProperty!viewForm.action?propertyId=<s:property value="%{basicProperty.upicNo}"/>' >
		        		<s:property value="%{basicProperty.upicNo}"/>
		        	</a>
		        </td>		    
		   	</s:if>
	        <s:else>
		       	<td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
		        	<span class="bold"><s:property value="%{ackMessage}"/></span>
		        </td>
	        </s:else>
		</tr>
		</table>
	</div>
	<div class="buttonbottom" align="center">
			<s:if test="%{userDesgn.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@ASSISTANT_DESGN)}">
				<td><s:submit value="Create" name="Create" id="Create"  method="newForm" cssClass="buttonsubmit" /></td>
			</s:if>
			<s:if test="%{basicProperty.upicNo!=null && !basicProperty.upicNo.isEmpty()}">
				<td>
					<input type="button" name="button2" id="button2"
							value="GenerateCalSheet" class="button"
							onclick="window.location='../notice/propertyIndividualCalSheet!generateCalSheet.action?indexNum=<s:property value="%{basicProperty.upicNo}"/>';" />
				</td>
			</s:if>
			<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</div>
  </s:push>
  </s:form>
  </body>
</html>
