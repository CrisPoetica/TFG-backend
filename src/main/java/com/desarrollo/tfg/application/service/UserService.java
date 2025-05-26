package com.desarrollo.tfg.application.service;

import com.desarrollo.tfg.application.dto.RegisterRequest;
import com.desarrollo.tfg.application.dto.UserResponse;
import com.desarrollo.tfg.application.mapper.UserMapper;
import com.desarrollo.tfg.domain.entity.User;
import com.desarrollo.tfg.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserResponse register(RegisterRequest req) {
    if (userRepo.existsByUsername(req.getUsername())) {
      throw new RuntimeException("Usuario ya existe");
    }
    User u = userMapper.toEntity(req);
    u.setFirstLogin(true);
    u.setPassword(passwordEncoder.encode(req.getPassword()));
    User saved = userRepo.save(u);
    return userMapper.toDto(saved);
  }

  public UserResponse getCurrentUser() {
    String username =
      SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepo.findByUsername(username)
      .map(userMapper::toDto)
      .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
