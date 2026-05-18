package com.example.jobsapp.config;

import com.example.jobsapp.utils.Tools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(Tools pdfTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(pdfTools)
                .build();
    }
}