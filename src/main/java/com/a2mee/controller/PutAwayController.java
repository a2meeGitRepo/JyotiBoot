package com.a2mee.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

import com.a2mee.dao.PlantMstDao;
import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.EmployeeMst;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.ItemMst;
import com.a2mee.model.ModelMst;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.Plant;
import com.a2mee.model.PurchaseOrder;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.PurchaseOrderItem;
import com.a2mee.model.SkillMst;
import com.a2mee.model.Storage;
import com.a2mee.model.StorageBinMst;
import com.a2mee.model.TitleMst;
import com.a2mee.model.VendorMst;
import com.a2mee.model.dto.AcceptedMatDto;
import com.a2mee.model.dto.ItemLocMapDto;
import com.a2mee.model.dto.ItemMstDto;
import com.a2mee.model.dto.PurchaseOrderCsvDto;
import com.a2mee.model.dto.PurchaseOrderDto;
import com.a2mee.model.dto.ResponseDto;
import com.a2mee.model.dto.StockDto;
import com.a2mee.model.dto.StorageCsvDto;
import com.a2mee.model.dto.StorageLocationItems;
import com.a2mee.services.GrnItemLotServices;
import com.a2mee.services.ItemLocMapService;
import com.a2mee.services.ItemMstServices;
import com.a2mee.services.MaterialInspectServices;
import com.a2mee.services.MtlStockInServices;
import com.a2mee.services.StorageBinMstService;
import com.a2mee.util.API;
import com.opencsv.CSVReader;

@RestController
@RequestMapping(API.putAway)
@CrossOrigin("*")
public class PutAwayController {

	@Autowired
	StorageBinMstService storageBinMstService;
	
	@Autowired
	ItemMstServices itemMstServices;
	
	@Autowired
	ItemLocMapService itemLocMapService;
	
	@Autowired
	MaterialInspectServices materialInspectServices;
	
	@Autowired
	MtlStockInServices mtlStockInServices;
	
	/* for Desktop Screen */
	public final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	
	@GetMapping(API.getTotalStorageLocationCount)
	public @ResponseBody int getTotalStorageLocationCount() {
		try {
		return storageBinMstService.getStorageBinList().size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	

	@GetMapping(API.storageBinItemWiseListPagination)
	public @ResponseBody List<StorageLocationItems> storageBinItemWiseListPagination(@PathVariable("pageno") int pageno,@PathVariable("perPage") int perPage) {
		try {

			List<StorageLocationItems> items= new ArrayList<>();
			List<StorageBinMst> list 	= storageBinMstService.getStorageBinListByPagination(pageno,perPage);
			//System.out.println("================================ SIZE =============="+list.size());
			
			for(StorageBinMst storageBinMst :list){
				StorageLocationItems locationItems= new StorageLocationItems();
				locationItems.setStorageBinCode(storageBinMst.getStorageBinCode());
				String itemsMaps="";
				List<ItemLocMap> maps= storageBinMstService.getItemLocationMap(storageBinMst.getStorageBinCode());
				//System.out.println("MAP SIZE :::::::::::::::::::::::::::::::;"+maps.size());

				
				if(maps.size()!=0){
					for(ItemLocMap locMap:maps){
						if(itemsMaps==""){
							itemsMaps=locMap.getItemMst().getId();
						}else{
							itemsMaps +=" , "+locMap.getItemMst().getId();
						}
					}	
				}else{
					itemsMaps="Not Allocated ";
				}
				locationItems.setItemMaps(itemsMaps);
				items.add(locationItems);
				
			}
				return items;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping(API.storageBinList)
	public @ResponseBody List<StorageBinMst> getStorageBinList() {
		try {
		return storageBinMstService.getStorageBinList();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<StorageBinMst>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(API.addStorageBin)
	public @ResponseBody ResponseEntity addStorageBin(@RequestBody StorageBinMst storageBinMst)
	{		
		try{
			storageBinMstService.addStorageBin(storageBinMst);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping(API.delStorageBin)
	public @ResponseBody ResponseEntity delStorageBin(@RequestBody StorageBinMst storageBinMst)
	{
		try{
			long storageBinId = storageBinMst.getStorageBinId();
			storageBinMstService.delStorageBin(storageBinId);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(API.itemList)
	public @ResponseBody List<ItemMstDto> getItemList() {
		try {
		return itemMstServices.getItemList();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<ItemMstDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.itemLocMapList)
	public @ResponseBody List<ItemLocMap> getItemLocMap() {
		try {
		return itemLocMapService.getItemLocMap();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<ItemLocMap>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.delItemLocMap)
	public @ResponseBody ResponseEntity delItemLocMap(@RequestParam("itemLocMapId") long itemLocMapId) {
		try {
			itemLocMapService.delItemLocMap(itemLocMapId);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(API.addItemLocMap)
	public @ResponseBody ResponseEntity addItemLocMap(@RequestBody List<ItemLocMapDto> theItemLocMapsDto)
	{
		try{
//			String storageBinCode = theItemLocMapsDto.get(0).getStorageBinCode();
//			long storageBinId = storageBinMstService.getstorageBinMstByCode(storageBinCode).get(0).getStorageBinId();
//			System.out.println(storageBinCode);
//			itemLocMapService.delItemLocMap(storageBinId);
			
			for (ItemLocMapDto theItemLocMapDto : theItemLocMapsDto) {
					StorageBinMst theStorageBinMst = storageBinMstService.getstorageBinMstByCode(theItemLocMapDto.getStorageBinCode()).get(0);
					ItemMst theItemMst = itemMstServices.getItemById(theItemLocMapDto.getItemMstId()).get(0);
					ItemLocMap theItemLocMap = new ItemLocMap();
					theItemLocMap.setItemMst(theItemMst);
					theItemLocMap.setStorageBinMst(theStorageBinMst);
					itemLocMapService.saveitemLocMap(theItemLocMap);
				}
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(API.acceptedList)
	public @ResponseBody List<AcceptedMatDto> getAcceptedList() {
		try {
		return materialInspectServices.getAcceptedList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	@GetMapping(API.mapItemToStorageBin)
	public @ResponseBody ResponseEntity mapItemToStorageBin(@RequestParam("itemMstId") String itemMstId,
			@RequestParam("storageBinCode") String storageBinCode) {
//		log.debug("start "+this.getClass().getName()+" selectedPurchaseOrderList : venId = "+ venId+" itemId = "+itemId);
		try {
			
			System.out.println(" Storage Location "+storageBinCode+" itemMstId :: "+itemMstId);
			List<ItemLocMap> itemLocMaps = itemLocMapService.searchByItemIdStorageCode(itemMstId, storageBinCode);
			System.out.println("itemLocMaps :: "+itemLocMaps.size());
			if(itemLocMaps.size() != 0) {
				return new ResponseEntity(itemLocMaps,HttpStatus.OK);
			}
			else
				return new ResponseEntity(itemLocMaps,HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return (ResponseEntity)new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.storeItem)
	public @ResponseBody ResponseEntity storeItem(@RequestParam("grnNo") long grnNo,
			@RequestParam("storageBinCode") String storageBinCode)
	{
		try{			
			boolean flag = materialInspectServices.storeItem(grnNo, storageBinCode);
			if(flag) {
				materialInspectServices.updateStorageBin(grnNo, storageBinCode);
				ResponseDto responseDto = new ResponseDto();
				responseDto.setMessage("Success");
				return new ResponseEntity(responseDto,HttpStatus.OK);
			}
			else
			{
				ResponseDto responseDto = new ResponseDto();
				responseDto.setMessage("Fail");
				return new ResponseEntity(responseDto,HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDto responseDto = new ResponseDto();
			responseDto.setMessage("Fail");
			return new ResponseEntity(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(API.getStockList)
	public @ResponseBody List<StockDto> getStockList(@RequestParam("storageBinCode") String storageBinCode) {
		try {
		return materialInspectServices.getStockList(storageBinCode);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	@GetMapping(API.getStockListByItem)
	public @ResponseBody List<StockDto> getStockListByItem(@RequestParam("storageBinCode") String storageBinCode, @RequestParam("itemId") String itemId) {
		try {
		return materialInspectServices.getStockListByItem(storageBinCode, itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return (List<StockDto>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 *  Add Plant in Database
	 * @Author : Praful Sable
	 * @Date : 07/12/2019
	 * 
	*/	
	
	@PostMapping(API.addPlant)
	public @ResponseBody ResponseEntity addPlant(@RequestBody Plant plant)
	{		
		try{
			storageBinMstService.addPlant(plant);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/*
	 *  List Plant from Database
	 * @Author : Praful Sable
	 * @Date : 07/12/2019
	 * 
	*/	
	@GetMapping(API.listPlant)
	public @ResponseBody List<Plant> getListPlant() {
		try {
		return storageBinMstService.getListPlant();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<Plant>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 *  Delete Plant in Database
	 * @Author : Praful Sable
	 * @Date : 07/12/2019
	 * 
	*/
	@PostMapping(API.deletePlant)
	public @ResponseBody ResponseEntity deletePlant(@RequestBody Plant plant)
	{		
		try{
			storageBinMstService.deletePlant(plant);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	
	/*
	 *  Add Storage in Database
	 * @Author : Praful Sable
	 * @Date : 07/12/2019
	 * 
	*/	
	
	@PostMapping(API.addStorage)
	public @ResponseBody ResponseEntity addStorage(@RequestBody Storage storage)
	{		
		try{
			storageBinMstService.addStorage(storage);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/*
	 *  List Plant from Database
	 * @Author : Praful Sable
	 * @Date : 07/12/2019
	 * 
	*/	
	@GetMapping(API.listStorage)
	public @ResponseBody List<Storage> getStorageList() {
		try {
		return storageBinMstService.getListStorage();
		} catch (Exception e) {
			e.printStackTrace();
			return (List<Storage>) new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 *  Delete Plant in Database
	 * @Author : Praful Sable
	 * @Date : 07/12/2019
	 * 
	*/
	@PostMapping(API.deleteStorage)
	public @ResponseBody ResponseEntity deleteStorage(@RequestBody Storage storage)
	{		
		try{
			storageBinMstService.deleteStorage(storage);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/*
	 * Check is a bin has items assigned
	 * @Author : Anurag Roy
	 * @Date : 07/12/2019
	 * 
	*/
	@GetMapping(API.binItemCheck)
	public @ResponseBody ResponseEntity binItemCheck(@RequestParam("storageBinId") long storageBinId) {
//		log.debug("start "+this.getClass().getName()+" selectedPurchaseOrderList : venId = "+ venId+" itemId = "+itemId);
		try {
			List<ItemLocMap> itemLocMaps = itemLocMapService.getItemLocByStorageCode(storageBinId);
			
			if(itemLocMaps.size() != 0) {
				return new ResponseEntity(itemLocMaps,HttpStatus.OK);
			}
			else
				return new ResponseEntity(itemLocMaps,HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return (ResponseEntity)new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
  
	@GetMapping(API.getAssingStorageLocByItem)
	public @ResponseBody ResponseEntity getAssingStorageLocByItem(@RequestParam("itemId") String itemId) {
//		log.debug("start "+this.getClass().getName()+" selectedPurchaseOrderList : venId = "+ venId+" itemId = "+itemId);
		try {
			Set<StorageBinMst> itemLocMaps = itemLocMapService.getAssingStorageLocByItem(itemId);
			
			if(itemLocMaps.size() != 0) {
				return new ResponseEntity(itemLocMaps,HttpStatus.OK);
			}
			else
				return new ResponseEntity(itemLocMaps,HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return (ResponseEntity)new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/* Upload CSV file to Database
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(API.uploadCsvStorageBins)
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
					    File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
					    
					    try {
					        try (InputStream is = file.getInputStream();
					                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
					            int i;
					            //write file to server
					            while ((i = is.read()) != -1) {
					                stream.write(i);
					            }
					            stream.flush();
					        }
					    } catch (IOException e) {
					        model.put("msg", "failed to process file because : " + e.getMessage());
					        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
					    }
				    
						CSVReader reader = null;
						
						try	{
							reader = new CSVReader(new FileReader(serverFile));
				            String[] line;
				            reader.readNext();
			            	while ((line = reader.readNext()) != null) {
				            	
			            		StorageCsvDto storageCsvDto = new StorageCsvDto();
			            		storageCsvDto.setPlantCode(line[0]);
			            		storageCsvDto.setStorageCode(line[1]);
			            		storageCsvDto.setRackCode(line[2]);
			            		storageCsvDto.setColumnCode(line[3]);
			            		storageCsvDto.setRowCode(line[4]);
			            		storageCsvDto.setPalleteSide(line[5]);
			            		storageCsvDto.setItemId(line[6]);
			            		System.out.println("ITE ID"+line[6]);
			            		List<Plant> plants = storageBinMstService.getPlantByCode(storageCsvDto.getPlantCode());
			            		String storageBinCode = storageCsvDto.getPlantCode()+"-"+storageCsvDto.getStorageCode()+"-"+storageCsvDto.getRackCode()
            					+storageCsvDto.getColumnCode()+storageCsvDto.getRowCode()+storageCsvDto.getPalleteSide();
			            		if(plants.size()>0) {
			            			List<Storage> storages = storageBinMstService.getstorageByCode(storageCsvDto.getStorageCode());
			            			if(storages.size()==0) {
			            				Storage storage = new Storage();
			            				storage.setStorage_location(storageCsvDto.getStorageCode());
			            				storage.setDeletes(1);
			            				storageBinMstService.addStorage(storage);
			            				storages.add(storage);
			            			}
			            			
			            			List<StorageBinMst> storageBins = storageBinMstService.getstorageBinMstByCode(storageBinCode);
			            			if(storageBins.size()==0) {
			            				StorageBinMst storageBin = new StorageBinMst();
			            				storageBin.setStorageBinCode(storageBinCode);			            				
			            				storageBin.setRackLetter(storageCsvDto.getRackCode());
			            				storageBin.setColumnNo(storageCsvDto.getColumnCode());
			            				storageBin.setRowLetter(storageCsvDto.getRowCode());
			            				storageBin.setPaletteSide(storageCsvDto.getPalleteSide());
			            				storageBin.setPlant(plants.get(0));
			            				storageBin.setStorage(storages.get(0));
			            				storageBin.setActive(1);
			            				storageBinMstService.addStorageBin(storageBin);
			            			}
			            		}
			            		if(!line[6].isEmpty()) {
			            			List<StorageBinMst> storageBinMst = storageBinMstService.getStorageBinByCode(storageBinCode);
				            		List<ItemLocMap> itemLocMaps = itemLocMapService.getItemLocByCodeAndItem(storageCsvDto.getItemId(), storageBinMst.get(0).getStorageBinId());
				            		if(itemLocMaps.size()==0 && !storageCsvDto.getItemId().isEmpty()) {
				            			ItemLocMap theItemLocMap = new ItemLocMap();
				            			theItemLocMap.setItemMst(itemMstServices.getItemById(storageCsvDto.getItemId()).get(0));
				            			theItemLocMap.setStorageBinMst(storageBinMstService.getStorageBinByCode(storageBinCode).get(0));
				            			itemLocMapService.saveitemLocMap(theItemLocMap);
				            		}
			            		}
			            		
				            }			          	

				        } catch (IOException e) {
				        	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
				        }
				}
			}
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);		
		}
	}
	
	/* for uploading old Stock */
	@PostMapping(API.uploadOldStock)
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
						
						
						Plant plant = storageBinMstService.getPlantByCode("1000").get(0);
					//	Storage storage = storageBinMstService.getstorageByCode("1050").get(0);
						

						

						int i = 1;
						while (i <= datatypeSheet.getLastRowNum()) {
//						while (i <= 50) {

							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(i++);

							if (row.getCell(0) != null) {								
								String itemId = formatter.formatCellValue(row.getCell(0));
								String itemDetail = formatter.formatCellValue(row.getCell(1));
								String storageBinCode = formatter.formatCellValue(row.getCell(2));
								String storageCode = formatter.formatCellValue(row.getCell(5));
								double stockQty = Double.parseDouble(formatter.formatCellValue(row.getCell(7)));
								String uom = formatter.formatCellValue(row.getCell(8));
								System.out.println("STORAGE BIN "+storageBinCode);
								Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
							    Matcher matcher = pattern.matcher(storageBinCode);

								if(storageBinCode.isEmpty() || storageCode.isEmpty()  || stockQty<1) {
									System.out.println("In IF Condition Continue");
									continue;
								}
								
							
								//if(storageBinCode.charAt(3)=='L' || storageBinCode.charAt(3)=='R') {
									ItemMst item = itemMstServices.findById(itemId);
									if(item == null) {
										ItemMst newItem = new ItemMst();
										newItem.setId(itemId);
										newItem.setItemDtl(itemDetail);
										item = itemMstServices.add(newItem);										
									}
									
									log.info(i+" - "+item.getId());
									
									Storage storage = storageBinMstService.getSingleStorageByCode(storageCode);
									
//									if(storage==null) {
//										Storage newStorage = new Storage();
//										newStorage.setStorage_location(storageCode);
//										newStorage.setStorage_desc(storageCode);
//										newStorage.setDeletes(1);
//										storage = storageBinMstService.addStorage(newStorage);
//									}
									
									
									StorageBinMst storageBin = storageBinMstService.findByStorageBinByCode(storageBinCode);
									if(storageBin == null && storage!=null) {
										StorageBinMst newStorageBin = new StorageBinMst();
										newStorageBin.setStorageBinCode("1000-"+storageCode+"-"+storageBinCode);
										System.out.println("STORAGE BIN 1"+storageBinCode);
										System.out.println("Length"+storageBinCode.length());
										
										newStorageBin.setRackLetter(Character.toString(storageBinCode.charAt(0)));
									
										if(storageBinCode.length()>2) {
											newStorageBin.setColumnNo(Character.toString(storageBinCode.charAt(1)));
										}
										if(storageBinCode.length()>3) {
											newStorageBin.setRowLetter(Character.toString(storageBinCode.charAt(2)));
										}
										if(storageBinCode.length()>4) {
											newStorageBin.setPaletteSide(Character.toString(storageBinCode.charAt(3)));
										}
										
										
										newStorageBin.setActive(1);
										newStorageBin.setPlant(plant);
										newStorageBin.setStorage(storage);
										System.out.println("");
										storageBin = storageBinMstService.add(newStorageBin);
									}
									
									ItemLocMap itemLoc = itemLocMapService.findByItemAndStorage(item, storageBin);
									
									if(itemLoc == null) {
										ItemLocMap newItemLoc = new ItemLocMap();
										newItemLoc.setItemMst(item);
										newItemLoc.setStorageBinMst(storageBin);
										itemLoc = itemLocMapService.add(newItemLoc);
					
									}
									
									
									MtlStockIn matIn = mtlStockInServices.getByItemAndStorage(item,storageBin.getStorageBinCode());
									
									if(matIn == null) {
										MtlStockIn newMatIn = new MtlStockIn();
										newMatIn.setInMtlQty(stockQty);
										newMatIn.setItemMst(item);
										newMatIn.setRemainQty(stockQty);
										newMatIn.setStatus("A");
										newMatIn.setStorageBinCode(storageBin.getStorageBinCode());
										matIn = mtlStockInServices.save(newMatIn);
										
									}else {
										/*matIn.setInMtlQty(matIn.getInMtlQty() + stockQty);
										matIn.setRemainQty(matIn.getRemainQty() + stockQty);*/
										matIn.setInMtlQty(stockQty);
										matIn.setRemainQty(stockQty);
										matIn = mtlStockInServices.save(matIn);
									}
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
