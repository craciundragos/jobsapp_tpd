package com.example.jobsapp.mapper;
import com.example.jobsapp.DTOs.UserDTO;
import com.example.jobsapp.DTOs.UserRegistrationDTO;
import com.example.jobsapp.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toUser(UserRegistrationDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole() != null ? dto.getRole() : false); // default = CANDIDATE daca e null
        return user;
    }


    public static List<UserDTO> toUserDTOList(List<User> users) {
        if (users == null) return null;

        return users.stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

}