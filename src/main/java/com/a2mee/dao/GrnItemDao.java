package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.GrnItem;
import com.a2mee.model.dto.GrnDto;

public interface GrnItemDao {
	
	public List<GrnItem> getGrnItemList();

	public void saveGrnItem(GrnItem theGrnItem);

	public List<Object[]> getGrnItemList(String itemId);

	public void updateSapData(GrnItem grnItem);

	public void deleteGrn(GrnDto grnDto);

}
