package com.example.jobsapp.controller;

import com.example.jobsapp.service.ChromaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chroma")
@RequiredArgsConstructor
public class ChromaController {

    private final ChromaService chromaService;

    @PostMapping("/test")
    public String testAdd() {
        chromaService.testAdd();
        return "OK - Added test document";
    }
}
