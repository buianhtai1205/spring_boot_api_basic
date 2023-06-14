package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.model.Feedback;
import com.dev.studyspringboot.model.OrderProduct;
import com.dev.studyspringboot.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private IFeedbackService iFeedbackService;

    public record FeedbackAndOrderProduct(Feedback feedback, OrderProduct orderProduct) {
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addFeedback(
            @RequestBody FeedbackAndOrderProduct feedbackAndOrderProduct )
    {
        System.out.println(feedbackAndOrderProduct.feedback().toString());
        System.out.println(feedbackAndOrderProduct.orderProduct().toString());
        iFeedbackService.addFeedback(
                feedbackAndOrderProduct.feedback(),
                feedbackAndOrderProduct.orderProduct() );
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Feedback created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateFeedback(
            @PathVariable("id") Long feedbackId,
            @RequestBody Feedback feedback )
    {
        iFeedbackService.updateFeedback(feedbackId, feedback);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Feedback updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteFeedback(
            @PathVariable("id") Long feedbackId )
    {
        iFeedbackService.deleteFeedback(feedbackId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Feedback deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    @PreAuthorize("permitAll")
    public ResponseEntity<?> getAllFeedback()
    {
        List<Feedback> feedbacks = iFeedbackService.getAllFeedback();
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list feedback successfully")
                .data(feedbacks)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    @PreAuthorize("permitAll")
    public ResponseEntity<?> getOneFeedback(
            @PathVariable("id") Long feedbackId )
    {
        Feedback feedback = iFeedbackService.getOneFeedback(feedbackId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get feedback successfully")
                .data(feedback)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("permitAll")
    public ResponseEntity<?> getAllFeedbackOfProduct(
            @PathVariable("id") Long productId )
    {
        List<Feedback> feedbacks = iFeedbackService.getAllFeedbackOfProduct(productId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list feedback of product successfully")
                .data(feedbacks)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

