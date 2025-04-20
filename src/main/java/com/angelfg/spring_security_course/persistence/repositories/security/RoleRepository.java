package com.angelfg.spring_security_course.persistence.repositories.security;

import com.angelfg.spring_security_course.persistence.entities.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String defaultRole);
}
