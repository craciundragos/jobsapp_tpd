package com.example.jobsapp.controller;

import com.example.jobsapp.DTOs.AiServiceRequestDTO;
import com.example.jobsapp.DTOs.AiServiceResponsetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.jobsapp.aiClient;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AiServiceController {

    private final aiClient aiServiceClient;

    @PostMapping("/applications/score")
    public ResponseEntity<AiServiceResponsetDTO> score(@RequestBody AiServiceRequestDTO requestDTO) {
        return ResponseEntity.ok(aiServiceClient.scoreCandidateRag(requestDTO));
    }

    @GetMapping("/test/ai-health")
    public ResponseEntity<String> checkAiHealth() {
        return ResponseEntity.ok(aiServiceClient.healthCheck());
    }
}