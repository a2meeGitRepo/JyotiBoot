package com.a2mee.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.FinalQCInspectionDao;
import com.a2mee.dao.GrnItemLotDao;
import com.a2mee.dao.MaterialInspectDao;
import com.a2mee.dao.QuarantineDao;
import com.a2mee.model.dto.MaterialInspDto;
import com.a2mee.model.dto.QuarantineDto;
import com.a2mee.model.dto.QuarantineInspectionDto;
import com.a2mee.model.FinalQualityCheck;
import com.a2mee.model.GrnItemLot;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.Quarantine;
import com.a2mee.model.QuarantineInspectionSummary;
import com.a2mee.model.RejectReasonMst;
import com.a2mee.services.FinalQCInspectionServices;
import com.a2mee.services.MtlStockInServices;
import com.a2mee.services.QuarantineServices;
import com.a2mee.util.Constants;
import com.a2mee.util.CustomLogger;

@Service
public class QuarantineServicesImpl implements QuarantineServices {	

	@Autowired
	FinalQCInspectionServices finalQCInspectionService;

	@Autowired
	QuarantineServices quarantineServices;
	
	@Autowired
	MtlStockInServices inServices;
	
	@Autowired
	QuarantineDao quarantineDao;

	@Autowired
	FinalQCInspectionDao finalQCInspectionDao;

	@Autowired
	MaterialInspectDao materialInspectDao;

	@Autowired
	GrnItemLotDao grnItemLotDao;

	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Quarantine addQuarantineORHoldItems(Quarantine quarantineS) {
		Quarantine quar = quarantineDao.addQuarantineORHoldItems(quarantineS);
		return quar;
	}

	@Override
	public List<QuarantineDto> getAllQuarantineHoldList() {
		List<Object[]> list = quarantineDao.getAllQuarantineHoldList();
		List<QuarantineDto> qDto = new ArrayList<>();
		list.forEach(arr -> {
			QuarantineDto qua = new QuarantineDto();
			qua.setItemMasterId((String) arr[1]);
			qua.setItemMstDetails((String) arr[0]);
			qDto.add(qua);
		});
		return qDto;
	}

	@Override
	public List<QuarantineDto> getQuarantineList(String itemId, String stageName) {
		List<Object[]> list = quarantineDao.getQuarantineList(itemId, stageName);
		List<QuarantineDto> qDto = new ArrayList<>();
		list.forEach(arr -> {
			double quarHoldQTY = ((double) (arr[2]));
			if (quarHoldQTY > 0) {
				QuarantineDto qudto = new QuarantineDto();
				qudto.setItemMasterId((String) arr[0]);
				qudto.setQrcode((String) arr[1]);
				qudto.setHoldQty((long) quarHoldQTY);
				qudto.setUom("KG");
				qudto.setStage((String) arr[4]);
				qDto.add(qudto);
			}
		});
		return qDto;
	}

	@Override
	public List<QuarantineDto> scanQrcodeAtQuarantineRecord(String qrcode) {
		List<Object[]> list = quarantineDao.scanQrcodeAtQuarantineRecord(qrcode);
		List<QuarantineDto> quDtoList = new ArrayList<>();
		list.forEach(arr -> {
			if (arr[0] != null) {
				QuarantineDto qd = new QuarantineDto();
				double QTY = ((double) (arr[0]));
				qd.setHoldQty(QTY);
				qd.setQrcode((String) arr[1]);
				qd.setStage((String) arr[2]);
				Integer i = new Integer((int) arr[3]);
				qd.setQuar_id(i.longValue());
				quDtoList.add(qd);
			} else {
				quDtoList.add(null);
			}
		});

		return quDtoList;
	}

	@Override
	public double saveQuarantineSummaryData(QuarantineInspectionDto quarantineInspectionDto) {

		String qrcode = null;
		if (quarantineInspectionDto.getAcceptQty() != 0 /*||quarantineInspectionDto.getRejectQty() != 0 */) {
			QuarantineInspectionSummary quInSummy = new QuarantineInspectionSummary();
			quInSummy.setHoldQty(-quarantineInspectionDto.getAcceptQty());
			quInSummy.setQuarInspectQty(quarantineInspectionDto.getAcceptQty());
			quInSummy.setStatus(Constants.MATERIAL_ACCEPTE_STATUS.getValue());
			Quarantine quar = new Quarantine();
			quar.setQurtId(quarantineInspectionDto.getQuarantineId());
			quInSummy.setQuarantine(quar);
			quInSummy.setOperator(quarantineInspectionDto.getOperator());
			quarantineDao.saveQuarantineSummaryData(quInSummy);
			Quarantine quarantine = quarantineDao.getQuarantineListById(quarantineInspectionDto.getQuarantineId());
			// Above Function Call QualityCheck

			if (quarantine.getStageName().equals("QC")) {
				qrcode = qualityCheck(quarantineInspectionDto, quarantine);
			} else if (quarantine.getStageName().equals("GRN")) {
				qrcode = materialInspection(quarantineInspectionDto, quarantine);
			} else if (quarantine.getStageName().equals("AOI")) {
				qrcode = AssemblyOnlineInsepction(quarantineInspectionDto, quarantine);
			}
		}//nik work on suraj code
		else{
			Quarantine quarantine = quarantineDao.getQuarantineListById(quarantineInspectionDto.getQuarantineId());
			// Above Function Call QualityCheck

			if (quarantine.getStageName().equals("QC")) {
				qrcode = qualityCheck(quarantineInspectionDto, quarantine);
			} else if (quarantine.getStageName().equals("GRN")) {
				qrcode = materialInspection(quarantineInspectionDto, quarantine);
			} else if (quarantine.getStageName().equals("AOI")) {
				qrcode = AssemblyOnlineInsepction(quarantineInspectionDto, quarantine);
			}
			
		}
		List<QuarantineDto> list = scanQrcodeAtQuarantineRecord(qrcode);
		double qty = 0.00;
		for (QuarantineDto q : list) {
			qty = q.getHoldQty();
		}
		return qty;
	}

	public String AssemblyOnlineInsepction(QuarantineInspectionDto quarantineInspectionDto, Quarantine quarantine) {
		RejectReasonMst rrM = new RejectReasonMst();

		if (quarantineInspectionDto.getRejectQty() != 0) {
			QuarantineInspectionSummary quInSummy1 = new QuarantineInspectionSummary();
			quInSummy1.setHoldQty(-quarantineInspectionDto.getRejectQty());
			quInSummy1.setQuarInspectQty(quarantineInspectionDto.getRejectQty());
			quInSummy1.setStatus(Constants.MATERIAL_REJECT_STATUS.getValue());
			rrM.setRejId(quarantineInspectionDto.getRejectReason());
			quInSummy1.setRejectReasonMst(rrM);
			Quarantine quar1 = new Quarantine();
			quar1.setQurtId(quarantineInspectionDto.getQuarantineId());
			quInSummy1.setQuarantine(quar1);
			quInSummy1.setOperator(quarantineInspectionDto.getOperator());
			quarantineDao.saveQuarantineSummaryData(quInSummy1);
			// qualityCheckServices.rejectedResonSendToIot(qrcode,
			// quarantineInspectionDto.getRejectReason());
		}
		           /*acepted qty send to FG*/
		/*else {
			List<Object[]>list=quarantineDao.getDataFromPoshiftQrCodeTable(quarantine.getQrcode());
		
			String itemId = null ;
			String itemName = null;
			long poNo = 0;
			for(Object[] obj:list) {
				poNo=(long)obj[0];
				itemId=(String)obj[1];
				itemName=(String)obj[2];
			}
			
			FinishedGoods finishedGoods = new FinishedGoods();
			finishedGoods.setItemId(itemId);
			finishedGoods.setItemName(itemName);
			finishedGoods.setBoxQrcode(quarantine.getQrcode());
			finishedGoods.setItemQuantity(quarantineInspectionDto.getAcceptQty());
			finishedGoods.setPoMstId(poNo);
			finishedGoods.setStatus("M");
			finishedGoods.setTimestamp(LocalDate.now());
			
			
		}*/

		return quarantine.getQrcode();
	}

	public String materialInspection(QuarantineInspectionDto quarantineInspectionDto, Quarantine quarantine) {
		RejectReasonMst rrM = new RejectReasonMst();
		String qrcode = null;
		GrnItemLot grnitemLotDataObject = null;
		List<Object[]> obj = materialInspectDao.updateQuarantineDataIntoMaterialInspection(quarantine.getQrcode());
		for (Object[] o : obj) {
			MaterialInspect materialInspection;
			if (quarantineInspectionDto.getAcceptQty() != 0) {
				MaterialInspect mi = new MaterialInspect();
				GrnItemLot gil = new GrnItemLot();
				Integer i = new Integer((int) o[0]);
				gil.setGrnItemLotId(i.longValue());
				mi.setGrnItemLot(gil);
				mi.setMtlQty(-quarantineInspectionDto.getAcceptQty());
				mi.setMtlStatus(Constants.MATERIAL_HOLD_STATUS.getValue());
				qrcode = (String) o[1];
				mi.setOriginalStatus(Constants.HOLD.getValue());
				materialInspection = materialInspectDao.saveInspMterial(mi);

				MaterialInspect mi1 = new MaterialInspect();
				GrnItemLot gil1 = new GrnItemLot();
				Integer i1 = new Integer((int) o[0]);
				gil1.setGrnItemLotId(i1.longValue());
				mi1.setGrnItemLot(gil1);
				mi1.setMtlQty(quarantineInspectionDto.getAcceptQty());
				mi1.setMtlStatus(Constants.MATERIAL_ACCEPTE_STATUS.getValue());
				qrcode = (String) o[1];
				mi1.setOriginalStatus(Constants.HOLD.getValue());
				mi1.setDate(LocalDate.now());
				materialInspection = materialInspectDao.saveInspMterial(mi1);
				/* |-----------------update stockIn and item Stock -------------| */
				MaterialInspDto materialInspDto = new MaterialInspDto();
				materialInspDto.setItmLotId(materialInspection.getGrnItemLot().getGrnItemLotId());
				materialInspDto.setAcceptqty(quarantineInspectionDto.getAcceptQty());
				inServices.saveStock(materialInspDto, materialInspection);
				grnitemLotDataObject = grnItemLotDao
						.getElementById(materialInspection.getGrnItemLot().getGrnItemLotId());
				double qty = 0, qty1 = 0;
				qty = grnitemLotDataObject.getApproQty() + quarantineInspectionDto.getAcceptQty();
				grnItemLotDao.updateInspetSumm(grnitemLotDataObject.getGrnItemLotId(), qty,
						Constants.MATERIAL_ACCEPTE_STATUS.getValue());

				qty1 = grnitemLotDataObject.getHoldQty() - quarantineInspectionDto.getAcceptQty();
				grnItemLotDao.updateInspetSumm(grnitemLotDataObject.getGrnItemLotId(), qty1,
						Constants.MATERIAL_HOLD_STATUS.getValue());
			}
			if (quarantineInspectionDto.getRejectQty() != 0) {
				
				
				double qty = 0, qty1 = 0;
				MaterialInspect mi2 = new MaterialInspect();
				GrnItemLot gil2 = new GrnItemLot();
				Integer i2 = new Integer((int) o[0]);
				gil2.setGrnItemLotId(i2.longValue());
				mi2.setGrnItemLot(gil2);
				mi2.setMtlQty(quarantineInspectionDto.getRejectQty());
				mi2.setMtlStatus(Constants.MATERIAL_REJECT_STATUS.getValue());
				qrcode = (String) o[1];
				rrM.setRejId(quarantineInspectionDto.getRejectReason());
				mi2.setRejectReasonMst(rrM);
				mi2.setOriginalStatus(Constants.HOLD.getValue());
				mi2.setDate(LocalDate.now());
				materialInspection = materialInspectDao.saveInspMterial(mi2);

				MaterialInspect mi3 = new MaterialInspect();
				GrnItemLot gil3 = new GrnItemLot();
				Integer i3 = new Integer((int) o[0]);
				gil3.setGrnItemLotId(i3.longValue());
				mi3.setGrnItemLot(gil3);
				mi3.setMtlQty(-quarantineInspectionDto.getRejectQty());
				mi3.setMtlStatus(Constants.MATERIAL_HOLD_STATUS.getValue());
				qrcode = (String) o[1];
				rrM.setRejId(quarantineInspectionDto.getRejectReason());
				mi3.setRejectReasonMst(rrM);
				mi3.setOriginalStatus(Constants.HOLD.getValue());
				mi3.setDate(LocalDate.now());
				materialInspection = materialInspectDao.saveInspMterial(mi3);
				grnitemLotDataObject = grnItemLotDao
						.getElementById(materialInspection.getGrnItemLot().getGrnItemLotId());
				qty = grnitemLotDataObject.getRejQty() + quarantineInspectionDto.getRejectQty();
				grnItemLotDao.updateInspetSumm(grnitemLotDataObject.getGrnItemLotId(), qty,
						Constants.MATERIAL_REJECT_STATUS.getValue());

				qty1 = grnitemLotDataObject.getHoldQty() - quarantineInspectionDto.getRejectQty();
				grnItemLotDao.updateInspetSumm(grnitemLotDataObject.getGrnItemLotId(), qty1,
						Constants.MATERIAL_HOLD_STATUS.getValue());

				QuarantineInspectionSummary quInSummy1 = new QuarantineInspectionSummary();
				quInSummy1.setHoldQty(-quarantineInspectionDto.getRejectQty());
				quInSummy1.setQuarInspectQty(quarantineInspectionDto.getRejectQty());
				quInSummy1.setStatus(Constants.MATERIAL_REJECT_STATUS.getValue());
				rrM.setRejId(quarantineInspectionDto.getRejectReason());
				quInSummy1.setRejectReasonMst(rrM);
				Quarantine quar1 = new Quarantine();
				quar1.setQurtId(quarantineInspectionDto.getQuarantineId());
				quInSummy1.setQuarantine(quar1);
				quInSummy1.setOperator(quarantineInspectionDto.getOperator());
				quarantineDao.saveQuarantineSummaryData(quInSummy1);
				// qualityCheckServices.rejectedResonSendToIot(qrcode,
				// quarantineInspectionDto.getRejectReason());
			}
		}

		return qrcode;
	}

	public String qualityCheck(QuarantineInspectionDto quarantineInspectionDto, Quarantine quarantine) {
		RejectReasonMst rrM = new RejectReasonMst();
		String qrcode = null;
		if (quarantine.getStageName().equals("QC")) {
			List<FinalQualityCheck> finalqcList = finalQCInspectionService
					.updateFinalQCFromQuarantine(quarantine.getQrcode());
			for (FinalQualityCheck fqc : finalqcList) {

				if (quarantineInspectionDto.getAcceptQty() != 0) {
					FinalQualityCheck fqcNewObj = new FinalQualityCheck();
					// fqc.setQualityCheckId(0);
					qrcode = fqc.getQrcode();
					fqcNewObj.setqCInput(fqc.getqCInput());
					fqcNewObj.setQrcode(fqc.getQrcode());
					fqcNewObj.setActualQty(fqc.getActualQty());
					fqcNewObj.setDateOfInspection(fqc.getDateOfInspection());
					fqcNewObj.setReason(null);
					fqcNewObj.setOperator(fqc.getOperator());
					fqcNewObj.setQuantity(-quarantineInspectionDto.getAcceptQty());
					fqcNewObj.setStatus(Constants.MATERIAL_HOLD_STATUS.getValue());
					fqcNewObj.setRejectReasonMst(null);
					finalQCInspectionDao.finalQCInspectionSave(fqcNewObj);

					FinalQualityCheck fqcNewObj1 = new FinalQualityCheck();
					fqcNewObj1.setqCInput(fqc.getqCInput());
					fqcNewObj1.setQrcode(fqc.getQrcode());
					fqcNewObj1.setActualQty(fqc.getActualQty());
					fqcNewObj1.setDateOfInspection(fqc.getDateOfInspection());
					fqcNewObj1.setReason(null);
					fqcNewObj1.setOperator(fqc.getOperator());
					fqcNewObj1.setQuantity(quarantineInspectionDto.getAcceptQty());
					fqcNewObj1.setStatus(Constants.MATERIAL_ACCEPTE_STATUS.getValue());
					fqcNewObj1.setRejectReasonMst(null);
					finalQCInspectionDao.finalQCInspectionSave(fqcNewObj1);
				}
				if (quarantineInspectionDto.getRejectQty() != 0) {
					qrcode = fqc.getQrcode();
					QuarantineInspectionSummary quInSummy1 = new QuarantineInspectionSummary();
					quInSummy1.setHoldQty(-quarantineInspectionDto.getRejectQty());
					quInSummy1.setQuarInspectQty(quarantineInspectionDto.getRejectQty());
					quInSummy1.setStatus(Constants.MATERIAL_REJECT_STATUS.getValue());
					rrM.setRejId(quarantineInspectionDto.getRejectReason());
					quInSummy1.setRejectReasonMst(rrM);
					Quarantine quar1 = new Quarantine();
					quar1.setQurtId(quarantineInspectionDto.getQuarantineId());
					quInSummy1.setQuarantine(quar1);
					quInSummy1.setOperator(quarantineInspectionDto.getOperator());
					quarantineDao.saveQuarantineSummaryData(quInSummy1);

					FinalQualityCheck fqcNewObj2 = new FinalQualityCheck();
					fqcNewObj2.setqCInput(fqc.getqCInput());
					fqcNewObj2.setQrcode(fqc.getQrcode());
					fqcNewObj2.setActualQty(fqc.getActualQty());
					fqcNewObj2.setDateOfInspection(fqc.getDateOfInspection());
					fqcNewObj2.setReason(null);
					fqcNewObj2.setOperator(fqc.getOperator());
					fqcNewObj2.setQuantity(-quarantineInspectionDto.getRejectQty() );
					fqcNewObj2.setStatus(Constants.MATERIAL_HOLD_STATUS.getValue());
					fqcNewObj2.setRejectReasonMst(rrM);
					finalQCInspectionDao.finalQCInspectionSave(fqcNewObj2);
					
					List<String> sourceMorA = finalQCInspectionDao.findQcInputSource(qrcode);
					if (sourceMorA.size() > 0) {
						if (sourceMorA.get(0).equals(Constants.MOLD.getValue())) {
							finalQCInspectionService.rejectedResonSendToIot(fqc.getQrcode(),
									quarantineInspectionDto.getRejectReason());
						}
					}
				}
			}
		}
		return qrcode;
	}

	@Override
	public List<Quarantine> validateQurantineListData(String itemMstId, String qrcode, String stage) {
		List<Quarantine> listData = quarantineDao.validateQurantineListData(itemMstId, qrcode, stage);
		return listData;
	}

	@Override
	public void addQuarantineSummaryData(double holdQty, Quarantine qua) {
		QuarantineInspectionSummary qis = new QuarantineInspectionSummary();
		qis.setHoldQty(holdQty);
		// qis.setOperator(quarantine.getOperator());
		qis.setQuarantine(qua);
		qis.setStatus(Constants.MATERIAL_HOLD_STATUS.getValue());
		quarantineDao.addQuarantineSummaryData(qis);
	}

	@Override
	public List<QuarantineDto> getAllQuarantineList() {
		List<Object[]> quarantineAllList = quarantineDao.getAllQuarantineList();
		List<QuarantineDto> quarantineAllListDto = new ArrayList<>();
		quarantineAllList.forEach(arr -> {
			QuarantineDto quarantineDto = new QuarantineDto();
			double holdQTY = ((double) arr[2]);
			if (holdQTY > 0) {
				quarantineDto.setStage((String) arr[0]);
				quarantineDto.setQrcode((String) arr[1]);
				quarantineDto.setHoldQty(holdQTY);
				quarantineDto.setUom((String) arr[3]);
				quarantineAllListDto.add(quarantineDto);
			}
		});
		return quarantineAllListDto;
	}

	@Override
	public void addAllQuarantineHoldData(String itemId, String qrcode, double holdQty, String stage) {
		ItemMst itemst = new ItemMst();
		Quarantine quar = new Quarantine();
		itemst.setId(itemId);
		quar.setItemMst(itemst);
		quar.setQrcode(qrcode);
		quar.setUom(Constants.UOM.getValue());
		quar.setStageName(stage);
		List<Quarantine> listData = quarantineServices.validateQurantineListData(itemId, qrcode, stage);
		if (listData.size() == 0 || listData == null) {
			Quarantine qua = quarantineServices.addQuarantineORHoldItems(quar);
			quarantineServices.addQuarantineSummaryData(holdQty, qua);

		}
		if (listData.size() != 0 || listData != null) {
			CustomLogger.log.info("else part");
			for (Quarantine quartine : listData) {
				quartine.getQurtId();
				quarantineServices.addQuarantineSummaryData(holdQty, quartine);
			}
		}
	}

}
