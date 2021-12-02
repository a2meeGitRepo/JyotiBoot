package com.a2mee.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.ItemDao;
import com.a2mee.model.ItemMst;
import com.a2mee.services.ItemsService;
@Service
public class ItemsServiceImpl implements ItemsService {
	@Autowired
	ItemDao itemDao;
	
	@Override
	public List<ItemMst> getItemList() {
		// TODO Auto-generated method stub
		return itemDao.findAll();
	}

	@Override
	public void updateitem(ItemMst itemMst) {
		// TODO Auto-generated method stub
		itemDao.save(itemMst);
	}

	@Override
	public ItemMst getItemById(String itemCode) {
		// TODO Auto-generated method stub
		Optional<ItemMst>  optional=itemDao.findByItemId(itemCode);
		return optional.isPresent()?optional.get():null;
	}

	@Override
	public List<ItemMst> itemListByPagination(int pageno, int perPage) {
		// TODO Auto-generated method stub
		return itemDao.itemListByPagination(pageno,perPage);
	}

}
