#UP

INSERT INTO EGPEN_BILLMISINFO_MAPPING
    (ID,OBJECTTYPE,PENSIONERDEPT,BILLDEPT,BILLFUNCTION,BILLFUND)
VALUES
    (SEQ_EGPEN_BILLMISINFO_MAPPING.NEXTVAL,'Gratuity','Any','G','00','FundFromMaster');


INSERT INTO EG_APPCONFIG_VALUES (ID, KEY_ID, VALUE , EFFECTIVE_FROM) 
values(SEQ_EG_APPCONFIG_VALUES.nextval, (SELECT id FROM eg_appconfig WHERE key_name='GRATUITY_DEBIT_ACCOUNTCODE'), 
'2104002',Sysdate-1);


UPDATE CHARTOFACCOUNTS SET PURPOSEID = (SELECT ID FROM EGF_ACCOUNTCODE_PURPOSE WHERE NAME = 'Gratuity Payable Code')
WHERE GLCODE = '3501102';



#DOWN

DELETE FROM EGPEN_BILLMISINFO_MAPPING WHERE OBJECTTYPE = 'Gratuity';
DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM eg_appconfig WHERE key_name='GRATUITY_DEBIT_ACCOUNTCODE');

UPDATE CHARTOFACCOUNTS SET PURPOSEID = NULL 
WHERE GLCODE = '3501102';