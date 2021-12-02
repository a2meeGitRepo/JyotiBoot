package com.a2mee.dao.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.a2mee.dao.ReportDao;
import com.a2mee.model.PurchaseOrderError;
import com.a2mee.model.PurchaseOrderItem;

@Repository
@Transactional
public class ReportDaoImpl implements ReportDao {
	
	@PersistenceContext
	private EntityManager entityManager;	

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getStocks(String plantCode, String storageLoc, String itemId, LocalDate date) {
		String hql = "SELECT DISTINCT p.plant_code, s.storage_location, sb.storageBinCode, i.id, i.itemDtl, gil.approQty, "
				+ "gil.holdQty, gil.rejQty FROM GrnItemLot gil INNER JOIN MaterialInspect m ON m.grnItemLot.grnItemLotId = gil.grnItemLotId"
				+ " INNER JOIN ItemMst i ON gil.itemMstId = i.id INNER JOIN StorageBinMst sb ON sb.storageBinCode = m.storageBinCode"
				+ " INNER JOIN Storage s ON sb.storage.storage_id = s.storage_id INNER JOIN Plant p ON sb.plant.plant_id = p.plant_id WHERE"
				+ " p.plant_code =:plantCode AND s.storage_location=:storageLoc AND i.id=:itemId AND m.date=:date";
		try {
			return entityManager.createQuery(hql)
					.setParameter("plantCode", plantCode)
					.setParameter("storageLoc", storageLoc)
					.setParameter("itemId", itemId)
					.setParameter("date", date)
					.getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getStocksByDate(LocalDate date1, LocalDate date2) {
		String hql = "SELECT DISTINCT p.plant_code, s.storage_location, sb.storageBinCode, i.id, i.itemDtl, gil.approQty, "
				+ "gil.holdQty, gil.rejQty FROM GrnItemLot gil INNER JOIN MaterialInspect m ON m.grnItemLot.grnItemLotId = gil.grnItemLotId"
				+ " INNER JOIN ItemMst i ON gil.itemMstId = i.id INNER JOIN StorageBinMst sb ON sb.storageBinCode = m.storageBinCode"
				+ " INNER JOIN Storage s ON sb.storage.storage_id = s.storage_id INNER JOIN Plant p ON sb.plant.plant_id = p.plant_id"
				+ " WHERE m.date BETWEEN :startDate AND :endDate";
		List<Object[]> result = entityManager.createQuery(hql)
				.setParameter("startDate", date1)
				.setParameter("endDate", date2)
				.getResultList();
		System.out.println(result.toString());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getStocksByItemAndDate(LocalDate date1, LocalDate date2, String itemId) {
		String hql = "SELECT DISTINCT p.plant_code, s.storage_location, sb.storageBinCode, i.id, i.itemDtl, gil.approQty, "
				+ "gil.holdQty, gil.rejQty FROM GrnItemLot gil INNER JOIN MaterialInspect m ON m.grnItemLot.grnItemLotId = gil.grnItemLotId"
				+ " INNER JOIN ItemMst i ON gil.itemMstId = i.id INNER JOIN StorageBinMst sb ON sb.storageBinCode = m.storageBinCode"
				+ " INNER JOIN Storage s ON sb.storage.storage_id = s.storage_id INNER JOIN Plant p ON sb.plant.plant_id = p.plant_id"
				+ " WHERE i.id=:itemId AND m.date BETWEEN :startDate AND :endDate";
		List<Object[]> result = entityManager.createQuery(hql)
				.setParameter("startDate", date1)
				.setParameter("endDate", date2)
				.setParameter("itemId", itemId)
				.getResultList();
		System.out.println(result.toString());
		return result;
	}

	@Override
	public List<Object[]> getTodaysGrn(LocalDate searchDate) {
		java.util.Date date = Date.from(searchDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String hql = "SELECT distinct g.grnId,g.grnEntryDate,gil.itemLotNo, gil.grnItemLotId ,gi.itemDtils,gil.boxQty,"
				+ "g.venName,gil.itemBarcode,g.grnBy,gi.inwardDate,gi.invoiceNumber,gi.challanNumber,gi.docDate,gi.remarks,g.errorSolveDate,"
				+ "g.inwardTime,g.grnStatus,g.grnNo,gi.itemTolRecd,gi.itemMst.id,g.purchaseOrderNo,gi.type,gi.boe,gi.courier,gi.docketNo,gi.sapGrnNo,"
				+ "gi.sapGrnDate,gi.delayDays,gi.resPerson,gi.delayPerc,gi.accDocHandover,gi.handoverDate,gi.vehicleNo,gi.vehicleStatus,"
				+ "gi.unloadType,gi.packingType,gi.grnItemId FROM GRN g "
				+ "INNER JOIN GrnItem gi on gi.grn = g.grnId INNER JOIN GrnItemLot gil on gil.grnItem= gi.grnItemId "
				+ "WHERE g.grnEntryDate=:searchDate";
		List<Object[]> result = entityManager.createQuery(hql)
				.setParameter("searchDate", date)
				.getResultList();
		System.out.println(result.toString());
		return result;
	}

	@Override
	public List<Object[]> getGrnByDateRange(LocalDate from, LocalDate to) {
		java.util.Date startDate = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.util.Date endDate = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String hql = "SELECT distinct g.grnId,g.grnEntryDate,gil.itemLotNo, gil.grnItemLotId ,gi.itemDtils,gil.boxQty,"
				+ "g.venName,gil.itemBarcode,g.grnBy,gi.inwardDate,gi.invoiceNumber,gi.challanNumber,gi.docDate,gi.remarks,g.errorSolveDate,"
				+ "g.inwardTime,g.grnStatus,g.grnNo,gi.itemTolRecd,gi.itemMst.id,g.purchaseOrderNo,gi.type,gi.boe,gi.courier,gi.docketNo,gi.sapGrnNo,"
				+ "gi.sapGrnDate,gi.delayDays,gi.resPerson,gi.delayPerc,gi.accDocHandover,gi.handoverDate,gi.vehicleNo,gi.vehicleStatus,"
				+ "gi.unloadType,gi.packingType,gi.grnItemId FROM GRN g "
				+ "INNER JOIN GrnItem gi on gi.grn = g.grnId INNER JOIN GrnItemLot gil on gil.grnItem= gi.grnItemId "
				+ "WHERE g.grnEntryDate BETWEEN :startDate AND :endDate";
		List<Object[]> result = entityManager.createQuery(hql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		System.out.println(result.toString());
		return result;
	}

	@Override
	public List<PurchaseOrderError> getTodaysDeviation(LocalDate searchDate) {
		java.util.Date date = Date.from(searchDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String hql = "from PurchaseOrderError WHERE date=:date";
		return entityManager.createQuery(hql, PurchaseOrderError.class)
				.setParameter("date", date)
				.getResultList();
	}

	@Override
	public List<PurchaseOrderError> getDeviationByDateRange(LocalDate from, LocalDate to) {
		java.util.Date startDate = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.util.Date endDate = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
		String hql = "from PurchaseOrderError WHERE date BETWEEN :startDate AND :endDate";
		return entityManager.createQuery(hql, PurchaseOrderError.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
	}

	@Override
	public PurchaseOrderItem getPoItemById(Long poItemNo) {
		String hql = "from PurchaseOrderItem WHERE purchaseOrderItemId=:poItemNo";
		return entityManager.createQuery(hql, PurchaseOrderItem.class)
				.setParameter("poItemNo", poItemNo)
				.getSingleResult();
	}

	@Override
	public List<Object[]> getTodaysPutAway(LocalDate searchDate) {
		String hql = "SELECT DISTINCT sb.storageBinCode, i.id, i.itemDtl, gil.approQty"
				+ " FROM GrnItemLot gil INNER JOIN MaterialInspect m ON m.grnItemLot.grnItemLotId = gil.grnItemLotId"
				+ " INNER JOIN ItemMst i ON gil.itemMstId = i.id INNER JOIN StorageBinMst sb ON sb.storageBinCode = m.storageBinCode"
				+ " WHERE m.date=:date";
		return entityManager.createQuery(hql)
				.setParameter("date", searchDate)
				.getResultList();
	}

	@Override
	public List<Object[]> getPutAwayByDateRange(LocalDate from, LocalDate to) {
		String hql = "SELECT DISTINCT sb.storageBinCode, i.id, i.itemDtl, gil.approQty"
				+ " FROM GrnItemLot gil INNER JOIN MaterialInspect m ON m.grnItemLot.grnItemLotId = gil.grnItemLotId"
				+ " INNER JOIN ItemMst i ON gil.itemMstId = i.id INNER JOIN StorageBinMst sb ON sb.storageBinCode = m.storageBinCode"
				+ " WHERE m.date BETWEEN :startDate AND :endDate";
		return entityManager.createQuery(hql)
				.setParameter("startDate", from)
				.setParameter("endDate", to)
				.getResultList();
	}

	@Override
	public List<Object[]> getPickingComponents(Long pickingId, String date) {
		// TODO Auto-generated method stub
		String hql = "SELECT pc.componentMst.compCode,pc.pickedQty,pc.storageBinCode,pc.itemNo,pc.postingDocNo,pc.pickingCompId FROM PickingComponent pc  WHERE pc.pickingMst.pickingId=:pickingId and DATE(pc.pickedDate)=DATE(:pickedDate) and pc.pickedQty!=0";
		return entityManager.createQuery(hql)
				.setParameter("pickedDate", date)
				.setParameter("pickingId", pickingId)
				.getResultList();
	}

	@Override
	public List<Object[]> getPickingComponentsByRange(Long pickingId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		String hql = "SELECT pc.componentMst.compCode,pc.pickedQty,pc.storageBinCode,pc.itemNo,pc.postingDocNo FROM PickingComponent pc  WHERE pc.pickingMst.pickingId=:pickingId and DATE(pc.pickedDate) BETWEEN DATE(:startDate) AND DATE(:endDate)";
		return entityManager.createQuery(hql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("pickingId", pickingId)
				.getResultList();
	}

	@Override
	public List<Object[]> getGrnListByItem(String itemMstId) {
		// TODO Auto-generated method stub
		String hql = "SELECT gi.itemMst.id,gi.itemQty,gi.grn.purchaseOrderNo,gi.grn.grnEntryDate FROM GrnItem gi  WHERE gi.itemMst.id=:itemMstId";
		return entityManager.createQuery(hql)
				.setParameter("itemMstId", itemMstId)
				.getResultList();
	}
	
	@Override
	public List<Object[]> getGrnListByItemDate(String itemMstId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		String hql = "SELECT gi.itemMst.id,gi.itemQty,gi.grn.purchaseOrderNo,gi.grn.grnEntryDate, gi.grn.grnBy FROM GrnItem gi  WHERE gi.itemMst.id=:itemMstId and DATE(gi.grn.grnEntryDate) BETWEEN DATE(:startDate) AND DATE(:endDate)";
		return entityManager.createQuery(hql)
				.setParameter("itemMstId", itemMstId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
	}

	@Override
	public List<Object[]> getPickingComponentByItemDateNull(String itemMstId) {
		// TODO Auto-generated method stub
		String hql = "SELECT pc.componentMst.compCode,pc.pickedQty,pc.pickingMst.proOrdNo,pc.pickedDate FROM PickingComponent pc  WHERE pc.componentMst.compCode=:itemMstId and pc.pickedDate is not null";
		return entityManager.createQuery(hql)
				.setParameter("itemMstId", itemMstId)
				.getResultList();
	}

	@Override
	public List<Object[]> getPickingComponentByItemDateNullDateRange(String itemMstId, String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		String hql = "SELECT pc.componentMst.compCode,pc.pickedQty,pc.pickingMst.proOrdNo,pc.pickedDate, pc.pickingMst.employee.firstName,pc.pickingMst.employee.lastName FROM PickingComponent pc  WHERE pc.componentMst.compCode=:itemMstId and pc.pickedDate is not null and DATE(pc.pickedDate) BETWEEN DATE(:startDate) AND DATE(:endDate)";
		return entityManager.createQuery(hql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("itemMstId", itemMstId)
				.getResultList();
	}

	@Override
	public List<Object[]> getPendingComponent(String itemMstId) {
		// TODO Auto-generated method stub
		String hql = "SELECT pc.pickingMst.assignDate, pc.pickingMst.modelCode, pc.pickingMst.employee.firstName, pc.pickingMst.employee.lastName, pc.pickingMst.proOrdNo, pc.componentMst.compCode, pc.componentMst.compDesc, pc.pickCompQty FROM PickingComponent pc  WHERE pc.componentMst.compCode=:itemMstId and pc.pickedDate is null";
		return entityManager.createQuery(hql).setParameter("itemMstId", itemMstId)
				.getResultList();
	}

	@Override
	public Object getStockQtyByMaterial(String itemMstId) {
		// TODO Auto-generated method stub
		String hql = "SELECT SUM(remainQty) FROM MtlStockIn WHERE itemMst.id= :itemMstId";
		return entityManager.createQuery(hql)
				.setParameter("itemMstId", itemMstId)
				.getSingleResult();
	}

	
	

	
}
