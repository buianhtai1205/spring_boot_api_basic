package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {
    void addProduct(Product product);
    void updateProduct(Long productId, Product product);
    void deleteProduct(Long productId);
    List<Product> getAllProduct();
    Product getOneProduct(Long productId);
}
