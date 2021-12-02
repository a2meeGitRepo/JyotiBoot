package com.a2mee.services;

import java.util.List;

import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.VendorMst;

public interface VendorMstServices {
	public List<MasterDto> getvenList();

	public List<VendorMst> getVenByCode(String vendorCode);
	public void saveVen(VendorMst vendorMst);

	public List<MasterDto> getVenGrnList();

	public List<VendorMst> getEntireVenMst();
}
