package com.a2mee.dao;
import java.util.List;

import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.dto.PurchaseOrderDto;

public interface PurchaseOrderDao{
	
public List<Object[]> getPurchaseOrderList(String venId,String itemId);
public List<Object[]> searchByPurchaseOrderNo(long purchaseorderNo);
public List<Object[]> getNoErrPurchaseOrderList(String venId, String itemId);
public List<Object[]> searchByNoErrPurchaseOrderNo(long purchaseOrderNo);
public List<Object[]> getErrPurchaseOrderList(String venId, String itemId);
public List<Object[]> searchByErrPurchaseOrderNo(long purchaseOrderNo);
public PurchaseOrder searchByPurchaseOrderId(long purchaseOrderId);
public List<PurchaseOrder> getPurchaseOrderListByNo(long purchaseOrder);
public void savePurchaseOrder(PurchaseOrder purchaseOrder);
public List<PurchaseOrder> getEntirePurchaseOrder();
public List<PurchaseOrder> getInwardedPo();

public List<PurchaseOrder> getPurchaseOrders(int pageNo, int itemPerPage);
public int getTotalCount();

}