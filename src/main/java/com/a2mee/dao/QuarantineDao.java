package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.Quarantine;
import com.a2mee.model.QuarantineInspectionSummary;

public interface QuarantineDao {

	public Quarantine addQuarantineORHoldItems(Quarantine quarantineS);
	
	public List<Object[]> getAllQuarantineHoldList();
	
	public List<Object[]> getQuarantineList(String itemId, String stageName);
	
	public List<Object[]> scanQrcodeAtQuarantineRecord(String qrcode);
	
	public boolean saveQuarantineSummaryData(QuarantineInspectionSummary quarantineInspectionSummary);
	
	public List<Quarantine> validateQurantineListData(String itemMstId, String qrcode, String stage);
	
	public void addQuarantineSummaryData(QuarantineInspectionSummary quarantineInspectionSummary) ;
	
	public Quarantine getQuarantineListById(long quarId);
	
	public List<Object[]> getAllQuarantineList();
	
	public List<Object[]> getDataFromPoshiftQrCodeTable(String qrCode);
}
