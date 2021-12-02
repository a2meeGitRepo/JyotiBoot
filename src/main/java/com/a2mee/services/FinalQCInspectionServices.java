package com.a2mee.services;

import java.util.List;

import com.a2mee.model.dto.FinalQCDto;
import com.a2mee.model.dto.ItemMstDto;
import com.a2mee.model.FinalQualityCheck;

public interface FinalQCInspectionServices {

	public FinalQCDto scanQrcode(String qrcode);

	public double finalQCInspectionSave(FinalQCDto qualityCheckDto);
	
	public List<FinalQCDto> getMainLineFinalInspectionList(String itemName);

	public List<ItemMstDto> getFinalQualityCheckItemList();
	
	public List<FinalQualityCheck> updateFinalQCFromQuarantine(String qrcode);
	
	public List<FinalQCDto> getRemaingQtyByqrcodeNItemId(String qrcode);
	
	public List<String> getMachineTimeByQrCode(String qrcode);
	
	public void rejectedResonSendToIot(String qrcode, long rejectedResonMst );
}
