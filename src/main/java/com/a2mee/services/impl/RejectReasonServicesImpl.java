package com.a2mee.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.RejectReasonDao;
import com.a2mee.model.Reasonmaster;
import com.a2mee.model.RejectReasonMst;
import com.a2mee.services.RejectReasonServices;

@Service
public class RejectReasonServicesImpl implements RejectReasonServices {
	@Autowired
	RejectReasonDao rejectReasonDao;

	@Override
	public List<RejectReasonMst> getRejectReasonList() {
		//return rejectReasonDao.getRejectReasonList();
		List<Reasonmaster> newList = rejectReasonDao.getNewRejectReasonList();
		List<RejectReasonMst> heyNewList = new ArrayList<>();
		for(Reasonmaster rm : newList) {
			RejectReasonMst rrm  = new RejectReasonMst();
			Integer i = new Integer((int)rm.getReason_id());
			rrm.setRejId(i.longValue());
			rrm.setReason(rm.getRejection_reason());
			heyNewList.add(rrm);
		}
		return heyNewList;
	}

	@Override
	public List<RejectReasonMst> getRejectReasonListForGrn() {
		List<Reasonmaster> newList = rejectReasonDao.getNewRejectReasonListForGrn();
		List<RejectReasonMst> heyNewList = new ArrayList<>();
		for(Reasonmaster rm : newList) {
			RejectReasonMst rrm  = new RejectReasonMst();
			Integer i = new Integer((int)rm.getReason_id());
			rrm.setRejId(i.longValue());
			rrm.setReason(rm.getRejection_reason());
			heyNewList.add(rrm);
		}
		return heyNewList;
	}
}
