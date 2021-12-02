package com.a2mee.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.a2mee.dao.GrnItemLotDao;
import com.a2mee.model.GrnItemLot;
import com.a2mee.util.Constants;

@Transactional
@Repository
public class GrnItemLotDaoImpl implements GrnItemLotDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Integer updateQrCode(long grItmlotId, String qrCodeNo) {

		String hql = "UPDATE GrnItemLot SET itemBarcode = :qrCodeNo WHERE grnItemLotId= :grItmlotId ";
		int value = entityManager.createQuery(hql).setParameter("grItmlotId", grItmlotId)
				.setParameter("qrCodeNo", qrCodeNo).executeUpdate();
		return value;
	}

	public Integer updateInspetSumm(long grItmlotId, double qty, String status) {
		int value = 1;
		GrnItemLot grnItemLot = (GrnItemLot) entityManager.find(GrnItemLot.class, grItmlotId);
		if (status.equalsIgnoreCase(Constants.MATERIAL_ACCEPTE_STATUS.getValue())) {
			grnItemLot.setApproQty(qty);
		}
		if (status.equalsIgnoreCase(Constants.MATERIAL_REJECT_STATUS.getValue())) {
			grnItemLot.setRejQty(qty);
		}
		if (status.equalsIgnoreCase(Constants.MATERIAL_HOLD_STATUS.getValue())) {
			grnItemLot.setHoldQty(qty);
		}
		entityManager.flush();
		return value;
	}

	@Override
	public GrnItemLot getElementById(long grItmlotId) {
		GrnItemLot grnItemLot = null;
		try {
		    grnItemLot = entityManager.find(GrnItemLot.class, grItmlotId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grnItemLot;
	}

	public List<Object[]> getDatabtQrcode(String qrCode) {
		String hql = "SELECT gil.grn_item_lot_id,gil.batch_qty,gil.appro_qty,gil.rej_qty,gil.hold_qty FROM grn_item_lot gil WHERE gil.grn_barcode= :itemBarcode";
		List<Object[]> grnItemLot = entityManager.createNativeQuery(hql).setParameter("itemBarcode", qrCode).getResultList();

		return grnItemLot;
	}

	@Override
	public List<Object[]> findItemUseingGrnLotId(long grnLotId) {
		String sql = "SELECT gi.item_mst_id,gi.grn_item_id FROM grn_item_lot gil INNER JOIN grn_item gi ON gi.grn_item_id=gil.grn_item_id WHERE gil.grn_item_lot_id= :grnLotId";
		List<Object[]> grnItemId = entityManager.createNativeQuery(sql).setParameter("grnLotId", grnLotId).getResultList();
		return grnItemId;
	}

	@Override
	public GrnItemLot saveGrnItemLot(GrnItemLot theGrnItemLot) {
		entityManager.persist(theGrnItemLot);
		return theGrnItemLot;
	}

}
