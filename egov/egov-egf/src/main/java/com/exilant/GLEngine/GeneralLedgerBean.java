/*
 * Created on Dec 21, 2005
 * @author Sumit
 */
package com.exilant.GLEngine;

public class GeneralLedgerBean
{
	private String voucherdate;
	private String vouchernumber;
	private String name;
	private String type;
	private String debitamount;
	private String creditamount;
	private String narration;
	private String amount;
	private String fund;
	private String glcode;
	private String CGN;
	private String runningDrCr;
    private String startDate;
    private String endDate;
    //private String fund_id;
    private String fundSource_id;  
    private String code;
    private String accName;
    private String ulbName;
    private String totalCount;
    private String isConfirmedCount;
    private String revEntry; 
    private String accEntityId;
    private String particulars;
    private String accEntityKey;
    private String reportType;
    private String opDr;
    private String opCr;
    private String clCr;
    private String clDr;
    private String tDr;
    private String tCr;
    private String status;
    private String accountCodes;
    private String fund_id;
    private String accountCode;
    private String fundName;
    private String openingBal;
    private String closingBal;
    private String cheques;//used in chequeinhand report as seperate column to display cheque-payeename lists
    private String debitVoucherTypeName;
    private String creditVoucherTypeName;
    private String voucher_name;
    private String voucherName;
    private String dept_name;
    private String deptId;
    private String vhId;
    private String subLedgerTypeName;
    
    public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getDebitVoucherTypeName() {
		return debitVoucherTypeName;
	}
	public void setDebitVoucherTypeName(String debitVoucherTypeName) {
		this.debitVoucherTypeName = debitVoucherTypeName;
	}
	public String getCreditVoucherTypeName() {
		return creditVoucherTypeName;
	}
	public void setCreditVoucherTypeName(String creditVoucherTypeName) {
		this.creditVoucherTypeName = creditVoucherTypeName;
	}
   
	/**
	 * @return Returns the accountCode.
	 */
	public String getAccountCode() {
		return accountCode;
	}
	/**
	 * @param accountCode The accountCode to set.
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
    /*
     * Added by abdulla
     */
    private String creditcode;
    private String creditdate;
    private String debitparticular;
    private String creditparticular;
    private String creditvouchernumber;
    private String glCode1;
    private String snapShotDateTime;
	/**
	 * @return Returns the creditvouchernumber.
	 */
	public String getCreditvouchernumber() {
		return creditvouchernumber;
	}
	/**
	 * @param creditvouchernumber The creditvouchernumber to set.
	 */
	public void setCreditvouchernumber(String creditvouchernumber) {
		this.creditvouchernumber = creditvouchernumber;
	}
	/**
	 * @return Returns the creditparticular.
	 */
	public String getCreditparticular() {
		return creditparticular;
	}
	/**
	 * @param creditparticular The creditparticular to set.
	 */
	public void setCreditparticular(String creditparticular) {
		this.creditparticular = creditparticular;
	}
	/**
	 * @return Returns the debitparticular.
	 */
	public String getDebitparticular() {
		return debitparticular;
	}
	/**
	 * @param debitparticular The debitparticular to set.
	 */
	public void setDebitparticular(String debitparticular) {
		this.debitparticular = debitparticular;
	}
	/**
	 * @return Returns the creditdate.
	 */
	public String getCreditdate() {
		return creditdate;
	}
	/**
	 * @param creditdate The creditdate to set.
	 */
	public void setCreditdate(String creditdate) {
		this.creditdate = creditdate;
	}
   
	/**
	 * @return Returns the creditcode.
	 */
	public String getCreditcode() {
		return creditcode;
	}
	/**
	 * @param creditcode The creditcode to set.
	 */
	public void setCreditcode(String creditcode) {
		this.creditcode = creditcode;
	}
	/**
	 *
	 */
	
	public GeneralLedgerBean()
	{
		voucherdate = "";
		vouchernumber = "";
		name = "";
		type = "";
		debitamount = "";
		creditamount = "";
		narration = "";
		amount = "";
		fund = "";
		glcode = "";
		runningDrCr="";
        startDate="";
        endDate="";
       // fund_id = "";
        fundSource_id = "";       
        code= "";
        accName= "";
        ulbName="";
        totalCount="";
        isConfirmedCount="";
        revEntry="";
        accEntityId="";
        accEntityKey="";
        reportType="";
        opDr="";
        opCr="";
        clCr="";
        clDr="";
        tDr="";
        tCr="";
        status="";
        accountCodes="";
        creditcode="";
        creditdate="";
        debitparticular="";
        creditparticular="";
        creditvouchernumber="";
        snapShotDateTime="";
        this.glCode1 = "";
        fund_id="";
        voucher_name="";
        dept_name="";
        accountCode="";
        fundName="";
        openingBal="";
        closingBal="";
        cheques = "";
        
        

	}


	/**
	 * @return Returns the cGN.
	 */
	public String getCGN() {
		return CGN;
	}
	/**
	 * @param cgn The cGN to set.
	 */
	public void setCGN(String cgn) {
		CGN = cgn;
	}
	/**
	 * @return Returns the amount.
	 */
	public String getAmount()
	{
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	/**
	 * @return Returns the creditamount.
	 */
	public String getCreditamount()
	{
		return creditamount;
	}
	/**
	 * @param creditamount The creditamount to set.
	 */
	public void setCreditamount(String creditamount)
	{
		this.creditamount = creditamount;
	}
	/**
	 * @return Returns the debitamount.
	 */
	public String getDebitamount()
	{
		return debitamount;
	}
	/**
	 * @param debitamount The debitamount to set.
	 */
	public void setDebitamount(String debitamount)
	{
		this.debitamount = debitamount;
	}
	/**
	 * @return Returns the fund.
	 */
	public String getFund()
	{
		return fund;
	}
	/**
	 * @param fund The fund to set.
	 */
	public void setFund(String fund)
	{
		this.fund = fund;
	}
	/**
	 * @return Returns the glcode.
	 */
	public String getGlcode()
	{
		return glcode;
	}
	/**
	 * @param glcode The glcode to set.
	 */
	public void setGlcode(String glcode)
	{
		this.glcode = glcode;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return Returns the narration.
	 */
	public String getNarration()
	{
		return narration;
	}
	/**
	 * @param narration The narration to set.
	 */
	public void setNarration(String narration)
	{
		this.narration = narration;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	/**
	 * @return Returns the voucherdate.
	 */
	public String getVoucherdate()
	{
		return voucherdate;
	}
	/**
	 * @param voucherdate The voucherdate to set.
	 */
	public void setVoucherdate(String voucherdate)
	{
		this.voucherdate = voucherdate;
	}
	/**
	 * @return Returns the vouchernumber.
	 */
	public String getVouchernumber()
	{
		return vouchernumber;
	}
	/**
	 * @param vouchernumber The vouchernumber to set.
	 */
	public void setVouchernumber(String vouchernumber)
	{
		this.vouchernumber = vouchernumber;
	}


		public String getRunningDrCr()
		{
			return runningDrCr;
		}
		public void setRunningDrCr(String runningDrCr)
		{
			this.runningDrCr = runningDrCr;
		}
        
        /**
         * @return Returns the fromDate.
         */
        public String getStartDate()
        {
            return startDate;
        }
        /**
         * @param fromDate The from date.
         */
        public void setStartDate(String startDate)
        {
            this.startDate = startDate;
        }
        /**
         * @return Returns the to date.
         */
        public String getEndDate()
            {
                return endDate;
            }

        /**
         * @param toDate The to date to set.
         */   
        public void setEndDate(String endDate)
            {
                this.endDate = endDate;
            }
        
        /**
         * @return Returns the fund_id.
         */
       /* public String getFund_id() 
        {
        	if(LOGGER.isDebugEnabled())     LOGGER.debug("fund id in bean"+fund_id);
            return fund_id;
        }
        *//**
         * @param fund_id The fund_id to set.
         *//*
        public void setFund_id(String fund_id)
        {
        	if(LOGGER.isDebugEnabled())     LOGGER.debug("fund id set in bean"+fund_id);
            this.fund_id = fund_id;
        }*/
        /**
         * @return Returns the fundSource_id.
         */
        public String getFundSource_id() 
        {
            return fundSource_id;
        }
        /**
         * @param fundSource_id The fundSource_id to set.
         */
        public void setFundSource_id(String fundSource_id) 
        {
            this.fundSource_id = fundSource_id;
        }
               
             
        public String getCode()
        {
            return code;
        }
        
        public void setCode(String code)
        {
            this.code= code;
        }
        
        public String getAccName()
        {
            return accName;
        }
        
        public void setAccName(String accName)
        {
            this.accName= accName;
        }
        
        public String getUlbName()
        {
            return ulbName;
        }
        
        public void setUlbName(String ulbName)
        {
            this.ulbName= ulbName;
        }
        
        public String getTotalCount()
        {
            return  totalCount;
        }
        
        public void setTotalCount(String totalCount)
        {
            this.totalCount= totalCount;
        }
        public String getIsConfirmedCount()
        {
            return isConfirmedCount;
        }
        
        public void setIsConfirmedCount(String isConfirmedCount)
        {
            this.isConfirmedCount= isConfirmedCount;
        }         
        public String getRevEntry()
        {
            return revEntry;
        }
        
        public void setRevEntry(String revEntry)
        {
            this.revEntry= revEntry;
        }   
       
        public String getAccEntityId()
        {
            return accEntityId;
        }
        
        public void setAccEntityId(String accEntityId)
        {
            this.accEntityId= accEntityId;
        } 
        public String getParticulars()
        {
            return particulars;
        }
        
        public void setParticulars(String particulars)
        {
            this.particulars= particulars;
        }   
        public String getAccEntityKey()
        {
            return accEntityKey;
        }
        
        public void setAccEntityKey(String accEntityKey)
        {
            this.accEntityKey= accEntityKey;
        }   
        public String getReportType()
        {
            return reportType;
        }
        
        public void setReportType(String reportType)
        {
            this.reportType= reportType;
        }   
        
        public String getOpDr()
        {
            return opDr;
        }
        
        public void setOpDr(String opDr)
        {
            this.opDr= opDr;
        }  
        
        public String getOpCr()
        {
            return opCr;
        }
        
        public void setOpCr(String opCr)
        {
            this.opCr= opCr;
        } 
        
        public String getClDr()
        {
            return clDr;
        }
        
        public void setClDr(String clDr)
        {
            this.clDr= clDr;
        }  
        public String getClCr()
        {
            return clCr;
        }
        
        public void setClCr(String clCr)
        {
            this.clCr= clCr;
        }  
        
        public String getTDr()
        {
            return tDr;
        }
        
        public void setTDr(String tDr)
        {
            this.tDr= tDr;
        }  
        public String getTCr()
        { 
            return tCr;
        }
        
        public void setTCr(String tCr)
        {
            this.tCr=tCr;
        }  
        
        public String getStatus()
        {
            return status;
        }
        
        public void setStatus(String status)
        {
            this.status=status;
        }  
        public String getOpeningBal() 
        {
        	return openingBal;
        }
        public void setOpeningBal(String openingBal)
        {
        	this.openingBal = openingBal;
        }
        public String getClosingBal()
        {
        	return closingBal;
        }
        public void setClosingBal(String closingBal) 
        {
        	this.closingBal = closingBal;
        }
                
        
        
        
	/**
	 * @return Returns the accountCodes.
	 */
	public String getAccountCodes() {
		return accountCodes;
	}
	/**
	 * @param accountCodes The accountCodes to set.
	 */
	public void setAccountCodes(String accountCodes) {
		this.accountCodes = accountCodes;
	}
	/**
	 * @return Returns the glCode1.
	 */
	public String getGlCode1() {
		return glCode1;
	}
	/**
	 * @param glCode1 The glCode1 to set.
	 */
	public void setGlCode1(String glCode1) {
		this.glCode1 = glCode1;
	}
	/**
	 * @return Returns the snapShotDateTime.
	 */
	public String getSnapShotDateTime() {
		return snapShotDateTime;
	}
	/**
	 * @param snapShotDateTime The snapShotDateTime to set.
	 */
	public void setSnapShotDateTime(String snapShotDateTime) {
		this.snapShotDateTime = snapShotDateTime;
	}
	/**
	 * @return Returns the fund_id.
	 */
	public String getFund_id() {
		return fund_id;
	}
	/**
	 * @param fund_id The fund_id to set.
	 */
	public void setFund_id(String fund_id) {
		this.fund_id = fund_id;
	}
	/**
	 * @return Returns the voucher_name
	 */
	public String getVoucher_name() {
		return voucher_name;
	}
	/**
	 * @param voucher_name The voucher_name to set
	 */
	public void setVoucher_name(String voucher_name) {
		this.voucher_name = voucher_name;
	}
	/**
	 * @return Returns the fundName.
	 */
	public String getFundName() {
		return fundName;
	}
	/**
	 * @param fundName The fundName to set.
	 */
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getCheques() {
		return cheques;
	}
	public void setCheques(String cheques) {
		this.cheques = cheques;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getVhId() {
		return vhId;
	}
	public void setVhId(String vhId) {
		this.vhId = vhId;
	}
	public String getSubLedgerTypeName() {
		return subLedgerTypeName;
	}
	public void setSubLedgerTypeName(String subLedgerTypeName) {
		this.subLedgerTypeName = subLedgerTypeName;
	}
	
	
	
}