package com.kfc.app.service;


import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.dto.WorkshopDto;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Workshop;
import org.springframework.data.domain.Pageable;

public interface WorkshopService {
    WorkshopDto save(WorkshopDto dto);
    WorkshopDto update(Integer id, WorkshopDto dto);
    ResultPageWrapper<WorkshopDto> findByOrganizationId(Integer orgId, Pageable paging);
    Workshop getWorkshopEntityByDto(WorkshopDto workshopDto, Organization org);
    WorkshopDto getById(Integer id);
}
