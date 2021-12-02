
package com.a2mee.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.DeleteMapping;
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

import com.a2mee.model.ErrorMst;
import com.a2mee.model.ItemMst;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.PurchaseOrderItem;
import com.a2mee.model.VendorMst;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.dto.PurchaseOrderCsvDto;
import com.a2mee.model.dto.PurchaseOrderDto;
import com.a2mee.model.dto.PurchaseOrderErrDto;
import com.a2mee.services.ErrorMstServices;
import com.a2mee.services.ItemMstServices;
import com.a2mee.services.PurchaseOrderErrService;
import com.a2mee.services.PurchaseOrderItemService;
import com.a2mee.services.PurchaseOrderService;
import com.a2mee.services.VendorMstServices;
import com.a2mee.util.API;
import com.opencsv.CSVReader;

@RestController
@RequestMapping(API.purchaseOrder)
@CrossOrigin("*")
public class PurchaseOrderController {

	@Autowired
	PurchaseOrderService purchaseOrderService;

	@Autowired
	ItemMstServices itemMstServices;

	@Autowired
	VendorMstServices vendorMstServices;
	
	@Autowired
	ErrorMstServices errorMstServices;

	@Autowired
	PurchaseOrderItemService purchaseOrderItemService;
	
	@Autowired
	private PurchaseOrderErrService purchaseOrderErrorService;
	

	/* for Desktop Screen */
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/* Get entire PO count
	 * Required for Testing - can be commented out
	*/	
	@GetMapping(API.getTotalCount)
	public @ResponseBody int getTotalCount() {
		try {
		return purchaseOrderService.getTotalCount();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/* Get entire PO
	 * Required for Testing - can be commented out
	*/	
	@GetMapping(API.getPurchaseOrders)
	public @ResponseBody List<PurchaseOrder> getPurchaseOrders(@RequestParam("pageNo") int pageNo,
			@RequestParam("itemPerPage") int itemPerPage) {
		try {
		return purchaseOrderService.getPurchaseOrders(pageNo ,itemPerPage);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrder>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Send Entire PO Data
	 * When searched by VenID and ItemId
	*/	
	@GetMapping(API.submitPurchaseOrder)
	public @ResponseBody List<PurchaseOrderDto> selectedPurchaseOrderList(@RequestParam("venId") String venId,
			@RequestParam("itemId") String itemId) {
		log.debug("start "+this.getClass().getName()+" selectedPurchaseOrderList : venId = "+ venId+" itemId = "+itemId);
		try {
		return purchaseOrderService.getPurchaseOrderList(venId, itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrderDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Send PO Data that doesn't have any error or whose GRN is not created
	 * When searched by VenID and ItemId
	*/	
	@GetMapping(API.submitNoErrPurchaseOrder)
	public @ResponseBody List<PurchaseOrderDto> selectedNoErrPurchaseOrderList(@RequestParam("venId") String venId,
			@RequestParam("itemId") String itemId) {
		//System.out.println("start "+this.getClass().getName()+" selectedPurchaseOrderList : venId = "+ venId+" itemId = "+itemId);
		try {
		return purchaseOrderService.getNoErrPurchaseOrderList(venId, itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrderDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Send PO Data that has errors
	 * When searched by VenID and ItemId
	*/	
	@GetMapping(API.submitErrPurchaseOrder)
	public @ResponseBody List<PurchaseOrderErrDto> selectedErrPurchaseOrderList(@RequestParam("venId") String venId,
			@RequestParam("itemId") String itemId) {
		log.debug("start "+this.getClass().getName()+" selectedPurchaseOrderList : venId = "+ venId+" itemId = "+itemId);
		try {
		return purchaseOrderService.getErrPurchaseOrderList(venId, itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrderErrDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.inwardedPo)
	public @ResponseBody List<PurchaseOrder> getInwardedPo() {
		try {
			return purchaseOrderService.getInwardedPo();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrder>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Send Entire PO Data
	 * When searched by PO Number
	*/	
	@GetMapping(API.searchByPurchaseOrderno)
	public @ResponseBody List<PurchaseOrderDto> searchByPurchaseOrderNo(@RequestParam("purchaseOrderNo") long purchaseOrderNo) {
		log.debug("start "+this.getClass().getName()+" serchByPurchaseOrderNo : purchaseOrderNo = "+ purchaseOrderNo);
		try {
			return purchaseOrderService.searchByPurchaseOrderNo(purchaseOrderNo);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrderDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Send Entire PO Data
	 * When searched by PO ID
	*/	
	@GetMapping(API.searchByPurchaseOrderId)
	public @ResponseBody PurchaseOrder searchByPurchaseOrderId(@RequestParam("purchaseOrderNo") long purchaseOrderId) {
		log.debug("start "+this.getClass().getName()+" serchByPurchaseOrderNo : purchaseOrderNo = "+ purchaseOrderId);
		try {
			return purchaseOrderService.searchByPurchaseOrderId(purchaseOrderId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/* Send PO Data that doesn't have any error or whose GRN is not created
	 * When searched by PO Number
	 *
	*/	
	@GetMapping(API.searchByNoErrPurchaseOrderno)
	public @ResponseBody List<PurchaseOrderDto> searchByNoErrPurchaseOrderNo(@RequestParam("purchaseOrderNo") long purchaseOrderNo) {
		try {
			return purchaseOrderService.searchByNoErrPurchaseOrderNo(purchaseOrderNo);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrderDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Send PO Data that has errors
	 * When searched by PO Number
	 * 
	 * 
	*/	
	@GetMapping(API.searchByErrPurchaseOrderno)
	public @ResponseBody List<PurchaseOrderErrDto> searchByErrPurchaseOrderNo(@RequestParam("purchaseOrderNo") long purchaseOrderNo) {
		log.debug("start "+this.getClass().getName()+" serchByPurchaseOrderNo : purchaseOrderNo = "+ purchaseOrderNo);
		try {
			return purchaseOrderService.searchByErrPurchaseOrderNo(purchaseOrderNo);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrderErrDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Retrieve Material List on call
	 * 
	 * 
	*/	
	@GetMapping(API.poItemList)
	public @ResponseBody List<MasterDto> getItemList(@RequestParam("venId") String venId) {
		log.debug("start "+this.getClass().getName()+" getItemList : venId = "+ venId);
		try{
			return itemMstServices.getItemList(venId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<MasterDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Retrieve Vendor List on call
	 * 
	 * 
	*/	
	@GetMapping(API.poVenList)
	public @ResponseBody List<MasterDto> getVenList() {
	
		try{
			return vendorMstServices.getvenList();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<MasterDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Retrieve Error Mst on call
	 * 
	 * 
	*/
	@GetMapping(API.errorList)
	public @ResponseBody List<ErrorMst> getErrorList() {
		log.debug("start "+this.getClass().getName()+" getErrorList method run ");
		try{
			return errorMstServices.getErrorList();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<ErrorMst>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Update selected purchase order details for GRN
	 * 
	 * 
	*/
	@PostMapping(API.updateList)
	public @ResponseBody ResponseEntity updatePuOrder(@RequestBody List<PurchaseOrderItem> purchaseOrderItems)
	{		
		try{
			purchaseOrderItems.forEach(p -> purchaseOrderItemService.updatePoItem(p));
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	/* Save Deviations 
	 * if the POId and error code is same, it wont be saved(to avoid duplicate entries)
	 *
	*/	
	@PostMapping(API.poDeviations)
	public @ResponseBody ResponseEntity<PurchaseOrderError> savePuOrderErrors(@RequestBody List<PurchaseOrderError> thePurchaseOrderErrors)
	{
		try{
			long purchaseOrderItemId = thePurchaseOrderErrors.get(0).getPurchaseOrderItemId();
			purchaseOrderErrorService.deletePuOrderErrors(purchaseOrderItemId);
			for (PurchaseOrderError thePurchaseOrderError : thePurchaseOrderErrors) {
					purchaseOrderErrorService.savePuOrderErrors(thePurchaseOrderError);
//					System.out.println("outside clause");
				}
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/* Get Error list of a PoId 
	 * if the POId and error code is same, it wont be saved(to avoid duplicate entries)
	 *
	*/	
	@GetMapping(API.getErrorlistByPoItemID)
	public @ResponseBody List<PurchaseOrderError> getErrorListByPoID(@RequestParam("purchaseOrderItemId") long purchaseOrderItemId) {
		log.debug("start "+this.getClass().getName()+" getVenList method run ");
		try{
			return purchaseOrderErrorService.getErrorListByPoItemId(purchaseOrderItemId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<PurchaseOrderError>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Save Deviations 
	 * if the POId and error code is same, it wont be saved(to avoid duplicate entries)
	 *
	*/	
	@PostMapping(API.updateDeviations)
	public @ResponseBody ResponseEntity<PurchaseOrderError> updatePuOrderErrors(@RequestBody List<PurchaseOrderError> thePurchaseOrderErrors)
	{
		try{
			long purchaseOrderItemId = thePurchaseOrderErrors.get(0).getPurchaseOrderItemId();
			boolean flag = purchaseOrderErrorService.deletePuOrderErrors(purchaseOrderItemId);
			if(flag) {
			for (PurchaseOrderError thePurchaseOrderError : thePurchaseOrderErrors) {
//				System.out.println(thePurchaseOrderError.toString());
					purchaseOrderErrorService.savePuOrderErrors(thePurchaseOrderError);
//					System.out.println("outside clause");
				}
			}
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/* Delete Deviations 
	 * if the POId and error code is same, it wont be saved(to avoid duplicate entries)
	 *
	*/
	@GetMapping(API.delDeviations)
	public @ResponseBody ResponseEntity<PurchaseOrderError> delPuOrderErrors(@RequestParam("purchaseOrderItemId") long purchaseOrderItemId)
	{
		try{
			boolean flag = purchaseOrderErrorService.delPuOrderErrors(purchaseOrderItemId);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/* Upload CSV file to Database
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(API.uploadCsvPurchaseOrder)
	public @ResponseBody ResponseEntity postFile(ModelMap model, @ModelAttribute(value="file") MultipartFile file, HttpServletRequest request){
		System.out.println("FILE NAME"+file.getOriginalFilename());
		try {
			if (!(file == null)) {
				if (file.isEmpty()) {
					return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
				} else {					
						String rootPath = request.getSession().getServletContext().getRealPath("/");
					    File dir = new File(rootPath + File.separator + "uploadedfile");
					    if (!dir.exists()) {
					        dir.mkdirs();
					    }
					  //  File dir = new File(System.getProperty("catalina.base"), "uploads");
						File uplaodedFile = new File(dir + file.getOriginalFilename());
						file.transferTo(uplaodedFile);
						FileInputStream excelFile = new FileInputStream(uplaodedFile);
					    File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
						Workbook workbook = new XSSFWorkbook(excelFile);
						Sheet datatypeSheet = workbook.getSheetAt(0); 
					
						//CSVReader reader = null;
					
						
						/************Anurag*************/
						List<VendorMst> entireVendorMst = vendorMstServices.getEntireVenMst();
						List<PurchaseOrder> entirePurchaseOrder = purchaseOrderService.getEntirePurchaseOrder();						
						List<ItemMst> entireItemMst = itemMstServices.getEntireItemMst();
						List<PurchaseOrderItem> entirePurchaseOrderItem = purchaseOrderItemService.getEntirePurchaseOrderItem();
						/************Anurag*************/
						
						int j = 1;
						while (j <= datatypeSheet.getLastRowNum())  {
							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(j++);
							PurchaseOrderCsvDto purchaseOrderCsv = new PurchaseOrderCsvDto();
						    purchaseOrderCsv.setPlant(row.getCell(0).toString());
						    System.out.println("LINE1 ::"+row.getCell(2));
						    String poNo=row.getCell(1).toString();
						    
						    
						    System.out.println("LINE1 ::"+poNo);
						    System.out.println("LINE1 ::"+poNo.substring(0,1)+"  "+poNo.substring(2,poNo.length()-2));
						   
						    purchaseOrderCsv.setPurchaseOrder(Long.parseLong(poNo.substring(0,1)+poNo.substring(2,poNo.length()-2)));
						    purchaseOrderCsv.setItem(row.getCell(2).toString());
						    purchaseOrderCsv.setCreated_on(row.getCell(3).toString());
						    purchaseOrderCsv.setOrder_qty(Double.parseDouble(row.getCell(4).toString()));
						    purchaseOrderCsv.setOpen_qty(Double.parseDouble(row.getCell(5).toString()));
						    purchaseOrderCsv.setVendorCode(row.getCell(6).toString());
						    purchaseOrderCsv.setVendorName(row.getCell(7).toString());
						    purchaseOrderCsv.setPurchaseOrg(row.getCell(8).toString());
						    purchaseOrderCsv.setPurchasingGrp(row.getCell(9).toString());
						    purchaseOrderCsv.setCompany(row.getCell(10).toString());
						    //System.out.println("PO NO :: "+poNo+"   Code :: "+line[12]);
						    purchaseOrderCsv.setMaterialCode(row.getCell(11).toString());
						    purchaseOrderCsv.setMaterialDesc(row.getCell(12).toString());
						    purchaseOrderCsv.setDeliveryDate(row.getCell(13).toString());
						    purchaseOrderCsv.setNetPrice(Double.parseDouble(row.getCell(14).toString()));
						    purchaseOrderCsv.setCurrency(row.getCell(15).toString());
						    purchaseOrderCsv.setOrder_unit(row.getCell(17).toString());
						   // purchaseOrderCsv.setDoc_condition_no(row.getCell(21).toString());
						    
						    
							
						    /************Anurag*************/
						    List<PurchaseOrder> purchaseOrders = new ArrayList<>();
						    
						    //Implement in future//
						    /*entirePurchaseOrder.parallelStream()
						    .filter(p -> p.getPurchaseOrderNo() == purchaseOrderCsv.getPurchaseOrder())
						    .forEach(p -> purchaseOrders.add(p));*/
						    //Implement in future -- end//
						    
						    for(PurchaseOrder thePurchaseOrder : entirePurchaseOrder) {
						    	if(thePurchaseOrder.getPurchaseOrderNo() == purchaseOrderCsv.getPurchaseOrder())
						    		purchaseOrders.add(thePurchaseOrder);
						    }
						    /************End*************/
						    		
						    PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
						    PurchaseOrder purchaseOrder = new PurchaseOrder();
						    ItemMst itemMst = new ItemMst();
						    
						    if(purchaseOrders.size()==0) {
								VendorMst vendorMst = new VendorMst();
								
								/************Anurag*************/
								List<VendorMst> vendorMstList = new ArrayList<>();
								
								//Implement in future//
								/*entireVendorMst.parallelStream()
						        .filter(v -> v.getId().equals(purchaseOrderCsv.getVendorCode()))
						        .forEach(v -> vendorMstList.add(v));*/
						        //Implement in future -- end//
								
								for(VendorMst theVendorMst : entireVendorMst) {
						        	if(theVendorMst.getId().equals(purchaseOrderCsv.getVendorCode()))
						        		vendorMstList.add(theVendorMst);
						        }
								/************End*************/
								
								if(vendorMstList.size()==0) {
									
									vendorMst.setId(purchaseOrderCsv.getVendorCode());
									vendorMst.setVenName(purchaseOrderCsv.getVendorName());
									vendorMstServices.saveVen(vendorMst);
									/************Anurag*************/
									entireVendorMst.add(vendorMst);
									purchaseOrder.setVendorMst(vendorMst);
									/************End*************/
								}
								/************Anurag*************/
								else {
									purchaseOrder.setVendorMst(vendorMstList.get(0));
								}
								/************End*************/
								
								purchaseOrder.setPurchaseOrderNo(purchaseOrderCsv.getPurchaseOrder());
								purchaseOrder.setVenName(purchaseOrderCsv.getVendorName());
								purchaseOrder.setDocConditionNo(purchaseOrderCsv.getDoc_condition_no());
								purchaseOrder.setCreateOn(purchaseOrderCsv.getCreated_on());
								purchaseOrder.setDeliveryDate(purchaseOrderCsv.getDeliveryDate());
								purchaseOrderService.savePurchaseOrder(purchaseOrder);
								entirePurchaseOrder.add(purchaseOrder);
							}
						  
						    /************Anurag*************/
							List<ItemMst> itemMstlist = new ArrayList<>();
							
							//Implement in future//
							/*entireItemMst.parallelStream()
						    .filter(i -> i.getId().equals(purchaseOrderCsv.getMaterialCode()))
						    .forEach(i -> itemMstlist.add(i));*/
						    //Implement in future -- end//
							
							for(ItemMst theItemMst : entireItemMst) {
						    	if(theItemMst.getId().equals(purchaseOrderCsv.getMaterialCode()))
						    		itemMstlist.add(theItemMst);
						    }
							/************End*************/
						    
							if(itemMstlist.size()==0) {
								
								itemMst.setId(purchaseOrderCsv.getMaterialCode());
								itemMst.setItemDtl(purchaseOrderCsv.getMaterialDesc());
								itemMstServices.saveItem(itemMst);
								/************Anurag*************/
								entireItemMst.add(itemMst);
								purchaseOrderItem.setItemMst(itemMst);
								/************End*************/
							}
							/************Anurag*************/
							else {
								purchaseOrderItem.setItemMst(itemMstlist.get(0));
							}
							/************End*************/
							
							
							
							/************Anurag*************/
						    List<PurchaseOrder> purchaseOrders2 = new ArrayList<>();
						    
						  //Implement in future//
						    /*entirePurchaseOrder.parallelStream()
						    .filter(p -> p.getPurchaseOrderNo()==purchaseOrderCsv.getPurchaseOrder())
						    .forEach(p -> purchaseOrders2.add(p));*/
						    //Implement in future -- end//
						    
						    for(PurchaseOrder thePurchaseOrder : entirePurchaseOrder) {
						    	if(thePurchaseOrder.getPurchaseOrderNo() == purchaseOrderCsv.getPurchaseOrder())
						    		purchaseOrders2.add(thePurchaseOrder);
						    }
						    /************End*************/
							
						    PurchaseOrder purchaseOrder2 = purchaseOrders2.get(0);
						    long purchaseOrderId = purchaseOrder2.getPurchaseOrderNo();
						    
						    /************Anurag*************/
							List<ItemMst> itemMstlist2 = new ArrayList<>();
							
							//Implement in future//
							/*entireItemMst.parallelStream()
						    .filter(i -> i.getId().equals(purchaseOrderCsv.getMaterialCode()))
						    .forEach(i -> itemMstlist2.add(i));*/
						    //Implement in future -- end//
							
							for(ItemMst theItemMst : entireItemMst) {
						    	if(theItemMst.getId().equals(purchaseOrderCsv.getMaterialCode()))
						    		itemMstlist2.add(theItemMst);
						    }
							/************End*************/
						    
						    itemMst = itemMstlist2.get(0);
						    String itemId = itemMst.getId();
						    
						    /************Anurag*************/
							List<PurchaseOrderItem> purchaseOrderItemList = new ArrayList<>();
							
							//Implement in future//
							/*entirePurchaseOrderItem.parallelStream()
						    .filter(pi -> pi.getPurchaseOrder().getPurchaseOrderNo() == purchaseOrderId && pi.getItemMst().getId().equals(itemId))
						    .forEach(pi -> purchaseOrderItemList.add(pi));*/
						    //Implement in future -- end//
							
							for(PurchaseOrderItem thePurchaseOrderItem : entirePurchaseOrderItem) {
						    	if(thePurchaseOrderItem.getPurchaseOrder().getPurchaseOrderNo() == purchaseOrderId && thePurchaseOrderItem.getItemMst().getId().equals(itemId))
						    		purchaseOrderItemList.add(thePurchaseOrderItem);
						    }
							/************End*************/
						    
						    if(purchaseOrderItemList.size()==0) {
						        purchaseOrderItem.setPurchaseOrder(purchaseOrder2);
						        purchaseOrderItem.setItemDtils(purchaseOrderCsv.getMaterialDesc());
						        purchaseOrderItem.setItemMsrUnit(purchaseOrderCsv.getOrder_unit());
						        purchaseOrderItem.setCurrency(purchaseOrderCsv.getCurrency());
						        purchaseOrderItem.setItemQty(purchaseOrderCsv.getOrder_qty());
						        purchaseOrderItem.setNetPrice(purchaseOrderCsv.getNetPrice());
						        
						        purchaseOrderItemService.savePurchaseOrderItem(purchaseOrderItem);
						        entirePurchaseOrderItem.add(purchaseOrderItem);
						    }
						}					
				}
			}
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}	
	
}
