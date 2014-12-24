package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.contractorBill.ContractorBillRegister;

public class ContractorBillWFService extends PersistenceService<ContractorBillRegister, Long> {
	private EmployeeService employeeService;

	public ContractorBillWFService() {
		setType(ContractorBillRegister.class);
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}