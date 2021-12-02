package com.a2mee.services;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.a2mee.model.dto.PurchaseOrderCsvDto;
import com.a2mee.model.dto.PurchaseOrderDto;
import com.a2mee.model.dto.PurchaseOrderErrDto;
import com.a2mee.model.PurchaseOrder;


public interface PurchaseOrderService {

	public List<PurchaseOrderDto> getPurchaseOrderList(String venId,String itemId);
	public List<PurchaseOrderDto> searchByPurchaseOrderNo(long purchaseOrderNo);
	public List<PurchaseOrderDto> getNoErrPurchaseOrderList(String venId, String itemId);
	public List<PurchaseOrderDto> searchByNoErrPurchaseOrderNo(long purchaseOrderNo);
	public List<PurchaseOrderErrDto> getErrPurchaseOrderList(String venId, String itemId);
	public List<PurchaseOrderErrDto> searchByErrPurchaseOrderNo(long purchaseOrderNo);
	public PurchaseOrder searchByPurchaseOrderId(long purchaseOrderId);
	public List<PurchaseOrder> getPurchaseOrderListByNo(long purchaseOrder);
	public void savePurchaseOrder(PurchaseOrder purchaseOrder);
	public List<PurchaseOrder> getEntirePurchaseOrder();
	public List<PurchaseOrder> getInwardedPo();
	
	public List<PurchaseOrder> getPurchaseOrders(int pageNo, int itemPerPage);
	public int getTotalCount();


}
