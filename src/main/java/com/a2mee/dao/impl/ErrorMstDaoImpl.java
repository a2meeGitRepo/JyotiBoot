package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.ErrorMstDao;
import com.a2mee.model.ErrorMst;


@Transactional
@Repository
public class ErrorMstDaoImpl implements ErrorMstDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<ErrorMst> getErrorList() {

		String hql = "FROM ErrorMst";
		List<ErrorMst> errorList = entityManager.createQuery(hql, ErrorMst.class).getResultList();
		return errorList;
	}

}
