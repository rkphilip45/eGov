/*
 * @(#)ComplaintAction.java 3.0, 5 Aug, 2013 2:54:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.web.actions.citizen;

import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage("egov")
public class ComplaintAction extends org.egov.pgr.web.actions.complaint.ComplaintAction {
	private static final long serialVersionUID = 1L;

	@Override
	public void prepare() {
		this.pgrCommonUtils.setCitizenUser();
		super.prepare();
	}
}