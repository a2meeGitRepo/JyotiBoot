package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.StockItem;

public interface StockItemDao {
	
	public StockItem saveAndSaveStock(StockItem stockItem);
	
	public StockItem checkAvailability(String  matrialId);

	public List<Object> getTotalItemStockQtyByItemId(String itemId);

	public List<StockItem> getMaterialStock();

}
