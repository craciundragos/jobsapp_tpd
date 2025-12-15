package com.example.jobsapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final String UPLOAD_DIR = "D:/jobsapp_resumes/";

    public String saveResume(MultipartFile resumeFile) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String fileName = UUID.randomUUID() + "_" + resumeFile.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        resumeFile.transferTo(filePath.toFile());

        return fileName;
    }
}
