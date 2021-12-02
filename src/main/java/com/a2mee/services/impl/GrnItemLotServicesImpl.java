package com.a2mee.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.GrnItemLotDao;
import com.a2mee.dao.MaterialInspectDao;
import com.a2mee.dao.MaterialTransactionRepo;
import com.a2mee.model.dto.MaterialInspDto;
import com.a2mee.model.dto.PoMaterialListDto;
import com.a2mee.model.GrnItemLot;
import com.a2mee.model.MaterialInspect;
import com.a2mee.model.MaterialTransaction;
import com.a2mee.model.RejectReasonMst;
import com.a2mee.services.GrnItemLotServices;
import com.a2mee.services.MtlStockInServices;
import com.a2mee.services.QuarantineServices;
import com.a2mee.util.Constants;

@Service
public class GrnItemLotServicesImpl implements GrnItemLotServices {

	@Autowired
	GrnItemLotDao grnItemLotDao;

	@Autowired
	MaterialInspectDao materialInspectDao;

	@Autowired
	MtlStockInServices inServices;
	
	@Autowired
	QuarantineServices quarantineServices;

	GrnItemLot grnItemLot;
	
	@Autowired
	MaterialTransactionRepo materialTransactionRepo;

	@Override
	public Integer updateQrCode(long grItmlotId, String qrCodeNo) {

		return grnItemLotDao.updateQrCode(grItmlotId, qrCodeNo);
	}

	public PoMaterialListDto getDatabyQrScan(String qrCode) {
		List<Object[]> grnItemLot = grnItemLotDao.getDatabtQrcode(qrCode);

		List<PoMaterialListDto> list = new ArrayList<PoMaterialListDto>();

		/*
		 * grnItemLot.forEach(vmArray -> { long aQty; long rQty; long hQty;
		 * 
		 * PoMaterialListDto gd = new PoMaterialListDto(); gd.setId((long) vmArray[0]);
		 * aQty = (long) vmArray[2]; rQty = (long) vmArray[3]; hQty = (long) vmArray[4];
		 * long qty = ((long) vmArray[1]) - aQty - rQty - hQty; gd.setQty(qty);
		 * gd.setName(qrCode); list.add(gd); });
		 */
		PoMaterialListDto gd = new PoMaterialListDto();
		double recQty = 0;
		double aQty = 0;
		double rQty = 0;
		double hQty = 0;
		for (Object[] o : grnItemLot) {

			
			gd.setId(((Integer) o[0]).longValue());
			recQty += (double) o[1];
			aQty += (double) o[2];
			rQty += (double) o[3];
			hQty += (double) o[4];
		}
		double qty = recQty - aQty - rQty - hQty;
		if(qty<0)
		{
			System.out.println("return lot");
		}
		gd.setQty(qty);
		gd.setName(qrCode);
		list.add(gd);

		return gd;

	}

	private MaterialInspect convertDtotoEntity(MaterialInspDto materialInspDto) {
		grnItemLot = grnItemLotDao.getElementById(materialInspDto.getItmLotId());

		MaterialInspect mi = null;
		if ((materialInspDto.getAcceptqty()) != 0) {
			mi = new MaterialInspect();
			mi.setMtlStatus(Constants.MATERIAL_ACCEPTE_STATUS.getValue());
			mi.setMtlQty(materialInspDto.getAcceptqty());
			mi.setGrnItemLot(grnItemLot);
			/*mi.setDocEntry(grnItemLot.getDocEntry());
			mi.setLineNum(grnItemLot.getGrnDocNo());
			mi.setSysNumber(grnItemLot.getSysNum());*/
			mi.setOriginalStatus("P");
			mi.setDate(LocalDate.now());
			MaterialInspect m = materialInspectDao.saveInspMterial(mi);
			saveStockIn(materialInspDto, m);

		}
		if ((materialInspDto.getHoldqty()) != 0) {
			mi = new MaterialInspect();
			mi.setMtlStatus(Constants.MATERIAL_HOLD_STATUS.getValue());
			mi.setMtlReason(materialInspDto.getHoldreason());
			mi.setMtlQty(materialInspDto.getHoldqty());
			mi.setGrnItemLot(grnItemLot);
			/*mi.setDocEntry(grnItemLot.getDocEntry());
			mi.setLineNum(grnItemLot.getGrnDocNo());
			mi.setSysNumber(grnItemLot.getSysNum());*/
			mi.setOriginalStatus("P");
			mi.setDate(LocalDate.now());
			materialInspectDao.saveInspMterial(mi);
			try {
			List<Object[]> list=grnItemLotDao.findItemUseingGrnLotId(materialInspDto.getItmLotId());
			for (Object[] objects : list) {
				String itemId=(String)objects[0];
			    quarantineServices.addAllQuarantineHoldData(itemId, materialInspDto.getBarCode(), materialInspDto.getHoldqty(), "GRN");
			}
			}catch(Exception e) {e.printStackTrace();}
			

		}
		if ((materialInspDto.getRejqty()) != 0) {
			mi = new MaterialInspect();
			mi.setMtlStatus(Constants.MATERIAL_REJECT_STATUS.getValue());
			RejectReasonMst rr =new RejectReasonMst();
			rr.setRejId(materialInspDto.getRejtreasonid());
			mi.setRejectReasonMst(rr);
			mi.setMtlQty(materialInspDto.getRejqty());
			mi.setGrnItemLot(grnItemLot);
			/*mi.setDocEntry(grnItemLot.getDocEntry());
			mi.setLineNum(grnItemLot.getGrnDocNo());
			mi.setSysNumber(grnItemLot.getSysNum());*/
			mi.setOriginalStatus("P");
			mi.setDate(LocalDate.now());
			materialInspectDao.saveInspMterial(mi);
			
		}
		saveMaterialTransaction (mi);
		return mi;
	}

	private void saveStockIn(MaterialInspDto materialInspDto, MaterialInspect m) {
		inServices.saveStock(materialInspDto, m);
	}

	private MaterialInspDto calculetAndUpdat(MaterialInspDto materialInspDto) {
		long grnLotId = materialInspDto.getItmLotId();
		double qty = 0;

		if (materialInspDto.getAcceptqty() != 0) {
			qty = grnItemLot.getApproQty() + materialInspDto.getAcceptqty();
			grnItemLotDao.updateInspetSumm(grnLotId, qty, Constants.MATERIAL_ACCEPTE_STATUS.getValue());
		}
		if (materialInspDto.getHoldqty() != 0) {
			qty = grnItemLot.getHoldQty() + materialInspDto.getHoldqty();
			grnItemLotDao.updateInspetSumm(grnLotId, qty, Constants.MATERIAL_HOLD_STATUS.getValue());
		}
		if (materialInspDto.getRejqty() != 0) {
			qty = grnItemLot.getRejQty() + materialInspDto.getRejqty();
			grnItemLotDao.updateInspetSumm(grnLotId, qty, Constants.MATERIAL_REJECT_STATUS.getValue());
		}

		return materialInspDto;
	}

	@Override
	public PoMaterialListDto saveMaterialSumm(MaterialInspDto materialInspDto) {
		this.convertDtotoEntity(materialInspDto);
		this.calculetAndUpdat(materialInspDto);
		
		return this.getDatabyQrScan(materialInspDto.getBarCode());
	}

	@Override
	public GrnItemLot saveGrnItemLot(GrnItemLot theGrnItemLot) {
		return grnItemLotDao.saveGrnItemLot(theGrnItemLot);
	}

	@Override
	public void saveStockInGorGreenChanl(MaterialInspDto materialInspDto, MaterialInspect m) {
		// TODO Auto-generated method stub
		saveStockIn( materialInspDto,  m);
	}
	
	public void saveMaterialTransaction (MaterialInspect m) {
		// TODO Auto-generated method stub
		MaterialTransaction materialTransaction= new MaterialTransaction();
		materialTransaction.setItemDetial(m.getGrnItemLot().getGrnItem().getItemMst().getItemDtl());
		materialTransaction.setItemId(m.getGrnItemLot().getGrnItem().getItemMst().getId());
		materialTransaction.setQuntity(m.getMtlQty());
		materialTransaction.setTranactionType("IN");
		materialTransaction.setTranaction_date(new Date());
		materialTransactionRepo.save(materialTransaction);
	}
}
