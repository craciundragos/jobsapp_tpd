package com.example.jobsapp.repository;
import com.example.jobsapp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.jobsapp.entity.Job;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>{
    List<Job> findByCompany(Company company);

}
