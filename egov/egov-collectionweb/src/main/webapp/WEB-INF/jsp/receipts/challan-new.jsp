<%@ include file="/includes/taglibs.jsp" %>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/challan.js"></script>
<script type="text/javascript" src="/egi/commonyui/build/autocomplete/autocomplete-debug.js"></script>
<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
#subledgercodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#subledgercodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#subledgercodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#subledgercodescontainer ul {padding:5px 0;width:100%;}
	#subledgercodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#subledgercodescontainer li.yui-ac-highlight {background:#ff0;}
	#subledgercodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	th.yui-dt-hidden,
tr.yui-dt-odd .yui-dt-hidden,
tr.yui-dt-even .yui-dt-hidden
 {
display:none;
}
</style>
<script type="text/javascript">
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

 
path="${pageContext.request.contextPath}";
<jsp:useBean id="now" class="java.util.Date" />

<fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${now}" />
var currDate = "${currDate}";



function onBodyLoad(){
	if(document.getElementById('challanDate').value==""){
		document.getElementById("challanDate").value=currDate;
	}
	
	if('<s:property value="designationId"/>'!=null && '<s:property value="designationId"/>'!="")
	{
		onChangeDesignation('<s:property value="designationId"/>');
	}
	loadDropDownCodesFunction();
	loadDropDownCodes();
	
	loadFinancialYearList();
	
	check();
	
	if(dom.get("deptId")!=null){
		document.getElementById('deptId').disabled=true;
	}
	
	// page has to be disabled when view through search option/ when challan has to be modified -->
	<s:if test="%{sourcePage=='search' || (model.id!=null && model.challan.state.value=='CREATED') || (sourcePage=='inbox' && model.challan.state.value!='REJECTED')}">
			setAsViewPage();
	</s:if>
	return true;
}


	

function refreshInbox() {
        var x=opener.top.opener;
        if(x==null){
            x=opener.top;
        }
        x.document.getElementById('inboxframe').contentWindow.egovInbox.from = 'Inbox';
	    x.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
}



function setAsViewPage(){
	var el = document.forms[0].elements;
	
	// disable all elements except the workflow action buttons, print button and close button
	for(var i=0;i<el.length;i++){
	
		if(el[i].name!='method:save'){
		
			el[i].setAttribute('disabled',true);
		}
		if(dom.get("buttonprint")!=null){
			document.getElementById('buttonprint').disabled=false;
			document.getElementById('buttonclose2').disabled=false;		
		}
	
	}
	 
	<s:if test="%{sourcePage=='inbox' && model.challan.state.value=='APPROVED'}">
	if(document.getElementById('receiptMisc.fund.id')!=null)
		document.getElementById('receiptMisc.fund.id').disabled=false;
	if(document.getElementById('functionCode')!=null)
		document.getElementById('functionCode').disabled=false;
		 for(var i=0;i<billDetailTableIndex+1;i++)
	{
		if(null != document.getElementById('billDetailslist['+i+'].accounthead')){
			document.getElementById('billDetailslist['+i+'].functionDetail').disabled=false;
			document.getElementById('billDetailslist['+i+'].functionIdDetail').disabled=false;
			document.getElementById('billDetailslist['+i+'].accounthead').disabled=false;
			document.getElementById('billDetailslist['+i+'].glcodeIdDetail').disabled=false;
		}
	}
	  for (var j=0; j<slDetailTableIndex+1; j++ )
		{
			if(document.getElementById('subLedgerlist['+j+'].glcode.id')!=null){
				document.getElementById('subLedgerlist['+j+'].glcode.id').disabled=false;
				document.getElementById('subLedgerlist['+j+'].detailType.id').disabled=false;
				document.getElementById('subLedgerlist['+j+'].detailCode').disabled=false;
				document.getElementById('subLedgerlist['+j+'].detailKey').disabled=false;
				document.getElementById('subLedgerlist['+j+'].detailTypeName').disabled=false;
				document.getElementById('subLedgerlist['+j+'].detailKeyId').disabled=false;
			}
		}
	</s:if>
	document.getElementById('calendarLink').style.display="none";
	document.getElementById('receiptId').disabled=false;
	document.getElementById('actionName').disabled=false;
	document.getElementById('sourcePage').disabled=false;
	<s:if test="%{sourcePage=='inbox'}"> 
	if(document.getElementById('approvalRemarks')!=null)
		document.getElementById('approvalRemarks').disabled=false;
	if(document.getElementById('designationId')!=null)
		document.getElementById('designationId').disabled=false;
	if(document.getElementById('approverDeptId')!=null)
		document.getElementById('approverDeptId').disabled=false;
	if(document.getElementById('positionUser')!=null)
		document.getElementById('positionUser').disabled=false;	
	if(document.getElementById('buttonverify')!=null)
		document.getElementById('buttonverify').disabled=false;
	if(document.getElementById('buttonreject')!=null)
		document.getElementById('buttonreject').disabled=false;
	</s:if>
}


function validate(obj){
	var actionname=document.challan.actionName.value;
	var valid=true;
	dom.get("challan_error_area").style.display="none";
	document.getElementById("challan_error_area").innerHTML="";
 	if(dom.get("actionErrors")!=null)
 	{
		dom.get("actionErrors").style.display="none";
	}
	if(dom.get("actionMessages")!=null)
	{
		dom.get("actionMessages").style.display="none";
	}
		
	//validations for challan approval
	if(actionname=="CHALLAN_CHECK" || actionname=="CHALLAN_APPROVE" || actionname=="CHALLAN_REJECT")
	{
		if(obj.value=="Reject Challan")
		{
			if(dom.get("approvalRemarks").value.trim().length==0)
			{
				document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.approvalRemarks.errormessage" />'+ "<br>";
				valid=false;
			}
		}
	}
	//validations for challan cancel
	else if(actionname=="CHALLAN_CANCEL"){
		if(obj.value=="Cancel Challan")
		{
			if(dom.get("challan.reasonForCancellation").value.trim().length==0)
			{
				document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.reasonForCancellation.errormessage" />'+ "<br>";
				valid=false;
			}
		}
	}
	//validations for challan create/modify
	else{
	 	if(null != document.getElementById('challanDate') && document.getElementById('challanDate').value.trim().length == 0){
			document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.challanDate.errormessage" />'+ "<br>";
			valid=false;
		}
		 
		 <s:if test="%{isFieldMandatory('fund')}"> 
	 	 	if(null != document.getElementById('receiptMisc.fund.id') && document.getElementById('receiptMisc.fund.id').value == -1){
				document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.fundcode.errormessage" />'+  "<br>";
				valid=false;
		 	}
		</s:if>
		<s:if test="%{isFieldMandatory('department')}"> 
			 if(null!= document.getElementById('deptId') && document.getElementById('deptId').value == -1){
					document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.deptcode.errormessage" />'+ '<br>';
					valid=false;
			 }
		</s:if>
		if(!validateAccountDetail()){
			valid=false;
		}
		if(!validateSubLedgerDetail()){
			valid=false;
		}
		if(null!= document.getElementById('designationId') && document.getElementById('designationId').value == -1){
			document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.designation.errormessage" />'+ "<br>";
			valid=false;
		}
		
		if(null!= document.getElementById('positionUser') && document.getElementById('positionUser').value == -1){
			document.getElementById("challan_error_area").innerHTML+='<s:text name="challan.position.errormessage" />'+ "<br>";
			valid=false;
		}
		
	}
	if(valid==false){
			dom.get("challan_error_area").style.display="block";
			window.scroll(0,0);
	}
	else{
	var elems = document.forms[0].elements;
		for(var i=0;i<elems.length;i++){
			elems[i].disabled=false;
		}
		doLoadingMask('#loadingMask');
	}

	return valid;
	
}

function checkreset(){
	document.forms[0].reset();
	dom.get("challan_error_area").style.display="none";
	if(dom.get("actionErrors")!=null){
		dom.get("actionErrors").style.display="none";}
	if(dom.get("actionMessages")!=null){
		dom.get("actionMessages").style.display="none";}
	document.getElementById("challanDate").value=currDate;
	if(dom.get("receiptMisc.fund.id")!=null){
	document.getElementById("receiptMisc.fund.id").selectedIndex = 0;
	}
	if(dom.get("referencenumber")!=null){
		document.getElementById("referencenumber").value="";
	}
	document.getElementById("totalcramount").value=0;
	//document.getElementById("totaldbamount").value=0;
	resetTables('<s:property value="%{currentFinancialYearId}"/>');
}



function loadFinancialYearList(){
var fYear=new Array();
var fcount=0;
	<s:iterator value="%{billDetailslist}">
		fYear[fcount]='<s:property value="financialYearId"/>';
		fcount++;
	</s:iterator>
	for(var i=0;i<billDetailTableIndex+1;i++){
		if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
			var selectedyearid=fYear[i];
			var obj=document.getElementById('billDetailslist['+i+'].financialYearId');
			for(var k=0;k<obj.options.length;k++){
					if(obj.options[k].value==selectedyearid){
						obj.options[k].selected=true;
					}
			}
		}
	}
	
}

var totalcramt=0;
var totaldbamt=0;
var fYearOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.financialYearList">
	    fYearOptions.push({label:'<s:property value="finYearRange"/>', value:'<s:property value="id"/>'})
	</s:iterator>
var makeBillDetailTable = function() {
		var billDetailColumns = [ 
			{key:"function",label:'Function', formatter:createTextFieldFormatterForFunction(VOUCHERDETAILLIST,".functionDetail",VOUCHERDETAILTABLE)},
			{key:"accounthead", label:'Account Head <span class="mandatory">*</span>',formatter:createLongTextFieldFormatter(VOUCHERDETAILLIST,".accounthead",VOUCHERDETAILTABLE)},				
			{key:"glcode",label:'Account Code ', formatter:createTextFieldFormatter(VOUCHERDETAILLIST,".glcodeDetail","text",VOUCHERDETAILTABLE)},
			{key:"creditamount",label:'Amount (Rs.)', formatter:createAmountFieldFormatter(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmount()",VOUCHERDETAILTABLE)},
			{key:"financialYearId",label:'Financial Year <span class="mandatory">*</span>', formatter:createDropdownFormatterFYear(VOUCHERDETAILLIST,'<s:property value="%{currentFinancialYearId}"/>'),  dropdownOptions:fYearOptions},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
			{key:"glcodeid",hidden:true, formatter:createTextFieldFormatter(VOUCHERDETAILLIST,".glcodeIdDetail","hidden",VOUCHERDETAILTABLE)},
			{key:"functionid",hidden:true,formatter:createTextFieldFormatter(VOUCHERDETAILLIST,".functionIdDetail","hidden")}
		];
		
	    var billDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",billDetailColumns, billDetailDS);
		
		<s:if test="%{sourcePage=='search' || (model.id!=null && model.challan.state.value=='CREATED') || (sourcePage=='inbox' && model.challan.state.value!='REJECTED')}">
			var addColumn = billDetailsTable.getColumn(5);
			billDetailsTable.hideColumn(addColumn);
			var delColumn = billDetailsTable.getColumn(6);
			billDetailsTable.hideColumn(delColumn);
		</s:if>
		billDetailsTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
				if (column.key == 'Add') { 
					billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
					updateAccountTableIndex();
				}
				if (column.key == 'Delete') { 	
					if(this.getRecordSet().getLength()>1){			
						this.deleteRow(record);
						allRecords=this.getRecordSet();
						for(var i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}
						updateCreditAmount();
						// updateDebitAmount();
						updatetotalAmount();
						check();
					}
					else{
						alert("This row can not be deleted");
					}
				}
					 
		});
		<s:iterator value="billDetailslist" status="stat">
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
				    "functionid":'<s:property value="functionIdDetail"/>',
				    "function":'<s:property value="functionDetail"/>',
					"glcodeid":'<s:property value="glcodeIdDetail"/>',
					"glcode":'<s:property value="glcodeDetail"/>',
					"accounthead":'<s:property value="accounthead"/>',
					"creditamount":'<s:property value="%{creditAmountDetail}"/>',
					"financialYearId":'<s:property value="%{fYear}"/>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGrid(VOUCHERDETAILLIST,'functionIdDetail',index,'<s:property value="functionIdDetail"/>');
				updateGrid(VOUCHERDETAILLIST,'functionDetail',index,'<s:property value="functionDetail"/>');
				updateGrid(VOUCHERDETAILLIST,'glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
				updateGrid(VOUCHERDETAILLIST,'glcodeDetail',index,'<s:property value="glcodeDetail"/>');
				updateGrid(VOUCHERDETAILLIST,'accounthead',index,'<s:property value="accounthead"/>');
				updateGrid(VOUCHERDETAILLIST,'creditAmountDetail',index,'<s:property value="creditAmountDetail"/>');
				updateGridDropdown('financialYearId',index,'<s:property value="finYearRange"/>','<s:property value="id"/>');
				totalcramt = totalcramt+parseFloat('<s:property value="creditAmountDetail"/>');
				// totaldbamt = totaldbamt+parseFloat('<s:property value="debitAmountDetail"/>');
				updateAccountTableIndex();	
			</s:iterator>
				
	
		var tfoot = billDetailsTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var td1 = tr.insertCell(-1);
		td1.align="center";
		td1.colSpan = 3;
		td1.setAttribute('class','tdfortotal');
		td1.style.textAlign="right";
		td1.style.borderTop = '1px #c8c8c8 solid';
		td1.innerHTML='<b>Total</b>&nbsp;&nbsp;&nbsp;';
		td1.className='tdfortotal';
		var td2 = tr.insertCell(-1);
		td2.width="100";
		td2.height="32";
		td2.align="center";
		td2.style.borderTop = '1px #c8c8c8 solid';
		td2.setAttribute('class','tdfortotal');
		td2.className='tdfortotal';
		td2.style.padding='4px 10px';
		td2.innerHTML="<input type='text' style='text-align:right;width:80px;align:center;height:20px;'  id='totalcramount' name='totalcramount' readonly='true' tabindex='-1'/>";
		if(totalcramt>0){
			totalcramt=totalcramt.toFixed(2);
		}
		document.getElementById('totalcramount').value=totalcramt;
		// var td3 = tr.insertCell(-1);
		// td3.width="100";
		// td3.height="32";
		// td3.align="center";
		// td3.style.borderTop = '1px #c8c8c8 solid';
		// td3.setAttribute('class','tdfortotal');
		// td3.className='tdfortotal';
		// td3.style.padding='4px 10px';
		// td3.innerHTML="<input type='text' style='text-align:right;width:80px;align:center;height:20px;'  id='totaldbamount' name='totaldbamount' readonly='true' tabindex='-1'/>";
		// document.getElementById('totaldbamount').value=totaldbamt;
		<s:if test="%{sourcePage=='search' || (model.id!=null && model.challan.state.value=='CREATED') || (sourcePage=='inbox' && model.challan.state.value!='REJECTED')}">
			var td4 = tr.insertCell(-1);
			td4.style.borderTop = '1px #c8c8c8 solid';
			td4.setAttribute('class','tdfortotal');
			td4.className='tdfortotal';
			td4.innerHTML='&nbsp;';
		</s:if>
	<s:else>
		var td4 = tr.insertCell(-1);
		td4.colSpan = 4;
		td4.style.borderTop = '1px #c8c8c8 solid';
		td4.setAttribute('class','tdfortotal');
		td4.className='tdfortotal';
		td4.innerHTML='&nbsp;';
		</s:else>
	}//end of makeBillDetailTable
	
		
	var glcodeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.glcodeList">
	    glcodeOptions.push({label:'<s:property value="glcode"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var detailtypeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.detailTypeList">
	    detailtypeOptions.push({label:'<s:property value="name"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var detailCodeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.detailCodeList">
	    detailtypeOptions.push({label:'<s:property value="name"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var makeSubLedgerTable = function() {
		var subledgerColumns = [ 
			
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>', formatter:createDropdownFormatterCode(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>', formatter:createDropdownFormatterDetail(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',formatter:createSLDetailCodeTextFieldFormatter(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)")},
			{key:"detailKey",label:'Name', formatter:createSLLongTextFieldFormatter(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount (Rs.)', formatter:createSLAmountFieldFormatter(SUBLEDGERLIST,".amount")},
			{key:"glcode",hidden:true, formatter:createSLHiddenFieldFormatter(SUBLEDGERLIST,".subledgerCode")},
			{key:"detailTypeName",hidden:true, formatter:createSLHiddenFieldFormatter(SUBLEDGERLIST,".detailTypeName")},
			{key:"detailKeyId",hidden:true, formatter:createSLHiddenFieldFormatter(SUBLEDGERLIST,".detailKeyId")}
		];
	    var subledgerDS = new YAHOO.util.DataSource(); 
		subLedgersTable = new YAHOO.widget.DataTable("subLedgerTable",subledgerColumns, subledgerDS);
		
		subLedgersTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
				if (column.key == 'Add') { 
					subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
					updateSLTableIndex();
					check();
				}
				if (column.key == 'Delete') { 			
					if(this.getRecordSet().getLength()>1){			
						this.deleteRow(record);
						allRecords=this.getRecordSet();
						for(var i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}
					}
					else{
						alert("This row can not be deleted");
					}
				}  
		});
		<s:iterator value="subLedgerlist" status="stat">
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
					"glcode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailTypeName":'<s:property value="detailTypeName"/>',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKey"/>',
					"detailKey":'<s:property value="detailKey"/>',
					"creditAmount":'<s:property value="%{creditAmount}"/>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridSLDropdown('glcode.id',index,'<s:property value="glcode.id"/>','<s:property value="subledgerCode"/>');
				updateGridSLDropdown('detailType.id',index,'<s:property value="detailType.id"/>','<s:property value="detailTypeName"/>');
				updateSLGrid('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGrid('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGrid('detailKey',index,'<s:property value="detailKey"/>');
				updateSLGrid('amount',index,'<s:property value="amount"/>');
				updateSLTableIndex();
			</s:iterator>
		
	}

function onChangeDesignation(designationId)
{
	 var approverDeptId;
	 if(document.getElementById('approverDeptId')){
	  approverDeptId=document.getElementById('approverDeptId').value;
	 }
	if(document.getElementById('positionUser')){
		populatepositionUser({designationId:designationId,approverDeptId:approverDeptId});
	}
}
function onChangeDeparment(approverDeptId)
{
	var receiptheaderId='<s:property value="model.id"/>';
	if(document.getElementById('designationId')){
		populatedesignationId({approverDeptId:approverDeptId,receiptheaderId:receiptheaderId});
	}
}

function openVoucherSearch(){
	window.open ("/EGF/voucher/voucherSearch!beforesearch.action","VoucherSearch","resizable=yes,scrollbars=yes,top=40, width=900, height=650");
}
function populatepositionuseronload()
{
	if(document.getElementById('positionUser')){
		document.getElementById('positionUser').value='<s:property value="positionUser"/>';
	}	
}



</script>
<title><s:text name="challan.pagetitle"/>
</title>
</head>
<div class="errorstyle" id="challan_error_area" style="display:none;"></div>
<body onLoad="onBodyLoad();refreshInbox();window.setTimeout('populatepositionuseronload()', 2000);" ><br>
<div class="formmainbox">
<s:if test="%{hasErrors()}">
    <div id="actionErrors" class="errorstyle">
      <s:actionerror/>
      <s:fielderror/>
    </div>
</s:if>
<s:if test="%{hasActionMessages()}">
    <div id="actionMessages" class="messagestyle" align="center">
    	<s:actionmessage theme="simple"/>
    </div>
    <div class="blankspace">&nbsp;</div>
</s:if>

<s:form theme="simple" name="challan" action="challan">
<s:token/>
<s:push value="model">

<div class="subheadnew">
<s:if test="%{model.id==null}" >
	<s:text name="challan.title.create"/>
</s:if>
<s:elseif test="%{sourcePage=='inbox' && model.challan.state.value=='CREATED'}">
	<s:text name="challan.title.check"/>
</s:elseif>
<s:elseif test="%{sourcePage=='inbox' && model.challan.state.value=='CHECKED'}">
	<s:text name="challan.title.approve"/>
</s:elseif>
<s:elseif test="%{sourcePage=='inbox' && model.challan.state.value=='REJECTED'}">
	<s:text name="challan.title.cancel.modify"/>
</s:elseif>
<s:elseif test="%{model.challan.state.value=='APPROVED'}">
	<s:text name="challan.title.validate"/>
</s:elseif>
<s:else>
	<s:text name="challan.title.view"/>
</s:else>
</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<s:if test="%{model.id!=null}" >
	<tr>
	 	<td width="4%" class="bluebox2">&nbsp;</td>
	    	<td width="21%" class="bluebox2"><s:text name="challan.challanNumber"/></td>
	    	<td width="24%" class="bluebox2"><s:textfield name="challan.challanNumber" id="challan.challanNumber" value="%{challan.challanNumber}" readonly="true"/></td>
	     	<td width="21%" class="bluebox2"><s:text name="challan.challanstatus"/></td>
	    	<td width="24%" class="bluebox2"><s:textfield name="challan.status.description" id="challan.status.description" value="%{challan.status.description}" readonly="true"/></td>
	</tr>
</s:if>
<tr>
	      <td width="4%" class="bluebox">&nbsp;</td>
	     <td width="21%" class="bluebox"><s:text name="challan.date"/><span class="mandatory">*</span></td>
	      		  <s:date name="challan.challanDate" var="cdFormat" format="dd/MM/yyyy"/>
	      <td width="24%" class="bluebox">
	      		<s:textfield id="challanDate" name="challan.challanDate" value="%{cdFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
	      		<a  id="calendarLink" href="javascript:show_calendar('forms[0].challanDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  >
	      		<img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="middle" />
	      		</a><div class="highlight2" style="width:80px">DD/MM/YYYY</div>				
	      </td>
	        
   		<s:if test="%{shouldShowHeaderField('billNumber')}">
   			<td width="21%" class="bluebox"><s:text name="challan.billNumber"/></td>
			<td width="30%" class="bluebox"><s:textfield name="referencenumber" id="referencenumber" value="%{referencenumber}"  maxlength="50"/></td>
   		</s:if>
   		<s:else>
   			<td width="21%" class="bluebox">&nbsp;</td>
   			<td width="30%" class="bluebox">&nbsp;</td>
   		</s:else>
 		
	    </tr>
	    <tr> <td width="4%" class="bluebox2">&nbsp;</td>
	    <td width="21%" class="bluebox2"><s:text name="challan.payeename"/></td>
	    <td width="24%" class="bluebox2"><s:textfield name="receiptPayeeDetails.payeename" id="receiptPayeeDetails.payeeName" value="%{receiptPayeeDetails.payeename}" maxlength="100"/></td>
	     <td width="21%" class="bluebox2"><s:text name="challan.payeeAddress"/></td>
	    <td width="24%" class="bluebox2"><s:textarea name="receiptPayeeDetails.payeeAddress" id="receiptPayeeDetails.payeeAddress" value="%{receiptPayeeDetails.payeeAddress}" cols="18" rows="2" maxlength="1024" onkeyup="return ismaxlength(this)"/></td>

	    </tr>
	  <tr> 
	      	<td width="4%" class="bluebox">&nbsp;</td>
		    <td width="21%" class="bluebox"><s:text name="challan.narration"/></td>
		    <td width="24%" class="bluebox"><s:textarea name="referenceDesc" id="referenceDesc" value="%{referenceDesc}" cols="18" rows="2" maxlength="250" onkeyup="return ismaxlength(this)"/></td>
		    <td width="21%" class="bluebox"><s:text name="challan.voucherNumber"/></td>
		    <td width="24%" class="bluebox"><s:textarea name="voucherNumber" id="voucherNumber" value="%{voucherNumber}" cols="18" rows="1" maxlength="25" /></td>
	    </tr>
	    <tr>
		    <td width="4%" class="bluebox">&nbsp;</td>
		    <td width="21%" class="bluebox">&nbsp;</td>
		    <td width="24%" class="bluebox">&nbsp;</td>
		    <td width="21%" class="bluebox">&nbsp;</td>
		    <td width="24%" class="bluebox"><a href="#" onclick="openVoucherSearch();">Search For Voucher</a></td>
	    </tr> 
	    <s:if test="%{shouldShowHeaderField('fund') || shouldShowHeaderField('department')}">
	     <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>
	       <s:if test="%{shouldShowHeaderField('fund')}">
	      		<td width="21%" class="bluebox2"><s:text name="challan.fund"/><s:if test="%{isFieldMandatory('fund')}"><span class="bluebox2"><span class="mandatory">*</span></span></s:if></td>
		  		<td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="receiptMisc.fund" id="receiptMisc.fund.id" cssClass="selectwk"  list="dropdownData.fundList" listKey="id" listValue="name" value="%{receiptMisc.fund.id}" /> </td> 
		   </s:if>
		  <s:else>
  			<td class="bluebox2" colspan="2"></td>
  			</s:else>
  			  <s:if test="%{shouldShowHeaderField('department')}">
		   <td width="21%" class="bluebox2"><s:text name="challan.department"/><s:if test="%{isFieldMandatory('department')}"><span class="bluebox2"><span class="mandatory">*</span></span></s:if></td>
		  <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="deptId" id="deptId" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName"  /> </td>
	       </s:if>
		   <s:else>
  			<td class="bluebox2" colspan="2"></td>
  			</s:else>
	     </tr>
	     </s:if>
	      
		  <s:if test="%{shouldShowHeaderField('field')}">
		   <tr>
		    <td width="4%" class="bluebox">&nbsp;</td>
		    <td width="21%" class="bluebox"><s:text name="challan.field"/><s:if test="%{isFieldMandatory('field')}"><span class="mandatory">*</span></s:if>  </td>
		    <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="boundaryId" id="boundaryId" cssClass="selectwk" list="dropdownData.fieldList" listKey="id" listValue="name" /></td>
		    <td class="bluebox" colspan="2"></td>
  		 </tr>
	     </s:if>
	      <s:if test="%{shouldShowHeaderField('service')}">
	     <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>
	      <td width="21%" class="bluebox2"><s:text name="challan.service"/></td>
		  <td width="30%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('challan.select')}" name="challan.service" id="challan.service" cssClass="selectwk" list="dropdownData.serviceList" listKey="id" listValue="serviceName" value="%{challan.service.id}" onchange="getAccountDetails(this);getServiceMISDetails(this);" /></td>
		     <td width="21%" class="bluebox2" colspan="2"></td>
	    </tr>
	     </s:if>
	   
</table>
</td></tr>

 <tr><td>
    
	<div class="subheadsmallnew"><span class="subheadnew"><s:text name="challan.billdetails"/></span></div>
	
	<div class="yui-skin-sam" align="center">
       <div id="billDetailTable"></div>
       
     </div>
     <script>
		
		makeBillDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="100%";
	 </script>
	 <div id="codescontainer"></div>
	 <br/>
	 <div class="subheadsmallnew"><span class="subheadnew"><s:text name="billreceipt.billdetails.SubLedger"/></span></div>
	
	 	
		<div class="yui-skin-sam" align="center">
	       <div id="subLedgerTable"></div>
	     </div>
		<script>
			
			makeSubLedgerTable();
			
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="100%";
		</script>
 <div id="subledgercodescontainer"></div>
	 </td>
</tr>
</table>
<br>

<!-- Change to Manual WorkFlow on Create Challan-->
 <!-- When request is to check/approve challan -->
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td> 
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <!-- Remarks have to displayed on Challan Check/Approve. -->
		<s:if test="%{sourcePage=='inbox' && (model.challan.state.value=='CREATED' || model.challan.state.value=='CHECKED')}">
		<tr>
		   <td width="4%" class="bluebox">&nbsp;</td>
		   <td width="21%" class="bluebox"><s:text name="challan.approve.remarks"/></td>
		   <td width="75%" class="bluebox" colspan="3"><s:textfield id="approvalRemarks" type="text" name="approvalRemarks" cssStyle="width:75%" /></td>
		</tr>
		</s:if>
		<!-- Reason For Cancellation has to be displayed for Challan CANCEL	 -->
		<s:if test="%{(sourcePage=='inbox' && model.challan.state.value=='REJECTED') || (actionName=='CHALLAN_MODIFY' && hasErrors())}">
			<tr>
			   <td width="4%" class="bluebox">&nbsp;</td>
			   <td width="21%" class="bluebox"><s:text name="challan.reason.cancellation"/></td>
			   <td width="75%" class="bluebox" colspan="3"><s:textfield type="text" id="challan.reasonForCancellation" name="challan.reasonForCancellation" value="%{challan.reasonForCancellation}" cssStyle="width:75%" /></td>
			</tr>
		</s:if>
		<!--  Designation and Position has to be displayed for Challan Create/Check/Modify -->
		<!--  Designation and Position should not be displayed when invoked from search -->
		<s:if test="%{model.id==null || (sourcePage=='inbox' && model.challan.state.value=='CREATED' || model.challan.state.value=='REJECTED' || model.challan.state.value=='CHECKED')}">
		<tr> 
		<div class="subheadnew">
			<s:text name="approval.authority.information"/>
		</div>
		 </tr>
		<tr>
			<td width="4%" class="bluebox2">&nbsp;</td>
			<td width="15%" class="bluebox2"> Approver Department <s:if test="%{model.id==null}"><span class="mandatory">*</span></s:if></td>
			<td width="20%" class="bluebox2"><s:select headerKey="" headerValue="%{getText('challan.select')}" name="approverDeptId" id="approverDeptId" cssClass="selectwk" list="dropdownData.approverDepartmentList" listKey="id" listValue="deptName" 
onChange="onChangeDeparment(this.value)" /> 
		<egov:ajaxdropdown id="designationIdDropdown" fields="['Text','Value']" dropdownId='designationId'
			         url='receipts/ajaxChallanApproval!approverDesignationList.action' selectedValue="%{designationId}"/>
			</td>

			
		      	<td width="15%" class="bluebox2"><s:text name="challan.approve.designation"/><s:if test="%{model.id==null}"><span class="mandatory">*</span></s:if></td>
			  <td width="20%" class="bluebox2"><s:select headerKey="" headerValue="%{getText('challan.select')}" name="designationId" id="designationId" cssClass="selectwk"  list="dropdownData.designationMasterList" listKey="designationId" listValue="designationName" onChange="onChangeDesignation(this.value)"/>
			  <egov:ajaxdropdown id="positionUserDropdown" fields="['Text','Value']" dropdownId='positionUser'
			         url='receipts/ajaxChallanApproval!positionUserList.action' selectedValue="%{position.id}"/>	 
			 </td>
			 <td width="15%" class="bluebox2"><s:text name="challan.approve.userposition"/><s:if test="%{model.id==null}"><span class="mandatory">*</span></s:if></td>
				<td width="20%" class="bluebox2">
					<s:select headerValue="%{getText('challan.select')}"  headerKey="-1"
	                list="dropdownData.postionUserList" listKey="position.id" id="positionUser" listValue="position.name"
	                label="positionUser" name="positionUser" value="%{position.id}"/>
				</td>
		</tr>
		</s:if>
	</table>
	</td></tr>
	</table>
<div id="loadingMask" style="display:none;overflow:hidden;text-align: center"><img src="${pageContext.request.contextPath}/images/bar_loader.gif"/> <span style="color: red">Please wait....</span></div>

<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/> </div>
<!-- </div> -->
	<input type="hidden" name="actionName" id="actionName" value="{actionName}" />
	 <div class="buttonbottom" align="center" id="printButton">
		<!-- Action Buttons should be displayed only in case of Create New Challan or 
		     If page is opened from inbox -->
		<s:if test="%{model.id==null || sourcePage=='inbox' || (actionName=='CHALLAN_MODIFY' && hasErrors()) || (actionName=='CHALLAN_VALIDATE' && hasErrors())}" >
			<s:iterator value="%{validActions}">
				<s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="%{name}" name="actionButton" method="save" onclick="document.challan.actionName.value='%{name}';return validate(this);"/>
		    </s:iterator>	
	    </s:if>
	   
	     &nbsp;
	    <s:if test="%{model.id==null}" >
	    	<input name="button" type="button" class="button" id="button" value="Reset" onclick="checkreset();"/>
	    </s:if>
	    <s:if test="%{model.id!=null}" >
				
				<s:submit type="submit" cssClass="buttonsubmit" id="buttonprint" value="Print" method="printChallan" /> 			
	    </s:if>
	    &nbsp;<input name="button" type="button" class="button" id="buttonclose2" value="Close" onclick="window.close();" />
    </div>
   
   	<s:hidden id="receiptId" name="receiptId" value="%{model.id}"/>
   	<s:hidden id="sourcePage" name="sourcePage" value="%{sourcePage}"/>
	<s:hidden id="challanNextState" name="challanNextState" value="%{challan.state.nextAction}"/>
</s:push>
</s:form>
</div>
</body>

