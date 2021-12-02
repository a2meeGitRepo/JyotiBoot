package com.a2mee.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.MtlStockInDao;
import com.a2mee.dao.ReportDao;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.PendingReport;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.PurchaseOrderItem;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.GrnItemDto;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.dto.PickingReportDto;
import com.a2mee.model.dto.PoDeviationsDto;
import com.a2mee.model.dto.StockListDto;
import com.a2mee.services.ReportServices;
import com.a2mee.util.CustomLogger;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReportServicesImpl implements ReportServices {

	@Autowired
	ReportDao reportdao;	
	@Autowired
	MtlStockInDao mtlStockInDao;
	@Autowired
	PendingReportTableRepo pendingReportTableRepo;
	
	
	@Override
	public List<StockListDto>  getStocks(String plantCode, String storageLoc, String itemId, String date) {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate searchDate = LocalDate.parse(date, df);
			List<Object[]> data = reportdao.getStocks(plantCode, storageLoc, itemId, searchDate);
			System.out.println("size in service="+data.size());
			List<StockListDto> sList = new ArrayList<StockListDto>();
			data.forEach(stArray -> {
				 	StockListDto sl  =new StockListDto();
					 sl.setPlantCode((String)stArray[0]);
					 sl.setStoreLoc((String)stArray[1]);
					 sl.setStorageBins((String)stArray[2]);
					 sl.setItemId((String)stArray[3]);
					 sl.setItemDtl((String)stArray[4]);
					 sl.setApproQty((double)stArray[5]);
					 sl.setHoldQty((double)stArray[6]);
					 sl.setRejQty((double)stArray[7]);
					 sList.add(sl);				
				});		 
			 return sList;
		} catch(Exception e) {
			return null;
		}		
	}

	@Override
	public List<StockListDto> getStocksByDate(String startDate, String endDate) {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date1 = LocalDate.parse(startDate, df);
			LocalDate date2 = LocalDate.parse(endDate, df);
			List<Object[]> data = reportdao.getStocksByDate(date1, date2);
			System.out.println("size in service="+data.size());
			List<StockListDto> sList = new ArrayList<StockListDto>();
			data.forEach(stArray -> {
				 	StockListDto sl  =new StockListDto();
					 sl.setPlantCode((String)stArray[0]);
					 sl.setStoreLoc((String)stArray[1]);
					 sl.setStorageBins((String)stArray[2]);
					 sl.setItemId((String)stArray[3]);
					 sl.setItemDtl((String)stArray[4]);
					 sl.setApproQty((double)stArray[5]);
					 sl.setHoldQty((double)stArray[6]);
					 sl.setRejQty((double)stArray[7]);
					 sList.add(sl);				
				});		 
			 return sList;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public List<StockListDto> getStocksByItemAndDate(String startDate, String endDate, String itemId) {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date1 = LocalDate.parse(startDate, df);
			LocalDate date2 = LocalDate.parse(endDate, df);
			List<Object[]> data = reportdao.getStocksByItemAndDate(date1, date2, itemId);
			System.out.println("size in service="+data.size());
			List<StockListDto> sList = new ArrayList<StockListDto>();
			data.forEach(stArray -> {
				 	StockListDto sl  =new StockListDto();
					 sl.setPlantCode((String)stArray[0]);
					 sl.setStoreLoc((String)stArray[1]);
					 sl.setStorageBins((String)stArray[2]);
					 sl.setItemId((String)stArray[3]);
					 sl.setItemDtl((String)stArray[4]);
					 sl.setApproQty((double)stArray[5]);
					 sl.setHoldQty((double)stArray[6]);
					 sl.setRejQty((double)stArray[7]);
					 sList.add(sl);				
				});		 
			 return sList;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public List<GrnDto> getTodaysGrn(LocalDate searchDate) {
		List<Object[]> list=reportdao.getTodaysGrn(searchDate);
		System.out.println("size in service="+list.size());
		 List<GrnDto> grdList = new ArrayList<GrnDto>();
		 list.forEach(vmArray -> {
			    GrnDto gd  =new GrnDto();
			    gd.setGrnId((long)vmArray[0]);
			    gd.setGrnEntryDate((Date)vmArray[1]);
			    gd.setItemLotNo((String)vmArray[2]);
			    gd.setGrnItmLotID((long)vmArray[3]);
			    gd.setItemDtils((String)vmArray[4]);
			    gd.setBoxQty((long)vmArray[5]);
			    gd.setVenName((String)vmArray[6]);
			    gd.setBarcode((String)vmArray[7]);
			    gd.setGrnBy((String)vmArray[8]);
			    gd.setInwardDate((Date)vmArray[9]);
			    gd.setInvoiceNumber((String)vmArray[10]);
			    gd.setChallanNumber((String)vmArray[11]);
			    gd.setDocDate((Date)vmArray[12]);
			    gd.setRemarks((String)vmArray[13]);
			    gd.setErrorSolveDate((Date)vmArray[14]);
			    gd.setInwardTime((String)vmArray[15]);
			    gd.setGrnStatus((String)vmArray[16]);
			    gd.setGrnNo((long)vmArray[17]);
			    gd.setItemTolRecd((double)vmArray[18]);
			    gd.setItemMstId((String)vmArray[19]);
			    gd.setPurchaseOrderNo((long)vmArray[20]);
			    
			    gd.setType((String)vmArray[21]);
			    gd.setBoe((String)vmArray[22]);
			    gd.setCourier((String)vmArray[23]);
			    gd.setDocketNo((String)vmArray[24]);
			    gd.setSapGrnNo((String)vmArray[25]);
			    gd.setSapGrnDate((Date)vmArray[26]);
			    gd.setDelayDays((String)vmArray[27]);
			    gd.setResPerson((String)vmArray[28]);
			    gd.setDelayPerc((String)vmArray[29]);
			    gd.setAccDocHandover((String)vmArray[30]);
			    gd.setHandoverDate((Date)vmArray[31]);
			    gd.setVehicleNo((String)vmArray[32]);
			    gd.setVehicleStatus((String)vmArray[33]);
			    gd.setUnloadType((String)vmArray[34]);
			    gd.setPackingType((String)vmArray[35]);
			    gd.setGrnItemId((long)vmArray[36]);
			    grdList.add(gd);
				
			});
		 
		return grdList;
	}

	@Override
	public List<GrnDto> getGrnByDateRange(LocalDate from, LocalDate to) {
		List<Object[]> list=reportdao.getGrnByDateRange(from,to);
		System.out.println("size in service="+list.size());
		 List<GrnDto> grdList = new ArrayList<GrnDto>();
		 list.forEach(vmArray -> {
			    GrnDto gd  =new GrnDto();
			    gd.setGrnId((long)vmArray[0]);
			    gd.setGrnEntryDate((Date)vmArray[1]);
			    gd.setItemLotNo((String)vmArray[2]);
			    gd.setGrnItmLotID((long)vmArray[3]);
			    gd.setItemDtils((String)vmArray[4]);
			    gd.setBoxQty((long)vmArray[5]);
			    gd.setVenName((String)vmArray[6]);
			    gd.setBarcode((String)vmArray[7]);
			    gd.setGrnBy((String)vmArray[8]);
			    gd.setInwardDate((Date)vmArray[9]);
			    gd.setInvoiceNumber((String)vmArray[10]);
			    gd.setChallanNumber((String)vmArray[11]);
			    gd.setDocDate((Date)vmArray[12]);
			    gd.setRemarks((String)vmArray[13]);
			    gd.setErrorSolveDate((Date)vmArray[14]);
			    gd.setInwardTime((String)vmArray[15]);
			    gd.setGrnStatus((String)vmArray[16]);
			    gd.setGrnNo((long)vmArray[17]);
			    gd.setItemTolRecd((double)vmArray[18]);
			    gd.setItemMstId((String)vmArray[19]);
			    gd.setPurchaseOrderNo((long)vmArray[20]);
			    
			    gd.setType((String)vmArray[21]);
			    gd.setBoe((String)vmArray[22]);
			    gd.setCourier((String)vmArray[23]);
			    gd.setDocketNo((String)vmArray[24]);
			    gd.setSapGrnNo((String)vmArray[25]);
			    gd.setSapGrnDate((Date)vmArray[26]);
			    gd.setDelayDays((String)vmArray[27]);
			    gd.setResPerson((String)vmArray[28]);
			    gd.setDelayPerc((String)vmArray[29]);
			    gd.setAccDocHandover((String)vmArray[30]);
			    gd.setHandoverDate((Date)vmArray[31]);
			    gd.setVehicleNo((String)vmArray[32]);
			    gd.setVehicleStatus((String)vmArray[33]);
			    gd.setUnloadType((String)vmArray[34]);
			    gd.setPackingType((String)vmArray[35]);
			    gd.setGrnItemId((long)vmArray[36]);
			    grdList.add(gd);
				
			});
		 
		return grdList;
	}

	@Override
	public List<PoDeviationsDto> getTodaysDeviation(LocalDate searchDate) {
		List<PurchaseOrderError> purchaseOrderErrors = reportdao.getTodaysDeviation(searchDate);
		Set<Long> poItemNos = new HashSet<>();
		List<PoDeviationsDto> poDeviations = new ArrayList<>();
		for(PurchaseOrderError purchaseOrderError:purchaseOrderErrors) {
			long tempPoItemNo = purchaseOrderError.getPurchaseOrderItemId();
			poItemNos.add(tempPoItemNo);
		}
		for(Long poItemNo:poItemNos) {
			PurchaseOrderItem poItem = reportdao.getPoItemById(poItemNo);
			StringBuilder errors = new StringBuilder();
			PoDeviationsDto poDeviation = new PoDeviationsDto();
			for(PurchaseOrderError purchaseOrderError: purchaseOrderErrors) {				
				if(poItemNo == purchaseOrderError.getPurchaseOrderItemId()) {
					errors.append(purchaseOrderError.getErrorMst().getErrorMessage()+"; ");
					poDeviation.setStatus(purchaseOrderError.getStatus());
				}				
			}
			String errorList = errors.toString();
			errorList = errorList.replaceAll("; $", "");
			poDeviation.setPurchaseOrderNo(poItem.getPurchaseOrder().getPurchaseOrderNo());
			poDeviation.setPoItemId(poItem.getItemMst().getId());
			poDeviation.setPoItemDtl(poItem.getItemMst().getItemDtl());
			poDeviation.setErrors(errorList);
			poDeviations.add(poDeviation);
		}
		
		return poDeviations;
	}

	@Override
	public List<PoDeviationsDto> getDeviationByDateRange(LocalDate from, LocalDate to) {
		List<PurchaseOrderError> purchaseOrderErrors = reportdao.getDeviationByDateRange(from, to);
		Set<Long> poItemNos = new HashSet<>();
		List<PoDeviationsDto> poDeviations = new ArrayList<>();
		for(PurchaseOrderError purchaseOrderError:purchaseOrderErrors) {
			long tempPoItemNo = purchaseOrderError.getPurchaseOrderItemId();
			poItemNos.add(tempPoItemNo);
		}
		for(Long poItemNo:poItemNos) {
			PurchaseOrderItem poItem = reportdao.getPoItemById(poItemNo);
			StringBuilder errors = new StringBuilder();
			PoDeviationsDto poDeviation = new PoDeviationsDto();
			for(PurchaseOrderError purchaseOrderError: purchaseOrderErrors) {				
				if(poItemNo == purchaseOrderError.getPurchaseOrderItemId()) {
					errors.append(purchaseOrderError.getErrorMst().getErrorMessage()+"; ");
				}
			}
			String errorList = errors.toString();
			errorList = errorList.replaceAll("; $", "");
			poDeviation.setPurchaseOrderNo(poItem.getPurchaseOrder().getPurchaseOrderNo());
			poDeviation.setPoItemId(poItem.getItemMst().getId());
			poDeviation.setPoItemDtl(poItem.getItemMst().getItemDtl());
			poDeviation.setErrors(errorList);
			poDeviations.add(poDeviation);
		}
		
		return poDeviations;
	}

	@Override
	public List<StockListDto> getTodaysPutAway(LocalDate searchDate) {
		try {
			List<Object[]> data = reportdao.getTodaysPutAway(searchDate);
			System.out.println("size in service="+data.size());
			List<StockListDto> sList = new ArrayList<StockListDto>();
			data.forEach(stArray -> {
					String[] str =((String)stArray[0]).split("-");
				 	StockListDto sl  =new StockListDto();
				 	sl.setPlantCode(str[0]);
				 	sl.setStoreLoc(str[1]);
					sl.setStorageBins(str[2]);
					sl.setItemId((String)stArray[1]);
					sl.setItemDtl((String)stArray[2]);
					sl.setApproQty((double)stArray[3]);
					sList.add(sl);				
				});		 
			 return sList;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public List<StockListDto> getPutAwayByDateRange(LocalDate from, LocalDate to) {
		try {
			List<Object[]> data = reportdao.getPutAwayByDateRange(from, to);
			System.out.println("size in service="+data.size());
			List<StockListDto> sList = new ArrayList<StockListDto>();
			data.forEach(stArray -> {
					String[] str =((String)stArray[0]).split("-");
				 	StockListDto sl  =new StockListDto();
				 	sl.setPlantCode(str[0]);
				 	sl.setStoreLoc(str[1]);
					sl.setStorageBins(str[2]);
					sl.setItemId((String)stArray[1]);
					sl.setItemDtl((String)stArray[2]);
					sl.setApproQty((double)stArray[3]);
					sList.add(sl);				
				});		 
			 return sList;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public List<PickingReportDto> getPickingComponents(Long pickingId, String date) {
		// TODO Auto-generated method stub
		List<Object[]> data = reportdao.getPickingComponents(pickingId,date);
		System.out.println("size in service="+data.size());
		List<PickingReportDto> sList = new ArrayList<PickingReportDto>();
		data.forEach(stArray -> {
			System.out.println("stArray[2]"+stArray[0]+"    = "+stArray[2]+" id "+stArray[5]);
			String[] str =((String)stArray[2]).split("-");
			PickingReportDto sl  =new PickingReportDto();
		 	sl.setPlantCode(str[0]);
		 	sl.setLocationCode(str[1]);
			sl.setCompCode((String)stArray[0]);
			sl.setIssueQty((double) stArray[1]);
			System.out.println("Post No.:"+stArray[4]);
			try {
				sl.setItemNo(stArray[3].toString());
				sl.setPostingDocNo((String) stArray[4]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sList.add(sl);				
		});	
		return sList;
	}

	@Override
	public List<PickingReportDto> getPickingComponentsByRange(Long pickingId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		List<Object[]> data = reportdao.getPickingComponentsByRange(pickingId,startDate,endDate);
		System.out.println("size in service="+data.size());
		List<PickingReportDto> sList = new ArrayList<PickingReportDto>();
		data.forEach(stArray -> {
			String[] str =((String)stArray[2]).split("-");
			PickingReportDto sl  =new PickingReportDto();
		 	sl.setPlantCode(str[0]);
		 	sl.setLocationCode(str[1]);
			sl.setCompCode((String)stArray[0]);
			sl.setIssueQty((double) stArray[1]);
			sl.setItemNo(stArray[3].toString());
			sl.setPostingDocNo((String) stArray[4]);
			sList.add(sl);				
		});	
		return sList;
	}

	@Override
	public List<Object[]> getGrnListByItem(String itemMstId) {
		// TODO Auto-generated method stub
		return reportdao.getGrnListByItem(itemMstId);
	}

	@Override
	public List<Object[]> getGrnListByItemDate(String itemMstId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return reportdao.getGrnListByItemDate(itemMstId, startDate, endDate);
	}

	@Override
	public List<Object[]> getPickingComponentByItemDateNull(String itemMstId) {
		// TODO Auto-generated method stub
		return reportdao.getPickingComponentByItemDateNull(itemMstId);
	}

	@Override
	public List<Object[]> getPickingComponentByItemDateNullDateRange(String itemMstId, String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return reportdao.getPickingComponentByItemDateNullDateRange(itemMstId, startDate,endDate);
	}

	@Override
	public List<Object[]> getPendingComponent(String itemMstId) {
		// TODO Auto-generated method stub
		return reportdao.getPendingComponent(itemMstId);
	}

	@Override
	public Object getStockQtyByMaterial(String itemMstId) {
		// TODO Auto-generated method stub
		return reportdao.getStockQtyByMaterial(itemMstId);
	}

	@Override
	public MtlStockIn getmtlStockInByItem(String itemMstId) {
		// TODO Auto-generated method stub
		List<MtlStockIn> mtls=mtlStockInDao.getmtlStockInByItem(itemMstId);
		//System.out.println("Material Stock IN "+mtls.size());
		if(mtls.size()==0){
			return null;
		}else{
			return mtls.get(0);	
		}
		
	}

	@Override
	public List<PendingReport> getPendingReportByDateAndStatus(String reportBy, Date reportDate) {
		// TODO Auto-generated method stub
		System.out.println("Report By "+reportBy);
		try {
			if(reportBy.equalsIgnoreCase("ALL")){
				System.out.println("SER SIZE :: "+pendingReportTableRepo.getPendingReportByDateAndAll(reportDate).size());
				return pendingReportTableRepo.getPendingReportByDateAndAll(reportDate);
			}else{
				System.out.println(" Ser SIZE :: "+pendingReportTableRepo.getPendingReportByDateAndStatus(reportBy,reportDate).size());
		
				return pendingReportTableRepo.getPendingReportByDateAndStatus(reportBy,reportDate);
			}
		} catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
			return null;
			
			
		}
		
	
	
	}

	@Override
	public void updatePendignReport(PendingReport pendingReport) {
		// TODO Auto-generated method stub
		pendingReportTableRepo.save(pendingReport);
	}

	@Override
	public List<PendingReport> getPendingReportByDateAndStatusAndCode(String reportBy, Date reportDate, String code) {
		// TODO Auto-generated method stub
		//System.out.println("reportBy :: "+reportBy+"    reportDate ::  "+reportDate+"   code:: "+code);
		if(reportBy.equalsIgnoreCase("ALL")){
			return pendingReportTableRepo.getPendingReportByDateAndStatusAndCodeAll(reportDate,code);
		}else{
			return pendingReportTableRepo.getPendingReportByDateAndStatusAndCode(reportBy,reportDate,code);
		}
	}

}
