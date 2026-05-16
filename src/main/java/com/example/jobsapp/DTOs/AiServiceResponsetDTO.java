package com.example.jobsapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiServiceResponsetDTO {
    private Double score;
    private String explanation;
}