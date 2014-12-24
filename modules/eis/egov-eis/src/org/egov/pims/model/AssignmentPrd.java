package org.egov.pims.model;

// Generated Aug 8, 2007 6:26:41 PM by Hibernate Tools 3.2.0.b9


import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * AssignmentPrd generated by hbm2java
 */
public class AssignmentPrd implements java.io.Serializable,Comparable
{

	private Integer id;

	private Date fromDate;
	private PersonalInformation employeeId;

	private Date toDate;
private Set<org.egov.pims.model.Assignment> egpimsAssignment = new HashSet<org.egov.pims.model.Assignment>(
			0);

	public AssignmentPrd()
	{
	}



	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Date getFromDate()
	{
		return this.fromDate;
	}

	public void setFromDate(Date fromDate)
	{
		this.fromDate = fromDate;
	}

	public Date getToDate()
	{
		return this.toDate;
	}

	public void setToDate(Date toDate)
	{
		this.toDate = toDate;
	}
	public void addAssignment(Assignment egEmpAssignment)
	{
		if(getEgpimsAssignment()!=null)
			getEgpimsAssignment().add(egEmpAssignment);
	}
	public void removeAssignment(Assignment egEmpAssignment)
	{
		getEgpimsAssignment().remove(egEmpAssignment);
	}

/**
 * @return Returns the egpimsAssignment.
 */
public Set<org.egov.pims.model.Assignment> getEgpimsAssignment() {
	return egpimsAssignment;
}
/**
 * @param egpimsAssignment The egpimsAssignment to set.
 */
public void setEgpimsAssignment(
		Set<org.egov.pims.model.Assignment> egpimsAssignment) {
	this.egpimsAssignment = egpimsAssignment;
}



public PersonalInformation getEmployeeId() {
	return employeeId;
}



public void setEmployeeId(PersonalInformation employeeId) {
	this.employeeId = employeeId;
}

public static Comparator assignPrdComparator = new Comparator() {
	public int compare(Object empAssignment, Object anotherEmpAssignment) {
		Date assFromDate1 = ((AssignmentPrd)empAssignment).getFromDate();
		Date assFromDate2 = ((AssignmentPrd)anotherEmpAssignment).getFromDate();
		return assFromDate1.compareTo(assFromDate2);
	}
};

public int compareTo(Object anotherAssignment) throws ClassCastException {
    if (!(anotherAssignment instanceof AssignmentPrd))
      throw new ClassCastException("A Assignment object expected.");
    Integer assignmentId = ((AssignmentPrd) anotherAssignment).getId();  
    return this.id.compareTo(assignmentId);  
  }

}