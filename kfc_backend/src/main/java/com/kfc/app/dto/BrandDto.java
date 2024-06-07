package com.kfc.app.dto;

import com.kfc.app.entities.Brand;
import java.io.Serializable;

public class BrandDto implements Serializable {
    private Integer id;
    private String name;

    public BrandDto () {

    }

    public BrandDto (Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
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

    public boolean hasDifferentName(String name){
        return this.getName() != null && !this.getName().equals(name);
    }
}
