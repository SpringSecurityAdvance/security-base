package com.angelfg.spring_security_course.services;

import com.angelfg.spring_security_course.dtos.SaveProduct;
import com.angelfg.spring_security_course.persistence.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    //    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findOneById(Long productId);
    Product createOne(SaveProduct saveProduct);
    Product updateOneById(Long productId, SaveProduct saveProduct);
    Product disableOneById(Long productId);
}
