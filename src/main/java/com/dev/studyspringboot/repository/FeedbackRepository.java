package com.dev.studyspringboot.repository;

import com.dev.studyspringboot.model.Feedback;
import com.dev.studyspringboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProduct(Product product);
}
