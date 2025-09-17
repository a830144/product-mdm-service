package com.mysap.sd.product.repository;

import com.mysap.sd.product.entity.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {

    // Find all ratings for a given product
    List<ProductRating> findByProductId(Long productId);

    // Optional: find latest rating for a product
    ProductRating findTopByProductIdOrderByCreatedAtDesc(Long productId);

    // Optional: check if product has a rating above certain threshold
    boolean existsByProductIdAndRatingGreaterThan(Long productId, Double rating);
}

