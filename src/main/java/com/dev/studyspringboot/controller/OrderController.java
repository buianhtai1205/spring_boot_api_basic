package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.dto.OrderDTO;
import com.dev.studyspringboot.model.Order;
import com.dev.studyspringboot.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@PreAuthorize("hasRole('USER')")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @PostMapping("/create")
    public ResponseEntity<?> addOrder(
            @Validated @RequestBody OrderDTO order )
    {
        iOrderService.addOrder(order);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Order created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable("id") Long orderId )
    {
        iOrderService.deteleOrder(orderId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Order deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllOrder() {
        List<Order> orders = iOrderService.getAllOrder();
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list order successfully")
                .data(orders)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<?> getOneOrder(
            @PathVariable("id") Long orderId )
    {
        Order order = iOrderService.getOneOrder(orderId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get order successfully")
                .data(order)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
