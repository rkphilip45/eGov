package org.egov.web.actions.masters;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bank;
import org.egov.commons.utils.BankAccountType;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;

@ParentPackage("egov")
public class BankAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private Bank bank = new Bank();
	private PersistenceService<Bank, Integer> bankPersistenceService;
	private String mode;
	// For jquery BankName auto complete
	private String term;

	@Override
	public void prepare() {
		// DO NOTHING
	}

	@Override
	@SkipValidation
	public String execute() {
		if ("MODIFY".equals(mode)) {
			if (StringUtils.isBlank(bank.getName())) {
				return "search";
			}
			else {
				bank = bankPersistenceService.find("FROM Bank WHERE name = ?", bank.getName());
				if(bank==null)
				{
					return "search";
				}else{
				return "modify";
				}
			}
		} else if ("UNQ_NAME".equals(mode)) {
			checkUniqueBankName();
		} else if ("UNQ_CODE".equals(mode)) {
			checkUniqueBankCode();
		} else if ("AUTO_COMP_BANK_NAME".equals(mode)) {
			populateBankNames();
		}
		return SUCCESS;
	}

	@Override
	public Object getModel() {
		return bank;
	}

	public String save() {
		try {
			if (bank.getId() == null) {
				// TODO Dirty Code can be avoided by extending BaseModel for Bank
				final Date currentDate = new Date();
				bank.setCreated(currentDate);
				bank.setLastmodified(currentDate);
				bank.setModifiedby(BigDecimal.valueOf(Double.valueOf(EGOVThreadLocals.getUserId())));
				bankPersistenceService.persist(bank);
			} else {
				bank.setLastmodified(new Date());
				bank.setModifiedby(BigDecimal.valueOf(Double.valueOf(EGOVThreadLocals.getUserId())));
				bankPersistenceService.update(bank);
			}
			addActionMessage(getText("Bank Saved Successfully"));
		} catch (final ConstraintViolationException e) {
			throw new ValidationException(Arrays.asList(new ValidationError("Duplicate Bank", "Duplicate Bank")));
		} catch (final Exception e) {
			addActionMessage(getText("Bank information can't be saved."));
			throw new ValidationException(Arrays.asList(new ValidationError("An error occured contact Administrator", "An error occured contact Administrator")));
		}
		return "modify";
	}

	private void checkUniqueBankCode() {
		final Bank bank = bankPersistenceService.find("from Bank where code=?", this.bank.getCode());
		writeToAjaxResponse(String.valueOf(bank == null));
	}

	private void checkUniqueBankName() {
		final Bank bank = bankPersistenceService.find("from Bank where name=?", this.bank.getName());
		writeToAjaxResponse(String.valueOf(bank == null));
	}

	private void populateBankNames() {
		final JSONArray jsonArray = new JSONArray(persistenceService.findAllBy("select name FROM Bank WHERE lower(name) like ?", StringUtils.lowerCase(term + "%")));
		writeToAjaxResponse(jsonArray.toString());
	}

	public String getBankAccountTypesJSON() {
		final StringBuilder bankAcTypesJson = new StringBuilder(":;");
		for (final BankAccountType value : BankAccountType.values()) {
			bankAcTypesJson.append(value.name()).append(":").append(value.name()).append(";");
		}
		bankAcTypesJson.deleteCharAt(bankAcTypesJson.lastIndexOf(";"));
		return bankAcTypesJson.toString();
	}

	public String getFundsJSON() {
		final List<Object[]> funds = persistenceService.findAllBy("SELECT id, name FROM Fund WHERE isactive=?", Boolean.TRUE);
		final StringBuilder fundJson = new StringBuilder(":;");
		for (final Object [] fund : funds) {
			fundJson.append((Integer)fund[0]).append(":").append(fund[1]).append(";");
		}
		fundJson.deleteCharAt(fundJson.lastIndexOf(";"));
		return fundJson.toString();
	}

	public String getAccountTypesJSON() {
		final List<Object[]> accounttypes = persistenceService.findAllBy("SELECT name,id FROM CChartOfAccounts WHERE glcode LIKE '450%' AND classification=3 AND  UPPER(name) LIKE '%BANK%' ORDER BY glcode");
		final StringBuilder accountdetailtypeJson = new StringBuilder("{\"\":\"\",");
		for (Object[] accType : accounttypes) {
			accType[0] = org.egov.infstr.utils.StringUtils.escapeJavaScript((String)accType[0]); 
			accountdetailtypeJson.append("\"").append(accType[1]+"#"+accType[0]).append("\"").append(":").append("\"").append(accType[0]).append("\"").append(",");
		}
		accountdetailtypeJson.deleteCharAt(accountdetailtypeJson.lastIndexOf(","));
		return accountdetailtypeJson.append("}").toString();
	}

	@Override
	public void validate() {
		if (bank.getName().equals("")) {
			addFieldError("name", getText("bank.name.field.required"));
		}
		if (bank.getCode().equals("")) {
			addFieldError("code", getText("bank.code.field.required"));
		}
	}

	private void writeToAjaxResponse(final String response) {
		try {
			final HttpServletResponse httpResponse = ServletActionContext.getResponse();
			final Writer httpResponseWriter = httpResponse.getWriter();
			IOUtils.write(response, httpResponseWriter);
			IOUtils.closeQuietly(httpResponseWriter);
		}
		catch (final IOException e) {
			LOG.error("Error occurred while processing Ajax response", e);
		}
	}

	public void setTerm(final String term) {
		this.term = term;
	}

	public void setBankPersistenceService(final PersistenceService<Bank, Integer> bankPersistenceService) {
		this.bankPersistenceService = bankPersistenceService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}
}
