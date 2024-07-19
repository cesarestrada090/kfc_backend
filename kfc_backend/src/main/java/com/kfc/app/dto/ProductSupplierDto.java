package com.kfc.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kfc.app.entities.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ProductSupplierDto implements Serializable {

    private Integer id;
    private List<ProductDto> product;
    private SupplierDto supplier;
    private OrganizationDto organization;
    private Double cost;
    private Double discount;
    private Integer deliveryTime;
    private Integer minLimitQuantity;
    private String paymentConditions;
    private boolean status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private UserDto createdBy;
    private UserDto updatedBy;


    public ProductSupplierDto() {
    }

    public ProductSupplierDto(ProductSupplier productSupplier) {
        this.cost = productSupplier.getCost();
        this.discount = productSupplier.getDiscount();
        this.deliveryTime = productSupplier.getDeliveryTime();
        this.minLimitQuantity = productSupplier.getMinLimitQuantity();
        this.paymentConditions = productSupplier.getPaymentConditions();
        this.status = productSupplier.getStatus();
        this.createdAt = productSupplier.getCreatedAt();
        this.updatedAt = productSupplier.getUpdatedAt();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ProductDto> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDto> product) {
        this.product = product;
    }

    public SupplierDto getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDto supplier) {
        this.supplier = supplier;
    }

    public OrganizationDto getOrganization() {
        return organization;
    }

    public void setOrganizationId(OrganizationDto organization) {
        this.organization = organization;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getMinLimitQuantity() {
        return minLimitQuantity;
    }

    public void setMinLimitQuantity(Integer minLimitQuantity) {
        this.minLimitQuantity = minLimitQuantity;
    }

    public String getPaymentConditions() {
        return paymentConditions;
    }

    public void setPaymentConditions(String paymentConditions) {
        this.paymentConditions = paymentConditions;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
