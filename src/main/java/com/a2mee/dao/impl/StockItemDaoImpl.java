package com.a2mee.dao.impl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.datetime.joda.LocalDateTimeParser;
import org.springframework.stereotype.Repository;

import com.a2mee.dao.StockItemDao;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.StockItem;
import com.mysql.fabric.xmlrpc.base.Data;

@Transactional
@Repository

public class StockItemDaoImpl implements StockItemDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Value(value = "${stockItemDao.checkAvailability.hql}")
	private String stockItemDaoCheckAvailabilityHql;

	@SuppressWarnings("unchecked")
	@Override
	public StockItem saveAndSaveStock(StockItem stockItem) {
		entityManager.persist(stockItem);
		return stockItem;
	}

	@Override
	public StockItem checkAvailability(String id) {
			
		StockItem stockItemList = entityManager.createQuery(stockItemDaoCheckAvailabilityHql, StockItem.class).setParameter("itemMstid", id).getSingleResult();
		return stockItemList;

	}

	// SELECT its.`item_total_qty` FROM `item_stock` its WHERE
	// its.`item_mst_id`='RM0024';
	@Override
	public List<Object> getTotalItemStockQtyByItemId(String itemId) {
		String hql = "SELECT its.itemTolQty FROM StockItem its WHERE its.itemMst.id=:itemId";
		List<Object> poNumList = entityManager.createQuery(hql).setParameter("itemId", itemId).getResultList();
		return poNumList;
	}

	@Override
	public List<StockItem> getMaterialStock() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from StockItem ",StockItem.class).getResultList();
	}

	

}
