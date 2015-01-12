/*
 * @(#)ActionHibernateDAO.java 3.0, 14 Jun, 2013 5:43:04 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.Action;
import org.hibernate.Query;
import org.hibernate.Session;

public class ActionHibernateDAO extends GenericHibernateDAO implements ActionDAO {
	private static Logger LOGGER = LoggerFactory.getLogger(ActionHibernateDAO.class);

	public ActionHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public ActionHibernateDAO() {
		super(Action.class, null);
	}

	@Override
	public Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	@Override
	public Action findActionByName(final String name) {
		final Query qry = this.getSession().createQuery("from org.egov.lib.rrbac.model.Action act where act.name =:name ");
		qry.setString("name", name);
		return (Action) qry.uniqueResult();
	}

	private List<Action> getQueryResult(String query, final String contextPath, final String url, final boolean hasContextPath) {
		if (hasContextPath) {
			query = query + " and upper(contextRoot) like :contextPath";
		}
		final Query qry = this.getSession().createQuery(query + "  order by urlOrderId desc");
		qry.setString("fullURL", url);
		if (hasContextPath) {
			qry.setString("contextPath", contextPath);
		}
		return qry.list();
	}

	@Override
	public Action findActionByURL(String contextPath, final String url) {
		List<Action> queryResult = new ArrayList<Action>();
		Action actionForURL = null;
		LOGGER.info("findActionByURL(contextPath, url) : URL--" + url);
		final boolean hasContextPath = org.apache.commons.lang.StringUtils.isNotBlank(contextPath);
		if (hasContextPath) {
			contextPath = contextPath.startsWith("/") ? contextPath.substring(1).toUpperCase() : contextPath.toUpperCase();
		}
		// There are no query params with URL
		if (url.indexOf("?") == -1) {
			queryResult = this.getQueryResult("from org.egov.lib.rrbac.model.Action where replace(url,'/../'||contextRoot||'/','/') = :fullURL and queryParams is null", contextPath, url, hasContextPath);
		} else { // There are query params exists with URL
			queryResult = this.getQueryResult("from org.egov.lib.rrbac.model.Action where :fullURL = replace(url,'/../'||contextRoot||'/','/')||'?'||queryParams ", contextPath, url, hasContextPath);
			if (queryResult.isEmpty()) {
				queryResult = this.getQueryResult("from org.egov.lib.rrbac.model.Action where :fullURL like replace(url,'/../'||contextRoot||'/','/')||'?'||queryParams  ||'%' ", contextPath, url, hasContextPath);
			}
		}
		actionForURL = queryResult.isEmpty() ? null : queryResult.get(0);
		LOGGER.info("findActionByURL(contextPath, url) : actionForURL--" + actionForURL);
		return actionForURL;
	}

	@Override
	public List<Action> getActionWithRoles() {
		try {
			return this.getSession().createQuery("from org.egov.lib.rrbac.model.Action act left join fetch act.roles").list();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred at getActionWithRG. ", e);
		}
	}

	@Override
	public List<Action> getActionWithRG() {
		try {
			return this.getSession().createQuery("from org.egov.lib.rrbac.model.Action act left join fetch act.ruleGroup").list();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Error occurred at getActionWithRG. ", e);
		}
	}
}