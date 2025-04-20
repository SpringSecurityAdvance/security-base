package com.angelfg.spring_security_course.services;

import com.angelfg.spring_security_course.dtos.SaveUser;
import com.angelfg.spring_security_course.persistence.entities.security.User;

import java.util.Optional;

public interface UserService {
    User registrOneCustomer(SaveUser newUser);
    Optional<User> findOneByUsername(String username);
}
