package com.kfc.app.service.impl;

import com.kfc.app.dto.PersonDto;
import com.kfc.app.entities.Person;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.repository.PersonRepository;
import com.kfc.app.service.PersonService;
import com.kfc.app.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    @BeforeEach
    public void setUp() {
        personService = new PersonServiceImpl(personRepository);
    }
    
    @Test
    public void testSavePerson_Success() throws Exception {
        PersonDto personDto = new PersonDto(1,"John", "Doe", "johndoe@mail.com", "1234567890", "123456789");
        // Mock repository behavior
        when(personRepository.findByDocumentNumber(any())).thenReturn(Optional.empty());
        when(personRepository.save(any(Person.class))).thenReturn(MapperUtil.map(personDto, Person.class));
        // Call the service method
        PersonDto savedPersonDto = personService.save(personDto);
        // Verify interactions and results
        verify(personRepository, times(1)).findByDocumentNumber(personDto.getDocumentNumber());
        verify(personRepository, times(1)).save(any(Person.class));
        assertEquals(personDto.getFirstName(), savedPersonDto.getFirstName());
        assertEquals(personDto.getLastName(), savedPersonDto.getLastName());
    }

    @Test
    public void testSavePerson_DocumentNumberDuplicated() throws Exception {
        PersonDto personDto = new PersonDto(1,"John", "Doe", "johndoe@mail.com", "1234567890", "123456789");
        when(personRepository.findByDocumentNumber(personDto.getDocumentNumber())).thenReturn(Optional.of(new Person()));
        assertThrows(DuplicatedException.class, () -> personService.save(personDto));
    }

    @Test
    public void testUpdatePerson_DocumentNumberDuplicated() throws Exception {
        Integer id = 1; // Replace with actual ID
        PersonDto originalPersonDto = new PersonDto(1,"John", "Doe", "johndoe@mail.com", "1234567890", "123456789");
        PersonDto updatedPersonDto = new PersonDto(1,"John", "Updated Doe", "updated.doe@mail.com", "9876543210", "123456789"); // Existing document number

        // Mock repository behavior
        when(personRepository.findById(id)).thenReturn(Optional.of(MapperUtil.map(originalPersonDto, Person.class)));
        when(personRepository.findByDocumentNumber(updatedPersonDto.getDocumentNumber())).thenReturn(Optional.of(new Person()));

        // Call the service method (expect exception)
        assertThrows(DuplicatedException.class, () -> personService.update(id, updatedPersonDto));
    }

    @Test
    public void testUpdatePerson_Success() throws Exception {
        Integer id = 1; // Replace with actual ID
        PersonDto originalPersonDto = new PersonDto(1,"John", "Doe", "johndoe@mail.com", "1234567890", "123456789");
        PersonDto updatedPersonDto = new PersonDto(1,"John", "Updated Doe", "updated.doe@mail.com", "9876543210", "9876543210"); // New document number

        // Mock repository behavior
        when(personRepository.findById(id)).thenReturn(Optional.of(MapperUtil.map(originalPersonDto, Person.class)));
        // No existing person found with the new document number
        when(personRepository.findByDocumentNumber(updatedPersonDto.getDocumentNumber())).thenReturn(Optional.empty());

        // Call the service method
        PersonDto savedPersonDto = personService.update(id, updatedPersonDto);

        // Verify updated data
        assertEquals(updatedPersonDto.getFirstName(), savedPersonDto.getFirstName());
        assertEquals(updatedPersonDto.getLastName(), savedPersonDto.getLastName());
        assertEquals(updatedPersonDto.getEmail(), savedPersonDto.getEmail());
        assertEquals(updatedPersonDto.getPhoneNumber(), savedPersonDto.getPhoneNumber());
        assertEquals(updatedPersonDto.getDocumentNumber(), savedPersonDto.getDocumentNumber());
    }
}