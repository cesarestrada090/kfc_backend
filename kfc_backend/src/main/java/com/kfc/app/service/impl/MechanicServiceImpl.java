package com.kfc.app.service.impl;

import com.kfc.app.dto.*;
import com.kfc.app.entities.*;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.InvalidPasswordException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.MechanicRepository;
import com.kfc.app.repository.UserRepository;
import com.kfc.app.service.MechanicService;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.PersonService;
import com.kfc.app.service.UserService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;
    private final PersonService personService;
    private final OrgService orgService;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository,
                               PersonService personService,
                               OrgService orgService) {
        this.mechanicRepository = mechanicRepository;
        this.personService = personService;
        this.orgService = orgService;
    }

    @Override
    @Transactional
    public MechanicDto save(MechanicDto mechanicDto) {
        PersonDto personDto = mechanicDto.getPerson();
        if(mechanicRepository.findByDocumentNumberAndOrganizationId(mechanicDto.getOrganization().getId(), personDto.getDocumentNumber()) != null){
            throw new DuplicatedException("Document number already exists: " + personDto.getDocumentNumber() + " and org id: "+ mechanicDto.getOrganization().getId());
        }
        
        Organization organization = orgService.getOrCreateOrgEntity(mechanicDto.getOrganization());
        Person person = personService.getOrCreatePersonEntity(personDto);
        
        // Save mechanic
        Mechanic mechanic = new Mechanic();
        mechanic.setLicenceCode(mechanicDto.getLicenceCode());
        mechanic.setSpecialization(mechanicDto.getSpecialization());
        mechanic.setNotes(mechanicDto.getNotes());
        mechanic.setStatus(mechanicDto.isStatus());
        mechanic.setCreatedAt(LocalDateTime.now());
        mechanic.setUpdatedAt(LocalDateTime.now());
        mechanic.setPerson(person);
        mechanic.setOrganization(organization);
        
        mechanic = mechanicRepository.save(mechanic);
        return MapperUtil.map(mechanic, MechanicDto.class);
    }

    @Override
    @Transactional
    public MechanicDto update(Integer id, MechanicDto mechanicDTO) {
        Mechanic mechanicEntity = getMechanicEntityById(id);
        
        Person personEntity = mechanicEntity.getPerson();
        if(mechanicDTO.getPerson() != null && mechanicDTO.getPerson().getDocumentNumber() != null){
            PersonDto personDto = mechanicDTO.getPerson();
            if(personDto.hasDifferentDocumentNumber(personEntity.getDocumentNumber())){
                if(mechanicRepository.findByDocumentNumberAndOrganizationId(mechanicDTO.getOrganization().getId(), personDto.getDocumentNumber()) != null){
                    throw new DuplicatedException("Document number already exists: " + personDto.getDocumentNumber() + " and org id: "+ mechanicDTO.getOrganization().getId());
                }
                personEntity.setDocumentNumber(personDto.getDocumentNumber());
            }
            personEntity.setEmail(personDto.getEmail());
            personEntity.setLastName(personDto.getLastName());
            personEntity.setFirstName(personDto.getFirstName());
            personEntity.setPhoneNumber(personDto.getPhoneNumber());
        }

        Organization organization = mechanicEntity.getOrganization();
        if(mechanicDTO.getOrganization() != null && mechanicDTO.getOrganization().getRuc() != null){
            OrganizationDto organizationDto = mechanicDTO.getOrganization();
            if(organizationDto.hasDifferentRUC(organization.getRuc())){
                if(orgService.rucAlreadyExists(organizationDto.getRuc()) != null){
                    throw new DuplicatedException("Ruc already exists for " + organizationDto.getRuc());
                }
                organization.setRuc(organizationDto.getRuc());
            }
            organization.setDescription(organizationDto.getDescription());
            organization.setUpdatedAt(LocalDateTime.now());
            organization.setName(organizationDto.getName());
        }
        
        mechanicEntity.setLicenceCode(mechanicDTO.getLicenceCode());
        mechanicEntity.setSpecialization(mechanicDTO.getSpecialization());
        mechanicEntity.setNotes(mechanicDTO.getNotes());
        mechanicEntity.setStatus(mechanicDTO.isStatus());
        mechanicEntity.setUpdatedAt(LocalDateTime.now());
        mechanicRepository.save(mechanicEntity);
        return MapperUtil.map(mechanicEntity, MechanicDto.class);
    }

    @Override
    public MechanicDto getById(Integer id) {
        Optional<Mechanic> mechanic = mechanicRepository.findById(id);
        if (mechanic.isEmpty()) {
            throw new NotFoundException("Mechanic Id does not exist: " + id);
        }
        return MapperUtil.map(mechanic.get(), MechanicDto.class);
    }

    @Override
    public ResultPageWrapper<MechanicDto> getAll(Pageable paging) {
        Page<Mechanic> mechanics = mechanicRepository.findAll(paging);
        if (mechanics.isEmpty()) {
            throw new NotFoundException("Mechanics not found");
        }
        return PaginationUtil.prepareResultWrapper(mechanics, MechanicDto.class);
    }

    public Mechanic getMechanicEntityById(Integer id) {
        Optional<Mechanic> existingMechanic = mechanicRepository.findById(id);
        return existingMechanic.orElseThrow(() -> new NotFoundException("Mechanic not found with id: " + id));
    }

    @Override
    public ResultPageWrapper<MechanicDto> findByOrganizationId(Integer orgId, Pageable paging) {
        OrganizationDto orgDto = this.orgService.getById(orgId);
        Page<Mechanic> warehouses = mechanicRepository.findByOrganizationId(orgDto.getId(), paging);
        if(warehouses.isEmpty()){
            throw new NotFoundException("Mechanic does not exists");
        }
        return PaginationUtil.prepareResultWrapper(warehouses, MechanicDto.class);
    }
}
