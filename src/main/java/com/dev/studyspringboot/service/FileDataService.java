package com.dev.studyspringboot.service;

import com.dev.studyspringboot.model.FileData;
import com.dev.studyspringboot.repository.FileDataRepository;
import com.dev.studyspringboot.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileDataService implements IFileDataService{
    @Autowired
    private FileDataRepository fileDataRepository;
    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        fileDataRepository.save(
                FileData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .data( FileUtils.compressFile(file.getBytes()) )
                .build());
    }

    @Override
    public byte[] downloadFile(String fileName) {
        Optional<FileData> dbFileData = Optional.ofNullable(fileDataRepository.findByName(fileName));
        return FileUtils.decompressFile(dbFileData.get().getData());
    }


}
