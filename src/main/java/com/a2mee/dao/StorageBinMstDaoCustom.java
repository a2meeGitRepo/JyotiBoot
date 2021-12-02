package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.StorageBinMst;

public interface StorageBinMstDaoCustom {


	List<StorageBinMst> getStorageBinList();

	void addStorageBin(StorageBinMst storageBinMst);

	void delStorageBin(long storageBinId);

	List<StorageBinMst> getstorageBinMstByCode(String storageBinCode);
	List<StorageBinMst> getStorageBinListByPagination(int pageno, int perPage);

}
