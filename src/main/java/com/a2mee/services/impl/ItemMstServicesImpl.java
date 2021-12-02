package com.a2mee.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.ItemMstDao;
import com.a2mee.dao.PendingReportRepo;
import com.a2mee.model.dto.ItemMstDto;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.ItemMst;
import com.a2mee.model.PendingReport;
import com.a2mee.services.ItemMstServices;

@Service
public class ItemMstServicesImpl implements ItemMstServices {
	
	@Autowired
	ItemMstDao itemMstDao;
	@Autowired
	PendingReportRepo pendingReportRepo;

	/*for GRN barcode generation page*/
	@Override
	public List<MasterDto> getItemList(String venId) {
		List<Object[]> vList = itemMstDao.getItemList(venId);
		System.out.println("size in service=" + vList.size());
		List<MasterDto> vmdList = new ArrayList<MasterDto>();
		vList.forEach(vmArray -> {
			MasterDto vmd  =new MasterDto();
			vmd.setId((String)vmArray[0]);
			vmd.setName((String)vmArray[1]);
			vmd.setVendorNo((String)vmArray[2]);
			vmd.setContactPerson((String)vmArray[3]);
			vmdList.add(vmd);
		});
		return vmdList;
	}
	
	@Override
	public List<MasterDto> getItemGrnlist(String venId) {
		List<Object[]> vList = itemMstDao.getItemGrnlist(venId);
		System.out.println("size in service=" + vList.size());
		List<MasterDto> vmdList = new ArrayList<MasterDto>();
		vList.forEach(vmArray -> {
			MasterDto vmd  =new MasterDto();
			vmd.setId((String)vmArray[0]);
			vmd.setName((String)vmArray[1]);
			vmd.setVendorNo((String)vmArray[2]);
			vmd.setContactPerson((String)vmArray[3]);
			vmdList.add(vmd);
		});
		return vmdList;
	}

	/*for production order*/
	public List<MasterDto> getProOrdItemList(LocalDate startDate, LocalDate endDate) {
		List<Object[]> poList = itemMstDao.getProOrdItemList(startDate,  endDate);
		System.out.println("size in service=" + poList.size());
		List<MasterDto> vmdList = new ArrayList<MasterDto>();
		poList.forEach(vmArray -> {
			MasterDto vmd  =new MasterDto();
			vmd.setId((String)vmArray[1]);
			vmd.setName((String)vmArray[0]);
			vmdList.add(vmd);
		});
		return vmdList;
	}

	@Override
	public List<ItemMst> getItemByCode(String materialCode) {
		// TODO Auto-generated method stub
		return itemMstDao.getItemByCode(materialCode);
	}

	@Override
	public void saveItem(ItemMst itemMst) {
		itemMstDao.saveItem(itemMst);
	}

	@Override
	public List<ItemMstDto> getItemList() {
		List<Object[]> itemList = itemMstDao.getItemList();
		System.out.println("OBJECT SIZE :: "+itemList.size());
		List<ItemMstDto> vmdList = new ArrayList<ItemMstDto>();
		try {
			itemList.forEach(vmArray -> {
				ItemMstDto vmd  = new ItemMstDto();
				
				if((String)vmArray[0]!=null){
					vmd.setItemMstId((String)vmArray[0]);
				}else{
					vmd.setItemMstId("");
				}
				
				if((String)vmArray[1]!=null){
					vmd.setItemDtl((String)vmArray[1]);
				}else{
					vmd.setItemDtl("");
				}
				if((String)vmArray[2]!=null){
					vmd.setUom((String)vmArray[2]);
				}else{
					vmd.setUom("");
				}
				
				if((Long)vmArray[3]!=null){
					String mtlGroup=(vmArray[3]).toString();
					vmd.setMtlGroup(mtlGroup);
				}else{
					vmd.setMtlGroup("");
				}
				if((String)vmArray[4]!=null){
					vmd.setMaterialSource((String)vmArray[4]);
				}else{
					vmd.setMaterialSource("");
				}
				if((String)vmArray[5]!=null){
					vmd.setScmMode((String)vmArray[5]);
				}else{
					vmd.setScmMode("");
				}
				if((String)vmArray[6]!=null){
					vmd.setMatMstLog((String)vmArray[6]);
				}else{
					vmd.setMatMstLog("");
				}
				
				
				
			
				vmdList.add(vmd);
				
				
				
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return vmdList;
	}

	@Override
	public List<ItemMst> getItemById(String itemMstId) {
		return itemMstDao.getItemById(itemMstId);
	}

	@Override
	public List<ItemMst> getEntireItemMst() {
		// TODO Auto-generated method stub
		return itemMstDao.getEntireItemMst();
	}

	@Override
	public ItemMst findById(String itemId) {
		List<ItemMst> item = getItemById(itemId);
		if(item.size() == 0)
			return null;

		return item.get(0);
	}

	@Override
	public ItemMst add(ItemMst newItem) {
		itemMstDao.saveItem(newItem);
		return newItem;
	}

	@Override
	public void savePendingReport(List<PendingReport> pendings) {
		// TODO Auto-generated method stub
		pendingReportRepo.saveAll(pendings);
	}

	@Override
	public void savePendingReport1(PendingReport pendingDto) {
		// TODO Auto-generated method stub
		System.out.println("===================PENDING SAVRD =================");
		pendingReportRepo.save(pendingDto);
	}


}
