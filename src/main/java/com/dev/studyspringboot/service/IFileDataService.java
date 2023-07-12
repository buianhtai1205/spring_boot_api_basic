package com.dev.studyspringboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileDataService {
    void uploadFile(MultipartFile file) throws IOException;
    byte[] downloadFile(String fileName);
}
