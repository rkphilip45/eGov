<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
                              
                           <div>
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr>
												<td colspan="7" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="title.search.result" />
													</div>
												</td>
											</tr>
										</table>

                                    <s:if test="%{searchResult.fullListSize != 0}">
                                        <display:table name="searchResult" pagesize="30"
											uid="currentRow" cellpadding="0" cellspacing="0"
											requestURI=""
											style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
										
										<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:4%;text-align:center">						
											<input name="radio" type="radio" id="radio"
												value="<s:property value='%{#attr.currentRow.id}'/>"
												onClick="setworkorderId(this);" />
										</display:column>
                                        
                                              <display:column headerClass="pagetableth"
												class="pagetabletd" title="Sl.No"
												titleKey="column.title.SLNo"
												style="width:2%;text-align:right" >
												<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
												</display:column>
                                            
                                             <display:column headerClass="pagetableth"
												class="pagetabletd" title="Workorder Number"
												titleKey="workorder.search.workordernumber"
												style="width:10%;text-align:left">
                                                <s:property  value='%{#attr.currentRow.workOrderNumber}' />
                                                <s:hidden name="workorderId" id="workorderId" value="%{#attr.currentRow.id}" />
									   <s:hidden name="workOrderStateId" id="workOrderStateId" value="%{#attr.currentRow.state.id}" />
									   <s:hidden name="docNumber" id="docNumber" value="%{#attr.currentRow.documentNumber}" />
									   <s:hidden id="objNo" name="objNo" value="%{#attr.currentRow.workOrderNumber}" />
                                            </display:column>
                                          
                                            <display:column headerClass="pagetableth"
												class="pagetabletd" title="Work Order Date"
												titleKey="workorder.search.workorderdate"
												style="width:6%;text-align:left" >
									  <s:date name="#attr.currentRow.workOrderDate" format="dd/MM/yyyy" />
                                               <s:hidden id="appDate" name="appDate" 	value="%{#attr.currentRow.workOrderDate}" />
                                            </display:column>
                                         
                                            <display:column headerClass="pagetableth"
												class="pagetabletd" title="Contractor"
												titleKey="workorder.search.contractor"
												style="width:15%;text-align:left" property="contractor.name" />
                                              
                                            <display:column headerClass="pagetableth"
												class="pagetabletd" title="Owner"
												titleKey="workorder.search.owner"
												style="width:10%;text-align:left" property="owner" />
                                         
                                           <display:column headerClass="pagetableth"
												class="pagetabletd" title="Status"
												titleKey="workorder.search.status"
												style="width:8%;text-align:left" property="status" />
							
							      <display:column headerClass="pagetableth"
												class="pagetabletd" title="Total"
												titleKey="workorder.search.workordervalue"
												style="width:10%;text-align:right" >
                                            <s:text name="contractor.format.number" >
								   	 <s:param name="rate" value='%{#attr.currentRow.workOrderAmount}' />
								   </s:text>
                                           </display:column>
                                   </display:table> 
                               </s:if> 
                               <s:elseif test="%{searchResult.fullListSize == 0}">
											<div>
												<table width="100%" border="0" cellpadding="0"
													cellspacing="0">
													<tr>
														<td align="center">
															<font color="red">No record Found.</font>
														</td>
													</tr>
												</table>
											</div>
					 </s:elseif>  
</div> 
                       		