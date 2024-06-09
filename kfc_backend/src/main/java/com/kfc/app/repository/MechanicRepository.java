package com.kfc.app.repository;

import com.kfc.app.dto.MechanicDto;
import com.kfc.app.dto.PersonDto;
import com.kfc.app.entities.Maintenance;
import com.kfc.app.entities.Mechanic;
import com.kfc.app.entities.User;
import com.kfc.app.entities.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MechanicRepository extends JpaRepository<Mechanic, Integer> {
    Page<Mechanic> findByOrganizationId(Integer orgId, Pageable paging);
    @Query("SELECT m " +
            "FROM Mechanic m " +
            "JOIN Person p on m.person.id=p.id " +
            "WHERE p.documentNumber = :documentNumber AND m.organization.id = :orgId")
    MechanicDto findByDocumentNumberAndOrganizationId(@Param("orgId")String orgId, @Param("documentNumber")Integer documentNumber);
}