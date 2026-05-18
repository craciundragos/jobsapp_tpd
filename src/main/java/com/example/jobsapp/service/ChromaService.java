package com.example.jobsapp.service;

import com.example.jobsapp.ChromaClient;
import com.example.jobsapp.DTOs.MatchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChromaService {

    private final ChromaClient chromaClient;

    @Value("${chroma.cv-collection}")
    private String CV_COLLECTION;

    @Value("${chroma.job-req-collection}")
    private String JOB_REQ_COLLECTION;
    private final OllamaEmbeddingService ollamaEmbeddingService;

    public void uploadCvChunks(Integer jobId, Integer userId, List<String> chunks) {

        List<String> ids = new ArrayList<>();
        List<Map<String, Object>> metas = new ArrayList<>();
        List<List<Double>> embeddings = new ArrayList<>();


        for (int i = 0; i < chunks.size(); i++) {
            ids.add("cv_" + userId + "_job_" + jobId + "_chunk_" + i);
            String chunk = chunks.get(i);
            metas.add(Map.of(
                    "jobId", jobId,
                    "userId", userId,
                    "chunkIndex", i
            ));
            embeddings.add(ollamaEmbeddingService.embed(chunk));

        }

        chromaClient.addToCollection(
                CV_COLLECTION,
                ids,
                chunks,
                metas,
                embeddings
        );
    }

    public void uploadJobRequirements(Integer jobId, List<String> chunks) {

        List<String> ids = new ArrayList<>();
        List<Map<String, Object>> metas = new ArrayList<>();
        List<List<Double>> embeddings = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            ids.add("job_" + jobId + "_chunk_" + i);
            String chunk = chunks.get(i);

            metas.add(Map.of(
                    "type", "job",
                    "jobId", jobId,
                    "chunkIndex", i
            ));
            embeddings.add(ollamaEmbeddingService.embed(chunk));
            System.out.println(embeddings);

        }

        chromaClient.addToCollection(
                JOB_REQ_COLLECTION,
                ids,
                chunks,
                metas,
                embeddings
        );
    }
    public List<Double> getJobRequirementEmbeddings(Integer jobId) {
        return chromaClient.getJobReqEmbeddings(JOB_REQ_COLLECTION, jobId);
    }


    public List<MatchResult> matchCvToJob(Integer jobId, Integer userId) {

        List<Double> jobEmbedding = getJobRequirementEmbeddings(jobId);

        Map<String, Object> queryResult = chromaClient.query(
                CV_COLLECTION,
                jobEmbedding,
                5,
                jobId,
                userId
        );

        List<List<String>> docs = (List<List<String>>) queryResult.get("documents");
        List<List<Double>> dists = (List<List<Double>>) queryResult.get("distances");

        List<MatchResult> matches = new ArrayList<>();

        if (docs != null && dists != null) {
            List<String> chunks = docs.get(0);
            List<Double> distances = dists.get(0);

            for (int i = 0; i < chunks.size(); i++) {
                double similarity = 1 - distances.get(i);
                matches.add(new MatchResult(chunks.get(i), similarity));
            }
        }

        return matches;
    }


    public void testAdd() {
        String text = "hello from java";
        List<Double> embedding = ollamaEmbeddingService.embed(text);

        chromaClient.addToCollection(
                CV_COLLECTION,
                List.of("java_test"),
                List.of("hello from java"),
                List.of(Map.of("debug", true)),
                List.of(embedding)
        );
    }


}
