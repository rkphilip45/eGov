<%@ include file="/includes/taglibs.jsp"%>
<html>

    <head>
        <title><s:text name="tradelicense.reports.wardwise.title" /></title>
    </head>

    <body>
        <s:form action="#" theme="simple">
            <s:token />
        <div class="subheadnew">
            <s:text name="tradelicense.reports.wardwise.subtitle" />
        </div>

        
      <s:iterator value="%{totalList}" status="rowTotal">
      <table style="background-color:#e8edf1;width:98%;height:120px;padding:0px;margin:10 0 0 5px;">
      <tr><td class="bluebox"><s:text name="totalnewlicenses" /></td><td  class="bluebox" style="text-align:right;" ><s:property value="%{TOTAL_NEW}"/></td></tr>
      <tr><td class="greybox"><s:text name="totalrenewedlicenses" /></td><td class="greybox" style="text-align:right;" ><s:property value="%{TOTAL_RENEWED}"/></td></tr>
        <tr><td class="bluebox"><s:text name="totalpendingrenewallicenses" /></td><td class="bluebox" style="text-align:right;" ><s:property value="%{TOTAL_PENDING}"/></td></tr>
      <tr><td class="greybox"><s:text name="totalobjectedlicenses" /></td><td class="greybox" style="text-align:right;" ><s:property value="%{TOTAL_OBJ}"/></td></tr>
      <tr><td class="bluebox"><s:text name="totalcancelledlicenses" /></td><td class="bluebox" style="text-align:right;" ><s:property value="%{TOTAL_CAN}"/></td></tr>
      <tr><td class="greybox"><s:text name="totalissuedlicenses" /></td><td class="greybox" style="text-align:right;" ><s:property value="%{TOTAL_ISSUED}"/></td></tr>
       <tr><td class="bluebox"><s:text name="totalamountcollected" /></td><td class="bluebox" style="text-align:right;" ><s:property value="%{TOTAL_AMT}"/></td></tr></table>
      </s:iterator>
        <logic:empty name="paginateList">
            <s:actionerror />


        </logic:empty>

        <logic:notEmpty name="paginateList">

            <display:table name="paginateList" uid="currentRowObject"
                decorator="org.displaytag.decorator.TotalTableDecorator"
                cellpadding="0" cellspacing="0" export="true" id="reportsmodule"
                style="background-color:#e8edf1;width:98%;height:120px;padding:0px;margin:10 0 0 5px;"
                requestURI="" >
				
	<display:caption> &nbsp;</display:caption>
                <display:column class="blueborderfortd" title="WARD"
                    style="text-align:center;width:10%"  property="WARD" />

                <display:column class="blueborderfortd" title="New"
                    style="text-align:right;width:10%" property="NEW" />

                <display:column class="blueborderfortd" title="Renewed"
                    style="text-align:right;width:10%" property="RENEWED" />

                <display:column class="blueborderfortd" title="Pending Renewals"
                    style="text-align:right;width:10%" property="PENDING_RENEWALS" />

                <display:column class="blueborderfortd" title="Objections"
                    style="text-align:right;width:10%" property="OBJECTED" />

                <display:column class="blueborderfortd" title="Cancelled"
                    style="text-align:right;width:10%" property="CANCELLED" />

                <display:column class="blueborderfortd" title="Total Licenses Issued"
                    style="text-align:right;width:10%" property="TOTAL_LICENSES" />

                <display:column class="blueborderfortd" title="Total Amount Collected"
                    style="text-align:right;width:10%" property="TOTAL_AMOUNT" format="{0, number, ###0.00}" />

                <display:setProperty name="export.csv" value="false" />
                <display:setProperty name="export.excel" value="true" />
                <display:setProperty name="export.xml" value="false" />
                <display:setProperty name="export.pdf" value="true" />
                <display:setProperty name="export.pdf.filename" value="tradeLicense-wardwiseLicenses.pdf" />
		<display:setProperty name="export.excel.filename" value="tradeLicense-wardwiseLicenses.xls" />
            </display:table>

        </logic:notEmpty>

        <div class="buttonbottom">
        <input name="button2" type="button" class="button" id="button"
            onclick="window.close()" value="Close" />
        </div>

        </s:form>
    </body>
</html>