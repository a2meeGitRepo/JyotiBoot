package com.a2mee.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.PurchaseOrderItemDao;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.PurchaseOrderItem;
import com.a2mee.services.PurchaseOrderItemService;

@Service
public class PurchaseOrderItemServicesImpl implements PurchaseOrderItemService {

	@Autowired
	PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Override
	public List<PurchaseOrderItem> getPurchaseOrderItemList() {
		return purchaseOrderItemDao.getPurchaseOrderItemList();
	}

	@Override
	public List<PurchaseOrderItem> getPuOrItemListbyId(long purchaseOrderId) {
		return purchaseOrderItemDao.getPuOrItemListbyId(purchaseOrderId);
	}

	@Override
	public void savePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		// TODO Auto-generated method stub
		purchaseOrderItemDao.savePurchaseOrderItem(purchaseOrderItem);
	}

	@Override
	public PurchaseOrderItem searchByPoNoItemId(long purchaseOrderNo, String itemMstId) {
		return purchaseOrderItemDao.searchByPoNoItemId(purchaseOrderNo, itemMstId);
	}

	@Override
	public List<PurchaseOrderItem> listByPoNoItemId(long purchaseOrderId, String itemId) {
		return purchaseOrderItemDao.listByPoNoItemId(purchaseOrderId, itemId);
	}

	@Override
	public List<PurchaseOrderItem> getEntirePurchaseOrderItem() {
		// TODO Auto-generated method stub
		return purchaseOrderItemDao.getEntirePurchaseOrderItem();
	}

	@Override
	public void updatePoItem(PurchaseOrderItem thePurchaseOrderItem) {
		purchaseOrderItemDao.updatePoItem(thePurchaseOrderItem);
	}

}
