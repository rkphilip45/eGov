
/** This function is the reimplementation of Calculator.
*** This takes the collection made and splits up the collection into arrears, penalty and current year
*** PT and calculates the balance. 
**/

var doCalc = true;
function newCalculator()
{
   if(doCalc == true)
   {	
      doCalc = false;
     
     if(document.payment.totalcollection.value != "" && document.payment.totalcollection.value != 0.00)
   	 {
			
		 var collection_curr = eval(document.payment.totalcollection.value);
		 // Declare vars to hold Dmd and Collection values
		 // Demand vars 1st column

		 var arrearsTotDmd	=	eval(document.payment.arrearstot.value); //not hidden chk this
		 var arPnltyDmd 	= 	eval(document.payment.arrearspenaltydmd.value);
		 var curPnltydmd 	= 	eval(document.payment.currentpenaltydmd.value);
		 var curDmdPt 		= 	eval(document.payment.demandtot.value);
		 var totalDmdAmt 	=   eval(document.payment.totaldemandamt.value);

		// Past Coll vars 3rd Column
		 var arrCollection  =  eval(document.payment.pastarrearscollection.value);
		 var arrPnltyColn	=  eval(document.payment.pastarrearspenaltycollection.value);	
		 var curPnltyColn	=  eval(document.payment.pastcurrentpenaltycollection.value);
		 var curPtColn		=  eval(document.payment.pastCurrentPtcollection.value);
		 
		 //Balance vars 4th Column
		 
		 var arrBalance		=	eval(document.payment.arrearsbalancepayment.value);
		 var arrPnltyBal	=	eval(document.payment.arrearspenaltypaybalance.value);
		 var curPnltyBal	=	eval(document.payment.currentpenaltypaybalance.value);
		 var cutPtBal		=	eval(document.payment.currentbalance.value);
		 var totBal			=	eval(document.payment.totalbalanceamt.value);
		 
		 var   m_totalamtpaid= 0.00;
		 
		 //Deduct Arrearsamt from Collecton made if it is Gt Arrearstotal payable,ie if Arrears Total > collection + Amt Already paid for Arrears
		 //here mainly 2 cases arise 1.where totBal is +ve val 2. where totBal is -ve val. these r handled seperately.
		 
		 if(totBal > 0)
		 {
			 if((collection_curr > arrearsTotDmd - arrCollection) && (arrearsTotDmd - arrCollection >= 0))
			 {
				document.payment.arrearscollection.value= roundoff(arrearsTotDmd - arrCollection);					
				m_totalamtpaid +=  arrearsTotDmd;
				document.payment.arrearsbalance.value ="0.00";
				collection_curr = collection_curr - (arrearsTotDmd - arrCollection);
				//alert('here in if 1'+m_totalamtpaid);
				
			 }
			 else if((collection_curr < arrearsTotDmd - arrCollection) && (arrearsTotDmd - arrCollection > 0))
			 {		
				document.payment.arrearscollection.value= roundoff(collection_curr);
				m_totalamtpaid +=  collection_curr + arrCollection;//add past 
				document.payment.arrearsbalance.value = roundoff(arrearsTotDmd-(collection_curr + arrCollection));
				collection_curr = 0;
				//alert('here in else1 '+m_totalamtpaid);
			 }
			 //here v r handling the case where arrearsTotDmd is wrong. calculation in the whole function depends on Dmds but 
			 //though when arrearsTotDmd is wrong amt shuold be collected towards arrears.
			 
			 if(arrCollection > arrearsTotDmd && collection_curr >= (arrBalance))
			 {
				document.payment.arrearscollection.value= roundoff(arrBalance);
				m_totalamtpaid +=  arrCollection + arrBalance;//past arrears col acts as arrearsdmd here 
				document.payment.arrearsbalance.value = "0.00";
				collection_curr = collection_curr - (arrBalance);
				//alert('here in if 2'+m_totalamtpaid);
			 }
			 else if(arrCollection > arrearsTotDmd && collection_curr < (arrBalance))
			 {
				document.payment.arrearscollection.value= roundoff(collection_curr);
				m_totalamtpaid +=  collection_curr + arrCollection;// past arrears col acts as arrearsdmd here
				document.payment.arrearsbalance.value = roundoff((arrBalance - collection_curr));
				collection_curr = 0;
			 }

			 //Now deduct the Arrears Penalty from the Collection
			 if((collection_curr > arPnltyDmd - arrPnltyColn) && (collection_curr > 0))
			 {
				document.payment.arrearspenaltycollection.value = roundoff(arPnltyDmd - arrPnltyColn);
				m_totalamtpaid +=arPnltyDmd;
				document.payment.arrearspenaltybalance.value ="0.00";
				collection_curr = collection_curr - (arPnltyDmd - arrPnltyColn);
				//alert('here in if 3'+m_totalamtpaid);
				
			 }
			 else
			 {
				document.payment.arrearspenaltycollection.value= roundoff(collection_curr);
				m_totalamtpaid +=collection_curr +arrPnltyColn;
				document.payment.arrearspenaltybalance.value= roundoff(arPnltyDmd-(collection_curr + arrPnltyColn));
				collection_curr = 0;
			 }

			 //Now deduct the Current Penalty from the Collection

			 if((collection_curr > curPnltydmd - curPnltyColn) && (collection_curr>0))
			 {
				document.payment.currentpenaltycollection.value =roundoff(curPnltydmd - curPnltyColn);
				m_totalamtpaid += curPnltydmd;
				document.payment.currentpenaltybalance.value ="0.00";
				collection_curr	=	collection_curr - (curPnltydmd - curPnltyColn);
				//alert('here in if 4'+m_totalamtpaid);
			 }
			 else
			 {
				document.payment.currentpenaltycollection.value =roundoff(collection_curr);
				m_totalamtpaid += collection_curr + curPnltyColn ;
				document.payment.currentpenaltybalance.value = roundoff(curPnltydmd-(collection_curr + curPnltyColn));
				collection_curr	= 0;
			 }

			 //Now deduct the Current PT/currentpaymentcollection
			 if((collection_curr > curDmdPt - curPtColn) && (collection_curr>0))
			 {
				document.payment.currentpaymentcollection.value =roundoff(curDmdPt - curPtColn);
				m_totalamtpaid += curDmdPt;
				document.payment.currentpaymentbalance.value ="0.00";
				collection_curr	=	collection_curr - (curDmdPt  - curPtColn);

			 }
			 else
			 {
				document.payment.currentpaymentcollection.value =roundoff(collection_curr);
				m_totalamtpaid += collection_curr + curPtColn;//earlier function not used to deduct curPtColn here
				document.payment.currentpaymentbalance.value = roundoff(curDmdPt-(collection_curr + curPtColn)) ;
				collection_curr	= 0;
				//alert('here in else 5'+m_totalamtpaid);
			 }

			 //Fianally lets calc the Totbalance bal amt is got from adding up all bals
			
			 if(arrCollection > arrearsTotDmd)//in case if arrearsDmd/arrearesTot is wrong in DB  bal is calculated here
			 {
			 	  document.payment.totalbalance.value =  roundoff((totalDmdAmt-arrearsTotDmd+arrCollection+arrBalance ) - m_totalamtpaid - collection_curr);
				
			 }
			 else //normal case
			 {
			 	 //alert('totalDmdAmt'+roundoff(totalDmdAmt));
			 	// alert('m_totalamtpaid '+roundoff(m_totalamtpaid));
			 	// alert('collection_curr '+roundoff(collection_curr));
			 	// alert('Balance '+roundoff(totalDmdAmt - m_totalamtpaid - collection_curr));
			 	 document.payment.totalbalance.value =  roundoff(totalDmdAmt - m_totalamtpaid - collection_curr);
			 	 
		         }
		         
			 
			 
		 }
		 else if(totBal <= 0)
		 {
			
			document.payment.totalbalance.value =  roundoff(totBal - collection_curr);
			
		 }
		 
		 if(document.payment.totalbalance.value < 0)
		 {
			//document.payment.totalbalance.value =  roundoff(totBal - collection_curr);
			alert('Balance should not be Negative');
			document.payment.totalbalance.value = "";
		 }
	}
  }	
}

function paymentload()
{
	document.payment.ddno.disabled = true;
	document.payment.dddate.disabled = true;
	document.payment.chequedate.disabled = true;
	document.payment.chequeno.disabled = true;
	document.payment.banksname.disabled = true;
	document.payment.cardNo.disabled = true;
	document.payment.authorizationCode.disabled = true;
	document.payment.cardType.disabled = true;
	document.payment.modeOfPayment[0].checked = false;
	document.payment.modeOfPayment[1].checked = false;
	document.payment.modeOfPayment[2].checked = false;
	document.payment.modeOfPayment[3].checked = false;
	document.payment.modeOfPayment[4].checked = false;
	document.payment.duration[0].checked = false;
	document.payment.duration[1].checked = false;
	document.payment.duration[2].checked = false;
	document.payment.duration[3].checked = false;
	document.payment.arrearsduration[0].checked = false;
	document.payment.arrearsduration[1].checked = false;
	
	document.payment.ddno.value="";
	document.payment.dddate.value="";
	document.payment.chequedate.value="";
	document.payment.chequeno.value="";
	document.payment.banksname.value="";
	
	document.payment.cardNo.value = "";
	document.payment.authorizationCode.value = "";
	
	
	if(document.payment.callcalculator.value == "true")
	 	newCalculator();
	
}

/////////////////////////////////////////////////////////////////
///For Past Year Payment
function paymentloadForPastYrPayment()
{
	document.payment.ddno.disabled = true;
	document.payment.dddate.disabled = true;
	document.payment.chequedate.disabled = true;
	document.payment.chequeno.disabled = true;
	document.payment.banksname.disabled = true;
	document.payment.modeOfPayment[0].checked = false;
	document.payment.modeOfPayment[1].checked = false;
	document.payment.modeOfPayment[2].checked = false;
	document.payment.duration[0].checked = false;
	document.payment.duration[1].checked = false;
	document.payment.duration[2].checked = false;
	document.payment.duration[3].checked = false;
	document.payment.arrearsduration[0].checked = false;
	document.payment.arrearsduration[1].checked = false;
	
	document.payment.ddno.value="";
	document.payment.dddate.value="";
	document.payment.chequedate.value="";
	document.payment.chequeno.value="";
	document.payment.banksname.value="";
	
	if(document.payment.callcalculator.value == "true")
	 	newCalculator();
	
}
function serChargeCalc()
{
	//alert('Hii');
	
	if(document.payment.totalcollection.value != "" && document.payment.totalcollection.value != 0.00)
	{
		var totColAmt = eval(document.payment.totalcollection.value);
		//alert('hi'+totColAmt);
		var serCharge = roundoff(eval(totColAmt*0.0175));
		//alert('hi'+serCharge);
		var grandTot = roundoff(totColAmt+eval(serCharge));
		//alert('hi'+grandTot);
		if(confirm('For Debit/Credit Card Payment BankServiceCharge Applicable is : Rs.'+serCharge+'                                                                                                                                     Total Amount Payable is : Rs. '+grandTot+""))
		{
			 return true;
		}
		else
		{
			document.payment.modeOfPayment[3].checked = false;
			document.payment.modeOfPayment[4].checked = false;
			 return false;	
		}
	
	}
		

}