#UP
update EGF_BUDGETDETAIL
set BUDGETAVAILABLE=30000000
where EXECUTING_DEPARTMENT=(SELECT id_dept FROM EG_DEPARTMENT WHERE dept_name='BR-Bus Route Roads')
and FUNCTION=(SELECT id FROM FUNCTION WHERE code='0201')
and FUND=(SELECT id FROM FUND WHERE code='0102');

update EGF_BUDGETDETAIL
set BUDGETAVAILABLE=15000000
where EXECUTING_DEPARTMENT=(SELECT id_dept FROM EG_DEPARTMENT WHERE dept_name='BR-Bus Route Roads')
and FUNCTION=(SELECT id FROM FUNCTION WHERE code='0101')
and FUND=(SELECT id FROM FUND WHERE code='0102');
#DOWN