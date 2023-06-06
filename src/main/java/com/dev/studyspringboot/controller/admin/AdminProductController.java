package com.dev.studyspringboot.controller.admin;

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
    public ResponseEntity<String> addProduct(
            @Validated @RequestBody Product product )
    {
        iProductService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable("id") Long productId,
            @RequestBody Product product )
    {
        iProductService.updateProduct(productId, product);
        return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("id") Long productId )
    {
        iProductService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
    }

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
