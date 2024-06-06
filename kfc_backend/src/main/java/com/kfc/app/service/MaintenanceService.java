package com.kfc.app.service;

import com.kfc.app.dto.MaintenanceDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.MaintenanceDto;
import org.springframework.data.domain.Pageable;

public interface MaintenanceService {
    MaintenanceDto save(MaintenanceDto dto);
    MaintenanceDto update(Integer id, MaintenanceDto dto);
    ResultPageWrapper<MaintenanceDto> findByOrganizationId(Integer orgId, Pageable paging);
    MaintenanceDto getById(Integer id);
}
