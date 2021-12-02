package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.MaterialInspectDao;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.dto.AcceptedMatDto;
import com.a2mee.model.dto.ItemMstDto;
import com.a2mee.model.dto.StockDto;
import com.a2mee.services.MaterialInspectServices;

@Service
public class MaterialInspectServicesImpl implements MaterialInspectServices {
	
	@Autowired
	MaterialInspectDao materialInspectDao;

	@Override
	public boolean storeItem(long grnNo, String storageBinCode) {
		return materialInspectDao.storeItem(grnNo, storageBinCode);
	}

	@Override
	public List<AcceptedMatDto> getAcceptedList() {
		List<Object[]> matList = materialInspectDao.getAcceptedList();
		System.out.println("size in service=" + matList.size());
		List<AcceptedMatDto> vmdList = new ArrayList<AcceptedMatDto>();
		matList.forEach(vmArray -> {
			AcceptedMatDto vmd  = new AcceptedMatDto();
			vmd.setGrnNo((long)vmArray[0]);
			vmd.setGrnEntryDate((Date)vmArray[1]);
			vmd.setItemDetails((String)vmArray[2]);
			vmd.setQrCode((String)vmArray[3]);
			vmd.setItemId((String)vmArray[4]);
			vmdList.add(vmd);
		});
		return vmdList;
	}

	@Override
	public List<StockDto> getStockList(String storageBinCode) {
		// TODO Auto-generated method stub
		List<Object[]> matList = materialInspectDao.getStockList(storageBinCode);
		List<StockDto> stockList = new ArrayList<StockDto>();
		matList.forEach(vmArray -> {
			StockDto stock = new StockDto();
			System.out.println("Item Id:"+vmArray[0]);
			stock.setItemMstId((String) vmArray[0]);
			stock.setItemDtl((String)vmArray[1]);
			stock.setQty((double)vmArray[2]);
			stock.setStorageBinCode((String)vmArray[3]);
			stockList.add(stock);
		});
		
		return stockList;
	}

	@Override
	public List<StockDto> getStockListByItem(String storageBinCode, String itemId) {
		List<Object[]> matList = materialInspectDao.getStockListByItem(storageBinCode,itemId);
		List<StockDto> stockList = new ArrayList<StockDto>();
		matList.forEach(vmArray -> {
			StockDto stock = new StockDto();
			System.out.println("Item Id:"+vmArray[0]);
			stock.setItemMstId((String) vmArray[0]);
			stock.setItemDtl((String)vmArray[1]);
			stock.setQty((double)vmArray[2]);
			stock.setStorageBinCode((String)vmArray[3]);
			stockList.add(stock);
		});
		
		return stockList;
	}

	@Override
	public List<StockDto> getStockByComp(String compCode) {
		List<Object[]> matList = materialInspectDao.getStockByComp(compCode);
		List<StockDto> stockList = new ArrayList<StockDto>();
		System.out.println("MATERIAL SIZE :: "+matList.size());
		matList.forEach(vmArray -> {
			StockDto stock = new StockDto();
			System.out.println("Item Id:"+vmArray[0]);
			stock.setMtlStockInId((long) vmArray[0]);			
			stock.setQty((double)vmArray[1]);
			stock.setItemMstId((String)vmArray[2]);
			stock.setStorageBinCode((String)vmArray[3]);
			stock.setItemDtl((String)vmArray[4]);
			stockList.add(stock);
		});
		
		return stockList;
	}

	@Override
	public MtlStockIn getStockById(long stockInId) {
		return materialInspectDao.getStockById(stockInId);
	}

	@Override
	public void updateStock(long id, double remainQty) {
		materialInspectDao.updateStock(id,remainQty);
	}

	@Override
	public void updateStorageBin(long grnNo, String storageBinCode) {
		// TODO Auto-generated method stub
		materialInspectDao.updateStorageBin(grnNo,storageBinCode);
	}

}
