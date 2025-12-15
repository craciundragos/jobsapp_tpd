package com.example.jobsapp.service;

import com.example.jobsapp.DTOs.ApplicationDTO;
import com.example.jobsapp.DTOs.MatchResult;
import com.example.jobsapp.entity.Application;
import com.example.jobsapp.entity.Job;
import com.example.jobsapp.entity.User;
import com.example.jobsapp.mapper.ApplicationMapper;
import com.example.jobsapp.repository.ApplicationRepository;
import com.example.jobsapp.repository.JobRepository;
import com.example.jobsapp.repository.UserRepository;
import com.example.jobsapp.utils.TextChunker;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final FileStorageService fileStorageService;
    private final TextExtractService textExtractorService;
    private final ChromaService chromaService;
    private final TextChunker textChunker;
    private final OllamaService ollamaService;

    public ApplicationDTO applyToJob(Integer jobId, Integer userId, MultipartFile resumeFile, String role) throws IOException {
        validateApplication(jobId, userId, role);
        String resumePath = fileStorageService.saveResume(resumeFile);

        String cvText = textExtractorService.extractText(resumePath);
        System.out.println("EXTRACTED CV TEXT:\n" + cvText);

        List<String> chunks = textChunker.splitIntoChunks(cvText, 500);
        System.out.println("CHUNKS COUNT = " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("CHUNK " + i + ": " + chunks.get(i));
        }
        chromaService.uploadCvChunks(jobId, userId, chunks);
        List<MatchResult> matchResults = chromaService.matchCvToJob(jobId, userId);
        System.out.println("MATCH RESULTS = " + matchResults);
        String aiResponse = ollamaService.scoreCandidate(jobId, matchResults);
        System.out.println("SCORE RESULT = " + aiResponse);
        Application application = saveApplication(jobId, userId, resumePath, aiResponse);

        return ApplicationMapper.toApplicationDTO(application);
    }


    private Application saveApplication(Integer jobId, Integer userId, String resumePath, String aiResponse) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setResumeUrl(resumePath);
        application.setStatus("submitted");
        try {
            ObjectMapper mapper = new ObjectMapper();
            aiResponse = aiResponse.replaceAll("```json", "");
            aiResponse = aiResponse.replaceAll("```", "");
            Map<String, Object> json = mapper.readValue(aiResponse, Map.class);

            Integer score = ((Integer) json.get("score"));
            String explanation = (String) json.get("explanation");

            application.setRankAi(score);
            application.setExplanation(explanation);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + aiResponse, e);
        }
        return applicationRepository.save(application);
    }


    public void validateApplication(Integer jobId, Integer userId, String role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new RuntimeException("You have already applied for this job.");
        }
    }


    public List<ApplicationDTO> getApplicationsByJob(Integer jobId) {
     List<ApplicationDTO> applications = applicationRepository.findByJobId(jobId)
                .stream()
                .map(ApplicationMapper::toApplicationDTO)
                .toList();

        return applications;
    }
}
