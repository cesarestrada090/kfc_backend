package com.kfc.app.service.impl;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import com.kfc.app.dto.WarehouseDto;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import com.kfc.app.entities.Warehouse;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.WarehouseRepository;
import com.kfc.app.service.PersonService;
import com.kfc.app.service.UserService;
import com.kfc.app.service.WarehouseService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    private final UserService userService;
    private final PersonService personService;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                UserService userService,
                                PersonService personService) {
        this.warehouseRepository = warehouseRepository;
        this.userService = userService;
        this.personService = personService;
    }

    @Override
    public WarehouseDto save(WarehouseDto dto) {
        Person person = personService.getOrCreatePersonEntity(dto.getUserDto().getPerson());
        User user = userService.getOrCreateUserEntityByDto(dto.getUserDto(), person);
        Warehouse warehouse = createWarehouseEntityByDto(dto, user);
        warehouse = warehouseRepository.save(warehouse);
        return MapperUtil.map(warehouse, WarehouseDto.class);
    }

    @Override
    public WarehouseDto update(Integer id, WarehouseDto dto) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isEmpty()) {
            throw new NotFoundException("Warehouse not found with id: " + id);
        }
        Person person = personService.getOrCreatePersonEntity(dto.getUserDto().getPerson());
        User user = userService.getOrCreateUserEntityByDto(dto.getUserDto(), person);
        Warehouse warehouse = createWarehouseEntityByDto(dto, user);
        warehouseRepository.save(warehouse);
        return MapperUtil.map(optionalWarehouse.get(), WarehouseDto.class);
    }

    @Override
    public ResultPageWrapper<WarehouseDto> findByUserId(Integer userId, Pageable paging) {
        UserDto userDto = userService.getById(userId);
        Page<Warehouse> warehouses = warehouseRepository.findByUserId(userDto.getId(), paging);
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
        return MapperUtil.map(optionalWarehouse.get(), WarehouseDto.class);
    }

    private Warehouse createWarehouseEntityByDto(WarehouseDto warehouseDto, User user){
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseDto.getId());
        warehouse.setName(warehouseDto.getName());
        warehouse.setAddress(warehouseDto.getAddress());
        warehouse.setCity(warehouseDto.getCity());
        warehouse.setStatus(warehouseDto.isStatus());
        warehouse.setUser(user);
        return warehouse;
    }
}