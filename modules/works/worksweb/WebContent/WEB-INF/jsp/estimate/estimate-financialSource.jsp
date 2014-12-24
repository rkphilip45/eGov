<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<style type="text/css">
.yui-dt0-col-TaxAmt {
  width: 5%;
}
.yui-dt-col-Percentage{
	text-align:right;
}

</style> 
<script>
var fundSourceDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="fundSourceList" status="status">  
    {"label":"<s:property value="%{name}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
    
function validateFinancialSourceForm(){
	var totalFinSrcPercentage=calculateTotal();
	if(getNumber(totalFinSrcPercentage)>100){
	    document.getElementById("finSource_error").style.display='';
  		document.getElementById("finSource_error").innerHTML='The Total Percentage cannot be greater than 100'; 
     	return false;
    }
	return true;
}       
       
function calculateTotalFinSrcPercentage(elem,recordId){
	record=financialSourceDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
 	if(isNaN(elem.value) || getNumber(elem.value)<0){
		dom.get('error'+elem.id).style.display='';
		return;
  	}
  	if(getNumber(dom.get("Percentage"+record.getId()).value)>100){
		dom.get('error'+elem.id).style.display='';
		return;
  	}

	var totalFinSrcPercentage=calculateTotal();
	
	if(getNumber(totalFinSrcPercentage)>100){
	    document.getElementById("finSource_error").style.display='';
  		document.getElementById("finSource_error").innerHTML='The Total Percentage cannot be greater than 100'; 
     	return;
    }
    else if(getNumber(totalFinSrcPercentage)>100 && document.getElementById("finSource_error").innerHTML=='The Total Percentage cannot be greater than 100'){
    	document.getElementById("finSource_error").innerHTML='';   
    	document.getElementById("finSource_error").style.display='none';
    }
	dom.get("totalFinSrcPercentage").innerHTML=totalFinSrcPercentage;	  		
}

function calculateTotal(){

	var records= financialSourceDataTable.getRecordSet();
	var totalFinSrcPercentage=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("Percentage"+records.getRecord(i).getId()).value!=0.0){
			totalFinSrcPercentage=  totalFinSrcPercentage + eval(getNumber(dom.get("Percentage"+records.getRecord(i).getId()).value));
		}
	}
	if(totalFinSrcPercentage<=100 && document.getElementById("finSource_error").innerHTML=='The Total Percentage cannot be greater than 100'){
		document.getElementById("finSource_error").style.display='none';
		document.getElementById("finSource_error").innerHTML='';
	}
	return totalFinSrcPercentage;
}

function createPercTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();    
	    var fieldName = "financingSourceList[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onkeyup='validateDecimal(this)' onblur='calculateTotalFinSrcPercentage(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var percTextboxFormatter = createPercTextBoxFormatter(5,5);

function createFundSourceIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "financingSourceList[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var fundSourceIdHiddenFormatter= createFundSourceIDFormatter(10,10);

var financialSourceDataTable;
var makeFinancialSourceDataTable= function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var financialSourceColumnDefs = [		
		{key:"fundSource", hidden:true, formatter:fundSourceIdHiddenFormatter, sortable:false, resizeable:true},
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"Name", label:'<span class="mandatory">*</span>Source Name', formatter:createDropdownFormatter('financingSourceList','id'), dropdownOptions:fundSourceDropdownOptions},		
		{key:"Percentage",label:'<span class="mandatory">*</span>Percentage',formatter:percTextboxFormatter, sortable:false, resizeable:false, width : 275},
	
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}"),width : 83},

	 	{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}"), width : 82}
	];
		
	var financialSourceDataSource = new YAHOO.util.DataSource(); 
	financialSourceDataTable= new YAHOO.widget.DataTable("financialSourceTable",financialSourceColumnDefs, financialSourceDataSource , {initialRequest:"query=orders&results=10"});	
	financialSourceDataTable.subscribe("cellClickEvent", financialSourceDataTable.onEventShowCellEditor); 
	financialSourceDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
		   financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1});
		}

if(column.key == 'Name'){ 

<s:if test="%{model.id !=null}">
	if(record.getCount() == 1){
		alert('can not be changed');
    }
</s:if>
<s:else>
	if(record.getCount() == 0){
		alert('can not be changed');
	}
</s:else>

}
		if (column.key == 'Delete') { 			
			if(this.getRecordSet().getLength()>1){	
			<s:if test="%{model.id !=null}">
			    if(record.getCount() == 1){
				alert('can not be deleted');
        		    }else{
				this.deleteRow(record);
				var totalPerc=calculateTotal();					
				dom.get("totalFinSrcPercentage").innerHTML=totalPerc;						
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
				   this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			    }
			</s:if>
			<s:elseif test="%{model.id ==null}">
			   if(record.getCount() == 0){
				alert('can not be deleted');
			   }else{
				this.deleteRow(record);
				var totalPerc=calculateTotal();					
				dom.get("totalFinSrcPercentage").innerHTML=totalPerc;						
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
				   this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			   }
			</s:elseif>
			<s:else>
				this.deleteRow(record);
				var totalPerc=calculateTotal();					
				dom.get("totalFinSrcPercentage").innerHTML=totalPerc;						
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
				   this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			</s:else>

			}
			else
			{
				alert("This row can not be deleted");
			}
		}        
	});	
	
	financialSourceDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='Name'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
    	    validateDuplicate(this.getRecordSet(),oArgs); 
        	this.updateCell(record,this.getColumn('fundSource'),fundSourceDropdownOptions[selectedIndex].value);
        }
	});
	
	<!-- disabling 1st fund source -->
	
	if(document.getElementById('testError')==null) {
		financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1,Name:"${abstractEstimate.fundSource.id}",fundSource:"${abstractEstimate.fundSource.id}"});
	}
	else if(document.getElementById('testError')!=null && document.getElementById('testError').value!='<s:text name="financingsource.fundsource.null" />') {
		financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1,Name:"${abstractEstimate.fundSource.id}",fundSource:"${abstractEstimate.fundSource.id}"});
	}
	else 
		financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1,Name:"0",fundSource:"0"});
	if(dom.get("Nameyui-rec0")!=null) {
	dom.get("Nameyui-rec0").disabled=true;
    dom.get("Nameyui-rec0").readonly=true;	
    }	
	dom.get("Percentage"+financialSourceDataTable.getRecordSet().getRecord(0).getId()).value="100";
	
	var tfoot = financialSourceDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 2;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'Total';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'totalFinSrcPercentage','');
	addCell(tr,3,'filler','');
    var elem = document.getElementById('type');
  	var appConfigValuesToSkipBudget='<s:property value="%{appConfigValuesToSkipBudget}"/>';
  	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget.split('[');
	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[1].split(']');
  	appConfigValuesToSkipBudget=appConfigValuesToSkipBudget[0].split(', ');
    for(i=0;i<appConfigValuesToSkipBudget.length;i++){
	    if(appConfigValuesToSkipBudget[i]==elem.options[elem.selectedIndex].text) {
	addCell(tr,4,'filler',''); 	
			break;	
		}
	}
	return {
	    oDS: financialSourceDataSource,
	    oDT:financialSourceDataTable
	};    
}

function validateDuplicate(records,oArgs){
    for(i=0;i<records.getLength();i++){ 
     var selectedIndex=oArgs.target.selectedIndex;
       if(dom.get("fundSource"+records.getRecord(i).getId()).value==fundSourceDropdownOptions[selectedIndex].value){
          document.getElementById("finSource_error").innerHTML='The Source name '+fundSourceDropdownOptions[selectedIndex].label+' is already selected'; 
          document.getElementById("finSource_error").style.display='';
          oArgs.target.selectedIndex=0;
          return;
       } 
       else{
       	 document.getElementById("finSource_error").style.display='none';
       	 document.getElementById("finSource_error").innerHTML='';	
       	 }
    }
}

</script>
        <br/>
        <s:iterator value="getFieldErrors().entrySet()" var="entry">
		    <s:iterator value="#entry.value">
				<input type="hidden" name="testError" id="testError" value='<s:property value="%{top}"/>'/>	
		     </s:iterator>
		 </s:iterator>
		<table id="financialSourceHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
		 <div class="errorstyle" id="finSource_error" style="display:none;"></div>
              	<tr>
                	<td colspan="4" class="headingwk">
                		<div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
                		<div class="headplacer"><s:text name='page.header.financial.source.detail'/></div>
                	</td>
              	</tr>
              	<tr>
                	<td colspan="5">
                	<div class="yui-skin-sam">
                    	<div id="financialSourceTable"></div>
                    	<div id="financialSourceTotals"></div>  
                	</div>
                	</td>
                </tr>
                
                <tr>
                <td  colspan="5" class="shadowwk"></td>
                </tr>
		</table> 
	<script>
		makeFinancialSourceDataTable();
        <s:iterator id="finSourceIterator" value="financialDetails[0].financingSources" status="row_status">
          <s:if test="#row_status.count == 1">
             financialSourceDataTable.updateRow(0,
                                   {fundSource:'<s:property value="fundSource.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Name:'<s:property value="fundSource.name"/>',   
                                    Percentage:'<s:property value="percentage"/>',
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
          </s:if>
          <s:else>
             financialSourceDataTable.addRow(
                                   {fundSource:'<s:property value="fundSource.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Name:'<s:property value="fundSource.name"/>',   
                                    Percentage:'<s:property value="percentage"/>',
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
          </s:else>
          
        var record = financialSourceDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
        var column = financialSourceDataTable.getColumn('Name');
        for(i=0; i < fundSourceDropdownOptions.length; i++) {
            if (fundSourceDropdownOptions[i].value == '<s:property value="fundSource.id"/>') {
                financialSourceDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
            }
        } 
 
        var column = financialSourceDataTable.getColumn('Percentage');  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage"/>';
        calculateTotalFinSrcPercentage(dom.get(column.getKey()+record.getId()), record.getId());
        </s:iterator>
	</script>       