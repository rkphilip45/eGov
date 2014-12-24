<%@ page language="java"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page import="java.util.*,
		org.apache.log4j.Logger,org.egov.infstr.utils.EgovMasterDataCaching,
		org.egov.commons.EgPartytype,org.egov.commons.EgwTypeOfWork,
		java.text.DecimalFormat,org.egov.lib.deduction.client.PartyTypeForm"
%>

<html>
<head>
	
	<title>PartyType Master Setup</title>
	
<%  
PartyTypeForm ptform=(PartyTypeForm)request.getAttribute("PartyTypeForm"); 
%>
<script>
var myrowId;
function ButtonPress(arg)
{		
	if(!validatePartyTypeForm(document.PartyTypeForm))
		return; 
		
		
	if(document.PartyTypeForm.parentId.options[document.PartyTypeForm.parentId.selectedIndex].value!=0)
	{
		var codeVal=document.PartyTypeForm.code.value;
		if(codeVal != "")
		{
		var parentCode=document.PartyTypeForm.parentId.options[document.PartyTypeForm.parentId.selectedIndex].text;
			if(parentCode==codeVal)
			{
				alert("Code and Parent Code cannot be same!!!!");
				return false;
			}
		
		}
		
	}
	
	document.getElementById("button").value=arg;
	var mode="${mode}";
	if(mode == "create")
	{					
		document.PartyTypeForm.action = "../deduction/PartyTypeMaster.do?submitType=createPartyType";
		document.PartyTypeForm.submit();		
	}
	if(mode != "create")
	{			
		document.PartyTypeForm.action = "../deduction/PartyTypeMaster.do?submitType=modifyPartyType";
		document.PartyTypeForm.submit();		
	}
}
function uniqueCheckForPartyCode()
{
	<% 
	if(ptform.getCode()==null)
	{%>

		// For create Mode	
		booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'eg_partytype', 'CODE', 'code', 'no', 'no');

			if(booleanValue==false)
			{
				alert("This Code already used for some other Party Type!!!!");
				return false;
			}
	<%
	}
	else
	{
	%>
		// For Modify Mode
		var partyCodeNew=document.getElementById('code').value;
		var partyCodeOld="<%=(ptform.getCode())%>";

		if(partyCodeNew!=partyCodeOld)
		{
			booleanValue=uniqueCheckingBoolean('../commonyui/egov/uniqueCheckAjax.jsp', 'eg_partytype', 'CODE', 'code', 'no', 'no');

			if(booleanValue==false)
			{
			alert("This Code already used for some other Party Type!!!!");
			return false;
			}		
		}	

	<%
	}
	%>
}

function setDefault()
{
	<% 
		ArrayList partyMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-partyTypeMaster");
	%>
	
	var target='<%=(request.getAttribute("alertMessage"))%>';
	if(target!="null")
	{
		alert('<%=request.getAttribute("alertMessage")%>');
	}
	
	var buttonType="${buttonType}";		
	if(buttonType == "saveclose")
		window.close();
			
	var mode="${mode}";
	if(mode == "create")
	{		
		document.getElementById("row2").style.display="block";	
		document.getElementById("row3").style.display="none";
		document.getElementById("row4").style.display="none";		
	}
	
	if (target!="null")
	
	{
	 	window.location= "../deduction/PartyTypeMaster.do?submitType=beforeCreate";		
	}
	
	if(mode == "modify")
	{
		document.title='Modify Party Type';
		//document.getElementById('screenName').innerHTML='Modify Party Type';
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="block";
		document.getElementById("row4").style.display="none";		
	}
	if(mode == "view")
	{
		document.title='View Party Type';
		//document.getElementById('screenName').innerHTML='View Party Type';
		document.getElementById("row4").style.display="block";
		document.getElementById("row2").style.display="none";	
		document.getElementById("row3").style.display="none";	
		
		for(var i=0;i<document.PartyTypeForm.length;i++)
		{
			if(document.PartyTypeForm.elements[i].value != "Close")
			{
				document.PartyTypeForm.elements[i].disabled =true;
			}					
		}
	}
}

function onClickCancel()
{
	document.PartyTypeForm.reset();
}	


</script>
</head>
<body onload="setDefault()">

<html:form  action="/deduction/PartyTypeMaster.do" >
<table align='center' class="tableStyle"> 

 <tr><td colspan=4>&nbsp;</td></tr>
<tr>
<html:hidden property="id" />
<td class="txt" align="right" >Code<SPAN class="leadon">*&nbsp;</SPAN></td>
<td class="fieldcell"><html:text onblur="uniqueCheckForPartyCode();" property= "code" maxlength="20"/></td> 
</tr>
<tr>
<td height="43" class="txt" align="right" >Parent Code&nbsp;</td>
<td class="smallfieldcell"  width="25%" height="43">
	 <html:select property="parentId" styleClass="combowidth" style="width:150px">
		<html:option value='0'>--Choose--</html:option>
		<c:forEach var="ptype" items="<%=partyMasterList%>" > 
		<html:option value="${ptype.id}">${ptype.name}</html:option> 
	</c:forEach>
    	</html:select> </td>
<td></td>
<td></td>
</tr>

<tr>
<td class="labelcell" align="right" width="25%">Description<SPAN class="leadon">*&nbsp;</SPAN></td>
<td class="smallfieldcell" align="center" width="25%"><html:textarea property="description" styleClass="combowidthforGLCode" /></td>
</tr>
<tr>
<td>

<tr><td>&nbsp;</td></tr> 
<tr  id="row2" name="row2">
<td  align="center" colspan=4>
<input type=hidden name="button" id="button"/>
<html:button styleClass="button" value="Save & Close" property="b2" onclick="ButtonPress('saveclose')" />
<html:button styleClass="button" value="Save & New" property="b4" onclick="ButtonPress('savenew')" />
<html:reset styleClass="button" value="Cancel" property="b1" onclick="onClickCancel()" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close();" />
</td>
</tr>
<tr style="DISPLAY: none" id="row3" name="row3">
<td  align="center" colspan=4>
<html:button styleClass="button" value=" Save " property="b4" onclick="ButtonPress('saveclose')" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close();" />
</td>
</tr>
<tr style="DISPLAY: none" id="row4" name="row4">
<td  align="center" colspan=4>
<html:button styleClass="button" value="Close" property="b3" onclick="window.close();" />
</td>
</tr>

</td>
</tr>
</table>
<html:javascript formName="PartyTypeForm"/> 
</html:form>

</html>	