package com.kfc.app.service.impl;

import com.kfc.app.dto.BrandDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Brand;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.BrandRepository;
import com.kfc.app.service.BrandService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional
    public BrandDto save(BrandDto brandDto) {
        Brand brand = MapperUtil.map(brandDto, Brand.class);
        if (this.brandAlreadyExists(brandDto.getName())){
            throw new DuplicatedException("Brand name duplicated for " + brandDto.getName());
        }
        brand = brandRepository.save(brand);
        return MapperUtil.map(brand, BrandDto.class);
    }

    @Override
    public BrandDto getById(Integer id){
        Optional<Brand> brand = brandRepository.findById(id);
        if(brand.isEmpty()){
            throw new NotFoundException("Brand Id does not exists: " + id);
        }
        return MapperUtil.map(brand.get(), BrandDto.class);
    }

    @Override
    public Brand getBrandEntityById(Integer id) {
        Optional<Brand> optBrand = brandRepository.findById(id);
        if(optBrand.isPresent()){
            return optBrand.get();
        } else {
            throw new NotFoundException("Brand not found with id: " + id);
        }
    }

    @Override
    public Boolean brandAlreadyExists(String name) {
        Optional<Brand> brand = brandRepository.
                findByName(name);
        return brand.isPresent();
    }

    @Override
    @Transactional
    public BrandDto update(Integer id, BrandDto brandDto){
        Brand brandEntity = getBrandEntityById(id);
        if(brandEntity == null){
            throw new NotFoundException("Brand does not exists: " + brandDto);
        }
        if (!brandDto.hasDifferentName(brandEntity.getName())) {
            throw new DuplicatedException("Brand name duplicated for " + brandDto.getName());
        }
        brandEntity.setName(brandDto.getName());

        brandRepository.save(brandEntity);
        return MapperUtil.map(brandEntity, brandDto.getClass());
    }

    @Override
    public ResultPageWrapper<BrandDto> getAll(Pageable paging){
        Page<Brand> brandList = brandRepository.findAll(paging);
        if(brandList.isEmpty()){
            throw  new NotFoundException("Brand does not exists");
        }
        return PaginationUtil.prepareResultWrapper(brandList, BrandDto.class);
    }
}
