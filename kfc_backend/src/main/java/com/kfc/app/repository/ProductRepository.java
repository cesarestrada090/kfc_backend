package com.kfc.app.repository;

import com.kfc.app.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Optional<Product> findByNameAndOrganizationId(String name, Integer orgId);

}
