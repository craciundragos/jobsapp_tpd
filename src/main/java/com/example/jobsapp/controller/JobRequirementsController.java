package com.example.jobsapp.controller;

import com.example.jobsapp.DTOs.JobRequirementsFormDTO;
import com.example.jobsapp.service.JobRequirementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobRequirementsController {

    private final JobRequirementsService service;

    @PostMapping("/{jobId}/requirements")
    public ResponseEntity<?> saveRequirements(
            @PathVariable Integer jobId,
            @RequestBody JobRequirementsFormDTO form
    ) {
        service.saveRequirements(jobId, form);
        return ResponseEntity.ok("Requirements saved");
    }
}
