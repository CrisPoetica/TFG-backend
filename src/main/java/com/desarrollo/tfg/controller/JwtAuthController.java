package com.desarrollo.tfg.controller;

import com.desarrollo.tfg.application.dto.LoginRequest;
import com.desarrollo.tfg.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class JwtAuthController {
  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  @PostMapping("/login")
  public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest req) {
    // 1. Autenticar credenciales
    UsernamePasswordAuthenticationToken authToken =
      new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
    authManager.authenticate(authToken);

    // 2. Generar JWT
    UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
    String token = jwtUtil.generateToken(userDetails);

    // 3. Devolver en el body
    return ResponseEntity.ok(Map.of("token", token));
  }
}
