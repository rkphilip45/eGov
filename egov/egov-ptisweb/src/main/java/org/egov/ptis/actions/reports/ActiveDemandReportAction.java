package org.egov.ptis.actions.reports;

import static java.lang.Boolean.FALSE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.ptis.bean.ActiveDemandInfo;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.opensymphony.xwork2.validator.annotations.Validation;

/**
 * 
 * @author subhash
 *
 */
@ParentPackage("egov")
@Validation
public class ActiveDemandReportAction extends BaseFormAction {

	private static final String WARD = "Ward";
	private static final String PART_NO = "PartNo";
	private static final String TOTAL = "Total";
	private static final long serialVersionUID = 1L;
	private Date asOnDate;
	private Boolean objPropsIncluded = Boolean.FALSE;
	private List propertyTypes;
	private Map<String, Object> params = new HashMap<String, Object>();
	private List<ActiveDemandInfo> resultList;
	private Boolean resultPage = Boolean.FALSE;
	private Integer boundaryId;
	private String reportType;
	private String selectedPropertyTypes = new String();

	@SuppressWarnings("unchecked")
	public void prepare() {
		List<PropertyTypeMaster> propTypes = persistenceService.findAllBy("from PropertyTypeMaster order by orderNo");
		addDropdownData("propTypes", propTypes);
		
		int i = 0;
		if (propertyTypes != null) {
			for (Object typeId : propertyTypes) {
				for (PropertyTypeMaster type : propTypes) {
					if (type.getId().equals(Long.valueOf(String.valueOf(typeId)))) {
						if (i == 0) {
							selectedPropertyTypes = type.getType();
						} else {
							selectedPropertyTypes = selectedPropertyTypes + ", " + type.getType();
						}
						i++;
						break;
					}
				}
			}
		}
	}

	@Override
	public Object getModel() {
		return null;
	}

	@SkipValidation
	public String newForm() {
		return NEW;
	}

	public SQLQuery prepareQuery() {
		StringBuffer queryStr = new StringBuffer("");
		String groupBy = null;
		String orderBy = null;
		if (reportType != null && reportType.equalsIgnoreCase(WARD)) {
			queryStr.append("SELECT WARDBNDRY.ID_BNDRY \"boundaryId\", WARDBNDRY.NAME \"boundaryName\", \"count\", \"arrDmd\", \"currDmd\", \"arrDmd\" + \"currDmd\" \"totDmd\""
					+ " FROM (SELECT ACTDMD.WARDID \"wardId\", COUNT(*) \"count\", SUM(ACTDMD.ARREAR_DEMAND) \"arrDmd\", SUM(ACTDMD.CURR_DEMAND) \"currDmd\" ");
			groupBy = new String(
					" GROUP BY ACTDMD.WARDID), EG_BOUNDARY WARDBNDRY WHERE \"wardId\" = WARDBNDRY.ID_BNDRY");
			orderBy = new String(" order by \"wardId\"");
		} else if (reportType != null && reportType.equalsIgnoreCase(PART_NO)) {
			queryStr.append("SELECT ACTDMD.PARTNO \"partNo\", COUNT(*) \"count\", SUM(ACTDMD.ARREAR_DEMAND) \"arrDmd\", SUM(ACTDMD.CURR_DEMAND) \"currDmd\", SUM(ACTDMD.ARREAR_DEMAND) + SUM(ACTDMD.CURR_DEMAND) \"totDmd\"  ");
			groupBy = new String(" GROUP BY ACTDMD.PARTNO");
			orderBy = new String(" order by \"partNo\"");
		} else {
			queryStr.append("SELECT ZONEBNDRY.ID_BNDRY \"boundaryId\", ZONEBNDRY.NAME \"boundaryName\", \"count\", \"arrDmd\", \"currDmd\", \"arrDmd\" + \"currDmd\" \"totDmd\""
					+ " FROM (SELECT ACTDMD.ZONEID \"ZONEID\", COUNT(*) \"count\", NVL(SUM(ACTDMD.ARREAR_DEMAND), 0) \"arrDmd\", NVL(SUM(ACTDMD.CURR_DEMAND), 0) \"currDmd\" ");
			groupBy = new String(
					" GROUP BY ACTDMD.ZONEID), EG_BOUNDARY ZONEBNDRY WHERE \"ZONEID\" = ZONEBNDRY.ID_BNDRY");
			orderBy = new String(" order by \"ZONEID\"");
		}
		String fromClause = new String(" FROM EGPT_MV_ACTIVE_DEMAND ACTDMD, (SELECT UPICNO,MAX(DMD_ACTIVATION_DATE) AS MXDATE FROM EGPT_MV_ACTIVE_DEMAND");
		StringBuffer whereClause = new StringBuffer("");
		whereClause = prepareSearchCriteria(whereClause);
		whereClause.append(" GROUP BY UPICNO) M where ACTDMD.UPICNO = M.UPICNO AND ACTDMD.DMD_ACTIVATION_DATE = M.MXDATE ");
		queryStr.append(fromClause).append(whereClause).append(groupBy).append(orderBy);
		SQLQuery query = persistenceService.getSession().createSQLQuery(queryStr.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(ActiveDemandInfo.class));
		return query;
	}

	private StringBuffer prepareSearchCriteria(StringBuffer whereClause) {
		if (propertyTypes != null && !propertyTypes.isEmpty()) {
			whereClause.append(" WHERE PROPTYMASTER in (:propTypes)");
			params.put("propTypes", propertyTypes);
		}
		if (asOnDate != null) {
			whereClause.append(" AND DMD_ACTIVATION_DATE <= (:asOnDate)");
			params.put("asOnDate", asOnDate);
		}
		if (objPropsIncluded == null || objPropsIncluded.equals(FALSE)) {
			whereClause.append(" AND STATUS <> 'O'");
		}
		if (reportType != null && reportType.equalsIgnoreCase(WARD)) {
			whereClause.append(" and ZONEID = :zoneId");
			params.put("zoneId", boundaryId);
		} else if (reportType != null && reportType.equalsIgnoreCase(PART_NO)) {
			whereClause.append(" and WARDID = :wardId");
			params.put("wardId", boundaryId);
		}
		return whereClause;
	}

	@ValidationErrorPage(value = NEW)
	public String search() {
		resultPage = Boolean.TRUE;
		SQLQuery query = prepareQuery();
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value instanceof Collection) {
				query.setParameterList(key, (Collection) value);
			} else {
				query.setParameter(key, value);
			}
		}
		resultList = query.list();
		prepareTotals();
		return NEW;
	}

	private void prepareTotals() {
		ActiveDemandInfo totals = new ActiveDemandInfo();
		if (resultList != null && !resultList.isEmpty()) {
			if (reportType != null && reportType.equalsIgnoreCase(PART_NO)) {
				totals.setPartNo(TOTAL);
			} else {
				totals.setBoundaryName(TOTAL);
			}
			totals.setArrDmd(BigDecimal.ZERO);
			totals.setCurrDmd(BigDecimal.ZERO);
			totals.setCount(BigDecimal.ZERO);
			totals.setTotDmd(BigDecimal.ZERO);
		}
		for (ActiveDemandInfo activeDemandInfo : resultList) {
			totals.setArrDmd(totals.getArrDmd().add(activeDemandInfo.getArrDmd()));
			totals.setCurrDmd(totals.getCurrDmd().add(activeDemandInfo.getCurrDmd()));
			totals.setCount(new BigDecimal(totals.getCount() + activeDemandInfo.getCount()));
			totals.setTotDmd(totals.getTotDmd().add(activeDemandInfo.getTotDmd()));
		}
		if (resultList != null && !resultList.isEmpty()) {
			resultList.add(totals);
		}
	}

	public void validate() {
		if (propertyTypes == null || propertyTypes.isEmpty()) {
			addActionError(getText("mandatory.propTypes"));
		}
	}

	public Date getAsOnDate() {
		return asOnDate;
	}

	public void setAsOnDate(Date asOnDate) {
		this.asOnDate = asOnDate;
	}

	public Boolean getObjPropsIncluded() {
		return objPropsIncluded;
	}

	public void setObjPropsIncluded(Boolean objPropsIncluded) {
		this.objPropsIncluded = objPropsIncluded;
	}

	public List getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(List propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public List<ActiveDemandInfo> getResultList() {
		return resultList;
	}

	public void setResultList(List<ActiveDemandInfo> resultList) {
		this.resultList = resultList;
	}

	public Boolean getResultPage() {
		return resultPage;
	}

	public void setResultPage(Boolean resultPage) {
		this.resultPage = resultPage;
	}

	public Integer getBoundaryId() {
		return boundaryId;
	}

	public void setBoundaryId(Integer boundaryId) {
		this.boundaryId = boundaryId;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getSelectedPropertyTypes() {
		return selectedPropertyTypes;
	}

	public void setSelectedPropertyTypes(String selectedPropertyTypes) {
		this.selectedPropertyTypes = selectedPropertyTypes;
	}

}
