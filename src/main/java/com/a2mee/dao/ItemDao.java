package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a2mee.model.ItemMst;

public interface ItemDao extends JpaRepository<ItemMst, Integer> ,ItemDaoCustome{
	@Query("from ItemMst i where i.id=?1")
	Optional<ItemMst> findByItemId(String  itemCode);


	

}
