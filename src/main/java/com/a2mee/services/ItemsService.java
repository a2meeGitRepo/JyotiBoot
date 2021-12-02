package com.a2mee.services;

import java.util.List;

import com.a2mee.model.ItemMst;

public interface ItemsService {

	List<ItemMst> getItemList();

	void updateitem(ItemMst itemMst);

	ItemMst getItemById(String itemCode);

	List<ItemMst> itemListByPagination(int pageno, int perPage);

}
