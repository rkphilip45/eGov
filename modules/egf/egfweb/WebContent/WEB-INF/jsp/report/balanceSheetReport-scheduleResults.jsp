<%@ include file="balanceSheetReport-form.jsp" %>
<div class="buttonbottom"><s:text name="report.export.options"/>: <a href='/EGF/report/balanceSheetReport!generateBalanceSheetScheduleXls.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.asOndate=<s:property value="model.asOndate"/>&majorCode=<s:property value="#parameters['majorCode']" />'>Excel</a> | <a href='/EGF/report/balanceSheetReport!generateBalanceSheetSchedulePdf.action?showDropDown=false&model.period=<s:property value="model.period"/>&model.currency=<s:property value="model.currency"/>&model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.asOndate=<s:property value="model.asOndate"/>&majorCode=<s:property value="#parameters['majorCode']" />'>PDF</a></div>