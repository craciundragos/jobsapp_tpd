package com.example.jobsapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "job_requirements")
public class JobRequirements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(columnDefinition = "JSON")
    private String rawFormJson;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String generatedText;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.time.LocalDateTime createdAt;
}
