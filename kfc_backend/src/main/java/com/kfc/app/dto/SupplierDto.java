package com.kfc.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kfc.app.entities.Supplier;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SupplierDto implements Serializable {

    private Integer id;
    private String supplierCode;
    private String name;
    private String description;
    private String contact;
    private String positionContact;
    private String postalCode;
    private String address;
    private String province;
    private String city;
    private String country;
    private String telephone;
    private String fax;
    private String email;
    private String homepage;
    private String notes;
    private OrganizationDto organization;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private UserDto createdBy;
    private UserDto updatedBy;

    public SupplierDto () {

    }

    public SupplierDto (Supplier supplier) {
        this.supplierCode = supplier.getSupplierCode();
        this.name = supplier.getName();
        this.description = supplier.getDescription();
        this.contact = supplier.getContact();
        this.positionContact = supplier.getPositionContact();
        this.postalCode = supplier.getPostalCode();
        this.address = supplier.getAddress();
        this.province = supplier.getProvince();
        this.city = supplier.getCity();
        this.country = supplier.getCountry();
        this.telephone = supplier.getTelephone();
        this.fax = supplier.getFax();
        this.email = supplier.getHomepage();
        this.homepage = supplier.getHomepage();
        this.notes = supplier.getNotes();
        this.createdAt = supplier.getCreatedAt();
        this.updatedAt = supplier.getUpdatedAt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPositionContact() {
        return positionContact;
    }

    public void setPositionContact(String positionContact) {
        this.positionContact = positionContact;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OrganizationDto getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDto organization) {
        this.organization = organization;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDto createdBy) {
        this.createdBy = createdBy;
    }

    public UserDto getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UserDto updatedBy) {
        this.updatedBy = updatedBy;
    }
}
