package com.desarrollo.tfg.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  Long   id;
  String username;
  String email;
  Boolean firstLogin;
}
