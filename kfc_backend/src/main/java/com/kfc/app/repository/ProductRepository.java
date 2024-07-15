package com.kfc.app.repository;

import com.kfc.app.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Optional<Product> findByNameAndOrganizationId(String name, Integer orgId);

    Page<Product> findByOrganizationId(@Param("orgId")Integer orgId, Pageable paging);

}
