package com.mysap.sd.product.controller;

import com.mysap.sd.product.entity.Product;
import com.mysap.sd.product.entity.ProductRating;
import com.mysap.sd.product.repository.ProductRatingRepository;
import com.mysap.sd.product.repository.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{productId}/ratings")
public class ProductRatingController {

    private static final Logger log = LoggerFactory.getLogger(ProductRatingController.class);

    private final ProductRepository productRepository;
    private final ProductRatingRepository ratingRepository;

    public ProductRatingController(ProductRepository productRepository,
                                   ProductRatingRepository ratingRepository) {
        this.productRepository = productRepository;
        this.ratingRepository = ratingRepository;
    }
    
    @CrossOrigin(origins = "*") // or specific origin
    @PostMapping
    @Operation(summary = "Add a new rating for a product")
    public ResponseEntity<?> addRating(@PathVariable Long productId,
                                       @RequestBody ProductRating rating) {
        return productRepository.findById(productId).map(product -> {
            rating.setProduct(product); // set association
            ProductRating saved = ratingRepository.save(rating);
            log.info("Added rating {} for product {}", rating.getRating(), product.getCode());
            return ResponseEntity.status(201).body(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "*") // or specific origin
    @GetMapping
    @Operation(summary = "Get all ratings for a product")
    public ResponseEntity<List<ProductRating>> getRatings(@PathVariable Long productId) {
        if (!productRepository.existsById(productId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ratingRepository.findByProductId(productId));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get the most recent rating for a product")
    public ResponseEntity<ProductRating> getLatestRating(@PathVariable Long productId) {
        ProductRating latest = ratingRepository.findTopByProductIdOrderByCreatedAtDesc(productId);
        if (latest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latest);
    }
}
