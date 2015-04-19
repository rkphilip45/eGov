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

import org.egov.commons.Bankaccount;
import org.egov.exceptions.EGOVRuntimeException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BankaccountDAO {

	private SessionFactory sessionFactory;

	public BankaccountDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public void createBankaccount(final Bankaccount bankaccount) {
		try {
			getSession().save(bankaccount);
			getSession().getSessionFactory();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while creating Bank Account",e);
		}
	}

	public void updateBankaccount(final Bankaccount bankaccount) {
		try {
			getSession().saveOrUpdate(bankaccount);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while updating Bank Account",e);
		}
	}

	public void removeBankaccount(final Bankaccount bankaccount) {
		try {
			getSession().delete(bankaccount);
			getSession().getSessionFactory();
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while deleting Bank Account",e);
		}
	}

	public Bankaccount getBankaccountById(final int bankaccount) {
		try {
			return (Bankaccount) getSession().get(Bankaccount.class, bankaccount);
		} catch (final Exception e) {
			throw new EGOVRuntimeException("Exception occurred while getting Bank Account by Id",e);
		}
	}

}