package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.service.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fileStorage")
@PreAuthorize("permitAll")
public class FileStorageController {
    @Autowired
    private IFileStorageService fileStorageService;

    @PostMapping("/database/upload")
    public ResponseEntity<?> uploadFileToDatabase(
            @RequestParam("file")MultipartFile file
            ) throws IOException {
        fileStorageService.uploadFileToDatabase(file);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("File uploaded successful")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/database/download/{fileName}")
    public ResponseEntity<?> downloadFileFromDatabase(
            @PathVariable("fileName") String fileName
    ) {
        byte[] fileData = fileStorageService.downloadFileFromDatabase(fileName);
        return  ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(fileData);
    }

    @PostMapping("/system/upload")
    public ResponseEntity<?> uploadFileToSystem(
            @RequestParam("file")MultipartFile file
    ) throws IOException {
        fileStorageService.uploadFileToSystem(file);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("File uploaded successful")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/system/download/{fileName}")
    public ResponseEntity<?> downloadFileFromSystem(
            @PathVariable("fileName") String fileName
    ) throws IOException {
        byte[] fileData = fileStorageService.downloadFileFromSystem(fileName);
        return  ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(fileData);
    }
}
