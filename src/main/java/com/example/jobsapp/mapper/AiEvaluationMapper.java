package com.example.jobsapp.mapper;

import com.example.jobsapp.DTOs.AiEvaluationDTO;
import com.example.jobsapp.entity.AiEvaluation;
import com.example.jobsapp.entity.Application;

import java.util.List;
import java.util.stream.Collectors;

public class AiEvaluationMapper {


    public static AiEvaluationDTO toAiEvaluationDTO(AiEvaluation aiEvaluation) {
        if (aiEvaluation == null) return null;

        AiEvaluationDTO dto = new AiEvaluationDTO();
        dto.setId(aiEvaluation.getId());
        dto.setResumeText(aiEvaluation.getResumeText());
        dto.setSummary(aiEvaluation.getSummary());
        dto.setWeaknesses(aiEvaluation.getWeaknesses());
        dto.setStrengths(aiEvaluation.getStrengths());
        dto.setRecommandation(aiEvaluation.getRecommandation());
        dto.setScore(aiEvaluation.getScore());
        if (aiEvaluation.getApplication() != null) {
            dto.setApplicationId(aiEvaluation.getApplication().getId());
        }
        dto.setModel(aiEvaluation.getModel());
        return dto;
    }


    public static List<AiEvaluationDTO> toAiEvaluationDTOList(List<AiEvaluation> evaluations) {
        if (evaluations == null) return null;

        return evaluations.stream()
                .map(AiEvaluationMapper::toAiEvaluationDTO)
                .collect(Collectors.toList());
    }


    public static AiEvaluation toAiEvaluation(AiEvaluationDTO dto, Application application) {
        if (dto == null || application == null) return null;

        AiEvaluation aiEvaluation = new AiEvaluation();
        aiEvaluation.setId(dto.getId());
        aiEvaluation.setResumeText(dto.getResumeText());
        aiEvaluation.setSummary(dto.getSummary());
        aiEvaluation.setWeaknesses(dto.getWeaknesses());
        aiEvaluation.setStrengths(dto.getStrengths());
        aiEvaluation.setRecommandation(dto.getRecommandation());
        aiEvaluation.setScore(dto.getScore());
        aiEvaluation.setApplication(application);
        aiEvaluation.setModel(dto.getModel());
        return aiEvaluation;
    }
}