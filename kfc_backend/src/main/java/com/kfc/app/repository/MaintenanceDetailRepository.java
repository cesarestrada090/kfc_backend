package com.kfc.app.repository;

import com.kfc.app.entities.MaintenanceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaintenanceDetailRepository extends JpaRepository<MaintenanceDetail, Integer> {
    List<MaintenanceDetail> findByMaintenanceId(@Param("maintenanceId")Integer maintenanceId);
}