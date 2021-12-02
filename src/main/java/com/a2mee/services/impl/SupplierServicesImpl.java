package com.a2mee.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.SupplierComponantRepo;
import com.a2mee.dao.SupplierRepo;
import com.a2mee.model.Supplier;
import com.a2mee.model.SupplierComponants;
import com.a2mee.services.SupplierServices;
@Service
public class SupplierServicesImpl implements SupplierServices{
	@Autowired
	SupplierRepo supplierRepo;
	@Autowired
	SupplierComponantRepo supplierComponantRepo;
	@Override
	public List<Supplier> getSupplierList() {
		// TODO Auto-generated method stub
		return supplierRepo.findAll();
	}

	@Override
	public List<SupplierComponants> getSupplierComponantsBySupplier(long supplierId) {
		// TODO Auto-generated method stub
		return supplierComponantRepo.getSupplierComponantsBySupplier(supplierId);
	}

	@Override
	public void addSupplierComponant(SupplierComponants supplierComponants) {
		// TODO Auto-generated method stub
		supplierComponantRepo.save(supplierComponants);
	}

	@Override
	public List<SupplierComponants> getSupplierComponantsByItem(String itemId) {
		// TODO Auto-generated method stub
		return supplierComponantRepo.getSupplierComponantsByItem(itemId);
	}

	@Override
	public Supplier getSupplierComponantsBySupplierCode(String supplierCode) {
		// TODO Auto-generated method stub
		Optional<Supplier> optional=supplierRepo.findBySupplierCode(supplierCode);
		return optional.isPresent()?optional.get():null;
	}

	@Override
	public void saveSupplier(Supplier supplier2) {
		// TODO Auto-generated method stub
		supplierRepo.save(supplier2);
	}

	@Override
	public List<SupplierComponants> getsupplierByComponantCode(String compCode) {
		// TODO Auto-generated method stub
		return supplierComponantRepo.getSupplierComponantsByItem(compCode);
	}

}
