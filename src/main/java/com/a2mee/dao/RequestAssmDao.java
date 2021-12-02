package com.a2mee.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a2mee.model.RequestAssembly;

@Repository
public interface RequestAssmDao extends JpaRepository<RequestAssembly, Long> {

	@Query("from RequestAssembly r where r.productionOrder.prodOrdNo=?1")
	List<RequestAssembly> getReqAssms(long prodOrdNo);

	@Transactional
	@Modifying
	@Query("UPDATE RequestAssembly r SET r.status=?2 WHERE r.reqAssmId=?1")
	void assmReceived(long reqAssmId, String status);

	@Query("from RequestAssembly r where r.status!=?1")
	List<RequestAssembly> getReqAssms(String status);

	@Transactional
	@Modifying
	@Query("UPDATE RequestAssembly r SET r.status=?2 WHERE r.reqAssmId=?1")
	void consumeAssm(long reqAssmId, String status);

	@Query("from RequestAssembly r where r.status=?1 order by r.requiredDate")
	List<RequestAssembly> getRequestedAssms(String status);

}
