package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.ItemMst;

public interface ItemDaoCustome {
	List<ItemMst> itemListByPagination(int pageno, int perPage);

}
