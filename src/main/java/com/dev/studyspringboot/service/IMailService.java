package com.dev.studyspringboot.service;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IMailService {

    void sendMail(MultipartFile[] files, String to, String cc, String subject, String body);
}
