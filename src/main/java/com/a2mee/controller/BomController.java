package com.a2mee.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.ItemMst;
import com.a2mee.model.ModelMst;
import com.a2mee.services.AssemblyService;
import com.a2mee.services.CompService;
import com.a2mee.services.ItemMstServices;
import com.a2mee.services.ModelService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.bom)
@CrossOrigin("*")
public class BomController {

	@Autowired
	ModelService modelMstService;

	@Autowired
	AssemblyService assemblyMstService;
	
	@Autowired
	CompService compMstService;
	
	@Autowired
	ItemMstServices itemMstServices;

	/* for Desktop Screen */
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/* for uploading BOM */
	@PostMapping(API.uploadBom)
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
						FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

						Sheet datatypeSheet = workbook.getSheetAt(0); //contains model and assembly
						Sheet datatypeSheet2 = workbook.getSheetAt(1); //contains components to model
						DataFormatter formatter = new DataFormatter();

						//List<ModelMst> entireModelList = modelMstService.getModels();
						System.out.println("MODEL datatypeSheet.getLastRowNum() "+datatypeSheet.getLastRowNum());
						int i = 1;
						while (i <= datatypeSheet.getLastRowNum()) { //for saving model and assembly

							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet.getRow(i++);
							System.out.println("MODEL row "+row);
							if (row.getCell(0) != null) {
								String modelCode = formatter.formatCellValue(row.getCell(11));
								String assmCode = formatter.formatCellValue(row.getCell(12));
								String assmDesc = formatter.formatCellValue(row.getCell(16));
								double qty = Double.parseDouble(formatter.formatCellValue(row.getCell(17)));
								String uom = formatter.formatCellValue(row.getCell(18));
								String assmCat = formatter.formatCellValue(row.getCell(23));
								String materialStagePriority = formatter.formatCellValue(row.getCell(24));
								ModelMst modelCheck = modelMstService.getModelByCode(modelCode);
								
								System.out.println("MODEL CODE "+modelCode);
								if (modelCheck == null) {
									
									ModelMst theModel = new ModelMst();
									theModel.setModelCode(modelCode);
									modelCheck = modelMstService.addModel(theModel);
								}
								//System.out.println("Model :: "+modelCheck.getModelCode());

								AssemblyMst assemblyCheck = assemblyMstService.getassemblyByCode(assmCode, modelCode);
								if (assemblyCheck == null) {
									AssemblyMst theAssembly = new AssemblyMst();
									theAssembly.setAssemblyCode(assmCode);
									theAssembly.setAssemblyDesc(assmDesc);
									theAssembly.setAssemblyQty(qty);
									theAssembly.setModel(modelCheck);
									theAssembly.setUom(uom);
									theAssembly.setAssmCat(assmCat);
									theAssembly.setMaterialStagePriority(materialStagePriority);
									assemblyCheck = assemblyMstService.addAssembly(theAssembly);
								}
							//	System.out.println("Assembly :: "+assemblyCheck.getAssemblyCode());

							}
						}

						i = 1;
						while (i <= datatypeSheet2.getLastRowNum()) { //for saving component mapped to assembly

							XSSFRow row = null;
							row = (XSSFRow) datatypeSheet2.getRow(i++);				

							if (row.getCell(0) != null) {
								XSSFCellStyle cs = (XSSFCellStyle) row.getCell(14).getCellStyle();
								//if (cs.getFillForegroundColorColor() == null) {
									String serialNo = formatter.formatCellValue(row.getCell(0));
									String modelCode = formatter.formatCellValue(row.getCell(11));
									String assmCode = formatter.formatCellValue(row.getCell(12), evaluator);
									String compCode = formatter.formatCellValue(row.getCell(15));
									String compDesc = formatter.formatCellValue(row.getCell(16));
									double qty = Double.parseDouble(formatter.formatCellValue(row.getCell(17)));
									String uom = formatter.formatCellValue(row.getCell(18));
									
									ModelMst modelMst= assemblyMstService.getModelByCode(modelCode);
									
									
									AssemblyMst assemblyCheck = assemblyMstService.getassemblyByAssCode(assmCode, modelCode);
									ItemMst item = itemMstServices.findById(compCode);
									if(item ==null){
										ItemMst itemMst= new ItemMst();
										itemMst.setId(compCode);;
										itemMst.setItemDtl(compDesc);
										itemMstServices.saveItem(itemMst);
										
									}
									if(assemblyCheck != null) {		
										ComponentMst componentCheck = compMstService.getCompByCodes(assmCode, compCode, modelCode);

										if (componentCheck == null) {
											
										
											ComponentMst theComp = new ComponentMst();
											theComp.setAssembly(assemblyCheck);
											theComp.setCompQty(qty);
											theComp.setCompCode(compCode);
											theComp.setCompDesc(compDesc);
											theComp.setUom(uom);
											componentCheck = compMstService.addComp(theComp);
											System.out.println("Component Saved :: "+theComp.getCompCode()+"Line No ::"+serialNo);
										}else{
											System.out.println("Component Exits :: "+compCode+"Line No ::"+serialNo);

										}
										
										
										
										
										
									}
								}
							}
						//}
						
//						System.out.println(i);

						logger.info("Successfully imported");
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
	
	/* for uploading BOM */
	@GetMapping(API.modelList)
	public @ResponseBody ResponseEntity<List<ModelMst>> getModelList() {
		try {
			return new ResponseEntity(modelMstService.getModels(), HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
