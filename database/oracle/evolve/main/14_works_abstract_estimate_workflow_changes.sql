#UP

DELETE FROM EG_WF_MATRIX WHERE OBJECTTYPE='AbstractEstimate';
INSERT INTO EG_WF_MATRIX
   (ID, OBJECTTYPE, DEPARTMENT, ADDITIONALRULE, CURRENTSTATE, NEXTSTATE, NEXTACTION,NEXTSTATUS, NEXTDESIGNATION, FROMDATE, FROMQTY, VALIDACTIONS)
 VALUES
   (EG_WF_MATRIX_SEQ.nextVal,'AbstractEstimate', 'BR-Bus Route Roads', 'budgetApp', 'NEW','CREATED', 'Pending Admin Sanction', 'CREATED', 'EXECUTIVE ENGINEER', sysdate, 0, 'Forward,Cancel');

INSERT INTO EG_WF_MATRIX
   (ID, OBJECTTYPE, DEPARTMENT,  ADDITIONALRULE, CURRENTSTATE, NEXTSTATE, NEXTACTION,NEXTSTATUS, NEXTDESIGNATION, FROMDATE, FROMQTY, VALIDACTIONS)
 VALUES
   (EG_WF_MATRIX_SEQ.nextVal,'AbstractEstimate', 'BR-Bus Route Roads', 'budgetApp', 'REJECTED','CREATED', 'Pending Admin Sanction', 'CREATED', 'EXECUTIVE ENGINEER', sysdate, 0, 'Forward,Cancel');

INSERT INTO EG_WF_MATRIX
   (ID, OBJECTTYPE, DEPARTMENT, ADDITIONALRULE, CURRENTSTATE,PENDINGACTIONS,NEXTSTATE, NEXTACTION,NEXTSTATUS, FROMDATE, FROMQTY, VALIDACTIONS)
 VALUES
   (EG_WF_MATRIX_SEQ.nextVal, 'AbstractEstimate', 'BR-Bus Route Roads', 'budgetApp', 'CREATED','Pending Admin Sanction','ADMIN_SANCTIONED', 'END', 'ADMIN_SANCTIONED', sysdate, 0, 'Approve,Reject');

INSERT INTO EG_USER ( ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,ID_DEPARTMENT, LOCALE, USER_NAME, PWD,
PWD_REMINDER, UPDATETIME, UPDATEUSERID, EXTRAFIELD1,EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO,
ISACTIVE, FROMDATE, TODATE ) VALUES ( 
SEQ_EG_USER.nextval, NULL, 'MR.', 'AEETEST', NULL, NULL, NULL, (select id_dept from eg_department where dept_name = 'BR-Bus Route Roads'), NULL, 
'AEETEST', 'DTRJto+XfKQ=', '804MQU', sysdate, (select ID_USER from eg_user where USER_NAME like 'egovernments'), 
NULL, NULL, NULL, NULL, 'N', 1, NULL, 1, TO_Date( '04/01/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '05/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 

INSERT INTO EG_USERROLE ( ID_ROLE, ID_USER, ID, FROMDATE, TODATE,IS_HISTORY ) VALUES ( 
(select ID_ROLE from EG_ROLES where ROLE_NAME like 'Works User'), (select ID_USER from eg_user where USER_NAME like 'AEETEST'), SEQ_EG_USERROLE.nextval,  TO_Date( '04/01/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  null, 'N'); 

INSERT INTO EG_POSITION (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE) Values
   ('WorksUser_01', seq_pos.nextval, 1, 0, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME = 'ASSISTANT EXECUTIVE ENGINEER'), TO_DATE('04/01/2013 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
   
INSERT INTO EG_EMPLOYEE(ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, EMPLOYMENT_STATUS, ID_USER, EMP_FIRSTNAME, ISACTIVE, CODE, CREATED_BY, LASTMODIFIED_DATE)
 Values (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, 'M', 'N', 'N', 1, (select id_user from eg_user where user_name = 'AEETEST'), 'AEETEST', 1, 1, (select id_user from eg_user where user_name = 'egovernments'), sysdate);
 
INSERT INTO EG_EMP_ASSIGNMENT_PRD(ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values (SEQ_ASS_PRD.nextval, TO_DATE('04/01/2013 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where code=1));
 
INSERT INTO EG_EMP_ASSIGNMENT (ID, DESIGNATIONID, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values (SEQ_ASS.nextval, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME = 'ASSISTANT EXECUTIVE ENGINEER'), 
   (SELECT ID FROM EG_EMP_ASSIGNMENT_PRD WHERE ID_EMPLOYEE = (SELECT ID FROM EG_EMPLOYEE WHERE CODE=1) ), 
   (SELECT ID_DEPT FROM EG_DEPARTMENT WHERE DEPT_NAME='BR-Bus Route Roads'), 
   (SELECT ID FROM EG_POSITION WHERE POSITION_NAME='WorksUser_01'));
   
INSERT INTO EG_USER ( ID_USER, TITLE, SALUTATION, FIRST_NAME, MIDDLE_NAME, LAST_NAME, DOB,ID_DEPARTMENT, LOCALE, USER_NAME, PWD,
PWD_REMINDER, UPDATETIME, UPDATEUSERID, EXTRAFIELD1,EXTRAFIELD2, EXTRAFIELD3, EXTRAFIELD4, IS_SUSPENDED, ID_TOP_BNDRY, REPORTSTO,
ISACTIVE, FROMDATE, TODATE ) VALUES ( 
SEQ_EG_USER.nextval, NULL, 'MR.', 'EETEST', NULL, NULL, NULL, (select id_dept from eg_department where dept_name = 'BR-Bus Route Roads'), NULL, 
'EETEST', 'DTRJto+XfKQ=', '804MQU', sysdate, (select ID_USER from eg_user where USER_NAME like 'egovernments'), 
NULL, NULL, NULL, NULL, 'N', 1, NULL, 1, TO_Date( '04/01/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '05/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 

INSERT INTO EG_USERROLE ( ID_ROLE, ID_USER, ID, FROMDATE, TODATE,IS_HISTORY ) VALUES ( 
(select ID_ROLE from EG_ROLES where ROLE_NAME like 'Works User'), (select ID_USER from eg_user where USER_NAME like 'EETEST'), SEQ_EG_USERROLE.nextval,  TO_Date( '04/01/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  null, 'N'); 

INSERT INTO EG_POSITION (POSITION_NAME, ID, SANCTIONED_POSTS, OUTSOURCED_POSTS, DESIG_ID, EFFECTIVE_DATE) Values
   ('WorksUser_02', seq_pos.nextval, 1, 0, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME = 'EXECUTIVE ENGINEER'), TO_DATE('04/01/2013 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));
   
INSERT INTO EG_EMPLOYEE(ID, GENDER, IS_HANDICAPPED, IS_MED_REPORT_AVAILABLE, EMPLOYMENT_STATUS, ID_USER, EMP_FIRSTNAME, ISACTIVE, CODE, CREATED_BY, LASTMODIFIED_DATE)
 Values (EGPIMS_PERSONAL_INFO_SEQ.NEXTVAL, 'M', 'N', 'N', 1, (select id_user from eg_user where user_name = 'EETEST'), 'EETEST', 1, 11, (select id_user from eg_user where user_name = 'egovernments'), sysdate);
 
INSERT INTO EG_EMP_ASSIGNMENT_PRD(ID, FROM_DATE, TO_DATE, ID_EMPLOYEE)
 Values (SEQ_ASS_PRD.nextval, TO_DATE('04/01/2013 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/31/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), (select id from eg_employee where code=11));
 
INSERT INTO EG_EMP_ASSIGNMENT (ID, DESIGNATIONID, ID_EMP_ASSIGN_PRD, MAIN_DEPT, POSITION_ID)
 Values (SEQ_ASS.nextval, (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME = 'EXECUTIVE ENGINEER'), 
   (SELECT ID FROM EG_EMP_ASSIGNMENT_PRD WHERE ID_EMPLOYEE = (SELECT ID FROM EG_EMPLOYEE WHERE CODE=11) ), 
   (SELECT ID_DEPT FROM EG_DEPARTMENT WHERE DEPT_NAME='BR-Bus Route Roads'), 
   (SELECT ID FROM EG_POSITION WHERE POSITION_NAME='WorksUser_02'));
   
   
UPDATE EG_ACTION SET QUERYPARAMS = 'moduleName=Works' WHERE NAME = 'DocUpload';

INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='EgiHomePage') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='DocUpload') FROM EG_ROLES WHERE ROLE_NAME = 'SuperUser');
INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='Upload Documents for Administration') FROM EG_ROLES WHERE ROLE_NAME = 'SuperUser');
INSERT INTO EG_ROLEACTION_MAP(roleid, actionid) select (select id_role FROM EG_ROLES WHERE role_name = 'Works User'), id from eg_action where context_root like 'egworks';
INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='AjaxDesignationDropdown') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='AjaxApproverDropdown') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='polldrafts') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='pollinbox') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='PopulateWFHistory') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');

INSERT INTO EG_ACTION (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES (seq_eg_action.NEXTVAL, 'WorksWorkFlowAdditionRule', NULL,NULL, SYSDATE, '/estimate/ajaxEstimate!getWorkFlowAdditionalRule.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name like 'Works'), NULL, 'WorksWorkFlowAdditionRule', 0, NULL, 'egworks');

INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='WorksWorkFlowAdditionRule') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');

INSERT INTO EG_ACTION (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES (seq_eg_action.NEXTVAL, 'EstimateGetFactor', NULL,NULL, SYSDATE, '/estimate/ajaxEstimate!getFactor.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name like 'Works'), NULL, 'EstimateGetFactor', 0, NULL, 'egworks');

INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='EstimateGetFactor') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');

INSERT INTO EG_ROLEACTION_MAP (SELECT ID_ROLE,(select id from eg_action where name='DEFAULT') FROM EG_ROLES WHERE ROLE_NAME = 'Works User');

#DOWN

DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='DEFAULT') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='EstimateGetFactor') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ACTION WHERE URL LIKE '/estimate/ajaxEstimate!getFactor.action' AND CONTEXT_ROOT = 'egworks';

DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='WorksWorkFlowAdditionRule') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ACTION WHERE URL LIKE '/estimate/ajaxEstimate!getWorkFlowAdditionalRule.action' AND CONTEXT_ROOT = 'egworks';

DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='EgiHomePage') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='PopulateWFHistory') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='pollinbox') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='polldrafts') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='AjaxApproverDropdown') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='AjaxDesignationDropdown') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID in (select id from eg_action where context_root = 'egworks') 
AND ROLEID in (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'Works User');

DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='DocUpload') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'SuperUser');
DELETE FROM EG_ROLEACTION_MAP WHERE ACTIONID = (select id from eg_action where name='Upload Documents for Administration') 
AND ROLEID = (SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME = 'SuperUser');

UPDATE EG_ACTION SET QUERYPARAMS = null WHERE NAME = 'DocUpload';


DELETE FROM EG_EMP_ASSIGNMENT where DESIGNATIONID in (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME = 'ASSISTANT EXECUTIVE ENGINEER')
and ID_EMP_ASSIGN_PRD in (SELECT ID FROM EG_EMP_ASSIGNMENT_PRD WHERE ID_EMPLOYEE = (SELECT ID FROM EG_EMPLOYEE WHERE CODE=1))
and MAIN_DEPT in (SELECT ID_DEPT FROM EG_DEPARTMENT WHERE DEPT_NAME='BR-Bus Route Roads')
and position_id in (SELECT ID FROM EG_POSITION WHERE POSITION_NAME='WorksUser_01');

DELETE FROM EG_EMP_ASSIGNMENT_PRD where id_employee = (select id from eg_employee where code=1);
DELETE FROM EG_EMPLOYEE where ID_USER = (select id_user from eg_user where user_name = 'AEETEST');
DELETE FROM eg_position where POSITION_NAME = 'WorksUser_01';
DELETE FROM eg_userrole where id_role = (select ID_ROLE from EG_ROLES where ROLE_NAME like 'Works User') 
and id_user = (select ID_USER from eg_user where USER_NAME like 'AEETEST');
DELETE FROM eg_user where user_name = 'AEETEST';

DELETE FROM EG_EMP_ASSIGNMENT_PRD where id_employee = (select id from eg_employee where code=11);
DELETE FROM EG_EMPLOYEE where ID_USER = (select id_user from eg_user where user_name = 'EETEST');
DELETE FROM eg_position where POSITION_NAME = 'WorksUser_02';
DELETE FROM eg_userrole where id_role = (select ID_ROLE from EG_ROLES where ROLE_NAME like 'Works User') 
and id_user = (select ID_USER from eg_user where USER_NAME like 'EETEST');
DELETE FROM eg_user where user_name = 'EETEST';

DELETE FROM EG_WF_MATRIX WHERE OBJECTTYPE='AbstractEstimate';