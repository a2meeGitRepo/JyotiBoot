package com.a2mee.services.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.a2mee.dao.FinalQCInspectionDao;
import com.a2mee.model.dto.FinalQCDto;
import com.a2mee.model.dto.ItemMstDto;
import com.a2mee.model.CustomRuntimeException;
import com.a2mee.model.FinalQualityCheck;
import com.a2mee.model.ItemMst;
import com.a2mee.model.QCInput;
import com.a2mee.model.Reasonmaster;
import com.a2mee.model.RejectReasonMst;
import com.a2mee.services.FinalQCInspectionServices;
import com.a2mee.services.QuarantineServices;
import com.a2mee.util.Constants;
import com.a2mee.util.CustomLogger;
import com.a2mee.util.GlobalUtils;
import com.a2mee.util.SendDataToIot;

@Service
public class FinalQCInspectionServicesImpl implements FinalQCInspectionServices {

	@Autowired
	FinalQCInspectionDao qualityCheckDao;

	@Autowired
	QuarantineServices quarantineServices;

	public final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public FinalQCDto scanQrcode(String qrcode) {

		List<String> sourceMorA = qualityCheckDao.findQcInputSource(qrcode);
		if (sourceMorA.size() > 0) {
			if (sourceMorA.get(0).equals(Constants.MOLD.getValue())) {
				CustomLogger.log.info("Molding");
				List<Object[]> list = qualityCheckDao.getQualityCheck(qrcode);
				FinalQCDto qualityCheckDto = new FinalQCDto();
				list.forEach(arr -> {
					double poQTY = ((BigDecimal) (arr[0])).doubleValue();
					qualityCheckDto.setPoActualQty(poQTY);
					qualityCheckDto.setQcInputId((int) arr[1]);
					qualityCheckDto.setItemMstId((String) arr[2]);
					qualityCheckDto.setRemaingQty((double) arr[3]);
					qualityCheckDto.setSource(Constants.MOLD.getValue());
				});
				return qualityCheckDto;
			}
			if (sourceMorA.get(0).equals(Constants.ASSEMBLING.getValue()) && sourceMorA.size() >= 0) {
				CustomLogger.log.info("Assembly...");
				List<Object[]> list = qualityCheckDao.getQualityCheckForAssembliyQrCode(qrcode);
				FinalQCDto qualityCheckDto = new FinalQCDto();
				list.forEach(arr -> {
					// double poQTY = ((BigDecimal) (arr[0])).doubleValue();

					qualityCheckDto.setPoActualQty((double) arr[0]);
					qualityCheckDto.setQcInputId((int) arr[1]);
					qualityCheckDto.setItemMstId((String) arr[2]);
					qualityCheckDto.setRemaingQty((double) arr[3]);
					qualityCheckDto.setSource(Constants.ASSEMBLING.getValue());
				});
				return qualityCheckDto;
			} else {
				CustomLogger.log.info("Valid source not found......!");
				throw new CustomRuntimeException("not Found....");
			}
		}
		return null;
	}

	@Override
	public double finalQCInspectionSave(FinalQCDto qualityCheckDto) {

		try {
			long totalQty = qualityCheckDto.getAcceptQty() + qualityCheckDto.getRejectQty()
					+ qualityCheckDto.getHoldQty();
			// Accept quantity
			if (qualityCheckDto.getAcceptQty() != 0) {
				FinalQualityCheck qc = new FinalQualityCheck();
				QCInput qcInput = new QCInput();
				qcInput.setQcID(qualityCheckDto.getQcInputId());
				qc.setqCInput(qcInput);
				qc.setQrcode(qualityCheckDto.getQrcode());
				qc.setActualQty(qualityCheckDto.getPoActualQty());
				qc.setDateOfInspection(LocalDate.now());
				qc.setOperator(qualityCheckDto.getOperator());
				qc.setQuantity(qualityCheckDto.getAcceptQty());
				qc.setStatus(Constants.MATERIAL_ACCEPTE_STATUS.getValue());
				qc.setFlag(qualityCheckDto.getFlag());
				qualityCheckDao.finalQCInspectionSave(qc);
			}

			// Rejected quantity
			if (qualityCheckDto.getRejectQty() != 0 && qualityCheckDto.getRejectReasonMst() != 0) {
				RejectReasonMst rrm = new RejectReasonMst();
				rrm.setRejId(qualityCheckDto.getRejectReasonMst());
				FinalQualityCheck qc1 = new FinalQualityCheck();
				QCInput qcInput1 = new QCInput();
				qcInput1.setQcID(qualityCheckDto.getQcInputId());
				qc1.setqCInput(qcInput1);
				qc1.setActualQty(qualityCheckDto.getPoActualQty());
				qc1.setDateOfInspection(LocalDate.now());
				qc1.setOperator(qualityCheckDto.getOperator());
				qc1.setQrcode(qualityCheckDto.getQrcode());
				qc1.setQuantity(qualityCheckDto.getRejectQty());
				qc1.setStatus(Constants.MATERIAL_REJECT_STATUS.getValue());
				qc1.setRejectReasonMst(rrm);
				qc1.setFlag(qualityCheckDto.getFlag());
				qualityCheckDao.finalQCInspectionSave(qc1);
				
				List<String> sourceMorA = qualityCheckDao.findQcInputSource(qualityCheckDto.getQrcode());
				if (sourceMorA.size() > 0) {
					if (sourceMorA.get(0).equals(Constants.MOLD.getValue())) {
				rejectedResonSendToIot(qualityCheckDto.getQrcode(), qualityCheckDto.getRejectReasonMst());
					}
				}
				
			}

			// Holed quantity
			if (qualityCheckDto.getHoldQty() != 0) {
				FinalQualityCheck qc2 = new FinalQualityCheck();
				QCInput qcInput2 = new QCInput();
				qcInput2.setQcID(qualityCheckDto.getQcInputId());
				qc2.setqCInput(qcInput2);
				qc2.setActualQty(qualityCheckDto.getPoActualQty());
				qc2.setDateOfInspection(LocalDate.now());
				qc2.setOperator(qualityCheckDto.getOperator());
				qc2.setQrcode(qualityCheckDto.getQrcode());
				qc2.setQuantity(qualityCheckDto.getHoldQty());
				qc2.setStatus(Constants.MATERIAL_HOLD_STATUS.getValue());
				qc2.setReason(qualityCheckDto.getHoldReason());
				qc2.setFlag(qualityCheckDto.getFlag());
				qualityCheckDao.finalQCInspectionSave(qc2);
				List<String> qcList = qualityCheckDao.getItemIdForQuarantine(qualityCheckDto.getQcInputId());
				// Quarantine method
				try {
					quarantineServices.addAllQuarantineHoldData(qcList.get(0), qualityCheckDto.getQrcode(),
							qualityCheckDto.getHoldQty(), "QC");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return updateSaveQCInputTableByQrcode(qualityCheckDto.getItemMstId(), totalQty,
					qualityCheckDto.getQrcode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	@Override
	public void rejectedResonSendToIot(String qrcode, long rejectedResonMst ) {
		try {
			List<String> dateTimeList = getMachineTimeByQrCode(qrcode);
			String datetime = dateTimeList.get(0);
			CustomLogger.log.info("IOT TIME ===>" + datetime);
			Reasonmaster reason = qualityCheckDao.getReasonMstById(rejectedResonMst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double updateSaveQCInputTableByQrcode(String itemName, long totalQ, String qrcode) {
		CustomLogger.log.info("Signature" + itemName + "" + totalQ + "" + qrcode);
		List<FinalQCDto> list = getRemaingQtyByqrcodeNItemId(qrcode);
		double actualQty = 0, remaingQty = 0;
		double finalTo = 0;
		for (FinalQCDto fqcD : list) {
			// actualQty = fqcD.getPoActualQty();
			remaingQty = fqcD.getRemaingQty();

			log.info("^^^^^^^^^^^^^^^^" + actualQty);
		}
		if (remaingQty >= 0) {
			finalTo = remaingQty - totalQ;
			qualityCheckDao.updateQCinputTableByqrCode(finalTo, qrcode);
		}
		CustomLogger.log.info("finalTo" + finalTo);
		return finalTo;
	}

	@Override
	public List<FinalQCDto> getMainLineFinalInspectionList(String itemName) {
		List<Object[]> finalQCList = qualityCheckDao.getMainLineFinalInspectionList(itemName);

		FinalQCDto fqcd = null;
		TreeMap<String, FinalQCDto> tm = new TreeMap<String, FinalQCDto>();
		for (Object[] arr : finalQCList) {

			FinalQCDto existingObj = null;
			try {
				existingObj = tm.get((String) arr[2]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (existingObj == null) {

				fqcd = new FinalQCDto();
				fqcd.setItemMstId((String) arr[0]);
				fqcd.setItemDetails((String) arr[1]);
				fqcd.setQrcode((String) arr[2]);
				fqcd.setPoActualQty((double) arr[3]);
				fqcd.setRemaingQty((double) arr[4]);
				Integer i = new Integer((int) arr[8]);
				fqcd.setQcInputId(i.longValue());

				String status = (String) arr[5];
				double qty = 0;
				try {
					qty = ((double) (arr[6]));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CustomLogger.log.info("@@@>>>>>>>>>>>>>>" + qty);
				if (qty > 0.0) {
					if (status.equals("A")) {
						fqcd.setAcceptQty((long) qty);
					} else if (status.equals("R")) {
						fqcd.setRejectQty((long) qty);
					} else {
						fqcd.setHoldQty((long) qty);
					}
					tm.put(fqcd.getQrcode(), fqcd);
				} else {

					fqcd.setAcceptQty(0);

					fqcd.setRejectQty(0);

					fqcd.setHoldQty(0);

					tm.put(fqcd.getQrcode(), fqcd);
				}
			} else {
				String status = (String) arr[5];
				double qty = ((double) (arr[6]));
				CustomLogger.log.info("@@" + qty);
				if (status.equals("A")) {
					existingObj.setAcceptQty((long) qty);
				} else if (status.equals("R")) {
					existingObj.setRejectQty((long) qty);
				} else {
					existingObj.setHoldQty((long) qty);
				}
			}
		}
		List<FinalQCDto> fQCList = new ArrayList<FinalQCDto>();
		fQCList.addAll(tm.values());
		return fQCList;
	}

	@Override
	public List<FinalQCDto> getRemaingQtyByqrcodeNItemId(String qrcode) {
		List<Object[]> finalQCList = qualityCheckDao.getRemaingQtyByqrcodeNItemId(qrcode);
		List<FinalQCDto> fQCList = new ArrayList<FinalQCDto>();
		for (Object[] arr : finalQCList) {
			FinalQCDto fqc = new FinalQCDto();
			fqc.setRemaingQty((double) arr[1]);
			fqc.setItemMstId((String) arr[0]);
			fQCList.add(fqc);
		}
		return fQCList;
	}

	@Override
	public List<ItemMstDto> getFinalQualityCheckItemList() {

		List<Object[]> list = qualityCheckDao.getFinalQualityCheckItemList();
		List<ItemMstDto> itemDtoList = new ArrayList<ItemMstDto>();
		list.forEach(arr -> {
			ItemMstDto imd = new ItemMstDto();
			imd.setItemMstId((String) arr[0]);
			imd.setItemDtl((String) arr[2]);
			itemDtoList.add(imd);
		});
		return itemDtoList;
	}

	@Override
	public List<FinalQualityCheck> updateFinalQCFromQuarantine(String qrcode) {

		return qualityCheckDao.updateFinalQCFromQuarantine(qrcode);
	}

	
	@Override
	public List<String> getMachineTimeByQrCode(String qrcode) {
		List<Object[]> list = qualityCheckDao.getMachineTimeByQrCode(qrcode);
		List<String> machineTime = new ArrayList<>();
		list.forEach(arr -> {
			String dateTime = (String) arr[0].toString();
			machineTime.add(dateTime);
		});
		return machineTime;
	}


}
