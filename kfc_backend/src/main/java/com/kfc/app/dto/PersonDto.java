package com.kfc.app.dto;

import com.kfc.app.entities.Person;
import com.kfc.app.entities.User;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link User}
 */
public class PersonDto implements Serializable {
    private Integer id;
    @Size(max = 45)
    private String firstName;
    @Size(max = 45)
    private String lastName;
    private String documentNumber;
    private String phoneNumber;
    private String email;
    public PersonDto() {
    }
    public PersonDto(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getLastName();
        this.documentNumber = person.getDocumentNumber();
        this.phoneNumber = person.getPhoneNumber();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasDifferentDocumentNumber(String documentNumber){
        return this.getDocumentNumber()!= null && !this.getDocumentNumber().equals(documentNumber);
    }
}