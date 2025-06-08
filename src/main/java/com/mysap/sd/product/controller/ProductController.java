package com.mysap.sd.product.controller;

import com.mysap.sd.product.entity.Product;
import com.mysap.sd.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        if (product.getCode() == null || product.getName() == null) {
            return ResponseEntity.badRequest().body("Code and Name are required.");
        }
        if (repository.existsByCode(product.getCode())) {
            return ResponseEntity.badRequest().body("Duplicate code.");
        }
        Product saved = repository.save(product);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
