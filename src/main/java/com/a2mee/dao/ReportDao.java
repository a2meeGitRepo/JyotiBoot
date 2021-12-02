package com.a2mee.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.PurchaseOrderItem;

public interface ReportDao {

	public List<Object[]> getStocks(String plantCode, String storageLoc, String itemId, LocalDate searchDate);

	public List<Object[]> getStocksByDate(LocalDate date1, LocalDate date2);

	public List<Object[]> getStocksByItemAndDate(LocalDate date1, LocalDate date2, String itemId);

	public List<Object[]> getTodaysGrn(LocalDate searchDate);

	public List<Object[]> getGrnByDateRange(LocalDate from, LocalDate to);

	public List<PurchaseOrderError> getTodaysDeviation(LocalDate searchDate);

	public List<PurchaseOrderError> getDeviationByDateRange(LocalDate from, LocalDate to);

	public PurchaseOrderItem getPoItemById(Long poItemNo);

	public List<Object[]> getTodaysPutAway(LocalDate searchDate);

	public List<Object[]> getPutAwayByDateRange(LocalDate from, LocalDate to);

	public List<Object[]> getPickingComponents(Long pickingId, String date);

	public List<Object[]> getPickingComponentsByRange(Long pickingId, String startDate, String endDate);

	public List<Object[]> getGrnListByItem(String itemMstId);

	public List<Object[]> getGrnListByItemDate(String itemMstId, String startDate, String endDate);

	public List<Object[]> getPickingComponentByItemDateNull(String itemMstId);

	public List<Object[]> getPickingComponentByItemDateNullDateRange(String itemMstId, String startDate,
			String endDate);

	public List<Object[]> getPendingComponent(String itemMstId);

	public Object getStockQtyByMaterial(String itemMstId);
}
