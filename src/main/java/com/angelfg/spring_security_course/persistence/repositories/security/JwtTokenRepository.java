package com.angelfg.spring_security_course.persistence.repositories.security;

import com.angelfg.spring_security_course.persistence.entities.security.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String jwt);
}
