package com.example.jobsapp.DTOs;
import lombok.Data;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
@Data
public class    UserLoginDTO {
    @Email(message = "Invalid email address.")
    @NotBlank(message = "Email can't be blank")
    private String email;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 4, max = 25, message = "Password must be between 4 and 25 characters")
    private String password;
}
