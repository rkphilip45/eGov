<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="EGF" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<link href="/EGF/cssnew/budget.css" rel="stylesheet" type="text/css" />
<link href="/EGF/cssnew/commonegovnew.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/EGF/cssnew/tabber.css" TYPE="text/css">
<script type="text/javascript" src="/EGF/javascript/tabber.js"></script>
<script type="text/javascript" src="/EGF/javascript/tabber2.js"></script>
<title> <s:text name="remit.recovery.create.title"/></title>
<script>
var vTypeOfAccount="RECEIPTS_PAYMENTS,PAYMENTS";                                 
function loadBank(fundId){
	populatebank({fundId:fundId.options[fundId.selectedIndex].value,typeOfAccount:vTypeOfAccount})	
}
function loadBankForFund(fundId){
	populatebank({fundId:fundId.options[fundId.selectedIndex].value})	
}
function validateFund(){
	var fund = document.getElementById('fundId').value;
	var bank = document.getElementById('bank');
	if(fund == -1 && bank.options.length==1){
		alert("Please select a Fund")
		return false;
	}
	return true;
}
function checkLength(obj)
{
	if(obj.value.length>1024)
	{
		alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function populateAccNumbers(bankBranch){
	var fund = document.getElementById('fundId');
	branchId = bankBranch.options[bankBranch.selectedIndex].value.split("-")[1];
	bankId = bankBranch.options[bankBranch.selectedIndex].value.split("-")[0];
	populatebankaccount({branchId:branchId,bankId:bankId,fundId:fund.options[fund.selectedIndex].value+'&date='+new Date(), typeOfAccount:vTypeOfAccount});	
}
function populateAccNumbersForId(bankBranchId){
	var fund = document.getElementById('fundId');
	populatebankaccount({branchId:bankBranchId,fundId:fund.options[fund.selectedIndex].value})	
}
function onLoadTask(){
	var fund = document.getElementById('fundId');
	selectedFund = '<s:property value="fund.id"/>';
	for(i=0;i<fund.options.length;i++){
		if(fund.options[i].value==selectedFund){
			fund.options[i].selected = true;
		}
	}
	document.getElementById('fundId').disabled=true;
	//document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	loadBank(document.getElementById('fundId'))
	<s:if test="%{bankaccount.id !=null}">
		var bank = document.getElementById('bank');
		selectedBank = '<s:property value="bankaccount.bankbranch.id"/>';
		for(i=0;i<bank.options.length;i++){
			if(bank.options[i].value.split('-')[1]==selectedBank){
				bank.options[i].selected = true;
			}
		}
	</s:if>
	<s:if test="%{bankaccount.id !=null}">
		selectedAccount ='<s:property value="bankaccount.id"/>';
		var bankAccount = document.getElementById('bankaccount');
		for(i=0;i<bankAccount.options.length;i++){
			if(bankAccount.options[i].value==selectedAccount){
				bankAccount.options[i].selected = true;
			}
		}
	</s:if>
}

function populateUser(){
	
	var desgFuncry = document.getElementById("designationId").value;
	var array = desgFuncry.split("-");
	var functionary = array[1];
	var desgId = array[0];
	if(desgId==""){ // when user doesnot selects any value in the designation drop down.
		desgId=-1;
	}
	populateapproverUserId({departmentId:document.getElementById("departmentid").value,
	designationId:desgId,functionaryName:functionary})
		
}
function validateApproveUser(name,value){
	document.getElementById("actionName").value= name;
<s:if test="%{wfitemstate !='END'}">
	 if( (value == 'Approve' || value=='Send for Approval' || value == 'Forward' || value == 'Save And Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
		alert("Please Select the user");
		return false;
	}
</s:if>
	return true;
}

	function validate(name,value)
		{
		document.getElementById('lblError').innerHTML = "";
			if(!validateMIS())
			  return false;
		   if(document.getElementById('bank').value=='-1')
		   {
		   document.getElementById('lblError').innerHTML='Please select Bank';
		   return false;
		   }
		   if(document.getElementById('bankaccount').value=='-1')
		   {
		   document.getElementById('lblError').innerHTML='Please select Bank Account';
		   return false;
		   } 
			if(!validateApproveUser(name,value))    
				return false;
			return true;
		}

</script>
</head>
<body>
	<s:form action="remitRecovery" theme="simple" name="remittanceForm"  >
	<s:push value="model">
		<jsp:include page="../budget/budgetHeader.jsp">
        	<jsp:param name="heading" value="Remittance Recovery" />
		</jsp:include>
		<div align="center">
<font  style='color: red ;'> 
<p class="error-block" id="lblError" ></p>
</font>
</div>
<span class="mandatory" >
				<div id="Errors" ><s:actionerror /><s:fielderror /></div>
				<s:actionmessage />
			</span>
		
		<div class="formmainbox"><div class="subheadnew"><s:text name="remit.recovery.create.title"/></div>
		<div id="budgetSearchGrid" style="display:block;width:100%;border-top:1px solid #ccc;" >
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
				<td>
				<div align="left"><br/>
  					<table border="0" cellspacing="0" cellpadding="0" width="100%">
        			<tr>
        			<td> 
		            <div class="tabber">
		            <div class="tabbertab" id="searchtab">
               			<h2><s:text name="remit.recovery.header"/></h2>
	                	<span>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						
							<tr><td align="center" colspan="6" class="serachbillhead"><s:text name="remit.recovery.header"/></td></tr>
							 <tr>
						   		 <td class="bluebox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('vouchernumber')}">
								<td class="bluebox"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
								<td class="bluebox"><s:textfield name="voucherNumber" id="vouchernumber" /></td>
							</s:if>
						   <td class="bluebox" width="18%"><s:text name="voucher.date"/>&nbsp;<span class="mandatory">*</span></td>
							<s:date name='voucherDate' id="voucherDateId" format='dd/MM/yyyy'/>
							<td class="bluebox" width="34%">
							<div name="daterow" >
							<s:textfield  name="voucherDate" id="voucherDate" maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="15" value="%{voucherDateId}"/><A href="javascript:show_calendar('forms[0].voucherDate',null,null,'DD/MM/YYYY');" style="text-decoration:none" align="left"><img  width="18" height="18" border="0" align="absmiddle" alt="Date" src="${pageContext.request.contextPath}/image/calendaricon.gif" /></A> </div></td> 
				 			</tr><tr>
							<jsp:include page="../voucher/vouchertrans-filter-new.jsp"/>
							
							</tr>
							<tr>
		<td class="bluebox">&nbsp;</td>
	    <egov:ajaxdropdown id="bank" fields="['Text','Value']" dropdownId="bank" url="voucher/common!ajaxLoadBanksByFundAndType.action" />
	    <td class="bluebox"><s:text name="bank"/>&nbsp;<span class="bluebox"><span class="mandatory">*</span></span></td>
	    <td class="bluebox">
	    	<s:select name="bank" id="bank" list="dropdownData.bankList" listKey="bank.id+'-'+id" listValue="bank.name+' '+branchname" headerKey="-1" headerValue="----Choose----" onclick="validateFund()" onChange="populateAccNumbers(this);"  />
	    </td>
	    <egov:ajaxdropdown id="accountNumber" fields="['Text','Value']" dropdownId="bankaccount" url="voucher/common!ajaxLoadAccNumAndType.action" />
		<td class="bluebox"><s:text name="account.number"/>&nbsp;<span class="bluebox"><span class="mandatory">*</span></span></td>
		<td class="bluebox">
			<s:select  name="commonBean.accountNumberId" id="bankaccount" list="dropdownData.accNumList" listKey="id" listValue="chartofaccounts.glcode+'--'+accountnumber+'--'+accounttype" headerKey="-1" headerValue="----Choose----"/>
		</td>
	</tr>
	  <tr class="greybox">
  <td class="greybox">&nbsp;</td>
    <td class="greybox">Payment Amount&nbsp;</td>
    <td class="greybox"><label name="remitAmount" id="remitAmount"/></td>
    <egov:updatevalues id="availableBalance" fields="['Text']" url="/payment/payment!ajaxGetAccountBalance.action"/>
		<td class="greybox"><span id="balanceText" style="display:none" width="18%"><s:text name="balance.available"/>&nbsp;</span></td>
		<td class="greybox"><span id="balanceAvl"  style="display:none" width="32%"><s:textfield name="commonBean.availableBalance" id="availableBalance" readonly="readonly" style="text-align:right" value="%{commonBean.availableBalance}"/></span></td>
  </tr>
  <tr>
	<td class="bluebox">&nbsp;</td>
	<td class="bluebox"><s:text name="modeofpayment"/>&nbsp;</td>
    <td class="bluebox"><s:radio name="modeOfPayment" id="paymentMode" list="%{modeOfCollectionMap}"/></td>
	<td/>
	<td/>
	</tr>
  <tr>
    <td class="greybox">&nbsp;</td>
    <td class="greybox">Narration</td>
    <td class="greybox" colspan="4"><textarea name="description" id="narration" type="text" style="width:580px;"></textarea></td>
	<td></td>
  </tr>  
</table>
        		</span>                  
              		</div>	
          			<div class="tabbertab" id="contractortab">
           			<h2><s:text name="remit.recovery.detais"/></h2>
                	<span>
					<table align="center" border="0" cellpadding="0" cellspacing="0" class="newtable">
						<tr><td colspan="6"><div class="subheadsmallnew"><s:text name="remit.recovery.detais"/></div></td></tr>
						<tr>
							<td colspan="6">
							<div  style="float:left; width:100%;">
					
							<jsp:include page="remitRecoveryPayment-form.jsp"/>
							<s:hidden  name="remittanceBean.recoveryId" />
								<div class="yui-skin-sam" align="center">
      								 <div id="recoveryDetailsTableNew"></div>
    							 </div>
     						<script>
								populateRecoveryDetailsForPayment();
								document.getElementById('recoveryDetailsTableNew').getElementsByTagName('table')[0].width="80%";
							 </script><br>
							</table>
							<table align="center" id="totalAmtTable">
							<tr >
							<td width="906"></td>
							<td >Total Amount</td>
							<td ><s:textfield name="remittanceBean.totalAmount" id="totalAmount" style='width:90px;text-align:right' readonly="true" value="0"/></td>
							</tr>
													
						</div>
						</td>
					</tr>
					</table>                    
               		</span>                
                	</div>
                
			</div> <!-- tabber div -->
			</td>
       		</tr>
      		</table>
		</div>
		</td>
		</tr>
		</table>
	</div>
	</div>
		<s:if test="%{wfitemstate !='END'}">
				<%@include file="../voucher/workflowApproval.jsp"%>
	</s:if>
	<table align="center">
	<tr>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox" >Comments</td>
		<td class="bluebox" colspan="4"><s:textarea name="comments" id="comments" cols="100" rows="3" onblur="checkLength(this)" value="%{getComments()}"/></td>
	</tr>
	</table>
	<div  class="buttonbottom" id="buttondiv">
		<s:hidden  name="paymentid" value="%{paymentheader.id}"/>
		<s:hidden  name="scriptName" id="scriptName" value="paymentHeader.nextDesg"/>
		<s:hidden  name="actionname" id="actionName" value="%{action}"/>
		<s:iterator value="%{getValidActions()}" var="p"  status="s">
		  <s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="create" onclick="return validate('%{name}','%{description}')"/>
		</s:iterator>
		<s:submit method="search" value="Back " cssClass="buttonsubmit" id="backbtnid"/>
		<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
	</div>
	<script type="text/javascript">
	//alert('<s:property value="fund.id"/>');                               
	//populatebank({fundId:<s:property value="fundId.id"/>,typeOfAccount:"PAYMENT,RECEIPTS_PAYMENTS"});
	populatebank({fundId:<s:property value="fundId.id"/>,typeOfAccount:vTypeOfAccount})	
	calcTotalForPayment();
	</script>
	
	<s:if test="%{!validateUser('createpayment')}">
		<script>
			//document.getElementById('searchBtn').disabled=true;
			document.getElementById('Errors').innerHTML='<s:text name="payment.invalid.user"/>';
			if(document.getElementById('vouchermis.departmentid'))
			{
				var d = document.getElementById('vouchermis.departmentid');
				d.options[d.selectedIndex].text='----Choose----';
				d.options[d.selectedIndex].text.value=-1;
			}
			disableControls(0,true);
			document.getElementById("closeButton").disabled=false;
		</script>
		</s:if>
		<s:if test="%{validateUser('balancecheck')}">
			if(document.getElementById('balanceText'))
			{
				document.getElementById('balanceText').style.display='block';
				document.getElementById('balanceAvl').style.display='block';
			}
	</s:if>	
	</s:push><s:token/>
	</s:form>
	
</body>

</html>