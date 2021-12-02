package com.a2mee.services;

import java.util.List;

import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.GRN;
import com.a2mee.model.PurchaseOrder;


public interface GRNService {

	public List<GrnDto> getGrnList(String venId,String itemId);
	public List<GrnDto> serchByPONo(long purchaseOrderNo);
	public void saveGrn(GRN theGrn);
	public List<GRN> getGrnByGrnNo(long grnNo);
	
}
