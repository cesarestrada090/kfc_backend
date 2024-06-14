package com.kfc.app.service.impl;

import com.kfc.app.dto.*;
import com.kfc.app.entities.*;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.OrganizationRepository;
import com.kfc.app.repository.ProductRepository;
import com.kfc.app.service.*;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrganizationRepository orgRepository;
    private final UserService userService;
    private final BrandService brandService;
    private final ProductTypeService productTypeService;
    private final OrgService orgService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              OrganizationRepository orgRepository,
                              UserService userService,
                              BrandService brandService,
                              ProductTypeService productTypeService,
                              OrgService orgService) {
        this.productRepository = productRepository;
        this.orgRepository = orgRepository;
        this.userService = userService;
        this.brandService = brandService;
        this.productTypeService = productTypeService;
        this.orgService = orgService;
    }

    @Override
    @Transactional
    public ProductDto save(ProductDto productDto) {
        User user = userService.getUserEntityById(productDto.getCreatedBy().getId());
        Brand brand = brandService.getBrandEntityById(productDto.getBrand().getId());
        ProductType productType = productTypeService.getProductTypeEntityById(productDto.getProductType().getId());
        Organization organization = orgService.getOrgEntityById(productDto.getOrganization().getId());
        if (this.nameAlreadyExists(productDto)) {
            throw new DuplicatedException("Product name duplicated for " + productDto.getName());
        }
        Product productEntity = MapperUtil.map(productDto, Product.class);
        productEntity.setProductCode(productDto.getProductCode());
        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setWarranty(productDto.getWarranty());
        productEntity.setWeight(productDto.getWeight());
        productEntity.setDimensions(productDto.getDimensions());
        productEntity.setSerialNumber(productDto.getSerialNumber());
        productEntity.setBarCode(productDto.getBarCode());
        productEntity.setExpirationTime(productDto.getExpirationTime());
        productEntity.setBrand(brand);
        productEntity.setProductType(productType);
        productEntity.setOrganization(organization);
        productEntity.setManufacturingDate(productDto.getManufacturingDate());
        productEntity.setCreatedAt(LocalDateTime.now());
        productEntity.setCreatedBy(user);
        productEntity = productRepository.save(productEntity);
        return MapperUtil.map(productEntity, ProductDto.class);
    }

    @Override
    public ProductDto getById(Integer id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new NotFoundException("Product Id does not exists: " + id);
        }
        return MapperUtil.map(product.get(), ProductDto.class);
    }

    @Override
    public ResultPageWrapper<ProductDto> getAll(Pageable paging){
        Page<Product> productList = productRepository.findAll(paging);
        if(productList.isEmpty()){
            throw  new NotFoundException("Product does not exists");
        }
        return PaginationUtil.prepareResultWrapper(productList, ProductDto.class);
    }

//    @Override
//    public ProductDto findByNameInOrganization(String productname, ){
//        Optional<Product> product = productRepository.findByDocumentNumber(documentNumber);
//        return person.map(value -> MapperUtil.map(value, PersonDto.class)).orElse(null);
//    }

    @Override
    public Boolean nameAlreadyExists(ProductDto productDto) {
        Optional<Product> product = productRepository.
                findByNameAndOrganizationId(productDto.getName(), productDto.getOrganization().getId());
        return product.isPresent();
    }

}
