package com.mysap.sd.product.entity;



import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String type;

    @Column(name = "active")
    private boolean active = true;

    // === Association with ProductRating ===
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductRating> ratings = new ArrayList<>();

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<ProductRating> getRatings() { return ratings; }
    public void setRatings(List<ProductRating> ratings) { this.ratings = ratings; }

    // Helper methods for bidirectional sync
    public void addRating(ProductRating rating) {
        ratings.add(rating);
        rating.setProduct(this);
    }

    public void removeRating(ProductRating rating) {
        ratings.remove(rating);
        rating.setProduct(null);
    }
}
