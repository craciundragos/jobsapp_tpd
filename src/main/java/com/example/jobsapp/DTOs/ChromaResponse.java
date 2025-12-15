package com.example.jobsapp.DTOs;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChromaResponse {
    private List<List<String>> ids;
    private List<List<String>> documents;
    private List<List<Map<String, Object>>> metadatas;
    private List<List<Double>> distances;
}
