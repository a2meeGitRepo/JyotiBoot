package com.a2mee.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.ModelPlan;
import com.a2mee.model.PickingMst;

@Repository
public interface PickingDao extends JpaRepository<PickingMst, Long> {

	@Query("from PickingMst p where p.modelPlan.modelPlanId=?1 and p.modelCode=?2 and p.proOrdNo=?3")
	PickingMst getPickingByModelPo(long modelPlanId, String modelCode, String prodOrdNo);

	@Query("from PickingMst p where p.modelCode=?1")
	List<PickingMst> getProOrdersByModel(String modelCode);

	@Query("from PickingMst p where p.proOrdNo=?1")
	List<PickingMst> getPickingByPrdOrdNo(String proOrderNo);

	@Query("from PickingMst p where p.modelCode=?1")
	List<PickingMst> getPickingByModel(String modelCode);

//	@Query("Select p from PickingMst p, PickingComponent pc where pc.componentMst.compCode=?2 and pc.pickingAssembly.assemblyMst.assemblyCode=?1 and pc.pickingMst.pickingId=p.pickingId and (pc.status=0 or pc.status=2)")
//	List<PickingMst> getPickingByAssemblyAndComp(String assemblyCode, String compCode);
	
	@Query("Select pc.pickingCompId, pc.pickCompQty, p.modelCode, p.proOrdNo, pc.status from PickingMst p, PickingComponent pc where pc.componentMst.compCode=?2 and pc.pickingAssembly.assemblyMst.assemblyCode=?1 and pc.pickingMst.pickingId=p.pickingId and pc.itemBit=1 and (pc.status=0 or pc.status=2)")
	List<Object[]> getPickingByAssemblyAndComp(String assemblyCode, String compCode);

	@Transactional
	@Modifying
	@Query("UPDATE PickingMst p SET p.status=4 WHERE p.pickingId=?1")
	void updateApproval(long pickingId);

	@Query("Select DISTINCT(p.modelCode) from PickingMst p")
	List<String> getModelsInPicking();

	@Query("from PickingMst p WHERE p.modelCode=?1")
	List<PickingMst> getPOPickingByModel(String modelCode);


}
