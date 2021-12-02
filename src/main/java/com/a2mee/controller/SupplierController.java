package com.a2mee.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.a2mee.model.GrnItem;
import com.a2mee.model.ItemMst;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.Supplier;
import com.a2mee.model.SupplierComponants;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.services.ItemsService;
import com.a2mee.services.ReportServices;
import com.a2mee.services.SupplierServices;
import com.a2mee.util.API;

import ch.qos.logback.core.net.SyslogOutputStream;

@RestController
@RequestMapping(API.suuplier)
@CrossOrigin("*")

public class SupplierController {
	@Autowired
	SupplierServices supplierServices;
	@Autowired
	ItemsService itemsService;
	
	@GetMapping(API.getSupplierList)
	public List<Supplier> getSupplierList() {
	
		return supplierServices.getSupplierList();
	}
	
	@GetMapping(API.getSupplierComponantsBySupplier)
	public List<SupplierComponants> getSupplierComponantsBySupplier(@RequestParam("supplierId") long supplierId) {
	
		//long supplierId2=Long.valueOf(supplierId);
		return supplierServices.getSupplierComponantsBySupplier(supplierId);
	}
	
	@GetMapping(API.getSupplierComponantsByItem)
	public List<SupplierComponants> getSupplierComponantsByItem(@RequestParam("itemId") String itemId) {
				//long supplierId2=Long.valueOf(supplierId);
		return supplierServices.getSupplierComponantsByItem(itemId);
	}
	@PostMapping(API.addSupplierComponant)
	public @ResponseBody ResponseEntity addSupplierComponant(@RequestBody SupplierComponants supplierComponants)
	{		
		try{
			supplierServices.addSupplierComponant(supplierComponants);
			//System.out.print("hello");
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/* for Desktop Screen */
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/* for uploading Annual Plan or Sales Order */
	@PostMapping(API.uploadSupplierItems)
	public @ResponseBody ResponseEntity postFile(ModelMap model, @ModelAttribute(value = "file") MultipartFile file,
			HttpServletRequest request) {
		try {
			System.out.println("-------------------HELLO------------------------");
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


						int i = 1;
						while (i <= datatypeSheet.getLastRowNum()) { // for saving model plan

							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(i++);
							String str = row.getCell(0).toString();
							if(str.length()==0) {
								continue;
							}
							if (row.getCell(0) != null) {
								
								String supplierCode=row.getCell(0).toString();
								String supplierName=row.getCell(1).toString();
								String itemCode=row.getCell(2).toString();
								String itemName=row.getCell(3).toString();
								String priority=row.getCell(4).toString();
								String unitPrice=row.getCell(4).toString();
								Supplier supplier=supplierServices.getSupplierComponantsBySupplierCode(supplierCode);
								ItemMst itemMst= itemsService.getItemById(itemCode);
								SupplierComponants supplierComponants= new SupplierComponants();
								
								if(supplier==null){
									Supplier supplier2= new Supplier();
									supplier2.setSupplierCode(supplierCode);
									supplier2.setSupplierName(supplierName);
									supplierServices.saveSupplier(supplier2);
									supplierComponants.setSupplier(supplier2);
								}else{
									supplierComponants.setSupplier(supplier);
								}
								if(itemMst==null){
									ItemMst itemMst2= new ItemMst(); 
									itemMst2.setId(itemCode);
									itemMst2.setItemDtl(itemName);
									itemsService.updateitem(itemMst2);
									supplierComponants.setItemMst(itemMst2);
								}else{
									supplierComponants.setItemMst(itemMst);
								}
								supplierComponants.setUnitPrice(Double.parseDouble(unitPrice));
								supplierComponants.setPriority(Integer.parseInt(priority));
								supplierComponants.setActive(1);
								supplierComponants.setAddedDate(new Date());
								supplierServices.addSupplierComponant(supplierComponants);
								

							}
						}

						


						logger.info("Successfully imported modelplan");
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
	
	@GetMapping(API.getsupplierByComponantCode)
	public Set<Supplier> getsupplierByComponantCode(@RequestParam("compCode") String compCode) {
				//long supplierId2=Long.valueOf(supplierId);
		Set<Supplier> suppliers = new HashSet<>();
		List<SupplierComponants> list =  supplierServices.getsupplierByComponantCode(compCode);
		for(SupplierComponants componants:list ){
			suppliers.add(componants.getSupplier());
		}
		 return suppliers;
	}
}
