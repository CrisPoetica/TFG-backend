package com.desarrollo.tfg.application.dto.tasks;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
  private Long id;
  private String dayOfWeek;
  private String description;
  private String type;
  private boolean completed;
}
