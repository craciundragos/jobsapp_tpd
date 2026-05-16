package com.example.jobsapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreResult {
    private Integer score;
    private String explanation;
}