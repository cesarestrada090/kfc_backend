package com.kfc.app.service;

import com.kfc.app.dto.BrandDto;
import com.kfc.app.dto.ProductDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Product;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto save(ProductDto productDto);

    ProductDto getById(Integer id);

    ResultPageWrapper<ProductDto> getAll(Pageable paging);

    Boolean nameAlreadyExists (ProductDto productDto);

//    ProductDto update(Integer id, ProductDto dto);
//
//    Boolean productAlreadyExists(String ruc);
//
//    Product getProductEntityById(Integer brandId);
}
