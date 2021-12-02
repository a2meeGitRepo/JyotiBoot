package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MtlStockIn;

public interface MaterialInspectDao {
	
	public MaterialInspect saveInspMterial(MaterialInspect materialInspect);

	public List<Object[]> updateQuarantineDataIntoMaterialInspection(String qrcode);

	public boolean storeItem(long grnNo, String storageBinCode);

	public List<Object[]> getAcceptedList();

	public List<Object[]> getStockList(String storageBinCode);

	public List<Object[]> getStockListByItem(String storageBinCode, String itemId);

	public List<Object[]> getStockByComp(String compCode);

	public MtlStockIn getStockById(long stockInId);

	public void updateStock(long id, double remainQty);

	public List<Object> getAvailableStockQty(String compCode);

	public void updateStorageBin(long grnNo, String storageBinCode);
	
}
