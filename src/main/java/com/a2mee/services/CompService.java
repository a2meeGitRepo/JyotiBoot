package com.a2mee.services;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;

public interface CompService {

	ComponentMst getCompByCodes(String assmCode, String compCode, String modelCode);

	ComponentMst addComp(ComponentMst theComp);

	List<ComponentMst> getCompByAssm(AssemblyMst assemblyMst);

	double getCompCountByMonth(String month, String year, String compCode);

	List<ComponentMst> getCompByMonth(String month, String year);

	List<ComponentMst> getComponentByAssembly(String assemblyCode);

	List<ComponentMst> getComponents();

	ComponentMst getCompByComCodes(String comcode,String modelCode);

	List<ComponentMst> getComponentByAssemblyID(Long assmblyId);

	ComponentMst getComponentByComCode(String compCode);

}
