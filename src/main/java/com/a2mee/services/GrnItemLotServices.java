package com.a2mee.services;

import java.util.List;

import com.a2mee.model.dto.MaterialInspDto;
import com.a2mee.model.dto.PoMaterialListDto;
import com.a2mee.model.GrnItemLot;
import com.a2mee.model.MaterialInspect;

public interface GrnItemLotServices {

	public Integer updateQrCode(long grItmlotId,String qrCodeNo);
	
//	public List<PoMaterialListDto> saveMaterialSumm(MaterialInspDto materialInspDto);
//	public List<PoMaterialListDto> getDatabyQrScan(String qrCode);
	public PoMaterialListDto saveMaterialSumm(MaterialInspDto materialInspDto);
	public PoMaterialListDto getDatabyQrScan(String qrCode);

	public GrnItemLot saveGrnItemLot(GrnItemLot theGrnItemLot);


	public void saveStockInGorGreenChanl(MaterialInspDto materialInspDto, MaterialInspect m);

}
