package com.kfc.app.repository;

import com.kfc.app.dto.MechanicDto;
import com.kfc.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN Person p on u.person.id=p.id " +
            "WHERE p.documentNumber = :documentNumber AND u.organization.id = :orgId")
    MechanicDto findByDocumentNumberAndOrganizationId(@Param("orgId")Integer orgId, @Param("documentNumber")String documentNumber);
    Optional<User> findByUsernameAndOrganizationId(String username, Integer orgId);
}