#UP
delete from EG_APPCONFIG_VALUES where KEY_ID in (select id from eg_appconfig where key_name='MAXEXTRALINEITEMPERCENTAGE');

Insert into EG_APPCONFIG_VALUES
   (ID, KEY_ID, EFFECTIVE_FROM, VALUE)
 Values
(SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where key_name='MAXEXTRALINEITEMPERCENTAGE'), TO_DATE('04/01/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), '0');


#DOWN

delete from EG_APPCONFIG_VALUES where KEY_ID in (select id from eg_appconfig where key_name='MAXEXTRALINEITEMPERCENTAGE');


