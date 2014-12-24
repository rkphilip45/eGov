<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>

		path="/EGF";
		var totaldbamt=0,totalcramt=0;
		
		var makeVoucherDetailTable = function() {
		var voucherDetailColumns = [ 
			{key:"functionid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
			{key:"function",label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail")},
			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
			{key:"debitamount",label:'Debit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
			{key:"creditamount",label:'Credit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var voucherDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
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
					updateDebitAmountJV();updateCreditAmountJV();
					check();
					loadSlFunction();
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
					"debitamount":'<s:property value="%{debitAmountDetail}"/>',
					"creditamount":'<s:property value="%{creditAmountDetail}"/>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridPJV('functionIdDetail',index,'<s:property value="functionIdDetail"/>');
				updateGridPJV('functionDetail',index,'<s:property value="functionDetail"/>');
				updateGridPJV('glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
				updateGridPJV('glcodeDetail',index,'<s:property value="glcodeDetail"/>');
				updateGridPJV('accounthead',index,'<s:property value="accounthead"/>');
				updateGridPJV('debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
				updateGridPJV('creditAmountDetail',index,'<s:property value="creditAmountDetail"/>');
				totaldbamt = totaldbamt+parseFloat('<s:property value="debitAmountDetail"/>');
				totalcramt = totalcramt+parseFloat('<s:property value="creditAmountDetail"/>');
				updateAccountTableIndex();	
			</s:iterator>
				
	
		var tfoot = billDetailsTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 5;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totaldbamount' name='totaldbamount' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalcramount' name='totalcramount' readonly='true' tabindex='-1'/>";
		document.getElementById('totaldbamount').value=totaldbamt;
		document.getElementById('totalcramount').value=totalcramt;
	}
	
	var glcodeOptions=[{label:"---Select---", value:"0"}];
	<s:iterator value="dropdownData.glcodeList">
	    glcodeOptions.push({label:'<s:property value="glcode"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var detailtypeOptions=[{label:"---Select---", value:"0"}];
	<s:iterator value="dropdownData.detailTypeList">
	    detailtypeOptions.push({label:'<s:property value="name"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var funcOptions=[{label:"---Select---", value:"0"}];
	var makeSubLedgerTable = function() {
		var subledgerColumns = [ 
			{key:"functionDetail",label:'Function Name',width:90, formatter:createSLDropdownFormatterFuncJV(SUBLEDGERLIST,".functionDetail"),dropdownOptions:funcOptions},
			{key:"glcode",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:120, formatter:createSLDetailCodeTextFieldFormatterJV(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)", ".search", "openSearchWindowFromJV(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:createSLHiddenFieldFormatterJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name',width:120, formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount',width:90, formatter:createSLAmountFieldFormatterJV(SUBLEDGERLIST,".amount")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
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
				loadSlFunction();
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
					"functionDetail":'<s:property value="functionDetail"/>',
					"glcode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailTypeName":'<s:property value="detailTypeName"/>',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKey"/>',
					"detailKey":'<s:property value="detailKey"/>',
					"debitAmount":'<s:property value="%{debitAmount}"/>',
					"creditAmount":'<s:property value="%{creditAmount}"/>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridSLDropdownPJV('functionDetail',index,'<s:property value="functionDetail"/>');
				updateSLGridPJV('subledgerCode',index,'<s:property value="subledgerCode"/>');
				updateSLGridPJV('detailTypeName',index,'<s:property value="detailTypeName"/>');
				updateSLGridPJV('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGridPJV('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGridPJV('detailKey',index,'<s:property value="detailKey"/>');
				updateSLGridPJV('amount',index,'<s:property value="amount"/>');
				updateSLTableIndex();
			</s:iterator>
		
	}
	



// logic to re-populate the account code in the account code drop down in the subledger table data grid.
// basicaly required when validation fails.
function populateslDropDown(){
	var accountCodes=new Array();
	for(var i=0;i<billDetailTableIndex+1;i++){
	if(null != document.getElementById('billDetailslist['+i+'].glcodeDetail')){
		accountCodes[i] = document.getElementById('billDetailslist['+i+'].glcodeDetail').value;
	}
	}
	var url = path+'/voucher/common!getDetailCode.action?accountCodes='+accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackJV, null);
}
var callbackJV = {
success: function(o) {
		var test= o.responseText;
		test = test.split('~');
		slAccountCodes.length=0;
		for (var j=0; j<slDetailTableIndex;j++ )
		{
			var d=document.getElementById('subLedgerlist['+j+'].glcode.id');
			if(null != d && test.length >1 && d.value ==0 )
			{
				
				d.options.length=((test.length)/2)+1;
				for (var i=1; i<((test.length)/2)+1;i++ )
				{
					d.options[i].text=test[i*2-2];
					d.options[i].value=test[i*2 -1];
					slAccountCodes.push(test[i*2 -1]);
				}
			} 
			if(test.length<2)
			{
				var d = document.getElementById('subLedgerlist['+j+'].glcode.id');
				if(d)
				{
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
				}
			}
		}
		
			// logic to re-populate the account code in the account code drop down in the subledger table data grid.
			// basicaly required when validation fails.
			<s:iterator value="subLedgerlist" status="stat">
				if('<s:property value="glcode.id"/>' !="" || '<s:property value="glcode.id"/>' !=0){
					var index = '<s:property value="#stat.index"/>';
					updateGridSLDropdownGL('glcode.id',index,'<s:property value="glcode.id"/>');
				}
				
			</s:iterator>
	loadSlFunction();// load the functions in the SL grid after validation fails.
    },
    failure: function(o) {
    	alert('failure');
    }
}
function updateGridSLDropdownGL(field,index,value){

	document.getElementById('subLedgerlist['+index+'].'+field).value=value;
	loadDetailType(index);
}

var loadDetailType = function(index) { 
		var subledgerid=document.getElementById('subLedgerlist['+index+'].glcode.id');
		var accountCode = subledgerid.options[subledgerid.selectedIndex].text;
		document.getElementById('subLedgerlist['+index+'].subledgerCode').value =accountCode;
		var url = path+'/voucher/common!getDetailType.action?accountCode='+accountCode+'&index='+index;
		var transaction = YAHOO.util.Connect.asyncRequest('POST', url, postType, null);
};
var postType = {
success: function(o) {
		var detailType= o.responseText;
		var detailRecord = detailType.split('#');
		var eachItem;
		var obj;
		for(var i=0;i<detailRecord.length;i++)
		{
			eachItem =detailRecord[i].split('~');
			if(obj==null)
			{
				obj = document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailType.id');
				if(obj!=null)
					obj.options.length=detailRecord.length+1;
			}
			if(obj!=null)
			{
				obj.options[i+1].text=eachItem[1];
				obj.options[i+1].value=eachItem[2];
				document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailTypeName').value = eachItem[1];
			}
			
			if(eachItem.length==1) // for deselect the subledger code
			{
				var d = document.getElementById('subLedgerlist['+i+'].detailType.id');
				d.options.length=1;
				d.options[0].text='---Select---';
				d.options[0].value=0;
			}
		} 
			<s:iterator value="subLedgerlist" status="stat">
			if('<s:property value="detailType.id"/>' !="" || '<s:property value="detailType.id"/>' !=0){
				var index = '<s:property value="#stat.index"/>';
				updateSLDetailDropdown('detailType.id',index,'<s:property value="detailType.id"/>');
				updateGridSLDropdownPJV('functionDetail',index,'<s:property value="functionDetail"/>');
			}
				
			</s:iterator>
		
		
    },
    failure: function(o) {
    	alert('failure');
    }
}

function updateSLDetailDropdown(field,index,value){
	document.getElementById('subLedgerlist['+index+'].'+field).value=value;
}

function createTextFieldFormatterJV(prefix,suffix,type){
	
    return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		el.innerHTML = " <input type='"+type+"' id='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' name='"+prefix+"["+billDetailTableIndex+"]"+suffix+"' style='width:90px;' onkeyup='autocompletecode(this,event)' autocomplete='off' onblur='fillNeibrAfterSplitGlcode(this);'/>";
		
	}
}


	</script>
<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:600px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	.yui-skin-sam tr.yui-dt-odd{background-color:#FFF;}
</style>