package com.a2mee.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;

import com.a2mee.dao.CustomAssmDao;
import com.a2mee.dao.PickingAssemblyDao;
import com.a2mee.dao.ProOrderDao;
import com.a2mee.dao.RequestAssmDao;
import com.a2mee.model.CustomAssembly;
import com.a2mee.model.PickingAssembly;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.RequestAssembly;
import com.a2mee.model.dto.PickStockDto;
import com.a2mee.model.dto.ResponseDto;
import com.a2mee.services.ProOrderService;
import com.mysql.jdbc.ReplicationMySQLConnection;

@Service
public class ProOrderServiceImpl implements ProOrderService {

	@Autowired
	ProOrderDao proOrderDao;
	
	@Autowired
	CustomAssmDao customAssmDao;
	
	@Autowired
	RequestAssmDao requestAssmDao;
	
	@Autowired
	PickingAssemblyDao pickingAssemblyDao;
	
	private String status = "";
	
	@Override
	public List<ProductionOrder> getAllProOrder() {
		return proOrderDao.findAll();
	}

	@Override
	public void saveAll(List<ProductionOrder> incomingProOrders) {
		proOrderDao.saveAll(incomingProOrders);
	}

	@Override
	public List<Long> getSalesOrders() {
		return proOrderDao.getSalesOrders();
	}

	@Override
	public List<ProductionOrder> getProOrdersByPo(long proOrderNo) {
		return proOrderDao.findByProdOrdNo(proOrderNo);
	}

	@Override
	public List<ProductionOrder> getProOrdersBySo(long salesOrder) {
		return proOrderDao.findBySalesOrdNo(salesOrder);
	}

	@Override
	public void updateApproval(long prodOrdId) {
		proOrderDao.updateApproval(prodOrdId);
	}

	@Override
	public void updateCustomApproval(long proOrderNo) {
		proOrderDao.updateCustomApproval(proOrderNo);
	}

	@Override
	public void addCustomAssm(List<CustomAssembly> customAssms) {
		customAssmDao.saveAll(customAssms);
	}

	@Override
	public List<ProductionOrder> getApprovedPo() {
		return proOrderDao.getApprovedPo();
	}

	@Override
	public List<RequestAssembly> getReqAssms(long prodOrdNo) {
		return requestAssmDao.getReqAssms(prodOrdNo);
	}

	@Override
	public List<CustomAssembly> getCustomAssembly(String modelCode, long prodOrdNo) {
		return customAssmDao.getCustomAssembly(modelCode, prodOrdNo);
	}

	@Override
	public List<RequestAssembly> getReqAssms() {
		status = "C";
		return requestAssmDao.getReqAssms(status);
	}

	@Override
	public void addReqAssm(RequestAssembly reqAssembly) {
		requestAssmDao.save(reqAssembly);
	}

	@Override
	public void addReqAssms(List<RequestAssembly> reqAssemblies) {
		requestAssmDao.saveAll(reqAssemblies);
	}

	@Override
	public void assmReceived(long reqAssmId) {
		status = "A";
		requestAssmDao.assmReceived(reqAssmId, status);
	}

	@Override
	public ResponseDto consumeAssm(long reqAssmId) {
		ResponseDto response = new ResponseDto();
		
		RequestAssembly reqAssm = requestAssmDao.findById(reqAssmId).get();
		List<PickingAssembly> pickAssms = pickingAssemblyDao.getAssmsByAssmCode(reqAssm.getAssemblyCode());
		double reqQty = reqAssm.getAssemblyQty();
		double availQty = 0;
		for(PickingAssembly pickAssm : pickAssms) {
			availQty += pickAssm.getRemQty();
		}
		
		if(reqQty>availQty) {
			response.setMessage("failed");
			return response;
		}
		
		for(PickingAssembly pickAssm : pickAssms) {
			double pickQty = pickAssm.getRemQty();
			if(reqQty>pickQty) {
				pickAssm.setRemQty(0);
				reqQty -= pickQty;
			}else {
				pickAssm.setRemQty(pickQty - reqQty);
			}
		}
		pickingAssemblyDao.saveAll(pickAssms);
		
		status = "C";
		requestAssmDao.consumeAssm(reqAssmId, status);
		
		response.setMessage("success");
		return response;
	}

	@Override
	public List<RequestAssembly> getRequestedAssms() {
		status = "R";
		return requestAssmDao.getRequestedAssms(status);
	}

	@Override
	public List<Long> getProOrdNo(String modelCode) {
		// TODO Auto-generated method stub
		return proOrderDao.getProOrdNo(modelCode);
	}

	@Override
	public List<ProductionOrder> getProductionOrders() {
		// TODO Auto-generated method stub
		return proOrderDao.findAll();
	}

	@Override
	public void updateProductionOrder(ProductionOrder productionOrder) {
		// TODO Auto-generated method stub
		proOrderDao.save(productionOrder);
	}

	@Override
	public ProductionOrder getProductionOrderByPONO(String prodOrdNo) {
		// TODO Auto-generated method stub
		System.out.println("Production Order  for "+prodOrdNo+"   :: ");
		if(prodOrdNo==null||prodOrdNo=="null"||prodOrdNo==""){
			System.out.println("Production Order  IS NULL  ");
			return null;
		}else{
			long prod=Long.parseLong (prodOrdNo);
			Optional<ProductionOrder> optional= proOrderDao.getProductionOrderByPONO(prod);
			//System.out.println("Production Order  for "+prodOrdNo+"   :: "+optional.isPresent());
			return optional.isPresent()?optional.get():null;
		}
		
	}

	@Override
	public List<ProductionOrder> getProductionOrdersByPagination(int pageno, int perPage) {
		// TODO Auto-generated method stub
		return proOrderDao.getProductionOrdersByPagination(pageno,perPage);
	}



}
