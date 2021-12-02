package com.a2mee.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.CustomAssembly;

@Repository
public interface CustomAssmDao extends JpaRepository<CustomAssembly, Long>{

	@Query("from CustomAssembly c where c.model.modelCode=?1 and c.productionOrder.prodOrdNo=?2")
	List<CustomAssembly> getCustomAssembly(String modelCode, long prodOrdNo);

}
