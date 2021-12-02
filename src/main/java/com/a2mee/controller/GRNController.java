package com.a2mee.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.dao.MaterialInspectDao;
import com.a2mee.model.GRN;
import com.a2mee.model.GrnItem;
import com.a2mee.model.GrnItemLot;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.PurchaseOrderItem;
import com.a2mee.model.RejectReasonMst;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.GrnItemDto;
import com.a2mee.model.dto.GrnSaveDto;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.dto.MaterialInspDto;
import com.a2mee.model.dto.PoMaterialListDto;
import com.a2mee.services.GRNService;
import com.a2mee.services.GrnItemLotServices;
import com.a2mee.services.GrnItemServices;
import com.a2mee.services.ItemMstServices;
import com.a2mee.services.PurchaseOrderItemService;
import com.a2mee.services.PurchaseOrderService;
import com.a2mee.services.RejectReasonServices;
import com.a2mee.services.VendorMstServices;
import com.a2mee.util.API;
import com.a2mee.util.Constants;

@RestController
@RequestMapping(API.grn)
@CrossOrigin
public class GRNController {

	@Autowired
	GRNService grnService;

	@Autowired
	GrnItemServices grnItemService;

	@Autowired
	ItemMstServices itemMstServices;

	@Autowired
	VendorMstServices vendorMstServices;

	@Autowired
	GrnItemLotServices grnItemLotServices;

	@Autowired
	RejectReasonServices rejectReasonServices;
	
	@Autowired
	PurchaseOrderService purchaseOrderService;

	@Autowired
	PurchaseOrderItemService purchaseOrderItemService;
	@Autowired
	MaterialInspectDao materialInspectDao;

	/* for Desktop Screen */

	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping(API.createGrn)
	public @ResponseBody ResponseEntity<GRN> saveGrn(@RequestBody GrnSaveDto theGrnSaveDto) {
		try {
			
			System.out.println("Create GRN :: "+theGrnSaveDto.toString());
			PurchaseOrder thePurchaseOrder = purchaseOrderService
					.searchByPurchaseOrderId(theGrnSaveDto.getPurchaseOrderNo());
			PurchaseOrderItem purchaseOrderItem = purchaseOrderItemService
					.searchByPoNoItemId(theGrnSaveDto.getPurchaseOrderNo(), theGrnSaveDto.getItemMstId());

			if (theGrnSaveDto.getPoiStatus().equals("1")) {
				purchaseOrderItem.setPoiStatus(theGrnSaveDto.getPoiStatus());
				purchaseOrderItemService.savePurchaseOrderItem(purchaseOrderItem);
			}

			GRN theGrn = new GRN();

			theGrn.setPurchaseOrderNo(theGrnSaveDto.getPurchaseOrderNo());
			theGrn.setGrnNo(theGrnSaveDto.getGrnNo());
			theGrn.setGrnEntryDate(theGrnSaveDto.getGrnEntryDate());
			theGrn.setVenName(thePurchaseOrder.getVenName());
			theGrn.setGrnStatus(theGrnSaveDto.getGrnStatus());
			theGrn.setGrnBy(theGrnSaveDto.getGrnBy());
			theGrn.setDocConditionNo(thePurchaseOrder.getDocConditionNo());
			theGrn.setErrorSolveDate(theGrnSaveDto.getErrorSolveDate());
			theGrn.setInwardTime(theGrnSaveDto.getInwardTime());
			theGrn.setVendorMst(thePurchaseOrder.getVendorMst());

			grnService.saveGrn(theGrn);

			ItemMst theItemMst = itemMstServices.getItemByCode(theGrnSaveDto.getItemMstId()).get(0);
			theGrn = grnService.getGrnByGrnNo(theGrnSaveDto.getGrnNo()).get(0);
			GrnItem theGrnItem = new GrnItem();
			theGrnItem.setGrn(theGrn);
			theGrnItem.setItemDtils(theGrnSaveDto.getItemDetails());
			theGrnItem.setItemQty(theGrnSaveDto.getItemQty());
			theGrnItem.setItemMsrUnit(theGrnSaveDto.getItemMsrUnit());
			theGrnItem.setItemTolRecd(theGrnSaveDto.getGrnQty());
			theGrnItem.setNetPrice(theGrnSaveDto.getNetPrice());
			theGrnItem.setCurrency(theGrnSaveDto.getCurrency());
			theGrnItem.setItemMst(theItemMst);
			theGrnItem.setInwardDate(purchaseOrderItem.getInwardDate());
			theGrnItem.setInvoiceNumber(purchaseOrderItem.getInvoiceNumber());
			theGrnItem.setChallanNumber(purchaseOrderItem.getChallanNumber());
			theGrnItem.setDocDate(purchaseOrderItem.getDocDate());
			theGrnItem.setRemarks(purchaseOrderItem.getRemarks());
			theGrnItem.setType(purchaseOrderItem.getType());
			theGrnItem.setBoe(purchaseOrderItem.getBoe());
			theGrnItem.setCourier(purchaseOrderItem.getCourier());
			theGrnItem.setDocketNo(purchaseOrderItem.getDocketNo());
			theGrnItem.setSapGrnNo(purchaseOrderItem.getSapGrnNo());
			theGrnItem.setSapGrnDate(purchaseOrderItem.getSapGrnDate());
			theGrnItem.setDelayDays(purchaseOrderItem.getDelayDays());
			theGrnItem.setResPerson(purchaseOrderItem.getResPerson());
			theGrnItem.setDelayPerc(purchaseOrderItem.getDelayPerc());
			theGrnItem.setAccDocHandover(purchaseOrderItem.getAccDocHandover());
			theGrnItem.setHandoverDate(purchaseOrderItem.getHandoverDate());
			theGrnItem.setVehicleNo(purchaseOrderItem.getVehicleNo());
			theGrnItem.setVehicleStatus(purchaseOrderItem.getVehicleStatus());
			theGrnItem.setUnloadType(purchaseOrderItem.getUnloadType());
			theGrnItem.setPackingType(purchaseOrderItem.getPackingType());		
			
			grnItemService.saveGrnItem(theGrnItem);

			GrnItemLot theGrnItemLot = new GrnItemLot();
			theGrnItemLot.setGrnItem(theGrnItem);
			theGrnItemLot.setBoxQty(theGrnSaveDto.getBoxQty());
			theGrnItemLot.setBatchQty(theGrnSaveDto.getBatchQty());
			theGrnItemLot.setItemMstId(theGrnSaveDto.getItemMstId());
			theGrnItemLot.setGrnNo(theGrnSaveDto.getGrnNo());

			GrnItemLot grnItemLot=grnItemLotServices.saveGrnItemLot(theGrnItemLot);

			List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemService
					.getPuOrItemListbyId(theGrnSaveDto.getPurchaseOrderNo());
			int poItemsSize = purchaseOrderItems.size();
			int count = 0;

			for (PurchaseOrderItem poItem : purchaseOrderItems) {
				if (poItem.getPoiStatus() != null)
					count++;
			}

			if (count == poItemsSize) {
				PurchaseOrder purchaseOrder = purchaseOrderService
						.getPurchaseOrderListByNo(theGrnSaveDto.getPurchaseOrderNo()).get(0);
				purchaseOrder.setPurchaseOrderStatus("1");
				purchaseOrderService.savePurchaseOrder(purchaseOrder);
			}
			if(theGrnSaveDto.getType().equalsIgnoreCase("Green Channel")){
				MaterialInspect mi = null;	mi = new MaterialInspect();
				mi.setMtlStatus(Constants.MATERIAL_ACCEPTE_STATUS.getValue());
				mi.setMtlQty(theGrnSaveDto.getGrnQty());
				mi.setGrnItemLot(grnItemLot);
				/*mi.setDocEntry(grnItemLot.getDocEntry());
				mi.setLineNum(grnItemLot.getGrnDocNo());
				mi.setSysNumber(grnItemLot.getSysNum());*/
				mi.setOriginalStatus("P");
				mi.setDate(LocalDate.now());
				MaterialInspDto materialInspDto= new  MaterialInspDto();
				materialInspDto.setAcceptqty(theGrnSaveDto.getGrnQty());
				materialInspDto.setBarCode(grnItemLot.getItemBarcode());
				materialInspDto.setItmLotId(grnItemLot.getGrnItemLotId());
				//materialInspDto.setUserId(theGrnSaveDto.getU);
				MaterialInspect m = materialInspectDao.saveInspMterial(mi);
				grnItemLotServices.saveStockInGorGreenChanl(materialInspDto, m);
			}

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Update SAP details for GRN
	 * 
	 * 
	*/
	@PostMapping(API.updateSapData)
	public @ResponseBody ResponseEntity updateSapData(@RequestBody GrnItem grnItem)
	{		
		try{
			grnItemService.updateSapData(grnItem);
			System.out.print("hello");
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/* Delete facility for GRN
	 * 
	 * 
	*/
	@PostMapping(API.deleteGrn)
	public @ResponseBody ResponseEntity deleteGrn(@RequestBody GrnDto grnDto)
	{		
		try{
			grnItemService.deleteGrn(grnDto);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(API.grnItemList)
	public @ResponseBody List<GrnItemDto> getGrnItemList(@RequestParam("itemId") String itemId) {
		try {
			return grnItemService.getGrnItemList(itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<GrnItemDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.submitGrn)
	public @ResponseBody List<GrnDto> selectedGrnList(@RequestParam("venId") String venId,
			@RequestParam("itemId") String itemId) {
		log.debug("start " + this.getClass().getName() + " selectedGrnList : venId = " + venId + " itemId = " + itemId);
		try {
			return grnService.getGrnList(venId, itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<GrnDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.serchByPOno)
	public @ResponseBody List<GrnDto> serchByGrnNo(@RequestParam("purchaseOrderNo") long purchaseOrderNo) {
		log.debug("start " + this.getClass().getName() + " serchByPONo : purchaseOrderNo = " + purchaseOrderNo);
		try {
			return grnService.serchByPONo(purchaseOrderNo);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<GrnDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.itemlist)
	public @ResponseBody List<MasterDto> getItemList(@RequestParam("venId") String venId) {
		log.debug("start " + this.getClass().getName() + " getItemList : venId = " + venId);
		try {
			return itemMstServices.getItemList(venId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<MasterDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.itemGrnlist)
	public @ResponseBody List<MasterDto> getItemGrnList(@RequestParam("venId") String venId) {
		log.debug("start " + this.getClass().getName() + " getItemList : venId = " + venId);
		try {
			return itemMstServices.getItemGrnlist(venId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<MasterDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.venlist)
	public @ResponseBody List<MasterDto> getVenList() {
		log.debug("start " + this.getClass().getName() + " getVenList method run ");
		try {
			return vendorMstServices.getvenList();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<MasterDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(API.venGrnlist)
	public @ResponseBody List<MasterDto> getVenGrnList() {
		log.debug("start " + this.getClass().getName() + " getVenList method run ");
		try {
			return vendorMstServices.getVenGrnList();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<MasterDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(API.updateQr)
	public @ResponseBody ResponseEntity updateQrCode(@RequestParam("grItmlotId") Integer grItmlotId,
			@RequestParam("qrCodeNo") String qrCodeNo) {
		try {
			log.debug("start " + this.getClass().getName() + " updateQrCode : grItmlotId = " + grItmlotId
					+ " qrCodeNo = " + qrCodeNo);
			grnItemLotServices.updateQrCode(grItmlotId, qrCodeNo);
			log.debug("");
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", e);
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/* for mobile Screen */
	@GetMapping(API.scanQr)
	public @ResponseBody ResponseEntity<PoMaterialListDto> scanQrcode(@RequestParam("qrCodeNo") String qrCodeNo) {
		try {
			return new ResponseEntity(grnItemLotServices.getDatabyQrScan(qrCodeNo), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.rejectResonList)
	public @ResponseBody List<RejectReasonMst> rejectedReson() {
		try {
			return rejectReasonServices.getRejectReasonListForGrn();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<RejectReasonMst>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(API.inspectMateril)
	public @ResponseBody ResponseEntity<PoMaterialListDto> inspectMateril(
			@RequestBody MaterialInspDto materialInspDto) {
		try {
			System.out.println("INSPECTION :: "+materialInspDto.toString());
			return new ResponseEntity(grnItemLotServices.saveMaterialSumm(materialInspDto), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
	