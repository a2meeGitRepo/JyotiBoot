package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ModelMst;

@Repository
public interface AssemblyMstDao extends JpaRepository<AssemblyMst, Long> {

	@Query("from AssemblyMst a where a.assemblyCode=?1 and a.model.modelCode=?2")
	Optional<AssemblyMst> getByCodes(String assmCode, String modelCode);
	
	
	@Query("from AssemblyMst a where a.assemblyCode=?1 and a.model.modelCode=?2")
	Optional<AssemblyMst> findByAssemblyCodeAndModelModelCode(String assmCode, String modelCode);

	List<AssemblyMst> findByModel(ModelMst theModel);

	@Query("select a from AssemblyMst a where a.model.modelCode=?1")
	List<AssemblyMst> getByModelCode(String modelCode);

	@Query("from AssemblyMst a where a.model.modelCode=?1")
	List<AssemblyMst> getassemblyByCode(String modelCode);

	@Query("from AssemblyMst a where a.model.modelId=?1")
	List<AssemblyMst> getAssemblyByModelId(Long modelId);

}
