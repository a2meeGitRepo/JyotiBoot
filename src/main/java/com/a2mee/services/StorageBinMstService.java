package com.a2mee.services;

import java.util.List;

import com.a2mee.model.ItemLocMap;
import com.a2mee.model.ItemMst;
import com.a2mee.model.Plant;
import com.a2mee.model.Storage;
import com.a2mee.model.StorageBinMst;

public interface StorageBinMstService {

	List<StorageBinMst> getStorageBinList();

	void addStorageBin(StorageBinMst storageBinMst);

	void delStorageBin(long storageBinId);

	List<StorageBinMst> getstorageBinMstByCode(String storageBinCode);

	void addPlant(Plant plant);

	List<Plant> getListPlant();

	void deletePlant(Plant plant);

	Storage addStorage(Storage storage);

	List<Storage> getListStorage();

	void deleteStorage(Storage storage);

	List<Plant> getPlantByCode(String plantCode);

	List<Storage> getstorageByCode(String storageCode);

	List<StorageBinMst> getStorageBinByCode(String storageCode);

	StorageBinMst findByStorageBinByCode(String storageBinCode);

	StorageBinMst add(StorageBinMst newStorageBin);

	Storage getSingleStorageByCode(String storageCode);

	List<StorageBinMst> getStorageBinListByPagination(int pageno, int perPage);

	List<ItemLocMap> getItemLocationMap(String storageBinCode);

}
