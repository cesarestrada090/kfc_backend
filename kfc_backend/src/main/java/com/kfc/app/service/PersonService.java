package com.kfc.app.service;


import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Person;
import org.springframework.data.domain.Pageable;

public interface PersonService {
    PersonDto save(PersonDto PersonDto);
    PersonDto update(Integer id, PersonDto dto);
    PersonDto findByDocumentNumber(String documentNumber);
    PersonDto getById(Integer id);
    Person getPersonEntityById(Integer personDto);
    Person getOrCreatePersonEntity(PersonDto personDto);
    ResultPageWrapper<PersonDto> getAll(Pageable paging);
}
