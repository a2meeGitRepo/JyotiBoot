package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.EmployeeMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.dto.ModelPoDto;

@Repository
public interface ModelPlanDao extends JpaRepository<ModelPlan, Long> {

	@Query("from ModelPlan m where m.modelCode=?1 and m.month=?2 and m.year=?3")
	Optional<List<ModelPlan>> getModelPlanByFields(String modelCode, String month, String year);

	@Query("select m from ModelPlan m where m.month=?1 and m.year=?2")
	List<ModelPlan> getModelByMonth(String month, String year);

	@Query("from ModelPlan m where m.modelCode=?1 and m.month=?2 and m.year=?3")
	List<ModelPlan> getModelPlansByFields(String modelCode, String month, String year);

	@Transactional
	@Modifying
	@Query("update ModelPlan m set m.proOrdNo=?2 , m.employee.empId=?3 where m.modelPlanId=?1")
	void updateModelPlan(Long modelPlanId, String proOrdNo, long empId);

	@Query("from ModelPlan m where m.modelCode=?1 and m.proOrdNo!=null")
	List<ModelPlan> getProOrdersByModel(String modelCode);

	@Query("from ModelPlan m where m.proOrdNo=?1")
	ModelPlan getModelPlanByProOrdNo(String proOrdNo);

	

	
}
