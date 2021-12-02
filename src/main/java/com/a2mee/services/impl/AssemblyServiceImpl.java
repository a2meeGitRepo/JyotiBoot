package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.event.spi.EvictEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.a2mee.dao.AssemblyMstDao;
import com.a2mee.dao.ModelMstDao;
import com.a2mee.dao.ModelPlanDao;
import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ModelMst;
import com.a2mee.model.ModelPlan;
import com.a2mee.services.AssemblyService;

@Service
public class AssemblyServiceImpl implements AssemblyService {

	@Autowired
	AssemblyMstDao assemblyMstDao;

	@Autowired
	ModelMstDao modelMstDao;

	@Autowired
	ModelPlanDao modelPlanDao;

	@Override
	public AssemblyMst getassemblyByCode(String assmCode, String modelCode) {
		Optional<AssemblyMst> assembly = assemblyMstDao.getByCodes(assmCode, modelCode);
		if (assembly.isPresent())
			return assembly.get();
		else
			return null;
	}

	@Override
	public AssemblyMst addAssembly(AssemblyMst theAssembly) {
		return assemblyMstDao.save(theAssembly);
	}

	@Override
	public AssemblyMst getassemblyByAssCode(String assmCode, String modelCode) {
		Optional<AssemblyMst> assembly = assemblyMstDao.findByAssemblyCodeAndModelModelCode(assmCode, modelCode);
		if (assembly.isPresent())
			return assembly.get();
		else
			return null;
	}

	@Override
	public List<AssemblyMst> getAssmByModel(ModelPlan modelPlan) {
		Optional<ModelMst> model = modelMstDao.findByModelCode(modelPlan.getModelCode());
		List<AssemblyMst> assemblies = null;
		if (model.isPresent()){
			assemblies = assemblyMstDao.findByModel(model.get());
		}
			
		if (assemblies != null){
			assemblies.stream().forEach(a -> a.setAssemblyQty(a.getAssemblyQty() * modelPlan.getQty()));
		}
			
		return assemblies;
	}

	@Override
	public double getAssmCountByMonth(String month, String year, String assmCode) {
		List<ModelPlan> modelPlans = modelPlanDao.getModelByMonth(month, year);
		double totalAssmQty = 0;
		for (ModelPlan theModelPlan : modelPlans) {
			totalAssmQty = totalAssmQty
					+ (assemblyMstDao.getByCodes(assmCode, theModelPlan.getModelCode()).get().getAssemblyQty()
							* theModelPlan.getQty());
		}
		return totalAssmQty;
	}

	@Override
	public List<AssemblyMst> getAssmByMonth(String month, String year) {
		List<ModelPlan> modelPlans = modelPlanDao.getModelByMonth(month, year);
		List<AssemblyMst> finalAssemblies = new ArrayList<>();
		for (ModelPlan theModePlan : modelPlans) {
			List<AssemblyMst> assemblies = assemblyMstDao.getByModelCode(theModePlan.getModelCode());
			assemblies.stream().forEach(a -> a.setAssemblyQty(a.getAssemblyQty() * theModePlan.getQty()));
			finalAssemblies.addAll(assemblies);
		}
		
		 System.out.println("heloo------"+finalAssemblies);
		 
		 /*Less Verbose Method*/
		 finalAssemblies = new ArrayList<>(finalAssemblies.stream()
                 .collect(Collectors.toMap(AssemblyMst::getAssemblyCode, o -> o,
                                (a, b) -> new AssemblyMst(a.getAssemblyCode(), a.getAssemblyDesc(),
                                                       a.getAssemblyQty() + b
                                                           .getAssemblyQty(), a.getUom()), LinkedHashMap::new)).values());
		 
		 /*Verbose Method*/
//		 HashSet<String> assmCode=new HashSet<>();		 
//		 Map<String, Double> assemblyDupMap = new HashMap<>();
//		 for (Iterator<AssemblyMst> it = finalAssemblies.iterator(); it.hasNext(); ){
//		       AssemblyMst a = it.next();
//		       if(!assmCode.add(a.getAssemblyCode())){ 
//		          // store the duplicate information 
//		          double currentQty = assemblyDupMap.getOrDefault(a.getAssemblyCode(), 0D);
//		          assemblyDupMap.put(a.getAssemblyCode(), currentQty + a.getAssemblyQty()); 
//		          // remove it from the list
//		          it.remove();        
//		       }
//		 };
//		 
//		 finalAssemblies.forEach( a -> Optional.ofNullable(assemblyDupMap.get(a.getAssemblyCode()))
//                 .ifPresent(qty -> a.incrementQty(qty)));
		 
		 finalAssemblies.forEach(f->{
			 f.setModel(null);
			 f.setAssmblyId(null);
		 });
		 
		 return finalAssemblies;
	}

	@Override
	public AssemblyMst getassemblyByAssmId(Long assmblyId) {
		Optional<AssemblyMst> opt = assemblyMstDao.findById(assmblyId);
		return opt.isPresent() ? opt.get() : null;
	}

	@Override
	public List<AssemblyMst> getassemblyByCode(String modelCode) {
		return assemblyMstDao.getassemblyByCode(modelCode);
	}

	@Override
	public List<AssemblyMst> getAssemblies() {
		return assemblyMstDao.findAll();
	}

	@Override
	public ModelMst getModelByCode(String modelCode) {
		// TODO Auto-generated method stub
		Optional<ModelMst> mts=modelMstDao.findByModelCode(modelCode);
		return mts.isPresent()?mts.get():null;
	}

	@Override
	public List<AssemblyMst> getAssemblyByModelId(Long modelId) {
		// TODO Auto-generated method stub
		return assemblyMstDao.getAssemblyByModelId(modelId);
	}
}
