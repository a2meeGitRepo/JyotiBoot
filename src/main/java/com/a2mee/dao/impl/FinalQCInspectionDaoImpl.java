package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.a2mee.dao.FinalQCInspectionDao;
import com.a2mee.model.FinalQualityCheck;
import com.a2mee.model.Reasonmaster;

@Repository
@Transactional
public class FinalQCInspectionDaoImpl implements FinalQCInspectionDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Object[]> getQualityCheck(String qrcode) {
		String sql = "SELECT SUM(posqcc.`good_pice`), qi.qc_input_id, psqc.`item_mst_id`, qi.`remaing_qty`  FROM qc_input qi \r\n"
				+ "INNER JOIN `po_shift_qr_code` psqc ON qi.`po_shift_qr_code`=psqc.`qr_code` \r\n"
				+ "INNER JOIN `po_shift_qr_code_count` posqcc ON posqcc.`po_shift_id`=psqc.`po_shift_id` \r\n"
				+ "WHERE qi.`po_shift_qr_code`=:qrcode\r\n" + " AND qi.`remaing_qty`>0 GROUP BY psqc.`po_shift_id`;";
		Query q = entityManager.createNativeQuery(sql).setParameter("qrcode", qrcode);
		List<Object[]> qcList = q.getResultList();
		return qcList;
	}

	@Override
	public void finalQCInspectionSave(FinalQualityCheck qualityCheck) {
		entityManager.persist(qualityCheck);
		entityManager.flush();

	}

	@Override
	public List<Object[]> getMainLineFinalInspectionList(String itemId) {
		String hql = "SELECT im.`item_mst_id`, im.`item_detail`, qi.`po_shift_qr_code`,qi.`total_qty`, qi.`remaing_qty`, fqc.`status`, SUM(fqc.`quantity`), qi.`shift_date`,qi.`qc_input_id` FROM qc_input qi \r\n"
				+ "INNER JOIN item_mst im  ON qi.`po_row_item_id` = im.`item_mst_id` \r\n"
				+ "LEFT OUTER JOIN final_quality_check fqc ON qi.`qc_input_id` = fqc.`qc_input_id`\r\n"
				+ "WHERE im.`item_mst_id`=:itemId AND qi.`remaing_qty`>0 \r\n"
				+ "GROUP BY qi.`qc_input_id`, fqc.`status`";
		Query q = entityManager.createNativeQuery(hql)
				// .setParameter("sDate", Date.valueOf(sDate))
				// .setParameter("eDate", Date.valueOf(eDate))
				.setParameter("itemId", itemId);
		List<Object[]> qcList = q.getResultList();
		return qcList;
	}
	
	@Override
	public List<Object[]> getRemaingQtyByqrcodeNItemId(String qrcode) {
		String hql = "SELECT im.`item_mst_id`, qi.`remaing_qty`\r\n" + 
				"FROM qc_input qi \r\n" + 
				"INNER JOIN item_mst im  ON qi.`po_row_item_id` = im.`item_mst_id` \r\n" + 
				"WHERE qi.`po_shift_qr_code` =:qrcode AND qi.`remaing_qty`>0 \r\n";
		Query q = entityManager.createNativeQuery(hql)
				.setParameter("qrcode", qrcode);
		List<Object[]> qcList = q.getResultList();
		return qcList;
	}

	@Override
	public void updateQCinputTableByqrCode(double remingQty, String qrcode) {

		String hql = "UPDATE qc_input SET `remaing_qty` =:remingQty  WHERE `po_shift_qr_code` =:qrcode1";
		Query q = entityManager.createNativeQuery(hql).setParameter("remingQty", remingQty).setParameter("qrcode1",
				qrcode);
		q.executeUpdate();
	}

	@Override
	public List<String> getItemIdForQuarantine(long qcInputId) {

		String hql = "SELECT DISTINCT qi.`po_row_item_id` FROM `final_quality_check` fqc  INNER JOIN qc_input qi ON fqc.`qc_input_id` = qi.`qc_input_id` WHERE qi.`qc_input_id` ="
				+ qcInputId;
		Query q = entityManager.createNativeQuery(hql);
		return q.getResultList();

	}

	@Override
	public List<Object[]> getFinalQualityCheckItemList() {
		String hql = "SELECT DISTINCT q.`po_row_item_id`, q.`source`,im.`item_detail` FROM qc_input q LEFT OUTER JOIN item_mst im ON im.`item_mst_id`=q.`po_row_item_id` WHERE q.`remaing_qty`>0 ";
		Query q = entityManager.createNativeQuery(hql);
		return q.getResultList();
	}

	@Override
	public List<FinalQualityCheck> updateFinalQCFromQuarantine(String qrcode) {
		String h = "H";
		String hql = " FROM FinalQualityCheck fqc WHERE fqc.qrcode=:qrcode AND fqc.status=:h";
		Query f = entityManager.createQuery(hql).setParameter("qrcode", qrcode).setParameter("h", h).setMaxResults(1);
		return f.getResultList();
	}

	@Override
	public List<String> findQcInputSource(String qrcode) {
		String hql = "SELECT qi.`source` FROM qc_input qi WHERE qi.`po_shift_qr_code` =:qrcode AND qi.`remaing_qty`>0 ";
		List<String> source = entityManager.createNativeQuery(hql).setParameter("qrcode", qrcode).getResultList();
		return source;
	}

	@Override
	public List getQualityCheckForAssembliyQrCode(String qrcode) {
		String hql = "SELECT\r\n" + "        qi.`total_qty`,\r\n" + "        qi.qc_input_id,\r\n"
				+ "        aqc.`assembly_item_id`,\r\n" + "        qi.`remaing_qty`  \r\n" + "    FROM\r\n"
				+ "        qc_input qi   \r\n" + "    INNER JOIN\r\n"
				+ "        `assembly_qr_code` aqc ON aqc.`assembly_qr_code` = qi.`po_shift_qr_code`\r\n"
				+ "    WHERE\r\n" + "        qi.`po_shift_qr_code`=:qrcode \r\n" + "        AND qi.`remaing_qty`>0 \r\n"
				+ "    ";
		Query q = entityManager.createNativeQuery(hql)

				.setParameter("qrcode", qrcode);
		List<Object[]> qcList = q.getResultList();
		return qcList;
	}

	@Override
	public List<Object[]> getMachineTimeByQrCode(String qrcode) {
		String hql = "SELECT DATE_FORMAT(psqcc.`up_date`, '%Y%m%d%H%m%s'), psqcc.`po_shift_id`\r\n" + 
				"FROM `po_shift_qr_code` psqc \r\n" + 
				"INNER JOIN `po_shift_qr_code_count` psqcc ON psqc.`po_shift_id` = psqcc.`po_shift_id`\r\n" + 
				"WHERE psqc.`qr_code`=:qrcode";
		Query q = entityManager.createNativeQuery(hql)
				.setParameter("qrcode", qrcode);
		List<Object[]> qcList = q.getResultList();
		return qcList;
	}

	@Override
	public Reasonmaster getReasonMstById(long reasonId) {
		Reasonmaster reason = entityManager.find(Reasonmaster.class, (int)reasonId);
		entityManager.flush();
		return reason;
	}
}
