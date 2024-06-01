package com.kfc.app.service.impl;

import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Person;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.PersonRepository;
import com.kfc.app.service.PersonService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public PersonDto save(PersonDto personDto) {
        Person person = MapperUtil.map(personDto, Person.class);
        if(personRepository.findByDocumentNumber(person.getDocumentNumber()).isPresent()){
            throw new DuplicatedException("Document number duplicated for " + personDto.getDocumentNumber());
        }
        person = personRepository.save(person);
        return MapperUtil.map(person, PersonDto.class);
    }
    @Override
    @Transactional
    public PersonDto update(Integer id, PersonDto personDto){
        Optional<Person> currentPerson = personRepository.findById(id);
        if(currentPerson.isEmpty()){
            throw new NotFoundException("User does not exists: " + personDto);
        }
        Person personEntity = currentPerson.get();
        // if you include document number different from actual, 
        // we need to validate that the new one should not exist
        if(personDto.hasDifferentDocumentNumber(currentPerson.get().getDocumentNumber()) ){
            if(findByDocumentNumber(personDto.getDocumentNumber()) != null){
                throw new DuplicatedException("Document Number duplicated for " + personDto.getDocumentNumber());
            }
            personEntity.setDocumentNumber(personDto.getDocumentNumber());
        }
        personEntity.setPhoneNumber(personDto.getPhoneNumber());
        personEntity.setEmail(personDto.getEmail());
        personEntity.setFirstName(personDto.getFirstName());
        personEntity.setLastName(personDto.getLastName());
        personRepository.save(personEntity);
        return MapperUtil.map(personEntity, personDto.getClass());
    }
    

    @Override
    public PersonDto findByDocumentNumber(String documentNumber){
        Optional<Person> person = personRepository.findByDocumentNumber(documentNumber);
        return person.map(value -> MapperUtil.map(value, PersonDto.class)).orElse(null);
    }

    @Override
    public PersonDto getById(Integer id){
        Optional<Person> person = personRepository.findById(id);
        if(person.isEmpty()){
            throw new NotFoundException("Person Id does not exists: " + id);
        }
        return MapperUtil.map(person.get(), PersonDto.class);
    }

    @Override
    public Person getPersonEntity(PersonDto personDto) {
        Optional<Person> optPerson = personRepository.findById(personDto.getId());
        if(optPerson.isPresent()){
            return optPerson.get();
        } else {
            throw new NotFoundException("Person not found with id: " + personDto.getId());
        }
    }

    @Override
    public ResultPageWrapper<PersonDto> getAll(Pageable paging){
        Page<Person> personList = personRepository.findAll(paging);
        if(personList.isEmpty()){
            throw  new NotFoundException("Person does not exists");
        }
        return PaginationUtil.prepareResultWrapper(personList, PersonDto.class);
    }

    public Person getOrCreatePersonEntity(PersonDto personDto){
        Person person = null;
        // If person does not have id, we should create new Person
        if (personDto.getId() == null) {
            // Check if the new Person is using duplicated document number
            if(this.findByDocumentNumber(personDto.getDocumentNumber()) != null){
                throw new DuplicatedException("Document Number duplicated for " + personDto.getDocumentNumber());
            }
            // Create Person Entity
            person = new Person();
            person.setFirstName(personDto.getFirstName());
            person.setLastName(personDto.getLastName());
            person.setEmail(personDto.getEmail());
            person.setDocumentNumber(personDto.getDocumentNumber());
            person.setPhoneNumber(personDto.getPhoneNumber());
        } else {
            // otherwise get Person Entity from DB
            person = this.getPersonEntity(personDto);
        }
        return person;
    }
}
