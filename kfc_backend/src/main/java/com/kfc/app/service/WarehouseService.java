package com.kfc.app.service;


import com.kfc.app.dto.UserDto;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarehouseService {
    WarehouseDto save(WarehouseDto dto);
    WarehouseDto update(Integer id, WarehouseDto dto);
    ResultPageWrapper<WarehouseDto> findByUserId(Integer userId, Pageable paging);
    WarehouseDto getById(Integer id);
}
