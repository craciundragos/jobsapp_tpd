package com.example.jobsapp.controller;

import com.example.jobsapp.entity.Company;
import com.example.jobsapp.mapper.CompanyMapper;
import com.example.jobsapp.security.CustomUserDetails;
import com.example.jobsapp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.jobsapp.DTOs.CompanyDTO;
import java.util.List;
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@RestController
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Integer recruiterId = userDetails.getId();

        CompanyDTO createdCompany = companyService.createCompany(companyDTO, recruiterId);

        return ResponseEntity.ok(createdCompany);
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getCompaniesOfRecruiter() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer recruiterId = userDetails.getId();

        List<CompanyDTO> companyDTOs = companyService.getCompaniesOfRecruiter(recruiterId)
                .stream()
                .map(CompanyMapper::toCompanyDTO)
                .toList();

        return ResponseEntity.ok(companyDTOs);
    }
}