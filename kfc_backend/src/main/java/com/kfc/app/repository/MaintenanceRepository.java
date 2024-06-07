package com.kfc.app.repository;

import com.kfc.app.entities.Maintenance;
import com.kfc.app.entities.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    @Query("SELECT m " +
            "FROM Maintenance m " +
            "JOIN Workshop w on m.workshop.id=w.id " +
            "WHERE w.organization.id = :orgId")
    Page<Maintenance> findMaintenanceByOrganizationId(@Param("orgId")Integer orgId, Pageable paging);
}