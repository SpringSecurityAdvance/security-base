package com.angelfg.spring_security_course.persistence.repositories;

import com.angelfg.spring_security_course.persistence.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
