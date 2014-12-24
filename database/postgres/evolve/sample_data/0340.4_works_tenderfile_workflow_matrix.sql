#UP

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,
NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) 
values (EG_WF_MATRIX_SEQ.nextVal,'Electrical','TenderFile','NEW',null,null,'JUNIOR ENGINEER,ASSISTANT ENGINEER',null,'Created','Pending for Approval','EXECUTIVE ENGINEER',
'CREATED','Forward,Cancel',null,null);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,
NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) 
values (EG_WF_MATRIX_SEQ.nextVal,'Electrical','TenderFile','Created',null,'Pending for Approval','EXECUTIVE ENGINEER',
null,'Approved','END',null,'APPROVED','Approve,Reject',null,null);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,
NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) 
values (EG_WF_MATRIX_SEQ.nextVal,'Electrical','TenderFile','Rejected',null,null,'JUNIOR ENGINEER,ASSISTANT ENGINEER',
'null','Created','Pending for Approval','EXECUTIVE ENGINEER','RESUBMITTED','Forward,Cancel',null,null);

#DOWN

delete from EG_WF_MATRIX where DEPARTMENT='Electrical' and OBJECTTYPE='TenderFile';




