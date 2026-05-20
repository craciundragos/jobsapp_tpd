package com.example.jobsapp.service;

import com.example.jobsapp.entity.Application;
import com.example.jobsapp.repository.ApplicationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.jobsapp.DTOs.MatchResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiScoringAsyncService {

    private final ApplicationRepository applicationRepository;
    private final OllamaService ollamaService;

    @Async
    public void scoreApplicationAsyncAgentic(Integer applicationId, String jobReqText, String resumeS3Key) {
        try {
            String aiJson = ollamaService.scoreOnlyAgentic(
                    applicationId,
                    jobReqText,
                    resumeS3Key
            );

            JsonNode json = parseAiJson(aiJson);

            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            application.setRankAi(json.get("score").asInt());
            application.setExplanation(json.get("explanation").asText());

            applicationRepository.save(application);

        } catch (Exception e) {
            e.printStackTrace();

            applicationRepository.findById(applicationId).ifPresent(application -> {
                application.setExplanation("AI scoring failed: " + e.getMessage());
                applicationRepository.save(application);
            });
        }
    }

    @Async
    public void scoreApplicationAsyncRAG(Integer applicationId, String jobReqText, List<MatchResult> matchResults) {
        try {
            String aiJson = ollamaService.scoreCandidateRag(
                    applicationId,
                    jobReqText,
                    matchResults
            );

            JsonNode json = parseAiJson(aiJson);

            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            application.setRankAi(json.get("score").asInt());
            application.setExplanation(json.get("explanation").asText());

            applicationRepository.save(application);

        } catch (Exception e) {
            e.printStackTrace();

            applicationRepository.findById(applicationId).ifPresent(application -> {
                application.setExplanation("AI scoring failed: " + e.getMessage());
                applicationRepository.save(application);
            });
        }
    }

    private JsonNode parseAiJson(String aiJson) throws Exception {
        if (aiJson == null || aiJson.isBlank()) {
            throw new RuntimeException("AI response is empty");
        }

        int start = aiJson.indexOf('{');
        int end = aiJson.lastIndexOf('}');

        if (start < 0 || end < 0 || end <= start) {
            throw new RuntimeException("AI response does not contain a JSON object: " + aiJson);
        }

        String jsonOnly = aiJson.substring(start, end + 1);
        return new ObjectMapper().readTree(jsonOnly);
    }
}