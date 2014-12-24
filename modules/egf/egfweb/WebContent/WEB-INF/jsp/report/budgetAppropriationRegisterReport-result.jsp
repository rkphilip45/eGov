<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<script>

function disableTheFiels() {

	document.getElementById("asOnDate").value = '<s:property value="strAsOnDate"/>';
//	document.getElementById("department").value = '<s:property value="deptId"/>';
//	document.getElementById("function").value = '<s:property value="funcId"/>';
//	document.getElementById("fund").value = '<s:property value="fundID"/>';
	
	document.getElementById("asOnDate").disabled = true;
	document.getElementById("department").disabled = true;
	document.getElementById("function").disabled = true;
	document.getElementById("fund").disabled = true;
	document.getElementById("budgetHeadId").disabled = true;

}

</script>
<body>
<s:form action="budgetAppropriationRegisterReport" theme="simple" name="budgetAppropriationRegister-Result">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
        <th colspan="6">
			<div class="reportheader">
			<b>Budget Appropriation Register</b> 			
			</div>
			<!-- 
			<div class="reportheader">
				Total Amount : <b><s:property value="totalGrant"/></b> 			
			</div>
			-->
		</th>
      </tr>
      <br/>
	<tr>
	    <td class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="report.department"/><s:if test="%{isFieldMandatory('executingDepartment')}"><span class="mandatory">*</span></s:if></td>
	    <td class="bluebox"><s:select list="dropdownData.executingDepartmentList"  listKey="id" listValue="deptName" name="department.id" headerKey="0" headerValue="--- Select ---" value="department.id" id="department"></s:select></td>
		<td class="bluebox"><s:text name="report.budged.head"/><span class="mandatory">*</span></td>
		<td class="bluebox"><s:select list="dropdownData.budgetGroupList"  listKey="id" listValue="name" name="budgetDetail.budgetGroup.id" headerKey="0" headerValue="--- Select ---" value="budgetGroup.id" id="budgetHeadId"></s:select></td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	<tr>
	    <td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="report.function.center"/><s:if test="%{isFieldMandatory('function')}"><span class="mandatory">*</span></s:if></td>
	    <td class="greybox"><s:select list="dropdownData.functionList"  listKey="id" listValue="name" name="function.id" headerKey="0" headerValue="--- Select ---"  value="function.id" id="function"></s:select></td>
		<td class="greybox">As on Date:<span class="mandatory">*</span></td>
		<td class="greybox">
			<s:textfield name="asOnDate" id="asOnDate" cssStyle="width:100px" onkeyup="DateFormat(this,this.value,event,false,'3')"/>(dd/mm/yyyy)
		</td>
		<td class="greybox">&nbsp;</td>
	</tr>

	<tr>
	  <td class="bluebox">&nbsp;</td>
		<td  class="bluebox">
			<s:text name="report.fund"/><s:if test="%{isFieldMandatory('fund')}"><span class="mandatory">*</span></s:if>
		</td>
	    <td  class="bluebox" colspan="4" ><s:select list="dropdownData.fundList"  listKey="id" listValue="name" name="fund" headerKey="0" headerValue="--- Select ---"  value="fund.id" id="fund"></s:select></td>
	</tr>	
	<tr>
		<td colspan="6">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">BE - (<s:property value="finYearRange"/>) (Rs): <s:if test="%{isBeDefined==true}"><s:text name="format.number" ><s:param value="%{beAmount}"/></s:text></s:if><s:else> <s:text name="report.budget.not.found"/></s:else></td>
					<td class="greybox" >Additional appropriation (Rs)  : <s:if test="%{isBeDefined==true}"><s:text name="format.number" ><s:param value="%{addtionalAppropriationForBe}"/></s:text></s:if><s:else></s:else></td>
					<td class="greybox" align="left">RE - (<s:property value="finYearRange"/>) (Rs):<s:if test="%{isReDefined==true}"> <s:text name="format.number" ><s:param value="%{reAmount}"/></s:text></s:if><s:else></s:else></td>
				</tr>
			</table>
		</td>
	</tr>	
</table>



<s:if test="%{updatedBdgtAppropriationRegisterList.size()>0}">
<table width="99%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="bluebox"> 
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tablebottom">
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
	              <tr>
	                <th class="bluebgheadtd" >Sl. No.</th>
	                <th class="bluebgheadtd" >Budget Appropriation #</th>
	                <th class="bluebgheadtd" >Bill Number</th>
	                <th class="bluebgheadtd" >Bill Date</th>
	                <th class="bluebgheadtd" >Voucher Number</th>
	                <th class="bluebgheadtd" >Voucher Date</th>
	                <th class="bluebgheadtd" >Amount (Rs)</th>
	                <th class="bluebgheadtd" >Cumulative Total (Rs)</th>
	                <th class="bluebgheadtd" >Balance Available (Rs)</th>
				  </tr>
				<s:iterator value="updatedBdgtAppropriationRegisterList" status="stat">
					<tr>
						<td class="blueborderfortd"><div align="center"><s:property value="serailNumber"/></div></td>
						<td class="blueborderfortd"><div align="left"><s:property value="bdgApprNumber"/></div></td>
						<td class="blueborderfortd"><div align="left"><s:property value="billNumber"/>&nbsp;</div></td>
						<td class="blueborderfortd"><div align="left"><s:date name="%{billDate}" format="dd/MM/yyyy"/>&nbsp;</div></td>
						<td class="blueborderfortd"><div align="left"><s:property value="VoucherNumber"/>&nbsp;</div></td>
						<td class="blueborderfortd"><div align="left"><s:date name="%{voucherDate}" format="dd/MM/yyyy"/>&nbsp;</div></td>
						<td class="blueborderfortd"><div align="right"><s:text name="format.number" ><s:param value="%{billAmount}"/></s:text>&nbsp;</div></td>
						<td class="blueborderfortd"><div align="right"><s:text name="format.number" ><s:param value="%{cumulativeAmount}"/></s:text>&nbsp;</div></td>
						<td class="blueborderfortd"><div align="right"><s:text name="format.number" ><s:param value="%{balanceAvailableAmount}"/></s:text>&nbsp;</div></td>
					</tr>
				</s:iterator>
			</table>
        </div>
        </td>
      </tr>
	  <tr>
		<td>
		<div class="excelpdf">
			<a href='${pageContext.request.contextPath}/report/budgetAppropriationRegisterReport!generatePdf.action?asOnDate=<s:property value="strAsOnDate"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&budgetGroup.id=<s:property value="budgetGroup.id"/>&fund.id=<s:property value="fund.id"/>'>PDF</a> <img align="absmiddle" src="${pageContext.request.contextPath}/images/pdf.png">
			<a href='${pageContext.request.contextPath}/report/budgetAppropriationRegisterReport!generateXls.action?asOnDate=<s:property value="strAsOnDate"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&budgetGroup.id=<s:property value="budgetGroup.id"/>&fund.id=<s:property value="fund.id"/>'>Excel</a> <img align="absmiddle" src="${pageContext.request.contextPath}/images/excel.png">
		</div>		
		</td>
	  </tr>
	</table></td>
	</tr>
</table>
</s:if>
<s:else>
	<br/>
	<div class="reportheader">
		No data found
	</div>
</s:else>
	<script>disableTheFiels();</script>
</s:form>
</body>
