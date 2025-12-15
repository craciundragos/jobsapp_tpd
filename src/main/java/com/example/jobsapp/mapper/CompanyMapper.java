    package com.example.jobsapp.mapper;

    import com.example.jobsapp.DTOs.CompanyDTO;
    import com.example.jobsapp.entity.Company;
    import com.example.jobsapp.entity.User;

    import java.util.List;
    import java.util.stream.Collectors;

    public class CompanyMapper {

        public static CompanyDTO toCompanyDTO(Company company) {
            if (company == null) return null;

            CompanyDTO dto = new CompanyDTO();
            dto.setId(company.getId());
            dto.setName(company.getName());
            if (company.getRecruiter() != null) {
                dto.setRecruiterId(company.getRecruiter().getId());
            }
            return dto;
        }

        public static List<CompanyDTO> toCompanyDTOList(List<Company> companies) {
            if (companies == null) return null;

            return companies.stream()
                    .map(CompanyMapper::toCompanyDTO)
                    .collect(Collectors.toList());
        }


        public static Company toCompany(CompanyDTO dto, User recruiter) {
            if (dto == null || recruiter == null) return null;

            Company company = new Company();
            company.setId(dto.getId()); // can be null if new
            company.setName(dto.getName());
            company.setRecruiter(recruiter);
            return company;
        }
    }
