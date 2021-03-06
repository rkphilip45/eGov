/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgfAccountcodePurpose;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class ChartOfAccountsHibernateDAO extends GenericHibernateDAO implements ChartOfAccountsDAO {

	private static final Logger LOG = LoggerFactory.getLogger(ChartOfAccountsHibernateDAO.class);

	public ChartOfAccountsHibernateDAO() {
		super(CChartOfAccounts.class, null);
	}

	public ChartOfAccountsHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	/**
	 * This API will give the list of detailed active for posting chartofaccounts list
	 * @return
	 * @throws EGOVException
	 */
	@Override
	public List<CChartOfAccounts> getDetailedAccountCodeList() throws EGOVException {
		return getCurrentSession().createQuery("select acc from CChartOfAccounts acc where acc.classification='4' and acc.isActiveForPosting = 1 order by acc.glcode").list();
	}

	@Override
	public CChartOfAccounts getCChartOfAccountsByGlCode(final String glCode) {
		final Query qry = getCurrentSession().createQuery("from CChartOfAccounts coa where coa.glcode =:glCode order by glcode");
		qry.setString("glCode", glCode);
		return (CChartOfAccounts) qry.uniqueResult();
	}

	/**
	 * This API will return the accountdetailtype for an account code when the accountcode and the respective accountdetailtype name is passed.
	 * @param glcode - This the chartofaccount code (mandatory)
	 * @param name - This is the accountdetailtype name that is associated with the account code (mandatory)
	 * @return - Returns the accountdetailtype object if the account code is having the passed accountdetailtype name, else NULL
	 */
	@Override
	public Accountdetailtype getAccountDetailTypeIdByName(final String glCode, final String name) throws Exception {
		if (null == glCode || null == name || name.equals("")) {
			throw new EGOVException("Account code and accountdetail type name cannot be NULL or empty");
		}

		Query query = getCurrentSession().createQuery("from CChartOfAccounts where glcode=:glCode");
		query.setString("glCode", glCode);
		if (query.list().isEmpty()) {
			throw new EGOVException("glcode= " + glCode + " is not present in ChartOfAccounts table");
		}

		query = getCurrentSession().createQuery("from Accountdetailtype where id in (select cd.detailTypeId from CChartOfAccountDetail as cd,CChartOfAccounts as c where cd.glCodeId=c.id and c.glcode=:glCode) and name=:name");
		query.setString("glCode", glCode);
		query.setString("name", name);
		return (Accountdetailtype) query.uniqueResult();
	}

	@Override
	public List<String> getGlcode(final String minGlcode, final String maxGlcode, final String majGlcode) throws Exception {
		Query qry = null;
		final StringBuffer qryStr = new StringBuffer("select coa.glcode from CChartOfAccounts coa where ");
		if (!minGlcode.equals("") && !maxGlcode.equals("")) {
			qryStr.append(" coa.glcode between :minGlcode and :maxGlcode ");
			qry = getCurrentSession().createQuery(qryStr.toString());
			qry.setString("minGlcode", minGlcode + "%");
			qry.setString("maxGlcode", maxGlcode + "%");
		} else if (!maxGlcode.equals("")) {
			qryStr.append(" coa.glcode like :maxGlcode ");
			qry = getCurrentSession().createQuery(qryStr.toString());
			qry.setString("maxGlcode", maxGlcode + "%");
		} else if (!majGlcode.equals("")) {
			qryStr.append(" coa.glcode =:majGlcode ");
			qry = getCurrentSession().createQuery(qryStr.toString());
			qry.setString("majGlcode", majGlcode);
		}
		return qry == null ? Collections.EMPTY_LIST : qry.list();
	}

	/**
	 * This API will return the list of detailed chartofaccounts objects that are active for posting for the Type.
	 * @param -Accounting type-(Asset (A), Liability (L), Income (I), Expense (E))
	 * @return list of chartofaccount objects
	 */
	@Override
	public List<CChartOfAccounts> getActiveAccountsForType(final char type) throws EGOVException {
		return getCurrentSession().createQuery("from CChartOfAccounts acc where acc.classification='4' and acc.isActiveForPosting = 1 and type=:type order by acc.name").setCharacter("type", type).list();
	}

	/**
	 * to get the list of chartofaccounts based on the purposeId. First query will get the detail codes for the purpose is mapped to major code level. second query will get the detail codes for the purpose is mapped to minor code level. last one will get the detail codes are mapped to the detail
	 * code level.
	 * @param purposeId
	 * @return list of COA object(s)
	 */
	@Override
	public List<CChartOfAccounts> getAccountCodeByPurpose(final Integer purposeId) throws EGOVException {
		List<CChartOfAccounts> accountCodeList = null;
		try {
			if (purposeId == null || purposeId.intValue() == 0) {
				throw new EGOVException("Purpose Id is null or empty or zero");
			}

			final EgfAccountcodePurpose accountcodePurpose = (EgfAccountcodePurpose) getCurrentSession().createQuery(" from EgfAccountcodePurpose purpose where purpose.id=" + purposeId.intValue()).uniqueResult();

			if (accountcodePurpose == null) {
				throw new EGOVException("Purpose ID " + purposeId.intValue() + " is not defined in the system");
			}

			final Query qry3 = getCurrentSession()
					.createQuery(
							" FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId))) AND classification=4 AND isActiveForPosting=1 order by glcode");
			qry3.setLong("purposeId", purposeId);
			accountCodeList = qry3.list();

			final Query qry = getCurrentSession().createQuery(
					" FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId)) AND classification=4 AND isActiveForPosting=1 order by glcode");
			qry.setLong("purposeId", purposeId);
			accountCodeList = qry.list();

			final Query qry1 = getCurrentSession().createQuery(" FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId) AND classification=4 AND isActiveForPosting=1 order by glcode");
			qry1.setLong("purposeId", purposeId);
			accountCodeList.addAll(qry1.list());

			final Query qry2 = getCurrentSession().createQuery(" FROM CChartOfAccounts WHERE purposeid=:purposeId AND classification=4 AND isActiveForPosting=1 order by glcode");
			qry2.setLong("purposeId", purposeId);
			accountCodeList.addAll(qry2.list());

		} catch (final Exception e) {
			LOG.error("Error occurred while getting CCA using Purpose Id", e);
			throw new EGOVException("Error occurred while getting CCA using Purpose Id", e);
		}
		return accountCodeList;
	}

	/**
	 * This API will return the list of non control detailed chartofaccount codes that are active for posting.
	 * @return list of chartofaccount objects.
	 */
	@Override
	public List<CChartOfAccounts> getNonControlCodeList() throws EGOVException {
		try {
			return getCurrentSession().createQuery(" from CChartOfAccounts acc where acc.classification=4 and acc.isActiveForPosting=1 and acc.id not in (select cd.glCodeId from CChartOfAccountDetail cd) ").list();
		} catch (final Exception e) {
			LOG.error("Error occurred while getting NonControl Codes", e);
			throw new EGOVException("Error occurred while getting NonControl Codes", e);
		}
	}

	/**
	 * @description- This method returns a list of detail type object based on the glcode.
	 * @param glCode - glcode supplied by the client.
	 * @return List<Accountdetailtype> -list of Accountdetailtype object(s).
	 * @throws EGOVException
	 */
	@Override
	public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode) throws EGOVException {

		Query query = null;
		if (null == glCode) {
			throw new EGOVException("glcode supplied by the client is  null ");
		}
		// checking if the glcode is exists in ChartOfAccounts table.
		query = getCurrentSession().createQuery("from CChartOfAccounts where glcode=:glCode");
		query.setString("glCode", glCode);
		if (query.list().isEmpty()) {
			throw new EGOVException("glcode= " + glCode + " is not present in ChartOfAccounts table");
		}
		try {
			query = getCurrentSession().createQuery("from Accountdetailtype where id in (select cd.detailTypeId from CChartOfAccountDetail " + " as cd,CChartOfAccounts as c where cd.glCodeId=c.id and c.glcode=:glCode)");
			query.setString("glCode", glCode);
		} catch (final Exception e) {
			LOG.error("Error occurred while getting Account Detail Types By GLCode", e);
			throw new EGOVException("Error occurred while getting Account Detail Types By GLCode", e);
		}

		return query == null ? Collections.EMPTY_LIST : query.list();
	}

	/**
	 * @author manoranjan
	 * @description -Get list of COA for a list of types.
	 * @param type - list of types,e.g income, Assets etc.
	 * @return listChartOfAcc - list of chartofaccounts based on the given list of types
	 * @throws ValidationException
	 */
	@Override
	public List<CChartOfAccounts> getActiveAccountsForTypes(final char[] type) throws ValidationException {

		if (null == type) {
			throw new ValidationException(Arrays.asList(new ValidationError("type", "The supplied value for chartofaccount type  can not be null")));
		} else if (type.length == 0) {
			throw new ValidationException(Arrays.asList(new ValidationError("type", "The supplied value for chartofaccount type  can not be empty")));
		}
		final StringBuffer CoaTypeQuery = new StringBuffer(200);
		StringBuffer coaType = new StringBuffer();
		for (int i = 0; i < type.length; i++) {
			if (i != type.length - 1) {
				coaType = coaType.append("'").append(type[i]).append("'").append(",");
			} else {
				coaType = coaType.append("'").append(type[i]).append("'");
			}

		}
		CoaTypeQuery.append("from CChartOfAccounts where classification=4 and isActiveForPosting=1 and type in (").append(coaType.toString()).append(")");
		return getCurrentSession().createQuery(CoaTypeQuery.toString()).list();

	}

	/**
	 * @author manoranjan
	 * @description - Get list of Chartofaccount objects for a list of purpose ids
	 * @param purposeId - list of purpose ids.
	 * @return listChartOfAcc - list of chartofaccount objects for the given list of purpose id
	 * @throws ValidationException
	 */
	@Override
	public List<CChartOfAccounts> getAccountCodeByListOfPurposeId(final Integer[] purposeId) throws ValidationException {

		if (null == purposeId) {
			throw new ValidationException(Arrays.asList(new ValidationError("purposeId", "the supplied purposeId  can not be null")));
		} else if (purposeId.length == 0) {
			throw new ValidationException(Arrays.asList(new ValidationError("purposeId", "the supplied purposeId  can not be empty")));
		}

		final StringBuffer purposeIds = new StringBuffer();
		for (int i = 0; i < purposeId.length; i++) {
			if (i != purposeId.length - 1) {
				purposeIds.append(purposeId[i]).append(",");
			} else {
				purposeIds.append(purposeId[i]);
			}
		}
		List<CChartOfAccounts> listChartOfAcc;
		final StringBuffer query = new StringBuffer();
		query.append(" FROM CChartOfAccounts WHERE purposeid in(").append(purposeIds).append(")AND classification=4 AND isActiveForPosting=1 ");
		listChartOfAcc = getCurrentSession().createQuery(query.toString()).list();
		query.setLength(0);
		query.append(" from CChartOfAccounts where parentId IN (");
		query.append(" select id  FROM CChartOfAccounts WHERE purposeid in(").append(purposeIds).append("))AND classification=4 AND isActiveForPosting=1 ");
		listChartOfAcc.addAll(getCurrentSession().createQuery(query.toString()).list());
		query.setLength(0);
		query.append(" from CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN (");
		query.append(" select id  FROM CChartOfAccounts WHERE purposeid in(").append(purposeIds).append(")))AND classification=4 AND isActiveForPosting=1 ");
		listChartOfAcc.addAll(getCurrentSession().createQuery(query.toString()).list());
		query.setLength(0);
		query.append(" from CChartOfAccounts where   parentId IN (select id from  CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN (");
		query.append(" select id  FROM CChartOfAccounts WHERE purposeid in(").append(purposeIds).append("))))AND classification=4 AND isActiveForPosting=1 ");
		listChartOfAcc.addAll(getCurrentSession().createQuery(query.toString()).list());

		return listChartOfAcc;
	}

	/**
	 * @author manoranjan
	 * @description - This api will return the list of detailed chartofaccounts objects that are active for posting.
	 * @param glcode - The input is the chartofaccounts code.
	 */
	@Override
	public List<CChartOfAccounts> getListOfDetailCode(final String glCode) throws ValidationException {

		if (null == glCode) {
			throw new ValidationException(Arrays.asList(new ValidationError("glcode null", "the glcode value supplied can not be null")));
		} else if (glCode.trim().isEmpty()) {
			throw new ValidationException(Arrays.asList(new ValidationError("glcode blank", "the glcode value supplied can not be blank")));
		} else if (getCurrentSession().createQuery("from CChartOfAccounts where glcode=:glcode").setString("glcode", glCode).list().isEmpty()) {
			throw new ValidationException(Arrays.asList(new ValidationError("glcode not exist", "the glcode value supplied doesnot exist in the System")));
		} else {
			final StringBuffer query = new StringBuffer(200);
			query.append(" FROM CChartOfAccounts WHERE glcode=:glcode").append(" AND classification=4 AND isActiveForPosting=1 ");
			final List<CChartOfAccounts> listChartOfAcc = getCurrentSession().createQuery(query.toString()).setString("glcode", glCode).list();

			query.setLength(0);
			query.append(" from CChartOfAccounts where parentId IN (");
			query.append(" select id  FROM CChartOfAccounts WHERE glcode=:glcode").append(") AND classification=4 AND isActiveForPosting=1 ");
			listChartOfAcc.addAll(getCurrentSession().createQuery(query.toString()).setString("glcode", glCode).list());

			query.setLength(0);
			query.append(" from CChartOfAccounts where parentId IN (select id from CChartOfAccounts where parentId IN (");
			query.append(" select id  FROM CChartOfAccounts WHERE glcode=:glcode").append("))AND classification=4 AND isActiveForPosting=1 ");
			listChartOfAcc.addAll(getCurrentSession().createQuery(query.toString()).setString("glcode", glCode).list());

			query.setLength(0);
			query.append(" from CChartOfAccounts where parentId IN (select id from  CChartOfAccounts where   parentId IN (select id from CChartOfAccounts where parentId IN (");
			query.append(" select id  FROM CChartOfAccounts WHERE glcode=:glcode").append(")))AND classification=4 AND isActiveForPosting=1 ");
			listChartOfAcc.addAll(getCurrentSession().createQuery(query.toString()).setString("glcode", glCode).list());
			return listChartOfAcc;
		}
	}

	@Override
	public List<CChartOfAccounts> getBankChartofAccountCodeList() {
		return getCurrentSession().createQuery("select chartofaccounts from Bankaccount").list();
	}

}
