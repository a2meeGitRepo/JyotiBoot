package com.a2mee.services;

import java.util.List;
import java.util.Set;

import com.a2mee.model.ItemLocMap;
import com.a2mee.model.ItemMst;
import com.a2mee.model.StorageBinMst;

public interface ItemLocMapService {

	void saveitemLocMap(ItemLocMap theItemLocMap);

	List<ItemLocMap> getItemLocByStorageCode(long storageBinId);

	void delItemLocMap(long itemLocMapId);

	List<ItemLocMap> searchByItemIdStorageId(String itemMstId, long storageBinId);

	List<ItemLocMap> getItemLocMap();

	List<ItemLocMap> getItemLocByCodeAndItem(String itemId, long storageBinId);

	List<ItemLocMap> searchByItemIdStorageCode(String itemMstId, String storageBinCode);

	ItemLocMap findByItemAndStorage(ItemMst item, StorageBinMst storageBin);

	ItemLocMap add(ItemLocMap newItemLoc);

	Set<StorageBinMst> getAssingStorageLocByItem(String itemId);

}
