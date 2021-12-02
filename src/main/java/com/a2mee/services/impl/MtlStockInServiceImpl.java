package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.a2mee.dao.MtlStockInDao;
import com.a2mee.dao.StockItemDao;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.MaterialInspDto;
import com.a2mee.model.dto.MtlStockInDto;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.StockItem;
import com.a2mee.model.StorageBinMst;
import com.a2mee.services.MtlStockInServices;

@Service
public class MtlStockInServiceImpl implements MtlStockInServices {

	@Autowired
	MtlStockInDao mtlStockInDao;

	@Autowired
	StockItemDao stockItemDao;

	public MtlStockIn saveStock(MaterialInspDto materialInspDto, MaterialInspect materialInspect) {
		List<Object[]> list = mtlStockInDao.getMtrilIdandQrNo(materialInspDto.getItmLotId());

		list.forEach(vmArray -> {
			MtlStockIn msi = new MtlStockIn();
			String id = ((String) vmArray[0]);
			Date d = ((Date) vmArray[1]);
			msi.setInMtlQty(materialInspDto.getAcceptqty());
			msi.setRemainQty(materialInspDto.getAcceptqty());
			//msi.setMaterialInspect(materialInspect);
			msi.setGrnDate(d);
			msi.setCreateDate(new Date());
			msi.setItemMst(new ItemMst(id));
			msi.setBarCode((String) vmArray[2]);
			msi.setStatus("A");
			MtlStockIn mtlStockIn = mtlStockInDao.saveInStock(msi);
			saveAndUpdateStock(mtlStockIn);

		});

		return null;
	}

	public void saveAndUpdateStock(MtlStockIn mtlStockIn) {
		StockItem stockItem = null;
		try {
			stockItem = stockItemDao.checkAvailability(mtlStockIn.getItemMst().getId());
		} catch (Exception e) {
//			e.printStackTrace();
		}

		if (stockItem == null) {
			stockItem = new StockItem();
			stockItem.setItemTolQty(mtlStockIn.getInMtlQty());
			stockItem.setItemMst(mtlStockIn.getItemMst());
			stockItem.setUpdate(new Date());
			stockItemDao.saveAndSaveStock(stockItem);

		} else {
			stockItem.setItemTolQty(stockItem.getItemTolQty() + mtlStockIn.getInMtlQty());
			stockItem.setUpdate(new Date());
			stockItemDao.saveAndSaveStock(stockItem);

		}
	}

	@Override
	public MtlStockInDto getMtlStockInMinDateQty(String itemId, Boolean status) {

			List<Object[]> mtlStockRemQty = mtlStockInDao.getMtlStockInMinDateQtyWithoutChackOut(itemId, status);
			MtlStockInDto mtlStockInDto = new MtlStockInDto();
			mtlStockRemQty.forEach(arr -> {
				mtlStockInDto.setFifoRemainQty((double) arr[0]);
				mtlStockInDto.setGrnDate((Date) arr[1]);
				mtlStockInDto.setQrcode((String) arr[2]);
			});

			return mtlStockInDto;
		
	}

	@Override
	public MtlStockIn getByItem(ItemMst item) {
		return mtlStockInDao.getByItem(item);
	}

	@Override
	public MtlStockIn save(MtlStockIn newMatIn) {
		return mtlStockInDao.save(newMatIn);
	}

	@Override
	public MtlStockIn getByItemAndStorage(ItemMst item, String storageBinCode) {
		// TODO Auto-generated method stub
		return mtlStockInDao.getByItemAndStorage(item,storageBinCode);
	}

	@Override
	public List<MtlStockIn> getStockListByItem(String itemMstId) {
		// TODO Auto-generated method stub
		return mtlStockInDao.getStockListByItem(itemMstId);
	}

	@Override
	public List<MtlStockIn> getStockListByItemDate(String itemMstId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return mtlStockInDao.getStockListByItemDate(itemMstId, startDate,endDate);
	}

	@Override
	public List<MtlStockIn> getGrnListByItem(String itemMstId) {
		// TODO Auto-generated method stub
		return mtlStockInDao.getGrnListByItem(itemMstId);
	}

	@Override
	public List<MtlStockIn> getGrnListByItemDate(String itemMstId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return mtlStockInDao.getGrnListByItemDate(itemMstId, startDate, endDate);
	}
}
