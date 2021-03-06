package org.egov.billsaccounting.model;

// Generated Mar 6, 2008 11:33:35 AM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;
import java.util.Date;
import org.egov.commons.CVoucherHeader; 
import org.egov.model.bills.EgBillregister;
import org.egov.commons.Relation;


/**
 * Supplierbilldetail generated by hbm2java
 */
public class Supplierbilldetail implements java.io.Serializable
{

	private Integer id;  
	
	

	private CVoucherHeader voucherheader;

	private Relation relation;

	private EgBillregister egBillregister; 

	private Date billdate;

	private String billnumber;

	private BigDecimal otherrecoveries=new BigDecimal(0);

	private String mrnnumber;

	private Date mrndate;

	private BigDecimal billamount=new BigDecimal(0);

	private BigDecimal passedamount=new BigDecimal(0);

	private String approvedby;

	private BigDecimal payableaccount;

	private String narration;

	private Integer worksdetailid;

	private BigDecimal tdsamount=new BigDecimal(0);

	private Boolean tdspaidtoit;

	private BigDecimal paidamount=new BigDecimal(0);

	private BigDecimal advadjamt=new BigDecimal(0);

	private Boolean isreversed;

	private BigDecimal assetid;

	private BigDecimal capRev=new BigDecimal(0);

	private BigDecimal mrnid;

	private Date paybydate;

	public Supplierbilldetail()
	{
	}

	public Supplierbilldetail(Integer id, CVoucherHeader voucherheader,
			Relation relation, BigDecimal billamount, BigDecimal passedamount,
			Integer worksdetailid)
	{
		this.id = id;
		this.voucherheader = voucherheader;
		this.relation = relation;
		this.billamount = billamount;
		this.passedamount = passedamount;
		this.worksdetailid = worksdetailid;
	}

	public Supplierbilldetail(Integer id, CVoucherHeader voucherheader,
			Relation relation, EgBillregister egBillregister, Date billdate,
			String billnumber, BigDecimal otherrecoveries, String mrnnumber,
			Date mrndate, BigDecimal billamount, BigDecimal passedamount,
			String approvedby, BigDecimal payableaccount, String narration,
			Integer worksdetailid, BigDecimal tdsamount,
			Boolean tdspaidtoit, BigDecimal paidamount, BigDecimal advadjamt,
			Boolean isreversed, BigDecimal assetid, BigDecimal capRev,
			BigDecimal mrnid, Date paybydate)
	{
		this.id = id;
		this.voucherheader = voucherheader;
		this.relation = relation;
		this.egBillregister = egBillregister;
		this.billdate = billdate;
		this.billnumber = billnumber;
		this.otherrecoveries = otherrecoveries;
		this.mrnnumber = mrnnumber;
		this.mrndate = mrndate;
		this.billamount = billamount;
		this.passedamount = passedamount;
		this.approvedby = approvedby;
		this.payableaccount = payableaccount;
		this.narration = narration;
		this.worksdetailid = worksdetailid;
		this.tdsamount = tdsamount;
		this.tdspaidtoit = tdspaidtoit;
		this.paidamount = paidamount;
		this.advadjamt = advadjamt;
		this.isreversed = isreversed;
		this.assetid = assetid;
		this.capRev = capRev;
		this.mrnid = mrnid;
		this.paybydate = paybydate;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public CVoucherHeader getVoucherheader()
	{
		return this.voucherheader;
	}

	public void setVoucherheader(CVoucherHeader voucherheader)
	{
		this.voucherheader = voucherheader;
	}

	public Relation getRelation()
	{
		return this.relation;
	}

	public void setRelation(Relation relation)
	{
		this.relation = relation;
	}

	public EgBillregister getEgBillregister()
	{
		return this.egBillregister;
	}

	public void setEgBillregister(EgBillregister egBillregister)
	{
		this.egBillregister = egBillregister;
	}

	public Date getBilldate()
	{
		return this.billdate;
	}

	public void setBilldate(Date billdate)
	{
		this.billdate = billdate;
	}

	public String getBillnumber()
	{
		return this.billnumber;
	}

	public void setBillnumber(String billnumber)
	{
		this.billnumber = billnumber;
	}

	public BigDecimal getOtherrecoveries()
	{
		return this.otherrecoveries;
	}

	public void setOtherrecoveries(BigDecimal otherrecoveries)
	{
		this.otherrecoveries = otherrecoveries;
	}

	public String getMrnnumber()
	{
		return this.mrnnumber;
	}

	public void setMrnnumber(String mrnnumber)
	{
		this.mrnnumber = mrnnumber;
	}

	public Date getMrndate()
	{
		return this.mrndate;
	}

	public void setMrndate(Date mrndate)
	{
		this.mrndate = mrndate;
	}

	public BigDecimal getBillamount()
	{
		return this.billamount;
	}

	public void setBillamount(BigDecimal billamount)
	{
		this.billamount = billamount;
	}

	public BigDecimal getPassedamount()
	{
		return this.passedamount;
	}

	public void setPassedamount(BigDecimal passedamount)
	{
		this.passedamount = passedamount;
	}

	public String getApprovedby()
	{
		return this.approvedby;
	}

	public void setApprovedby(String approvedby)
	{
		this.approvedby = approvedby;
	}

	public BigDecimal getPayableaccount()
	{
		return this.payableaccount;
	}

	public void setPayableaccount(BigDecimal payableaccount)
	{
		this.payableaccount = payableaccount;
	}

	public String getNarration()
	{
		return this.narration;
	}

	public void setNarration(String narration)
	{
		this.narration = narration;
	}

	public Integer getWorksdetailid()
	{
		return this.worksdetailid;
	}
  
	public void setWorksdetailid(Integer worksdetailid)
	{
		this.worksdetailid = worksdetailid;
	}

	public BigDecimal getTdsamount()
	{
		return this.tdsamount;
	}

	public void setTdsamount(BigDecimal tdsamount)
	{
		this.tdsamount = tdsamount;
	}

	public Boolean getTdspaidtoit()
	{
		return this.tdspaidtoit;
	}

	public void setTdspaidtoit(Boolean tdspaidtoit)
	{
		this.tdspaidtoit = tdspaidtoit;
	}

	public BigDecimal getPaidamount()
	{
		return this.paidamount;
	}

	public void setPaidamount(BigDecimal paidamount)
	{
		this.paidamount = paidamount;
	}

	public BigDecimal getAdvadjamt()
	{
		return this.advadjamt;
	}

	public void setAdvadjamt(BigDecimal advadjamt)
	{
		this.advadjamt = advadjamt;
	}

	public Boolean getIsreversed()
	{
		return this.isreversed;
	}

	public void setIsreversed(Boolean isreversed)
	{
		this.isreversed = isreversed;
	}

	public BigDecimal getAssetid()
	{
		return this.assetid;
	}

	public void setAssetid(BigDecimal assetid)
	{
		this.assetid = assetid;
	}

	public BigDecimal getCapRev()
	{
		return this.capRev;
	}

	public void setCapRev(BigDecimal capRev)
	{
		this.capRev = capRev;
	}

	public BigDecimal getMrnid()
	{
		return this.mrnid;
	}

	public void setMrnid(BigDecimal mrnid)
	{
		this.mrnid = mrnid;
	}

	public Date getPaybydate()
	{
		return this.paybydate;
	}

	public void setPaybydate(Date paybydate)
	{
		this.paybydate = paybydate;
	}

	
	
}
