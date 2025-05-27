package com.desarrollo.tfg.application.dto.habits;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HabitLogResponse {
  private Long id;
  private LocalDate logDate;
  private Boolean done;
}
