package com.kfc.app.repository;

import com.kfc.app.entities.Organization;
import com.kfc.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findByRuc(String username);
}