#UP

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='BILL_DEFAULT_BUDGETHEAD_ACCOUNTCODE'),SYSDATE, 'no');  

#DOWN

DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID=(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='BILL_DEFAULT_BUDGETHEAD_ACCOUNTCODE');