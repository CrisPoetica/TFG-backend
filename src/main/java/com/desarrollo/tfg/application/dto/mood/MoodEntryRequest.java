package com.desarrollo.tfg.application.dto.mood;

import com.desarrollo.tfg.domain.entity.MoodType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodEntryRequest {
  private String date;      // "yyyy-MM-dd"
  private MoodType mood;
  private String notes;
}
