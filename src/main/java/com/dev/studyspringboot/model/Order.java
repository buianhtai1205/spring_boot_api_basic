package com.dev.studyspringboot.model;

import com.dev.studyspringboot.util.enums.OrderStatus;
import com.dev.studyspringboot.util.enums.PaymentMethod;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameRecipient;
    private String addressRecipient;
    private String phoneRecipient;
    @Nullable
    private String message;
    private int totalPrice;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private int isPay = 0;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PROCESSING;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "shipper_id")
    @Nullable
    private User shipper;
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderProduct> orderProducts;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
