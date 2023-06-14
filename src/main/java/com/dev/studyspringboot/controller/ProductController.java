package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.model.Product;
import com.dev.studyspringboot.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @PreAuthorize("permitAll")
    @GetMapping("/")
    public ResponseEntity<?> getAllProduct()
    {
        List<Product> products = iProductService.getAllProduct();
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list product successfully")
                .data(products)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("permitAll")
    @GetMapping("/show/{id}")
    public ResponseEntity<?> getOneProduct(
            @PathVariable("id") Long productId )
    {
        Product product = iProductService.getOneProduct(productId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get product successfully")
                .data(product)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
