package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.a2mee.dao.ProductionOrderRepoCustome;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.StorageBinMst;
@Repository
@Transactional
public class ProductionOrderRepoCustomeImpl implements ProductionOrderRepoCustome{

	
	@PersistenceContext
	EntityManager entityManager;
	
	
	@Override
	public List<ProductionOrder> getProductionOrdersByPagination(int pageno, int perPage) {
		// TODO Auto-generated method stub
		
		int total_count=entityManager.createQuery("from ProductionOrder ").getResultList().size();
		int firstR = total_count-(pageno*perPage);
		int maxR = total_count-((pageno-1)*perPage);
		
	
		if(firstR<0) {
			firstR=0;
		}
		Query query = entityManager.createQuery("from ProductionOrder");

				query.setFirstResult(firstR); // modify this to adjust paging
				query.setMaxResults(maxR);
        return (List<ProductionOrder>) query.getResultList();
			}

}
