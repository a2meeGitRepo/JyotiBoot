package com.a2mee.dao;

public class DashboardModelPlanDto {

	private String model;
	private double lastMonth;
	private double currentMonth;
	private double nextMonth;
	
	private String currentMonthName;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(double lastMonth) {
		this.lastMonth = lastMonth;
	}

	public double getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(double currentMonth) {
		this.currentMonth = currentMonth;
	}

	public double getNextMonth() {
		return nextMonth;
	}

	public void setNextMonth(double nextMonth) {
		this.nextMonth = nextMonth;
	}

	public String getCurrentMonthName() {
		return currentMonthName;
	}

	public void setCurrentMonthName(String currentMonthName) {
		this.currentMonthName = currentMonthName;
	}
	
	
	
	
	
	
}
