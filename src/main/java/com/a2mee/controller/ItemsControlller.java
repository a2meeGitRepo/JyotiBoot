package com.a2mee.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

import com.a2mee.model.GrnItem;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.Plant;
import com.a2mee.model.Storage;
import com.a2mee.model.StorageBinMst;
import com.a2mee.model.dto.GrnItemDto;
import com.a2mee.services.GRNService;
import com.a2mee.services.ItemMstServices;
import com.a2mee.services.ItemsService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.items)
@CrossOrigin
public class ItemsControlller {
	
	@Autowired
	ItemsService itemsService;
	@Autowired
	ItemMstServices itemMstServices;
	/* for Desktop Screen */
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@GetMapping(API.itemListByPagination)
	public @ResponseBody List<ItemMst> itemListByPagination(@PathVariable("pageno") int pageno,@PathVariable("perPage") int perPage) {
		try {
			return itemsService.itemListByPagination(pageno,perPage);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<ItemMst>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.itemList)
	public @ResponseBody List<ItemMst> itemList() {
		try {
			return itemsService.getItemList();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<ItemMst>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping(API.getTotalItemCount)
	public @ResponseBody int getTotalItemCount() {
		try {
			return itemsService.getItemList().size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	@PostMapping(API.updateitem)
	public @ResponseBody ResponseEntity updateitem(@RequestBody ItemMst itemMst)
	{		
		try{
			itemsService.updateitem(itemMst);
			//System.out.print("hello");
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	@PostMapping(API.uploadItems)
	public @ResponseBody ResponseEntity uploadOldStock(ModelMap model, @ModelAttribute(value = "file") MultipartFile file,
			HttpServletRequest request) {
		try {
			if (!(file == null)) {				
				if (file.isEmpty()) {
					log.info("File not found");
				} else {
					log.info(file.getOriginalFilename());
					try {
						File dir = new File(System.getProperty("catalina.base"), "uploads");
						File uplaodedFile = new File(dir + file.getOriginalFilename());
						file.transferTo(uplaodedFile);
						FileInputStream excelFile = new FileInputStream(uplaodedFile);
						log.info("hiiii@" + excelFile);
						Workbook workbook = new XSSFWorkbook(excelFile);
						Sheet datatypeSheet = workbook.getSheetAt(0); //contains store 1050
						DataFormatter formatter = new DataFormatter();
						
						
					
						

						int i = 1;
						while (i <= datatypeSheet.getLastRowNum()) {
//						
							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(i++);

							if (row.getCell(0) != null) {								
								

								String itemId = formatter.formatCellValue(row.getCell(0));
								String itemDtl=formatter.formatCellValue(row.getCell(1));
								String groupId=formatter.formatCellValue(row.getCell(2));
								String invntItmF=formatter.formatCellValue(row.getCell(3));
								String itemFrName=formatter.formatCellValue(row.getCell(4));
								String itmInspectF=formatter.formatCellValue(row.getCell(5));
								String prchseItmF=formatter.formatCellValue(row.getCell(6));
								String itmStatus=formatter.formatCellValue(row.getCell(7));
								String saleItmF=formatter.formatCellValue(row.getCell(8));
								String uom=formatter.formatCellValue(row.getCell(9));
								String materialSource=formatter.formatCellValue(row.getCell(10));
								String scmMode=formatter.formatCellValue(row.getCell(11));
								String matMstLog=formatter.formatCellValue(row.getCell(12));
									ItemMst item = itemMstServices.findById(itemId);
									/*
									System.out.println("itemId :: "+itemId);
									System.out.println("itemDtl :: "+itemDtl);
									
									System.out.println("groupId :: "+groupId.isEmpty());
									System.out.println("invntItmF :: "+invntItmF);
									System.out.println("itemFrName :: "+itemFrName);
									System.out.println("itmInspectF :: "+itmInspectF);
									System.out.println("prchseItmF :: "+prchseItmF);
									System.out.println("itmStatus :: "+itmStatus);
									System.out.println("saleItmF :: "+saleItmF);
									System.out.println("uom :: "+uom);*/
									
									if(item !=null){
										ItemMst itemMst= new ItemMst();
										
										if(! itemId.isEmpty()){
											itemMst.setId(itemId);
										}
										if(! itemDtl.isEmpty()){
											itemMst.setItemDtl(itemDtl);
										}
										
										
										if(! groupId.isEmpty()){
											itemMst.setGrpId(Long.valueOf(groupId));
										}
										
										if(! itemFrName.isEmpty()){
										itemMst.setItemFrName(itemFrName);
										}
										itemMst.setUom(uom);
										
										if( ! itemDtl.isEmpty()){
										itemMst.setItemDtl(itemDtl);
										}
										if(! invntItmF.isEmpty()){
										itemMst.setInvntItmF(invntItmF.charAt(0));
										}
										if(! itmInspectF.isEmpty()){
										itemMst.setItmInspectF(itmInspectF.charAt(0));
										}
										if(! itmStatus.isEmpty()){
										itemMst.setItmStatus(itmStatus.charAt(0));
										}
										if(! prchseItmF.isEmpty()){
										itemMst.setPrchseItmF(prchseItmF.charAt(0));
										}
										if(! saleItmF.isEmpty()){
										itemMst.setSaleItmF(saleItmF.charAt(0));
										}
										if(! materialSource.isEmpty()){
											itemMst.setMaterialSource(materialSource);
											}
										if(! scmMode.isEmpty()){
											itemMst.setScmMode(scmMode);
											}
										
										if(! matMstLog.isEmpty()){
											itemMst.setMatMstLog(matMstLog);;
											}
										itemsService.updateitem(itemMst);
									
									
									}else{
										ItemMst itemMst= new ItemMst();
										System.out.println("Group ID :: "+groupId+"  CON1 "+groupId.isEmpty()+"   con 2 ");
										if(! itemId.isEmpty()){
											itemMst.setId(itemId);
										}
										if(! itemDtl.isEmpty()){
											itemMst.setItemDtl(itemDtl);
										}
										
										if(! groupId.isEmpty()){
											itemMst.setGrpId(Long.valueOf(groupId));
										}
										
										
										if(! itemFrName.isEmpty()){
											itemMst.setItemFrName(itemFrName);
										}
											
										  
										if(! uom.isEmpty()){
											itemMst.setUom(uom.toString());
										}
											
											if(! itemDtl.isEmpty()){
												itemMst.setItemDtl(itemDtl);
											}
											
											if(! invntItmF.isEmpty()){
												itemMst.setInvntItmF(invntItmF.charAt(0));
											}
											if(! itmInspectF.isEmpty()){
												itemMst.setItmInspectF(itmInspectF.charAt(0));
											}
											if(! itmStatus.isEmpty()){
												itemMst.setItmStatus(itmStatus.charAt(0));
											}
											if(! prchseItmF.isEmpty()){
												itemMst.setPrchseItmF(prchseItmF.charAt(0));
											}
											if(! saleItmF.isEmpty()){
												itemMst.setSaleItmF(saleItmF.charAt(0));
											}
											if(! materialSource.isEmpty()){
												itemMst.setMaterialSource(materialSource);
												}
										
											if(! scmMode.isEmpty()){
												itemMst.setScmMode(scmMode);
												}
											
											if(! matMstLog.isEmpty()){
												itemMst.setMatMstLog(matMstLog);;
												}
											//item =itemMst;
											itemsService.updateitem(itemMst);
									}
									
								//	itemsService.updateitem(item);
									
								
							//	}
							}
						}

						log.info("Successfully imported");
						workbook.close();
					} catch (FileNotFoundException e) {
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
}
