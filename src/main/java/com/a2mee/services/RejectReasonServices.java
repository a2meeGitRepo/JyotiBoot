package com.a2mee.services;

import java.util.List;

import com.a2mee.model.RejectReasonMst;

public interface RejectReasonServices {
	public List<RejectReasonMst> getRejectReasonList();
	public List<RejectReasonMst> getRejectReasonListForGrn();
}
