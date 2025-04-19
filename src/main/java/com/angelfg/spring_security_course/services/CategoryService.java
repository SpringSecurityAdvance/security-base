package com.angelfg.spring_security_course.services;

import com.angelfg.spring_security_course.dtos.SaveCategory;
import com.angelfg.spring_security_course.persistence.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);
    Optional<Category> findOneById(Long categoryId);
    Category createOne(SaveCategory saveCategory);
    Category updateOneById(Long categoryId, SaveCategory saveCategory);
    Category disableOneById(Long categoryId);
}
