package com.kfc.app.service.impl;
import com.kfc.app.dto.PersonDto;
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

import java.time.LocalDateTime;
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
        Person person = personService.getOrCreatePersonEntity(dto.getUser().getPerson());
        User user = userService.getOrCreateUserEntityByDto(dto.getUser(), person);
        Warehouse warehouse = getWarehouseEntityByDto(dto, user);
        warehouse = warehouseRepository.save(warehouse);
        return MapperUtil.map(warehouse, WarehouseDto.class);
    }

    @Override
    public WarehouseDto update(Integer id, WarehouseDto dto) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isEmpty()) {
            throw new NotFoundException("Warehouse not found with id: " + id);
        }
        PersonDto personDto = dto.getUser().getPerson();
        // update person
        Person person = personService.getPersonEntity(dto.getUser().getPerson());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setEmail(personDto.getEmail());
        person.setPhoneNumber(personDto.getPhoneNumber());
        person.setDocumentNumber(personDto.getDocumentNumber());
        
        // update user
        UserDto userDto = dto.getUser();
        User user = userService.getUserEntityById(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPerson(person);
        user.setUpdatedAt(LocalDateTime.now());
        
        //update warehouse
        Warehouse warehouse = getWarehouseEntityByDto(dto, user);
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
        return warehouseRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, WarehouseDto.class))
                .orElseThrow(() -> new NotFoundException("Warehouse not found with id: " + id));
    }

    public Warehouse getWarehouseEntityByDto(WarehouseDto warehouseDto, User user){
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseDto.getId());
        warehouse.setName(warehouseDto.getName());
        warehouse.setAddress(warehouseDto.getAddress());
        warehouse.setCity(warehouseDto.getCity());
        warehouse.setStatus(warehouseDto.isStatus());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse.setUser(user);
        if(warehouseDto.getId() == null){
            warehouse.setCreatedAt(LocalDateTime.now());
        }
        return warehouse;
    }
}