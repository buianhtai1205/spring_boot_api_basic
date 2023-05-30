package com.dev.studyspringboot.repository;

import com.dev.studyspringboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query("UPDATE Product p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :productId")
    void softDeleteById(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand WHERE p.deletedAt IS NULL")
    List<Product> findAllByDeletedAtIsNull();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.brand LEFT JOIN FETCH p.feedbacks WHERE p.id = :productId AND p.deletedAt IS NULL")
    Product findByIdAndDeletedAtIsNull(@Param("productId") Long productId);
}
