package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.model.Feedback;
import com.dev.studyspringboot.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackController {
    @Autowired
    private IFeedbackService iFeedbackService;

    @DeleteMapping("/delete/{id}")
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
}

