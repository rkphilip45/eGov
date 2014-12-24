<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>  
<head>  
	<link rel="stylesheet" type="text/css" href="/EGF/cssnew/ccMenu.css"/>
    <title>Cheque Assignment Search</title>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
</head>
	<body>  
		<s:form action="chequeAssignment" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Cheque Assignment Search" />
			</jsp:include>
 			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="chq.assignment.heading.search"/></div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0" id="paymentTable">
				<tr>  
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.select"/><s:checkbox id="selectall" name="selectall" onclick="checkAll(this)"/></th>
				    <th class="bluebgheadtdnew"><s:text name="Sl No"/></th>
				      
				    <s:if test="%{paymentMode=='cheque'}">
				    	<th class="bluebgheadtdnew"><s:text name="chq.assignment.partycode"/></th>
				    	
				    </s:if>
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.payment.voucherno"/></th>
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.payment.voucherdate"/></th>
				    <th class="bluebgheadtdnew"><s:text name="chq.assignment.payment.amount"/></th>
				      <s:if test="%{reassignSurrenderChq && paymentMode=='cheque'}">
				    	<th class="bluebgheadtdnew" width="10%"><s:text name="chq.assignment.instrument.no"/><span class="mandatory">*</span></th>
				    	<th class="bluebgheadtdnew"><s:text name="chq.assignment.instrument.date"/><span class="mandatory">*</span><br>(dd/mm/yyyy)</th>
				    </s:if>
				    <s:elseif test="%{!isChequeNoGenerationAuto() && paymentMode=='cheque'}">
				    	<th class="bluebgheadtdnew" width="10%"><s:text name="chq.assignment.instrument.no"/><span class="mandatory">*</span></th>
				    	<th class="bluebgheadtdnew"><s:text name="chq.assignment.instrument.date"/><span class="mandatory">*</span><br>(dd/mm/yyyy)</th>
				    </s:elseif>
				</tr>
				<s:iterator var="p" value="chequeAssignmentList" status="s">  
	        	<tr>  
			  	  <td style="text-align:center" class="blueborderfortdnew"><s:hidden id="voucherHeaderId" name="chequeAssignmentList[%{#s.index}].voucherHeaderId" value="%{voucherHeaderId}"/><s:checkbox name="chequeAssignmentList[%{#s.index}].isSelected" id="isSelected%{#s.index}" onclick="update(this)"/></td>
			  	   <td align="left"   style="text-align:center" class="blueborderfortdnew"/> <s:property value="#s.index+1" /> </td>  
			  	  <s:if test="%{paymentMode=='cheque'}">
			  	  	 <td style="text-align:center" class="blueborderfortdnew"><s:hidden id="paidTo" name="chequeAssignmentList[%{#s.index}].paidTo" value="%{paidTo}"/><s:property value="%{paidTo}"/></td>
			  	  </s:if>
				  <td style="text-align:center" class="blueborderfortdnew"><s:hidden id="voucherNumber" name="chequeAssignmentList[%{#s.index}].voucherNumber" value="%{voucherNumber}"/><s:hidden id="detailtypeid" name="chequeAssignmentList[%{#s.index}].detailtypeid" value="%{detailtypeid}"/><s:hidden id="detailkeyid" name="chequeAssignmentList[%{#s.index}].detailkeyid" value="%{detailkeyid}"/><s:property value="%{voucherNumber}" /></td>
      			  <td style="text-align:center" class="blueborderfortdnew"><s:hidden id="voucherDate" name="chequeAssignmentList[%{#s.index}].voucherDate" value="%{voucherDate}"/><s:date name="%{voucherDate}" format="dd/MM/yyyy"/></td>
      			  <td style="text-align:right" class="blueborderfortdnew"><s:hidden id="paidAmount" name="chequeAssignmentList[%{#s.index}].paidAmount" value="%{paidAmount}"/><s:text name="format.number" ><s:param value="%{paidAmount}"/></s:text></td>
					<s:if test="%{reassignSurrenderChq && paymentMode=='cheque'}">
      			  	 <td style="text-align:center" class="blueborderfortdnew"><s:textfield id="chequeNumber%{#s.index}" name="chequeAssignmentList[%{#s.index}].chequeNumber" value="%{chequeNumber}" onchange="validateReassignSurrenderChequeNumber(this)"  size="10"/></td>
      			  	 <td style="text-align:center" class="blueborderfortdnew"><s:date name="chequeDate" var="tempChequeDate" format="dd/MM/yyyy"/><s:textfield id="chequeDate%{#s.index}" name="chequeAssignmentList[%{#s.index}].chequeDate" value="%{tempChequeDate}" size="10"/><a href="javascript:show_calendar('forms[0].chequeDate<s:property value="#s.index"/>');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a></td>
      			  </s:if>
      			  <s:elseif test="%{!isChequeNoGenerationAuto() && paymentMode=='cheque'}">
      			  	 <td style="text-align:center" class="blueborderfortdnew"><s:textfield id="chequeNumber%{#s.index}" name="chequeAssignmentList[%{#s.index}].chequeNumber" value="%{chequeNumber}" onchange="validateChequeNumber(this)"  size="10"/></td>
      			  	 <td style="text-align:center" class="blueborderfortdnew"><s:date name="chequeDate" var="tempChequeDate" format="dd/MM/yyyy"/><s:textfield id="chequeDate%{#s.index}" name="chequeAssignmentList[%{#s.index}].chequeDate" value="%{tempChequeDate}" size="10"/><a href="javascript:show_calendar('forms[0].chequeDate<s:property value="#s.index"/>');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a></td>
      			  </s:elseif>
				</tr>
				</s:iterator>
			</table>
			<div class="subheadsmallnew" id="noRecordsDiv" style="visibility:hidden">No Records Found</div>
			<br/>
			<div id="departmentDiv" style="visibility:visible">
			<s:hidden name="reassignSurrenderChq"/>
				<table align="center" width="100%" cellspacing="0">
					<tr>
						<td class="greybox">
							<s:text name="chq.assignment.department"/><span class="mandatory">*</span>
							<s:select name="vouchermis.departmentid" id="departmentid" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----"  value="%{voucherHeader.vouchermis.departmentid.id}"/>
						</td>
						<s:if test="%{reassignSurrenderChq && paymentMode!='cheque'}">
							<td class="greybox">
								<s:text name="chq.assignment.instrument.no"/><span class="mandatory">*</span>
								<s:textfield id="chequeNumber0" name="chequeNo" value="%{chequeNo}" onchange="validateReassignSurrenderChequeNumber(this)"/>
							</td>
							<td class="greybox">
								<s:text name="chq.assignment.instrument.date"/><span class="mandatory">*</span>(dd/mm/yyyy)
								<s:date name="chequeDt" var="tempChequeDate" format="dd/MM/yyyy"/><s:textfield id="chequeDt" name="chequeDt" value="%{tempChequeDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].chequeDt');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)
							</td>
						</s:if>
						
						
						<s:elseif test="%{!isChequeNoGenerationAuto() && paymentMode!='cheque'}">
							<td class="greybox">
								<s:text name="chq.assignment.instrument.no"/><span class="mandatory">*</span>
								<s:textfield id="chequeNumber0" name="chequeNo" value="%{chequeNo}" onchange="validateChequeNumber(this)"/>
							</td>
							<td class="greybox">
								<s:text name="chq.assignment.instrument.date"/><span class="mandatory">*</span>(dd/mm/yyyy)
								<s:date name="chequeDt" var="tempChequeDate" format="dd/MM/yyyy"/><s:textfield id="chequeDt" name="chequeDt" value="%{tempChequeDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].chequeDt');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)
							</td>
						</s:elseif>
						<s:if  test="%{paymentMode!='cheque'}">
							<td class="greybox">
								<s:text name="chq.assignment.instrument.infavourof"/><span class="mandatory">*</span>
								<s:textfield id="inFavourOf" name="inFavourOf" value="%{inFavourOf}" maxlength="50"/>
							</td>
						</s:if>
					</tr>
				</table>
			</div>
			<div  class="buttonbottom">
				<s:hidden id="selectedRows" name="selectedRows" value="%{selectedRows}"/>
				<s:hidden id="paymentMode" name="paymentMode" value="%{paymentMode}"/>
				<s:hidden id="bankaccount" name="bankaccount" value="%{bankaccount}"/>
				<s:submit id="assignChequeBtn" method="createInstrument" value="Assign Cheque" cssClass="buttonsubmit" onclick="return validate()" />
				<input type="button" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
		</div>
		</s:form>
		<script>
			function update(obj)
			{
				if(obj.checked)
					document.getElementById('selectedRows').value=parseInt(document.getElementById('selectedRows').value)+1;
				else
					document.getElementById('selectedRows').value=parseInt(document.getElementById('selectedRows').value)-1;
			}
			function validate()
			{
				if(dom.get('departmentid') && dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value==-1)
				{
					alert('Select Cheque Issued From');
					return false;
				}
				if(document.getElementById('selectedRows').value=='' || document.getElementById('selectedRows').value==0)
				{
					alert('Please select the payment voucher');
					return false;
				}
				dom.get('departmentid').disabled=false;
				return true;
			}
			function validateChequeNumber(obj)
			{
				if(isNaN(obj.value))
				{
					alert('Cheque number contains alpha characters.');
					obj.value='';
					return false;
				}
				var index = obj.id.substring(12,obj.id.length);
				if(obj.value=='')
					return true;
					
				if(dom.get('departmentid') && dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value==-1)
				{
					alert('Select Cheque Issued From');
					obj.value='';
					return false;
				}
				var dept = dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value;
				var url = '${pageContext.request.contextPath}/voucher/common!ajaxValidateChequeNumber.action?bankaccountId='+document.getElementById('bankaccount').value+'&chequeNumber='+obj.value+'&index='+index+'&departmentId='+dept;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url,callback , null);
			}
			
			function validateReassignSurrenderChequeNumber(obj)
			{
				if(isNaN(obj.value))
				{
					alert('Cheque number contains alpha characters.');
					obj.value='';
					return false;
				}
				var index = obj.id.substring(12,obj.id.length);
				if(obj.value=='')
					return true;
					
				if(dom.get('departmentid') && dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value==-1)
				{
					alert('Select Cheque Issued From');
					obj.value='';
					return false;
				}
				var dept = dom.get('departmentid').options[dom.get('departmentid').selectedIndex].value;
				var url = '${pageContext.request.contextPath}/voucher/common!ajaxValidateReassignSurrenderChequeNumber.action?bankaccountId='+document.getElementById('bankaccount').value+'&chequeNumber='+obj.value+'&index='+index+'&departmentId='+dept;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackReassign, null);
			}
			var callback = {
				success: function(o) {
					var res=o.responseText;
					res = res.split('~');
					if(res[1]=='false')
					{
						alert('Enter valid cheque number or This Cheque number has been already used');
						document.getElementById('chequeNumber'+parseInt(res[0])).value='';
					}
			    },
			    failure: function(o) {
			    	alert('failure');
			    }
			}
				var callbackReassign = {
				success: function(o) {
					var res=o.responseText;
					res = res.split('~');
					if(res[1]=='false')
					{
						alert('This cheque number is not there in the surrendered list');     
						document.getElementById('chequeNumber'+parseInt(res[0])).value='';
					}
			    },
			    failure: function(o) {
			    	alert('failure');
			    }
			}
			function nextChqNo(obj) 
			{
				var index = obj.id.substring(11,obj.id.length);
				var sRtn = showModalDialog("../HTML/SearchNextChqNo.html?accntNoId="+document.getElementById('bankaccount').value, "","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
				if (sRtn != undefined)
					document.getElementById("chequeNumber"+index).value = sRtn;
			}
			
			function checkAll(obj)
			{
				var t = document.getElementById('paymentTable').rows;
				if(obj.checked)
				{
					for(var i=0;i<t.length-1;i++)
						document.getElementById('isSelected'+i).checked=true;
					document.getElementById('selectedRows').value=t.length-1;
				}
				else
				{
					for(var i=0;i<t.length-1;i++)
						document.getElementById('isSelected'+i).checked=false;
					document.getElementById('selectedRows').value=0;
				}
			}
		</script>
		<s:if test="%{isFieldMandatory('department')}">
			<script>
				document.getElementById('departmentid').disabled=true;
			</script>
		</s:if>
		<s:if test="chequeAssignmentList == null || chequeAssignmentList.size==0">
			<script>
				document.getElementById('noRecordsDiv').style.visibility='visible';
				document.getElementById('departmentDiv').style.visibility='hidden';
				document.getElementById('assignChequeBtn').style.display='none';
			</script>
		</s:if>
	</body>  
</html>