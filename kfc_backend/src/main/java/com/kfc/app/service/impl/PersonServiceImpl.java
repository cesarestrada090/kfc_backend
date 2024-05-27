package com.kfc.app.service.impl;

import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Person;
import com.kfc.app.exception.DuplicatedUserException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.PersonRepository;
import com.kfc.app.service.PersonService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDto save(PersonDto personDto) {
        Person person = MapperUtil.map(personDto, Person.class);
        if(personRepository.findByDocumentNumber(person.getDocumentNumber()).isPresent()){
            throw new DuplicatedUserException("Document number duplicated for " + personDto.getDocumentNumber());
        }
        person = personRepository.save(person);
        return MapperUtil.map(person, PersonDto.class);
    }
    @Override
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
                throw new DuplicatedUserException("Document Number duplicated for " + personDto.getDocumentNumber());
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
    public ResultPageWrapper<PersonDto> getAll(Pageable paging){
        Page<Person> personList = personRepository.findAll(paging);
        if(personList.isEmpty()){
            throw  new NotFoundException("Person does not exists");
        }
        return PaginationUtil.prepareResultWrapper(personList, PersonDto.class);
    }
}
