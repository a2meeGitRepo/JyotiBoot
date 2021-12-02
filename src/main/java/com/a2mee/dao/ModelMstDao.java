package com.a2mee.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.ModelMst;

@Repository
public interface ModelMstDao extends JpaRepository<ModelMst, Long> {
	@Query("From ModelMst m where m.modelCode=?1")
	Optional<ModelMst> findByModelCode(String modelCode);


}
