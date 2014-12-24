<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>

<html>
<head>
<title><s:text name='page.title.estimate.template.search'/></title>
<script type="text/javascript">

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatesubType({category:categoryId});
}

loadSubType= function(req, res){
	if(dom.get('subTypeOfWork').value != ""){
		dom.get('subType').value=dom.get('subTypeOfWork').value;
	}
}

function submitEstimateTemplateSearchForm() {
	document.estimateTemplateSearchForm.status.disabled=false;
    document.estimateTemplateSearchForm.action='${pageContext.request.contextPath}/estimate/estimateTemplate!searchDetails.action';
    dom.get('estimateTemplateCode').value="";
	dom.get('typeOfWork').value=""; 
	dom.get('subTypeOfWork').value="";
    document.estimateTemplateSearchForm.submit();
}

function bodyOnLoad(){
	if(dom.get('estimateTemplateCode').value != ""){
		dom.get('code').value=dom.get('estimateTemplateCode').value;  
	}
	if(dom.get('typeOfWork').value != ""){
		dom.get('workType').value=dom.get('typeOfWork').value;
		populatesubType({category:dom.get('workType').value});
	}
	 <s:if test="%{sourcePage.equals('searchForEstimate')}">
	 	document.estimateTemplateSearchForm.status.value=1;
	 	document.estimateTemplateSearchForm.status.disabled=true;
	 </s:if>
}
</script>
</head>
<body onload="bodyOnLoad()" class="yui-skin-sam">

<script src="<egov:url path='js/works.js'/>"></script>
   <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="errorstyle" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
   </s:if>
   <s:if test="%{hasActionMessages()}">
       <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
       </div>
   </s:if>
<s:form theme="simple" name="estimateTemplateSearchForm" onsubmit="return validateDataBeforeSubmit(this);">
  <s:hidden name="estimateTemplateCode" id="estimateTemplateCode" />
  <s:hidden name="typeOfWork" id="typeOfWork" />
  <s:hidden name="subTypeOfWork" id="subTypeOfWork" />
<div class="errorstyle" id="estimateTemplate_error"
				style="display: none;"></div>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	<div class="rbcontent2"><div class="datewk">
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
				<td colspan="4">&nbsp;</td>
			 </tr>
			 <tr>
				<td colspan="4">&nbsp;</td>
			 </tr>
			 <tr>
			 <td colspan="4" class="headingwk" align="left">
				<div class="arrowiconwk">
				  <img src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
				  <s:text name='title.search.criteria' />
				</div>
			  </td>
			 </tr>
             <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="estimate.template.search.type" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.template.search.default.select')}" name="workType" id="workType" cssClass="selectwk" list="dropdownData.parentCategoryList" listKey="id" listValue="description" value="%{workType.id}" onChange="setupSubTypes(this);"/>
                <egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']" dropdownId='subType' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{subType.id}" afterSuccess="loadSubType" />
                </td>
                <td class="whiteboxwk"><s:text name="estimate.template.search.subtype" />:</td>
                <td class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.template.search.default.select')}" name="subType" value="%{subType.id}" id="subType" cssClass="selectwk" list="dropdownData.categoryList" listKey="id" listValue="description"/></td>
              </tr>
               <tr>
                <td width="11%" class="greyboxwk"><s:text name="estimate.template.search.code" />:</td>
                <td width="21%" class="greybox2wk"><s:textfield name="code" value="%{code}" id="code" cssClass="selectwk" />
                <td class="greyboxwk"><s:text name="estimate.template.search.description" />:</td>
                <td class="greybox2wk"><s:textarea name="description" cols="35" cssClass="selectwk" id="description" value="%{description}"/></td>
               </tr>
               <tr>
                <td class="whiteboxwk"><s:text name="estimate.template.search.name" />:</td>
                <td class="whitebox2wk"><s:textarea name="name" cols="35" cssClass="selectwk" id="name" value="%{name}"/></td>
                <td width="15%" class="whiteboxwk"><s:text name="estimate.template.search.status" />:</td>
                <td width="53%" class="whitebox2wk"><s:select headerKey="0"  list="#{'0':'INACTIVE', '1':'ACTIVE'}"  name="status"  value="%{status}" id="status" cssClass="selectwk"/> </td>
               </tr>
               <tr>
                <td  colspan="4" class="shadowwk"> </td>               
               </tr>
               <tr><td>&nbsp;</td></tr>			
               <tr>
                 <td colspan="4"> 
                   <div class="buttonholderwk">
		             <p>
			           <input type="button" class="buttonadd" value="SEARCH" id="searchButton" name="button" onclick="submitEstimateTemplateSearchForm()" />&nbsp;
			           <input type="button" class="buttonfinal" value="RESET" id="resetbutton" name="clear" onclick="this.form.reset();">&nbsp;
		               <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" />
	                 </p>
		          </div>
                </td>
              </tr>
              <tr>
				<td colspan="4" align="left">
					 <table width="100%" border="0" cellspacing="0" cellpadding="0">
						 <tr>
							<td class="headingwk">
								<div class="arrowiconwk">
									<img src="${pageContext.request.contextPath}/image/arrow.gif" />
								</div>
								<div class="headplacer">
									<s:text name="estimate.template.searh.result.title" />
								</div>
							</td>
						 </tr>
                      </table> 
                 </td>
               </tr> 
     </table>               
     </div>
        <%@ include file='estimateTemplate-searchResults.jsp'%> 	
	 <div class="rbbot2"><div></div></div>
</div>
</div>
</div>
<s:hidden name="sourcePage" id="sourcePage"
									value="%{sourcePage}" />
</s:form>
</body>

</html>