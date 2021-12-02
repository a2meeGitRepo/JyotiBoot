package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.PurchaseOrderErrDao;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.dto.PurchaseOrderErrDto;

@Repository
@Transactional
public class PurchaseOrderErrDaoImpl implements PurchaseOrderErrDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void savePuOrderErrors(PurchaseOrderError thePurchaseOrderError) {
		entityManager.merge(thePurchaseOrderError);
	}

	@Override
	public boolean delPuOrderErrors(long purchaseOrderItemId) {
		int deletes = 1;
		String status = "Done";
		Query query = entityManager.createQuery("UPDATE PurchaseOrderError AS pe SET pe.deletes=:deletes,pe.status=:status WHERE pe.purchaseOrderItemId =:purchaseOrderItemId");
		query.setParameter("purchaseOrderItemId", purchaseOrderItemId);
		query.setParameter("deletes", deletes);
		query.setParameter("status", status);
		int i = query.executeUpdate();
		if(i>0) return true;
		else return false;
	}

	@Override
	public List<PurchaseOrderError> getErrorListByPoItemId(long purchaseOrderItemId) {
		// TODO Auto-generated method stub
		
			@SuppressWarnings("unchecked")
			List<PurchaseOrderError> purchaseOrderList = entityManager.createQuery("FROM PurchaseOrderError WHERE purchaseOrderItemId= :purchaseOrderItemId ",PurchaseOrderError.class).setParameter("purchaseOrderItemId", purchaseOrderItemId)
												.getResultList();
			return purchaseOrderList;
		
	}

	@Override
	public boolean deletePuOrderErrors(long purchaseOrderItemId) {		
		Query query = entityManager.createQuery("DELETE FROM PurchaseOrderError WHERE purchaseOrderItemId= :purchaseOrderItemId");
		query.setParameter("purchaseOrderItemId", purchaseOrderItemId);
		int i = query.executeUpdate();
		if(i>0) return true;
		else return false;
	}

	@Override
	public int getCountByPurchaseOrderNoAndITem(long purchaseOrderNO, long purchaseOrderItem) {
		
		
		long count=(long) entityManager.createQuery("Select Count(*) FROM PurchaseOrderError WHERE purchaseOrderItemId= :purchaseOrderItemId and purchaseOrderNo=:purchaseOrderNo").setParameter("purchaseOrderItemId", purchaseOrderItem).setParameter("purchaseOrderNo", purchaseOrderNO).getSingleResult();

		return (int) count;
	}

}
