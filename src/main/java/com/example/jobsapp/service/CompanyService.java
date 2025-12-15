package com.example.jobsapp.service;

import com.example.jobsapp.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.jobsapp.DTOs.CompanyDTO;
import com.example.jobsapp.entity.Company;
import com.example.jobsapp.entity.User;
import com.example.jobsapp.mapper.CompanyMapper;
import com.example.jobsapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    public CompanyDTO createCompany(CompanyDTO companyDTO, Integer recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        Company company = CompanyMapper.toCompany(companyDTO, recruiter);
        Company saved = companyRepository.save(company);

        return CompanyMapper.toCompanyDTO(saved);
    }

    public List<Company> getCompaniesOfRecruiter(Integer recruiterId) {
        userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return companyRepository.findByRecruiter_Id(recruiterId);
    }
}
