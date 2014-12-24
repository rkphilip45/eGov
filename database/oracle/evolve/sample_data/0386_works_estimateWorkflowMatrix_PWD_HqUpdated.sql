#UP

delete from EG_WF_MATRIX  WHERE OBJECTTYPE='AbstractEstimate' and DEPARTMENT='Public Work' and ADDITIONALRULE in ('HQDepositCodeApp','HQBudgetApp') and 
fromqty=0 and toqty=250000; 

-- Additional rule - HQBudgetApp

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','NEW',null,null,'JUNIOR ENGINEER','HQBudgetApp','CREATED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','CREATED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','CREATED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQBudgetApp','TECH_SANCTIONED','Pending Budgetary Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','TECH_SANCTIONED',null,'Pending Budgetary Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Budgetary Appropriation Before Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Budgetary Appropriation Before Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Budgetary Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Budgetary Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Budgetary Appropriation After Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Budgetary Appropriation After Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Budgetary Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Budgetary Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','HQBudgetApp','ADMIN_SANCTIONED','END',null,'ADMIN_SANCTIONED','Approve,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','REJECTED',null,null,'JUNIOR ENGINEER','HQBudgetApp','RESUBMITTED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','RESUBMITTED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','RESUBMITTED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQBudgetApp','TECH_SANCTIONED','Pending Budgetary Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);


-- Additional rule - HQDepositCodeApp

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','NEW',null,null,'JUNIOR ENGINEER','HQDepositCodeApp','CREATED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','CREATED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','CREATED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQDepositCodeApp','TECH_SANCTIONED','Pending Deposit Code Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','TECH_SANCTIONED',null,'Pending Deposit Code Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Deposit Code Appropriation Before Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Deposit Code Appropriation Before Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Deposit Code Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Deposit Code Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Deposit Code Appropriation After Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Deposit Code Appropriation After Provision by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Deposit Code Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Deposit Code Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','HQDepositCodeApp','ADMIN_SANCTIONED','END',null,'ADMIN_SANCTIONED','Approve,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','REJECTED',null,null,'JUNIOR ENGINEER','HQDepositCodeApp','RESUBMITTED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','RESUBMITTED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','RESUBMITTED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQDepositCodeApp','TECH_SANCTIONED','Pending Deposit Code Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);


#DOWN

delete from EG_WF_MATRIX  WHERE OBJECTTYPE='AbstractEstimate' and DEPARTMENT='Public Work' and ADDITIONALRULE in ('HQDepositCodeApp','HQBudgetApp') and 
fromqty=0 and toqty=250000;

-- Additional rule - HQBudgetApp

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','NEW',null,null,'JUNIOR ENGINEER','HQBudgetApp','CREATED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','CREATED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','CREATED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQBudgetApp','TECH_SANCTIONED','Pending Budgetary Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','TECH_SANCTIONED',null,'Pending Budgetary Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Budgetary Appropriation by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Budgetary Appropriation by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Budgetary Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Budgetary Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Budgetary Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Budgetary Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','HQBudgetApp','FINANCIALLY_SANCTIONED','Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','HQBudgetApp','ADMIN_SANCTIONED','END',null,'ADMIN_SANCTIONED','Approve,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','REJECTED',null,null,'JUNIOR ENGINEER','HQBudgetApp','RESUBMITTED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','RESUBMITTED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','RESUBMITTED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQBudgetApp','TECH_SANCTIONED','Pending Budgetary Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);

--- Additional Rule HQDepositCodeApp

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','NEW',null,null,'JUNIOR ENGINEER','HQDepositCodeApp','CREATED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','CREATED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','CREATED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQDepositCodeApp','TECH_SANCTIONED','Pending Deposit Code Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','TECH_SANCTIONED',null,'Pending Deposit Code Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Deposit Code Appropriation by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Deposit Code Appropriation by EXECUTIVE ENGINEER','EXECUTIVE ENGINEER','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Deposit Code Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Deposit Code Appropriation by ASSISTANT SUPERINTENDENT','ASSISTANT SUPERINTENDENT','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Deposit Code Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Deposit Code Appropriation by Chief Accounts and Finance Officer','Chief Accounts and Finance Officer','HQDepositCodeApp','FINANCIALLY_SANCTIONED','Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','FINANCIALLY_SANCTIONED','Forward,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','FINANCIALLY_SANCTIONED',null,'Pending Admin Sanction','DEPUTY MUNICIPAL COMMISSIONER','HQDepositCodeApp','ADMIN_SANCTIONED','END',null,'ADMIN_SANCTIONED','Approve,Reject',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','REJECTED',null,null,'JUNIOR ENGINEER','HQDepositCodeApp','RESUBMITTED','Pending Approval for Technical Sanction','DEPUTY ENGINEER','RESUBMITTED','Forward,Cancel',0,250000);

Insert into EG_WF_MATRIX (ID,DEPARTMENT,OBJECTTYPE,CURRENTSTATE,CURRENTSTATUS,PENDINGACTIONS,CURRENTDESIGNATION,ADDITIONALRULE,NEXTSTATE,NEXTACTION,NEXTDESIGNATION,NEXTSTATUS,VALIDACTIONS,FROMQTY,TOQTY) values (EG_WF_MATRIX_SEQ.nextVal,'Public Work','AbstractEstimate','RESUBMITTED',null,'Pending Approval for Technical Sanction','DEPUTY ENGINEER','HQDepositCodeApp','TECH_SANCTIONED','Pending Deposit Code Appropriation by ASST. COMMISSIONER / WARD OFFICER','ASST. COMMISSIONER / WARD OFFICER','TECH_SANCTIONED','Forward,Reject',0,250000);
