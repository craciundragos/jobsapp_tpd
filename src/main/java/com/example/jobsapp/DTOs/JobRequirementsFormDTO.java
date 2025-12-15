package com.example.jobsapp.DTOs;

import lombok.Data;
import java.util.List;

@Data
public class JobRequirementsFormDTO {
    private Integer minExperience;
    private List<String> mustHaveSkills;
    private List<String> niceToHaveSkills;
    private List<String> softSkills;
    private String seniority;
}
