package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.ApiResponse;
import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.response.brand.BrandResponse;
import com.dev.studyspringboot.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brand")
@PreAuthorize("permitAll")
public class BrandController {
    @Autowired
    private IBrandService iBrandService;

    @GetMapping("/")
    public ResponseEntity<?> getAllBrand()
    {
        List<Brand> brands = iBrandService.getAllBrand();
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list brand successfully")
                .data(brands)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/option1") // worst option
    public ResponseEntity<?> getBrands() {
        List<Brand> brands = iBrandService.getAllBrand();

        List<BrandResponse> brandResponseList = brands.stream()
                .map(brand -> {
                    return BrandResponse.builder()
                            .name(brand.getName())
                            .address(brand.getAddress())
                            .imageUrl(brand.getImageUrl())
                            .build();
                })
                .collect(Collectors.toList());

        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list brand successfully")
                .data(brandResponseList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/option2") // best option
    public ResponseEntity<?> getBrands2() {
        List<BrandResponse> brandResponseList = iBrandService.getBrands();
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list brand successfully")
                .data(brandResponseList)
                .build();
//        ApiResponse response = new ApiResponse<>(brandResponseList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<?> getOneBrand(
            @PathVariable("id") Long brandId )
    {
        Brand brand = iBrandService.getOneBrand(brandId);
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get brand successfully")
                .data(brand)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
