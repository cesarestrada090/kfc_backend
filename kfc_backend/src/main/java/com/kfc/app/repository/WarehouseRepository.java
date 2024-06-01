package com.kfc.app.repository;

import com.kfc.app.entities.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    Page<Warehouse> findByOrganizationId(Integer orgId, Pageable paging);
}