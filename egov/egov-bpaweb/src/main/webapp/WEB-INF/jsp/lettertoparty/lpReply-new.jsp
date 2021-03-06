<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:text name="letterToPartyReply"/>
</title>

<head>
<sj:head jqueryui="true" jquerytheme="blitzer" loadAtOnce="true" />


<script type="text/javascript">
jQuery.noConflict();

function validateForm(){
	var count =0;
	var j=0;
jQuery("[id=mandatorycheck]").each(function(index) {	
   		jQuery(this).find(':checkbox').css('outline-color', '');
		jQuery(this).find(':checkbox').css('outline-style', '');
		jQuery(this).find(':checkbox').css('outline-width', '');
   		jQuery(this).next("td").find('textarea').css("border", '');
   		});	
   		
   		
	dom.get("lp_error").style.display='none';
	
	if(document.getElementById('letterToPartyReplyDesc').value==null || document.getElementById('letterToPartyReplyDesc').value==""){
		dom.get("lp_error").style.display = '';
		document.getElementById("lp_error").innerHTML = '<s:text name="lpreply.desp.mandatory" />';
		return false;
	}
	
	jQuery("[id=mandatorycheck]").each(function(index) {	
		
       if(jQuery(this).find(':checkbox').prop('checked')){
    	  	dom.get("lp_error").style.display = 'none';
    		document.getElementById("lp_error").innerHTML='';
   	   	;
   		
   		}else{ 		
   		  j++;
   		
   		dom.get("lp_error").style.display = '';
   		jQuery(this).find(':checkbox').css('outline-color', 'red');
		jQuery(this).find(':checkbox').css('outline-style', 'solid');
		jQuery(this).find(':checkbox').css('outline-width', 'thin');
		
		}
		});		
	var size=jQuery('#checklistsize').val();
	
	if(size==j){
	document.getElementById("lp_error").innerHTML = 'Atleast one checkList needs be select';
			return false;
	}

	jQuery("[id=mandatorycheck]").each(function(index) {	
			if(jQuery(this).find(':checkbox').prop('checked')){
							var rem=jQuery(this).next("td").find('textarea').attr("value");
   							if(rem==""){
   								dom.get("lp_error").style.display = '';
   									document.getElementById("lp_error").innerHTML = 'Please enter the remarks for the checked checkList';
   									jQuery(this).next("td").find('textarea').css("border", "1px solid red");
   									count++;
   									}
   					}
   		});	
   		if(count!=0)
			return false;
   		
}

function confirmClose(){
	var result = confirm("Do you want to close the window?");
	if(result==true){
		window.close();
		return true;
	}else{
		return false;
	}
}

function  openSearchAutoDcr()
{
	window.open('${pageContext.request.contextPath}/search/searchAutoDcr!searchAuto.action?','window','scrollbars=yes,resizable=no,height=800,width=900,status=yes');

}



function callSetAutoDCR(autoDcrNum,applicant_name,address,email,mobileno,plotno,doorno,village,surveyno,blockno,plotarea)
{
	
	document.getElementById("autoDcrNum").value=autoDcrNum;
	
	//alert("autodcr"+document.getElementById("autoDcr.autoDcrNum").value);
}
function onreportupload()
	 {
	     var v= dom.get("docNumber").value;
	     var url;
	     if(v==null||v==''||v=='To be assigned')
	     {
		   url="/egi/docmgmt/basicDocumentManager.action?moduleName=BPA";
	     }
	     else
	     {
		   
	       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=BPA";
	     }
	     var wdth = 1000;
	     var hght = 400;
	     window.open(url,'docupload','width='+wdth+',height='+hght);
	 }	
</script>

</head>
<body onload="refreshInbox();">
<div class="errorstyle" id="lp_error" style="display:none;" >
</div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
</s:if>

<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
				<s:actionmessage />
		</div>
</s:if>

<s:form action="lpReply" theme="simple">
<s:token/>
<s:push value="model">

<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="requestID" name="requestID" value="%{requestID}"/>

<div  id="lpdetails" class="formmainbox">
<s:if test="%{letterParty != null && letterParty.isHistory != 'Y'}">
<div class="headingbg"><s:text name="lettertoparty.header"/></div>
 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
 		
	   	 	<tr>
	   	 		<td class="bluebox" ><s:text name="lpNum"/> : </td>
				<td class="bluebox" >
					<s:property value="existLpNum"/>
				</td>
	   	 		<td class="bluebox" colspan="2">&nbsp;</td>
          </tr>
          <tr>
	   	 		<td class="bluebox" ><s:text name="lpreason"/> : </td>
				<td class="bluebox" >
					<s:property value="existLpReason"/>
				</td>
	   	 		<td class="bluebox" ><s:text name="lpdescription"/></td>
	 			<td class="bluebox">
	 				<s:property value="existLpRemarks"/>
	 			</td>
          </tr>
       
          <tr>
	   	 	     <td class="bluebox" ><s:text name="lpReplyDesc"/> <span class="mandatory" >*</span>: </td>
	 			<td class="bluebox">
	 				<s:textarea id="letterToPartyReplyDesc" name="letterToPartyReplyDesc" value="%{letterToPartyReplyDesc}" cols="20" rows="3"/>
	 			</td>  
	 			<td class="bluebox" ><s:text name="lpReplyRemarks"/> : </td>
	 			<td class="bluebox">
	 				<s:textarea id="letterToPartyReplyRemarks" name="letterToPartyReplyRemarks" value="%{letterToPartyReplyRemarks}" cols="20" rows="3"/>
	 			</td>   			
          </tr>
                        <tr>
	   	 	     <td class="bluebox" ><s:text name="autoDcrNum"/> : </td>
	 			<td class="bluebox">
	 				<s:textfield id="autoDcrNum" name="autoDcrNum" value="%{autoDcrNum}" readonly="true" />
	 				<a href="javascript:openSearchAutoDcr();">Search AutoDcr</a>
	 			</td>  
	 			<td class="bluebox" colspan="2">&nbsp;</td>
          </tr>

		<tr> 
			<td class="bluebox">
								<s:text name="Document upload: " />
						</td>
						<s:if test="documentNum==null || documentNum==''">
								<td id="addlink" class="bluebox">
									<div class="buttonholderrnew">
										<input type="button" id="browse" value="Browse"
											onclick="onreportupload();return false;"
											class="buttoncreatenewcase" />
									</div>
								</td>
							</s:if>
					
						<!--<s:else>
							<td id="viewlink" class="bluebox">
									<s:text name="view documents" />
									<egov:docFiles moduleName="BPA"
										documentNumber="${documentNum}" id="documentRef"
										showOnLoad="true" cssClass="bluebox" />
							</td>
						</s:else>-->
							<td class="bluebox"/>
							  <td class="bluebox"/>
                    
                      
                       
				<s:hidden name="documentNum" id="docNumber" value="%{documentNum}"></s:hidden>
                      
                      
                      
                </tr>

	 </table>
	 </s:if>
	 </div>
  	<s:if test="%{mode!='view'}">
		 <s:url id="checklistajax" value="/lettertoparty/lpReply!showCheckList.action"  escapeAmp="false">
		 	<s:param name="serviceTypeId" value=""></s:param>	
		 	<s:param name="registrationId" value="%{registrationId}"></s:param>	
		 </s:url>
	     <sj:div href="%{checklistajax}" indicator="indicator"  cssClass="" id="tab1"  dataType="html" onCompleteTopics="completedchecklistDiv">
	       <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
	     </sj:div>
	</s:if>
	 <div class="buttonbottom" align="center">
		<table>
		<tr>
		 <s:if test="%{mode!='view' && (letterParty != null && letterParty.isHistory != 'Y')}">
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save" method="createLpReply" onclick="return validateForm()"/></td>	
	  	 </s:if>	         
	  	 <s:if test="%{mode=='view'}">
	  		<td>
	  			<s:submit type="submit" cssClass="buttonsubmit" value="Print Ack" id="print" name="print" method="ackPrint"/>
	  		</td>
	  	</s:if>
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="confirmClose();"/>
	  		</td>
	  	</tr>
        </table>
    </div>
</s:push>
</s:form>

</body>
</html>
