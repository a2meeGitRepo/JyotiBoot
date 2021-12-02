package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.GrnItemDao;
import com.a2mee.model.GrnItem;
import com.a2mee.model.ItemMst;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.services.ItemMstServices;

@Transactional
@Repository
public class GrnItemDaoImpl implements GrnItemDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	ItemMstServices itemMstServices;
	
	@Value(value = "${grnItemDao.getGrnItemList.hql}")
	private String grnItemDaoGetGrnItemListHql;

	@Override
	public List<GrnItem> getGrnItemList() {
		return entityManager.createQuery(grnItemDaoGetGrnItemListHql).getResultList();

	}

	@Override
	public void saveGrnItem(GrnItem theGrnItem) {
		entityManager.persist(theGrnItem);
	}

	@Override
	public List<Object[]> getGrnItemList(String itemId) {
		@SuppressWarnings("unchecked")
		List<Object[]> grnItemList = entityManager.createQuery("SELECT DISTINCT itemQty,itemDtils,itemTolRecd from GrnItem g where g.itemMst.id = :itemId").setParameter("itemId", itemId).getResultList();
		return grnItemList;
	}

	@Override
	public void updateSapData(GrnItem grnItem) {
		entityManager.createQuery("UPDATE GrnItem AS gi SET gi.sapGrnNo=:sapGrnNo, gi.sapGrnDate=:sapGrnDate WHERE gi.grnItemId =:grnItemId")
		.setParameter("grnItemId",grnItem.getGrnItemId())
		.setParameter("sapGrnNo",grnItem.getSapGrnNo())
		.setParameter("sapGrnDate",grnItem.getSapGrnDate())
		.executeUpdate();
	}

	@Override
	public void deleteGrn(GrnDto grnDto) {
		entityManager.createQuery("DELETE FROM GrnItemLot gil where gil.grnItem.grnItemId=:grnItemId")
		.setParameter("grnItemId",grnDto.getGrnItemId())
		.executeUpdate();
		
		entityManager.createQuery("DELETE FROM GrnItem gi where gi.grn.grnId=:grnId")
		.setParameter("grnId",grnDto.getGrnId())
		.executeUpdate();
		
		entityManager.createQuery("DELETE FROM GRN g where g.grnId=:grnId")
		.setParameter("grnId",grnDto.getGrnId())
		.executeUpdate();
		
		entityManager.createQuery("UPDATE PurchaseOrderItem AS pi SET pi.poiStatus=NULL where pi.purchaseOrder.purchaseOrderNo=:purchaseOrderNo AND pi.itemMst.id=:itemMstId")
		.setParameter("purchaseOrderNo",grnDto.getPurchaseOrderNo())
		.setParameter("itemMstId",grnDto.getItemMstId())
		.executeUpdate();
		
		entityManager.createQuery("UPDATE PurchaseOrder AS p SET p.purchaseOrderStatus=NULL WHERE p.purchaseOrderNo =:purchaseOrderNo")
		.setParameter("purchaseOrderNo",grnDto.getPurchaseOrderNo())
		.executeUpdate();
	}
}
