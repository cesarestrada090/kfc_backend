package com.kfc.app.service.impl;

import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.OrganizationDto;
import com.kfc.app.dto.UserDto;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.OrganizationRepository;
import com.kfc.app.repository.PersonRepository;
import com.kfc.app.service.OrgService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrgService {

    private final OrganizationRepository orgRepository;
    private final PersonRepository personRepository;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository orgRepository, PersonRepository personRepository) {
        this.orgRepository = orgRepository;
        this.personRepository = personRepository;
    }

    @Override
    public OrganizationDto save(OrganizationDto orgDto) {
        PersonDto personDto = orgDto.getLegalRepresentation();
        if (this.rucAlreadyExists(orgDto.getRuc())){
            throw new DuplicatedException("Ruc duplicated for " + orgDto.getRuc());
        }
        Person personEntity = MapperUtil.map(personDto, Person.class);;
        Organization userEntity = MapperUtil.map(orgDto, Organization.class);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setLegalRepresentationPerson(personEntity);
        userEntity = orgRepository.save(userEntity);
        return MapperUtil.map(userEntity, OrganizationDto.class);
    }
    @Override
    public OrganizationDto update(Integer id, OrganizationDto orgDto){
        Optional<Organization> currentOrg = orgRepository.findById(id);
        if(currentOrg.isEmpty()){
            throw new NotFoundException("Org does not exists: " + orgDto);
        }
        Organization organization = currentOrg.get();
        Person personEntity = organization.getLegalRepresentationPerson();
        preparePersonEntity(orgDto.getLegalRepresentation(), personEntity);
        prepareOrgEntity(orgDto, organization);
        orgRepository.save(organization);
        return MapperUtil.map(organization, orgDto.getClass());
    }

    private void prepareOrgEntity(OrganizationDto orgDto, Organization orgEntity) {
        orgEntity.setDescription(orgDto.getDescription());
        orgEntity.setName(orgDto.getName());
        orgEntity.setRuc(orgDto.getRuc());
        orgEntity.setUpdatedAt(LocalDateTime.now());
    }
    
    @Override
    public Boolean rucAlreadyExists(String ruc) {
        Optional<Organization> org = orgRepository.
                findByRuc(ruc);
        return org.isPresent();
    }

    @Override
    public OrganizationDto getById(Integer id){
        Optional<Organization> user = orgRepository.findById(id);
        if(user.isEmpty()){
            throw new NotFoundException("Org Id does not exists: " + id);
        }
        return MapperUtil.map(user.get(), OrganizationDto.class);
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
            if(personRepository.findByDocumentNumber(personDto.getDocumentNumber()).isPresent()){
                throw new DuplicatedException("Document Number duplicated for " + personDto.getDocumentNumber());
            }
            personEntity.setDocumentNumber(personDto.getDocumentNumber());
        }
        personEntity.setLastName(personDto.getLastName());
        personEntity.setFirstName(personDto.getFirstName());
        personEntity.setPhoneNumber(personDto.getPhoneNumber());
        personEntity.setEmail(personDto.getEmail());
    }
}