<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<title><s:text name='page.header.negotiation' />
	</title>
	<body onload="getDefaults();populateDetails();populateDesignation();">
		<script src="<egov:url path='js/works.js'/>"></script>
		<%-- script src="<egov:url path='js/helper.js'/>"></script --%>		
		<script>

function getDefaults() {
	getCurrentDate();
	roundOffEstimateAmount();	
	toggleDetails(document.tenderNegotiationForm.tenderType);
	getMarketValueEstimateAmount();
	if(document.tenderNegotiationForm.tenderType.value==dom.get("tenderType")[1].value) {
		document.tenderNegotiationForm.percQuotedRateAmount.value=document.tenderNegotiationForm.percQuotedRate.value;
		var perQuotedRateValue=document.tenderNegotiationForm.percQuotedRateAmount.value;
		if(perQuotedRateValue.startsWith("-")){
			perQuotedRateValue=perQuotedRateValue.substring(1,perQuotedRateValue.length);
			document.tenderNegotiationForm.percQuotedRateAmount.value=perQuotedRateValue;;
			dom.get('percSign').value=1;
		}
		/* Added for story card #1110. Providing a drop down to select the +/- sign for negotiated rate in the Negotiation Details */
		document.tenderNegotiationForm.percNegotiatedRate.value=document.tenderNegotiationForm.percNegotiatedAmountRate.value;
		
		var percNegotiatedRateValue=document.tenderNegotiationForm.percNegotiatedRate.value;
		if(percNegotiatedRateValue.startsWith("-")){
			percNegotiatedRateValue=percNegotiatedRateValue.substring(1,percNegotiatedRateValue.length);
			document.tenderNegotiationForm.percNegotiatedRate.value=percNegotiatedRateValue;
			dom.get('percSignNegoRate').value=1;
		}
		
		getQuotedAmount(document.tenderNegotiationForm.percQuotedRateAmount);		
		getNegotiatedAmount(document.tenderNegotiationForm.percNegotiatedRate);
	}
	dispWorkOrderAmtTable();
}

function dispWorkOrderAmtTable(){

 	var statusObj=document.getElementById("status");
 	if(statusObj!=null){
	var statusIndex = statusObj.selectedIndex;
	var statusValue=statusObj.options[statusIndex].text;
	
	if(statusObj.options[statusIndex].text=='Work OrderIssued'){

	  document.getElementById("workOrderamountdetails").style.display='';	
			
	} 
	else{

	  document.getElementById("workOrderamountdetails").style.display='none';	
	}	
	}
}

function showHeaderTab(){
  var hiddenid = document.forms[0].id.value;
  document.getElementById('negotiation_header').style.display=''; 
 // document.getElementById("workOrderamountdetails").style.display='';
 dispWorkOrderAmtTable();
  setCSSClasses('detailsTab','Last');
  setCSSClasses('headerTab','First Active'); 
  //setCSSClasses('headerTab','Last'); 
  hideDetailsTab();
  dispWorkOrderAmtTable();
}

function hideHeaderTab(){
  document.getElementById('negotiation_header').style.display='none';
  document.getElementById("workOrderamountdetails").style.display='none';
}

function hideDetailsTab(){
  document.getElementById('negotiation_details').style.display='none';
}

function showDetailsTab(){ 
  document.getElementById('negotiation_details').style.display='';
  document.getElementById('detailsTab').setAttribute('class','Active');
  document.getElementById('detailsTab').setAttribute('className','Active');
  hideHeaderTab();
 
  setCSSClasses('detailsTab','Last Active ActiveLast');
  setCSSClasses('headerTab','First BeforeActive');
  var negotiationDate=document.forms[0].negotiationDate.value;
  var hiddenNegotiationDate=document.forms[0].hiddenNegotiationDate.value;
 
  if(negotiationDate!=''){ 	
  	getMarketRatesAsOnDateChange();		
	calculateMarketRateAmount();
  } 
}

function setCSSClasses(id,classes){
    document.getElementById(id).setAttribute('class',classes);
    document.getElementById(id).setAttribute('className',classes);
}

function validateDataBeforeSubmit(tenderNegotiationForm) {
    if(!validateHeaderBeforeSubmit(tenderNegotiationForm)) {
    	disableContractorDetails();
    	return false;	
    }    	
    if(!validateDetailsBeforeSubmit(tenderNegotiationForm)) {
    	disableContractorDetails();
    	return false;
    }
    return true;
}

function enableFields(){
	for(i=0;i<document.tenderNegotiationForm.elements.length;i++){
	        document.tenderNegotiationForm.elements[i].disabled=false;
	        document.tenderNegotiationForm.elements[i].readonly=false;	       
	}		
	
}

function getCurrentDate() {
	var negotiationDate=document.tenderNegotiationForm.negotiationDate;	
	if(negotiationDate.value=='') {
		negotiationDate.value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
	}
	var approvedDate=document.tenderNegotiationForm.approvedDate;
	if(approvedDate!=null) {
		<s:if test="%{model.currentState!=null && model.currentState.value!=null && model.currentState.previous!=null && model.currentState.previous.value=='APPROVED'}">
			if(approvedDate.value=='') {
				approvedDate.value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
			}
		</s:if>
	}
}

<!-- setaction for setting status -->
	function setAction(){		
		enableFields();
 		var statusObj=document.getElementById("status");
		var statusIndex = statusObj.selectedIndex;
		var newStatusValue=statusObj.options[statusIndex].text; 
		 if(newStatusValue == "Acceptance Letter Issued"){
		 	document.tenderNegotiationForm.nsActionName.value="ACCEPTANCE_LETTER_ISSUED";
		 }else if(newStatusValue == "Acceptance Letter Acknowledged"){
		 	document.tenderNegotiationForm.nsActionName.value="ACCEPTANCE_LETTER_ACKNOWLEDGED";
		 }else if(newStatusValue == "Agreement Order signed"){
		 	document.tenderNegotiationForm.nsActionName.value="AGREEMENT_ORDER_SIGNED";
		 }else if(newStatusValue == "Work OrderIssued"){
		 	document.tenderNegotiationForm.nsActionName.value="WORK_ORDER_ISSUED";
		 }else if(newStatusValue == "Work Order Acknowledged"){
		 	document.tenderNegotiationForm.nsActionName.value="WORK_ORDER_ACKNOWLEDGED";
		 }else if(newStatusValue == "Site Handing Over"){
		 	document.tenderNegotiationForm.nsActionName.value="SITE_HANDING_OVER";
		 }
		 
		document.getElementById("option").value="setStatus";	
		document.getElementById("approvedDateHidden").value=document.getElementById("approvedDate").value
	}
	
	function validateCancel() {
		var msg='<s:text name="tenderNegotiation.cancel.confirm"/>';
		var estNo='<s:property value="model.negotiationNumber"/>';
		if(!confirmCancel(msg,estNo)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	function viewDocument(){
	  viewDocumentManager(dom.get("docNumber").value); 
	}
	
	function diableTenderType(){
		dom.get('tenderType').disabled=true
	}


function validateWFUser(name){
	document.getElementById('approver_error').style.display ='none';
	if(name != 'reject' && name != 'cancel' && name!='save') {
	 	<s:if test="%{model.currentState==null || (model.currentState.nextAction!='Approval completed')}" >
		if(null != document.getElementById("designationId") && document.getElementById("designationId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.designation.null"/>';
			return false;
		}
		if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
			document.getElementById('approver_error').style.display ='';	
			document.getElementById('approver_error').innerHTML ="";
			document.getElementById('approver_error').innerHTML ='<s:text name="workflow.approver.null"/>';
			return false;
		} 
	 	</s:if>
	}
	return true; 
}
	
function validate(text){	
	if(!validateWFUser(text))
		return false;
		
	enableFields();
	return true;
}

function validateResolData() {
	<s:if test="%{model !=null && model.currentState!=null && model.currentState.value=='CHECKED' && model.currentState.owner.desigId.designationName == 'CHIEF ENGINEER'}">		
		if(!validateResolutionData())
			return false;
	</s:if>	
	return true;
}	

function confirmApproveNote(){
	if(!validateResolData())
		return false;
	enableFields();
	if(!validateDataBeforeSubmit(document.tenderNegotiationForm))
	   return false;	
	window.open("${pageContext.request.contextPath}/tender/tenderNegotiation!acceptanceNote.action",'', 'height=500,width=700,scrollbars=yes,left=0,top=0,status=yes');
		
}


</script>
		<s:if test="%{hasErrors()}"> 
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<div class="errorstyle" id="negotiation_error" style="display: none;">
		<s:text name="tenderHeader.tenderNo.isunique"/></div>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:property value="%{tenderHeader.tenderNo}"/>/<s:property value="%{negotiationNumber}" />
				&nbsp;
				<s:actionmessage theme="simple" />
			</div>
		</s:if>


		<s:form theme="simple" name="tenderNegotiationForm"
			onsubmit="return validateDataBeforeSubmit(this);">
			<s:push value="model">
				<s:hidden name="id" id="id"/>
				<s:hidden name="tenderHeader.id" />
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2">
								<div></div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">
									<div class="estimateno">
										<s:text name="tenderNegotiation.negotiationNo" />
										:
										<s:if test="%{not negotiationNumber}">&lt; <s:text
												name="message.notAssigned" /> &gt;</s:if>
										<s:property value="negotiationNumber" />
									</div>
									<!--<span class="bold"><s:text name="message.today" />
									</span>
									<egov:now />-->
								</div>
		<s:hidden name="model.documentNumber" id="docNumber" />
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
											<div id="header">
												<ul id="Tabs">
													<li id="headerTab" class="First Active">
														<a id="header_1" href="#" onclick="showHeaderTab();"><s:text
																name="tenderNegotiation.tab.header" />
														</a>
													</li>
													<li id="detailsTab" class="Last">
														<a id="header_4" href="#" onclick="showDetailsTab();"><s:text
																name="tenderNegotiation.tab.details" />
														</a>
													</li>
												</ul>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<div id="negotiation_header">
												<%@ include file="negotiation-header.jsp"%>
											</div>
										</td>
									</tr>
<!-- start workorder amont -->
									<tr>
									  <td>
									    <div id="workOrderamountdetails" style="display:none;">
									 	 <%@ include file="workOrderAmountDetails.jsp"%>
									    </div>
									  </td>
									</tr>
<!-- end workorder amont -->
									<tr>
										<td>
											<div id="negotiation_details" style="display: none;">
												<%@ include file="negotiation-details.jsp"%> 
											</div>
										</td>
									</tr>
						<!--  negotiationprepared by onjan9th 2010  -->
								  	<tr>
								            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
								              <tr>
								                <td width="17%" class="whiteboxwk"><span class="mandatory">*</span><span class="epstylewk"><s:text name='negotiation.preparedBy'/>:</span></td>
								                <td width="17%" class="whitebox2wk">            
													 <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="negotiationPreparedBy" id="negotiationPreparedBy" 
													value="%{negotiationPreparedBy.idPersonalInformation}" cssClass="selectwk" list="dropdownData.preparedByList"
													listKey="id" listValue="employeeName" onchange='showNegotiationDesignation(this);'/>
								                </td>
								                <td width="12%" class="whiteboxwk"><s:text name='tenderNegotiation.approverdesignation'/>:</td>
								                <td width="54%" class="whitebox2wk"><s:textfield name="designationNegotiation"  type="text" readonly="true" disabled="disabled" cssClass="selectboldwk" id="designationNegotiation" size="40" tabIndex="-1" /></td>								
								              </tr>
								            </table></td>
								            <s:hidden name="scriptName" id="scriptName" value="TenderResponse"></s:hidden>
								 	</tr>
								 	
								 	<tr> 
									    <td>
									    <div id="manual_workflow">
									   		<%@ include file="../workflow/workflow.jsp"%> 
									    </div>
									     </td>
            						</tr>		
            						
            						<tr id="resolutionDetailsRow" style="display: none" > 
            							<td>
									    <div id="resolution_details"> 
										   	<%@ include file="tender-resolutionData.jsp"%> 
									    </div> 
									    </td>
            						</tr>						 	
								 	 							 		
									<tr>
										<td>
											<div align="right" class="mandatory"
												style="font-size: 11px; padding-right: 20px;">
												*
												<s:text name="message.mandatory" />
											</div>
										</td>
									</tr>
								</table>
							</div>
							<div class="rbbot2">
								<div></div> 
							</div>
						</div>
					</div>
				</div>
				<div class="buttonholderwk" id="buttons">
					<input type="hidden" name="actionName" id="actionName" />
					<!--   Action buttons have to displayed only if the page is directed from the inbox   -->
					<s:if test="%{sourcepage=='inbox' || model.currentState==null || 
	model.currentState.value=='NEW' || model.currentState.value=='REJECTED' || hasErrors()}">
					
						<s:iterator value="%{validActions}">
							<s:if test="%{description!=''}">
								<s:if test="%{description=='CANCEL' && model.negotiationNumber!=null}">
									<s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" 
										name="%{name}" method="cancel" 
										onclick="enableFields();document.tenderNegotiationForm.actionName.value='%{name}';return validateCancel();"/>
							  	</s:if>
								<s:elseif test="%{name != null && name == 'reject'}">
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="%{name}"
										onclick="enableFields();document.tenderNegotiationForm.actionName.value='%{name}'" />
								</s:elseif>
								<s:elseif test="%{name != null && name == 'approve'}">
									<input type="button" class="buttonfinal" value="APPROVE" id="approve"
											name="approve" onclick="document.tenderNegotiationForm.actionName.value='approve';return confirmApproveNote();" />
								</s:elseif>
							
								<s:elseif test="%{name != null && name == 'forward' && model.currentState.previous.owner.desigId.designationName != 'CHIEF ENGINEER'}">
								<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.tenderNegotiationForm.actionName.value='%{name}';return validate('%{name}');" />
								</s:elseif>
								
								
									<!-- 
								<s:elseif test="%{name != null && name == 'forward' && sourcepage=='inbox' && model.currentState.owner.desigId.designationName == 'SUPERINTENDING ENGINEER'}">
								<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.tenderNegotiationForm.actionName.value='%{name}';return validate('%{name}');" />
								</s:elseif>
								 -->
								
								<s:elseif test="%{name != null && name != 'forward'}">
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{description}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.tenderNegotiationForm.actionName.value='%{name}';return validate('%{name}');"/>
								</s:elseif>
								
							</s:if>
						</s:iterator>
						<!--  s:submit type="submit" cssClass="buttonfinal" value="SAVE" id="save" name="save" method="save" /  -->
					</s:if>
					
					<s:if test="%{id==null}">
						<input type="button" class="buttonfinal" value="CLEAR" id="button"
							name="button"
							onclick="window.open('${pageContext.request.contextPath}/tender/tenderNegotiation!newform.action?estimateId=<s:property value='%{estimateId}'/>','_self');" />
					</s:if>
					<input type="button" class="buttonfinal" value="CLOSE"
						id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='tenderNegotiation.close.confirm'/>');" />
					<s:if test="%{model.id!=null && model.negotiationNumber!=null}">
						<input type="button"
							onclick="window.open('${pageContext.request.contextPath}/tender/tenderNegotiationPDF.action?tenderResponseId=<s:property value='%{model.id}'/>');"
							class="buttonpdf" value="VIEW PDF" id="pdfButton"
							name="pdfButton" />
					</s:if>
					
					<s:if test="%{mode=='search'}">
  						<input type="submit" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
  					</s:if>
 					<s:else>
  						<input type="submit" class="buttonadd" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();return false;" /> 
  					</s:else>
				</div>
			</s:push>
		</s:form>
		
		<s:elseif test=""></s:elseif>
<script>

function disableContractorDetails() {
	records=contractorsDataTable.getRecordSet();
 	for(i=0;i<records.getLength();i++){   
       	dom.get("code"+records.getRecord(i).getId()).disabled=true;
        dom.get("name"+records.getRecord(i).getId()).disabled=true;
    }
}

function inboxState(){
<s:if test="%{(model.id!=null && hasErrors()) || model.id!=null}">
  diableTenderType();
</s:if>
 
 <s:if test="%{mode=='search' || (sourcepage=='inbox' && model.currentState.value=='CHECKED' && model.currentState.owner.desigId.designationName == 'CHIEF ENGINEER')}">
        toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
      	//hideElements(['workflowDetials']);
      	showElements(['approverCommentsRow']);
      	links=document.tenderNegotiationForm.getElementsByTagName("a"); 
        disableLinks(links,['aprdDatelnk']);
        document.getElementById('resolutionDetailsRow').style.display="";
       	enableResolutionFields();
        <s:if test="%{model !=null && model.currentState!=null && model.currentState.previous!=null && model.currentState.previous.owner.desigId.designationName == 'CHIEF ENGINEER'}">
        	hideElements(['workflowDetials']);        	
       	</s:if>
       	 contractorsDataTable.removeListener('cellClickEvent');
 </s:if>
 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value=='NEW'}">
        toggleFields(false,[]);
        showElements(['approverCommentsRow']);
        diableTenderType();
        disableContractorDetails();
 </s:elseif>
 <s:elseif test="%{sourcepage=='inbox' && model.currentState.value!='REJECTED' && model.currentState.value!='END'}">
        toggleFields(true,['departmentid','designationId','approverUserId','approverComments']);
        links=document.tenderNegotiationForm.getElementsByTagName("a"); 
        disableLinks(links,['aprdDatelnk']);
        showElements(['approverCommentsRow']);
        contractorsDataTable.removeListener('cellClickEvent');
 </s:elseif>
<s:if test="%{mode=='search' && (model.currentState.value=='END' 
&& model.currentState.previous.value=='APPROVED') ||  (model.currentState.value=='Acceptance Letter Issued' || 
model.currentState.value=='Acceptance Letter Acknowledged'
 	  || model.currentState.value=='Agreement Order signed' || model.currentState.value=='Work OrderIssued' 
 	  || model.currentState.value=='Work Order Acknowledged' || model.currentState.value=='Site Handing Over')}">
    toggleFields(true,[]);
    	var linksList=document.tenderNegotiationForm.getElementsByTagName("a");
   	for(j=0;j<linksList.length;j++){
			if(links[j].id=='resolutionDatelnk')
				links[j].onclick=function(){return false;};    
	} 
     contractorsDataTable.removeListener('cellClickEvent');
</s:if>
<s:if test="%{mode=='search' || sourcepage=='inbox'}">
 enabledivChilderns("buttons");
</s:if>
<s:if test="%{mode=='search'}">
	hideElements(['approverCommentsRow']);
	 contractorsDataTable.removeListener('cellClickEvent');
</s:if>
} 
</script>
</body>
</html>