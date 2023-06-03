package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.Feedback;
import com.dev.studyspringboot.model.OrderProduct;

import java.util.List;

public interface IFeedbackService {
    void addFeedback(Feedback feedback, OrderProduct orderProduct);
    void updateFeedback(Long feedbackId, Feedback feedback);
    void deleteFeedback(Long feedbackId);
    List<Feedback> getAllFeedback();
    Feedback getOneFeedback(Long feedbackId);
    List<Feedback> getAllFeedbackOfProduct(Long productId);
}
