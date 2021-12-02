package com.a2mee.services;

import java.time.LocalDate;
import java.util.List;

import com.a2mee.model.dto.ItemMstDto;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.ItemMst;
import com.a2mee.model.PendingReport;

public interface ItemMstServices {
	public List<MasterDto> getItemList(String venId);
	public List<MasterDto> getProOrdItemList(LocalDate startDate, LocalDate endDate);
	public List<ItemMst> getItemByCode(String materialCode);
	public void saveItem(ItemMst itemMst);
	public List<ItemMstDto> getItemList();
	public List<ItemMst> getItemById(String itemMstId);
	public List<MasterDto> getItemGrnlist(String venId);
	public List<ItemMst> getEntireItemMst();
	public ItemMst findById(String itemId);
	public ItemMst add(ItemMst newItem);
	public void savePendingReport(List<PendingReport> pendings);
	public void savePendingReport1(PendingReport pendingDto);
}
