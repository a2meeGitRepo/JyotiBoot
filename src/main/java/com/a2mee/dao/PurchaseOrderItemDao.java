package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.PurchaseOrderItem;

public interface PurchaseOrderItemDao {
	
	public List<PurchaseOrderItem> getPurchaseOrderItemList();

	public List<PurchaseOrderItem> getPuOrItemListbyId(long purchaseOrderId);

	public void savePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem);

	public PurchaseOrderItem searchByPoNoItemId(long purchaseOrderNo, String itemMstId);

	public List<PurchaseOrderItem> listByPoNoItemId(long purchaseOrderId, String itemId);

	public List<PurchaseOrderItem> getEntirePurchaseOrderItem();

	public void updatePoItem(PurchaseOrderItem thePurchaseOrderItem);

}
