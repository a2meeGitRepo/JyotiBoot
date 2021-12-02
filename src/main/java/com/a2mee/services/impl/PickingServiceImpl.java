package com.a2mee.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.a2mee.dao.ItemLocMapDao;
import com.a2mee.dao.MaterialInspectDao;
import com.a2mee.dao.MaterialTransactionRepo;
import com.a2mee.dao.MtlStockInDao;
import com.a2mee.dao.PickingAssemblyDao;
import com.a2mee.dao.PickingCompDao;
import com.a2mee.dao.PickingDao;
import com.a2mee.dao.ProductionOrderRepo;
import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.ItemLocMap;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MaterialTransaction;
import com.a2mee.model.ModelMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.PickingAssembly;
import com.a2mee.model.PickingComponent;
import com.a2mee.model.PickingMst;
import com.a2mee.model.ProductionOrder;
import com.a2mee.model.dto.DoubleValueDto;
import com.a2mee.model.dto.ModelPoDto;
import com.a2mee.model.dto.PickStockDto;
import com.a2mee.model.dto.PickStockMultipleDto;
import com.a2mee.model.dto.PickingAssemblyDto;
import com.a2mee.model.dto.PickingCompDto;
import com.a2mee.model.dto.PickingComponentDto;
import com.a2mee.model.dto.PickingDto;
import com.a2mee.model.dto.StockDto;
import com.a2mee.services.AssemblyService;
import com.a2mee.services.CompService;
import com.a2mee.services.MaterialInspectServices;
import com.a2mee.services.ModelService;
import com.a2mee.services.PickingService;

@Service
public class PickingServiceImpl implements PickingService {
	
	@Autowired
	PickingDao pickingDao;
	
	@Autowired
	PickingAssemblyDao pickingAssemblyDao;
	
	@Autowired
	PickingCompDao pickingCompDao;
	
	@Autowired
	MtlStockInDao mtlStockInDao;
	
	@Autowired
	AssemblyService assemblyService;
	
	@Autowired
	CompService compService;
	
	@Autowired
	ModelService modelService;
	
	@Autowired
	MaterialInspectServices materialInspectServices;
	
	@Autowired
	ProductionOrderRepo productionOrderRepo;
	@Autowired
	ItemLocMapDao itemLocMapDao;
	@Autowired
	MaterialTransactionRepo materialTransactionRepo;
	@Override
	public void assignPicker(PickingMst pickingMst) {
		
		pickingMst = pickingDao.save(pickingMst);
		ModelMst  modelMst=modelService.getModelByCode(pickingMst.getModelCode());
		List<AssemblyMst> assemblies = assemblyService.getAssemblyByModelId(modelMst.getModelId());
		//List<AssemblyMst> assemblies = assemblyService.getassemblyByCode(pickingMst.getModelPlan().getModelCode());
		ProductionOrder  productionOrder= productionOrderRepo.getPorductionOrder(Long.parseLong(pickingMst.getProOrdNo()));
		productionOrder.setIsApproved(1);
		productionOrderRepo.save(productionOrder);
		List<PickingAssembly> pickingAssemblies = new ArrayList<>();
		List<PickingComponent> pickingcomponents = new ArrayList<>();
		
		for(AssemblyMst assembly:assemblies) {
			PickingAssembly pickingAssembly = new PickingAssembly();
			pickingAssembly.setAssemblyMst(assembly);
			pickingAssembly.setPickingMst(pickingMst);
			pickingAssembly.setPickAssmQty(assembly.getAssemblyQty() * pickingMst.getPickQty());			
			//pickingAssemblies.add(pickingAssembly);
			pickingAssemblyDao.save(pickingAssembly);
			List<ComponentMst> components = compService.getComponentByAssemblyID(assembly.getAssmblyId());
			for(ComponentMst component:components) {
				
				
				
				
				PickingComponent pickingComponent = new PickingComponent();
				pickingComponent.setComponentMst(component);
				pickingComponent.setPickingAssembly(pickingAssembly);
				pickingComponent.setPickingMst(pickingMst);				
				pickingComponent.setPickCompQty(component.getCompQty() * pickingAssembly.getPickAssmQty());
				 pickingcomponents.add(pickingComponent);
				PickingComponent pc= pickingCompDao.save(pickingComponent);
				
				System.out.println("Component :: "+component.getCompCode()+"    For Assembly : "+pickingAssembly.getAssemblyMst().getAssemblyCode()+"Saved ID "+pc.getPickingCompId());

			}
		}
		
		//pickingAssemblies = pickingAssemblyDao.saveAll(pickingAssemblies);
		
		/*for(PickingAssembly pickingAssembly:pickingAssemblies) {
			
		}*/
		
		//pickingcomponents = pickingCompDao.saveAll(pickingcomponents);
		
		ModelPlan modelPlan = pickingMst.getModelPlan();
		modelPlan.setAssigned(1);
		modelPlan.setEmployee(pickingMst.getEmployee());
		modelPlan.setProOrdNo(pickingMst.getProOrdNo());
		modelService.addModelPlan(modelPlan);
	}

	@Override
	public double getTotalComplCount(String userName) {
		Calendar cal = Calendar.getInstance();
		String month = new SimpleDateFormat("MMM").format(cal.getTime());
		String year = "" + Calendar.getInstance().get(Calendar.YEAR);
		List<PickingAssembly> pickingAssemblies = pickingAssemblyDao.getAssemblyByPicker(userName, month, year);
		double count = 0;
		for(PickingAssembly pickingAssembly:pickingAssemblies) {
			count = count + pickingAssembly.getPickAssmQty();
		}
		return count;
	}

	@Override
	public List<PickingAssembly> getAssms(String userName) {
		/*Calendar cal = Calendar.getInstance();
		String month = new SimpleDateFormat("MMM").format(cal.getTime());
		String year = "" + Calendar.getInstance().get(Calendar.YEAR);
		return pickingAssemblyDao.getAssms(userName, month, year);*/
		return pickingAssemblyDao.getAssmsByUsername(userName);
	}

	@Override
	public List<PickingComponent> getCompsByAssm(String userName, String assemblyCode) {
		Calendar cal = Calendar.getInstance();
		String month = new SimpleDateFormat("MMM").format(cal.getTime());
		String year = "" + Calendar.getInstance().get(Calendar.YEAR);
		return pickingCompDao.getCompsByAssm(userName, month, year, assemblyCode);
	}

	@Override
	public List<StockDto> getStockByComp(String compCode) {
		return materialInspectServices.getStockByComp(compCode);
	}
	
	//Old Code for update Picking Stock
	/*@Override
	public void updateStocks(PickStockDto pickStockDto) {
		
		System.out.println("Pick Stock:"+pickStockDto.toString());
		double pickedQty = pickStockDto.getPickedQty();
	//	DoubleValueDto doubleValueDto = new DoubleValueDto();

			List<PickingComponent> pickingComponents = pickingCompDao.getPickingByCompId(pickStockDto.getPickingCompId());
			for (PickingComponent pickingComponent : pickingComponents) {
				if(pickedQty!=0) {
					if(pickingComponent.getStatus()==0) {
						if(pickedQty == pickingComponent.getPickCompQty()) {
							pickingComponent.setPickedQty(pickedQty);
							System.out.println("Picked Qty in Greater Than:"+pickedQty);
						
							
						//	pickedQty = (double)(pickedQty - pickingComponent.getPickCompQty());
							pickingComponent.setStorageBinCode(pickStockDto.getStorageBinCode());
							pickingComponent.setPickedDate(new Date());
							pickingComponent.setStatus(1);
							System.out.println("Component in If:"+pickingComponent.toString());
							pickingCompDao.save(pickingComponent);
						}
						if(pickedQty < pickingComponent.getPickCompQty())	{
							System.out.println("Picked Qty in Less than:"+pickedQty);

						///	double pickedQtyInit = pickedQty;
							pickingComponent.setPickedQty(pickedQty);
							pickingComponent.setStorageBinCode(pickStockDto.getStorageBinCode());
							pickingComponent.setPickedDate(new Date());
							pickingComponent.setStatus(2);
							System.out.println("Component Picked Qty:"+pickingComponent.toString());
							System.out.println("Picked Qty:"+pickingComponent.getPickedQty());
							PickingComponent pickingComponentObj = pickingCompDao.save(pickingComponent);
							System.out.println("Response:"+pickingComponentObj);
							
							System.out.println("Component After Save:"+pickingComponent.toString());
							pickedQty = 0.0;
						}
						
					}
					if(pickingComponent.getStatus()==2) {
						if(pickedQty==(pickingComponent.getPickCompQty()-pickingComponent.getPickedQty())) {
							pickingComponent.setPickedQty(pickingComponent.getPickCompQty());
							
							pickingComponent.setStorageBinCode(pickStockDto.getStorageBinCode());
							pickingComponent.setPickedDate(new Date());
							pickingComponent.setStatus(1);
							pickingCompDao.save(pickingComponent);
						}else {
							pickingComponent.setPickedQty(pickingComponent.getPickedQty()+pickedQty);
							
							pickingComponent.setStorageBinCode(pickStockDto.getStorageBinCode());
							pickingComponent.setPickedDate(new Date());
							pickingComponent.setStatus(2);
							pickingCompDao.save(pickingComponent);
						}
					}
					PickingAssembly pickAssembly = pickingComponent.getPickingAssembly();
					List<PickingComponent> unCompletedComponents = pickingCompDao.getCompsByAssmAndStatus(pickAssembly);		
					if(unCompletedComponents.size()==0) {
						pickAssembly.setStatus(1);
						pickAssembly.setRemQty(pickAssembly.getPickAssmQty());
					}else {
						pickAssembly.setStatus(2);
						pickAssembly.setRemQty(pickAssembly.getPickAssmQty());
					}		
					pickingAssemblyDao.save(pickAssembly);
					
					//Production Order Status
					
					PickingMst pickingMst = pickingComponent.getPickingMst();
					List<PickingAssembly> unCompleteAssembly = pickingAssemblyDao.getAssemblyByPickingAndStatus(pickingMst);
					if(unCompleteAssembly.size()==0) {
						pickingMst.setStatus(1);
					}else {
						pickingMst.setStatus(2);
					}
					pickingDao.save(pickingMst);
					
				}
				/*else {
					pickingComponent.setStatus(0);
					pickingComponent.setStorageBinCode(pickStockDto.getStorageBinCode());
					pickingComponent.setPickedDate(new Date());
					pickingCompDao.save(pickingComponent);
					
				}
			}

		
		//PickingComponent pickComponent = pickingCompDao.findById(pickStockDto.getCompCode()).get();	
		/*PickingComponent pickComponent = new PickingComponent();
		pickComponent.setPickedQty(pickComponent.getPickedQty() + pickStockDto.getPickedQty());		
		if(pickComponent.getPickedQty() == pickComponent.getPickCompQty()) {
			pickComponent.setStatus(1);
		}else if(pickComponent.getPickedQty() == 0) {
			pickComponent.setStatus(0);
		}else {
			pickComponent.setStatus(2);
		}		
		pickComponent.setStorageBinCode(pickStockDto.getStorageBinCode());
		pickComponent.setPickedDate(new Date());
		pickingCompDao.save(pickComponent);	
		
		
		MtlStockIn materialStock = materialInspectServices.getStockById(pickStockDto.getStockInId());
		materialStock.setRemainQty(materialStock.getRemainQty() - pickStockDto.getPickedQty());
		materialInspectServices.updateStock(materialStock.getId(), materialStock.getRemainQty());
	} */
	
	@Override
	public void updateStocks(PickStockMultipleDto pickStockMultipleDto) {
		MaterialTransaction materialTransaction= new MaterialTransaction();
		ComponentMst  componentMst= compService.getComponentByComCode(pickStockMultipleDto.getPickStockDtos().get(0).getCompCode());
		materialTransaction.setItemId(pickStockMultipleDto.getPickStockDtos().get(0).getCompCode());
		materialTransaction.setItemDetial(componentMst.getCompDesc());
		materialTransaction.setQuntity(pickStockMultipleDto.getTotalQty());
		materialTransaction.setTranaction_date(new Date());
		materialTransaction.setTranactionType("OUT");
		materialTransactionRepo.save(materialTransaction);
		
		
		
		System.out.println("Pick Stock:"+pickStockMultipleDto.toString());
		double totalQty = pickStockMultipleDto.getTotalQty();
	//	double pickedQty = pickStockMultipleDto.getTotalQty();
	//	DoubleValueDto doubleValueDto = new DoubleValueDto();
		List<PickStockDto> pickStockDtos = pickStockMultipleDto.getPickStockDtos();
		for (PickStockDto pickStockDto : pickStockDtos) {
			System.out.println("Picking Com ID:"+pickStockDto.getPickingCompId());
			//List<PickingComponent> pickingComponents = pickingCompDao.getPickingComponentByPickingAndAssemblyAndComponent(pickStockDto.getPickingId(), pickStockDto.getAssemblyCode(), pickStockDto.getCompCode());
			List<PickingComponent> pickingComponents = pickingCompDao.getPickingByCompId(pickStockDto.getPickingCompId());
			System.out.println("Picking Com Size :"+pickingComponents.size());
			
			for (PickingComponent pickingComponent : pickingComponents) {
				System.out.println("Picking Com :"+pickingComponent.toString());
				if(totalQty >= pickingComponent.getPickCompQty()) {
					if(pickingComponent.getStatus()==0) {
						pickingComponent.setPickedQty(pickingComponent.getPickCompQty());
						//totalQty= totalQty-pickingComponent.getPickCompQty();
						try {
							//	pickedQty = (double)(pickedQty - pickingComponent.getPickCompQty());
								pickingComponent.setStorageBinCode(pickStockMultipleDto.getStorageBinCode());
								pickingComponent.setPickedDate(new Date());
								pickingComponent.setStatus(1);
								System.out.println("Component in If:"+pickingComponent.toString());
								pickingCompDao.save(pickingComponent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
//					if(pickingComponent.getStatus()==2) {
//						pickingComponent.setPickedQty(pickingComponent.getPickCompQty());
//						totalQty= totalQty-(pickingComponent.getPickedQty()+pickingComponent.getPickCompQty());
//						try {
//							//	pickedQty = (double)(pickedQty - pickingComponent.getPickCompQty());
//								pickingComponent.setStorageBinCode(pickStockMultipleDto.getStorageBinCode());
//								pickingComponent.setPickedDate(new Date());
//								pickingComponent.setStatus(1);
//								System.out.println("Component in If:"+pickingComponent.toString());
//								pickingCompDao.save(pickingComponent);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
					
				}
				
				try {
					PickingAssembly pickAssembly = pickingComponent.getPickingAssembly();
					List<PickingComponent> unCompletedComponents = pickingCompDao.getCompsByAssmAndStatus(pickAssembly);		
					if(unCompletedComponents.size()==0) {
						pickAssembly.setStatus(1);
						pickAssembly.setRemQty(pickAssembly.getPickAssmQty());
					}else {
						pickAssembly.setStatus(2);
						pickAssembly.setRemQty(pickAssembly.getPickAssmQty());
					}		
					pickingAssemblyDao.save(pickAssembly);
					
					//Production Order Status
					
					PickingMst pickingMst = pickingComponent.getPickingMst();
					List<PickingAssembly> unCompleteAssembly = pickingAssemblyDao.getAssemblyByPickingAndStatus(pickingMst);
					if(unCompleteAssembly.size()==0) {
						pickingMst.setStatus(1);
					}else {
						pickingMst.setStatus(2);
					}
					pickingDao.save(pickingMst);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
						
		}
		
		
		MtlStockIn materialStock = materialInspectServices.getStockById(pickStockMultipleDto.getStockInId());
		materialStock.setRemainQty(materialStock.getRemainQty() - pickStockMultipleDto.getTotalQty());
		materialInspectServices.updateStock(materialStock.getId(), materialStock.getRemainQty());
	}

	@Override
	public List<PickingAssembly> getKitStock() {
		return pickingAssemblyDao.getKitStock();
	}
	@Override
	public List<PickingAssemblyDto> getAssmsByPOPickingByArr(long[] pckingids) {
		List<PickingAssembly> pickingAssembliesOneByArray = pickingAssemblyDao.getPickingByModelPoByArray(pckingids);

		System.out.println("RES SIZE :: "+pickingAssembliesOneByArray.size());

		
		
		return null;
		
	}
	@Override
	public List<PickingAssemblyDto> getAssmsByPOPicking(List<PickingDto> modelPos) {
		
		List<PickingAssemblyDto> pickingAssemblyDtos = new ArrayList<PickingAssemblyDto>();
		
		
		for(PickingDto pickingDto:modelPos ){
			List<PickingAssembly> pickingAssembliesOne = pickingAssemblyDao.getPickingByModelPo(pickingDto.getPickingId(),pickingDto.getModelCode(),pickingDto.getProOrdNo());
			for (PickingAssembly pickingAssembly : pickingAssembliesOne) {
				//List<PickingComponent> components = pickingCompDao.getPickingByAssmPo(pickingDto.getPickingId(),pickingDto.getModelCode(),pickingDto.getProOrdNo(),pickingAssembly.getAssemblyMst().getAssemblyCode());
//				if(components.size()>0){
					PickingAssemblyDto assemblyDto = new PickingAssemblyDto();
					if (pickingAssemblyDtos.stream().anyMatch(pickingAssemblyDto -> pickingAssemblyDto.getAssemblyCode() == pickingAssembly.getAssemblyMst().getAssemblyCode())) {
						pickingAssemblyDtos.stream().filter(pickingAssemblyDto -> pickingAssemblyDto.getAssemblyCode().equals(pickingAssembly.getAssemblyMst().getAssemblyCode())).forEach(pickingAssemblyDto -> pickingAssemblyDto.setPickAssmQty(pickingAssemblyDto.getPickAssmQty()+pickingAssembly.getPickAssmQty()));
						
					} else {
					    // does not contain the id
						assemblyDto.setAssemblyCode(pickingAssembly.getAssemblyMst().getAssemblyCode());
						assemblyDto.setAssemblyDesc(pickingAssembly.getAssemblyMst().getAssemblyDesc());
						assemblyDto.setPickAssmQty(pickingAssembly.getPickAssmQty());
						if(pickingAssembly.getStatus()!=0) {
							assemblyDto.setStatus(pickingAssembly.getStatus());
						}
						pickingAssemblyDtos.add(assemblyDto);
					}
				//}
			}
			
		}
		/*
		for (PickingDto modelPo : modelPos) {
			List<PickingAssembly> pickingAssembliesOne = pickingAssemblyDao.getPickingByModelPo(modelPo.getPickingId(),modelPo.getModelCode(),modelPo.getProOrdNo());
			System.out.println("Picking ASS "+pickingAssembliesOne.size()+"  PICKING ID   :: "+modelPo.getPickingId());
			
			for (PickingAssembly pickingAssembly : pickingAssembliesOne) {
				List<PickingComponent> components = pickingCompDao.getPickingComponentByPickingAssebly(modelPo.getPickingId(),);
				//List<PickingComponent> components = pickingCompDao.getPickingByAssmPo(modelPo.getPickingId(),modelPo.getModelCode(),modelPo.getProOrdNo(),pickingAssembly.getAssemblyMst().getAssemblyCode());
				
				System.out.println("Picking ID ::"+modelPo.getPickingId()+" Model Code :: "+modelPo.getModelCode()+" Prod No :: "+modelPo.getProOrdNo()+" Assembly :: "+pickingAssembly.getAssemblyMst().getAssemblyCode()+" SIZE  :: "+components.size());
				if(components.size()>0){
					PickingAssemblyDto assemblyDto = new PickingAssemblyDto();
					if (pickingAssemblyDtos.stream().anyMatch(pickingAssemblyDto -> pickingAssemblyDto.getAssemblyCode() == pickingAssembly.getAssemblyMst().getAssemblyCode())) {
						pickingAssemblyDtos.stream().filter(pickingAssemblyDto -> pickingAssemblyDto.getAssemblyCode().equals(pickingAssembly.getAssemblyMst().getAssemblyCode())).forEach(pickingAssemblyDto -> pickingAssemblyDto.setPickAssmQty(pickingAssemblyDto.getPickAssmQty()+pickingAssembly.getPickAssmQty()));
						
					} else {
					    // does not contain the id
						assemblyDto.setAssemblyCode(pickingAssembly.getAssemblyMst().getAssemblyCode());
						assemblyDto.setAssemblyDesc(pickingAssembly.getAssemblyMst().getAssemblyDesc());
						assemblyDto.setPickAssmQty(pickingAssembly.getPickAssmQty());
						if(pickingAssembly.getStatus()!=0) {
							assemblyDto.setStatus(pickingAssembly.getStatus());
						}
						pickingAssemblyDtos.add(assemblyDto);
					}
				}
				
				
			}
		}
		
*/
		return pickingAssemblyDtos;
	}

	@Override
	public List<PickingComponentDto> getCompsByAssmCodePO(List<PickingDto> modelPos, String assemblyCode) {
		// TODO AutcomponentDtoo-generated method stub
		List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
		for (PickingDto modelPo : modelPos) {
			List<PickingComponent> pickingComponents = pickingCompDao.getPickingByAssmPo(modelPo.getPickingId(),modelPo.getModelCode(),modelPo.getProOrdNo(),assemblyCode);
			System.out.println("SIZ ID "+pickingComponents.size());
			
			/*	System.out.println("PCKINg ID "+modelPo.getPickingId());
			System.out.println("MODEL CODE ID "+modelPo.getModelCode());
			System.out.println("PCKINg ID "+modelPo.getProOrdNo());
			System.out.println("PCKINg ID "+assemblyCode);
*/
			
			
			//System.out.println("Picking Id:: "+modelPo.getPickingId()+" Model Code ::  "+modelPo.getModelCode()+"  PROD NO :: "+modelPo.getProOrdNo()+" Assembly "+assemblyCode+"  SIZE :: "+pickingComponents.size());
			for (PickingComponent pickingComponent : pickingComponents) {
				
			//	System.out.println("PichCompoNant ID :: "+pickingComponent.getPickingCompId()+"    Item NO : "+pickingComponent.getItemNo()+" ITEM BIT :: "+pickingComponent.getItemBit());
				
			//	System.out.println("Comp Code:"+pickingComponent.getComponentMst().getCompCode());
				PickingComponentDto pickingComponentDto = new PickingComponentDto();
				if(pickingComponentDtos.stream().anyMatch(componentDto -> componentDto.getCompCode() == pickingComponent.getComponentMst().getCompCode())) {
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode())).forEach(componentDto -> componentDto.setPickCompQty(componentDto.getPickCompQty()+pickingComponent.getPickCompQty()));
					pickingComponentDtos.stream().filter(componentDto -> componentDto.getCompCode().equals(pickingComponent.getComponentMst().getCompCode())).forEach(componentDto -> componentDto.setPickedQty(componentDto.getPickedQty()+pickingComponent.getPickedQty()));
				System.out.println("IF FOR "+pickingComponent.getComponentMst().getCompCode());
				
				}else {
					System.out.println("ELSE FOR "+pickingComponent.getComponentMst().getCompCode());

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
			}
		}
		pickingComponentDtos.stream().filter(componentDto -> componentDto.getPickCompQty()==componentDto.getPickedQty()).forEach(componentDto -> componentDto.setStatus(1));
		
		pickingComponentDtos.stream().filter(componentDto -> componentDto.getPickCompQty()>componentDto.getPickedQty()).filter(componentDto -> componentDto.getPickedQty() != 0).forEach(componentDto -> componentDto.setStatus(2));
		
		
		return pickingComponentDtos;
	}

	@Override
	public List<PickingMst> getPickingByPrdOrdNo(String proOrderNo) {
		// TODO Auto-generated method stub
		return pickingDao.getPickingByPrdOrdNo(proOrderNo);
	}

	@Override
	public List<PickingAssembly> getPickingAssemblyByPicking(long pickingId) {
		// TODO Auto-generated method stub
		return pickingAssemblyDao.getByPickingId(pickingId);
	}

	@Override
	public void updateAllPickingAssembly(List<PickingAssembly> pickingAssemblies) {
		// TODO Auto-generated method stub
		pickingAssemblyDao.saveAll(pickingAssemblies);
	}

	@Override
	public Set<PickingDto> getProOrderByModel(String modelCode) {
		// TODO Auto-generated method stub
		Set<PickingDto> pickingDtoList = new HashSet<PickingDto>();
		List<PickingMst> pickingList = pickingDao.getPickingByModel(modelCode);

		for (PickingMst pickingMst : pickingList) {
		//	System.out.println("Pxicking list :: "+pickingMst.getProOrdNo());
			PickingDto pickingDto = new PickingDto();
			pickingDto.setModelCode(pickingMst.getModelCode());
			pickingDto.setPickingId(pickingMst.getPickingId());
			pickingDto.setProOrdNo(pickingMst.getProOrdNo());
			pickingDto.setPickQty(pickingMst.getPickQty());
			pickingDto.setStatus(pickingMst.getStatus());
			pickingDtoList.add(pickingDto);
		}
		
		return pickingDtoList;
	}

	@Override
	public List<PickingAssemblyDto> getPickingAssemblyDtoByPicking(long pickingId) {
		// TODO Auto-generated method stub
		List<PickingAssemblyDto> pickigAssemblyDtos = new ArrayList<PickingAssemblyDto>();
		
		List<PickingAssembly> pickingAssemblies = pickingAssemblyDao.getByPickingId(pickingId);
		
		for (PickingAssembly pickingAssembly : pickingAssemblies) {
			PickingAssemblyDto pickingAssemblyDto = new PickingAssemblyDto();
			pickingAssemblyDto.setPickingAssmId(pickingAssembly.getPickingAssmId());
			pickingAssemblyDto.setAssemblyCode(pickingAssembly.getAssemblyMst().getAssemblyCode());
			pickingAssemblyDto.setAssemblyDesc(pickingAssembly.getAssemblyMst().getAssemblyDesc());
			pickingAssemblyDto.setPickAssmQty(pickingAssembly.getPickAssmQty());
			pickingAssemblyDto.setStatus(pickingAssembly.getStatus());
			pickigAssemblyDtos.add(pickingAssemblyDto);
		}
		
		return pickigAssemblyDtos;
	}

	@Override
	public List<PickingComponentDto> getPickingComponentDtoByPickingAssm(long pickingAssmId) {
		// TODO Auto-generated method stub
		List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
		
		List<PickingComponent> pickingComponents = pickingCompDao.getPickingComponentByPickingAssmAndStatus(pickingAssmId);
		
		for (PickingComponent pickingComponent : pickingComponents) {
			PickingComponentDto pickingComponentDto = new PickingComponentDto();
			pickingComponentDto.setPickingCompId(pickingComponent.getPickingCompId());
			pickingComponentDto.setCompCode(pickingComponent.getComponentMst().getCompCode());
			pickingComponentDto.setCompDesc(pickingComponent.getComponentMst().getCompDesc());
			pickingComponentDto.setPickCompQty(pickingComponent.getPickCompQty());
			pickingComponentDto.setPickedQty(pickingComponent.getPickedQty());
			
			double availableQty =0;
			List<MtlStockIn> mtlStockInList = mtlStockInDao.getAvailableStockByComp(pickingComponent.getComponentMst().getCompCode());
			if(mtlStockInList.size()>0) {
				availableQty = mtlStockInList.get(0).getRemainQty();
				pickingComponentDto.setAvailableQty(availableQty);
			}else {
				pickingComponentDto.setAvailableQty(0);
			}
			
			pickingComponentDto.setAssemblyCode(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyCode());
			pickingComponentDtos.add(pickingComponentDto);
		}
		
		return pickingComponentDtos;
	}

	@Override
	public List<PickingCompDto> getPickingPOByComp(List<PickingDto> modelPos,String assemblyCode, String compCode) {
		// TODO Auto-generated method stub
		List<PickingCompDto> pickingDtoList = new ArrayList<PickingCompDto>();
		//List<PickingMst> pickingList = pickingDao.getPickingByAssemblyAndComp(assemblyCode,compCode);
		List<Object[]> pickingList = pickingDao.getPickingByAssemblyAndComp(assemblyCode,compCode);
		/*for (PickingMst pickingMst : pickingList) {
			if(modelPos.stream().anyMatch(pickingDto -> pickingDto.getPickingId() == pickingMst.getPickingId())) {
				PickingDto pickingDto = new PickingDto();
				pickingDto.setModelCode(pickingMst.getModelCode());
				pickingDto.setPickingId(pickingMst.getPickingId());
				pickingDto.setProOrdNo(pickingMst.getProOrdNo());
				pickingDto.setPickQty(pickingMst.getPickQty());
				pickingDto.setStatus(pickingMst.getStatus());
				pickingDtoList.add(pickingDto);
			}
			
		}*/
		
			pickingList.forEach(arr -> {
				System.out.println("Production Ord No:"+(String)arr[3]);
				if(modelPos.stream().anyMatch(pickingDto -> pickingDto.getProOrdNo().equals((String)arr[3]))) {
					PickingCompDto pickingDto = new PickingCompDto();
					pickingDto.setModelCode((String)arr[2]);
					pickingDto.setPickingCompId((long)arr[0]);
					pickingDto.setProOrdNo((String)arr[3]);
					pickingDto.setPickCompQty((double)arr[1]);
					pickingDto.setStatus((int)arr[4]);
					pickingDtoList.add(pickingDto);
				}
			});	
		
		return pickingDtoList;
	}

	@Override
	public List<PickingComponentDto> getPickingComponentByPickingAndAssemblyAndComponent(long pickingId,
			String assemblyCode, String compCode) {
		// TODO Auto-generated method stub
		List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
		List<PickingComponent> pickingComponents= new ArrayList<PickingComponent>();
		try {
			pickingComponents = pickingCompDao.getPickingComponentByPickingAndAssemblyAndComponent(pickingId,assemblyCode,compCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (PickingComponent pickingComponent : pickingComponents) {
			PickingComponentDto pickingComponentDto = new PickingComponentDto();
			pickingComponentDto.setPickingCompId(pickingComponent.getPickingCompId());
			pickingComponentDto.setCompCode(pickingComponent.getComponentMst().getCompCode());
			pickingComponentDto.setCompDesc(pickingComponent.getComponentMst().getCompDesc());
			pickingComponentDto.setAssemblyCode(pickingComponent.getPickingAssembly().getAssemblyMst().getAssemblyCode());
			pickingComponentDto.setPickCompQty(pickingComponent.getPickCompQty());
			pickingComponentDto.setStatus(pickingComponent.getStatus());
			pickingComponentDtos.add(pickingComponentDto);
		}
		
		return pickingComponentDtos;
	}

	@Override
	public List<PickingComponent> getPickingComponentByPickingAssembly(long pickingAssmId) {
		// TODO Auto-generated method stub
//		List<PickingComponentDto> pickingComponentDtos = new ArrayList<PickingComponentDto>();
		List<PickingComponent> pickingComponents= pickingCompDao.getPickingComponentByPickingAssembly(pickingAssmId);
		
		
		return pickingComponents;
	}

	@Override
	public void updateAllPickingComponent(List<PickingComponent> pickingComponents) {
		// TODO Auto-generated method stub
		pickingCompDao.saveAll(pickingComponents);
	}

	@Override
	public void updateApproval(long pickingId) {
		// TODO Auto-generated method stub
		pickingDao.updateApproval(pickingId);
	}

	@Override
	public List<String> modelsInPicking() {
		// TODO Auto-generated method stub
		return pickingDao.getModelsInPicking();
	}

	@Override
	public List<PickingDto> POPickingByModel(String modelCode) {
		// TODO Auto-generated method stub
		List<PickingDto> pickingDtos = new ArrayList<PickingDto>();
		List<PickingMst> pickingMsts = pickingDao.getPOPickingByModel(modelCode);
		for (PickingMst pickingMst : pickingMsts) {
			PickingDto pickingDto = new PickingDto();
			pickingDto.setModelCode(pickingMst.getModelCode());
			pickingDto.setPickingId(pickingMst.getPickingId());
			pickingDto.setPickQty(pickingMst.getPickQty());
			pickingDto.setProOrdNo(pickingMst.getProOrdNo());
			pickingDto.setReservationNo(pickingMst.getReservationNo());
			pickingDto.setStatus(pickingMst.getStatus());
			pickingDtos.add(pickingDto);
		}
		
		return pickingDtos;
	}

	@Override
	public void addPicking(PickingMst pickingMst) {
		// TODO Auto-generated method stub
		pickingDao.save(pickingMst);
	}

	@Override
	public Optional<PickingComponent> getPickingComponentByModelPlanAndComponent(Long pickingId, String itemId ,double qty) {
		// TODO Auto-generated method stub
		return pickingCompDao.getPickingComponentByModelPlanAndComponent(pickingId, itemId,qty);
	}

	@Override
	public void addPickingComponent(PickingComponent pickingComponent) {
		// TODO Auto-generated method stub
		pickingCompDao.save(pickingComponent);
	}

	@Override
	public List<PickingComponent> getPickingComponentsByModelPlanAndComponent(Long pickingId, String itemId) {
		// TODO Auto-generated method stub
		return pickingCompDao.getPickingComponentsByModelPlanAndComponent(pickingId, itemId);
	}

	@Override
	public List<PickingComponent> getPickingComponentByItemDateNull(String itemMstId) {
		// TODO Auto-generated method stub
		return pickingCompDao.getPickingComponentByItemDateNull(itemMstId);
	}

	@Override
	public List<PickingComponent> getPickingComponentByItemDateNullDateRange(String itemMstId, String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return pickingCompDao.getPickingComponentByItemDateNullDateRange(itemMstId,startDate,endDate);
	}

	@Override
	public List<ItemLocMap> getItemLocationsByComponent(String compCode) {
		// TODO Auto-generated method stub
		return itemLocMapDao.getAssingStorageLocByItem(compCode);
	}

	@Override
	public List<PickingComponent> getPickingComponentsByModelPlanAndComponentAndAssemblyCode(Long pickingId,
			String itemId, String assmCode) {
		// TODO Auto-generated method stub
		return pickingCompDao.getPickingComponentsByModelPlanAndComponentAndAssemblyCode(pickingId,itemId,assmCode);
	}

	@Override
	public List<PickingComponent> getPickingComponentsByModelPlanAndComponentAndQTY(Long pickingId, String itemId,
			double qty) {
		// TODO Auto-generated method stub
		return pickingCompDao.getPickingComponentsByModelPlanAndComponentAndQTY(pickingId,itemId,qty);
	}


	
}
