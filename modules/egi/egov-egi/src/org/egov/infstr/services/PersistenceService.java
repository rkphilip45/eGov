/*
 * @(#)PersistenceService.java 3.0, 17 Jun, 2013 3:07:38 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.util.Version;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericDAO;
import org.egov.infstr.models.BaseModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

public class PersistenceService<T, ID extends Serializable> implements GenericDAO<T, ID> {
	private static final Logger LOG = LoggerFactory.getLogger(PersistenceService.class);
	private static final String DEFAULT_FIELD = "_hibernate_class";
	protected SessionFactory factory;
	protected Class<T> type;
	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	public void setType(final Class<T> type) {
		this.type = type;
	}

	public Session getSession() {
		final Session session = this.factory.getSession();
		return session;
	}

	protected void validate(final T model) {
		final List<ValidationError> errors = this.validateModel(model);
		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

	protected List<ValidationError> validateModel(final T model) {
		LOG.debug("Validating Model");
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		if (model == null) {
			errors.add(new ValidationError("", "model.null"));
			return errors;
		}
		final Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(model);
		for (final ConstraintViolation<T> constraintViolation : constraintViolations) {
			final Iterator<Node> nodes = constraintViolation.getPropertyPath().iterator();
			while (nodes.hasNext()) {
				errors.add(new ValidationError(nodes.next().getName(), constraintViolation.getMessage()));
			}
		}
		if (model instanceof BaseModel) {
			final BaseModel basemodel = (BaseModel) model;
			final List<ValidationError> dependentValMessages = basemodel.validate();
			if (dependentValMessages != null) {
				errors.addAll(dependentValMessages);
			}
		}
		return errors;
	}

	public T find(final String query, final Object... params) {
		final List<T> results = findAllBy(query, params);
		return results.isEmpty() ? null : results.get(0);
	}

	public T find(final String query) {
		final Query q = getSession().createQuery(query);
		return (T) q.uniqueResult();
	}

	protected T findById(final ID id) {
		return (T) (id == null ? null : getSession().get(this.type, id));
	}

	public List<T> findAllBy(final String query, final Object... params) {
		final Query q = getQueryWithParams(query, params);
		return q.list();
	}

	/**
	 * @param query
	 * @param pageNumber used to determine the offset from which to return the results
	 * @param pageSize Number of records to be returned in the page. If null then all records that match query are returned
	 * @param params
	 * @return
	 */
	public Page findPageBy(final String query, final Integer pageNumber, final Integer pageSize, final Object... params) {
		final Query q = getQueryWithParams(query, params);
		return new Page(q, pageNumber, pageSize);
	}

	private Query getQueryWithParams(final String query, final Object... params) {
		final Query q = getSession().createQuery(query);
		int index = 0;
		for (final Object param : params) {
			q.setParameter(index, param);
			index++;
		}
		return q;
	}

	public List<T> findAllByNamedQuery(final String namedQuery, final Object... params) {
		final Query q = getNamedQueryWithParams(namedQuery, params);
		return q.list();
	}

	/**
	 * @param namedQuery
	 * @param pageNumber used to determine the offset from which to return the results
	 * @param pageSize Number of records to be returned in the page. If null then all records that match query are returned
	 * @param params
	 * @return Page instance that can be used to implement pagination
	 */
	public Page findPageByNamedQuery(final String namedQuery, final Integer pageNumber, final Integer pageSize, final Object... params) {
		final Query q = getNamedQueryWithParams(namedQuery, params);
		return new Page(q, pageNumber, pageSize);
	}

	private Query getNamedQueryWithParams(final String namedQuery, final Object... params) {
		final Query q = getSession().getNamedQuery(namedQuery);
		int index = 0;
		for (final Object param : params) {
			if (param instanceof Collection) {
				q.setParameterList(String.valueOf("param_" + index), (Collection) param);
			} else {
				q.setParameter(index, param);
			}
			index++;
		}
		return q;
	}

	public T findByNamedQuery(final String namedQuery, final Object... params) {
		final List<T> results = findAllByNamedQuery(namedQuery, params);
		return results.isEmpty() ? null : results.get(0);
	}

	public T persist(final T model) {
		validate(model);
		getSession().saveOrUpdate(model);
		return model;
	}

	public T merge(final T model) {
		validate(model);
		return (T) getSession().merge(model);
	}

	public void setSessionFactory(final SessionFactory factory) {
		this.factory = factory;
	}

	@Override
	public T create(final T entity) {
		validate(entity);
		final Long id = (Long) getSession().save(entity);
		return (T) getSession().load(this.type, id);
	}

	@Override
	public void delete(final T entity) {
		getSession().delete(entity);
	}

	@Override
	public List<T> findAll() {
		return getSession().createCriteria(this.type).list();
	}

	@Override
	public List<T> findByExample(final T exampleT) {
		final Criteria criteria = getSession().createCriteria(this.type);
		return criteria.add(Example.create(exampleT)).list();
	}

	@Override
	public T findById(final ID id, final boolean lock) {
		return findById(id);
	}

	@Override
	public T update(final T entity) {
		validate(entity);
		getSession().update(entity);
		return entity;
	}

	public List<T> findAll(final String... orderByFields) {
		final Criteria c = getSession().createCriteria(this.type);
		for (final String orderBy : orderByFields) {
			c.addOrder(Order.asc(orderBy).ignoreCase());
		}
		return c.list();
	}

	public List<T> search(final String queryString, final int pageNumber, final int pageSize) {
		return search(queryString, pageNumber, pageSize, PagingStrategy.PAGE);
	}

	public List<T> search(final String queryString) {
		return search(queryString, 0, 0, PagingStrategy.NONE);
	}

	public String getNamedQuery(final String namedQuery) {
		return getSession().getNamedQuery(namedQuery).getQueryString();
	}

	private List<T> search(final String queryString, final int pageNumber, final int pageSize, final PagingStrategy paging) {
		// FIXME first line has fixed with some value, have to check the credibility of this code.
		final QueryParser parser = new QueryParser(Version.LUCENE_34, DEFAULT_FIELD, new StandardAnalyzer(Version.LUCENE_34));
		parser.setAllowLeadingWildcard(true);
		parser.setDefaultOperator(Operator.AND);
		final FullTextSession searchSession = getSearchSession();
		try {
			final FullTextQuery query = searchSession.createFullTextQuery(parser.parse(queryString), this.type);
			paging.setup(query, pageNumber, pageSize);
			return query.list();
		} catch (final ParseException e) {
			throw new EGOVRuntimeException("invalid.search.string", e);
		}
	}

	protected FullTextSession getSearchSession() {
		return Search.getFullTextSession(getSession());
	}

	enum PagingStrategy {
		PAGE {
			@Override
			public void setup(final FullTextQuery query, final int pageNumber, final int pageSize) {
				query.setFirstResult(pageNumber * pageSize + 1).setMaxResults(pageSize);
			}
		},
		NONE {
		};
		public void setup(final FullTextQuery query, final int pageNumber, final int pageSize) {
		};
	}

	public void indexEntity() {
		final List<T> results = getSession().createCriteria(this.type).list();
		final FullTextSession searchSession = getSearchSession();
		searchSession.flush();
		for (final T entity : results) {
			searchSession.index(entity);
		}
	}

	public void addIndexparams(final Map<String, List> indexparams, final String key, final Object... values) {
		final List objparams = new ArrayList();
		for (final Object value : values) {
			objparams.add(value);
		}
		indexparams.put(key, objparams);
	}

	public void addFilterCriteriaForObject(final Map<String, List> params, final Criteria c, final String... orderbyFields) {
		for (final Map.Entry<String, List> entry : params.entrySet()) {
			if (entry.getKey().contains("date") || entry.getKey().contains("Date")) {
				c.add(Restrictions.between(entry.getKey(), entry.getValue().get(0), entry.getValue().get(1)));
			} else {
				c.add(Restrictions.eq(entry.getKey(), entry.getValue().get(0)));
			}
		}
		for (final String orderBy : orderbyFields) {
			c.addOrder(Order.asc(orderBy).ignoreCase());
		}
	}

}