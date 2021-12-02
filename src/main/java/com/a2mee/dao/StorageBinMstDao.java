package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.StorageBinMst;

@Repository
public interface StorageBinMstDao extends JpaRepository<StorageBinMst, Long>, StorageBinMstDaoCustom {

	@Query("from StorageBinMst where storageBinCode LIKE (CONCAT('%',?1,'%'))")
	List<StorageBinMst> getByStorageBinCode(String storageBinCode);


}
