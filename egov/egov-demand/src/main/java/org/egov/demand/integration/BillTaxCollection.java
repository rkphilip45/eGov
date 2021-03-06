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
package org.egov.demand.integration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.InvalidAccountHeadException;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.dao.EgBillDetailsDao;
import org.egov.demand.dao.EgBillReceiptDao;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.exceptions.EGOVRuntimeException;
/*import org.egov.erpcollection.integration.models.BillReceiptInfo;
import org.egov.erpcollection.integration.models.ReceiptAccountInfo;*/
import org.hibernate.ObjectNotFoundException;

public class BillTaxCollection
{
	/** Api to link bill to receipt.  
	 * 
	 * @param org.egov.infstr.collections.integration.models.BillReceiptInfo bri 
	 * @param org.egov.lib.rjbac.user.User user
	 * 
	 * @return org.egov.demand.model.BillReceipt
	 * 
	 * @throws InvalidAccountHeadException 
	 */
	public BillReceipt linkBillToReceipt(BillReceiptInfo bri) throws InvalidAccountHeadException,ObjectNotFoundException
	{
		BillReceipt billRecpt = null;
		try
		{
			if(bri!=null)
			{
				billRecpt = new BillReceipt();
				EgBillDao billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
				EgBillDetailsDao billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
				EgBillReceiptDao billrcrDao= DCBDaoFactory.getDaoFactory().getEgBillReceiptDao();
				EgBill egBill = (EgBill)billDao.findById(new Long(bri.getBillReferenceNum()), false);
				List<EgBillDetails> billDetList= billDetDao.getBillDetailsByBill(egBill);
				BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri, billDetList);
				billRecpt.setBillId(egBill);
				billRecpt.setReceiptAmt(totalCollectedAmt);
				billRecpt.setReceiptNumber(bri.getReceiptNum());
				billRecpt.setReceiptDate(bri.getReceiptDate().toDate());
				billRecpt.setCollectionStatus(bri.getReceiptStatus().getCode());
				billRecpt.setIsCancelled(Boolean.FALSE);
				billRecpt.setCreatedBy(bri.getCreatedBy());
				billRecpt.setModifiedBy(bri.getModifiedBy());
				billRecpt.setCreatedDate(new Date());
				billRecpt.setModifiedDate(new Date());
				billrcrDao.create(billRecpt);
			}
		}
		catch(EGOVRuntimeException e)
		{
			throw new EGOVRuntimeException("Exception in linkBillToReceipt"+e);
		}
		return billRecpt;
	}


	/** Api to update the bill details with the amount paid
	 * 
	 * @param org.egov.infstr.collections.integration.models.BillReceiptInfo bri 
	 *  
	 * @return void
	 * 
	 * @throws InvalidAccountHeadException 
	 */
	public EgBill updateBillDetails(BillReceiptInfo bri) throws InvalidAccountHeadException
	{
		EgBill egBill = null;
		try
		{
			if(bri!=null)
			{
				EgBillDao billDao = DCBDaoFactory.getDaoFactory().getEgBillDao();
				EgBillDetailsDao billDetDao = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();

				egBill = (EgBill)billDao.findById(new Long(bri.getBillReferenceNum()), false);
				List<EgBillDetails> billDetList= billDetDao.getBillDetailsByBill(egBill);

				BigDecimal totalCollectedAmt = calculateTotalCollectedAmt(bri, billDetList);

				for(EgBillDetails billDet : billDetList)
				{
					Boolean glCodeExist = false;
					for(ReceiptAccountInfo acctDet : bri.getAccountDetails())
					{  
						if(billDet.getGlcode().equals(acctDet.getGlCode()))
						{
							glCodeExist = true;
							billDet.setCollectedAmount(acctDet.getCrAmount());
							billDet.setCrAmount(acctDet.getCrAmount());
							billDet.setDrAmount(acctDet.getDrAmount());
							//billDet.setOrderNo(1);
							billDetDao.update(billDet);
						}
					}
					if(!glCodeExist)
					{
						throw new InvalidAccountHeadException("GlCode does not exist for "+billDet.getGlcode());
					}
				}
				egBill.setTotalCollectedAmount(totalCollectedAmt);

				billDao.update(egBill);
			}
		}
		catch(EGOVRuntimeException e)
		{
			throw new EGOVRuntimeException("Exception in updateBillDetails"+e);
		}
		return egBill;
	}

	public BigDecimal calculateTotalCollectedAmt(BillReceiptInfo bri, List<EgBillDetails> billDetList) throws InvalidAccountHeadException
	{
		BigDecimal totalCollAmt = BigDecimal.ZERO;
		try
		{
			if(bri!=null && billDetList!=null)
			{
				for(EgBillDetails billDet : billDetList)
				{
					Boolean glCodeExist = false;
					for(ReceiptAccountInfo acctDet : bri.getAccountDetails())
					{  
						if(billDet.getGlcode().equals(acctDet.getGlCode()))
						{
							glCodeExist = true;
							totalCollAmt = totalCollAmt.add(acctDet.getCrAmount());
						}
					}
					if(!glCodeExist)
					{
						throw new InvalidAccountHeadException("GlCode does not exist for "+billDet.getGlcode());
					}
				}
			}
		}
		catch(EGOVRuntimeException e)
		{
			throw new EGOVRuntimeException("Exception in calculate Total Collected Amt"+e);
		}

		return totalCollAmt;
	}
}

