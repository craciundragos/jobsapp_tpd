package com.example.jobsapp.DTOs;

import lombok.Data;

@Data
public class JobDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Integer companyId;
    private String companyName;
}