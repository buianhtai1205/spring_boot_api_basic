package com.dev.studyspringboot.model;


import com.dev.studyspringboot.util.enums.ReceiveStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity = 1;
    @Enumerated(EnumType.STRING)
    private ReceiveStatus receiveStatus = ReceiveStatus.NOT_RECEIVED;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
