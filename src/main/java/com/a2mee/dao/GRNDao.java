package com.a2mee.dao;
import java.util.List;

import com.a2mee.model.GRN;
import com.a2mee.model.PurchaseOrder;

public interface GRNDao{
	
public List<Object[]> getGrnList(String venId,String itemId);
public List<Object[]> serchByPONo(long purchaseOrderNo);
public void saveGrn(GRN theGrn);
public List <GRN> getGrnByGrnNo(long grnNo);
}