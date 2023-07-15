package com.dev.studyspringboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileStorageService {
    void uploadFileToDatabase(MultipartFile file) throws IOException;
    byte[] downloadFileFromDatabase(String fileName);
    void uploadFileToSystem(MultipartFile file) throws IOException;
    byte[] downloadFileFromSystem(String fileName) throws IOException;
    String uploadFileToAWSS3(MultipartFile file);
    byte[] downloadFileFromAWSS3(String fileName);
}
