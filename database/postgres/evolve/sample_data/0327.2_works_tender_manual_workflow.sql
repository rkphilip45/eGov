#UP
update eg_script set script=
'from org.egov.pims.dao import EisDAOFactory            
from org.egov.pims.commons import DesignationMaster            
from org.egov.pims.commons.dao import DesignationMasterDAO          
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException          
from org.egov import EGOVException   
from  org.egov.exceptions import NoSuchObjectException          
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()        
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()          
def save_and_submit():          
    save_tenderResponse()          
    return submit()          
def save_tenderResponse():          
    if not wfItem.getCurrentState():     
        wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
    return (persistenceService.persist(wfItem),None)        
    if wfItem.getCurrentState().getValue() == ''REJECTED'':  
        wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()))  
        return (persistenceService.persist(wfItem),None)        
    return (None,None)     
def submit():          
    state=''DEFAULT''          
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
    return approvals[state]()   
def reject():   
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()), comments)   
    return (persistenceService.persist(wfItem),None)
def cancel():  
    wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()),comments)  
    wfItem.changeState(''END'',persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()),'''')           
    return (persistenceService.persist(wfItem),None)   
def submit_for_approval():          
    try:         
        position=find_position(wfItem)    
        if wfItem.getCurrentState().getValue() == ''REJECTED'':
            wfItem.changeState(''RESUBMITTED'',''Pending for Approval'',position,comments)   
	else:  
	    wfItem.changeState(''CREATED'',''Pending for Approval'',position,comments)   
        result=persistenceService.persist(wfItem)          
        return (result,None)          
    except ValidationException,e:          
        return (None,e.getErrors())
'
where name = 'TenderResponse.workflow';

update eg_script set script=script||( 
'def approve():   
    try:
        currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
        if (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'' or currentDesig == ''Zonal Officer'') and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED''):
            position=find_position(wfItem)   
            state=''CHECKED''   
            action=''Pending for Approval''
        if currentDesig == ''SUPERINTENDING ENGINEER'' and (wfItem.getCurrentState().getValue() == ''CHECKED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED''):
            position=find_position(wfItem)   
            state=''CHECKED''   
            action=''Pending for Approval'' 
	if currentDesig == ''CHIEF ENGINEER'' and (wfItem.getCurrentState().getValue() == ''CHECKED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED''): 
            position=find_position(wfItem)   
            state=''CHECKED''   
            action=''Pending for Approval'' 
   	wfItem.changeState(state,action,position,comments)
    	result=persistenceService.persist(wfItem)            
    	return (result,None)   
    except ValidationException,e:            
        return (None,e.getErrors())
def approveall():   
    try:
        if not wfItem.getCurrentState():     
            wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
            state=''APPROVED''   
            action='''' 
            position=wfItem.getCurrentState().getOwner()
        else:
            state=''APPROVED''   
            action='''' 
            position=wfItem.getCurrentState().getOwner()     
   	wfItem.changeState(state,action,position,comments)
        if wfItem.getCurrentState().getValue() == ''APPROVED'':  
            wfItem.changeState(''END'',action,position,comments)   
    	result=persistenceService.persist(wfItem)            
    	return (result,None)   
    except ValidationException,e:            
        return (None,e.getErrors())
')
where name = 'TenderResponse.workflow';

update eg_script set script=script||( 
'def find_position(tenderResponse):              
    try:	  
	next_position=persistenceService.getEisManager().getPositionforEmp(tenderResponse.getWorkflowApproverUserId())      
    except:  
        pass  
    if not next_position:  
        raise ValidationException,[ValidationError(''currentState.position'',''works.tenderResponseworkflow.no_employee_position'')]   
    return next_position         
approvals={          
    ''DEFAULT'':save_and_submit,  
    ''NEW'':submit_for_approval,          
    ''CREATED'':approve,             
    ''REJECTED'':submit_for_approval          
}          
transitions={
 ''save'':save_tenderResponse,            
 ''submit_for_approval'':submit,          
 ''reject'':reject,          
 ''cancel'':cancel,
 ''forward'':approve, 
 ''approve'':approveall
}          
result,validationErrors=transitions[action.getName()]()')
where name = 'TenderResponse.workflow';

#DOWN
update eg_script set script=
'from org.egov.pims.dao import EisDAOFactory            
from org.egov.pims.commons import DesignationMaster            
from org.egov.pims.commons.dao import DesignationMasterDAO          
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException          
from org.egov import EGOVException   
from  org.egov.exceptions import NoSuchObjectException          
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()        
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()          
def save_and_submit():          
    save_tenderResponse()          
    return submit()          
def save_tenderResponse():          
    if not wfItem.getCurrentState():     
        wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
    return (persistenceService.persist(wfItem),None)        
    if wfItem.getCurrentState().getValue() == ''REJECTED'':  
        wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()))  
        return (persistenceService.persist(wfItem),None)        
    return (None,None)     
def submit():          
    state=''DEFAULT''          
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
    return approvals[state]()   
def reject():   
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()), comments)   
    return (persistenceService.persist(wfItem),None)
def cancel():  
    wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()),comments)  
    wfItem.changeState(''END'',persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()),'''')           
    return (persistenceService.persist(wfItem),None)   
def submit_for_approval():          
    try:         
        position=find_position(wfItem)    
        if wfItem.getCurrentState().getValue() == ''REJECTED'':
            wfItem.changeState(''RESUBMITTED'',''Pending for Approval'',position,comments)   
	else:  
	    wfItem.changeState(''CREATED'',''Pending for Approval'',position,comments)   
        result=persistenceService.persist(wfItem)          
        return (result,None)          
    except ValidationException,e:          
        return (None,e.getErrors())
'
where name = 'TenderResponse.workflow';

update eg_script set script=script||( 
'def approve():   
    try:
        currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
        if (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'' or currentDesig == ''Zonal Officer'') and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED''):
            position=find_position(wfItem)   
            state=''CHECKED''   
            action=''Pending for Approval''
        if currentDesig == ''SUPERINTENDING ENGINEER'' and (wfItem.getCurrentState().getValue() == ''CHECKED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED''):
            position=find_position(wfItem)   
            state=''CHECKED''   
            action=''Pending for Approval''
        if wfItem.getCurrentState().getValue() == ''CHECKED'' and currentDesig == ''CHIEF ENGINEER'':
            state=''APPROVED''   
            action='''' 
            position=wfItem.getCurrentState().getOwner()
   	wfItem.changeState(state,action,position,comments)
        if wfItem.getCurrentState().getValue() == ''APPROVED'':  
            wfItem.changeState(''END'',action,position,comments)   
    	result=persistenceService.persist(wfItem)            
    	return (result,None)   
    except ValidationException,e:            
        return (None,e.getErrors())
def approveall():   
    try:
        if not wfItem.getCurrentState():     
            wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
            state=''APPROVED''   
            action='''' 
            position=wfItem.getCurrentState().getOwner()
        else:
            state=''APPROVED''   
            action='''' 
            position=wfItem.getCurrentState().getOwner()     
   	wfItem.changeState(state,action,position,comments)
        if wfItem.getCurrentState().getValue() == ''APPROVED'':  
            wfItem.changeState(''END'',action,position,comments)   
    	result=persistenceService.persist(wfItem)            
    	return (result,None)   
    except ValidationException,e:            
        return (None,e.getErrors())
')
where name = 'TenderResponse.workflow';

update eg_script set script=script||( 
'def find_position(tenderResponse):              
    try:	  
	next_position=persistenceService.getEisManager().getPositionforEmp(tenderResponse.getWorkflowApproverUserId())      
    except:  
        pass  
    if not next_position:  
        raise ValidationException,[ValidationError(''currentState.position'',''works.tenderResponseworkflow.no_employee_position'')]   
    return next_position         
approvals={          
    ''DEFAULT'':save_and_submit,  
    ''NEW'':submit_for_approval,          
    ''CREATED'':approve,             
    ''REJECTED'':submit_for_approval          
}          
transitions={
 ''save'':save_tenderResponse,            
 ''submit_for_approval'':submit,          
 ''reject'':reject,          
 ''cancel'':cancel,
 ''forward'':approve, 
 ''approve'':approveall
}          
result,validationErrors=transitions[action.getName()]()')
where name = 'TenderResponse.workflow';
