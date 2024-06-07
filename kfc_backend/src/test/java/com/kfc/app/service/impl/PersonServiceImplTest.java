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
}