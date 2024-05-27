package com.kfc.app.service.impl;

import com.kfc.app.controller.UserController;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.User;
import com.kfc.app.exception.DuplicatedUserException;
import com.kfc.app.exception.InvalidPasswordException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import com.kfc.app.dto.UserDto;
import com.kfc.app.repository.UserRepository;
import com.kfc.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = MapperUtil.map(userDto, User.class);
        if(userRepository.findUserByUsername(user.getUsername()).isPresent()){
            throw new DuplicatedUserException("Username duplicated for " + userDto.getUsername());
        }
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        user = userRepository.save(user);
        return MapperUtil.map(user, UserDto.class);
    }
    @Override
    public UserDto update(Integer id, UserDto userDto){
        Optional<User> existingEntity = userRepository.findById(id);
        if(existingEntity.isEmpty()){
            throw new NotFoundException("User does not exists: " + userDto);
        }
        User user = existingEntity.get();
        user.setPassword(userDto.getPassword());
        user.setUpdatedAt(LocalDate.now());
        userRepository.save(user);
        return MapperUtil.map(user, userDto.getClass());
        
    }

    @Override
    public UserDto getByUsernameAndPassword(UserDto userDto) {
        Optional<User> user = userRepository.
                findUserByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());
        if(user.isEmpty()) {
            throw new InvalidPasswordException("Username: " + userDto.getUsername());
        }
        return MapperUtil.map(user.get(), UserDto.class);
    }

    @Override
    public Boolean existUsername(String username) {
        Optional<User> user = userRepository.
                findUserByUsername(username);
        return user.isPresent();
    }

    @Override
    public UserDto getById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new NotFoundException("User Id does not exists: " + id);
        }
        return MapperUtil.map(user.get(), UserDto.class);
    }
    @Override
    public ResultPageWrapper<UserDto> getAll(Pageable paging){
        Page<User> users = userRepository.findAll(paging);
        if(users.isEmpty()){
            throw  new NotFoundException("Users does not exists");
        }
        return PaginationUtil.prepareResultWrapper(users, UserDto.class);
    }
}
