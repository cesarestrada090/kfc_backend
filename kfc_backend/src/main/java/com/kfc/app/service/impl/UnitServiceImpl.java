package com.kfc.app.service.impl;

import com.kfc.app.dto.OrganizationDto;
import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UnitDto;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.Unit;
import com.kfc.app.entities.User;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.UnitRepository;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.UnitService;
import com.kfc.app.service.UserService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UnitServiceImpl implements UnitService {
    
    private final UnitRepository unitRepository;
    private final OrgService orgService;
    private final UserService userService;

    public UnitServiceImpl(UnitRepository unitRepository,
                           OrgService orgService,
                           UserService userService) {
        this.unitRepository = unitRepository;
        this.orgService = orgService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public UnitDto save(UnitDto dto) {
        Unit workshop = getUnitEntityByDto(dto);
        workshop = unitRepository.save(workshop);
        return MapperUtil.map(workshop, UnitDto.class);
    }

    @Override
    public UnitDto update(Integer id, UnitDto dto) {
        Optional<Unit> optionalUnit = unitRepository.findById(id);
        if (optionalUnit.isEmpty()) {
            throw new NotFoundException("Unit not found with id: " + id);
        }

        // Update organization
        OrganizationDto orgDto = dto.getOrganization();
        Organization organization = orgService.getOrgEntityById(orgDto.getId());
        if(orgDto.getDescription() != null){
            organization.setDescription(orgDto.getDescription());
            organization.setRuc(orgDto.getRuc());
            organization.setName(orgDto.getName());
            organization.setUpdatedAt(LocalDateTime.now());
        }
        
        // Update person
        PersonDto personDto = dto.getOrganization().getLegalRepresentation();
        if(personDto != null && personDto.getFirstName() != null) {
            Person legalRepresentation = organization.getLegalRepresentation();
            legalRepresentation.setFirstName(personDto.getFirstName());
            legalRepresentation.setLastName(personDto.getLastName());
            legalRepresentation.setEmail(personDto.getEmail());
            legalRepresentation.setPhoneNumber(personDto.getPhoneNumber());
            legalRepresentation.setDocumentNumber(personDto.getDocumentNumber());
            organization.setLegalRepresentation(legalRepresentation);
        }
        
        //update unit
        Unit unit = optionalUnit.get();
        unit.setModel(dto.getModel());
        unit.setDescription(dto.getDescription());
        unit.setType(dto.getType());
        unit.setRegistrationPlate(dto.getRegistrationPlate());
        unit.setSerialNumber(dto.getSerialNumber());
        unit.setUpdatedAt(LocalDateTime.now());
        // TODO: change for logged in user
        unit.setLastUpdatedBy(unit.getCreatedBy());
        unitRepository.save(unit);
        
        return MapperUtil.map(optionalUnit.get(), UnitDto.class);
    }

    @Override
    public ResultPageWrapper<UnitDto> findByOrganizationId(Integer orgId, Pageable paging) {
        OrganizationDto orgDto = orgService.getById(orgId);
        Page<Unit> warehouses = unitRepository.findByOrganizationId(orgDto.getId(), paging);
        if(warehouses.isEmpty()){
            throw new NotFoundException("Units does not exists");
        }
        return PaginationUtil.prepareResultWrapper(warehouses, UnitDto.class);
    }

    @Override
    public UnitDto getById(Integer id) {
        return unitRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, UnitDto.class))
                .orElseThrow(() -> new NotFoundException("Unit not found with id: " + id));
    }

    public Unit getUnitEntityByDto(UnitDto unitDto){
        Organization org = orgService.getOrCreateOrgEntity(unitDto.getOrganization());
        User user = userService.getUserEntityById(unitDto.getCreatedBy().getId());
        Unit unit = new Unit();
        unit.setModel(unitDto.getModel());
        unit.setDescription(unitDto.getDescription());
        unit.setType(unitDto.getType());
        unit.setRegistrationPlate(unitDto.getRegistrationPlate());
        unit.setCreatedAt(LocalDateTime.now());
        unit.setUpdatedAt(LocalDateTime.now());
        unit.setRegistrationPlate(unitDto.getRegistrationPlate());
        unit.setSerialNumber(unitDto.getSerialNumber());
        // TODO: change for logged in user
        unit.setLastUpdatedBy(user);
        unit.setCreatedBy(user);
        unit.setOrganization(org);
        return unit;
    }
    
    public Unit getUnitEntityById(Integer unitId){
        return unitRepository.findById(unitId).orElseThrow(()-> new NotFoundException("Not found Unit with id:" + unitId));
    }
}