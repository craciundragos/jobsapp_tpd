package com.example.jobsapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiServiceRequestDTO {
    private Long cvId;
    private Long jobId;
}