package com.a2mee.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.a2mee.model.ItemLocMap;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.PickingAssembly;
import com.a2mee.model.PickingComponent;
import com.a2mee.model.PickingMst;
import com.a2mee.model.dto.ModelPoDto;
import com.a2mee.model.dto.PickStockDto;
import com.a2mee.model.dto.PickStockMultipleDto;
import com.a2mee.model.dto.PickingAssemblyDto;
import com.a2mee.model.dto.PickingCompDto;
import com.a2mee.model.dto.PickingComponentDto;
import com.a2mee.model.dto.PickingDto;
import com.a2mee.model.dto.StockDto;

public interface PickingService {

	void assignPicker(PickingMst pickingMst);

	double getTotalComplCount(String userName);

	List<PickingAssembly> getAssms(String userName);

	List<PickingComponent> getCompsByAssm(String userName, String assemblyCode);

	List<StockDto> getStockByComp(String compCode);

	void updateStocks(PickStockMultipleDto pickStockMultipleDto);

	List<PickingAssembly> getKitStock();

	List<PickingAssemblyDto> getAssmsByPOPicking(List<PickingDto> modelPos);

	List<PickingComponentDto> getCompsByAssmCodePO(List<PickingDto> modelPos, String assemblyCode);

	List<PickingMst> getPickingByPrdOrdNo(String proOrderNo);

	List<PickingAssembly> getPickingAssemblyByPicking(long pickingId);

	void updateAllPickingAssembly(List<PickingAssembly> pickingAssemblies);

	Set<PickingDto> getProOrderByModel(String modelCode);

	List<PickingAssemblyDto> getPickingAssemblyDtoByPicking(long pickingId);

	List<PickingComponentDto> getPickingComponentDtoByPickingAssm(long pickingAssmId);

	List<PickingCompDto> getPickingPOByComp(List<PickingDto> modelPos,String assemblyCode, String compCode);

	List<PickingComponentDto> getPickingComponentByPickingAndAssemblyAndComponent(long pickingId, String assemblyCode,
			String compCode);

	List<PickingComponent> getPickingComponentByPickingAssembly(long pickingAssmId);

	void updateAllPickingComponent(List<PickingComponent> pickingComponents);

	void updateApproval(long pickingId);

	List<String> modelsInPicking();

	List<PickingDto> POPickingByModel(String modelCode);

	void addPicking(PickingMst pickingMst);

	Optional<PickingComponent> getPickingComponentByModelPlanAndComponent(Long pickingId, String itemId ,double qty);

	void addPickingComponent(PickingComponent pickingComponent);

	List<PickingComponent> getPickingComponentsByModelPlanAndComponent(Long pickingId, String itemId);

	List<PickingComponent> getPickingComponentByItemDateNull(String itemMstId);

	List<PickingComponent> getPickingComponentByItemDateNullDateRange(String itemMstId, String startDate,
			String endDate);

	List<ItemLocMap> getItemLocationsByComponent(String compCode);

	List<PickingAssemblyDto> getAssmsByPOPickingByArr(long[] pickingids);

	List<PickingComponent> getPickingComponentsByModelPlanAndComponentAndAssemblyCode(Long pickingId, String itemId,
			String assmCode);

	List<PickingComponent> getPickingComponentsByModelPlanAndComponentAndQTY(Long pickingId, String itemId, double qty);



}
