package com.kfc.app.service.impl;

import com.kfc.app.dto.MaintenanceDetailDto;
import com.kfc.app.dto.MaintenanceDto;
import com.kfc.app.dto.ResultPageWrapper;
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
import java.util.Optional;

@Service
public class MaintenanceDetailServiceImpl implements MaintenanceDetailService {
    
    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceDetailRepository maintenanceDetailRepository;

    public MaintenanceDetailServiceImpl(MaintenanceRepository maintenanceRepository,
                                        MaintenanceDetailRepository maintenanceDetailRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceDetailRepository = maintenanceDetailRepository;
    }

    @Override
    @Transactional
    public MaintenanceDetailDto save(MaintenanceDetailDto dto) {
        MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
        maintenanceDetail.setQuantity(dto.getQuantity());
        maintenanceDetail.setDescription(dto.getDescription());
        maintenanceDetail = maintenanceDetailRepository.save(maintenanceDetail);
        return MapperUtil.map(maintenanceDetail, MaintenanceDetailDto.class);
    }

    @Override
    public MaintenanceDetailDto update(Integer id, MaintenanceDetailDto dto) {
        Optional<MaintenanceDetail> maintenanceOpt = maintenanceDetailRepository.findById(id);
        if (maintenanceOpt.isEmpty()) {
            throw new NotFoundException("Maintenance Detail not found with id: " + id);
        }
        MaintenanceDetail maintenanceDetail = maintenanceOpt.get();
        maintenanceDetail.setQuantity(dto.getQuantity());
        maintenanceDetail.setDescription(dto.getDescription());
        maintenanceDetail = maintenanceDetailRepository.save(maintenanceDetail);
        return MapperUtil.map(maintenanceDetail, MaintenanceDetailDto.class);
    }

    @Override
    public ResultPageWrapper<MaintenanceDetailDto> findByOrganizationId(Integer orgId, Pageable paging) {
        Page<Maintenance> maintenances = maintenanceRepository.findMaintenanceByOrganizationId(orgId, paging);
        return PaginationUtil.prepareResultWrapper(maintenances, MaintenanceDetailDto.class);
    }

    @Override
    public MaintenanceDetailDto getById(Integer id) {
        return maintenanceRepository.findById(id)
                .map(warehouse -> MapperUtil.map(warehouse, MaintenanceDetailDto.class))
                .orElseThrow(() -> new NotFoundException("Maintenance not found with id: " + id));
    }
}