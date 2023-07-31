package com.dev.studyspringboot.service;

import com.dev.studyspringboot.converter.BrandMapper;
import com.dev.studyspringboot.exception.NullException;
import com.dev.studyspringboot.exception.ResourceNotFoundException;
import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.repository.BrandRepository;
import com.dev.studyspringboot.response.brand.BrandResponse;
import com.dev.studyspringboot.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements IBrandService{

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public void addBrand(Brand brand) {
        if (brand != null) {
            brandRepository.save(brand);
        } else throw new NullException("Brand is null value!");
    }

    @Override
    public void updateBrand(Long brandId, Brand brand) {
        if (brand != null) {
            Brand existingBrand = brandRepository.findByIdAndDeletedAtIsNull(brandId);
            if (existingBrand != null) {
                ReflectionUtils.copyNonNullFields(brand, existingBrand);

                if (Objects.equals(existingBrand.getId(), brandId)) {
                    brandRepository.save(existingBrand);
                } else throw new RuntimeException("Has Error when edit request!");

            } else throw new ResourceNotFoundException("Brand has id: " + brandId + " NOT exist!");
        } else throw new NullException("Brand is null value!");
    }

    @Transactional
    @Override
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findByIdAndDeletedAtIsNull(brandId);
        if (brand != null) {
            brandRepository.softDeleteById(brandId);
        } else throw new ResourceNotFoundException("Brand has id: " + brandId + " NOT exist!");
    }

    @Override
    public List<Brand> getAllBrand() {
        return brandRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Brand getOneBrand(Long brandId) {
        Brand brand = brandRepository.findByIdAndDeletedAtIsNull(brandId);
        if (brand != null) {
            return brand;
        } else throw new ResourceNotFoundException("Brand has id: " + brandId + " NOT exist!");
    }

    @Override
    public List<BrandResponse> getBrands() {
        List<Brand> brands = brandRepository.findAllByDeletedAtIsNull();
        return brands.stream()
                .map(brand -> brandMapper.brandToBrandResponse(brand))
                .collect(Collectors.toList());
    }
}
