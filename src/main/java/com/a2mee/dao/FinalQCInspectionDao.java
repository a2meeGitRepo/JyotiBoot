package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.FinalQualityCheck;
import com.a2mee.model.Reasonmaster;

public interface FinalQCInspectionDao {

	public List<Object[]> getQualityCheck(String qrcode);

	public void finalQCInspectionSave(FinalQualityCheck qualityCheck);

	public List<Object[]> getMainLineFinalInspectionList(String itemName);

	public void updateQCinputTableByqrCode(double remingQty, String qrcode);

	public List<String> getItemIdForQuarantine(long qcInputId);

	public List<Object[]> getFinalQualityCheckItemList();

	public List<FinalQualityCheck> updateFinalQCFromQuarantine(String qrcode);

	public List<String> findQcInputSource(String qrcode);

	public List getQualityCheckForAssembliyQrCode(String qrcode);
	
	public List<Object[]> getRemaingQtyByqrcodeNItemId(String qrcode);
	
	public List<Object[]> getMachineTimeByQrCode(String qrcode);
	
	public Reasonmaster getReasonMstById(long reasonId);
	

}
