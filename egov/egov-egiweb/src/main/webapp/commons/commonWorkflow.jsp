<!-- #-------------------------------------------------------------------------------
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
#------------------------------------------------------------------------------- -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>


function getUsersByDesignationAndDept()
{
	populateapproverPositionId({approverDepartmentId:document.getElementById('approverDepartment').value,designationId:document.getElementById('approverDesignation').value});
}

function callAlertForDepartment()
{
    var value=document.getElementById("approverDepartment").value;
	if(value=="-1")
	{
		alert("Please select the Approver Department");
		document.getElementById("approverDepartment").focus();
		return false;
	}
}

function callAlertForDesignation()
{
	var value=document.getElementById("approverDesignation").value;
	if(value=="-1")
	{
		alert("Please select the approver designation");
		document.getElementById("approverDesignation").focus();
		return false;
	}
}
	
function loadDesignationByDeptAndType(typeValue,departmentValue,currentStateValue,amountRuleValue,additionalRuleValue,pendingActionsValue)
{
	  var designationObj =document.getElementById('approverDesignation');
	  designationObj.options.length = 0;
	  designationObj.options[0] = new Option("----Choose----","-1");
	  var approverObj = document.getElementById('approverPositionId');
	  approverObj.options.length = 0;
	  approverObj.options[0] = new Option("----Choose----","-1");
	  populateapproverDesignation({departmentRule:departmentValue,type:typeValue,amountRule:amountRuleValue,additionalRule:additionalRuleValue,
	  													currentState:currentStateValue,pendingAction:pendingActionsValue});
}

function loadDesignationByDeptAndTypeWithDate(typeValue,departmentValue,currentStateValue,amountRuleValue,additionalRuleValue,pendingActionsValue,date)
{
	  var designationObj =document.getElementById('approverDesignation');
	  designationObj.options.length = 0;
	  designationObj.options[0] = new Option("----Choose----","-1");
	  var approverObj = document.getElementById('approverPositionId');
	  approverObj.options.length = 0;
	  approverObj.options[0] = new Option("----Choose----","-1");
	  populateapproverDesignation({departmentRule:departmentValue,type:typeValue,amountRule:amountRuleValue,additionalRule:additionalRuleValue,
	  													currentState:currentStateValue,pendingAction:pendingActionsValue,date:date});
}

/**
 *   This method is to validate Approver
 */
	
function validateWorkFlowApprover(name)
{
    document.getElementById('workFlowAction').value=name;
	<s:if test="%{getNextAction()!='END'}">
    if((name=="Forward" || name=="Approve" || name=="approve" || name=="forward") && document.getElementById('approverPositionId').value=="-1")
    {
        alert("Please Select the Approver ");
		return false;
    }
    </s:if>
	
    return true;
}
function validateWorkFlowApprover(name,errorDivId)
{
	document.getElementById(errorDivId).style.display='none';
    document.getElementById('workFlowAction').value=name;
	<s:if test="%{getNextAction()!='END'}">
    if((name=="Forward" || name=="Approve" || name=="approve" || name=="forward") && 
    		document.getElementById('approverPositionId').value=="-1")
    {
        document.getElementById(errorDivId).style.display='';
        document.getElementById(errorDivId).innerHTML = "Please Select the Approver";
		return false;
    }
    </s:if>
    return true;
}
	
	
</script>
<s:hidden id="workFlowAction" name="workFlowAction"/>
<s:if test="%{getNextAction()!='END'}">
<div class="blueshadow"></div>
<s:hidden id="currentState" name="currentState" value="%{state.value}"/>
<s:hidden id="currentDesignation" name="currentDesignation" value="%{currentDesignation}"/>
<s:hidden id="additionalRule" name="additionalRule" value="%{additionalRule}"/>
<s:hidden id="amountRule" name="amountRule" value="%{amountRule}"/>
<s:hidden id="workFlowDepartment" name="workFlowDepartment" value="%{workFlowDepartment}"/>
<s:hidden id="pendingActions" name="pendingActions" value="%{pendingActions}"/>
 <h1 class="${approverHeadCSS}" >Approval Authority Information</h1>
	  	 <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  	 	 <tr>   
	  	 	 <td class="${approverCSS}" width="16%">&nbsp;</td>
			 <td class="${approverCSS}" id="deptLabel"><s:text name="workflow.approver.department"/></td>
			 <td class="${approverCSS}">
				<s:select name="approverDepartment" id="approverDepartment" list="dropdownData.approverDepartmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{approverDepartment}"  onchange="loadDesignationFromMatrix();"/>
				<egov:ajaxdropdown fields="['Text','Value']" url="workflow/ajaxWorkFlow!getDesignationsByObjectType.action" id="approverDesignation" dropdownId="approverDesignation" contextToBeUsed="/egi"/>
			</td>
			<td class="${approverCSS}" width="10%">&nbsp;</td>	  
			<td class="${approverCSS}" width="14%">Approver Designation:</td>
			<td class="${approverCSS}" width="33%">
				<s:select id="approverDesignation" name="approverDesignation" list="dropdownData.desgnationList" listKey="designationId" headerKey="-1" listValue="designationName" headerValue="----Choose----" onchange="populateApprover();" onfocus="callAlertForDepartment();"/>
				<egov:ajaxdropdown id="approverPositionId" fields="['Text','Value']" dropdownId="approverPositionId" url="workflow/ajaxWorkFlow!getPositionByPassingDesigId.action" contextToBeUsed="/egi" />
			</td>
			<td class="${approverCSS}" width="10%">Approver:</td>
			<td class="${approverCSS}" width="16%">
			  	<s:select id="approverPositionId"  name="approverPositionId" list="dropdownData.approverList" headerKey="-1" headerValue="----Choose----" listKey="id" listValue="firstName"  onfocus="callAlertForDesignation();" value="%{approverPositionId}" /></td> 
			<td class="${approverCSS}" width="16%">&nbsp;</td>
			 </tr>
		</table>
</s:if>


 <div id="workflowCommentsDiv" align="center">
         <table width="100%">
         <tr>
           <td width="10%" class="${approverCSS}">&nbsp;</td>
            <td width="20%" class="${approverCSS}">&nbsp;</td>
           <td class="${approverCSS}" width="13%"> Approver Comments: </td>
           <td class="${approverCSS}"> 
           	<textarea id="approverComments" name="approverComments" rows="2" cols="35" ></textarea>  
           </td>
           <td class="${approverCSS}">&nbsp;</td>
           <td width="10%" class="${approverCSS}">&nbsp;</td>
           <td  class="${approverCSS}">&nbsp;</td>
           </tr>
         </table>
  </div>
