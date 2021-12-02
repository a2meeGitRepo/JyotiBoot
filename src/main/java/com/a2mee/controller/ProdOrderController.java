package com.a2mee.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.CustomAssembly;
import com.a2mee.model.ItemMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.PurchaseOrderItem;
import com.a2mee.model.RequestAssembly;
import com.a2mee.model.dto.AssemblyRecDto;
import com.a2mee.model.dto.ModelPoDto;
import com.a2mee.model.dto.PickStockDto;
import com.a2mee.model.dto.ResponseDto;
import com.a2mee.services.AssemblyService;
import com.a2mee.services.CompService;
import com.a2mee.services.ModelService;
import com.a2mee.services.ProOrderService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.proOrder)
@CrossOrigin("*")
public class ProdOrderController {

	@Autowired
	ProOrderService proOrderService;

	@Autowired
	AssemblyService assemblyService;
	
	@Autowired
	CompService compService;
	
	@Autowired
	ModelService modelPlanService;

	/* for Desktop Screen */
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/* for uploading Annual Plan or Sales Order */
	@PostMapping(API.uploadProOrder)
	public @ResponseBody ResponseEntity postFile(ModelMap model, @ModelAttribute(value = "file") MultipartFile file,
			HttpServletRequest request) {
		try {
			if (!(file == null)) {
				if (file.isEmpty()) {
					logger.info("File not found");
				} else {
					logger.info(file.getOriginalFilename());
					try {
						File dir = new File(System.getProperty("catalina.base"), "uploads");
						File uplaodedFile = new File(dir + file.getOriginalFilename());
						file.transferTo(uplaodedFile);
						FileInputStream excelFile = new FileInputStream(uplaodedFile);
						logger.info("hiiii@" + excelFile);
						Workbook workbook = new XSSFWorkbook(excelFile);
						Sheet datatypeSheet = workbook.getSheetAt(0); // contains model plan
						DataFormatter formatter = new DataFormatter();

						List<ProductionOrder> dbProOrders = proOrderService.getAllProOrder();
						List<ProductionOrder> incomingProOrders = new ArrayList<>();
						List<ProductionOrder> finalProOrders = new ArrayList<>();

						int i = 1;
						while (i <= datatypeSheet.getLastRowNum()) { // for saving model plan

							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(i++);
						//	String str = row.getCell(0);
							if( row.getCell(0)==null) {
								continue;
							}
							if (row.getCell(0) != null) {
								System.out.println("START DATE IDEAL"+row.getCell(1).toString());
								Date startDateIdeal = !row.getCell(1).toString().isEmpty()
										? new SimpleDateFormat("yyyy-MMM-dd").parse(row.getCell(1).toString())
										: null;
								Date startDateActual = !row.getCell(2).toString().isEmpty()
										? new SimpleDateFormat("yyyy-MMM-dd").parse(row.getCell(2).toString())
										: null;
								Date machineEndDate = !row.getCell(3).toString().isEmpty()
										? new SimpleDateFormat("yyyy-MMM-dd").parse(row.getCell(3).toString())
										: null;
								Date dispatchDate = !row.getCell(4).toString().isEmpty()
										? new SimpleDateFormat("yyyy-MMM-dd").parse(row.getCell(4).toString())
										: null;

								ProductionOrder proOrd = new ProductionOrder();

								proOrd.setStartDateIdeal(startDateIdeal);
								proOrd.setStartDateActual(startDateActual);
								proOrd.setMachineEndDate(machineEndDate);
								proOrd.setDispatchDate(dispatchDate);
								proOrd.setUid(row.getCell(5).toString());
								proOrd.setHeader(row.getCell(6).toString());
								proOrd.setOct(row.getCell(7).toString());
								proOrd.setModelCode(row.getCell(8).toString());

								row.getCell(9).setCellType(CellType.STRING);
//								System.out.println(row.getCell(9).toString());
								proOrd.setProdOrdNo(Long.parseLong(row.getCell(9).toString()));
								row.getCell(10).setCellType(CellType.STRING);
								proOrd.setFinixId(Long.parseLong(row.getCell(10).toString()));
								row.getCell(11).setCellType(CellType.STRING);
								proOrd.setSalesOrdNo(Long.parseLong(row.getCell(11).toString()));

								proOrd.setCustomerName(row.getCell(12).toString());
								proOrd.setCtrl(row.getCell(13).toString());
								proOrd.setBranch(row.getCell(14).toString());
								proOrd.setPaymentStatus(row.getCell(15).toString());
								proOrd.setOnFloorStatus(row.getCell(16).toString());
								proOrd.setPriorityOrder(row.getCell(17).toString());
								incomingProOrders.add(proOrd);

							}
						}

						if (dbProOrders.size() > 0) {
							for (ProductionOrder incomingProOrder : incomingProOrders) {
								for (ProductionOrder dbProOrder : dbProOrders) {
									if (dbProOrder.getProdOrdNo() == incomingProOrder.getProdOrdNo()
											&& dbProOrder.getSalesOrdNo() == incomingProOrder.getSalesOrdNo()) {
										incomingProOrder.setFlag("F");
									}
								}
							}
						}

						for (ProductionOrder incomingProOrder : incomingProOrders) {
							if (incomingProOrder.getFlag() == null) {
								finalProOrders.add(incomingProOrder);
							}
						}

						if (finalProOrders.size() > 0) {
							proOrderService.saveAll(finalProOrders);
						}

						logger.info("Successfully imported modelplan");
						workbook.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**************** Production Manager ***************************/

	/* for getting sales Order list */
	@GetMapping(API.salesOrderlist)
	public @ResponseBody ResponseEntity getSalesOrders() {
		try {
			List<Long> salesOrders = proOrderService.getSalesOrders();
			return new ResponseEntity(salesOrders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Its for Old Application Start
	
	/* for gettin pro Order on ProOrder number */
	@GetMapping(API.serchByProOno)
	public @ResponseBody ResponseEntity getProOrdersByPo(@RequestParam(value = "proOrderNo") long proOrderNo) {
		try {
			List<ProductionOrder> productionOrders = proOrderService.getProOrdersByPo(proOrderNo);
			return new ResponseEntity(productionOrders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* for getting Pro order on So number */
	@GetMapping(API.serchBySalesOno)
	public @ResponseBody ResponseEntity getProOrdersBySo(@RequestParam(value = "salesOrder") long salesOrder) {
		try {
			List<ProductionOrder> productionOrders = proOrderService.getProOrdersBySo(salesOrder);
			return new ResponseEntity(productionOrders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Its for Old Application End
	
	/* get assemblies from machine code */
	@GetMapping(API.getAssemblies)
	public @ResponseBody ResponseEntity getAssemblies() {
		try {
			List<AssemblyMst> assemblies = assemblyService.getAssemblies();
			return new ResponseEntity(assemblies, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* get assemblies from machine code */
	@GetMapping(API.getComponents)
	public @ResponseBody ResponseEntity getComponents() {
		try {
			List<ComponentMst> components = compService.getComponents();
			return new ResponseEntity(components, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* get assemblies from machine code */
	@GetMapping(API.getAssmByModelCode)
	public @ResponseBody ResponseEntity getAssmByModelCode(@RequestParam(value = "modelCode") String modelCode) {
		try {
			List<AssemblyMst> assemblies = assemblyService.getassemblyByCode(modelCode);
			return new ResponseEntity(assemblies, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* get Production Order by machine code */
	@GetMapping(API.getPOByModelCode)
	public @ResponseBody ResponseEntity getProOrdersByModel(@RequestParam(value = "modelCode") String modelCode) {
		try {
			System.out.println("Model Code:"+modelCode);
			List<ModelPoDto> prodOrds = modelPlanService.getProOrdersByModel(modelCode);
			return new ResponseEntity(prodOrds, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* update as normal approval */
	@GetMapping(API.approved)
	public @ResponseBody ResponseEntity updateApproval(@RequestParam(value = "prodOrdId") long prodOrdId) {
		try {
			proOrderService.updateApproval(prodOrdId);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Create custom assemblies */
	@PostMapping(API.createCustomAssm)
	public @ResponseBody ResponseEntity createCustomAssm(@RequestBody List<AssemblyRecDto> customAssmDtos) {
		try {
			proOrderService.updateCustomApproval(customAssmDtos.get(0).getProOrderNo());
			ProductionOrder proOrder = proOrderService.getProOrdersByPo(customAssmDtos.get(0).getProOrderNo()).get(0);
			List<CustomAssembly> customAssms = new ArrayList<>();
			for (AssemblyRecDto customAssmDto : customAssmDtos) {
				CustomAssembly customAssembly = new CustomAssembly();
				customAssembly.setAssemblyCode(customAssmDto.getAssemblyCode());
				customAssembly.setAssemblyDesc(customAssmDto.getAssemblyDesc());
				customAssembly.setAssemblyQty(customAssmDto.getAssemblyQty());
				customAssembly.setModel(customAssmDto.getModel());
				customAssembly.setProductionOrder(proOrder);
				customAssembly.setUom(customAssmDto.getUom());
				customAssms.add(customAssembly);
			}
			proOrderService.addCustomAssm(customAssms);

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**************** Production Engineer ***************************/
	/* for getting sales Order list */
	@GetMapping(API.getApprovedPo)
	public @ResponseBody ResponseEntity getApprovedPo() {
		try {
			List<ProductionOrder> proOrders = proOrderService.getApprovedPo();
			return new ResponseEntity(proOrders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* get requested assms by proOrd No. */
	@GetMapping(API.getReqAssmsByPo)
	public @ResponseBody ResponseEntity getReqAssms(@RequestParam(value = "prodOrdNo") long prodOrdNo) {
		try {
			List<RequestAssembly> reqAssms = proOrderService.getReqAssms(prodOrdNo);
			return new ResponseEntity(reqAssms, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* get requested assms by proOrd No. */
	@GetMapping(API.getCustomAssembly)
	public @ResponseBody ResponseEntity getCustomAssembly(@RequestParam(value = "modelCode") String modelCode,
			@RequestParam(value = "prodOrdNo") long prodOrdNo) {
		try {
			List<CustomAssembly> cusAssms = proOrderService.getCustomAssembly(modelCode, prodOrdNo);
			return new ResponseEntity(cusAssms, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* get all requested assms */
	@GetMapping(API.getReqAssms)
	public @ResponseBody ResponseEntity getReqAssms() {
		try {
			List<RequestAssembly> reqAssms = proOrderService.getReqAssms();
			return new ResponseEntity(reqAssms, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Create request assembly */
	@PostMapping(API.createReqAssm)
	public @ResponseBody ResponseEntity createReqAssm(@RequestBody AssemblyRecDto assemblyRecDto) {
		try {
			ProductionOrder proOrder = proOrderService.getProOrdersByPo(assemblyRecDto.getProOrderNo()).get(0);

			RequestAssembly reqAssembly = new RequestAssembly();
			reqAssembly.setAssemblyCode(assemblyRecDto.getAssemblyCode());
			reqAssembly.setAssemblyDesc(assemblyRecDto.getAssemblyDesc());
			reqAssembly.setAssemblyQty(assemblyRecDto.getAssemblyQty());
			reqAssembly.setModel(assemblyRecDto.getModel());
			reqAssembly.setProductionOrder(proOrder);
			reqAssembly.setUom(assemblyRecDto.getUom());
			reqAssembly.setRemark(assemblyRecDto.getRemark());
			reqAssembly.setRequestDateTime(assemblyRecDto.getRequestDateTime());
			reqAssembly.setRequestedBy(assemblyRecDto.getRequestedBy());
			reqAssembly.setRequiredDate(assemblyRecDto.getRequiredDate());
			reqAssembly.setStatus(assemblyRecDto.getStatus());

			proOrderService.addReqAssm(reqAssembly);

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Create request assemblies */
	@PostMapping(API.createReqAssms)
	public @ResponseBody ResponseEntity createReqAssms(@RequestBody List<AssemblyRecDto> assemblyRecDtos) {
		try {
			ProductionOrder proOrder = proOrderService.getProOrdersByPo(assemblyRecDtos.get(0).getProOrderNo()).get(0);

			List<RequestAssembly> reqAssemblies = new ArrayList<>();

			for (AssemblyRecDto assemblyRecDto : assemblyRecDtos) {
				RequestAssembly reqAssembly = new RequestAssembly();
				reqAssembly.setAssemblyCode(assemblyRecDto.getAssemblyCode());
				reqAssembly.setAssemblyDesc(assemblyRecDto.getAssemblyDesc());
				reqAssembly.setAssemblyQty(assemblyRecDto.getAssemblyQty());
				reqAssembly.setModel(assemblyRecDto.getModel());
				reqAssembly.setProductionOrder(proOrder);
				reqAssembly.setUom(assemblyRecDto.getUom());
				reqAssembly.setRemark(assemblyRecDto.getRemark());
				reqAssembly.setRequestDateTime(assemblyRecDto.getRequestDateTime());
				reqAssembly.setRequestedBy(assemblyRecDto.getRequestedBy());
				reqAssembly.setRequiredDate(assemblyRecDto.getRequiredDate());
				reqAssembly.setStatus(assemblyRecDto.getStatus());
				reqAssemblies.add(reqAssembly);
			}

			proOrderService.addReqAssms(reqAssemblies);

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* received assembly update */
	@GetMapping(API.assmReceived)
	public @ResponseBody ResponseEntity assmReceived(@RequestParam(value = "reqAssmId") long reqAssmId) {
		try {
			proOrderService.assmReceived(reqAssmId);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* consumed assembly update */
	@GetMapping(API.consumeAssm)
	public @ResponseBody ResponseEntity consumeAssm(@RequestParam(value = "reqAssmId") long reqAssmId) {
		try {
			ResponseDto response = proOrderService.consumeAssm(reqAssmId);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* get all requested assms - Picking Manager View */
	@GetMapping(API.getRequestedAssms)
	public @ResponseBody ResponseEntity getRequestedAssms() {
		try {
			List<RequestAssembly> reqAssms = proOrderService.getRequestedAssms();
			return new ResponseEntity(reqAssms, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* get all requested assms - Picking Manager View */
	@GetMapping(API.getProOrdNo)
	public @ResponseBody ResponseEntity<List<Long>> getProOrdNo(@PathVariable("modelCode") String modelCode) {
		try {
			System.out.println("Model Code:"+modelCode);
			List<Long> proOrdNos = proOrderService.getProOrdNo(modelCode);
			return new ResponseEntity(proOrdNos, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	@GetMapping(API.getTotalPOCount)
	public @ResponseBody int getTotalPOCount() {
		try {
			return proOrderService.getProductionOrders().size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	@GetMapping(API.getProductionOrders)
	public @ResponseBody ResponseEntity getProductionOrders() {
		try {
			List<ProductionOrder> reqAssms = proOrderService.getProductionOrders();
			return new ResponseEntity(reqAssms, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	@GetMapping(API.getProductionOrdersByPagination)
	public @ResponseBody ResponseEntity getProductionOrdersByPagination(@PathVariable("pageno") int pageno,@PathVariable("perPage") int perPage) {
		try {
			List<ProductionOrder> reqAssms = proOrderService.getProductionOrdersByPagination(pageno,perPage);
			return new ResponseEntity(reqAssms, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/*@PostMapping(API.updateProductionOrder)
	public @ResponseBody ResponseEntity updateProductionOrder(@RequestBody ProductionOrder productionOrder)
	{		
		try{
			proOrderService.updateProductionOrder(productionOrder);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}*/	
	@PostMapping(API.updateProductionOrder)
	public @ResponseBody ResponseEntity updateProductionOrder(@RequestBody ProductionOrder productionOrder)
	{		
		try{
			proOrderService.updateProductionOrder(productionOrder);
			//System.out.print("hello");
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping(API.updateStatusProductionOrder)
	public @ResponseBody ResponseEntity updateStatusProductionOrder(@RequestBody ProductionOrder productionOrder)
	{		
		try{
			if(productionOrder.getActive()==1){
				productionOrder.setActive(0);
			}else{
				productionOrder.setActive(1);
			}
			proOrderService.updateProductionOrder(productionOrder);
			//System.out.print("hello");
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
}
