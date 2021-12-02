package com.a2mee.services.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.AssemblyMstDao;
import com.a2mee.dao.CompMstDao;
import com.a2mee.dao.ModelPlanDao;
import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.ModelMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.services.CompService;

@Service
public class CompServiceImpl implements CompService {
	
	@Autowired
	CompMstDao compMstDao;
	
	@Autowired
	AssemblyMstDao assemblyMstDao;
	
	@Autowired
	ModelPlanDao modelPlanDao;

	@Override
	public ComponentMst getCompByCodes(String assmCode, String compCode, String modelCode) {
		Optional<ComponentMst> component = compMstDao.getByCodes(assmCode,compCode,modelCode);
		if(component.isPresent()) return component.get();
		else return null;
	}

	@Override
	public ComponentMst addComp(ComponentMst theComp) {
		return compMstDao.save(theComp);
	}

	@Override
	public List<ComponentMst> getCompByAssm(AssemblyMst assemblyMst) {
		Optional<AssemblyMst> assembly = assemblyMstDao.findById(assemblyMst.getAssmblyId());
		List<ComponentMst> components = null;
		if(assembly.isPresent())
			components = compMstDao.findByAssembly(assembly.get());
		if(components != null)
			components.stream().forEach(c -> c.setCompQty(c.getCompQty() * assemblyMst.getAssemblyQty()));
		return components;
	}

	@Override
	public double getCompCountByMonth(String month, String year, String compCode) {
		List<ModelPlan> modelPlans = modelPlanDao.getModelByMonth(month, year);
		double totalCompQty = 0;
		for(ModelPlan theModelPlan : modelPlans) {
			double assmQty = 0;
			Optional<ComponentMst> component = compMstDao.getByModelCode(compCode, theModelPlan.getModelCode());
			if(component.isPresent()) {
				assmQty = component.get().getAssembly().getAssemblyQty() * theModelPlan.getQty();
				totalCompQty = totalCompQty + (component.get().getCompQty() * assmQty);
			}
		}
		return totalCompQty;
	}

	@Override
	public List<ComponentMst> getCompByMonth(String month, String year) {
		List<ModelPlan> modelPlans = modelPlanDao.getModelByMonth(month, year);
		List<ComponentMst> finalComponents = new ArrayList<>();
		for(ModelPlan theModelPlan : modelPlans) {
			double assmQty = 0;
			List<ComponentMst> components = compMstDao.findByModelCode(theModelPlan.getModelCode());
			for(ComponentMst component : components) {				
				assmQty = component.getAssembly().getAssemblyQty() * theModelPlan.getQty();
				component.setCompQty(component.getCompQty() * assmQty);
			}
			finalComponents.addAll(components);
		}
		 System.out.println("heloo------"+finalComponents);
		 
		 finalComponents = new ArrayList<>(finalComponents.stream()
                 .collect(Collectors.toMap(ComponentMst::getCompCode, o -> o,
                                (a, b) -> new ComponentMst(a.getCompCode(), a.getCompDesc(),
                                                       a.getCompQty() + b
                                                           .getCompQty(), a.getUom()), LinkedHashMap::new)).values());
		 
		 finalComponents.forEach(e->{
			e.setAssembly(null); 
			e.setCompId(null);
			e.setCompQty(Double.parseDouble(new DecimalFormat("#.00").format(e.getCompQty())));
		 });
		 System.out.println("heloo------"+finalComponents);
		 
		 return finalComponents;
	}

	@Override
	public List<ComponentMst> getComponentByAssembly(String assemblyCode) {
		return compMstDao.getComponentByAssembly(assemblyCode);
	}

	@Override
	public List<ComponentMst> getComponents() {
		// TODO Auto-generated method stub
		return compMstDao.findAll();
	}

	@Override
	public ComponentMst getCompByComCodes(String comCode,String modelCode) {
		// TODO Auto-generated method stub
		try {
			Optional<ComponentMst> optional=compMstDao.findByCode(comCode,modelCode);
			System.out.println("optional    :: "+optional.isPresent());
			return optional.isPresent()?optional.get():null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
		
		
		
	}

	@Override
	public List<ComponentMst> getComponentByAssemblyID(Long assmblyId) {
		// TODO Auto-generated method stub
		return compMstDao.getComponentByAssemblyID(assmblyId);
	}

	@Override
	public ComponentMst getComponentByComCode(String compCode) {
		// TODO Auto-generated method stub
	if(compMstDao.getComponentByAssemblyID(compCode).size()==0){
		return null;
	}else{
		return compMstDao.getComponentByAssemblyID(compCode).get(0);
	}
	}

	

}