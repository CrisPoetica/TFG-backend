package com.desarrollo.tfg.application.dto.weekplanner;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Data
public class CreatePlanRequest {
  @NotNull
  private LocalDate weekStart;
}
