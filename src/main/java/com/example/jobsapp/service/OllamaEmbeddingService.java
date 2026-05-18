package com.example.jobsapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OllamaEmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${ollama.embeddings-url}")
    private String OLLAMA_URL;

    public List<Double> embed(String text) {

        Map<String, Object> request = Map.of(
                "model", "nomic-embed-text",
                "prompt", text
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                OLLAMA_URL,
                HttpMethod.POST,
                entity,
                Map.class
        );
        System.out.println("OLLAMA RAW RESPONSE = " + response.getBody());
        if (response.getBody() == null || response.getBody().get("embedding") == null) {
            throw new RuntimeException("Failed to generate embedding");
        }

        return (List<Double>) response.getBody().get("embedding");
    }
}
