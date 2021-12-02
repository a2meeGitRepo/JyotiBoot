package com.a2mee.dao.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.PurchaseOrderDao;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.dto.PurchaseOrderDto;


@Transactional
@Repository
public class PurchaseOrderDaoImpl implements PurchaseOrderDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Value(value = "${purchaseOrderDao.getPurchaseOrderList.hql}")
	private String purchaseOrderDaoGetPurchaseOrderListHql;

	@Value(value = "${purchaseOrderDao.searchByPurchaseOrderNo.hql}")
	private String purchaseOrderDaoSearchByPurchaseOrderNoHql;
	
	@Value(value = "${purchaseOrderDao.getNoErrPurchaseOrderList.hql}")
	private String purchaseOrderDaoGetNoErrPurchaseOrderListHql;
	
	@Value(value = "${purchaseOrderDao.searchByNoErrPurchaseOrderNo.hql}")
	private String purchaseOrderDaoSearchByNoErrPurchaseOrderNoHql;
	
	@Value(value = "${purchaseOrderDao.getErrPurchaseOrderList.hql}")
	private String purchaseOrderDaoGetErrPurchaseOrderListHql;
	
	@Value(value = "${purchaseOrderDao.searchByErrPurchaseOrderNo.hql}")
	private String purchaseOrderDaoSearchByErrPurchaseOrderNoHql;
	
	@Override
	public List<Object[]> getPurchaseOrderList(String venId, String itemId) {
		return getPoList(purchaseOrderDaoGetPurchaseOrderListHql, venId, itemId);
	}

	@Override
	public List<Object[]> searchByPurchaseOrderNo(long purchaseOrderNo) {
		return getPoList(purchaseOrderDaoSearchByPurchaseOrderNoHql, purchaseOrderNo);
	}

	@Override
	public List<Object[]> getNoErrPurchaseOrderList(String venId, String itemId) {
		return getPoList(purchaseOrderDaoGetNoErrPurchaseOrderListHql, venId, itemId);
	}

	@Override
	public List<Object[]> searchByNoErrPurchaseOrderNo(long purchaseOrderNo) {
		return getPoList(purchaseOrderDaoSearchByNoErrPurchaseOrderNoHql, purchaseOrderNo);
	}
	
	@Override
	public List<Object[]> getErrPurchaseOrderList(String venId, String itemId) {
		return getPoList(purchaseOrderDaoGetErrPurchaseOrderListHql, venId, itemId);
	}

	@Override
	public List<Object[]> searchByErrPurchaseOrderNo(long purchaseOrderNo) {
		return getPoList(purchaseOrderDaoSearchByErrPurchaseOrderNoHql, purchaseOrderNo);
	}
		
	private List<Object[]> getPoList(String hql, String venId, String itemId) {
		@SuppressWarnings("unchecked")
		List<Object[]> purchaseOrderList = entityManager.createQuery(hql).setParameter("venId", venId)
											.setParameter("itemId", itemId).getResultList();

		return purchaseOrderList;
	}

	private List<Object[]> getPoList(String hql, long purchaseOrderNo) {
		@SuppressWarnings("unchecked")
		List<Object[]> purchaseOrderList = entityManager.createQuery(hql).setParameter("purchaseOrderNo", purchaseOrderNo)
											.getResultList();
		return purchaseOrderList;
	}

	@Override
	public PurchaseOrder searchByPurchaseOrderId(long purchaseOrderId) {
		PurchaseOrder purchaseOrderList = entityManager.createQuery("FROM PurchaseOrder WHERE purchaseOrderNo= :purchaseOrderId ",PurchaseOrder.class).setParameter("purchaseOrderId", purchaseOrderId)
				.getSingleResult();
		return purchaseOrderList;
	}

	@Override
	public List<PurchaseOrder> getPurchaseOrderListByNo(long purchaseOrder) {
		// TODO Auto-generated method stub
		List<PurchaseOrder> purchaseOrderList = entityManager.createQuery("FROM PurchaseOrder WHERE purchaseOrderNo= :purchaseOrderNo ",PurchaseOrder.class).setParameter("purchaseOrderNo", purchaseOrder)
				.getResultList();
		return purchaseOrderList;
	}

	@Override
	public void savePurchaseOrder(PurchaseOrder purchaseOrder) {
		// TODO Auto-generated method stub
		entityManager.persist(purchaseOrder);
		entityManager.flush();
	}

	@Override
	public List<PurchaseOrder> getEntirePurchaseOrder() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from PurchaseOrder",PurchaseOrder.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchaseOrder> getInwardedPo() {
		// TODO Auto-generated method stub
		 LocalDate date = LocalDate.now();
		 java.util.Date today = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return entityManager.createQuery("SELECT p from PurchaseOrder p, PurchaseOrderItem pi WHERE pi.purchaseOrder.purchaseOrderNo = p.purchaseOrderNo AND pi.inwardDate IS NOT NULL AND pi.poiStatus IS NULL AND pi.inwardDate=:today ORDER BY pi.updDateTime DESC",PurchaseOrder.class)
				.setParameter("today", today)
				.getResultList();
	}

	@Override
	public List<PurchaseOrder> getPurchaseOrders(int pageNo, int itemPerPage) {
		long total_count = (long) entityManager.createQuery("SELECT count(p) FROM PurchaseOrder p").getSingleResult();
		System.out.println("count========"+total_count);
		int firstR = (int)total_count-(pageNo*itemPerPage);
		int maxR = (int)total_count-((pageNo-1)*itemPerPage);
		
		String sql = "SELECT p FROM PurchaseOrder p";
		Query query = entityManager.createQuery(sql);
		query.setFirstResult(firstR);
		query.setMaxResults(maxR);
		List result = query.getResultList();
		return result;
//		return entityManager.createQuery("from PurchaseOrder",PurchaseOrder.class).getResultList();
	}

	@Override
	public int getTotalCount() {
		long result = (long) entityManager.createQuery("SELECT count(p) FROM PurchaseOrder p").getSingleResult();
		int result2 = (int) result;
		return result2;
	}
}
