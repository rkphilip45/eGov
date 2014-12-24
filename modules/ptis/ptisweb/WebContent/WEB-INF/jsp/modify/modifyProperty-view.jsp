<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<s:if test="modifyRsn=='AMALG'">
			<title><s:text name='AmalgProp.title' /></title>
		</s:if>
		<s:if test="modifyRsn=='BIFURCATE'">
			<title><s:text name='BifurProp.title' /></title>
		</s:if>
		<s:if test="modifyRsn=='MODIFY' || modifyRsn=='OBJ' || modifyRsn=='DATA_ENTRY'">
			<title><s:text name='ModProp.title' /></title>
		</s:if>
		<sx:head />
		<script type="text/javascript">
	function loadOnStartUp() {
   		var propType = '<s:property value="%{propertyDetail.propertyTypeMaster.type}" />';
   		if(propType=="Open Plot") {
			document.getElementById("floorDetails").style.display="none";
			document.getElementById("floorHeader").style.display="none";
		    var tbl = document.getElementById('floorDetails');	
			if(tbl!=null) {
				var rowo = tbl.rows;
				resetCreateFloorDetails(rowo);
			}		
		}
   		<s:if test="%{extra_field4 != 'Yes'}">
		    var btnPVR = document.getElementById("GeneratePrativrutta");
		    if (btnPVR != null) {
		    	btnPVR.disabled = false;
		    }
		</s:if>
	}
   
	function generatenotice(){
	    document.ModifyPropertyForm.action="../notice/propertyTaxNotice!generateNotice.action?indexNumber=<s:property value='%{basicProp.upicNo}'/>";
		document.ModifyPropertyForm.submit();
	}
			  
	function generatePrativrutta(){
	  	window.open("../notice/propertyTaxNotice!generateNotice.action?indexNumber=<s:property value='%{basicProp.upicNo}'/>&noticeType=Prativrutta","","resizable=yes,scrollbars=yes,top=40, width=900, height=650");
	  	document.getElementById("GeneratePrativrutta").disabled = true;
	}
				  
	function generateBill(){
		document.ModifyPropertyForm.action="../bills/billGeneration!generateBill.action?indexNumber=<s:property value='%{basicProp.upicNo}'/>";
		document.ModifyPropertyForm.submit();
	}				  
</script>
	</head>

	<body onload="loadOnStartUp();">
		<div align="left">
			<s:actionerror />
		</div>
		<s:if test="%{hasActionMessages()}">
			<div id="actionMessages" class="messagestyle" align="center">
				<s:actionmessage theme="simple" />
			</div>
			<div class="blankspace">
				&nbsp;
			</div>
		</s:if>
		<!-- Area for error display -->
		<div class="errorstyle" id="property_error_area"
			style="display: none;"></div>
		<s:form name="ModifyPropertyForm" action="modifyProperty"
			theme="simple" validate="true">
			<s:push value="model">
			<s:token/>
				<s:hidden label="noticeType" id="noticeType" name="noticeType"
					value="%{extra_field2}" />
				<s:hidden name="modifyRsn" value="%{modifyRsn}" />
				<div class="formmainbox">
					<div class="formheading"></div>
					<div class="headingbg">
						<s:if test="modifyRsn=='AMALG'">
							<s:text name="AmalgProp.title" />
						</s:if>
						<s:if test="modifyRsn=='BIFURCATE'">
							<s:text name="BifurProp.title" />
						</s:if>
						<s:if test="modifyRsn=='MODIFY' || modifyRsn=='OBJ' || modifyRsn=='DATA_ENTRY'">
							<s:text name="ModProp.title" />
						</s:if>
					</div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<s:if test="modifyRsn == 'MODIFY' || modifyRsn == 'DATA_ENTRY'">
								<%@ include file="../modify/modifyOrDataUpdateView.jsp"%>
							</s:if>
							<s:else>
								<%@ include file="../modify/modifyPropertyView.jsp"%>
							</s:else>
							
						</tr>
						<s:if test="%{userRole == @org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
							<tr>
								<%@ include file="../workflow/property-workflow.jsp"%>
							</tr>
						</s:if>
						<s:else>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="bluebox2" width="6%">
										&nbsp;
									</td>
									<td class="bluebox" width="10%">
										<s:text name='approver.comments' />
									</td>
									<td class="bluebox" width="8%">
										<s:textarea name="workflowBean.comments" id="comments"
											rows="3" cols="80" onblur="checkLength(this);" />
									</td>
									<td class="bluebox" width="15%" colspan="2"></td>
								</tr>
								<s:hidden name="workflowBean.actionName"
									id="workflowBean.actionName" />
							</table>
						</s:else>
						<s:hidden name="modelId" id="modelId" value="%{modelId}" />
						<tr>
							<font size="2"><div align="left" class="mandatory">
									<s:text name="mandtryFlds" />
								</div> </font>
						</tr>
						<div class="buttonbottom" align="center">
						<tr>
							<s:if
								test="%{model.state.value.endsWith(@org.egov.ptis.nmc.constants.NMCPTISConstants@WF_STATE_NOTICE_GENERATION_PENDING)}">
								<s:if test="%{extra_field3!='Yes'}">
									<input type="button" name="GenerateNotice" id="GenerateNotice"
										value="Generate Notice" class="button"
										onclick="return generatenotice()" />
								</s:if>

								<s:if test="%{extra_field4!='Yes'}">
									<input type="button" name="GeneratePrativrutta"
										id="GeneratePrativrutta" value="Generate Prativrutta"
										class="button" onclick="return generatePrativrutta()" />
								</s:if>
								<s:if
									test="%{extra_field5!='Yes' && property.propertyDetail.propertyMutationMaster.code == 'OBJ'}">
									<input type="button" name="GenerateBill" id="GenerateBill"
										value="Generate Bill" class="button"
										onclick="return generateBill();" />
								</s:if>
							</s:if>
							<s:else>
								<s:if test="modifyRsn=='AMALG'">
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
										<td>
											<s:submit value="Forward" name="Forward"
												id='Amalgamate:Forward' cssClass="buttonsubmit"
												method="forwardView" onclick="setWorkFlowInfo(this);" /></td>
									</s:if>
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTAPPROVER_ROLE}">
									<td>
										<s:submit value="Approve" name="Approve"
											id='Amalgamate:Approve' cssClass="buttonsubmit"
											method="approve" onclick="setWorkFlowInfo(this);" />
									</td>
									</s:if>
									<td>
										<s:submit value="Reject" name="Reject"
											id='Amalgamate:Reject' cssClass="buttonsubmit"
											method="reject" onclick="setWorkFlowInfo(this);" />
									</td>
								</s:if>
								<s:if test="modifyRsn=='BIFURCATE'">
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
										<td>
											<s:submit value="Forward" name="Forward"
												id='Bifurcate:Forward' cssClass="buttonsubmit"
												method="forwardView" onclick="setWorkFlowInfo(this);" />
										</td>
									</s:if>
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTAPPROVER_ROLE}">
									<td>
										<s:submit value="Approve" name="Approve"
											id='Bifurcate:Approve' cssClass="buttonsubmit"
											method="approve" onclick="setWorkFlowInfo(this);" />
									</td>
									</s:if>
									<td>
										<s:submit value="Reject" name="Reject"
											id='Bifurcate:Reject' cssClass="buttonsubmit"
											method="reject" onclick="setWorkFlowInfo(this);" />
									</td>
								</s:if>
								<s:if test="modifyRsn=='MODIFY' || modifyRsn=='OBJ' || modifyRsn=='DATA_ENTRY'">
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE}">
										<td>
											<s:submit value="Forward" name="Forward" id='Modify:Forward'
												cssClass="buttonsubmit" method="forwardView"
												onclick="setWorkFlowInfo(this);" />
										</td>
									</s:if>
									<s:if test="%{userRole==@org.egov.ptis.nmc.constants.NMCPTISConstants@PTAPPROVER_ROLE}">
									<td>
										<s:submit value="Approve" name="Approve" id='Modify:Approve'
											cssClass="buttonsubmit" method="approve"
											onclick="setWorkFlowInfo(this);" />
									</td>
									</s:if>
									<td>
										<s:submit value="Reject" name="Reject" id='Modify:Reject'
											cssClass="buttonsubmit" method="reject"
											onclick="setWorkFlowInfo(this);" />
									</td>
								</s:if>
							</s:else>
							<td>
								<input type="button" name="button2" id="button2" value="Close"
									class="button" onclick="window.close();" />
							</td>
						</tr>
						</div>
					</table>
				</div>
			</s:push>
		</s:form>
	</body>
</html>