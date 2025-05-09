package com.angelfg.spring_security_course.services.impl;

import com.angelfg.spring_security_course.dtos.SaveProduct;
import com.angelfg.spring_security_course.exceptions.ObjectNotFoundException;
import com.angelfg.spring_security_course.persistence.entities.Category;
import com.angelfg.spring_security_course.persistence.entities.Product;
import com.angelfg.spring_security_course.persistence.repositories.ProductRepository;
import com.angelfg.spring_security_course.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    //    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findOneById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product createOne(SaveProduct saveProduct) {
        Product product = new Product();
        product.setName(saveProduct.getName());
        product.setPrice(saveProduct.getPrice());
        product.setStatus(Product.ProductStatus.ENABLED);

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Product updateOneById(Long productId, SaveProduct saveProduct) {
        Product productFromDB = productRepository.findById(productId)
                        .orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));

        productFromDB.setName(saveProduct.getName());
        productFromDB.setPrice(saveProduct.getPrice());

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        productFromDB.setCategory(category);

        return productRepository.save(productFromDB);
    }

    @Override
    public Product disableOneById(Long productId) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id " + productId));
        productFromDB.setStatus(Product.ProductStatus.DISABLED);

        return productRepository.save(productFromDB);
    }

}
