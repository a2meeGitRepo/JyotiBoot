package com.a2mee.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a2mee.model.PendingReport;

public interface PendingReportTableRepo extends JpaRepository<PendingReport, Integer> {
	@Query("From PendingReport p where Date(p.reportDate)=?2 and p.status=?1" )
	List<PendingReport> getPendingReportByDateAndStatus(String reportBy,Date reportDate);
	@Query("From PendingReport p where Date(p.reportDate)=?1")
	List<PendingReport> getPendingReportByDateAndAll(Date reportDate);
	
	@Query("From PendingReport p where Date(p.reportDate)=?2 and p.status=?1 and p.compCode=?3" )
	List<PendingReport> getPendingReportByDateAndStatusAndCode(String reportBy, Date reportDate, String code);
	
	@Query("From PendingReport p where Date(p.reportDate)=?1 and p.compCode=?2" )
	List<PendingReport> getPendingReportByDateAndStatusAndCodeAll(Date reportDate, String code);
}
