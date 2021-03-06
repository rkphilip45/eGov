/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
/*
* Added By Rajalakshmi D.N. on 06/02/2007
* Checks whether the date Parameters are filled correctly and whether they are within the current date...before submitting the form
* @param dateSelection obj,FromDateObj,ToDateObj 
*/


function validateDateBeforeSubmit(dateSelObj,fdateObj,tdateObj)
{

	var rType;
	var rTo;
	var rFrom;
	var rTF;
	//alert("JSSSSSSSSSSSSS");
	//alert(tdateObj.value+fdateObj.value+dateSelObj.value);
	/*if(dateSelObj.value == "-1")
	{
			alert("Please select the date");
			dateSelObj.focus();
			return false;
	}*/

	if((fdateObj.value == "" || tdateObj.value == "") && dateSelObj.value == "3")
	{
		alert("Please fill the date parameters correctly");
		fdateObj.focus();
		return false;
	}
	if(tdateObj.value == "" && dateSelObj.value == "1")
	{
		alert("To date is necessary");
		tdateObj.focus();
		return false;
	}

	if(fdateObj.value == ""  && dateSelObj.value == "2")
	{
		alert("From date is necessary");
		fdateObj.focus();
		return false;
	}
	
	/****** Validation of entered date *******/
	
	if(dateSelObj.value == "1" && tdateObj.value != "")
	{
		rType=validateDate(tdateObj.value);
		if(rType==false)
		{
			alert('Please enter the valid To-date');
			tdateObj.value="";
			tdateObj.focus();
			return false;
		}		
	}	
			
	if(dateSelObj.value == "2" && fdateObj.value != "")
	{
		rType=validateDate(fdateObj.value);
		if(rType==false)
		{
			alert('Please enter the valid From-date');
			fdateObj.value="";
			fdateObj.focus();
			return false;
		}				
	}	
					
	if(dateSelObj.value == "3" && fdateObj.value != "" && tdateObj.value != "")
	{
		rFrom=validateDate(fdateObj.value);
		rTo=validateDate(tdateObj.value);
		
		//alert('I m here');
		if(rFrom==false || rTo==false)
		{
			alert('Please Enter Valid Date');
			if(rFrom==false)
			{
				fdateObj.value="";
				fdateObj.focus();
			}
				
			if(rTo==false)
			{
				tdateObj.value="";
				tdateObj.focus();
			}
			return false;
		}
		
		else
		{
			//alert('I m inside F & T date');
			rTF=checkFdateTdate(fdateObj.value,tdateObj.value);
			if(rTF==false)
			{
				alert('From date should be less than or equal to To Date');
				//fdateObj.value="";
				fdateObj.focus();
				return false;
			}			
			
		}
		
	}	
	
	return true;

}

/*
* Added By Rajalakshmi D.N. on 06/02/2007
* Checks whether the fromDate is necessary based on dateSelection criteria
* @param dateSelection obj
*/

function myFromDate(dateSelObj)
{

	if(dateSelObj.value == "-1")
	{
		alert("Please select the date");
		dateSelObj.focus();
		return false;
	}
	if(dateSelObj.value == "0")
	{
		
		alert("From Date is not necessary when Date Selection is All Dates");
		dateSelObj.focus();
		return false;
	}
	
	else if(dateSelObj.value == "4")
	{
			
			alert("From Date is not necessary when Date Selection is Current Date");
			dateSelObj.focus();
			return false;
	}
	else if(dateSelObj.value == "1")
	{
			alert("From Date is not necessary when Date Selection is Before");
			dateSelObj.focus();
			return false;
		
	}
	return true;
}

/*
* Added By Rajalakshmi D.N. on 06/02/2007
* Checks whether the toDate is necessary based on dateSelection criteria
* @param dateSelection obj
*/
function myToDate(dateSelObj)
{
	if(dateSelObj.value == "-1")
	{
		alert("Please select the date");
		dateSelObj.focus();
		return false;
	}

	if(dateSelObj.value == "0")
	{		
		alert("To Date is not necessary when Date Selection is  All Dates");
		dateSelObj.focus();
		return false;
	}
	
	else if(dateSelObj.value == "4")
	{			
		alert("To Date is not necessary when Date Selection is  Current Date");
		dateSelObj.focus();
		return false;
	}
	else if(dateSelObj.value == "2")
	{
					
		alert("To Date is not necessary when Date Selection is After");
		dateSelObj.focus();
		return false;
	}
	return true;
}


/**
* Added by Rajalakshmi D.N.
* @Params FromDate and ToDate
**/
function enable_date(obj,o1,o2)

{

	//var value = document.AggCollDateForm.dateSelection.value ;
	var value = obj.value ;

	if(value == 0)
	{
		  //o1.disabled = true;
		  //o2.disabled = true;
		  o1.value="";
		  o2.value="";
		  o1.readOnly = true;
		  o2.readOnly = true;
		  
	}
	else if(value == 1)
	{
		//o1.disabled = true;
		//o2.disabled = false;	
		o1.value="";
		o2.value="";
		o1.readOnly = true;
		o2.readOnly = false;
	}
	else if(value == 2)
	{
		//o2.disabled = true;
		//o1.disabled = false;	
		o1.value="";
		o2.value="";
		o2.readOnly = true;
		o1.readOnly = false;
		
	}
	else if(value == 4)
	{
		  //o1.disabled = true;
		  //o2.disabled = true;
		  o1.value="";
		  o2.value="";
		  o1.readOnly = true;
		  o2.readOnly = true;
	}
    	else if(value == 3)
	{
	    //o1.disabled = false;
	    //o2.disabled = false;
		o1.value="";
		o2.value="";
	    o1.readOnly = false;
	    o2.readOnly = false;
	}
}

/**
* Checks whether the From date is greater than  To date
**/
function checkFdateTdate(fromDate,toDate)
{
	//ENTERED DATE FORMAT MM/DD/YYYY	

	//alert('From Year'+fromDate.substr(6,4)+'To'+toDate.substr(6,4));
	//alert('From Month'+fromDate.substr(0,2)+'To'+toDate.substr(0,2));
	//alert('From Date'+fromDate.substr(3,2)+'To'+toDate.substr(3,2));
	
	if(fromDate.substr(6,4) > toDate.substr(6,4))
	{
		return false;
	}
	
	else if(fromDate.substr(6,4) == toDate.substr(6,4))
	{
		if(fromDate.substr(3,2) > toDate.substr(3,2))
		{
			return false;
		}
	   
		else if(fromDate.substr(3,2) == toDate.substr(3,2))
		{
			if(fromDate.substr(0,2) > toDate.substr(0,2))
			{
				return false;
			}	
	
			else 
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}
	else
	{
		return true;
	}
}
  
/*function validateDate(strValue)
{
    var today = new Date();
    var day = today.getDate();
    var month = today.getMonth();
    var year = today.getYear();
    
    //SYS DATE FORMAT DD/MM/YYYY
    //ENTERED DATE FORMAT DD/MM/YYYY
    
   //alert('ENTERED YEAR='+strValue.substr(6,4)+'YEAR='+year);
   //alert('ENTERED MONTH='+strValue.substr(0,2)+'MONTH='+month);
   //alert('ENTERED DATE='+strValue.substr(3,2)+'DAY='+day);
        	//alert();
    if(strValue!="")
    {
        //Check for year
        //If entered year is less than the current year return true
        if (strValue.substr(6,4) < year )
        {
             return true;
        }
        //If entered year is greater than the current year return false
        else if ( strValue.substr(6,4) > year)
        {
    
            return false;
        }
 
 	//If entered year is equal the current year return false
        else if( strValue.substr(6,4) == year )
        {
            if(strValue.substr(3,2)< (month+1))
            {            	
                 return true;
            }
            if(strValue.substr(3,2)> (month+1))
            {
                 return false;
            }
            if (strValue.substr(3,2) == (month+1) && strValue.substr(0,2)>day)
            {
               	return false;
            }
            
            if(document.AggCollModeForm.fdate.value.substr(3,2)==(month+1) && document.AggCollModeForm.fdate.value.substr(0,2)==day)
            {
               	return false;
            }
            
            else
            {   
                return true;
            }
        }
        else
        {
            return true;
        }
    }
    else 
    {
        return true;
    }
}*/


//Validates that the Date entered belongs to the givenfinancial year 
function validateFinancialYear(financialYear, collDateObj)
{
    //alert('Inside validateFinancialYear'+financialYear);
    var collMonth = eval(collDateObj.substr(3,2));
    var collYear = eval(collDateObj.substr(8,2));
    var finYear = eval(financialYear);
    //alert('collYear'+collYear);
   // alert('collMonth'+collMonth);
  //  alert('finYear'+finYear);
    
    //if(collMonth <= 3 && finYear == (collYear -1))
    
    if(collMonth > 3 && collYear == (finYear -1))
        return true;
    else if(collMonth <= 3 && finYear == collYear)
        return true
    else 
        return false;
    
}

function validateCollDateFinYear(collDateObj)
{	
    var today = new Date();
    var collYear=eval(collDateObj.substr(6,4));
    finYear=today.getYear();
    var FinMonth=today.getMonth();
    //alert("FinMonth"+FinMonth+"finYear"+finYear);
    collMonth=eval(collDateObj.substr(3,2));
    if(collMonth>3 && collYear == finYear )
    return true;
    else if(collMonth<=3 && FinMonth<=2 && collYear == finYear)
    return true;
    else
    return false;
   
   
       
}       

/**
* Checks whether the date entered is within the current financial Year 
**/
/*function validateCollDateFinYear(strValue)
{
//alert("Entered validateCollectionDate() fun");
    var today = new Date();
    var sys_day = today.getDate();
    var sys_month = today.getMonth();
    var sys_year = today.getYear();
    

//alert("Entered validateCollectionDate() fun    "+sys_month);
sys_month=eval(sys_month)+eval(1);

    var coll_year=strValue.substr(6,4);
    var coll_month=strValue.substr(3,2);
    var coll_day=strValue.substr(0,2);
    
   if(sys_month>=04)
   {
   	sys_yearNext=eval(sys_year)+eval(1);
   	if(coll_month>=4)
   	{
   		if(coll_year<sys_year)
   		{
   			return false;
   		}
   	}
   }
   
   else if(sys_year<4)
   {
   	if(coll_month<4)
   	{
   		
   
   
        
    //alert("SYS MONTH="+sys_month+" Coll_MONTH="+coll_month);
    if(coll_month>=4)
    {
    	//if(coll_year<sys_year)
    	if(coll_year<(eval(sys_year)-eval(1)))
    	{
    		return false;
    	}
    }
    else if(coll_month<=3)
    {
    	//if((coll_year!=sys_year) || (coll_year!=(eval(sys_year)-eval(1))))
    	//alert("SYS YEAR="+sys_year+" Coll_YEAR="+coll_year);
    	if(coll_year!=(eval(sys_year)))
    	{
    		return false;
    	}
    }
    
    else
    {
    	return true;
    }
    
}

*/

/**
* Checks whether the date entered is greater than the current date 
**/
function validateDate(strValue)
{
    /*if(!validateEnteredDate(strValue))
        return false;*/
    var today = new Date();
    var D = "" + (today.getDate());
    var day = "0" + D;
    day = day.substring(day.length-2, day.length); 
    var M = "" + (today.getMonth()+1);
    var month = "0" + M;
    month = month.substring(month.length-2, month.length); 
    var year = today.getFullYear();
    
    //SYS DATE FORMAT DD/MM/YYYY
    //ENTERED DATE FORMAT DD/MM/YYYY
    //alert("HIIIIIIIIIII");
    
   /*alert('ENTERED YEAR='+strValue.substr(6,4)+'YEAR='+year);
    alert('ENTERED MONTH='+strValue.substr(0,2)+'MONTH='+month);
    alert('ENTERED DATE='+strValue.substr(3,2)+'DAY='+day);*/

    if(strValue!="")
    {
        //Check for year
        //If entered year is less than the current year return true
        if (strValue.substr(6,4) < year )
        {
        	//alert('ENTERED YEAR='+strValue.substr(6,4));
        	//alert('YEAR='+year);
            return true;
        }
        //If entered year is greater than the current year return false
        else if ( strValue.substr(6,4) > year)
        {
            //alert('pls enter the valid date');
            return false;
        }
        //If entered year is equal the current year return false
        else if( strValue.substr(6,4) == year )
        {
            if(strValue.substr(3,2)< (month))
            {
                 return true;
            }
            if(strValue.substr(3,2)> (month))
            {
            	//alert('pls enter the valid date');
                return false;
            }
            if (strValue.substr(3,2) == (month) && strValue.substr(0,2)>day)
            {
            	//alert('pls enter the valid date');
            	return false;
            }
            
            /*if(document.AggCollDateForm.fdate.value.substr(3,2)==(month+1) && document.AggCollDateForm.fdate.value.substr(0,2)==day)
            {
            	return false;
            }*/
            
            else
            {   
                return true;
            }
        }
        else
        {
            return true;
        }
    }
    else 
    {
        return true;
    }
}

/**
* Checks whether the date entered is greater than the current date 
* (Used when validation.xml is used for validation)
**/
function validateDateJS(obj)
{

    
    /*if(!validateEnteredDate(strValue))
        return false;*/

    var today = new Date();
    var day = today.getDate();
    var month = today.getMonth();
    var year = today.getFullYear();

     //SYS DATE FORMAT DD/MM/YYYY
    //ENTERED DATE FORMAT DD/MM/YYYY
    //alert("HIIIIIIIIIII");
    
   /* alert('ENTERED YEAR='+strValue.substr(6,4)+'YEAR='+year);
    alert('ENTERED MONTH='+strValue.substr(0,2)+'MONTH='+month);
    alert('ENTERED DATE='+strValue.substr(3,2)+'DAY='+day);*/


    var strValue=obj.value;
    

    if(strValue!="")
    {
        //Check for year
        //If entered year is less than the current year return true
        if (strValue.substr(6,4) < year )
        {
        	//alert('ENTERED YEAR='+strValue.substr(6,4));
        	//alert('YEAR='+year);
            return true;
        }
        //If entered year is greater than the current year return false
        else if ( strValue.substr(6,4) > year)
        {
            alert('pls enter the valid date');
            obj.focus();
            return false;
        }
        //If entered year is equal the current year return false
        else if( strValue.substr(6,4) == year )
        {
            if(strValue.substr(3,2)< (month+1))
            {
                  return true;
            }
            if(strValue.substr(3,2)> (month+1))
            {
            	alert('pls enter the valid date');
            	obj.focus();
                return false;
            }
           
           if (strValue.substr(3,2) == (month+1) && strValue.substr(0,2)>day)
            {
            	alert('pls enter the valid date');
            	obj.focus();
            	return false;
            }
            
            /*if(document.AggCollDateForm.fdate.value.substr(3,2)==(month+1) && document.AggCollDateForm.fdate.value.substr(0,2)==day)
            {
            	return false;
            }*/
            
            else
            {   
                return true;
            }
        }
        else
        {
            return true;
        }
    }
    else 
    {
        return true;
    }
}

/**
* Check for the valid characters in the date fields 
**/
function checkDate(obj)
{

var validChars="0123456789/";
var dt=obj.value;
var len= 0;
var invalid=false;

if(obj.readOnly==false)
{
if(dt!="" && dt!=null)
{
	len= dt.length;
	for(var i=0;i<len && invalid==false;i++)
	{
		chars=dt.charAt(i);
		
		if(validChars.indexOf(chars)==-1)
		{			
			invalid=true;
		}
	}
	
	if(invalid==true)
	{
		alert("Please enter the valid characters");
		obj.value="";
		obj.focus();
	}
}
}
return;
}

/**
* Check whether the values entered in the date fields are in dd/MM/yyyy format 
* and whether the dd, MM and yyyy values are valid
**/
function validateDateFormat(obj)
{
 var dtStr=obj.value;
 
 var year;
 var day;
 var month;
 var leap=0;
 var valid=true;
 var oth_valid=true;
 var feb=false;
 var validDate=true;
 var Ret=true;
 
 if (dtStr == 'dd/mm/yyyy'){
 
 	return true;
 }
 
 
  if(obj.readOnly==false)
  {
  
    if(dtStr!="" && dtStr!=null)
    {
    	year=dtStr.substr(6,4);
    	month=dtStr.substr(3,2);
    	day=dtStr.substr(0,2);
    	if(dtStr.indexOf("/")=="2" && dtStr.lastIndexOf("/")=="5")    	
    	validDate=true;    	
    	else    	
    	validDate=false;
    	checkDate(obj);
    	
    	//alert("day="+day+" month="+month);
    	if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length!=10)
    	{
    		validDate=false;
    	}
    		
    	if(validDate==true)
    	{
    		//if(year>1900 && year<=
    		leap=year%4;
 		
 		//alert("Hi Feb LEAP="+leap);
 		 if(month=="02")
		 {
		  		//alert("dffffffffffffg");
		 		feb=true;
		 }

 		
 		if(leap==0 && month=="02")
    		{
    			//alert("111111111111111111111");
    			if(day>29)
    			{
    				valid=false;
    				feb=true;
    			}
    			//alert("Leap Year");    			
    			/*if(month=="2") // || month==02)
    			{
    				valid=false;
    				feb=true;
    			}
    			else if(month=="02")
    			{
    				if(day>29)
    				{
    					valid=false;
    					feb=true;
    				}
    			}*/
    			
    		}
    	
    		else if(month=="02" && day>28)
    		{    
    			valid=false;
    			feb=true;
    			//alert("222222222222222222");
    			/*if(month=="2")
    			{
    				valid=false;
    				feb=true;
    			}
    			else if(month=="02")
    			{
    				if(day>28)
    				{
    					valid=false;
    					feb=true;
    				}
    			}*/
    		}
    		
    		if(feb==false)
    		{    	
    			//alert("33333333333333333333333");
    			if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12")
    			{
    				if(day>31)
    				{    					
    					oth_valid=false;
    				}
    			}
    		
    			else if(month=="04" || month==06 || month=="09" || month=="11") 
    			{
    				if(day>30)
    				{    					
    					oth_valid=false;
    				}
    			}
    		
    			else
    			{
    				oth_valid=false;
    			}
    		
    		}
    	}
    }	
   
   //alert("valid="+valid+" oth_valid="+oth_valid+" validDate="+validDate);
    if(valid==false || oth_valid==false || validDate==false)
    {
    	alert("Please enter the valid date in the dd/MM/yyyy Format only");
    	obj.value="";
    	obj.focus();
    	Ret=false;
    }
    return Ret;
   }
}

/**
* Checks whether the collection date is within the selected payments'financial year
* RETURN TYPE:: true -- if within the financial year......Else returns false
**/

function validateCollectionDate(paymentYear,collectionDate)
{
	var pmntYear=paymentYear;
	var collDate=collectionDate;		
	
	var prevYear=pmntYear.substr(0,4);
	var nextYear=eval(prevYear)+eval(0001);
	
	/*alert("Payment year="+pmntYear+"CollectionYear="+collectionDate);	
	alert("Substring Year="+prevYear+"Substring Collection="+collDate.substr(6,4));	
	alert("Financial Year="+prevYear+"-"+nextYear);*/
	
	if(collDate.substr(6,4) > nextYear || collDate.substr(6,4) <prevYear)
	{
		//alert("The collection date should be between the payment year!!");
		//obj.value="";
		return false;
	}
	
	else if(collDate.substr(6,4)<=nextYear && collDate.substr(6,4)>=prevYear)
	{
		if(collDate.substr(6,4)==nextYear)
		{
			//alert("ENTERED NEXT YEAR");
			if(collDate.substr(3,2)>3)
			{
				return false;
			}
			else
			{
				if(collDate.substr(3,2)<3)				
				{
					return true;
				}
				
				if(collDate.substr(3,2)==3)				
				{
					// R if(collDate.substr(0,2)<=31)
					if(collDate.substr(0,2)<=day)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}

		if(collDate.substr(6,4)==prevYear)
		{
			//alert("ENTERED PREVIOUS YEAR");
			if(collDate.substr(3,2)<4)
			{
				return false;
			}
			else
			{
				if(collDate.substr(3,2)>4)
				{
					return true;
				}
				if(collDate.substr(3,2)==4)
				{
					if(collDate.substr(0,2)>=1)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		
	}
			
	else 
	{
		return true;
	}	 
	
}

/**
* Checks whether the collection date is greater than the 01 April of the first year of the 
* financial year selected and <=current date 
* RETURN TYPE:: true -- if within the financial year......Else returns false
**/

function validateCollectionDateCurrenDate(paymentYear,collectionDate)
{
	var pmntYear=paymentYear;
	var collDate=collectionDate;		
	
	var prevYear=pmntYear.substr(0,4);
	
	//alert("Ins Year="+paymentYear);
	if(isNaN(prevYear) && pmntYear!="Choose")
	{
		//alert("Prior");
		prevYear="2000";
		//return false;
	}
	
	var nextYear=eval(prevYear)+eval(0001);
	
	/******Newly added for the collection date<=todays date and >01April of the financial year selected ***/
	var today = new Date();
   	var day = today.getDate();
   	var month = today.getMonth();
   	var year = today.getYear();
	month=eval(month)+eval(1);
	nextYear=year;
	/***********************************************/
	/*alert("Payment year="+pmntYear+"CollectionYear="+collectionDate);	
	alert("Substring Year="+prevYear+"Substring Collection="+collDate.substr(6,4));	
	alert("Financial Year="+prevYear+"-"+nextYear);*/
	
	if(collDate.substr(6,4) > nextYear || collDate.substr(6,4) <prevYear)
	{
		//alert("The collection date should be between the payment year!!");
		//obj.value="";
		return false;
	}
	
	else if(collDate.substr(6,4)<=nextYear && collDate.substr(6,4)>=prevYear)
	{
		if(collDate.substr(6,4)==nextYear)
		{
			//alert("ENTERED NEXT YEAR");
			//R if(collDate.substr(3,2)>3)
			if(collDate.substr(3,2)>month)
			{
				return false;
			}
			else
			{
				//R if(collDate.substr(3,2)<3)
				if(collDate.substr(3,2)<month)
				{
					return true;
				}
				//R if(collDate.substr(3,2)==3)
				if(collDate.substr(3,2)==month)
				{
					// R if(collDate.substr(0,2)<=31)
					if(collDate.substr(0,2)<=day)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}

		if(collDate.substr(6,4)==prevYear)
		{
			//alert("ENTERED PREVIOUS YEAR");
			if(collDate.substr(3,2)<4)
			{
				return false;
			}
			else
			{
				if(collDate.substr(3,2)>4)
				{
					return true;
				}
				if(collDate.substr(3,2)==4)
				{
					if(collDate.substr(0,2)>=1)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		
	}
			
	else 
	{
		return true;
	}
		 
	
}

//Check whether selected date is less than current date
function valCurrentDate(date1)
{
		
	var token1 =date1.split("/");	
	var d1 = new Date(token1[2], parseInt(token1[1])-1, token1[0]);	
	var d2 = new Date();						
	if(d1 <= d2)
	 {	 
		return true;

	}
	else
	{
		 return false;

	}

}
