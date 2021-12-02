/*package com.a2mee.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.dao.MtlStockInDao;
import com.a2mee.dao.PickingCompDao;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.PickingComponent;
import com.a2mee.model.dto.ComponentRequestDto;
import com.a2mee.model.dto.PickingComponentDto;
import com.a2mee.model.dto.PickingComponentDtoObj;
import com.a2mee.services.AssemblyService;
import com.a2mee.services.CompService;
import com.a2mee.services.ModelService;
import com.a2mee.services.PickingService;
import com.a2mee.util.API;
import com.a2mee.util.ComponentRequestTestDto;

@RestController
@RequestMapping(API.test)
@CrossOrigin("*")
public class Test {
	
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
	
	
	
	@PostMapping(API.testAPISingle)
	public @ResponseBody PickingComponentDto getDatByPicIDAndAsseblyCodeSingle(@RequestBody ComponentRequestTestDto componentRequestDto) {
	
		List<PickingComponentDto> repickingComponents = new ArrayList<PickingComponentDto>();

			List<PickingComponent> pickingComponents = pickingCompDao.getPickingComByCompAndPickId(componentRequestDto.getPickingIds(),componentRequestDto.getComCode());
			List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
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
					System.out.println("Location For Compo :"+pickingComponentDto.getCompCode()+" is :: "+locMap.getStorageBinMst().getStorageBinCode());
			
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
	
			}
			return repickingComponents.get(0);
					
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping(API.testAPI)
	public @ResponseBody List<PickingComponentDto>  getDatByPicIDAndAsseblyCode(@RequestBody ComponentRequestTestDto componentRequestDto) {
	
		
		List<PickingComponent> pickingComponents = pickingCompDao.getPickingComByAssmPoAndPickId(componentRequestDto.getPickingIds(),componentRequestDto.getAssemblyCodes());
		List<PickingComponentDto> repickingComponents = new ArrayList<PickingComponentDto>();

					List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
					PickingComponentDtoObj componentDtoObj= new  PickingComponentDtoObj();
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
								pickingComponentDto.setAvailableQty(availableQty);
							}else {
								pickingComponentDto.setAvailableQty(0);
							}
				
							pickingComponentDtos.add(pickingComponentDto);
							}
						List<ItemLocMap> locations=pickingService.getItemLocationsByComponent(pickingComponentDto.getCompCode());
					
							PickingComponentDto componentDto2= new PickingComponentDto();
							componentDto2.setAssemblyCode(pickingComponentDto.getAssemblyCode());
							componentDto2.setAssemblyDesc(pickingComponentDto.getAssemblyDesc());
							componentDto2.setCompCode(pickingComponentDto.getCompCode());
							componentDto2.setCompDesc(pickingComponentDto.getCompDesc());
							componentDto2.setPickCompQty(pickingComponentDto.getPickCompQty()-pickingComponentDto.getPickedQty());
							
							componentDto2.setStatus(pickingComponentDto.getStatus());
							componentDto2.setLocation(componentRequestDto.getLocation());
						
							List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByCompAndBinCode(pickingComponentDto.getCompCode(),componentRequestDto.getLocation());
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
					//componentDtoObj.setComponents(repickingComponents);
					return repickingComponents;
		
	}
	
	
	
	
	
	
	@PostMapping(API.getLocationWiseCompsByAssmCodePO)
	public @ResponseBody ResponseEntity<PickingComponentDtoObj> getLocationWiseCompsByAssmCodePO(@RequestBody ComponentRequestDto componentRequestDto) {
		try {
			PickingComponentDtoObj componentDtoObj= new  PickingComponentDtoObj();
			List<PickingComponentDto> repickingComponents = new ArrayList<PickingComponentDto>();
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
			

			componentDtoObj.setComponents(repickingComponents);
		
			return new ResponseEntity<>(componentDtoObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
*/