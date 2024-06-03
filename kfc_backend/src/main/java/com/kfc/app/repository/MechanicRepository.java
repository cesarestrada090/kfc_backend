package com.kfc.app.repository;

import com.kfc.app.entities.Mechanic;
import com.kfc.app.entities.User;
import com.kfc.app.entities.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MechanicRepository extends JpaRepository<Mechanic, Integer> {
    Page<Mechanic> findByOrganizationId(Integer orgId, Pageable paging);
}