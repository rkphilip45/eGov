package org.egov.pims.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserRole;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.EisDAOFactory;
import org.egov.pims.dao.EmployeeDepartmentDAO;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.empLeave.model.DatePeriodFY;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeNomineeMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author DivyaShree
 *
 */
public class EisUtilService implements EISServeable
{
	private static final Logger LOGGER = Logger.getLogger(EisUtilService.class);
	private final String EMPVIEWDEPTIDSLOGGEDINUSER="EMPVIEW-DEPTIDS-LOGGEDINUSER";
	private PersistenceService persistenceService;
	
	
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public List<Position> getPositionsForUser(Integer user, Date date){

		
		List<Position> positionList = new ArrayList<Position>();
		try{
			String mainStr = "";
			
				mainStr="select distinct(a.position) from Assignment a where a.assignmentPrd.employeeId.userMaster.id =?";
			
			if (date == null){
				date = new Date();
			}
			
			mainStr += " and ((a.assignmentPrd.toDate is null and a.assignmentPrd.fromDate<= ?) or (a.assignmentPrd.fromDate <= ? and a.assignmentPrd.toDate >= ?))";
			positionList = (List)getPersistenceService().findAllBy(mainStr,user,date,date,date);
			
			
		}catch(Exception e){
				LOGGER.error("Exception while getting the getPositionsForUser="+e.getMessage());
				 throw new EGOVRuntimeException(e.getMessage(),e);
			
		}
		return positionList;
	
	}




	
	public Position getPrimaryPositionForUser(Integer userId, Date date) {
		
			Position position=null;
		
		try{
			String mainStr = "";
			
				mainStr="select a.position from Assignment a where a.isPrimary='Y'";
				
				if(userId!=null && userId!=0)
				{
					mainStr+=" and a.assignmentPrd.employeeId.userMaster.id =?";
					
				}
			
			if (date == null){
				date = new Date();
			}
			
			mainStr += " and ((a.assignmentPrd.toDate is null and a.assignmentPrd.fromDate<= ?) or (a.assignmentPrd.fromDate <= ? and a.assignmentPrd.toDate >= ?))";
			position = (Position)getPersistenceService().find(mainStr,userId,date,date,date);
			
			
		}catch(Exception e){
				LOGGER.error("Exception while getting the getPrimaryPositionForUser="+e.getMessage());
				throw new EGOVRuntimeException(e.getMessage(),e);
			
		}
		return position;
		
	}




	
	public User getUserForPosition(Integer positionId, Date date) 
	{
		User user = null;
		try{
			String mainStr = "";
			
				mainStr="select emp.userMaster from EmployeeView emp where emp.position.id = ?";
				
				
			
			if (date == null){
				date = new Date();
			}
			
			mainStr += " and ((emp.toDate is null and emp.fromDate<= ?) or (emp.fromDate <= ? and emp.toDate >= ?))";
			user = (User)getPersistenceService().find(mainStr,positionId,date,date,date);
		}catch(Exception e){
				LOGGER.error("Exception while getting the getUserForPosition="+e.getMessage());
				throw new EGOVRuntimeException(e.getMessage(),e);
			
		}
		return user;
		
	}
	
	
	public List<EmployeeView> getEmployeeInfoList(HashMap paramMap) {
		List<EmployeeView> empInfoList = new ArrayList<EmployeeView>();
		Integer departmentId = paramMap.get("departmentId") != null ? Integer.parseInt((String)paramMap.get("departmentId")) : null;
		Integer designationId = paramMap.get("designationId") != null ? Integer.parseInt((String)paramMap.get("designationId")) : null;
		Integer functionaryId = paramMap.get("functionaryId") != null ? Integer.parseInt((String)paramMap.get("functionaryId")) : null;
		String code = (String)paramMap.get("code");
		String name = (String)paramMap.get("name");
		Integer status = paramMap.get("status") != null ? Integer.parseInt((String)paramMap.get("status")) : null;
		Integer empType = paramMap.get("empType") != null ? Integer.parseInt((String)paramMap.get("empType")) : null;
		String searchAll = (String)paramMap.get("searchAll");
		Integer boundaryId = paramMap.get("boundaryId")!=null ?Integer.parseInt((String)paramMap.get("boundaryId")) :null;
		Integer userId = paramMap.get("userId")!=null ?Integer.parseInt((String)paramMap.get("userId")) :null;
		List<String> roleList = paramMap.get("roleList")!=null?(List<String>)paramMap.get("roleList") : new ArrayList<String>();
		List<User> userList = new ArrayList<User>();
		
		if(boundaryId!=null && boundaryId!=0)
		{
			Boundary boundary=null;
			BoundaryDAO boundaryDAO = new BoundaryDAO();	
			boundary=boundaryDAO.getBoundary(boundaryId);  
			//Exclude get users list if boundary is city level
			 if(boundary.getParent()!=null)
			  try 
			  {
				//userList = getListOfUsersByBoundaryId(boundaryId);
				userList = getListOfUsersForGivenBoundaryId(boundaryId);
			  } catch (NoSuchObjectException e)
			  {
				
				LOGGER.error(e);
			  }
			  
			 
		} 
		
		//Checking for boundary and role to get the list of users(It's taking 2/3 sec more time than writing qry)
		/*if(boundaryId!=null && boundaryId!=0)
		{		
			List<User> tempUserList = new ArrayList<User>();
			try{
				tempUserList = getListOfUsersByBoundaryId(boundaryId);
			}catch (NoSuchObjectException e){				
				LOGGER.error(e);
			}
			if(!tempUserList.isEmpty()){
				if(!roleList.isEmpty()){
					for(User userObj : getListOfUsersByRole(roleList)){
						if(tempUserList.contains(userObj)){
							userList.add(userObj);
						}
					}
				}
			}
		}
		else if(!roleList.isEmpty()){
			userList = getListOfUsersByRole(roleList);
		}	*/
		
		if("Y".equalsIgnoreCase(searchAll)){
			empInfoList = persistenceService.findAllBy("from EmployeeView");
		}
		else {
			String mainStr = "from EmployeeView ev where" ;
			if(code!=null&&!code.equals(""))
			{
				mainStr +=" upper(trim(ev.employeeCode)) = :employeeCode and ";
			}
			if(departmentId != null && departmentId.intValue() != 0)
			{
				mainStr +=" ev.deptId.id= :deptId and ";
			}
			if(designationId != null && designationId.intValue() != 0)
			{
				mainStr += " ev.desigId.designationId = :designationId and ";
			}
			if(functionaryId != null && functionaryId.intValue() !=0)
			{
				mainStr += " ev.functionary.id = :functionaryId and ";
			}
			
			if(empType != null && empType.intValue()!=0)
			{
				mainStr += " ev.employeeType.id=:employeeType and";
			}
			
			//Adding Boundary
			if(boundaryId !=null && boundaryId.intValue()!=0)
			{
				if(!userList.isEmpty())
				{
					mainStr += " ev.userMaster in(:userObjList) and ";
				}
			}
			if(userId != null && userId.intValue() != 0)
			{
				mainStr += " ev.userMaster.id =:userId and ";
			}
			if(!roleList.isEmpty()){				
				mainStr += "ev.userMaster.id in(select userRole.user.id from UserRole userRole where " +
						"((userRole.fromDate <= SYSDATE and userRole.toDate >= SYSDATE) or " +
						"(userRole.fromDate <= SYSDATE and userRole.toDate is null)) and " +
						"userRole.role.roleName in(:roleList) ) and";				
			}			
			if(name!= null && !name.equals(""))
			{
				mainStr += " trim(upper(ev.employeeName))  like '%"+name.trim().toUpperCase()+"%' and ";
			}
			if(status != null && status.intValue() != 0 )
			{
				mainStr += " ev.employeeStatus.id = :employeeStatus and ";
			}					
			if(status != null && status.intValue() != 0 && designationId != null && designationId.intValue() == 0)
			{
				mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) and ev.employeeStatus.id = :employeeStatus ";
			}
			else if(status != null && status.intValue() == 0 && designationId != null && designationId.intValue() != 0)
			{
				mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
			}
			//Inspite of SearchAll is true or false, if employee code is entered, search for all active and inactive employees
			else if(code!=null && !code.equals(""))
			{
				mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
						" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
						" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
						" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
			}
			else if((status != null && status.intValue() != 0) || (designationId != null && designationId.intValue() == 0))
			{
				mainStr +="  ((ev.toDate IS NULL AND ev.fromDate <= SYSDATE) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE) " +
						" OR (ev.fromDate IN (SELECT MAX (evn.fromDate)  FROM EmployeeView  evn   " +
						" WHERE evn.id = ev.id AND NOT EXISTS  (SELECT evn2.id FROM EmployeeView evn2 WHERE evn2.id = ev.id AND " +
						" ((evn2.toDate  IS NULL AND evn2.fromDate <= SYSDATE) OR (evn2.fromDate <= SYSDATE AND evn2.toDate > SYSDATE)) )))) ";
			}
			else
			{
				mainStr +=" ((ev.toDate is null and ev.fromDate <= SYSDATE ) OR (ev.fromDate <= SYSDATE AND ev.toDate > SYSDATE)) ";
			}		
			mainStr +=" and ev.isActive='1' "; // getting only active employees for any kind of search
			Query qry = null;
			qry = persistenceService.getSession().createQuery(mainStr);
			LOGGER.info("qryqryqryqry"+qry.toString());
			if(code!=null&&!code.equals(""))
			{
				qry.setString("employeeCode", code);
			}
			if(departmentId != null && departmentId.intValue() != 0)
			{
				qry.setInteger("deptId", departmentId);
			}
			if(designationId != null && designationId.intValue() != 0)
			{
				qry.setInteger("designationId", designationId);
			}
			if(functionaryId != null && functionaryId.intValue() != 0)
			{
				qry.setInteger("functionaryId", functionaryId);
			}
			if(status != null && status.intValue() != 0)
			{
				qry.setInteger("employeeStatus", status);
			}
			if(boundaryId !=null && boundaryId.intValue()!=0)
			{
				if(!userList.isEmpty())
				{
					qry.setParameterList("userObjList",userList);
				}
			}
			if(userId != null && userId.intValue()!=0)
			{
				qry.setInteger("userId", userId);
			}
			if(!roleList.isEmpty()){
				qry.setParameterList("roleList",roleList);
			}
			if(empType != null && empType.intValue()!=0)
			{
				qry.setInteger("employeeType", empType.intValue());
			}
			empInfoList = (List)qry.list();
		}
		return empInfoList;
	}
	
	
	public List<Position> getUniquePositionList(HashMap<String,String> paramMap,Date date){
		List<Position> positionList = new ArrayList<Position>();
		Integer departmentId = paramMap.get("departmentId") != null ? Integer.parseInt(paramMap.get("departmentId")) : null;
		Integer designationId = paramMap.get("designationId") != null ? Integer.parseInt(paramMap.get("designationId")) : null;
		Integer functionaryId = paramMap.get("functionaryId") != null ? Integer.parseInt(paramMap.get("functionaryId")) : null;
		Integer functionId = paramMap.get("functionId") != null ? Integer.parseInt(paramMap.get("functionId")) : null;
		Integer fundId = paramMap.get("fundId") != null ? Integer.parseInt(paramMap.get("fundId")) : null;
		if(date == null){
			date = new Date();
		}
		
		String qryString = "select distinct(ass.position) from Assignment ass where ";
		if(departmentId != null && departmentId.intValue() != 0){
			qryString +=" ass.deptId.id= :deptId and ";
		}
		if(designationId != null && designationId.intValue() != 0){
			qryString += " ass.desigId.designationId = :designationId and ";
		}
		if(functionaryId != null && functionaryId.intValue() !=0)
		{
			qryString += " ass.functionary.id = :functionaryId and ";
		}
		if(functionId != null && functionId.intValue() !=0)
		{
			qryString += " ass.functionId.id = :functionId and ";
		}
		if(fundId != null && fundId.intValue() !=0)
		{
			qryString += " ass.fundId.id = :fundId and ";
		}
		qryString += " ass.assignmentPrd.id in (select asprd.id from AssignmentPrd asprd where asprd.fromDate <= :date and asprd.toDate >= :date)";
				
		Query qry = null;
		LOGGER.info("qryqryqryqryString--------"+qryString);	
		qry = persistenceService.getSession().createQuery(qryString);
		LOGGER.info("qryqryqryqry----"+qry.toString());		
		if(departmentId != null && departmentId.intValue() != 0)
		{
			qry.setInteger("deptId", departmentId);
		}
		if(designationId != null && designationId.intValue() != 0)
		{
			qry.setInteger("designationId", designationId);
		}
		if(functionaryId != null && functionaryId.intValue() != 0)
		{
			qry.setInteger("functionaryId", functionaryId);
		}
		if(functionId != null && functionId.intValue() != 0)
		{
			qry.setInteger("functionId", functionId);
		}
		if(fundId != null && fundId.intValue() != 0)
		{
			qry.setInteger("fundId", fundId);
		}
		qry.setDate("date", date);
		positionList = (List)qry.list();
		return positionList;
	}
	
	
	public List<PersonalInformation>  getAllHodEmpByDept(Integer deptId) throws Exception
	{
		List<PersonalInformation> employeeObj = null;
		EmployeeDepartmentDAO employeeDepartmentDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO();
		employeeObj=employeeDepartmentDAO.getAllHodEmpByDept(deptId);
		return employeeObj;
	}
	
	public List getListOfUsersByBoundaryId(Integer boundaryId) throws NoSuchObjectException
	{
		PersonalInformationDAO personalInfomationDAO =EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
		List listOfUserByBoundary=personalInfomationDAO.getListOfUsersByBoundaryId(boundaryId);
		return listOfUserByBoundary;
	}
	public List getListOfUsersForGivenBoundaryId(Integer boundaryId) throws NoSuchObjectException
	{
		PersonalInformationDAO personalInfomationDAO =EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
		List listOfUserByBoundary=personalInfomationDAO.getListOfUsersForGivenBoundaryId(boundaryId);
		return listOfUserByBoundary;
	}
	
	//To get a Nominee passing a List of Nominee Id's
	public Map<Long,String> getNomineeByIdMap(List<Long> nomineeIdsList)
	{
		Map<Long,String> nomineeMap=null;
		try {
			Query qry = null;
			
			nomineeMap = new LinkedHashMap<Long, String>();
			String qryString="";
			if(nomineeIdsList!=null && !nomineeIdsList.isEmpty())
			{
			  qryString = "from EmployeeNomineeMaster where id in (:nomineeIdsList)";
			}
			System.out.println(persistenceService);
			qry = persistenceService.getSession().createQuery(qryString);
			List<Long> nomineeList = new ArrayList<Long>();
			if(nomineeIdsList!=null && !nomineeIdsList.isEmpty())
			{
					for (Long nominee : nomineeIdsList) {
						nomineeList.add(nominee);
					}
					qry.setParameterList("nomineeIdsList", nomineeList);
			}
			List<EmployeeNomineeMaster> nomineeObjList = qry.list();
			for (EmployeeNomineeMaster empNom : nomineeObjList) {
				nomineeMap.put(empNom.getId(), empNom.getNomineeName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nomineeMap;
	}
	
	//To get Nominee Master based on Id
	public EmployeeNomineeMaster getNomineeMstrById(Long id)
	{
		Query qry = null;
		String qryString="";
		if(id!=null)
		{
			qryString = "from EmployeeNomineeMaster where id =:id";
			
		}
		qry = persistenceService.getSession().createQuery(qryString);
		if(id!=null)
		{
			qry.setLong("id", id);
		}
		return (EmployeeNomineeMaster)qry.uniqueResult();
		
	}
	
    /** 
     * Purpose : To get the next/prior working date for given date,noOfDays prior or after to given date.Checking with current financial year.
     * @param givenDate
     * @param noOfDays
     * @param priorOrAfter
     * @return
     */
    public Date getPriorOrAfterWorkingDate(Date givenDate,int noOfDays,DATE_ORDER enumtype)
    {
    	Date expectedDate= new Date();
    	try{
    	
    		GregorianCalendar tmpToDate=new GregorianCalendar();
	    	tmpToDate.setTime(givenDate);
	    	if(enumtype.equals(DATE_ORDER.PRIOR))
	    	{
	    		tmpToDate.add(Calendar.DATE, -noOfDays);
	    	}
	    	else if(enumtype.equals(DATE_ORDER.AFTER))
			{
	    		tmpToDate.add(Calendar.DATE,noOfDays);
			}
	    	
	    	Set<String> holidaySet = new HashSet<String>();
	    	
	    	List objDateList = new ArrayList();
	    	if(enumtype.equals(DATE_ORDER.PRIOR))
	    	{
	    		objDateList = EisManagersUtill.getListOfFinYrandDate(new Date(tmpToDate.getTimeInMillis()),givenDate);
	    	}
	    	else if(enumtype.equals(DATE_ORDER.AFTER))
			{
	    		objDateList = EisManagersUtill.getListOfFinYrandDate(givenDate,new Date(tmpToDate.getTimeInMillis()));
			}
	    	
			if (objDateList.size() == 1) 
			{
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				holidaySet = EisManagersUtill.getEmpLeaveService().getHolidaySet(finYrandDateFirst.getFinancial());
			} 
			else if (objDateList.size() == 2)
			{
				DatePeriodFY finYrandDateFirst = (DatePeriodFY) objDateList.get(0);
				DatePeriodFY finYrandDateSecond = (DatePeriodFY) objDateList.get(1);
				holidaySet = EisManagersUtill.getEmpLeaveService().getHolidaySet(finYrandDateFirst.getFinancial());
				holidaySet.addAll(EisManagersUtill.getEmpLeaveService().getHolidaySet(finYrandDateSecond.getFinancial()));
			}
	    	
	    	if(!holidaySet.isEmpty())
	    	{
				int tmpNoOfDays = noOfDays;
		    	for(int i=0;i<tmpNoOfDays;i++)
		    	{
		    		Date tmpExptectedDate = null;
		    		GregorianCalendar tmpCurrDate=new GregorianCalendar();
		    		tmpCurrDate.setTime(givenDate);
		    		if(enumtype.equals(DATE_ORDER.PRIOR)){
		    			tmpCurrDate.add(Calendar.DATE,-(i+1));
		    		}
		    		else if(enumtype.equals(DATE_ORDER.AFTER))
		    		{
		    			tmpCurrDate.add(Calendar.DATE,i+1);
		    		}
		    		
		    		tmpExptectedDate = new Date(tmpCurrDate.getTimeInMillis());
		    		
		    		Iterator it = holidaySet.iterator();
		    		while(it.hasNext())
		    		{
		    			if(new SimpleDateFormat("yyyy-MM-dd").format(tmpExptectedDate).equals(it.next().toString()))
		    			{
		    				tmpNoOfDays = tmpNoOfDays+1;
		    				break;
		    			}
		    		}
		    		expectedDate = tmpExptectedDate;
		    	}
	    	}
    	}catch(Exception e){
    		LOGGER.error("Exception in getPriorOrAfterWorkingDate()=="+ e.getMessage());
    	}
    	
    	return expectedDate;
    }
    /**
     * return all distinct Designations to which employees are assigned in the given department for given date. 
     * This list includes primary as well as secondary assignments.
     * If there is No Designation for the given department then returns the empty list
     * @param departmentId
     * @param givenDate
     * @return DesignationMaster List
     */
    public List<DesignationMaster> getAllDesignationByDept(Integer departmentId, Date givenDate)
    {
    	List<DesignationMaster> designationMstrObj = new ArrayList<DesignationMaster>();
    	Date userGivenDate=givenDate==null?new Date():givenDate;
    	Integer deptId=departmentId;
    		Criteria criteria=persistenceService.getSession().createCriteria(EmployeeView.class,"ev").
	    	createAlias("deptId", "department").add(Restrictions.eq("department.id", deptId))
	    	.add(Restrictions.and(Restrictions.le("ev.fromDate", userGivenDate), Restrictions.ge("ev.toDate", userGivenDate)));
	    	
	    	ProjectionList projections = Projections.projectionList()
	    	.add(Projections.property("ev.desigId"));
	    	criteria.setProjection(projections);
	    	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	    	
	    designationMstrObj=(List<DesignationMaster>)criteria.list();
    	
    	return designationMstrObj;
    	
    	
    }
    
    /**
    * Get all users for the given department and designation id's for the given date
    *@param deptId the Department Id
    *@param desigId the Designation Id
    *@param date the java.util.Date
    *@return List of Users
    */
	public List<User> getUsersByDeptAndDesig(Integer deptId, Integer desigId, Date date) {
		date=date==null?new Date():date;
		Criteria criteria=persistenceService.getSession().createCriteria(EmployeeView.class,"emp")
		.add(Restrictions.eq("deptId.id", deptId))
		.add(Restrictions.eq("desigId.designationId", desigId))
		.add(Restrictions.le("emp.fromDate", date))
		.add(Restrictions.or(Restrictions.ge("emp.toDate", date), Restrictions.isNull("emp.toDate")))
		.add(Restrictions.isNotNull("emp.userMaster"));
		ProjectionList projections = Projections.projectionList().add(Projections.property("emp.userMaster"));
		criteria.setProjection(projections);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
    
    /**
     * 
     * @param empId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<Assignment> getPrimartAssignmentForGivenDateRange(Integer empId,Date fromDate,Date toDate)
    {
    	List<Assignment> assignmentObj = new ArrayList<Assignment>();
    	Criteria criteria=persistenceService.getSession().createCriteria(EmployeeView.class,"emp")
    	.add(Restrictions.eq("emp.isPrimary", 'Y'))
    	.add(Restrictions.eq("emp.id", empId)).add(Restrictions.and(Restrictions.le("emp.fromDate",fromDate), Restrictions.ge("emp.toDate", fromDate)));
    	
    	if(toDate!=null)
    	{
    		criteria.add(Restrictions.and(Restrictions.le("emp.fromDate", toDate), Restrictions.ge("emp.toDate", toDate)));
    	}
    	
    	ProjectionList projections = Projections.projectionList()
    	.add(Projections.property("emp.assignment"));
    	criteria.setProjection(projections);
    	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	assignmentObj = (List<Assignment>)criteria.list();
    	return assignmentObj;
    }
    /**
	 * Based on the ISFILTERBYDEPT flag api returns departmentlist 
	 * if ISFILTERBYDEPT is YES then return Department list for the login user,if the user is hod of depts those depts includes in the list
	 * if ISFILTERBYDEPT is NO then returns all the departments
	 */
	public List<DepartmentImpl> getDeptsForUser()     
	{
		String filterByDept=EGovConfig.getAppConfigValue("EIS-PAYROLL", "FILTERBYDEPT", "NO");
		List<DepartmentImpl> deptlist=null;
    	if(filterByDept!=null && filterByDept.toUpperCase().equals("YES"))  
    	{
    		List<BigDecimal>	deptList= getPersistenceService().findPageByNamedQuery("EMPVIEW-DEPTIDS-LOGGEDINUSER", 0,null,Integer.valueOf(EGOVThreadLocals.getUserId()),Integer.valueOf(EGOVThreadLocals.getUserId())).getList();
    		if(deptList.isEmpty())
    			return Collections.emptyList();
    		else
    		{
	    		List<Integer> deptListInt=new ArrayList<Integer>();
	    		for(BigDecimal deptId:deptList)
	    		{ 
	    			if(deptId!=null)   
	    			{
	    			deptListInt.add(deptId.intValue());       
	    			}
	    		}
	    		deptlist=getPersistenceService().getSession().createCriteria(DepartmentImpl.class).add(Restrictions.in("id", deptListInt)).list();
    		}
    	}
    	else
    	{
    		deptlist=getPersistenceService().getSession().createCriteria(DepartmentImpl.class).list();      		
    	}
    	return deptlist;
	}
	
	public List<User> getListOfUsersByRole(List<String> roleList){
		List<User> userList = new ArrayList<User>();		
		Criteria criteria = persistenceService.getSession().createCriteria(UserRole.class,"userRole")
			.createAlias("userRole.role", "role")
	    	.add(Restrictions.in("role.roleName", roleList));
		
		criteria.add(Restrictions.disjunction()				
				.add(Restrictions.and(Restrictions.le("userRole.fromDate", new Date()), Restrictions.ge("userRole.toDate", new Date())))
				.add(Restrictions.and(Restrictions.le("userRole.fromDate", new Date()), Restrictions.isNull("userRole.toDate"))));				
    		
    	ProjectionList projections = Projections.projectionList().add(Projections.property("userRole.user"));
    	criteria.setProjection(projections);
    	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	userList = (List<User>)criteria.list();
		
		return userList;
	}
	
	public boolean isValidWorkflowUser(Position owner)
	{
		boolean validuser=false;
		List<Position> positions=getPositionsForUser(Integer.valueOf(EGOVThreadLocals.getUserId()), new Date());
		if(positions.contains(owner))
		{
			validuser= true;
		}
		return validuser;
			
	}
	
	/**
	 * @param deptId
	 * @param desigId
	 * @return list of employee view objects who are HODs
	 * 
	 */
	
	public List<EmployeeView> getHODListByDeptAndDesig(Integer deptId,Integer desigId)
	{
		List<EmployeeView> empList = new ArrayList<EmployeeView>();
		String isInHODDept = " empView.assignment.id in (select hodDept.assignment.id from EmployeeDepartment hodDept where hodDept.dept.id=?) ";
		empList = (List<EmployeeView>) persistenceService.findAllBy("select empView from EmployeeView empView where empView.isActive=1 and " +
				" empView.isPrimary='Y' and empView.fromDate<? and empView.toDate>? " +
				" and ((empView.deptId.id=? and empView.desigId.id=? and "+isInHODDept+") or (" +isInHODDept+
				" ))",new Date(),new Date(),deptId,desigId,deptId,deptId);
		
		return empList;
	}
	
    
}