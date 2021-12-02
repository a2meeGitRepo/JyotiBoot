package com.a2mee.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a2mee.model.MaterialTransaction;

public interface MaterialTransactionRepo extends JpaRepository<MaterialTransaction, Integer>{
	@Query("from MaterialTransaction m where Date(m.tranactionDate)=?1")
	List<MaterialTransaction> gettodaysTranction(Date date);

}
