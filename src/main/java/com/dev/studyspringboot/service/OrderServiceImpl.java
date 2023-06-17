package com.dev.studyspringboot.service;

import com.dev.studyspringboot.dto.OrderDTO;
import com.dev.studyspringboot.exception.NullException;
import com.dev.studyspringboot.exception.ResourceNotFoundException;
import com.dev.studyspringboot.model.Order;
import com.dev.studyspringboot.model.OrderProduct;
import com.dev.studyspringboot.model.Product;
import com.dev.studyspringboot.model.User;
import com.dev.studyspringboot.repository.OrderProductRepository;
import com.dev.studyspringboot.repository.OrderRepository;
import com.dev.studyspringboot.repository.ProductRepository;
import com.dev.studyspringboot.repository.UserRepository;
import com.dev.studyspringboot.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public void addOrder(OrderDTO order) {
        if (order != null) {
            User user = userRepository.findByIdAndDeletedAtIsNull(order.getUser().getId());

            if (user != null) {
                order.setUser(user);
                List<OrderProduct> orderProducts = order.getOrderProducts();

                for (OrderProduct orderProduct : orderProducts) {
                    Product product = productRepository.findByIdAndDeletedAtIsNull(orderProduct.getProduct().getId());
                    if (product != null) {
                        Order newOrder = new Order();
                        ReflectionUtils.copyNonNullFields(order, newOrder);
                        orderRepository.saveAndFlush(newOrder);
                        orderProduct.setOrder(newOrder);
                        orderProductRepository.save(orderProduct);
                    } else throw new ResourceNotFoundException("Product has ID: "
                            + orderProduct.getProduct().getId() + " NOT exist!");
                }
            } else throw new ResourceNotFoundException("User has id: " + order.getUser().getId() + " NOT exist!");
        } else throw new NullException("Order is null value!");
    }

    @Override
    public void updateOrder(Long orderId, Order order) {
        if (order != null) {
            Order existingOrder = orderRepository.findById(orderId).orElse(null);

            if (existingOrder != null) {
                ReflectionUtils.copyNonNullFields(order, existingOrder);

                if (existingOrder.getId().equals(orderId)) {
                    orderRepository.save(existingOrder);
                } else throw new RuntimeException("Has Error when edit request!");

            } else throw new ResourceNotFoundException("Order has id: " + orderId + " NOT exist!");

        } else throw new NullException("Order is null value!");
    }

    @Transactional
    @Override
    public void deteleOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null) {
            orderRepository.deleteById(orderId);
        } else throw new ResourceNotFoundException("Order has id: " + orderId + " NOT exist!");

    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOneOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order has id: " + orderId + " NOT exist!"));
    }
}
