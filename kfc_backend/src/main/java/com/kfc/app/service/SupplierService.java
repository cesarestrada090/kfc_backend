package com.kfc.app.service;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.SupplierDto;
import org.springframework.data.domain.Pageable;

public interface SupplierService {

    SupplierDto save(SupplierDto supplierDto);

    SupplierDto getById(Integer id);

    ResultPageWrapper<SupplierDto> findByOrganizationId(Integer userId, Pageable paging);

    ResultPageWrapper<SupplierDto> getAll(Pageable paging);

    Boolean nameAlreadyExists (SupplierDto supplierDto);

    SupplierDto update(Integer id, SupplierDto dto);
}
