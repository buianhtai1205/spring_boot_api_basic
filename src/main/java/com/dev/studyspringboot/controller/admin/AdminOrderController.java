package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.model.Order;
import com.dev.studyspringboot.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderController {
    @Autowired
    private IOrderService iOrderService;

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("id") Long orderId,
            @RequestBody Order order )
    {
        iOrderService.updateOrder(orderId, order);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Order updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
