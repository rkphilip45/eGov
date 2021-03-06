/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/**
 * 
 */
package org.egov.ptis.actions.objection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.objection.Objection;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.actions.BaseFormAction;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class RejectionLetterAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(RejectionLetterAction.class);
	private Objection objection = new Objection();
	private static final String REJECTIONLETTERTEMPLATE = "objectionRejectionLetter";
	private PersistenceService<Objection, Long> objectionService;
	protected ReportService reportService;
	PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	private Integer reportId = -1;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	@Override
	public Object getModel() {
		
		return objection;
	}
	
	@Override
	public void prepare() {
		
		if(null != objection.getId() ){
			objection = objectionService.findById(objection.getId(),false);
		}
	}
	@SkipValidation
	public String print(){
		
		ReportRequest reportRequest = new ReportRequest(REJECTIONLETTERTEMPLATE, objection, getParamMap());
		reportRequest.setPrintDialogOnOpenReport(true);
		ReportOutput reportOutput = reportService.createReport(reportRequest);
		reportId =  addingReportToSession(reportOutput);
		return "print";
	}
	
	
	private Map<String, Object> getParamMap(){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("date", DDMMYYYYFORMATS.format(new Date()));
		paramMap.put("objectionNo", objection.getObjectionNumber());
		paramMap.put("description", objection.getDetails());
		paramMap.put("objectionDate", DDMMYYYYFORMATS.format(objection.getRecievedOn()));
		Boundary zone = objection.getBasicProperty().getBoundary().getParent();
		paramMap.put("zoneNo",zone!=null?zone.getBoundaryNum().toString():"");
		paramMap.put("slNo", propertyTaxNumberGenerator.getRejectionLetterSerialNum());
		paramMap.put("owner", ptisCacheMgr.buildOwnerFullName(objection.getBasicProperty().getProperty().getPropertyOwnerSet()));
		paramMap.put("address", ptisCacheMgr.buildAddressByImplemetation(objection.getBasicProperty().getAddress()));
		return paramMap;
	}
	
	protected Integer addingReportToSession(ReportOutput reportOutput){
		 return	ReportViewerUtil.addReportToSession(reportOutput, getSession());
	}
	public Objection getObjection() {
		return objection;
	}

	public void setObjection(Objection objection) {
		this.objection = objection;
	}

	public void setObjectionService(
			PersistenceService<Objection, Long> objectionService) {
		this.objectionService = objectionService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

}
