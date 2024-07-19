package com.kfc.app.service;

import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.ProductSupplier;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductSupplierService {

    ProductSupplierDto save(ProductSupplierDto productSupplierDto);

    ProductSupplierDto getById(Integer id);

    ResultPageWrapper<ProductSupplierDto> findByOrganizationId(Integer orgId, Pageable paging);

    List<ProductSupplierDto> findAllProductsBySupplierId(Integer orgId, Integer supplierId);

    ProductSupplier getProductSupplier (Integer supplierId, Integer productId, Integer orgId);

    ProductSupplierDto update(Integer id, ProductSupplierDto dto);
}
