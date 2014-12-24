<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name="ChBaseRateArea.title"></s:text> </title>
	<sx:head/>
	<script type="text/javascript">

		function populateWard() {
			populatewardId( {
				zoneId : document.getElementById("zoneId").value
			});
		}
		function populateArea() {
			populateareaId( {
				wardId : document.getElementById("wardId").value
			});
		}			

	</script>
</head>  
<body>
  <div align="left">
  	<s:actionerror/>
  </div>
  
  <s:form name="ChBaseRateAreaForm" action="../admin/changeStreetRate.action" theme="simple" method="post">
  <div class="formmainbox">
  	<div class="formheading"></div>
	<div class="headingbg"><s:text name="ChBaseRateAreaHeader"/></div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="greybox" width="100%" colspan="5">
				<center>
					<table border="0" cellspacing="0" cellpadding="0" width="300px">
						<tr>
							<td class="greybox" width="10px">
								<s:text name="Zone" />
								<span class="mandatory1">*</span> :
							</td>
							<td class="greybox" width="20%">
								<s:select headerKey="-1" headerValue="%{getText('default.select')}"
									name="zoneId" id="zoneId" listKey="id" listValue="name"
									list="dropdownData.Zone" cssClass="selectnew"
									onchange="populateWard();" value="%{zoneId}" cssStyle="width:150px" />
							</td>
						</tr>
					</table>
				</center>
			</td>
		</tr>
		<tr>
			<td class="bluebox" width="100%" colspan="5">
				<center>
					<table border="0" cellspacing="0" cellpadding="0" width="300px">
						<tr>
							<egov:ajaxdropdown id="wardId" fields="['Text','Value']"
								dropdownId="wardId" url="common/ajaxCommon!wardByZone.action" />
							<td class="bluebox" width="10px">
								<s:text name="Ward" />
								<span class="mandatory1">*</span> :
							</td>
							<td class="bluebox" width="20%">
								<s:select name="wardId" id="wardId" list="dropdownData.wardList"
									listKey="id" listValue="name" headerKey="-1"
									headerValue="%{getText('default.select')}"
									onchange="return populateArea();" value="%{wardId}" cssStyle="width:150px" />
							</td>
						</tr>
					</table>
				</center>
			</td>
		</tr>
		<tr>
			<td class="greybox" width="100%" colspan="5">
				<center>
					<table border="0" cellspacing="0" cellpadding="0" width="300px">
						<tr>
							<egov:ajaxdropdown id="areaId" fields="['Text','Value']" dropdownId="areaId" url="common/ajaxCommon!areaByWard.action" />
							<td class="greybox" width="10px"><s:text name="Area" /><span class="mandatory1">*</span> : </td>
							<td class="greybox" width="20%">
								<s:select name="areaId" id="areaId" list="dropdownData.areaList"
							    listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{areaId}" cssStyle="width:150px" />
							</td>
						</tr>
					</table>
				</center>
			</td>
		</tr>		
	  	<tr>
	  	<td class="bluebox" colspan="5">
	  	<div class="mandatory" align="left">
	  		<font size="2"><s:text name="mandtryFlds"/></font>
	  	</div>
	  	</td>
	  </tr>
	  </table>
	  <div class="buttonsearch" align="center">
		   	<s:submit cssClass="buttonsubmit" value="Search" method="search"/>
		   	<input type="button" name="button2" id="button2" value="Close" class="button" onclick="return confirmClose();"/>
	  </div>           	
	</div>
	</s:form>
</body>
</html>

