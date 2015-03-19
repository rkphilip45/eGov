package org.egov.infra.events.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String module;
	private String eventCode;
	private Map<String, String> params;
	private Date dateRaised;
	private String responseType;
	private String responseTemplate;

	public Event(String module, String eventCode, Map<String, String> params) {
		super();
		this.module = module;
		this.eventCode = eventCode;
		this.params = params;
	}

	public void addParam(String key, String value) {
		getParams().put(key, value);
	}

	public void addParams(Map<String, String> params) {

		getParams().putAll(params);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateRaised() {
		return dateRaised;
	}

	public void setDateRaised(Date dateRaised) {
		this.dateRaised = dateRaised;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getResponseTemplate() {
		return responseTemplate;
	}

	public void setResponseTemplate(String responseTemplate) {
		this.responseTemplate = responseTemplate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Event [id=").append(id).append(", module=")
				.append(module).append(", eventCode=").append(eventCode)
				.append(", params=").append(params).append(", dateRaised=")
				.append(dateRaised).append(", responseType=")
				.append(responseType).append(", responseTemplate=")
				.append(responseTemplate);
		return builder.toString();
	}

}