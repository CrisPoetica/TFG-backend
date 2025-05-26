package com.desarrollo.tfg.application.mapper;

import com.desarrollo.tfg.application.dto.RegisterRequest;
import com.desarrollo.tfg.application.dto.UserResponse;
import com.desarrollo.tfg.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User toEntity(RegisterRequest dto) {
    return User.builder()
      .username(dto.getUsername())
      .password(dto.getPassword())   // Ojo: password aún sin hashear
      .email(dto.getEmail())
      .roles("ROLE_USER") // Enum de roles
      .build();
  }

  public UserResponse toDto(User user) {
    return new UserResponse(user.getId(), user.getUsername(),
      user.getEmail(), user.getFirstLogin());
  }
}
