package com.example.jobsapp.service;

import com.example.jobsapp.DTOs.MatchResult;
import com.example.jobsapp.entity.JobRequirements;
import com.example.jobsapp.repository.JobRequirementsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class OllamaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private final JobRequirementsRepository jobRequirementsRepository;

    public String generateResponse(String prompt) {
        Map<String, Object> request = Map.of(
                "model", "deepseek-r1:8b",
                "prompt", prompt,
                "stream", false
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

        Object content = response.getBody().get("response");
        return content != null ? content.toString() : "No response from model.";
    }

    public String scoreCandidate(int jobId, List<MatchResult> matchResults) {

        List<String> cvChunks = matchResults.stream()
                .map(MatchResult::getCvChunk)
                .toList();

        JobRequirements req = jobRequirementsRepository.findByJobId(jobId);
        if (req == null) {
            return "ERROR: No job requirements found for jobId = " + jobId;
        }
        String jobRequirementsText = req.getGeneratedText();

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI specializing in evaluating candidates resumes for job postings.\n")
                .append("Using retrieved CV chunks from a vector database, evaluate how well the candidate fits the job.\n\n")
                .append("JOB REQUIREMENTS:\n")
                .append(jobRequirementsText)
                .append("\n\n")
                .append("### MOST RELEVANT PARTS OF THE CV:\n");

        for (String chunk : cvChunks) {
            prompt.append("- ").append(chunk).append("\n");
        }

        prompt.append("""
            
            ---
            
            Your task:
            1. Compare CV skills vs job requirements.
            2. Identify strengths and weaknesses.
            3. Provide a match percentage from 0 to 100.

            Respond ONLY in EXACT JSON format:
            {
              "score": <number>,
              "explanation": "<string>"
            }
            
            """);

        return generateResponse(prompt.toString());
    }
}