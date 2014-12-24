<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

<%@ page language="java"%>
<html>

<head>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/accountCheque.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<title>Account Cheque Create</title>

</head>

	
<body onload="clearHeaderData();" >

			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Account Cheque Create" />
			</jsp:include>
	<s:form action="accountCheque" theme="simple" name="chequeMaster" >	
			<s:if test="hasActionMessages()">
   				<font  style='color: green ; font-weight:bold '> 
     					<s:actionmessage/>
   				</font>
			</s:if>
		<div class="formheading"/><div class="subheadnew">Cheque Master</div>
		<br>
		<s:if test="%{bankaccount != null}">
		<table width="100%" cellspacing="0" cellpadding="0" border="0"  align="center">
		<tr align="center">
	
			<td  align="center" class="subheadcustom" style="text-align:center"><font style='font-weight:bold;'> Bank Details</font></p>
		</td>
	
		</tr>
	</table>
			<table border="0" width="100%">
				
				<tr>
					<td class="bluebox">Bank </td>
					<td class="bluebox"><s:property value="bankaccount.bankbranch.bank.name"/></td>
					<td class="bluebox">Bank Branch</td>
					<td class="bluebox"><s:property value="bankaccount.bankbranch.branchname"/></td>
				</tr>
				<tr>
					<td class="greybox">Account Number </td>
					<td class="greybox"><s:property value="bankaccount.accountnumber"/></td>
					<td class="greybox">Fund</td>
					<td class="greybox"><s:property value="bankaccount.fund.name"/></td>
				</tr>
			</table>
				
			<s:hidden name="bankAccId" id="bankAccId" value="%{bankaccount.id}"/>
		</s:if>
<br>
<table width="100%" cellspacing="0" cellpadding="0" border="0"  align="center">
		<tr align="center">
	
			<td  align="center" class="subheadcustom" style="text-align:center"><font style='font-weight:bold;'> Add New Cheque </font></p>
		</td>
	
		</tr>
	</table>
<font  style='color: red ; font-weight:bold '> 
<p class="error-block" id="lblError" ></p></font>
<table border="0" width="100%">
		
		<tr>
			
			<td class="greybox" width="25%">From Cheque Number<span class="mandatory">*</span></td>
			<td class="greybox" width="25%"><s:textfield name="fromChqNo" id="fromChqNo" maxlength="18" onkeyup="validateOnlyNumber(this);" /></td>
			<td class="greybox" width="25%">To Cheque Number<span class="mandatory">*</span></td>
			<td class="greybox" width="25%" ><s:textfield name="toChqNo" id="toChqNo" maxlength="18" onkeyup="validateOnlyNumber(this);" 
/></td>
			
		</tr>

	<tr>
			
			<td class="bluebox" width="25%">Received Date<span class="mandatory">*</span></td>
			<td class="bluebox" width="25%"><s:textfield name="receivedDate" id="receivedDate" maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" />
<a href="javascript:show_calendar('chequeMaster.receivedDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img  src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
</td>
			<td class="bluebox" width="25%">Department<span class="mandatory">*</span></td>
			<td class="bluebox" width="25%" ><s:select name="departmentList" id="departmentList" list="dropdownData.departmentList" listKey="id" listValue="deptName"  multiple="true" size="6" required="true"/></td>
			
		</tr>
	
</table>
<table width="90%" cellspacing="0" cellpadding="0" border="0"  align="center">
		<tr align="center">
	
	<td  align="center"  style="text-align:center"><input type="button" name="Done" onclick="updateGridData()" class="buttongeneral" value="Click to add new cheque" align="middle"/>
		</td>
	
	</tr>
</table>

<br>

<jsp:include page="accountChequeChequeDetailGrid.jsp"/>
	
	 		<table width="100%" cellspacing="0" cellpadding="0" border="0"  align="center">
		<tr align="center">
	
			<td  align="center" class="subheadcustom" style="text-align:center"><font style='font-weight:bold;'>Existing Cheque Details</font></p>
		</td>
	
		</tr>
	</table>

		<font  style='color: red ; font-weight:bold '> 
		<p class="error-block" id="lblErrorGrid" ></p></font>


<div class="yui-skin-sam" align="center">
       <div id="chequeDetailsGridTable" >
    

     <script>
		
		makeChequeDetailsGridTable();
		document.getElementById('chequeDetailsGridTable').getElementsByTagName('table')[0].width="30%"
	 </script>
<br>
<div class="buttonbottom">
<s:submit type="submit" cssClass="buttonsubmit" value="Save" id="save" name="save" method="save" />
<input type="button" id="Close" value="Close" onclick="javascript:window.close()" class="button"/>
 </div>
 </div></div>
<s:hidden name="deletedChqDeptId" id="deletedChqDeptId"/>
<br>
<s:token/>
</s:form>

</body>

</html>