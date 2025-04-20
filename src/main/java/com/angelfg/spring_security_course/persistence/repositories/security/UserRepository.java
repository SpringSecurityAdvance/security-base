package com.angelfg.spring_security_course.persistence.repositories.security;

import com.angelfg.spring_security_course.persistence.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
