package com.desarrollo.tfg.application.dto.habits;

import lombok.Data;

@Data
public class CreateHabitRequest {
  private String name;
  private String description;
}

