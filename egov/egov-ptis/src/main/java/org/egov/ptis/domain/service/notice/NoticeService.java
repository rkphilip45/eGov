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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.service.notice;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.springframework.beans.factory.annotation.Autowired;

public class NoticeService {
	private static final Logger LOGGER = Logger.getLogger(NoticeService.class);
	PersistenceService<BasicProperty, Long> basicPrpertyService;
	PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	@Autowired
	private ModuleDao moduleDao;

	/**
	 * This method populates the <code>PtNotice</code> object along with notice
	 * input stream
	 * 
	 * @param basicProperty
	 *            the <code>BasicProperty</code> object for which the notice is
	 *            generated
	 * @param noticeNo - notice no
	 * @param noticeType - type of notice
	 * @param fileStream - input stream of generated notice.           
	 * 
	 */
	public PtNotice saveNotice(String noticeNo, String noticeType, BasicProperty basicProperty, InputStream fileStream) {
		PtNotice ptNotice = new PtNotice();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		ptNotice.setModuleId(module.getId());
		ptNotice.setNoticeDate(new Date());
		ptNotice.setNoticeNo(noticeNo);
		ptNotice.setNoticeType(noticeType);
		ptNotice.setUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		ptNotice.setBasicProperty(basicProperty);
		ptNotice.setIsBlob('Y');
		try {
			ptNotice.setNoticeFile(IOUtils.toByteArray(fileStream));
		} catch (IOException e) {
			LOGGER.error("Exception while saving Bill notice.",e);
			throw new EGOVRuntimeException("Exception while saving Bill notice.", e);
		}
		basicProperty.addNotice(ptNotice);
		basicPrpertyService.update(basicProperty);
		return ptNotice;
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}
}
