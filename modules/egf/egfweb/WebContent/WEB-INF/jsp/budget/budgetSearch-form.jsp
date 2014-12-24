<script>
	function populateSubSchemes(scheme){
		populatebudgetDetail_subScheme({schemeId:scheme.options[scheme.selectedIndex].value})
	}
	
	function populateBudgets(financialYearRange){
		populatebudgetDetail_budget({financialYear:financialYearRange.options[financialYearRange.selectedIndex].value,skipPrepare:true})
	}
	function onHeaderSubSchemePopulation(req,res){
		if(budgetDetailsTable != null){
			headerSubScheme=dom.get('budgetDetail_subScheme');
			pattern = 'budgetDetailList[{index}].subScheme.id'
			processGrid(budgetDetailsTable,function(element,grid){
				if(element) copyOptions(headerSubScheme,element)
			},pattern)
		}
		if(typeof preselectSubScheme=='function') preselectSubScheme()
    }
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/calenderNew.js"></script>
<div class="formmainbox"><div class="formheading"></div> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
	 	<td width="10%" class="bluebox">&nbsp;</td>
		<td class="bluebox"><s:text name="budget.financialYear"/><span class="mandatory">*</span>
	    <td width="22%" class="bluebox"><s:select list="dropdownData.financialYearList"  listKey="id" listValue="finYearRange" name="financialYear" id="financialYearRange" onchange="populateBudgets(this);"></s:select></td>
	</tr>
	<tr>
	    <td width="10%" class="greybox">&nbsp;</td>
	    <egov:ajaxdropdown id="budget" fields="['Text','Value']" dropdownId="budgetDetail_budget" url="budget/budgetSearch!ajaxLoadBudget.action"/>
		<td class="greybox"><s:text name="budgetdetail.budget"/>
	    <td width="22%" class="greybox"><s:select list="dropdownData.budgetList"  listKey="id" listValue="name" name="budget" value="model.budget.id" id="budgetDetail_budget" disabled="%{headerDisabled}" headerKey="0" headerValue="--- Select ---"></s:select></td>
		<s:if test="%{shouldShowHeaderField('executingDepartment') || shouldShowGridField('executingDepartment')}">
			<td  class="greybox"><s:text name="budgetdetail.executingDepartment"/></td>
		    <td width="22%" class="greybox"><s:select list="dropdownData.executingDepartmentList"  listKey="id" listValue="deptName" name="executingDepartment" headerKey="0" headerValue="--- Select ---" onchange="updateGrid('executingDepartment.id',document.getElementById('budgetDetail_executingDepartment').selectedIndex)" value="executingDepartment.id" id="budgetDetail_executingDepartment"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('fund') || shouldShowGridField('fund')}">
			    <td class="bluebox">&nbsp;</td>
				<td  class="bluebox"><s:text name="budgetdetail.fund"/></td>
			    <td  class="bluebox"><s:select list="dropdownData.fundList"  listKey="id" listValue="name" name="fund" headerKey="0" headerValue="--- Select ---" onchange="updateGrid('fund.id',document.getElementById('budgetDetail_fund').selectedIndex)" value="fund.id" id="budgetDetail_fund"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('function') || shouldShowGridField('function')}">
				<td  class="bluebox"><s:text name="budgetdetail.function"/></td>
			    <td  class="bluebox"><s:select list="dropdownData.functionList"  listKey="id" listValue="name" name="function" headerKey="0" headerValue="--- Select ---" onchange="updateGrid('function.id',document.getElementById('budgetDetail_function').selectedIndex)" value="function.id" id="budgetDetail_function"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('scheme') || shouldShowGridField('scheme')}">
				<td width="10%" class="bluebox">&nbsp;</td>
				<td class="greybox"><s:text name="budgetdetail.scheme"/></td>
			    <td class="greybox"><s:select list="dropdownData.schemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="scheme" onchange="updateGrid('scheme.id',document.getElementById('budgetDetail_scheme').selectedIndex);populateSubSchemes(this);" value="scheme.id" id="budgetDetail_scheme"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('subScheme') || shouldShowGridField('subScheme')}">
				<egov:ajaxdropdown id="subScheme" fields="['Text','Value']" dropdownId="budgetDetail_subScheme" url="budget/budgetDetail!ajaxLoadSubSchemes.action" afterSuccess="onHeaderSubSchemePopulation"/>
				<td class="greybox"><s:text name="budgetdetail.subScheme"/></td>
			    <td class="greybox"><s:select list="dropdownData.subSchemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="subScheme" onchange="updateGrid('subScheme.id',document.getElementById('budgetDetail_subScheme').selectedIndex)" value="subScheme.id" id="budgetDetail_subScheme"></s:select></td>
		</s:if>
	</tr>
	<tr>
		<s:if test="%{shouldShowHeaderField('functionary') || shouldShowGridField('functionary')}">
				<td class="bluebox"><s:text name="budgetdetail.functionary"/></td>
			    <td class="bluebox"><s:select list="dropdownData.functionaryList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="functionary" onchange="updateGrid('functionary.id',document.getElementById('budgetDetail_functionary').selectedIndex)" value="functionary.id" id="budgetDetail_functionary"></s:select></td>
		</s:if>
		<s:if test="%{shouldShowHeaderField('boundary') || shouldShowGridField('boundary')}">
				<td class="bluebox"><s:text name="budgetdetail.field"/></td>
			    <td class="bluebox"><s:select list="dropdownData.boundaryList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="boundary" onchange="updateGrid('boundary.id',document.getElementById('budgetDetail_boundary').selectedIndex)" value="boundary.id" id="budgetDetail_boundary"></s:select></td>
		</s:if>
		<s:else>
		    <td class="bluebox">&nbsp;</td>
		    <td class="bluebox">&nbsp;</td>
		</s:else>
	</tr>
	</table>
</div>
<script>
<s:if test="%{(shouldShowHeaderField('scheme') and shouldShowHeaderField('subScheme')) or (shouldShowGridField('scheme') and shouldShowGridField('subScheme'))}">
populateSubSchemes(document.getElementById('budgetDetail_scheme'))
function preselectSubScheme(){
	subSchemes =  document.getElementById('budgetDetail_subScheme');
	selectedValue="<s:property value='subScheme.id'/>"
	for(i=0;i<subSchemes.options.length;i++){
	  if(subSchemes.options[i].value==selectedValue){
		subSchemes.selectedIndex=i;
		break;
	  }
	}
}
</s:if>
</script>	