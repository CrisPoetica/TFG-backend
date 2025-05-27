package com.desarrollo.tfg.application.dto.habits;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LogHabitRequest {
  private LocalDate logDate;
  private Boolean done;
}
