package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.ProductionOrder;

@Repository
public interface ProOrderDao extends JpaRepository<ProductionOrder, Long> ,ProductionOrderRepoCustome{

	@Query("select DISTINCT(p.salesOrdNo) from ProductionOrder p where p.isApproved=0")
	List<Long> getSalesOrders();

	List<ProductionOrder> findByProdOrdNo(long proOrderNo);

	List<ProductionOrder> findBySalesOrdNo(long salesOrder);

	@Transactional
	@Modifying
	@Query("UPDATE ProductionOrder p SET p.isApproved=1 WHERE p.prodOrdId=?1")
	void updateApproval(long prodOrdId);

	@Transactional
	@Modifying
	@Query("UPDATE ProductionOrder p SET p.isApproved=2 WHERE p.prodOrdNo=?1")
	void updateCustomApproval(long proOrderNo);

	@Query("from ProductionOrder p where p.isApproved!=0")
	List<ProductionOrder> getApprovedPo();
	
	@Query("select p.prodOrdNo from ProductionOrder p where p.modelCode=?1 and NOT EXISTS (SELECT mp.proOrdNo FROM ModelPlan mp where mp.modelCode=?1 and mp.proOrdNo=p.prodOrdNo)")
	List<Long> getProOrdNo(String modelCode);
	
	@Query("from ProductionOrder p where p.prodOrdNo=?1")
	Optional<ProductionOrder> getProductionOrderByPONO(long prodOrdNo);


}
