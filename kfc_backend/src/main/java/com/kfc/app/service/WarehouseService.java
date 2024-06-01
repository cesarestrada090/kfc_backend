package com.kfc.app.service;


import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.dto.ResultPageWrapper;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    WarehouseDto save(WarehouseDto dto);
    WarehouseDto update(Integer id, WarehouseDto dto);
    ResultPageWrapper<WarehouseDto> findByOrganizationId(Integer userId, Pageable paging);
    WarehouseDto getById(Integer id);
}
