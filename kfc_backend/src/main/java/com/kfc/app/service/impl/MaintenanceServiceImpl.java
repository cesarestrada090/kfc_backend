package com.kfc.app.service.impl;

import com.kfc.app.dto.*;
import com.kfc.app.entities.*;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.MaintenanceDetailRepository;
import com.kfc.app.repository.MaintenanceRepository;
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
public class MaintenanceServiceImpl implements MaintenanceService {
    
    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceDetailRepository maintenanceDetailRepository;
    private final UnitService unitService;
    private final UserService userService;
    private final WorkshopService workshopService;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository,
                                  MaintenanceDetailRepository maintenanceDetailRepository,
                                  WorkshopService workshopService,
                                  UnitService unitService,
                                  UserService userService) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceDetailRepository = maintenanceDetailRepository;
        this.workshopService = workshopService;
        this.unitService = unitService;
        this.userService = userService;
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
        saveMaintenanceDetail(dto, maintenance.getId());
        return MapperUtil.map(maintenance, MaintenanceDto.class);
    }

    private void saveMaintenanceDetail(MaintenanceDto dto, Integer maintenanceId) {
        if (dto.getMaintenanceDetails().isEmpty()) {
            return;
        }
        List<MaintenanceDetail> maintenanceDetailList = new ArrayList<>();
        for (MaintenanceDetailDto detailDto : dto.getMaintenanceDetails()) {
            MaintenanceDetail detail = new MaintenanceDetail();
            detail.setMaintenanceId(maintenanceId);  // Set the saved maintenance ID
            detail.setQuantity(detailDto.getQuantity());
            detail.setDescription(detailDto.getDescription());
            maintenanceDetailList.add(detail);
        }
        if (!maintenanceDetailList.isEmpty()){
            maintenanceDetailRepository.saveAll(maintenanceDetailList);
        }
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
        return PaginationUtil.prepareResultWrapper(maintenances, MaintenanceDto.class);
    }

    @Override
    public MaintenanceDto getById(Integer id) {
        return maintenanceRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, MaintenanceDto.class))
                .orElseThrow(() -> new NotFoundException("Maintenance not found with id: " + id));
    }
}