package org.egov.pgr.repository;

import org.egov.infra.persistence.service.HibernateRepository;
import org.egov.pgr.entity.ComplaintStatus;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ComplaintStatusRepository extends HibernateRepository<ComplaintStatus> {
	private SessionFactory sessionFactory;
	 
	 @Autowired
	   public ComplaintStatusRepository(SessionFactory sessionFactory) {
	        super(sessionFactory, ComplaintStatus.class);  
	    }

	

	
}