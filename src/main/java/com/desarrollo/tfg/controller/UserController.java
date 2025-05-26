package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.UserResponse;
import com.desarrollo.tfg.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public UserResponse me() {
    return userService.getCurrentUser();
  }
}
