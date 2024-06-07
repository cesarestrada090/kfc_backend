package com.kfc.app.dto;

import com.kfc.app.entities.ProductType;
import java.io.Serializable;

public class ProductTypeDto implements Serializable {

    private Integer id;

    private String name;

    private String category;

    public ProductTypeDto () {
    }

    public ProductTypeDto (ProductType productType) {
        this.id = productType.getId();
        this.name = productType.getName();
        this.category = productType.getCategory();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean hasDifferentName(String name){
        return this.getName() != null && !this.getName().equals(name);
    }
}
