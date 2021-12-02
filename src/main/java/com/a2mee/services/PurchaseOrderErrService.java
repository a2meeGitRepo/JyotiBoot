package com.a2mee.services;

import java.util.List;

import com.a2mee.model.dto.PurchaseOrderErrDto;
import com.a2mee.model.PurchaseOrderError;

public interface PurchaseOrderErrService {
	public void savePuOrderErrors(PurchaseOrderError thePurchaseOrderError);

	public boolean delPuOrderErrors(long purchaseOrderItemId);

	public List<PurchaseOrderError> getErrorListByPoItemId(long purchaseOrderItemId);

	public boolean deletePuOrderErrors(long purchaseOrderItemId);
}
