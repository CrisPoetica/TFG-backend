package com.desarrollo.tfg.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  String username;
  String password;
  String email;
}
