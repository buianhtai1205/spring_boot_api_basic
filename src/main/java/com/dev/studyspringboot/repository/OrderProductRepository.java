package com.dev.studyspringboot.repository;

import com.dev.studyspringboot.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
