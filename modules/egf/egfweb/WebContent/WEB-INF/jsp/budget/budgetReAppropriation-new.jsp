<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<html>  
<head>  
    <title> <s:text name="budget.reappropriation.title"/></title>
    <link rel="stylesheet" href="/EGF/cssnew/tabber.css" TYPE="text/css">
	<script type="text/javascript" src="/EGF/javascript/tabber.js"></script>
	<script type="text/javascript" src="/EGF/javascript/tabber2.js"></script>
    <STYLE type="text/css">
	    .yui-dt-liner { 
		    text-align: right; 
		} 
    </STYLE>
</head>  
<body>  
 		<%@ include file='common-includes.jsp'%>
		<jsp:include page="budgetHeader.jsp"/>
		<%@ include file='budgetReAppropriationSetUp.jsp'%>
		<%@ include file='addReAppropriationSetUp.jsp'%>
					<script>
					
	function populateSubSchemes(scheme){
		populatebudgetReAppropriation_subScheme({schemeId:scheme.options[scheme.selectedIndex].value})
	}
	
	function onHeaderSubSchemePopulation(req,res){
		if(budgetDetailsTable != null){
			headerSubScheme=dom.get('budgetReAppropriation_subScheme');
			pattern = 'budgetDetailList[{index}].subScheme.id'
			processGrid(budgetDetailsTable,function(element,grid){
				if(element) copyOptions(headerSubScheme,element)
			},pattern)
		}
		if(typeof preselectSubScheme=='function') preselectSubScheme()
    }
    
    <s:if test="%{shouldShowHeaderField('scheme') and shouldShowHeaderField('subScheme')}">
	populateSubSchemes(document.getElementById('budgetReAppropriation_scheme'))
	function preselectSubScheme(){
		subSchemes =  document.getElementById('budgetReAppropriation_subScheme');
		selectedValue="<s:property value='subScheme.id'/>"
		for(i=0;i<subSchemes.options.length;i++){
		  if(subSchemes.options[i].value==selectedValue){
			subSchemes.selectedIndex=i;
			break;
		  }
		}
		updateGrid('subScheme.id',document.getElementById('budgetReAppropriation_subScheme').selectedIndex);
	}
	</s:if>
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/calenderNew.js"></script>
		
		<script>
			if(opener != null && opener.top != null && opener.top.document.getElementById('inboxframe')!=null){
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
			}
		
			function validate(checkUser){
				if(validateMandatoryFields() == false)
					return false;
				anticipatory = false;
				estimate = false;
				invalidNumber = "";
				for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
					if(isNaN(document.getElementById('budgetReAppropriationList['+i+'].anticipatoryAmount').value) )
						anticipatory = true;
				}				
				for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
					if(isNaN(document.getElementById('budgetReAppropriationList['+i+'].deltaAmount').value) )
						estimate = true;
				}
				if(alertMessage(estimate,anticipatory) ==false)
					return false;	
				anticipatory = false;
				estimate = false;
				for(i=0;i<budgetReAppropriationTable.getRecordSet().getLength();i++){
					if(isNaN(document.getElementById('newBudgetReAppropriationList['+i+'].anticipatoryAmount').value))
						anticipatory = true;
				}				
				for(i=0;i<budgetReAppropriationTable.getRecordSet().getLength();i++){
					if(isNaN(document.getElementById('newBudgetReAppropriationList['+i+'].deltaAmount').value) )
						estimate = true;
				}		
				if(alertMessage(estimate,anticipatory) ==false)
					return false;
				for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
					if(document.getElementById('budgetReAppropriationList['+i+'].budgetDetail.budgetGroup.id').value != 0 && document.getElementById('budgetReAppropriationList['+i+'].deltaAmount').value <= 0)
						invalidNumber = "Addition/Dedution amount should be greater than 0";
					if(document.getElementById('budgetReAppropriationList['+i+'].budgetDetail.budgetGroup.id').value != 0 && document.getElementById('budgetReAppropriationList['+i+'].anticipatoryAmount').value <= 0)
						invalidNumber = "Anticipatory amount should be greater than 0";
					if(document.getElementById('budgetReAppropriationList['+i+'].changeRequestType').value == 'Deduction' && document.getElementById('budgetReAppropriationList['+i+'].deltaAmount').value > parseInt(document.getElementById('budgetReAppropriationList['+i+'].availableAmount').innerHTML))
						invalidNumber = "Dedution amount should be less than or equal to Balance Fund Available Amount";
				}
				for(i=0;i<budgetReAppropriationTable.getRecordSet().getLength();i++){
					if(document.getElementById('newBudgetReAppropriationList['+i+'].budgetDetail.budget.id').value != 0 && document.getElementById('newBudgetReAppropriationList['+i+'].deltaAmount').value <= 0)
						invalidNumber = "Addition/Dedution amount should be greater than 0";
					if(document.getElementById('newBudgetReAppropriationList['+i+'].budgetDetail.budget.id').value != 0 && document.getElementById('newBudgetReAppropriationList['+i+'].anticipatoryAmount').value <= 0)
						invalidNumber = "Anticipatory amount should be greater than 0";
					if(document.getElementById('newBudgetReAppropriationList['+i+'].budgetDetail.budget.id').value != 0 && document.getElementById('newBudgetReAppropriationList['+i+'].deltaAmount').value < 0)
						invalidNumber = "Addition amount should be greater than 0";
				}	
				if(invalidNumber != ""){
					alert(invalidNumber);
					return false;
				}
				if(checkUser){
					document.getElementById("actionName").value = 'forward';
					if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
						alert("Please Select the user");
						return false;
					}
				}
				if(!checkUser){
					document.getElementById("actionName").value = 'save';
				}
				document.budgetDetailForm.submit();
				return;
			}

			function alertMessage(estimate,anticipatory){
				if(estimate && anticipatory){
					alert('Estimate amount and Anticipatory amount must be a number');
					return false;
				}else if(estimate){
					alert('Estimate amount must be a number');
					return false;
				}else if(anticipatory){
					alert('Anticipatory amount must be a number');
					return false;
				}
			}
			function validateMandatoryFields(){
				if(document.getElementById('financialYear').value==0){
					alert('Please select a Financial year');
					return false;
				}
				return true;
			}
			var callback = {
				     success: function(o) {
						document.getElementById('beReGrid').innerHTML = o.responseText;
						element = document.getElementById('isBeRe');
						if(document.getElementById('newBeRe').value == 'RE')
							element.selectedIndex = 1;
						else
							element.selectedIndex = 0;
						updateBudgetDropDown();
				        },
				     failure: function(o) {
				     }
			} 
			function getBeRe(){
				element = document.getElementById('financialYear')
				id = element.options[element.selectedIndex].value;
				var transaction = YAHOO.util.Connect.asyncRequest('GET', 'budgetReAppropriation!ajaxLoadBeRe.action?id='+id, callback, null);
			}
			function updateBudgetDropDown(){
				newBudgetList=document.getElementById('newBudgetDropDownList')
				for(i=0;i<budgetReAppropriationTable.getRecordSet().getLength();i++){
					element = document.getElementById('newBudgetReAppropriationList['+i+'].budgetDetail.budget.id')
					if(element){
						copyOptions(newBudgetList,element)
					}
				}
			}
			function updateBudgetDropDownForRow(index){
				newBudgetList=document.getElementById('newBudgetDropDownList')
				element = document.getElementById('newBudgetReAppropriationList['+index+'].budgetDetail.budget.id')
				if(element){
					copyOptions(newBudgetList,element)
				}
			}
		</script>
		<s:actionmessage theme="simple"/>
		<s:actionerror/>  
		<s:fielderror />  
		<s:form name="budgetDetailForm" action="budgetReAppropriation" theme="simple" >
		<s:token/>
			<div align="left"><br/>
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
        	<tr>
       		<td> 
         		<div class="tabber">
           			<div class="tabbertab" style="height: 430px;">
           				<h2>Additional Appropriation</h2>
           					<span>
           						<div  class="subheadnew" ><s:text name="budget.reappropriation.title"/></div><br>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
									    <td width="10%" class="bluebox">&nbsp;</td>
										<td class="bluebox"><s:text name="budget.financialYear"/><span class="mandatory">*</span></td>
									    <td class="bluebox"><s:select list="dropdownData.financialYearList"  listKey="id" listValue="finYearRange" name="financialYear.id" value="financialYear.id" id="financialYear" headerKey="0" headerValue="--- Select ---" onchange="getBeRe();"></s:select></td>
										<td class="bluebox" width="19%"><s:text name="budget.bere"/></td>
									    <td class="bluebox"><s:select name="isBeRe" id="isBeRe" list="#{'BE':'BE','RE':'RE'}" value="beRe" disabled="true"/></td>
									</tr>
									<tr>
										<s:if test="%{shouldShowHeaderField('executingDepartment')}">
											<td class="greybox">&nbsp;</td>
											<td  class="greybox">
												<s:text name="budgetdetail.executingDepartment"/>
												<s:if test="%{isFieldMandatory('executingDepartment')}"><span class="mandatory">*</span></s:if>
											</td>
										    <td width="22%" class="greybox"><s:select list="dropdownData.executingDepartmentList"  listKey="id" listValue="deptName" name="budgetDetail.executingDepartment.id" headerKey="0" headerValue="--- Select ---" onchange="updateGrid('budgetDetail.executingDepartment.id',document.getElementById('budgetReAppropriation_executingDepartment').selectedIndex);updateReAppGrid('budgetDetail.executingDepartment.id',document.getElementById('budgetReAppropriation_executingDepartment').selectedIndex);" value="budgetDetail.executingDepartment.id" id="budgetReAppropriation_executingDepartment"></s:select></td>
										</s:if>
										<s:if test="%{shouldShowHeaderField('fund')}">
												<td  class="greybox">
													<s:text name="fund"/>
													<s:if test="%{isFieldMandatory('fund')}"><span class="mandatory">*</span></s:if>
												</td>
											    <td  class="greybox"><s:select list="dropdownData.fundList"  listKey="id" listValue="name" name="budgetDetail.fund.id" headerKey="0" headerValue="--- Select ---" onchange="updateGrid('budgetDetail.fund.id',document.getElementById('budgetReAppropriation_fund').selectedIndex);updateReAppGrid('budgetDetail.fund.id',document.getElementById('budgetReAppropriation_fund').selectedIndex)" value="fund.id" id="budgetReAppropriation_fund"></s:select></td>
										</s:if>
									</tr>
									<tr>
										<s:if test="%{shouldShowHeaderField('function')}">
										    <td class="bluebox">&nbsp;</td>
											<td  class="bluebox">
												<s:text name="function"/>
												<s:if test="%{isFieldMandatory('function')}"><span class="mandatory">*</span></s:if>
											</td>
										    <td  class="bluebox"><s:select list="dropdownData.functionList"  listKey="id" listValue="name" name="budgetDetail.function.id" headerKey="0" headerValue="--- Select ---" onchange="updateGrid('budgetDetail.function.id',document.getElementById('budgetReAppropriation_function').selectedIndex);updateReAppGrid('budgetDetail.function.id',document.getElementById('budgetReAppropriation_function').selectedIndex)" value="function.id" id="budgetReAppropriation_function"></s:select></td>
										</s:if>
										<s:if test="%{shouldShowHeaderField('functionary')}">
												<td class="bluebox">
													<s:text name="functionary"/>
													<s:if test="%{isFieldMandatory('functionary')}"><span class="mandatory">*</span></s:if>
												</td>
											    <td class="bluebox"><s:select list="dropdownData.functionaryList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.functionary.id" onchange="updateGrid('budgetDetail.functionary.id',document.getElementById('budgetReAppropriation_functionary').selectedIndex);updateReAppGrid('budgetDetail.functionary.id',document.getElementById('budgetReAppropriation_functionary').selectedIndex)" value="functionary.id" id="budgetReAppropriation_functionary"></s:select></td>
										</s:if>
									</tr>
									<tr>
										<s:if test="%{shouldShowHeaderField('scheme')}">
												<td width="10%" class="bluebox">&nbsp;</td>
												<td class="greybox">
													<s:text name="scheme"/>
													<s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if>
												</td>
											    <td class="greybox"><s:select list="dropdownData.schemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.scheme.id" onchange="updateGrid('budgetDetail.scheme.id',document.getElementById('budgetReAppropriation_scheme').selectedIndex);populateSubSchemes(this);" value="scheme.id" id="budgetReAppropriation_scheme"></s:select></td>
										</s:if>
										<s:if test="%{shouldShowHeaderField('subScheme')}">
												<egov:ajaxdropdown id="subScheme" fields="['Text','Value']" dropdownId="budgetReAppropriation_subScheme" url="budget/budgetDetail!ajaxLoadSubSchemes.action" afterSuccess="onHeaderSubSchemePopulation"/>
												<td class="greybox">
													<s:text name="subscheme"/>
													<s:if test="%{isFieldMandatory('subScheme')}"><span class="mandatory">*</span></s:if>
												</td>
											    <td class="greybox"><s:select list="dropdownData.subSchemeList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.subScheme" onchange="updateGrid('budgetDetail.subScheme.id',document.getElementById('budgetReAppropriation_subScheme').selectedIndex);updateReAppGrid('budgetDetail.subScheme.id',document.getElementById('budgetReAppropriation_subScheme').selectedIndex)" value="subScheme.id" id="budgetReAppropriation_subScheme"></s:select></td>
										</s:if>
										
									</tr>
									<tr>
										<s:if test="%{shouldShowHeaderField('boundary')}">
												<td class="bluebox">
													<s:text name="field"/>
													<s:if test="%{isFieldMandatory('boundary')}"><span class="mandatory">*</span></s:if>
												</td>
											    <td class="bluebox"><s:select list="dropdownData.boundaryList"  listKey="id" listValue="name" headerKey="0" headerValue="--- Select ---" name="budgetDetail.boundary.id" onchange="updateGrid('budgetDetail.boundary.id',document.getElementById('budgetReAppropriation_boundary').selectedIndex)" value="boundary.id" id="budgetReAppropriation_boundary"></s:select></td>
										</s:if>
										<s:else>
										    <td class="bluebox">&nbsp;</td>
										    <td class="bluebox">&nbsp;</td>
										</s:else>
									</tr>
									<tr>
											<td class="greybox">&nbsp;</td>
											<td class="greybox"><s:text name="budgetReAppropriation.asOnDate"/></td>
											<td class="greybox"><input type="text"  id="date" name="appropriationMisc.reAppropriationDate" style="width:100px" value='<s:date name="appropriationMisc.reAppropriationDate" format="dd/MM/yyyy"/>'/><a href="javascript:show_calendar('budgetDetailForm.date');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
										    <td class="greybox"><s:text name="budgetReAppropriation.comments"/></td>
										    <td class="greybox"><s:textarea id="appropriationMisc.remarks" name="appropriationMisc.remarks" cols="50"/></td>
									</tr>
								</table>
								<br/>
				<div align="center"><s:submit method="loadActuals" value="Get Actuals" cssClass="buttonsubmit"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="budgetDetailFormTable">
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan="9">
				<div class="subheadsmallnew"><strong><s:text name="budget.reappropriation.add.release"/></strong></div>
			</td>
		</tr>
		</table>
			 <div class="yui-skin-sam" style="width:100%;overflow-x:auto; overflow-y:hidden;">
		       <div id="budgetDetailTable"></div>
		       <br/>
		     </div>
		<script>
			makeBudgetDetailTable();
			hideColumns();
			document.getElementById('budgetDetailTable').getElementsByTagName('table')[0].width = "100%";
			addGridRows();
			updateAllGridValues()
			<s:if test="%{getActionErrors().size()>0 || getFieldErrors().size()>0}">
				setValues();
			</s:if>
			for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
				computeAvailable("budgetReAppropriationList",i);
			}
		</script>
		<br/>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="budgetReAppropriationFormTable">
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan="9">
				<div class="subheadsmallnew"><strong><s:text name="budget.reappropriation.add"/></strong></div>
			</td>
		</tr>
		</table>
			 <div class="yui-skin-sam" style="width:100%;overflow-x:auto; overflow-y:hidden;">
		       <div id="budgetReAppropriationsTable"></div>
		       <br/>
		     </div>
		<script>
			makeBudgetReAppropriationTable();
			hideReAppropriationTableColumns();
			document.getElementById('budgetReAppropriationsTable').getElementsByTagName('table')[0].width = "100%";
			addReAppGridRows();
			updateAllReAppGridValues()
			<s:if test="%{getActionErrors().size()>0 || getFieldErrors().size()>0}">
				setValuesForReAppropriation();
			</s:if>
		</script>
							</span>
						</div>
						<div class="tabbertab" style="height: 430px;">
		                  	<h2>Approval Details</h2>
			                <span>
			                	<input type="hidden" name="scriptName" id="scriptName" value="BudgetDetail.nextDesg"/>
			               		<s:hidden name="miscId" id="miscId" />
			               		<%@include file="../voucher/workflowApproval.jsp"%>
			                </span>
						</div> <!-- Individual tab -->
				</div>
					</td>
		</tr>
			
	</table>
</div>
		<div class="buttonbottom" style="padding-bottom:10px;"> 
			<input type="submit" value="Save" id="budgetReAppropriation__create" name="method:create" onClick="javascript: return validate(false);" class="buttonsubmit"/>
			<input type="submit" value="Forward" id="budgetReAppropriation__createAndForward" name="method:createAndForward" onClick="javascript: return validate(true);" class="buttonsubmit"/>
			<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
		</div>
		<s:hidden  name="actionName" id="actionName"/>
		</s:form>  
		<div id="beReGrid" style="display:none"></div>
	</body>  
</html>