package com.kfc.app.controller;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import com.kfc.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/app")
public class UserController {
    private final UserService userService;
    private static final Logger log = Logger.getLogger(UserController.class.getName());
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="user/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<UserDto> authenticate(@Valid @RequestBody UserDto userDto){
        try {
            log.info("Request: " + userDto);
            userDto = userService.getUserByUserAndPassword(userDto);
        } catch (Exception e){
            return new ResponseEntity<>(userDto, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value="user/{id}", method = RequestMethod.GET, consumes = {"*/*"}, produces = "application/json")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") Integer id){
        UserDto userDto = null;
        try {
            log.info("Request by id: " + id);
            userDto = userService.getUserById(id);
        } catch (Exception e){
            return new ResponseEntity<>(userDto, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @RequestMapping(value="user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto){
        try {
            log.info("Request: " + userDto);
            userDto = userService.save(userDto);
        } catch (Exception e){
            return new ResponseEntity<>(userDto, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping(value="user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update(@PathVariable(value = "id") int id, @Valid @RequestBody UserDto userDto){
        try {
            userService.update(id, userDto);
        } catch (IllegalArgumentException e){
            log.info("Excepcion en: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="user")
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Map<String, Object> response = new HashMap<>();
        try {
            Pageable paging = PageRequest.of(page-1, size);
            ResultPageWrapper<UserDto> resultPageWrapper = userService.getAll(paging);
            response.put("users", resultPageWrapper.getPagesResult());
            response.put("currentPage", resultPageWrapper.getCurrentPage());
            response.put("totalItems", resultPageWrapper.getTotalItems());
            response.put("totalPages", resultPageWrapper.getTotalPages());
        } catch (Exception e){
            log.info("Excepcion en: "+e.getMessage());
return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
