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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<s:if	test="basicProperty.objections.size()>0">
		<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<s:text name="objection.details.heading" />
				</div>
			</td>
		</tr>
		<tr>
				<td colspan="5" class="bluebgheadtd">

				<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" class="tablebottom">
        	 <tr> 
        	 <th class="bluebgheadtd"><s:text name="slno" /> </th>
        	 <th class="bluebgheadtd"><s:text name="objection.number" /> </th>
        	 <th class="bluebgheadtd"><s:text name="objection.received.date" /> </th>
        	 <th class="bluebgheadtd"><s:text name="objection.received.by"/></th>
        	 
        	 <th class="bluebgheadtd"><s:text name="remarks.head"/></th>
        	 <th class="bluebgheadtd"><s:text name="objection.status"/></th></tr>
        	 <s:iterator var="s" value="basicProperty.objections" status="status">
			<tr>
			<td class="greybox"><div align="center"><s:property value="#status.index+1" /></div></td>
			 <td class="greybox" >
			 <s:hidden name="id" value="%{id}"></s:hidden>
			 <a href="${pageContext.request.contextPath}/objection/objection!viewObjectionDetails.action?objection.objectionNumber=<s:property value="%{objectionNumber}"/>">
			 <s:property value="%{objectionNumber}"/></a> </td>	
			    <td class="greybox" ><div align="center"><s:property default="N/A" value="%{fmtdReceivedOn}" /></div></td>			
			<td class="greybox" ><div align="center"><s:property default="N/A" value="%{recievedBy}"/></div></td>
			
			<td  class="greybox" ><div align="center"><s:property default="N/A" value="%{remarks}"/></div></td>
			<td  class="greybox" ><div align="center"><s:property default="N/A" value="%{egwStatus.description}"/>
			</div></td>
			</tr></s:iterator>
			</table>
			</td>
			</tr>
</s:if>
