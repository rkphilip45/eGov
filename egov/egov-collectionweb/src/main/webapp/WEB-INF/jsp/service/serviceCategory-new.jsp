<%@ include file="/includes/taglibs.jsp" %>
<html>  
<head> 

     <title><s:text name="add.title.serviceCategory"/></title>  
</head>  
	<body>  
		<div class="subheadnew"><s:text name="add.title.serviceCategory"/></div>
		<span>
		<font  style='color: red ; font-weight:bold ' size="2">
                               <s:actionerror/>  
                               <s:fielderror />
                               </font>
          </span>
		
		<s:form action="serviceCategory" theme="simple" >  
		
			<%@ include file='serviceCategory-form.jsp'%>
				
			<div align="left" class="mandatory">* Mandatory Fields</div>
	 		
	 		<div class="buttonbottom">
	 			<s:submit name="button1" cssClass="buttonsubmit" id="button32" method="create" value="Save"/>
				<input name="button2" type="button" class="buttonsubmit" id="button" onclick="location.href='serviceCategory.action'" value="List"/>
				<s:reset name="button3" cssClass="button" id="button" value="Cancel"/>
				<input name="button4" type="button" class="button" id="button" onclick="window.close()" value="Close"/>
			</div>

 		</s:form>  
	    </body>  
</html>