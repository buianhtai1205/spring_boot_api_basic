package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.model.Order;
import com.dev.studyspringboot.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @PostMapping("/create")
    public ResponseEntity<String> addOrder(
            @RequestBody Order order )
    {
        iOrderService.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOrder(
            @PathVariable("id") Long orderId,
            @RequestBody Order order )
    {
        iOrderService.updateOrder(orderId, order);
        return ResponseEntity.status(HttpStatus.OK).body("Order updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable("id") Long orderId )
    {
        iOrderService.deteleOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Order deleted successfully");
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrder() {
        List<Order> orders = iOrderService.getAllOrder();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Order> getOneOrder(
            @PathVariable("id") Long orderId )
    {
        Order order = iOrderService.getOneOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
