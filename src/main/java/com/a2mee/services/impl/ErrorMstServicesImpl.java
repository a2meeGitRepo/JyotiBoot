package com.a2mee.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a2mee.dao.ErrorMstDao;
import com.a2mee.model.ErrorMst;
import com.a2mee.services.ErrorMstServices;

@Service
public class ErrorMstServicesImpl implements ErrorMstServices {
	
	@Autowired
	ErrorMstDao errorMstDao;

	@Override
	public List<ErrorMst> getErrorList() {
		return errorMstDao.getErrorList();
	}

}
