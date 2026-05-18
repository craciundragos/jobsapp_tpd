package com.example.jobsapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${file.upload-temp-dir}")
    private String uploadTempDir;

    public SavedResume saveResume(MultipartFile resumeFile) throws IOException {
        Files.createDirectories(Paths.get(uploadTempDir));

        String originalFilename = resumeFile.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "resume.pdf";
        }

        // elimină path-ul venit de la client, ex: D:/folder/file.pdf sau D:\folder\file.pdf
        originalFilename = originalFilename.replace("\\", "/");
        originalFilename = originalFilename.substring(originalFilename.lastIndexOf("/") + 1);

        // elimină caractere riscante din numele fișierului
        originalFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

        String fileName = UUID.randomUUID() + "_" + originalFilename;

        Path tempFilePath = Paths.get(uploadTempDir, fileName);

        resumeFile.transferTo(tempFilePath.toFile());

        String s3Key = "resumes/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(resumeFile.getContentType())
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromFile(tempFilePath)
        );

        return new SavedResume(fileName, s3Key, tempFilePath.toString());
    }

    public record SavedResume(
            String fileName,
            String s3Key,
            String localTempPath
    ) {}
}