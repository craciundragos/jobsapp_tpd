package com.example.jobsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ChromaClient {

    private final String baseUrl = "http://localhost:8000/api/v1/collections";
    private final ObjectMapper mapper = new ObjectMapper();
    private final CloseableHttpClient http = HttpClients.createDefault();

    public void addToCollection(String collectionId, List<String> ids,
                                List<String> documents, List<Map<String, Object>> metadatas,
                                List<List<Double>> embeddings) {


        try {
            Map<String, Object> body = new java.util.HashMap<>();
            body.put("ids", ids);
            body.put("documents", documents);
            body.put("metadatas", metadatas);
            body.put("embeddings", embeddings);

            String json = mapper.writeValueAsString(body);
            System.out.println("JSON TRIMIS CATRE CHROMA:\n" + json);

            HttpPost post = new HttpPost(baseUrl + "/" + collectionId + "/add");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            var response = http.execute(post);
            int status = response.getCode();

            if (status != 201 && status != 200) {
                throw new RuntimeException("Chroma error: " + status);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to contact Chroma", e);
        }
    }

    public Map<String, Object> query(
            String collectionId,
            List<Double> queryEmbedding,
            int nResults,
            int jobId,
            int userId
    ) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("query_embeddings", List.of(queryEmbedding));
            body.put("where", Map.of(
                    "$and", List.of(
                            Map.of("userId", userId),
                            Map.of("jobId", jobId)
                    )
            ));
            body.put("n_results", nResults);

            String json = mapper.writeValueAsString(body);

            HttpPost post = new HttpPost(baseUrl + "/" + collectionId + "/query");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            var response = http.execute(post);

            if (response.getCode() != 200) {
                throw new RuntimeException("Chroma query error: " + response.getCode());
            }

            return mapper.readValue(response.getEntity().getContent(), Map.class);

        } catch (IOException e) {
            throw new RuntimeException("Failed to query Chroma", e);
        }
    }

    public List<Double> getJobReqEmbeddings(String collectionId, int jobId) {

        try {
            Map<String, Object> body = new java.util.HashMap<>();
            body.put("include", List.of("embeddings"));
            body.put("where", Map.of("jobId", jobId));

            String json = mapper.writeValueAsString(body);

            HttpPost post = new HttpPost(baseUrl + "/" + collectionId + "/get");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            var response = http.execute(post);
            int status = response.getCode();

            if (status != 200) {
                throw new RuntimeException("Chroma GET embeddings error: status=" + status);
            }

            Map<String, Object> result = mapper.readValue(
                    response.getEntity().getContent(), Map.class);

            List<List<Double>> embeddings = (List<List<Double>>) result.get("embeddings");

            if (embeddings == null || embeddings.isEmpty()) {
                throw new RuntimeException("No embeddings found for jobId " + jobId);
            }

            return embeddings.get(0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to GET embeddings from Chroma", e);
        }
    }
}
