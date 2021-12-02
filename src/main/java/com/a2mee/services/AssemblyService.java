package com.a2mee.services;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ModelMst;
import com.a2mee.model.ModelPlan;

public interface AssemblyService {

	AssemblyMst getassemblyByCode(String assmCode, String modelCode);

	AssemblyMst addAssembly(AssemblyMst theAssembly);

	AssemblyMst getassemblyByAssCode(String assmCode, String modelCode);

	List<AssemblyMst> getAssmByModel(ModelPlan modelPlan);

	double getAssmCountByMonth(String month, String year, String assmCode);

	List<AssemblyMst> getAssmByMonth(String month, String year);

	AssemblyMst getassemblyByAssmId(Long assmblyId);

	List<AssemblyMst> getassemblyByCode(String modelCode);

	List<AssemblyMst> getAssemblies();

	ModelMst getModelByCode(String modelCode);

	List<AssemblyMst> getAssemblyByModelId(Long modelId);


}
