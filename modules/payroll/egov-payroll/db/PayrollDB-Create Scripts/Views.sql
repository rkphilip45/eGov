CREATE OR REPLACE FORCE VIEW EG_EIS_EMPLOYEEINFO
(ASS_ID, PRD_ID, ID, CODE, NAME, 
 DESIGNATIONID, FROM_DATE, TO_DATE, REPORTS_TO, DATE_OF_FA, 
 ISACTIVE, DEPT_ID, FUNCTIONARY_ID, POS_ID, USER_ID)
AS 
SELECT EEA.ID,EAP.ID,EE.ID,EE.CODE,EE.EMP_FIRSTNAME||' '||EE.EMP_MIDDLENAME||' '||EE.EMP_LASTNAME, 
   EEA.DESIGNATIONID,EAP.FROM_DATE,EAP.TO_DATE,EEA.REPORTS_TO,EE.DATE_OF_FIRST_APPOINTMENT,EE.ISACTIVE,
   EEA.MAIN_DEPT,EEA.ID_FUNCTIONARY,EEA.POSITION_ID,ee.ID_USER 
   FROM EG_EMP_ASSIGNMENT_PRD EAP, EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE 
   WHERE EE.ID = EAP.ID_EMPLOYEE 
  AND EAP.ID=EEA.ID_EMP_ASSIGN_PRD;


CREATE OR REPLACE FORCE VIEW VEGDCB_CESSMASTER
(ID, ID_MODULE, ID_INSTALLMENT, PERCENTAGE)
AS 
SELECT CM.ID,FP.MODULEID , IM.ID_INSTALLMENT, CM.PERCENTAGE 
FROM EG_INSTALLMENT_MASTER IM, FISCALPERIOD FP, FINANCIALYEAR FY, EGF_CESS_MASTER CM
WHERE IM.START_DATE=FP.STARTINGDATE AND IM.END_DATE=FP.ENDINGDATE 
AND FP.MODULEID = IM.ID_MODULE 
AND CM.FINANCIALYEARID = FY.FINANCIALYEAR
AND FY.ID = FP.FINANCIALYEARID;


CREATE OR REPLACE FORCE VIEW VEGDCB_TOTAL_CESS
(TOTAL_CESS_PERC, ID_INSTALLMENT)
AS 
SELECT SUM(PERCENTAGE),ID_INSTALLMENT FROM VEGDCB_CESSMASTER GROUP BY ID_MODULE,ID_INSTALLMENT;


CREATE OR REPLACE FORCE VIEW VEGF_TAX_CESS
(ID, NAME, GLCODE, PERCENTAGE, FINANCIALYEARID, 
 PURPOSEID, ISACTIVE)
AS 
SELECT C2.ID, C3.NAME,C3.GLCODE, C1.PERCENTAGE, C1.FINANCIALYEARID, C3.PURPOSEID, C1.ISACTIVE
FROM EGF_CESS_MASTER C1, EGF_TAX_CODE C2, CHARTOFACCOUNTS C3
WHERE
C1.TAXCODEID = C2.ID AND
C1.GLCODEID = C3.ID AND
C1.ISACTIVE = 1;


CREATE OR REPLACE FORCE VIEW V_EG_ROLE_ACTION_MODULE_MAP
(MODULE_ID, MODULE_NAME, PARENT_ID, ACTION_ID, ACTION_NAME, 
 ACTION_URL, ORDER_NUMBER, TYPEFLAG, IS_ENABLED)
AS 
select m.ID_MODULE as module_id,  m.module_name as module_name,
m.PARENTID as parent_id,  null, null, null,
m.ORDER_NUM as order_number, 'M', m.ISENABLED as is_enabled
from eg_module m
union
select null,  null,
a.MODULE_ID as parent_id,  a.ID as action_id, a.DISPLAY_NAME as action_name, a.URL || decode(a.QUERYPARAMS, null, '', '?' || a.QUERYPARAMS) as action_url,
a.ORDER_NUMBER as order_number, 'A', a.IS_ENABLED as is_enabled
from eg_action a;


CREATE OR REPLACE FORCE VIEW V_MODULE_ACTION_FOR_MODULE
(ID, NAME, ACTION_URL)
AS 
SELECT distinct v.module_id,v.module_name,null FROM V_EG_ROLE_ACTION_MODULE_MAP v
UNION
SELECT distinct v.action_id ,v.action_name,v.action_url FROM V_EG_ROLE_ACTION_MODULE_MAP v;


