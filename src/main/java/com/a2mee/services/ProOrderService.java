package com.a2mee.services;

import java.util.List;

import com.a2mee.model.AssemblyMst;
import com.a2mee.model.CustomAssembly;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.RequestAssembly;
import com.a2mee.model.dto.PickStockDto;
import com.a2mee.model.dto.ResponseDto;

public interface ProOrderService {

	List<ProductionOrder> getAllProOrder();

	void saveAll(List<ProductionOrder> incomingProOrders);

	List<Long> getSalesOrders();

	List<ProductionOrder> getProOrdersByPo(long proOrderNo);

	List<ProductionOrder> getProOrdersBySo(long salesOrder);

	void updateApproval(long prodOrdId);

	void updateCustomApproval(long proOrderNo);

	void addCustomAssm(List<CustomAssembly> customAssms);

	List<ProductionOrder> getApprovedPo();

	List<RequestAssembly> getReqAssms(long prodOrdNo);

	List<CustomAssembly> getCustomAssembly(String modelCode, long prodOrdNo);

	List<RequestAssembly> getReqAssms();

	void addReqAssm(RequestAssembly reqAssembly);

	void assmReceived(long reqAssmId);

	ResponseDto consumeAssm(long reqAssmId);

	List<RequestAssembly> getRequestedAssms();

	void addReqAssms(List<RequestAssembly> reqAssemblies);

	List<Long> getProOrdNo(String modelCode);

	List<ProductionOrder> getProductionOrders();

	void updateProductionOrder(ProductionOrder productionOrder);

	ProductionOrder getProductionOrderByPONO(String prodOrdNo);

	List<ProductionOrder> getProductionOrdersByPagination(int pageno, int perPage);

}
