package com.a2mee.services;

import java.util.List;

import com.a2mee.model.dto.MaterialInspDto;
import com.a2mee.model.dto.MtlStockInDto;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.StorageBinMst;

public interface MtlStockInServices {
	public MtlStockIn saveStock(MaterialInspDto materialInspDto, MaterialInspect materialInspect);

	public MtlStockInDto getMtlStockInMinDateQty(String itemId, Boolean status);

	public MtlStockIn getByItem(ItemMst item);

	public MtlStockIn save(MtlStockIn newMatIn);

	public MtlStockIn getByItemAndStorage(ItemMst item, String storageBinCode);

	public List<MtlStockIn> getStockListByItem(String itemMstId);

	public List<MtlStockIn> getStockListByItemDate(String itemMstId, String startDate, String endDate);

	public List<MtlStockIn> getGrnListByItem(String itemMstId);

	public List<MtlStockIn> getGrnListByItemDate(String itemMstId, String startDate, String endDate);
}
