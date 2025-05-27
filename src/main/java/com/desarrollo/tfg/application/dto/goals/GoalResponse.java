package com.desarrollo.tfg.application.dto.goals;

import com.desarrollo.tfg.domain.entity.Goal;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponse {
  private Long id;
  private String title;
  private String description;
  private String status;
  private LocalDateTime createdAt;

  public static GoalResponse fromEntity(Goal g) {
    return GoalResponse.builder()
      .id(g.getId())
      .title(g.getTitle())
      .description(g.getDescription())
      .status(g.getStatus())
      .createdAt(g.getCreatedAt())
      .build();
  }
}
