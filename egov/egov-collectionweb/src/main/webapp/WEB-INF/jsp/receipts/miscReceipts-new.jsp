



<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/fonts/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
        <script type="text/javascript" src="/egi/commonyui/yui2.7/yuiloader/yuiloader-min.js"></script>
        <script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>

        <script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
        <script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
        <script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>
        <script type="text/javascript" src="/egi/commonyui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/MiscReceipts.js"></script>        
<script type="text/javascript" src="${pageContext.request.contextPath}/js/MiscReceiptsService.js"></script>   
<style type="text/css">
    #codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
    #codescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
    #codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
    #codescontainer ul {padding:5px 0;width:100%;}
    #codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
    #codescontainer li.yui-ac-highlight {background:#ff0;}
    #codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    #rebatecodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
    #rebatecodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
    #rebatecodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
    #rebatecodescontainer ul {padding:5px 0;width:100%;}
    #rebatecodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
    #rebatecodescontainer li.yui-ac-highlight {background:#ff0;}
    #rebatecodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    #bankcodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
    #bankcodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
    #bankcodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
    #bankcodescontainer ul {padding:5px 0;width:100%;}
    #bankcodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
    #bankcodescontainer li.yui-ac-highlight {background:#ff0;}
    #bankcodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    #subledgercodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
    #subledgercodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
    #subledgercodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
    #subledgercodescontainer ul {padding:5px 0;width:100%;}
    #subledgercodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
    #subledgercodescontainer li.yui-ac-highlight {background:#ff0;}
    #subledgercodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    
</style>
 
<script type="text/javascript">
var billscount=0;
var multiplepayee="false";
path="${pageContext.request.contextPath}";

    var currDate = "${currDate}";   

function addRow(tableObj,rowObj)
{
    var tbody=tableObj.tBodies[0];
    tbody.appendChild(rowObj);
}

function validateMiscReceipt()
{
    if(!validateMiscDetails()){
        return false;
    }
    return true;
}

function resetMisc(){
    document.getElementById("voucherDate").value=currDate;
    if(document.getElementById("schemeId")!=null){
        document.getElementById("schemeId").options.length = 1;
    }
    if(document.getElementById("subschemeId")!=null){
        document.getElementById("subschemeId").options.length = 1;
    }
    if(document.getElementById("receiptMisc.fundsource.id")!=null){
        document.getElementById("receiptMisc.fundsource.id").selectedIndex = 0;
    }
    if(document.getElementById("receiptMisc.idFunctionary.id")!=null){
        document.getElementById("receiptMisc.idFunctionary.id").selectedIndex = 0;
    }
    if(dom.get("voucherNum")!=null){
        document.getElementById("voucherNum").value="";
    }
    if(document.getElementById("bankBranchMaster")!=null){
        document.getElementById("bankBranchMaster").options.length = 1;
    }
    if(document.getElementById("accountNumberMaster")!=null){
        document.getElementById("accountNumberMaster").options.length = 1;
    }
    resetTables();
    document.getElementById("billCreditDetailslist[0].creditAmountDetail").value=0;
    document.getElementById("billRebateDetailslist[0].debitAmountDetail").value=0;
    document.getElementById("subLedgerlist[0].amount").value=0;
    document.getElementById("totalcramount").value=0;
    document.getElementById("totaldbamount").value=0;
    var paidby = '<s:property value="%{paidBy}"/>';
    paidby = paidby.replace("\'","'");
    document.getElementById('paidBy').value=paidby;
    if(dom.get("actionErrorMessages")!=null){
        dom.get("actionErrorMessages").style.display="none";}
    if(dom.get("actionMessages")!=null){
        dom.get("actionMessages").style.display="none";}
    
}

function onBodyLoadMiscReceipt()
{
    document.getElementById("voucherDate").value=currDate;
    if(document.getElementById("deptId")!=null){
        document.getElementById("deptId").disabled=true;
    }
    loadDropDownCodes();
    loadDropDownRebateCodes();
    loadDropDownCodesFunction();
    checkData();
    updatetotalAmount();
    document.getElementById('paidBy').readOnly=true;
    check();
    var paidby = '<s:property value="%{paidBy}"/>';
    paidby = paidby.replace("\'","'");
    document.getElementById('paidBy').value=paidby;
}
function checkfund()
{
    var tempfund=document.getElementById("fundId").value;

    if(tempfund=="-1"){
        document.getElementById("receipt_error_area").innerHTML="";
        document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingfund.errormessage" />'+ '<br>';
        dom.get("receipt_error_area").style.display="block";
        return false;
    }
}
function checkscheme()
{
    var tempscheme=document.getElementById("schemeId").value;
    if(tempscheme=="-1"){
        document.getElementById("receipt_error_area").innerHTML="";
        document.getElementById("receipt_error_area").innerHTML+='<s:text name="billreceipt.missingscheme.errormessage" />'+ '<br>';
        dom.get("receipt_error_area").style.display="block";
        return false;
    }
}
function checkData(){
    if(document.getElementById("receiptMisc.fund.id")!=null){
        if(document.getElementById("receiptMisc.fund.id").value!="" && document.getElementById("receiptMisc.fund.id").value!="-1"){
            document.getElementById('fundId').value=document.getElementById('receiptMisc.fund.id').value;
        }
    }

    if(document.getElementById("receiptMisc.scheme.id")!=null){
        if(document.getElementById("receiptMisc.scheme.id").value!="" && document.getElementById("receiptMisc.scheme.id").value!="-1"){
            document.getElementById('schemeId').value=document.getElementById('receiptMisc.scheme.id').value;
        }
        else{
            document.getElementById("receiptMisc.scheme.id").value="-1";    
        }
        if(document.getElementById("receiptMisc.subscheme.id").value!="" && document.getElementById("receiptMisc.subscheme.id").value!="-1"){
            document.getElementById('subschemeId').value=document.getElementById('receiptMisc.subscheme.id').value;
        }
        else{
            document.getElementById("receiptMisc.subscheme.id").value="-1"; 
        } 
        if(document.getElementById("receiptMisc.fundsource.id").value!="" && document.getElementById("receiptMisc.fundsource.id").value!="-1"){
            document.getElementById('fundSourceId').value=document.getElementById('receiptMisc.fundsource.id').value;
        }
        else{
            document.getElementById("receiptMisc.fundsource.id").value="-1";    
        }
    }
}

function getSchemelist(fund)
{
    if(document.getElementById("receiptMisc.scheme.id")!=null){
        populateschemeId({fundId:fund.options[fund.selectedIndex].value})
    }
    
    // if a subscheme had been chosen, set fund source to default value 'Select'
    if(document.getElementById("subschemeId").value!=-1){
        document.getElementById("fundSourceId").value=-1;
    }
    
    document.getElementById("subschemeId").value=-1;
    document.getElementById("subschemeId").options.length = 1;
    populatefundSourceId({subSchemeId:-1})
}

function getBankBranchList(fund){
        var serviceName=dom.get("serviceName").value;
        if(fund.options[fund.selectedIndex].value!="-1"){
            populatebankBranchMaster({serviceName:serviceName,fundId:fund.options[fund.selectedIndex].value});
        }
        else{
            if(document.getElementById("bankBranchMaster")!=null){
                document.getElementById("bankBranchMaster").options.length = 1;
            }
            if(document.getElementById("accountNumberMaster")!=null){
                document.getElementById("accountNumberMaster").options.length = 1;
            }
        }
}

function getSubSchemelist(scheme)
{
    populatesubschemeId({schemeId:scheme.options[scheme.selectedIndex].value})  
}

function getFundSourcelist(subScheme)
{
    populatefundSourceId({subSchemeId:subScheme.options[subScheme.selectedIndex].value})
}
    
function setFundId(){
    document.getElementById('receiptMisc.fund.id').value=document.getElementById('fundId').value
}
function setSchemeId(){
    document.getElementById('receiptMisc.scheme.id').value=document.getElementById('schemeId').value
}
function setSubSchemeId(){
    document.getElementById('receiptMisc.subscheme.id').value=document.getElementById('subschemeId').value
}
function setFundSourceId(){
    document.getElementById('receiptMisc.fundsource.id').value=document.getElementById('fundSourceId').value
}

function validateMiscDetails()
{
if(dom.get("actionErrorMessages")!=null){
    dom.get("actionErrorMessages").style.display="none";}
if(dom.get("actionMessages")!=null){
    dom.get("actionMessages").style.display="none";}
var valid=true;
     // Javascript validation of the MIS Manadate attributes.
     <s:if test="%{isFieldMandatory('voucherdate')}"> 
                 if(null != document.getElementById('voucherDate') && document.getElementById('voucherDate').value.trim().length == 0){

                    document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.voucherdate.errormessage" />'+ "<br>";
                    valid=false;
                }
                  var currDate = "${currDate}";
                 var vhDate=document.getElementById('voucherDate').value;
                    if(vhDate.trim().length != 0){
                        if(!checkFdateTdate(vhDate,currDate))
                        {
                            document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.voucherdate.incorrectmessage" />'+ "<br>";
                            valid=false;
                        }
                    }
             </s:if>
            <s:if test="%{isFieldMandatory('vouchernumber')}"> 
                 if(null != document.getElementById('voucherNum') && document.getElementById('voucherNum').value.trim().length == 0 ){
                    document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.vouchernumber.errormessage" />'+  "<br>";
                    valid=false;
                 }
                 else if(!validateAlphaNumeric(document.getElementById('voucherNum').value)){
                    document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.vouchernumber.incorrectformatmessage" />'+  "<br>";
                    valid=false;
                 }
                 
             </s:if>
             
         <s:if test="%{isFieldMandatory('fund')}"> 
             if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){
            
                    document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.fundcode.errormessage" />'+  "<br>";
                    valid=false;
             }
                </s:if>
         <s:if test="%{isFieldMandatory('field')}">
           	if(null != document.getElementById('boundaryId')) && document.getElementById('boundaryId').value==-1){
           		document.getElementByid("receipt_error_area").innerHTML+='<s:text name="miscreceipt.field.errormessage"/>'+ "<br>";
           		valid=false;
           	}
          </s:if>
         	
          <s:if test="%{isFieldMandatory('department')}"> 
                 if(null!= document.getElementById('deptId') && document.getElementById('deptId').value == -1){

                        document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.deptcode.errormessage" />'+ '<br>';
                        valid=false;
                 }
                  
            </s:if>
            <s:if test="%{isFieldMandatory('scheme')}"> 
                 if(null!=document.getElementById('schemeId') &&  document.getElementById('schemeId').value == -1){

                        document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.schemeId.errormessage" />'+ '<br>';
                        valid=false;
                 }
                
            </s:if>
            <s:if test="%{isFieldMandatory('subscheme')}"> 
                 if(null!= document.getElementById('subschemeId') && document.getElementById('subschemeId').value == -1){

                        document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.subschemeId.errormessage" />'+ '<br>';
                        valid=false;
                 }
                
            </s:if>
            <s:if test="%{isFieldMandatory('functionary')}"> 
                 if(null!=document.getElementById('receiptMisc.idFunctionary.id') &&  document.getElementById('receiptMisc.idFunctionary.id').value == -1){

                        document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.functionarycode.errormessage" />'+ '<br>';
                        valid=false;
                 }
                 
            </s:if>
            <s:if test="%{isFieldMandatory('fundsource')}"> 
                 if(null !=document.getElementById('receiptMisc.fundsource.id') &&  document.getElementById('receiptMisc.fundsource.id').value == -1){

                        document.getElementById("receipt_error_area").innerHTML+='<s:text name="miscreceipt.fundsourcecode.errormessage" />'+ '<br>';
                        valid=false;
                
                }
                
            </s:if>
            
            
        
    if(!validateAccountDetail()){
        valid=false;}
     if(!validateSubLedgerDetailforCredit()){
        valid=false;}
    if(!validateSubLedgerDetailforRebate()){
        valid=false;}
     
    
    return valid;
}
var totaldbamt=0,totalcramt=0;
                
    var makeCreditDetailTable = function() {
        var creditDetailColumns = [
            {key:"functionid",hidden:true,width:10, formatter:createTextFieldFormatterCredit(VOUCHERCREDITDETAILLIST,".functionIdDetail","hidden",VOUCHERCREDITDETAILTABLE)},
            {key:"function",label:'Function',width:90, formatter:createTextFieldFormatterForFunctionCredit(VOUCHERCREDITDETAILLIST,".functionDetail",VOUCHERCREDITDETAILTABLE)},
            {key:"glcodeid",hidden:true,width:10, formatter:createTextFieldFormatterCredit(VOUCHERCREDITDETAILLIST,".glcodeIdDetail","hidden",VOUCHERCREDITDETAILTABLE)},
            {key:"accounthead", label:'Account Head <span class="mandatory">*</span>',formatter:createLongTextFieldFormatterCredit(VOUCHERCREDITDETAILLIST,".accounthead",VOUCHERCREDITDETAILTABLE)},               
            {key:"glcode",label:'Account Code ', formatter:createTextFieldFormatterCredit(VOUCHERCREDITDETAILLIST,".glcodeDetail","text",VOUCHERCREDITDETAILTABLE)},
            {key:"creditamount",label:'Credit Amount (Rs.)', formatter:createAmountFieldFormatterRebate(VOUCHERCREDITDETAILLIST,".creditAmountDetail","updateCreditAmount()",VOUCHERCREDITDETAILTABLE)},
            {key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
            {key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
        ];
        
        var creditDetailDS = new YAHOO.util.DataSource(); 
        billCreditDetailsTable = new YAHOO.widget.DataTable("creditDetailTable",creditDetailColumns, creditDetailDS);
        billCreditDetailsTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Add') { 
                billCreditDetailsTable.addRow({SlNo:billCreditDetailsTable.getRecordSet().getLength()+1});
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
                    updatetotalAmount();
                    check();
                }
                else{
                    alert("This row can not be deleted");
                }
            }
            
             
        });
        <s:iterator value="billCreditDetailslist" status="stat">
                billCreditDetailsTable.addRow({SlNo:billCreditDetailsTable.getRecordSet().getLength()+1,
                    "functionid":'<s:property value="functionIdDetail"/>',
                    "function":'<s:property value="functionDetail"/>',
                    "glcodeid":'<s:property value="glcodeIdDetail"/>',
                    "glcode":'<s:property value="glcodeDetail"/>',
                    "accounthead":'<s:property value="accounthead"/>',
                    "creditamount":'<s:property value="%{creditAmountDetail}"/>'
                });
                var index = '<s:property value="#stat.index"/>';
                updateGridMisc(VOUCHERCREDITDETAILLIST,'functionIdDetail',index,'<s:property value="functionIdDetail"/>');
                updateGridMisc(VOUCHERCREDITDETAILLIST,'functionDetail',index,'<s:property value="functionDetail"/>');
                updateGridMisc(VOUCHERCREDITDETAILLIST,'glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
                updateGridMisc(VOUCHERCREDITDETAILLIST,'glcodeDetail',index,'<s:property value="glcodeDetail"/>');
                updateGridMisc(VOUCHERCREDITDETAILLIST,'accounthead',index,'<s:property value="accounthead"/>');
                updateGridMisc(VOUCHERCREDITDETAILLIST,'creditAmountDetail',index,'<s:property value="creditAmountDetail"/>');
                totalcramt = totalcramt+parseFloat('<s:property value="creditAmountDetail"/>');
                updateAccountTableIndex();  
            </s:iterator>
                
    
        var tfoot = billCreditDetailsTable.getTbodyEl().parentNode.createTFoot();
        var tr = tfoot.insertRow(-1);
        var td1 = tr.insertCell(-1);
        td1.align="center";
        td1.colSpan = 5;
        td1.setAttribute('class','tdfortotal');
        td1.style.textAlign="right";
        td1.style.borderTop = '1px #c8c8c8 solid';
        td1.innerHTML='<b>Total</b>&nbsp;&nbsp;&nbsp;';
        td1.className='tdfortotal';
        var td = tr.insertCell(-1);
        td.width="100";
        td.height="32";
        td.align="center";
        td.style.borderTop = '1px #c8c8c8 solid';
        td.setAttribute('class','tdfortotal');
        td.className='tdfortotal';
        td.style.padding='4px 10px';
        td.innerHTML="<input type='text' style='text-align:right;width:80px;align:center;height:20px;'  id='totalcramount' name='totalcramount' readonly='true' tabindex='-1'/>";
        if(totalcramt>0){
            totalcramt=totalcramt.toFixed(2);
        }
        document.getElementById('totalcramount').value=totalcramt;
        var td2 = tr.insertCell(-1);
        td2.colSpan = 2;
        td2.style.borderTop = '1px #c8c8c8 solid';
        td2.setAttribute('class','tdfortotal');
        td2.className='tdfortotal';
        td2.innerHTML='&nbsp;';
        
    }
    
    var makeRebateDetailTable = function() {
        var rebateDetailColumns = [ 
            {key:"functionid",hidden:true,width:10, formatter:createTextFieldFormatterRebate(VOUCHERREBATEDETAILLIST,".functionIdDetail","hidden",VOUCHERREBATEDETAILTABLE)},
            {key:"function",label:'Function ',width:90, formatter:createTextFieldFormatterForFunctionRebate(VOUCHERREBATEDETAILLIST,".functionDetail",VOUCHERREBATEDETAILTABLE)},
            {key:"glcodeid",hidden:true,width:10, formatter:createTextFieldFormatterRebate(VOUCHERREBATEDETAILLIST,".glcodeIdDetail","hidden",VOUCHERREBATEDETAILTABLE)},
            {key:"accounthead", label:'Account Head <span class="mandatory">*</span>',formatter:createLongTextFieldFormatterRebate(VOUCHERREBATEDETAILLIST,".accounthead",VOUCHERREBATEDETAILTABLE)},               
            {key:"glcode",label:'Account Code ', formatter:createTextFieldFormatterRebate(VOUCHERREBATEDETAILLIST,".glcodeDetail","text",VOUCHERREBATEDETAILTABLE)},
            {key:"debitamount",label:'Debit Amount (Rs.)', formatter:createAmountFieldFormatterRebate(VOUCHERREBATEDETAILLIST,".debitAmountDetail","updateDebitAmount()",VOUCHERREBATEDETAILTABLE)}, 
            {key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
            {key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
        ];
            
                var rebateDetailDS = new YAHOO.util.DataSource(); 
        rebateDetailsTable = new YAHOO.widget.DataTable("rebateDetailTable",rebateDetailColumns, rebateDetailDS);
        
        rebateDetailsTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'Add') { 
                rebateDetailsTable.addRow({SlNo:rebateDetailsTable.getRecordSet().getLength()+1});
                updateRebateDetailTableIndex();
            }
            if (column.key == 'Delete') {   
                if(this.getRecordSet().getLength()>1){          
                    this.deleteRow(record);
                    allRecords=this.getRecordSet();
                    for(var i=0;i<allRecords.getLength();i++){
                        this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                    }
                    updateDebitAmount();updatetotalAmount();
                    check();
                }
                else{
                    alert("This row can not be deleted");
                }
            }
            
             
        });
            <s:iterator value="billRebateDetailslist" status="stat">
            rebateDetailsTable.addRow({SlNo:rebateDetailsTable.getRecordSet().getLength()+1,
                    "functionid":'<s:property value="functionIdDetail"/>',
                    "function":'<s:property value="functionDetail"/>',
                    "glcodeid":'<s:property value="glcodeIdDetail"/>',
                    "glcode":'<s:property value="glcodeDetail"/>',
                    "accounthead":'<s:property value="accounthead"/>',
                    "debitamount":'<s:property value="%{debitAmountDetail}"/>'
                });
        var index = '<s:property value="#stat.index"/>';
                updateGridMisc(VOUCHERREBATEDETAILLIST,'functionIdDetail',index,'<s:property value="functionIdDetail"/>');
                updateGridMisc(VOUCHERREBATEDETAILLIST,'functionDetail',index,'<s:property value="functionDetail"/>');
                updateGridMisc(VOUCHERREBATEDETAILLIST,'glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
                updateGridMisc(VOUCHERREBATEDETAILLIST,'glcodeDetail',index,'<s:property value="glcodeDetail"/>');
                updateGridMisc(VOUCHERREBATEDETAILLIST,'accounthead',index,'<s:property value="accounthead"/>');
                updateGridMisc(VOUCHERREBATEDETAILLIST,'debitAmountDetail',index,'<s:property value="debitAmountDetail"/>');
                totaldbamt = totaldbamt+parseFloat('<s:property value="debitAmountDetail"/>');
                updateRebateDetailTableIndex();
        </s:iterator>
                
    
        var tfoot = rebateDetailsTable.getTbodyEl().parentNode.createTFoot();
        var tr = tfoot.insertRow(-1);
        var td1 = tr.insertCell(-1);
        td1.align="center";
        td1.colSpan = 5;
        td1.setAttribute('class','tdfortotal');
        td1.className='tdfortotal';
        td1.style.textAlign="right";
        td1.style.borderTop = '1px #c8c8c8 solid';
        td1.innerHTML='<b>Total</b>&nbsp;&nbsp;&nbsp;';
        var td = tr.insertCell(-1);
        td.width="100";
        td.height="30";
        td.align="center";
        td.style.borderTop = '1px #c8c8c8 solid';
        td.setAttribute('class','tdfortotal');
        td.className='tdfortotal';
        td.style.padding='4px 10px';
        td.innerHTML="<input type='text' style='text-align:right;width:80px;height:20px;'  id='totaldbamount' name='totaldbamount' readonly='true' tabindex='-1'/>";
        var td2 = tr.insertCell(-1);
        td2.colSpan = 2;
        td2.setAttribute('class','tdfortotal');
        td2.style.borderTop = '1px #c8c8c8 solid';  
        td2.className='tdfortotal';
        if(totaldbamt>0){
            totaldbamt=totaldbamt.toFixed(2);
        }
        document.getElementById('totaldbamount').value=totaldbamt;
        td2.innerHTML='&nbsp;';
    }
    
    
    
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
            {key:"glcode",hidden:true, formatter:createSLTextFieldFormatter(SUBLEDGERLIST,".subledgerCode","hidden")},
            {key:"glcode.id",label:'Account Code <span class="mandatory">*</span>', formatter:createDropdownFormatterCode(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
            {key:"detailTypeName",hidden:true, formatter:createSLTextFieldFormatter(SUBLEDGERLIST,".detailTypeName","hidden")},
            {key:"detailType.id",label:'Type <span class="mandatory">*</span>', formatter:createDropdownFormatterDetail(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
            {key:"detailCode",label:'Code <span class="mandatory">*</span>',formatter:createSLDetailCodeTextFieldFormatter(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)")},
            {key:"detailKeyId",hidden:true, formatter:createSLHiddenFieldFormatter(SUBLEDGERLIST,".detailKeyId")},
            {key:"detailKey",label:'Name', formatter:createSLLongTextFieldFormatter(SUBLEDGERLIST,".detailKey","")},
            {key:"amount",label:'Amount (Rs.)', formatter:createSLAmountFieldFormatter(SUBLEDGERLIST,".amount")},
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
                    "debitAmount":'<s:property value="%{debitAmount}"/>',
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
    
    
   

</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0" >

  <tr><td>
  
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
 
 
     <tr>
          <td width="4%" class="bluebox2">&nbsp;</td>
         <td width="21%" class="bluebox2"><s:text name="miscreceipt.voucher.date"/><span class="mandatory">*</span></td>
                  <s:date name="voucherDate" var="cdFormat" format="dd/MM/yyyy"/>
          <td width="24%" class="bluebox2">
                <s:textfield id="voucherDate" name="voucherDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
                <a href="javascript:show_calendar('forms[0].voucherDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  >
                <img src="${pageContext.request.contextPath}/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="middle" />
                </a><div class="highlight2" style="width:80px">DD/MM/YYYY</div>             
          </td>
               
           <s:if test="%{shouldShowHeaderField('field')}">
           <td width="21%" class="bluebox2"><s:text name="miscreceipt.field"/><s:if test="%{isFieldMandatory('field')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
          <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="boundaryId" id="boundaryId" cssClass="selectwk" list="dropdownData.fieldList" listKey="id" listValue="name"  /> </td>
            </s:if>
           <s:else>
            <td colspan=2 class="bluebox2"></td>
            </s:else>
         
           <s:if test="%{shouldShowHeaderField('vouchernumber')}">
           <td width="21%" class="bluebox2"><s:text name="miscreceipt.voucher.number"/><span class="mandatory">*</span></td>
        <td width="30%" class="bluebox2"><s:textfield name="voucherNum" id="voucherNum" maxlength="16"/></td>
        </s:if>
        <s:else>
        <td colspan=2 class="bluebox2"></td>
        </s:else>
        
        <tr>
        <td width="4%" class="bluebox">&nbsp;</td>
         
        <td width="21%" class="bluebox"><s:text name="miscreceipt.service.category" /> </td>
        <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="----Choose----" name="serviceCategory.id" id="serviceCategoryid" cssClass="selectwk" list="dropdownData.serviceCategoryList" listKey="id" listValue="name" value="%{serviceCategory.id}" onChange="populateService(this);" /> 
       	<egov:ajaxdropdown id="service"fields="['Text','Value']" dropdownId="serviceId" url="/receipts/ajaxReceiptCreate!ajaxLoadServiceByCategory.action" /></td>
        <td width="21%" class="bluebox"><s:text name="miscreceipt.service" /> </td>
        <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="----Choose----" name="service.id" id="serviceId" cssClass="selectwk"
	list="dropdownData.serviceList" listKey="id" listValue="code" value="%{service.id}" onchange="loadFinDetails(this);"/>
        </td>
         
       
        </tr>
        
        
        
        <s:if test="%{shouldShowHeaderField('fund') || shouldShowHeaderField('department')}">
         <tr>
          <td width="4%" class="bluebox2">&nbsp;</td>
           <s:if test="%{shouldShowHeaderField('fund')}">
          <td width="21%" class="bluebox2"><s:text name="miscreceipt.fund"/><s:if test="%{isFieldMandatory('fund')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
          <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="fundId" id="fundId" cssClass="selectwk" onChange="setFundId();getSchemelist(this);getBankBranchList(this);" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" />
          <egov:ajaxdropdown id="bankBranchMasterDropdown" fields="['Text','Value']" dropdownId='bankBranchMaster'
                url='receipts/ajaxBankRemittance!bankBranchList.action' selectedValue="%{bankbranch.id}"/> 
          <!-- <egov:ajaxdropdown id="schemeIdDropdown" fields="['Text','Value']" dropdownId='schemeId' url='receipts/ajaxReceiptCreate!ajaxLoadSchemes.action' /> -->
          <egov:ajaxdropdown id="schemeIdDropdown" fields="['Text','Value']" dropdownId='schemeId' url='../EGF/voucher/common!ajaxLoadSchemes.action' />
         <s:hidden label="receiptMisc.fund.id" id="receiptMisc.fund.id"  name="receiptMisc.fund.id"/>
          </td>
          </s:if>
           <s:else>
            <td colspan=2 class="bluebox2"></td>
            </s:else>
              <s:if test="%{shouldShowHeaderField('department')}">
           <td width="21%" class="bluebox2"><s:text name="miscreceipt.department"/><s:if test="%{isFieldMandatory('department')}"><span class="bluebox"><span class="mandatory">*</span></span></s:if></td>
          <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="deptId" id="deptId" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="deptName"  /> </td>
            </s:if>
           <s:else>
            <td colspan=2 class="bluebox2"></td>
            </s:else>
         </tr>
         </s:if>
          <s:if test="%{shouldShowHeaderField('fundsource') || shouldShowHeaderField('functionary')}">
        <tr>
          <td width="4%" class="bluebox">&nbsp;</td>
          <s:if test="%{shouldShowHeaderField('fundsource')}">
           <td width="21%" class="bluebox"><s:text name="miscreceipt.fundingsource"/> <s:if test="%{isFieldMandatory('fundsource')}"><span class="bluebox2"><span class="mandatory">*</span></span></s:if></td>
         <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="fundSourceId" id="fundSourceId" onclick="checkfund()" onchange="setFundSourceId();" cssClass="selectwk" list="dropdownData.fundsourceList" listKey="id" listValue="name"  /></td>
         <s:hidden label="receiptMisc.fundsource.id" id="receiptMisc.fundsource.id"  name="receiptMisc.fundsource.id"/>
         </s:if>
         <s:else>
        <td colspan=2 class="bluebox"></td>
        </s:else>
        <s:if test="%{shouldShowHeaderField('functionary')}">
          <td width="21%" class="bluebox"><s:text name="miscreceipt.functionary"/>  <s:if test="%{isFieldMandatory('functionary')}"><span class="bluebox2"><span class="mandatory">*</span></span></s:if> </td>
         <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="receiptMisc.idFunctionary.id" id="receiptMisc.idFunctionary.id" cssClass="selectwk" list="dropdownData.functionaryList" listKey="id" listValue="name"  /></td>
         
         </s:if>
         <s:else>
        <td colspan=2 class="bluebox"></td>
        </s:else>
        </tr>
        </s:if>
          <s:if test="%{shouldShowHeaderField('scheme')}">
          <tr>
          <td width="4%" class="bluebox2">&nbsp;</td>
          <td width="21%" class="bluebox2"><s:text name="miscreceipt.scheme"/> <s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory">*</span></s:if>  </td>
          <td width="24%" class="bluebox2">
          <s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="schemeId" id="schemeId" onclick="checkfund()" onchange="setSchemeId();getSubSchemelist(this)" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name"  value="%{scheme.id}" /> 
          <egov:ajaxdropdown id="subschemeId" fields="['Text','Value']" dropdownId='subschemeId' url='../EGF/voucher/common!ajaxLoadSubSchemes.action' />
          <s:hidden label="receiptMisc.scheme.id" id="receiptMisc.scheme.id"  name="receiptMisc.scheme.id"/>
          </td>
          
          <td width="21%" class="bluebox2"><s:text name="miscreceipt.subscheme"/> <s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory">*</span></s:if>  </td>

         <td width="30%" class="bluebox2">
          <s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="subschemeId" id="subschemeId" onchange="setSubSchemeId();getFundSourcelist(this)" onclick="checkscheme()" cssClass="selectwk" list="dropdownData.subschemeList" listKey="id" listValue="name"  /></td>
          <egov:ajaxdropdown id="fundSourceId" fields="['Text','Value']" dropdownId='fundSourceId'
           url='../EGF/voucher/common!ajaxLoadFundSource.action'  />
           <s:hidden label="receiptMisc.subscheme.id" id="receiptMisc.subscheme.id"  name="receiptMisc.subscheme.id"/>
         
        </tr>
        </s:if>
    </table>
  </td></tr>
  <tr><td>
      <s:hidden label="misctotalAmount" id="misctotalAmount"  name="misctotalAmount" value="0"/>
    <div class="subheadsmallnew"><span class="subheadnew"><s:text name="billreceipt.billdetails.Credit"/></span></div>
    
    <div class="yui-skin-sam" align="center">
       <div id="creditDetailTable"></div>
       
     </div>
     <script>
        
        makeCreditDetailTable();
        document.getElementById('creditDetailTable').getElementsByTagName('table')[0].width="100%";
     </script>
     <div id="codescontainer"></div>
     <br/>
    <div class="subheadsmallnew"><span class="subheadnew"><s:text name="billreceipt.billdetails.Rebate"/></span></div>
    
    <div class="yui-skin-sam" align="center">
       <div id="rebateDetailTable"></div>
     </div>
     <script>
        
        makeRebateDetailTable();
        document.getElementById('rebateDetailTable').getElementsByTagName('table')[0].width="100%";
     </script>
     <div id="rebatecodescontainer"></div>
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
  </td></tr>

      <tr>
        <td colspan="5"></td>
      </tr>
    </table>
    

    
