package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.GRNDao;
import com.a2mee.model.GRN;
import com.a2mee.model.PurchaseOrder;

@Transactional
@Repository
public class GRNDaoImpl implements GRNDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Value(value = "${grnDao.getGrnList.hql}")
	private String grnDaoGetGrnListHql;

	@Value(value = "${grnDao.serchByPONo.hql}")
	private String grnDaoSerchByPONoHql;

	public List<Object[]> getGrnList(String venId, String itemId) {

		@SuppressWarnings("unchecked")
		List<Object[]> grnList = entityManager.createQuery(grnDaoGetGrnListHql).setParameter("venId", venId)
				.setParameter("itemId", itemId).getResultList();

		return grnList;

	}

	@Override
	public List<Object[]> serchByPONo(long purchaseOrderNo) {
		@SuppressWarnings("unchecked")
		List<Object[]> grnList = entityManager.createQuery(grnDaoSerchByPONoHql).setParameter("purchaseOrderNo", purchaseOrderNo)
				.getResultList();
		System.out.println("size in dao=" + grnList.size());
		return grnList;
	}

	@Override
	public void saveGrn(GRN theGrn) {
		entityManager.persist(theGrn);
		
//		
//		entityManager.createQuery(grnDaoUpdateListHql)
//		.setParameter(0, theGrn.getPurchaseOrderId())
//		.executeUpdate();		
	}

	@Override
	public List<GRN> getGrnByGrnNo(long grnNo) {
		return entityManager.createQuery("from GRN where grnNo= :grnNo",GRN.class).setParameter("grnNo", grnNo)
				.getResultList();
	}

}
