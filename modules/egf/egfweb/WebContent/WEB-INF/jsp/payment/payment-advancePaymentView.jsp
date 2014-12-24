<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<link href="/EGF/cssnew/budget.css" rel="stylesheet" type="text/css" />
<link href="/EGF/cssnew/commonegovnew.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/EGF/cssnew/tabber.css" TYPE="text/css">
<script type="text/javascript" src="/EGF/javascript/tabber.js"></script>
<script type="text/javascript" src="/EGF/javascript/tabber2.js"></script>

<script>
function fetchDeptId() {
	var id = '<s:property value="defaultDept"/>';
	var did = '<s:property value="%{paymentheader.voucherheader.vouchermis.departmentid.id}"/>';
	if(id != null && id != '-1' && id != '' ) {
		document.getElementById('departmentid').value = id;
	} else if(did != null && did != '-1' && did != '' ) {
		document.getElementById('departmentid').value = did;
	}
}

function printVoucher(){
	document.forms[0].action='../report/billPaymentVoucherPrint!print.action?id=<s:property value="paymentheader.id"/>';
	document.forms[0].submit();
}
</script>

</head>

<body><br>
	<s:form action="payment" theme="simple" >
	 <s:push value="model">
		<jsp:include page="../budget/budgetHeader.jsp">
        	<jsp:param name="heading" value="Advance Payment View" />
		</jsp:include>
		<font  style='color: red ;'> 
<p id="lblError" style="font:bold" ></p>
</font>
		<span class="mandatory">
			<s:actionerror/>  
			<s:fielderror />
			<s:actionmessage />
		</span>
		<div class="formmainbox"><div class="subheadnew">Advance Payment View</div>
		<div id="budgetSearchGrid" style="display:block;width:100%;border-top:1px solid #ccc;" >
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
			<td>
			<div align="left"><br/>
    			<table border="0" cellspacing="0" cellpadding="0" width="100%">
          		<tr>
          		<td> 
            		<div class="tabber">
            		<div class="tabbertab" >
					<h2>Payment Details</h2>
					<span>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="center" colspan="6" class="serachbillhead">Payment Details</td>
						</tr>
  						<tr>
  							<td width="9%" class="bluebox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('fund')}">
								<td width="12%" class="bluebox"><strong><s:text name="voucher.fund"/></strong></td>
								<td width="20%" class="bluebox"><s:property value="%{paymentheader.voucherheader.fundId.name}"/></td>
							</s:if>
							<s:if test="%{shouldShowHeaderField('fundsource')}">
								<td width="17%" class="bluebox"><strong><s:text name="voucher.fundsource"/></strong></td>
								<td width="33%" class="bluebox"><s:property value="%{paymentheader.voucherheader.fundsourceId.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('department')}">
								<td class="greybox"><strong><s:text name="voucher.department"/></strong></td>
								<td class="greybox"><s:property value="%{paymentheader.voucherheader.vouchermis.departmentid.deptName}"/></td>
							</s:if>
							<s:if test="%{shouldShowHeaderField('functionary')}">
								<td class="greybox"><strong><s:text name="voucher.functionary"/></strong></td>
								<td class="greybox" colspan="2"><s:property value="%{paymentheader.voucherheader.vouchermis.functionary.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('scheme')}">
								<td class="bluebox"><strong><s:text name="voucher.scheme"/></strong></td>
								<td class="bluebox"><s:property value="%{paymentheader.voucherheader.vouchermis.schemeid.name}"/></td>
							</s:if>
							<s:if test="%{shouldShowHeaderField('subscheme')}">
								<td class="bluebox"><strong><s:text name="voucher.subscheme"/></strong></td>
								<td class="bluebox"><s:property value="%{paymentheader.voucherheader.vouchermis.subschemeid.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<s:if test="%{shouldShowHeaderField('field')}">
								<td class="greybox"><strong><s:text name="voucher.field"/></strong></td>
								<td class="greybox" colspan="4"><s:property value="%{paymentheader.voucherheader.vouchermis.divisionid.name}"/></td>
							</s:if>
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox"><strong><s:text name="payment.voucherno"/></strong></td>
							<td class="bluebox"><s:property value="%{paymentheader.voucherheader.voucherNumber}"/></td>
							<td class="bluebox"><strong><s:text name="payment.voucherdate"/></strong></td>
							<td class="bluebox"><s:date name="%{paymentheader.voucherheader.voucherDate}"  format="dd/MM/yyyy"/></td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox"><strong><s:text name="payment.bank"/></strong></td>
							<td class="greybox"><s:property value="%{paymentheader.bankaccount.bankbranch.bank.name+'-'+paymentheader.bankaccount.bankbranch.branchname}"/></td>
							<td class="greybox"><strong><s:text name="payment.bankaccount"/></strong></td>
							<td class="greybox"  colspan="2"><s:property value="%{paymentheader.bankaccount.accountnumber+'---'+paymentheader.bankaccount.accounttype}"/></td>
						</tr>
						<tr id="bankbalanceRow" style="visibility:hidden">
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox" width="15%"><strong><s:text name="payment.balance"/></strong></td>
							<td class="bluebox" colspan="4"><span id="balance"/></td>
						</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox" width="15%"><strong><s:text name="payment.narration"/></strong></td>
							<td class="bluebox" colspan="4"><s:property value="%{paymentheader.voucherheader.description}"/></td>
						</tr>
						<tr>
							<td class="greybox">&nbsp;</td>
							<td class="greybox"><strong><s:text name="payment.mode"/></strong></td>
							<td class="greybox"><s:text name="%{paymentheader.type}"/></td>
							<td class="greybox"><strong><s:text name="payment.amount"/></strong></td>
							<td class="greybox" colspan="2"><span id="paymentAmountspan"/></td>
						</tr>
					  	<tr>
					  		<td  colspan="6"> 
					  			<s:if test='%{wfitemstate !=  "END"}'>
									<%@include file="../voucher/workflowApproval.jsp"%>
								</s:if>
							</td>
					  	</tr>
						<tr>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox" ><strong>Comments</strong></td>
							<td class="bluebox" colspan="4"><s:textarea name="comments" id="comments" cols="100" rows="3" onblur="checkLength(this)" value="%{getComments()}"/></td>
						</tr>
					  	</table>
					  	<s:if test="%{paymentheader.state.id!=null}">
						  	<div id="wfHistoryDiv">
							  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
							        <c:param name="stateId" value="${paymentheader.state.id}"></c:param>
							    </c:import>
						  	</div>
					  </s:if>
                  	</span> 
					</div>
            		<div class="tabbertab">
                  	<h2>Advance Requisition Details</h2>
	                <span>
						<table align="center" border="0" cellpadding="0" cellspacing="0" class="newtable">
						<tr><td colspan="6"><div class="subheadsmallnew">Advance Requisition Details</div></td></tr>
						<tr>
						<td colspan="6">
							<div  style="float:left; width:100%;">
							<table id="miscBillTable" align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>  
								    <th class="bluebgheadtdnew">Advance Requisition Number</td>
								    <th class="bluebgheadtdnew">Advance Requisition Date</td>  
								    <th class="bluebgheadtdnew">Party Name</td>
								    <th class="bluebgheadtdnew">Advance Requisition Amount</td>
								</tr>
								<s:if test="%{advanceRequisitionList.size>0}">
									<s:iterator var="p" value="advanceRequisitionList" status="s">  
										<tr>
											<td style="text-align:center"  class="blueborderfortdnew"><s:property value="%{advanceRequisitionNumber}" /></td>
						      				<td style="text-align:center" class="blueborderfortdnew"><s:date name="%{advanceRequisitionDate}" format="dd/MM/yyyy"/></td>
						      				<td style="text-align:center"  class="blueborderfortdnew"><s:property value="%{#p.egAdvanceReqMises.payto}" /></td>
									     	<td style="text-align:right" class="blueborderfortdnew"><s:text name="format.number" ><s:param value="%{advanceRequisitionAmount}"/></s:text></td>
									        <c:set var="totalAmt" value="${advanceRequisitionAmount}"/>
										</tr>
									</s:iterator>
								</s:if>
								<tr>
									<td style="text-align:right" colspan="3" class="blueborderfortdnew"><strong>Grand Total</strong></td>
									<td style="text-align:right" class="blueborderfortdnew"><fmt:formatNumber value="${totalAmt}" pattern="#0.00"/></td>
								</tr>
							</table>
							</div>
						</td>
						</tr>
						</table>                    
					</span>                  
	                </div>
	                <div class="tabbertab" id="viewtab">
                  	<h2>Cheque Details</h2>
	                <span>
						<table align="center" border="0" cellpadding="0" cellspacing="0" class="newtable">
						<tr><td colspan="6"><div class="subheadsmallnew">Cheque Details</div></td></tr>
						<tr>
						<td colspan="4">
							<div  style="float:left; width:100%;">
							<table id="chequeTable" align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>  
								    <th class="bluebgheadtdnew">Cheque Number</td>
								    <th class="bluebgheadtdnew">Cheque Date</td>  
								    <th class="bluebgheadtdnew">Party Code</td>
								    <th class="bluebgheadtdnew">Cheque Amount</td>
								    <th class="bluebgheadtdnew">Cheque Status</td>
								</tr>
								<s:if test="%{instrumentHeaderList.size>0}">
									<s:iterator var="p" value="instrumentHeaderList" status="s">  
										<tr>
											<td style="text-align:center"  class="blueborderfortdnew"><s:property value="%{instrumentNumber}" /></td>
						      				<td style="text-align:center" class="blueborderfortdnew"><s:date name="%{instrumentDate}" format="dd/MM/yyyy"/></td>
						      				<td style="text-align:center"  class="blueborderfortdnew"><s:property value="%{payTo}" /></td>
									     	<td style="text-align:right" class="blueborderfortdnew"><s:property value="%{instrumentAmount}" /></td>
									     	<td style="text-align:center" class="blueborderfortdnew"><s:property value="%{statusId.description}"/></td>
										</tr>
									</s:iterator>
								</s:if>
							</table>
							<s:if test="%{instrumentHeaderList==null || instrumentHeaderList.size==0}">
								<div  class="bottom" align="center">
									No Cheque Details Found !
								</div>
							</s:if>
							</div>
						</td>
						</tr>
						</table>                    
					</span>                  
	                </div>			 <!-- individual tab -->
			     </div> <!-- tabbber div -->
			</td>
          	</tr>
        	</table>
		</div>
		</td>
		</tr>
		</table>
	</div>
	<div  class="buttonbottom" id="buttondiv">
		<s:hidden  name="paymentid" value="%{paymentheader.id}"/>
		<s:hidden  name="actionname" id="actionName" value="%{action}"/>
		<s:iterator value="%{getValidActions()}" var="p"  status="s">
			<s:if test='%{(description != "Modify Payment")}'>
		  		<s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="wfBtn%{#s.index}" name="%{name}" method="sendForApproval" onclick="return balanceCheck(this, '%{name}','%{description}')"/>
		  	</s:if>
		</s:iterator>
		<s:submit method="beforeSearch" value="Back " cssClass="buttonsubmit" id="backbtnid"/>
		<s:submit cssClass="button" id="printPreview" value="Print Preview"  onclick="printVoucher()"/>
		<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
	</div>
	</div>
	<script>
		document.getElementById('paymentAmountspan').innerHTML = "<fmt:formatNumber value='${totalAmt}' pattern='#0.00'/>";
		if('<%=request.getParameter("paymentid")%>'==null || '<%=request.getParameter("paymentid")%>'=='null'){
			document.getElementById('backbtnid').style.display='inline';
			document.getElementById('printPreview').disabled=true;
		}
		else{
			document.getElementById('backbtnid').style.display='none';
			document.getElementById('printPreview').disabled=false;
		}
			
		function checkLength(obj)
		{
			if(obj.value.length>1024)
			{
				alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
				obj.value = obj.value.substring(1,1024);
			}
		}
		//function refreshInbox()
		{
			if(opener && opener.top && opener.top.document.getElementById('inboxframe'))
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
		}
		
		if(document.getElementById('actionName').value!='' ||( '<%=request.getParameter("showMode")%>'!=null && '<%=request.getParameter("showMode")%>'=='view'))
		{
			document.getElementById('backbtnid').style.display='none';
			if(document.getElementById('wfBtn0'))
				document.getElementById('wfBtn0').style.display='none';
			if(document.getElementById('wfBtn1'))
				document.getElementById('wfBtn1').style.display='none';
			if(document.getElementById('wfBtn2'))
				document.getElementById('wfBtn2').style.display='none';
		}
		function balanceCheck(obj, name, value)
		{
			
			if(!validateAppoveUser(name,value))
				return false;
			if(obj.id=='wfBtn1') // in case of Reject
				return true;
			if(document.getElementById('balance'))
			{
				if(parseFloat(document.getElementById('paymentAmountspan').innerHTML)>parseFloat(document.getElementById('balance').innerHTML))
				{
					alert('Insufficient bank balance');
					return false;
				}
			}

	
			return true;
		}
		
		function validateAppoveUser(name,value){
			document.getElementById('lblError').innerHTML ="";
			document.getElementById("actionName").value= name;

			<s:if test="%{wfitemstate =='END'}">
				if(value == 'Approve' || value == 'Reject') {
					document.getElementById("approverUserId").value=-1;
					return true;
				}
			</s:if>
			<s:else>
				if( (value == 'Approve' || value == 'Save And Forward' || value=='Forward' ) && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
					document.getElementById('lblError').innerHTML ="Please Select the user";
					return false;
				}
			</s:else>
			
			return true;
		}
		
		
		function validateTab(indexx)
		{
			if(indexx==0)
			{
				document.getElementById('buttondiv').style.display='block';
			}
			else
			{
				document.getElementById('buttondiv').style.display='none';
			}
			return true;
		}
		
		var temp = window.setInterval(load,1);
		function load()
		{
			try{ if('<%=request.getParameter("showMode")%>'==null || '<%=request.getParameter("showMode")%>'=='null') document.getElementById('tabber2').style.display='none'; window.clearInterval(temp);}catch(e){}
		}
		if(document.getElementById('actionName').value=='modify')
			document.getElementById('wfHistoryDiv').style.display='none';
	</script>
	<s:if test="%{validateUser('balancecheck')}">
		<script>
			if(document.getElementById('bankbalanceRow'))
			{
				document.getElementById('bankbalanceRow').style.visibility='visible';
				document.getElementById('balance').innerHTML='<s:property value="%{balance}"/>'
			}
		</script>
	</s:if>
	<script>fetchDeptId();</script>
	 </s:push>
	</s:form>
</body>

</html>