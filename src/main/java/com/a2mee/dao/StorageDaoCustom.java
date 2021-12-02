package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.Plant;
import com.a2mee.model.Storage;
import com.a2mee.model.StorageBinMst;

public interface StorageDaoCustom {

	Storage addStorage(Storage storage);

	List<Storage> getListStorage();

	void deleteStorage(Storage storage);

	List<Plant> getPlantByCode(String plantCode);

	List<Storage> getstorageByCode(String storageCode);

	List<StorageBinMst> getStorageBinByCode(String storageCode);
}
