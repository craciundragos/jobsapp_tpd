package com.example.jobsapp.controller;

import com.example.jobsapp.DTOs.JobDTO;
import com.example.jobsapp.mapper.JobMapper;
import com.example.jobsapp.security.CustomUserDetails;
import com.example.jobsapp.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO jobDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Integer recruiterId = userDetails.getId();

        JobDTO createdJob = jobService.createJob(jobDTO, recruiterId);

        return ResponseEntity.ok(createdJob);
    }


    @GetMapping
    public ResponseEntity<List<JobDTO>> getJobs() {
        List<JobDTO> jobs;
            jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/recruiter")
    public List<JobDTO> getJobsByRecruiter() {
        CustomUserDetails userDetails =  (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Integer recruiterId = userDetails.getId();
        return jobService.getJobsByRecruiter(recruiterId)
                .stream()
                .map(JobMapper::toJobDTO)
                .toList();
    }
}

