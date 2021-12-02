package com.a2mee.util;

public enum Constants {
	
	ROWMATERIAL_WHERHOUSE("1RM"),
	WORK_IN_PROGRESS("1WIP"),
	MATERIAL_ACCEPTE_STATUS("A"),
	MATERIAL_REJECT_STATUS("R"),
	MATERIAL_HOLD_STATUS("H"),
	
	CONSUMABLE("C"),
	FINAL("F"),
	ASSEMBLING("A"),
	MOLD("M"),

	UOM("KG"),
	HOLD("H");
	
	public static final String RECEIVED_AT_STORE = "R";
	public static final String DISPATCHED_FROM_STORE = "D";

	
	
	
	String value;
	public String getValue() {
		return value;
	}
	private Constants(String value) {
		this.value = value;
	}
}
