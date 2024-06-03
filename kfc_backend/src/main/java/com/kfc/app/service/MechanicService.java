package com.kfc.app.service;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.MechanicDto;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.entities.Mechanic;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import org.springframework.data.domain.Pageable;

public interface MechanicService {
    MechanicDto save(MechanicDto userDto);
    MechanicDto update(Integer id, MechanicDto dto);
    MechanicDto getById(Integer id);
    Mechanic getMechanicEntityById(Integer id);
    ResultPageWrapper<MechanicDto> getAll(Pageable paging);
    ResultPageWrapper<MechanicDto> findByOrganizationId(Integer userId, Pageable paging);
}
