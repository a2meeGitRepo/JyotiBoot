package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.StorageDao;
import com.a2mee.dao.StorageDaoCustom;
import com.a2mee.model.Plant;
import com.a2mee.model.Storage;
import com.a2mee.model.StorageBinMst;
@Transactional
@Repository
public class StorageDaoImpl implements StorageDaoCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Value(value = "${storageDao.getStorageList.hql}")
	private String storageDaoGetStorageListHql;
	
	@Value(value = "${storageDao.updateStorage.hql}")
	private String storageDaoUpdateStorageHql;
	
	@Override
	public Storage addStorage(Storage storage) {
		// TODO Auto-generated method stub
		entityManager.persist(storage);
		entityManager.flush();
		return storage;
	}

	@Override
	public List<Storage> getListStorage() {
		// TODO Auto-generated method stub
		List<Storage> storageList = entityManager.createQuery(storageDaoGetStorageListHql, Storage.class).getResultList();
		return storageList;
	}

	@Override
	public void deleteStorage(Storage storage) {
		// TODO Auto-generated method stub
		entityManager.createQuery(storageDaoUpdateStorageHql)
		.setParameter("storage_id",storage.getStorage_id())
		.executeUpdate();
	}

	@Override
	public List<Plant> getPlantByCode(String plantCode) {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from Plant where plant_code=:plantCode", Plant.class).setParameter("plantCode", plantCode).getResultList();
	}

	@Override
	public List<Storage> getstorageByCode(String storageCode) {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from Storage where storage_location=:storageCode", Storage.class).setParameter("storageCode", storageCode).getResultList();
	}

	@Override
	public List<StorageBinMst> getStorageBinByCode(String storageCode) {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from StorageBinMst where storageBinCode=:storageCode", StorageBinMst.class).setParameter("storageCode", storageCode).getResultList();
	}

	
}
