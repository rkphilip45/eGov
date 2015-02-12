package org.egov.pgr.service;

import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.repository.ComplaintStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintStatusService {
	
	ComplaintStatusRepository complaintStatusRepository;
    
	@Autowired
	public ComplaintStatusService(ComplaintStatusRepository complaintStatusRepository) {
		super();
		this.complaintStatusRepository = complaintStatusRepository;
	}
	
	public ComplaintStatus load(Long id)
	{
	  return complaintStatusRepository.load(id);
	}
	
	

}