package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.a2mee.dao.RejectReasonDao;
import com.a2mee.model.Reasonmaster;
import com.a2mee.model.RejectReasonMst;

@Repository
@Transactional

public class RejectReasonDaoImpl implements RejectReasonDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Value(value = "${rejectReasonDao.getRejectReasonList.hql}")
	private String rejectReasonDaoGetRejectReasonListHql;

	@Override
	public List<RejectReasonMst> getRejectReasonList() {
		List<RejectReasonMst> rejectReasonList = entityManager
				.createQuery(rejectReasonDaoGetRejectReasonListHql, RejectReasonMst.class).getResultList();
		return rejectReasonList;
	}

	@Override
	public List<Reasonmaster> getIotReasonList() {
		String hql = "From  Reasonmaster";
		List<Reasonmaster> grtList = entityManager.createQuery(hql, Reasonmaster.class).getResultList();
		return grtList;
	}

	@Override
	public Reasonmaster findById(long id) {
int id1=(int)id;
		Reasonmaster rm = entityManager.find(Reasonmaster.class, id1);
		return rm;
	}
/*
	@Override
	public Reasonmaster resonMstIdUseingEqupmentId(String rejectionCode) {
		String hql = "from Reasonmaster where rejection_code= :rejectionCode ";
		Reasonmaster rm = entityManager.createQuery(hql, Reasonmaster.class).setParameter("rejectionCode", rejectionCode)
				.getSingleResult();
		return rm;
	}*/

	@Override
	public List<Reasonmaster> getNewRejectReasonList() {
		String hql = "FROM Reasonmaster where rejection_code is Not Null";
		List<Reasonmaster> rejectReasonList = entityManager
				.createQuery(hql, Reasonmaster.class).getResultList();
		return rejectReasonList;
	}
	@Override
	public List<Reasonmaster> getNewRejectReasonListForGrn() {
		String hql = "FROM Reasonmaster";
		List<Reasonmaster> rejectReasonList = entityManager
				.createQuery(hql, Reasonmaster.class).getResultList();
		return rejectReasonList;
	}

}
