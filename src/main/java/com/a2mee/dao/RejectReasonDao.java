package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.Reasonmaster;
import com.a2mee.model.RejectReasonMst;

public interface RejectReasonDao {
	public List<RejectReasonMst>getRejectReasonList();
	public List<Reasonmaster> getIotReasonList();
	public  Reasonmaster findById(long id);
	public List<Reasonmaster>getNewRejectReasonList();
	//public  Reasonmaster resonMstIdUseingEqupmentId(String rejectionCode);
	public List<Reasonmaster>getNewRejectReasonListForGrn();

}
