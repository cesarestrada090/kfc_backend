package com.kfc.app.service;


import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import com.kfc.app.exception.DuplicatedException;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto save(UserDto userDto);
    UserDto update(Integer id, UserDto dto);

    UserDto getByUsernameAndPassword(UserDto userDto);
    Boolean usernameAlreadyExists(String username);
    UserDto getById(Integer id);
    User getUserEntityById(Integer id);
    User getOrCreateUserEntityByDto(UserDto userDto, Person person);
    ResultPageWrapper<UserDto> getAll(Pageable paging);
}
