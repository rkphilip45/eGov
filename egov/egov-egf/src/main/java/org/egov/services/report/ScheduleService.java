package org.egov.services.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.Constants;
import org.egov.web.actions.report.IEStatementEntry;
import org.egov.web.actions.report.Statement;
import org.egov.web.actions.report.StatementEntry;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public abstract class ScheduleService {
	private static final Logger	LOGGER	= Logger.getLogger(ScheduleService.class);
	static final BigDecimal NEGATIVE = new BigDecimal("-1");
	GenericHibernateDaoFactory genericDao;
	int minorCodeLength;
	int majorCodeLength;
	int detailCodeLength;
	String voucherStatusToExclude;
	
	/*for detailed*/
	Map<String, Schedules> getScheduleToGlCodeMapDetailed(String reportType,String coaType) {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("SELECT coa1.glcode, s.schedule, s.schedulename, coa1.type, coa1.name" +
				" FROM chartofaccounts coa1, chartofaccounts coa2, chartofaccounts coa3, schedulemapping s" +
				" WHERE coa3.scheduleid  = s.id AND coa3.id = coa2.parentid AND coa2.id = coa1.parentid" +
				" AND coa3.classification=2 AND coa2.classification=3 AND coa1.classification=4" +
				" AND coa3.type         IN "+coaType+" AND coa2.type IN "+coaType+" AND coa1.type IN "+coaType+"" +
				" AND s.reporttype = '"+reportType+"' ORDER BY coa1.glcode");
		List<Object[]> results = query.list();
		Map<String,Schedules> scheduleMap = new LinkedHashMap<String, Schedules>();
		for (Object[] row : results) {
			if(!scheduleMap.containsKey(row[1].toString()))
				scheduleMap.put(row[1].toString(), new Schedules(row[1].toString(),row[2].toString()));
			scheduleMap.get(row[1].toString())
			.addChartOfAccount(new ChartOfAccount(row[0].toString(),row[3].toString(),row[4].toString()));
		}
		return scheduleMap;
	}
	
	Map<String, Schedules> getScheduleToGlCodeMap(String reportType,String coaType) {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select distinct coa.glcode,s.schedule,s.schedulename," +
				"coa.type,coa.name from chartofaccounts coa, schedulemapping s where s.id=coa.scheduleid and " +
				"coa.classification=2 and s.reporttype = '"+reportType+"' and coa.type in "+coaType+" " +
				"order by coa.glcode");
		List<Object[]> results = query.list();
		Map<String,Schedules> scheduleMap = new LinkedHashMap<String, Schedules>();
		for (Object[] row : results) {
			if(!scheduleMap.containsKey(row[1].toString())) scheduleMap.put(row[1].toString(), new Schedules(row[1].toString(),row[2].toString()));
			scheduleMap.get(row[1].toString()).addChartOfAccount(new ChartOfAccount(row[0].toString(),row[3].toString(),row[4].toString()));
		}
		return scheduleMap;
	}
	
	List<Object[]> getAllGlCodesForAllSchedule(String reportType,String coaType) {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select distinct coa.majorcode,s.schedule,s.schedulename," +
				"coa.type from chartofaccounts coa, schedulemapping s where s.id=coa.scheduleid and " +
				"coa.classification=2 and s.reporttype = '"+reportType+"' and coa.type in "+coaType+" " +
				"group by coa.majorcode,s.schedule,s.schedulename,coa.type order by coa.majorcode");
		return query.list();
	}

	List<Object[]> amountPerFundQueryForAllSchedules(String filterQuery, Date toDate, Date fromDate,String reportType) {
		String voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select sum(debitamount)-sum(creditamount),v.fundid,substr(c.glcode,0,"+minorCodeLength+")," +
				"c.name from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where  " +
				" v.id=g.voucherheaderid and c.id=g.glcodeid and v.id=mis.voucherheaderid and v.status not in("+voucherStatusToExclude+")  AND v.voucherdate <= '"+
				getFormattedDate(toDate)+"' and v.voucherdate >='"+getFormattedDate(fromDate)+
				"' and substr(c.glcode,0,"+minorCodeLength+") in (select distinct coa2.glcode from chartofaccounts coa2, " +
						"schedulemapping s where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = '"+reportType+"') "+filterQuery+
						" group by v.fundid,substr(c.glcode,0,"+minorCodeLength+"),c.name order by substr(c.glcode,0,"+minorCodeLength+")");
		
		return query.list();  
	}
	/*For view all schedules Detail*/
	List<Object[]> amountPerFundQueryForAllSchedulesDetailed(String filterQuery, Date toDate, Date fromDate,String reportType) {
		String voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select sum(debitamount)-sum(creditamount),v.fundid,substr(c.glcode,0,"+detailCodeLength+")," +
				"c.name from generalledger g,chartofaccounts c,voucherheader v ,vouchermis mis where  " +
				" v.id=g.voucherheaderid and c.id=g.glcodeid and v.id=mis.voucherheaderid and v.status not in("+voucherStatusToExclude+")  AND v.voucherdate <= '"+
				getFormattedDate(toDate)+"' and v.voucherdate >='"+getFormattedDate(fromDate)+
				"' and substr(c.glcode,0,"+detailCodeLength+") in (select DISTINCT coa4.glcode from chartofaccounts coa4 where coa4.parentid in (SELECT coa3.id" +
						" FROM chartofaccounts coa3 WHERE coa3.parentid IN(select coa2.id from chartofaccounts coa2, " +
						"schedulemapping s where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = '"+reportType+"'))) "+filterQuery+
						" group by v.fundid,substr(c.glcode,0,"+detailCodeLength+"),c.name order by substr(c.glcode,0,"+detailCodeLength+")");
		
		return query.list();  
	}
	
	public String getAppConfigValueFor(String module,String key){
		return genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key).get(0).getValue();
	}
	
	public String getFormattedDate(Date date){
		return Constants.DDMMYYYYFORMAT1.format(date);
	}

	void addRowToStatement(Statement statement, Object[] row,String glCode) {
		StatementEntry entry = new StatementEntry();
		entry.setGlCode(glCode);
		entry.setAccountName(row[3].toString());
		statement.add(entry);
	}
	public List<Fund> getFunds() {
		Criteria voucherHeaderCriteria =HibernateUtil.getCurrentSession().createCriteria(CVoucherHeader.class);
		List fundIdList = voucherHeaderCriteria.setProjection(Projections.distinct(Projections.property("fundId.id"))).list();
		if (!fundIdList.isEmpty())
			return HibernateUtil.getCurrentSession().createCriteria(Fund.class).add(Restrictions.in("id", fundIdList)).list();
		return new ArrayList<Fund>();
	}
	
	
	protected List<Object[]> getAllGlCodesForSubSchedule(String majorCode,Character type,String reportType){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Getting schedule for "+majorCode);
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select distinct coa.glcode,coa.name,s.schedule,s.schedulename from chartofaccounts coa, " +
				"schedulemapping s where s.id=coa.scheduleid and coa.classification=2 and s.reporttype = '"+reportType+"' and coa.majorcode='"+
				majorCode+"' and coa.type='"+type+"' order by coa.glcode");
		return query.list();
	}
	protected List<Object[]> getAllGlCodesForSchedule(String reportType){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Getting schedule for ");
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("SELECT coa1.glcode, s.schedule, s.schedulename, coa1.type, coa1.name"+
				" FROM chartofaccounts coa1, chartofaccounts coa2, chartofaccounts coa3, schedulemapping s"+
				" WHERE coa3.scheduleid  = s.id AND coa3.id = coa2.parentid AND coa2.id = coa1.parentid"+
				" AND coa3.classification=2 AND coa2.classification=3 AND coa1.classification=4"+
				" AND coa3.type   IN "+reportType+" AND coa2.type IN "+reportType+" AND coa1.type IN "+reportType+
				" AND s.reporttype = 'IE' ORDER BY coa1.glcode");
		return query.list();
	}
	protected List<Object[]> getAllDetailGlCodesForSubSchedule(String majorCode,Character type,String reportType){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Getting detail codes for "+majorCode+"reporttype"+reportType);
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select distinct coad.glcode,coad.name from chartofaccounts coa,chartofaccounts coad,"+
                    " schedulemapping s " +
				" where    s.id=coa.scheduleid  AND coa.classification=2 AND s.reporttype='"+reportType+"' and coad.majorcode='"+
				majorCode+"' and coa.type='"+type+"' and  coa.glcode=SUBSTR(coad.glcode,0,"+minorCodeLength+") and coad.classification=4 order by coad.glcode");
		return query.list();  
	}
	
	protected List<Object[]> getSchedule(String majorCode,Character type,String reportType){
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select distinct coa.glcode,coa.name,s.schedule,s.schedulename from chartofaccounts coa, " +
				"schedulemapping s where s.id=coa.scheduleid and coa.classification=2 and s.reporttype = '"+reportType+"' and coa.majorcode='"+
				majorCode+"' and coa.type='"+type+"' order by coa.glcode");
			
		return query.list();
	}
	
	protected List<Object[]> getAllLedgerTransaction(String majorcode,Date toDate,Date fromDate,String fundId,String filterQuery){
		if(LOGGER.isInfoEnabled())     LOGGER.info("Getting ledger transactions details where >>>> EndDate="+toDate +"from Date="+fromDate);
		String voucherStatusToExclude = getAppConfigValueFor("finance", "statusexcludeReport");
		String majorCodeQuery="";
		if(!majorcode.equals(""))
			majorCodeQuery=" and g.glcode like '"+majorcode+"%'";        
			    
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select g.glcode,coa.name,sum(g.debitamount)-sum(g.creditamount),v.fundid,coa.type,coa.majorcode from generalledger g,chartofaccounts coa ,"+
				"voucherheader v,vouchermis mis where v.id=mis.voucherheaderid and g.voucherheaderid=v.id and g.glcodeid=coa.id and v.voucherdate BETWEEN '"+ getFormattedDate(fromDate) +"' and '"+ 
				getFormattedDate(toDate)+"' and v.status not in ("+voucherStatusToExclude+") and v.id=g.voucherheaderid and v.fundid in"+fundId+filterQuery+ " and g.glcodeid=coa.id  " +
						"GROUP by g.glcode,coa.name,v.fundid ,coa.type ,coa.majorcode order by g.glcode,coa.name,coa.type");    
		return query.list();
	}
	
	List<Object[]> getRowsForGlcode(List<Object[]> resultMap, String glCode) {
		List<Object[]> rows = new ArrayList<Object[]>();
		for (Object[] row : resultMap) {
			if(row[2].toString().equalsIgnoreCase(glCode))
				rows.add(row);
		}
		return rows;		
	}

	protected void addRowForSchedule(Statement statement,List<Object[]> allGlCodes) {
		if(!allGlCodes.isEmpty()){
			statement.add(new StatementEntry("Schedule "+allGlCodes.get(0)[2].toString()+":",allGlCodes.get(0)[3].toString(),"",null,null,true));
		}
	}
	protected void addRowForIESchedule(Statement statement,List<Object[]> allGlCodes) {
		if(!allGlCodes.isEmpty()){
			statement.addIE(new IEStatementEntry("Schedule "+allGlCodes.get(0)[2].toString()+":",allGlCodes.get(0)[3].toString(),"",true));
		}
	}

	boolean contains(List<Object[]> result,String glCode){
		for (Object[] row : result) {
			if(row[2].toString().equalsIgnoreCase(glCode))
				return true;
		}
		return false;
	}
	
	void computeAndAddTotals(Statement statement) {
		BigDecimal currentTotal = BigDecimal.ZERO;
		BigDecimal previousTotal = BigDecimal.ZERO;
		for (int index = 0; index < statement.size(); index++) {
			if(statement.get(index).getCurrentYearTotal()!=null)
				currentTotal = currentTotal.add(statement.get(index).getCurrentYearTotal());
			if(statement.get(index).getPreviousYearTotal()!=null)
				previousTotal = previousTotal.add(statement.get(index).getPreviousYearTotal());
		}
		statement.add(new StatementEntry(null,"Total","",previousTotal,currentTotal,true));
	}
	
	/*for detailed*/
	void computeAndAddTotalsForSchedules(Statement statement) {
		BigDecimal currentTotal = BigDecimal.ZERO;
		BigDecimal previousTotal = BigDecimal.ZERO;
		Map<String,BigDecimal> fundTotals=new HashMap<String, BigDecimal>() ;
		for(StatementEntry entry : statement.getEntries()){
			if(entry.getAccountName().equals("Schedule Total")){
				entry.setCurrentYearTotal(currentTotal);
				entry.setPreviousYearTotal(previousTotal);
				entry.setFundWiseAmount(fundTotals);
				currentTotal = BigDecimal.ZERO;
				previousTotal = BigDecimal.ZERO;
				fundTotals=new HashMap<String, BigDecimal>();
			}else{
				if(entry.getCurrentYearTotal() != null)
					currentTotal = currentTotal.add(entry.getCurrentYearTotal());
				if(entry.getPreviousYearTotal() != null)
					previousTotal = previousTotal.add(entry.getPreviousYearTotal());
				
				for (Entry<String, BigDecimal> row : entry.getFundWiseAmount().entrySet()) {
						fundTotals.put(row.getKey(),fundTotals.get(row.getKey())!=null?fundTotals.get(row.getKey()).add(zeroOrValue(row.getValue())):zeroOrValue(row.getValue()));
				}
			}
		}
	}
	private BigDecimal zeroOrValue(BigDecimal value) {
		return value == null ? BigDecimal.ZERO : value;
	}

	List<Object[]> currentYearAmountQuery(String filterQuery,Date toDate, Date fromDate, String majorCode,String reportType) {
		Query query =HibernateUtil.getCurrentSession().createSQLQuery("select sum(debitamount)-sum(creditamount),v.fundid,c.glcode " +
				"from generalledger g,chartofaccounts c,voucherheader v,vouchermis mis  where " +
				" v.id=g.voucherheaderid and c.id=g.glcodeid and v.status not in("+voucherStatusToExclude+")  AND v.voucherdate <= '"
				+getFormattedDate(toDate)+"' and v.id=mis.voucherheaderid and v.voucherdate >='"+getFormattedDate(fromDate)+"' " +
				"and c.glcode in (select distinct coad.glcode from chartofaccounts coa2, schedulemapping s " +
				",chartofaccounts coad where s.id=coa2.scheduleid and coa2.classification=2 and s.reporttype = '"+reportType+"'" +
				" and coa2.glcode=SUBSTR(coad.glcode,0,"+minorCodeLength+") and coad.classification=4 and coad.majorcode='"+majorCode+"')  and c.majorcode='"+majorCode+"' and c.classification=4 "+filterQuery+
				" group by v.fundid,c.glcode order by c.glcode");
		return query.list();
	}
	

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
}

class ChartOfAccount{
	public final String glCode;
	public final String type;
	public final String name;
	private static final Logger	LOGGER	= Logger.getLogger(ChartOfAccount.class);
	public ChartOfAccount(String glCode,String type, String name) {
		this.glCode = glCode;
		this.type = type;
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return  31  + ((glCode == null) ? 0 : glCode.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		try{
			ChartOfAccount other = (ChartOfAccount) obj;
			return glCode.equals(other.glCode);
		}catch(Exception e){			
			LOGGER.error("Failed :"+ e.getMessage(),e);
			return false;
		}
	}
}

class Schedules{
	public final String scheduleNumber;
	public final String scheduleName;
	
	public final Set<ChartOfAccount> chartOfAccount = new LinkedHashSet<ChartOfAccount>();

	public Schedules(String scheduleNumber,String scheduleName) {
		this.scheduleNumber = scheduleNumber;
		this.scheduleName = scheduleName;
	}
	public boolean contains(String glCode) {
		return chartOfAccount.contains(new ChartOfAccount(glCode,null,null));
	}
	public String getCoaName(String glCode) {
		for (ChartOfAccount coa : chartOfAccount) {
			if(glCode.equalsIgnoreCase(coa.glCode))
				return coa.name;
		}
		return "";
	}
	public void addChartOfAccount(ChartOfAccount s){
		this.chartOfAccount.add(s);
	}
}


