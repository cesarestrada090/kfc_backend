package com.kfc.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kfc.app.entities.UserWarehouse;

public class UserWarehouseDto {
    private Integer userId;
    private String userName; // Username of the associated user
    private Integer warehouseId;
    private String warehouseName; // Name of the associated warehouse
    public UserWarehouseDto() {
    }
    
    public UserWarehouseDto(UserWarehouse userWarehouse) {
        this.userId = userWarehouse.getUser().getId();
        this.userName = userWarehouse.getUser().getUsername();
        this.warehouseId = userWarehouse.getWarehouse().getId();
        this.warehouseName = userWarehouse.getWarehouse().getName();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}