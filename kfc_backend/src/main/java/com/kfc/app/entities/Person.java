package com.kfc.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 145)
    @Column(name = "first_name", length = 45)
    private String firstName;

    @Size(max = 145)
    @Column(name = "last_name", length = 45)
    private String lastName;

    @Size(max = 145)
    @Column(name = "document_number", length = 45)
    private String documentNumber;

    @Size(max = 145)
    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @Size(max = 145)
    @Column(name = "email", length = 145)
    private String email;

    public Person() {
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
}