package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.ProductionOrder;

public interface ProductionOrderRepoCustome {
	List<ProductionOrder> getProductionOrdersByPagination(int pageno, int perPage);

}
