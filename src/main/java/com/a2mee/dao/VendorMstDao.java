package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.VendorMst;
import com.a2mee.model.dto.MasterDto;

public interface VendorMstDao {
	public List<Object[]> getVenList();

	public List<VendorMst> getVenByCode(String vendorCode);

	public void saveVen(VendorMst vendorMst);

	public List<Object[]> getVenGrnList();

	public List<VendorMst> getEntireVenMst();
}
