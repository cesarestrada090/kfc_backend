package com.kfc.app.dto;

import com.kfc.app.entities.Organization;

import java.io.Serializable;
import java.time.LocalDateTime;
public class OrganizationDto implements Serializable {

    private Long id;
    private String name;
    private String ruc;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PersonDto legalRepresentation;

    public OrganizationDto() {
    }

    public OrganizationDto(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.ruc = organization.getRuc();
        this.description = organization.getDescription();
        this.createdAt = organization.getCreatedAt();
        this.updatedAt = organization.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public PersonDto getLegalRepresentation() {
        return legalRepresentation;
    }

    public void setLegalRepresentation(PersonDto legalRepresentation) {
        this.legalRepresentation = legalRepresentation;
    }
}