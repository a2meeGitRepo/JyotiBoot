package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.PurchaseOrderDao;
import com.a2mee.dao.PurchaseOrderErrDao;
import com.a2mee.dao.impl.VendorMstDaoImpl;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.PurchaseOrderCsvDto;
import com.a2mee.model.dto.PurchaseOrderDto;
import com.a2mee.model.dto.PurchaseOrderErrDto;
import com.a2mee.model.ItemMst;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.VendorMst;
import com.a2mee.services.PurchaseOrderService;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;

	@Autowired
	private PurchaseOrderErrDao purchaseOrderErrDao;
	
	
	@Override
	public List<PurchaseOrderDto> getPurchaseOrderList(String venId, String itemId) {
		List<Object[]> list = purchaseOrderDao.getPurchaseOrderList(venId, itemId);
		return getPoList(list);
	}

	@Override
	public List<PurchaseOrderDto> searchByPurchaseOrderNo(long purchaseOrderNo) {
		List<Object[]>list = purchaseOrderDao.searchByPurchaseOrderNo(purchaseOrderNo);
		return getPoList(list);
	}

	@Override
	public List<PurchaseOrderDto> getNoErrPurchaseOrderList(String venId, String itemId) {
		List<Object[]> list = purchaseOrderDao.getNoErrPurchaseOrderList(venId, itemId);
		
		return getPoList(list);
	}

	@Override
	public List<PurchaseOrderDto> searchByNoErrPurchaseOrderNo(long purchaseOrderNo) {
		List<Object[]>list = purchaseOrderDao.searchByNoErrPurchaseOrderNo(purchaseOrderNo);
		return getPoList(list);
	}


	@Override
	public List<PurchaseOrderErrDto> getErrPurchaseOrderList(String venId, String itemId) {
		List<Object[]> list = purchaseOrderDao.getErrPurchaseOrderList(venId, itemId);
		return getErrPoList(list);
	}

	@Override
	public List<PurchaseOrderErrDto> searchByErrPurchaseOrderNo(long purchaseOrderNo) {
		List<Object[]>list = purchaseOrderDao.searchByErrPurchaseOrderNo(purchaseOrderNo);
		return getErrPoList(list);
	}
	
	private List<PurchaseOrderDto> getPoList(List<Object[]> list){
		System.out.println("size in service="+list.size());
		 List<PurchaseOrderDto> purchaseOrderList = new ArrayList<PurchaseOrderDto>();
		 list.forEach(vmArray -> {
			 PurchaseOrderDto gd = new PurchaseOrderDto();
			    gd.setPurchaseOrderNo((long)vmArray[0]);
			    gd.setCreateOn((String)vmArray[1]);
			    gd.setItemDetails((String)vmArray[2]);
			    gd.setVenName((String)vmArray[3]);
			    gd.setPurchaseOwner((String)vmArray[4]);
			    gd.setInwardDate((Date)vmArray[5]);
			    gd.setInvoiceNumber((String)vmArray[6]);
			    gd.setChallanNumber((String)vmArray[7]);
			    gd.setDocDate((Date)vmArray[8]);
			    gd.setRemarks((String)vmArray[9]);
			    gd.setItemQty((double)vmArray[10]);
			    gd.setItemMsrUnit((String)vmArray[11]);
			    gd.setItemMstId((String)vmArray[12]);			    
			    gd.setNetPrice((double)vmArray[13]);
			    gd.setCurrency((String)vmArray[14]);
			    gd.setPurchaseOrderItemId((long)vmArray[15]);
			    int purOrErrcount=purchaseOrderErrDao.getCountByPurchaseOrderNoAndITem((long)vmArray[0],(long)vmArray[15]);
			    if(purOrErrcount!=0){
			    	gd.setDeviationEnable(false);
			    }else{
			    	gd.setDeviationEnable(true);
			    }
			    
			    gd.setType((String)vmArray[16]);
			    gd.setBoe((String)vmArray[17]);
			    gd.setCourier((String)vmArray[18]);
			    gd.setDocketNo((String)vmArray[19]);
			    gd.setSapGrnNo((String)vmArray[20]);
			    gd.setSapGrnDate((Date)vmArray[21]);
			    gd.setDelayDays((String)vmArray[22]);
			    gd.setResPerson((String)vmArray[23]);
			    gd.setDelayPerc((String)vmArray[24]);
			    gd.setAccDocHandover((String)vmArray[25]);
			    gd.setHandoverDate((Date)vmArray[26]);
			    gd.setVehicleNo((String)vmArray[27]);
			    gd.setVehicleStatus((String)vmArray[28]);
			    gd.setUnloadType((String)vmArray[29]);
			    gd.setPackingType((String)vmArray[30]);
			    
			    		    
			    purchaseOrderList.add(gd);				
			});		 
		return purchaseOrderList;
	}
	
	private List<PurchaseOrderErrDto> getErrPoList(List<Object[]> list){
		 List<PurchaseOrderErrDto> purchaseOrderErrList = new ArrayList<PurchaseOrderErrDto>();
		 list.forEach(vmArray -> {
			 PurchaseOrderErrDto gd = new PurchaseOrderErrDto();
			    gd.setPurchaseOrderNo((long)vmArray[0]);
			    gd.setCreateOn((String)vmArray[1]);
			    gd.setItemDetails((String)vmArray[2]);
			    gd.setVenName((String)vmArray[3]);
			    gd.setPurchaseOwner((String)vmArray[4]);
			    gd.setInwardDate((Date)vmArray[5]);
			    gd.setInvoiceNumber((String)vmArray[6]);
			    gd.setChallanNumber((String)vmArray[7]);
			    gd.setDocDate((Date)vmArray[8]);
			    gd.setRemarks((String)vmArray[9]);
			    gd.setItemQty((double)vmArray[10]);
			    gd.setItemMsrUnit((String)vmArray[11]);
			    gd.setItemMstId((String)vmArray[12]);			    
			    gd.setNetPrice((double)vmArray[13]);
			    gd.setCurrency((String)vmArray[14]);
			    gd.setErrorId((int)vmArray[15]);
			    gd.setPurchaseOrderItemId((long)vmArray[16]);
			    
			    gd.setType((String)vmArray[17]);
			    gd.setBoe((String)vmArray[18]);
			    gd.setCourier((String)vmArray[19]);
			    gd.setDocketNo((String)vmArray[20]);
			    gd.setSapGrnNo((String)vmArray[21]);
			    gd.setSapGrnDate((Date)vmArray[22]);
			    gd.setDelayDays((String)vmArray[23]);
			    gd.setResPerson((String)vmArray[24]);
			    gd.setDelayPerc((String)vmArray[25]);
			    gd.setAccDocHandover((String)vmArray[26]);
			    gd.setHandoverDate((Date)vmArray[27]);
			    gd.setVehicleNo((String)vmArray[28]);
			    gd.setVehicleStatus((String)vmArray[29]);
			    gd.setUnloadType((String)vmArray[30]);
			    gd.setPackingType((String)vmArray[31]);
			    		    
			    purchaseOrderErrList.add(gd);
				
			});		 
		return purchaseOrderErrList;
	}

	@Override
	public PurchaseOrder searchByPurchaseOrderId(long purchaseOrderId) {
		return purchaseOrderDao.searchByPurchaseOrderId(purchaseOrderId);
	}

	@Override
	public List<PurchaseOrder> getPurchaseOrderListByNo(long purchaseOrder) {
		return purchaseOrderDao.getPurchaseOrderListByNo(purchaseOrder);
	}

	@Override
	public void savePurchaseOrder(PurchaseOrder purchaseOrder) {
		purchaseOrderDao.savePurchaseOrder(purchaseOrder);
	}

	@Override
	public List<PurchaseOrder> getEntirePurchaseOrder() {
		return purchaseOrderDao.getEntirePurchaseOrder();
	}

	@Override
	public List<PurchaseOrder> getInwardedPo() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderDao.getInwardedPo();
		
		HashSet<Long> poNo=new HashSet<>();
		purchaseOrders.removeIf(e->!poNo.add(e.getPurchaseOrderNo()));
		
		return purchaseOrders;
	}

	@Override
	public List<PurchaseOrder> getPurchaseOrders(int pageNo, int itemPerPage) {
		return purchaseOrderDao.getPurchaseOrders(pageNo, itemPerPage);
	}

	@Override
	public int getTotalCount() {
		return purchaseOrderDao.getTotalCount();
	}

}
