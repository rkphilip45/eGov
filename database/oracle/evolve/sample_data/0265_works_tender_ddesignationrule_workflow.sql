#UP
update eg_script set script=
'from java.lang import Long 
result=['''','''','''']
state=''DEFAULT'' 
if objectId:
    wfItem=genericService.find("from TenderResponse where id=?",[Long(objectId)])
if objectId:  
    if wfItem:
        if wfItem.getCurrentState():  
            currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
            print ''DDDDD-----DD''+currentDesig  
	    action=wfItem.getCurrentState().getNextAction()  
	    if action:  
    	        state=wfItem.getCurrentState().getValue()+''-''+action  
    	    else:   
	        state=wfItem.getCurrentState().getValue()

if ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'')  and department == ''L-Electrical''):
    result[0]="EXECUTIVE ENGINEER"
    result[1]="DIVISIONAL ELECTRICAL ENGINEER"
    result[2]="Zonal Officer"  
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department == ''''):
    result[0]="EXECUTIVE ENGINEER"   
    result[1]="DIVISIONAL ELECTRICAL ENGINEER"
    result[2]="Zonal Officer"  
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department != ''L-Electrical''):
    result[0]="EXECUTIVE ENGINEER"
    result[1]="DIVISIONAL ELECTRICAL ENGINEER"
    result[2]="Zonal Officer"
elif((state == ''CREATED-Pending for Approval'' or state == ''RESUBMITTED-Pending for Approval'') and (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'' or currentDesig == ''Zonal Officer'')):
    result[0]="SUPERINTENDING ENGINEER" 
elif ((state == ''CREATED-Pending for Approval'' or state == ''RESUBMITTED-Pending for Approval'') and currentDesig == ''SUPERINTENDING ENGINEER''):
    result[0]="CHIEF ENGINEER" 
'
where name = 'TenderResponse.nextDesignation';
#DOWN
update eg_script set script=
'from java.lang import Long 
result=['''','''']
state=''DEFAULT'' 
if objectId:
    wfItem=genericService.find("from TenderResponse where id=?",[Long(objectId)])
if objectId:  
    if wfItem:
        if wfItem.getCurrentState():  
            currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()  
	    action=wfItem.getCurrentState().getNextAction()  
	    if action:  
    	        state=wfItem.getCurrentState().getValue()+''-''+action  
    	    else:   
	        state=wfItem.getCurrentState().getValue()

if ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'')  and department == ''L-Electrical''):  
    result[0]="ASSISTANT DIVISIONAL ENGINEER"  
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department == ''''):  
    result[0]="ASSISTANT EXECUTIVE ENGINEER"   
    result[1]="ASSISTANT DIVISIONAL ENGINEER"  
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department != ''L-Electrical''):  
    result[0]="ASSISTANT EXECUTIVE ENGINEER"
elif((state == ''CREATED-Pending for Approval'' or state == ''RESUBMITTED-Pending for Approval'') and currentDesig == ''ASSISTANT DIVISIONAL ENGINEER'' and department == ''L-Electrical''):  
    result[0]="DIVISIONAL ELECTRICAL ENGINEER"    
    result[1]="SUPERINTENDING ENGINEER"  
elif ((state == ''CREATED-Pending for Approval'' or state == ''RESUBMITTED-Pending for Approval'') and currentDesig == ''ASSISTANT EXECUTIVE ENGINEER''):  
    result[0]="EXECUTIVE ENGINEER" 
    result[1]="SUPERINTENDING ENGINEER"
'
where name = 'TenderResponse.nextDesignation';