package com.kfc.app.repository;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.entities.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    Page<Warehouse> findByUserId(Integer userId, Pageable paging);
}