package com.kfc.app.repository;

import com.kfc.app.entities.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
    Optional<ProductType> findByNameAndCategory(String name, String category);
}
