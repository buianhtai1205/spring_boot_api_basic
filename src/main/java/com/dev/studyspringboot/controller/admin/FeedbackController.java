package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.model.Feedback;
import com.dev.studyspringboot.model.OrderProduct;
import com.dev.studyspringboot.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/feedback")
public class FeedbackController {
    @Autowired
    private IFeedbackService iFeedbackService;

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
}

