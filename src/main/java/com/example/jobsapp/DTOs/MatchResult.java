package com.example.jobsapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResult {
    private String cvChunk;
    private double similarity;
}