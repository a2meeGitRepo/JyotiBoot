package com.a2mee.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.a2mee.model.ItemMst;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.StorageBinMst;

@Repository
public interface MtlStockInDao extends JpaRepository<MtlStockIn, Long>, MtlStockInDaoCustom{

	@Query("from MtlStockIn where itemMst=?1 and materialInspect is null")
	MtlStockIn getByItem(ItemMst item);

	@Query("from MtlStockIn where itemMst.id=?1")
	List<MtlStockIn> getAvailableStockByComp(String compCode);
	
	@Query("from MtlStockIn where itemMst=?1 and storageBinCode=?2")
	MtlStockIn getByItemAndStorage(ItemMst item, String storageBinCode);

	@Query("from MtlStockIn where itemMst.id=?1 AND grnDate is null")
	List<MtlStockIn> getStockListByItem(String itemMstId);

	@Query("from MtlStockIn where itemMst.id=?1 AND grnDate is null AND  DATE(createDate) BETWEEN DATE(?2) AND DATE(?3)")
	List<MtlStockIn> getStockListByItemDate(String itemMstId, String startDate, String endDate);
	
	@Query("from MtlStockIn where itemMst.id=?1 AND createDate is null")
	List<MtlStockIn> getGrnListByItem(String itemMstId);

	@Query("from MtlStockIn where itemMst.id=?1 AND createDate is null AND  DATE(createDate) BETWEEN DATE(?2) AND DATE(?3)")
	List<MtlStockIn> getGrnListByItemDate(String itemMstId, String startDate, String endDate);
	
	
	@Query("from MtlStockIn where itemMst.id=?1")
	List<MtlStockIn> getmtlStockInByItem(String itemMstId);
	
	@Query("from MtlStockIn where itemMst.id=?1 and storageBinCode=?2")
	List<MtlStockIn> getAvailableStockByCompAndBinCode(String compCode, String location);
	
	@Query("from MtlStockIn where itemMst.id=?1 and storageBinCode=?2")
	Optional<MtlStockIn> getAvailableStockByCompAndLocation(String compCode, String location);
	
	
	@Query("select SUM(remainQty) from MtlStockIn m where m.itemMst.id=?1")
	String getTotalStockByItem(String id);
	
	@Query("select count(m) from MtlStockIn m")
	int materialLocationWiseStockCount();

	
	@Query("select count(*) from MtlStockIn e where e.itemMst.id  LIKE %:searchText% OR e.itemMst.itemDtl  LIKE %:searchText% OR e.storageBinCode LIKE %:searchText%")
	int materialLocationWiseStockSearchCount(@Param("searchText") String searchText);


	
	
	
	
	


	
	
	
}
