package com.a2mee.services;

import java.util.List;

import com.a2mee.model.Supplier;
import com.a2mee.model.SupplierComponants;

public interface SupplierServices {

	List<Supplier> getSupplierList();

	List<SupplierComponants> getSupplierComponantsBySupplier(long supplierId);

	void addSupplierComponant(SupplierComponants supplierComponants);

	List<SupplierComponants> getSupplierComponantsByItem(String itemId);

	Supplier getSupplierComponantsBySupplierCode(String supplierCode);

	void saveSupplier(Supplier supplier2);

	List<SupplierComponants> getsupplierByComponantCode(String compCode);

}
