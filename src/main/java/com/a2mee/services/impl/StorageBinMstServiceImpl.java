package com.a2mee.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.ItemLocMapDao;
import com.a2mee.dao.PlantMstDao;
import com.a2mee.dao.StorageBinMstDao;
import com.a2mee.dao.StorageDao;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.Plant;
import com.a2mee.model.Storage;
import com.a2mee.model.StorageBinMst;
import com.a2mee.services.StorageBinMstService;

@Service
public class StorageBinMstServiceImpl implements StorageBinMstService {
	
	@Autowired
	StorageBinMstDao storageBinMstDao;
	
	@Autowired
	PlantMstDao plantMstDao;
	
	@Autowired
	StorageDao storageDao;
	
	@Autowired
	ItemLocMapDao  itemLocMapDao;

	@Override
	public List<StorageBinMst> getStorageBinList() {
		return storageBinMstDao.getStorageBinList();
	}

	@Override
	public void addStorageBin(StorageBinMst storageBinMst) {
		storageBinMstDao.addStorageBin(storageBinMst);
	}

	@Override
	public void delStorageBin(long storageBinId) {
		storageBinMstDao.delStorageBin(storageBinId);
	}

	@Override
	public List<StorageBinMst> getstorageBinMstByCode(String storageBinCode) {
		return storageBinMstDao.getstorageBinMstByCode(storageBinCode);
	}

	@Override
	public void addPlant(Plant plant) {
		// TODO Auto-generated method stub
		plantMstDao.addPlant(plant);
	}

	@Override
	public List<Plant> getListPlant() {
		// TODO Auto-generated method stub
		return plantMstDao.getPlantList();
	}

	@Override
	public void deletePlant(Plant plant) {
		// TODO Auto-generated method stub
		plantMstDao.deletePlant(plant);
	}

	@Override
	public Storage addStorage(Storage storage) {
		// TODO Auto-generated method stub
		
		return storageDao.save(storage);
	}

	@Override
	public List<Storage> getListStorage() {
		// TODO Auto-generated method stub
		return storageDao.getListStorage();
	}

	@Override
	public void deleteStorage(Storage storage) {
		// TODO Auto-generated method stub
		storageDao.deleteStorage(storage);
	}

	@Override
	public List<Plant> getPlantByCode(String plantCode) {
		// TODO Auto-generated method stub
		return storageDao.getPlantByCode(plantCode);
	}

	@Override
	public List<Storage> getstorageByCode(String storageCode) {
		// TODO Auto-generated method stub
		return storageDao.getstorageByCode(storageCode);
	}

	@Override
	public List<StorageBinMst> getStorageBinByCode(String storageCode) {
		return storageDao.getStorageBinByCode(storageCode);
	}

	@Override
	public StorageBinMst findByStorageBinByCode(String storageBinCode) {
	//	System.out.println("storageBinCode :: "+storageBinCode);
		List<StorageBinMst> storageBinOptList = storageBinMstDao.getByStorageBinCode(storageBinCode);
		//Optional<StorageBinMst> storageBinOpt = storageBinMstDao.getByStorageBinCode(storageBinCode);
		
		if(storageBinOptList.size()==0) {
			return  null;
		}else {
			return  storageBinOptList.get(0);
		}
		
	}

	@Override
	public StorageBinMst add(StorageBinMst newStorageBin) {
		return storageBinMstDao.save(newStorageBin);
	}

	@Override
	public Storage getSingleStorageByCode(String storageCode) {
		// TODO Auto-generated method stub
		List<Storage> storages = storageDao.getstorageByCode(storageCode);
		
		if(storages.size()==0) {
			return  null;
		}else {
			return  storages.get(0);
		}
	}

	@Override
	public List<StorageBinMst> getStorageBinListByPagination(int pageno, int perPage) {
		// TODO Auto-generated method stub
		return storageBinMstDao.getStorageBinListByPagination(pageno,perPage);
	}

	@Override
	public List<ItemLocMap> getItemLocationMap(String storageBinCode) {
		// TODO Auto-generated method stub
		return itemLocMapDao.getItemLocationMap(storageBinCode);
	}
	
	

}
