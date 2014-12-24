<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 
.yui-dt-col-NonSorEstdAmt{
	text-align:right;
}
.yui-dt-col-NonSorTaxAmt{
	text-align:right;
}
.yui-dt-col-NonSorTotal{
	text-align:right;
}	
.yui-dt-col-rate{
	text-align:right;
}		
.yui-dt-col-quantity{
	text-align:right;
}
.yui-dt-col-serviceTaxPerc{
	text-align:right;
}

.yui-dt-col-nonSordescription{
	text-align:left;
}
.yui-dt-col-SlNo{
	text-align:left;
}
.yui-dt-col-rate{
	text-align:left;
}
.yui-dt-col-Uom{
	text-align:left;
}
.yui-dt-col-NonSorDelete{
	text-align:left;
}
</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<script>

var uomDropdownOptions=[{label:"--- Select ---", value:"0"},

    <s:iterator var="s" value="dropdownData.uomList" status="status">  
    {"label":"<s:property value="%{egUomcategory.category}"/> -- <s:property value="%{uom}" />" ,
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
function calculateNonSOR(elem,recordId){
	if(!checkUptoFourDecimalPlace(elem,dom.get('nonsor_error'),"Unit Rate"))
	  return;
	record=nonSorDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
	if(!validateNumberInTableCell(nonSorDataTable,elem,recordId)) return;
	if(elem.value!=''){ 
		if(!validateNonSORDescription(recordId)) return;
		if(!validateNonSorUom(recordId)) return;
	}
	 	
    
}
function recalculateNonSorTotalsOnDelete(record){
	if(dom.get("nonsorrate"+record.getId()).value !="" && dom.get("nonsorquantity"+record.getId()).value!=""){
	  dom.get("nonSorEstTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorEstTotal") -getNumber(record.getData("NonSorEstdAmt")));
	  dom.get("nonSorTaxTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorTaxTotal") - getNumber(record.getData("NonSorTaxAmt")));
	  dom.get("nonSorGrandTotal").innerHTML=roundTo(getNumericValueFromInnerHTML("nonSorGrandTotal") - getNumber(record.getData("NonSorTotal")));
	  document.getElementById("estimateValue").value=roundTo(eval(document.getElementById("grandTotal").innerHTML)+eval(document.getElementById("nonSorGrandTotal").innerHTML)+eval(document.getElementById("overHeadTotalAmnt").innerHTML));
	}
}

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id="nonsor"+oColumn.getKey()+oRecord.getId();
    var fieldName = "nonSorActivities[" + oRecord.getCount() + "]." + oColumn.getKey();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='calculateNonSOR(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(10,13);
var stFormatter = createTextBoxFormatter(5,5);

var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
   var fieldName = "nonSorActivities[" + oRecord.getCount() + "]." + "nonSor.description";
	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='90' maxlength='4000' name='"+fieldName+"' onblur='validateNonSORDescription(\""+oRecord.getId()+"\");'/>"
	el.innerHTML = markup;	 	
}

function createNonSorHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "nonSorActivities[" + oRecord.getCount() + "]." + "nonSor.uom.id";
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var nonSorHiddenFormatter = createNonSorHiddenFormatter(10,10);


var nonSorDataTable;
var makeNonSORDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var nonSorColumnDefs = [ 
		{key:"NonSorId", hidden:true,sortable:false, resizeable:false} ,
		{key:"SlNo", label:'Sl No', width:50,sortable:false, resizeable:false},
		{key:"nonSordescription", width:450,label:'Description<span class="mandatory">*</span>', formatter:textboxDescFormatter, sortable:false, resizeable:true},		
		{key:"Uom", label:'UOM<span class="mandatory">*</span>', width:250,formatter:"dropdown", dropdownOptions:uomDropdownOptions},
		{key:"nonSorUom", hidden:true, formatter:nonSorHiddenFormatter, sortable:false, resizeable:false},
		{key:"rate",label:'Unit Rate<span class="mandatory">*</span>',width:150, formatter:textboxFormatter,sortable:false, resizeable:false},
		<s:if test="%{mode!='view'}">
		{key:'NonSorDelete',label:'Delete',width:50,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	    </s:if>
	];
	var nonSorDataSource = new YAHOO.util.DataSource(); 
	nonSorDataTable = new YAHOO.widget.DataTable("nonSorTable",nonSorColumnDefs, nonSorDataSource, {MSG_EMPTY:"<s:text name='estimate.nonsor.initial.table.message'/>"});
	nonSorDataTable.subscribe("cellClickEvent", nonSorDataTable.onEventShowCellEditor); 
	nonSorDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'NonSorDelete') { 			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}        
	});
	
	nonSorDataTable.on('dropdownChangeEvent', function (oArgs) {
	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='Uom'){
            var selectedIndex=oArgs.target.selectedIndex;
            this.updateCell(record,this.getColumn('nonSorUom'),uomDropdownOptions[selectedIndex].value);
            if(!validateNonSorUom(record)) return;
        }
	});
	
    
	
		

	return {
	    oDS: nonSorDataSource,
	    oDT: nonSorDataTable
	};        

}

function validateNonSORDescription(recordId){
	record=nonSorDataTable.getRecord(recordId);
	if(dom.get("nonSordescription"+record.getId()).value==''){  		
  		document.getElementById("nonsor_error").innerHTML='<s:text name="estimate.nonsor.description.null"/>';
  		document.getElementById("nonsor_error").style.display='';
  		dom.get("nonsorrate"+record.getId()).value='';
      	return false;
  	}
  	else{
       	 document.getElementById("nonsor_error").style.display='none';
       	 document.getElementById("nonsor_error").innerHTML='';	
       	 }
	return true;   
}

function validateNonSorUom(recordId){
	record=nonSorDataTable.getRecord(recordId);
	if((dom.get("nonSorUom"+record.getId()).value=='0' || dom.get("nonSorUom"+record.getId()).value=='') && dom.get("nonsorrate"+record.getId()).value!=''){  		
  		document.getElementById("nonsor_error").innerHTML='<s:text name="estimate.nonsor.uom.null"/>';
  		document.getElementById("nonsor_error").style.display='';
  		dom.get("nonsorrate"+record.getId()).value='';
      	return false;
  	}
  	else{
       	 document.getElementById("nonsor_error").style.display='none';
       	 document.getElementById("nonsor_error").innerHTML='';	
       	 }
	return true;   
}

function resetNonSorTable(){
	nonSorDataTable.deleteRows(0,nonSorDataTable.getRecordSet().getLength());
	
}


</script>		
      
        <div class="errorstyle" id="nonsor_error" style="display:none;"></div>
		<table id="nonSorHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">              	
              	<tr>
                	<td colspan="9" class="headingwk" style="border-right-width: 0px" align="left">
                		<div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
                		<div class="headplacer" >Non-SOR</div>
                	</td>
                	<td  align="right" class="headingwk" style="border-left-width: 0px">
                	<s:if test="%{mode!='view'}">
                	<a id="addnonSorRow" href="#" onclick="nonSorDataTable.addRow({SlNo:nonSorDataTable.getRecordSet().getLength()+1});return false;"><img height="16" border="0" width="16" alt="Add Non-SOR" src="${pageContext.request.contextPath}/image/add.png" /></a>
                	</s:if>
                	</td>
              	</tr>
              	<tr>
                	<td colspan="10">
                	<div class="yui-skin-sam">
                	    <div id="nonSorTable"></div>                    	
                    	<div id="nonSorTotals"></div>  
                	</div>
                	</td>
                </tr>
                <tr>
                	<td colspan="10" class="shadowwk"></td>
                </tr>
		</table> 
	<script>
		makeNonSORDataTable();
		<s:iterator id="nonsoriterator" value="NonSORActivities" status="row_status">
            nonSorDataTable.addRow({NonSorID:'<s:property value="nonSor.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    nonSordescription:'<s:property value="nonSor.descriptionJS" escape="false"/>',
                                    Uom:'<s:property value="nonSor.uom.id"/>',
                                    nonSorUom:'<s:property value="nonSor.uom.id"/>',
                                    rate:'<s:property value="rate"/>',
                                    Delete:'X'});
        var record = nonSorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = nonSorDataTable.getColumn('nonSordescription');
        // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="nonSor.descriptionJS" escape="false"/>';
        
        var column = nonSorDataTable.getColumn('rate');  
        dom.get("nonsor"+column.getKey()+record.getId()).value = '<s:property value="rate"/>';
        
      
        column = nonSorDataTable.getColumn('Uom');
        for(i=0; i < uomDropdownOptions.length; i++) {
            if (uomDropdownOptions[i].value == '<s:property value="nonSor.uom.id"/>') {
                nonSorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
            }
        }
        
        </s:iterator>
	</script> 
	
 <script type="text/javascript">
  <s:if test="%{mode=='view'}">
	for(i=0;i<document.estimateTemplateForm.elements.length;i++){
		document.estimateTemplateForm.elements[i].disabled=true;
		document.estimateTemplateForm.elements[i].readonly=true;
	} 
  </s:if>
</script>   
