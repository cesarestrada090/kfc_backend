package com.kfc.app.service;

import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.ProductSupplier;
import org.springframework.data.domain.Pageable;

public interface ProductSupplierService {

    ProductSupplierDto save(ProductSupplierDto productSupplierDto);

//    ProductSupplierDto getById(Integer id);
//
//    ResultPageWrapper<ProductSupplierDto> findByOrganizationId(Integer userId, Pageable paging);
//
//    ResultPageWrapper<ProductSupplierDto> getAll(Pageable paging);
//
//    Boolean nameAlreadyExists (ProductSupplierDto productSupplierDto);
//
//    ProductSupplier getProductEntityById(Integer id);
//
//    ProductSupplierDto update(Integer id, ProductSupplierDto dto);
}
