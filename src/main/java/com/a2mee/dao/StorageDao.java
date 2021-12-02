package com.a2mee.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a2mee.model.Plant;
import com.a2mee.model.Storage;
import com.a2mee.model.StorageBinMst;

public interface StorageDao extends JpaRepository<Storage, Long>, StorageDaoCustom {

	

}
