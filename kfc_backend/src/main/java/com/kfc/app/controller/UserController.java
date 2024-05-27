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

import java.util.Map;
import java.util.logging.Logger;

@RequestMapping("v1/app/user")
@RestController
public class UserController extends AbstractController {
    private final UserService userService;
    private static final Logger log = Logger.getLogger(UserController.class.getName());
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value="/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<UserDto> authenticate(@Valid @RequestBody UserDto userDto){
        log.info("Login request: " + userDto);
        userDto = userService.getByUsernameAndPassword(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") Integer id){
        UserDto userDto = userService.getById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto){
        userDto = userService.save(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update(@PathVariable(value = "id") int id, @Valid @RequestBody UserDto userDto){
        userService.update(id, userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
        Pageable paging = PageRequest.of(page-1, size);
        ResultPageWrapper<UserDto> resultPageWrapper = userService.getAll(paging);
        Map<String, Object> response = prepareResponse(resultPageWrapper);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Override
    protected String getResource() {
        return "users";
    }
}
