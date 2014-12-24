/*
 * @(#)NewTradeLicenseAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.newtradelicense;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.Licensee;
import org.egov.license.domain.entity.MotorDetails;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.license.web.actions.common.BaseLicenseAction;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.service.EmployeeService;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
public class NewTradeLicenseAction extends BaseLicenseAction {

	private static final long serialVersionUID = 1L;
	protected TradeLicense tradeLicense = new TradeLicense();
	WorkflowService<TradeLicense> tradeLicenseWorkflowService;
	private TradeService ts;

	public NewTradeLicenseAction() {
		super();
		this.tradeLicense.setLicensee(new Licensee());
	}

	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	@Override
	@SkipValidation
	public String approve() {
		return super.approve();
	}

	@Override
	@Validations(requiredFields = { @RequiredFieldValidator(fieldName = "licensee.applicantName", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "licenseeZoneId", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "licenseZoneId", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "tradeName", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "applicationDate", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "licensee.gender", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "nameOfEstablishment", message = "", key = Constants.REQUIRED), @RequiredFieldValidator(fieldName = "address.houseNo", message = "", key = Constants.REQUIRED),
			@RequiredFieldValidator(fieldName = "licensee.address.houseNo", message = "", key = Constants.REQUIRED) }, emails = { @EmailValidator(message = "Please enter the valid Email Id", fieldName = "licensee.emailId",
			key = "Please enter the valid Email Id") }, stringLengthFields = { @StringLengthFieldValidator(fieldName = "nameOfEstablishment", maxLength = "100", message = "", key = "Maximum length for Name of Establishment is 100"),
			@StringLengthFieldValidator(fieldName = "remarks", maxLength = "500", message = "", key = "Maximum length for Remarks is 500"),
			@StringLengthFieldValidator(fieldName = "address.streetAddress1", maxLength = "500", message = "", key = "Maximum length for remaining address is 500"),
			@StringLengthFieldValidator(fieldName = "address.houseNo", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "address.streetAddress2", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "phoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
			@StringLengthFieldValidator(fieldName = "licensee.applicantName", maxLength = "100", message = "", key = "Maximum length for Applicant Name is 100"),
			@StringLengthFieldValidator(fieldName = "licensee.nationality", maxLength = "50", message = "", key = "Maximum length for Nationality is 50"),
			@StringLengthFieldValidator(fieldName = "licensee.fatherOrSpouseName", maxLength = "100", message = "", key = "Maximum length for Father Or SpouseName is 100"),
			@StringLengthFieldValidator(fieldName = "licensee.qualification", maxLength = "50", message = "", key = "Maximum length for Qualification is 50"),
			@StringLengthFieldValidator(fieldName = "licensee.panNumber", maxLength = "10", message = "", key = "Maximum length for PAN Number is 10"),
			@StringLengthFieldValidator(fieldName = "licensee.address.houseNo", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "licensee.address.streetAddress2", maxLength = "10", message = "", key = "Maximum length for house number is 10"),
			@StringLengthFieldValidator(fieldName = "licensee.address.streetAddress1", maxLength = "500", message = "", key = "Maximum length for remaining address is 500"),
			@StringLengthFieldValidator(fieldName = "licensee.phoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
			@StringLengthFieldValidator(fieldName = "licensee.mobilePhoneNumber", maxLength = "15", message = "", key = "Maximum length for Phone Number is 15"),
			@StringLengthFieldValidator(fieldName = "licensee.uid", maxLength = "12", message = "", key = "Maximum length for UID is 12") }, intRangeFields = {
			@IntRangeFieldValidator(fieldName = "noOfRooms", min = "1", max = "999", message = "", key = "Number of rooms should be in the range 1 to 999"),
			@IntRangeFieldValidator(fieldName = "address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0"),
			@IntRangeFieldValidator(fieldName = "licensee.age", min = "1", max = "100", message = "", key = "Age should be in the range of 1 to 100"),
			@IntRangeFieldValidator(fieldName = "licensee.address.pinCode", min = "100000", max = "999999", message = "", key = "Minimum and Maximum length for Pincode is 6 and all Digit Cannot be 0") })
	@ValidationErrorPage(Constants.NEW)
	public String create() {
		this.LOGGER.debug("Trade license Creation Parameters:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		if (this.tradeLicense.getLicenseZoneId() != null && this.tradeLicense.getBoundary() == null) {
			final Boundary boundary = (new BoundaryDAO()).getBoundary(this.tradeLicense.getLicenseZoneId().intValue());
			this.tradeLicense.setBoundary(boundary);
		}

		if (this.tradeLicense.getLicenseeZoneId() != null && this.tradeLicense.getLicensee().getBoundary() == null) {
			final Boundary boundary = (new BoundaryDAO()).getBoundary(this.tradeLicense.getLicenseeZoneId().intValue());
			this.tradeLicense.getLicensee().setBoundary(boundary);
		}
		if (this.tradeLicense.getInstalledMotorList() != null) {
			final Iterator<MotorDetails> motorDetails = this.tradeLicense.getInstalledMotorList().iterator();
			while (motorDetails.hasNext()) {
				final MotorDetails installedMotor = motorDetails.next();
				if ((installedMotor != null) && (installedMotor.getHp() != null) && (installedMotor.getNoOfMachines() != null) && (installedMotor.getHp().compareTo(BigDecimal.ZERO) != 0) && (installedMotor.getNoOfMachines().compareTo(Long.valueOf("0")) != 0)) {
					installedMotor.setLicense(this.tradeLicense);
				} else {
					motorDetails.remove();
				}
			}

		}
		this.LOGGER.debug(" Create Trade License Application Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense.getNameOfEstablishment());
		return super.create();
	}

	@Override
	public void prepareNewForm() {
		super.prepareNewForm();
		this.tradeLicense.setHotelGradeList(this.tradeLicense.populateHotelGradeList());
		this.tradeLicense.setHotelSubCatList(this.ts.getHotelCategoriesForTrade());
	}

	@Override
	@SkipValidation
	public String renew() {
		this.LOGGER.debug("Trade license renew Parameters:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		final BigDecimal deduction = this.tradeLicense.getDeduction();
		final BigDecimal otherCharges = this.tradeLicense.getOtherCharges();
		final BigDecimal swmFee = this.tradeLicense.getSwmFee();
		this.tradeLicense = (TradeLicense) this.ts.getPersistenceService().find("from License where id=?", this.tradeLicense.getId());
		this.tradeLicense.setOtherCharges(otherCharges);
		this.tradeLicense.setDeduction(deduction);
		this.tradeLicense.setSwmFee(swmFee);
		this.LOGGER.debug("Renew Trade License Application Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense.getNameOfEstablishment());
		return super.renew();
	}

	@Override
	@SkipValidation
	public String beforeRenew() {
		this.LOGGER.debug("Entering in the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
		this.tradeLicense = (TradeLicense) this.ts.getPersistenceService().find("from License where id=?", this.tradeLicense.getId());
		this.LOGGER.debug("Exiting from the beforeRenew method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return super.beforeRenew();
	}

	@Override
	public Object getModel() {
		return this.tradeLicense;
	}

	public WorkflowBean getWorkflowBean() {
		return this.workflowBean;
	}

	@Override
	protected License license() {
		return this.tradeLicense;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BaseLicenseService service() {
		this.ts.getPersistenceService().setType(TradeLicense.class);
		return this.ts;
	}

	public void setEisCommonsService(final EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setEmployeeService(final EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@SuppressWarnings("unchecked")
	public void setTradeLicenseWorkflowService(final WorkflowService tradeLicenseWorkflowService) {
		this.tradeLicenseWorkflowService = tradeLicenseWorkflowService;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	public void setWorkflowBean(final WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

}