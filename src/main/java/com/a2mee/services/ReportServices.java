package com.a2mee.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.a2mee.model.MtlStockIn;
import com.a2mee.model.PendingReport;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.dto.GrnDto;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.dto.PickingReportDto;
import com.a2mee.model.dto.PoDeviationsDto;
import com.a2mee.model.dto.StockListDto;

public interface ReportServices {

	public List<StockListDto>  getStocks(String plantCode, String storageLoc, String itemId, String date);

	public List<StockListDto> getStocksByDate(String startDate, String endDate);

	public List<StockListDto> getStocksByItemAndDate(String startDate, String endDate, String itemId);

	public List<GrnDto> getTodaysGrn(LocalDate searchDate);

	public List<GrnDto> getGrnByDateRange(LocalDate from, LocalDate to);

	public List<PoDeviationsDto> getTodaysDeviation(LocalDate searchDate);

	public List<PoDeviationsDto> getDeviationByDateRange(LocalDate from, LocalDate to);

	public List<StockListDto> getTodaysPutAway(LocalDate searchDate);

	public List<StockListDto> getPutAwayByDateRange(LocalDate from, LocalDate to);

	public List<PickingReportDto> getPickingComponents(Long pickingId, String date);

	public List<PickingReportDto> getPickingComponentsByRange(Long pickingId, String startDate, String endDate);

	public List<Object[]> getGrnListByItem(String itemMstId);

	public List<Object[]> getGrnListByItemDate(String itemMstId, String startDate, String endDate);

	public List<Object[]> getPickingComponentByItemDateNull(String itemMstId);

	public List<Object[]> getPickingComponentByItemDateNullDateRange(String itemMstId, String startDate,
			String endDate);

	public List<Object[]> getPendingComponent(String itemMstId);

	public Object getStockQtyByMaterial(String itemMstId);

	public MtlStockIn getmtlStockInByItem(String itemMstId);

	public List<PendingReport> getPendingReportByDateAndStatus(String reportBy, Date reportDate);

	public void updatePendignReport(PendingReport pendingReport);

	public List<PendingReport> getPendingReportByDateAndStatusAndCode(String reportBy, Date reportDate, String code);
}
 