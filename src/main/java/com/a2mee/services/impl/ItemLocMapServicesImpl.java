package com.a2mee.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.ItemLocMapDao;
import com.a2mee.dao.StorageBinMstDao;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.ItemMst;
import com.a2mee.model.StorageBinMst;
import com.a2mee.services.ItemLocMapService;
import com.a2mee.services.StorageBinMstService;

@Service
public class ItemLocMapServicesImpl implements ItemLocMapService{
	
	@Autowired
	ItemLocMapDao itemLocMapDao;
	
	@Autowired
	StorageBinMstService storageBinMstService;
	
	@Autowired
	StorageBinMstDao storageBinMstDao;

	@Override
	public void saveitemLocMap(ItemLocMap theItemLocMap) {
		itemLocMapDao.saveitemLocMap(theItemLocMap);
	}

	@Override
	public List<ItemLocMap> getItemLocByStorageCode(long storageBinId) {
		return itemLocMapDao.getItemLocByStorageCode(storageBinId);
	}

	@Override
	public void delItemLocMap(long itemLocMapId) {
		itemLocMapDao.delItemLocMap(itemLocMapId);
	}

	@Override
	public List<ItemLocMap> searchByItemIdStorageId(String itemMstId, long storageBinId) {
		return itemLocMapDao.searchByItemIdStorageId(itemMstId, storageBinId);
	}

	@Override
	public List<ItemLocMap> getItemLocMap() {
		return itemLocMapDao.getItemLocMap();
	}

	@Override
	public List<ItemLocMap> getItemLocByCodeAndItem(String itemId, long storageBinId) {
		return itemLocMapDao.searchByItemIdStorageId(itemId, storageBinId);
	}

	@Override
	public List<ItemLocMap> searchByItemIdStorageCode(String itemMstId, String storageBinCode) {
		return itemLocMapDao.searchByItemIdStorageCode(itemMstId, storageBinCode);
	}

	@Override
	public ItemLocMap findByItemAndStorage(ItemMst item, StorageBinMst storageBin) {
		Optional<ItemLocMap> itemLocMapOpt = itemLocMapDao.findByItemMstAndStorageBinMst(item, storageBin);
		return itemLocMapOpt.isPresent()? itemLocMapOpt.get() : null;
	}

	@Override
	public ItemLocMap add(ItemLocMap newItemLoc) {
		return itemLocMapDao.save(newItemLoc);
	}

	@Override
	public Set<StorageBinMst> getAssingStorageLocByItem(String itemId) {
		// TODO Auto-generated method stub
		Set<StorageBinMst> storages=new HashSet<StorageBinMst>();
		List<ItemLocMap> list=itemLocMapDao.getAssingStorageLocByItem(itemId);
		
		for (ItemLocMap itemLocMap:list){
			storages.add(itemLocMap.getStorageBinMst());
		}
		return storages;
	}
	
}
