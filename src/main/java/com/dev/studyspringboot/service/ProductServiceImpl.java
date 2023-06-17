package com.dev.studyspringboot.service;

import com.dev.studyspringboot.exception.NullException;
import com.dev.studyspringboot.exception.ResourceNotFoundException;
import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.model.Product;
import com.dev.studyspringboot.repository.BrandRepository;
import com.dev.studyspringboot.repository.ProductRepository;
import com.dev.studyspringboot.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public void addProduct(Product product) {
        if (product != null) {
            Brand brand = brandRepository.findByIdAndDeletedAtIsNull(product.getBrand().getId());
            if (brand != null) {
                product.setBrand(brand);
                productRepository.save(product);
            } else throw new ResourceNotFoundException("Brand has id: " + product.getBrand().getId() + " NOT exist!");
        } else throw new NullException("Product is null value!");
    }

    @Override
    public void updateProduct(Long productId, Product product) {
        if (product != null) {
            Product existingProduct = productRepository.findByIdAndDeletedAtIsNull(productId);
            if (existingProduct != null) {
                ReflectionUtils.copyNonNullFields(product, existingProduct);

                if (existingProduct.getId().equals(productId)) {
                    productRepository.save(existingProduct);
                } else throw new RuntimeException("Has Error when edit request!");

            } else throw new ResourceNotFoundException("Product has id: " + productId + " NOT exist!");
        } else throw new NullException("Product is null value!");
    }

    @Transactional
    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId);
        if (product != null) {
            productRepository.softDeleteById(productId);
        } else throw new ResourceNotFoundException("Product has id: " + productId + " NOT exist!");
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Product getOneProduct(Long productId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId);
        if (product != null) {
            return productRepository.findByIdAndDeletedAtIsNull(productId);
        } else throw new ResourceNotFoundException("Product has id: " + productId + " NOT exist!");
    }
}
