package com.a2mee.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.a2mee.model.PickingAssembly;
import com.a2mee.model.PickingMst;
import com.a2mee.model.dto.PickingDto;

@Repository
public interface PickingAssemblyDao extends JpaRepository<PickingAssembly, Long>{

	@Query("from PickingAssembly p where p.pickingMst.employee.empCode=?1 and p.pickingMst.month=?2 and p.pickingMst.year=?3 and p.status=1")
	List<PickingAssembly> getAssemblyByPicker(String userName, String month, String year);

	@Query("from PickingAssembly p where p.pickingMst.employee.empCode=?1 and p.pickingMst.month=?2 and p.pickingMst.year=?3")
	List<PickingAssembly> getAssms(String userName, String month, String year);

	List<PickingAssembly> findByStatus(int status);

	@Query("from PickingAssembly p where p.assemblyMst.assemblyCode=?1")
	List<PickingAssembly> getAssmsByAssmCode(String assemblyCode);

	@Query("from PickingAssembly p where p.status=1 and p.remQty!=0")
	List<PickingAssembly> getKitStock();

	@Query("from PickingAssembly p where p.pickingMst.pickingId=?1")
	List<PickingAssembly> getByPickingId(Long pickingId);

	@Query("from PickingAssembly p where p.pickingMst.pickingId=?1 and p.pickingMst.modelCode=?2 and p.pickingMst.proOrdNo=?3 and (p.status=0 or p.status=2)")
	List<PickingAssembly> getPickingByModelPo(long pickingId, String modelCode, String prodOrdNo);

	@Query("from PickingAssembly p where p.pickingMst=?1 and (p.status=0 or p.status=2)")
	List<PickingAssembly> getAssemblyByPickingAndStatus(PickingMst pickingMst);
	@Query("from PickingAssembly p where p.pickingMst.employee.empCode=?1 ")
	List<PickingAssembly> getAssmsByUsername(String userName);
	@Query(nativeQuery =true,value = "SELECT * from picking_assembly p where p.picking_id  IN (:modelPos)")
	List<PickingAssembly> getPickingByModelPoByArray(@Param("modelPos")long [] modelPos);

}
