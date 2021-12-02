package com.a2mee.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a2mee.model.ProductionOrder;

public interface ProductionOrderRepo extends JpaRepository<ProductionOrder, Integer>{
	@Query("from ProductionOrder p where p.prodOrdNo=?1")
	ProductionOrder getPorductionOrder(long proOrdNo);

}
