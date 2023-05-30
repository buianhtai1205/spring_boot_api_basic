package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.Brand;

import java.util.List;

public interface IBrandService {
    void addBrand(Brand brand);
    void updateBrand(Long brandId, Brand brand);
    void deleteBrand(Long brandId);
    List<Brand> getAllBrand();
    Brand getOneBrand(Long brandId);
}
