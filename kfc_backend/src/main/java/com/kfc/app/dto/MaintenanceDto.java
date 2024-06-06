package com.kfc.app.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MaintenanceDto implements Serializable {
    private Integer id;  
    private Integer unitId;
    private UnitDto unitDto;
    private WorkshopDto workshopDto;
    private LocalDateTime maintenanceDate;
    private String description;
    private boolean completed;
    private UserDto createdBy;
    private UserDto lastUpdatedBy;
    public MaintenanceDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public UnitDto getUnitDto() {
        return unitDto;
    }

    public void setUnitDto(UnitDto unitDto) {
        this.unitDto = unitDto;
    }

    public WorkshopDto getWorkshopDto() {
        return workshopDto;
    }

    public void setWorkshopDto(WorkshopDto workshopDto) {
        this.workshopDto = workshopDto;
    }

    public LocalDateTime getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDateTime maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public UserDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDto createdBy) {
        this.createdBy = createdBy;
    }

    public UserDto getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(UserDto lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}