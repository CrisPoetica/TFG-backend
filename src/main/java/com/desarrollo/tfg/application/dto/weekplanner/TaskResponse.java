package com.desarrollo.tfg.application.dto.weekplanner;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
  private Long id;
  private String dayOfWeek;
  private String description;
  private String type;
  private Boolean completed;
}
