package com.kfc.app.service;

import com.kfc.app.dto.ProductDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Product;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDto save(ProductDto productDto);

    ProductDto getById(Integer id);

    ResultPageWrapper<ProductDto> findByOrganizationId(Integer userId, Pageable paging);

    ResultPageWrapper<ProductDto> getAll(Pageable paging);

    Boolean nameAlreadyExists (ProductDto productDto);
    
    Product getProductEntityById(Integer id);

    ProductDto update(Integer id, ProductDto dto);

}
