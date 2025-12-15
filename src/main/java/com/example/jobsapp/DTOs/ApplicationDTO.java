package com.example.jobsapp.DTOs;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ApplicationDTO {
    private Integer id;
    private Integer userId;
    private Integer jobId;
    private String resumeUrl;
    private String status;
    private Integer rankAi;
    private String explanation;

}