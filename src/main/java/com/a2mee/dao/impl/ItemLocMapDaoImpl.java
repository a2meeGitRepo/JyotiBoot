package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.a2mee.dao.ItemLocMapDao;
import com.a2mee.dao.ItemLocMapDaoCustom;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.StorageBinMst;

@Repository
@Transactional
public class ItemLocMapDaoImpl implements ItemLocMapDaoCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void saveitemLocMap(ItemLocMap theItemLocMap) {
		entityManager.merge(theItemLocMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemLocMap> getItemLocByStorageCode(long storageBinId) {
		return entityManager.createQuery("from ItemLocMap where storageBinMst.storageBinId=:storageBinId",ItemLocMap.class).setParameter("storageBinId",storageBinId).getResultList();
	}

	@Override
	public void delItemLocMap(long itemLocMapId) {
		Query query = entityManager.createQuery("DELETE FROM ItemLocMap WHERE itemLocMapId= :itemLocMapId");
		query.setParameter("itemLocMapId", itemLocMapId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemLocMap> searchByItemIdStorageId(String itemMstId, long storageBinId) {
		return entityManager.createQuery("FROM ItemLocMap WHERE storageBinMst.storageBinId=:storageBinId AND itemMst.id=:itemMstId")
				.setParameter("storageBinId",storageBinId)
				.setParameter("itemMstId",itemMstId)
				.getResultList();
	}

	@Override
	public List<ItemLocMap> getItemLocMap() {
		return entityManager.createQuery("From ItemLocMap", ItemLocMap.class).getResultList();
	}

	@Override
	public List<ItemLocMap> searchByItemIdStorageCode(String itemMstId, String storageBinCode) {
		return entityManager.createQuery("FROM ItemLocMap i WHERE storageBinMst.storageBinCode=:storageBinCode AND itemMst.id=:itemMstId")
				.setParameter("storageBinCode",storageBinCode)
				.setParameter("itemMstId",itemMstId)
				.getResultList();
	}
	
	
}
