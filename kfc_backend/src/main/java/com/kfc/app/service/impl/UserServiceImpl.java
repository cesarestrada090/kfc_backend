package com.kfc.app.service.impl;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.User;
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

import java.util.Optional;

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
        userRepository.save(user);
        return MapperUtil.map(user, UserDto.class);
    }
    @Override
    public UserDto update(Integer id, UserDto userDto){
        if(userRepository.findById(id).isPresent()){
            User existingEntity = userRepository.findById(id).get();
            existingEntity.setPassword(userDto.getPassword());
            userRepository.save(existingEntity);
            return MapperUtil.map(existingEntity, userDto.getClass());
        }
        throw new NotFoundException("User does not exists: "+ userDto);
    }

    @Override
    public UserDto getUserByUserAndPassword(UserDto userDto) {
        Optional<User> user = userRepository.
                findUserByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());
        if(user.isEmpty()) {
            throw new NotFoundException("Incorrect password for user: " + userDto);
        }
        return MapperUtil.map(user.get(), UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new NotFoundException("User Id does not exists: " + id);
        }
        return MapperUtil.map(user, UserDto.class);
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
