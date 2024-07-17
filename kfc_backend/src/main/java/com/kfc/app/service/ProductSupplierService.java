package com.kfc.app.service;

import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.ProductSupplier;
import org.springframework.data.domain.Pageable;

public interface ProductSupplierService {

    ProductSupplierDto save(ProductSupplierDto productSupplierDto);

    ProductSupplierDto getById(Integer id);

    ResultPageWrapper<ProductSupplierDto> findByOrganizationId(Integer orgId, Pageable paging);

    ResultPageWrapper<ProductSupplierDto> findAllProductsBySupplierId(Integer orgId, Integer supplierId, Pageable paging);

    ProductSupplier getProductSupplier (ProductSupplierDto productSupplierDto);

    ProductSupplierDto update(Integer id, ProductSupplierDto dto);
}
