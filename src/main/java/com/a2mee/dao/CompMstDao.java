package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;

@Repository
public interface CompMstDao extends JpaRepository<ComponentMst, Long> {

	@Query("from ComponentMst c where c.assembly.assemblyCode=?1 and compCode=?2 and c.assembly.model.modelCode=?3")
	Optional<ComponentMst> getByCodes(String assmCode, String compCode, String modelCode);

	List<ComponentMst> findByAssembly(AssemblyMst assembly);
	
	@Query("from ComponentMst c where compCode=?1 and c.assembly.model.modelCode=?2")
	Optional<ComponentMst> getByModelCode(String compCode, String modelCode);

	@Query("select c from ComponentMst c where c.assembly.model.modelCode=?1")
	List<ComponentMst> findByModelCode(String modelCode);

	@Query("select c from ComponentMst c where c.assembly.assemblyCode=?1")
	List<ComponentMst> getComponentByAssembly(String  assemblyCode);
	@Query("from ComponentMst c where c.compCode=?1 and c.assembly.model.modelCode=?2")
	Optional<ComponentMst> findByCode(String compCode,String modelCode);
	
	@Query("select c from ComponentMst c where c.assembly.assmblyId=?1")
	List<ComponentMst> getComponentByAssemblyID(Long assmblyId);
	
	@Query("select c from ComponentMst c where c.compCode=?1")
	List<ComponentMst> getComponentByAssemblyID(String compCode);

}
