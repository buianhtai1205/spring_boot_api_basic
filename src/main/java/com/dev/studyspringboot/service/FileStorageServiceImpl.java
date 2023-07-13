package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.FileData;
import com.dev.studyspringboot.model.FileStorage;
import com.dev.studyspringboot.repository.FileDataRepository;
import com.dev.studyspringboot.repository.FileStorageRepository;
import com.dev.studyspringboot.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class FileStorageServiceImpl implements IFileStorageService{
    @Autowired
    private FileDataRepository fileDataRepository;
    @Autowired
    private FileStorageRepository fileStorageRepository;
    private final String FOLDER_PATH = "D:\\Img-Background\\FILE_STORAGE\\";

    @Override
    public void uploadFileToDatabase(MultipartFile file) throws IOException {
        fileDataRepository.save(
                FileData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .data( FileUtils.compressFile(file.getBytes()) )
                        .build());
    }

    @Override
    public byte[] downloadFileFromDatabase(String fileName) {
        Optional<FileData> dbFileData = Optional.ofNullable(fileDataRepository.findByName(fileName));
        return FileUtils.decompressFile(dbFileData.get().getData());
    }

    @Override
    public void uploadFileToSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        fileStorageRepository.save(FileStorage.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                .build());
        file.transferTo(new File(filePath));
    }

    @Override
    public byte[] downloadFileFromSystem(String fileName) throws IOException {
        Optional<FileStorage> fileStorage = fileStorageRepository.findByName(fileName);
        String filePath = fileStorage.get().getFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
