package com.kfc.app.service;

import com.kfc.app.dto.BrandDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Brand;
import org.springframework.data.domain.Pageable;

public interface BrandService {

    BrandDto save(BrandDto brandDto);

    BrandDto update(Integer id, BrandDto dto);

    Boolean brandAlreadyExists(String ruc);

    BrandDto getById(Integer id);

    ResultPageWrapper<BrandDto> getAll(Pageable paging);

    Brand getBrandEntityById(Integer brandId);
}
