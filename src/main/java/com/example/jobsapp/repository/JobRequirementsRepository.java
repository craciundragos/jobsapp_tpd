package com.example.jobsapp.repository;

import com.example.jobsapp.entity.JobRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface JobRequirementsRepository extends JpaRepository<JobRequirements, Integer> {
    JobRequirements findByJobId(Integer jobId);
}
