package com.angelfg.spring_security_course.persistence.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.angelfg.spring_security_course.persistence.entities.security.OperationEntity;

import java.util.List;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

    @Query("SELECT o FROM OperationEntity o where o.permitAll = true")
    List<OperationEntity> findByPubliccAcces();
}
