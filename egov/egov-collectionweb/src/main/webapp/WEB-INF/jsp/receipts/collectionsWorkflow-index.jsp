<%@ include file="/includes/taglibs.jsp"%>
<head>
<title>Collections Submission/Approval</title>
<script>
jQuery.noConflict();
jQuery(document).ready(function() {
  	 
     jQuery(" form ").submit(function( event ) {
    	 doLoadingMask();
    });
     doLoadingMask();
 });

jQuery(window).load(function () {
	undoLoadingMask();
});



var isSubmitAction = ${isSubmitAction};
var isApproveAction = ${isApproveAction};
var totalAmount = ${totalAmount};
var cashAmount = ${cashAmount};
var chequeAmount = ${chequeAmount};
var ddAmount = ${ddAmount};
var cardAmount = ${cardAmount};
var listApproveAction = "collectionsWorkflow!listApprove.action"

// Enable/disable buttons based on user selections
function enableButtons() {
	if (isChecked(document.getElementsByName('receiptIds'))) {
		if(isSubmitAction) {
			document.collectionsWorkflowForm.submitCollections.disabled = false;
			document.collectionsWorkflowForm.submitCollections.className="buttonsubmit";
		} else {
			document.collectionsWorkflowForm.approveCollections.disabled = false;
			document.collectionsWorkflowForm.rejectCollections.disabled = false;
			document.collectionsWorkflowForm.approveCollections.className= "buttonsubmit";
			document.collectionsWorkflowForm.rejectCollections.className= "buttonsubmit";

		}
	} else {
		if(isSubmitAction) {
			document.collectionsWorkflowForm.submitCollections.disabled = true;
			document.collectionsWorkflowForm.submitCollections.className="button";	
		} else {
			document.collectionsWorkflowForm.approveCollections.disabled = true;
			document.collectionsWorkflowForm.rejectCollections.disabled = true;
			document.collectionsWorkflowForm.approveCollections.className= "button";
			document.collectionsWorkflowForm.rejectCollections.className= "button";

		}
	}
}

// Adds commas to given numeric string
function addCommas(nStr)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

//Displays the summary of amounts
function refreshSummary() {
	document.collectionsWorkflowForm.totalAmount.value = addCommas(totalAmount.toFixed(2));
	document.collectionsWorkflowForm.cashAmount.value = addCommas(cashAmount.toFixed(2));
	document.collectionsWorkflowForm.chequeAmount.value = addCommas(chequeAmount.toFixed(2));
	document.collectionsWorkflowForm.ddAmount.value = addCommas(ddAmount.toFixed(2));
	document.collectionsWorkflowForm.cardAmount.value = addCommas(cardAmount.toFixed(2));
}

// Handle the event when user selects/deselects a receipt to be submitted/approved/rejected
function handleReceiptSelectionEvent(receiptAmount, instrumentType, isSelected) {
	// Enable/disable buttons based on number of receipts selected
	enableButtons();
	var delta = 0;

	if(isSelected == true) {
		delta = receiptAmount;
	} else {
		delta = receiptAmount * -1;
	}

	// Increment/decrement all amounts
	totalAmount = totalAmount + delta;

	if(instrumentType == "cash") {
		cashAmount = cashAmount + delta;
	}
	
	if(instrumentType == "cheque") {
		chequeAmount = chequeAmount + delta;
	}

	if(instrumentType == "dd") {
		ddAmount = ddAmount + delta;
	}

	if(instrumentType == "card") {
		cardAmount = cardAmount + delta;
	}

	// Set the total amount of selected receipts
	refreshSummary();
}

// Check if at least one receipt is selected
function isChecked(chk) {
	if (chk.length == undefined) {
 		if (chk.checked == true) {
  			return true;
 		} else {
 	 		return false;
 		}	
 	} else {
 		for (i = 0; i < chk.length; i++)
		{
			if (chk[i].checked == true ) {
				return true;
			}
		}
		return false;
 	}
}

// Changes selection of all receipts to given value (checked/unchecked)
function changeSelectionOfAllReceipts(checked) {
	chk = document.getElementsByName('receiptIds');
	if (chk.length == undefined) {
		chk.checked = checked;
 	} else {
 		for (j = 0; j < chk.length; j++)
		{
 			chk[j].checked = checked;
		}
 	}	
}

//DeSelect all receipts
function deSelectAll() {
	// DeSelect all checkboxes
	changeSelectionOfAllReceipts(false);

 	// Set all amounts to zero
	totalAmount = 0;
	cashAmount = 0;
	chequeAmount = 0;
	ddAmount = 0;
	cardAmount = 0;

	// Refresh the summary section
	refreshSummary();

	// Enable/disable buttons
	enableButtons();
}

// Select all receipts
function selectAll() {
	// Select all checkboxes
	changeSelectionOfAllReceipts(true);

 	// Set all amounts to original values
	totalAmount = ${totalAmount};
	cashAmount = ${cashAmount};
	chequeAmount = ${chequeAmount};
	ddAmount = ${ddAmount};
	cardAmount = ${cardAmount};

	// Refresh the summary section
	refreshSummary();

	// Enable/disable buttons
	enableButtons();
}

function changeCounterId(newCounterId) {
	if(newCounterId != ${counterId}) {
		document.collectionsWorkflowForm.action=listApproveAction;
		document.collectionsWorkflowForm.submit();
	}
}

function changeUserName(newUserName) {
	if(newUserName != "${userName}") {
		document.collectionsWorkflowForm.action=listApproveAction;
		document.collectionsWorkflowForm.submit();
	}
}

function changeServiceCode(newServiceCode) {
	if(newServiceCode != "${serviceCode}") {
		document.collectionsWorkflowForm.action=listApproveAction;
		document.collectionsWorkflowForm.submit();
	}
}

function setCheckboxStatuses(isSelected) {
	if(isSelected == true) {
		selectAll();
	} else {
		deSelectAll();
	}
}

function readOnlyCheckBox() {
   return false;
}

</script>
</head>
<body onload="javascript:refreshSummary()">

<s:form theme="simple" name="collectionsWorkflowForm">
<div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="${pageContext.request.contextPath}/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>
	<div class="subheadnew"><s:if test="%{isSubmitAction == true}">
		<s:text name="collectionsWorkflow.submitTitle" />
	</s:if> <s:else>
		<s:text name="collectionsWorkflow.approveTitle" />
	</s:else></div>
	<br />

	<s:if test="%{isSubmitAction == false}">
		<table width="80%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom" style="border-width: 0px">
			<tr>
				<td class="blueborderfortd" width="20%" style="border-width: 0px"
					align="right"><s:text name="collectionsWorkflow.serviceName" /></td>

				<td width="15%" class="blueborderfortd" align="left"
					style="border-width: 0px"><s:select
					headerKey="%{getText('collectionsWorkflow.serviceNames.all')}"
					headerValue="%{getText('collectionsWorkflow.serviceNames.all')}"
					name="serviceCode" id="serviceCode" cssClass="selectwk"
					list="dropdownData.serviceList" listKey="code"
					listValue="serviceName"
					onchange="return changeServiceCode(this.value)" /></td>

				<td class="blueborderfortd" width="15%" style="border-width: 0px"
					align="right"><s:text name="collectionsWorkflow.counterId" /></td>

				<td width="15%" class="blueborderfortd" align="left"
					style="border-width: 0px"><s:select headerKey="-1"
					headerValue="%{getText('collectionsWorkflow.counters.all')}"
					name="counterId" id="counterId" cssClass="selectwk"
					list="dropdownData.counterList" listKey="id" listValue="name"
					onchange="return changeCounterId(this.value)" /></td>

				<td class="blueborderfortd" width="20%" style="border-width: 0px"
					align="right"><s:text name="collectionsWorkflow.user" /></td>

				<td width="15%" class="blueborderfortd" align="left"
					style="border-width: 0px"><s:select
					headerKey="%{getText('collectionsWorkflow.users.all')}"
					headerValue="%{getText('collectionsWorkflow.users.all')}"
					name="userName" id="userName" cssClass="selectwk"
					list="dropdownData.receiptCreatorList" listKey="userName"
					listValue="userName" onchange="return changeUserName(this.value)" /></td>
			</tr>
		</table>
		<br />
	</s:if>

	<logic:notEmpty name="receiptHeaders">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">
			<div align="center">
			<display:table name="receiptHeaders"
				uid="currentRow" pagesize="30" style="border:1px;empty-cells:show;border-collapse:collapse;" cellpadding="0"
				cellspacing="0" export="false" requestURI="">

				<s:if test="%{allowPartialSelection == true}">
					<!--  Partial selection allowed. Enable the checkboxes -->
					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Select?<input type='checkbox' name='selectAllReceipts' value='on' onClick='setCheckboxStatuses(this.checked)' checked/>"
						style="width:5%; text-align: center">
						<input name="receiptIds" type="checkbox" id="receiptIds"
							value="${currentRow.id}"
							onClick="handleReceiptSelectionEvent(${currentRow.amount}, '${currentRow.instrumentType}', this.checked)"
							checked />
					</display:column>
				</s:if>
				<s:else>
					<!--  Partial selection allowed. Disable the checkboxes -->
					<display:column headerClass="bluebgheadtd" class="blueborderfortd"
						title="Select?<input type='checkbox' name='selectAllReceipts' value='on' onClick='return readOnlyCheckBox()' checked/>"
						style="width:5%; text-align: center">
						<input name="receiptIds" type="checkbox" id="receiptIds"
							value="${currentRow.id}" onClick="return readOnlyCheckBox()"
							checked />
					</display:column>
				</s:else>

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					property="receiptnumber" title="Receipt No."
					style="width:10%; text-align: center" />

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					property="receiptDate" title="Receipt Date"
					format="{0,date,dd/MM/yyyy}" style="width:10%; text-align: center" />

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					property="service.serviceName" title="Service" style="width:10%" />

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					title="Bill Number" style="width:10%">&nbsp;${currentRow.referencenumber}</display:column>

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					property="amount" title="Receipt Amount"
					format="{0, number, #,##0.00}" style="width:10%; text-align: right" />

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					property="instrumentsAsString" title="Instrument(s)"
					style="width:15%" />

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					property="receiptPayeeDetails.payeename" title="Payee Name"
					style="width:15%" />

				<display:column headerClass="bluebgheadtd" class="blueborderfortd"
					title="Bill Description" style="width:15%">&nbsp;${currentRow.referenceDesc}</display:column>

			</display:table></div>
			<br />
			<br />

			<table width="50%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="tablebottom">
				<tr>
					<th class="bluebgheadtd"><b><s:text
						name="%{getText('collectionsWorkflow.summary.instrumentType')}" /></b></th>
					<th class="bluebgheadtd"><b><s:text
						name="%{getText('collectionsWorkflow.summary.amount')}" /></b></th>
				</tr>
				<tr>
					<td class="blueborderfortd"><b><s:text
						name="%{getText('collectionsWorkflow.summary.cash')}" /></b></td>
					<td class="blueborderfortd"><s:textfield id="cashAmount"
						name="cashAmount"
						style="border-width:0px; text-align: right; width: 90%"
						disabled="true" /></td>
				</tr>
				<tr>
					<td class="blueborderfortd"><b><s:text
						name="%{getText('collectionsWorkflow.summary.cheque')}" /></b></td>
					<td class="blueborderfortd"><s:textfield name="chequeAmount"
						style="border-width:0px; text-align: right; width: 90%"
						disabled="true" /></td>
				</tr>
				<tr>
					<td class="blueborderfortd"><b><s:text
						name="%{getText('collectionsWorkflow.summary.dd')}" /></b></td>
					<td class="blueborderfortd"><s:textfield name="ddAmount"
						style="border-width:0px; text-align: right; width: 90%"
						disabled="true" /></td>
				</tr>
				<tr>
					<td class="blueborderfortd"><b><s:text
						name="%{getText('collectionsWorkflow.summary.card')}" /></b></td>
					<td class="blueborderfortd"><s:textfield name="cardAmount"
						style="border-width:0px; text-align: right; width: 90%"
						disabled="true" /></td>
				</tr>
				<tr>
					<td class="blueborderfortd"><b><s:text
						name="%{getText('collectionsWorkflow.summary.bank')}" /></b></td>
					<td class="blueborderfortd"><s:textfield name="bankAmount"
						style="border-width:0px; text-align: right; width: 90%"
						disabled="true" /></td>
				</tr>
				<tr>
					<td class="blueborderfortd"
						style="background-color: #F5F5F5; text-align: center;"><b><s:text
						name="%{getText('collectionsWorkflow.summary.total')}" /></b></td>
					<td class="blueborderfortd" style="background-color: #F5F5F5;"><s:textfield
						name="totalAmount" disabled="true"
						style="border-width: 0px;font-weight: bold; background-color: #F5F5F5; text-align: right; width: 90%" /></td>
				</tr>
			</table>

			<br />
			<s:label>Remarks</s:label>
			<s:textfield id="remarks" type="text" name="remarks"
				cssStyle="width:45%" maxlength="1024"/>
			<br />
			<br />
			<div class="buttonbottom"><!--  If action is submit, show only submit button -->
			<s:if test="%{isSubmitAction == true}">
				<s:submit type="submit" cssClass="buttonsubmit"
					id="submitCollections" name="submitCollections"
					value="Submit Collections" method="submitCollections"
					disabled="false"
					onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow!submitCollections.action'" />
			</s:if> <!-- else show only approve and reject buttons --> <s:else>
				<s:submit type="submit" cssClass="buttonsubmit"
					id="approveCollections" name="approveCollections"
					value="Approve Collections" method="approveCollections"
					disabled="false"
					onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow!approveCollections.action'" />
				&nbsp;<s:submit type="submit" cssClass="buttonsubmit"
					id="rejectCollections" name="rejectCollections"
					value="Reject Collections" method="rejectCollections"
					disabled="false"
					onclick="doLoadingMask('#loadingMask');document.collectionsWorkflowForm.action='collectionsWorkflow!rejectCollections.action'" />
			</s:else>
			&nbsp;<input type="button" class="button" id="buttonClose"
				value="<s:text name='common.buttons.close'/>"
				onclick="window.close()" />
			</div>
			
			</logic:notEmpty>

			<logic:empty name="receiptHeaders">
				<table width="90%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<div>&nbsp;</div>
						<div class="subheadnew"><s:text
							name="collectionsWorkflow.noReceipts" /></div>
					</tr>
				</table>
				<br />
					<input type="button" class="button" id="buttonClose"
						value="<s:text name='common.buttons.close'/>"
						onclick="window.close()" />
				</logic:empty>
			</s:form></body>
</html>