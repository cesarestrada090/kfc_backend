package com.kfc.app.service.impl;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.entities.User;
import com.kfc.app.entities.Warehouse;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.UserRepository;
import com.kfc.app.repository.WarehouseRepository;
import com.kfc.app.service.WarehouseService;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, UserRepository userRepository) {
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public WarehouseDto save(WarehouseDto dto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.getName());
        warehouse.setAddress(dto.getAddress());
        warehouse.setCity(dto.getCity());
        warehouse.setStatus(dto.isStatus());
        warehouse = warehouseRepository.save(warehouse);
        return new WarehouseDto(warehouse);
    }

    @Override
    public WarehouseDto update(Integer id, WarehouseDto dto) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isEmpty()) {
            throw new NotFoundException("Warehouse not found with id: " + id);
        }
        Warehouse warehouse = optionalWarehouse.get();
        warehouse.setName(dto.getName());
        warehouse.setAddress(dto.getAddress());
        warehouse.setCity(dto.getCity());
        warehouse.setStatus(dto.isStatus());
        warehouse = warehouseRepository.save(warehouse);
        return new WarehouseDto(warehouse);
    }

    @Override
    public ResultPageWrapper<WarehouseDto> findByUserId(Integer userId, Pageable paging) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        Page<Warehouse> warehouses = warehouseRepository.findByUserId(userId, paging);
        if(warehouses.isEmpty()){
            throw new NotFoundException("Warehouses does not exists");
        }
        return PaginationUtil.prepareResultWrapper(warehouses, WarehouseDto.class);
    }

    @Override
    public WarehouseDto getById(Integer id) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isEmpty()) {
            throw new NotFoundException("Warehouse not found with id: " + id);
        }
        return new WarehouseDto(optionalWarehouse.get());
    }
}