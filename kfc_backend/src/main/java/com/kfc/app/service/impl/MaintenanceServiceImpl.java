package com.kfc.app.service.impl;

import com.kfc.app.dto.*;
import com.kfc.app.entities.*;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.MaintenanceDetailRepository;
import com.kfc.app.repository.MaintenanceRepository;
import com.kfc.app.service.*;
import com.kfc.app.util.MapperUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    
    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceDetailRepository maintenanceDetailRepository;
    private final UnitService unitService;
    private final UserService userService;
    
    private final ProductService productService;
    private final WorkshopService workshopService;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository,
                                  MaintenanceDetailRepository maintenanceDetailRepository,
                                  WorkshopService workshopService,
                                  UnitService unitService,
                                  UserService userService,
                                  ProductService productService) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceDetailRepository = maintenanceDetailRepository;
        this.workshopService = workshopService;
        this.unitService = unitService;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public MaintenanceDto save(MaintenanceDto dto) {
        Unit unit = unitService.getUnitEntityById(dto.getUnit().getId());
        Workshop workshop = workshopService.getWorkshopEntityById(dto.getWorkshop().getId());
        User user = userService.getUserEntityById(dto.getCreatedBy().getId());
        Maintenance maintenance = new Maintenance();
        maintenance.setUnit(unit);
        maintenance.setWorkshop(workshop);
        maintenance.setDescription(dto.getDescription());
        maintenance.setMaintenanceDate(dto.getMaintenanceDate());
        maintenance.setCompleted(dto.isCompleted());
        maintenance.setUpdatedAt(LocalDateTime.now());
        maintenance.setCreatedAt(LocalDateTime.now());
        maintenance.setLastUpdatedBy(user);
        maintenance.setCreatedBy(user);
        maintenance = maintenanceRepository.save(maintenance);
        for (int i = 0 ; i < dto.getMaintenanceDetails().size(); i++ ){
            MaintenanceDetailDto detailDto = dto.getMaintenanceDetails().get(i);
            MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
            maintenanceDetail.setQuantity(detailDto.getQuantity());
            maintenanceDetail.setDescription(detailDto.getDescription());
            // Product
            Product product = productService.getProductEntityById(detailDto.getProductId());
            maintenanceDetail.setProduct(product);
            maintenanceDetail.setMaintenance(maintenance);
            maintenanceDetailRepository.save(maintenanceDetail);
        }
     
        return MapperUtil.map(maintenance, MaintenanceDto.class);
    }
    
    @Override
    public MaintenanceDto update(Integer id, MaintenanceDto dto) {
        Optional<Maintenance> maintenanceOpt = maintenanceRepository.findById(id);
        if (maintenanceOpt.isEmpty()) {
            throw new NotFoundException("Maintenance not found with id: " + id);
        }
        Maintenance maintenance = maintenanceOpt.get();
        User user = userService.getUserEntityById(dto.getCreatedBy().getId());
        maintenance.setDescription(dto.getDescription());
        maintenance.setMaintenanceDate(dto.getMaintenanceDate());
        maintenance.setCompleted(dto.isCompleted());
        maintenance.setUpdatedAt(LocalDateTime.now());
        maintenance.setCreatedAt(LocalDateTime.now());
        maintenance.setLastUpdatedBy(user);
        maintenance.setCreatedBy(user);
        maintenance = maintenanceRepository.save(maintenance);
        return MapperUtil.map(maintenance, MaintenanceDto.class);
    }

    @Override
    public ResultPageWrapper<MaintenanceDto> findByOrganizationId(Integer orgId, Pageable paging) {
        Page<Maintenance> maintenances = maintenanceRepository.findMaintenanceByOrganizationId(orgId, paging);
        List<MaintenanceDto> maintenanceDtoList = new ArrayList<>();
        for(Maintenance maintenance : maintenances){
            MaintenanceDto maintenanceDto = MapperUtil.map(maintenance, MaintenanceDto.class);
            List<MaintenanceDetail> maintenanceDetails = maintenanceDetailRepository.findByMaintenanceId(maintenance.getId());
            List<MaintenanceDetailDto> maintenanceDetailDtoList = getMaintenanceDetailDtoList(maintenanceDetails);
            maintenanceDto.setMaintenanceDetails(maintenanceDetailDtoList);
            maintenanceDtoList.add(maintenanceDto);
        }
        ResultPageWrapper<MaintenanceDto> resultPageWrapper = new ResultPageWrapper<>();
        resultPageWrapper.setTotalPages(maintenances.getTotalPages());
        resultPageWrapper.setTotalItems(maintenances.getTotalElements());
        resultPageWrapper.setCurrentPage(maintenances.getNumber());
        resultPageWrapper.setPagesResult(maintenanceDtoList);
        return resultPageWrapper;
    }

    private static List<MaintenanceDetailDto> getMaintenanceDetailDtoList(List<MaintenanceDetail> maintenanceDetails) {
        List<MaintenanceDetailDto> maintenanceDetailDtos = new ArrayList<>();
        for(MaintenanceDetail maintenanceDetail : maintenanceDetails){
            MaintenanceDetailDto maintenanceDetailDto = new MaintenanceDetailDto();
            maintenanceDetailDto.setQuantity(maintenanceDetail.getQuantity());
            maintenanceDetailDto.setDescription(maintenanceDetail.getDescription());
            maintenanceDetailDto.setProductId(maintenanceDetail.getProduct().getId());
            maintenanceDetailDto.setMaintenanceId(maintenanceDetail.getMaintenance().getId());
            maintenanceDetailDto.setId(maintenanceDetail.getId());
            maintenanceDetailDtos.add(maintenanceDetailDto);
        }
        return maintenanceDetailDtos;
    }

    @Override
    public MaintenanceDto getById(Integer id) {
        return maintenanceRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, MaintenanceDto.class))
                .orElseThrow(() -> new NotFoundException("Maintenance not found with id: " + id));
    }
}