package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.ApiResponse;
import com.dev.studyspringboot.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/mail")
@PreAuthorize("permitAll")
public class SendMailController {

    @Autowired
    private IMailService mailService;

    @PostMapping("")
    public ResponseEntity<?> sendMail(
            @RequestParam(value = "files")MultipartFile[] files,
            String to,
            String cc,
            String subject,
            String body
    ) {
        mailService.sendMail(files, to, cc, subject, body);

        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Send mail to " + to + " successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
