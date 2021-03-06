package org.egov.payment.model;

// Generated Mar 10, 2008 12:54:41 PM by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;

import org.egov.billsaccounting.model.Contractorbilldetail;
import org.egov.billsaccounting.model.Salarybilldetail;
import org.egov.billsaccounting.model.Supplierbilldetail;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Chequedetail;
import org.egov.commons.Relation;


/**
 * Subledgerpaymentheader generated by hbm2java
 */
public class Subledgerpaymentheader implements java.io.Serializable
{

	private Integer id;

	private Chequedetail chequedetail;

	private Worksdetail worksdetail;

	private CVoucherHeader voucherheader;

	private Relation relationBySupplierid;

	private Relation relationByContractorid;

	private Supplierbilldetail supplierbilldetail;

	private Salarybilldetail salarybilldetail;

	private String type;

	private String typeofpayment;

	private Bankaccount bankaccount;

	private Contractorbilldetail contractorbilldetail;

	private String cashname;

	private BigDecimal paidby;

	private BigDecimal paidamount;

	private String narration;

	private Boolean isreversed;

	private BigDecimal paidto;

	public Subledgerpaymentheader()
	{
	}

	public Subledgerpaymentheader(Integer id, CVoucherHeader voucherheader,
			String type, String typeofpayment, BigDecimal paidamount)
	{
		this.id = id;
		this.voucherheader = voucherheader;
		this.type = type;
		this.typeofpayment = typeofpayment;
		this.paidamount = paidamount;
	}

	public Subledgerpaymentheader(Integer id, Chequedetail chequedetail,
			Worksdetail worksdetail, CVoucherHeader voucherheader,
			Relation relationBySupplierid, Relation relationByContractorid,
			Supplierbilldetail supplierbilldetail,
			Salarybilldetail salarybilldetail, String type,
			String typeofpayment, Bankaccount bankaccount,
			Contractorbilldetail contractorbilldetail, String cashname, BigDecimal paidby,
			BigDecimal paidamount, String narration, Boolean isreversed,
			BigDecimal paidto)
	{
		this.id = id;
		this.chequedetail = chequedetail;
		this.worksdetail = worksdetail;
		this.voucherheader = voucherheader;
		this.relationBySupplierid = relationBySupplierid;
		this.relationByContractorid = relationByContractorid;
		this.supplierbilldetail = supplierbilldetail;
		this.salarybilldetail = salarybilldetail;
		this.type = type;
		this.typeofpayment = typeofpayment;
		this.bankaccount = bankaccount;
		this.contractorbilldetail = contractorbilldetail;
		this.cashname = cashname;
		this.paidby = paidby;
		this.paidamount = paidamount;
		this.narration = narration;
		this.isreversed = isreversed;
		this.paidto = paidto;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Chequedetail getChequedetail()
	{
		return this.chequedetail;
	}

	public void setChequedetail(Chequedetail chequedetail)
	{
		this.chequedetail = chequedetail;
	}

	public Worksdetail getWorksdetail()
	{
		return this.worksdetail;
	}

	public void setWorksdetail(Worksdetail worksdetail)
	{
		this.worksdetail = worksdetail;
	}

	public CVoucherHeader getVoucherheader()
	{
		return this.voucherheader;
	}

	public void setVoucherheader(CVoucherHeader voucherheader)
	{
		this.voucherheader = voucherheader;
	}

	public Relation getRelationBySupplierid()
	{
		return this.relationBySupplierid;
	}

	public void setRelationBySupplierid(Relation relationBySupplierid)
	{
		this.relationBySupplierid = relationBySupplierid;
	}

	public Relation getRelationByContractorid()
	{
		return this.relationByContractorid;
	}

	public void setRelationByContractorid(Relation relationByContractorid)
	{
		this.relationByContractorid = relationByContractorid;
	}

	public Supplierbilldetail getSupplierbilldetail()
	{
		return this.supplierbilldetail;
	}

	public void setSupplierbilldetail(Supplierbilldetail supplierbilldetail)
	{
		this.supplierbilldetail = supplierbilldetail;
	}

	public Salarybilldetail getSalarybilldetail()
	{
		return this.salarybilldetail;
	}

	public void setSalarybilldetail(Salarybilldetail salarybilldetail)
	{
		this.salarybilldetail = salarybilldetail;
	}

	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTypeofpayment()
	{
		return this.typeofpayment;
	}

	public void setTypeofpayment(String typeofpayment)
	{
		this.typeofpayment = typeofpayment;
	}

	public Bankaccount getBankaccount()
	{
		return this.bankaccount;
	}

	public void setBankaccount(Bankaccount bankaccount)
	{
		this.bankaccount = bankaccount;
	}

	public Contractorbilldetail getContractorbilldetail()
	{
		return this.contractorbilldetail;
	}

	public void setContractorbilldetail(Contractorbilldetail contractorbilldetail)
	{
		this.contractorbilldetail = contractorbilldetail;
	}

	public String getCashname()
	{
		return this.cashname;
	}

	public void setCashname(String cashname)
	{
		this.cashname = cashname;
	}

	public BigDecimal getPaidby()
	{
		return this.paidby;
	}

	public void setPaidby(BigDecimal paidby)
	{
		this.paidby = paidby;
	}

	public BigDecimal getPaidamount()
	{
		return this.paidamount;
	}

	public void setPaidamount(BigDecimal paidamount)
	{
		this.paidamount = paidamount;
	}

	public String getNarration()
	{
		return this.narration;
	}

	public void setNarration(String narration)
	{
		this.narration = narration;
	}

	public Boolean getIsreversed()
	{
		return this.isreversed;
	}

	public void setIsreversed(Boolean isreversed)
	{
		this.isreversed = isreversed;
	}

	public BigDecimal getPaidto()
	{
		return this.paidto;
	}

	public void setPaidto(BigDecimal paidto)
	{
		this.paidto = paidto;
	}

}
