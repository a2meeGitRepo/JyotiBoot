package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.VendorMstDao;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.VendorMst;

@Transactional
@Repository

public class VendorMstDaoImpl implements VendorMstDao {
	@PersistenceContext
	private EntityManager entityManager;

	@Value(value = "${vendorMstDao.getVenListFromGrn.hql}")
	private String vendorMstDaoGetVenListFromGrnHql;
	
	@Value(value = "${vendorMstDao.getVenList.hql}")
	private String vendorMstDaoGetVenListHql;
	
	public List<Object[]> getVenList() {

		List<Object[]> venList = entityManager.createQuery(vendorMstDaoGetVenListHql).getResultList();

		return venList;
	}

	@Override
	public List<VendorMst> getVenByCode(String vendorCode) {
		// TODO Auto-generated method stub
		return entityManager.createQuery("FROM VendorMst WHERE id= :vendorCode ",VendorMst.class).setParameter("vendorCode", vendorCode).getResultList();
	}

	@Override
	public void saveVen(VendorMst vendorMst) {
		// TODO Auto-generated method stub
		entityManager.persist(vendorMst);
		entityManager.flush();
	}

	@Override
	public List<Object[]> getVenGrnList() {
		List<Object[]> venList = entityManager.createQuery(vendorMstDaoGetVenListFromGrnHql).getResultList();

		return venList;
	}

	@Override
	public List<VendorMst> getEntireVenMst() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from VendorMst",VendorMst.class).getResultList();
	}

}
