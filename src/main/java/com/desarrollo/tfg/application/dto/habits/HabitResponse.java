package com.desarrollo.tfg.application.dto.habits;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HabitResponse {
  private Long id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
}
