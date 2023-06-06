package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.model.Product;
import com.dev.studyspringboot.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProduct()
    {
        List<Product> products = iProductService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Product> getOneProduct(
            @PathVariable("id") Long productId )
    {
        Product product = iProductService.getOneProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
