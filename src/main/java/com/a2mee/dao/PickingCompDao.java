package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.a2mee.model.ComponentMst;
import com.a2mee.model.PickingAssembly;
import com.a2mee.model.PickingComponent;

@Repository
public interface PickingCompDao extends JpaRepository<PickingComponent, Long>{

	@Query("from PickingComponent pc where pc.pickingMst.employee.empCode=?1 and pc.pickingMst.month=?2 and pc.pickingMst.year=?3 and pc.pickingAssembly.assemblyMst.assemblyCode=?4")
	List<PickingComponent> getCompsByAssm(String userName, String month, String year, String assemblyCode);

	@Query("from PickingComponent pc where pc.pickingAssembly=?1 and pc.status!=1")
	List<PickingComponent> getCompsByAssmAndStatus(PickingAssembly pickAssembly);

	@Query("from PickingComponent pc where pc.pickingMst.pickingId=?1 and pc.pickingMst.modelCode=?2 and pc.pickingMst.proOrdNo=?3 and pc.pickingAssembly.assemblyMst.assemblyCode=?4 and pc.itemBit=1")
	List<PickingComponent> getPickingByAssmPo(long pickingId, String modelCode, String prodOrdNo,
			String assemblyCode);

	@Query("from PickingComponent pc where pc.pickingMst.modelPlan.modelPlanId=?1 and pc.pickingMst.modelCode=?2 and pc.pickingMst.proOrdNo=?3 and pc.pickingAssembly.assemblyMst.assemblyCode=?4 and pc.componentMst.compCode=?5 and pc.status!=1")
	List<PickingComponent> getCompsByPO(long modelPlanId, String modelCode, String prodOrdNo,String assemblyCode, String compCode);

	@Query("from PickingComponent pc where pc.pickingAssembly.pickingAssmId=?1 and (pc.status=0 or pc.status=2)")
	List<PickingComponent> getPickingComponentByPickingAssmAndStatus(long pickingAssmId);

	@Query("from PickingComponent pc where pc.pickingMst.pickingId=?1 and pc.pickingAssembly.assemblyMst.assemblyCode=?2 and pc.componentMst.compCode=?3 and (pc.status=0 or pc.status=2)")
	List<PickingComponent> getPickingComponentByPickingAndAssemblyAndComponent(long pickingId, String assemblyCode,
			String compCode);
	
	@Query("from PickingComponent pc where pc.pickingCompId=?1 and pc.status=0")
	List<PickingComponent> getPickingByCompId(Long pickingCompId);

	@Query("from PickingComponent pc where pc.pickingAssembly.pickingAssmId=?1")
	List<PickingComponent> getPickingComponentByPickingAssembly(long pickingAssmId);

	@Query("from PickingComponent pc where pc.pickingMst.pickingId=?1 and pc.componentMst.compCode=?2 and pc.pickCompQty=?3 and pc.itemNo=0")
	Optional<PickingComponent> getPickingComponentByModelPlanAndComponent(Long pickingId, String itemId,double qty);

	@Query("from PickingComponent pc where pc.pickingMst.pickingId=?1 and pc.componentMst.compCode=?2 and pc.itemBit=0")
	List<PickingComponent> getPickingComponentsByModelPlanAndComponent(Long pickingId, String itemId);

	@Query("from PickingComponent pc where pc.componentMst.compCode=?1 and pc.pickedDate!=null")
	List<PickingComponent> getPickingComponentByItemDateNull(String itemMstId);

	@Query("from PickingComponent pc where pc.componentMst.compCode=?1 and pc.pickedDate!=null and DATE(pc.pickedDate) BETWEEN DATE(?2) AND DATE(?3)")
	List<PickingComponent> getPickingComponentByItemDateNullDateRange(String itemMstId, String startDate,String endDate);
	
	@Query("from PickingComponent pc where pc.pickingMst.pickingId IN (:pickingIds) and pc.pickingAssembly.assemblyMst.assemblyCode IN (:assemblyCodes) and pc.itemBit=1")
	List<PickingComponent> getPickingComByAssmPoAndPickId(@Param("pickingIds")List<Long> pickingIds, @Param("assemblyCodes")List<String> assemblyCodes);
	//@Query("from PickingComponent pc where pc.pickingMst.pickingId IN (:pickingIds) and pc.componentMst.compCode=(:comCode)and pc.itemBit=1")
	//List<PickingComponent> getPickingComByCompAndPickId(@Param("pickingIds")List<Long> pickingIds, @Param("comCode")String comCode, List<String> list);
	@Query("from PickingComponent pc where pc.componentMst.compCode=?1 and pc.pickingMst.proOrdNo=?2")
	List<PickingComponent> getPickingComponantByComponentAndPO(String compCode, String proOrdNo);
	@Query("from PickingComponent pc where pc.pickingMst.pickingId =?1 and pc.pickingAssembly.assemblyMst.assemblyCode=?2 and pc.itemBit=1")
	List<PickingComponent> getPickingAssemblyCodeByCompAndPickId(long id, String assemblyCode);
	@Query("from PickingComponent pc where pc.pickingMst.pickingId IN (:pickingIds)and pc.pickingAssembly.assemblyMst.assemblyCode = (:assemblyCodes) and pc.componentMst.compCode=(:comCode)and pc.itemBit=1")
	List<PickingComponent> getPickingComByCompAndPickId(@Param("pickingIds")List<Long> pickingIds, @Param("comCode") String comCode,
			@Param("assemblyCodes") String assemblyCodes);
	@Query("from PickingComponent pc where pc.pickingMst.pickingId=?1 and pc.componentMst.compCode=?2 and pc.componentMst.assembly.assemblyCode=?3 and pc.itemBit=0")
	List<PickingComponent> getPickingComponentsByModelPlanAndComponentAndAssemblyCode(Long pickingId, String itemId,
			String assmCode);
	@Query("from PickingComponent pc where pc.pickingMst.pickingId=?1 and pc.componentMst.compCode=?2 and pc.pickCompQty=?3 and pc.itemBit=0")
	List<PickingComponent> getPickingComponentsByModelPlanAndComponentAndQTY(Long pickingId, String itemId, double qty);

}
