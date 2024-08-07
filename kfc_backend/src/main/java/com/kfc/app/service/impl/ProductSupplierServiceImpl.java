package com.kfc.app.service.impl;

import com.kfc.app.dto.ProductDto;
import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.*;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.OrganizationRepository;
import com.kfc.app.repository.ProductSupplierRepository;
import com.kfc.app.service.*;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSupplierServiceImpl implements ProductSupplierService {

    private final ProductSupplierRepository productSupplierRepository;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final OrganizationRepository orgRepository;
    private final UserService userService;
    private final OrgService orgService;

    public ProductSupplierServiceImpl (ProductSupplierRepository productSupplierRepository,
                                      OrganizationRepository orgRepository,
                                      UserService userService,
                                      SupplierService supplierService,
                                      ProductService productService,
                                      OrgService orgService) {
        this.productSupplierRepository = productSupplierRepository;
        this.orgRepository = orgRepository;
        this.userService = userService;
        this.orgService = orgService;
        this.supplierService = supplierService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public ProductSupplierDto save(ProductSupplierDto productSupplierDto) {
        Organization organization = orgService.getOrgEntityById(productSupplierDto.getOrganization().getId());
        User user = userService.getUserEntityById(productSupplierDto.getCreatedBy().getId());
        Supplier supplier = supplierService.getSupplierEntityById(productSupplierDto.getSupplier().getId());
        ProductSupplier productSupplier = MapperUtil.map(productSupplierDto, ProductSupplier.class);
        productSupplier.setStatus(true);
        productSupplier.setOrganization(organization);
        productSupplier.setSupplier(supplier);
        productSupplier.setCreatedAt(LocalDateTime.now());
        productSupplier.setUpdatedAt(LocalDateTime.now());
        productSupplier.setCreatedBy(user);
        productSupplier.setUpdatedBy(user);
        for (ProductDto prod: productSupplierDto.getProduct()) {
            if (getProductSupplier(productSupplierDto.getSupplier().getId(), prod.getId(),
                    productSupplierDto.getOrganization().getId()) == null) {
                Product product = productService.getProductEntityById(prod.getId());
                productSupplier.setProduct(product);
                productSupplier = productSupplierRepository.save(productSupplier);
            }
        }
        return MapperUtil.map(productSupplier, ProductSupplierDto.class);
    }

    @Override
    @Transactional
    public ProductSupplierDto update(Integer id, ProductSupplierDto productSupplierDto){
        Optional<ProductSupplier> productSupplierOpt = productSupplierRepository.findById(id);
        if (productSupplierOpt.isEmpty()) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        ProductSupplier productSupplier = productSupplierOpt.get();
        User user = userService.getUserEntityById(productSupplierDto.getCreatedBy().getId());
        Supplier supplier = supplierService.getSupplierEntityById(productSupplierDto.getSupplier().getId());
        Organization organization = orgService.getOrgEntityById(productSupplierDto.getOrganization().getId());
//        Product product = productService.getProductEntityById(productSupplierDto.getProduct().getId());
        productSupplier.setSupplier(supplier);
        productSupplier.setOrganization(organization);
//        productSupplier.setProduct(product);
        productSupplier.setCost(productSupplierDto.getCost());
        productSupplier.setDiscount(productSupplierDto.getDiscount());
        productSupplier.setDeliveryTime(productSupplierDto.getDeliveryTime());
        productSupplier.setPaymentConditions(productSupplierDto.getPaymentConditions());
        productSupplier.setStatus(productSupplierDto.getStatus());
        productSupplier.setUpdatedAt(LocalDateTime.now());
        productSupplier.setUpdatedBy(user);
        productSupplier = productSupplierRepository.save(productSupplier);
        return MapperUtil.map(productSupplier, ProductSupplierDto.class);
    }

    @Override
    public ProductSupplier getProductSupplier(Integer supplierId, Integer productId, Integer orgId) {
        Optional<ProductSupplier> productSupplier =
                productSupplierRepository.findBySupplierIdAndProductIdAndOrganizationId(supplierId, productId, orgId);

        return (productSupplier.isPresent()) ? productSupplier.get() : null;
    }

    @Override
    public ProductSupplierDto getById(Integer id){
        Optional<ProductSupplier> productSupplier = productSupplierRepository.findById(id);
        if(productSupplier.isEmpty()){
            throw new NotFoundException("Product Id does not exists: " + id);
        }
        return MapperUtil.map(productSupplier.get(), ProductSupplierDto.class);
    }

    @Override
    public ResultPageWrapper<ProductSupplierDto> findByOrganizationId(Integer orgId, Pageable paging) {
        Page<ProductSupplier> productSuppliers = productSupplierRepository.findByOrganizationId(orgId, paging);
        if(productSuppliers.isEmpty()){
            throw new NotFoundException("There are not products for this organization.");
        }
        return PaginationUtil.prepareResultWrapper(productSuppliers, ProductSupplierDto.class);
    }

    @Override
    public List<ProductSupplierDto> findAllProductsBySupplierId(Integer orgId, Integer supplierId) {
        Optional<List<ProductSupplier>> productSuppliers = productSupplierRepository.findByOrganizationIdAndSupplierId(orgId, supplierId);
//        if(productSuppliers.isEmpty()){
//            throw new NotFoundException("There are not products for this organization.");
//        }
        return MapperUtil.mapAll(productSuppliers.get(), ProductSupplierDto.class);
    }

}
