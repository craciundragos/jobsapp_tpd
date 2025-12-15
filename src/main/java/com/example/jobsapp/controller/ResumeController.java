package com.example.jobsapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ResumeController {
    @GetMapping("/resume/{filename}")
    public ResponseEntity<byte[]> getResume(@PathVariable String filename) throws Exception {
        Path path = Path.of("D:/jobsapp_resumes/" + filename);
        byte[] bytes = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                .body(bytes);
    }
}
