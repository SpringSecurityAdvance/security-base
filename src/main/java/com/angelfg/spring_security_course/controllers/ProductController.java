package com.angelfg.spring_security_course.controllers;

import com.angelfg.spring_security_course.dtos.SaveProduct;
import com.angelfg.spring_security_course.persistence.entities.Product;
import com.angelfg.spring_security_course.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR')")
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    @GetMapping
    public ResponseEntity<Page<Product>> findAll(Pageable pageable){
        Page<Product> productsPage = productService.findAll(pageable);

        if (productsPage.hasContent()) return ResponseEntity.ok(productsPage);

        return ResponseEntity.notFound().build();
    }

    // @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR')")
    @PreAuthorize("hasAuthority('READ_ONE_PRODUCT')")
    @GetMapping("/{productId}")
    public ResponseEntity<Product> findOneById(@PathVariable Long productId){
        Optional<Product> product = productService.findOneById(productId);

        if (product.isPresent()) return ResponseEntity.ok(product.get());

        return ResponseEntity.notFound().build();
    }

    // @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PreAuthorize("hasAuthority('CREATE_ONE_PRODUCT')")
    @PostMapping
    public ResponseEntity<Product> createOne(@Valid @RequestBody SaveProduct saveProduct){
        Product product = productService.createOne(saveProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR')")
    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateOneById(
            @PathVariable Long productId,
            @Valid @RequestBody SaveProduct saveProduct
    ) {
        Product product = productService.updateOneById(productId, saveProduct);
        return ResponseEntity.ok(product);
    }

    // @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PreAuthorize("hasAuthority('DISABLE_ONE_PRODUCT')")
    @PutMapping("/{productId}/disabled")
    public ResponseEntity<Product> disableOneById(@PathVariable Long productId){
        Product product = productService.disableOneById(productId);
        return ResponseEntity.ok(product);
    }

}
