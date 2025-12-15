package com.example.jobsapp.mapper;

import com.example.jobsapp.DTOs.JobDTO;
import com.example.jobsapp.entity.Job;
import com.example.jobsapp.entity.Company;

import java.util.List;
import java.util.stream.Collectors;

public class JobMapper {

    public static JobDTO toJobDTO(Job job) {
        if (job == null) return null;

        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setStatus(job.getStatus());
        if (job.getCompany() != null) {
            dto.setCompanyId(job.getCompany().getId());
        }
        dto.setCompanyName(job.getCompany().getName());
        return dto;
    }

    public static List<JobDTO> toJobDTOList(List<Job> jobs) {
        if (jobs == null) return null;

        return jobs.stream()
                .map(JobMapper::toJobDTO)
                .collect(Collectors.toList());
    }

    public static Job toJob(JobDTO dto, Company company) {
        if (dto == null || company == null) return null;

        Job job = new Job();
        job.setId(dto.getId());
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setStatus(dto.getStatus());
        job.setCompany(company);

        return job;
    }
}