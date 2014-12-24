package org.egov.payroll.model;

// Generated Aug 28, 2007 7:45:39 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.EISServeable;
import org.egov.masters.model.BillNumberMaster;
import org.egov.model.bills.EgBillregister;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.utils.EisManagersUtill;
/**
 * EgpayEmppayroll generated by hbm2java
 */
public class EmpPayroll extends StateAware implements java.io.Serializable ,Comparable{

	//private Long id;

	private CFinancialYear financialyear;

	private EgBillregister billRegister;

	private Assignment empAssignment;

	private PersonalInformation employee;

	private BigDecimal grossPay;

	private BigDecimal netPay;

	//private User createdby;

	//private Date createddate;

	private Double numdays;

	private BigDecimal month;

	private String exceptionComments;

	private EgwStatus status;

	private Set<Earnings> earningses = new HashSet(0);

	private Set<Deductions> deductionses = new HashSet(0);

	private BigDecimal totalDeductions ;

	private BigDecimal totalEarnings ;

	private String rejectComment;

	private BigDecimal basicPay;

	private PayTypeMaster payType;

	private Date fromDate;

	private Date toDate;

	private Double workingDays=0.0;

	private String modifyRemarks;

	private Position approverPos;
	private String userName;

	//private Date lastmodifieddate;
	
	private BillNumberMaster billNumber;

	public EmpPayroll() {
	}

	public EmpPayroll(EgBillregister billRegister,
				Assignment empAssignment, PersonalInformation employee,
				BigDecimal grossPay, BigDecimal netPay,
				Date createddate, CFinancialYear financialyear, Double numdays,
				BigDecimal month,EgwStatus status,PayTypeMaster payType,Date fromDate,Date toDate,Double workingDays,BigDecimal basicPay) {
			//this.id = id;
			this.billRegister = billRegister;
			this.empAssignment = empAssignment;
			this.employee = employee;
			this.grossPay = grossPay;
			this.netPay = netPay;
			//this.createdby = createdby;
			//this.createddate = createddate;
			this.financialyear = financialyear;
			this.numdays = numdays;
			this.month = month;
			this.status = status;
			this.basicPay=basicPay;
			this.payType = payType ;
			this.fromDate=fromDate;
			this.toDate=toDate;
			this.workingDays=workingDays;
	}

	public CFinancialYear getFinancialyear() {
		return this.financialyear;
	}

	public void setFinancialyear(CFinancialYear financialyear) {
		this.financialyear = financialyear;
	}


	public EgBillregister getBillRegister() {
		return this.billRegister;
	}

	public void setBillRegister(EgBillregister billRegister) {
		this.billRegister = billRegister;
	}

	public Assignment getEmpAssignment() {
		return this.empAssignment;
	}

	public void setEmpAssignment(Assignment empAssignment) {
		this.empAssignment = empAssignment;
	}

	public PersonalInformation getEmployee() {
		return this.employee;
	}

	public void setEmployee(PersonalInformation employee) {
		this.employee = employee;
	}

	public BigDecimal getGrossPay() {
		return this.grossPay;
	}

	public void setGrossPay(BigDecimal grossPay) {
		this.grossPay = grossPay;
	}

	public BigDecimal getNetPay() {
		return this.netPay;
	}

	public void setNetPay(BigDecimal netPay) {
		this.netPay = netPay;
	}

	/*public User getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(User createdby) {
		this.createdby = createdby;
	}*/

	/*public Date getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}*/

	public double getNumdays() {
		return this.numdays;
	}

	public void setNumdays(Double numdays) {
		this.numdays = numdays;
	}

	public BigDecimal getMonth() {
		return this.month;
	}

	public void setMonth(BigDecimal month) {
		this.month = month;
	}

	public String getExceptionComments() {
		return this.exceptionComments;
	}

	public void setExceptionComments(String exceptionComments) {
		this.exceptionComments = exceptionComments;
	}

	public EgwStatus getStatus() {
		return this.status;
	}

	public void setStatus(EgwStatus status) {
		this.status = status;
	}

	public Set<Earnings> getEarningses() {
		return this.earningses;
	}

	public void setEarningses(Set<Earnings> earningses) {
		this.earningses = earningses;
	}
	/**
 	 * Adds the Earnings attached to the Employee payroll
 	 * @param earningses
 	 * @return
 	 */
 	public Set addEarnings(Earnings earningses)
 	{
 		getEarningses().add(earningses);
 		return this.earningses;
 	}

	public String getRejectComment() {
		return rejectComment;
	}

	public void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}

	public Set<Deductions> getDeductionses() {
		return this.deductionses;
	}

	public void setDeductionses(Set<Deductions> deductionses) {
		this.deductionses = deductionses;
	}

	/**
 	 * Adds the Deductions attached to the Employee payroll
 	 * @param deductionses
 	 * @return
 	 */
 	public Set addDeductions(Deductions deductionses)
 	{
 		getDeductionses().add(deductionses);
 		return this.deductionses;
 	}


 	public BigDecimal getTotalDeductions() {
 		this.totalDeductions = BigDecimal.ZERO;
		for(Deductions deduction : this.deductionses){
			this.totalDeductions = this.totalDeductions.add(deduction.getAmount());
		}
		return this.totalDeductions;
	}

 	public BigDecimal getTotalEarnings() {
 		this.totalEarnings = BigDecimal.ZERO;
		for(Earnings earning : this.earningses){
			this.totalEarnings = this.totalEarnings.add(earning.getAmount());
		}
		return this.totalEarnings;
	}

	public void setTotalDeductions(BigDecimal totalDeductions) {
		this.totalDeductions = totalDeductions;
	}

	public BigDecimal getBasicPay() {
		return basicPay;
	}

	public void setBasicPay(BigDecimal basicPay) {
		this.basicPay = basicPay;
	}


	public int compareTo(Object anotherPerson) throws ClassCastException {
	    if (!(anotherPerson instanceof EmpPayroll))
	    {
	    	throw new ClassCastException("A EmpPayroll object expected.");
	    }
	    String anotherEmpCode = ((EmpPayroll) anotherPerson).getEmployee().getEmployeeCode();
	    return this.employee.getEmployeeCode().compareTo(anotherEmpCode);
	  }

	public static Comparator EmpNameComparator = new Comparator() {
	    public int compare(Object empPayroll, Object anotherEmppayroll) {
	      String empName1 = ((EmpPayroll) empPayroll).getEmployee().getEmployeeFirstName().toUpperCase();
	      String empName2 = ((EmpPayroll) anotherEmppayroll).getEmployee().getEmployeeFirstName().toUpperCase();
          return empName1.compareTo(empName2);

	    }
	 };

	public static Comparator DesignationComparator = new Comparator() {
		public int compare(Object empPayroll, Object anotherEmpPayroll) {
			String empDesg1 = ((EmpPayroll)empPayroll).getEmpAssignment().getDesigId().getDesignationName();
			String empDesg2 = ((EmpPayroll)anotherEmpPayroll).getEmpAssignment().getDesigId().getDesignationName();
			return empDesg1.compareTo(empDesg2);
		}
	};

	public PayTypeMaster getPayType() {
		return payType;
	}

	public void setPayType(PayTypeMaster payType) {
		this.payType = payType;
	}


///		following mwthods for JBPM workflow		\\\\\\\\\\

	public String getCode()
	{
		return this.employee.getEmployeeCode().toString();
	}

	public String getName()
	{
		return this.employee.getEmployeeName();
	}

	public String getDesignation()
	{
		return this.empAssignment.getDesigId().getDesignationName();
	}

	public String getFund()
	{
		if(this.empAssignment.getFundId() != null)
			{
			return this.empAssignment.getFundId().getName();
			}
		else
			{
			return "";
			}
	}

	public String getFunction()
	{
		if(this.empAssignment.getFunctionId() != null)
			{
			return this.empAssignment.getFunctionId().getName();
			}
		else
			{
			return "";
			}
	}

	public String getField()
	{
		return "";
	}

	public String getGross()
	{
		return this.grossPay.toString();
	}

	public String getDeduction()
	{
		if(getTotalDeductions() != null)
		{
			return getTotalDeductions().toString();
		}
		else{
			return "";
		}


	}

	public String getNetpay()
	{
		return this.netPay.toString();
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Double getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(Double workingDays) {
		this.workingDays = workingDays;
	}

	public String getModifyRemarks() {
		return modifyRemarks;
	}

	public void setModifyRemarks(String modifyRemarks) {
		this.modifyRemarks = modifyRemarks;
	}

	/*public Date getLastmodifieddate() {
		return lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}	*/

	public void setNetPay() {
		this.netPay = this.getTotalEarnings().subtract(this.getTotalDeductions());
	}

	@Override
	public String getStateDetails() {
		String month = EisManagersUtill.getMonthsStrVsDays(this.month.intValue());
		String groupingByComb;
		groupingByComb = month + " " + this.financialyear.getFinYearRange() + "-" + this.empAssignment.getDeptId().getDeptName() ;
	 	String functionaryGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTIONARY",new Date()).getValue();
	 	if("true".equalsIgnoreCase(functionaryGroup)){
	 		groupingByComb = groupingByComb  + "-" + this.empAssignment.getFunctionary().getName();
	 	}
	 	String fundGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUND",new Date()).getValue();
	 	if("true".equalsIgnoreCase(fundGroup)){
	 		groupingByComb = groupingByComb  + "-" + this.empAssignment.getFundId().getName();
	 	}
	 	String functionGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTION",new Date()).getValue();
	 	if("true".equalsIgnoreCase(functionGroup)){
	 		groupingByComb = groupingByComb  + "-" + this.empAssignment.getFunctionId().getName();
	 	}
	 	String billNumberGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_BILL_NUMBER",new Date()).getValue();
	 	if("true".equalsIgnoreCase(billNumberGroup)){
	 		groupingByComb = groupingByComb  + "-" +this.billNumber.getBillNumber();
	 	}
		return groupingByComb;
	}

	public Position getApproverPos() {
		return approverPos;
	}

	public void setApproverPos(Position approverPos) {
		this.approverPos = approverPos;
	}

	public BillNumberMaster getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(BillNumberMaster billNumber) {
		this.billNumber = billNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	
/*	@Override
	public String myLinkId() {
		return this.month.toString()+"-"+this.financialyear.getFinYearRange()+"-"+this.empAssignment.getDeptId().getDeptName()+"MyLink";
	}*/

	
}