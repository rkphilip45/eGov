<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<script>
function setupAjaxAssettype(elem){
    asset_type_id=elem.options[elem.selectedIndex].value;
   	populateparentcat({assetTypeId:asset_type_id});
}

window.onload = function () {
    <s:if test="fromDiaryModule">
    	document.getElementById("assettype").disabled = true;
	</s:if>
	<s:else>
		document.getElementById("assettype").disabled = false;
	</s:else>
}

</script>
<html>
	<head>
	    <title>- <s:text name="page.title.asset.search" /></title>
	</head>
	<body>

		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
		</s:if>
		<s:form action="asset" theme="simple"
			name="assetSearchPluginForm">
			<div class="errorstyle" id="search_error" style="display:none;"></div>
			<div class="navibarshadowwk">
			</div>
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2">
			</div>
			<div class="rbcontent2">
			<s:hidden name="userMode" />
			<s:hidden name="rowId" id="rowid"/>
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table id="assetSearchTable" width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name="search.criteria" />
									</div>
								</td>
							</tr>
							<s:if test="fromDiaryModule">
								<s:hidden name="catTypeId" value="%{catTypeId}"/>
							</s:if>
							<s:hidden name="fromDiaryModule" value="%{fromDiaryModule}"/>
							<tr>
								<td width="11%" class="whiteboxwk">
									<s:text name="asset.cat.type" />:
								</td>
								<td width="21%" class="whitebox2wk">
									<s:select headerKey=""
										headerValue="%{getText('list.default.select')}"
										name="catTypeId" id="assettype"
										cssClass="selectwk" list="dropdownData.assetTypeList"
										listKey="id" listValue='name'
										value="%{catTypeId}"
										onChange="setupAjaxAssettype(this);" />
									<egov:ajaxdropdown id="populateParentcat"
										fields="['Text','Value']" dropdownId='parentcat'
										url='assetmaster/ajaxAsset!populateParentCategories.action'
										selectedValue="%{id}" />
								</td>
								<td width="15%" class="whiteboxwk">
									<s:text name="asset.cat.name" />:
								</td>
								<td width="53%" class="whitebox2wk">
									<s:select headerKey=""
										headerValue="%{getText('list.default.select')}" name="parentId"
										id="parentcat" cssClass="selectwk"
										list="dropdownData.assetCategoryList" listKey="id" value="%{parentId}"
										listValue='name'/>
								</td>			
							</tr>
							<tr>	
								<td width="11%" class="greyboxwk">
								<s:text name="asset.department" />:</td>
								<td width="21%" class="greybox2wk">
									<s:select id="dept" name="departmentId" cssClass="selectwk"
									list="dropdownData.departmentList" listKey="id" listValue="deptName" 
									headerKey="" headerValue="%{getText('list.default.select')}"/>		
								</td>
								<td width="15%" class="greyboxwk" id="tdstatlabel">
									<span class="mandatory">*</span>
									<s:text name="asset.status" />:
								</td>
								<td width="53%" class="greybox2wk" id="tdstatus">				
									<s:select id="status" name="statusId" cssClass="selectwk" 		 
									list="dropdownData.statusList" listKey="id" listValue='description'
									multiple="true" size="3"/>
								</td>
								<td width="53%" class="greybox2wk" id="tdstatbr" colspan="2">				
									</br>
								</td>
							</tr>
							<tr>	
								<td width="11%" class="whiteboxwk">
									<s:text name="asset.code" />:
								</td>
								<td width="21%" class="whitebox2wk">
									<s:textfield name="code" id="code" cssClass="selectboldwk"/>								
								</td>
								<td width="15%" class="whiteboxwk">
									<s:text name="asset.description" />:
								</td>
								<td width="53%" class="whitebox2wk">
								<s:textfield name="description" id="description" cssClass="selectboldwk"/>	
								</td>		  
							</tr>
							<tr>
								<td width="11%" class="greyboxwk">
									<s:text	name="asset.location.ward" />:
								</td>
								<td width="21%" class="greybox2wk" colspan="3">
									<s:select id="wardId" name="locationId" cssClass="selectwk"
										list="dropdownData.wardsList" listKey="id" listValue="name" 
										headerKey="" headerValue="%{getText('list.default.select')}"
										value="%{locationId}"/>	
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<div align="right" class="mandatory"
										style="font-size: 11px; padding-right: 20px;">
										*
										<s:text name="default.message.mandatory" />
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
										<s:submit cssClass="buttonfinal" value="SEARCH" id="searchButton" method="showSerachResult"/>
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="4">
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
													<s:text name="search.result" />
												</div>
											</td>
										</tr>
										<tr>
											<td width="10%" class="tablesubheadwk">
												Sl No
											</td>
											<td width="15%" class="tablesubheadwk">
												Code
											</td>
											<td width="15%" class="tablesubheadwk">
												Name
											</td>
											<td width="15%" class="tablesubheadwk">
												Category
											</td>
											<td width="15%" class="tablesubheadwk">
												Department
											</td>
											<td width="15%" class="tablesubheadwk">
												Ward
											</td>
											<td width="15%" class="tablesubheadwk">
												Status
											</td>
											
										</tr>
										<s:if test="%{assetList.size != 0}">
											<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<s:iterator id="assetIterator" value="assetList"
														status="row_status">
														<tr onmousedown="" onmouseover="changeColor(this, true);"
															onmouseout="changeColor(this, false);" href="#"
															onclick="javascript:returnBackToParent('<s:property value="id"/>','<s:property value="code"/>','<s:property value="name"/>', '<s:property value="assetCategory.name"/>')"
															id="getassresult" style="cursor: hand">
															<!-- <td width="10%"><s:property value="%{id}" /> </td> -->
															<td width="10%"">
																<s:property value="#row_status.count" />
																&nbsp;
															</td>
															<td width="15%">
																<s:property value="%{code}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{name}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{assetCategory.name}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{department.deptName}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{ward.name}" />
																&nbsp;
															</td>
															<td width="15%"">
																<s:property value="%{status.description}" />
																&nbsp;
															</td>
															
														</tr>
													</s:iterator>
												</table>
										</s:if>
										<s:elseif test="%{assetList.size == 0}">
											<tr>
												<td colspan="7" align="center">
													<font color="red">No record Found.</font>
												</td>
											</tr>
										</s:elseif>
									</table>
								</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
			</table>
			</div>
			<div class="rbbot2">
			</div>
			</div>
			</div>
			</div>
		</s:form>
		<script type="text/javascript">
			
			$('tdstatus').show();
			$('tdstatlabel').show();
			$('tdstatbr').hide();
			
			function returnBackToParent(id,code,name,category) {
				var wind;
				var data = new Array();
				row_id = $('rowid').value;
				wind=window.dialogArguments;
				if(wind==undefined){
					wind=window.opener;
					data = row_id + '`~`' + id + '`~`' + code + '`~`' + name + '`~`' + category;
					window.opener.update(data);
				}
		
				else{
					wind=window.dialogArguments;
					wind.result = row_id + '`~`' + id + '`~`' + code + '`~`' + name + '`~`' + category;
				}
				window.close();
			}

			function changeColor(tableRow, highLight)
			{
				if (highLight)
				{
				  tableRow.style.backgroundColor = '#dcfac9';
				}
				else
				{
				  tableRow.style.backgroundColor = 'white';
				}
			}
			
		</script>
	</body>
</html>
