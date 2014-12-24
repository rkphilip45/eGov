<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="sx" uri="/WEB-INF/struts-jquery-tags.tld"%>
<html>
	<head>
		<title><s:text name="page.title.renewtrade" /></title>
		<sx:head />
		<script>
			function closethis() {
				if (confirm("Do you want to close this window ?")) {
					window.close();
				}
			}
			
			function printthis() {
				if (confirm("Do you want to print this screen ?")) {
					var html="<html>";
					html+= document.getElementById('content').innerHTML;
					html+="</html>";
					
					var printWin = window.open('','','left=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
					printWin.document.write(html);
					printWin.document.close();
					printWin.focus();
					printWin.print();
					printWin.close();
				}		
			}
		</script>
	</head>
	<body>
		<div id="content">
			<table align="center" width="100%">
				<tbody>
					<tr>
						<td>
							<div align="center">
								<center>
									<div class="formmainbox">
										<div class="headingbg" id="headingdiv">
											<s:text name="page.title.renewtrade" />
										</div>
										<table>
											<tr>
												<td align="left" style="color: #FF0000">
													<s:actionerror cssStyle="color: #FF0000" />
													<s:fielderror />
													<s:actionmessage />
												</td>
											</tr>
										</table>
										<s:form action="newTradeLicense" theme="css_xhtml" name="renewForm">
										<s:token/>
											<s:push value="model">
												<s:hidden name="docNumber" />
                        <s:hidden name="model.id"/>
												<s:hidden id="detailChanged" name="detailChanged"></s:hidden>
												<c:set var="trclass" value="greybox" />
												<table width="100%">
													<%@ include file='../../common/view.jsp'%>
													<c:choose>
														<c:when test="${trclass=='greybox'}">
															<c:set var="trclass" value="bluebox" />
														</c:when>
														<c:when test="${trclass=='bluebox'}">
															<c:set var="trclass" value="greybox" />
														</c:when>
													</c:choose>
													<tr>
														<td class="<c:out value="${trclass}"/>" width="5%">
															&nbsp;
														</td>
														<td class="<c:out value="${trclass}"/>">
															<b><s:text name="license.motor.installed" />
															</b>
														</td>
														<td class="<c:out value="${trclass}"/>">
															<s:if test="%{motorInstalled}">
																<s:text name="Yes" />
															</s:if>
															<s:else>
																<s:text name="No" />
															</s:else>
														</td>
														<c:choose>
		<c:when test="${docNumber!=null && docNumber!='' }">
		<td class="<c:out value="${trclass}"/>" colspan="5">
		<a href="/egi/docmgmt/basicDocumentManager!viewDocument.action?moduleName=egtradelicense&docNumber=${docNumber}" target="_blank">View Attachments</a>
		</td>
		</c:when>
		<c:otherwise>
		<td class="<c:out value="${trclass}"/>" colspan="2">
		</c:otherwise>
		</c:choose>
													</tr>
													
													<s:if test="%{motorInstalled}">
														<tr>
															<td colspan="8">
																<%@ include file='../../common/motordetailsview.jsp'%>
															</td>
														</tr>
													</s:if>
                          <tr>
		                        <td colspan="5" class="headingwk">
                          			<div class="arrowiconwk">
                          				<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20"/>
                          			</div>
                          			<div class="headplacer">
                          				<s:text name='license.title.otherdetail' />
                          			</div>
                          		</td>
                          	</tr>
                          <tr>
                          <td class="<c:out value="${trclass}"/> width="5%"></td>
                          <td class="<c:out value="${trclass}"/>">
                            <s:text name='license.othercharges' />
                          </td>
                          <td class="<c:out value="${trclass}"/>">
                            <s:textfield name="otherCharges" onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,8),formatCurrency(otherCharges)" />
                          </td>
                          <td class="<c:out value="${trclass}"/>">
                            <s:text name='license.deduction' />
                          </td>
                          <td class="<c:out value="${trclass}"/>" <s:textfield name="deduction"  onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,8),formatCurrency(deduction)" /></td>
                        </tr>
                        <c:choose>
							<c:when test="${trclass=='greybox'}">
								<c:set var="trclass" value="bluebox" />
							</c:when>
							<c:when test="${trclass=='bluebox'}">
								<c:set var="trclass" value="greybox" />
							</c:when>
						</c:choose>
						<tr>
						<td class="<c:out value="${trclass}"/> width="5%"></td>
							<td class="<c:out value="${trclass}"/>">
								<s:text name='license.swmfee' />
							</td>
							<td class="<c:out value="${trclass}"/>">
								<s:textfield name="swmFee" maxlength="8" onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,8),formatCurrency(swmFee)" />
							</td>
							<td colspan="4" class="<c:out value="${trclass}"/> width="5%"></td>
						</tr>
												</table>
                          <div>
                      <table>
                        <tr class="buttonbottom" id="buttondiv" style="align: middle">
                          <td>
                            <s:submit type="submit" cssClass="buttonsubmit" value="Save" id="Save" method="renew"  />
                          </td>
                          <td>
                            <input type="button" value="Close" onclick="javascript:window.close()" class="button" />
                          </td>
                        </tr>
                      </table>
                    </div>
											</s:push>
										</s:form>
									</div>
								</center>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
										<div class="mandatory" style="font-size: 11px;" align="left">
											* Mandatory Fields
										</div>
	</body>
</html>