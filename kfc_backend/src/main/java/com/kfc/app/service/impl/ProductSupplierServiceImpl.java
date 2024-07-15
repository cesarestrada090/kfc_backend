package com.kfc.app.service.impl;

import com.kfc.app.dto.ProductSupplierDto;
import com.kfc.app.entities.*;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.repository.OrganizationRepository;
import com.kfc.app.repository.ProductRepository;
import com.kfc.app.repository.ProductSupplierRepository;
import com.kfc.app.repository.SupplierRepository;
import com.kfc.app.service.*;
import com.kfc.app.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        User user = userService.getUserEntityById(productSupplierDto.getCreatedBy().getId());
        Organization organization = orgService.getOrgEntityById(productSupplierDto.getOrganization().getId());
        Supplier supplier = supplierService.getSupplierEntityById(productSupplierDto.getSupplier().getId());
        Product product = productService.getProductEntityById(productSupplierDto.getProduct().getId());
//        if (this.nameAlreadyExists(productSupplierDto)) {
//            throw new DuplicatedException("Product name duplicated for " + productSupplierDto.getName());
//        }
        ProductSupplier productSupplier = MapperUtil.map(productSupplierDto, ProductSupplier.class);
        productSupplier.setOrganization(organization);
        productSupplier.setSupplier(supplier);
        productSupplier.setProduct(product);
        productSupplier.setCreatedAt(LocalDateTime.now());
        productSupplier.setUpdatedAt(LocalDateTime.now());
        productSupplier.setCreatedBy(user);
        productSupplier.setUpdatedBy(user);
        productSupplier = productSupplierRepository.save(productSupplier);
        return MapperUtil.map(productSupplier, ProductSupplierDto.class);
    }

}
