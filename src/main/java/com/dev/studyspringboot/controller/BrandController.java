package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private IBrandService iBrandService;

    @GetMapping("/")
    public ResponseEntity<List<Brand>> getAllBrand()
    {
        List<Brand> brands = iBrandService.getAllBrand();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Brand> getOneBrand(
            @PathVariable("id") Long brandId )
    {
        Brand brand = iBrandService.getOneBrand(brandId);
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }
}
