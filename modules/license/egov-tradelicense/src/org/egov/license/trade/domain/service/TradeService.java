/*
 * @(#)TradeService.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.domain.service;

import java.util.List;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.LicenseAppType;
import org.egov.license.domain.entity.LicenseStatus;
import org.egov.license.domain.entity.LicenseStatusValues;
import org.egov.license.domain.entity.MotorMaster;
import org.egov.license.domain.entity.NatureOfBusiness;
import org.egov.license.domain.entity.WorkflowBean;
import org.egov.license.domain.entity.transfer.LicenseTransfer;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseUtils;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;

public class TradeService extends BaseLicenseService {
	private PersistenceService<TradeLicense, Long> tps;
	private WorkflowService<TradeLicense> tradeLicenseWorkflowService;

	public PersistenceService<TradeLicense, Long> getTps() {
		return this.tps;
	}

	public void setTps(final PersistenceService<TradeLicense, Long> tps) {
		this.tps = tps;
	}

	public TradeService() {
		this.setPersistenceService(this.tps);
	}

	@Override
	protected NatureOfBusiness getNatureOfBusiness() {
		final NatureOfBusiness natureOfBusiness = (NatureOfBusiness) this.persistenceService.find("from org.egov.license.domain.entity.NatureOfBusiness where   name='Permanent'");
		return natureOfBusiness;
	}

	@Override
	protected Module getModuleName() {
		final Module module = (Module) this.persistenceService.find("from org.egov.infstr.commons.Module where parent is null and moduleName=?", "Trade License");
		return module;
	}

	@Override
	public License additionalOperations(final License license, final Set<EgDemandReasonMaster> egDemandReasonMasters, final Installment installment) {
		final TradeLicense tl = (TradeLicense) license;
		final List<MotorMaster> motorMasterList = this.persistenceService.findAllBy("from org.egov.license.domain.entity.MotorMaster");
		tl.setMotorMasterList(motorMasterList);
		tl.additionalDemandDetails(egDemandReasonMasters, installment);
		return tl;
	}

	public void setTradeLicenseWorkflowService(final WorkflowService<TradeLicense> tradeLicenseWorkflowService) {
		this.tradeLicenseWorkflowService = tradeLicenseWorkflowService;
	}

	@Override
	protected WorkflowService<TradeLicense> workflowService() {
		return this.tradeLicenseWorkflowService;
	}

	@Override
	public void setEisCommonsService(final EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void transferLicense(final TradeLicense tl, final LicenseTransfer licenseTransfer) {
		final String runningApplicationNumber = this.getNextRunningNumber(tl.getClass().getSimpleName().toUpperCase() + "_APPLICATION_NUMBER");
		final String currentApplno = tl.getApplicationNumber();
		final String generatedApplicationNumber = tl.generateApplicationNumber(runningApplicationNumber);
		tl.setApplicationNumber(currentApplno);
		licenseTransfer.setLicense(tl);
		licenseTransfer.setType("TradeLicense");
		tl.setLicenseTransfer(licenseTransfer);
		licenseTransfer.setOldApplicationNumber(generatedApplicationNumber);
		this.persistenceService.persist(tl);
	}

	public void initiateWorkFlowForTransfer(final License license, final WorkflowBean workflowBean) {
		final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		try {
			this.tradeLicenseWorkflowService.start(license, position, workflowBean.getComments());
		} catch (final EGOVRuntimeException e) {
			if (license.getState().getValue().equalsIgnoreCase("END")) {
				license.setState(null);
				this.persistenceService.persist(license);
				this.tradeLicenseWorkflowService.start(license, position, workflowBean.getComments());
			} else {
				throw e;
			}
		}
		license.getState().setText2(license.getWorkflowIdentityForTransfer());
		final LicenseStatus underWorkflowStatus = (LicenseStatus) this.persistenceService.find("from org.egov.license.domain.entity.LicenseStatus where code='UWF'");
		license.setStatus(underWorkflowStatus);
		this.processWorkFlowForTransfer(license, workflowBean);
		return;
	}

	public void processWorkFlowForTransfer(final License license, final WorkflowBean workflowBean) {
		if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONSAVE)) {
			final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			license.changeState(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE + "NEW", position, workflowBean.getComments());
			license.getState().setText2(license.getWorkflowIdentityForTransfer());
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONAPPROVE)) {
			Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			license.changeState(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE + Constants.WORKFLOW_STATE_APPROVED, position, workflowBean.getComments());
			license.getState().setText2(license.getWorkflowIdentityForTransfer());
			license.acceptTransfer();
			position = this.eisCommonsService.getPositionByUserId(license.getCreatedBy().getId());
			license.changeState(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE + Constants.WORKFLOW_STATE_GENERATECERTIFICATE, position, workflowBean.getComments());
			license.getState().setText2(license.getWorkflowIdentityForTransfer());
			license.getLicenseTransfer().setApproved(true);
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONFORWARD)) {
			final Position nextPosition = this.eisCommonsService.getPositionByUserId(workflowBean.getApproverUserId());
			license.changeState(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE + Constants.WORKFLOW_STATE_FORWARDED, nextPosition, workflowBean.getComments());
			license.getState().setText2(license.getWorkflowIdentityForTransfer());
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONREJECT)) {
			if (license.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
				final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				this.workflowService().end(license, position);
				license.getLicenseTransfer().setApproved(false);
				license.getState().setText2(license.getWorkflowIdentityForTransfer());
			} else {
				final Position position = this.eisCommonsService.getPositionByUserId(license.getCreatedBy().getId());
				license.changeState(Constants.WORKFLOW_STATE_TYPE_TRANSFERLICENSE + Constants.WORKFLOW_STATE_REJECTED, position, workflowBean.getComments());
				license.getState().setText2(license.getWorkflowIdentityForTransfer());
			}
		} else if (workflowBean.getActionName().equalsIgnoreCase(Constants.BUTTONGENERATEDCERTIFICATE)) {
			final Position position = this.eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
			this.workflowService().end(license, position);
			final LicenseStatus activeStatus = (LicenseStatus) this.persistenceService.find("from org.egov.license.domain.entity.LicenseStatus where code='ACT'");
			license.setStatus(activeStatus);
		}
		return;
	}

	@Override
	protected LicenseAppType getLicenseApplicationTypeForRenew() {
		final LicenseAppType appType = (LicenseAppType) this.persistenceService.find("from org.egov.license.domain.entity.LicenseAppType where   name='Renewal'");
		return appType;
	}

	@Override
	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	@Override
	protected LicenseAppType getLicenseApplicationType() {
		final LicenseAppType appType = (LicenseAppType) this.persistenceService.find("from org.egov.license.domain.entity.LicenseAppType where   name='New'");
		return appType;
	}

	public void revokeSuspendedLicense(final License license, final LicenseUtils licenseUtils, final LicenseStatusValues licenseStatusValues) {
		license.setActive(false);
		license.setStatus(licenseUtils.getLicenseStatusbyCode("ACT"));
		licenseStatusValues.setLicense(license);
		licenseStatusValues.setLicenseStatus(licenseUtils.getLicenseStatusbyCode("ACT"));
		licenseStatusValues.setActive(true);
		licenseStatusValues.setReason(Integer.valueOf(Constants.REASON_REVOKESUSPENTION_NO_4));
		license.addLicenseStatusValuesSet(licenseStatusValues);
		this.tps.update((TradeLicense) license);
		return;
	}

	public List getHotelCategoriesForTrade() {
		final List subCategory = this.persistenceService
				.findAllBy("select id from org.egov.license.domain.entity.SubCategory where upper(name) like '%HOTEL%' and licenseType.id= (select id from org.egov.license.domain.entity.LicenseType where name='TradeLicense')");
		return subCategory;
	}
}