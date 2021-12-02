package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.ModelMstDao;
import com.a2mee.dao.ModelPlanDao;
import com.a2mee.dao.PickingCompDao;
import com.a2mee.dao.PickingDao;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.ModelMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.PickingComponent;
import com.a2mee.model.PickingMst;
import com.a2mee.model.dto.ModelPoDto;
import com.a2mee.services.ModelService;

@Service
@Transactional
public class ModelServiceImpl implements ModelService {
	
	@Autowired
	ModelMstDao modelMstDao;
	
	@Autowired
	ModelPlanDao modelPlanDao;
	
	@Autowired
	PickingDao pickingDao;
	@Autowired
	PickingCompDao pickingCompDao;

	@Override
	public List<ModelMst> getModels() {
		return modelMstDao.findAll();
	}

	@Override
	public ModelMst addModel(ModelMst model) {
		return modelMstDao.save(model);
	}

	@Override
	public ModelMst getModelByCode(String modelCode) {
		Optional<ModelMst> model = modelMstDao.findByModelCode(modelCode);
		return model.isPresent()?model.get():null;
	}

	@Override
	public List<ModelPlan> getModelPlanByFields(String modelCode, String month, String year) {
		Optional<List<ModelPlan>> theModelPlans = modelPlanDao.getModelPlanByFields(modelCode, month, year);
		return theModelPlans.isPresent()? theModelPlans.get() : null;
	}

	@Override
	public ModelPlan addModelPlan(ModelPlan theModelPlan) {
		return modelPlanDao.save(theModelPlan);
	}

	@Override
	public List<ModelPlan> getModelByMonth(String month, String year) {
		return modelPlanDao.getModelByMonth(month,year);
	}

	@Override
	public List<ModelPlan> getModelPlan() {
		return modelPlanDao.findAll();
	}

	@Override
	public List<ModelPlan> getModelPlansByFields(String modelCode, String month, String year) {
		List<ModelPlan> theModelPlan = modelPlanDao.getModelPlansByFields(modelCode, month, year);
		return theModelPlan;
	}

	@Override
	public void addModelPlans(List<ModelPlan> modelPlans) {
		// TODO Auto-generated method stub
		modelPlanDao.saveAll(modelPlans);
	}

	@Override
	public void updateModelPlan(ModelPlan modelPlan) {
		// TODO Auto-generated method stub
		System.out.println("Model Id:"+modelPlan.getModelPlanId()+" Employee Id:"+modelPlan.getEmployee().getEmpId()+" Prod Ord:"+modelPlan.getProOrdNo()); 
		modelPlanDao.updateModelPlan(modelPlan.getModelPlanId(),modelPlan.getProOrdNo(),modelPlan.getEmployee().getEmpId());
	}

	@Override
	public List<ModelPoDto> getProOrdersByModel(String modelCode) {
		// TODO Auto-generated method stub
		System.out.println("Model Code in Service:"+modelCode);
		List<ModelPoDto> prodOrds = new ArrayList<ModelPoDto>();
		//List<ModelPlan> list = new ArrayList<ModelPlan>();
		List<PickingMst> list = new ArrayList<PickingMst>();
		/*try {
			list = modelPlanDao.getProOrdersByModel(modelCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("LIst:"+list.toString());
		for(ModelPlan modelPlan : list) {
			ModelPoDto modelPo = new ModelPoDto();
			modelPo.setModelPlanId(modelPlan.getModelPlanId());
			modelPo.setModelCode(modelPlan.getModelCode());
			modelPo.setProdOrdNo(modelPlan.getProOrdNo());
			prodOrds.add(modelPo);
		}*/
		
		try {
			list = pickingDao.getProOrdersByModel(modelCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("LIst:"+list.toString());
		for(PickingMst pickingMst : list) {
			ModelPoDto modelPo = new ModelPoDto();
			modelPo.setModelPlanId(pickingMst.getModelPlan().getModelPlanId());
			modelPo.setModelCode(pickingMst.getModelCode());
			modelPo.setProdOrdNo(pickingMst.getProOrdNo());
			modelPo.setStatus(pickingMst.getStatus());
			prodOrds.add(modelPo);
		}
		//System.out.println("Prods:"+prodOrds.toString());
		return prodOrds;
	}

	@Override
	public ModelPlan getModelPlanByProOrdNo(String proOrdNo) {
		// TODO Auto-generated method stub
		return modelPlanDao.getModelPlanByProOrdNo(proOrdNo);
	}

	@Override
	public List<PickingComponent> getPickingComponantByComponentAndPO(ComponentMst componentMst, String proOrdNo) {
		// TODO Auto-generated method stub
		return pickingCompDao.getPickingComponantByComponentAndPO(componentMst.getCompCode(),proOrdNo);
	}
}
