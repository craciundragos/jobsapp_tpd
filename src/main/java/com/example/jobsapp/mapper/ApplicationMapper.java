package com.example.jobsapp.mapper;

import com.example.jobsapp.DTOs.ApplicationDTO;
import com.example.jobsapp.entity.Application;
import com.example.jobsapp.entity.User;
import com.example.jobsapp.entity.Job;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationMapper {


    public static ApplicationDTO toApplicationDTO(Application application) {
        if (application == null) return null;

        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        if (application.getUser() != null) {
            dto.setUserId(application.getUser().getId());
        }
        if (application.getJob() != null) {
            dto.setJobId(application.getJob().getId());
        }
        dto.setResumeUrl(application.getResumeUrl());
        dto.setStatus(application.getStatus());
        dto.setRankAi(application.getRankAi());
        dto.setExplanation(application.getExplanation());
        return dto;
    }


    public static List<ApplicationDTO> toApplicationDTOList(List<Application> applications) {
        if (applications == null) return null;

        return applications.stream()
                .map(ApplicationMapper::toApplicationDTO)
                .collect(Collectors.toList());
    }


    public static Application toApplication(ApplicationDTO dto, User user, Job job) {
        if (dto == null || user == null || job == null) return null;

        Application application = new Application();
        application.setId(dto.getId());
        application.setUser(user);
        application.setJob(job);
        application.setResumeUrl(dto.getResumeUrl());
        application.setStatus(dto.getStatus());
        application.setRankAi(dto.getRankAi());
        application.setExplanation(dto.getExplanation());
        return application;
    }
}