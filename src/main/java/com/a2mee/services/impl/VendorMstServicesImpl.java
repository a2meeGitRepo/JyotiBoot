package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.VendorMstDao;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.VendorMst;
import com.a2mee.services.VendorMstServices;

@Service
public class VendorMstServicesImpl implements VendorMstServices {

	@Autowired
	VendorMstDao vendorMstDao;

	@Override
	public List<MasterDto> getvenList() {
		List<Object[]> vList = vendorMstDao.getVenList();
		System.out.println("size in service=" + vList.size());
		List<MasterDto> vmdList = new ArrayList<MasterDto>();
		vList.forEach(vmArray -> {
			MasterDto vmd  =new MasterDto();
			vmd.setId((String)vmArray[0]);
			vmd.setName((String)vmArray[1]);
			vmdList.add(vmd);
		});
		return vmdList;
	}

	@Override
	public List<VendorMst> getVenByCode(String vendorCode) {
		// TODO Auto-generated method stub
		return vendorMstDao.getVenByCode(vendorCode);
	}

	@Override
	public void saveVen(VendorMst vendorMst) {
		// TODO Auto-generated method stub
		vendorMstDao.saveVen(vendorMst);
	}

	@Override
	public List<MasterDto> getVenGrnList() {
		List<Object[]> vList = vendorMstDao.getVenGrnList();
		System.out.println("size in service=" + vList.size());
		List<MasterDto> vmdList = new ArrayList<MasterDto>();
		vList.forEach(vmArray -> {
			MasterDto vmd  =new MasterDto();
			vmd.setId((String)vmArray[0]);
			vmd.setName((String)vmArray[1]);
			vmdList.add(vmd);
		});
		return vmdList;
	}

	@Override
	public List<VendorMst> getEntireVenMst() {
		// TODO Auto-generated method stub
		return vendorMstDao.getEntireVenMst();
	}
}
