package com.desarrollo.tfg.application.dto.weekplanner;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PlanResponse {
  private Long id;
  private LocalDate weekStart;
  private LocalDateTime generatedAt;
  private List<TaskResponse> tasks;
}
