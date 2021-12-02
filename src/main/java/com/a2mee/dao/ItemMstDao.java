package com.a2mee.dao;

import java.time.LocalDate;
import java.util.List;

import com.a2mee.model.ItemMst;

public interface ItemMstDao {

	public List<Object[]> getItemList(String venId);
	public List<Object[]> getProOrdItemList(LocalDate startDate, LocalDate endDate);
	public List<Object[]> getItemForProQrCode(String mouldId);
	
	public ItemMst findById(String itemId);
	public List<ItemMst> getItemByCode(String materialCode);
	public void saveItem(ItemMst itemMst);
	public List<Object[]> getItemList();
	public List<ItemMst> getItemById(String itemMstId);
	public List<Object[]> getItemGrnlist(String venId);
	public List<ItemMst> getEntireItemMst();
	public int getCount();
	public List<ItemMst> getItemMstByPagination(int pageno, int perPage);
	public long materialStockCountBySearch(String search);
	public List<ItemMst> getItemMstByPagination(int pageno, int perPage, String search);
}
