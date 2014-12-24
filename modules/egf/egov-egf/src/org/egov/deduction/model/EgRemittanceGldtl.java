package org.egov.deduction.model;

// Generated Oct 3, 2007 7:28:41 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;
import java.util.Date;
import org.egov.model.recoveries.Recovery;


/**
 * EgRemittanceGldtl generated by hbm2java
 */
public class EgRemittanceGldtl implements java.io.Serializable
{

	private Integer id; 

	private Generalledgerdetail generalledgerdetail;

	private BigDecimal gldtlamt;

	private Date lastmodifieddate;
	
	private BigDecimal remittedamt;

	private Recovery recovery;
	
	public EgRemittanceGldtl()
	{
	}

	public EgRemittanceGldtl(Generalledgerdetail generalledgerdetail)
	{
		this.generalledgerdetail = generalledgerdetail;		
	}

	public EgRemittanceGldtl(Generalledgerdetail generalledgerdetail,
			BigDecimal gldtlamt,Date lastmodifieddate, BigDecimal remittedamt, Recovery recovery)
	{
		this.generalledgerdetail = generalledgerdetail;		
		this.gldtlamt = gldtlamt;
		this.lastmodifieddate = lastmodifieddate;
		this.remittedamt = remittedamt;
		this.recovery = recovery;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Generalledgerdetail getGeneralledgerdetail()
	{
		return this.generalledgerdetail;
	}

	public void setGeneralledgerdetail(Generalledgerdetail generalledgerdetail)
	{
		this.generalledgerdetail = generalledgerdetail;
	}
	
	public BigDecimal getGldtlamt()
	{
		return this.gldtlamt;
	}

	public void setGldtlamt(BigDecimal gldtlamt)
	{
		this.gldtlamt = gldtlamt;
	}

	public Date getLastmodifieddate()
	{
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Date lastmodifieddate)
	{
		this.lastmodifieddate = lastmodifieddate;
	}

	/**
	 * @return the remittedamt
	 */
	public BigDecimal getRemittedamt()
	{
		return remittedamt;
	}

	/**
	 * @param remittedamt the remittedamt to set
	 */
	public void setRemittedamt(BigDecimal remittedamt)
	{
		this.remittedamt = remittedamt;
	}

	public Recovery getRecovery() {
		return recovery;
	}

	public void setRecovery(Recovery recovery) {
		this.recovery = recovery;
	}

}