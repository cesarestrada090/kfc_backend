package com.kfc.app.service.impl;

import com.kfc.app.dto.*;
import com.kfc.app.entities.*;
import com.kfc.app.exception.NotFoundException;
import com.kfc.app.repository.MaintenanceRepository;
import com.kfc.app.repository.MaintenanceRepository;
import com.kfc.app.service.*;
import com.kfc.app.util.MapperUtil;
import com.kfc.app.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    
    private final MaintenanceRepository maintenanceRepository;
    private final UnitService unitService;
    private final WorkshopService workshopService;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository,
                                  WorkshopService workshopService,
                                  UnitService unitService) {
        this.maintenanceRepository = maintenanceRepository;
        this.workshopService = workshopService;
        this.unitService = unitService;
    }

    @Override
    @Transactional
    public MaintenanceDto save(MaintenanceDto dto) {
        Maintenance workshop = getMaintenanceEntityByDto(dto);
        workshop = maintenanceRepository.save(workshop);
        return MapperUtil.map(workshop, MaintenanceDto.class);
    }

    @Override
    public MaintenanceDto update(Integer id, MaintenanceDto dto) {
        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findById(id);
        if (optionalMaintenance.isEmpty()) {
            throw new NotFoundException("Maintenance not found with id: " + id);
        }

        
        return MapperUtil.map(optionalMaintenance.get(), MaintenanceDto.class);
    }

    @Override
    public ResultPageWrapper<MaintenanceDto> findByOrganizationId(Integer orgId, Pageable paging) {
     
        Page<Maintenance> warehouses = maintenanceRepository.findAll(paging);
  
        return PaginationUtil.prepareResultWrapper(warehouses, MaintenanceDto.class);
    }

    @Override
    public MaintenanceDto getById(Integer id) {
        return maintenanceRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, MaintenanceDto.class))
                .orElseThrow(() -> new NotFoundException("Maintenance not found with id: " + id));
    }

    private Maintenance getMaintenanceEntityByDto(MaintenanceDto maintenanceDto){
        
        return null;
    }
}