package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/brand")
public class AdminBrandController {
    @Autowired
    private IBrandService iBrandService;

    @PostMapping("/create")
    public ResponseEntity<?> addBrand(
            @RequestBody Brand brand )
    {
        iBrandService.addBrand(brand);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Brand created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBrand(
            @PathVariable("id") Long brandId,
            @RequestBody Brand brand )
    {
        iBrandService.updateBrand(brandId, brand);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Brand updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteBrand(
            @PathVariable("id") Long brandId )
    {
        iBrandService.deleteBrand(brandId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Brand deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllBrand()
    {
        List<Brand> brands = iBrandService.getAllBrand();
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list brand successfully")
                .data(brands)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<?> getOneBrand(
            @PathVariable("id") Long brandId )
    {
        Brand brand = iBrandService.getOneBrand(brandId);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get brand successfully")
                .data(brand)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
