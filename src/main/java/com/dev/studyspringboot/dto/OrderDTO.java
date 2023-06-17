package com.dev.studyspringboot.dto;

import com.dev.studyspringboot.model.OrderProduct;
import com.dev.studyspringboot.model.User;
import com.dev.studyspringboot.util.enums.OrderStatus;
import com.dev.studyspringboot.util.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    @NotEmpty(message = "Name Recipient is required!")
    private String nameRecipient;
    @NotEmpty(message = "Address Recipient is required!")
    private String addressRecipient;
    @NotEmpty(message = "Phone Recipient is required!")
    private String phoneRecipient;
    private String message;
    @NotEmpty(message = "Total Price is required!")
    @Min(value = 0, message = "Total Price must be greater than or equal to 0!")
    private int totalPrice;
    @NotEmpty(message = "Payment Method is required!")
    private PaymentMethod paymentMethod;
    private int isPay = 0;
    private OrderStatus orderStatus = OrderStatus.PROCESSING;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotEmpty(message = "User is required!")
    private User user;
    private User shipper;
    @NotEmpty(message = "Products Detail is required!")
    private List<OrderProduct> orderProducts;
}
