package com.kfc.app.service.impl;

import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.dto.SupplierDto;
import com.kfc.app.entities.*;
import com.kfc.app.exception.DuplicatedException;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.SupplierRepository;
import com.kfc.app.service.OrgService;
import com.kfc.app.service.SupplierService;
import com.kfc.app.service.UserService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final UserService userService;
    private final OrgService orgService;
    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl (UserService userService,
                                SupplierRepository supplierRepository,
                                OrgService orgService) {
        this.userService = userService;
        this.orgService = orgService;
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional
    public SupplierDto save(SupplierDto supplierDto) {
        User user = userService.getUserEntityById(supplierDto.getCreatedBy().getId());
        Organization organization = orgService.getOrgEntityById(supplierDto.getOrganization().getId());
        if (this.nameAlreadyExists(supplierDto)) {
            throw new DuplicatedException("Supplier name duplicated for " + supplierDto.getName());
        }
        Supplier supplier = MapperUtil.map(supplierDto, Supplier.class);
        supplier.setOrganization(organization);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setCreatedBy(user);
        supplier.setUpdatedBy(user);
        supplier = supplierRepository.save(supplier);
        return MapperUtil.map(supplier, SupplierDto.class);

    }

    @Override
    @Transactional
    public SupplierDto update(Integer id, SupplierDto supplierDto){
        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isEmpty()) {
            throw new NotFoundException("Supplier not found with id: " + id);
        }
        Supplier supplier = supplierOpt.get();
        User user = userService.getUserEntityById(supplierDto.getCreatedBy().getId());
        Organization organization = orgService.getOrgEntityById(supplierDto.getOrganization().getId());

        supplier.setSupplierCode(supplierDto.getSupplierCode());
        supplier.setName(supplierDto.getName());
        supplier.setDescription(supplierDto.getDescription());
        supplier.setContact(supplierDto.getContact());
        supplier.setPositionContact(supplierDto.getPositionContact());
        supplier.setPostalCode(supplierDto.getPostalCode());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setProvince(supplierDto.getProvince());
        supplier.setCity(supplierDto.getCity());
        supplier.setCountry(supplierDto.getCountry());
        supplier.setTelephone(supplierDto.getTelephone());
        supplier.setFax(supplierDto.getFax());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setHomepage(supplierDto.getHomepage());
        supplier.setNotes(supplierDto.getNotes());
        supplier.setOrganization(organization);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplier.setUpdatedBy(user);
        supplier = supplierRepository.save(supplier);
        return MapperUtil.map(supplier, SupplierDto.class);
    }

    public Supplier getSupplierEntityById(Integer supplierId) {
        Optional<Supplier> existingOrg = supplierRepository.findById(supplierId);
        return existingOrg.orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
    }

    @Override
    public SupplierDto getById(Integer id){
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if(supplier.isEmpty()){
            throw new NotFoundException("Product Id does not exists: " + id);
        }
        return MapperUtil.map(supplier.get(), SupplierDto.class);
    }

    @Override
    public ResultPageWrapper<SupplierDto> getAll(Pageable paging){
        Page<Supplier> supplierList = supplierRepository.findAll(paging);
        if(supplierList.isEmpty()){
            throw  new NotFoundException("Product does not exists");
        }
        return PaginationUtil.prepareResultWrapper(supplierList, SupplierDto.class);
    }

    @Override
    public ResultPageWrapper<SupplierDto> findByOrganizationId(Integer orgId, Pageable paging) {
        Page<Supplier> suppliers = supplierRepository.findByOrganizationId(orgId, paging);
        if(suppliers.isEmpty()){
            throw new NotFoundException("There are not suppliers for this organization.");
        }
        return PaginationUtil.prepareResultWrapper(suppliers, SupplierDto.class);
    }

    @Override
    public Boolean nameAlreadyExists(SupplierDto supplierDto) {
        Optional<Supplier> supplier = supplierRepository.
                findByNameAndOrganizationId(supplierDto.getName(), supplierDto.getOrganization().getId());
        return supplier.isPresent();
    }
}
