package com.example.jobsapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ai_evaluation")
public class    AiEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_evaluation_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "resume_text")
    private String resumeText;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Lob
    @Column(name = "weaknesses")
    private String weaknesses;

    @Lob
    @Column(name = "strengths")
    private String strengths;

    @Lob
    @Column(name = "recommandation")
    private String recommandation;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "model", length = 100)
    private String model;

}