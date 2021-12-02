package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.MaterialInspectDao;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MtlStockIn;


@Transactional
@Repository

public class MaterialInspectDaoImpl implements MaterialInspectDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Value(value = "${materialInspectDao.getAcceptedList.hql}")
	private String materialInspectDaoGetAcceptedListHql;
	
	@Value(value = "${materialInspectDao.getStockList.hql}")
	private String materialInspectDaoGetStockListHql;
	
	@Value(value = "${materialInspectDao.getStockListByItem.hql}")
	private String materialInspectDaoGetStockListByItemHql;
	
	private String mtlStatus = "A";

	@Override
	@Transactional
	public MaterialInspect saveInspMterial(MaterialInspect materialInspect) {
		entityManager.persist(materialInspect);	
		entityManager.flush();
		return materialInspect;
	}

	@Override
	public List<Object[]> updateQuarantineDataIntoMaterialInspection(String qrcode) {
		String sql ="SELECT mi.`grn_item_lot_id`, gil.`grn_barcode`, SUM(mi.`mtl_qty`)  \r\n" + 
				"FROM material_inspection mi \r\n" + 
				"INNER JOIN `grn_item_lot` gil ON mi.`grn_item_lot_id` = gil.`grn_item_lot_id`\r\n" + 
				"WHERE gil.grn_barcode =:qrcode AND mi.`mtl_status`='H'";
		
		List<Object[]> materialInspectionList = entityManager.createNativeQuery(sql)
				.setParameter("qrcode", qrcode).getResultList();
		return materialInspectionList;
	}

	@Override
	public boolean storeItem(long grnNo, String storageBinCode) {
		
		Query query = entityManager.createQuery("UPDATE MtlStockIn ms SET ms.storageBinCode=:storageBinCode WHERE ms.materialInspect.mtlId = (SELECT mtlId FROM MaterialInspect m WHERE m.mtlStatus=:mtlStatus and m.grnItemLot.grnNo=:grnNo)");
		query.setParameter("grnNo", grnNo);
		query.setParameter("storageBinCode", storageBinCode);
		query.setParameter("mtlStatus", mtlStatus);
		int i = query.executeUpdate();
		
		/*Query query1 = entityManager.createQuery("SELECT mtlId FROM MaterialInspect m WHERE m.mtlStatus=:mtlStatus and m.grnItemLot.grnNo=:grnNo");
		query1.setParameter("grnNo", grnNo);
		query1.setParameter("mtlStatus", mtlStatus);
		long id=(long) query1.getSingleResult();*/
		
		System.out.println("ID :: "+grnNo);
	//	List<MtlStockIn> list=
		//int i=1;
		
		System.out.println("1 :: "+1);
		if(i>0) return true;
		else return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAcceptedList() {
		System.out.println(mtlStatus);
		List<Object[]> acceptList = entityManager.createQuery(materialInspectDaoGetAcceptedListHql).setParameter("mtlStatus", mtlStatus).getResultList();
		System.out.println("Size in file: "+acceptList.size());
		return acceptList;
	}

	@Override
	public List<Object[]> getStockList(String storageBinCode) {
		// TODO Auto-generated method stub
		List<Object[]> stockList = entityManager.createQuery(materialInspectDaoGetStockListHql).setParameter("storageBinCode", storageBinCode).setParameter("mtlStatus", mtlStatus).getResultList();
		return stockList;
	}

	@Override
	public List<Object[]> getStockListByItem(String storageBinCode, String itemId) {
		List<Object[]> stockList = entityManager.createQuery(materialInspectDaoGetStockListByItemHql).setParameter("storageBinCode", storageBinCode).setParameter("itemId", itemId).setParameter("mtlStatus", mtlStatus).getResultList();
		return stockList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getStockByComp(String compCode) {
		return entityManager.createQuery("select m.id, m.remainQty, m.itemMst.id, m.storageBinCode, m.itemMst.itemDtl from MtlStockIn m where m.itemMst.id=:compCode and m.remainQty>0 and m.storageBinCode is not null").setParameter("compCode",compCode).getResultList();
	}

	@Override
	public MtlStockIn getStockById(long stockInId) {
		return entityManager.createQuery("from MtlStockIn m where m.id=:stockInId", MtlStockIn.class).setParameter("stockInId",stockInId).getSingleResult();
	}

	@Override
	public void updateStock(long id, double remainQty) {
		entityManager.createQuery("UPDATE MtlStockIn AS m SET m.remainQty =:remainQty WHERE m.id =:id").setParameter("id",id).setParameter("remainQty",remainQty).executeUpdate();
	}

	@Override
	public List<Object> getAvailableStockQty(String compCode) {
		// TODO Auto-generated method stub
		return entityManager.createQuery("select m.remainQty from MtlStockIn m where m.itemMst.id=:compCode").setParameter("compCode",compCode).getResultList();
	}

	@Override
	public void updateStorageBin(long grnNo, String storageBinCode) {
		// TODO Auto-generated method stub
		List<MaterialInspect> mList = entityManager.createQuery("FROM MaterialInspect m WHERE m.mtlStatus=:mtlStatus and m.grnItemLot.grnNo=:grnNo").setParameter("grnNo", grnNo).setParameter("mtlStatus", mtlStatus).getResultList();
		
		long mtlId = mList.get(0).getMtlId();
		System.out.println("Mtl Id:"+mtlId);
		
		Query query = entityManager.createQuery("UPDATE MaterialInspect m SET m.storageBinCode=:storageBinCode WHERE m.mtlId=:mtlId");
		query.setParameter("mtlId", mtlId);
		query.setParameter("storageBinCode", storageBinCode);
		query.executeUpdate();
	
	}
}
