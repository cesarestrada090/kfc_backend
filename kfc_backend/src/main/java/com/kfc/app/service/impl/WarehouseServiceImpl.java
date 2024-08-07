package com.kfc.app.service.impl;
import com.kfc.app.dto.*;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.Warehouse;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.WarehouseRepository;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.PersonService;
import com.kfc.app.service.UserService;
import com.kfc.app.service.WarehouseService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    private final UserService userService;
    private final OrgService orgService;
    private final PersonService personService;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                UserService userService,
                                PersonService personService,
                                OrgService orgService) {
        this.warehouseRepository = warehouseRepository;
        this.orgService = orgService;
        this.userService = userService;
        this.personService = personService;
    }

    @Override
    @Transactional
    public WarehouseDto save(WarehouseDto dto) {
        Organization organization = orgService.getOrCreateOrgEntity(dto.getOrganization());
        Warehouse warehouse = getWarehouseEntityByDto(dto, organization);
        warehouse = warehouseRepository.save(warehouse);
        return MapperUtil.map(warehouse, WarehouseDto.class);
    }

    @Override
    @Transactional
    public WarehouseDto update(Integer id, WarehouseDto warehouseDto) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        
        if (optionalWarehouse.isEmpty()) {
            throw new NotFoundException("Warehouse not found with id: " + id);
        }

        // Update organization
        OrganizationDto orgDto = warehouseDto.getOrganization();
        Organization organization = orgService.getOrgEntityById(orgDto.getId());
        if(orgDto.getDescription() != null){
            organization.setDescription(orgDto.getDescription());
            organization.setRuc(orgDto.getRuc());
            organization.setName(orgDto.getName());
            organization.setUpdatedAt(LocalDateTime.now());
        }
        
        
        // Update person
        PersonDto personDto = warehouseDto.getOrganization().getLegalRepresentation();
        if(personDto != null && personDto.getFirstName() != null) {
            Person legalRepresentation = organization.getLegalRepresentation();
            legalRepresentation.setFirstName(personDto.getFirstName());
            legalRepresentation.setLastName(personDto.getLastName());
            legalRepresentation.setEmail(personDto.getEmail());
            legalRepresentation.setPhoneNumber(personDto.getPhoneNumber());
            legalRepresentation.setDocumentNumber(personDto.getDocumentNumber());
            organization.setLegalRepresentation(legalRepresentation);
        }
        
        //update warehouse
        Warehouse warehouse = warehouseRepository.getReferenceById(id);
        
        warehouse.setName(warehouseDto.getName());
        warehouse.setAddress(warehouseDto.getAddress());
        warehouse.setCity(warehouseDto.getCity());
        warehouse.setStatus(warehouseDto.isStatus());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse.setOrganization(organization);
        warehouseRepository.save(warehouse);
        
        return MapperUtil.map(optionalWarehouse.get(), WarehouseDto.class);
    }

    @Override
    public ResultPageWrapper<WarehouseDto> findByOrganizationId(Integer orgId, Pageable paging) {
        OrganizationDto orgDto = orgService.getById(orgId);
        Page<Warehouse> warehouses = warehouseRepository.findByOrganizationId(orgDto.getId(), paging);
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

    public Warehouse getWarehouseEntityByDto(WarehouseDto warehouseDto, Organization org){
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseDto.getName());
        warehouse.setAddress(warehouseDto.getAddress());
        warehouse.setCity(warehouseDto.getCity());
        warehouse.setStatus(warehouseDto.isStatus());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouse.setOrganization(org);
        return warehouse;
    }
}