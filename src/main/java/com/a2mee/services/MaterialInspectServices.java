package com.a2mee.services;

import java.util.List;

import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.dto.AcceptedMatDto;
import com.a2mee.model.dto.StockDto;

public interface MaterialInspectServices {

	boolean storeItem(long grnNo, String storageBinCode);

	List<AcceptedMatDto> getAcceptedList();

	List<StockDto> getStockList(String storageBinCode);

	List<StockDto> getStockListByItem(String storageBinCode, String itemId);

	List<StockDto> getStockByComp(String compCode);

	MtlStockIn getStockById(long stockInId);

	void updateStock(long id, double remainQty);

	void updateStorageBin(long grnNo, String storageBinCode);

}
