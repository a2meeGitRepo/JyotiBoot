package com.a2mee.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.PurchaseOrderErrDao;
import com.a2mee.model.dto.PurchaseOrderErrDto;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.services.PurchaseOrderErrService;

@Service
public class PurchaseOrderErrServiceImpl implements PurchaseOrderErrService {
	
	@Autowired
	private PurchaseOrderErrDao purchaseOrderErrDao;

	@Override
	public void savePuOrderErrors(PurchaseOrderError thePurchaseOrderError) {
		purchaseOrderErrDao.savePuOrderErrors(thePurchaseOrderError);

	}

	@Override
	public boolean delPuOrderErrors(long purchaseOrderItemId) {
		return purchaseOrderErrDao.delPuOrderErrors(purchaseOrderItemId);
	}

	@Override
	public List<PurchaseOrderError> getErrorListByPoItemId(long purchaseOrderItemId) {
		// TODO Auto-generated method stub
		return purchaseOrderErrDao.getErrorListByPoItemId(purchaseOrderItemId);
	}

	@Override
	public boolean deletePuOrderErrors(long purchaseOrderItemId) {
		return purchaseOrderErrDao.deletePuOrderErrors(purchaseOrderItemId);
	}

}
