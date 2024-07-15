package com.kfc.app.repository;

import com.kfc.app.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    Optional<Supplier> findByNameAndOrganizationId(String name, Integer orgId);

    Page<Supplier> findByOrganizationId(@Param("organization_id")Integer orgId, Pageable paging);

}
