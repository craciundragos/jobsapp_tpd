package com.example.jobsapp.DTOs;
import lombok.Data;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private Boolean role;
}
