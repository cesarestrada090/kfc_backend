package com.kfc.app.service;


import com.kfc.app.dto.OrganizationDto;
import com.kfc.app.dto.ResultPageWrapper;
import org.springframework.data.domain.Pageable;

public interface OrgService {
    OrganizationDto save(OrganizationDto organizationDto);
    OrganizationDto update(Integer id, OrganizationDto dto);
    Boolean rucAlreadyExists(String ruc);
    OrganizationDto getById(Integer id);
    ResultPageWrapper<OrganizationDto> getAll(Pageable paging);
}
