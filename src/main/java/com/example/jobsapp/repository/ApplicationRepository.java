package com.example.jobsapp.repository;
import com.example.jobsapp.entity.Job;
import com.example.jobsapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.jobsapp.entity.Application;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer>{
    boolean existsByUserAndJob(User user, Job job);
    List<Application> findByJobId(Integer jobId);
}
