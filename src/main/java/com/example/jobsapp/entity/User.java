package com.example.jobsapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 100,unique = true)
    private String username;

    @Column(name = "email", nullable = false, length = 150,unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ColumnDefault("0")
    @Column(name = "role", nullable = false)
    private Boolean role = false;

}