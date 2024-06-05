package com.kfc.app.repository;

import com.kfc.app.entities.Unit;
import com.kfc.app.entities.Workshop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
    Page<Unit> findByOrganizationId(Integer orgId, Pageable paging);
}