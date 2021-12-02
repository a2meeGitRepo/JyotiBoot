package com.a2mee.services;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.a2mee.model.ComponentMst;
import com.a2mee.model.ModelMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.PickingComponent;
import com.a2mee.model.dto.ModelPoDto;

public interface ModelService {

	List<ModelMst> getModels();

	ModelMst addModel(ModelMst model);

	ModelMst getModelByCode(String modelCode);
	
	List<ModelPlan> getModelPlanByFields(String modelCode, String month, String year);

	ModelPlan addModelPlan(ModelPlan theModelPlan);
	
	List<ModelPlan> getModelByMonth(String month, String year);

	List<ModelPlan> getModelPlan();

	List<ModelPlan> getModelPlansByFields(String modelCode, String month, String year);

	void addModelPlans(List<ModelPlan> modelPlans);

	void updateModelPlan(ModelPlan modelPlan);

	List<ModelPoDto> getProOrdersByModel(String modelCode);

	ModelPlan getModelPlanByProOrdNo(String proOrdNo);

	List<PickingComponent> getPickingComponantByComponentAndPO(ComponentMst componentMst, String proOrdNo);

}
