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
package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintTypeService {

    private final ComplaintTypeRepository complaintTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ComplaintTypeService(final ComplaintTypeRepository complaintTypeRepository) {
        this.complaintTypeRepository = complaintTypeRepository;
    }

    public ComplaintType findBy(final Long complaintTypeId) {
        return complaintTypeRepository.findOne(complaintTypeId);
    }

    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT_TYPE)
    @Transactional
    public ComplaintType createComplaintType(final ComplaintType complaintType) {
        return complaintTypeRepository.save(complaintType);
    }

    @Transactional
    public void updateComplaintType(final ComplaintType complaintType) {
        complaintTypeRepository.save(complaintType);
    }

    public List<ComplaintType> findAll() {
        return complaintTypeRepository.findAll();
    }

    public List<ComplaintType> findAllByNameLike(final String name) {
        return complaintTypeRepository.findByNameContainingIgnoreCase(name);
    }

    public ComplaintType findByName(final String name) {
        return complaintTypeRepository.findByName(name);
    }

    public ComplaintType load(final Long id) {
        // FIXME alternative ?
        return (ComplaintType) entityManager.unwrap(Session.class).load(ComplaintType.class, id);
    }

    public Page<ComplaintType> getListOfComplaintTypes(Integer pageNumber, Integer pageSize) {
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "name");
        return complaintTypeRepository.findAll(pageable);
    }

    /**
     * List top 5 complaint types filed in last one month
     * 
     * @return complaint Type list
     */
    public List<ComplaintType> getFrequentlyFiledComplaints() {

        DateTime previousDate = new DateTime();
        DateTime currentDate = new DateTime();
        previousDate = previousDate.minusMonths(1);

        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint");
        criteria.setProjection(Projections.projectionList().add(Projections.property("complaint.complaintType"))
                .add(Projections.count("complaint.complaintType").as("count"))
                .add(Projections.groupProperty("complaint.complaintType")));
        criteria.add(Restrictions.between("complaint.createdDate", previousDate.toDate(), currentDate.toDate()));
        criteria.setMaxResults(5).addOrder(Order.desc("count"));
        List<Object> resultList = criteria.list();
        List<ComplaintType> complaintTypeList = new ArrayList<ComplaintType>();

        for (Object row : resultList) {
            Object[] columns = (Object[]) row;
            complaintTypeList.add((ComplaintType) columns[0]);
        }
        return complaintTypeList;

    }
}
