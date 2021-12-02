package com.a2mee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.a2mee.model.ItemMst;
import com.a2mee.model.StorageBinMst;

@Repository
@Transactional
public class ItemDaoCustomeImpl implements ItemDaoCustome {
	@PersistenceContext
	EntityManager entityManager;
	@Override
	public List<ItemMst> itemListByPagination(int pageno, int perPage) {
		// TODO Auto-generated method stub

		int total_count=entityManager.createQuery("from ItemMst ").getResultList().size();
		int firstR = total_count-(pageno*perPage);
		int maxR = total_count-((pageno-1)*perPage);
		
	
		if(firstR<0) {
			firstR=0;
		}
		Query query = entityManager.createQuery("from ItemMst");

				query.setFirstResult(firstR); // modify this to adjust paging
				query.setMaxResults(maxR);
        return (List<ItemMst>) query.getResultList();
	}

}
