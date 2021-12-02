package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.MtlStockInDao;
import com.a2mee.dao.MtlStockInDaoCustom;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MtlStockIn;

@Repository
@Transactional

public class MtlStockInDaoImpl implements MtlStockInDaoCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Value(value = "${mtlStockInDao.getMtrilIdandQrNo.hql}")
	private String mtlStockInDaoGetMtrilIdandQrNoHql;

	@Transactional(readOnly = false)
	public MtlStockIn saveInStock(MtlStockIn mtlStockIn) {
		entityManager.persist(mtlStockIn);
		entityManager.flush();
		return mtlStockIn;
	}

	@Override
	public List<Object[]> getMtrilIdandQrNo(long itmLotId) {
		List<Object[]> list = entityManager.createQuery(mtlStockInDaoGetMtrilIdandQrNoHql)
				.setParameter("itmLotId", itmLotId).getResultList();
		return list;
	}
	// SELECT SUM(msi.`remain_qty`), msi.`grn_date`, msi.`bar_code`FROM
	// `mtl_stock_in` msi WHERE msi.`remain_qty`>0 AND msi.`item_mst_id` = 'RM0024'
	// AND msi.`status` IS NULL GROUP BY msi.bar_code ORDER BY msi.grn_date;

//	@Override
//	public List<Object[]> getMtlStockInMinDateQtyChackOutBox(String itemId, String status) {
//		if(status.equals("R")) {
//			
//		}
//		String hql = "SELECT SUM(msi.remainQty), msi.grnDate, msi.barCode FROM MtlStockIn msi WHERE msi.remainQty>0 AND msi.itemMst.id =:itemId AND msi.status IS NULL GROUP BY msi.barCode ORDER BY msi.grnDate";
//		List<Object[]> list = entityManager.createQuery(hql).setParameter("itemId", itemId).setMaxResults(1).getResultList();
//		return list;
//
//	}

	@Override
	public List<Object[]> getMtlStockInMinDateQtyWithoutChackOut(String itemId, Boolean status) {
		String hql = "";
		if (status != null && status)
		{
			hql = "SELECT SUM(msi.remainQty), msi.grnDate, msi.barCode FROM MtlStockIn msi WHERE msi.remainQty>0 AND msi.itemMst.id =:itemId GROUP BY msi.barCode ORDER BY msi.grnDate";
				
		} else {
			hql = "SELECT SUM(msi.remainQty), msi.grnDate, msi.barCode FROM MtlStockIn msi WHERE msi.remainQty>0 AND msi.itemMst.id =:itemId AND msi.status IS NULL GROUP BY msi.barCode ORDER BY msi.grnDate";
		}
		
		List<Object[]> list = entityManager.createQuery(hql).setParameter("itemId", itemId).setMaxResults(1).getResultList();
		return list;
	}

	@Override
	public List<Object[]> showPickingList(String qrCode ,String itemId) {
		String sql="SELECT (mis.mtl_issu_qty-mis.mtl_issued_qty ) AS c ,im.`item_detail`,im.`item_mst_id`,mis.`mtl_issu_sum_id`,mis.`qr_code`  FROM mtl_issu_summary mis LEFT OUTER JOIN item_mst im ON im.`item_mst_id`=mis.`item_mst_id` WHERE mis.`item_mst_id`= :itemId AND mis.`qr_code`=:qrCode AND (mis.mtl_issu_qty-mis.mtl_issued_qty)>0";
		List<Object[]> list = entityManager.createNativeQuery(sql).setParameter("qrCode", qrCode).setParameter("itemId", itemId).getResultList();
		return list;
	}

	@Override
	public List<MtlStockIn> getListForStockInUpdate(String qrCode, String itemId ,int status) {
		String sql=null;
		if(status==1) {
			sql="Select * FROM `mtl_stock_in` msi WHERE msi.`bar_code`= :qrCode AND msi.`item_mst_id`= :itemId AND msi.`remain_qty`>0 ORDER BY msi.`grn_date`";
		}else {
		sql="Select * FROM `mtl_stock_in` msi WHERE msi.`bar_code`= :qrCode AND msi.`item_mst_id`= :itemId AND msi.`remain_qty`>0 AND msi.status IS NULL ORDER BY msi.`grn_date`";
		}
		List<MtlStockIn> list = entityManager.createNativeQuery(sql,MtlStockIn.class).setParameter("qrCode", qrCode).setParameter("itemId", itemId).getResultList();
		return list;
	}

	@Override
	public MtlStockIn updateStockInbyId(long stockInId,double qty) {
		String sql="UPDATE mtl_stock_in msi SET msi.`remain_qty`= :qty WHERE msi.`mtl_stk_in_id`= :stockInId ";
		entityManager.createNativeQuery(sql).setParameter("stockInId", stockInId).setParameter("qty", qty).executeUpdate();
		return null;
	}

	@Override
	public List<MtlStockIn> getMaterialStock(int pageno, int perPage) {
		// TODO Auto-generated method stub
		long result = (long) entityManager.createQuery("SELECT count(a) FROM MtlStockIn a").getSingleResult();
		int total_count=(int) result;

		
		int firstR = total_count - (pageno * perPage);
		int maxR = total_count - ((pageno - 1) * perPage);
		

		if(firstR<0) {
			firstR=0;
		}
		String sql="Select * FROM `mtl_stock_in`";

		
		List<MtlStockIn> list = entityManager.createNativeQuery(sql,MtlStockIn.class).setFirstResult(firstR).setMaxResults(maxR).getResultList();

		return list;
	}

	@Override
	public List<MtlStockIn> materialLocationWiseStockSearch(int pageno, int perPage, String search) {
		// TODO Auto-generated method stub
		//where e.itemMst.id  LIKE %:searchText% OR e.itemMst.itemDtl  LIKE %:searchText% OR e.storageBinCode LIKE %:searchText%
		long result = (long) entityManager.createQuery("SELECT count(e) FROM MtlStockIn e where e.itemMst.id  LIKE :searchText OR e.itemMst.itemDtl  LIKE :searchText OR e.storageBinCode LIKE :searchText").setParameter("searchText", "%"+search+"%").getSingleResult();
		int total_count=(int) result;

		
		int firstR = total_count - (pageno * perPage);
		int maxR = total_count - ((pageno - 1) * perPage);
		

		if(firstR<0) {
			firstR=0;
		}
	//	String sql="SELECT * FROM MtlStockIn e where e.itemMst.id  LIKE :searchText OR e.itemMst.itemDtl  LIKE :searchText OR e.storage_bin_code LIKE :searchText";

		
		List<MtlStockIn> list = entityManager.createQuery("FROM MtlStockIn e where e.itemMst.id  LIKE :searchText OR e.itemMst.itemDtl  LIKE :searchText OR e.storageBinCode LIKE :searchText",MtlStockIn.class).setParameter("searchText", "%"+search+"%").setFirstResult(firstR).setMaxResults(maxR).getResultList();

		return list;
	}
	
	

/*	@Override
	public List<MtlStockIn> materialStockBySearch(int pageno, int perPage, String search) {
		// TODO Auto-generated method stub
		long result = (long) entityManager.createQuery("SELECT count(a) FROM MtlStockIn a").getSingleResult();
		int total_count=(int) result;

		
		int firstR = total_count - (pageno * perPage);
		int maxR = total_count - ((pageno - 1) * perPage);
		

		if(firstR<0) {
			firstR=0;
		}
		String sql="Select * FROM `mtl_stock_in`";

		
		List<MtlStockIn> list = entityManager.createNativeQuery(sql,MtlStockIn.class).setFirstResult(firstR).setMaxResults(maxR).getResultList();

		return list;
	}*/
}
