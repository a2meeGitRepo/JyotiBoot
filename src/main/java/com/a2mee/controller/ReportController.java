package com.a2mee.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.ItemMstDto;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.dto.PendingDto;
import com.a2mee.model.dto.PendingReportDto;
import com.a2mee.model.dto.PendingRequestDto;
import com.a2mee.model.dto.PickingDto;
import com.a2mee.model.dto.PickingReportDto;
import com.a2mee.model.dto.PickingRequestDto;
import com.a2mee.model.dto.PoDeviationsDto;
import com.a2mee.model.dto.StockListDto;
import com.a2mee.model.dto.StockRequestDto;
import com.a2mee.model.dto.TransactionDto;
import com.a2mee.model.dto.TransactionRequestDto;
import com.a2mee.Application;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.PendingReport;
import com.a2mee.model.PickingComponent;
import com.a2mee.model.Plant;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.Storage;
import com.a2mee.model.Supplier;
import com.a2mee.model.SupplierComponants;
import com.a2mee.services.CompService;
import com.a2mee.services.ItemMstServices;
import com.a2mee.services.MtlStockInServices;
import com.a2mee.services.PickingService;
import com.a2mee.services.ProOrderService;
import com.a2mee.services.ReportServices;
import com.a2mee.services.SupplierServices;
import com.a2mee.util.API;
import com.a2mee.util.ConfigurationFile;
@EnableScheduling
@RestController
@CrossOrigin("*")
@RequestMapping("report")
public class ReportController {

	@Autowired
	ReportServices reportServices;
	
	@Autowired
	MtlStockInServices mtlStockInServices;
	
	@Autowired
	PickingService pickingService;
	@Autowired
	ItemMstServices itemMstServices;
	//@Autowired
	ConfigurationFile  configurationFile;
	@Autowired
	CompService compService;
	
	@Autowired
	SupplierServices supplierServices;
	@Autowired
	ProOrderService proOrderService;
	
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(API.listStocks)
	public @ResponseBody List<StockListDto> getStocks(@RequestBody StockRequestDto stockRequestDto) {
		try {
			List<StockListDto> finalStockListDto = new ArrayList<>();
			List<ItemMstDto> items = stockRequestDto.getItems();
			List<Plant> plants = stockRequestDto.getPlants();
			List<Storage> stores = stockRequestDto.getStores();
			String date = stockRequestDto.getDate();
			for (Plant plant : plants) {
				for (Storage store : stores) {
					for (ItemMstDto item : items) {
						double approQty = 0;
						double holdQty = 0;
						double rejQty = 0;
						StringBuilder storageBin = new StringBuilder();
						List<StockListDto> stockListDtos = new ArrayList<>();
						String plantCode = plant.getPlant_code();
						String storageLoc = store.getStorage_location();
						String itemId = item.getItemMstId();

						stockListDtos = reportServices.getStocks(plantCode, storageLoc, itemId, date);
						if (stockListDtos.isEmpty()) {
							continue;
						} else if (stockListDtos.size() > 1) {
							for (StockListDto stockListDto : stockListDtos) {
								approQty += stockListDto.getApproQty();
								holdQty += stockListDto.getHoldQty();
								rejQty += stockListDto.getRejQty();
								String[] arrOfStr = stockListDto.getStorageBins().split("-");
								storageBin.append(arrOfStr[2] + "; ");
							}
							String storageBins = storageBin.toString();
							storageBins = storageBins.replaceAll("; $", "");
							StockListDto theStockListDto = new StockListDto();
							theStockListDto.setPlantCode(plantCode);
							theStockListDto.setStoreLoc(storageLoc);
							theStockListDto.setStorageBins(storageBins);
							theStockListDto.setItemId(item.getItemMstId());
							theStockListDto.setItemDtl(item.getItemDtl());
							theStockListDto.setApproQty(approQty);
							theStockListDto.setHoldQty(holdQty);
							theStockListDto.setRejQty(rejQty);
							finalStockListDto.add(theStockListDto);
						} else {
							String[] arrOfStr = stockListDtos.get(0).getStorageBins().split("-");
							stockListDtos.get(0).setStorageBins(arrOfStr[2]);
							finalStockListDto.add(stockListDtos.get(0));
						}
					}
				}
			}
			return finalStockListDto;
		} catch (Exception e) {
			return (List<StockListDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(API.listStocksByDate)
	public @ResponseBody List<StockListDto> getStocksByDate(@RequestBody StockRequestDto stockRequestDto) {
		try {

			String startDate = stockRequestDto.getStartDate();
			String endDate = stockRequestDto.getEndDate();
			Set<String> itemList = new HashSet<>();
			List<StockListDto> fullStock = reportServices.getStocksByDate(startDate, endDate);
			List<StockListDto> finalStocks = new ArrayList<>();

			for (StockListDto stock : fullStock) {
				String item = stock.getItemId();
				itemList.add(item);
			}

			for (String itemId : itemList) {
				double approQty = 0;
				double holdQty = 0;
				double rejQty = 0;
				StringBuilder storageBin = new StringBuilder();
				StringBuilder plant = new StringBuilder();
				StringBuilder store = new StringBuilder();
				List<StockListDto> stockListDtos = reportServices.getStocksByItemAndDate(startDate, endDate, itemId);
				if (stockListDtos.size() > 1) {
					for (StockListDto stockListDto : stockListDtos) {
						approQty += stockListDto.getApproQty();
						holdQty += stockListDto.getHoldQty();
						rejQty += stockListDto.getRejQty();
						String[] arrOfStr = stockListDto.getStorageBins().split("-");
						storageBin.append(arrOfStr[2] + "; ");
						// plant.append(stockListDto.getPlantCode() + "; ");
						// store.append(stockListDto.getStoreLoc() + "; ");
					}
					String storageBins = storageBin.toString();
					storageBins = storageBins.replaceAll("; $", "");
					// String plants = plant.toString();
					// plants = plants.replaceAll("; $", "");
					// String stores = store.toString();
					// stores = stores.replaceAll("; $", "");
					StockListDto theStockListDto = new StockListDto();
					theStockListDto.setStorageBins(storageBins);
					theStockListDto.setPlantCode(stockListDtos.get(0).getPlantCode());
					theStockListDto.setStoreLoc(stockListDtos.get(0).getStoreLoc());
					theStockListDto.setItemId(itemId);
					theStockListDto.setItemDtl(stockListDtos.get(0).getItemDtl());
					theStockListDto.setApproQty(approQty);
					theStockListDto.setHoldQty(holdQty);
					theStockListDto.setRejQty(rejQty);
					finalStocks.add(theStockListDto);
				} else {
					String[] arrOfStr = stockListDtos.get(0).getStorageBins().split("-");
					stockListDtos.get(0).setStorageBins(arrOfStr[2]);
					finalStocks.add(stockListDtos.get(0));
				}
			}

			return finalStocks;

		} catch (Exception e) {
			return (List<StockListDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.todaysGrn)
	public List<GrnDto> getTodaysGrn(@RequestParam String date) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate searchDate = LocalDate.parse(date, df);
		return reportServices.getTodaysGrn(searchDate);
	}

	@GetMapping(API.grnByDateRange)
	public List<GrnDto> getGrnByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
		LocalDate from = LocalDate.parse(startDate);
		LocalDate to = LocalDate.parse(endDate);
		return reportServices.getGrnByDateRange(from, to);
	}

	@GetMapping(API.todaysDeviation)
	public List<PoDeviationsDto> getTodaysDeviation(@RequestParam String date) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate searchDate = LocalDate.parse(date, df);
		return reportServices.getTodaysDeviation(searchDate);
	}

	@GetMapping(API.DeviationByDateRange)
	public List<PoDeviationsDto> getDeviationByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
		LocalDate from = LocalDate.parse(startDate);
		LocalDate to = LocalDate.parse(endDate);
		return reportServices.getDeviationByDateRange(from, to);
	}

	@GetMapping(API.todaysPutAway)
	public List<StockListDto> getTodaysPutAway(@RequestParam String date) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate searchDate = LocalDate.parse(date, df);
		return reportServices.getTodaysPutAway(searchDate);
	}

	@GetMapping(API.putAwayByDateRange)
	public List<StockListDto> putAwayByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
		LocalDate from = LocalDate.parse(startDate);
		LocalDate to = LocalDate.parse(endDate);
		return reportServices.getPutAwayByDateRange(from, to);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(API.listPickings)
	public @ResponseBody List<PickingReportDto> getPickingList(@RequestBody PickingRequestDto stockRequestDto) {
		
		try {
			List<PickingReportDto> finalPickingReportDtos = new ArrayList<>();
			List<PickingDto> pickingDtos = stockRequestDto.getPickingDtos();

			String date = stockRequestDto.getDate();

			for (PickingDto pickingDto : pickingDtos) {

				List<PickingReportDto> pickingReportDtos = reportServices.getPickingComponents(pickingDto.getPickingId(), date);
				System.out.println("Picking Report "+pickingReportDtos.size());
				
				if (pickingReportDtos.isEmpty()) {
					continue;
				} else{
					for (PickingReportDto pickingReportDto : pickingReportDtos) {
						if(finalPickingReportDtos.stream().anyMatch(componentDto -> componentDto.getCompCode() == pickingReportDto.getCompCode())) {
							finalPickingReportDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingReportDto.getCompCode())).forEach(componentDto -> componentDto.setIssueQty(componentDto.getIssueQty()+pickingReportDto.getIssueQty()));
							
						}else {
							PickingReportDto finalPickingReportDto = new PickingReportDto();
							finalPickingReportDto.setCompCode(pickingReportDto.getCompCode());
							finalPickingReportDto.setIssueQty(pickingReportDto.getIssueQty());
							finalPickingReportDto.setPlantCode(pickingReportDto.getPlantCode());
							finalPickingReportDto.setLocationCode(pickingReportDto.getLocationCode());
							finalPickingReportDto.setProOrdNo(pickingDto.getProOrdNo());
							finalPickingReportDto.setReservationNo(pickingDto.getReservationNo());
							finalPickingReportDto.setMovement("261");
							System.out.println("Posting No.:"+pickingReportDto.getPostingDocNo());
							finalPickingReportDto.setItemNo(pickingReportDto.getItemNo());
							finalPickingReportDto.setPostingDocNo(pickingReportDto.getPostingDocNo());

							finalPickingReportDtos.add(finalPickingReportDto);
						}
					}
					
				} 
			}

			return finalPickingReportDtos;
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PickingReportDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(API.listPickingsByDate)
	public @ResponseBody List<PickingReportDto> getPickingsByDate(@RequestBody PickingRequestDto pickingRequestDto) {
		try {

			List<PickingReportDto> finalPickingReportDtos = new ArrayList<>();
			List<PickingDto> pickingDtos = pickingRequestDto.getPickingDtos();

			String startDate = pickingRequestDto.getStartDate();
			String endDate = pickingRequestDto.getEndDate();

			for (PickingDto pickingDto : pickingDtos) {

				List<PickingReportDto> pickingReportDtos = reportServices.getPickingComponentsByRange(pickingDto.getPickingId(), startDate,endDate);
				if (pickingReportDtos.isEmpty()) {
					continue;
				} else{
					for (PickingReportDto pickingReportDto : pickingReportDtos) {
						if(finalPickingReportDtos.stream().anyMatch(componentDto -> componentDto.getCompCode() == pickingReportDto.getCompCode())) {
							finalPickingReportDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingReportDto.getCompCode())).forEach(componentDto -> componentDto.setIssueQty(componentDto.getIssueQty()+pickingReportDto.getIssueQty()));
							
						}else {
							PickingReportDto finalPickingReportDto = new PickingReportDto();
							finalPickingReportDto.setCompCode(pickingReportDto.getCompCode());
							finalPickingReportDto.setIssueQty(pickingReportDto.getIssueQty());
							finalPickingReportDto.setPlantCode(pickingReportDto.getPlantCode());
							finalPickingReportDto.setLocationCode(pickingReportDto.getLocationCode());
							finalPickingReportDto.setProOrdNo(pickingDto.getProOrdNo());
							finalPickingReportDto.setReservationNo(pickingDto.getReservationNo());
							
							finalPickingReportDto.setMovement("261");
							finalPickingReportDto.setItemNo(pickingReportDto.getItemNo());
							finalPickingReportDto.setPostingDocNo(pickingReportDto.getPostingDocNo());
							
							finalPickingReportDtos.add(finalPickingReportDto);
						}
					}
					
				} 
			}

			return finalPickingReportDtos;

		} catch (Exception e) {
			return (List<PickingReportDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(API.listTransaction)
	public @ResponseBody List<TransactionDto> getTransactionList(@RequestParam("itemMstId") String itemMstId) {
		
		try {
			List<TransactionDto> transactions = new ArrayList<>();
			
			List<MtlStockIn> stockList = mtlStockInServices.getStockListByItem(itemMstId);
			
			for (MtlStockIn stock : stockList) {
				TransactionDto transactionDto = new TransactionDto();
				
				transactionDto.setStage("Old Stock");
				transactionDto.setItemMstId(stock.getItemMst().getId());
				transactionDto.setDate(stock.getCreateDate());
				transactionDto.setQty(stock.getInMtlQty());
					
				transactions.add(transactionDto);
			}
			
			List<Object[]> grnList = reportServices.getGrnListByItem(itemMstId);
			
			grnList.forEach(stArray -> {
				TransactionDto transactionDto = new TransactionDto();
				transactionDto.setStage("Material GRN");
				transactionDto.setItemMstId((String) stArray[0]);
				transactionDto.setQty((double) stArray[1]);
				transactionDto.setOrderNo(Long.toString((long) stArray[2]));
				transactionDto.setDate((Date) stArray[3]);
				transactionDto.setEmpName((String) stArray[4]);
				
				transactions.add(transactionDto);
			});
			
			List<Object[]> pickingComponents = reportServices.getPickingComponentByItemDateNull(itemMstId);
			
			pickingComponents.forEach(stArray -> {
				TransactionDto transactionDto = new TransactionDto();
				transactionDto.setStage("Production");
				transactionDto.setItemMstId((String) stArray[0]);
				transactionDto.setQty((double) stArray[1]);
				transactionDto.setOrderNo((String) stArray[2]);
				transactionDto.setDate((Date) stArray[3]);
				transactionDto.setEmpName((String) stArray[4]+" "+(String) stArray[5]);
				transactions.add(transactionDto);
			});

			return transactions;
		} catch (Exception e) {
			return (List<TransactionDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping(API.listTransactionDateRange)
	public @ResponseBody List<TransactionDto> getTransactionList(@RequestBody TransactionRequestDto transactionRequestDto) {
		
		try {
			List<TransactionDto> transactions = new ArrayList<>();
			
			List<MtlStockIn> stockList = mtlStockInServices.getStockListByItemDate(transactionRequestDto.getItemMstId(), transactionRequestDto.getStartDate(), transactionRequestDto.getEndDate());
			
			for (MtlStockIn stock : stockList) {
				TransactionDto transactionDto = new TransactionDto();
	
				transactionDto.setStage("Old Stock");
				transactionDto.setItemMstId(stock.getItemMst().getId());
				transactionDto.setDate(stock.getCreateDate());
				transactionDto.setQty(stock.getInMtlQty());
					
				transactions.add(transactionDto);
			}
			
			List<Object[]> grnList = reportServices.getGrnListByItemDate(transactionRequestDto.getItemMstId(), transactionRequestDto.getStartDate(), transactionRequestDto.getEndDate());
			
			grnList.forEach(stArray -> {
				TransactionDto transactionDto = new TransactionDto();
				
				transactionDto.setStage("Material GRN");
				transactionDto.setItemMstId((String) stArray[0]);
				transactionDto.setQty((double) stArray[1]);
				transactionDto.setOrderNo(Long.toString((long) stArray[2]));
				transactionDto.setDate((Date) stArray[3]);
				transactionDto.setEmpName((String) stArray[4]);
				
				transactions.add(transactionDto);
			});
			
			
			List<Object[]> pickingComponents = reportServices.getPickingComponentByItemDateNullDateRange(transactionRequestDto.getItemMstId(), transactionRequestDto.getStartDate(), transactionRequestDto.getEndDate());
			
			pickingComponents.forEach(stArray -> {
				TransactionDto transactionDto = new TransactionDto();
				transactionDto.setStage("Production");
				transactionDto.setItemMstId((String) stArray[0]);
				transactionDto.setQty((double) stArray[1]);
				transactionDto.setOrderNo((String) stArray[2]);
				transactionDto.setDate((Date) stArray[3]);
				transactionDto.setEmpName((String) stArray[4]+" "+(String) stArray[5]);
				transactions.add(transactionDto);
				
			});

			return transactions;
		} catch (Exception e) {
			return (List<TransactionDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Scheduled(cron = "*/10 * * * * ?")
	//@Scheduled(cron = "0 28 9 ? * *")
	//@GetMapping(API.listPending)
	//@Scheduled(fixedDelay = 900000)
	//@Scheduled(cron = "0 37 16 ? * *")
	public @ResponseBody void getPendingList() {
		
		try {
			List<PendingReport> pendings = new ArrayList<PendingReport>();
			//System.out.println("HELLO PENDING WORK  BEFORE ------------------------------");
			List<ItemMstDto> list =itemMstServices.getItemList();
			//System.out.println("HELLO PENDING WORK ------------------------------"+list.size());
			for (ItemMstDto pendingRequestDto : list) {
				
				List<Object[]> pendingList = reportServices.getPendingComponent(pendingRequestDto.getItemMstId());
				System.out.println("ITEMS MST ID :: "+pendingRequestDto.getItemMstId());
				System.out.println("PendingList SIZE  :: "+pendingList.size());
				pendingList.forEach(stArray -> {
//					if(pendings.stream().anyMatch(pendingDto -> pendingDto.getCompCode() == (String) stArray[5] && pendingDto.getAssignDate().toString().equals((String) stArray[0]))) {
//						pendings.stream().filter(pendingDto -> pendingDto.getCompCode().equals((String) stArray[5]) && pendingDto.getAssignDate().toString().equals((String) stArray[0])).forEach(pendingDto -> pendingDto.setPickCompQty(pendingDto.getPickCompQty()+(double) stArray[7]));
//						
//					}else {
					System.out.println("Componant Code "+(String) stArray[5]);
					MtlStockIn mtlStockIn = reportServices.getmtlStockInByItem((String) stArray[5]);

					PendingReport pendingDto = new PendingReport();
						pendingDto.setAssignDate((Date) stArray[0]);
						pendingDto.setModelCode((String) stArray[1]);
						pendingDto.setPicker((String) stArray[2]+" "+(String) stArray[3]);
						pendingDto.setProOrdNo((String) stArray[4]);
						pendingDto.setCompCode((String) stArray[5]);
						//ComponentMst componentMst=compService.getCompByComCodes(pendingDto.getCompCode());
						//System.out.println("Com Desc : "+componentMst.getCompDesc());
						pendingDto.setCompDesc((String) stArray[6]);
						pendingDto.setPickCompQty((double) stArray[7]);
						pendingDto.setUom(pendingRequestDto.getUom());
						pendingDto.setReportDate(new Date());
							if(mtlStockIn !=null){
								if(mtlStockIn.getRemainQty()<pendingDto.getPickCompQty()){
									pendingDto.setStockQty(mtlStockIn.getRemainQty());
									pendingDto.setStatus("Short Material");
									pendings.add(pendingDto);
								}
							}else{
										pendingDto.setStatus("Pending Material");
										pendings.add(pendingDto);
							}
							
							
					
						
				//	}
					
				});
			}
			itemMstServices.savePendingReport(pendings);

			
		} catch (Exception e) {
			}
	}
		//@Scheduled(cron = "*/10 * * * * ?")
		@Scheduled(cron = "0 48 4 ? * *")
		//@GetMapping(API.listPending)
		//@Scheduled(fixedDelay = 900000)
		@GetMapping(API.pendingReportManualCall)
		public @ResponseBody void getPendingList2() {
			
			try {
				List<PendingReport> pendings = new ArrayList<PendingReport>();
				List<ItemMstDto> list =itemMstServices.getItemList();
				System.out.println("List :: "+list.size());
				for (ItemMstDto pendingRequestDto : list) {
					//System.out.println("pendingRequestDto.getItemMstId() :: "+pendingRequestDto.toString());
					List<Object[]> pendingList = reportServices.getPendingComponent(pendingRequestDto.getItemMstId());
					System.out.println("ITEMS MST ID :: "+pendingRequestDto.getItemMstId());
					System.out.println("PendingList SIZE  :: "+pendingList.size());
					pendingList.forEach(stArray -> {
						

					
					
						
//						if(pendings.stream().anyMatch(pendingDto -> pendingDto.getCompCode() == (String) stArray[5] && pendingDto.getAssignDate().toString().equals((String) stArray[0]))) {
//							pendings.stream().filter(pendingDto -> pendingDto.getCompCode().equals((String) stArray[5]) && pendingDto.getAssignDate().toString().equals((String) stArray[0])).forEach(pendingDto -> pendingDto.setPickCompQty(pendingDto.getPickCompQty()+(double) stArray[7]));
//							
//						}else {
						System.out.println("Componant Code "+(String) stArray[5]);
						MtlStockIn mtlStockIn = reportServices.getmtlStockInByItem((String) stArray[5]);

						PendingReport pendingDto = new PendingReport();
							pendingDto.setAssignDate((Date) stArray[0]);
							pendingDto.setModelCode((String) stArray[1]);
							pendingDto.setPicker((String) stArray[2]+" "+(String) stArray[3]);
							pendingDto.setProOrdNo((String) stArray[4]);
							pendingDto.setCompCode((String) stArray[5]);
							pendingDto.setCompDesc((String) stArray[6]);
							pendingDto.setPickCompQty((double) stArray[7]);
							pendingDto.setUom(pendingRequestDto.getUom());
							pendingDto.setMtlGroup(pendingRequestDto.getMtlGroup());
							pendingDto.setReportDate(new Date());
							System.out.println("CHECKK   == "+stArray[4]);
							//ComponentMst componentMst=compService.getCompByComCodes(pendingDto.getCompCode());
							//System.out.println("Com Desc : "+componentMst.getCompDesc());
					/*		ProductionOrder productionOrder=new ProductionOrder();
							
						if(stArray[4]=="null"|| stArray[4]==""){
							productionOrder=null;
						}else{*/
							ProductionOrder productionOrder= proOrderService.getProductionOrderByPONO((String) stArray[4]);

						//}
							
							
							if (productionOrder!=null) {
							
								pendingDto.setPriorityOrder(productionOrder.getPriorityOrder());
								pendingDto.setOrderStartDate(productionOrder.getStartDateActual());
							}
							//System.out.println("Model Code  :: "+(String) stArray[1]);
							//System.out.println("componentsMst :: "+(String) stArray[6]);
							ComponentMst  componentsMst= compService.getCompByComCodes((String) stArray[6],(String) stArray[1]);
							//System.out.println("componentsMst :: "+(String) stArray[5]);
							if(componentsMst!=null){
								///System.out.println("Asssembly Priority ----- :: "+componentsMst.getAssembly().getMaterialStagePriority());
								pendingDto.setAssemblyMatStagePriority(componentsMst.getAssembly().getMaterialStagePriority());
							}
							//System.out.println("supplierComponants Comp  :: "+pendingDto.getCompCode());
							List<SupplierComponants> supplierComponants=supplierServices.getSupplierComponantsByItem( pendingDto.getCompCode());
							//System.out.println("supplierComponants List  :: "+supplierComponants.size());
							if(supplierComponants.size()!=0){
								Supplier supplier= new Supplier();
								int priority=1;
								double unitPrice=0;
								for(SupplierComponants componants:supplierComponants){
									
									if(componants.getPriority()==priority){
									//	System.out.println("Priotirt "+componants.getPriority());
									//	System.out.println("Priotirt "+componants.getSupplier().getSupplierName());
										supplier= componants.getSupplier();
										unitPrice=componants.getUnitPrice();
									}else if (componants.getPriority()<priority){
										supplier= componants.getSupplier();
										unitPrice=componants.getUnitPrice();
									}
									priority=componants.getPriority();
											
								}
								//System.out.println("SET SUPPLIER "+supplier.getSupplierName());
								pendingDto.setSupplier(supplier);
								pendingDto.setUnitPrice(unitPrice);
								//pendingDto.setTotalPrice(Double.parseDouble(pendingDto.getUnitPrice()*pendingDto.getPickCompQty());
									
							}
							pendingDto.setMaterialSource(pendingRequestDto.getMaterialSource());
													pendingDto.setMatMasterLog(pendingRequestDto.getMatMstLog());
							pendingDto.setScmMode(pendingRequestDto.getScmMode());
							pendingDto.setDerivedPaymentPriority(pendingDto.getAssemblyMatStagePriority());
							pendingDto.setPaymentType("LC");
									
								if(mtlStockIn !=null){
									System.out.println("+++ SAVE SHORD MATERIL+++");
									if(mtlStockIn.getRemainQty()<pendingDto.getPickCompQty()){
										pendingDto.setStockQty(mtlStockIn.getRemainQty());
										pendingDto.setStatus("Short Material");
										itemMstServices.savePendingReport1(pendingDto);

										//pendings.add(pendingDto);
									}
								}else{
									System.out.println("+++ PENDIGN MATERIL+++");
											pendingDto.setStatus("Pending Material");
											itemMstServices.savePendingReport1(pendingDto);
											//pendings.add(pendingDto);
											
								}
								
								
						
							
					//	}
						
					});
				}
				//itemMstServices.savePendingReport(pendings);

				
			} catch (Exception e) {
				e.printStackTrace();
				}
		}
	
	@SuppressWarnings("unchecked")
	@PostMapping(API.getPendingReport)
	public @ResponseBody List<PendingReport> getPendingReport(@RequestBody PendingReportDto reportDto) {
		
		try {
			//System.out.println("reportDto :: "+reportDto.toString());
			
			String reportBy="";
			if(reportDto.getReportBy().equalsIgnoreCase("Short")){
				reportBy ="Short Material";
			}else{
				reportBy ="All";
			}
			//System.out.println("reportBy :: "+reportBy);
			List<PendingReport> pendings =reportServices.getPendingReportByDateAndStatus(reportBy,reportDto.getReportDate());
			System.out.println("pendings :: "+pendings.size());
			return pendings;
			
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PendingReport>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

			}
	}
//	@SuppressWarnings("unchecked")
	@PostMapping(API.getDetialPendingReport)
	public @ResponseBody List<PendingReport> getDetialPendingReport(@RequestBody PendingReportDto reportDto) {
		
		try {
			List<PendingReport> pendingsReturn= new ArrayList<PendingReport>();
			System.out.println("reportDto :: "+reportDto.toString());
			
			String reportBy="";
			if(reportDto.getReportBy().equalsIgnoreCase("Short")){
				reportBy ="Short Material";
			}else{
				reportBy ="All";
			}
			//System.out.println("reportBy :: "+reportBy);
			List<PendingReport> pendings =reportServices.getPendingReportByDateAndStatus(reportBy,reportDto.getReportDate());
			System.out.println("pendings :: "+pendings.size());
			Set<String> codes= new  HashSet<String>();
			for (PendingReport report:pendings){
				codes.add(report.getCompCode());
				
			}
			//System.out.println("CODEs SIZE  :: "+codes.size());
			for (String  code:codes){
				//System.out.println("CODE :: "+code);
				double total =0;
				List<PendingReport> pendings2 =reportServices.getPendingReportByDateAndStatusAndCode(reportBy,reportDto.getReportDate(),code);
				//System.out.println("Pendings with code size :: "+pendings2.size());
				if(pendings2.size()!=0){
					for (PendingReport  pendingReport1:pendings2){
						total+=pendingReport1.getPickCompQty();
					}
					PendingReport pendingReport= pendings2.get(0);
					pendingReport.setPickCompQty(total);
					pendingsReturn.add(pendingReport);
				}
				
			}
			
			
			return pendingsReturn;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		//	return (List<PendingReport>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			return null;
			}
		
	}
	@PostMapping(API.listShort)
	public @ResponseBody List<PendingDto> getPendingShortList(@RequestBody List<PendingRequestDto> pendingRequestDtos,@RequestParam("reportBy")  String reportBy) {
		
		try {
			List<PendingDto> pendings = new ArrayList<PendingDto>();
			System.out.println("Report By :: "+reportBy);
			
			for (PendingRequestDto pendingRequestDto : pendingRequestDtos) {
				
				List<Object[]> pendingList = reportServices.getPendingComponent(pendingRequestDto.getItemMstId());
				System.out.println("ITEMS MST ID :: "+pendingRequestDto.getItemMstId());
				System.out.println("PendingList SIZE  :: "+pendingList.size());
				pendingList.forEach(stArray -> {
//					if(pendings.stream().anyMatch(pendingDto -> pendingDto.getCompCode() == (String) stArray[5] && pendingDto.getAssignDate().toString().equals((String) stArray[0]))) {
//						pendings.stream().filter(pendingDto -> pendingDto.getCompCode().equals((String) stArray[5]) && pendingDto.getAssignDate().toString().equals((String) stArray[0])).forEach(pendingDto -> pendingDto.setPickCompQty(pendingDto.getPickCompQty()+(double) stArray[7]));
//						
//					}else {
					System.out.println("Componant Code "+(String) stArray[5]);
					MtlStockIn mtlStockIn = reportServices.getmtlStockInByItem((String) stArray[5]);

						PendingDto pendingDto = new PendingDto();
						pendingDto.setAssignDate((Date) stArray[0]);
						pendingDto.setModelCode((String) stArray[1]);
						pendingDto.setPicker((String) stArray[2]+" "+(String) stArray[3]);
						pendingDto.setProOrdNo((String) stArray[4]);
						pendingDto.setCompCode((String) stArray[5]);
						pendingDto.setCompDesc((String) stArray[6]);
						pendingDto.setPickCompQty((double) stArray[7]);
						pendingDto.setUom(pendingRequestDto.getUom());
						if(mtlStockIn.getRemainQty()<pendingDto.getPickCompQty()){
							pendingDto.setStockQty(mtlStockIn.getRemainQty());
							pendingDto.setStatus("Short Material");
							pendings.add(pendingDto);

						}
				//	}
					
				});
			}
			

			return pendings;
		} catch (Exception e) {
			return (List<PendingDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
@Scheduled(cron = "0 58 11 ? * *")
	public void stopJar(){
		System.out.println("STOP JAR");
		LocalDate curdate = LocalDate.now();
		LocalDate hardcodedDate = LocalDate.parse("2021-05-01");
		System.out.println(hardcodedDate.compareTo(curdate)>=0);
		if (curdate.compareTo(hardcodedDate)>=0){
			System.out.println("Licence Exprided ::Contact Appint  to Mee Pune");
			System.exit(0);

		}

		
	}
	

	@PostMapping(API.updatePendingReport)
	public @ResponseBody void  updatePendingReport(@RequestBody PendingReport pendingReport) {
		
		try {
			
			reportServices.updatePendignReport(pendingReport);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			

			}
	}

}
