package com.desarrollo.tfg.application.dto.tasks;

import lombok.Data;

@Data
public class TaskRequest {
  private String dayOfWeek;
  private String description;
  private String type;
  private Boolean completed;
}
