package com.a2mee.services;

import java.util.List;

import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.GrnItemDto;
import com.a2mee.model.GrnItem;

public interface GrnItemServices {
	public List<GrnItem> getGrnItemList();

	public void saveGrnItem(GrnItem theGrnItem);

	public List<GrnItemDto> getGrnItemList(String itemId);

	public void updateSapData(GrnItem grnItem);

	public void deleteGrn(GrnDto grnDto);
}
