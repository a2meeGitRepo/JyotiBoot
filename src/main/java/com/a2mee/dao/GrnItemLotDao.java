package com.a2mee.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a2mee.model.GrnItemLot;

public interface GrnItemLotDao  {
	
	public Integer updateQrCode(long grItmlotId,String qrCodeNo);
	public Integer updateInspetSumm(long grItmlotId, double  qty, String status);
	public GrnItemLot getElementById(long grItmlotId );
	public List<Object[]> getDatabtQrcode(String qrCode);
	
	public List<Object[]> findItemUseingGrnLotId(long grnLotId);
	public GrnItemLot saveGrnItemLot(GrnItemLot theGrnItemLot);
	

}
