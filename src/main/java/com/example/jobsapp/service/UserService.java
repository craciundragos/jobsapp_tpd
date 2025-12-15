package com.example.jobsapp.service;

import com.example.jobsapp.DTOs.CompanyDTO;
import com.example.jobsapp.DTOs.UserDTO;
import com.example.jobsapp.DTOs.UserLoginDTO;
import com.example.jobsapp.DTOs.UserRegistrationDTO;
import com.example.jobsapp.entity.User;
import com.example.jobsapp.mapper.UserMapper;
import com.example.jobsapp.repository.UserRepository;
import com.example.jobsapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserDTO register(UserRegistrationDTO registrationDTO) {
        userRepository.findByEmail(registrationDTO.getEmail())
                .ifPresent(u -> { throw new RuntimeException("Email already in use"); });
        userRepository.findByUsername(registrationDTO.getUsername())
                .ifPresent(u -> { throw new RuntimeException("Username already in use"); });

        User user = UserMapper.toUser(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        User savedUser = userRepository.save(user);

        return UserMapper.toUserDTO(savedUser);
    }

    public LoginResponse login(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String role = user.getRole() ? "RECRUITER" : "CANDIDATE";
        String token = jwtService.generateToken(user.getEmail(), user.getId(), role);

        return new LoginResponse(UserMapper.toUserDTO(user), token);
    }



    public record LoginResponse(UserDTO user, String token) {}
}