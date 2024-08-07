package com.kfc.app.service.impl;

import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.OrganizationDto;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.OrganizationRepository;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.PersonService;
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
public class OrganizationServiceImpl implements OrgService {

    private final OrganizationRepository orgRepository;
    private final PersonService personService;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository orgRepository, PersonService personService) {
        this.orgRepository = orgRepository;
        this.personService = personService;
    }

    @Override
    @Transactional
    public OrganizationDto save(OrganizationDto orgDto) {
        PersonDto personDto = orgDto.getLegalRepresentation();
        if (this.rucAlreadyExists(orgDto.getRuc())){
            throw new DuplicatedException("Ruc duplicated for " + orgDto.getRuc());
        }
        if (orgDto.getLegalRepresentation() != null &&
                personService.findByDocumentNumber(orgDto.getLegalRepresentation().getDocumentNumber()) != null){
            throw new DuplicatedException("Document Number duplicated for " + orgDto.getLegalRepresentation().getDocumentNumber());
        }
        Person personEntity = MapperUtil.map(personDto, Person.class);
        Organization userEntity = MapperUtil.map(orgDto, Organization.class);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setLegalRepresentation(personEntity);
        userEntity = orgRepository.save(userEntity);
        return MapperUtil.map(userEntity, OrganizationDto.class);
    }
    @Override
    @Transactional
    public OrganizationDto update(Integer id, OrganizationDto orgDto){
        Optional<Organization> currentOrg = orgRepository.findById(id);
        if(currentOrg.isEmpty()){
            throw new NotFoundException("Org does not exists: " + orgDto);
        }
        Organization organization = currentOrg.get();
        Person personEntity = organization.getLegalRepresentation();
        PersonDto personDto = orgDto.getLegalRepresentation();
        
        if(personDto.hasDifferentDocumentNumber(personEntity.getDocumentNumber()) ){
            if(personService.findByDocumentNumber(personDto.getDocumentNumber()) != null){
                throw new DuplicatedException("Document Number duplicated for " + personDto.getDocumentNumber());
            }
            personEntity.setDocumentNumber(personDto.getDocumentNumber());
        }
        
        // Legal representation
        personEntity.setLastName(personDto.getLastName());
        personEntity.setFirstName(personDto.getFirstName());
        personEntity.setPhoneNumber(personDto.getPhoneNumber());
        personEntity.setEmail(personDto.getEmail());

        // Org creation
        organization.setDescription(orgDto.getDescription());
        organization.setName(orgDto.getName());
        organization.setRuc(orgDto.getRuc());
        organization.setUpdatedAt(LocalDateTime.now());
        
        orgRepository.save(organization);
        return MapperUtil.map(organization, orgDto.getClass());
    }
    
    @Override
    public Boolean rucAlreadyExists(String ruc) {
        Optional<Organization> org = orgRepository.
                findByRuc(ruc);
        return org.isPresent();
    }

    @Override
    public OrganizationDto getById(Integer id){
        Optional<Organization> organization = orgRepository.findById(id);
        if(organization.isEmpty()){
            throw new NotFoundException("Org Id does not exists: " + id);
        }
        return MapperUtil.map(organization.get(), OrganizationDto.class);
    }


    public Organization getOrgEntityById(Integer orgId) {
        Optional<Organization> existingOrg = orgRepository.findById(orgId);
        return existingOrg.orElseThrow(() -> new NotFoundException("Org not found with id: " + orgId));
    }
    
    
    @Override
    public ResultPageWrapper<OrganizationDto> getAll(Pageable paging){
        Page<Organization> orgs = orgRepository.findAll(paging);
        if(orgs.isEmpty()){
            throw  new NotFoundException("Org does not exists");
        }
        return PaginationUtil.prepareResultWrapper(orgs, OrganizationDto.class);
    }
    

    private void preparePersonEntity(PersonDto personDto, Person personEntity) {
        if(personDto.hasDifferentDocumentNumber(personEntity.getDocumentNumber()) ){
            if(personService.findByDocumentNumber(personDto.getDocumentNumber()) != null){
                throw new DuplicatedException("Document Number duplicated for " + personDto.getDocumentNumber());
            }
            personEntity.setDocumentNumber(personDto.getDocumentNumber());
        }
        personEntity.setLastName(personDto.getLastName());
        personEntity.setFirstName(personDto.getFirstName());
        personEntity.setPhoneNumber(personDto.getPhoneNumber());
        personEntity.setEmail(personDto.getEmail());
    }

    public Organization getOrCreateOrgEntity(OrganizationDto orgDto){
        Organization organization = null;
        if (orgDto.getId() == null) {
            // Create Organization
            if(this.rucAlreadyExists(orgDto.getRuc())){
                throw new DuplicatedException("RUC duplicated for " + orgDto.getRuc());
            }
            Person legalPerson = personService.getOrCreatePersonEntity(orgDto.getLegalRepresentation());
            // Create Organization Entity
            organization = new Organization();
            organization.setRuc(orgDto.getRuc());
            organization.setName(orgDto.getName());
            organization.setLegalRepresentation(legalPerson);
            organization.setDescription(orgDto.getDescription());
            organization.setCreatedAt(LocalDateTime.now());
            organization.setUpdatedAt(LocalDateTime.now());
        } else {
            // otherwise get Person Entity from DB
            organization = this.getOrgEntityById(orgDto.getId());
        }
        return organization;
    }
}
