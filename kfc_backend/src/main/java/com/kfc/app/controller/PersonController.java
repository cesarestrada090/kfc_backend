package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.PersonDto;
import com.kfc.app.service.PersonService;
import com.kfc.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RequestMapping("v1/app/person")
@RestController
public class PersonController extends AbstractController {
    private final PersonService personService;
    private static final Logger log = Logger.getLogger(PersonController.class.getName());
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    
    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<PersonDto> getUserById(@PathVariable(value = "id") Integer id){
        PersonDto personDto = personService.getById(id);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<PersonDto> save(@Valid @RequestBody PersonDto personDto){
        personDto = personService.save(personDto);
        return new ResponseEntity<>(personDto, HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDto> update(@PathVariable(value = "id") int id, @Valid @RequestBody PersonDto personDto){
        personDto = personService.update(id, personDto);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<PersonDto> resultPageWrapper = personService.getAll(paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Override
    protected String getResource() {
        return "persons";
    }
}
