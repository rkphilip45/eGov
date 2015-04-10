package org.egov.infra.admin.master.service;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.repository.BoundaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoundaryService {
    
    private BoundaryRepository boundaryRepository;
    
    @Autowired
    public BoundaryService(BoundaryRepository boundaryRepository) {
        this.boundaryRepository = boundaryRepository;
    }

    @Transactional
    public void createBoundary(Boundary boundary) {
        boundaryRepository.save(boundary);
    }
    
    @Transactional
    public void updateBoundary(Boundary boundary) {
        boundaryRepository.save(boundary);
    }
    
    @Transactional
    public Boundary getBoundaryById(Long id) {
        return boundaryRepository.findOne(id);
    }
    
    @Transactional
    public Boundary getBoundaryByName(String name) {
        return boundaryRepository.findByName(name);
    }
    
    @Transactional
    public List<Boundary> getAllBoundaries() {
        return boundaryRepository.findAll();
    }
    
    @Transactional
    public List<Boundary> getBoundaryByNameLike(String name) {
        return boundaryRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional
    public List<Boundary> getAllBoundariesByBoundaryTypeId(Long boundaryTypeId) {
        return boundaryRepository.findAllByBoundaryTypeId(boundaryTypeId);
    }
    
    @Transactional
    public Page<Boundary> getListOfBoundariesAsPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "name");
        return boundaryRepository.findAll(pageable);
    }
}