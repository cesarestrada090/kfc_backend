package com.kfc.app.service.impl;

import com.kfc.app.dto.*;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.Workshop;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.WorkshopRepository;
import com.kfc.app.repository.WorkshopRepository;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.PersonService;
import com.kfc.app.service.UserService;
import com.kfc.app.service.WorkshopService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorkshopServiceImpl implements WorkshopService {
    
    private final WorkshopRepository workshopRepository;
    private final OrgService orgService;

    public WorkshopServiceImpl(WorkshopRepository workshopRepository,
                               OrgService orgService) {
        this.workshopRepository = workshopRepository;
        this.orgService = orgService;
    }

    @Override
    @Transactional
    public WorkshopDto save(WorkshopDto dto) {
        Organization organization = orgService.getOrCreateOrgEntity(dto.getOrganization());
        Workshop warehouse = getWorkshopEntityByDto(dto, organization);
        warehouse = workshopRepository.save(warehouse);
        return MapperUtil.map(warehouse, WorkshopDto.class);
    }

    @Override
    public WorkshopDto update(Integer id, WorkshopDto dto) {
        Optional<Workshop> optionalWorkshop = workshopRepository.findById(id);
        
        if (optionalWorkshop.isEmpty()) {
            throw new NotFoundException("Workshop not found with id: " + id);
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
            Person legalRepresentation = organization.getLegalRepresentationPerson();
            legalRepresentation.setFirstName(personDto.getFirstName());
            legalRepresentation.setLastName(personDto.getLastName());
            legalRepresentation.setEmail(personDto.getEmail());
            legalRepresentation.setPhoneNumber(personDto.getPhoneNumber());
            legalRepresentation.setDocumentNumber(personDto.getDocumentNumber());
            organization.setLegalRepresentationPerson(legalRepresentation);
        }
        
        //update warehouse
        Workshop workshop = workshopRepository.getReferenceById(id);
        workshop.setName(dto.getName());
        workshop.setDescription(dto.getDescription());
        workshop.setCode(dto.getCode());
        workshop.setUpdatedAt(LocalDateTime.now());
        workshop.setOrganization(organization);
        workshopRepository.save(workshop);
        
        return MapperUtil.map(optionalWorkshop.get(), WorkshopDto.class);
    }

    @Override
    public ResultPageWrapper<WorkshopDto> findByOrganizationId(Integer orgId, Pageable paging) {
        OrganizationDto orgDto = orgService.getById(orgId);
        Page<Workshop> warehouses = workshopRepository.findByOrganizationId(orgDto.getId(), paging);
        if(warehouses.isEmpty()){
            throw new NotFoundException("Workshops does not exists");
        }
        return PaginationUtil.prepareResultWrapper(warehouses, WorkshopDto.class);
    }

    @Override
    public WorkshopDto getById(Integer id) {
        return workshopRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, WorkshopDto.class))
                .orElseThrow(() -> new NotFoundException("Workshop not found with id: " + id));
    }

    public Workshop getWorkshopEntityByDto(WorkshopDto workshopDto, Organization org){
        Workshop workshop = new Workshop();
        workshop.setName(workshopDto.getName());
        workshop.setDescription(workshopDto.getDescription());
        workshop.setCode(workshopDto.getCode());
        workshop.setStatus(workshopDto.isStatus());
        workshop.setUpdatedAt(LocalDateTime.now());
        workshop.setOrganization(org);
        return workshop;
    }
}