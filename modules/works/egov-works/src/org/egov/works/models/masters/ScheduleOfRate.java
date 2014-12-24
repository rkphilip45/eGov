package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Period;
import org.egov.infstr.ValidationError;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.utils.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.Valid;
import org.joda.time.LocalDate;

public class ScheduleOfRate extends BaseModel {
	static Integer MAX_DESCRIPTION_LENGTH = 100;

	@NotEmpty(message="sor.code.not.empty")  
	private String code;
	@Required(message="sor.category.not.null")
	
	private ScheduleCategory category; 
	
	@NotEmpty(message="sor.description.not.empty")
	private String description;
	@Required(message="sor.uom.not.null")
    private EgUom uom; 
	
    private ScheduleOfRateType type;
    
    public ScheduleOfRate(){}
    public ScheduleOfRate(String code,String description){
    	this.code = code;
    	this.description = description;
    }
    
   private List<Rate> rates = new LinkedList<Rate>();
   private List<MarketRate> marketRates = new LinkedList<MarketRate>();
   
	
   public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    @Valid
    public ScheduleCategory getCategory() {
		return category;
	}
	public void setCategory(ScheduleCategory category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public String getDescriptionJS() {
		return StringUtils.escapeJavaScript(description);
	}
	public String getDescriptionJSON() {
		return StringUtils.escapeJSON(description);
	}
	public String getSummaryJSON(){
		return StringUtils.escapeJSON(getSummary());		
	}

	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}
	public EgUom getUom() {
		return uom;
	}
	public void setUom(EgUom uomid) {
		this.uom = uomid;
	}
	public ScheduleOfRateType getType() {
		return type;
	}
	public void setType(ScheduleOfRateType type) {
		this.type = type;
	}

    public List<Rate> getRates() {
		return rates;
	}
	
	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}
	
	public String getSummary(){
		if(description.length()<=MAX_DESCRIPTION_LENGTH){
			return description;
		}
		return first(MAX_DESCRIPTION_LENGTH/2,description)+"..."+last(MAX_DESCRIPTION_LENGTH/2,description);
		
	}
	
	public String getSummaryJS(){
		return StringUtils.escapeJavaScript(getSummary());		
	}

	public String getCategorId(){
		return String.valueOf(this.category.getId());
	}
	
	protected String first(int number,String description) {
		return description.substring(0, number>=description.length()?description.length():number);
	}
	protected String last(int number,String description) {
		int begin = description.length() - number;
		return description.substring(begin<0?description.length():begin, description.length());
	}
	
	public String getSearchableData(){
		StringBuilder builder = new StringBuilder();
		builder.append(getCode()).append(" ").append(getDescription());
		return builder.toString();
	}
	public Rate getRateOn(Date estimateDate) {
		if(estimateDate==null) {
			throw new EGOVRuntimeException("no.rate.for.date");
		}
		for (Rate rate : rates) {
			if(isWithin(rate.getValidity(), estimateDate)){
				return rate;
			}
		}
		throw new EGOVRuntimeException("no.rate.for.date");
	}
	
	public boolean isWithin(Period period,Date dateTime){
		LocalDate start=new LocalDate(period.getStartDate()); 
		LocalDate end=null;
		if(period.getEndDate()!=null){
			end=new LocalDate(period.getEndDate());
		}
		LocalDate date=new LocalDate(dateTime);
		
		if(end==null) {
			return start.compareTo(date)<=0;
		}
		else {
			return start.compareTo(date)<=0 && end.compareTo(date)>=0;
		}
		
		
		//return (end!=null)? start.compareTo(date)<=0 && end.compareTo(date)>=0 : start.compareTo(date)<=0;
		
	}
	public boolean hasValidRateFor(Date estimateDate) {
		try{
			Rate rate=getRateOn(estimateDate);
			return rate!=null;
		}catch(EGOVRuntimeException e){
			return false;
		}
	}
	private List<ValidationError> checkForNoRatePresent() {
		
		if(rates!= null && rates.isEmpty()) {
			return Arrays.asList(new ValidationError("sorRate","sor.rate.altleastone_sorRate_needed"));
		}
		else {
			return null;
		}
		
		//return (rates.isEmpty())?null:Arrays.asList(new ValidationError("sorRate","sor.rate.altleastone_sorRate_needed"));
	}	
	
	private void removeEmptyRates() {
		List<Rate> emptyRateObjs = new LinkedList<Rate>();
		for(Rate rat : rates) {
			 if((rat.getRate() == null || rat.getRate().getValue() == 0.0) && (rat.getValidity() == null || (rat.getValidity().getStartDate() == null && rat.getValidity().getEndDate() == null))) {
				emptyRateObjs.add(rat);
			 }
		}
		rates.removeAll(emptyRateObjs);
	}
	
	protected List<ValidationError> validateRates() {
		List<ValidationError> errorList = null;
		boolean openEndedRangeFlag = false;
		
		for (Rate rate : rates) {
			if(rate.getValidity().getEndDate() == null && openEndedRangeFlag) {
				return Arrays.asList(new ValidationError("openendedrange","sor.rate.multiple.openendedrange"));
			}
			if(rate.getValidity().getEndDate() == null)	{
				openEndedRangeFlag = true;
			}
			
			errorList = rate.validate();
			
			if(errorList!=null)	{
				return errorList;
			}
		}		
		return errorList;
	}
	
	public void setRate(List<Rate> rates) {
		this.rates = rates;
	}
	public void addRate(Rate rat){
		this.rates.add(rat);
	}
	
	private List<ValidationError> validateDateRanges() {		
		List<Period> validDates = new ArrayList<Period>();		
		validDates.add(0,((Rate)rates.get(0)).getValidity());
		Date existingStartDate = null;
		Date existingEndDate = null;
		Date checkStartDate = null; 
		Date checkEndDate = null;
		Period existingPeriod = null;
		Period checkPeriod1 = null;
		boolean flag1 = true;
		int k=1;
		
		for(int i=1;i<rates.size();i++)	{
			checkStartDate = ((Rate)rates.get(i)).getValidity().getStartDate();
			checkEndDate = ((Rate)rates.get(i)).getValidity().getEndDate();
			checkPeriod1 = new Period(checkStartDate, checkEndDate);
			
			for(int j=0;j<validDates.size();j++) {
				existingStartDate = ((Period)validDates.get(j)).getStartDate();
				existingPeriod = (Period)validDates.get(j);
				
				if(validDates.get(j).getEndDate()==null) {
					existingEndDate=null;
				}else{
					existingEndDate=((Period)validDates.get(j)).getEndDate();
				}
				
				//existingEndDate=(validDates.get(j).getEndDate()!=null)?((Period)validDates.get(j)).getEndDate():null;
				
				//check if the period to be checked is within any of the existing periods.
				if(isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate) || (checkEndDate!=null && isWithin(existingPeriod, checkEndDate)) || (existingEndDate!=null && isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break;
				}
				else if((checkEndDate!=null && existingEndDate!=null) && (isWithin(existingPeriod, checkEndDate) ||  isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break; 
				}
			}
		
		if(flag1) {		
			validDates.add(k++, checkPeriod1);
		}
		else {			
			return Arrays.asList(new ValidationError("dateoverlap","sor.rate.dates.overlap"));
		}			
	}		
	return null;
	}
	

	/* start market rate */
	/**
	 * @return the marketRates
	 */
	public List<MarketRate> getMarketRates() {
		return marketRates;
	}
	/**
	 * @param marketRates the marketRates to set
	 */
	public void setMarketRates(List<MarketRate> marketRates) {
		this.marketRates = marketRates;
	}
	
	/* market rate */
	public MarketRate getMarketRateOn(Date estimateDate) {
		if(estimateDate==null) {
			throw new EGOVRuntimeException("no.marketrate.for.date");
		}
		for (MarketRate marketRate : marketRates) {
			if(isWithin(marketRate.getValidity(), estimateDate)){
				return marketRate;
			}
		}
		throw new EGOVRuntimeException("no.marketrate.for.date");
	}
	
	public boolean hasValidMarketRateFor(Date estimateDate) {
		try{
			MarketRate marketRate=getMarketRateOn(estimateDate);
			return marketRate!=null;
		}catch(EGOVRuntimeException e){
			return false;
		}
	}
	/*
	private List<ValidationError> checkForNoMarketRatePresent() {	
		if(marketRates!= null && marketRates.size() == 0) {			
			return Arrays.asList(new ValidationError("sorMarketRate","sor.marketrate.altleastone_sorRate_needed"));		
		}
		else {			
			return null;
		}	
	}
	*/
	private void removeEmptyMarketRates() {
		List<MarketRate> emptyMarketRateObjs = new LinkedList<MarketRate>();		
		for(MarketRate marketRate : marketRates) {
			if((marketRate.getMarketRate() == null || marketRate.getMarketRate().getValue() == 0.0) && (marketRate.getValidity() == null || (marketRate.getValidity().getStartDate() == null && marketRate.getValidity().getEndDate() == null))) {								
				emptyMarketRateObjs.add(marketRate);
			}
		}
		marketRates.removeAll(emptyMarketRateObjs);		
	}
	
	protected List<ValidationError> validateMarketRates() {
		List<ValidationError> errorList = null;
		boolean openEndedRangeFlag = false;
		
		for (MarketRate marketRate : marketRates) {
			if(marketRate!=null){
			if(marketRate.getValidity().getEndDate() == null && openEndedRangeFlag) {
				return Arrays.asList(new ValidationError("openendedrange","sor.marketrate.multiple.openendedrange"));
			}
			if(marketRate.getValidity().getEndDate() == null)	{
				openEndedRangeFlag = true;
			}
			errorList = marketRate.validate();
			if(errorList!=null)	{
				return errorList;
			}
			}
		}		
		return errorList;
	}	
	
	public void setMarketRate(List<MarketRate> marketRates) {
		this.marketRates = marketRates;
	}
	
	public void addMarketRate(MarketRate marketRate){
		this.marketRates.add(marketRate);
	}
	
	private List<ValidationError> validateDateRangesForMarketRate() {
		List<Period> validDates = new ArrayList<Period>();		
		validDates.add(0,((MarketRate)marketRates.get(0)).getValidity());
		Date existingStartDate = null;
		Date existingEndDate = null;
		Date checkStartDate = null; 
		Date checkEndDate = null;
		Period existingPeriod = null;
		Period checkPeriod1 = null;
		boolean flag1 = true;
		int k=1;
		
		for(int i=1;i<marketRates.size();i++)	{
			checkStartDate = ((MarketRate)marketRates.get(i)).getValidity().getStartDate();
			checkEndDate = ((MarketRate)marketRates.get(i)).getValidity().getEndDate();
			checkPeriod1 = new Period(checkStartDate, checkEndDate);
			
			for(int j=0;j<validDates.size();j++) {
				existingStartDate = ((Period)validDates.get(j)).getStartDate();
				existingPeriod = (Period)validDates.get(j);
				if(validDates.get(j).getEndDate()==null) {

					existingEndDate=null;
				}else{
					existingEndDate=((Period)validDates.get(j)).getEndDate();
				}
				
				//check if the period to be checked is within any of the existing periods.
				if(isWithin(existingPeriod, checkStartDate) || isWithin(checkPeriod1, existingStartDate) || (checkEndDate!=null && isWithin(existingPeriod, checkEndDate)) || (existingEndDate!=null && isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break;
				}
				else if((checkEndDate!=null && existingEndDate!=null) && (isWithin(existingPeriod, checkEndDate) ||  isWithin(checkPeriod1, existingEndDate)))	{
					flag1 = false;
					break; 
				}
			}
			if(flag1) {
				validDates.add(k++, checkPeriod1);
			}
			else {
				return Arrays.asList(new ValidationError("dateoverlap","sor.marketrate.dates.overlap"));
			}			
		}	
		
		return null;
	}
	/* ends market rate */
	
	public List<ValidationError> validate()	{
		List<ValidationError> errorList = null;
		removeEmptyRates();
		if(marketRates!=null && !marketRates.isEmpty()){
			removeEmptyMarketRates();
		}
		
		if((errorList = checkForNoRatePresent()) != null) {
			return errorList;
		}
		
		if((errorList = validateDateRanges())!=null) {
			return errorList;
		}

		if((errorList = validateRates())!=null) {
			return errorList;
		}
		
		/* for market rate */
		if(marketRates!=null && !marketRates.isEmpty()){
			if((errorList = validateDateRangesForMarketRate())!=null) {
				return errorList;
			}
			if((errorList = validateMarketRates())!=null) {
				return errorList;
			}
		}
			
		return errorList;
		
	}
}