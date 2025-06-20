package com.mysap.sd.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        return repository.findById(id)
            .map(product -> {
                product.setActive(false);
                repository.save(product);
                return ResponseEntity.ok("Product deactivated");
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updated) {
        return repository.findById(id)
            .map(product -> {
                product.setCode(updated.getCode());               
                product.setName(updated.getName());  
                product.setType(updated.getType());  
                repository.save(product);
                return ResponseEntity.ok(product);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get ALL products")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
