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
package org.egov.infstr.models;

import java.util.Map;

public class GeoLocation {

	private GeoLatLong geoLatLong;

	private String info1;

	private String info2;

	private String info3;

	private String info4;

	private String urlRedirect;

	private String urlDisplay;

	// Meta data information for the marker option
	private Map<String, Object> markerOptionData;

	public GeoLatLong getGeoLatLong() {
		return geoLatLong;
	}

	public String getInfo1() {
		return info1;
	}

	public String getInfo2() {
		return info2;
	}

	public String getInfo3() {
		return info3;
	}

	public String getInfo4() {
		return info4;
	}

	public String getUrlRedirect() {
		return urlRedirect;
	}

	public String getUrlDisplay() {
		return urlDisplay;
	}

	public void setGeoLatLong(GeoLatLong geoLatLong) {
		this.geoLatLong = geoLatLong;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public void setInfo3(String info3) {
		this.info3 = info3;
	}

	public void setInfo4(String info4) {
		this.info4 = info4;
	}

	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}

	public void setUrlDisplay(String urlDisplay) {
		this.urlDisplay = urlDisplay;
	}

	public Map<String, Object> getMarkerOptionData() {
		return markerOptionData;
	}

	public void setMarkerOptionData(Map<String, Object> markerOptionData) {
		this.markerOptionData = markerOptionData;
	}

}
