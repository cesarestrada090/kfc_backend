package com.kfc.app.service;


import com.kfc.app.dto.OrganizationDto;
import com.kfc.app.dto.ResultPageWrapper;
import com.kfc.app.entities.Organization;
import org.springframework.data.domain.Pageable;

public interface OrgService {
    OrganizationDto save(OrganizationDto organizationDto);
    OrganizationDto update(Integer id, OrganizationDto dto);
    Boolean rucAlreadyExists(String ruc);
    OrganizationDto getById(Integer id);
    Organization getOrCreateOrgEntity(OrganizationDto orgDto);
    ResultPageWrapper<OrganizationDto> getAll(Pageable paging);
    Organization getOrgEntityById(Integer orgId);
}
