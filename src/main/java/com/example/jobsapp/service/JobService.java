package com.example.jobsapp.service;

import com.example.jobsapp.DTOs.JobDTO;
import com.example.jobsapp.entity.Company;
import com.example.jobsapp.entity.Job;
import com.example.jobsapp.mapper.JobMapper;
import com.example.jobsapp.repository.CompanyRepository;
import com.example.jobsapp.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    public JobDTO createJob(JobDTO jobDTO, Integer recruiterId) {

        Company company = companyRepository.findById(jobDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!company.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You can only post jobs for your own companies.");
        }
        jobDTO.setStatus("OPEN");
        Job job = JobMapper.toJob(jobDTO, company);
        Job savedJob = jobRepository.save(job);

        return JobMapper.toJobDTO(savedJob);
    }

    public List<JobDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return JobMapper.toJobDTOList(jobs);
    }


    public List<JobDTO> getJobsByCompany(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        List<Job> jobs = jobRepository.findByCompany(company);
        return JobMapper.toJobDTOList(jobs);
    }

    public List<Job> getJobsByRecruiter(Integer recruiterId) {
        List<Company> companies = companyService.getCompaniesOfRecruiter(recruiterId);

        return companies.stream()
                .flatMap(company -> jobRepository.findByCompany(company).stream())
                .toList();
    }

}
