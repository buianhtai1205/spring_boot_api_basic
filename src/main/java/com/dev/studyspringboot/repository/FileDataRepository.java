package com.dev.studyspringboot.repository;

import com.dev.studyspringboot.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long> {
    FileData findByName(String fileName);
}
