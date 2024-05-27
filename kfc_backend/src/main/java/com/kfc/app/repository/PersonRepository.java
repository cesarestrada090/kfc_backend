package com.kfc.app.repository;

import com.kfc.app.dto.PersonDto;
import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByDocumentNumber(String username);
}