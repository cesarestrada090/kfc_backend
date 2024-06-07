package com.kfc.app.service;

import com.kfc.app.dto.ProductTypeDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Brand;
import com.kfc.app.entities.ProductType;
import org.springframework.data.domain.Pageable;

public interface ProductTypeService {

    ProductTypeDto save(ProductTypeDto productTypeDto);

    ProductTypeDto update(Integer id, ProductTypeDto dto);

    Boolean productTypeAlreadyExists(String name, String category);

    ProductTypeDto getById(Integer id);

    ResultPageWrapper<ProductTypeDto> getAll(Pageable paging);

    ProductType getProductTypeEntityById(Integer productTypeId);
}
