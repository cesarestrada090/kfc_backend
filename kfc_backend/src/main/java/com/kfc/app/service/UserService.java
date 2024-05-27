package com.kfc.app.service;


import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto save(UserDto userDto);
    UserDto update(Integer id, UserDto dto);

    UserDto getByUsernameAndPassword(UserDto userDto);

    Boolean usernameAlreadyExists(String username);
    UserDto getById(Integer id);
    ResultPageWrapper<UserDto> getAll(Pageable paging);
}
