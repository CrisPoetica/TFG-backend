package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.RegisterRequest;
import com.desarrollo.tfg.application.dto.UserResponse;
import com.desarrollo.tfg.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserService userService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse register(@RequestBody RegisterRequest req) {
    return userService.register(req);
  }

  // El endpoint /login Spring lo provee automáticamente con formLogin,
  // si necesitas JSON-login tendrías que implementar un filter personalizado.

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout() {
    // Está configurado en SecurityConfig, no requiere método body.
  }
}
