package com.example.jobsapp;

import com.example.jobsapp.DTOs.AiServiceRequestDTO;
import com.example.jobsapp.DTOs.AiServiceResponsetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class aiClient {

    private final RestTemplate restTemplate;

    @Value("${services.ai.base-url}")
    private String aiBaseUrl;

    public AiServiceResponsetDTO scoreCandidateRag(AiServiceRequestDTO requestDTO) {
        String url = aiBaseUrl + "/api/ai/score-rag";

        ResponseEntity<AiServiceResponsetDTO> response =
                restTemplate.postForEntity(url, requestDTO, AiServiceResponsetDTO.class);

        return response.getBody();
    }

    public String healthCheck() {
        String url = aiBaseUrl + "/api/ai/health";

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }

}