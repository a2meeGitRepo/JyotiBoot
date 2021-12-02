package com.a2mee.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a2mee.model.SupplierComponants;

public interface SupplierComponantRepo extends JpaRepository<SupplierComponants, Integer> {
	@Query("From SupplierComponants s where s.supplier.supplierId=?1")
	List<SupplierComponants> getSupplierComponantsBySupplier(long supplierId);
	@Query("From SupplierComponants s where s.itemMst.id=?1")
	List<SupplierComponants> getSupplierComponantsByItem(String itemId);

}
