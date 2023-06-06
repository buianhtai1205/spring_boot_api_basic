package com.dev.studyspringboot.controller.admin;

import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/brand")
public class BrandController {
    @Autowired
    private IBrandService iBrandService;

    @PostMapping("/create")
    public ResponseEntity<String> addBrand(
            @Validated @RequestBody Brand brand )
    {
        iBrandService.addBrand(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body("Brand created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBrand(
            @PathVariable("id") Long brandId,
            @RequestBody Brand brand )
    {
        iBrandService.updateBrand(brandId, brand);
        return ResponseEntity.status(HttpStatus.OK).body("Brand updated successfully");
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteBrand(
            @PathVariable("id") Long brandId )
    {
        iBrandService.deleteBrand(brandId);
        return ResponseEntity.status(HttpStatus.OK).body("Brand deleted successfully");
    }

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
