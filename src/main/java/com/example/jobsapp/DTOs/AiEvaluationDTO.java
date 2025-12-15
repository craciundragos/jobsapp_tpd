package com.example.jobsapp.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AiEvaluationDTO {
    private Integer id;
    private String resumeText;
    private String summary;
    private String weaknesses;
    private String strengths;
    private String recommandation;
    private BigDecimal score;
    private Integer applicationId;
    private String model;
}