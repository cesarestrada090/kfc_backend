package com.kfc.app.service;


import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.UserDto;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto save(UserDto userDto);
    UserDto update(Integer id, UserDto dto);
    UserDto getByUsernameAndPassword(UserDto userDto);
    Boolean usernameAlreadyExistsByOrgId(String username, Integer orgId);
    UserDto getById(Integer id);
    User getUserEntityById(Integer id);
    User getOrCreateUserEntityByDto(UserDto userDto, Person person, Organization organization);
    ResultPageWrapper<UserDto> getAll(Pageable paging);
}
