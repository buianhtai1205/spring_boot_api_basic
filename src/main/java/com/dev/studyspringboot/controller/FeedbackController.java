package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.model.Feedback;
import com.dev.studyspringboot.model.OrderProduct;
import com.dev.studyspringboot.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private IFeedbackService iFeedbackService;

    public record FeedbackAndOrderProduct(Feedback feedback, OrderProduct orderProduct) {
    }

    @PostMapping("/create")
    public ResponseEntity<String> addFeedback(
            @RequestBody FeedbackAndOrderProduct feedbackAndOrderProduct )
    {
        System.out.println(feedbackAndOrderProduct.feedback().toString());
        System.out.println(feedbackAndOrderProduct.orderProduct().toString());
        iFeedbackService.addFeedback(
                feedbackAndOrderProduct.feedback(),
                feedbackAndOrderProduct.orderProduct() );
        return ResponseEntity.status(HttpStatus.CREATED).body("Feedback created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFeedback(
            @PathVariable("id") Long feedbackId,
            @RequestBody Feedback feedback )
    {
        iFeedbackService.updateFeedback(feedbackId, feedback);
        return ResponseEntity.status(HttpStatus.OK).body("Feedback updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFeedback(
            @PathVariable("id") Long feedbackId )
    {
        iFeedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.status(HttpStatus.OK).body("Feedback deleted successfully");
    }

    @GetMapping("/")
    public ResponseEntity<List<Feedback>> getAllFeedback()
    {
        List<Feedback> feedbacks = iFeedbackService.getAllFeedback();
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Feedback> getOneFeedback(
            @PathVariable("id") Long feedbackId )
    {
        Feedback feedback = iFeedbackService.getOneFeedback(feedbackId);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<Feedback>> getAllFeedbackOfProduct(
            @PathVariable("id") Long productId )
    {
        List<Feedback> feedbacks = iFeedbackService.getAllFeedbackOfProduct(productId);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }
}

