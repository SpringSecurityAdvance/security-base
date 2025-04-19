package com.angelfg.spring_security_course.persistence.repositories;

import com.angelfg.spring_security_course.persistence.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}