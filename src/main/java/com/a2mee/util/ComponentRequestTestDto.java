package com.a2mee.util;

import java.util.List;

import org.hibernate.mapping.Array;

public class ComponentRequestTestDto {

	
@Override
	public String toString() {
		return "ComponentRequestTestDto [assemblyCodes=" + assemblyCodes + ", asmCodes=" + asmCodes + ", pickingIds="
				+ pickingIds + ", comCode=" + comCode + ", location=" + location + "]";
	}
private List<String> assemblyCodes;
private String asmCodes;
private List<Long> pickingIds;
private String  comCode;
private String  location;
public List<String> getAssemblyCodes() {
	return assemblyCodes;
}
public void setAssemblyCodes(List<String> assemblyCodes) {
	this.assemblyCodes = assemblyCodes;
}
public List<Long> getPickingIds() {
	return pickingIds;
}
public void setPickingIds(List<Long> pickingIds) {
	this.pickingIds = pickingIds;
}

public String getComCode() {
	return comCode;
}
public void setComCode(String comCode) {
	this.comCode = comCode;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public String getAsmCodes() {
	return asmCodes;
}
public void setAsmCodes(String asmCodes) {
	this.asmCodes = asmCodes;
}






}
