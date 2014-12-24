package org.egov.works.web.actions.contractorBill;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVException;
import org.egov.lib.admbndry.Boundary;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;


@Result(name=BaseFormAction.SUCCESS,type="stream",location="egBillRegisterPDF", params={"inputName","egBillRegisterPDF","contentType","application/pdf","contentDisposition","no-cache"})
@ParentPackage("egov")
public class ContractorBillPDFAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(ContractorBillAction.class);
	private Long egbillRegisterId;
	private InputStream egBillRegisterPDF;
	private EmployeeService employeeService;
	private ContractorBillService contractorBillService;
	private CommonsService commonsService;
	private WorksService worksService;
	private Boundary boundary =null;
		
	public String execute(){
		if(egbillRegisterId!=null){		
			ContractorBillRegister egBillregister=getEgBillregister();	
			MBHeader mBHeader=new MBHeader();
			MBForCancelledBill mbCancelBillObj=new MBForCancelledBill(); 
			if(egBillregister.getBillstatus().equals("CANCELLED")){
				mbCancelBillObj=(MBForCancelledBill)persistenceService.find("from MBForCancelledBill mbHeader where mbHeader.egBillregister.id = ?",egbillRegisterId);
			    mBHeader=mbCancelBillObj.getMbHeader();
			 }
			else{
			mBHeader=(MBHeader)getPersistenceService().find("select mbHeader from MBHeader mbHeader left join mbHeader.mbBills mbBills where mbBills.egBillregister.id = ?",egbillRegisterId);
			}
			AbstractEstimate estimate = mBHeader.getWorkOrderEstimate().getEstimate();
			boundary = getTopLevelBoundary(estimate.getWard());
			Map<String,String> pdfLabel=getPdfReportLabel();
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024*100);
			ContractorBillPDFGenerator pdfGenerator =new ContractorBillPDFGenerator(egBillregister,mBHeader,out,pdfLabel,contractorBillService);
			pdfGenerator.setPersistenceService(getPersistenceService());			
			pdfGenerator.setEmployeeService(employeeService);			
			pdfGenerator.setWorksService(worksService);
			pdfGenerator.setCommonsService(commonsService);			
			try {
				pdfGenerator.generatePDF();
			} catch (EGOVException e) {
				// TODO Auto-generated catch block
				logger.debug("exception "+e);
			}
			 catch (IOException ie) {
					// TODO Auto-generated catch block
					logger.debug("exception "+ie);
				}
			egBillRegisterPDF=new ByteArrayInputStream(out.toByteArray());
		}
		return SUCCESS;
	}
		
	private ContractorBillRegister getEgBillregister(){
		return (ContractorBillRegister)getPersistenceService().find("from ContractorBillRegister egBillregister where id = ?",egbillRegisterId);
	}
	
	protected Boundary getTopLevelBoundary(Boundary boundary) {
		Boundary b = boundary;
		while(b!=null && b.getParent()!=null){
			b=b.getParent();
		}
		return b;
	}	
	
	public void setEgbillRegisterId(Long egbillRegisterId) {		
		this.egbillRegisterId = egbillRegisterId;
	}
	
	public InputStream getEgBillRegisterPDF(){
		return egBillRegisterPDF;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setContractorBillService(ContractorBillService contractorBillService) {
		this.contractorBillService = contractorBillService;
	}
	
	public Map<String,String> getPdfReportLabel(){
		Map<String,String> pdfLabel=new HashMap<String,String>();		
		pdfLabel.put("contractorbill.pdf.leftheader", "Form No.CON 51");
		pdfLabel.put("contractorbill.pdf.mainheader", boundary==null?"":boundary.getName()+"\n   Contractor Bill");		
		pdfLabel.put("contractorbill.pdf.rightheader","Department: ");
		
		pdfLabel.put("contractorbill.pdf.contractoraddress","Contractor Name and Address:- ");
		pdfLabel.put("contractorbill.pdf.billno","Bill number: ");
		pdfLabel.put("contractorbill.pdf.dateofbill","Date of Bill Creation: ");
		pdfLabel.put("contractorbill.pdf.typeofbill","Part / Final Bill: ");
		
		pdfLabel.put("contractorbill.pdf.workdescription","Work Description: ");		
		pdfLabel.put("contractorbill.pdf.workcommencedon","Work Commenced on: ");
		pdfLabel.put("contractorbill.pdf.workcompleteon","Work Completed on: ");
		
		pdfLabel.put("contractorbill.pdf.projectcode","Project Code");
		pdfLabel.put("contractorbill.pdf.assetcode","Asset Code and Description");
		pdfLabel.put("contractorbill.pdf.Mbno","M Book \n No");
		pdfLabel.put("contractorbill.pdf.pages","Pages"); 
		pdfLabel.put("contractorbill.pdf.from","From");    
		pdfLabel.put("contractorbill.pdf.to","To");
		pdfLabel.put("contractorbill.pdf.estimateno","Estimate Number: "); 
		pdfLabel.put("contractorbill.pdf.estimateamt","Estimate Amount: "); 
		pdfLabel.put("contractorbill.pdf.todate","To Date ");
		pdfLabel.put("contractorbill.pdf.lastbill","Since Last Bill ");
		
		pdfLabel.put("contractorbill.pdf.valueofworkdone","Value of Work Done ");		
		pdfLabel.put("contractorbill.pdf.withholdrelease","With hold Release ");
		pdfLabel.put("contractorbill.pdf.netamount","Net amount payable (Rupees (in words)) ");		
		//approval details
		pdfLabel.put("contractorbill.pdf.preparedby","Approved By:");
		pdfLabel.put("contractorbill.pdf.checkedby","Checked By:");
		pdfLabel.put("contractorbill.pdf.approvaldetails","Approval Details");
		pdfLabel.put("contractorbill.pdf.aprvalstep","Approval Step");
		pdfLabel.put("contractorbill.pdf.name","Name");
		pdfLabel.put("contractorbill.pdf.designation","Designation");
		pdfLabel.put("contractorbill.pdf.aprvdon","Approved On");
		pdfLabel.put("contractorbill.pdf.remarks","Remarks");
		pdfLabel.put("contractorbill.pdf.deductions","Deductions");	
		
		pdfLabel.put("contractorbill.pdf.percentage", "Percentage (%)");
		
		pdfLabel.put("contractorbill.pdf.contractorbill","CONTRACTOR BILL");
		pdfLabel.put("contractorbill.pdf.certificate","CERTIFICATE");
		pdfLabel.put("contractorbill.pdf.certificatecontent1","1. Certified that the claim is correct, that necessary measurments have been made by me on \n" +
					"_________________________________ and the work has been satisfactorily performed vide pages \n" +
					"__________________________________ Measurement Book No.  __________________________________");
		
		pdfLabel.put("contractorbill.pdf.juniorengineer","Junior Engineer");
		pdfLabel.put("contractorbill.pdf.date","Date");
		
		pdfLabel.put("contractorbill.pdf.certificatecontent2","2. Certified that the work was / materials were duly check measured by me \n" +
				"on _______________________________\n");
		
		pdfLabel.put("contractorbill.pdf.certificatecontent3","3. Certified that the work has been completed, in accordance with the plan and estimate in a \n"+
				"substantial and satisfactory manner");
		
		pdfLabel.put("contractorbill.pdf.certificatecontent4","4. Certified that the contractor has employed Technical Assistant as required in the Agreement");
		pdfLabel.put("contractorbill.pdf.certificatecontent5","5. Certified that the debris has been removed and that the carriage way work and water table has "+
				"completed");
		pdfLabel.put("contractorbill.pdf.certificatecontent6","The Certificate mentioned against Sl.No. _________________________________________ are \n"+
				"applicable to this bill and the certificates mentioned against Sl.No._________________________________________________ \n"+
				"_______________________________  are deleted");
		pdfLabel.put("contractorbill.pdf.exeasstengineer","Exe. Engineer /Asst. Exe. Engineer");
		return pdfLabel;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public Object getModel() {
		return null;
	}
	
}