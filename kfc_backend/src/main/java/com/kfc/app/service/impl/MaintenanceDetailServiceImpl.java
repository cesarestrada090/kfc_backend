package com.kfc.app.service.impl;

import com.kfc.app.dto.MaintenanceDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Maintenance;
import com.kfc.app.entities.Unit;
import com.kfc.app.entities.User;
import com.kfc.app.entities.Workshop;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.MaintenanceRepository;
import com.kfc.app.service.MaintenanceService;
import com.kfc.app.service.UnitService;
import com.kfc.app.service.UserService;
import com.kfc.app.service.WorkshopService;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MaintenanceDetailServiceImpl implements MaintenanceService {
    
    private final MaintenanceRepository maintenanceRepository;
    private final UnitService unitService;
    private final UserService userService;
    private final WorkshopService workshopService;

    public MaintenanceDetailServiceImpl(MaintenanceRepository maintenanceRepository,
                                        WorkshopService workshopService,
                                        UnitService unitService,
                                        UserService userService) {
        this.maintenanceRepository = maintenanceRepository;
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
        return PaginationUtil.prepareResultWrapper(maintenances, MaintenanceDto.class);
    }

    @Override
    public MaintenanceDto getById(Integer id) {
        return maintenanceRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, MaintenanceDto.class))
                .orElseThrow(() -> new NotFoundException("Maintenance not found with id: " + id));
    }
}