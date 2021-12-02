package com.a2mee.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.a2mee.dao.DashboardModelPlanDto;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MaterialTransaction;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.StockItem;
import com.a2mee.model.dto.DashboardMaterialStock;


public interface DashBoardService {

	List<DashboardModelPlanDto> getDashboardModelPlans();

	//List<DashboardMaterialStock> getMaterialStock(int pageno, int perPage);

	List<MtlStockIn> materialLocationWiseStock(int pageno, int perPage);

//	List<MtlStockIn> materialStockBySearch(int pageno, int perPage, String search);

	int materialLocationWiseStockCount();

	int materialLocationWiseStockSearchCount(String search);

	List<MtlStockIn> materialLocationWiseStockSearch(int pageno, int perPage, String search);

	List<DashboardMaterialStock> getMaterialStock(int pageno, int perPage);

	int materialStockCount();

	List<DashboardMaterialStock> materialStockBySearch(int pageno, int perPage, String search);

	long materialStockCountBySearch(String search);

	List<MaterialTransaction> gettodaysTranction();

}
