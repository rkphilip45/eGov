#UP

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Estimate-CheckList'),  SYSDATE, 'Is this work proposed on NMC Land?');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Estimate-CheckList'),  SYSDATE, 'Export Regarding Transfer,N.O.C etc Taken?');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Estimate-CheckList'),  SYSDATE, 'Details drawings attached with proposal');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Estimate-CheckList'),  SYSDATE, 'Work will be done Stage Wise?');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Estimate-CheckList'),  SYSDATE, 'Proposed work is a Part of another work?');

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
SEQ_EG_APPCONFIG_VALUES.NEXTVAL, (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Estimate-CheckList'),  SYSDATE, 'if proposed work is new building, construction provision of Electrification and Water Supply is Made?');


#DOWN

DELETE FROM EG_APPCONFIG_VALUES WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Estimate-CheckList');