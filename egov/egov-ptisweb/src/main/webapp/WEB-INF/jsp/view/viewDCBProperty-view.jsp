#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ include file="/includes/taglibs.jsp"%>

<html>

	<head>
		<title><s:text name="viewDCB"></s:text></title>
		<script type="text/javascript">
			
			function loadOnStartup () {
				var btnCheckbox = document.getElementById('taxEnsureCheckbox');
				var btnPayTax = document.getElementById('PayTax');
				var buttorOperatorPayTax = document.getElementById('operatorPayBill');
				
				if (btnPayTax != null) {
					btnPayTax.disabled = (btnCheckbox.checked) ? false : true;
				}
				
				if (buttorOperatorPayTax != null) {
					buttorOperatorPayTax.disabled = (btnCheckbox.checked) ? false : true;
				}
			}
			
			function openNewWindow() {
				window.open('../view/viewDCBProperty!getPropertyArrears.action?' + 
							'propertyId=<s:property value="%{basicProperty.upicNo}" />&decorate=false', 
							'_blank', 'width=650, height=500', false);
			}

			function openHeadwiseDCBWindow() {
				window.open('../view/viewDCBProperty!displayHeadwiseDcb.action?' + 
							'propertyId=<s:property value="%{basicProperty.upicNo}" />', 
							'_blank', 'width=650, height=500, scrollbars=yes', false);
			}
					
			function switchPayTaxButton(ensureCheckbox) {
				var buttonPayTax = document.getElementById('PayTax');
				
				if (buttonPayTax == null) {
					document.getElementById('operatorPayBill').disabled = (ensureCheckbox.checked) ? false : true;
				} else {
					buttonPayTax.disabled = (ensureCheckbox.checked) ? false : true;	
				}
				
			}
			function setLevyPenalyBeforeSubmit() {
				var propertyId = '<s:property value="%{basicProperty.upicNo}"/>';
				window.location='../collection/collectPropertyTax!showPenalty.action?propertyId='+propertyId;
			}
		</script>
	</head>

	<body onload="loadOnStartup();">
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg">
				<s:text name="viewDCB" />
			</div>
			<s:form action="#" theme="simple">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<s:if test="propertyId!=null">
						<tr>
							<td class="bluebox" width="10%%"></td>
							<td class="bluebox" width="13%" colspan="2">
								<s:text name="prop.Id" /> :
							</td>
							<td class="bluebox" colspan="2">
								<span class="bold"> <s:property value="%{propertyId}" />
								</span>
							</td>
						</tr>
						<tr>
							<td class="greybox" width="10%"></td>
							<td class="greybox" width="5%" colspan="2">
								<s:text name="Ward" /> :
							</td>
							<td class="greybox" colspan="2">
								<span class="bold"> <s:property value="%{wardName}" /> </span>
							</td>

						</tr>

					</s:if>
					<tr>
						<td class="bluebox" width="10%"></td>
						<td class="bluebox" colspan="2">
							<s:text name="OwnerName" /> :
						</td>
						<td class="bluebox" colspan="2">
							<span class="bold"> <s:property value="%{ownerName}" /> </span>
						</td>
					</tr>
					<tr>
						<td class="greybox" width="10%"></td>
						<td class="greybox" colspan="2">
							<s:text name="PropertyAddress" /> :
						</td>
						<td class="greybox" colspan="2">
							<span class="bold"> <s:property value="%{propertyAddress}" />
							</span>
						</td>

					</tr>
					<tr>
						<td class="bluebox" width="10%"></td>
						<td class="bluebox" colspan="2">
							<s:text name="PropertyType" /> :
						</td>
						<td class="bluebox" colspan="2">
							<span class="bold"> <s:property value="%{propertyType}" />
							</span>
						</td>
					</tr>
					<s:if test="!dcbReport.getRecords().isEmpty()">
					<tr>
						<td class="greybox" width="10%"></td>
						<td class="greybox" colspan="2">
							<s:text name="curr.tax.amnt" /> :
						</td>
						<td class="greybox" colspan="2">
							<span class="bold"> <s:property value="%{currTaxAmount}" />
							</span>
						</td>
					</tr>
					<tr>
						<td colspan="8">
							<table width="100%" border="0" align="center" cellpadding="0"
								cellspacing="0" class="tablebottom">

								<tr>
									<th class="bluebgheadtd" width="15%">
										<s:text name="Installment" />
									</th>
									<th class="bluebgheadtd" width="20%" align="center" colspan="3">
										<s:text name="Demand" />
									</th>

									<th class="bluebgheadtd" width="20%" align="center" colspan="3">
										<s:text name="Collection" />
									</th>

									<th class="bluebgheadtd" width="10%" align="center" colspan="1">
										<s:text name="Rebate" />
									</th>

									<th class="bluebgheadtd" width="20%" align="center" colspan="3">
										<s:text name="Balance" />
									</th>

									<th class="bluebgheadtd" width="35%" align="center">
										<s:text name="PaymentDetails" />
									</th>
								</tr>
								<tr>

									<td class="blueborderfortd">
										<div align="center">											
											<a href="javascript:openHeadwiseDCBWindow();"> View DCB Head wise  </a>
										</div><br/>
										<div align="center">											
											<s:if test="%{basicProperty.isMigrated == 'Y'}">
												<a href="" onclick="openNewWindow();"> Show Arrears </a>
											</s:if>	
											<s:else>
												&nbsp;
											</s:else>										
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Tax" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="chkBncPenalty" />
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="ltPmtPenalty" />
											</span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Tax" /> </span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="chkBncPenalty" />
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="ltPmtPenalty" />
											</span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold">&nbsp; </span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="Tax" /> </span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="chkBncPenalty" />
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											<span class="bold"><s:text name="ltPmtPenalty" />
											</span>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="center">
											<span class="bold">&nbsp; </span>
										</div>
									</td>
								</tr>
								<s:set value="0" var="advance" />
								<s:set value="0" var="advrebate" />

								<s:iterator value="dcbReport.getRecords()" var="dcbreportmap">
									<tr>
										<td class="blueborderfortd">
											<div align="center">
												<s:property value="%{key.getDescription()}" />
											</div>
										</td>

										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getDemands()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
										</s:iterator>
										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getCollections()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
											<c:if test="${fieldnames == 'Advance Collection'}">
												<s:set value="%{value.getCollections()[#fieldnames]}"
													var="adv" />
												<c:set value="${advance + adv}" var="advance" />
											</c:if>
										</s:iterator>

										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">

											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'PENALTY' && fieldnames != 'FINES'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getRebates()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<s:set value="value.getRebates()[#fieldnames]" var="advreb" />
												<c:set value="${advrebate + advreb}" var="advrebate" />
											</c:if>
										</s:iterator>

										<s:iterator value="dcbReport.getFieldNames()" var="fieldnames">
											<c:if
												test="${fieldnames != 'Advance Collection' && fieldnames != 'Fines'}">
												<td class="blueborderfortd">
													<div align="right">
														<s:text name="format.money">
															<s:param value="value.getBalances()[#fieldnames]" />
														</s:text>
													</div>
												</td>
											</c:if>
										</s:iterator>

										<s:set name="InstallmentKey" value="key" />
										<!-- i and j variables are used for UI purpose only-->
										<c:set value="0" var="j" />
										<c:set value="0" var="i" />
										<s:iterator value="dcbReport.getReceipts()" var="dcbreport">
											<c:set value="${j+1}" var="j" />
											<s:if test="%{key== #InstallmentKey}">
												<c:set value="${i+1}" var="i" />
												<td class="blueborderfortd">
													<s:iterator value="value" var="receipt">
														<s:if test="%{#receipt.getReceiptStatus() != 'C'}">
													    <a href="/../collection/citizen/onlineReceipt!viewReceipt.action?receiptNumber=<s:property value="#receipt.getReceiptNumber()" />&consumerCode=<s:property value="%{encodedConsumerCode}" />&serviceCode=PT" target="_blank" >
															<s:property value="#receipt.getReceiptNumber()" />
														</a>	
																dated
														<s:date name="#receipt.getReceiptDate()"
																format="dd/MM/yyyy" var="rcptdate" />
															<s:property value="rcptdate" />
																with amount Rs.
														<s:text name="format.money">
															<s:param value="#receipt.getReceiptAmt()" />
														</s:text>
															<br />
														</s:if>
													</s:iterator>
												</td>
											</s:if>

											<c:if test="${fn:length(dcbReport.receipts)==j && i==0}">
												<td class="blueborderfortd">
													&nbsp;
												</td>
											</c:if>

										</s:iterator>
									</tr>

								</s:iterator>


								<tr>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="right">
											<b><s:text name="amtDue" />:</b>
										</div>
									</td>

									<s:iterator value="dcbReport.getFieldNames()" var="FieldNames">
										<c:if
											test="${FieldNames != 'Advance Collection' && FieldNames != 'Fines'}">
											<td class="blueborderfortd">
												<div align="right">
													<span class="bold"> 
														<s:text name="format.money">
															<s:param value="dcbReport.getFieldBalanceTotals()[#FieldNames]" />
														</s:text>
													</span>
												</div>
											</td>
										</c:if>
									</s:iterator>
									<td class="blueborderfortd">
										&nbsp;
									</td>
								</tr>

								<c:if test="${advance != null && advance != 0}">
									<tr>

										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>

										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd" width="10%">
											<div align="right">
												<b><s:text name="advancePayment" />:</b>
											</div>
										</td>

										<td class="blueborderfortd">
											<div align="right">
												<c:choose>
													<c:when test="${advrebate == null || advrebate == 0}">
														<b><fmt:formatNumber value="${advance}" pattern="#,##0.00"/> </b>
													</c:when>
													<c:otherwise>
														<b><fmt:formatNumber value="${advance - advrebate}" pattern="#,##0.00"/> </b>
													</c:otherwise>
												</c:choose>
											</div>
										</td>

										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											&nbsp;
										</td>
										<td class="blueborderfortd">
											&nbsp;
										</td>
									</tr>
								</c:if>

								<c:if test="${advrebate != null && advrebate != 0}">
									<tr>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>

										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											<div align="right">
												<b><s:text name="advanceRebate" />:</b>
											</div>
										</td>

										<td class="blueborderfortd">
											<div align="right">
												<b><fmt:formatNumber value="${advrebate}" pattern="#,##0.00"/></b>
											</div>
										</td>

										<td class="blueborderfortd">
											<div align="center">
												&nbsp;
											</div>
										</td>
										<td class="blueborderfortd">
											&nbsp;
										</td>
										<td class="blueborderfortd">
											&nbsp;
										</td>
									</tr>
								</c:if>

								<tr>

									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="right">
											<b><s:text name="balanceDue" />:</b>
										</div>
									</td>

									<td class="blueborderfortd">
										<div align="right">
											<span class="bold"> 
												<s:text name="format.money">
													<s:param value="dcbReport.getTotalBalance()" />
												</s:text>
											</span>
										</div>
									</td>
									<td class="blueborderfortd">
										<div align="center">
											&nbsp;
										</div>
									</td>
									<td class="blueborderfortd">
										&nbsp;
									</td>
									<td class="blueborderfortd">
										&nbsp;
									</td>
								</tr>
								<s:if
									test="%{getCancelledReceipts() != null && !getCancelledReceipts().isEmpty()}">
									<table width="100%" border="0" align="center" cellpadding="0"
												cellspacing="0" class="tablebottom">
									<tr>
										<td align="center">
											<div class="headingsmallbg">
												<s:text name="rcptHeader" />
											</div>
										</td>
									</tr>

									<tr>
										<td align="center">
											<table width="100%" border="0" align="center" cellpadding="0"
												cellspacing="0" class="tablebottom">
		
												<tr>
													<th class="bluebgheadtd">
														<s:text name="receiptNo" />
													</th>
													<th class="bluebgheadtd">
														<s:text name="receiptDate" />
													</th>
													<th class="bluebgheadtd">
														<s:text name="totalAmount" />
													</th>
												</tr>
		
												<s:iterator value="getCancelledReceipts()" var="rcpt">
		
													<tr>
														<td class="blueborderfortd">
															<div align="center">
																<s:property value="#rcpt.getReceiptNumber()" />
															</div>
														</td>
														<td class="blueborderfortd">
															<div align="center">
																<s:property value="#rcpt.getReceiptDate()" />
															</div>
														</td>
														<td class="blueborderfortd">
															<div align="center">
																<s:property value="#rcpt.getReceiptAmt()" />
															</div>
														</td>
													</tr>
												</s:iterator>
											</table>
										</td>
									</tr>
									</table>
								</s:if>												
							</table>				
							</td>
							</tr>
							</s:if>		
							</table>
							<div class="buttonbottom" align="center">
									<s:if test="%{errorMessage != null}">
										<div align="center" style="font-size:15px; color:red">
											<s:property value="%{errorMessage}" /><br><br>
											<s:text name="msg.activeDemand" />
										</div>			
									</s:if>
									<s:else>
										<div align="center">
											<s:checkbox name="taxEnsureCheckbox" id="taxEnsureCheckbox" onclick="switchPayTaxButton(this);" required="true" />
											<span style="font-size:15px; color:red ">										
												<s:text name="msg.payBill.verification" /> <br><br>
												<s:if test="basicProperty.isDemandActive == true">
													<s:text name="msg.activeDemand" />	
												</s:if>
												<s:else>
													<s:text name="getText('msg.inactiveDemand' , {demandEffectiveYear, noOfDaysForInactiveDemand})" />
												</s:else>
																						
											</span> 
										</div><br>
										<div align="center">
											<s:if test="%{isCitizen()}">
												<input type="button" name="PayTax" id="PayTax" value="Pay Tax" class="button"
															onclick="window.location='../citizen/collection/collection!generatePropertyTaxBill.action?indexNum=<s:property value="%{propertyId}" />';" />
											</s:if> 
											<s:else>
											<table>
												<tr>
													<td align="center">
															<input type="button" name="display" id="operatorPayBill"
																value="Pay Bill" class="button"
																onclick="setLevyPenalyBeforeSubmit();" />
														</td>
												</tr>
											</table>
										</s:else>
										</div>
									</s:else><br> 
									<input type="button" name="button2" id="button2" value="Close"
										class="button" onclick="return confirmClose();" />
									<input type="button" name="button2" id="button2" value="Back"
										class="button" onclick="history.back(-1)" />
							</div>
					</s:form>
				</div>
	</body>

</html>
