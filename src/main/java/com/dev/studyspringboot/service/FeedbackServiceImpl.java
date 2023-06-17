package com.dev.studyspringboot.service;

import com.dev.studyspringboot.exception.NullException;
import com.dev.studyspringboot.exception.ResourceNotFoundException;
import com.dev.studyspringboot.exception.WarningException;
import com.dev.studyspringboot.model.Feedback;
import com.dev.studyspringboot.model.OrderProduct;
import com.dev.studyspringboot.model.Product;
import com.dev.studyspringboot.repository.FeedbackRepository;
import com.dev.studyspringboot.repository.OrderProductRepository;
import com.dev.studyspringboot.repository.ProductRepository;
import com.dev.studyspringboot.util.ReflectionUtils;
import com.dev.studyspringboot.util.enums.ReceiveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements IFeedbackService{
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public void addFeedback(Feedback feedback, OrderProduct orderProduct) {
        if (feedback != null && orderProduct != null) {
            OrderProduct existingOrderProduct = orderProductRepository.findById(orderProduct.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("OrderProduct has id: "
                            + orderProduct.getProduct().getId() + " NOT exist!"));

            if (existingOrderProduct.getReceiveStatus() == ReceiveStatus.RECEIVED) {
                Product product = productRepository.findByIdAndDeletedAtIsNull(existingOrderProduct.getProduct().getId());

                feedback.setProduct(product);
                feedbackRepository.save(feedback);

                existingOrderProduct.setReceiveStatus(ReceiveStatus.EVALUATED);
                orderProductRepository.save(existingOrderProduct);
            } else throw new WarningException("Please choose received order before feedback!");

        } else throw new NullException("Feedback or OrderProduct is null value!");
    }

    @Override
    public void updateFeedback(Long feedbackId, Feedback feedback) {
        if (feedback != null) {
            Feedback existingFeedback = feedbackRepository.findById(feedbackId)
                    .orElseThrow(() -> new ResourceNotFoundException("Feedback has id: " + feedbackId + " NOT exist!"));

            ReflectionUtils.copyNonNullFields(feedback, existingFeedback);

            if (existingFeedback.getId().equals(feedbackId)) {
                feedbackRepository.save(existingFeedback);
            } else throw new RuntimeException("Has Error when edit request!");

        } else throw new NullException("Feedback is null value!");
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        Feedback existingFeedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (existingFeedback != null) {
            feedbackRepository.deleteById(feedbackId);
        } else throw new ResourceNotFoundException("Feedback has id: " + feedbackId + " NOT exist!");
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback getOneFeedback(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback has id: " + feedbackId + " NOT exist!"));
    }

    @Override
    public List<Feedback> getAllFeedbackOfProduct(Long productId) {
        Product existingProduct = productRepository.findByIdAndDeletedAtIsNull(productId);
        if (existingProduct != null) {
            return feedbackRepository.findByProduct(existingProduct);
        } else throw new ResourceNotFoundException("Product has id: " + productId + " NOT exist!");
    }
}
