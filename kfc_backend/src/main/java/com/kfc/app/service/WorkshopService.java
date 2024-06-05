package com.kfc.app.service;


import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.dto.WorkshopDto;
import org.springframework.data.domain.Pageable;

public interface WorkshopService {
    WorkshopDto save(WorkshopDto dto);
    WorkshopDto update(Integer id, WorkshopDto dto);
    ResultPageWrapper<WorkshopDto> findByOrganizationId(Integer orgId, Pageable paging);
    WorkshopDto getById(Integer id);
}
