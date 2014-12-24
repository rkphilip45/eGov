#UP

INSERT INTO EGF_ACCOUNTCODE_PURPOSE(ID,NAME, modifiedby, modifieddate, createddate, createdby) 
values((Select max(id)+1 from EGF_ACCOUNTCODE_PURPOSE),'LandEstate_ShopRent_purpose',(Select id_user from EG_USER WHERE
 user_name='egovernments'),sysdate,sysdate,(Select id_user from EG_USER WHERE user_name='egovernments'));

update chartofaccounts set purposeid=(Select id from EGF_ACCOUNTCODE_PURPOSE where name='LandEstate_ShopRent_purpose')
where glcode='1301003';

INSERT INTO EG_REASON_CATEGORY (ID_TYPE,NAME,CODE,ORDER_ID,LAST_UPDATED_TIMESTAMP) 
values (SEQ_EG_REASON_CATEGORY.NEXTVAL,'REBATE','REBATE',1,SYSDATE);

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASON_MASTER, ID_CATEGORY, IS_DEBIT, MODULE_ID, CODE,ORDER_ID, CREATE_TIME_STAMP, LAST_UPDATED_TIMESTAMP ) 
VALUES (SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL, 'Extra Charges',(select ID_TYPE from EG_REASON_CATEGORY where NAME='TAX' ), 'N', 
(select id_module from eg_module where MODULE_NAME='LandAndEstate'), 'EXTRACHARGES', 1,  sysdate, sysdate); 

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES(SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Rent',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'RENT','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES(SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Arrears on Rent',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'ARREARSONRENT','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES(SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Advance Deposit',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'ADVANCEDEPOSIT','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES(SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Ground Rent',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'GROUNDRENT','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES (SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Maintenance Charges',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'MCHARGE','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES (SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Arrears on Maintenance Charge',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'ARRSMCHARGE','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES(SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Arrears on Ground Rent',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'ARRSGROUNDRENT','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES (SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Discount',(select ID_TYPE from eg_reason_category where CODE='REBATE'),'Y',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'DISCOUNT','1',SYSDATE,SYSDATE);

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID) 
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Extra Charges','1301003',1,'EXTRACHARGES','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID) 
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Rent','1301003',1,'RENT','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Arrears on Rent','1301003',1,'ARREARSONRENT','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Advance Deposit','1301003',1,'ADVANCEDEPOSIT','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Ground Rent','1301003',1,'GROUNDRENT','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Maintenance Charges','1301003',1,'MAINTENANCECHARGES','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Arrears on Maintenance Charge','1301003',1,'ARREARSONMAINTENANCECHARGE','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Arrears on Ground Rent','1301003',1,'ARREARSONGROUNDRENT','LandEstate_ShopRent_purpose');

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,REASONNAME,GLCODE, ISACTIVE,DESCRIPTION,PURPOSEID)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','Discount','1301003',1,'DISCOUNT','LandEstate_ShopRent_purpose');

#DOWN

DELETE FROM EGLEMS_DEMANDREASONTYPE WHERE TYPE = 'SHOP';
DELETE FROM EG_DEMAND_REASON_MASTER WHERE REASON_MASTER IN('Extra Charges','Rent','Arrears on Rent','Advance Deposit','Ground Rent','Maintenance Charges','Arrears on Maintenance Charge','Arrears on Ground Rent','Discount');

DELETE FROM EG_REASON_CATEGORY WHERE NAME='REBATE';

update chartofaccounts set purposeid=null
where glcode='1301003';

DELETE FROM EGF_ACCOUNTCODE_PURPOSE WHERE NAME='LandEstate_ShopRent_purpose';
