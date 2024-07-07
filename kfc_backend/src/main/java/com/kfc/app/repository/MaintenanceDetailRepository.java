package com.kfc.app.repository;

import com.kfc.app.entities.Maintenance;
import com.kfc.app.entities.MaintenanceDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaintenanceDetailRepository extends JpaRepository<MaintenanceDetail, Integer> {
    Page<MaintenanceDetail> findByMaintenanceId(@Param("maintenanceId")Integer maintenanceId, Pageable paging);
}