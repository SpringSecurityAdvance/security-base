package com.angelfg.spring_security_course.services.impl;


import com.angelfg.spring_security_course.persistence.entities.security.Role;
import com.angelfg.spring_security_course.persistence.repositories.security.RoleRepository;
import com.angelfg.spring_security_course.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Value("${security.default.role}")
    private String defaultRole;

    @Override
    public Optional<Role> findDefaultRole() {
        return roleRepository.findByName(defaultRole);
    }

}
