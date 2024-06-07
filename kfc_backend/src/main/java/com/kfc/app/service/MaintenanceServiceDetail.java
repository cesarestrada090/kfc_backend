package com.kfc.app.service;

import com.kfc.app.dto.MaintenanceDetailDto;
import com.kfc.app.dto.MaintenanceDetailDto;
import com.kfc.app.dto.ResultPageWrapper;
import org.springframework.data.domain.Pageable;

public interface MaintenanceServiceDetail {
    MaintenanceDetailDto save(MaintenanceDetailDto dto);
    MaintenanceDetailDto update(Integer id, MaintenanceDetailDto dto);
    ResultPageWrapper<MaintenanceDetailDto> findByOrganizationId(Integer orgId, Pageable paging);
    MaintenanceDetailDto getById(Integer id);
}
