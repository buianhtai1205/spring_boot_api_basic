package com.dev.studyspringboot.model;

import com.dev.studyspringboot.util.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Nullable
    private String screen;
    @Nullable
    private String cpu;
    @Nullable
    private String card;
    @Nullable
    private String ram;
    @Nullable
    private String rom;
    @Nullable
    private String pin;
    @Nullable
    private float weight;
    @Nullable
    private String os;
    @Nullable
    private String connector;
    private int price;
    private int salePrice;
    @Nullable
    private String special;
    private int yearLaunch;
    private String imageUrl;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Nullable
    private LocalDateTime deletedAt;

    @ManyToOne()
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts;
    @OneToMany(mappedBy = "product")
    private List<Feedback> feedbacks;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
