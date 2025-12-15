package com.example.jobsapp.controller;

import com.example.jobsapp.service.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class OllamaController {

    private final OllamaService ollamaService;

    @PostMapping("/ask")
    public String askModel(@RequestBody String prompt) {
        return ollamaService.generateResponse(prompt);
    }
}
