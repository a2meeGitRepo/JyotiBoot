package com.a2mee.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a2mee.model.Supplier;

public interface SupplierRepo  extends JpaRepository<Supplier, Integer>{
	@Query("From Supplier s where s.supplierCode=?1")
	Optional<Supplier> findBySupplierCode(String supplierCode);

}
