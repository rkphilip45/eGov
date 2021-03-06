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
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
	<head>
		<title><s:text name="SearchNotice.title"/></title>
		<script type="text/javascript">
			function populateWard() {
				populatewardId( {
					zoneId : document.getElementById("zoneId").value
				});
			}
			function loadOnStartUp() { 
				if(document.getElementById("noticeFDate")==null || document.getElementById("noticeFDate").value=="DD/MM/YYYY" 
						|| document.getElementById("noticeFDate").value=="") {
					waterMarkInitialize('noticeFDate','DD/MM/YYYY');
				}
				if(document.getElementById("noticeTDate")==null || document.getElementById("noticeTDate").value=="DD/MM/YYYY" 
						|| document.getElementById("noticeTDate").value=="") {
					waterMarkInitialize('noticeTDate','DD/MM/YYYY');
				}
			} 
	 		function performBeforeSubmit() {
	 			if(document.getElementById("noticeFDate").value=='DD/MM/YYYY') {
	 				document.getElementById("noticeFDate").value = "";
	 			}
	 			if(document.getElementById("noticeTDate").value=='DD/MM/YYYY') {
	 				document.getElementById("noticeTDate").value = "";
	 			}
	 		}
	 		
	 		function displayNotice(noticeNumber, isBlob) {
		 		var sUrl;
		 		if(isBlob=='N'){
		 			sUrl = "/egi/docmgmt/ajaxFileDownload.action?moduleName=PT&docNumber="+noticeNumber+"&fileName="+noticeNumber+".pdf";
			 	} else {
			 		sUrl = "/ptis/reports/searchNotices!showNotice.action?noticeNumber="+noticeNumber;
				}
 				window.open(sUrl,"window",'scrollbars=yes,resizable=no,height=200,width=400,status=yes');
 			}
		</script>
		<sx:head/>
	</head>
	<body onload="loadOnStartUp();">
		<div align="left">
  			<s:actionerror/>
  			<s:fielderror />
  		</div>
		<s:form action="searchNotices" theme="simple" validate="true">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg"><s:text name="SearchNoticeHeader"/></div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td  width="15%">&nbsp;</td>
					<td  width="30%"><s:text name="OwnerName"/> :</td>
					<td ><s:textfield  name="ownerName" maxlength="512" onblur="trim(this,this.value);checkNotSpecialCharForName(this);"/></td>
					<td  colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="Zone"/> :</td>
					<td>
						<s:select name="zoneId" id="zoneId" list="dropdownData.Zone"
							listKey="id" listValue="name" headerKey="-1"
							headerValue="%{getText('default.select')}" value="%{zoneId}"
							onchange="populateWard()" />
						<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
							dropdownId="wardId" url="common/ajaxCommon!wardByZone.action" />
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="Ward"/> :</td>
					<td><s:select name="wardId" id="wardId"
							list="dropdownData.wardList" listKey="id" listValue="name"
							headerKey="-1" headerValue="%{getText('default.select')}"
							value="%{wardId}" onchange="return populatePartNumbers(this.id);" />
					</td>
					<td  colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<%@ include file="../common/partnumber.jsp" %>
					<td></td>
					<td colspan="2">&nbsp;</td>	
				</tr>
				<script type="text/javascript">
					<s:if test="%{partNo != null}">
						var wardDropdown = document.getElementById('partNumbers');
						var selectedPartNo = <s:property value="%{partNo}" />;
						populatePartNumbers('wardId');
						var selectedIndex = 0;
						for (var i = 0; i < wardDropdown.options.length; i++) {
							if (wardDropdown.options[i].value == selectedPartNo) {
								selectedIndex = i;
								break;
							}
						}
						wardDropdown.selectedIndex = selectedIndex;
					</s:if>
				</script>
				<tr>  
					<td >&nbsp;</td>
	    			<td ><s:text name="PropertyType"/> : </td>
	    			<td >
						<s:select name="propertyType" id="propTypeMaster" list="dropdownData.PropTypeMaster" listKey="id" listValue="type" 
							headerKey="-1" headerValue="%{getText('default.select')}" value="%{propertyType}"/>
	    			</td>
	    			<td  colspan="2">&nbsp;</td>
				</tr>
				<tr>  
					<td >&nbsp;</td>
	    			<td ><s:text name="NoticeType"/><span class="mandatory1">*</span> : </td>
	    			<td >
						<s:select name="noticeType" id="noticeType" list="noticeTypeMap"
							listKey="key" listValue="value" headerKey="-1" headerValue="%{getText('default.select')}"/>
	    			</td>
	    			<td  colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td >&nbsp;</td>
					<td ><s:text name="noticeNum"/> :</td>
					<td ><s:textfield  name="noticeNumber" onblur="trim(this,this.value);"/></td>
					<td  colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="noticeDateFrom"/> :</td>
					<td>
					<s:date name="noticeFromDate" var="noticeFromDt" format="dd/MM/yyyy"/>
					<s:textfield  name="noticeFromDate" id="noticeFDate" maxlength="10" 
							onkeyup="DateFormat(this,this.value,event,false,'3')" onfocus = "waterMarkTextIn('noticeFDate','DD/MM/YYYY');" 
        					onblur="validateDateFormat(this);waterMarkTextOut('noticeFDate','DD/MM/YYYY');" value="%{noticeFromDt}"/>
        		</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="noticeDateTo"/> :</td>
					<td>
					<s:date name="noticeToDate" var="noticeToDt" format="dd/MM/yyyy"/>
					<s:textfield  name="noticeToDate" id="noticeTDate" maxlength="10" 
							onkeyup="DateFormat(this,this.value,event,false,'3')" onfocus = "waterMarkTextIn('noticeTDate','DD/MM/YYYY');" 
        					onblur="validateDateFormat(this);waterMarkTextOut('noticeTDate','DD/MM/YYYY');" value="%{noticeToDt}"/>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="prop.Id"/> :</td>
					<td><s:textfield  name="indexNumber" onblur="trim(this,this.value);" value="%{indexNumber}" maxlength="30"/></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><s:text name="HouseNo"/> :</td>
					<td><s:textfield  name="houseNumber" onblur="trim(this,this.value);" value="%{houseNumber}"/></td>
					<td colspan="2">&nbsp;</td>
				</tr>
	</table>
	</div>
	<div class="buttonbottom" align="center">
		<tr>
		 <td><s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" value="Search" method="search" onclick="return performBeforeSubmit();"/></td>
		 <td><s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" value="Merge & Download" method="mergeAndDownload" onclick="return performBeforeSubmit();" /></td>
		 <td><s:submit name="button32" type="submit" cssClass="buttonsubmit" id="button32" value="Zip & Download" method="zipAndDownload" onclick="return performBeforeSubmit();" /></td>
		 <td><s:submit type="submit" cssClass="button" value="Reset" method="reset" onclick="return performBeforeSubmit();"/></td>
		 <td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</tr>
	</div>
	</s:form>

	<logic:notEmpty name="noticeList">
			
		<display:table name="searchResult" uid="currentRowObject" pagesize = "20" class="tablebottom" style="width:100%;" cellpadding="0" cellspacing="0" export="true" requestURI="">
			
		<display:caption>
		<div class="headingsmallbgnew" align="center" style="text-align:center;width:98%;">
			<span class="searchvalue1">Search Criteria :</span> 
			<s:if test="noticeType!='-1'"><s:text name="NoticeType"/>: <span class="mandatory"><s:property value="noticeType"/></span></s:if>
			<s:if test="ownerName!=''">, <s:text name="OwnerName"/>: <span class="mandatory"><s:property value="ownerName"/></span></s:if>
			<s:if test="zoneId!='-1'">, <s:text name="Zone"/>: <span class="mandatory"><s:property value="%{getBoundary(zoneId)}" /></span></s:if>
			<s:if test="wardId!='-1'">, <s:text name="Ward"/>: <span class="mandatory"><s:property value="%{getBoundary(wardId)}" /></span></s:if>
			<s:if test="propertyType!='-1'">, <s:text name="PropertyType"/>: <span class="mandatory"><s:property value="%{getPropType(propertyType)}"/></span></s:if>
			<s:if test="noticeNumber!=''">, <s:text name="noticeNum"/>: <span class="mandatory"><s:property value="noticeNumber"/></span></s:if>
			<s:if test="noticeFromDate!=null">, 
				<s:text name="noticeDateFrom"/>: 
				<s:date name="noticeFromDate" var="FromDateFormat" format="dd/MM/yyyy"/>
				<span class="mandatory"><s:property value="FromDateFormat"/></span>
			</s:if>
			<s:if test="noticeToDate!=null">, 
				<s:text name="noticeDateTo"/>: 
				<s:date name="noticeToDate" var="ToDateFormat" format="dd/MM/yyyy"/>
				<span class="mandatory"><s:property value="ToDateFormat"/></span>
			</s:if>
			<s:if test="indexNumber!=''">, <s:text name="prop.Id"/>: <span class="mandatory"><s:property value="indexNumber"/></span></s:if>
			<s:if test="houseNumber!=''">, <s:text name="HouseNo"/>: <span class="mandatory"><s:property value="houseNumber"/></span></s:if>
			</div>
		</display:caption>	
		
		<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Notice Type/ &#2344;&#2379;&#2335;&#2368;&#2360;&#2330;&#2366; &#2346;&#2381;&#2352;&#2325;&#2366;&#2352;" 
		style="text-align:center;width:10%;" property="noticeType"/>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Notice Number/ &#2344;&#2379;&#2335;&#2368;&#2360; &#2325;&#2381;&#2352;&#2350;&#2366;&#2306;&#2325;" 
		style="text-align:center;width:10%;">
		<a href="javascript:displayNotice('<s:property value="#attr.currentRowObject.noticeNo"/>','<s:property value="#attr.currentRowObject.isBlob"/>')">
			 <s:property value="#attr.currentRowObject.noticeNo"/>
		</a> 
			
		</display:column>
		
		<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Date of Issue of Notice" style="text-align:center;width:10%;">
		<s:date name="#attr.currentRowObject.noticeDate" var="noticeDt" format="dd/MM/yyyy"/>
		<s:property value="noticeDt" />
		</display:column >
		
		<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Index Number/ &#2311;&#2306;&#2337;&#2375;&#2325;&#2381;&#2360; &#2325;&#2381;&#2352;&#2350;&#2366;&#2306;&#2325;"
		 style="text-align:center;width:10%;">
		<s:iterator status="stat1" value="#attr.currentRowObject.basicProperty" >
    		<s:property value="upicNo"/>
    	</s:iterator>
    	</display:column>
    	
    	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="House Number" style="text-align:center;width:10%;">
    	<s:iterator status="stat1" value="#attr.currentRowObject.basicProperty.address">
    		<s:property value="houseNo"/>
    	</s:iterator>
    	</display:column>
  		
  		<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Name(s) of Owner" style="text-align:center;width:10%;">
  			<s:property value="%{getNonHistoryOwnerName(#attr.currentRowObject.basicProperty)}" />
		</display:column>
		
		<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Property Address/ &#2328;&#2352;&#2366;&#2330;&#2366;/&#2350;&#2366;&#2354;&#2350;&#2340;&#2381;&#2340;&#2375;&#2330;&#2366; &#2346;&#2340;&#2381;&#2340;&#2366;"
		style="text-align:center;width:10%;">
		<s:iterator status="stat1" value="#attr.currentRowObject.basicProperty.address">
    		<s:property value="houseNo"/>
    		<s:if test="doorNumOld!=null">
    		 ,<s:property value="doorNumOld"/> 
    		</s:if>
    		<s:if test="block!=null">
    		 ,<s:property value="block"/> 
    		</s:if>
    		<s:if test="pinCode!=null">
    		 ,<s:property value="pinCode"/>
    		</s:if>
    	</s:iterator>
		</display:column>
    	
		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.excel.filename" value="propertyTax-noticeReports.xls"/>
		<display:setProperty name="export.xml" value="false" />
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="propertyTax-noticeReports.pdf"/>


		</display:table>	
	</logic:notEmpty>
	
	<logic:empty name="noticeList">
			<s:if test="target=='searchresult'">
					<div class="headingsmallbgnew" style="text-align:center;width:100%;"><s:text name="searchresult.norecord"/></div>
			</s:if>
	</logic:empty>
	<script type="text/javascript">
  		paintAlternateColorForRows();
  	</script>
	</body>
</html>
