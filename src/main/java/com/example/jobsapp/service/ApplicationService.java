package com.example.jobsapp.service;

import com.example.jobsapp.DTOs.ApplicationDTO;
import com.example.jobsapp.DTOs.MatchResult;
import com.example.jobsapp.entity.Application;
import com.example.jobsapp.entity.Job;
import com.example.jobsapp.entity.JobRequirements;
import com.example.jobsapp.entity.User;
import com.example.jobsapp.mapper.ApplicationMapper;
import com.example.jobsapp.repository.ApplicationRepository;
import com.example.jobsapp.repository.JobRepository;
import com.example.jobsapp.repository.JobRequirementsRepository;
import com.example.jobsapp.repository.UserRepository;
import com.example.jobsapp.utils.TextChunker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final JobRequirementsRepository jobRequirementsRepository;
    private final OllamaService ollamaService;

    public ApplicationDTO applyToJobRag(Integer jobId, Integer userId, MultipartFile resumeFile) throws IOException {
        validateApplication(jobId, userId);
        String fileName = fileStorageService.saveResume(resumeFile);
        String resumePath = "/D/cv-uri/" + fileName;

        String cvText = ollamaService.extractFullCvTextTool(resumePath);
        System.out.println("EXTRACTED CV TEXT:\n" + cvText);

        List<String> chunks = textChunker.splitIntoChunks(cvText, 500);
        System.out.println("CHUNKS COUNT = " + chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("CHUNK " + i + ": " + chunks.get(i));
        }
        chromaService.uploadCvChunks(jobId, userId, chunks);
        List<MatchResult> matchResults = chromaService.matchCvToJob(jobId, userId);
        System.out.println("MATCH RESULTS = " + matchResults);
        JobRequirements jobRequirements = jobRequirementsRepository.findTopByJobIdOrderByIdDesc(jobId);
        String jobRequirementsText = jobRequirements.getGeneratedText();
        String aiResponse = ollamaService.scoreCandidateRag(jobId, jobRequirementsText, matchResults);
        System.out.println("SCORE RESULT = " + aiResponse);
        Application application = new Application();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(aiResponse);

        application.setUser(user);
        application.setJob(job);
        application.setResumeUrl(fileName);
        application.setRankAi(json.get("score").asInt());
        application.setExplanation(json.get("explanation").asText());
        application = applicationRepository.save(application);

        return ApplicationMapper.toApplicationDTO(application);
    }

    public ApplicationDTO applyToJobAgentic(Integer jobId, Integer userId, MultipartFile resumeFile) throws IOException {
        validateApplication(jobId, userId);
        System.out.println("intrat in metoda ");

        String fileName = fileStorageService.saveResume(resumeFile);
        System.out.println("intrat in metoda ");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setResumeUrl(fileName);
        application.setRankAi(null);
        application.setExplanation(null);
        application = applicationRepository.save(application);

        String resumeContainerPath = "/D/cv-uri/" + fileName;
        JobRequirements jobRequirements = jobRequirementsRepository.findTopByJobIdOrderByIdDesc(jobId);
        String jobReqText = jobRequirements.getGeneratedText();
        System.out.println("Calling agent for applicationId=" + application.getId());
        System.out.println(resumeContainerPath);
        String aiJson = ollamaService.scoreOnlyAgentic(
                application.getId(),
                jobReqText,
                resumeContainerPath
        );

        System.out.println("Agent call finished for applicationId=" + application.getId());
        System.out.println("AI RESPONSE: " + aiJson);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(aiJson);

        application.setRankAi(json.get("score").asInt());
        application.setExplanation(json.get("explanation").asText());

        application = applicationRepository.save(application);

        return ApplicationMapper.toApplicationDTO(application);
    }

    public void validateApplication(Integer jobId, Integer userId) {

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
