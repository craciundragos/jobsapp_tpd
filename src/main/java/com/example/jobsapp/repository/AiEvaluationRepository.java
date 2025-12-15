package com.example.jobsapp.repository;
import com.example.jobsapp.entity.AiEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiEvaluationRepository extends JpaRepository<AiEvaluation, Integer>{
}
