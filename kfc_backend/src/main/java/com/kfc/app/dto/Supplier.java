package com.kfc.app.dto;

import java.io.Serializable;
import java.util.List;

public class Supplier implements Serializable {
    private Integer supplierId;
    private String identifierNumber; //RUC
    private String supplierName;
    private String address;
    private String postalCode;
    private String email;
    private List<PersonDto> contacto;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getIdentifierNumber() {
        return identifierNumber;
    }

    public void setIdentifierNumber(String identifierNumber) {
        this.identifierNumber = identifierNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PersonDto> getContacto() {
        return contacto;
    }

    public void setContacto(List<PersonDto> contacto) {
        this.contacto = contacto;
    }
}
