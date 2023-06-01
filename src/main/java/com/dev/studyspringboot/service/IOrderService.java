package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.Order;

import java.util.List;

public interface IOrderService {
    void addOrder(Order order);
    void updateOrder(Long orderId, Order order);
    void deteleOrder(Long orderId);
    List<Order> getAllOrder();
    Order getOneOrder(Long orderId);
}
