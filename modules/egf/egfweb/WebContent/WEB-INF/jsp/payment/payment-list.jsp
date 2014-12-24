<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="org.egov.utils.FinancialConstants" %>
<html>  
<head>  
    <title>Modify Payment Search</title>
</head>
	<body>  
		<s:form action="payment" theme="simple" >
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Bill Payment Search" />
			</jsp:include>
			<span class="mandatory" id="errorSpan">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew">Modify Payment Search</div>
				<table align="center" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="greybox" width="30%"><s:text name="chq.assignment.paymentvoucherdatefrom"/> </td>
						<td class="greybox"><s:textfield name="fromDate" id="fromDate" maxlength="20" value="%{fromDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a><br/>(dd/mm/yyyy)</td>
						<td class="greybox" width="30%"><s:text name="chq.assignment.paymentvoucherdateto"/> </td>
						<td class="greybox"><s:textfield name="toDate" id="toDate" maxlength="20" value="%{toDate}" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
					</tr>
					<tr>
						<td class="bluebox" width="30%"><s:text name="chq.assignment.paymentvoucherno"/> </td>
						<td class="bluebox"><s:textfield name="voucherNumber" id="voucherNumber" maxlength="20" value="%{voucherNumber}"/></td>
					</tr>
					<jsp:include page="../voucher/vouchertrans-filter.jsp"/>
				</table>
				<div  class="buttonbottom">
					<s:hidden name="action" id="action" value="%{action}"/>
					<s:submit method="list" value="Search" id="searchBtn" cssClass="buttonsubmit" onclick="return list()"/>
					<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
				</div>
				<div id="listdiv" style="display:block">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
				        <tr>  
				            <th class="bluebgheadtd">Sl.No.</th>  
				            <th class="bluebgheadtd">Voucher Number</th>
				            <th class="bluebgheadtd">Voucher Date</th>  
				            <th class="bluebgheadtd">Bank Name</th>
				            <th class="bluebgheadtd">Account Number</th>
				            <th class="bluebgheadtd">Type</th>
				            <th class="bluebgheadtd">Amount</th>  
				        </tr> 
				        <c:set var="trclass" value="greybox"/> 
					    <s:iterator var="p" value="paymentheaderList" status="s">  
					    <tr>  
							<td class="<c:out value="${trclass}"/>" style="text-align:right"><s:property value="#s.index+1"/></td>
							<td class="<c:out value="${trclass}"/>">
							<s:if test="%{voucherheader.name=='Direct Bank Payment'}">
						  <a href="#" onclick="javascript:window.open('directBankPayment!beforeEdit.action?voucherHeader.id=<s:property value='%{voucherheader.id}'/>&fundId=<s:property value='%{voucherheader.fundId.id}'/>','Payment','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')"><s:property value="%{voucherheader.voucherNumber}" /></a></td>
							</s:if>
					        <s:else>
					        	<a href="#" onclick="javascript:window.open('payment!modify.action?paymentid=<s:property value='%{id}'/>&fundId=<s:property value='%{voucherheader.fundId.id}'/>','Payment','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')"><s:property value="%{voucherheader.voucherNumber}" /></a></td>
					      
					        </s:else>
					        <td class="<c:out value="${trclass}"/>"><s:date name="%{voucherheader.voucherDate}" format="dd/MM/yyyy"/></td>
					        <td class="<c:out value="${trclass}"/>"><s:property value="%{bankaccount.bankbranch.bank.name+'-'+bankaccount.bankbranch.branchname}" /></td>
					        <td class="<c:out value="${trclass}"/>"><s:property value="%{bankaccount.accountnumber}"/></td>
					        <td class="<c:out value="${trclass}"/>"><s:text name="%{type}" /></td>
					        <td class="<c:out value="${trclass}"/>" style="text-align:right"><s:property value="%{paymentAmount}"/></td>
					        <c:choose>
						        <c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
						        <c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
					        </c:choose>
					    </tr>  
					    </s:iterator>
					</table>
					<s:if test="%{paymentheaderList==null || paymentheaderList.size==0}">
						<div class="bottom" align="center">
							No Records Found !
						</div>
					</s:if>  
				</div>
			</div>
			<s:if test="%{validateUser('deptcheck')}">
				<script>
					if(document.getElementById('vouchermis.departmentid'))
						document.getElementById('vouchermis.departmentid').disabled=true;
				</script>
			</s:if>
		</s:form> 
		<script>
			function loadBank(obj){}
			function list()
			{
				if(document.getElementById('vouchermis.departmentid'))
					document.getElementById('vouchermis.departmentid').disabled=false;
				return true;
			}
			if(document.getElementById('action').value=='search')
				document.getElementById('listdiv').style.display='none'
		</script> 
	</body>  
</html>