package com.desarrollo.tfg.application.dto.weekplanner;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedTask {
  private String day;
  private String description;
  private String type;
}
