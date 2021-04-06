package com.example.report;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCreator(String creator);
    List<Report> findByOperatorAndStatus(String operator,Report.Status status);
    
}