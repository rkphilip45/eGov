#UP

/*************** LASTMODIFIED_DATE Added for EGPAY_PAYGENRULESSETUP_MSTR **************/


ALTER TABLE EGPAY_PAYGENRULESSETUP_MSTR ADD (LASTMODIFIED_DATE	DATE);
UPDATE EGPAY_PAYGENRULESSETUP_MSTR SET LASTMODIFIED_DATE = SYSDATE WHERE LASTMODIFIED_DATE IS NULL;

#DOWN