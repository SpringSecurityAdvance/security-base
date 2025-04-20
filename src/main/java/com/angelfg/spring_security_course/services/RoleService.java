package com.angelfg.spring_security_course.services;

import com.angelfg.spring_security_course.persistence.entities.security.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();
}
