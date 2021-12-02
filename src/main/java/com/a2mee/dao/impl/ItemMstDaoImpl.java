package com.a2mee.dao.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.ItemMstDao;
import com.a2mee.dao.ItemMstDao;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.StorageBinMst;
import com.a2mee.model.VendorMst;
import com.a2mee.model.dto.MasterDto;
import com.a2mee.model.dto.PoMaterialListDto;
import com.a2mee.util.Constants;

@Repository
@Transactional
public class ItemMstDaoImpl implements ItemMstDao {

	@PersistenceContext
	private EntityManager entityManager;

	/* on GRN barcode generation screen item get by vendar id */
	public List<Object[]> getItemList(String venId) {

		String hql = "SELECT distinct pi.itemMst.id, im.itemDtl, p.vendorMst.id, p.venName FROM PurchaseOrderItem pi INNER JOIN PurchaseOrder p on pi.purchaseOrder = p.purchaseOrderNo LEFT OUTER JOIN ItemMst im on im.id =pi.itemMst  WHERE p.vendorMst.id = :vendorId GROUP BY pi.itemMst.id";
		List<Object[]> itemList = entityManager.createQuery(hql).setParameter("vendorId", venId).getResultList();
		System.out.println("size item mst=" + itemList.size());
		return itemList;
	}
	
	@Override
	public List<Object[]> getItemGrnlist(String venId) {
		String hql = "SELECT distinct gi.itemMst.id, im.itemDtl, g.vendorMst.id, g.venName FROM GrnItem gi INNER JOIN GRN g on gi.grn.grnId = g.grnId INNER JOIN GrnItemLot gil on gil.grnItem= gi.grnItemId LEFT OUTER JOIN ItemMst im on im.id =gi.itemMst WHERE gil.itemBarcode IS NULL AND g.vendorMst.id = :vendorId GROUP BY gi.itemMst.id";
		List<Object[]> itemList = entityManager.createQuery(hql).setParameter("vendorId", venId).getResultList();
		System.out.println("size item mst=" + itemList.size());
		return itemList;
	}

	public List<Object[]> getProOrdItemList(LocalDate startDate, LocalDate endDate) {
		String o = "R";
//String sql=" SELECT DISTINCT im.item_detail AS col_0_0_, im.item_mst_id AS col_1_0_ FROM po_mst pm INNER JOIN po_row pr ON ( pr.po_mst_id=pm.po_mst_id ) LEFT OUTER JOIN item_mst im ON (pr.item_mst_id=im.item_mst_id ) INNER JOIN mtl_stock_in msi ON (im.item_mst_id=msi.item_mst_id)INNER JOIN `mtl_issu_summary` mis ON (mis.item_mst_id=msi.item_mst_id AND (mis.`mtl_issu_qty`-mis.`mtl_issued_qty`) = 0 ) WHERE msi.remain_qty>0 AND pm.po_status='N' AND (pm.po_post_date BETWEEN '2018-06-22' AND '2018-06-29') ORDER BY im.item_detail";
		
		String sql="SELECT TEMP.item_detail,TEMP.item_mst_id FROM (SELECT DISTINCT im.item_detail AS item_detail, pr.item_mst_id AS item_mst_id , mis.item_mst_id AS mis_item_mst_id FROM po_mst pm INNER JOIN po_row pr ON ( pr.po_mst_id=pm.po_mst_id ) LEFT OUTER JOIN item_mst im ON (pr.item_mst_id=im.item_mst_id ) INNER JOIN mtl_stock_in msi ON (pr.item_mst_id=msi.item_mst_id) LEFT OUTER JOIN `mtl_issu_summary` mis ON (mis.item_mst_id=msi.item_mst_id AND (mis.`mtl_issu_qty`-mis.`mtl_issued_qty`) != 0 ) WHERE msi.remain_qty>0 AND pm.po_status= :poStatus AND (pm.po_post_date BETWEEN :startDate AND :endDate)) AS TEMP WHERE TEMP.mis_item_mst_id IS NULL ORDER BY TEMP.item_detail";
		//String hql = "SELECT distinct im.itemDtl,im.id FROM ProOrd pm INNER JOIN ProOrdRow pr ON pr.proOrd.id=pm.id LEFT OUTER JOIN ItemMst im ON pr.itemMst.id=im.id INNER JOIN MtlStockIn msi ON im.id = msi.itemMst.id left outer join MaterialIssuSummary mis on mis.itemMst.id=msi.itemMst.id AND (mis.mtlIssuQty-mis.mtlIssuedQty) = 0   WHERE msi.remainQty>0 AND pm.poStatus=:poStatus and pm.poPostDate between :startDate and :endDate ORDER BY im.itemDtl";
		@SuppressWarnings("unchecked")
		List<Object[]> itemList = entityManager.createNativeQuery(sql).setParameter("poStatus", o)
				.setParameter("startDate", Date.valueOf(startDate)).setParameter("endDate", Date.valueOf(endDate))
				.getResultList();
		System.out.println("itemList on matrial mst=" + itemList.size());
		return itemList;
	}

	@Override
	public List<Object[]> getItemForProQrCode(String mouldId) {
		String n = "R";
		String m=Constants.FINAL.getValue();
		String m1=Constants.CONSUMABLE.getValue();
		String hql;
		List<Object[]> itemList = null;
		try {
		 hql = "SELECT im.`item_fr_name`,im.`item_mst_id` FROM mould_details md LEFT OUTER JOIN `item_mst` im ON md.`prod_code`=im.`item_mst_id` WHERE md.`mould_code`= :mouldId";
		 itemList = entityManager.createNativeQuery(hql).setParameter("mouldId", mouldId).getResultList();
		 
		if(itemList.size()==0) {
		// hql = "SELECT distinct im.itemDtl,im.id FROM ProOrd pm LEFT OUTER JOIN ItemMst im ON pm.itemMst.id=im.id WHERE pm.poStatus= :poStatus And (pm.orderType= :orderType or pm.orderType= :orderType1 )";
		 //itemList = entityManager.createQuery(hql).setParameter("poStatus", n).setParameter("orderType", m).setParameter("orderType1", m1).getResultList();
		/*hql="SELECT DISTINCT im.`item_detail`,im.`item_mst_id` FROM po_mst pm \r\n" + 
				"INNER JOIN po_row pr ON pr.`po_mst_id`=pm.`po_mst_id`\r\n" + 
				"INNER JOIN mtl_issu_summary mis ON mis.`item_mst_id`=pr.`item_mst_id`\r\n" + 
				"LEFT OUTER JOIN item_mst im ON pm.`item_mst_id`=im.`item_mst_id`\r\n" + 
				"WHERE pm.`po_status`= :poStatus AND (pm.`order_type`= :orderType OR pm.`order_type`= :orderType1 ) AND mis.`mtl_issued_qty`>0";*/
			hql="SELECT DISTINCT im.`item_fr_name`,im.`item_mst_id`,mip.`po_no`\r\n" + 
					"FROM `mtl_issu_po` mip \r\n" + 
					"INNER JOIN `po_mst` pm ON pm.`po_mst_id`=mip.`po_no`\r\n" + 
					"INNER JOIN po_row pr ON pr.`po_mst_id`=pm.`po_mst_id`\r\n" + 
					"INNER JOIN `mtl_issu_summary` mis ON mis.`mtl_issu_sum_id`=mip.`mtl_issu_sum_id`\r\n" + 
					"LEFT OUTER JOIN item_mst im ON pm.`item_mst_id`=im.`item_mst_id` \r\n" + 
					"WHERE pm.`po_status`= :poStatus AND (pm.`order_type`= :orderType OR pm.`order_type`= :orderType1 ) AND mis.`mtl_issued_qty`>0";
		itemList = entityManager.createNativeQuery(hql).setParameter("poStatus", n).setParameter("orderType", m).setParameter("orderType1", m1).getResultList();
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("itemList on matrial mst=" + itemList.size());
		return itemList;
	}

	@Override
	public ItemMst findById(String itemId) {
		ItemMst im=entityManager.find(ItemMst.class, itemId);
		return im;
	}

	@Override
	public List<ItemMst> getItemByCode(String materialCode) {
		// TODO Auto-generated method stub
		return entityManager.createQuery("FROM ItemMst WHERE id= :materialCode ",ItemMst.class).setParameter("materialCode", materialCode).getResultList();
	}

	@Override
	public void saveItem(ItemMst itemMst) {
		entityManager.persist(itemMst);
		entityManager.flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getItemList() {		
		return entityManager.createQuery("Select id, itemDtl, uom ,grpId ,materialSource,scmMode,matMstLog from ItemMst").getResultList();
	}

	@Override
	public List<ItemMst> getItemById(String itemMstId) {
		return entityManager.createQuery("from ItemMst where id=:itemMstId", ItemMst.class).setParameter("itemMstId", itemMstId).getResultList();
	}

	@Override
	public List<ItemMst> getEntireItemMst() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from ItemMst",ItemMst.class).getResultList();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (int) entityManager.createQuery("Select count(*)from ItemMst").getSingleResult();
	}

	@Override
	public List<ItemMst> getItemMstByPagination(int pageno, int perPage) {
		// TODO Auto-generated method stub
		long result = (long) entityManager.createQuery("SELECT count(a) FROM ItemMst a").getSingleResult();
		int total_count=(int) result;

		
		int firstR = total_count - (pageno * perPage);
		int maxR = total_count - ((pageno - 1) * perPage);
		

		if(firstR<0) {
			firstR=0;
		}
		String sql="Select * FROM `item_mst`";

		
		List<ItemMst> list = entityManager.createNativeQuery(sql,ItemMst.class).setFirstResult(firstR).setMaxResults(maxR).getResultList();

		return list;	}

	@Override
	public long materialStockCountBySearch(String search) {
		// TODO Auto-generated method stub
		
		
		System.out.println("SERACH BY PAGINATION CINT ");
		return (long) entityManager.createQuery("SELECT count(a) FROM ItemMst a where a.id LIKE :searchText  OR a.itemDtl LIKE :searchText").setParameter("searchText", "%"+search+"%").getSingleResult();
	}

	@Override
	public List<ItemMst> getItemMstByPagination(int pageno, int perPage, String search) {
		// TODO Auto-generated method stub
		long result = (long) entityManager.createQuery("SELECT count(a) FROM ItemMst a where a.id LIKE :searchText  OR a.itemDtl LIKE :searchText").setParameter("searchText", "%"+search+"%").getSingleResult();
		int total_count=(int) result;

		
		int firstR = total_count - (pageno * perPage);
		int maxR = total_count - ((pageno - 1) * perPage);
		

		if(firstR<0) {
			firstR=0;
		}
		String sql="Select * FROM `item_mst` where item_mst_id like :searchText OR item_detail like :searchText";

		
		List<ItemMst> list = entityManager.createNativeQuery(sql,ItemMst.class).setParameter("searchText", "%"+search+"%").setFirstResult(firstR).setMaxResults(maxR).getResultList();

		return list;
	}

}
