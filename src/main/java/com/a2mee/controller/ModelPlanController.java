package com.a2mee.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.a2mee.dao.MtlStockInDao;
import com.a2mee.dao.PickingCompDao;
import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.ModelMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.PickingAssembly;
import com.a2mee.model.PickingComponent;
import com.a2mee.model.PickingMst;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.dto.ComponentRequestDto;
import com.a2mee.model.dto.ModelPoDto;
import com.a2mee.model.dto.PickStockDto;
import com.a2mee.model.dto.PickStockMultipleDto;
import com.a2mee.model.dto.PickingAssemblyDto;
import com.a2mee.model.dto.PickingCompDto;
import com.a2mee.model.dto.PickingComponentDto;
import com.a2mee.model.dto.PickingComponentDtoObj;
import com.a2mee.model.dto.PickingDto;
import com.a2mee.model.dto.ResponseDto;
import com.a2mee.model.dto.StockDto;
import com.a2mee.services.AssemblyService;
import com.a2mee.services.CompService;
import com.a2mee.services.ModelService;
import com.a2mee.services.PickingService;
import com.a2mee.util.API;
import com.a2mee.util.ComponentRequestTestDto;

@RestController
@RequestMapping(API.modelPlan)
@CrossOrigin("*")
public class ModelPlanController {

	@Autowired
	ModelService modelService;

	@Autowired
	AssemblyService assemblyService;

	@Autowired
	CompService componentService;

	@Autowired
	PickingService pickingService;
	@Autowired
	MtlStockInDao mtlStockInDao;

	@Autowired
	PickingCompDao pickingCompDao;
	/* for Desktop Screen */
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	
	
	
	@PostMapping(API.getLocationWiseCompsByAssmCodePO)
	public @ResponseBody PickingComponentDtoObj getDatByPicIDAndAsseblyCode(@RequestBody ComponentRequestTestDto componentRequestDto) {

					List<PickingComponent> pickingComponents = pickingCompDao.getPickingComByAssmPoAndPickId(componentRequestDto.getPickingIds(),componentRequestDto.getAssemblyCodes());
					
					//System.out.println("PickingComponentDto :: "+pickingComponents.size()+"   PO :: "+componentRequestDto.getPickingIds()+"  assembly :: "+componentRequestDto.getAssemblyCodes());

					List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
					List<PickingComponentDto> repickingComponents = new ArrayList<PickingComponentDto>();
					PickingComponentDtoObj componentDtoObj= new  PickingComponentDtoObj();
					for(PickingComponent pickingComponent:pickingComponents){
						PickingComponentDto pickingComponentDto = new PickingComponentDto();
						if(pickingComponentDtos.stream().anyMatch(componentDto -> componentDto.getCompCode().equalsIgnoreCase(pickingComponent.getComponentMst().getCompCode()) && componentDto.getAssemblyCode().equalsIgnoreCase(pickingComponent.getComponentMst().getAssembly().getAssemblyCode()) )) {
							
							System.out.println("Component  Match : "+pickingComponent.getComponentMst().getCompCode()+"  For Assembly : "+pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyCode()+"   QTY :: "+pickingComponent.getComponentMst().getCompQty());

							
							pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode()) && componentDto.getAssemblyCode().equals(pickingComponent.getComponentMst().getAssembly().getAssemblyCode())).forEach(componentDto -> componentDto.setPickCompQty(componentDto.getPickCompQty()+pickingComponent.getPickCompQty()));
							pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode()) && componentDto.getAssemblyCode().equals(pickingComponent.getComponentMst().getAssembly().getAssemblyCode())).forEach(componentDto -> componentDto.setPickedQty(componentDto.getPickedQty()+pickingComponent.getPickedQty()));
						
						
						}else {
							
							pickingComponentDto.setAssemblyCode(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyCode());
							pickingComponentDto.setAssemblyDesc(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyDesc());
							pickingComponentDto.setCompCode(pickingComponent.getComponentMst().getCompCode());
							pickingComponentDto.setCompDesc(pickingComponent.getComponentMst().getCompDesc());
							pickingComponentDto.setPickCompQty(pickingComponent.getPickCompQty());
							pickingComponentDto.setPickedQty(pickingComponent.getPickedQty());
							
							System.out.println("Component add   : "+pickingComponentDto.getCompCode()+"  For Assembly : "+pickingComponentDto.getAssemblyCode()+"   QTY :: "+pickingComponentDto.getPickCompQty());

							
							List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(pickingComponentDto.getCompCode());
							if(locations.size()!=0){
								pickingComponentDto.setLocation(locations.get(0).getStorageBinMst().getStorageBinCode());
							}
							
							
							
							double availableQty =0;
							List<MtlStockIn> mtlStockInList = new ArrayList<MtlStockIn>();
							try {
								mtlStockInList = mtlStockInDao.getAvailableStockByComp(pickingComponent.getComponentMst().getCompCode());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(mtlStockInList.size()>0) {
								for (MtlStockIn mtlStockIn : mtlStockInList) {
									availableQty = mtlStockIn.getRemainQty()+availableQty;
								}
							//	availableQty = mtlStockInList.get(0).getRemainQty();
								pickingComponentDto.setAvailableQty(availableQty);
							}else {
								pickingComponentDto.setAvailableQty(0);
							}
							if(pickingComponentDto.getPickedQty() != 0){
								pickingComponentDto.setStatus(2);
							}
							if(pickingComponentDto.getPickCompQty()==pickingComponentDto.getPickedQty()){
								pickingComponentDto.setStatus(1);
							}
							pickingComponentDtos.add(pickingComponentDto);
					}
						/*List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(pickingComponentDto.getCompCode());
						for(ItemLocMap locMap:locations){
											
											PickingComponentDto componentDto2= new PickingComponentDto();
											componentDto2.setAssemblyCode(pickingComponentDto.getAssemblyCode());
											componentDto2.setAssemblyDesc(pickingComponentDto.getAssemblyDesc());
											componentDto2.setCompCode(pickingComponentDto.getCompCode());
											componentDto2.setCompDesc(pickingComponentDto.getCompDesc());
											componentDto2.setPickCompQty(pickingComponentDto.getPickCompQty()-pickingComponentDto.getPickedQty());
											
											componentDto2.setStatus(pickingComponentDto.getStatus());
											componentDto2.setLocation(locMap.getStorageBinMst().getStorageBinCode());
											//System.out.println("Location For Compo :"+pickingComponentDto.getCompCode()+" is :: "+locMap.getStorageBinMst().getStorageBinCode());
										//	componentDto.setLocation(locMap.getStorageBinMst().getStorageBinCode());
				
										//	repickingComponents.add(componentDto);
											//System.out.println("Component :: "+componentDto.getCompCode()+"    Storage Loaction :"+locMap.getStorageBinMst().getStorageBinCode());
											List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByCompAndBinCode(pickingComponentDto.getCompCode(),locMap.getStorageBinMst().getStorageBinCode());
											//System.out.println("SIZE MTL"+mtlStockInList.size());
											if (mtlStockInList.size()!=0) {
												double remainsQty=0;
												for(MtlStockIn mtlStockIn:mtlStockInList){
													if(mtlStockIn.getRemainQty()>remainsQty){
														remainsQty=mtlStockIn.getRemainQty();
													}
				
												}
												componentDto2.setAvailableQty(remainsQty);
												
												
											}else{
												componentDto2.setAvailableQty(0);
											}
											//componentDto.setLocation(locMap.getStorageBinMst().getStorageBinCode());
													if(componentDto2.getPickedQty() != 0){
														componentDto2.setStatus(2);
													}
													if(componentDto2.getPickCompQty()==componentDto2.getPickedQty()){
														componentDto2.setStatus(1);
													}
											repickingComponents.add(componentDto2);
							}*/
			
					}
					componentDtoObj.setComponents(pickingComponentDtos);
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getPickCompQty()==componentDto.getPickedQty()).forEach(componentDto -> componentDto.setStatus(1));
					
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getPickCompQty()>componentDto.getPickedQty()).filter(componentDto -> componentDto.getPickedQty() != 0).forEach(componentDto -> componentDto.setStatus(2));
					//System.out.println("END : "+new Date());
					return componentDtoObj;
		
	}
	
	
	
	

	/* for uploading Model Plan */
	@PostMapping(API.uploadModelPlan)
	public @ResponseBody ResponseEntity postFile(ModelMap model, @ModelAttribute(value = "file") MultipartFile file,
			HttpServletRequest request) throws ParseException {
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
						logger.info("Sheet Name:" + datatypeSheet.getSheetName());
						int i = 1;
						while (i <= datatypeSheet.getLastRowNum()) { // for saving model plan

							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(i++);
							logger.info("Cell 0:" + row.getCell(0));
							if (row.getCell(0) != null) {
								String modelCode = formatter.formatCellValue(row.getCell(0));
								double qty = Double.parseDouble(formatter.formatCellValue(row.getCell(1)));
								String month = formatter.formatCellValue(row.getCell(2));
								logger.info("Model Code:" + modelCode + " Month:" + month + " QTy:" + qty);
								if (month.length() > 3) {
									month = month.substring(0, 3).toUpperCase();
								} else {
									month = month.toUpperCase();
								}

								Calendar cal = Calendar.getInstance();
								Calendar cal2 = Calendar.getInstance();

								SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.ENGLISH);
								cal2.setTime(sdf.parse(month));
								if ((cal2.get(Calendar.MONTH)) < (cal.get(Calendar.MONTH))) {
									System.out.println("helloooo");
									continue;
								}

								String year = formatter.formatCellValue(row.getCell(3));
								if (Integer.parseInt(year) < Calendar.getInstance().get(Calendar.YEAR)) {
									continue;
								}
								logger.info("Model Code:" + modelCode + " Month:" + month + " Year:" + year);

								List<ModelPlan> modelPlans = modelService.getModelPlanByFields(modelCode, month, year);
								DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
								List<ModelPlan> theModelPlans = new ArrayList<ModelPlan>();
								if (modelPlans == null) {

									for (int qtyLoop = 1; qtyLoop <= qty; qtyLoop++) {
										ModelPlan theModelPlan = new ModelPlan();

										String qrcode = modelCode + "-" + (int) qty + "-" + df.format(new Date()) + "-"
												+ StringUtils.leftPad(String.valueOf(qtyLoop), 4, "0");
										theModelPlan.setModelCode(modelCode);
										theModelPlan.setMonth(month);
										theModelPlan.setQrcode(qrcode);
										theModelPlan.setQty(1);
										theModelPlan.setYear(year);
										theModelPlans.add(theModelPlan);
									}

									modelService.addModelPlans(theModelPlans);
								} else {
									ModelPlan lastModelPlan = modelPlans.get(modelPlans.size() - 1);
									String lastQr = lastModelPlan.getQrcode();
									String lastFourDigit = lastQr.substring(lastQr.length() - 4);
									for (int qtyLoop = 1; qtyLoop <= qty; qtyLoop++) {
										ModelPlan theModelPlan = new ModelPlan();
										int serialValue = Integer.parseInt(lastFourDigit) + qtyLoop;
										String qrcode = modelCode + "-" + (int) qty + "-" + df.format(new Date()) + "-"
												+ StringUtils.leftPad(String.valueOf(serialValue), 4, "0");
										theModelPlan.setModelCode(modelCode);
										theModelPlan.setMonth(month);
										theModelPlan.setQrcode(qrcode);
										theModelPlan.setQty(1);
										theModelPlan.setYear(year);
										theModelPlans.add(theModelPlan);
									}

									modelService.addModelPlans(theModelPlans);
								}
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
	
	/* for uploading Reservation and Item No */
	@PostMapping(API.uploadReservationItemNo)
	public @ResponseBody ResponseEntity uploadReservationItemNo(ModelMap model, @ModelAttribute(value = "file") MultipartFile file,
			HttpServletRequest request) throws ParseException {
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
						logger.info("Sheet Name:" + datatypeSheet.getSheetName());
						int i = 1;
						while (i <= datatypeSheet.getLastRowNum()) { // for saving model plan

							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(i++);
							if (row.getCell(0) != null) {
								
								String proOrdNo = formatter.formatCellValue(row.getCell(4));
								String reservationNo = formatter.formatCellValue(row.getCell(6));
								String itemId = formatter.formatCellValue(row.getCell(0));
								String assmCode = formatter.formatCellValue(row.getCell(9));
								if(itemId!=""){
								int itemNo = Integer.parseInt(formatter.formatCellValue(row.getCell(7)));
								String postingDocNo = formatter.formatCellValue(row.getCell(8));
								double qty = Double.parseDouble(formatter.formatCellValue(row.getCell(3)));  

								
								List<PickingMst> pickings = pickingService.getPickingByPrdOrdNo(proOrdNo);

								if(pickings!=null||pickings.size()!=0) {
									for (PickingMst pickingMst : pickings) {
										pickingMst.setReservationNo(reservationNo);
									
										List<PickingComponent> pickingComponents = pickingService.getPickingComponentsByModelPlanAndComponentAndAssemblyCode(pickingMst.getPickingId(),itemId,assmCode);
										System.out.println(" ASSM Code :: "+assmCode+" Com Code :: "+itemId+"  Picking Id :: "+pickingMst.getPickingId()+" SIZE :: "+pickingComponents.size()+" for :: "+i);
										
										if(pickingComponents.size()!=0) {
											//PickingComponent pickingComponent =pickingComponents.get(0);
											for (PickingComponent pickingComponent : pickingComponents) {
											
												//System.out.println("Componant :: "+pickingComponent.getComponentMst().getCompCode()+" PickCompQty  "+pickingComponent.getComponentMst().getCompQty()+"    Picking COM Id "+pickingComponent.getPickingCompId()+ "  ROW QTY  "+qty+" ROW NO  "+i);
												if(pickingComponent.getComponentMst().getCompQty()==qty){
													//System.out.println("====================SET ITEM NO===================");
													pickingComponent.setItemNo(itemNo);
													pickingComponent.setItemBit(1);
													System.out.println("ITEM NO UPLOADED ::"+pickingComponent.getComponentMst().getCompCode()+"  itemNo     "+itemNo );
													if(postingDocNo!=null) {
														pickingComponent.setPostingDocNo(postingDocNo);
													}
													//break;
												}else{
													
													System.out.println("====================NOT SET ITEM NO===================");
												}
												
												//pickingComponent.setItemBit(1);
												pickingService.addPickingComponent(pickingComponent);
												
											}
											
										}
									}
									
									
									
								}
							}
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

	/* for adding manual ModelPlan */
	@PostMapping(API.addModelPlans)
	public @ResponseBody ResponseEntity<List<ModelPlan>> addModelPlans(@RequestBody List<ModelPlan> modelPlans) {
		try {

			modelService.addModelPlans(modelPlans);

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* for adding manual ModelPlan */
	@PostMapping(API.addModelPlan)
	public @ResponseBody ResponseEntity<List<ModelPlan>> addModelPlan(@RequestBody ModelPlan modelPlan) {
		try {
			List<ModelPlan> registeredPlans = modelService.getModelPlansByFields(modelPlan.getModelCode(),
					modelPlan.getMonth(), modelPlan.getYear());
			System.out.println(modelPlan);
			int count = 0;
			System.out.println(registeredPlans.size());
			if (registeredPlans.size() != 0) {
				for (ModelPlan registeredPlan : registeredPlans) {
					if (registeredPlan.getAssigned() == 0) {
						count++;
						registeredPlan.setQty(modelPlan.getQty());
						modelService.addModelPlan(registeredPlan);
						break;
					}
				}
			}

			if (count == 0) {
				modelService.addModelPlan(modelPlan);
			}
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* get entire modelPLan */
	@GetMapping(API.modelPlanList)
	public @ResponseBody ResponseEntity getModelPlan() {
		try {
			return new ResponseEntity(modelService.getModelPlan(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* for viewing models to be build this month */
	@GetMapping(API.getModelByMonth)
	public @ResponseBody ResponseEntity getModelByMonth(@RequestParam(value = "month") String month,
			@RequestParam(value = "year") String year) {
		try {
			return new ResponseEntity(modelService.getModelByMonth(month, year), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* for viewing assemblies by selected model selected month */
	@PostMapping(API.getAssmByModel)
	public @ResponseBody ResponseEntity<List<AssemblyMst>> getAssmByModel(@RequestBody ModelPlan modelPlan) {
		try {
			List<AssemblyMst> assemblies = assemblyService.getAssmByModel(modelPlan);
			return new ResponseEntity<>(assemblies, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * for viewing components by selected assembly of selected model of selected
	 * month
	 */
	@PostMapping(API.getCompByAssm)
	public @ResponseBody ResponseEntity<List<ComponentMst>> getCompByAssm(@RequestBody AssemblyMst assemblyMst) {
		try {
			List<ComponentMst> components = componentService.getCompByAssm(assemblyMst);
			System.out.println("PROD NOT :: "+assemblyMst.getProOrdNo());
			for(ComponentMst componentMst:components){
				List<PickingComponent> list= modelService.getPickingComponantByComponentAndPO(componentMst,assemblyMst.getProOrdNo());
				for(PickingComponent  component:list){
						System.out.println(" PC :: "+component.getComponentMst().getCompCode()+"   "+component.getItemBit());
			
						if(component.getItemBit()!=0){
							componentMst.setReservationNo(list.get(0).getPickingMst().getReservationNo());
						}else{
							componentMst.setReservationNo("Not Found");
						}
				
				
				
				}
				
			}
			return new ResponseEntity<>(components, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* for viewing total assemblies to be build this month */
	@GetMapping(API.getAssmByMonth)
	public @ResponseBody ResponseEntity<List<AssemblyMst>> getAssmByMonth(@RequestParam(value = "month") String month,
			@RequestParam(value = "year") String year) {
		try {
			return new ResponseEntity<>(assemblyService.getAssmByMonth(month, year), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* for viewing total components to be build this month */
	@GetMapping(API.getCompByMonth)
	public @ResponseBody ResponseEntity<List<ComponentMst>> getCompByMonth(@RequestParam(value = "month") String month,
			@RequestParam(value = "year") String year) {
		try {
			return new ResponseEntity<>(componentService.getCompByMonth(month, year), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Update Model Plan with Picker and PO : Praful */
	@SuppressWarnings("rawtypes")
	@PostMapping(API.updateModelPlan)
	public @ResponseBody ResponseEntity updateModelPlan(@RequestBody ModelPlan modelPlan) {
		try {
			modelService.updateModelPlan(modelPlan);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Assigning picker to a Model Plan */
	@SuppressWarnings("rawtypes")
	@PostMapping(API.assignPicker)
	public @ResponseBody ResponseEntity assignPicker(@RequestBody PickingMst pickingMst) {
		try {
			pickingService.assignPicker(pickingMst);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Kitting Area */
	@GetMapping(API.getKitStock)
	public @ResponseBody ResponseEntity<List<PickingAssembly>> getKitStock() {
		try {
			List<PickingAssembly> pickingAssemblies = pickingService.getKitStock();
			return new ResponseEntity<>(pickingAssemblies, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* ****************** For Mobile Screen ****************** */
	@GetMapping(API.getTotalComplCount)
	public @ResponseBody ResponseEntity<Double> getTotalComplCount(@RequestParam(value = "userName") String userName) {
		try {
			double count = pickingService.getTotalComplCount(userName);
			return new ResponseEntity<>(count, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.getAssms)
	public @ResponseBody ResponseEntity<List<PickingAssembly>> getAssms(
			@RequestParam(value = "userName") String userName) {
		try {
			System.out.println("USerName :: " + userName);
			List<PickingAssembly> pickingAssemblies = pickingService.getAssms(userName);
			return new ResponseEntity<>(pickingAssemblies, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.getAssmsByUserAndModel)
	public @ResponseBody ResponseEntity<List<PickingAssembly>> getAssmsByUserAndModel(
			@RequestParam(value = "userName") String userName, @RequestParam(value = "model") String model) {
		try {
			System.out.println("USerName :: " + userName);
			List<PickingAssembly> pickingAssemblies = pickingService.getAssms(userName);
			List<PickingAssembly> pickingAssemblies1 = new ArrayList<PickingAssembly>();
			for (PickingAssembly pc : pickingAssemblies) {
				if (pc.getAssemblyMst().getModel().getModelCode().equalsIgnoreCase(model)) {
					pickingAssemblies1.add(pc);
				}

			}
			return new ResponseEntity<>(pickingAssemblies1, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@PostMapping(API.getAssmsByPOModelCode)
//	public @ResponseBody ResponseEntity<List<PickingAssemblyDto>> getAssmsByPOModelCode(
//			@RequestBody List<ModelPoDto> modelPos) {
//		System.out.println("Model PO:" + modelPos.toString());
//		try {
//
//			// List<PickingAssembly> pickingAssemblies = pickingService.getAssms(userName);
//
//			List<PickingAssemblyDto> pickingAssemblies = pickingService.getAssmsByPO(modelPos);
//
//			// List<PickingAssembly> pickingAssemblies1 = new ArrayList<PickingAssembly>();
//			// for(PickingAssembly pc:pickingAssemblies) {
//			// if(pc.getAssemblyMst().getModel().getModelCode().equalsIgnoreCase(model)) {
//			// pickingAssemblies1.add(pc);
//			// }
//			//
//			//
//			// }
//			return new ResponseEntity<>(pickingAssemblies, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	// Mobile Api for models by Picker
	@GetMapping(API.getAssmsByModelName)
	public @ResponseBody ResponseEntity<List<ModelMst>> getAssmsByModelName(
			@RequestParam(value = "userName") String userName) {
		try {
			System.out.println("USER IS "+userName);
			List<PickingAssembly> pickingAssemblies = pickingService.getAssms(userName);
			Set<String> madels = new HashSet<String>();
			List<ModelMst> modelNames = new ArrayList<ModelMst>();
			for (PickingAssembly pc : pickingAssemblies) {
				madels.add(pc.getAssemblyMst().getModel().getModelCode());
			}
			for (String str : madels) {
				ModelMst modelMst = new ModelMst();
				modelMst.setModelCode(str);
				modelNames.add(modelMst);
			}
			return new ResponseEntity<>(modelNames, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(API.getCompsByAssm)
	public @ResponseBody ResponseEntity<List<PickingComponent>> getCompsByAssm(
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "assemblyCode") String assemblyCode) {
		try {
			List<PickingComponent> pickingComponents = pickingService.getCompsByAssm(userName, assemblyCode);
			pickingComponents.forEach((p) -> {
				p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			});
			return new ResponseEntity<>(pickingComponents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@PostMapping(API.getCompsByAssmCodePO)
//	public @ResponseBody ResponseEntity<List<PickingComponentDto>> getCompsByAssmCodePO(
//			@RequestParam(value = "assemblyCode") String assemblyCode, @RequestBody List<ModelPoDto> modelPos) {
//		try {
//			System.out.println("Comps by Assembly Code:" + assemblyCode);
//			List<PickingComponentDto> pickingComponents = pickingService.getCompsByAssmCodePO(modelPos, assemblyCode);
//			// pickingComponents.forEach((p) -> {
//			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
//			// });
//			return new ResponseEntity<>(pickingComponents, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@GetMapping(API.getStockByComp)
	public @ResponseBody ResponseEntity<List<StockDto>> getStockByComp(
			@RequestParam(value = "compCode") String compCode ) {
		try {
			//
			List<StockDto> stocks = pickingService.getStockByComp(compCode);
			System.out.println("stocks :: " + stocks.size());
			for(StockDto stockDto:stocks){
				System.out.println("CHECK  Stock::  "+stockDto.toString());
				
			}
			return new ResponseEntity<>(stocks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.getPOByComp)
	public @ResponseBody ResponseEntity<List<StockDto>> getPOByComp(
			@RequestParam(value = "compCode") String compCode) {
		try {
			System.out.println("COMPONAMT CODE :: " + compCode);
			List<StockDto> stocks = pickingService.getStockByComp(compCode);
			return new ResponseEntity<>(stocks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//Old Code Update Picking Stock
	/*@PostMapping(API.updateStocks)
	public @ResponseBody ResponseEntity updateStocks(@RequestBody PickStockDto pickStockDto) {
		try {
			pickingService.updateStocks(pickStockDto);
			ResponseDto responseDto = new ResponseDto();
			responseDto.setMessage("Success");
			return new ResponseEntity(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDto responseDto = new ResponseDto();
			responseDto.setMessage("Fail");
			return new ResponseEntity(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	@PostMapping(API.updateStocks)
	public @ResponseBody ResponseEntity updateStocks(@RequestBody PickStockMultipleDto pickStockMultipleDto) {
		try {
			pickingService.updateStocks(pickStockMultipleDto);
			
			ResponseDto responseDto = new ResponseDto();
			responseDto.setMessage("Success");
			return new ResponseEntity(responseDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDto responseDto = new ResponseDto();
			responseDto.setMessage("Fail");
			return new ResponseEntity(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* for gettin pro Order on ProOrder number */
	@GetMapping(API.pickingByPrdOrdNo)
	public @ResponseBody ResponseEntity pickingByPrdOrdNo(@RequestParam(value = "proOrderNo") String proOrderNo) {
		try {
			List<PickingMst> pickings = pickingService.getPickingByPrdOrdNo(proOrderNo);
			return new ResponseEntity(pickings, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* for getting Pro order on So number */
	@GetMapping(API.pickingBySalesOno)
	public @ResponseBody ResponseEntity pickingBySalesOno(@RequestParam(value = "salesOrder") long salesOrder) {
		try {
			List<ProductionOrder> productionOrders = null;
			return new ResponseEntity(productionOrders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.getPickingAssemblyByPicking)
	public @ResponseBody ResponseEntity getPickingAssemblyByPicking(@RequestParam(value = "pickingId") long pickingId) {
		try {
			System.out.println("Picking ID "+pickingId);

			List<PickingAssembly> pickingAssemblies = pickingService.getPickingAssemblyByPicking(pickingId);
			
			System.out.println("Picking ASSEMBLY SIZE "+pickingAssemblies.size());
			return new ResponseEntity(pickingAssemblies, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* for adding manual ModelPlan */
	@PostMapping(API.updateAllPickingAssembly)
	public @ResponseBody ResponseEntity updateAllPickingAssembly(@RequestBody List<PickingAssembly> pickingAssemblies) {
		try {

			pickingService.updateAllPickingAssembly(pickingAssemblies);

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	/* get Production Order by machine code */
	@GetMapping(API.getPOByModelCode)
	public @ResponseBody ResponseEntity getProOrdersByModel(@RequestParam(value = "modelCode") String modelCode) {
		try {
			//System.out.println("Model Code:getPOByModelCode::"+modelCode);
			Set<PickingDto> prodOrds = pickingService.getProOrderByModel(modelCode);
			
			return new ResponseEntity(prodOrds, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.getPickingAssemblyDtoByPicking)
	public @ResponseBody ResponseEntity getPickingAssemblyDtoByPicking(@RequestParam(value = "pickingId") long pickingId) {
		try {
			
			List<PickingAssemblyDto> pickingAssemblyDtos = pickingService.getPickingAssemblyDtoByPicking(pickingId);
			return new ResponseEntity(pickingAssemblyDtos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(API.getAssmsByPOPicking)
	public @ResponseBody ResponseEntity<List<PickingAssemblyDto>> getAssmsByPOPicking(
			@RequestBody List<PickingDto> pickingPos) {
		System.out.println("Model PO:" + pickingPos.size());
		try {
			for(PickingDto dto:pickingPos){
				System.out.println("PROD :: "+dto.getProOrdNo());
				
			}
		//	System.out.println("Prod No Size:: "+pickingPos.size());

			
			List<PickingAssemblyDto> pickingAssemblies = pickingService.getAssmsByPOPicking(pickingPos);
			System.out.println("RES SIZE :: "+pickingAssemblies .size());
			return new ResponseEntity<>(pickingAssemblies, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*@PostMapping(API.getAssmsByPOPicking)
	public @ResponseBody ResponseEntity<List<PickingAssemblyDto>> getAssmsByPOPicking(
			@RequestBody long[] pickingids) {
	//	System.out.println("Model PO:" + pickingPos.toString());
		try {
		//	System.out.println("Prod No Size:: "+pickingPos.size());

			
			List<PickingAssemblyDto> pickingAssemblies = pickingService.getAssmsByPOPickingByArr(pickingids);
			System.out.println("RES SIZE :: "+pickingAssemblies);
			return new ResponseEntity<>(pickingAssemblies, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	@PostMapping(API.getCompsByAssmCodePO)
	public @ResponseBody ResponseEntity<List<PickingComponentDto>> getCompsByAssmCodePO(
			@RequestParam(value = "assemblyCode") String assemblyCode, @RequestBody List<PickingDto> modelPos) {
		try {
			List<PickingComponentDto> pickingComponents = pickingService.getCompsByAssmCodePO(modelPos, assemblyCode);

			for(PickingComponentDto dto:pickingComponents){
				System.out.println("PickingDto OBJ :" + dto.toString());
				List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(dto.getCompCode());
				for(ItemLocMap itemLocMap:locations ){
					System.out.println("Locations : "+itemLocMap.getStorageBinMst().getStorageBinCode());
				}
				//new Date();
			}
			System.out.println("Comps by Assembly Code:" + assemblyCode);
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(pickingComponents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping(API.getCompsByAssmCodePO2)
	public @ResponseBody ResponseEntity<PickingComponentDtoObj> getCompsByAssmCodePO2(@RequestBody ComponentRequestDto componentRequestDto) {
		try {
			PickingComponentDtoObj componentDtoObj= new  PickingComponentDtoObj();
			List<PickingComponentDto> repickingComponents = new ArrayList<PickingComponentDto>();
			for(String assemblyCode :componentRequestDto.getAssemblyCodes()){
				List<PickingComponentDto> pickingComponents = pickingService.getCompsByAssmCodePO(componentRequestDto.getModelPos(), assemblyCode);
				if(pickingComponents.size()!=0){
					for(PickingComponentDto componentDto:pickingComponents){
						List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(componentDto.getCompCode());
						List<String> locationstr= new ArrayList<String>();
						for(ItemLocMap itemLocMap:locations ){
							locationstr.add(itemLocMap.getStorageBinMst().getStorageBinCode());
							System.out.println("Locations for : "+componentDto.getCompCode()+"    :"+itemLocMap.getStorageBinMst().getStorageBinCode());
						}
						//componentDto.setLocations(locationstr);
						repickingComponents.add(componentDto);
					}
					
				}
			
			}
			componentDtoObj.setComponents(repickingComponents);
		//	System.out.println("Comps by Assembly Code:" + assemblyCode);
			
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(componentDtoObj, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@PostMapping(API.getLocationWiseCompsByAssmCodePO)
	public @ResponseBody ResponseEntity<PickingComponentDtoObj> getLocationWiseCompsByAssmCodePO(@RequestBody ComponentRequestDto componentRequestDto) {
		try {
			PickingComponentDtoObj componentDtoObj= new  PickingComponentDtoObj();
			List<PickingComponentDto> repickingComponents = new ArrayList<PickingComponentDto>();
			System.out.println("   PO :: "+componentRequestDto.getModelPos()+"  assembly :: "+componentRequestDto.getAssemblyCodes());

			for(String assemblyCode :componentRequestDto.getAssemblyCodes()){
				List<PickingComponentDto> pickingComponents = pickingService.getCompsByAssmCodePO(componentRequestDto.getModelPos(), assemblyCode);
				if(pickingComponents.size()!=0){
					for(PickingComponentDto componentDto:pickingComponents){
						List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(componentDto.getCompCode());
						for(ItemLocMap locMap:locations){
							PickingComponentDto componentDto2= new PickingComponentDto();
							componentDto2.setAssemblyCode(componentDto.getAssemblyCode());
							componentDto2.setAssemblyDesc(componentDto.getAssemblyDesc());
							componentDto2.setCompCode(componentDto.getCompCode());
							componentDto2.setCompDesc(componentDto.getCompDesc());
							componentDto2.setPickCompQty(componentDto.getPickCompQty()-componentDto.getPickedQty());
							
							componentDto2.setStatus(componentDto.getStatus());
							componentDto2.setLocation(locMap.getStorageBinMst().getStorageBinCode());
							System.out.println("Location For Compo :"+componentDto.getCompCode()+" is :: "+locMap.getStorageBinMst().getStorageBinCode());
						//	componentDto.setLocation(locMap.getStorageBinMst().getStorageBinCode());

						//	repickingComponents.add(componentDto);
							//System.out.println("Component :: "+componentDto.getCompCode()+"    Storage Loaction :"+locMap.getStorageBinMst().getStorageBinCode());
							List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByCompAndBinCode(componentDto.getCompCode(),locMap.getStorageBinMst().getStorageBinCode());
							//System.out.println("SIZE MTL"+mtlStockInList.size());
							if (mtlStockInList.size()!=0) {
								double remainsQty=0;
								for(MtlStockIn mtlStockIn:mtlStockInList){
									if(mtlStockIn.getRemainQty()>remainsQty){
										remainsQty=mtlStockIn.getRemainQty();
									}

								}
								componentDto2.setAvailableQty(remainsQty);
								
								
							}else{
								componentDto2.setAvailableQty(0);
							}
							//componentDto.setLocation(locMap.getStorageBinMst().getStorageBinCode());

							repickingComponents.add(componentDto2);
							}
					}
				}
			}
			
			
			
			
			
		/*	for(String assemblyCode :componentRequestDto.getAssemblyCodes()){
				List<PickingComponentDto> pickingComponents = pickingService.getCompsByAssmCodePO(componentRequestDto.getModelPos(), assemblyCode);
				if(pickingComponents.size()!=0){
					for(PickingComponentDto componentDto:pickingComponents){
						List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(componentDto.getCompCode());
						List<String> locationstr= new ArrayList<String>();
						for(ItemLocMap itemLocMap:locations ){
							locationstr.add(itemLocMap.getStorageBinMst().getStorageBinCode());
							System.out.println("Locations for : "+componentDto.getCompCode()+"    :"+itemLocMap.getStorageBinMst().getStorageBinCode());
						}
						componentDto.setLocations(locationstr);
						repickingComponents.add(componentDto);
					}
					
				}
			
			}*/
			componentDtoObj.setComponents(repickingComponents);
		//	System.out.println("Comps by Assembly Code:" + assemblyCode);
			
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(componentDtoObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	@PostMapping(API.getPickingPOByComp)
	public @ResponseBody ResponseEntity<List<PickingCompDto>> getPickingPOByComp(
			@RequestParam(value = "assemblyCode") String assemblyCode, @RequestParam(value = "compCode") String compCode, @RequestBody List<PickingDto> modelPos) {
		try {
			System.out.println("Comps by Assembly Code:" + assemblyCode);
			List<PickingCompDto> pickingComponents = pickingService.getPickingPOByComp(modelPos, assemblyCode,compCode);
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(pickingComponents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.getPickingComponentByPickingAndAssemblyAndComponent)
	public @ResponseBody ResponseEntity<List<PickingComponentDto>> getPickingComponentByPickingAndAssemblyAndComponent(
			@RequestParam(value = "assemblyCode") String assemblyCode, @RequestParam(value = "compCode") String compCode, @RequestParam(value = "pickingId") long pickingId) {
		try {
			System.out.println("Comps by Assembly Code:" + assemblyCode);
			List<PickingComponentDto> pickingComponents = pickingService.getPickingComponentByPickingAndAssemblyAndComponent(pickingId, assemblyCode,compCode);
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(pickingComponents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.getPickingComponentByPickingAssembly)
	public @ResponseBody ResponseEntity<List<PickingComponent>> getPickingComponentByPickingAssembly(@RequestParam(value = "pickingAssmId") long pickingAssmId) {
		try {
			System.out.println("Comps by Assembly Code:" + pickingAssmId);
			List<PickingComponent> pickingComponents = pickingService.getPickingComponentByPickingAssembly(pickingAssmId);
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(pickingComponents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* for for Update of all Picking Component */
	@PostMapping(API.updateAllPickingComponent)
	public @ResponseBody ResponseEntity updateAllPickingComponent(@RequestBody List<PickingComponent> pickingComponents) {
		try {

			pickingService.updateAllPickingComponent(pickingComponents);

			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* Approve After Customization or without Customization*/
	@GetMapping(API.approved)
	public @ResponseBody ResponseEntity updateApproval(@RequestParam(value = "pickingId") long pickingId) {
		try {
			pickingService.updateApproval(pickingId);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.modelsInPicking)
	public @ResponseBody ResponseEntity<List<String>> modelsInPicking() {
		try {
			
			List<String> models = pickingService.modelsInPicking();
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(models, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(API.POPickingByModel)
	public @ResponseBody ResponseEntity<List<PickingDto>> POPickingByModel(@RequestParam(value = "modelCode") String modelCode) {
		try {
			
			List<PickingDto> pickingDtos = pickingService.POPickingByModel(modelCode);
			// pickingComponents.forEach((p) -> {
			// p.setPickCompQty(p.getPickCompQty() - p.getPickedQty());
			// });
			return new ResponseEntity<>(pickingDtos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping(API.updateLocationWiseComponent)
	public @ResponseBody PickingComponentDto getDatByPicIDAndAsseblyCodeSingle(@RequestBody ComponentRequestTestDto componentTestRequestDto) {
	
		List<PickingComponentDto> repickingComponents = new ArrayList<PickingComponentDto>();
		System.out.println("UPDATE CODE REQUEST:: "+componentTestRequestDto.toString());
			List<PickingComponent> pickingComponents = pickingCompDao.getPickingComByCompAndPickId(componentTestRequestDto.getPickingIds(),componentTestRequestDto.getComCode(),componentTestRequestDto.getAsmCodes());
		
			
			System.out.println("pickingComponents "+pickingComponents.size());
			List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
			PickingComponentDtoObj componentDtoObj= new  PickingComponentDtoObj();

			for(PickingComponent pickingComponent:pickingComponents){
				System.out.println("Component  : "+pickingComponent.getComponentMst().getCompCode()+"  For Assembly : "+pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyCode()+"   QTY :: "+pickingComponent.getComponentMst().getCompQty());
				PickingComponentDto pickingComponentDto = new PickingComponentDto();
				if(pickingComponentDtos.stream().anyMatch(componentDto -> componentDto.getCompCode().equalsIgnoreCase(pickingComponent.getComponentMst().getCompCode())&& componentDto.getAssemblyCode().equals(pickingComponent.getComponentMst().getAssembly().getAssemblyCode()))) {
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode())&& componentDto.getAssemblyCode().equals(pickingComponent.getComponentMst().getAssembly().getAssemblyCode())).forEach(componentDto -> componentDto.setPickCompQty(componentDto.getPickCompQty()+pickingComponent.getPickCompQty()));
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode())&& componentDto.getAssemblyCode().equals(pickingComponent.getComponentMst().getAssembly().getAssemblyCode())).forEach(componentDto -> componentDto.setPickedQty(componentDto.getPickedQty()+pickingComponent.getPickedQty()));
				
				
				
				}else {
					
					pickingComponentDto.setAssemblyCode(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyCode());
					pickingComponentDto.setAssemblyDesc(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyDesc());
					pickingComponentDto.setCompCode(pickingComponent.getComponentMst().getCompCode());
					pickingComponentDto.setCompDesc(pickingComponent.getComponentMst().getCompDesc());
					pickingComponentDto.setPickCompQty(pickingComponent.getPickCompQty());
					pickingComponentDto.setPickedQty(pickingComponent.getPickedQty());
					List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(pickingComponentDto.getCompCode());
					if(locations.size()!=0){
						pickingComponentDto.setLocation(locations.get(0).getStorageBinMst().getStorageBinCode());
					}
					
					
					
					double availableQty =0;
					List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByComp(pickingComponent.getComponentMst().getCompCode());
					if(mtlStockInList.size()>0) {
						for (MtlStockIn mtlStockIn : mtlStockInList) {
							availableQty = mtlStockIn.getRemainQty()+availableQty;
						}
					//	availableQty = mtlStockInList.get(0).getRemainQty();
						pickingComponentDto.setAvailableQty(availableQty);
					}else {
						pickingComponentDto.setAvailableQty(0);
					}
					if(pickingComponentDto.getPickedQty() != 0){
						pickingComponentDto.setStatus(2);
					}
					if(pickingComponentDto.getPickCompQty()==pickingComponentDto.getPickedQty()){
						pickingComponentDto.setStatus(1);
					}
					pickingComponentDtos.add(pickingComponentDto);
			}
				/*List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(pickingComponentDto.getCompCode());
				for(ItemLocMap locMap:locations){
									
									PickingComponentDto componentDto2= new PickingComponentDto();
									componentDto2.setAssemblyCode(pickingComponentDto.getAssemblyCode());
									componentDto2.setAssemblyDesc(pickingComponentDto.getAssemblyDesc());
									componentDto2.setCompCode(pickingComponentDto.getCompCode());
									componentDto2.setCompDesc(pickingComponentDto.getCompDesc());
									componentDto2.setPickCompQty(pickingComponentDto.getPickCompQty()-pickingComponentDto.getPickedQty());
									
									componentDto2.setStatus(pickingComponentDto.getStatus());
									componentDto2.setLocation(locMap.getStorageBinMst().getStorageBinCode());
									//System.out.println("Location For Compo :"+pickingComponentDto.getCompCode()+" is :: "+locMap.getStorageBinMst().getStorageBinCode());
								//	componentDto.setLocation(locMap.getStorageBinMst().getStorageBinCode());
		
								//	repickingComponents.add(componentDto);
									//System.out.println("Component :: "+componentDto.getCompCode()+"    Storage Loaction :"+locMap.getStorageBinMst().getStorageBinCode());
									List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByCompAndBinCode(pickingComponentDto.getCompCode(),locMap.getStorageBinMst().getStorageBinCode());
									//System.out.println("SIZE MTL"+mtlStockInList.size());
									if (mtlStockInList.size()!=0) {
										double remainsQty=0;
										for(MtlStockIn mtlStockIn:mtlStockInList){
											if(mtlStockIn.getRemainQty()>remainsQty){
												remainsQty=mtlStockIn.getRemainQty();
											}
		
										}
										componentDto2.setAvailableQty(remainsQty);
										
										
									}else{
										componentDto2.setAvailableQty(0);
									}
									//componentDto.setLocation(locMap.getStorageBinMst().getStorageBinCode());
											if(componentDto2.getPickedQty() != 0){
												componentDto2.setStatus(2);
											}
											if(componentDto2.getPickCompQty()==componentDto2.getPickedQty()){
												componentDto2.setStatus(1);
											}
									repickingComponents.add(componentDto2);
					}*/
	
			}
		/*	List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
				for(PickingComponent pickingComponent:pickingComponents){
				PickingComponentDto pickingComponentDto = new PickingComponentDto();
				if(pickingComponentDtos.stream().anyMatch(componentDto -> componentDto.getCompCode() == pickingComponent.getComponentMst().getCompCode())) {
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode())).forEach(componentDto -> componentDto.setPickCompQty(componentDto.getPickCompQty()+pickingComponent.getPickCompQty()));
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode())).forEach(componentDto -> componentDto.setPickedQty(componentDto.getPickedQty()+pickingComponent.getPickedQty()));
				}else {
					pickingComponentDto.setAssemblyCode(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyCode());
					pickingComponentDto.setAssemblyDesc(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyDesc());
					pickingComponentDto.setCompCode(pickingComponent.getComponentMst().getCompCode());
					pickingComponentDto.setCompDesc(pickingComponent.getComponentMst().getCompDesc());
					pickingComponentDto.setPickCompQty(pickingComponent.getPickCompQty());
					pickingComponentDto.setPickedQty(pickingComponent.getPickedQty());
					double availableQty =0;
					List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByComp(pickingComponent.getComponentMst().getCompCode());
					if(mtlStockInList.size()>0) {
						for (MtlStockIn mtlStockIn : mtlStockInList) {
							availableQty = mtlStockIn.getRemainQty()+availableQty;
						}
					//	availableQty = mtlStockInList.get(0).getRemainQty();
						pickingComponentDto.setAvailableQty(availableQty);
					}else {
						pickingComponentDto.setAvailableQty(0);
					}
		
					pickingComponentDtos.add(pickingComponentDto);
			}
				List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(pickingComponentDto.getCompCode());
				for(ItemLocMap locMap:locations){
					PickingComponentDto componentDto2= new PickingComponentDto();
					componentDto2.setAssemblyCode(pickingComponentDto.getAssemblyCode());
					componentDto2.setAssemblyDesc(pickingComponentDto.getAssemblyDesc());
					componentDto2.setCompCode(pickingComponentDto.getCompCode());
					componentDto2.setCompDesc(pickingComponentDto.getCompDesc());
					componentDto2.setPickCompQty(pickingComponentDto.getPickCompQty()-pickingComponentDto.getPickedQty());
					
					componentDto2.setStatus(pickingComponentDto.getStatus());
					componentDto2.setLocation(locMap.getStorageBinMst().getStorageBinCode());
					System.out.println("UPDATE Location For Compo :"+pickingComponentDto.getCompCode()+" is :: "+locMap.getStorageBinMst().getStorageBinCode());
			
					List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByCompAndBinCode(pickingComponentDto.getCompCode(),locMap.getStorageBinMst().getStorageBinCode());
					if (mtlStockInList.size()!=0) {
						double remainsQty=0;
						for(MtlStockIn mtlStockIn:mtlStockInList){
							if(mtlStockIn.getRemainQty()>remainsQty){
								remainsQty=mtlStockIn.getRemainQty();
							}

						}
						componentDto2.setAvailableQty(remainsQty);
						
						
					}else{
						componentDto2.setAvailableQty(0);
					}
					//componentDto.setLocation(locMap.getStorageBinMst().getStorageBinCode());

				repickingComponents.add(componentDto2);
					}
	
			}*/
			componentDtoObj.setComponents(pickingComponentDtos);
			pickingComponentDtos.stream().filter(componentDto -> componentDto.getPickCompQty()==componentDto.getPickedQty()).forEach(componentDto -> componentDto.setStatus(1));
			
			pickingComponentDtos.stream().filter(componentDto -> componentDto.getPickCompQty()>componentDto.getPickedQty()).filter(componentDto -> componentDto.getPickedQty() != 0).forEach(componentDto -> componentDto.setStatus(2));
			//System.out.println("END : "+new Date());
			return pickingComponentDtos.get(0);					
	}
}
