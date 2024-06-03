package com.kfc.app.service.impl;

import com.kfc.app.dto.OrganizationDto;
import com.kfc.app.dto.PersonDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Organization;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.InvalidPasswordException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.PersonService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import com.kfc.app.dto.UserDto;
import com.kfc.app.repository.UserRepository;
import com.kfc.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonService personService;
    private final OrgService orgService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PersonService personService, 
                           OrgService orgService) {
        this.userRepository = userRepository;
        this.personService = personService;
        this.orgService = orgService;
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        if (this.usernameAlreadyExists(userDto.getUsername())){
            throw new DuplicatedException("Username duplicated for " + userDto.getUsername());
        }
        Person personEntity = personService.getOrCreatePersonEntity(userDto.getPerson());
        Organization orgEntity = orgService.getOrCreateOrgEntity(userDto.getOrganization());
        
        // user creation
        User user = createUserEntityByDto(userDto, personEntity, orgEntity);
        user = userRepository.save(user);
        
        return MapperUtil.map(user, UserDto.class);
    }
    @Override
    @Transactional
    public UserDto update(Integer id, UserDto userDto){
        // validate different username
        User userEntity = this.getUserEntityById(id);
        if (userDto.hasDifferentUserName(userEntity.getUsername())) {
            if(usernameAlreadyExists(userDto.getUsername())){
                throw new DuplicatedException("Username duplicated for " + userDto.getUsername());
            }
        }
        
        // validate different document number for user person
        PersonDto personDto = userDto.getPerson();
        Person personEntity = personService.getPersonEntityById(personDto.getId());
        if (personDto.hasDifferentDocumentNumber(personEntity.getDocumentNumber())) {
            if(personService.findByDocumentNumber(personDto.getDocumentNumber()) != null){
                throw new DuplicatedException("Username duplicated for " + personDto.getDocumentNumber());
            }
        }
        
        // validate different RUC for Organization
        OrganizationDto orgDto = userDto.getOrganization();
        Organization orgEntity = orgService.getOrgEntityById(orgDto.getId());
        if(orgDto.hasDifferentRUC(orgEntity.getRuc())){
            if(orgService.rucAlreadyExists(orgDto.getRuc())){
                throw new DuplicatedException("RUC duplicated for " + userDto.getUsername());
            }
        }

        // update organization
        if(orgDto.getDescription() != null){
            orgEntity.setDescription(orgDto.getDescription());
            orgEntity.setRuc(orgDto.getRuc());
            orgEntity.setName(orgDto.getName());
            orgEntity.setUpdatedAt(LocalDateTime.now());
        }
        
        
        // update user person
        if(personDto.getFirstName() != null) {
            personEntity.setFirstName(personDto.getFirstName());
            personEntity.setLastName(personDto.getLastName());
            personEntity.setEmail(personDto.getEmail());
            personEntity.setPhoneNumber(personDto.getPhoneNumber());
            personEntity.setDocumentNumber(personDto.getDocumentNumber());
        }
        // update User
        userEntity.setPerson(personEntity);
        userEntity.setOrganization(orgEntity);
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setUpdatedAt(LocalDateTime.now());
        
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

    public User getUserEntityById(Integer usedId) {
        Optional<User> existingUser = userRepository.findById(usedId);
        return existingUser.orElseThrow(() -> new NotFoundException("User not found with id: " + usedId));
    }
    
    public User getOrCreateUserEntityByDto(UserDto userDto, Person person, Organization organization) {
        User user = null;
        // If user does not have id, we should create new Person
        if(userDto.getId() == null){
            // Check if the new user have an existing username
            if(this.usernameAlreadyExists(userDto.getUsername())){
                throw new DuplicatedException("Username duplicated for " + userDto.getUsername());
            }
            user = this.createUserEntityByDto(userDto, person, organization);
        } else {
            // otherwise get the user from db
            user = this.getUserEntityById(userDto.getId());
        }
        return user;
    }

    private User createUserEntityByDto(UserDto userDto, Person person, Organization organization){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setOrganization(organization);
        user.setPerson(person);
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
