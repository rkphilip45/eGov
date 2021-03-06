<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<div class="formmainbox">
	 <div class="errorstyle" id="error_area" style="display:none;"></div>
	 <span align="center" style="display:none" id="CodeUnique">
 		<font  style='color: red ; font-weight:bold '> 
         	<s:text name="service.code.already.exists"/>
 		 </font>
	</span>
		<span class="mandatory">
			<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
			</span>
	<div class="subheadnew"><s:text name="service.master.create.header"></s:text> </div><br>

	<div class="subheadsmallnew">
				<span class="subheadnew"> <s:text name="service.create.details.header"></s:text> </span>
			</div><br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>

			<td width="25%" class="bluebox2"> <s:text name="service.master.search.category"></s:text> </td>
			<td width="25%" class="bluebox2"><s:property value="serviceCategory.name"/></td>
			<td width="25%" class="bluebox2"> </td><td width="25%" class="bluebox2"> </td>
		</tr>
		<s:set name="serviceCodeInitVal" id="serviceCodeInitVal" value="%{code}"></s:set>
		<tr>
			<egov:uniquecheck id="CodeUnique" fields="['Value']" url='/service/serviceDetails!codeUniqueCheck.action'
			 key='service.code.already.exists' />
			<td width="25%" class="bluebox"> <s:text name="service.create.code"></s:text><span class="mandatory">*</span></td>
			<td width="25%" class="bluebox"><s:textfield name="code" id="serviceCode" maxLength="12"
			 onkeyup="uniqueCheckCode();" onblur="clearCodeIfExists();"></s:textfield> </td>
			<td width="25%" class="bluebox"> <s:text name="service.create.name"></s:text><span class="mandatory">*</span></td>
			<td width="25%" class="bluebox"> <s:textfield name="serviceName" id="serviceName" maxLength="100" ></s:textfield> </td>
		</tr>
		<tr>

			<td width="25%" class="bluebox2"> <s:text name="service.master.enable"></s:text> </td>
			<td width="25%" class="bluebox2"><s:checkbox name="isEnabled" /></td>
			<td width="25%" class="bluebox2"><s:text name="service.master.type" /> </td>
			<td width="25%" class="bluebox2"> 
				<s:select list="#{'':'-----Select----','B':'Bill Based', 'C':'Collection', 'P':'Payment'}" 
				name="serviceType" id="serviceType"></s:select>
			</td>
			
		</tr>
		<tr>
			<td width="25%" class="bluebox"> <s:text name="service.master.isvouchertobecreated"></s:text> </td>
			<td width="25%" class="bluebox" ><s:checkbox name="voucherCreation" id="voucherCreation" onchange="return EnableVoucherDetails(this)"/></td>
		</tr>
		<tr id="voucherApprovedDetails">
		<td width="25%" class="bluebox2"><s:text name="service.master.isvouchertobeapproved" ></s:text> </td>
			<td width="25%" class="bluebox2">
			<s:select list="#{'false':'Pre-Approved','true':'Approved'}" 
				name="isVoucherApproved" id="isVoucherApproved"></s:select>
			</td>
			<td width="25%" class="bluebox2"></td>
			<td width="25%" class="bluebox2"></td>
		</tr>

	</table>
	
	<div class="subheadsmallnew">
				<span class="subheadnew"> <s:text name="service.create.findetails.header"></s:text> </span>
			</div><br>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="25%" class="bluebox2"> <s:text name="service.master.create.fund"></s:text> </td>
				<td width="25%" class="bluebox2">
				<s:select headerKey="-1" headerValue="----Choose----" name="fund" id="fundId" cssClass="selectwk"
					list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" onChange="populateSchemes(this);" /></td>
				<td width="25%" class="bluebox2"><s:text name="service.master.create.fundsource"></s:text>  </td>
				<td width="25%" class="bluebox2">
				<s:select headerKey="-1" headerValue="----Choose----" name="fundSource" id="fundSourceId" cssClass="selectwk"
					list="dropdownData.fundsourceList" listKey="id" listValue="name" value="%{fundSource.id}" /> </td>
			
		</tr>
		
		<tr>
		<egov:ajaxdropdown id="scheme"fields="['Text','Value']" dropdownId="schemeId" url="/receipts/ajaxReceiptCreate!ajaxLoadSchemes.action" />
			<td width="25%" class="bluebox"> <s:text name="service.master.create.scheme"></s:text> </td>
				<td width="25%" class="bluebox">
				<s:select headerKey="-1" headerValue="----Choose----" name="scheme" id="schemeId" cssClass="selectwk"
					list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme.id}"  onChange= "populatesubSchemes(this)" /></td>
		<egov:ajaxdropdown id="subscheme"fields="['Text','Value']" dropdownId="subschemeId" url="/receipts/ajaxReceiptCreate!ajaxLoadSubSchemes.action" />
				<td width="25%" class="bluebox"><s:text name="service.master.create.subscheme"></s:text>  </td>
				<td width="25%" class="bluebox">
				<s:select headerKey="-1" headerValue="----Choose----" name="subscheme" id="subschemeId" cssClass="selectwk"
					list="dropdownData.subschemeList" listKey="id" listValue="name" value="%{subscheme.id}" /> </td>
			
		</tr>
		
		<tr>
			<td width="25%" class="bluebox2"> <s:text name="service.master.create.functionary"></s:text> </td>
				<td width="25%" class="bluebox2">
				<s:select headerKey="-1" headerValue="----Choose----" name="functionary" id="functionaryId" cssClass="selectwk"
					list="dropdownData.functionaryList" listKey="id" listValue="name" value="%{functionary.id}" /></td>
				
			<td width="25%" class="bluebox2"> <s:text name="service.master.create.department"></s:text> </td>
				<td width="25%" class="bluebox2">
				<s:select name="departmentList"  list="dropdownData.departmentList" listKey="id"
					 listValue="deptName"  multiple="true" size="6" required="true"/></td>
		</tr>
		
		
		
		<s:hidden name="serviceCategory.id" id="serviceCategory.id"></s:hidden>
		<s:hidden name="id"></s:hidden>
	</table>
	<jsp:include page="finAccountsTable.jsp"/>

<div class="subheadsmallnew"><span class="subheadnew">Account Details</span></div>
    
    <div class="yui-skin-sam" align="center">
       <div id="accountsDetailTable"></div>
       
     </div>
     <script>
        
        makeAccountsDetailTable();
        document.getElementById('accountsDetailTable').getElementsByTagName('table')[0].width="100%";
     </script>
     <div id="codescontainer"></div>
     <br/>
    
      <div class="subheadsmallnew"><span class="subheadnew">Subledger Details</span></div>
    
        
        <div class="yui-skin-sam" align="center">
           <div id="subLedgerTable"></div>
         </div>
        <script>
            
            makeSubLedgerTable();
            
            document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="100%";
        </script>
<div id="subledgercodescontainer"></div> 
	
<div align="left" class="mandatorycoll"><s:text name="common.mandatoryfields"/></div>
</div>
<script>
function populateSchemes(fund){
	
	if(null != document.getElementById("schemeId")){
		
		populateschemeId({fundId:fund.options[fund.selectedIndex].value});
		populatesubschemeId({schemeId:-1});
	}
		
}
function populatesubSchemes(scheme){
	
	populatesubschemeId({schemeId:scheme.options[scheme.selectedIndex].value});	
	
}

function validate(){
	dom.get('error_area').innerHTML = '';
	dom.get("error_area").style.display="none"
	if(dom.get('serviceCode').value.trim().length == 0){
		dom.get("error_area").innerHTML = '<s:text name="service.code.null" />';
		dom.get("error_area").style.display="block";
		return false;
	}
	else if(dom.get('serviceName').value.trim().length == 0){
		dom.get("error_area").innerHTML = '<s:text name="service.name.null" />';
		dom.get("error_area").style.display="block";
		return false;
	}
	return true;
}
function uniqueCheckCode(){
	var serviceCode = dom.get("serviceCode").value.trim();
	var serviceCodeInitVal ='<s:property value="%{code}" />';
    if(serviceCode !="" && serviceCodeInitVal.trim() != serviceCode){
		populateCodeUnique({code:dom.get('serviceCode').value});
   }	
}

function clearCodeIfExists(){
	 if(dom.get("CodeUnique").style.display =="" ){
		 document.getElementById('serviceCode').value="";
	 }	
}
</script>