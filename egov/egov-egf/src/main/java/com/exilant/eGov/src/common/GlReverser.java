/*
 * Created on Jun 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.GLAccount;
import com.exilant.eGov.src.domain.ClosedPeriods;
import com.exilant.eGov.src.domain.GeneralLedger;
import com.exilant.eGov.src.domain.GeneralLedgerDetail;
import com.exilant.eGov.src.domain.VoucherDetail;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.VoucherMIS;
import com.exilant.eGov.src.domain.egfRecordStatus;
import com.exilant.eGov.src.transactions.ExilPrecision;
import com.exilant.eGov.src.transactions.PreDefinedAccCodes;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GlReverser {
Map vcHeader;
Map vcDetail;
Map gLedger;
Map gLedgerDetails;
Map egfDetail;
Map vmis;
//private String fieldID=null;
public String refcgvn=null;
private String newCessCGN=null;
private static final Logger LOGGER=Logger.getLogger(GlReverser.class);
private static TaskFailedException taskExc;
public GlReverser(){

}
public void reverse(Connection con,String cgNo,String newVcno,String effDate,String cgvNo,DataCollection dc)throws TaskFailedException,SQLException{
	//ClosedPeriods cp=new ClosedPeriods();
	if(ClosedPeriods.isClosedForPosting(effDate,con)){
		throw new TaskFailedException("Period Is Closed");
	}
	if(isReconciled(cgNo,con)){
		throw new TaskFailedException("Reconciliation Done Cant Reverse");
	}
	EGovernCommon cm=new EGovernCommon();
	if(!cm.isUniqueVN(newVcno,effDate,dc,con)){
		throw new TaskFailedException();
	}
	try{
		loadData(cgNo,con);
		String userId=dc.getValue("current_UserID");
		String newVhId=postInVoucherHeader(cgNo,newVcno,effDate,cgvNo,con,userId);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inserted to VH,VMIS,VR");
		//postInVoucherDetail(newVhId,con);
		postInVoucherDetail(newVhId,con);
		postInGeneralLedger(newVhId,con);
        if(LOGGER.isDebugEnabled())     LOGGER.debug("inserted into vD newVhId="+newVhId);
	}catch(Exception e){
		LOGGER.error("Exception in Reverse():"+e.toString());
		throw taskExc;
	}

}

// Reversing Voucher without dc 
public void reverse(Connection con,String cgNo,String newVcno,String effDate,String cgvNo, int userId)throws TaskFailedException,SQLException
{
	if(ClosedPeriods.isClosedForPosting(effDate,con))
	{
		throw new TaskFailedException("Period Is Closed");
	}
	if(isReconciled(cgNo,con))
	{
		throw new TaskFailedException("Reconciliation Done Cant Reverse");
	}
	EGovernCommon cm=new EGovernCommon();
	if(!cm.isUniqueVN(newVcno,effDate,con)){
		throw new TaskFailedException("Error: Duplicate Voucher Number");
	}
	try
	{
		loadData(cgNo,con);
		String newVhId=postInVoucherHeader(cgNo,newVcno,effDate,cgvNo,con,Integer.toString(userId));
		postInVoucherDetail(newVhId,con);
        if(LOGGER.isDebugEnabled())     LOGGER.debug("inserted into vD newVhId="+newVhId);
	}catch(Exception e)
	{
		LOGGER.error("Exception in Reverse():"+e.toString());
		throw taskExc;
	}
}

private void loadData(String cgNo,Connection con) throws TaskFailedException{
	String vhSql="select ID as \"id\" ,CGN as \"cgn\",CGDATE as \"cgDate\",NAME AS \"name\"," +
			     "TYPE as \"type\",DESCRIPTION as \"description\",EFFECTIVEDATE as \"effectiveDate\", " +
			     "VOUCHERNUMBER as \"voucherNumber\",VOUCHERDATE as \"voucherDate\", " +
			     "DEPARTMENTID as \"departmentId\",FUNDID as \"fundId\" ,FUNDSOURCEID as \"fundSourceId\",FISCALPERIODID as \"fiscalPeriodId\" ," +
			     "STATUS as \"status\",ORIGINALVCID as \"originalVcId\", cgvn as \"cgvn\" from VOUCHERHEADER where CGN='"+cgNo+"'";
	String vdSql="select ID as \"id\" ,LINEID as \"lineId\",VOUCHERHEADERID as \"voucherHeaderId\",GLCODE as \"glCode\"," +
				 "ACCOUNTNAME as \"accountName\",DEBITAMOUNT as \"debitAmount\",CREDITAMOUNT as \"creditAmount\"," +
				 "NARRATION as \"narration\" from VOUCHERDETAIL where VOUCHERHEADERID=";
	String glSql="select ID as \"id\" , VOUCHERLINEID as \"voucherLineId\", EFFECTIVEDATE as  \"effectiveDate\" ,GLCODEID as \"glCodeId\", GLCODE as \"glCode\"," +
				 "DEBITAMOUNT as \"debitAmount\",CREDITAMOUNT as \"creditAmount\", DESCRIPTION as \"description\", VOUCHERHEADERID as \"voucherHeaderId\",FUNCTIONID as \"functionId\" from GENERALLEDGER where VOUCHERHEADERID=";
	String egfSql="select ID as \"id\" , RECORD_TYPE as \"record_Type\", STATUS as  \"status\","+
	 			  "UPDATEDTIME as \"updateTime\",USERID as \"userId\", VOUCHERHEADERID as \"voucherHeaderId\" from EGF_RECORD_STATUS where VOUCHERHEADERID=";
	String vmisSql="Select ID as \"id\" ,voucherheaderid as \"voucherheaderid\", divisionid as \"divisionId\", departmentid as \"departmentId\", segmentid as \"segmentId\", sub_segmentid as \"subSegmentId\", fundsourceid as \"fundsourceid\", schemeid as \"scheme\", subschemeid as \"subscheme\",functionaryId as \"functionary\"  from vouchermis where voucherheaderid=";
	
	String extraSQL=" order by GLCODE ";
	DataExtractor de=DataExtractor.getExtractor();
	//load voucher header record for the cgn given
	vcHeader=de.extractIntoMap(vhSql,con,"cgn",VoucherHeader.class);
	if(vcHeader==null || vcHeader.size()==0){
		throw new TaskFailedException("Cant Reverse : Record Not Found For CGN Provided"+vhSql);
	}
	VoucherHeader vhObj=(VoucherHeader)vcHeader.get(cgNo);
	String vcHID=String.valueOf(vhObj.getId());
	//load vouchermis
	if(LOGGER.isDebugEnabled())     LOGGER.debug("Calling extraxt map for VoucherMIS  :" +vmisSql+vcHID);
	vmis=de.extractIntoMap(vmisSql+vcHID,con,"voucherheaderid",VoucherMIS.class);
	if(LOGGER.isDebugEnabled())     LOGGER.debug("Object for voucherMIS"+vmis);
			
	//load voucher detail records for that header
	vcDetail=de.extractIntoMap(vdSql+vcHID+extraSQL,con,"id",VoucherDetail.class);
	//load General Ledger
	gLedger=de.extractIntoMap(glSql+vcHID+extraSQL,con,"voucherLineId",GeneralLedger.class);
	egfDetail=de.extractIntoMap(egfSql+vcHID,con,"id",egfRecordStatus.class);
}
private String postInVoucherHeader(String cgNo,String newVcNo,String effDate,String cgvNo,Connection con,String uId) throws Exception{
	String today;
	//String oldVdId="";
	VoucherHeader vhObj=(VoucherHeader)vcHeader.get(cgNo);
	egfRecordStatus egfstatus = new egfRecordStatus();
	EGovernCommon egCom=new EGovernCommon();
	if(vhObj.getStatus().equals("1")){
		throw new TaskFailedException("Already Reversed");
	}else if(vhObj.getStatus().equals("2")){
		throw new TaskFailedException("It is a Reversed Entry");
	}
    if(LOGGER.isDebugEnabled())     LOGGER.debug("UPDATING VH");
	//update the old one with status as	 reversed
	vhObj.setId(String.valueOf(vhObj.getId()));
	//oldVdId=String.valueOf(vhObj.getId());
	vhObj.setStatus("1");
	vhObj.update(con);

	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	EGovernCommon cm=new EGovernCommon();
	today=cm.getCurrentDateTime(con);
	String dateformat = formatter.format(sdf.parse(today));
	if(LOGGER.isDebugEnabled())     LOGGER.debug("dateformat is  "+ dateformat);
	
	//insert the old one with status as reversed for egf_record_status
	egfstatus.setVoucherheaderId(String.valueOf(vhObj.getId()));
	if(LOGGER.isDebugEnabled())     LOGGER.debug("vhobj type "+ vhObj.getType());
	egfstatus.setStatus("2");
	egfstatus.setRecord_Type(vhObj.getName()+" "+vhObj.getType());
	egfstatus.setEffectiveDate(dateformat);
	egfstatus.setUserId(uId);
	//egfstatus.update(con);
	egfstatus.insert(con);
	if(LOGGER.isDebugEnabled())     LOGGER.debug("inserted in  EGF as status 2");
	
	//insert the old one with status as new
	String newCgNo=cgNo.substring(0,3)+egCom.getCGNumber();
    newCessCGN=newCgNo;
	vhObj.setCgn(newCgNo);
	vhObj.setVoucherNumber(newVcNo);
	vhObj.setVoucherDate(effDate);
	if(vhObj.getFiscalPeriodId()==null || vhObj.getFiscalPeriodId().length()==0){
		vhObj.setFiscalPeriodId("null");
	}
	String oldVHID=String.valueOf(vhObj.getId());
	if(LOGGER.isDebugEnabled())     LOGGER.debug("vhObj.getVoucherDate()========="+vhObj.getVoucherDate());
	vhObj.setFiscalPeriodId(cm.getFiscalPeriod(vhObj.getVoucherDate(),con));
	vhObj.setStatus("2");
	vhObj.setOriginalVcId(String.valueOf(vhObj.getId()));
	vhObj.setCgvn(cgvNo);
    if((refcgvn!=null && !refcgvn.equalsIgnoreCase(""))&& !refcgvn.equalsIgnoreCase("CESS"))
    {
        if(LOGGER.isDebugEnabled())     LOGGER.debug("Setting refcgvn");
        vhObj.setRefCgn(refcgvn);
    }
    if(LOGGER.isDebugEnabled())     LOGGER.debug("INSERTING VOUCHERHEADER");
	vhObj.setCreatedby(uId);
	vhObj.insert(con);

	//	insert the old one with status as cancelled for egf_record_status

	egfRecordStatus egfstatus1 = new egfRecordStatus();
	egfstatus1.setVoucherheaderId(String.valueOf(vhObj.getId()));
	egfstatus1.setRecord_Type(vhObj.getName()+" "+vhObj.getType());
	egfstatus1.setStatus("3");
	egfstatus1.setEffectiveDate(formatter.format(sdf.parse(today)));
	egfstatus1.setUserId(uId);
	if(LOGGER.isDebugEnabled())     LOGGER.debug("INSERTING VOUCHER STATUS");
	egfstatus1.insert(con);

	//Post to VoucherMIS table
	//if(fieldID != null)
	if(vmis.get(oldVHID) != null)
	{		
		VoucherMIS vmObj=(VoucherMIS)vmis.get(oldVHID);
		//VoucherMIS vmObj = new VoucherMIS();
		vmObj.setDivisionId(vmObj.getDivisionId());
		vmObj.setVoucherheaderid(String.valueOf(vhObj.getId()));
		vmObj.setDepartmentId(vmObj.getDepartmentId());
		vmObj.setSegmentId(vmObj.getSegmentId());
		vmObj.setSubSegmentId(vmObj.getSubSegmentId());
		vmObj.setFundsourceid(vmObj.getFundsourceid());
		vmObj.setScheme(vmObj.getScheme());
		vmObj.setSubscheme(vmObj.getSubscheme());
		vmObj.setCreateTimeStamp(formatter.format(sdf.parse(today)));
		vmObj.setFunctionary(vmObj.getFunctionary());
		if(LOGGER.isDebugEnabled())     LOGGER.debug("INSERTING VOUCHERMIS");
		vmObj.insert(con);
	}
	
	return String.valueOf(vhObj.getId());
}
private void postInVoucherDetail(String newVhId, Connection con) throws TaskFailedException,SQLException{
	Iterator it=vcDetail.keySet().iterator();
	String oldVdId="";
	while(it.hasNext()){
		VoucherDetail vdObj=(VoucherDetail)vcDetail.get(it.next());
		if(Double.parseDouble(vdObj.getDebitAmount())>0){
			vdObj.setCreditAmount(vdObj.getDebitAmount());
			vdObj.setDebitAmount("0");
		}else if(Double.parseDouble(vdObj.getCreditAmount())>0){
			vdObj.setDebitAmount(vdObj.getCreditAmount());
			vdObj.setCreditAmount("0");
		}
		vdObj.setVoucherHeaderID(newVhId);
		oldVdId=String.valueOf(vdObj.getId());
		vdObj.insert(con);
		//postInGeneralLedger(String.valueOf(vdObj.getId()),oldVdId,newVhId,con);
	}
}
private void postInGeneralLedger(String newVdId,String oldVdId,String newVhId,Connection con)throws TaskFailedException{
	try{
		String oldGlId="";
		//String glDetAmt="0";
		GeneralLedger glObj=(GeneralLedger)gLedger.get(oldVdId);
		if(Double.parseDouble(glObj.getDebitAmount())>0){
			glObj.setCreditAmount(glObj.getDebitAmount());
			glObj.setDebitAmount("0");
			//glDetAmt=glObj.getcreditAmount();
		}else if(Double.parseDouble(glObj.getCreditAmount())>0){	
			glObj.setDebitAmount(glObj.getCreditAmount());
			glObj.setCreditAmount("0");
			//glDetAmt=glObj.getdebitAmount();
		}
		glObj.setVoucherLineId(newVdId);
		glObj.setVoucherHeaderId(newVhId);
		oldGlId=String.valueOf(glObj.getId());
		 if(!(glObj.getFunctionId()==null || glObj.getFunctionId().equals(""))){
		 	glObj.setFunctionId(glObj.getFunctionId());
         }
        else
        {
        	glObj.setFunctionId(null);
        }
		glObj.insert(con);
		postInGLDetail(String.valueOf(glObj.getId()),oldGlId,con);
	}catch(Exception e){
		LOGGER.error("Exp here"+e.toString());
		throw taskExc;
	}
}
private void postInGeneralLedger(String newVhId,Connection con)throws TaskFailedException{
	try{
		Iterator it=gLedger.keySet().iterator();
		int voucherlineid=1;
		String oldGlId="";
		//String glDetAmt="0";
		while(it.hasNext()){
			GeneralLedger glObj =(GeneralLedger)gLedger.get(it.next());
		if(Double.parseDouble(glObj.getDebitAmount())>0){
			glObj.setCreditAmount(glObj.getDebitAmount());
			glObj.setDebitAmount("0");
			//glDetAmt=glObj.getcreditAmount();
		}else if(Double.parseDouble(glObj.getCreditAmount())>0){	
			glObj.setDebitAmount(glObj.getCreditAmount());
			glObj.setCreditAmount("0");
			//glDetAmt=glObj.getdebitAmount();
		}
		glObj.setVoucherLineId(String.valueOf(voucherlineid++));
		glObj.setVoucherHeaderId(newVhId);
		oldGlId=String.valueOf(glObj.getId());
		 if(!(glObj.getFunctionId()==null || glObj.getFunctionId().equals(""))){
		 	glObj.setFunctionId(glObj.getFunctionId());
         }
        else
        {
        	glObj.setFunctionId(null);
        }
		glObj.insert(con);
		postInGLDetail(String.valueOf(glObj.getId()),oldGlId,con);
		}
		}catch(Exception e){
		LOGGER.error("Exp here"+e.toString());
		throw taskExc;
		}
}

private void postInGLDetail(String glID,String oldGlId,Connection con) throws TaskFailedException,SQLException{
	String glDSql="select ID as \"id\",GENERALLEDGERID as \"glId\" ,DETAILKEYID as \"detailKeyId\",  DETAILTYPEID as \"detailTypeId\" ,AMOUNT as \"detailAmt\" from GENERALLEDGERDETAIL where GENERALLEDGERID="+oldGlId;
	DataExtractor de=DataExtractor.getExtractor();
	gLedgerDetails=de.extractIntoMap(glDSql,con,"id",GeneralLedgerDetail.class);
	Iterator it=gLedgerDetails.keySet().iterator();
	while(it.hasNext()){
		GeneralLedgerDetail glDObj=(GeneralLedgerDetail)gLedgerDetails.get(it.next());
		glDObj.setGLId(glID);
		//glDObj.setDetailAmt(glDetAmt);// by mani  added in the glDSql query for new subledger screen with multiple entities 
		glDObj.insert(con);
	}
}
public void reverseSubLedger(Connection con,String cgNo,String newVcno,String effDate,String cgvNo,DataCollection dc)throws TaskFailedException,SQLException{
 	//ClosedPeriods cp=new ClosedPeriods();
	if(ClosedPeriods.isClosedForPosting(effDate,con)){
		throw new TaskFailedException("Period Is Closed");
	}
	if(isReconciled(cgNo,con)){
		throw new TaskFailedException("Reconciliation Done Cant Reverse");
	}
	EGovernCommon cm=new EGovernCommon();
	if(!cm.isUniqueVN(newVcno,effDate,dc,con)){
		throw new TaskFailedException();
	}
	try{
		//ClosedPeriods cp=new ClosedPeriods();
		if(ClosedPeriods.isClosedForPosting(effDate,con)){
			throw new TaskFailedException("Period Is Closed");
		}

		if(isReconciled(cgNo,con)){
			throw new TaskFailedException("Reconciliation Done Cant Reverse");
		}

		if(!cm.isUniqueVN(newVcno,effDate,dc,con)){
			throw new TaskFailedException();
		}
		loadData(cgNo,con);
		String userId=dc.getValue("current_UserID");
		String newVhId=postInVoucherHeader(cgNo,newVcno,effDate,cgvNo,con,userId);
		String reverseType=dc.getValue("reverseType");
		if(reverseType!=null && (reverseType.equals("cash") || reverseType.length()==0)){
			postInVoucherDetail(newVhId,con);
		}else{
			postInVoucherDetail(dc.getValue("diffDebit"),newVhId,con);
		}

	}catch(Exception e){
		LOGGER.error("Exp reverse subledger:"+e.toString());
		throw taskExc;
	}

}
private void postInVoucherDetail(String diffDebitAmount,String newVhId, Connection con) throws Exception{
	Iterator it=vcDetail.keySet().iterator();
	String oldVdId="";
//	VoucherDetail diffVoucherDetail=new VoucherDetail();
	//CodeValidator cv=CodeValidator.getInstance();
	PreDefinedAccCodes pAccCodes=new PreDefinedAccCodes();
	GLAccount glAcc=(GLAccount)ChartOfAccounts.getGlAccountCodes().get(pAccCodes.getBankChargeCode());
	double diffDbAmount=ExilPrecision.convertToDouble(diffDebitAmount,2);
	while(it.hasNext()){
		Object tempObj=it.next();
		VoucherDetail vdObj=(VoucherDetail)vcDetail.get(tempObj);
		vdObj.setVoucherHeaderID(newVhId);
		if(Double.parseDouble(vdObj.getDebitAmount())>0){
			vdObj.setCreditAmount(vdObj.getDebitAmount());
			vdObj.setDebitAmount("0");
			oldVdId=String.valueOf(vdObj.getId());
			vdObj.insert(con);
			postInGeneralLedger(String.valueOf(vdObj.getId()),oldVdId,newVhId,con);
		}else if(Double.parseDouble(vdObj.getCreditAmount())>0){
			VoucherDetail diffVoucherDetail=vdObj;
			if(diffDbAmount>0){
				diffVoucherDetail.setGLCode(pAccCodes.getBankChargeCode());
				diffVoucherDetail.setAccountName(glAcc.getName());
				diffVoucherDetail.setDebitAmount(ExilPrecision.convertToString(diffDebitAmount,2));
				diffVoucherDetail.setCreditAmount("0");
				diffVoucherDetail.setVoucherHeaderID(newVhId);
				oldVdId=String.valueOf(diffVoucherDetail.getId());
				diffVoucherDetail.insert(con);
				postInGeneralLedger(String.valueOf(diffVoucherDetail.getId()),oldVdId,newVhId,diffDbAmount,0,con);
			}
			//reassign with original object as they are replaced
			vdObj=(VoucherDetail)vcDetail.get(tempObj);
			vdObj.setDebitAmount(ExilPrecision.convertToString(ExilPrecision.convertToDouble(vdObj.getCreditAmount(),2)
					 -diffDbAmount,2));
			vdObj.setCreditAmount("0");
			vdObj.setVoucherHeaderID(newVhId);
			oldVdId=String.valueOf(vdObj.getId());
			vdObj.insert(con);
			postInGeneralLedger(String.valueOf(vdObj.getId()),oldVdId,newVhId,diffDbAmount,1,con);
		}
	}
}
private void postInGeneralLedger(String newVdId,String oldVdId,String newVhId,double diffDbAmount,int usage,Connection con)throws TaskFailedException,SQLException{
	String oldGlId="";//,diffGlID="";
	PreDefinedAccCodes pAccCodes=new PreDefinedAccCodes();
	GeneralLedger glObj=(GeneralLedger)gLedger.get(oldVdId);
	glObj.setVoucherLineId(newVdId);
	glObj.setVoucherHeaderId(newVhId);
	oldGlId=String.valueOf(glObj.getId());
	//String glDetAmt="0";
	if(Double.parseDouble(glObj.getDebitAmount())>0){
		throw new TaskFailedException("Error Record Mismatch");
	}else if(Double.parseDouble(glObj.getCreditAmount())>0 && usage==1){
		glObj.setDebitAmount(ExilPrecision.convertToString(ExilPrecision.convertToDouble(glObj.getCreditAmount(),2)
				 -diffDbAmount,2));
		glObj.setCreditAmount("0");
		//glDetAmt=glObj.getdebitAmount();
	}else if(Double.parseDouble(glObj.getCreditAmount())>0 && usage==0){
		//it has all the chartofaccounts loaded as hashmap so get glid by giving glcode
		//CodeValidator cv=CodeValidator.getInstance();
		GLAccount glAcc=(GLAccount)ChartOfAccounts.getGlAccountCodes().get(pAccCodes.getBankChargeCode());
		glObj.setGlCode(pAccCodes.getBankChargeCode());
		glObj.setGlCodeId(String.valueOf(glAcc.getId()));
		glObj.setDebitAmount(String.valueOf(diffDbAmount));
		glObj.setCreditAmount("0");
		//glDetAmt=glObj.getdebitAmount();
	}
	if(!(glObj.getFunctionId()==null || glObj.getFunctionId().equals(""))){
	 	glObj.setFunctionId(glObj.getFunctionId());
   }
    else
    {
    	glObj.setFunctionId(null);
    }
	glObj.insert(con);
	postInGLDetail(String.valueOf(glObj.getId()),oldGlId,con);
}
private boolean isReconciled(String cgNo,Connection con) throws SQLException{
	boolean valid=false;
	String sql="select isreconciled from BANKRECONCILIATION bc ,voucherheader vh where bc.voucherheaderid=vh.id and vh.cgn= ?";
	PreparedStatement pst=null;
	ResultSet rset=null;
	try{
		pst=con.prepareStatement(sql);
		pst.setString(1, cgNo);
		rset=pst.executeQuery();
	if(rset.next() && rset.getString(1).equals("1")){

			valid=true;
		}
	}catch(Exception e){
		throw new SQLException();
	}
	finally{
		try{
			rset.close();
			pst.close();
		}catch(Exception e){LOGGER.error("Inside finally block");}
	}
	return valid;
}
public String reversePT(Connection con,String cgNo,String newVcno,String effDate,String cgvNo,DataCollection dc,String refcgvn1)throws TaskFailedException,SQLException
{
	if(LOGGER.isDebugEnabled())     LOGGER.debug("coming..");
    refcgvn=refcgvn1;
    reverse(con,cgNo,newVcno,effDate,cgvNo,dc);
    return newCessCGN;
}
}
