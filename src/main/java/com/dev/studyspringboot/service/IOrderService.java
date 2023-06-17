package com.dev.studyspringboot.service;

import com.dev.studyspringboot.dto.OrderDTO;
import com.dev.studyspringboot.model.Order;

import java.util.List;

public interface IOrderService {
    void addOrder(OrderDTO order);
    void updateOrder(Long orderId, Order order);
    void deteleOrder(Long orderId);
    List<Order> getAllOrder();
    Order getOneOrder(Long orderId);
}
