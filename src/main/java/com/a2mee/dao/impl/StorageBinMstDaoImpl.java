package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.a2mee.dao.StorageBinMstDaoCustom;
import com.a2mee.model.EmployeeMst;
import com.a2mee.model.StorageBinMst;

@Repository
@Transactional
public class StorageBinMstDaoImpl implements StorageBinMstDaoCustom {

	@PersistenceContext
	EntityManager entityManager;

	
	@Override
	public List<StorageBinMst> getStorageBinList() {
		@SuppressWarnings("unchecked")
		List<StorageBinMst> storageBins =  entityManager.createQuery("from StorageBinMst", StorageBinMst.class).getResultList();
		return storageBins;
	}

	@Override
	public void addStorageBin(StorageBinMst storageBinMst) {
		
		entityManager.merge(storageBinMst);
	
	}

	@Override
	public void delStorageBin(long storageBinId) {
		entityManager.createQuery("DELETE FROM StorageBinMst WHERE storageBinId= :storageBinId").setParameter("storageBinId", storageBinId).executeUpdate();
	}

	@Override
	public List<StorageBinMst> getstorageBinMstByCode(String storageBinCode) {
		return entityManager.createQuery("from StorageBinMst where storageBinCode=:storageBinCode", StorageBinMst.class).setParameter("storageBinCode", storageBinCode).getResultList();
	}

	@Override
	public List<StorageBinMst> getStorageBinListByPagination(int pageno, int perPage) {
		// TODO Auto-generated method stub
		
		
		
		int total_count=entityManager.createQuery("from StorageBinMst ").getResultList().size();
		int firstR = total_count-(pageno*perPage);   
		int maxR = total_count-((pageno-1)*perPage);
		
	
		if(firstR<0) {
			firstR=0;
		}
		Query query = entityManager.createQuery("from StorageBinMst");

				query.setFirstResult(firstR); // modify this to adjust paging
				query.setMaxResults(maxR);
        return (List<StorageBinMst>) query.getResultList();
		
	}

}
