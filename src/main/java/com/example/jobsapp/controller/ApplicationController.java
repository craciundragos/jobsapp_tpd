package com.example.jobsapp.controller;

import com.example.jobsapp.DTOs.ApplicationDTO;
import com.example.jobsapp.security.CustomUserDetails;
import com.example.jobsapp.service.ApplicationService;
import com.example.jobsapp.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.jobsapp.service.TextExtractService;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> applyToJob(
            @RequestParam("jobId") Integer jobId,
            @RequestPart("resume") MultipartFile resumeFile) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

            Integer userId = user.getId();
            String role = user.getAuthorities().iterator().next().getAuthority();

            ApplicationDTO dto = applicationService.applyToJobAgentic(jobId, userId, resumeFile);

            return ResponseEntity.ok(dto);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByJob(@PathVariable Integer jobId) {
        List<ApplicationDTO> apps = applicationService.getApplicationsByJob(jobId);
        return ResponseEntity.ok(apps);
    }

    private final TextExtractService textExtractService;

    @GetMapping("/test-ocr")
    public String test() {
        return textExtractService.extractText("D:\\Descarcari\\text-image-title.png");
    }


}
