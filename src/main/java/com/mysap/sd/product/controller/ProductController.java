package com.mysap.sd.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysap.sd.product.entity.Product;
import com.mysap.sd.product.repository.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository repository;

    @PostMapping
    @Operation(summary = "ADD A NEW PRODUCT")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        if (product.getCode() == null || product.getName() == null) {
            return ResponseEntity.badRequest().body("Code and Name are required.");
        }
        if (repository.existsByCode(product.getCode())) {
            return ResponseEntity.badRequest().body("Duplicate code.");
        }
        Product saved = repository.save(product);
        log.info("Product created: {}", product.getCode());
        log.warn("Payment delay for {}", product.getCode());
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    @Operation(summary = "Get ALL products")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
