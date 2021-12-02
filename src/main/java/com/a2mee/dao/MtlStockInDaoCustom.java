package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.MtlStockIn;

public interface MtlStockInDaoCustom {

	public MtlStockIn saveInStock(MtlStockIn mtlStockIn);

	public List<Object[]> getMtrilIdandQrNo(long inspectId);

//	public List<Object[]> getMtlStockInMinDateQtyChackOutBox(String itemId, String status);
	
	public List<Object[]> getMtlStockInMinDateQtyWithoutChackOut(String itemId, Boolean status);
	
	public List<Object[]> showPickingList(String qrCode,String itemId);
	public List<MtlStockIn> getListForStockInUpdate(String qrCode,String itemId,int status);
	
	public MtlStockIn updateStockInbyId(long stockInId,double qty);
	List<MtlStockIn> getMaterialStock(int pageno, int perPage);
	List<MtlStockIn> materialLocationWiseStockSearch(int pageno, int perPage, String search);


}
