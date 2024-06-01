package com.kfc.app.service.impl;

import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.InvalidPasswordException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.PersonRepository;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import com.kfc.app.dto.UserDto;
import com.kfc.app.repository.UserRepository;
import com.kfc.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

    @Override
    public UserDto save(UserDto userDto) {
        PersonDto personDto = userDto.getPerson();
        if (this.usernameAlreadyExists(userDto.getUsername())){
            throw new DuplicatedException("Username duplicated for " + userDto.getUsername());
        }
        if(personRepository.findByDocumentNumber(personDto.getDocumentNumber()).isPresent()){
            throw new DuplicatedException("Document number duplicated for " + personDto.getDocumentNumber());
        }
        Person personEntity = MapperUtil.map(personDto, Person.class);;
        User userEntity = MapperUtil.map(userDto, User.class);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
        userEntity.setPerson(personEntity);
        userEntity = userRepository.save(userEntity);
        return MapperUtil.map(userEntity, UserDto.class);
    }
    @Override
    public UserDto update(Integer id, UserDto userDto){
        Optional<User> currentUser = userRepository.findById(id);
        if(currentUser.isEmpty()){
            throw new NotFoundException("User does not exists: " + userDto);
        }
        User userEntity = currentUser.get();
        Person personEntity = userEntity.getPerson();
        preparePersonEntity(userDto.getPerson(), personEntity);
        prepareUserEntity(userDto, userEntity);
        userRepository.save(userEntity);
        return MapperUtil.map(userEntity, userDto.getClass());
    }

    @Override
    public UserDto getByUsernameAndPassword(UserDto userDto) {
        Optional<User> user = userRepository.
                findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());
        if(user.isEmpty()) {
            throw new InvalidPasswordException("Username: " + userDto.getUsername());
        }
        return MapperUtil.map(user.get(), UserDto.class);
    }

    @Override
    public Boolean usernameAlreadyExists(String username) {
        Optional<User> user = userRepository.
                findByUsername(username);
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
    private void prepareUserEntity(UserDto userDto, User userEntity) {
        // if you include username different from actual, 
        // we need to validate that the new one should not exist
        if(userDto.hasDifferentUserName(userEntity.getUsername()) ){
            if(usernameAlreadyExists(userDto.getUsername())){
                throw new DuplicatedException("Username duplicated for " + userDto.getUsername());
            }
            userEntity.setUsername(userDto.getUsername());
        }

        userEntity.setPassword(userDto.getPassword());
        userEntity.setUpdatedAt(LocalDateTime.now());
    }

    private void preparePersonEntity(PersonDto personDto, Person personEntity) {
        if(personDto.hasDifferentDocumentNumber(personEntity.getDocumentNumber()) ){
            if(personRepository.findByDocumentNumber(personDto.getDocumentNumber()).isPresent()){
                throw new DuplicatedException("Document Number duplicated for " + personDto.getDocumentNumber());
            }
            personEntity.setDocumentNumber(personDto.getDocumentNumber());
        }
        personEntity.setLastName(personDto.getLastName());
        personEntity.setFirstName(personDto.getFirstName());
        personEntity.setPhoneNumber(personDto.getPhoneNumber());
        personEntity.setEmail(personDto.getEmail());
    }

    public User getUserEntityById(Integer usedId) {
        Optional<User> existingUser = userRepository.findById(usedId);
        return existingUser.orElseThrow(() -> new NotFoundException("User not found with id: " + usedId));
    }
    
    public User getOrCreateUserEntityByDto(UserDto userDto, Person person) {
        User user = null;
        // If user does not have id, we should create new Person
        if(userDto.getId() == null){
            // Check if the new user have an existing username
            if(this.usernameAlreadyExists(userDto.getUsername())){
                throw new DuplicatedException("Username duplicated for " + userDto.getUsername());
            }
            user = this.createUserEntityByDto(userDto, person);
        } else {
            // otherwise get the user from db
            user = this.getUserEntityById(userDto.getId());
        }
        return user;
    }

    private User createUserEntityByDto(UserDto userDto, Person person){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPerson(person);
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
