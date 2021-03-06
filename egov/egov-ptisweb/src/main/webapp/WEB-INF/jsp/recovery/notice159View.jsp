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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<!--View Property Details -  Start   -->

	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="notice159.title"></s:text>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th class="bluebgheadtd">
						<s:text name="execution.date.head"></s:text>
					</th>
					<th class="bluebgheadtd">
						<s:text name="remarks.head"></s:text>
					</th>
					
					<th class="bluebgheadtd">
						<s:text name="notice159.head"></s:text>
					</th>
				</tr>
				<tr>
					<s:date name="ceaseNotice.executionDate" var="executionDateId" format="dd/MM/yyyy" />
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{executionDateId}" />
								</div>
							</td>
					<td class="greybox">
						<div align="center">
							<s:property value="%{ceaseNotice.remarks}" />
						</div>
					</td>
					
					<td class="greybox">
					<s:if test="%{ceaseNotice.notice != null}">
						<div align="center">
							 <a href="#" onclick="displayNotice('<s:property value='%{ceaseNotice.notice.noticeNo}'/>');"> <s:property value="%{ceaseNotice.notice.noticeNo}" /> </a>
						</div>
					</s:if><s:else>N/A </s:else>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
