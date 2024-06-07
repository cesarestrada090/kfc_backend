package com.kfc.app.service;


import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UnitDto;
import com.kfc.app.dto.UnitDto;
import com.kfc.app.entities.Unit;
import org.springframework.data.domain.Pageable;

public interface UnitService {
    UnitDto save(UnitDto dto);
    UnitDto update(Integer id, UnitDto dto);
    ResultPageWrapper<UnitDto> findByOrganizationId(Integer orgId, Pageable paging);
    Unit getUnitEntityByDto(UnitDto unitDto);
    Unit getUnitEntityById(Integer id);
    UnitDto getById(Integer id);
}
