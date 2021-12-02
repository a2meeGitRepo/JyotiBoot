package com.a2mee.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a2mee.model.PendingReport;

public interface PendingReportRepo extends JpaRepository<PendingReport, Integer> {

}
