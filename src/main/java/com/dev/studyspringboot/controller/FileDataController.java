package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.DefaultResponse;
import com.dev.studyspringboot.service.IFileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fileData")
@PreAuthorize("permitAll")
public class FileDataController {
    @Autowired
    private IFileDataService iFileDataService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file")MultipartFile file
            ) throws IOException {
        iFileDataService.uploadFile(file);
        DefaultResponse response = DefaultResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("File uploaded successful")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable("fileName") String fileName
    ) {
        byte[] fileData = iFileDataService.downloadFile(fileName);
        return  ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(fileData);
    }
}
