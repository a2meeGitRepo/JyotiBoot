package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.PurchaseOrderItemDao;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.PurchaseOrderItem;

@Transactional
@Repository
public class PurchaseOrderItemDaoImpl implements PurchaseOrderItemDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Value(value = "${purchaseOrderItemDao.getPurchaseOrderItemList.hql}")
	private String purchaseOrderItemDaoGetPurchaseOrderItemListHql;
	
	@Value(value = "${purchaseOrderItemDao.updateList.hql}")
	private String purchaseOrderItemDaoUpdateListHql;
	
	@Override
	public List<PurchaseOrderItem> getPurchaseOrderItemList() {
		return entityManager.createQuery(purchaseOrderItemDaoGetPurchaseOrderItemListHql).getResultList();
	}

	@Override
	public List<PurchaseOrderItem> getPuOrItemListbyId(long purchaseOrderId) {
		List<PurchaseOrderItem> purchaseOrderItemList = entityManager.createQuery("FROM PurchaseOrderItem WHERE purchaseOrder.purchaseOrderNo= :purchaseOrderId ",PurchaseOrderItem.class).setParameter("purchaseOrderId", purchaseOrderId)
				.getResultList();
		System.out.println("Size of purchaseOrderItemList"+purchaseOrderItemList.size());
		return purchaseOrderItemList;
	}

	@Override
	public void savePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		entityManager.persist(purchaseOrderItem);
		entityManager.flush();
	}

	@Override
	public PurchaseOrderItem searchByPoNoItemId(long purchaseOrderNo, String itemMstId) {
		return entityManager.createQuery("FROM PurchaseOrderItem WHERE purchaseOrder.purchaseOrderNo= :purchaseOrderNo AND itemMst.id= :itemMstId",PurchaseOrderItem.class)
				.setParameter("purchaseOrderNo", purchaseOrderNo)
				.setParameter("itemMstId", itemMstId)
				.getSingleResult();
	}

	@Override
	public List<PurchaseOrderItem> listByPoNoItemId(long purchaseOrderId, String itemId) {
		return entityManager.createQuery("FROM PurchaseOrderItem WHERE purchaseOrder.purchaseOrderNo= :purchaseOrderId AND itemMst.id= :itemId",PurchaseOrderItem.class)
				.setParameter("purchaseOrderId", purchaseOrderId)
				.setParameter("itemId", itemId)
				.getResultList();
	}

	@Override
	public List<PurchaseOrderItem> getEntirePurchaseOrderItem() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from PurchaseOrderItem",PurchaseOrderItem.class).getResultList();
	}

	@Override
	public void updatePoItem(PurchaseOrderItem thePurchaseOrderItem) {
			
			entityManager.createQuery(purchaseOrderItemDaoUpdateListHql)
			.setParameter("purchaseOrderItemId", thePurchaseOrderItem.getPurchaseOrderItemId())
			.setParameter("inwardDate", thePurchaseOrderItem.getInwardDate())
			.setParameter("invoiceNumber", thePurchaseOrderItem.getInvoiceNumber())
			.setParameter("challanNumber", thePurchaseOrderItem.getChallanNumber())
			.setParameter("docDate", thePurchaseOrderItem.getDocDate())
			.setParameter("remarks", thePurchaseOrderItem.getRemarks())
			.setParameter("type", thePurchaseOrderItem.getType())
			.setParameter("boe", thePurchaseOrderItem.getBoe())
			.setParameter("courier", thePurchaseOrderItem.getCourier())
			.setParameter("docketNo", thePurchaseOrderItem.getDocketNo())
			.setParameter("sapGrnNo", thePurchaseOrderItem.getSapGrnNo())
			.setParameter("sapGrnDate", thePurchaseOrderItem.getSapGrnDate())
			.setParameter("delayDays", thePurchaseOrderItem.getDelayDays())
			.setParameter("resPerson", thePurchaseOrderItem.getResPerson())
			.setParameter("delayPerc", thePurchaseOrderItem.getDelayPerc())
			.setParameter("accDocHandover", thePurchaseOrderItem.getAccDocHandover())
			.setParameter("handoverDate", thePurchaseOrderItem.getHandoverDate())
			.setParameter("vehicleNo", thePurchaseOrderItem.getVehicleNo())
			.setParameter("vehicleStatus", thePurchaseOrderItem.getVehicleStatus())
			.setParameter("unloadType", thePurchaseOrderItem.getUnloadType())
			.setParameter("packingType", thePurchaseOrderItem.getPackingType())
			.setParameter("updDateTime", thePurchaseOrderItem.getUpdDateTime())
			.executeUpdate();
	}

}
