package com.kfc.app.service.impl;

import com.kfc.app.dto.ProductDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Product;
import com.kfc.app.entities.User;
import com.kfc.app.entities.Brand;
import com.kfc.app.entities.ProductType;
import com.kfc.app.entities.Organization;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.OrganizationRepository;
import com.kfc.app.repository.ProductRepository;
import com.kfc.app.service.UserService;
import com.kfc.app.service.BrandService;
import com.kfc.app.service.ProductTypeService;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.ProductService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
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
        Product product = MapperUtil.map(productDto, Product.class);
        product.setBrand(brand);
        product.setProductType(productType);
        product.setOrganization(organization);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setCreatedBy(user);
        product.setUpdatedBy(user);
        product = productRepository.save(product);
        return MapperUtil.map(product, ProductDto.class);
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

    @Override
    public ResultPageWrapper<ProductDto> findByOrganizationId(Integer orgId, Pageable paging) {
        Page<Product> products = productRepository.findByOrganizationId(orgId, paging);
        if(products.isEmpty()){
            throw new NotFoundException("There are not products for this organization.");
        }
        return PaginationUtil.prepareResultWrapper(products, ProductDto.class);
    }

    @Override
    public Boolean nameAlreadyExists(ProductDto productDto) {
        Optional<Product> product = productRepository.
                findByNameAndOrganizationId(productDto.getName(), productDto.getOrganization().getId());
        return product.isPresent();
    }
    
    public Product getProductEntityById(Integer productId){
        return productRepository.findById(productId).orElseThrow(()-> new NotFoundException("Not found Product with id:" + productId));
    }

}
