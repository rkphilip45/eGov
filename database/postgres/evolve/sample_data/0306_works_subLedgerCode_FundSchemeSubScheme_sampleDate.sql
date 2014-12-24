#UP

Insert into fund(ID,CODE,NAME,LLEVEL,PARENTID,OPENINGDEBITBALANCE,OPENINGCREDITBALANCE,TRANSACTIONDEBITAMOUNT,
TRANSACTIONCREDITAMOUNT,ISACTIVE,LASTMODIFIED,CREATED,MODIFIEDBY,ISNOTLEAF,IDENTIFIER,PURPOSE_ID,PAYGLCODEID,RECVGLCODEID) 
VALUES(SEQ_FUND.nextval, 'DEP','Deposit Work',0,null,0,0,0,0,0,sysdate,sysdate,1,0,'D',12,null,null);

Insert into fund(ID,CODE,NAME,LLEVEL,PARENTID,OPENINGDEBITBALANCE,OPENINGCREDITBALANCE,TRANSACTIONDEBITAMOUNT,
TRANSACTIONCREDITAMOUNT,ISACTIVE,LASTMODIFIED,CREATED,MODIFIEDBY,ISNOTLEAF,IDENTIFIER,PURPOSE_ID,PAYGLCODEID,RECVGLCODEID) 
VALUES(SEQ_FUND.nextval, 'MP','MPLADS',0,null,0,0,0,0,0,sysdate,sysdate,1,0,'D',12,null,null);

Insert into fund(ID,CODE,NAME,LLEVEL,PARENTID,OPENINGDEBITBALANCE,OPENINGCREDITBALANCE,TRANSACTIONDEBITAMOUNT,
TRANSACTIONCREDITAMOUNT,ISACTIVE,LASTMODIFIED,CREATED,MODIFIEDBY,ISNOTLEAF,IDENTIFIER,PURPOSE_ID,PAYGLCODEID,RECVGLCODEID) 
VALUES(SEQ_FUND.nextval, 'MLA','MLACDS',0,null,0,0,0,0,0,sysdate,sysdate,1,0,'D',12,null,null);



INSERT INTO SCHEME(ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,DESCRIPTION,FUNDID,SECTORID,AAES,FIELDID)
VALUES(SEQ_SCHEME.nextval,'DEP123','Deposit Works123','01-Apr-2008','31-Mar-2012',1,'deposit works',(select id from fund where code='DEP'),null,null,null);

INSERT INTO SCHEME(ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,DESCRIPTION,FUNDID,SECTORID,AAES,FIELDID)
VALUES(SEQ_SCHEME.nextval,'MP123','MPLADS123','01-Apr-2008','31-Mar-2012',1,'MPLADS',(select id from fund where code='MP'),null,null,null);

INSERT INTO SCHEME(ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,DESCRIPTION,FUNDID,SECTORID,AAES,FIELDID)
VALUES(SEQ_SCHEME.nextval,'MLA123','MLALDS123','01-Apr-2008','31-Mar-2012',1,'MLALDS',(select id from fund where code='MLA'),null,null,null);



INSERT INTO SUB_SCHEME(ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,SCHEMEID,LASTMODIFIEDDATE,DEPARTMENT,INITIAL_ESTIMATE_AMOUNT,
COUNCIL_LOAN_PROPOSAL_NUMBER,COUNCIL_LOAN_PROPOSAL_DATE,COUNCIL_ADMIN_SANCTION_NUMBER,COUNCIL_ADMIN_SANCTION_DATE,
GOVT_LOAN_PROPOSAL_NUMBER,GOVT_LOAN_PROPOSAL_DATE,GOVT_ADMIN_SANCTION_NUMBER,GOVT_ADMIN_SANCTION_DATE)
VALUES(SEQ_SUB_SCHEME.nextval,'DEP000','DEP WORKS','01-Apr-2008','31-Mar-2012',1,(select id from scheme where code='DEP123'),SYSDATE,
null,null,null,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO SUB_SCHEME(ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,SCHEMEID,LASTMODIFIEDDATE,DEPARTMENT,INITIAL_ESTIMATE_AMOUNT,
COUNCIL_LOAN_PROPOSAL_NUMBER,COUNCIL_LOAN_PROPOSAL_DATE,COUNCIL_ADMIN_SANCTION_NUMBER,COUNCIL_ADMIN_SANCTION_DATE,
GOVT_LOAN_PROPOSAL_NUMBER,GOVT_LOAN_PROPOSAL_DATE,GOVT_ADMIN_SANCTION_NUMBER,GOVT_ADMIN_SANCTION_DATE)
VALUES(SEQ_SUB_SCHEME.nextval,'MP999','MPLADS999','01-Apr-2008','31-Mar-2012',1,(select id from scheme where code='MP123'),SYSDATE,
null,null,null,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO SUB_SCHEME(ID,CODE,NAME,VALIDFROM,VALIDTO,ISACTIVE,SCHEMEID,LASTMODIFIEDDATE,DEPARTMENT,INITIAL_ESTIMATE_AMOUNT,
COUNCIL_LOAN_PROPOSAL_NUMBER,COUNCIL_LOAN_PROPOSAL_DATE,COUNCIL_ADMIN_SANCTION_NUMBER,COUNCIL_ADMIN_SANCTION_DATE,
GOVT_LOAN_PROPOSAL_NUMBER,GOVT_LOAN_PROPOSAL_DATE,GOVT_ADMIN_SANCTION_NUMBER,GOVT_ADMIN_SANCTION_DATE)
VALUES(SEQ_SUB_SCHEME.nextval,'MLA555','MLALDS098','01-Apr-2008','31-Mar-2012',1,(select id from scheme where code='MLA123'),SYSDATE,
null,null,null,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

#DOWN


DELETE SUB_SCHEME WHERE SCHEMEID=(select id from scheme where code='DEP123');
DELETE SUB_SCHEME WHERE SCHEMEID=(select id from scheme where code='MP123');
DELETE SUB_SCHEME WHERE SCHEMEID=(select id from scheme where code='MLA123');

DELETE SCHEME WHERE FUNDID=(select id from fund where code='DEP');
DELETE SCHEME WHERE FUNDID=(select id from fund where code='MP');
DELETE SCHEME WHERE FUNDID=(select id from fund where code='MLA');

DELETE FUND WHERE CODE IN ('DEP','MP','MLA');


