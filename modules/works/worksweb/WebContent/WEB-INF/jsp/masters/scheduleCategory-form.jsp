<script>
function setFocus(obj,defaultval)
{
	if(obj.value==defaultval ){
		document.getElementById(obj.id).value="";
		document.getElementById(obj.id).style.color='#000000';
		}
}
function setBlur(obj,defaultval)
{
  if(obj.value == ""){
		document.getElementById(obj.id).value=defaultval;
				document.getElementById(obj.id).style.color='#999999';

	}
}
function methodTest()
{
  if(document.getElementById("code").value=="Category code"){
		document.getElementById("code").value="";
		
		
	}
if(document.getElementById("description").value=="Category Name"){
		document.getElementById("description").value="";
		
	}

}
</script>
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2">
	<div></div>
	</div>
	<div class="rbcontent2">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="4" class="headingwk">
					<div class="arrowiconwk"><img
						src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
					<div class="headplacer">SOR Category</div>
					</td>
				</tr>
				<td class="aligncenter">
				<table align="center" width="300" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td width="42%" class="tablesubheadwka">Category code</td>
						<td width="42%" class="tablesubheadwka">Category Name</td>

					</tr>
			</table>
				<% 
							int count=0;
						%>
					<s:iterator var="p" value="scheduleCategoryList" > 
						<% 
							count++;
						%>
					</s:iterator>
				<% if(count>10){ %>

				<div class="findscroller">
				<%}%>
				<table align="center" width="300" border="0" cellpadding="0"
					cellspacing="0">
					<s:iterator var="p" value="scheduleCategoryList">
						<tr>
							<td width="42%" class="whitebox3wka"><s:property value="%{code}" /></td>
							<td width="42%" class="whitebox3wka"><s:property value="%{description}" />
							</td>
						</tr>
					</s:iterator>
				</table>
				</td>

				<tr>
					<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="30%" class="greyboxwk"><span class="bold">Add
							SOR Category:</span></td>

							<td width="70%" class="greybox2wk"><s:textfield
								cssClass="selectwk grey" name="code" maxlength="15" id="code" value ="Category code" size="40" onfocus="setFocus(this,'Category code')" onblur="setBlur(this,'Category code')"/> <span
								cssClass="buttonholderwk"> <s:textfield
								cssClass="selectwk grey" name="description" maxlength="150" id="description" value = "Category Name"  size="40" onfocus="setFocus(this,'Category Name')" onblur="setBlur(this,'Category Name')"/> <s:submit value="Save" cssClass="buttonfinal" value="SAVE" id="saveButton" name="button" method="create" onclick="methodTest();"/> </span></td>
						</tr>
					</table>
					</td>
				     </tr>
				    <tr> 
				  <td class="shadowwk"></td> 
                		</tr> 
			</table>
			</td>
		</tr>
	</table>
	<% if(count>10){ %>

				</div>
				<%}%>
	
	</div>
	<div class="rbbot2">
	<div></div>
	</div>
	</div>
	</div>
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton"
		name="closeButton" onclick="window.close();" />