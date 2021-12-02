package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.a2mee.dao.QuarantineDao;
import com.a2mee.model.Quarantine;
import com.a2mee.model.QuarantineInspectionSummary;

@Transactional
@Repository
public class QuarantineDaoImpl implements QuarantineDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Quarantine addQuarantineORHoldItems(Quarantine quarantine) {
		entityManager.persist(quarantine);
		entityManager.flush();
		return quarantine;
	}

	@Override
	public List<Object[]> getAllQuarantineHoldList() {
	/*	String hql = "SELECT DISTINCT q.itemMst.itemDtl, q.itemMst.id FROM Quarantine q INNER JOIN ItemMst im ON im.id = q.itemMst.id";
		List<Object[]> quarantineList = entityManager.createQuery(hql).getResultList();*/
		String sql="SELECT temp.`item_detail`, temp.`item_mst_id` FROM (SELECT DISTINCT q.`stage_name`, q.`qrCode`, SUM(qis.`hold_qty`) AS hold_qty, im.`uom`,im.`item_detail`, q.`item_mst_id`  \r\n" + 
				"FROM `quarantine` q \r\n" + 
				"INNER JOIN `quarantine_inspection_summary` qis ON qis.`qurt_id`= q.`qurt_id` \r\n" + 
				"LEFT OUTER JOIN `item_mst` im ON im.`item_mst_id`=q.`item_mst_id` \r\n" + 
				"GROUP BY qis.`qurt_id`)AS temp WHERE hold_qty>0 GROUP BY temp.`item_mst_id`";
		List<Object[]> quarantineList = entityManager.createNativeQuery(sql).getResultList();
		
		return quarantineList;
	}

	@Override
	public List<Object[]> getQuarantineList(String itemId, String stageName) {
		if (!itemId.isEmpty() && !stageName.isEmpty()) {
			String hql = "SELECT q.`item_mst_id`, q.`qrCode`, SUM(qis.`hold_qty`) AS holdQty, q.`uom`, q.`stage_name` FROM `quarantine` q INNER JOIN quarantine_inspection_summary qis ON qis.`qurt_id` = q.`qurt_id` WHERE q.`item_mst_id` ="
					+ "'" + itemId + "'" + " AND q.`stage_name`=" + "'" + stageName + "'" + " GROUP BY qis.`qurt_id`";
			List<Object[]> quarantineList = entityManager.createNativeQuery(hql).getResultList();
			return quarantineList;
		} else if (!itemId.isEmpty()) {
			String hql = "SELECT q.`item_mst_id`, q.`qrCode`, SUM(qis.`hold_qty`) AS holdQty, q.`uom`, q.`stage_name` FROM `quarantine` q INNER JOIN quarantine_inspection_summary qis ON qis.`qurt_id` = q.`qurt_id` WHERE q.`item_mst_id` ="
					+ "'" + itemId + "'" + " GROUP BY qis.`qurt_id`";
			List<Object[]> quarantineList = entityManager.createNativeQuery(hql).getResultList();
			return quarantineList;
		} else {
			String hql = "SELECT q.`item_mst_id`, q.`qrCode`, SUM(qis.`hold_qty`) AS holdQty, q.`uom`, q.`stage_name` FROM `quarantine` q INNER JOIN quarantine_inspection_summary qis ON qis.`qurt_id` = q.`qurt_id` WHERE "
					+ " q.`stage_name`=" + "'" + stageName + "'" + " GROUP BY qis.`qurt_id`";
			List<Object[]> quarantineList = entityManager.createNativeQuery(hql).getResultList();
			return quarantineList;
		}
	}

	@Override
	public List<Object[]> scanQrcodeAtQuarantineRecord(String qrcode) {
		// String hql = "SELECT SUM(q.`hold_qty`) AS hold_qty, q.`qrCode`,
		// q.`stage_name`, q.`qurt_id` FROM quarantine q WHERE q.`qrCode` ="
		// + "'" + qrcode + "'" + " AND q.hold_qty >0 ORDER BY `qrcode`";

		String hql = "SELECT  SUM(qis.`hold_qty`) AS holdQty, q.`qrCode`, q.`stage_name`, q.`qurt_id` FROM `quarantine` q INNER JOIN quarantine_inspection_summary qis ON qis.`qurt_id` = q.`qurt_id` WHERE q.`qrCode`="
				+ "'" + qrcode + "'" + "  GROUP BY qis.`qurt_id`";
		List<Object[]> quarantineScanList = entityManager.createNativeQuery(hql).getResultList();
		return quarantineScanList;
	}

	@Override
	public boolean saveQuarantineSummaryData(QuarantineInspectionSummary quarantineInspectionSummary) {
		entityManager.persist(quarantineInspectionSummary);
		entityManager.flush();
		return true;
	}

	@Override
	public List<Quarantine> validateQurantineListData(String itemMstId, String qrcode, String stage) {
		String hql = "FROM Quarantine q WHERE q.itemMst.id =:itemMstId AND q.qrcode =:qrcode AND q.stageName =:stage";
		List<Quarantine> quarantineScanList = entityManager.createQuery(hql).setParameter("itemMstId", itemMstId)
				.setParameter("qrcode", qrcode).setParameter("stage", stage).getResultList();
		return quarantineScanList;

	}

	@Override
	public void addQuarantineSummaryData(QuarantineInspectionSummary quarantineInspectionSummary) {
		entityManager.persist(quarantineInspectionSummary);
		entityManager.flush();
	}

	@Override
	public Quarantine getQuarantineListById(long quarId) {
		Quarantine quratine = entityManager.find(Quarantine.class, quarId);
		entityManager.flush();
		return quratine;
	}

	@Override
	public List<Object[]> getAllQuarantineList() {
		String hql = "SELECT DISTINCT q.`stage_name`, q.`qrCode`, SUM(qis.`hold_qty`) AS hold_qty,im.`uom`  FROM `quarantine` q INNER JOIN `quarantine_inspection_summary` qis ON qis.`qurt_id`= q.`qurt_id` LEFT OUTER JOIN `item_mst` im ON im.`item_mst_id`=q.`item_mst_id`  GROUP BY qis.`qurt_id` ";
		List<Object[]> quarantineAllList = entityManager.createNativeQuery(hql).getResultList();
		return quarantineAllList;
	}

	@Override
	public List<Object[]> getDataFromPoshiftQrCodeTable(String qrCode) {
		String hql = "SELECT  pdqc.`po_mst_id`,pdqc.`item_mst_id`,im.`item_detail`\r\n" + 
				"FROM `po_shift_qr_code` pdqc \r\n" + 
				"LEFT OUTER JOIN `item_mst` im ON im.`item_mst_id`=pdqc.`item_mst_id`\r\n" + 
				"WHERE pdqc.`qr_code`= :qrCode";
		List<Object[]> poShifData = entityManager.createNativeQuery(hql).setParameter("qrCode", qrCode).getResultList();
		return poShifData;
	}

}
