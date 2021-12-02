package com.a2mee.services;

import java.util.List;
import java.util.Map;

import com.a2mee.model.dto.QuarantineDto;
import com.a2mee.model.dto.QuarantineInspectionDto;
import com.a2mee.model.Quarantine;

public interface QuarantineServices {

	public Quarantine addQuarantineORHoldItems(Quarantine quarantineS);
	
	public List<QuarantineDto> getAllQuarantineHoldList();
	
	public List<QuarantineDto> getQuarantineList(String itemId, String stageName);

	public List<QuarantineDto> scanQrcodeAtQuarantineRecord(String qrcode);
	
	public double saveQuarantineSummaryData(QuarantineInspectionDto quarantineInspectionDto);
	
	public List<Quarantine> validateQurantineListData(String itemMstId, String qrcode, String stage);
	
	public void addQuarantineSummaryData(double holdQty, Quarantine qua);

	public List<QuarantineDto> getAllQuarantineList();
	
	public void addAllQuarantineHoldData(String itemId, String qrcode, double holdQty, String stage);
	
}
