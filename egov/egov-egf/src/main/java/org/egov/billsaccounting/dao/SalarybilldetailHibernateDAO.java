/*
 * SalarybillHibernateDAO.java Created on Mar 11, 2008
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.billsaccounting.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.billsaccounting.model.Salarybilldetail;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish P
 * @version 1.00 
 */

public class SalarybilldetailHibernateDAO extends GenericHibernateDAO
{

	/**
	 * @param persistentClass
	 * @param session
	 */
	public SalarybilldetailHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
		// TODO Auto-generated constructor stub
	}

	public List<Salarybilldetail> getSalarybilldetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, String month, String vhNoFrom, String vhNoTo,Department dept,String functionary)
    {
    	Query qry;
        StringBuffer qryStr = new StringBuffer();
        List<Salarybilldetail> salBillList=null;
        qryStr.append(" from Salarybilldetail sb where sb.voucherheader.status=0 AND sb.netpay > sb.paidamount");
										       
        if(fundId!=null)
        {
            qryStr.append(" and sb.voucherheader.fundId=:fundId");           
        } 	
        
        if(vhFromDate!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherDate >=:vhFromDate");
        }
        if(vhToDate!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherDate <=:vhToDate");
        }
        if(vhNoFrom!=null && vhNoTo!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherNumber between :vhNoFrom and :vhNoTo");
        } 
        else if(vhNoFrom!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherNumber >=:vhNoFrom");
        }
    	else if(vhNoTo!=null)
    	{
            qryStr.append(" and sb.voucherheader.voucherNumber <=:vhNoTo");
        }
    	if(fundSource!=null)
        {
            qryStr.append(" and sb.egBillregister.egBillregistermis.fundsource=:fundSource");           
        } 
    	if(month!=null)
        { 
            qryStr.append(" and sb.mmonth=:month");
        }
    	if(dept!=null)
    	{
    		qryStr.append(" and sb.egBillregister.egBillregistermis.egDepartment=:dept");
    	}
    	if(functionary!=null)
    	{
    		qryStr.append(" and sb.egBillregister.egBillregistermis.functionaryid=:functionary");
    	}
    		
    		
        qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        if(fundId!=null)
        	qry.setInteger("fundId",fundId);       
        if(vhFromDate!=null)
        	qry.setDate("vhFromDate", vhFromDate);
        if(vhToDate!=null)
        	qry.setDate("vhToDate", vhToDate);
        if(fundSource!=null)
        	qry.setEntity("fundSource",fundSource);
        if(month!=null)
        	qry.setString("month",month);
        if(vhNoFrom!=null && vhNoTo!=null)
        {
        	qry.setString("vhNoFrom", vhNoFrom);
        	qry.setString("vhNoTo", vhNoTo);
        }
    	else if(vhNoFrom!=null)
    		qry.setString("vhNoFrom", vhNoFrom);
    	else if(vhNoTo!=null)
    		qry.setString("vhNoTo", vhNoTo);
        if(dept!=null)
    	{
    		qry.setEntity("dept", dept);
    	}
    	if(functionary!=null)
    	{
    		qry.setBigDecimal("functionary", new BigDecimal(functionary));
    	}
        salBillList=qry.list();
        return salBillList;
    }
	
}

