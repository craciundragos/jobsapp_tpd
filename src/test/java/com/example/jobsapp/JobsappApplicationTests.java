package com.example.jobsapp;

import com.example.jobsapp.DTOs.UserLoginDTO;
import com.example.jobsapp.security.JwtService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.jobsapp.DTOs.UserRegistrationDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class JobsappApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void shouldFail_whenEmailIsBlank() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("");
        dto.setPassword("password123");

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFail_whenEmailIsInvalid() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("invalid");
        dto.setPassword("password123");

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFail_whenPasswordIsBlank() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("");

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFail_whenPasswordTooShort() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("123");

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPass_whenAllFieldsValid() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("password123");

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFail_whenUsernameIsBlank() {
    UserRegistrationDTO dto = new UserRegistrationDTO();
    dto.setUsername("");
    dto.setEmail("test@test.com");
    dto.setPassword("password123");
    dto.setRole(true);

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertFalse(violations.isEmpty());
}

    @Test
    void shouldFail_whenUsernameTooShort() {
    UserRegistrationDTO dto = new UserRegistrationDTO();
    dto.setUsername("ab");
    dto.setEmail("test@test.com");
    dto.setPassword("password123");
    dto.setRole(true);

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertFalse(violations.isEmpty());
}

    @Test
    void shouldFail_whenEmailIsInvalidReg() {
    UserRegistrationDTO dto = new UserRegistrationDTO();
    dto.setUsername("testuser");
    dto.setEmail("invalid-email");
    dto.setPassword("password123");
    dto.setRole(true);

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertFalse(violations.isEmpty());
}

    @Test
    void shouldPass_whenAllFieldsValidReg() {
    UserRegistrationDTO dto = new UserRegistrationDTO();
    dto.setUsername("testuser");
    dto.setEmail("test@test.com");
    dto.setPassword("password123");
    dto.setRole(true);

    Set<ConstraintViolation<UserRegistrationDTO>> violations = validator.validate(dto);

    assertTrue(violations.isEmpty());
}

    @Test
    void createJob_shouldFail_whenUserIsCandidate() throws Exception {
        // token pentru CANDIDATE
        String token = jwtService.generateToken(
                "test@test.com",
                1,
                "CANDIDATE"
        );

        String jobJson = """
            {
              "title": "Backend Developer",
              "description": "Spring Boot job",
              "companyId": 1
            }
        """;

        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson))
                .andExpect(status().isForbidden());
    }
}


