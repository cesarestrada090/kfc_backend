package com.kfc.app.service.impl;

import com.kfc.app.dto.ProductTypeDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.ProductType;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.ProductTypeRepository;
import com.kfc.app.service.ProductTypeService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    @Transactional
    public ProductTypeDto save(ProductTypeDto productTypeDto) {
        ProductType productType = MapperUtil.map(productTypeDto, ProductType.class);
        if (this.productTypeAlreadyExists(productTypeDto.getName(), productTypeDto.getCategory())){
            throw new DuplicatedException("Product type name duplicated for " + productTypeDto.getName());
        }
        productType = productTypeRepository.save(productType);
        return MapperUtil.map(productType, ProductTypeDto.class);
    }

    @Override
    public ProductTypeDto getById(Integer id){
        Optional<ProductType> productType = productTypeRepository.findById(id);
        if(productType.isEmpty()){
            throw new NotFoundException("Product type Id does not exists: " + id);
        }
        return MapperUtil.map(productType.get(), ProductTypeDto.class);
    }

    @Override
    public ProductType getProductTypeEntityById(Integer id) {
        Optional<ProductType> optProductType = productTypeRepository.findById(id);
        if(optProductType.isPresent()){
            return optProductType.get();
        } else {
            throw new NotFoundException("Product type not found with id: " + id);
        }
    }

    @Override
    public Boolean productTypeAlreadyExists(String name, String category) {
        Optional<ProductType> productType = productTypeRepository.
                findByNameAndCategory(name, category);
        return productType.isPresent();
    }

    @Override
    @Transactional
    public ProductTypeDto update(Integer id, ProductTypeDto productTypeDto){
        ProductType productTypeEntity = getProductTypeEntityById(id);
        if(productTypeEntity == null){
            throw new NotFoundException("Product type does not exists: " + productTypeDto);
        }
        if (!productTypeDto.hasDifferentName(productTypeEntity.getName())) {
            throw new DuplicatedException("Product type name duplicated for " + productTypeDto.getName());
        }
        productTypeEntity.setName(productTypeDto.getName());
        productTypeEntity.setCategory(productTypeDto.getCategory());

        productTypeRepository.save(productTypeEntity);
        return MapperUtil.map(productTypeEntity, productTypeDto.getClass());
    }

    @Override
    public ResultPageWrapper<ProductTypeDto> getAll(Pageable paging){
        Page<ProductType> productTypeList = productTypeRepository.findAll(paging);
        if(productTypeList.isEmpty()){
            throw  new NotFoundException("Product type does not exists");
        }
        return PaginationUtil.prepareResultWrapper(productTypeList, ProductTypeDto.class);
    }
}
