package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.ItemLocMap;

public interface ItemLocMapDaoCustom {


	void saveitemLocMap(ItemLocMap theItemLocMap);

	List<ItemLocMap> getItemLocByStorageCode(long storageBinId);

	void delItemLocMap(long storageBinId);

	List<ItemLocMap> searchByItemIdStorageId(String itemMstId, long storageBinId);

	List<ItemLocMap> getItemLocMap();

	List<ItemLocMap> searchByItemIdStorageCode(String itemMstId, String storageBinCode);
}
