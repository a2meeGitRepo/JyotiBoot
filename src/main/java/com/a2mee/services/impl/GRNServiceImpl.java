package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.a2mee.dao.GRNDao;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.GRN;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.services.GRNService;


@Service
public class GRNServiceImpl implements GRNService {
	@Autowired
	GRNDao grnDao;

	
	@Override
	public List<GrnDto> getGrnList(String venId, String itemId) {
		
		List<Object[]> list=grnDao.getGrnList(venId, itemId);
		System.out.println("size in service="+list.size());
		 List<GrnDto> grdList = new ArrayList<GrnDto>();
		 list.forEach(vmArray -> {
			    GrnDto gd  =new GrnDto();
			    gd.setGrnId((long)vmArray[0]);
			    gd.setGrnEntryDate((Date)vmArray[1]);
			    gd.setItemLotNo((String)vmArray[2]);
			    gd.setGrnItmLotID((long)vmArray[3]);
			    gd.setItemDtils((String)vmArray[4]);
			    gd.setBatchQty((double)vmArray[5]);
			    gd.setVenName((String)vmArray[6]);
			    gd.setBarcode((String)vmArray[7]);
			    gd.setGrnBy((String)vmArray[8]);
			    gd.setInwardDate((Date)vmArray[9]);
			    gd.setInvoiceNumber((String)vmArray[10]);
			    gd.setChallanNumber((String)vmArray[11]);
			    gd.setDocDate((Date)vmArray[12]);
			    gd.setRemarks((String)vmArray[13]);
			    gd.setErrorSolveDate((Date)vmArray[14]);
			    gd.setInwardTime((String)vmArray[15]);
			    gd.setGrnStatus((String)vmArray[16]);
			    gd.setGrnNo((long)vmArray[17]);
			    gd.setItemTolRecd((double)vmArray[18]);
			    gd.setItemMstId((String)vmArray[19]);
			    gd.setPurchaseOrderNo((long)vmArray[20]);
			    
			    gd.setType((String)vmArray[21]);
			    gd.setBoe((String)vmArray[22]);
			    gd.setCourier((String)vmArray[23]);
			    gd.setDocketNo((String)vmArray[24]);
			    gd.setSapGrnNo((String)vmArray[25]);
			    gd.setSapGrnDate((Date)vmArray[26]);
			    gd.setDelayDays((String)vmArray[27]);
			    gd.setResPerson((String)vmArray[28]);
			    gd.setDelayPerc((String)vmArray[29]);
			    gd.setAccDocHandover((String)vmArray[30]);
			    gd.setHandoverDate((Date)vmArray[31]);
			    gd.setVehicleNo((String)vmArray[32]);
			    gd.setVehicleStatus((String)vmArray[33]);
			    gd.setUnloadType((String)vmArray[34]);
			    gd.setPackingType((String)vmArray[35]);
			    gd.setGrnItemId((long)vmArray[36]);
			    gd.setBoxQty((long)vmArray[37]);
			    grdList.add(gd);
				
			});
		 
		return grdList;
	}

	
	@Override
	public List<GrnDto> serchByPONo(long purchaseOrderNo) {
		List<Object[]>list=grnDao.serchByPONo(purchaseOrderNo);
		List<GrnDto> gList = new ArrayList<GrnDto>();
		list.forEach(gArray -> {
		    GrnDto gd  =new GrnDto();
		    gd.setBatchQty((double)gArray[5]);
		    gd.setGrnId((long)gArray[0]);
		    gd.setGrnEntryDate((Date)gArray[1]);
		    gd.setVenName((String)gArray[6]);
		    gd.setItemDtils((String)gArray[4]);
		    gd.setItemLotNo((String)gArray[2]);
		    gd.setGrnItmLotID((long)gArray[3]);
		    gd.setBarcode((String)gArray[7]);
		    gd.setGrnBy((String)gArray[8]);
		    gd.setInwardDate((Date)gArray[9]);
		    gd.setInvoiceNumber((String)gArray[10]);
		    gd.setChallanNumber((String)gArray[11]);
		    gd.setDocDate((Date)gArray[12]);
		    gd.setRemarks((String)gArray[13]);
		    gd.setErrorSolveDate((Date)gArray[14]);
		    gd.setInwardTime((String)gArray[15]);
		    gd.setGrnStatus((String)gArray[16]);
		    gd.setGrnNo((long)gArray[17]);
		    gd.setItemTolRecd((double)gArray[18]);
		    gd.setItemMstId((String)gArray[19]);
		    gd.setPurchaseOrderNo((long)gArray[20]);
		    
		    gd.setType((String)gArray[21]);
		    gd.setBoe((String)gArray[22]);
		    gd.setCourier((String)gArray[23]);
		    gd.setDocketNo((String)gArray[24]);
		    gd.setSapGrnNo((String)gArray[25]);
		    gd.setSapGrnDate((Date)gArray[26]);
		    gd.setDelayDays((String)gArray[27]);
		    gd.setResPerson((String)gArray[28]);
		    gd.setDelayPerc((String)gArray[29]);
		    gd.setAccDocHandover((String)gArray[30]);
		    gd.setHandoverDate((Date)gArray[31]);
		    gd.setVehicleNo((String)gArray[32]);
		    gd.setVehicleStatus((String)gArray[33]);
		    gd.setUnloadType((String)gArray[34]);
		    gd.setPackingType((String)gArray[35]);
		    gd.setGrnItemId((long)gArray[36]);
		    gd.setBoxQty((long)gArray[37]);
		    gList.add(gd);
			
		});
		return gList;
	}


	@Override
	public void saveGrn(GRN theGrn) {
		grnDao.saveGrn(theGrn);
	}


	@Override
	public List<GRN> getGrnByGrnNo(long grnNo) {
		return grnDao.getGrnByGrnNo(grnNo);
	}

}
