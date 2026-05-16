package com.example.jobsapp.service;

import com.example.jobsapp.DTOs.JobRequirementsFormDTO;
import com.example.jobsapp.entity.JobRequirements;
import com.example.jobsapp.repository.JobRequirementsRepository;
import com.example.jobsapp.utils.TextChunker;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobRequirementsService {

    private final JobRequirementsRepository repo;
    private final ObjectMapper mapper;
    private final TextChunker textChunker;
    private final ChromaService chromaService;

    public void saveRequirements(Integer jobId, JobRequirementsFormDTO form) {
        try {
            String formJson = mapper.writeValueAsString(form);

            String generatedText = buildRequirementsText(form);

            JobRequirements req = new JobRequirements();
            req.setJobId(jobId);
            req.setRawFormJson(formJson);
            req.setGeneratedText(generatedText);

            repo.save(req);
            List<String> chunks = textChunker.splitIntoChunks(generatedText, 9999);
            chromaService.uploadJobRequirements(jobId, chunks);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save requirements", e);
        }
    }

    private String buildRequirementsText(JobRequirementsFormDTO form) {
        return """
               Requirements:
               Minimum experience: %d years
               Must-have skills: %s
               Nice-to-have skills: %s
               Soft skills: %s
               Seniority: %s
               """.formatted(
                form.getMinExperience(),
                String.join(", ", form.getMustHaveSkills()),
                String.join(", ", form.getNiceToHaveSkills()),
                String.join(", ", form.getSoftSkills()),
                form.getSeniority()
        );
    }
}
