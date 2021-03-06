package org.egov.dao.bills;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;

public abstract class BillsDaoFactory {
	public BillsDaoFactory()
	{
		super();
	}
	private static final Logger logger = Logger.getLogger(BillsDaoFactory.class);

    private static final BillsDaoFactory EJB3_PERSISTENCE = null;

    private static final BillsDaoFactory HIBERNATE = new BillsHibernateDaoFactory ();

    private static final BillsDaoFactory retFac = resolveDAOFactory();

    public static BillsDaoFactory getDAOFactory()
    {
    	if(logger.isDebugEnabled())     logger.debug("getDAOFactory called."+retFac);
    	return retFac;
    }

    private static BillsDaoFactory resolveDAOFactory()
    {
    	
    	if(logger.isDebugEnabled())     logger.debug("Resolving CommonsDaoFactory.");
    	String method = EGovConfig.getProperty("COMMONS-FACTORY-IMPL","HIBERNATE","PTIS");
    	if(logger.isDebugEnabled())     logger.debug("Factory implementation name:"+method+".");
    	if(method != null)
    	{
    		if(method.trim().equalsIgnoreCase("HIBERNATE"))
    		{
    			if(logger.isDebugEnabled())     logger.debug("Returning Hibernate Implementation of CommonsDaoFactory");
    			return HIBERNATE;
    		}
    		else
    		{
    			if(logger.isDebugEnabled())     logger.debug("Returning EJB3_PERSISTENCE Implementation of CommonsDaoFactory");
    			return EJB3_PERSISTENCE;
    		}
    	}
    	if(logger.isDebugEnabled())     logger.debug("Returning null. CommonsDaoFactory could not be resolved.");
    	return  null;
    }
    
    public abstract EgBillRegisterHibernateDAO getEgBillRegisterHibernateDAO();
    public abstract EgBilldetailsDAO getEgBilldetailsDAO();   
}
