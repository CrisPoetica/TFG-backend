package com.desarrollo.tfg.application.dto.tasks;

import lombok.Data;

@Data
public class TaskRequest {
  private String dayOfWeek;    // "Lunes", "Martes", ...
  private String description;
  private String type;         // "HÁBITO", "META", etc.
  private Boolean completed;
}
