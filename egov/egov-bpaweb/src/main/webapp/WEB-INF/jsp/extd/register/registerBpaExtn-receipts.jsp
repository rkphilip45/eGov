<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />

 <SCRIPT>
  jQuery.noConflict();

</SCRIPT>

 <div align="center" id="orddetdiv">
 	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="orderTbl">
 		<tr>
						<td colspan="6"><div class="headingbg"><span class="bold">Fee collection Details</span></div></td>
					</tr>
 		<tr>
 			<td>
 		
 	      <s:if test="%{billRecptInfoList.size!=0}">
  	        <c:set value="0" var="i"/>
  	       
 			<s:iterator value="billRecptInfoList" var="receipt">
			 		<table width="100%" border="0" cellspacing="0" cellpadding="0" id="signDet">   
					 <tr>
						<td class="bluebox" width="13%"><c:set value="${i+1}" var="i"/>
						</td>
							<td class="bluebox" width="13%">Receipt Number:</td>
						<td class="bluebox"> 
							<b> 	<a href="/../collection/citizen/onlineReceipt!viewReceipt.action?receiptNumber=<s:property value="#receipt.getReceiptNum()" />&consumerCode=<s:property value="%{registrationId}" />&serviceCode=EXTDBPA" target="_blank" >
                                                       <s:property value="#receipt.getReceiptNum()" /> 
                                                    </a></b>
						</td>
						<td class="bluebox">Dated : </td>
						<td class="bluebox">
							<s:date name="#receipt.getReceiptDate()" format="dd/MM/yyyy" var="rcptdate" /><s:property value="rcptdate" /></div></td>
						<td class="bluebox">with amount Rs. :<b> <s:property value="#receipt.getTotalAmount()" /></b></td>
					</tr>
						
				</table>
	 		
	 		
	 	
			</s:iterator> 
		  </s:if>
		  <s:else>
		  <td  class="blueborderfortd"> No Receipts found.</td>
 		</s:else>	
			</td></tr>   
		</table>
 </div>
 
