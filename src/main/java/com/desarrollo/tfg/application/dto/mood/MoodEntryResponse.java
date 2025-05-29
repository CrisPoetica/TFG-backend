package com.desarrollo.tfg.application.dto.mood;

import com.desarrollo.tfg.domain.entity.MoodType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodEntryResponse {
  private Long id;
  private Long userId;
  private String date;      // "yyyy-MM-dd"
  private MoodType mood;
  private String notes;
  private String createdAt; // ISO datetime
  private String updatedAt;
}
