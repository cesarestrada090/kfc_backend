package com.kfc.app.repository;

import com.kfc.app.entities.Warehouse;
import com.kfc.app.entities.Workshop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopRepository extends JpaRepository<Workshop, Integer> {
    Page<Workshop> findByOrganizationId(Integer orgId, Pageable paging);
}