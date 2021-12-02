package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.GrnItemDao;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.GrnItemDto;
import com.a2mee.model.GrnItem;
import com.a2mee.services.GrnItemServices;

@Service
public class GrnItemServicesImpl implements GrnItemServices {

	@Autowired
	GrnItemDao grnItemDao;
	
	@Override
	public List<GrnItem> getGrnItemList() {
		return grnItemDao.getGrnItemList();
	}

	@Override
	public void saveGrnItem(GrnItem theGrnItem) {
		grnItemDao.saveGrnItem(theGrnItem);
	}

	@Override
	public List<GrnItemDto> getGrnItemList(String itemId) {
		List<Object[]> list=grnItemDao.getGrnItemList(itemId);
		System.out.println("size in service="+list.size());
		 List<GrnItemDto> grdList = new ArrayList<GrnItemDto>();
		 list.forEach(vmArray -> {
			 GrnItemDto gd  =new GrnItemDto();
			 gd.setItemQty((double)vmArray[0]);
			    gd.setItemDtl((String)vmArray[1]);			    
			    gd.setItemTolRecd((double)vmArray[2]);
			    grdList.add(gd);				
			});
		 
		return grdList;
	}

	@Override
	public void updateSapData(GrnItem grnItem) {
		grnItemDao.updateSapData(grnItem);
	}

	@Override
	public void deleteGrn(GrnDto grnDto) {
		grnItemDao.deleteGrn(grnDto);
	}

}
