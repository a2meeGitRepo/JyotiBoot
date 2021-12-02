package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.ItemLocMap;
import com.a2mee.model.ItemMst;
import com.a2mee.model.StorageBinMst;

@Repository
public interface ItemLocMapDao extends JpaRepository<ItemLocMap, Long>, ItemLocMapDaoCustom {

	Optional<ItemLocMap> findByItemMstAndStorageBinMst(ItemMst item, StorageBinMst storageBin);
	@Query("From ItemLocMap i where i.itemMst.id=?1")
	List<ItemLocMap> getAssingStorageLocByItem(String itemId);
	
	
	@Query("From ItemLocMap i where i.storageBinMst.storageBinCode=?1")
	List<ItemLocMap> getItemLocationMap(String storageBinCode);
	

}
