package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.dto.PurchaseOrderErrDto;

public interface PurchaseOrderErrDao {
	public void savePuOrderErrors(PurchaseOrderError thePurchaseOrderError);

	public boolean delPuOrderErrors(long purchaseOrderId);

	public List<PurchaseOrderError> getErrorListByPoItemId(long purchaseOrderItemId);

	public boolean deletePuOrderErrors(long purchaseOrderItemId);

	public int getCountByPurchaseOrderNoAndITem(long purchaseOrderNO, long purchaseOrderItem);
}
