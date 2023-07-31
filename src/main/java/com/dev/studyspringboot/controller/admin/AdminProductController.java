package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.dto.ApiResponse;
import com.dev.studyspringboot.model.Product;
import com.dev.studyspringboot.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {
    @Autowired
    private IProductService iProductService;

    @PostMapping("/create")
    public ResponseEntity<?> addProduct(
            @Validated @RequestBody Product product )
    {
        iProductService.addProduct(product);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Product created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long productId,
            @RequestBody Product product )
    {
        iProductService.updateProduct(productId, product);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Product updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable("id") Long productId )
    {
        iProductService.deleteProduct(productId);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Product deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllProduct()
    {
        List<Product> products = iProductService.getAllProduct();
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Get list product successfully")
                .data(products)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<?> getOneProduct(
            @PathVariable("id") Long productId )
    {
        Product product = iProductService.getOneProduct(productId);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Get list product successfully")
                .data(product)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
